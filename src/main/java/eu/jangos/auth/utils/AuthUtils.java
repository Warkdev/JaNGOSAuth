package eu.jangos.auth.utils;

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
import eu.jangos.auth.network.packet.server.SAuthLogonChallengePacket;
import eu.jangos.auth.network.packet.server.SAuthLogonProofPacket;
import eu.jangos.auth.network.srp.SRPServer;

/**
 * AuthUtils is an helper class for the authentication server, allowing to generate server packets.
 * @author Warkdev
 * @version v0.1 BETA.
 */
public class AuthUtils {

    /**
     * Check whether the ip passed in parameter is a valid IPv4 address or not.
     * @param ipAddress The IP to be validated
     * @return a boolean value, true if the address is a valid IPv4, false otherwise.
     */
    public static boolean isValidIP4Address(String ipAddress) {
        if (ipAddress.matches("^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})$")) {
            String[] groups = ipAddress.split("\\.");

            for (int i = 0; i <= 3; i++) {
                String segment = groups[i];
                if (segment == null || segment.length() <= 0) {
                    return false;
                }

                int value = 0;
                try {
                    value = Integer.parseInt(segment);
                } catch (NumberFormatException e) {
                    return false;
                }
                if (value > 255) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    
    /**
     * Generates a SAuthLogonChallengePacket based on the SRPServer calculation.
     * @param server The SRPServer used to make the calculation of the challenge.
     * @return The created packet.
     * @see SAuthLogonChallengePacket
     * @see SRPServer
     */
    public static SAuthLogonChallengePacket generateSAuthLogonChallengePacket(SRPServer server) {
        SAuthLogonChallengePacket sPacket = new SAuthLogonChallengePacket(AuthClientCmd.CMD_AUTH_LOGON_CHALLENGE);

        sPacket.setResult(AuthServerCmd.AUTH_SUCCESS);
        sPacket.setB(server.getB());
        sPacket.setG(SRPServer.getG().asByteArray(1)[0]);
        sPacket.setN(SRPServer.getN());
        sPacket.setSalt(server.getS());

        return sPacket;
    }

    /**
     * Generates an SAuthLogonProofPacket from a byte proof.
     * @param b The proof, in an array of bytes, calculated by the server.
     * @return The generated packet.
     * @see SAuthLogonProofPacket
     */
    public static SAuthLogonProofPacket generateSAuthLogonProofPacket(byte[] b) {
        SAuthLogonProofPacket sPacket = new SAuthLogonProofPacket(AuthClientCmd.CMD_AUTH_LOGON_PROOF);

        BigNumber proof = new BigNumber();
        proof.setBinary(b);        
        
        sPacket.setProof(proof);

        return sPacket;
    }
}
