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

import java.util.HashMap;

import javax.swing.JTextArea;

import org.cybergarage.xml.Node;
import org.cybergarage.xml.Parser;
import org.cybergarage.xml.ParserException;
import org.cybergarage.xml.parser.JaxpParser;

import visualization.BottomPane;
import visualization.config.Config;
import visualization.config.RightPane;
import visualization.config.Config.BOX;
import visualization.config.Config.CONFIG;
import visualization.config.Config.NETWORK;
import visualization.config.Config.PANE;
import visualization.objects.TextArea;

/**
 * Contains all of the XML parsing code needed by the INAV client.
 * 
 * @author robinson
 */
public class Xml
{
    private HashMap<Object, Object> registry;
    private Parser                  parser;

    public static final String      GRAPHREFRESH = "RefreshTime";
    public static final String      EDGELIFE     = "EdgeLife";

    public Xml(HashMap<Object, Object> registry)
    {
        registry.put(NETWORK.XML, this);
        this.registry = registry;
        parser = new JaxpParser();
    }

    /**
     * This allows the configuration to be parsed and set from the server.
     * 
     * @param xml
     */
    public void shoveConfigXML(String xml)
    {
        // System.err.println(xml);
        Node node;
        try
        {
            ConnectionsTextBuffer connectionsTextBuffer = new ConnectionsTextBuffer(BottomPane.getConnectionScrollText());
            // this can take a while....
            node = parser.parse(xml);
            if (node == null)
            {
                System.err.println("Error loading string. CRAP!");
                return;
            }

            int value = 1;
            RightPane pane = ((RightPane) registry.get(PANE.RIGHT));

            value = Integer.parseInt(node.getNode(GRAPHREFRESH).getValue());
            Config.put(CONFIG.GRAPHREFRESH, value);
            pane.getGraphRefreshSlider().setValue(value);
            connectionsTextBuffer.appendln("RefreshTime : " + value);

            value = Integer.parseInt(node.getNode(EDGELIFE).getValue());
            Config.put(CONFIG.EDGELIFE, value);
            pane.getEdgeLifeSlider().setValue(value);
            connectionsTextBuffer.appendln("EdgeLife    : " + value);
            connectionsTextBuffer.flush();

        }
        catch (ParserException e)
        {
            System.err.println("Error loading string. CRAP!");
            return;
        }
    }

    /**
     * This parses the XML for the connection information.
     * 
     * @param xml
     */
    public void shoveConnectionXML(String xml)
    {
        // System.err.println(xml);
        Node node;
        try
        {
            ConnectionsTextBuffer connectionsTextBuffer = new ConnectionsTextBuffer(BottomPane.getConnectionScrollText());
            
            // this can take a while...
            node = parser.parse(xml);
            if (node == null)
            {
                System.err.println("Error loading string. CRAP!");
                return;
            }

            String ip = node.getNode("IP").getValue();
            String dns = node.getNode("DNS").getValue();

            Node bandwidthNode = node.getNode("Bandwidth");
            String speed = bandwidthNode.getNode("Total").getValue();
            String inSpeed = bandwidthNode.getNode("Incomming").getValue();
            String outSpeed = bandwidthNode.getNode("Outgoing").getValue();

            ((JTextArea) registry.get(BOX.IP)).setText(ip);
            ((JTextArea) registry.get(BOX.DNS)).setText(dns);
            ((JTextArea) registry.get(BOX.SPEED)).setText(speed);
            ((JTextArea) registry.get(BOX.INSPEED)).setText(inSpeed);
            ((JTextArea) registry.get(BOX.OUTSPEED)).setText(outSpeed);

            connectionsTextBuffer.appendln("IP  : " + ip);
            connectionsTextBuffer.appendln("DNS : " + dns);
            connectionsTextBuffer.appendln("Bandwidth:");
            connectionsTextBuffer.appendln("  Total : " + speed);
            connectionsTextBuffer.appendln("  Incomming : " + inSpeed);
            connectionsTextBuffer.appendln("  Outgoing  : " + outSpeed);

            Node connectionsNodes = node.getNode("Connections");

            Node outgoingNodes = connectionsNodes.getNode("Outgoing");
            connectionsTextBuffer.appendln("Outgoing Connections");
            for (int j = 0; j < outgoingNodes.getNNodes(); j++ )
            {
                Node connectionNode = outgoingNodes.getNode(j);
                String protocol = connectionNode.getNode("Protocol").getValue();

                Node sourceNode = connectionNode.getNode("Source");
                String sourceIP = sourceNode.getNode("IP").getValue();
                String sourcePort = sourceNode.getNode("Port").getValue();

                Node destNode = connectionNode.getNode("Destination");
                String destIP = destNode.getNode("IP").getValue();
                String destPort = destNode.getNode("Port").getValue();

                connectionsTextBuffer.appendln("  " + protocol + " : " + sourceIP + ":" +
                                               sourcePort + " -> " + destIP + ":" + destPort);
            }

            Node incomingNodes = connectionsNodes.getNode("Incoming");
            connectionsTextBuffer.appendln("Incoming Connections");
            for (int j = 0; j < incomingNodes.getNNodes(); j++ )
            {
                Node connectionNode = incomingNodes.getNode(j);
                String protocol = connectionNode.getNode("Protocol").getValue();

                Node sourceNode = connectionNode.getNode("Source");
                String sourceIP = sourceNode.getNode("IP").getValue();
                String sourcePort = sourceNode.getNode("Port").getValue();

                Node destNode = connectionNode.getNode("Destination");
                String destIP = destNode.getNode("IP").getValue();
                String destPort = destNode.getNode("Port").getValue();

                connectionsTextBuffer.appendln("  " + protocol + " : " + sourceIP + ":" +
                                               sourcePort + " <- " + destIP + ":" + destPort);
            }

            ((JTextArea) registry.get(BOX.CONNECTIONS)).setText(String.valueOf(outgoingNodes.getNNodes() +
                                                                           incomingNodes.getNNodes()));
            connectionsTextBuffer.flush();
        }
        catch (ParserException e)
        {
            e.printStackTrace();
            BottomPane.appendln("****Error parsing XML. CRAP!****");
            System.err.println("****Error parsing XML. CRAP!****");
        }
    }
}

/**
 * This allows the connections text area (in the INAV client) to be "buffered" so it doesn't slow
 * down the client when a connection request is processed.
 * 
 * @author robinson
 */
class ConnectionsTextBuffer
{
    private static String buffer = "";
    private TextArea      connectionsText;

    /**
     * creates a new buffer for the connectionsText text field in the INAV client.
     * 
     * @param connectionsText
     */
    public ConnectionsTextBuffer(TextArea connectionsText)
    {
        this.connectionsText = connectionsText;
        buffer = "";
        connectionsText.appendln("Loading information...");
    }

    /**
     * flushes the buffer to the connections text area in the client
     */
    public void flush()
    {
        connectionsText.appendln(buffer);
        buffer = "";
    }

    /**
     * adds the contents as a new line in the buffer.
     * 
     * @param string
     */
    public void appendln(String string)
    {
        buffer += string + "\n";
    }
}
