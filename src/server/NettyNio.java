package server;

/*
    @project GameServer
    @author Ashime
    Created on 2/1/2018.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

import file.io.IniFile;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.ipfilter.UniqueIpFilter;
import io.netty.handler.timeout.IdleStateHandler;
import server.coder.PacketDecoder;
import server.filters.MacAddressFilter;
import server.handlers.PacketHandler;
import server.handlers.SessionHandler;
import ui.Console;

import java.util.logging.Level;
import java.util.logging.Logger;

/*
GENERAL INFORMATION:
1. NettyNio.io
    This 3rd library is being used to improve performance by using less resources,
    and better throughput to lower latency.
    >> Website Link:        https://netty.io/
    >> Documentation Link:  https://netty.io/wiki/index.html
    >> Download Link:       https://netty.io/downloads.html

TODO:
1. When client does RST packets, the server produces errors. It is either due to child options or
the PacketHandler needs a method to handle the exception.
2. Commenting
*/

public class NettyNio
{
    /*==============================
                 Client
    ===============================*/
    private  static EventLoopGroup clientAcceptor;
    private static EventLoopGroup clientWorker;
    private static ServerBootstrap clientBoot;
    private static ChannelFuture clientChannel;

    private final int clientAcceptionThreads = IniFile.getClientAcceptingThreads();
    private final int clientWorkingThreads = IniFile.getClientWorkingThreads();

    private String connectionIP = IniFile.getConnectionIP();
    private  int connectionPort = IniFile.getConnectionPort();

    private final boolean uniqueIpFilter = IniFile.isUniqueIpFilter();
    private final int ping = IniFile.getPing();
    private final int disconnect = IniFile.getDisconnect();

    /*=============================
                Server
    ==============================*/
    private static EventLoopGroup serverAcceptor;
    private static EventLoopGroup serverWorker;
    private static ServerBootstrap serverBoot;
    private static ChannelFuture serverChannel;

    private int serverAcceptionThreads = IniFile.getServerAcceptingThreads();
    private int serverWorkingThreads = IniFile.getServerWorkingThreads();

    private final String serverIP = IniFile.getServerIP();
    private final  int serverPort = IniFile.getServerPort();

    public void start()
    {
        try
        {
            /*==================================================================
                                    CLIENT TO SERVER
            ===================================================================*/
            clientAcceptor = new NioEventLoopGroup(clientAcceptionThreads);
            clientWorker = new NioEventLoopGroup(clientWorkingThreads);

            clientBoot = new ServerBootstrap();
            clientBoot.group(clientAcceptor, clientWorker).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer()
            {
                @Override
                protected void initChannel(Channel ch)
                {
                    // TODO: IMPORTANT: Need some type of firewall or ip checker to block clients from connecting.
                    ch.pipeline().addLast("macAddressFilter", new MacAddressFilter());

                    // UniqueIpFilter only allows one IP per channel, so a client cannot connect more than once.
                    if (uniqueIpFilter) { ch.pipeline().addLast(new UniqueIpFilter()); }

                    // Inactivity Handler. Ping the client every # seconds of inactivity and DC client after # seconds Check the Common.ini.
                    ch.pipeline().addLast("idleStateHandler", new IdleStateHandler(disconnect, ping, 0));

                    // Encoder/Decoder for converting ByteBuf into byte[] and vise versa.
                    ch.pipeline().addLast("byteDecoder", new ByteArrayDecoder());
                    ch.pipeline().addLast("byteEncoder", new ByteArrayEncoder());

                    // Session Handler
                    ch.pipeline().addLast("sessionHandler", new SessionHandler());

                    // PacketDecoder checks, split, and passes packets down.
                    ch.pipeline().addLast("packetDecoder", new PacketDecoder());
                    // PacketEncoder checks, adds, and pushes packets up.
//                    ch.pipeline().addLast("packetEncoder", new PacketEncoder());

                    // Packet Handler will check and pass the packets their corresponding classes.
                    ch.pipeline().addLast("packetHandler", new PacketHandler());
                }
            }).childOption(ChannelOption.TCP_NODELAY, true).childOption(ChannelOption.AUTO_READ, true);

            clientChannel = clientBoot.bind(connectionIP, connectionPort).sync();
            Console.displayMessage("INFO", "Client connection address - " + connectionIP + ":" + connectionPort);
            
            /*==================================================================
                                    SERVER TO SERVER
            ===================================================================*/
            serverAcceptor = new NioEventLoopGroup(serverAcceptionThreads);
            serverWorker = new NioEventLoopGroup(serverWorkingThreads);

            serverBoot = new ServerBootstrap();
            serverBoot.group(serverAcceptor, serverWorker).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer()
            {
                @Override
                protected void initChannel(Channel ch)
                {
                    // TODO: Need to add ip filter to only accept ip connects. Basically a WhiteList.
                    //ch.pipeline().addFirst("serverFirewall", new RuleBasedIpFilter(new ServerFirewall()));

                    // Not given a choice on whether to enable it. For security reasons, the Server To Server address will only
                    // accept one connection per IP.
                    ch.pipeline().addLast(new UniqueIpFilter());

                    // Frame Decoder to break incoming continuous packets up by frames so further decoders can separate
                    // each message accordingly.
                    ch.pipeline().addLast("frameDecoder", new DelimiterBasedFrameDecoder(1024, Delimiters.nulDelimiter()));

                    // Server to Server will be doing message to message packets.
                    ch.pipeline().addLast("stringDecoder", new StringDecoder());
                    ch.pipeline().addLast("stringEncoder", new StringEncoder());

                    // TODO: Add Handler.
                }
            });

            serverChannel = serverBoot.bind(serverIP, serverPort).sync();
            Console.displayMessage("INFO", "Server connection address - " + serverIP + ":" + serverPort);
        }
        catch (InterruptedException ex) {
            Logger.getLogger(NettyNio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void stop()
    {
        if (clientChannel != null)
            clientChannel.channel().closeFuture();

        if (serverChannel != null)
            serverChannel.channel().closeFuture();

        if (clientAcceptor != null)
            clientAcceptor.shutdownGracefully();

        if (serverAcceptor != null)
            serverAcceptor.shutdownGracefully();

        if (clientWorker != null)
            clientWorker.shutdownGracefully();

        if (serverWorker != null)
            serverWorker.shutdownGracefully();

        Console.displayMessage("INFO", "Server has successfully shutdown!");
    }
}
