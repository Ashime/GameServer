package utility;

/*
    @project GameServer
    @author Ashime
    Created on 5/13/2019.

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

import java.nio.charset.StandardCharsets;

public class Convert
{
    /**
     * @param input - The byte[] that will be converted.
     * @return - Returns a string of hex values.
     */
    public static String byteArrayToHexString(byte[] input)
    {
        StringBuilder sb = new StringBuilder(input.length * 2);

        for (byte b : input)
            sb.append(String.format("%02x", b));

        return sb.toString();
    }

    public static byte[] hexStringToByteArray(String input)
    {
        int len = input.length();
        byte[] data = new byte[len / 2];

        for (int i = 0; i < len; i += 2)
        {
            data[i / 2] = (byte) ((Character.digit(input.charAt(i), 16) << 4)
                    + Character.digit(input.charAt(i + 1), 16));
        }

        return data;
    }

    /**
     * @param input - Byte[] that will be converted.
     * @return - Returns a string in the format of UTF-8.
     */
    public static String byteArrayToUTF8String(byte[] input)
    {
        return new String(input, StandardCharsets.UTF_8);
    }

    /**
     * @param input - Byte[] that will be converted.
     * @return - Returns an integer value.
     */
    public static int byteArrayToInt(byte[] input)
    {
        String hex = byteArrayToHexString(input);
        int output = Integer.parseInt(hex, 16);

        return output;
    }

    /**
     * @param input - 2 byte integer value.
     * @return - Returns a byte[].
     */
    public static byte[] intToByteArray(int input)
    {
        byte[] output = new byte[2];

        output[0] = (byte) (input >> 8);
        output[1] = (byte) (input);

        return output;
    }
}
