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

import java.util.logging.Logger;

import prefuse.action.assignment.ColorAction;
import prefuse.visual.VisualItem;
import visualization.config.Config;
import visualization.config.TextFields;
import visualization.config.Config.COLOR;
import visualization.config.Config.CONFIG;

public class DataColorAction extends ColorAction
{
    private int     zeroColor;
    private int     lowColor;
    private int     midColor;
    private int     highColor;
    private Integer lowValue;
    private Integer midValue;

    public DataColorAction(String colorField)
    {
        super(TextFields.EDGES, colorField);
        setupPalette();
    }

    public void setupPalette()
    {
        zeroColor = (Config.getColor(COLOR.ZEROCOLOR)).getRGB();
        lowColor = (Config.getColor(COLOR.LOWCOLOR)).getRGB();
        midColor = (Config.getColor(COLOR.MIDCOLOR)).getRGB();
        highColor = (Config.getColor(COLOR.HIGHCOLOR)).getRGB();

        lowValue = Integer.parseInt(Config.getString(CONFIG.COLORLOWTEXT));
        midValue = Integer.parseInt(Config.getString(CONFIG.COLORMIDTEXT));
    }

    /**
     * Set up the state of this encoding Action.
     * 
     * @see prefuse.action.EncoderAction#setup()
     */
    protected void setup()
    {}

    /**
     * @see prefuse.action.assignment.ColorAction#getColor(prefuse.visual.VisualItem)
     */
    public int getColor(VisualItem item)
    {
        // check for any cascaded rules first
        Object o = lookup(item);
        if (o != null)
        {
            if (o instanceof ColorAction)
            {
                return ((ColorAction) o).getColor(item);
            }
            else if (o instanceof Integer)
            {
                return ((Integer) o).intValue();
            }
            else
            {
                Logger.getLogger(this.getClass().getName())
                      .warning("Unrecognized Object from predicate chain.");
            }
        }

        // otherwise perform data-driven assignment
        return getBracketColor(getBracket(item.getInt(TextFields.WEIGHT)));
    }

    public int getBracket(int value)
    {
        if (value <= 1)
            return 1;
        else if (value <= lowValue)
            return 2;
        else if (value <= midValue)
            return 3;
        else
            return 4;
    }

    public int getBracketColor(int value)
    {
        switch (value)
        {
            case 1:
                return zeroColor;
            case 2:
                return lowColor;
            case 3:
                return midColor;
            case 4:
                return highColor;
        }
        return highColor;
    }
}
