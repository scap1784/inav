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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Point;

import javax.swing.BorderFactory;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Picker extends JPanel implements ChangeListener
{

    protected JColorChooser tcc;
    private JFrame          frame = new JFrame();
    private ColorButton     button;

    public Picker(Color color)
    {
        super(new BorderLayout());

        // Set up color chooser for setting text color
        tcc = new JColorChooser(color);
        tcc.getSelectionModel().addChangeListener(this);
        tcc.setBorder(BorderFactory.createTitledBorder("Pick bandwidth color for edges"));

        add(tcc, BorderLayout.PAGE_END);

        // Create and set up the content pane.
        JComponent newContentPane = this;
        newContentPane.setOpaque(true); // content panes must be opaque
        frame.setContentPane(newContentPane);

        // Display the window.
        frame.pack();
        frame.setVisible(false);
    }

    public void stateChanged(ChangeEvent e)
    {
        button.setColor(tcc.getColor());
    }

    protected void toggleShow(ColorButton button2)
    {
        frame.setVisible( !frame.isVisible());
        Point point = MouseInfo.getPointerInfo().getLocation();
        point.translate( +20, 0);
        frame.setLocation(point);
        this.button = button2;
    }

    protected Color getColor()
    {
        return tcc.getColor();
    }
}