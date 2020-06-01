package packet.coder;
/*
    @project GameServer
    @author Ashime
    Created on 4/20/2020.

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
import packet.Category;
import packet.Protocol;
import packet.s2c.AnsEnterServer;
import packet.s2c.AnsUnknown2;

public class MessageDecoder implements Category, Protocol
{
    private static byte[] outPacket = {};

    public static byte[] decodeMessage(byte[] inPacket)
    {
        switch (inPacket[1])
        {
            case Protocol.C2S_askEnterServer:
            {
                Log.formatLog(inPacket, "[C2S] askEnterServer");
                outPacket = AnsEnterServer.createPacket();
                Log.formatLog(outPacket, "[S2C] ansEnterServer");
                break;
            }
            case Protocol.C2S_askUnknown1:
            {
                Log.formatLog(inPacket, "[C2S] askUnknown1");
                outPacket = AnsUnknown2.createPacket();
                Log.formatLog(outPacket, "[S2C] ansUnknown1");
                break;
            }
            default:
            {
                Log.formatLog(inPacket, "[C2S] Unknown Packet");
                break;
            }
        }

        return outPacket;
    }
}
