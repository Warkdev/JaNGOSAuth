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
import eu.jangos.auth.utils.BigNumber;
import io.netty.buffer.ByteBuf;
import java.nio.ByteOrder;

/**
 * CAuthReconnectProofPacket is an object representing a WoW Packet coming from
 * the client for an reconnection proof challenge.
 *
 * @author Warkdev
 * @version v0.1 BETA.
 */
public class CAuthReconnectProofPacket extends AbstractAuthClientPacket {

    /**
     * This is the first part of the client proof.
     */
    private BigNumber R1;

    /**
     * This is the second part of the client proof.
     */
    private BigNumber R2;

    /**
     * This is the third part of the client proof.
     */
    private BigNumber R3;

    /**
     * Number of keys, not used.
     */
    private byte keyNumber;    

    /**
     * Constructor with Opcode.
     *
     * @param opcode
     */
    public CAuthReconnectProofPacket(AuthClientCmd opcode) {
        super(opcode);
        this.R1 = new BigNumber();
        this.R2 = new BigNumber();
        this.R3 = new BigNumber();
    }

    /**
     * Getter for R1
     *
     * @return
     */
    public BigNumber getR1() {
        return R1;
    }

    /**
     * Setter for R1
     *
     * @param R1
     */
    public void setR1(BigNumber R1) {
        this.R1 = R1;
    }

    /**
     * Getter for R2
     *
     * @return
     */
    public BigNumber getR2() {
        return R2;
    }

    /**
     * Setter for R2
     *
     * @param R2
     */
    public void setR2(BigNumber R2) {
        this.R2 = R2;
    }

    /**
     * Getter for the R3
     *
     * @return
     */
    public BigNumber getR3() {
        return R3;
    }

    /**
     * Setter for the CRC.
     *
     * @param crc
     */
    public void setR3(BigNumber R3) {
        this.R3 = R3;
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

    public String toString() {
        String toString = "[CAuthLogonProofPacket [Opcode:" + this.opcode
                + ", R1:" + this.R1.toHexString()
                + ", R2:" + this.R2.toHexString()
                + ", R3:" + this.R3.toHexString()
                + ", Number of keys:" + this.keyNumber                
                + " ] ]";
        return toString;
    }

    @Override
    public void decode(ByteBuf buf) throws Exception {
        ByteBuf in = buf.order(ByteOrder.BIG_ENDIAN);
        
        if (buf.readableBytes() < 57) {
            throw new Exception();
        }

        this.R1.setBinary(in.readBytes(16).array());
        this.R2.setBinary(in.readBytes(20).array(), false);
        this.R3.setBinary(in.readBytes(20).array(), false);

        this.keyNumber = in.readByte();        
    }

}
