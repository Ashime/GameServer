package file;

/*
    @project GameServer
    @author Ashime
    Created on 07/12/2017.

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
import ui.Console;
import utility.Convert;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;

// TODO: Commenting

public class Log
{
    private static String fileName;

    public static void logInfo(String type, String message)
    {
        checkDirectory();

        switch (type)
        {
            case "m": // message
            {
                fileName = IniFile.getLogFolder() + "/Console/Game Server/GameServer " + formatDate() + ".txt";
                write(fileName, message);
                break;
            }
            case "p": // packet
            {
                fileName = IniFile.getLogFolder() + "/Packet/Game Server/GamePackets " + formatDate() + ".txt";
                write(fileName, message);
                break;
            }
            default:
            {
                Console.displayMessage("WARN", "Unknown message type: " + type);
                fileName = IniFile.getLogFolder() + "/Error/Game Server/GameServer " + formatDate() + ".txt";
                break;
            }
        }
    }

    private static void write(String fileName, String message)
    {
        File file = new File(fileName);
        BufferedWriter write = null;
        FileWriter fileStream;

        // If file is not in the specified location then create a new one.
        if (!file.exists() || !file.isDirectory())
        {
            try
            {
                fileStream = new FileWriter(file, true);
                write = new BufferedWriter(fileStream);
                write.write(message);
                write.newLine();

            } catch (IOException ex) {
                Logger.getLogger(Log.class.getName()).log(Level.SEVERE, null, ex);
            }
            finally
            {
                if (write != null)
                {
                    try { write.close(); }
                    catch (IOException ex) {
                        Logger.getLogger(Log.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        else
        {
            try
            {
                PrintWriter writer = new PrintWriter(file, "UTF-16");
                writer.println(message);
                writer.close();
            } catch (FileNotFoundException | UnsupportedEncodingException ex) {
                Logger.getLogger(Log.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private static void checkDirectory()
    {
        File consoleDirectory = new File(IniFile.getLogFolder() + "/Console/Game Server");
        File packetDirectory = new File(IniFile.getLogFolder() + "/Packet/Game Server");

        if (!consoleDirectory.exists())
            consoleDirectory.mkdirs();
        else if (!packetDirectory.exists())
            packetDirectory.mkdirs();
    }

    public static String formatDate() {
        return new SimpleDateFormat(IniFile.getDateFormat()).format(new GregorianCalendar().getTime());
    }

    public static String formatTime() {
        return new SimpleDateFormat(IniFile.getTimeFormat()).format(new GregorianCalendar().getTime());
    }

    public static void formatLog(byte[] inPacket, String packetType)
    {
        logString(packetType);

        String data = new String(inPacket, StandardCharsets.UTF_8);
        logString("Unicode: ");
        logString(data);

        logString("HEX: ");
        logByte(inPacket);
        logString("");
    }

    private static void logString(String msg) {
        logInfo("p", msg);
    }

    private static void logByte(byte[] msg) {
        String message = Convert.byteArrayToHexString(msg);
        logInfo("p", message);
    }
}
