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
 * SAuthLogonChallengePacket is an object representing a WoW Packet sent to the
 * client for an authentication challenge.
 *
 * @author Warkdev
 * @version v0.1 BETA.
 */
public class SAuthLogonChallengePacket extends AbstractAuthServerPacket {

    /**
     * The AuthResult, always success for this packet.
     */
    private AuthServerCmd result;

    /**
     * Error is a byte used to fill-in the packet, it is always equals to 0.
     */
    private byte error;

    /**
     * B is an ephemeral sever's public value.
     */
    private BigNumber B;

    /**
     * Always 1 byte.
     */
    private byte gLength;

    /**
     * g is the generator number for N. Alwaus 1 byte length.
     */
    private byte g;

    /**
     * Length of N, Always 32 bytes.
     */
    private byte nLength;

    /**
     * N is a 32 byte fixed safe-prime.
     */
    private BigNumber n;

    /**
     * Salt is the S of the SRP6 protocol. It is a 32 bytes length value.
     */
    private BigNumber salt;

    /**
     * Unknown3 is a random 16 bytes number.
     */
    private BigNumber unk3;

    /**
     * Security flag is always 0 in Classic client.
     */
    private byte securityFlag;

    /**
     * Default constructor with opcode.
     *
     * @param code
     */
    public SAuthLogonChallengePacket(AuthClientCmd code) {
        super(code);
        this.error = 0;
        this.gLength = 1;
        this.nLength = 32;
        this.unk3 = new BigNumber("0");
        this.securityFlag = 0;
    }

    public byte getError() {
        return error;
    }

    public void setError(byte error) {
        this.error = error;
    }

    public AuthServerCmd getResult() {
        return this.result;
    }

    public void setResult(final AuthServerCmd result) {
        this.result = result;
    }

    public BigNumber getB() {
        return B;
    }

    public void setB(BigNumber B) {
        this.B = B;
    }

    public byte getgLength() {
        return gLength;
    }

    public void setgLength(byte gLength) {
        this.gLength = gLength;
    }

    public byte getG() {
        return g;
    }

    public void setG(byte g) {
        this.g = g;
    }

    public byte getnLength() {
        return nLength;
    }

    public void setnLength(byte nLength) {
        this.nLength = nLength;
    }

    public BigNumber getN() {
        return n;
    }

    public void setN(BigNumber n) {
        this.n = n;
    }

    public BigNumber getSalt() {
        return salt;
    }

    public void setSalt(BigNumber salt) {
        this.salt = salt;
    }

    public BigNumber getUnk3() {
        return unk3;
    }

    public void setUnk3(BigNumber unk3) {
        this.unk3 = unk3;
    }

    public byte getSecurityFlag() {
        return securityFlag;
    }

    public void setSecurityFlag(byte securityFlag) {
        this.securityFlag = securityFlag;
    }

    public String toString() {
        String toString = "[SAuthLogonChallengePacket [Opcode:" + this.code
                + ", Error:" + this.error
                + ", Result:" + this.result
                + ", B:" + this.B.toHexString()
                + ", GLength:" + this.gLength
                + ", g:" + this.g
                + ", NLength:" + this.nLength
                + ", N:" + this.n.toHexString()
                + ", s:" + this.salt.toHexString()
                + ", Unknown3:" + this.unk3.toHexString()
                + ", SecFlags:" + this.securityFlag
                + " ] ]";
        return toString;
    }

    @Override
    public void encode(ByteBuf buf) throws Exception {
        // Packet structure:
        // 1b - 1b - 1b - 32b - 1b - 1b - 1b - 32b - 32b - 16b - 1b            
        // opcode - error - authresult - B - gLength - g - nLength - N - s - unknown3 - SecFlags

        buf.writeByte(this.code.getValue());
        buf.writeByte(this.error);
        buf.writeByte(this.result.getValue());
        buf.writeBytes(this.B.asByteArray(32));
        buf.writeByte(this.gLength);
        buf.writeByte(this.g);
        buf.writeByte(this.nLength);
        buf.writeBytes(this.n.asByteArray(32));
        buf.writeBytes(this.salt.asByteArray(32));
        buf.writeBytes(this.unk3.asByteArray(16));
        buf.writeByte(this.securityFlag);
    }
}
