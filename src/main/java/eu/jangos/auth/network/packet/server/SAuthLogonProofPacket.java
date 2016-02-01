package eu.jangos.auth.network.packet.server;

/*
 * Copyright 2016 Talendrys.
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
 * SAuthLogonProofPacket is an object representing a WoW Packet sent to the
 * client for an authentication proof.
 *
 * @author Warkdev
 * @version v0.1 BETA.
 */
public class SAuthLogonProofPacket extends AbstractAuthServerPacket {

    /**
     * The result for this packet is always success.
     */
    private AuthServerCmd result;

    /**
     * The actual proof sent by the server.
     */
    private BigNumber proof;

    /**
     * Account Flag, always 0 for classic.
     */
    private int accountFlag;

    /**
     * Constructor with the opcode.
     *
     * @param opcode
     */
    public SAuthLogonProofPacket(AuthClientCmd opcode) {
        super(opcode);
        this.result = AuthServerCmd.AUTH_SUCCESS;
        this.accountFlag = 0;
    }

    public AuthServerCmd getResult() {
        return result;
    }

    public void setResult(AuthServerCmd result) {
        this.result = result;
    }

    public BigNumber getProof() {
        return proof;
    }

    public void setProof(BigNumber proof) {
        this.proof = proof;
    }

    public int getAccountFlag() {
        return accountFlag;
    }

    public void setAccountFlag(int accountFlag) {
        this.accountFlag = accountFlag;
    }

    public String toString() {
        String toString = "[SAuthLogonProofPacket [Opcode:" + this.code
                + ", Result:" + this.result
                + ", Proof:" + this.proof.toHexString()
                + ", Account Flag:" + this.accountFlag
                + " ] ]";
        return toString;
    }

    @Override
    public void encode(ByteBuf buf) throws Exception {
        // Packet structure:
        // 1b - 1b - 20b - 4b
        // opcode - authresult - Proof - AccountFlags

        buf.writeByte(this.code.getValue());
        buf.writeByte(this.result.getValue());
        buf.writeBytes(this.proof.asByteArray(20));
        buf.writeInt(this.accountFlag);
    }

}
