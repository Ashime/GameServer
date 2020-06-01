package packet.handlers;
/*
    @project GameServer
    @author Ashime
    Created on 5/17/2020.

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
import utility.Convert;
import utility.Utility;

import java.util.Arrays;

public class GetCharacterList
{
    private static Query query;
    private static byte[] username = new byte[50];
    private static byte[] shortUsername = {};

    public static void getList(byte[] inPacket)
    {
        username = Arrays.copyOfRange(inPacket, 10, 60);
        shortUsername = Arrays.copyOfRange(username, 0, Utility.indexOf(username, "0"));
        query.getCharacterList(Convert.byteArrayToUTF8String(shortUsername));

        query.getQueryResults();
    }
}
