package eu.jangos.auth.network.handler;

/*
 * Copyright 2016 Warkdev.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import eu.jangos.auth.authenum.AuthStep;
import eu.jangos.auth.controller.AccountService;
import eu.jangos.auth.controller.ParameterService;
import eu.jangos.auth.controller.RealmService;
import eu.jangos.auth.exception.AuthStepException;
import eu.jangos.auth.model.Account;
import eu.jangos.auth.network.opcode.AuthClientCmd;
import eu.jangos.auth.network.opcode.AuthServerCmd;
import eu.jangos.auth.network.packet.AbstractAuthClientPacket;
import eu.jangos.auth.network.packet.AbstractAuthServerPacket;
import eu.jangos.auth.network.packet.client.CAuthLogonChallengePacket;
import eu.jangos.auth.network.packet.client.CAuthLogonProofPacket;
import eu.jangos.auth.network.packet.client.CAuthReconnectProofPacket;
import eu.jangos.auth.network.packet.server.SAuthLogonChallengePacket;
import eu.jangos.auth.network.packet.server.SAuthLogonFailedPacket;
import eu.jangos.auth.network.packet.server.SAuthLogonProofPacket;
import eu.jangos.auth.network.packet.server.SAuthRealmList;
import eu.jangos.auth.network.packet.server.SAuthReconnectChallengePacket;
import eu.jangos.auth.network.packet.server.SAuthReconnectProofPacket;
import eu.jangos.auth.network.srp.SRPServer;
import eu.jangos.auth.utils.AuthUtils;
import eu.jangos.auth.utils.BigNumber;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AuthServerHandler is an extension of ChannelInboundHandlerAdapter. It handles all the business logic
 * related to the network level such as SRP6 protocol implementation, WoW protocol implementation or orchestration of service layers.
 * It does not really apply business logic related to the entities, these tasks are delegated to controllers.
 * 
 * This handler is not sharable, it is instanciated for each TCP connection.
 * 
 * @author Warkdev
 * @version v0.1 BETA.
 */
public class AuthServerHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(AuthServerHandler.class);
    
    private AuthStep step;
    private CAuthLogonChallengePacket cChallenge;
    private SAuthLogonChallengePacket sChallenge;
    private CAuthLogonProofPacket cProof;
    private SAuthLogonProofPacket sProof;    
    private SAuthRealmList sRealm;
    private SAuthReconnectChallengePacket sRChallenge;
    private CAuthReconnectProofPacket cRProof;
    private SAuthReconnectProofPacket sRProof;
    
    private Account account;
    private final AccountService accountService;
    private static final RealmService realmService = new RealmService();
    private static final ParameterService parameterService = new ParameterService();
    
    // Helper for SRP implementation.
    private SRPServer srp;
    
    /**
     * Constructor of AuthServerHandler.
     */
    public AuthServerHandler() {
        super();        
        this.step = AuthStep.STEP_INIT;
        this.accountService = new AccountService();
        this.account = null;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws NoSuchAlgorithmException, UnsupportedEncodingException, AuthStepException {
        AbstractAuthClientPacket request = (AbstractAuthClientPacket) msg;

        logger.debug("Context: "+ctx.name()+", Message received: "+request);

        // By default, logon is failed.
        AbstractAuthServerPacket response = new SAuthLogonFailedPacket(AuthClientCmd.CMD_AUTH_LOGON_CHALLENGE);
        ((SAuthLogonFailedPacket) response).setResult(AuthServerCmd.AUTH_FAIL_BANNED);

        switch (request.getOpcode()) {
            case CMD_AUTH_LOGON_CHALLENGE:
                if(step != AuthStep.STEP_INIT)
                    throw new AuthStepException("Step state is invalid.");
                
                this.cChallenge = (CAuthLogonChallengePacket) request;                                
                
                boolean update = false;                                
                
                // Checking integrity of the packet.
                if (this.cChallenge.getAccountName() == null || this.cChallenge.getAccountName().isEmpty()) {
                    logger.debug("Context: "+ctx.name()+", account: "+this.cChallenge.getAccountName()+" : Packet integrity cannot be verified.");
                    ((SAuthLogonFailedPacket) response).setResult(AuthServerCmd.AUTH_FAIL_UNKNOWN_ACCOUNT);
                    break;
                }
                
                if (this.cChallenge.getAccountName().length() != this.cChallenge.getAccountLength()) {
                    logger.debug("Context: "+ctx.name()+", account: "+this.cChallenge.getAccountName()+" : Packet integrity cannot be verified.");
                    ((SAuthLogonFailedPacket) response).setResult(AuthServerCmd.AUTH_FAIL_UNKNOWN_ACCOUNT);
                    break;
                }                                
                
                // Checking build number.
                if (this.cChallenge.getBuild() < Integer.parseInt(parameterService.getParameter("minSupportedBuild")) || this.cChallenge.getBuild() > Integer.parseInt(parameterService.getParameter("maxSupportedBuild"))){
                    logger.debug("Context: "+ctx.name()+", account: "+this.cChallenge.getAccountName()+" : Build is not supported.");
                    ((SAuthLogonFailedPacket) response).setResult(AuthServerCmd.AUTH_FAIL_VERSION_INVALID);
                    break;
                }
                    
                // Checking account existence.
                if(! this.accountService.checkExistence(this.cChallenge.getAccountName().toUpperCase())){
                    logger.debug("Context: "+ctx.name()+", account: "+this.cChallenge.getAccountName()+" : Account does not exist.");
                    ((SAuthLogonFailedPacket) response).setResult(AuthServerCmd.AUTH_FAIL_UNKNOWN_ACCOUNT);
                    break;
                }
                
                this.account = this.accountService.getAccount(this.cChallenge.getAccountName());
                
                // Checking if account is online.
                if(this.account.isOnline()) {
                    logger.debug("Context: "+ctx.name()+", account: "+this.cChallenge.getAccountName()+" : Account is online.");
                    ((SAuthLogonFailedPacket) response).setResult(AuthServerCmd.AUTH_FAIL_ALREADY_ONLINE);
                    break;
                }
                
                // Checking if account is locked.
                if(this.account.isLocked()) {
                    logger.debug("Context: "+ctx.name()+", account: "+this.cChallenge.getAccountName()+" : Account is locked.");
                    ((SAuthLogonFailedPacket) response).setResult(AuthServerCmd.AUTH_FAIL_SUSPENDED);
                    break;
                }
                
                // Checking if account is banned -- Includes IP & Account.
                if(this.accountService.isBanned(this.account, this.cChallenge.getIp().getHostAddress())) {
                    logger.debug("Context: "+ctx.name()+", account: "+this.cChallenge.getAccountName()+" : Account is banned.");
                    ((SAuthLogonFailedPacket) response).setResult(AuthServerCmd.AUTH_FAIL_BANNED);
                    break;
                }                                
                
                this.srp = new SRPServer(this.account.getHashPass(), this.account.getName());
                               
                // Generating the salt.
                if(this.account.getSalt() == null){
                    logger.debug("Context: "+ctx.name()+", account: "+this.cChallenge.getAccountName()+" : Generating salt.");
                    this.account.setSalt(this.srp.generateS().toHexString());
                    update = true;
                } else {
                    this.srp.setSalt(new BigNumber(this.account.getSalt(),16));
                }
                
                // Generating the verifier.
                if(this.account.getVerifier() == null){
                    logger.debug("Context: "+ctx.name()+", account: "+this.cChallenge.getAccountName()+" : Generating verifier.");
                    this.account.setVerifier(this.srp.generateV().toHexString());
                    update = true;
                } else {
                    this.srp.setV(new BigNumber(this.account.getVerifier(),16));
                }
                
                // Updating the database with the new values for the verifier and the salt.
                if(update)
                {
                    logger.debug("Context: "+ctx.name()+", account: "+this.cChallenge.getAccountName()+" : Updating the account...");
                    this.accountService.update(this.account);
                }
                
                // Calculating the first step for the SRP session.
                this.srp.step1();                

                // Creating the Logon Challenge Packet.
                this.sChallenge = AuthUtils.generateSAuthLogonChallengePacket(this.srp);

                response = this.sChallenge;                                
                
                // Server is then expecting the proof, avoid by-passing authentication.
                this.step = AuthStep.STEP_CHALLENGE;
                break;
            case CMD_AUTH_LOGON_PROOF:
                if(step != AuthStep.STEP_CHALLENGE)
                    throw new AuthStepException("Step state is invalid.");
                
                this.cProof = (CAuthLogonProofPacket) request;

                if(this.srp.step2(this.cProof.getA(), this.cProof.getM())){                    
                    // Server accepts the proof, sending back our proof.
                    this.sProof = AuthUtils.generateSAuthLogonProofPacket(this.srp.generateHashLogonProof(this.cProof.getA()));
                    
                    // Setting account parameters in DB.
                    this.accountService.login(this.account, this.cChallenge.getIp().getHostAddress(), this.cChallenge.getCountry(), this.srp.getSessionKey().toHexString());                    
                    
                    // Server is the expecting the realm packet.
                    this.step = AuthStep.STEP_PROOF;
                    response = this.sProof;
                } else {
                    logger.debug("Context: "+ctx.name()+", account: "+this.cChallenge.getAccountName()+" : Incorrect password.");
                    
                    // Server refuses the proof, password is incorrect.
                    ((SAuthLogonFailedPacket) response).setResult(AuthServerCmd.AUTH_FAIL_UNKNOWN_ACCOUNT);
                    
                    // Updating lock counter.                    
                    this.accountService.updateFailedAttempt(this.account);
                    
                    // Return to init state, don't let the client proposing other proofs.
                    this.step = AuthStep.STEP_INIT;
                }                                             

                break;
            case CMD_AUTH_RECONNECT_CHALLENGE:                
                if(step != AuthStep.STEP_INIT)
                    throw new AuthStepException("Step state is invalid.");
                
                this.cChallenge = (CAuthLogonChallengePacket) request;
                
                this.account = this.accountService.getAccount(this.cChallenge.getAccountName());
                
                // If not found, we must close the session.
                if(this.account == null)
                {
                    logger.debug("Context: "+ctx.name()+", account: "+this.cChallenge.getAccountName()+" : Account does not exist.");
                    ctx.close();
                    break;
                }
                
                // Account name is incorrect, we close the session.
                if(!this.account.getName().equals(this.cChallenge.getAccountName()))
                {
                    logger.debug("Context: "+ctx.name()+", account: "+this.cChallenge.getAccountName()+" : Incorrect account.");
                    ctx.close();
                    break;
                }
                
                // Account does not have any session key.
                if(this.account.getSessionkey() == null || this.account.getSessionkey().isEmpty())
                {
                    logger.debug("Context: "+ctx.name()+", account: "+this.cChallenge.getAccountName()+" : No session key.");
                    ctx.close();
                    break;
                }
                
                this.srp = new SRPServer(this.account.getHashPass(), this.account.getName());                                
                
                this.sRChallenge = new SAuthReconnectChallengePacket(AuthClientCmd.CMD_AUTH_RECONNECT_CHALLENGE);    
                this.sRChallenge.setResult(AuthServerCmd.AUTH_SUCCESS);
                this.sRChallenge.setChallenge(new BigNumber().setRand(16));
                
                response = this.sRChallenge;
                
                this.step = AuthStep.STEP_PROOF;
                
                break;
            case CMD_AUTH_RECONNECT_PROOF:
                if(step != AuthStep.STEP_PROOF)
                    throw new AuthStepException("Step state is invalid.");
                
                this.cRProof = (CAuthReconnectProofPacket) request;                                
                
                byte[] hashReconnect = this.srp.generateHashReconnectProof(this.cRProof.getR1(), this.sRChallenge.getChallenge(), new BigNumber(account.getSessionkey(), 16));
                BigNumber hash = new BigNumber();
                hash.setBinary(hashReconnect, false);
                
                // We check if the server has the same hash than the client.
                if(hash.equals(this.cRProof.getR2()))
                {                    
                    this.sRProof = AuthUtils.generateSAuthReconnectProofPacket(hashReconnect);
                
                    response = sRProof;
                    
                    this.step = AuthStep.STEP_REALM;
                } else {
                    logger.debug("Context: "+ctx.name()+", account: "+this.cChallenge.getAccountName()+" : Incorrect proof sent by the client.");
                }                                                                
                
                break;            
            case CMD_REALM_LIST:                
                if(step != AuthStep.STEP_PROOF && step != AuthStep.STEP_REALM)
                    throw new AuthStepException("Step state is invalid.");
                
                // Creating Realm packet.
                this.sRealm = new SAuthRealmList(AuthClientCmd.CMD_REALM_LIST);
                
                // Setting realms.
                this.sRealm.addRealms(realmService.getAllRealms());
                
                // Set accountID.
                this.sRealm.setAccount(this.account);
                
                response = this.sRealm;
                this.step = AuthStep.STEP_REALM;
                
                break;                
            default:
                logger.error("Context: "+ctx.name()+", account: "+this.cChallenge.getAccountName()+" : Opcode is not supported");
        }

        logger.debug("Context: "+ctx.name()+", account: "+this.cChallenge.getAccountName()+", response: "+response);

        ctx.writeAndFlush(response);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }
}
