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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Config
{
    public static final String CLICK_TO_EDIT = "click to edit";

    private static HashMap     config        = new HashMap<Object, Object>();

    public static enum CONFIG
    {
        GRAPHREFRESH, EDGELIFE, SERVER, PORT, COLORLOWTEXT, COLORMIDTEXT, ALERTVALUE, SEARCHFIELDTEXT
    }

    public static enum COLOR
    {
        ZEROCOLOR, LOWCOLOR, MIDCOLOR, HIGHCOLOR
    }

    public static enum VIZ
    {
        RENDERER, ACTIONS, VISUALIZATION, GRAPHWINDOW, TTC
    }

    public static enum NETWORK
    {
        XML
    }

    public static enum PANE
    {
        LEFT, RIGHT, BOTTOM
    }

    public static enum BOX
    {
        IP, DNS, SPEED, INSPEED, OUTSPEED, CONNECTIONS
    }

    public Config()
    {}

    public static void resetConfig()
    {
        config = new HashMap<Integer, Object>();

        put(CONFIG.SERVER, "pluto.inside.scaparra.com");
        put(CONFIG.PORT, "5000");
        put(CONFIG.COLORLOWTEXT, "100");
        put(CONFIG.COLORMIDTEXT, "1000");
        put(CONFIG.ALERTVALUE, false);
        put(CONFIG.SEARCHFIELDTEXT, CLICK_TO_EDIT);
        put(COLOR.ZEROCOLOR, new Color(179, 255, 179));
        put(COLOR.LOWCOLOR, Color.GREEN);
        put(COLOR.MIDCOLOR, Color.GRAY);
        put(COLOR.HIGHCOLOR, Color.RED);
    }

    public static void loadConfig()
    {
        try
        {
            FileInputStream fis = new FileInputStream("INAV.config");
            GZIPInputStream gzis = new GZIPInputStream(fis);
            ObjectInputStream in = new ObjectInputStream(gzis);
            config = (HashMap<Integer, Object>) in.readObject();
            in.close();
        }
        catch (Exception e)
        {
            System.err.println("Error loading saved variables, using defaults.");
            resetConfig();
        }
    }

    /**
     * returns null if it's a new value, or the value of the old one if it already exists.
     */
    public static Object put(Object key, Object value)
    {
        Object temp = config.put(key, value);

        ObjectOutputStream out;
        try
        {
            FileOutputStream fos = new FileOutputStream("INAV.config");
            GZIPOutputStream gzos = new GZIPOutputStream(fos);
            out = new ObjectOutputStream(gzos);
            out.writeObject(config);
            out.flush();
            out.close();
        }
        catch (IOException e)
        {
            System.err.println("Open of INAV.config AND/OR save unsuccesful.");
        }

        return temp;
    }

    /**
     * Returns the string value of an object regardless of it's type.
     */
    public static String getString(Object key)
    {
        return (String) config.get(key).toString();
    }

    /**
     * Returns the value -1 if the key doesn't exist or is of the wrong type.
     */
    public static int getInt(Object key)
    {
        try
        {
            return (Integer) config.get(key);
        }
        catch (ClassCastException e)
        {
            return -1;
        }
    }

    public static Color getColor(COLOR color)
    {
        try
        {
            return (Color) config.get(color);
        }
        catch (ClassCastException e)
        {
            return new Color(179, 255, 179);
        }
    }

    public static Boolean getBoolean(Object key)
    {
        try
        {
            return (Boolean) config.get(key);
        }
        catch (ClassCastException e)
        {
            return false;
        }
    }
}
