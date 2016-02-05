package eu.jangos.auth;

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
import eu.jangos.auth.controller.ParameterService;
import eu.jangos.auth.controller.RealmService;
import eu.jangos.auth.model.Realm;
import eu.jangos.auth.network.handler.AuthServerHandler;
import eu.jangos.auth.network.decoder.AuthPacketDecoder;
import eu.jangos.auth.network.encoder.AuthPacketEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AuthServer is the main class of the authentication server for JaNGOS. This
 * class is responsible to startup the listener server.
 *
 * @author Warkdev
 * @version v0.1 BETA.
 */
public class AuthServer {
    private static final Logger logger = LoggerFactory.getLogger(AuthServer.class);

    static private ParameterService ps = new ParameterService();
    static private RealmService rs = new RealmService();
    
    static final boolean SSL = false;
    static final int PORT = Integer.parseInt(ps.getParameter("authPort"));
    static InetAddress HOST;
    static final String VERSION = ps.getParameter("authVersion");
    
    /**
     * Main of the AuthServer program.
     * @param args the command line arguments
     * @throws InterruptedException in case of interruption of the running process.
     */
    public static void main(String[] args) throws InterruptedException {
       
        logger.info("Starting JaNGOS authentication server version "+VERSION+".");
        logger.info("JaNGOS is an opensource project, check-out : https://github.com/Warkdev/JaNGOSAuth !");        
        
        logger.info("Loading realm definition...");
        
        for(Realm r : rs.getAllRealms()){
            logger.info("Realm "+r.getId()+" : "+r.getName());
        }
        
        // Configure the server.
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();            
            
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new AuthPacketDecoder(), new AuthPacketEncoder(), new AuthServerHandler());
                        }
                    });

            ChannelFuture f;

            // Start the server.   
            try {
                HOST = InetAddress.getByAddress(ps.getParameter("authAddress").getBytes());
                f = b.bind(HOST, PORT).sync();
                logger.info("JaNGOS authentication server started listening on "+HOST.getHostAddress()+":"+PORT);
            } catch (UnknownHostException ex) {
                f = b.bind(PORT);
                logger.info("JaNGOS authentication server started listening on port "+PORT);
            }

            // Wait until the server socket is closed.
            f.channel().closeFuture().sync();
        } finally {
            logger.info("JaNGOS authentication server shutting down.");
            // Shut down all event loops to terminate all threads.
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
