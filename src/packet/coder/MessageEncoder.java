package packet.coder;
/*
    @project GameServer
    @author Ashime
    Created on 4/30/2020.

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

import utility.Convert;
import utility.Utility;

public class MessageEncoder
{
    /**
     * @param category
     * @param protocol
     * @param message
     * @return
     */
    public static byte[] createShortPacket(byte category, byte protocol, byte message)
    {
        byte[] output = new byte[3];

        output[0] = category;
        output[1] = protocol;
        output[2] = message;

        return encodeMessage(output);
    }

    /**
     * @param category Packet category
     * @param protocol Packet protocol
     * @param message  Packet message
     * @return
     */
    public static byte[] createLongPacket(byte category, byte protocol, byte[] message)
    {
        byte[] output = new byte[message.length + 2]; // +2 for category and protocol.

        output[0] = category;
        output[1] = protocol;

        System.arraycopy(message,0,output,2, message.length);

        return encodeMessage(output);
    }

    public static byte[] encodeMessage(byte[] msg)
    {
        int size;
        byte[] bSize;
        byte[] outPacket;

        // Get size of the message.
        size = msg.length;

        // Convert size from int into byte array.
        bSize = Convert.intToByteArray(size);

        // create a destination array that is the size of the two arrays
        outPacket = new byte[bSize.length + msg.length];

        // copy ciphertext into start of destination (from pos 0, copy ciphertext.length bytes)
        System.arraycopy(bSize, 0, outPacket, 0, bSize.length);

        // copy mac into end of destination (from pos ciphertext.length, copy mac.length bytes)
        System.arraycopy(msg, 0, outPacket, bSize.length, msg.length);

        // Flip the first two bytes.
        outPacket = Utility.flip(outPacket, 0, 1);

        return outPacket;
    }
}
