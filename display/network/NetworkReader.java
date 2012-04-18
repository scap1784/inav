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

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

import visualization.BottomPane;
import visualization.GraphRenderer;
import visualization.GraphWindow;
import visualization.config.TextFields;
import visualization.config.Config.NETWORK;
import visualization.config.Config.PANE;
import visualization.config.Config.VIZ;
import visualization.objects.TextArea;

public class NetworkReader implements Runnable
{
    public boolean                  vizReady = false;

    private Thread                  thread   = null;

    private HashMap<Object, Object> registry;
    private DataInputStream         in;

    private GraphRenderer           renderer;
    private Xml                     xml;

    public NetworkReader(HashMap<Object, Object> registry, Socket socket, DataInputStream in2)
    {
        this.registry = registry;
        this.in = in2;

        xml = new Xml(registry);
    }

    /**
     * If the client has connected to the server, then we will try to read the data sent to us by
     * the server. This runs in it's own thread, so this won't slow down anything else...
     */
    private void buffer()
    {
        try
        {
            while (true)
            {
                int type = in.read();
                // System.err.println("TYPE:" + type);

                switch (type)
                {
                    case TextFields._CONFIG:
                    {
                        xml.shoveConfigXML(readXML(in.readInt()));
                        break;
                    }

                    case TextFields._GRAPHREFRESH:
                    {
                        int numberOfEdges = in.readInt();
                        // System.err.println("Number of Edges: " + numberOfEdges);
                        for (int i = 0; i < numberOfEdges; i++ )
                            Buffer.put(in.readLong(), in.readInt());
                        break;
                    }
                    case TextFields._REMOVEDEDGES:
                    {
                        int numberOfDeadEdges = in.readInt();
                        // System.err.println("Number of Dead Edges: " +
                        // numberOfDeadEdges);
                        for (int i = 0; i < numberOfDeadEdges; i++ )
                            Buffer.put(in.readLong(), -1);
                        break;
                    }
                    case TextFields._NODEDATA:
                    {
                        xml.shoveConnectionXML(readXML(in.readInt()));
                        break;
                    }
                    case TextFields._FILTERTYPES:
                        break;
                }
            }
        }
        catch (IOException e)
        {
            BottomPane.appendln("READER: Socket to the server has been closed!");
        }
    	NetworkClient.closeData();
    }

    /**
     * Reads in our XML data (a string.) We read this until size is reached. (so we ignore
     * newlines.)
     */
    private char lineBuffer[] = new char[128];
    private String readXML(int size) throws IOException
    {
        char buf[] = lineBuffer;

        int room = buf.length;
        int offset = 0;
        int c = 0;

        while (offset < size)
        {
            switch (c = in.read())
            {
                default:
                    if ( --room < 0)
                    {
                        buf = new char[offset + 128];
                        room = buf.length - offset - 1;
                        System.arraycopy(lineBuffer, 0, buf, 0, offset);
                        lineBuffer = buf;
                    }
                    buf[offset++ ] = (char) c;
                    break;
            }
        }
        if ((c == -1) && (offset == 0))
        {
            return null;
        }
        return String.copyValueOf(buf, 0, offset);
    }

    /**
     * Start the network data collecting agent.
     */
    public void start()
    {
        if (thread == null)
            thread = new Thread(this, "INAV Network Reader");
        thread.start();
    }

    /**
     * Stop the network data collecting agent.
     */
    public void stop()
    {
    	if (thread != null)
            thread = null;
    }

    /**
     * This is the run method and runs if/when we connect to the server
     */
    public void run()
    {
        try
        {
            while (thread != null)
            {
                if (renderer != null && ((GraphWindow) registry.get(VIZ.GRAPHWINDOW)).graph() != null)
                    buffer();
                else
                    renderer = (GraphRenderer) registry.get(VIZ.RENDERER);
            }
        }
        catch (OutOfMemoryError e)
        {
            BottomPane.appendln("ERROR: RAN OUT OF MEMORY");
            System.out.println("ERROR: RAN OUT OF MEMORY");
            stop();
        }
    }
}
