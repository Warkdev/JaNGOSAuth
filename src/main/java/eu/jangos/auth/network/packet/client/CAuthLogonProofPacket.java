package eu.jangos.auth.network.packet.client;

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
import eu.jangos.auth.network.packet.AbstractAuthClientPacket;
import eu.jangos.auth.utils.BigNumber;
import io.netty.buffer.ByteBuf;
import java.nio.ByteOrder;

/**
 * CAuthLogonChallengePacket is an object representing a WoW Packet coming from
 * the client for an authentication proof challenge.
 *
 * @author Warkdev
 * @version v0.1 BETA.
 */
public class CAuthLogonProofPacket extends AbstractAuthClientPacket {

    /**
     * This is the public client value.
     */
    private BigNumber A;

    /**
     * The actual proof sent by the client.
     */
    private BigNumber M;

    /**
     * Not used.
     */
    private BigNumber crc;

    /**
     * Number of keys, not used.
     */
    private byte keyNumber;

    /**
     * Security flag, always 0.
     */
    private byte securityFlag;

    /**
     * Constructor with Opcode.
     *
     * @param opcode
     */
    public CAuthLogonProofPacket(AuthClientCmd opcode) {
        super(opcode);
        this.A = new BigNumber();
        this.M = new BigNumber();
        this.crc = new BigNumber();
    }

    /**
     * Getter for A
     *
     * @return
     */
    public BigNumber getA() {
        return A;
    }

    /**
     * Setter for A
     *
     * @param A
     */
    public void setA(BigNumber A) {
        this.A = A;
    }

    /**
     * Getter for M
     *
     * @return
     */
    public BigNumber getM() {
        return M;
    }

    /**
     * Setter for M
     *
     * @param M
     */
    public void setM(BigNumber M) {
        this.M = M;
    }

    /**
     * Getter for the CRC
     *
     * @return
     */
    public BigNumber getCrc() {
        return crc;
    }

    /**
     * Setter for the CRC.
     *
     * @param crc
     */
    public void setCrc(BigNumber crc) {
        this.crc = crc;
    }

    /**
     * Getter for the key number.
     *
     * @return
     */
    public byte getKeyNumber() {
        return keyNumber;
    }

    /**
     * Setter for the key number.
     *
     * @param keyNumber
     */
    public void setKeyNumber(byte keyNumber) {
        this.keyNumber = keyNumber;
    }

    /**
     * Getter for the security flag.
     *
     * @return
     */
    public byte getSecurityFlag() {
        return securityFlag;
    }

    /**
     * Setter for the security flag.
     *
     * @param securityFlag
     */
    public void setSecurityFlag(byte securityFlag) {
        this.securityFlag = securityFlag;
    }

    public String toString() {
        String toString = "[CAuthLogonProofPacket [Opcode:" + this.opcode
                + ", A:" + this.A.toHexString()
                + ", M:" + this.M.toHexString()
                + ", CRC:" + this.crc.toHexString()
                + ", Number of keys:" + this.keyNumber
                + ", SecFlags:" + this.securityFlag
                + " ] ]";
        return toString;
    }

    @Override
    public void decode(ByteBuf buf) throws Exception {
        ByteBuf in = buf.order(ByteOrder.BIG_ENDIAN);
        
        if (buf.readableBytes() < 74) {
            throw new Exception();
        }

        this.A.setBinary(in.readBytes(32).array());
        this.M.setBinary(in.readBytes(20).array(), false);
        this.crc.setBinary(in.readBytes(20).array(), false);

        this.keyNumber = in.readByte();
        this.securityFlag = in.readByte();        
    }

}
