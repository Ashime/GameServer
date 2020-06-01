package main;

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

import file.io.IniFile;
import security.Tea;
import server.NettyNio;
import sql.Connect;
import sql.Query;
import ui.Console;
import utility.Convert;

/*
GENERAL INFORMATION:
1. Operating Systems Supported
    a. Windows
        This application fully supports Windows 10.
*/

public class GameServer
{
    public static void main(String[] args)
    {
        // Setup for console interface. Allows access to display messages on the console.
        Console console = new Console();
        console.initConsole();  // Initializes the interface.

        // Initializes and pulls all the data from Common.ini and GameServer.ini.
        IniFile ini = new IniFile();
        ini.parseIni();

        Console.displayMessage("MSG", "==================================================================");
        Console.displayMessage("MSG", "                     SUN Online Game Server");
        Console.displayMessage("MSG", "                     Client Version: " + IniFile.getVersion());
        Console.displayMessage("MSG", "==================================================================");

        Connect connect = new Connect();
        connect.testConnection();   // Tests database connection.

        Query query = new Query();
        query.getStoredMacAddresses();

        NettyNio server = new NettyNio();
        server.start();            // Starts up the C2S and S2S logic and business.
    }
}
