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

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;

public class INAV_Test
{

    public static void main(String[] args) throws SocketException, UnsupportedLookAndFeelException
    {

        // Make sure we have nice window decorations.
        MetalLookAndFeel.setCurrentTheme(new OceanTheme());
        UIManager.setLookAndFeel(new MetalLookAndFeel());

        // Create and set up the window.
        new OpenDialog();

        // System.out.println(Viz.GraphRenderer.ipToStringWithDots(16777216));
        // System.err.print("->" + (49152 & 0x0000FF00) + "->" +
        // ((Integer)((49152 & 0x0000FF00)>>8)).toString());
    }
}
// 3233857728 192.192.192.192

// -1062717658 192.168.55.40

// -1061109824 -> 192.192.191.192
// -1061109824

// 49152 is 192
// 10111111 00000000

// tmp -1062717656

