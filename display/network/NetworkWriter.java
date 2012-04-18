/**
* INAV - Interactive Network Active-traffic Visualization
* Copyright © 2007  Nathan Robinson, Jeff Scaparra
*
* This file is a part of INAV.
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package network;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

import visualization.BottomPane;

public class NetworkWriter
{
    private HashMap<Object, Object> registry;
    private static DataOutputStream out;

    public NetworkWriter(HashMap<Object, Object> registry, Socket socket, DataOutputStream out2)
    {
        this.out = out2;
        this.registry = registry;
    }

    private static void requestingAlert()
    {
        BottomPane.appendln("Requesting data from the server...");
    }

    private static void errorAlert()
    {
        BottomPane.appendln("Couldn't write data to the socket!");
        System.err.println("Couldn't write data to the socket!");
    }

    /**
     * If the client has connected to the server, then we will try to write the data to the server
     * This runs in it's own thread, so this won't slow down anything else...
     * 
     * @param type
     *            the data type of what we are sending.
     * @param data
     *            the data contents that we are sending.
     */
    public static void writeData(byte type, int data)
    {
        try
        {
            requestingAlert();
            out.write(type);
            out.writeInt(data);
            out.flush();
        }
        catch (IOException e)
        {
            errorAlert();
        }
    }

    public static void writeData(byte type, String data)
    {
        try
        {
            requestingAlert();
            out.write(type);
            int size = data.length();
            out.writeInt((Integer) size);
            for (int i = 0; i < size; i++ )
            {
                out.write(data.charAt(i));
            }
            out.flush();
        }
        catch (IOException e)
        {
            errorAlert();
        }
    }
}
