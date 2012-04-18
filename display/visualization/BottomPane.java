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
import java.awt.Dimension;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import visualization.config.Config.PANE;
import visualization.objects.TextArea;

public class BottomPane extends JPanel
{
    private static TextArea connectionScrollText;

    public BottomPane(HashMap<Object, Object> registry)
    {
        super();
        registry.put(PANE.BOTTOM, this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);

        JScrollPane pane = new JScrollPane();
        pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        pane.setPreferredSize(new Dimension(450, 100));
        pane.setBorder(null);

        connectionScrollText = new TextArea("Information Log.\n");
        pane.setAutoscrolls(true);
        pane.setViewportView(connectionScrollText);

        add(pane);
    }

    public static void appendln(Object string)
    {
        connectionScrollText.appendln(string.toString());
    }
    
    public static TextArea getConnectionScrollText()
    {
        return connectionScrollText;
    }
}
