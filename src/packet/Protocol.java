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

public interface Protocol
{
    byte C2S_askEnterServer        = 0x76;
    byte S2C_ansEnterServer        = 0x5B;
    byte C2S_askUnknown1        = (byte) 0x9F;
    byte S2C_ansUnknown1        = (byte) 0x85;
    byte C2S_askUnknownA        = (byte) 0xD5; // Loops until server gives data (unknown).
}
