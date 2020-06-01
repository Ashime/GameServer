package sql;

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

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Query
{
    private final Connect connect = new Connect();

    private ArrayList<String> queryResults = new ArrayList<>();
    private static final ArrayList<String> macAddresses = new ArrayList<>();

    private CallableStatement statement = null;
    private ResultSet result = null;
    private final int timeOut = IniFile.getTimeOut();

    public void getStoredMacAddresses()
    {
        connect.openConnection();

        try
        {
            statement = connect.getConnection().prepareCall("{call SP_GetMacAddresses()}");
            statement.setEscapeProcessing(true);
            statement.setQueryTimeout(timeOut);

            statement.executeQuery();

            result = statement.getResultSet();

            // result.next() will jump to the next row.
            while (result.next())
            {
                // Cycles through the columns - one column at a time.
                for (int j = 1; j <= result.getMetaData().getColumnCount(); j++)
                    macAddresses.add(result.getString(j));
            }

            statement.close();
            connect.closeConnection();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void getCharacterList(String Username)
    {
        queryResults = new ArrayList<>();
        connect.openConnection();

        try
        {
            statement = connect.getConnection().prepareCall("{call SP_GetCharacterList(?)}");
            statement.setEscapeProcessing(true);
            statement.setQueryTimeout(timeOut);

            statement.setNString(1, Username);
            statement.executeQuery();

            result = statement.getResultSet();

            // result.next() will jump to the next row.
            while (result.next())
            {
                // Cycles through the columns - one column at a time.
                for (int j = 1; j <= result.getMetaData().getColumnCount(); j++)
                    queryResults.add(result.getString(j));
            }

            statement.close();
            connect.closeConnection();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    // --------------------- GETTERS ---------------------
    public ArrayList<String> getQueryResults() {
        return queryResults;
    }

    public static ArrayList<String> getMacAddresses() {
        return macAddresses;
    }
}
