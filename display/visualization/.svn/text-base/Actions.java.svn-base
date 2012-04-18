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

import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.assignment.ColorAction;
import prefuse.action.filter.GraphDistanceFilter;
import prefuse.activity.Activity;
import prefuse.util.ColorLib;
import prefuse.visual.VisualItem;
import visualization.config.TextFields;
import visualization.config.Config.VIZ;
import visualization.objects.DataColorAction;
import visualization.objects.ForceLayout;

public class Actions 
{
    private HashMap<Object, Object> registry;
    private Visualization           m_vis;
    protected GraphDistanceFilter   filter;

    protected DataColorAction       nEdgesFill;
    private DataColorAction         nEdges;
    private ActionList              animate;

    public Actions(HashMap<Object, Object> registry, Visualization m_vis)
    {
        registry.put(VIZ.ACTIONS, this);
        this.registry = registry;
        this.m_vis = m_vis;
    }

    // we must remember to START the actions (done in the rightPane, when the
    // client connects.)

    public void doSomething()
    {

        int highlightColor = ColorLib.rgb(255, 200, 125);
        int fixedColor = ColorLib.rgb(156, 156, 190);
        int hoverStrokeColor = ColorLib.gray(50);
        int hoverFillColor = ColorLib.gray(200);
        int fillColor = ColorLib.rgb(237, 237, 255);

        // --------------------------------------------------------------------
        // create actions to process the visual data
        nEdgesFill = new DataColorAction(VisualItem.FILLCOLOR);
        nEdgesFill.add(VisualItem.HIGHLIGHT, highlightColor);

        nEdges = new DataColorAction(VisualItem.STROKECOLOR);
        nEdges.add(VisualItem.HIGHLIGHT, highlightColor);

        ActionList runOnCommand = new ActionList(1);
        runOnCommand.add(nEdges);
        runOnCommand.add(nEdgesFill);
        m_vis.putAction("runOnCommand", runOnCommand);

        ActionList runEveryFiveSeconds = new ActionList(Activity.INFINITY, 5000L);
        runEveryFiveSeconds.add(nEdges);
        runEveryFiveSeconds.add(nEdgesFill);
        m_vis.putAction("runInFiveSeconds", runEveryFiveSeconds);
        m_vis.run("runInFiveSeconds");

        // first set up all the color actions
        ColorAction nFill = new ColorAction(TextFields.NODES, VisualItem.FILLCOLOR);
        nFill.setDefaultColor(fillColor);
        nFill.add(VisualItem.HOVER, hoverFillColor);
        nFill.add(VisualItem.FIXED, fixedColor);
        nFill.add(VisualItem.HIGHLIGHT, highlightColor);

        ColorAction nStroke = new ColorAction(TextFields.NODES, VisualItem.STROKECOLOR);
        nStroke.setDefaultColor(ColorLib.gray(190));
        nStroke.add(VisualItem.HOVER, hoverStrokeColor);

        ColorAction nText = new ColorAction(TextFields.NODES, VisualItem.TEXTCOLOR);
        nText.setDefaultColor(ColorLib.gray(30));
        nText.add(VisualItem.HOVER, hoverStrokeColor);
        // nText.add(VisualItem.FIXED, ColorLib.rgb(10,10,10));

        // bundle the color actions
        ActionList colors = new ActionList();
        colors.add(nStroke);
        colors.add(nText);
        colors.add(nFill);

        // ANIMATE
        animate = new ActionList(Activity.INFINITY);
        animate.add(new ForceLayout(false));
        animate.add(colors);
        animate.add(new RepaintAction());

        // finally, we register our ActionList with the Visualization.
        // we can later execute our Actions by invoking a method on our
        // Visualization, using the name we've chosen below.
        m_vis.putAction("draw", animate);
    }

    public DataColorAction getNEdgesFill()
    {
        return nEdgesFill;
    }

    public void setNEdgesFill(DataColorAction edgesFill)
    {
        nEdgesFill = edgesFill;
    }

    public DataColorAction getNEdges()
    {
        return nEdges;
    }

    public void setNEdges(DataColorAction edges)
    {
        nEdges = edges;
    }
}
