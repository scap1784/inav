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

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.HashMap;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import network.NetworkWriter;
import prefuse.Display;
import prefuse.Visualization;
import prefuse.controls.ControlAdapter;
import prefuse.controls.DragControl;
import prefuse.controls.NeighborHighlightControl;
import prefuse.controls.PanControl;
import prefuse.controls.ToolTipControl;
import prefuse.controls.WheelZoomControl;
import prefuse.controls.ZoomControl;
import prefuse.controls.ZoomToFitControl;
import prefuse.data.tuple.TupleSet;
import prefuse.util.GraphLib;
import prefuse.visual.VisualItem;
import visualization.config.TextFields;
import visualization.config.Config.BOX;
import visualization.config.Config.PANE;
import visualization.config.Config.VIZ;
import visualization.objects.ToolTip;

public class LeftPane extends Display
{
    private HashMap<Object, Object> registry;
    private ToolTipControl          ttc;

    public LeftPane()
    {}

    public LeftPane(HashMap<Object, Object> registry2, Visualization visualization)
    {
        super(visualization);

        this.registry = registry2;
        registry.put(PANE.LEFT, this);

        setSize(600, 600);
        pan(350, 350);
//        setForeground(Color.GRAY);
        setBackground(Color.WHITE);

        // main display controls
        setHighQuality(true);

        ttc = new ToolTip(GraphLib.LABEL);
        registry.put(VIZ.TTC, ttc);

        addControlListener(ttc);

        addControlListener(new DragControl());
        addControlListener(new PanControl());
        addControlListener(new ZoomControl(prefuse.controls.Control.MIDDLE_MOUSE_BUTTON));
        addControlListener(new WheelZoomControl());
        addControlListener(new ZoomToFitControl(prefuse.controls.Control.MIDDLE_MOUSE_BUTTON));
        addControlListener(new NeighborHighlightControl("runOnCommand"));

        addControlListener(new ControlAdapter()
        {
            public void itemClicked(VisualItem item, MouseEvent evt)
            {
                if (SwingUtilities.isLeftMouseButton(evt))
                {
                    if (item.canGetString(GraphLib.LABEL))
                    {
                        // node data send request.
                        NetworkWriter.writeData(TextFields._NODEDATA, item.getInt("nodeKey"));
                    }
                    else if (item.canGetInt(TextFields.WEIGHT))
                    {
                        ((JTextArea) registry.get(BOX.IP)).setText(String.valueOf(item.getInt(TextFields.WEIGHT)));
                    }
                }
                else if (SwingUtilities.isRightMouseButton(evt))
                {
                    Visualization vis = item.getVisualization();
                    TupleSet ts = vis.getFocusGroup(Visualization.FOCUS_ITEMS);

                    if (ts.containsTuple(item))
                        ts.removeTuple(item);
                    else
                        ts.addTuple(item);
                }
            }
        });
    }
}
