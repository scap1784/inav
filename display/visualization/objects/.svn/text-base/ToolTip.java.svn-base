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

package visualization.objects;

import java.awt.event.MouseEvent;

import prefuse.Display;
import prefuse.controls.ToolTipControl;
import prefuse.util.GraphLib;
import prefuse.visual.VisualItem;
import visualization.config.TextFields;

public class ToolTip extends ToolTipControl
{
    private VisualItem item;
    private Display    d;

    public ToolTip(String field)
    {
        super(field);
    }

    public void itemEntered(VisualItem item, MouseEvent e)
    {
        this.d = (Display) e.getSource();
        this.item = item;
        updateTooltip();
    }

    public void itemExited(VisualItem item, MouseEvent e)
    {
        this.d = (Display) e.getSource();
        d.setToolTipText(null);
    }

    public void updateTooltip()
    {
        // if (item == null)
        // {
        // if (d != null) d.setToolTipText(null);
        // return;
        // }
        // optimize the simple case
        if (item.canGetString(GraphLib.LABEL))
            d.setToolTipText(item.getString(GraphLib.LABEL));
        else if (item.canGetInt(TextFields.WEIGHT))
            d.setToolTipText(String.valueOf(item.getInt(TextFields.WEIGHT)));
    }
}
