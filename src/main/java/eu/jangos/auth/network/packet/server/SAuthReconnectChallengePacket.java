package eu.jangos.auth.network.packet.server;

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
import eu.jangos.auth.network.opcode.AuthServerCmd;
import eu.jangos.auth.network.packet.AbstractAuthServerPacket;
import eu.jangos.auth.utils.BigNumber;
import io.netty.buffer.ByteBuf;

/**
 * SAuthReconnectChallengePacket is an object representing a WoW Packet sent to the
 * client for an reconnection challenge.
 *
 * @author Warkdev
 * @version v0.1 BETA.
 */
public class SAuthReconnectChallengePacket extends AbstractAuthServerPacket {

    /**
     * The AuthResult, always success for this packet.
     */
    private AuthServerCmd result;

    /**
     * Challenge is the challenge sent by the server.
     */
    private BigNumber challenge;

    /**
     * Filler is an array of 16 bytes;
     */
    private byte[] filler;
    
    /**
     * Default constructor with opcode.
     *
     * @param code
     */
    public SAuthReconnectChallengePacket(AuthClientCmd code) {
        super(code);
        this.filler = new byte[16];
        for(int i = 0; i < filler.length; i++)
        {
            this.filler[i] = 0;
        }
    }

    public AuthServerCmd getResult() {
        return this.result;
    }

    public void setResult(final AuthServerCmd result) {
        this.result = result;
    }

    public BigNumber getChallenge() {
        return this.challenge;
    }

    public void setChallenge(BigNumber challenge) {
        this.challenge = challenge;
    }    

    @Override
    public String toString() {
        String toString = "[SAuthReconnectChallengePacket [Opcode:" + this.code
                + ", Result:" + this.result
                + ", Challenge:" + this.challenge.toHexString()                                
                + " ] ]";
        return toString;
    }

    @Override
    public void encode(ByteBuf buf) throws Exception {
        // Packet structure:
        // 1b - 1b - 16b - 16b
        // opcode - authresult - challenge - filler (16b 0)

        buf.writeByte(this.code.getValue());        
        buf.writeByte(this.result.getValue());
        buf.writeBytes(this.challenge.asByteArray(16));
        buf.writeBytes(this.filler);        
    }
}
