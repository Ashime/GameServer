package server.filters;

/*
    @project GameServer
    @author Ashime
    Created on 4/26/2020.

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

import sql.Query;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.ipfilter.AbstractRemoteAddressFilter;

import java.net.SocketAddress;

public class MacAddressFilter extends AbstractRemoteAddressFilter
{
    @Override
    protected boolean accept(ChannelHandlerContext channelHandlerContext, SocketAddress socketAddress) throws Exception
    {
        boolean isAccepted = true;

        // Netty uses the mac address to create the channel id, please see link (https://netty.io/4.1/api/io/netty/channel/ChannelId.html)
        String[] mac = channelHandlerContext.channel().id().asLongText().split("-");

        // Runs mac address through the list of banned mac addresses from database.
        for (String temp : Query.getMacAddresses())
        {
            // If mac address matches, the close the channel and return false.
            if (temp.matches(mac[0]))
            {
                isAccepted = false;
                channelHandlerContext.channel().close();
            }
        }
        return isAccepted;
    }
}
