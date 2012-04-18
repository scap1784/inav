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

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class OpenDialog extends JFrame
{

    public OpenDialog()
    {
        super("INAV Launcher");

        // Create and set up the window.
        // JFrame.setDefaultLookAndFeelDecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        getContentPane().setLayout(new GridLayout(0, 2));

        // Add the ubiquitous "Hello World" label.
        getContentPane().add(new JLabel("Server address."));
        getContentPane().add(new JTextField("puto.inside.scaparra.com"));
        getContentPane().add(new JButton("Lauch"));
        getContentPane().add(new JButton("Cancel"));

        setSize(200, 200);
        setMaximumSize(getSize());

        // Display the window.
        pack();
        setVisible(true);

        // Get the screen size
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();

        // Calculate the frame location
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;

        // Set the new frame location
        setLocation(x, y);
        toFront();
    }

}
