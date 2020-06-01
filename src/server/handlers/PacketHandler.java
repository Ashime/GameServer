package server.handlers;

/*
    @project GameServer
    @author Ashime
    Created on 6/22/2019.

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

import file.Log;
import file.io.IniFile;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import packet.Category;
import packet.Protocol;
import packet.s2c.Ans48D5;
import packet.s2c.AnsEnterServer;
import packet.s2c.AnsUnknown2;
import ui.Console;

// TODO: Commenting

public class PacketHandler extends ChannelDuplexHandler
{
    private static byte[] outPacket;
    private final int bufferSize = IniFile.getBufferSize();

    private ByteBuf buffer = null;

    @Override
    public void channelActive(ChannelHandlerContext ctx)
    {
        buffer = ctx.alloc().buffer(bufferSize);
    }
/*
    C2S: 48, 76     - YES 4-6 (UserID), 7-54 (Username)
    S2C: 7A, 5B     - YES
    C2S: 7A, 9F ^ -  0x87, 0x00, 0xB8, 0xD4 - YES
    S2C: 7A, 85   - WAIT - YES
    >> ENCRYPTION STARTS <<
*/
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
    {
        byte[] inPacket = (byte[]) msg;

        if(inPacket[0] == Category.unknown1)
        {
            if(inPacket[1] == Protocol.C2S_askEnterServer)
            {
                Log.formatLog(inPacket, "[C2S] AskEnterServer");
                outPacket = AnsEnterServer.createPacket();
                Log.formatLog(outPacket, "[S2C] AnsEnterServer");
            }
            else if(inPacket[1] == Protocol.C2S_askUnknownA)
            {
                Log.formatLog(inPacket, "[C2S] AskUnknownA");
                outPacket = Ans48D5.createPacket();
                Log.formatLog(outPacket, "[S2C] AnsUnknownA");
            }
            else
            {
                Log.formatLog(inPacket, "Unknown protocol!");
                ctx.fireChannelReadComplete();
            }
        }
        else if(inPacket[0] == Category.unknown2)
        {
            if(inPacket[1] == Protocol.C2S_askUnknown1)
            {
                Log.formatLog(inPacket, "[C2S] AskUnknown2");
                outPacket = AnsUnknown2.createPacket();
                Log.formatLog(outPacket, "[S2C] AnsUnknown2");
            }
            else
            {
                Log.formatLog(inPacket, "Unknown protocol!");
                ctx.fireChannelReadComplete();
            }
        }
        else
        {
            Log.formatLog(inPacket, "Unknown category!");
            ctx.fireChannelReadComplete();
        }


//        switch(inPacket[0])
//        {
//            case Category.unknown1:
//            {
//                outPacket = MessageDecoder.decodeMessage(inPacket);
//                ctx.write(outPacket);
//                break;
//            }
//            case Category.unknown2:
//            {
//                outPacket = MessageDecoder.decodeMessage(inPacket);
//                ctx.write(outPacket);
//                break;
//            }
//            default:
//            {
//                Log.formatLog(inPacket, "Unknown category!");
//                ctx.fireChannelReadComplete();
//                break;
//            }
//        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        Console.displayMessage("ERR", cause.toString());
        cause.printStackTrace();

        ctx.flush();
        ctx.close();
    }
}
