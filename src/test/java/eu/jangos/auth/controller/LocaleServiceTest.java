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
 * @author Warkdev.
 */
public class LocaleServiceTest {
    
    public LocaleServiceTest() {
    }

    /**
     * Test of getLocaleForString method, of class LocaleService.
     */
    @Test
    public void testGetLocaleForString() {
        System.out.println("getLocaleForString");
        String locale = "frFR";
        LocaleService instance = new LocaleService();        
        assertNotNull(instance.getLocaleForString(locale));
    }
    
    /**
     * Test of getLocaleForString method, of class LocaleService.
     */
    @Test
    public void testGetLocaleForUnknownString() {
        System.out.println("testGetLocaleForUnknownString");
        String locale = "testLocale";
        LocaleService instance = new LocaleService();        
        assertNotSame(instance.getLocaleForString(locale).getLocaleString(), locale);
    }
    
    /**
     * Test of getLocaleForString method, of class LocaleService.
     */
    @Test
    public void testGetLocaleForNullString() {
        System.out.println("testGetLocaleForNullString");
        String locale = null;
        LocaleService instance = new LocaleService();        
        assertNotNull(instance.getLocaleForString(locale));
    }
}
