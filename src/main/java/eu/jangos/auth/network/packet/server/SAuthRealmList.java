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
import eu.jangos.auth.model.Account;
import eu.jangos.auth.model.Realm;
import eu.jangos.auth.model.RealmAccount;
import eu.jangos.auth.network.opcode.AuthClientCmd;
import eu.jangos.auth.network.packet.AbstractAuthServerPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * SAuthRealmList is an object representing a WoW Packet sent to the client with
 * the least of realms.
 *
 * @author Warkdev
 * @version v0.1 BETA.
 */
public class SAuthRealmList extends AbstractAuthServerPacket {

    /**
     * The packet size.
     */
    private short size;

    /**
     * The number of realms.
     */
    private byte numRealms;

    /**
     * List of realm available.
     */
    private List<Realm> listRealms;

    /**
     * Packet Unknown - always 2
     */
    private short unknown;

    /**
     * Account requesting the realm list.
     *
     * @since 10/03/2015
     */
    private Account account;

    /**
     * Constructor with default values.
     *
     * @param code
     */
    public SAuthRealmList(AuthClientCmd code) {
        super(code);
        this.size = 7; // Default packet size.
        this.numRealms = (byte) 0; // Default number of realm.
        this.listRealms = new ArrayList<>();
        this.unknown = (short) 2;
    }

    /**
     * Add a realm to the list of realm already know by this packet.
     *
     * @param r The realm to be added. If it already exists, it is not added.
     */
    public void addRealm(Realm r) {
        if (this.listRealms.add(r)) {
            this.size += (short) getRealmLength(r);
            ++this.numRealms;
        }
    }

    /**
     * Remove a realm from the list of realms already known by this packet.
     *
     * @param r The realm to be removed.
     */
    public void remRealm(Realm r) {
        if (this.listRealms.remove(r)) {
            this.size -= (short) getRealmLength(r);
            --this.numRealms;
        }
    }

    /**
     * Add a collection of realms to this packet.
     *
     * @param listRealms The list of realms to be added.
     */
    public void addRealms(List<Realm> listRealms) {
        for (Realm r : listRealms) {
            addRealm(r);
        }
    }

    /**
     * Returns the size of the packet.
     *
     * @return Size of the packet.
     */
    public short getSize() {
        return size;
    }

    /**
     * Set the size of the packet.
     *
     * @param size The size to be set.
     */
    public void setSize(short size) {
        this.size = size;
    }

    /**
     * Returns the number of realms known by this packet.
     *
     * @return The number of realms.
     */
    public byte getNumRealms() {
        return numRealms;
    }

    /**
     * Set the number of realms known by this packet.
     *
     * @param numRealms The number of realms to be set.
     */
    public void setNumRealms(byte numRealms) {
        this.numRealms = numRealms;
    }

    /**
     * Getter for the list of realms known by this packet.
     *
     * @return The list of realms known by this packet.
     */
    public List<Realm> getListRealms() {
        return listRealms;
    }

    /**
     * Set the list of realms known by this packet.
     *
     * @param listRealms The list of realms to be set.
     */
    public void setListRealms(List<Realm> listRealms) {
        this.listRealms = listRealms;
    }

    public short getUnknown() {
        return unknown;
    }

    public void setUnknown(short unknown) {
        this.unknown = unknown;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public String toString() {
        String toString = "[SAuthRealmList [Opcode:" + this.code
                + ", size:" + this.size
                + ", unknown:" + 0
                + ", NumRealms:" + this.numRealms;

        for (Realm r : this.listRealms) {
            toString += " [ type:" + r.getRealmtype().getId()
                    + ", invalid:" + r.isInvalid()
                    + ", offline:" + r.isOffline()
                    + ", show version:" + r.isShowversion()
                    + ", new players:" + r.isNewplayers()
                    + ", recommended:" + r.isRecommended()
                    + ", name:" + r.getName()
                    + ", address:" + r.getAddress() + ":" + r.getPort()
                    + ", population:" + r.getPopulation()
                    + ", characters:" + 0
                    + ", timezone:" + r.getRealmtimezone().getId()
                    + ", unknown:" + 0
                    + "]";
        }

        toString += ", unknown:" + this.unknown
                + " ] ]";
        return toString;
    }

    /**
     * Calculate the size, in bytes, of this realm implementation, used for
     * network transfer.
     *
     * @return The size of this realm with the formula (14 + address + ":" +
     * port + name)
     */
    private int getRealmLength(Realm r) {               
        return 14 + (r.getAddress() + ":" + r.getPort()).length() + r.getName().length();
    }

    /**
     * This method is converting the realm flags into a single integer.
     * @param r The realm for which the flags needs to be converted.
     * @return The integer value corresponding to the flags.
     */
    private int convertFlagsToInt(Realm r) {
        int flags = 0;
        flags+=(r.isInvalid() ? 1 : 0);
        flags+=(r.isOffline() ? 2 : 0);
        flags+=(r.isShowversion() ? 4 : 0);
        // flags+=(0*8); // Unknown flag
        // flags+=(0*16); // Unknown flag
        flags+=(r.isNewplayers() ? 32 : 0);
        flags+=(r.isRecommended() ? 64 : 0);
        // flags+=(0*128); // unknown flag
        
        return flags;
    }
    
    @Override
    public void encode(ByteBuf buf) throws Exception {
        // Packet structure:
        // 1b - 2b - 4b - 1b - [ 4b - 1b - ?b - ?b - 4b - 1b - 1b - 1b ] - 2b
        // opcode - size - 0 - numRealms - [ Type - Flags - Name - Address - Population - Num Chars - Timezone - Unknown ] - Unknown                        

        buf.writeByte(this.code.getValue());
        buf.order(ByteOrder.LITTLE_ENDIAN).writeShort(this.size);
        buf.writeInt(0); // Unknown 
        buf.writeByte(this.numRealms);

        for (Realm r : this.listRealms) {
            buf.order(ByteOrder.LITTLE_ENDIAN).writeInt(r.getRealmtype().getId());                        
            buf.writeByte(convertFlagsToInt(r));
            ByteBufUtil.writeAscii(buf, r.getName());
            buf.writeByte((byte) 0); // End string
            ByteBufUtil.writeAscii(buf, r.getAddress() + ":" + r.getPort());
            buf.writeByte((byte) 0); // End string
            buf.order(ByteOrder.LITTLE_ENDIAN).writeFloat(r.getPopulation());

            byte chars = 0;

            if(account.getRealmAccounts() != null)
            {            
                // Should calculate here the number of chars on this realm.
                for(RealmAccount rel : (Set<RealmAccount>) account.getRealmAccounts())
                {
                    if(rel.getId().getFkRealm() == r.getId())
                    {
                        chars = (byte) rel.getNumChars();
                    }
                }
            }
            
            buf.writeByte(chars); // Amount of character
            buf.writeByte(r.getRealmtimezone().getId());
            buf.writeByte((byte) 0);  // End string
        }

        buf.order(ByteOrder.LITTLE_ENDIAN).writeShort(this.unknown);
    }
}
