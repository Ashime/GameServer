package server.coder;

/*
    @project GameServer
    @author Ashime
    Created on 05/22/2019.

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

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import security.Tea;
import utility.Convert;
import utility.Utility;


public class PacketDecoder extends ChannelInboundHandlerAdapter
{
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
    {
        byte[] inPacket = (byte[]) msg;
        decodePacket(ctx, inPacket);
    }

    private static void decodePacket(ChannelHandlerContext ctx, byte[] msg)
    {
        byte[] bSize;
        int size;
        byte[] message = msg;

        // Flip the size bytes around.
        message = Utility.flip(msg, 0, 1);

        // Split the size header off.
        bSize = Utility.split(message, 0, 2);

        // Convert size from byte array into int.
        size = Convert.byteArrayToInt(bSize);

        // Split message from size header
        message = Utility.split(message, 2, message.length);

        if (message.length == size)
            // Passes the message down the pipeline.
            ctx.fireChannelRead(message);
        else
            // Drops the packet.
            ctx.fireChannelReadComplete();
    }
}
