package eu.jangos.auth.network.packet.client;

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

import eu.jangos.auth.network.opcode.AuthClientCmd;
import eu.jangos.auth.network.packet.AbstractAuthClientPacket;
import io.netty.buffer.ByteBuf;
import java.net.InetAddress;
import java.nio.charset.Charset;

/**
 * CAuthLogonChallengePacket is an object representing a WoW Packet coming from the client
 * for an authentication challenge.
 * @author Warkdev
 * @version v0.1 BETA.
 */
public class CAuthLogonChallengePacket extends AbstractAuthClientPacket {
    private int error;
    private String game;
    private String version;
    private int build;
    private String platform;
    private String os;
    private String country;
    private int timezone;
    private InetAddress ip;
    private int accountLength;
    private String accountName;
    
    /**
     * Default constructor with opcode.
     * @param opcode 
     */
    public CAuthLogonChallengePacket(AuthClientCmd opcode) {
        super(opcode);
    }

    /**
     * Return the error int, usage is unknown.
     * @return 
     */
    public int getError() {
        return error;
    }

    /**
     * Set the error integer.
     * @param error 
     */
    public void setError(int error) {
        this.error = error;
    }
    
    /**
     * Getter for the game.
     * @return 
     */
    public String getGame() {
        return game;
    }

    /**
     * Setter for the game.
     * @param game 
     */
    public void setGame(String game) {
        this.game = game;
    }

    /**
     * Getter for the version
     * @return 
     */
    public String getVersion() {
        return version;
    }

    /**
     * Setter for the version.
     * @param version 
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Getter for the build.
     * @return 
     */
    public int getBuild() {
        return build;
    }

    /**
     * Setter for the build.
     * @param build 
     */
    public void setBuild(int build) {
        this.build = build;
    }

    /**
     * Getter for the platform.
     * @return 
     */
    public String getPlatform() {
        return platform;
    }

    /**
     * Setter for the platform.
     * @param platform 
     */
    public void setPlatform(String platform) {
        this.platform = platform;
    }

    /**
     * Getter for the OS.
     * @return 
     */
    public String getOs() {
        return os;
    }

    /**
     * Setter for the OS.
     * @param os 
     */
    public void setOs(String os) {
        this.os = os;
    }

    /**
     * Getter for the country.
     * @return 
     */
    public String getCountry() {
        return country;
    }

    /**
     * Setter for the country.
     * @param country 
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Getter for the timezone, can be linked to the table RealmTimezone.
     * @return 
     */
    public int getTimezone() {
        return timezone;
    }

    /**
     * Setter for the timezone.
     * @param timezone 
     */
    public void setTimezone(int timezone) {
        this.timezone = timezone;
    }

    /**
     * Getter for the IP address of the client.
     * @return 
     */
    public InetAddress getIp() {
        return ip;
    }

    /**
     * Setter of the IP address of the client.
     * @param ip 
     */
    public void setIp(InetAddress ip) {
        this.ip = ip;
    }

    /**
     * Length of the account of the client.
     * @return 
     */
    public int getAccountLength() {
        return accountLength;
    }

    /**
     * Setter for the length of the account of the client.
     * @param accountLength 
     */
    public void setAccountLength(int accountLength) {
        this.accountLength = accountLength;
    }

    /**
     * Getter for the account name.
     * @return 
     */
    public String getAccountName() {
        return accountName;
    }

    /**
     * Setter for the account name.
     * @param accountName 
     */
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
        
    @Override
    public String toString(){
        String toString="[CAuthLogonChallengePacket [Opcode:"+this.opcode
                +", Error:"+this.error
                +", Game:"+this.game
                +", Version:"+this.version
                +", Build:"+this.build
                +", Platform:"+this.platform
                +", OS:"+this.platform
                +", Country:"+this.country
                +", Timezone:"+this.timezone
                +", IP:"+this.ip
                +", Account Length:"+this.accountLength
                +", AccountName:"+this.accountName
                +" ] ]"
                ;
        return toString;
    }  
    
    @Override
    public void decode(ByteBuf buf) throws Exception{
        // Decoding packet.
        
        this.error = buf.readByte();
        final int size = buf.readShort();
        
        if(buf.readableBytes() < size){
            // Used to return in the decoder.
            throw new Exception();
        }
        
        this.game = new StringBuilder(new String(buf.readBytes(4).array(), Charset.defaultCharset())).reverse().toString();
        this.version = buf.readByte() + "." + buf.readByte() + "." + buf.readByte();
        this.build = buf.readShort();
        this.platform = new StringBuilder(new String(buf.readBytes(4).array(), Charset.defaultCharset())).reverse().toString();
        this.os = new StringBuilder(new String(buf.readBytes(4).array(), Charset.defaultCharset())).reverse().toString();
        this.country = new StringBuilder(new String(buf.readBytes(4).array(), Charset.defaultCharset())).reverse().toString();
        this.timezone = buf.readInt();
        this.ip = InetAddress.getByAddress(buf.readBytes(4).array());
        this.accountLength = buf.readUnsignedByte();
        this.accountName = new String(buf.readBytes(this.accountLength).array(), Charset.defaultCharset());                
    }
}
