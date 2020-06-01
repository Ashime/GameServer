package server.handlers;

/*
    @project GameServer
    @author Ashime
    Created on 6/20/2020.

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

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import utility.Convert;

import java.net.SocketAddress;
import java.util.ArrayList;

/*
GENERAL INFORMATION:
This class is designed to handle all active sessions with currently
connected clients. Output: IP:Port,Key
*/

public class SessionHandler extends ChannelDuplexHandler
{
    private static final ArrayList<String> clientSession = new ArrayList<String>();
    private static String user;

    @Override
    public void channelActive(ChannelHandlerContext ctx)
    {
        addClient(ctx.channel().remoteAddress());
        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx)
    {
        String client = getUser(ctx.channel().remoteAddress());
        removeClient(client);
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
    {
        byte[] inPacket = (byte[]) msg;
        ctx.fireChannelRead(inPacket);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise)
    {
        if (!ctx.channel().isOpen() || !ctx.channel().isActive())
            ctx.fireChannelInactive();

        ctx.writeAndFlush(msg);
    }

    public void addClient(SocketAddress address)
    {
        user = address.toString();
        clientSession.add(user);
    }

    public void removeClient(String user)
    {
        clientSession.remove(user);
    }

    private String getUser(SocketAddress address)
    {
        String user = "";

        for (String temp : clientSession)
        {
            if (temp.contains(address.toString()))
            {
                user = temp;
                break;
            }
        }

        return user;
    }

    public static int getActiveUsers() {
        return clientSession.size();
    }
}
