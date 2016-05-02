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
 * SAuthReconnectProofPacket is an object representing a WoW Packet sent to the
 * client for an reconnect proof.
 *
 * @author Warkdev
 * @version v0.1 BETA.
 */
public class SAuthReconnectProofPacket extends AbstractAuthServerPacket {

    /**
     * The result for this packet is always success.
     */
    private AuthServerCmd result;

    /**
     * Constructor with the opcode.
     *
     * @param opcode
     */
    public SAuthReconnectProofPacket(AuthClientCmd opcode) {
        super(opcode);
        this.result = AuthServerCmd.AUTH_SUCCESS;
    }

    public AuthServerCmd getResult() {
        return result;
    }

    public void setResult(AuthServerCmd result) {
        this.result = result;
    }

    @Override
    public String toString() {
        String toString = "[SAuthReconnectProofPacket [Opcode:" + this.code
                + ", Result:" + this.result
                + " ] ]";
        return toString;
    }

    @Override
    public void encode(ByteBuf buf) throws Exception {
        // Packet structure:
        // 1b - 1b
        // opcode - authresult

        buf.writeByte(this.code.getValue());
        buf.writeByte(this.result.getValue());
    }

}
