package ui;
/*
    @project GameServer
    @author Ashime
    Created on 4/29/2020.

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

import server.handlers.ResetHandler;
import server.handlers.SessionHandler;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CMD implements ActionListener
{
    @Override
    public void actionPerformed(ActionEvent ae)
    {
        String command = Console.getCmdLine().getText();
        Console.getCmdLine().setText("");

        if (command.matches("/help"))
        {
            Console.displayMessage("CMD", "----------------------------------------------------------");
            Console.displayMessage("CMD", "List of commands:");
            Console.displayMessage("CMD", "/activity        -  Displays stats of server activity.");
            Console.displayMessage("CMD", "/font #          -  Changes the font size.");
            Console.displayMessage("CMD", "/restart #       -  Restarts the NioServer.");
            Console.displayMessage("CMD", "----------------------------------------------------------");
        }
        else if (command.matches("/activity"))
        {
            Console.displayMessage("CMD", "Active Connections: " + SessionHandler.getActiveUsers());
        }
        else if (command.contains("/font"))
        {
            StringBuilder size = new StringBuilder();
            int fontSize;
            char[] cmd = command.toCharArray();

            for (char c : cmd) {
                if (Character.isDigit(c))
                    size.append(c);
            }

            if (size.toString().matches("") || size.toString().matches("0"))
                size.append(12);

            fontSize = Integer.parseInt(size.toString());

            Console.getTextPane().setFont(new Font("Lucida Console", 1, fontSize));
            Console.getCmdLine().setFont(new Font("Lucida Console", 1, fontSize));
        }
        else if (command.contains("/restart"))
        {
            StringBuilder time = new StringBuilder();
            int resetTime;
            char[] cmd = command.toCharArray();

            for (char c : cmd)
            {
                if (Character.isDigit(c))
                    time.append(c);
            }

            if (time.toString().matches(""))
                time.append(0);

            resetTime = Integer.parseInt(time.toString());
            ResetHandler reset = new ResetHandler(resetTime);
            Thread t = new Thread(reset);
            t.start();
        }
        else
        {
            Console.displayMessage("CMD", "Unknown command. Use /help for the command list.");
        }
    }
}
