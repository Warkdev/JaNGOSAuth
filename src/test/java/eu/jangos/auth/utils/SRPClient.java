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
 * SRPClient is an helper class to handle SRP6 calculation and hashing methods
 * for WoW client since it deviates from the original SRP6 specification..
 *
 * @author Warkdev
 * @version v0.1 BETA.
 */
public class SRPClient {

    /**
     * k is a constant used to generate other proofs. Equals to 3.
     */
    private static final BigNumber k = new BigNumber("3");

    /**
     * a is the client private value of 19 bytes, it is never shared and is only
     * used for one authentication.
     */
    private final BigNumber a = new BigNumber().setRand(19); // client private value              
    
    /**
     * A is the client public value.
     */
    private BigNumber A; // client public value        

    /**
     * x is the password verifier, never held in database, always calculated
     * from H(s | H(I | ":" | p)).
     */
    private BigNumber x; // Intermediate verifier           

    /**
     * M is the client proof to show the client that the server knows the
     * password.
     */
    private BigNumber M; // Server M calculation

    /**
     * S is the session key, unencrypted.
     */
    private BigNumber S; // Session Key

    /**
     * K is the hashed session key.
     */
    private BigNumber K; // Hashed Session Key

    // Cryptographic digest.
    private MessageDigest md;

    /**
     * Constructor of SRPServer
     *
     */
    public SRPClient() {
        try {
            md = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("No such algorithm available");
        }       
    }

    public static BigNumber getK() {
        return k;
    }

    public BigNumber getSessionKey() {
        return K;
    }

    public BigNumber getX() {
        return x;
    }

    public void setX(BigNumber x) {
        this.x = x;
    }

    public void setS(BigNumber S) {
        this.S = S;
    }

    public void setK(BigNumber K) {
        this.K = K;
    }

    public BigNumber getA() {
        return A;
    }

    public void setA(BigNumber A) {
        this.A = A;
    }

    public BigNumber getM() {
        return M;
    }

    public void setM(BigNumber M) {
        this.M = M;
    }

    public MessageDigest getMd() {
        return md;
    }

    public void setMd(MessageDigest md) {
        this.md = md;
    }   

    /**
     * Performs the first step of the authentication challenge, verifying that
     * the client knows the information the server has generated.
     *
     * @param account The name of the account.
     * @param password The password for this account.
     * @param B The B value sent by the server.
     * @param g The g valie sent by the server.
     * @param N The N value sent by the server.
     * @param s The salt value sent by the server.
     * @return The M generated value.
     */
    public BigNumber step1(String account, String password, BigNumber B, BigNumber g, BigNumber N, BigNumber s) {
        // Generate A the client public key.
        this.A = g.modPow(a, N);
        
        // Generate u - the so called "Random scrambling parameter"                
        this.md.update(this.A.asByteArray(32));
        this.md.update(B.asByteArray(32));

        BigNumber u = new BigNumber();
        u.setBinary(this.md.digest());

        // Generate x - H(S, H(I:P))
        // Generate H(I:P)                       
        String user = account.toUpperCase() + ":" + password.toUpperCase();        
        this.md.update(user.getBytes());
        byte[] p = this.md.digest();                                      
        
        // Generate H(S, p)
        this.md.update(s.asByteArray(32));
        this.md.update(p);        
        BigNumber x = new BigNumber();
        x.setBinary(this.md.digest());        
        
        // Generate S - the Session key
        this.S = new BigNumber();
        this.S = (B.remainder(k.multiply(g.modPow(x, N)))).modPow((a.add(u.multiply(x))), N);        

        // Generate vK - the hashed session key, hashed with H hash function
        byte[] t = this.S.asByteArray(32);
        byte[] t1 = new byte[16];
        byte[] t2 = new byte[16];
        byte[] vK = new byte[40];

        for (int i = 0; i < 16; i++) {
            t1[i] = t[i * 2];
            t2[i] = t[i * 2 + 1];
        }

        this.md.update(t1);

        byte[] digest = this.md.digest();
        for (int i = 0; i < 20; i++) {
            vK[i * 2] = digest[i];
        }

        this.md.update(t2);
        digest = this.md.digest();
        for (int i = 0; i < 20; i++) {
            vK[i * 2 + 1] = digest[i];
        }

        // generating M - the server's SRP6 M value
        // Formula: H(H(N)^H(g),H(I),s,A,B,K)
        // H(N)
        this.md.update(N.asByteArray(32));
        byte[] hash = this.md.digest();

        // H(g)
        this.md.update(g.asByteArray(1));
        digest = this.md.digest();
        
        // H(N)^H(g)
        for (int i = 0; i < 20; i++) {
            hash[i] ^= digest[i];
        }

        // H(I)
        System.out.println(account.getBytes().length);
        this.md.update(account.getBytes());        
        byte[] t4 = new byte[20];
        t4 = this.md.digest();

        this.K = new BigNumber();
        this.K.setBinary(vK);
        BigNumber t3 = new BigNumber();
        t3.setBinary(hash);
        BigNumber t4_correct = new BigNumber();
        t4_correct.setBinary(t4);
        
        this.md.update(t3.asByteArray());
        this.md.update(t4_correct.asByteArray());
        this.md.update(s.asByteArray());
        this.md.update(this.A.asByteArray());
        this.md.update(B.asByteArray());
        this.md.update(this.K.asByteArray());

        byte[] m = this.md.digest();
        this.M = new BigNumber();
        this.M.setBinary(m, false);
        
        return this.M;
    }

    @Override
    public String toString() {
        String toString = "";

        toString += "k: " + k.toHexString() + "\n";
                        
        toString += "A: " + this.A.toHexString() + "\n";
        //toString += "x: "+this.x.toHexString()+"\n";
        toString += "M: " + this.M.toHexString() + "\n";
        toString += "S: " + this.S.toHexString() + "\n";
        toString += "K: " + this.K.toHexString() + "\n";        

        return toString;
    }
}

