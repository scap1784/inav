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

package visualization;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.Timer;

import prefuse.Constants;
import prefuse.Display;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.EdgeRenderer;
import visualization.config.RightPane;
import visualization.config.Config.PANE;
import visualization.config.Config.VIZ;
import visualization.objects.LabelRenderer;

public class Visualization implements ActionListener, WindowStateListener
{
    public HashMap<Object, Object> registry;

    private GraphWindow            mainView;
    private GraphRenderer          renderer;

    private JFrame                 frame;

    private Timer                  timer1;
    private Timer                  timer2;

    private Display                display;
    private EdgeRenderer           edgeRenderer;
    private LabelRenderer          nodeRenderer;
    private prefuse.Visualization  m_vis;

    private boolean                prettyMode = false;

    /**
     * This sets the networking client for the visualization and constructs the viz.
     */
    public Visualization(HashMap<Object, Object> registry)
    {
        this.registry = registry;

        this.registry.put(VIZ.VISUALIZATION, this);

        JFrame frame = createGraphUI(registry);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * @param g
     * @param label
     * @return
     */
    public JFrame createGraphUI(HashMap<Object, Object> registry)
    {
        frame = new JFrame("Interactive Network Active-traffic Visualization Client")
        {
            Rectangle normalBounds = new Rectangle();

            /**
             * Gets the normal bounds. This is the bounds used when restoring from maximized or
             * iconified states.
             * 
             * @return the normal bounds
             */
            public Rectangle getNormalBounds()
            {
                if (this.normalBounds != null)
                {
                    return this.normalBounds;
                }
                else
                {
                    return getBounds();
                }
            }

            /*
             * (non-Javadoc)
             * 
             * @see java.awt.Frame#setExtendedState(int)
             */
            public synchronized void setExtendedState(int state)
            {
                boolean isMaximized = ((state & MAXIMIZED_BOTH) != 0);

                Rectangle bounds = getNormalBounds(); // current normal bounds

                super.setExtendedState(state);

                // if maximized in any direction, set normal bounds to whatever
                // the
                // pre-maximized bounds were
                isMaximized = ((getExtendedState() & MAXIMIZED_BOTH) != 0);
                if (isMaximized)
                {
                    setNormalBounds(bounds);
                }
                else
                // not maximized, null normal bounds
                {
                    setNormalBounds(null);
                }

                // if the normal bounds is not the same as the current bounds,
                // make the bounds the "normal" bounds.
                if ( !isMaximized && !bounds.equals(getBounds()))
                {
                    setBounds(bounds);
                }
            }

            /**
             * Sets the normal bounds.
             * 
             * @param r -
             *            the bounds
             */
            public void setNormalBounds(Rectangle r)
            {
                this.normalBounds = r;
            }
        };

        renderer = new GraphRenderer(registry);
        mainView = new GraphWindow(registry);
        renderer.setGraph(mainView.graph());

        // create a new JSplitPane to present the interface
        JSplitPane rightSplitPane = new JSplitPane();
        JSplitPane leftSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

        leftSplitPane.setTopComponent(new LeftPane(registry, mainView.m_vis()));
        leftSplitPane.setBottomComponent(new BottomPane(registry));
        leftSplitPane.setOneTouchExpandable(true);
        leftSplitPane.setResizeWeight(1);
        leftSplitPane.setContinuousLayout(false);
        leftSplitPane.setDividerLocation( -1);

        rightSplitPane.setLeftComponent(leftSplitPane);
        rightSplitPane.setRightComponent(new RightPane(registry));
        rightSplitPane.setOneTouchExpandable(true);
        rightSplitPane.setResizeWeight(1);
        rightSplitPane.setContinuousLayout(false);
        rightSplitPane.setDividerLocation( -1);

        frame.setContentPane(rightSplitPane);
        frame.pack();
        frame.setVisible(true);

        timer1 = new Timer(10, new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                synchronized (m_vis)
                {
                    for (int i = 0; i < 10; i++ )
                        renderer.modifyGraph();
                    // renderer.modifyArray();
                }
                m_vis.run("runOnCommand");
            }
        });

        timer2 = new Timer(2000, new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                checkAndAdjustQuality();
                // renderer.clusterGraph();
            }
        });

        return frame;
    }

    public void createSystem()
    {
        if (m_vis == null)
            m_vis = (prefuse.Visualization) ((GraphWindow) registry.get(VIZ.GRAPHWINDOW)).m_vis();
        if (display == null)
            display = (prefuse.Display) registry.get(PANE.LEFT);
        if (edgeRenderer == null)
            edgeRenderer = (EdgeRenderer) ((DefaultRendererFactory) m_vis.getRendererFactory()).getDefaultEdgeRenderer();
        if (nodeRenderer == null)
            nodeRenderer = (LabelRenderer) ((DefaultRendererFactory) m_vis.getRendererFactory()).getDefaultRenderer();
    }

    public void start()
    {
        timer1.start();
        timer2.start();
    }

    public void stop()
    {
        timer1.stop();
        timer2.stop();
    }

    public void actionPerformed(ActionEvent e)
    {
        e.getSource();
    }

    public void checkAndAdjustQuality()
    {
        int frameRate = display.getFrameRate() != 0 ? (((Double) display.getFrameRate()).intValue())
                                                   : 29;

        if (prettyMode)
        {
            // display.setHighQuality(false);
             edgeRenderer.setEdgeType(Constants.EDGE_TYPE_CURVE);
            // nodeRenderer.simpleRender(true);
            // nodeRenderer.setRenderType(LabelRenderer.RENDER_TYPE_DRAW_AND_FILL);
            // nodeRenderer.setRenderType(LabelRenderer.RENDER_TYPE_DRAW);
            // nodeRenderer.setRenderType(LabelRenderer.RENDER_TYPE_FILL);
            // nodeRenderer.setRenderType(LabelRenderer.RENDER_TYPE_NONE);
        }
        else if (frameRate > 30)
            display.setHighQuality(true);
        else
        {
            if (frameRate < 30)
            {
                display.setHighQuality(false);
            }
            if (frameRate < 25)
            {
            }
            if (frameRate < 20)
            {
                 edgeRenderer.setEdgeType(Constants.EDGE_TYPE_CURVE);
            }
            if (frameRate < 15)
            {
                 edgeRenderer.setEdgeType(Constants.EDGE_TYPE_LINE);
            }
            if (frameRate < 10)
            {
                // nodeRenderer.setTextField("");
                // nodeRenderer.ignoreText(true);
            }
            if (frameRate < 5)
            {
            }
        }
    }

    public void windowStateChanged(WindowEvent e)
    {
        System.err.println("EVENT:" + e);
        e.getWindow().validate();

    }

    public void setPrettyMode(boolean prettyMode)
    {
        this.prettyMode = prettyMode;
    }

    public GraphWindow getMainView()
    {
        return mainView;
    }
}
