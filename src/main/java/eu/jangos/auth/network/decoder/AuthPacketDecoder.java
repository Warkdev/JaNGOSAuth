package eu.jangos.auth.network.decoder;

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
import eu.jangos.auth.network.packet.client.CAuthLogonChallengePacket;
import eu.jangos.auth.network.packet.client.CAuthLogonProofPacket;
import eu.jangos.auth.network.packet.client.CAuthRealmList;
import eu.jangos.auth.network.packet.client.CAuthReconnectProofPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.nio.ByteOrder;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AuthPacketDecoder is an extension of the ByteMessageDecoder of Netty. It is
 * in charge of translation incoming byte network packets into WoW
 * understandable packet manageable by the authentication server.
 *
 * The decoder currently supports the following WoW packets: -
 * CMD_AUTH_LOGON_CHALLENGE - CMD_AUTH_LOGON_PROOF - CMD_REALM_LIST
 *
 * @author Warkdev
 * @version v0.1 BETA.
 * @see AuthClientCmd for defined opcode.
 */
public class AuthPacketDecoder extends ByteToMessageDecoder {

    private static final Logger logger = LoggerFactory.getLogger(AuthPacketDecoder.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        ByteBuf msg = in.order(ByteOrder.LITTLE_ENDIAN);

        if (msg.readableBytes() < 1) {
            return;
        }

        final AuthClientCmd code = AuthClientCmd.convert(msg.readByte());

        if (code == null) {
            return;
        }

        AbstractAuthClientPacket packet = null;

        switch (code) {
            case CMD_AUTH_LOGON_CHALLENGE:
                packet = new CAuthLogonChallengePacket(code);                                                              
                break;
            case CMD_AUTH_LOGON_PROOF:
                packet = new CAuthLogonProofPacket(code);               
                break;
            case CMD_REALM_LIST:
                packet = new CAuthRealmList(code);                
                break;
            case CMD_AUTH_RECONNECT_CHALLENGE:                
                packet = new CAuthLogonChallengePacket(code);
                break;
            case CMD_AUTH_RECONNECT_PROOF:
                packet = new CAuthReconnectProofPacket(code);
                break;
            default:
                logger.error("Context: " + ctx.name() + "Packet received, opcode not supported: " + code);
                msg.clear();
                ctx.close();
                break;
        }

        if (packet != null) {
            try {
                logger.debug("Context: " + ctx.name() + "Packet received, opcode: " + code);
                packet.decode(msg);
            } catch (Exception e) {
                return;
            }
            out.add(packet);
            msg.clear();
        }
    }

}
