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

import eu.jangos.auth.network.decoder.AuthPacketDecoder;
import eu.jangos.auth.network.encoder.AuthPacketEncoder;
import eu.jangos.auth.network.opcode.AuthClientCmd;
import eu.jangos.auth.network.packet.client.CAuthLogonChallengePacket;
import io.netty.channel.embedded.EmbeddedChannel;
import java.net.InetAddress;
import org.junit.After;
import org.junit.Test;
import org.junit.Before;

/**
 *
 * @author Warkdev
 */
public class AuthServerHandlerTest {
    
    EmbeddedChannel ch;
    
    public AuthServerHandlerTest() {
    }

    @Before
    public void setup(){
        ch = new EmbeddedChannel(new AuthServerHandler());        
    }
    
    @After
    public void tearDown(){
        ch.close();
    }
    
    /**
     * Test of channelRead method, of class AuthServerHandler.
     */
    @Test
    public void testLoginFlow() throws Exception {
        System.out.println("channelRead");
              
        CAuthLogonChallengePacket logon = new CAuthLogonChallengePacket(AuthClientCmd.CMD_AUTH_LOGON_CHALLENGE);
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
                                        
        ch.writeInbound(logon);   
        System.out.println(ch.readOutbound());
        
        System.out.println("End channelRead");
    }       
}
