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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;

import javax.swing.JButton;

import visualization.Actions;
import visualization.config.Config;
import visualization.config.Config.COLOR;
import visualization.config.Config.VIZ;

public class ColorButton extends JButton
{

    private Picker  colorPicker;
    private HashMap registry;
    private Color   color;
    private COLOR     value;

    public ColorButton(COLOR value, HashMap registry)
    {
        super();

        this.value = value;
        this.registry = registry;
        color = (Color) Config.getColor(value);

        setForeground(color);
        setBackground(color);
        colorPicker = new Picker(color);

        // setAlignmentX(Component.CENTER_ALIGNMENT);
        setMinimumSize(new Dimension(10, 10));
        setPreferredSize(new Dimension(10, 10));
        setMaximumSize(new Dimension(10, 10));
        addMouseListener(new MouseListener()
        {
            public void mouseClicked(MouseEvent e)
            {}

            public void mouseEntered(MouseEvent e)
            {}

            public void mouseExited(MouseEvent e)
            {}

            public void mousePressed(MouseEvent e)
            {}

            public void mouseReleased(MouseEvent e)
            {
                MouseReleased(colorPicker);
            }
        });
    }

    public void MouseReleased(Picker colorPicker)
    {
        colorPicker.toggleShow(this);
    }

    public void setColor(Color color)
    {
        onResetSetColor(color);
        Config.put(value, color);
    }

    public void onResetSetColor(Color color)
    {
        setForeground(color);
        setBackground(color);
        ((Actions) registry.get(VIZ.ACTIONS)).getNEdges().setupPalette();
        ((Actions) registry.get(VIZ.ACTIONS)).getNEdgesFill().setupPalette();
    }

}
