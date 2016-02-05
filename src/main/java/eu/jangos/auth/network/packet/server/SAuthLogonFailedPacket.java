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
import io.netty.buffer.ByteBuf;

/**
 * SAuthLogonFailedPacket is an object representing a WoW Packet sent to the
 * client for an authentication failed.
 *
 * @author Warkdev
 * @version v0.1 BETA.
 */
public class SAuthLogonFailedPacket extends AbstractAuthServerPacket {

    /**
     * This unused byte is necessary in case of failed logon.
     */
    private byte unused;

    /**
     * Failure reason.
     *
     * @see AuthServerCmd
     */
    private AuthServerCmd result;

    public SAuthLogonFailedPacket(AuthClientCmd code) {
        super(code);
        this.unused = 0;
    }

    public byte getUnused() {
        return unused;
    }

    public void setUnused(byte unused) {
        this.unused = unused;
    }

    public AuthServerCmd getResult() {
        return this.result;
    }

    public void setResult(final AuthServerCmd result) {
        this.result = result;
    }

    @Override
    public String toString() {
        String toString = "[AuthLogonFailedPacket [Opcode:" + this.code
                + ", unused:" + this.unused
                + ", result:" + this.result
                + " ] ]";

        return toString;
    }

    @Override
    public void encode(ByteBuf buf) throws Exception {
        // Packet structure:
        // 1b - 1b - 1b
        // opcode - unknown - authresult            
        
        buf.writeByte(this.code.getValue());
        buf.writeByte(this.unused);
        buf.writeByte(this.result.getValue());
    }
}
