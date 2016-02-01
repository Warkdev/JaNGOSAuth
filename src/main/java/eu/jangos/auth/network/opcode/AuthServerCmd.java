package eu.jangos.auth.network.opcode;

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

/**
 * AuthServerCmd is an enumeration of all possible command that the server may send back to the client.
 * @author Warkdev
 * @version v0.1 BETA.
 */
public enum AuthServerCmd {
    /**
     * Success, any step.
     */
    AUTH_SUCCESS((byte) 0),
    
    /**
     * Unable to connect, usage unknown.
     */
    AUTH_FAIL_UNKNOWN0((byte) 1),                 
    
    /**
     * Unable to connect, unknown.
     */
    AUTH_FAIL_UNKNOWN1((byte) 2),                 
    
    /**
     * Account is banned.
     * "This game account has been closed and is no longer available for use".
     */
    AUTH_FAIL_BANNED((byte) 3),                
    
    /**
     * Account is unknown.
     * "The information you have entered is not valid. Please check the spelling".
     */
    AUTH_FAIL_UNKNOWN_ACCOUNT((byte) 4),                 
    
    /**
     * Password is incorrect.
     * "The information you have entered is not valid. Please check the spelling".
     * The client reject next login attempts after this error, so in code used WOW_FAIL_UNKNOWN_ACCOUNT for both cases.
     */
    AUTH_FAIL_INCORRECT_PASSWORD((byte) 5),                 
    
    /**
     * Account is already logged in.
     * This account is already logged into game. Please check the spelling and try again.
     */    
    AUTH_FAIL_ALREADY_ONLINE((byte) 6),   
    
    /**
     * Account prepaid time is over.
     * You have used up your prepaid time for this account. Please purchase more to continue playing.
     */
    AUTH_FAIL_NO_TIME((byte) 7),
    
    /**
     * Database is in maintenance.
     * Could not log in to game at this time. Please try again later.
     */
    AUTH_FAIL_DB_BUSY((byte) 8),
    
    /**
     * Version is invalid.
     * Unable to validate game version. This may be caused by file corruption or interference of another program. 
     * Please visit site for more information and possible solutions to this issue.
     */
    AUTH_FAIL_VERSION_INVALID((byte) 9),                 
    
    /**
     * Version needs to be updated. * Not supported by jE4W *
     * Downloading.
     */
    AUTH_FAIL_VERSION_UPDATE((byte) 10),                 
    
    /**
     * Server is invalid.
     * Unable to connect.
     */
    AUTH_FAIL_INVALID_SERVER((byte) 11),
    
    /**
     * Account is locked.
     * This game account has been temporarily suspended. Please go to site/banned.html for further information
     */
    AUTH_FAIL_SUSPENDED((byte) 12),
    
    /**
     * No access for this account.
     * Unable to connect.
     */
    AUTH_FAIL_FAIL_NOACCESS((byte) 13),
    
    /**
     * Connected (used in Vanilla ? Not sure..)
     * Connected.
     */
    AUTH_SUCCESS_SURVEY((byte) 14),
    
    /**
     * Parent control has restricted access on this account.
     * Access to this account has been blocked by parental controls. Your settings may be changed in your account preferences at site.
     */
    AUTH_FAIL_PARENTCONTROL((byte) 15);
    
    
    private final byte result;

    private AuthServerCmd(byte result)
    {
        this.result = result;
    }
    
    public byte getValue(){
        return this.result;
    }
    
    public static AuthServerCmd convert(byte value) {
        for(AuthServerCmd cmd : AuthServerCmd.values()){
            if(cmd.getValue() == value)
                return cmd;
        }            
        return null;
    } 
}
