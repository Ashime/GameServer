package sql;

/*
    @project GameServer
    @author Ashime
    Created on 7/12/2017.

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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


/*
GENERAL INFORMATION:
1. Microsoft JDBC Driver
    This is being used to connect, communicate, and etc. with MSSQL databases.
    Please see ../AuthServer/libs/ReadMe.txt for further information.
    >> Download Link: https://www.microsoft.com/en-us/download/details.aspx?id=55539

TODO: Commenting
*/

public class Connect
{
    private static Connection connection;
    private static String sqlPath;      // Connect pathway.

    public void testConnection()
    {

        try
        {
            sqlPath = "jdbc:sqlserver://" + IniFile.getSqlAddr() + ":" + IniFile.getSqlPort() + ";" + "Database=" + IniFile.getSqlDBN() + ";" + "Username=" + IniFile.getSqlUser() + ";" + "Password=" + IniFile.getSqlPass();
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connection = DriverManager.getConnection((sqlPath));

            Console.displayMessage("INFO", "SQL test connection was successful!");
        }
        catch (ClassNotFoundException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
            Console.displayMessage("ERR", "Corresponding JDBC Driver cannot be found or is corrupted!");

        }
        catch (SQLException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
            Console.displayMessage("ERR", "Cannot connect to SQL database. Check GameServer.ini or SQL database configuration.");
        }
        finally
        {
            if (connection != null)
            {
                try
                {
                    connection.close();
                }
                catch (SQLException ex) {
                    Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void openConnection()
    {
        try
        {
            connection = DriverManager.getConnection((sqlPath));
        }
        catch (SQLException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
            Console.displayMessage("ERR", "Cannot connect to SQL database. Check GameServer.ini or SQL database configuration.");
        }
    }

    public void closeConnection()
    {
        if (connection != null)
        {
            try
            {
                connection.close();
            }
            catch (SQLException ex) {
                Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    // Getter
    public Connection getConnection() {
        return connection;
    }
}
