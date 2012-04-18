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

import network.Buffer;
import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.util.GraphLib;
import trove.TLongIntHashMap;
import trove.TLongIntIterator;
import visualization.config.TextFields;
import visualization.config.Config.VIZ;

public class GraphRenderer
{
    private static final String NODE_KEY = "nodeKey";

    private Graph                   graph;

    private HashMap<Object, Object> registry;

    private prefuse.Visualization   m_vis;


    private int                     maxEdgeWeight     = 0;
    protected int                   clusterSize;

    private Node                    nodeSource        = null;
    private Node                    nodeDest          = null;
    private Edge                    nodesEdge         = null;
    private int                     IPsource;
    private int                     IPdest;
    long                            edgeID;

    public boolean                  alertValueEnabled = false;

    public GraphRenderer(HashMap<Object, Object> registry)
    {
        registry.put(VIZ.RENDERER, this);
        this.registry = registry;
    }

    /**
     * this sets our graph to use.
     * 
     * @param graph
     */
    public void setGraph(Graph graph)
    {
        this.graph = graph;
        this.m_vis = (prefuse.Visualization) ((GraphWindow) registry.get(VIZ.GRAPHWINDOW)).m_vis();
//        this.ttc = ((ToolTip) registry.get(VIZ.TTC));
        // this.clusterSize =
        // (Integer)(((Config)registry.get(Config.CONFIG)).get(Config.ANCHORCLUSTERSIZE));
    }

    public void clusterGraph()
    {
        if (true)
            return;

        // Iterator items = m_vis.visibleItems(NODES);
        // TableNodeItem item;
        // int count;
        // Iterator neighbors;
        // Edge edge;
        // TableNodeItem neighbor;
        // while (items.hasNext())
        // {
        // item = (TableNodeItem)items.next();
        // count = item.getDegree();
        // if (item.getParent() == null)
        // {
        // System.err.println("item has null parent!! -> " +
        // item.getString(GraphLib.LABEL));
        // item.setFixed(true);
        // }
        // if (item.isFixed())
        // {
        // Iterator nodes = item.neighbors();
        // while (nodes.hasNext())
        // {
        // TableNodeItem node = (TableNodeItem)nodes.next();

        // // System.err.println(item.getString(GraphLib.LABEL));
        // node.setStartX(item.getX());
        // node.setStartY(item.getY());
        // }

        // m_vis.getVisualItem(NODES, nodeDest).setStartX(
        // m_vis.getVisualItem(NODES, nodeSource).getX());
        // m_vis.getVisualItem(NODES, nodeDest).setStartY(
        // m_vis.getVisualItem(NODES, nodeSource).getY());
        // }

        // if (count > clusterSize)
        // {
        // item.setFixed(true);
        // neighbors = item.neighbors();
        // while (neighbors.hasNext())
        // {
        // neighbor = (TableNodeItem)neighbors.next();

        // if (neighbor.isFixed())
        // {
        // move this away, so we don't get cluster fscked.
        // double distanceX = neighbor.getX() - item.getX();
        // double distanceY = neighbor.getY() - item.getY();
        // item.setX(distanceX+50);
        // item.setY(distanceY+50);
        // ForceItem fitem = (ForceItem)item.get(ForceDirectedLayout.FORCEITEM);
        // fitem.
        // }

        // eVItem.setHighlighted(true);
        // eVItem.setFillColor(ColorLib.rgba(255, 200, 200, 150));
        // eVItem.setStrokeColor(ColorLib.rgba(255, 200, 200, 150));
        // eVItem.setInt(WEIGHT, 0);
        // }
        // }
        // }

        /*
         * //////// works with just regular graph items. Iterator items2 = graph.nodes(); while
         * (items2.hasNext()) { Node node = ((TableNode)items2.next()); // this sets all nodes to be
         * FIXED int count2 = node.getDegree(); // String string = node.getString(GraphLib.LABEL); //
         * int longer = node.getInt("nodeKey"); // System.out.println(count + " " + string + " " +
         * longer); // m_vis.getGroup(Visualization.FOCUS_ITEMS).addTuple(item); //
         * item.setFixed(true); Iterator edges = node.edges(); while (edges.hasNext()) { Edge edge =
         * ((TableEdge)edges.next()); // System.out.println(edge.isDirected()); } //// end //
         * AggregateItem aitem = (AggregateItem)at.addItem(); // AggregateItem aitem =
         * at.getAggregates(t) // aitem.setInt("id", i); // aitem.addItem(item); }
         */
        // Node source = nodeLookup.get(edge.source());
        // if (source != null && source.getDegree() > 2)
        // //(Integer)registry.get(ANCHOR_CLUSTER_SIZE)) ;
        // VisualItem f = (VisualItem)graph.getNode(1);
        // VisualItem f = (VisualItem)((prefuse.visual.tuple.TableNode)source);
        // mainView.m_vis().getGroup(prefuse.Visualization.FOCUS_ITEMS).setTuple(f);
        // f.setFixed(true);
        // System.err.println("JOH");
    }

    public void modifyGraph()
    {
        TLongIntHashMap map = Buffer.get();
        
        // if we are ahead of the game... do nothing.
        if (map == null)
            return;
        
        // setup the iterator..
        TLongIntIterator set = map.iterator();
        while (set.hasNext())
        {
            set.advance();
            long id = set.key();
            
            int weight = set.value();
            IPsource = (int) (id >> 32);
            IPdest = (int) (id & 0xFFFFFFFFFFFFFFFFL);

            nodeSource = graph.getNodeFromKey(IPsource);
            nodeDest = graph.getNodeFromKey(IPdest);
            Edge edge = null;

            // then we remove it from the graph.
            if (weight == -1)
            {
                if (nodeSource != null && nodeDest != null)
                    edge = graph.getEdge(nodeSource, nodeDest);

                // remove the edge from the vis.
                if (edge != null && edge.isValid())
                    graph.removeEdge(edge);

                // remove the respective nodes if necessary (don't keep nodes that have no edge.)
                if (nodeSource != null && nodeSource.getDegree() == 0)
                    graph.removeNode(nodeSource);
                if (nodeDest != null && nodeDest.getDegree() == 0)
                    graph.removeNode(nodeDest);
            }

            // then we add/modify it to the graph.
            else
            {
                // this is so that the "auto set" colors thing works.
                maxEdgeWeight = (maxEdgeWeight + Math.max(maxEdgeWeight, weight)) / 2;
                
                // this is generally the PARENT of the two nodes.
                if (nodeSource == null)
                {
                    nodeSource = graph.addNode();
                    nodeSource.setInt(NODE_KEY, IPsource);
                    nodeSource.setString(GraphLib.LABEL, ipToStringWithDots(IPsource));
                    
                    // this sets the "new" nodes to appear behind their "parent" nodes (instead of at loc:0.0)
//                    VisualItem visualItem = m_vis.getVisualItem(TextFields.NODES, (Tuple)nodeSource);
//                    if (nodeDest != null)
//                    {
//                        VisualItem visualParentItem = m_vis.getVisualItem(TextFields.NODES, (Tuple) nodeDest);
//                        visualItem.setX(visualParentItem.getX());
//                        visualItem.setY(visualParentItem.getY());
//                        System.err.print("SOURCE: " + ipToStringWithDots(destIP));
//                        System.err.print(" : " + visualParentItem.getX());
//                        System.err.println(" : " + visualItem.getX());
//                    }
                }

                // this is generally the CHILD of the two nodes.
                if (nodeDest == null)
                {
                    nodeDest = graph.addNode();
                    nodeDest.setInt(NODE_KEY, IPdest);
                    nodeDest.setString(GraphLib.LABEL, ipToStringWithDots(IPdest));
                    
//                    // this sets the "new" nodes to appear behind their "parent" nodes (instead of at loc:0.0)
//                    VisualItem visualItem = m_vis.getVisualItem(TextFields.NODES, (Tuple)nodeDest);
//                    VisualItem visualParentItem = m_vis.getVisualItem(TextFields.NODES, (Tuple)nodeSource);
//                    visualItem.setX(visualParentItem.getX());
//                    visualItem.setY(visualParentItem.getY());
//                    visualItem.setStartX(visualParentItem.getX());
//                    visualItem.setSize(200);
//                    
//                    System.err.print("DEST: " + ipToStringWithDots(destIP));
//                    System.err.print(" : " + visualParentItem.getX());
//                    System.err.println(" : " + visualItem.getX());
//                    
//                    PrefuseLib.getMemoryUsageInKB(); // good idea to know the size of stuff, and what's going on...
                    
//                     m_vis.getFocusGroup(Visualization.FOCUS_ITEMS).addTuple(visualItem);
                    // System.err.println(ipToStringWithDots(IPdest) + " : " + m_vis.getVisualItem(TextFields.NODES, (Tuple)nodeDest).getX());
                }

                nodesEdge = graph.getEdge(nodeSource, nodeDest);
                if (nodesEdge == null)
                    nodesEdge = graph.addEdge(nodeSource, nodeDest);
                nodesEdge.setInt(TextFields.WEIGHT, weight);
            }
        }
    }

    /**
     * this converts an integer to an IP address.
     * 
     * @param a
     *            the integer
     * @return the IP address as a string.
     */
    public final static String ipToStringWithDots(int a)
    {
        String temp;
        temp = ((Integer) ((int) ((((long) a) & 0xFF000000L) >> 24))).toString();
        temp += ".";
        temp += ((Integer) ((a & 0x00FF0000) >> 16)).toString();
        temp += ".";
        temp += ((Integer) ((a & 0x0000FF00) >> 8)).toString();
        temp += ".";
        temp += ((Integer) (a & 0x000000FF)).toString();
        return temp;
    }

    public void updateEdgesRender()
    {
        m_vis.run("runOnCommand");
        clusterGraph();
    }

    public int getMaxEdgeWeight()
    {
        return maxEdgeWeight;
    }

    public void resetMaxEdgeWeight()
    {
        maxEdgeWeight = 0;
    }
}
