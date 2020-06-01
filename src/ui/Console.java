package ui;

/*
    @project GameServer
    @author Ashime
    Created on 07/07/2017.

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

import file.Log;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

// TODO: Commenting

public class Console
{
    private static final JFrame frame = new JFrame();
    private static final JTextPane textPane = new JTextPane();    // Keep this static, otherwise other classes/methods cannot update the JTextPane accordingly.
    private static final JTextField cmdLine = new JTextField();
    private static JScrollPane scrollPane = new JScrollPane();

    // Initializing the console interface.
    public void initConsole()
    {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   // Sets the frame's closing to default.
        frame.setTitle("SUN Online Game Server");     // Application title.
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage("Icon.png"));       // Set application icon.

        textPane.setEditable(false);            // Cannot be edited.
        textPane.setBackground(Color.BLACK);    // Sets textArea background to black.
        textPane.setFont(new Font("Lucida Console", 1, 12));

        scrollPane = new JScrollPane(textPane); // Initializes scrollPane with textArea inside.
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);   // Set scrollbar to always vertical.
        frame.add(scrollPane, BorderLayout.CENTER);  // Adds the scrollPane to the frame.

        cmdLine.setFont(new Font("Lucida Console", 1, 12)); // Font style and size of command line.
        cmdLine.addActionListener(new CMD()); // Attach listener to the command line bar.
        frame.add(cmdLine, BorderLayout.SOUTH);  // Attach command line bar to the bottom of the frame.

        frame.setPreferredSize(new Dimension(800, 250));

        frame.pack();   // Packs the frame.
        frame.setVisible(true); // Sets the frame's visibility to true.
    }

    // Display any messages to the console interface.
    public static void displayMessage(String style, String message)
    {
        // Default Messaging Style. Prints the text in white.
        StyledDocument standard = textPane.getStyledDocument();
        Style standardStyle = textPane.addStyle("Default Message", null);
        StyleConstants.setForeground(standardStyle, Color.WHITE);

        // Information Messaging Style. Prints the text in green.
        StyledDocument info = textPane.getStyledDocument();
        Style infoStyle = textPane.addStyle("INFO Message", null);
        StyleConstants.setForeground(infoStyle, Color.GREEN);

        // Warning Messaging Style. Prints the text in yellow.
        StyledDocument warning = textPane.getStyledDocument();
        Style warningStyle = textPane.addStyle("WARNING Message", null);
        StyleConstants.setForeground(warningStyle, Color.YELLOW);

        // Error Messaging Style. Prints the text in red.
        StyledDocument error = textPane.getStyledDocument();
        Style errorStyle = textPane.addStyle("ERROR Message", null);
        StyleConstants.setForeground(errorStyle, Color.RED);

        // Command Messaging Style. Prints the text in red.
        StyledDocument command = textPane.getStyledDocument();
        Style commandStyle = textPane.addStyle("ERROR Message", null);
        StyleConstants.setForeground(commandStyle, Color.CYAN);

        // Setup to take the string and add new line indentation after it.
        String newMessage = "[" + Log.formatDate() + "]" + "[" + Log.formatTime() + "]: " + message + "\n";

        try
        {
            switch (style)
            {
                case "MSG":
                {
                    standard.insertString(standard.getLength(), "[MSG]", standardStyle);
                    standard.insertString(standard.getLength(), newMessage, standardStyle);

                    String currentMessage = " [MSG]" + newMessage;

                    Log.logInfo("m", currentMessage);
                    break;
                }
                // Info Messaging Style. Prints [INFO]: + string.
                case "INFO":
                {
                    info.insertString(info.getLength(), "[INFO]", infoStyle);
                    standard.insertString(standard.getLength(), newMessage, standardStyle);

                    String currentMessage = "[INFO]" + newMessage;

                    Log.logInfo("m", currentMessage);
                    break;
                }
                // Warning Messaging Style. Prints [WARNING]: + string.
                case "WARN":
                {
                    warning.insertString(warning.getLength(), "[WARN]", warningStyle);
                    standard.insertString(standard.getLength(), newMessage, standardStyle);

                    String currentMessage = "[WARN]" + newMessage;

                    Log.logInfo("m", currentMessage);
                    break;
                }
                // Error Messaging Style. Prints [ERROR]: + string.
                case "ERR":
                {
                    error.insertString(error.getLength(), "[ERR]", errorStyle);
                    standard.insertString(standard.getLength(), newMessage, standardStyle);

                    String currentMessage = " [ERR]" + newMessage;

                    Log.logInfo("m", currentMessage);
                    break;
                }
                case "CMD":
                {
                    command.insertString(command.getLength(), "[CMD]", commandStyle);
                    standard.insertString(standard.getLength(), newMessage, standardStyle);

                    String currentMessage = "[CMD]" + newMessage;

                    Log.logInfo("m", currentMessage);
                    break;
                }
                default:
                    System.out.println("[ERR]: Wrong output. Please check passing variable.");
                    break;
            }
        } catch (BadLocationException ex) {
            Logger.getLogger(Console.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // ---------------- GETTERS ----------------
    public static JTextField getCmdLine() {
        return cmdLine;
    }
    public static JTextPane getTextPane() {
        return textPane;
    }
}