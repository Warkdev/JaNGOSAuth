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
package eu.jangos.auth.controller;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Warkdev
 */
public class BannedIPServiceTest {
    
    public BannedIPServiceTest() {
    }

    /**
     * Test of isIPBanned method, of class BannedIPService.
     */
    @Test
    public void testIsIPBannedOK() {
        System.out.println("testIsIPBannedOK");
        String ip = "153.124.12.1";
        BannedIPService instance = new BannedIPService();                
        assertTrue(instance.isIPBanned(ip));
    }
    
    /**
     * Test of isIPBanned method, of class BannedIPService.
     */
    @Test
    public void testIsIPBannedKO() {
        System.out.println("testIsIPBannedKO");
        String ip = "127.0.0.1";
        BannedIPService instance = new BannedIPService();
        assertFalse(instance.isIPBanned(ip));
    }
    
    /**
     * Test of isIPBanned method, of class BannedIPService.
     */
    @Test
    public void testIsIPBannedBoundaries() {
        System.out.println("testIsIPBannedKO");
        String ip = "389.0.0.1";
        BannedIPService instance = new BannedIPService();
        assertTrue(instance.isIPBanned(ip));
        
        ip = "127.0.1";
        assertTrue(instance.isIPBanned(ip));
        
        ip = "";
        assertTrue(instance.isIPBanned(ip));
        
        ip = "azdadsqd.adzza";
        assertTrue(instance.isIPBanned(ip));
        
        ip = null;
        assertTrue(instance.isIPBanned(ip));
    }
}
