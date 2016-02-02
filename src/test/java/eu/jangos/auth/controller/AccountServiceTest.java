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
package eu.jangos.auth.controller;

import eu.jangos.auth.model.Account;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Talendrys
 */
public class AccountServiceTest {
    
    public AccountServiceTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

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
        System.out.println("checkExistence");
        String name = "test";
        AccountService instance = new AccountService();        
        boolean result = instance.checkExistence(name);
        assertTrue(result);                                        
    }

    @Test
    public void testCheckExistenceKO() {
        // Then, we test to get some non-existing accounts.
        AccountService instance = new AccountService(); 
        assertFalse(instance.checkExistence(""));
        assertFalse(instance.checkExistence("a"));
    }
    
    @Test
    public void testCheckExistenceBoundaries() {
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
    public void testLogin() {
        System.out.println("login");
        Account account = null;
        String ip = "";
        String locale = "";
        String session = "";
        AccountService instance = new AccountService();
        instance.login(account, ip, locale, session);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isBanned method, of class AccountService.
     */
    @Test
    public void testIsBanned() {
        System.out.println("isBanned");
        Account account = null;
        String ip = "";
        AccountService instance = new AccountService();
        boolean expResult = false;
        boolean result = instance.isBanned(account, ip);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateFailedAttempt method, of class AccountService.
     */
    @Test
    public void testUpdateFailedAttempt() {
        System.out.println("updateFailedAttempt");
        Account account = null;
        AccountService instance = new AccountService();
        instance.updateFailedAttempt(account);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of update method, of class AccountService.
     */
    @Test
    public void testUpdate() {
        System.out.println("update");
        Account account = null;
        AccountService instance = new AccountService();
        instance.update(account);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
