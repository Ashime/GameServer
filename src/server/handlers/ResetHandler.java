package server.handlers;

/*
    @project GameServer
    @author Ashime
    Created on 4/3/2018.

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

import server.NettyNio;
import ui.Console;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
Information:
        ResetHandler deals with the command '/restart #' when the user presses
        enter on the command line. The number entered after restart is how long
        the delay is before restarting. Delaying the restart can be useful to stop all
        incoming connections for so long. The restart command restarts the
        NioServer and reuses the already existing addresses for binding.
*/

public class ResetHandler implements Runnable
{
    private final int resetTime;
    private NettyNio server = new NettyNio();

    public ResetHandler(int time) {
        resetTime = time;
    }

    @Override
    public void run()
    {
        try
        {
            server.stop();
            TimeUnit.SECONDS.sleep(resetTime);
            Console.displayMessage("INFO", "Server is now restarting!");
            server.start();
        }
        catch (Exception ex) {
            Logger.getLogger(ResetHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
