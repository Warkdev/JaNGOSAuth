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
package eu.jangos.auth.network.handler;

import eu.jangos.auth.controller.AccountService;
import eu.jangos.auth.controller.ParameterService;
import eu.jangos.auth.model.Account;
import eu.jangos.auth.network.opcode.AuthClientCmd;
import eu.jangos.auth.network.opcode.AuthServerCmd;
import eu.jangos.auth.network.packet.AbstractAuthServerPacket;
import eu.jangos.auth.network.packet.client.CAuthLogonChallengePacket;
import eu.jangos.auth.network.packet.client.CAuthLogonProofPacket;
import eu.jangos.auth.network.packet.client.CAuthRealmList;
import eu.jangos.auth.network.packet.server.SAuthLogonChallengePacket;
import eu.jangos.auth.network.packet.server.SAuthLogonFailedPacket;
import eu.jangos.auth.network.packet.server.SAuthLogonProofPacket;
import eu.jangos.auth.network.packet.server.SAuthRealmList;
import eu.jangos.auth.network.srp.SRPClient;
import eu.jangos.auth.utils.BigNumber;
import io.netty.channel.embedded.EmbeddedChannel;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.Before;

/**
 *
 * @author Warkdev
 */
public class AuthServerHandlerTest {

    EmbeddedChannel ch;
    ParameterService ps;
    int maxFailure;
    CAuthLogonChallengePacket logon;

    public AuthServerHandlerTest() {
    }

    @Before
    public void setup() throws UnknownHostException {
        ch = new EmbeddedChannel(new AuthServerHandler());
        ps = new ParameterService();
        maxFailure = Integer.parseInt(ps.getParameter("maxFailedAttempt"));

        // Creating a default logon packet.
        logon = new CAuthLogonChallengePacket(AuthClientCmd.CMD_AUTH_LOGON_CHALLENGE);
        logon.setAccountName("test");
        logon.setAccountLength(4);
        logon.setBuild(5875);
        logon.setCountry("frFR");
        logon.setError(3);
        logon.setGame("WoW");
        logon.setVersion("1.12.1");
        logon.setPlatform("x86");
        logon.setOs("x86");
        logon.setTimezone(60);
        logon.setIp(InetAddress.getLocalHost());
    }

    @After
    public void tearDown() {
        AccountService as = new AccountService();
        
        Account account = as.getAccount("TEST");
        account.setOnline(false);
        as.update(account);
        
        ch.close();
    }

    @AfterClass
    public static void end() {
        AccountService as = new AccountService();
        Account account = as.getAccount("FAILED");
        account.setLocked(false);
        account.setFailedattempt(0);

        as.update(account);
    }

    /**
     * This method is testing an usual login flow with valid credentials.
     */
    @Test
    public void testLoginFlow() {
        System.out.println("testLoginFlow");

        // First, send a challenge packet from the client.
        ch.writeInbound(logon);

        AbstractAuthServerPacket response = (AbstractAuthServerPacket) ch.readOutbound();

        assertTrue(response instanceof SAuthLogonChallengePacket);

        // Then, we should get the challenge packet from the server.
        SAuthLogonChallengePacket sChallenge = (SAuthLogonChallengePacket) response;
        assertTrue(sChallenge.getResult() == AuthServerCmd.AUTH_SUCCESS);

        SRPClient srp = new SRPClient();
        byte[] g = {sChallenge.getG()};

        BigNumber M = srp.step1(logon.getAccountName(), "test", sChallenge.getB(), new BigNumber(g), sChallenge.getN(), sChallenge.getSalt());

        CAuthLogonProofPacket cproof = new CAuthLogonProofPacket(AuthClientCmd.CMD_AUTH_LOGON_PROOF);
        cproof.setA(srp.getA());
        cproof.setCrc(M);
        cproof.setM(M);
        cproof.setKeyNumber((byte) 0);
        cproof.setSecurityFlag((byte) 0);

        // Third, we send the proof to the server.
        ch.writeInbound(cproof);

        // Server is then sending back its proof.
        response = (AbstractAuthServerPacket) ch.readOutbound();

        // We check the packet type.
        assertTrue(response instanceof SAuthLogonProofPacket);

        SAuthLogonProofPacket sProof = (SAuthLogonProofPacket) response;

        // We check the code.
        assertTrue(sProof.getResult() == AuthServerCmd.AUTH_SUCCESS);
        // We check the server proof.     
        BigNumber bnProof = new BigNumber();
        bnProof.setBinary(srp.generateHashLogonProof());
        assertTrue(bnProof.equals(sProof.getProof()));

        // We request the realm list now.
        CAuthRealmList cRList = new CAuthRealmList(AuthClientCmd.CMD_REALM_LIST);

        ch.writeInbound(cRList);

        // Server should be now sending us realm list.
        response = (AbstractAuthServerPacket) ch.readOutbound();

        // We check the packet type.
        assertTrue(response instanceof SAuthRealmList);

        // Now, we are logged in.
    }

    @Test
    /**
     * This method is testing a login challenge entering a wrong password.
     */
    public void testLoginWrongPassword() {
        System.out.println("testLoginWrongPassword");

        // First, send a challenge packet from the client.
        ch.writeInbound(logon);

        AbstractAuthServerPacket response = (AbstractAuthServerPacket) ch.readOutbound();

        assertTrue(response instanceof SAuthLogonChallengePacket);

        // Then, we should get the challenge packet from the server.
        SAuthLogonChallengePacket sChallenge = (SAuthLogonChallengePacket) response;
        assertTrue(sChallenge.getResult() == AuthServerCmd.AUTH_SUCCESS);

        SRPClient srp = new SRPClient();
        byte[] g = {sChallenge.getG()};

        // We know that testa is a wrong password for this.
        BigNumber M = srp.step1(logon.getAccountName(), "testa", sChallenge.getB(), new BigNumber(g), sChallenge.getN(), sChallenge.getSalt());

        CAuthLogonProofPacket cproof = new CAuthLogonProofPacket(AuthClientCmd.CMD_AUTH_LOGON_PROOF);
        cproof.setA(srp.getA());
        cproof.setCrc(M);
        cproof.setM(M);
        cproof.setKeyNumber((byte) 0);
        cproof.setSecurityFlag((byte) 0);

        // Third, we send the proof to the server.
        ch.writeInbound(cproof);

        // Server is then sending back its proof.
        response = (AbstractAuthServerPacket) ch.readOutbound();

        // We check the packet type.
        assertTrue(response instanceof SAuthLogonFailedPacket);

        SAuthLogonFailedPacket sLogon = (SAuthLogonFailedPacket) response;

        // We check the code.
        assertTrue(sLogon.getResult() == AuthServerCmd.AUTH_FAIL_UNKNOWN_ACCOUNT);

        // Connection must be closed by server.
    }

    @Test
    /**
     * This method is testing the login flow using a non-existing account.
     */
    public void testLoginNonExistingAccount() {
        System.out.println("testLoginNonExistingAccount");

        logon.setAccountName("UnknownAccount");

        // First, send a challenge packet from the client.
        ch.writeInbound(logon);

        AbstractAuthServerPacket response = (AbstractAuthServerPacket) ch.readOutbound();

        assertTrue(response instanceof SAuthLogonFailedPacket);

        SAuthLogonFailedPacket sLogon = (SAuthLogonFailedPacket) response;

        // We check the reason.        
        assertTrue(sLogon.getResult() == AuthServerCmd.AUTH_FAIL_UNKNOWN_ACCOUNT);

        // Connection must be closed by server.
    }

    @Test
    /**
     * This method is testing the login flow using a null account.
     */
    public void testLoginAccountNull() {
        System.out.println("testLoginAccountNull");

        logon.setAccountName(null);

        // First, send a challenge packet from the client.
        ch.writeInbound(logon);

        AbstractAuthServerPacket response = (AbstractAuthServerPacket) ch.readOutbound();

        assertTrue(response instanceof SAuthLogonFailedPacket);

        SAuthLogonFailedPacket sLogon = (SAuthLogonFailedPacket) response;

        // We check the reason.        
        assertTrue(sLogon.getResult() == AuthServerCmd.AUTH_FAIL_UNKNOWN_ACCOUNT);

        // Connection must be closed by server.
    }

    @Test
    /**
     * This method is testing the login flow using an unsupported test version
     * for our data.
     */
    public void testLoginUnsupportedBuildVersion() {
        System.out.println("testLoginUnsupportedBuildVersion");

        logon.setBuild(5876);

        // First, send a challenge packet from the client.
        ch.writeInbound(logon);

        AbstractAuthServerPacket response = (AbstractAuthServerPacket) ch.readOutbound();

        assertTrue(response instanceof SAuthLogonFailedPacket);

        SAuthLogonFailedPacket sLogon = (SAuthLogonFailedPacket) response;

        // We check the reason.        
        assertTrue(sLogon.getResult() == AuthServerCmd.AUTH_FAIL_VERSION_INVALID);

        // Connection must be closed by server.
    }

    @Test
    /**
     * This method is testing the login flow using a banned account.
     */
    public void testLoginBannedAccount() {
        System.out.println("testLoginBannedAccount");

        logon.setAccountName("banned");
        logon.setAccountLength(logon.getAccountName().length());

        // First, send a challenge packet from the client.
        ch.writeInbound(logon);

        AbstractAuthServerPacket response = (AbstractAuthServerPacket) ch.readOutbound();

        assertTrue(response instanceof SAuthLogonFailedPacket);

        SAuthLogonFailedPacket sLogon = (SAuthLogonFailedPacket) response;

        // We check the reason.        
        assertTrue(sLogon.getResult() == AuthServerCmd.AUTH_FAIL_BANNED);

        // Connection must be closed by server.
    }

    @Test
    /**
     * This method is testing the login flow using a locked account.
     */
    public void testLoginLockedAccount() {
        System.out.println("testLoginLockedAccount");

        logon.setAccountName("lock");
        logon.setAccountLength(logon.getAccountName().length());

        // First, send a challenge packet from the client.
        ch.writeInbound(logon);

        AbstractAuthServerPacket response = (AbstractAuthServerPacket) ch.readOutbound();

        assertTrue(response instanceof SAuthLogonFailedPacket);

        SAuthLogonFailedPacket sLogon = (SAuthLogonFailedPacket) response;

        // We check the reason.        
        assertTrue(sLogon.getResult() == AuthServerCmd.AUTH_FAIL_SUSPENDED);

        // Connection must be closed by server.
    }

    @Test
    /**
     * This method is testing the login flow using an account that is already
     * online.
     */
    public void testAccountAlreadyLoggedIn() {
        System.out.println("testAccountAlreadyLoggedIn");

        logon.setAccountName("online");
        logon.setAccountLength(logon.getAccountName().length());

        // First, send a challenge packet from the client.
        ch.writeInbound(logon);

        AbstractAuthServerPacket response = (AbstractAuthServerPacket) ch.readOutbound();

        assertTrue(response instanceof SAuthLogonFailedPacket);

        SAuthLogonFailedPacket sLogon = (SAuthLogonFailedPacket) response;

        // We check the reason.        
        assertTrue(sLogon.getResult() == AuthServerCmd.AUTH_FAIL_ALREADY_ONLINE);
        // Connection must be closed by server.
    }

    @Test
    /**
     * This method is testing the locking mechanism while using too many times a
     * wrong password.
     */
    public void testAccountMaxFailures() {
        System.out.println("testAccountMaxFailures");

        logon.setAccountName("failed");
        logon.setAccountLength(logon.getAccountName().length());

        for (int i = 0; i <= maxFailure; i++) {
            // First, send a challenge packet from the client.
            ch.writeInbound(logon);

            AbstractAuthServerPacket response = (AbstractAuthServerPacket) ch.readOutbound();

            if (i < maxFailure) {
                assertTrue(response instanceof SAuthLogonChallengePacket);
                // Then, we should get the challenge packet from the server.
                SAuthLogonChallengePacket sChallenge = (SAuthLogonChallengePacket) response;
                assertTrue(sChallenge.getResult() == AuthServerCmd.AUTH_SUCCESS);

                SRPClient srp = new SRPClient();
                byte[] g = {sChallenge.getG()};

                // We know that testa is a wrong password for this.
                BigNumber M = srp.step1(logon.getAccountName(), "testa", sChallenge.getB(), new BigNumber(g), sChallenge.getN(), sChallenge.getSalt());

                CAuthLogonProofPacket cproof = new CAuthLogonProofPacket(AuthClientCmd.CMD_AUTH_LOGON_PROOF);
                cproof.setA(srp.getA());
                cproof.setCrc(M);
                cproof.setM(M);
                cproof.setKeyNumber((byte) 0);
                cproof.setSecurityFlag((byte) 0);

                // Third, we send the proof to the server.
                ch.writeInbound(cproof);

                // Server is then sending back its proof.
                response = (AbstractAuthServerPacket) ch.readOutbound();

                // We check the packet type.
                assertTrue(response instanceof SAuthLogonFailedPacket);

                SAuthLogonFailedPacket sLogon = (SAuthLogonFailedPacket) response;

                // We check the code.
                assertTrue(sLogon.getResult() == AuthServerCmd.AUTH_FAIL_UNKNOWN_ACCOUNT);
                // Connection must be closed by server.                
                
            } else {
                assertTrue(response instanceof SAuthLogonFailedPacket);

                SAuthLogonFailedPacket sLogon = (SAuthLogonFailedPacket) response;

                // We check the reason.        
                assertTrue(sLogon.getResult() == AuthServerCmd.AUTH_FAIL_SUSPENDED);

                // Connection must now be closed by the server.                
            }
        }
    }

    @Test
    /**
     * This method is testing to send authentication packets in a wrong
     * sequence.
     */
    public void testWrontPacketSequenceNoChallenge() {
        System.out.println("testWrontPacketSequenceNoChallenge");

        // First, send a challenge packet from the client.
        ch.writeInbound(logon);

        AbstractAuthServerPacket response = (AbstractAuthServerPacket) ch.readOutbound();       

        // We request the realm list now.
        CAuthRealmList cRList = new CAuthRealmList(AuthClientCmd.CMD_REALM_LIST);

        ch.writeInbound(cRList);

        // Server should not reply and close the session.
        response = (AbstractAuthServerPacket) ch.readOutbound();

        assertNull(response);
        assertFalse(ch.isOpen());
    }

    @Test
    /**
     * This method is testing to send authentication packets in a wrong
     * sequence.
     */
    public void testWrontPacketSequenceNotAuthed() {
        System.out.println("testWrontPacketSequenceNotAuthed");

        // We request the realm list now.
        CAuthRealmList cRList = new CAuthRealmList(AuthClientCmd.CMD_REALM_LIST);

        ch.writeInbound(cRList);

        // Server should not reply and close the session.
        AbstractAuthServerPacket response = (AbstractAuthServerPacket) ch.readOutbound();

        assertNull(response);
        assertFalse(ch.isOpen());
    }

    @Test
    /**
     * This method is testing to send authentication packets in a wrong
     * sequence.
     */
    public void testWrontPacketSequenceNoHello() {
        System.out.println("testWrontPacketSequenceNoChallenge");

        BigNumber B = new BigNumber("912CDD3E0D11335DB9162D6FF9EC4F1D6B59875C6DC70FCF275AF3568D09E52");
        BigNumber N = new BigNumber("894B645E89E1535BBDAD5B8B290650530801B18EBFBF5E8FAB3C82872A3E9BB7");
        BigNumber g = new BigNumber("7");
        BigNumber salt = new BigNumber("85527F8E3F3C8DBCCCAB6543D139B53B374E07B959F5D20685DAA284D272A21A");

        SRPClient srp = new SRPClient();

        BigNumber M = srp.step1(logon.getAccountName(), "test", B, g, N, salt);

        CAuthLogonProofPacket cproof = new CAuthLogonProofPacket(AuthClientCmd.CMD_AUTH_LOGON_PROOF);
        cproof.setA(srp.getA());
        cproof.setCrc(M);
        cproof.setM(M);
        cproof.setKeyNumber((byte) 0);
        cproof.setSecurityFlag((byte) 0);

        // Third, we send the proof to the server.
        ch.writeInbound(cproof);

        // Server is then sending back its proof.
        AbstractAuthServerPacket response = (AbstractAuthServerPacket) ch.readOutbound();        

        assertNull(response);
        
        assertFalse(ch.isOpen());        
    }
}
