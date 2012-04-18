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

package visualization.config;

import java.awt.Color;
import java.net.SocketException;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import network.NetworkClient;
import visualization.GraphWindow;
import visualization.config.Config.NETWORK;
import visualization.config.Config.PANE;
import visualization.config.Config.VIZ;
import visualization.objects.Slider;

public class RightPane extends JPanel
{
    private HashMap<Object, Object> registry;
    private boolean                 isConnected = false;
    private BoxFactory              boxes       = null;

    public RightPane(HashMap<Object, Object> registry)
    {
        super();
        registry.put(PANE.RIGHT, this);
        this.registry = registry;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);

        Box top = new Box(BoxLayout.Y_AXIS);
        top.setBorder(BorderFactory.createTitledBorder("Configuration"));

        Box bottom = new Box(BoxLayout.Y_AXIS);
        bottom.setBorder(BorderFactory.createTitledBorder("Information"));

        boxes = new BoxFactory(registry, this);

        top.add(boxes.serverBox());
        top.add(boxes.colorBox());
        top.add(boxes.refreshBox());
//        top.add(boxes.alertBox());
        top.add(boxes.searchBox());
        // top.add(new
        // JForcePanel(((ForceDirectedLayout)((Actions)registry.get(ACTIONS)).animate.get(0)).getForceSimulator()));

        // top.add(generalBox());

        bottom.add(boxes.IPBox());
        bottom.add(boxes.DNSBox());
        bottom.add(boxes.performanceBox());

        add(top);
        add(Box.createVerticalGlue());
        add(bottom);
    }

    protected void tryToConnect(JTextArea serverField, JTextArea portField, JButton connectButton)
    {
        try
        {
            new NetworkClient(registry).connect(serverField.getText(),
                                                Integer.parseInt(portField.getText()));
        }
        catch (NumberFormatException e1)
        {
        }
        catch (SocketException e1)
        {
        }

        if (NetworkClient.didConnect())
        {
            GraphWindow localGraphWindow = ((GraphWindow) registry.get(VIZ.GRAPHWINDOW));
            synchronized (localGraphWindow.m_vis())
            {
                visualization.Visualization vis = ((visualization.Visualization) registry.get(VIZ.VISUALIZATION));

                vis.createSystem();
                localGraphWindow.renderNodesAndEdges();
                localGraphWindow.startGraph(localGraphWindow.graph());

                // now we run our action list
                localGraphWindow.m_vis().run("renderDistance");
                localGraphWindow.m_vis().run("draw");

                vis.start();
                isConnected = !isConnected;
                connectButton.setText(isConnected ? "Disconnect" : "Connect");
            }
        }
    }

    protected void tryToDisconnect(JButton connectButton)
    {
        visualization.Visualization vis = ((visualization.Visualization) registry.get(VIZ.VISUALIZATION));
        vis.stop();
        vis.getMainView().m_vis().cancel("draw");
        synchronized (vis.getMainView().m_vis())
        {

            synchronized (vis.getMainView().graph())
            {
                // vis.mainView.graph().dispose();
                vis.getMainView().graph().clear();
            }
            vis.getMainView().m_vis().reset();
            vis.getMainView().m_vis().repaint();
        }
        NetworkClient.closeData();
        isConnected = !isConnected;
        connectButton.setText(isConnected ? "Disconnect" : "Connect");

    }

    protected boolean getConnected()
    {
        return isConnected;
    }

    public Slider getEdgeLifeSlider()
    {
        return boxes.edgeLifeSlider;
    }

    public Slider getGraphRefreshSlider()
    {
        return boxes.graphRefreshSlider;
    }
}
