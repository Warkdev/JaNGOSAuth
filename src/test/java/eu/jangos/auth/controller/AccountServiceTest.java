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
import java.util.Date;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * AccountServiceTest is the Unit & Integration class for Account testing.
 * @author Warkdev
 */
public class AccountServiceTest {

    /**
     * Test of getAccount method, of class AccountService.
     */
    @Test
    public void testGetAccountOK() {
        // First we test to get an existing account.
        System.out.println("getAccountOK");
        String name = "test";
        AccountService instance = new AccountService();
        Account result = instance.getAccount(name);
        assertNotNull(result);
    }

    @Test
    public void testGetAccountKO() {
        // Then we test to get some non-existing accounts.  
        System.out.println("getAccountKO");
        AccountService instance = new AccountService();

        assertNull(instance.getAccount(""));
        assertNull(instance.getAccount("a"));
    }

    @Test
    public void testGetAccountBoundaries() {
        // Then we test with some wrong data.
        System.out.println("getAccountBoundaries");
        AccountService instance = new AccountService();

        assertNull(instance.getAccount(null));
        assertNull(instance.getAccount("/"));
        assertNull(instance.getAccount("%"));
        assertNull(instance.getAccount("%test%"));
        assertNull(instance.getAccount("?"));
        assertNull(instance.getAccount("*"));
        assertNull(instance.getAccount("+2"));
    }

    /**
     * Test of checkExistence method, of class AccountService.
     */
    @Test
    public void testCheckExistenceOK() {
        // First, we test with an existing account.
        System.out.println("checkExistenceOK");
        String name = "test";
        AccountService instance = new AccountService();
        boolean result = instance.checkExistence(name);
        assertTrue(result);
    }

    @Test
    public void testCheckExistenceKO() {
        System.out.println("checkExistenceKO");
        // Then, we test to get some non-existing accounts.
        AccountService instance = new AccountService();
        assertFalse(instance.checkExistence(""));
        assertFalse(instance.checkExistence("a"));
    }

    @Test
    public void testCheckExistenceBoundaries() {
        System.out.println("checkExistenceBoundaries");
        // Then we test with some wrong data.
        AccountService instance = new AccountService();
        assertFalse(instance.checkExistence(null));
        assertFalse(instance.checkExistence("/"));
        assertFalse(instance.checkExistence("%"));
        assertFalse(instance.checkExistence("%test%"));
        assertFalse(instance.checkExistence("?"));
        assertFalse(instance.checkExistence("*"));
        assertFalse(instance.checkExistence("+2"));
    }

    /**
     * Test of login method, of class AccountService.
     */
    @Test
    public void testLoginOK() {
        System.out.println("loginOK");
        String name = "test";
        AccountService as = new AccountService();
        Account account = as.getAccount(name);
        String ip = "127.0.0.1";
        String locale = "frFR";
        String session = "640067444B1823BA653F6141D2F7508D213F3A213D9ED6C0A469AAD3FBB584C45458D83CF796A369";
        AccountService instance = new AccountService();
        assertTrue(instance.login(account, ip, locale, session));
    }

    @Test
    public void testLoginAccountNull() {
        System.out.println("testLoginAccountNull");
        Account account = null;
        String ip = "127.0.0.1";
        String locale = "frFR";
        String session = "640067444B1823BA653F6141D2F7508D213F3A213D9ED6C0A469AAD3FBB584C45458D83CF796A369";
        AccountService instance = new AccountService();
        assertFalse(instance.login(account, ip, locale, session));
    }

    @Test
    public void testLoginAccountNotExisting() {
        System.out.println("testLoginAccountNotExisting");
        String name = "dont exist";
        Account account = new Account(name, "AAAAAAA", new Date(), "0.0.0.0", 0, false, false, new Date());
        String ip = "127.0.0.1";
        String locale = "frFR";
        String session = "640067444B1823BA653F6141D2F7508D213F3A213D9ED6C0A469AAD3FBB584C45458D83CF796A369";
        AccountService instance = new AccountService();
        assertFalse(instance.login(account, ip, locale, session));
    }

    @Test
    public void testLoginAccountLocked() {
        System.out.println("testLoginAccountLocked");
        String name = "locked";        
        String ip = "127.0.0.1";
        String locale = "frFR";
        String session = "640067444B1823BA653F6141D2F7508D213F3A213D9ED6C0A469AAD3FBB584C45458D83CF796A369";
        AccountService instance = new AccountService();
        Account account = instance.getAccount(name);
        assertFalse(instance.login(account, ip, locale, session));
    }
    
    @Test
    public void testLoginAccountBanned() {
        System.out.println("testLoginAccountBanned");
        String name = "banned";        
        String ip = "127.0.0.1";
        String locale = "frFR";
        String session = "640067444B1823BA653F6141D2F7508D213F3A213D9ED6C0A469AAD3FBB584C45458D83CF796A369";
        AccountService instance = new AccountService();
        Account account = instance.getAccount(name);
        assertFalse(instance.login(account, ip, locale, session));
    }
    
        @Test
    public void testLoginAccountOnline() {
        System.out.println("testLoginAccountOnline");
        String name = "test";        
        String ip = "127.0.0.1";
        String locale = "frFR";
        String session = "640067444B1823BA653F6141D2F7508D213F3A213D9ED6C0A469AAD3FBB584C45458D83CF796A369";
        AccountService instance = new AccountService();
        Account account = instance.getAccount(name);
        assertFalse(instance.login(account, ip, locale, session));
    }
    
    @Test
    public void testLoginInvalidIP() {
        System.out.println("testLoginInvalidIP");
        String name = "test";
        AccountService as = new AccountService();
        Account account = as.getAccount(name);
        String ip = "389.0.0.1";
        String locale = "frFR";
        String session = "640067444B1823BA653F6141D2F7508D213F3A213D9ED6C0A469AAD3FBB584C45458D83CF796A369";
        AccountService instance = new AccountService();
        assertFalse(instance.login(account, ip, locale, session));

        ip = "127.0.1";
        assertFalse(instance.login(account, ip, locale, session));

        ip = "";
        assertFalse(instance.login(account, ip, locale, session));

        ip = "azad.azda";
        assertFalse(instance.login(account, ip, locale, session));

        ip = null;
        assertFalse(instance.login(account, ip, locale, session));
    }

    @Test
    public void testLoginInvalidLocale() {
        System.out.println("testLoginInvalidLocale");
        String name = "test";
        AccountService as = new AccountService();
        Account account = as.getAccount(name);
        String ip = "127.0.0.1";
        String locale = null;
        String session = "640067444B1823BA653F6141D2F7508D213F3A213D9ED6C0A469AAD3FBB584C45458D83CF796A369";
        AccountService instance = new AccountService();
        // This test should return true since invalid locale are translated to a parameter in DB.
        assertTrue(instance.login(account, ip, locale, session));

        locale = "";
        assertTrue(instance.login(account, ip, locale, session));       
    }

    /**
     * Test of isBanned method, of class AccountService.
     */
    @Test
    public void testNotBanned() {
        System.out.println("testNotBanned");
        String name = "test";        
        String ip = "127.0.0.1";
        AccountService instance = new AccountService();
        Account account = instance.getAccount(name);                
        assertFalse(instance.isBanned(account, ip));        
    }
    
    @Test
    public void testIPBanned() {
        System.out.println("testIPBanned");
        String name = "test";        
        String ip = "153.124.12.1";
        AccountService instance = new AccountService();
        Account account = instance.getAccount(name);                
        assertTrue(instance.isBanned(account, ip));        
    }
    
    @Test
    public void testAccountBanned() {
        System.out.println("testAccountBanned");
        String name = "banned";        
        String ip = "127.0.0.1";
        AccountService instance = new AccountService();
        Account account = instance.getAccount(name);                
        assertTrue(instance.isBanned(account, ip));        
    }
    
        @Test
    public void testNullAccountBanned() {
        System.out.println("testNullAccountBanned");
        Account account = null;
        String ip = "127.0.0.1";        
        AccountService instance = new AccountService();
        assertTrue(instance.isBanned(account, ip));
    }

    @Test
    public void testInvalidAccountBanned() {
        System.out.println("testInvalidAccountBanned");
        String name = "dont exist";
        Account account = new Account(name, "AAAAAAA", new Date(), "0.0.0.0", 0, false, false, new Date());
        String ip = "127.0.0.1";        
        AccountService instance = new AccountService();
        assertTrue(instance.isBanned(account, ip));
    }
    
    @Test
    public void testInvalidIPBanned() {
        System.out.println("testInvalidIPBanned");
        String name = "test";        
        String ip = "389.0.0.1";
        AccountService instance = new AccountService();
        Account account = instance.getAccount(name);                
        assertTrue(instance.isBanned(account, ip));    
        
        ip = "127.0.1";
        assertTrue(instance.isBanned(account, ip));

        ip = "";
        assertTrue(instance.isBanned(account, ip));

        ip = "azad.azda";
        assertTrue(instance.isBanned(account, ip));

        ip = null;
        assertTrue(instance.isBanned(account, ip));
    }
        
    /**
     * Test of updateFailedAttempt method, of class AccountService.
     */
    @Test
    public void testUpdateFailedAttemptLocked() {
        System.out.println("updateFailedAttempt");
        String name = "test";       
        AccountService instance = new AccountService();
        Account account = instance.getAccount(name);
        ParameterService ps = new ParameterService();
        
        // We retrieve the maximum number of attempt before a locking
        int max = Integer.parseInt(ps.getParameter("maxFailedAttempt"));
        
        // First of all, we make sure that the account has no failed attempt.
        account.setFailedattempt(0);
        account.setLocked(false);
        instance.update(account);
        
        // Then, we update the counter.
        for(int i = 0; i<= max; i++)
        {
            instance.updateFailedAttempt(account);  
        }
          
        // Then, we validate that the account is locked.
        assertTrue(account.isLocked());
        
        // Finally, we set back the account to a normal situation.
        account.setFailedattempt(0);
        account.setLocked(false);
        instance.update(account);
    }    
}
