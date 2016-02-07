package eu.jangos.auth.network.srp;

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
import eu.jangos.auth.utils.BigNumber;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * SRPServer is an helper class to handle SRP6 calculation and hashing methods
 * for WoW client since it deviates from the original SRP6 specification..
 *
 * @author Warkdev
 * @version v0.1 BETA.
 */
public class SRPServer {

    /**
     * N is a safe-prime of 32 bytes length. Equals to
     * 894B645E89E1535BBDAD5B8B290650530801B18EBFBF5E8FAB3C82872A3E9BB7.
     */
    private static final BigNumber N = new BigNumber("894B645E89E1535BBDAD5B8B290650530801B18EBFBF5E8FAB3C82872A3E9BB7", 16);

    /**
     * g is a generator of 1 byte. Equals to 7.
     */
    private static final BigNumber g = new BigNumber("7");

    /**
     * k is a constant used to generate other proofs. Equals to 3.
     */
    private static final BigNumber k = new BigNumber("3");

    /**
     * b is the server private value of 19 bytes, it is never shared and is only
     * used for one authentication.
     */
    private final BigNumber b = new BigNumber().setRand(19); // server private value        

    /**
     * s is the Salt generated, it is stored in database once generated for a
     * given account.
     */
    private BigNumber s; // Salt

    /**
     * v is the Password Verifier generated, it is stored in database once
     * generated for a given account.
     */
    private BigNumber v; // Verifier

    /**
     * gmod is used to calculate B, the server public value.
     */
    private BigNumber gmod; // gmod - used to calculate B

    /**
     * B is the server public value.
     */
    private BigNumber B; // server public value        

    /**
     * x is the password verifier, never held in database, always calculated
     * from H(s | H(I | ":" | p)).
     */
    private BigNumber x; // Intermediate verifier           

    /**
     * hashpass is the H(I | ":" | p) hash using SHA1.
     */
    private byte[] hashpass;

    /**
     * M2 is the server proof to show the client that the server knows the
     * password.
     */
    private BigNumber M2; // Server M calculation

    /**
     * S is the session key, unencrypted.
     */
    private BigNumber S; // Session Key

    /**
     * K is the hashed session key.
     */
    private BigNumber K; // Hashed Session Key

    /**
     * I is the account name.
     */
    byte[] I = null; // Client name

    // Cryptographic digest.
    private MessageDigest md;

    /**
     * Constructor of SRPServer
     *
     * @param hashpass hashpass of the account
     * @param account account name
     */
    public SRPServer(String hashpass, String account) {
        try {
            md = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("No such algorithm available");
        }

        this.hashpass = new BigNumber(hashpass).asByteArray(0, false);        
        this.I = account.getBytes();
    }

    public static BigNumber getN() {
        return N;
    }

    public static BigNumber getG() {
        return g;
    }

    public static BigNumber getK() {
        return k;
    }

    public BigNumber getSessionKey() {
        return K;
    }

    public BigNumber getS() {
        return s;
    }

    public BigNumber getX() {
        return x;
    }

    public byte[] getHashpass() {
        return hashpass;
    }

    public BigNumber getV() {
        return v;
    }

    public void setV(BigNumber v) {
        this.v = v;
    }

    public BigNumber getGmod() {
        return gmod;
    }

    public void setX(BigNumber x) {
        this.x = x;
    }

    public void setHashpass(byte[] hashpass) {
        this.hashpass = hashpass;
    }

    public void setS(BigNumber S) {
        this.S = S;
    }

    public void setK(BigNumber K) {
        this.K = K;
    }

    public void setGmod(BigNumber gmod) {
        this.gmod = gmod;
    }

    public BigNumber getB() {
        return B;
    }

    public BigNumber getSalt() {
        return this.s;
    }

    public void setSalt(BigNumber salt) {
        this.s = salt;
    }

    public void setB(BigNumber B) {
        this.B = B;
    }

    public byte[] getI() {
        return I;
    }

    public void setI(byte[] I) {
        this.I = I;
    }

    public BigNumber getM2() {
        return M2;
    }

    public void setM2(BigNumber M2) {
        this.M2 = M2;
    }

    public MessageDigest getMd() {
        return md;
    }

    public void setMd(MessageDigest md) {
        this.md = md;
    }

    /**
     * Generates a new random 32-bytes Salt.
     *
     * @return The BigNumber generated Salt.
     */
    public BigNumber generateS() {
        if (this.s == null) {
            this.s = new BigNumber().setRand(32);
        }

        return this.s;
    }

    /**
     * Generates a new Password Verifier based on known parameters as in SRP6
     * implementation.
     *
     * @return The BigNumber generated Verifier.
     */
    public BigNumber generateV() {
        if (this.v == null) {
            this.x = new BigNumber();

            this.x = new BigNumber();
            // Generate x - the Private key
            this.md.update(s.asByteArray(32));
            this.md.update(this.hashpass);
            this.x.setBinary(this.md.digest());            
            
            // Generate the verifier
            if (this.v == null) {
                this.v = this.g.modPow(this.x, this.N);
            }
        }

        return this.v;
    }

    /**
     * Performs the first step of the authentication challenge, generating the
     * server public value.
     */
    public void step1() {
        // Generate B - The server public value - (k.v + g^b)        
        this.gmod = this.g.modPow(this.b, this.N);
        this.B = (this.v.multiply(this.k).add(this.gmod)).remainder(this.N);
    }

    /**
     * Performs the second step of the authentication challenge, verifying that
     * the server knows the information the client has generated.
     *
     * @param A The A sent by the client.
     * @param M1 The proof sent by the client.
     * @return True if the server recognize the proof, false otherwise.
     */
    public boolean step2(BigNumber A, BigNumber M1) {
        // Generate u - the so called "Random scrambling parameter"
        // H(A | B)
        this.md.update(A.asByteArray(32));
        this.md.update(this.B.asByteArray(32));
               
        BigNumber u = new BigNumber();
        u.setBinary(this.md.digest());
        
        // Generate S - the Session key - (A.v^u)^b
        this.S = (A.multiply(this.v.modPow(u, this.N))).modPow(this.b, this.N);        
        
        // Generate vK - the hashed session key, hashed with H hash function        
        byte[] t = this.S.asByteArray(32);
        byte[] t1 = new byte[16];
        byte[] vK = new byte[40];

        for (int i = 0; i < 16; i++) {
            t1[i] = t[i * 2];
        }

        this.md.update(t1);

        byte[] digest = this.md.digest();
        for (int i = 0; i < 20; i++) {
            vK[i * 2] = digest[i];
        }

        for (int i = 0; i < 16; i++) {
            t1[i] = t[i * 2 + 1];
        }

        this.md.update(t1);
        digest = this.md.digest();
        for (int i = 0; i < 20; i++) {
            vK[i * 2 + 1] = digest[i];
        }

        // generating M - the server's SRP6 M value
        // Formula: H(H(N)^H(g),H(I),s,A,B,K)
        // H(N)
        this.md.update(this.N.asByteArray(32));
        byte[] hash = this.md.digest();

        // H(g)
        this.md.update(this.g.asByteArray(1));
        digest = this.md.digest();
        
        // H(N)^H(g)
        for (int i = 0; i < 20; i++) {
            hash[i] ^= digest[i];
        }

        this.md.update(I);
        byte[] t4 = this.md.digest();
        
        this.K = new BigNumber();
        this.K.setBinary(vK);                                
        
        BigNumber t3 = new BigNumber();
        t3.setBinary(hash);
        BigNumber t4_correct = new BigNumber();
        t4_correct.setBinary(t4);
                
        // All together
        this.md.update(t3.asByteArray());
        this.md.update(t4_correct.asByteArray());
        this.md.update(this.s.asByteArray());
        this.md.update(A.asByteArray());
        this.md.update(this.B.asByteArray());
        this.md.update(this.K.asByteArray());

        byte[] m = this.md.digest();
        this.M2 = new BigNumber();
        this.M2.setBinary(m, false);                                                
        
        return this.M2.equals(M1);
    }

    /**
     * Generates the hash logon proof from A and M1.
     *
     * @param A The Big Number A to be used.
     * @return The Hash Logon Proof.
     */
    public byte[] generateHashLogonProof(BigNumber A) {
        // Formula: H(A | M2 | K)       
        this.md.update(A.asByteArray(32));
        this.md.update(this.M2.asByteArray(20, false));
        this.md.update(this.K.asByteArray(40));       
        
        return this.md.digest();
    }

    @Override
    public String toString() {
        String toString = "";

        toString += "N: " + N.toHexString() + "\n";
        toString += "g: " + g.toHexString() + "\n";
        toString += "k: " + k.toHexString() + "\n";

        toString += "s: " + this.s.toHexString() + "\n";
        toString += "v: " + this.v.toHexString() + "\n";
        toString += "gmod: " + this.gmod.toHexString() + "\n";
        toString += "B: " + this.B.toHexString() + "\n";
        //toString += "x: "+this.x.toHexString()+"\n";
        toString += "hashpass: " + new BigNumber(this.hashpass).toHexString() + "\n";
        toString += "M2: " + this.M2.toHexString() + "\n";
        toString += "S: " + this.S.toHexString() + "\n";
        toString += "K: " + this.K.toHexString() + "\n";
        toString += "I: " + new BigNumber(this.I).toHexString() + "\n";

        return toString;
    }
}
