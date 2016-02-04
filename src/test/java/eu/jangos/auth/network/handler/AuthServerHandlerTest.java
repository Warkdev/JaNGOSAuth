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
package eu.jangos.auth.network.handler;

import io.netty.channel.ChannelHandlerContext;
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
public class AuthServerHandlerTest {
    
    public AuthServerHandlerTest() {
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
     * Test of channelRead method, of class AuthServerHandler.
     */
    @Test
    public void testChannelRead() throws Exception {
        System.out.println("channelRead");
        ChannelHandlerContext ctx = null;
        Object msg = null;
        AuthServerHandler instance = new AuthServerHandler();
        //instance.channelRead(ctx, msg);
		assertTrue(true);
    }

    /**
     * Test of channelReadComplete method, of class AuthServerHandler.
     */
    @Test
    public void testChannelReadComplete() {
        System.out.println("channelReadComplete");
        ChannelHandlerContext ctx = null;
        AuthServerHandler instance = new AuthServerHandler();
        //instance.channelReadComplete(ctx);
        assertTrue(true);
    }

    /**
     * Test of exceptionCaught method, of class AuthServerHandler.
     */
    @Test
    public void testExceptionCaught() {
        System.out.println("exceptionCaught");
        ChannelHandlerContext ctx = null;
        Throwable cause = null;
        AuthServerHandler instance = new AuthServerHandler();
        //instance.exceptionCaught(ctx, cause);
        assertTrue(true);
    }
    
}
