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

import eu.jangos.auth.model.Account;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Warkdev.
 */
public class BannedAccountServiceTest {
    
    public BannedAccountServiceTest() {
    }

    /**
     * Test of isAccountBanned method, of class BannedAccountService.
     */
    @Test
    public void testIsAccountBannedOK() {
        System.out.println("isAccountBannedOK");
        // We know this name exist in our test data.
        AccountService as = new AccountService();
        Account account = as.getAccount("test");
        BannedAccountService instance = new BannedAccountService();                
        assertFalse(instance.isAccountBanned(account));
    }
    
    /**
     * Test of isAccountBanned method, of class BannedAccountService.
     */
    @Test
    public void testIsAccountBannedKO() {
        System.out.println("isAccountBannedKO");
        // We know this name exist in our test data.
        AccountService as = new AccountService();
        Account account = as.getAccount("banned");
        BannedAccountService instance = new BannedAccountService();                
        assertTrue(instance.isAccountBanned(account));
    }
    
    /**
     * Test of isAccountBanned method, of class BannedAccountService.
     */
    @Test
    public void testIsAccountBannedBoundaries() {
        System.out.println("testIsAccountBannedBoundaries");
        // We know this name exist in our test data.        
        Account account = null;
        BannedAccountService instance = new BannedAccountService();                
        assertTrue(instance.isAccountBanned(account));
    }    
}
