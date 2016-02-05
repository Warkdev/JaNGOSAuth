package eu.jangos.auth.network.opcode;

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

/**
 * AuthClientCmd is the enumeration of all Command that can be sent by the client during the authentication process.
 * @author Warkdev
 * @version v0.1 BETA.
 */
public enum AuthClientCmd {
    /**
     * Initial authentication step, the client sent a challenge.
     */
    CMD_AUTH_LOGON_CHALLENGE((byte) 0),
    
    /**
     * Second authentication step, the client is sending his proof.
     */
    CMD_AUTH_LOGON_PROOF((byte) 1),
    
    /**
     * Not supported by JaNGOS. Usage ?
     */
    CMD_AUTH_RECONNECT_CHALLENGE((byte) 2),
    
    /**
     * Not supported by JaNGOS. Usage ?
     */
    CMD_AUTH_RECONNECT_PROOF((byte) 3),
    
    /**
     * Third authentication step, the client is requesting the realm list.
     */
    CMD_REALM_LIST((byte) 16),
    
    /**
     * Not supported by JaNGOS. Usage ?
     */
    CMD_XFER_INITIATE((byte) 48),
    
    /**
     * Not supported by JaNGOS. Usage ?
     */
    CMD_XFER_DATA((byte) 49),
    
    /**
     * Not supported by JaNGOS. Usage ?
     */
    CMD_XFER_ACCEPT((byte) 50),
    
    /**
     * Not supported by JaNGOS. Usage ?
     */
    CMD_XFER_RESUME((byte) 51),
    
    /**
     * Not supported by JaNGOS. Usage ?
     */
    CMD_XFER_CANCEL((byte) 52);
    
    private final byte code;

    private AuthClientCmd(byte code)
    {
        this.code = code;
    }
    
    public byte getValue(){
        return this.code;
    }
    
    public static AuthClientCmd convert(byte value) {
        for(AuthClientCmd cmd : AuthClientCmd.values()){
            if(cmd.getValue() == value)
                return cmd;
        }            
        return null;
    } 
}
