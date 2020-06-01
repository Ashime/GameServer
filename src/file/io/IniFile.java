package file.io;

/*
    @project GameServer
    @author Ashime
    Created on 07/10/2017.

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

import org.ini4j.Wini;
import ui.Console;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
GENERAL INFORMATION:
1. ini4j
    This is a 3rd Party Library being used to read, write, and etc. with ini files.
    >> Download Link: http://ini4j.sourceforge.net/
    >> Basic Tutorials: http://ini4j.sourceforge.net/tutorial/index.html
    >> Javadocs: http://ini4j.sourceforge.net/apidocs/index.html
*/

public class IniFile
{
    private static String dateFormat;               // Date format preference. See Common.ini.
    private static String timeFormat;               // Time format preference. See Common.ini.
    private static String version;                  // Build version for the client.
    private static String protocol;                 // Protocol version for the client.
    private static int ping;                        // How often the server pings the client due to inactivity.
    private static int disconnect;                  // How long until the server disconnects the client due to inactivity.
    private static boolean uniqueIpFilter;          // Enable or disable OneIpFilterHandler.
    private static int timeOut;                     // How long a query can wait before timing out (in seconds).

    private static int bufferSize;                  // Buffer size for ByteBuf in NettyNio.
    private static String logFolder;                // Directory location where logs should be placed.
    private static boolean trustedDevices;          // Determines whether trusted devices will be enabled.
    private static String hmacKey;                  // HMAC SHA-512 key.

    private static int clientAcceptingThreads;      // Number of threads dedicated towards accepting clients.
    private static int clientWorkingThreads;        // Number of threads dedicated towards handling all of the processing for the clients.
    private static int serverAcceptingThreads;      // Number of threads dedicated towards accepting servers.
    private static int serverWorkingThreads;        // Number of threads dedicated towards handling all of the processing for other servers.

    private static String connectionIP;             // Client connection IP.
    private static int connectionPort;              // Client outside connection Port.

    private static String serverIP;                 // Server-side connection IP.
    private static int serverPort;                  // Server-side connection Port.

    private static String sqlAddr;                  // SQL IP Address.
    private static int sqlPort;                     // SQL Port number.
    private static String sqlDBN;                   // SQL Database Name.
    private static String sqlUser;                  // SQL Username for login.
    private static String sqlPass;                  // SQL Password for login.

    // Calls all methods to pull the data needed.
    public void parseIni()
    {
        commonServerInfo();
        gameServerInfo();
    }

    // Parses all sections in the Common.ini.
    private static void commonServerInfo()
    {
        try
        {
            // Opens the file for parsing.
            Wini ini = new Wini(new File("Common.ini"));

            // [COMMON] section.
            dateFormat = ini.get("COMMON", "DATE", String.class);
            timeFormat = ini.get("COMMON", "TIME", String.class);

            // [VERSION] section.
            version = ini.get("VERSION", "VERSION", String.class);
            protocol = ini.get("VERSION", "PROTOCOL", String.class);

            // [NETWORK] section.
            ping = ini.get("NETWORK", "PING", int.class);
            disconnect = ini.get("NETWORK", "DISCONNECT", int.class);

            // [SECURITY] section
            uniqueIpFilter = ini.get("SECURITY", "UNIQUE_IP_FILTER", boolean.class);

            // [SQL QUERY] section.
            timeOut = ini.get("SQL QUERY", "TIME_OUT", int.class);

            ini.clear();
        } catch (IOException ex) {
            Logger.getLogger(IniFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Parses all sections in the GameServer.ini.
    private static void gameServerInfo()
    {
        try
        {
            // Opens the file for parsing.
            Wini ini = new Wini(new File("GameServer.ini"));

            // [COMMON]
            bufferSize = ini.get("COMMON", "BUFFER_SIZE", int.class);
            logFolder = ini.get("COMMON", "LOG_FOLDER", String.class).replace("\\", "/");

            // [CLIENT] section.
            connectionIP = ini.get("CLIENT", "CONNECTION_IP", String.class);
            connectionPort = ini.get("CLIENT", "CONNECTION_PORT", int.class);
            clientAcceptingThreads = ini.get("CLIENT", "ACCEPTING_THREADS", int.class);
            clientWorkingThreads = ini.get("CLIENT", "WORKING_THREADS", int.class);

            // [SERVER] section.
            serverIP = ini.get("SERVER", "IP", String.class);
            serverPort = ini.get("SERVER", "PORT", int.class);
            serverAcceptingThreads = ini.get("SERVER", "ACCEPTING_THREADS", int.class);
            serverWorkingThreads = ini.get("SERVER", "WORKING_THREADS", int.class);

            // [SQL SERVER] section.
            sqlAddr = ini.get("SQL SERVER", "IP", String.class);
            sqlPort = ini.get("SQL SERVER", "PORT", int.class);
            sqlDBN = ini.get("SQL SERVER", "DATABASE", String.class);
            sqlUser = ini.get("SQL SERVER", "USERNAME", String.class);
            sqlPass = ini.get("SQL SERVER", "PASSWORD", String.class);

            checkThreads();
            ini.clear();
        } catch (IOException ex) {
            Logger.getLogger(IniFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Checks if threads are larger than 0, if not then set them to 1.
    private static void checkThreads()
    {
        if (clientAcceptingThreads <= 0)
        {
            clientAcceptingThreads = 1;
            Console.displayMessage("WARN", "Client's accepting threads was less than or equal to 0.");
            Console.displayMessage("WARN", "Threads has been set to 1. See GameServer.ini");
        }
        else if (clientWorkingThreads <= 0)
        {
            clientWorkingThreads = 1;
            Console.displayMessage("WARN", "Client's working threads was less than or equal to 0.");
            Console.displayMessage("WARN", "Threads has been set to 1. See GameServer.ini");
        }

        if (serverAcceptingThreads <= 0)
        {
            serverAcceptingThreads = 1;
            Console.displayMessage("WARN", "Server's accepting threads was less than or equal to 0.");
            Console.displayMessage("WARN", "Threads has been set to 1. See GameServer.ini");
        }
        else if (serverWorkingThreads <= 0)
        {
            serverWorkingThreads = 1;
            Console.displayMessage("WARN", "Server working threads was less than or equal to 0.");
            Console.displayMessage("WARN", "Threads has been set to 1. See GameServer.ini");
        }
    }
    
    /*=======================
            Getters
    =========================*/
    public static String getDateFormat() {
        return dateFormat;
    }

    public static String getTimeFormat() {
        return timeFormat;
    }

    public static String getVersion() {
        return version;
    }

    public static String getProtocol() {
        return protocol;
    }

    public static int getPing() {
        return ping;
    }

    public static int getDisconnect() {
        return disconnect;
    }

    public static boolean isUniqueIpFilter() {
        return uniqueIpFilter;
    }

    public static int getTimeOut() {
        return timeOut;
    }

    public static int getBufferSize() {
        return bufferSize;
    }

    public static String getLogFolder() {
        return logFolder;
    }

    public static boolean isTrustedDevices() {
        return trustedDevices;
    }

    public static String getHmacKey() {
        return hmacKey;
    }

    public static String getConnectionIP() {
        return connectionIP;
    }

    public static int getConnectionPort() {
        return connectionPort;
    }

    public static int getClientAcceptingThreads() {
        return clientAcceptingThreads;
    }

    public static int getClientWorkingThreads() {
        return clientWorkingThreads;
    }

    public static String getServerIP() {
        return serverIP;
    }

    public static int getServerPort() {
        return serverPort;
    }

    public static int getServerAcceptingThreads() {
        return serverAcceptingThreads;
    }

    public static int getServerWorkingThreads() {
        return serverWorkingThreads;
    }

    public static String getSqlAddr() {
        return sqlAddr;
    }

    public static Integer getSqlPort() {
        return sqlPort;
    }

    public static String getSqlDBN() {
        return sqlDBN;
    }

    public static String getSqlUser() {
        return sqlUser;
    }

    public static String getSqlPass() {
        return sqlPass;
    }

}
