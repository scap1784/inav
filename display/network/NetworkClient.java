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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;

import visualization.BottomPane;
import visualization.config.Config.NETWORK;
import visualization.config.Config.PANE;
import visualization.objects.TextArea;

public class NetworkClient
{
    public String                   server;
    public int                      port;

    private static boolean                 didConnect;

    private static Socket           socket = null;
    private static DataOutputStream out    = null;
    private static DataInputStream  in     = null;

    private static NetworkReader    reader;
    private NetworkWriter           writer;

    private HashMap<Object, Object> registry;
    private static TextArea         connectionsText;

    /**
     * This connects to the INAV server
     * 
     * @param server
     * @param port
     * @throws SocketException
     */
    public NetworkClient(HashMap<Object, Object> registry)
    {
        this.registry = registry;
        this.connectionsText = BottomPane.getConnectionScrollText();
    }

    public void connect(String server, int port) throws SocketException
    {
        this.server = server;
        this.port = port;

        connectionsText.appendln("Starting a new instance of the INAV network client.");

        didConnect = false;
        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
        for (NetworkInterface netint : Collections.list(nets))
            if (netint.getName().contains("eth"))
                displayInterfaceInformation(netint);

        try
        {
            socket = new Socket(server, port);
            // socket.setReceiveBufferSize(256);
            // socket.setSendBufferSize(256);
            // socket.setSoTimeout(2000);
            out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

            writer = new NetworkWriter(registry, socket, out);
            reader = new NetworkReader(registry, socket, in);

            reader.start();

            connectionsText.appendln("Connected to host: " + server);
            didConnect = true;
        }

        catch (UnknownHostException e)
        {
            connectionsText.appendln("Don't know about host: " + server);
        }
        catch (IOException e)
        {
            connectionsText.appendln("Couldn't get I/O for the connection to: " + server);
        }
    }

    /**
     * This displays info about our network connection.
     * 
     * @param netint
     *            the network interface we are looking at
     * @throws SocketException
     */
    private void displayInterfaceInformation(NetworkInterface netint) throws SocketException
    {
        connectionsText.appendln("Display name: " + netint.getDisplayName());
        connectionsText.appendln("Name: " + netint.getName());
        Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
        for (InetAddress inetAddress : Collections.list(inetAddresses))
        {
            connectionsText.appendln("InetAddress: " + inetAddress.getHostAddress());
        }
    }

    /**
     * This closes the network socket properly in case we want to do so.
     */
    public static void closeData()
    {
        try
        {
            connectionsText.appendln("Closing connection to server...");
            out.close();
            in.close();
            socket.close();
            reader.stop();
            didConnect = false;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * @return TRUE if we successfully connected to the server FALSE otherwise
     */
    public static boolean didConnect()
    {
        return didConnect;
    }
}
