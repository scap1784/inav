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

import java.net.SocketException;
import java.util.HashMap;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;

import visualization.BottomPane;
import visualization.Visualization;
import visualization.config.Config;

public class INAV_Client
{
    public static void main(String[] args) throws SocketException, UnsupportedLookAndFeelException
    {
        String versionAndCopyright = 
               ("INAV v.15 Copyright © 2007  Nathan Robinson, Jeff Scaparra\n\n" +
                "This program is free software: you can redistribute it and/or modify\n" +
                "it under the terms of the GNU General Public License as published by\n" +
                "the Free Software Foundation, either version 3 of the License, or\n" +
                "(at your option) any later version.\n\n" +
                "This program is distributed in the hope that it will be useful,\n" +
                "but WITHOUT ANY WARRANTY; without even the implied warranty of\n" +
                "MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n" +
                "GNU General Public License for more details.\n\n" +
                "" +
                "Please ensure checksum offload is DISABLED on your network interface.");
        
        System.out.println(versionAndCopyright);
        MetalLookAndFeel.setCurrentTheme(new OceanTheme());
        UIManager.setLookAndFeel(new MetalLookAndFeel());

        HashMap<Object, Object> registry = new HashMap<Object, Object>();
        Config.loadConfig();

        new Visualization(registry);
        BottomPane.appendln(versionAndCopyright);
        // new Service();
    }
}
