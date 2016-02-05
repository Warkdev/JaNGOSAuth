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
import io.netty.buffer.ByteBuf;

/**
 * CAuthRealmList is an object representing a WoW Packet coming from the client
 * for a realm list request.
 *
 * @author Warkdev
 * @version v0.1 BETA.
 */
public class CAuthRealmList extends AbstractAuthClientPacket {

    /**
     * Constructor with opcode.
     * @param opcode 
     */
    public CAuthRealmList(AuthClientCmd opcode) {
        super(opcode);
    }

    public String toString() {
        String toString = "[CAuthRealmList [Opcode:" + this.opcode
                + " ] ]";

        return toString;
    }

    @Override
    public void decode(ByteBuf buf) throws Exception {
        // Nothing to decode here.
    }

    @Override
    public void encode(ByteBuf buf) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
