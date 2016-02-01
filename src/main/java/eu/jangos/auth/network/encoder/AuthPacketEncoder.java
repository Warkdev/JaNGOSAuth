package eu.jangos.auth.network.encoder;

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

import eu.jangos.auth.network.packet.AbstractAuthServerPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AuthPacketEncoder is an extension of MessageToByteEncoder, it encodes WoW understandable packets
 * into byte packets sent over the network.
 * 
 * The following WoW packets are currently translated:
 * - Any failed logon (password, locked, ban, unknown, ...)
 * - Logon challenge packet
 * - Logon proof packet
 * - Realm list packet
 * 
 * @author Warkdev
 * @version v0.1 BETA.
 */
public class AuthPacketEncoder extends MessageToByteEncoder<AbstractAuthServerPacket> {    
    private static final Logger logger = LoggerFactory.getLogger(AuthPacketEncoder.class);
    
    @Override
    protected void encode(ChannelHandlerContext ctx, AbstractAuthServerPacket msg, ByteBuf out) throws Exception {     
        logger.debug("Context: "+ctx.name()+", packet: "+msg.toString());
        msg.encode(out);        
    }
    
}
