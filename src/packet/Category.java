package packet;
/*
    @project GameServer
    @author Ashime
    Created on 5/01/2020.

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

public interface Category
{
    byte unknown1 = 0x48; // CONNECTION - STAY CONN TO GS
    byte unknown2 = 0x7A; // CHAR_SELECT/CHAR_CREATE/CHARACTR
}

/*
    USERID: 87,00,B8,D4

    C2S: 48, 76     - YES 4-6 (UserID), 7-54 (Username)
    S2C: 7A, 5B     - YES
    C2S: 7A, 9F ^ -  0x87, 0x00, 0xB8, 0xD4 - YES
  * S2C: 7A, 85   - WAIT - YES
    >> ENCRYPTION STARTS <<
 */
