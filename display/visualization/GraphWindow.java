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

import java.util.HashMap;

import javax.swing.JPanel;

import prefuse.Constants;
import prefuse.Visualization;
import prefuse.data.Graph;
import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.data.event.TupleSetListener;
import prefuse.data.tuple.TupleSet;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.EdgeRenderer;
import prefuse.util.GraphLib;
import prefuse.visual.VisualGraph;
import prefuse.visual.VisualItem;
import visualization.config.TextFields;
import visualization.config.Config.VIZ;
import visualization.objects.LabelRenderer;

public class GraphWindow extends JPanel
{
    private static final String NODE_KEY = "nodeKey";
    private Graph                   graph;
    private Visualization           m_vis;
    private HashMap<Object, Object> registry;

    public GraphWindow(HashMap<Object, Object> registry)
    {
        // initialize display and data
        m_vis = new Visualization();
        
        registry.put(VIZ.GRAPHWINDOW, this);
        this.registry = registry;

        // --------------------------------------------------------------------
        // set up the renderers

        // draw the nodes as shapes with text in them
        LabelRenderer nodeR = new LabelRenderer();
        nodeR.setRoundedCorner(8, 8);

        // draw the edges
        EdgeRenderer edgeR = new EdgeRenderer(Constants.EDGE_TYPE_CURVE,
                                              Constants.EDGE_ARROW_FORWARD);
        edgeR.setArrowHeadSize(6, 10);

        // setup the renderer
        DefaultRendererFactory drf = new DefaultRendererFactory();
        drf.setDefaultRenderer(nodeR);
        drf.setDefaultEdgeRenderer(edgeR);
        m_vis.setRendererFactory(drf);

        new Actions(registry, m_vis).doSomething();

        Table nodes = new Table();
        nodes.addColumn(NODE_KEY, int.class, new Integer( -1));

        Table edges = new Table();
        edges.addColumn(Graph.DEFAULT_SOURCE_KEY, int.class, new Integer( -1));
        edges.addColumn(Graph.DEFAULT_TARGET_KEY, int.class, new Integer( -1));
        edges.addColumn(TextFields.WEIGHT, int.class, new Integer(0));

        graph = new Graph(nodes,
                          edges,
                          true,
                          NODE_KEY,
                          Graph.DEFAULT_SOURCE_KEY,
                          Graph.DEFAULT_TARGET_KEY);

        // --------------------------------------------------------------------
        // register the data with a visualization

        // fix selected focus nodes
        TupleSet focusGroup = m_vis.getGroup(Visualization.FOCUS_ITEMS);
        focusGroup.addTupleSetListener(new TupleSetListener()
        {
            public void tupleSetChanged(TupleSet ts, Tuple[] add, Tuple[] rem)
            {
                for (int i = 0; i < rem.length; ++i)
                    ((VisualItem) rem[i]).setFixed(false);
                for (int i = 0; i < add.length; ++i)
                {
                    ((VisualItem) add[i]).setFixed(false);
                    ((VisualItem) add[i]).setFixed(true);
                }
                if (ts.getTupleCount() == 0)
                {
                    ts.addTuple(rem[0]);
                    ((VisualItem) rem[0]).setFixed(false);
                }
                m_vis.run("draw");
            }
        });
        // this is the END of creating the UI. from here on, we have to
        // render/configure it.
    }

    public Graph graph()
    {
        return this.graph;
    }

    /**
     * necessary to run this after each addition of a node.
     */
    public void renderNodesAndEdges()
    {
        // TODO can i get rid of this??
        m_vis.setInteractive(TextFields.EDGES, null, false);
        m_vis.setValue(TextFields.NODES, null, VisualItem.SHAPE, Constants.SHAPE_ELLIPSE);
        m_vis.setValue(TextFields.EDGES, null, VisualItem.INTERACTIVE, Boolean.FALSE);
    }

    public void startGraph(Graph g)
    {
        g.getNodeTable().addColumns(GraphLib.LABEL_SCHEMA);

        // update graph
        m_vis.removeGroup(TextFields.GRAPH);
        VisualGraph vg = m_vis.addGraph(TextFields.GRAPH, g);

        // VisualItem f = (VisualItem) vg.getNode(0);
        // m_vis.getGroup(Visualization.FOCUS_ITEMS).setTuple(f);
        // f.setFixed(true);

    }

    public Visualization m_vis()
    {
        return m_vis;
    }
}
