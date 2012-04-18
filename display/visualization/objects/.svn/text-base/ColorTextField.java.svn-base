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

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

import javax.swing.JTextField;

import visualization.Actions;
import visualization.config.Config;
import visualization.config.Config.CONFIG;
import visualization.config.Config.VIZ;

public class ColorTextField extends JTextField
{
    private ColorTextField    button;
    private HashMap           registry;
    private CONFIG               configName;
    private String            configValue;

    public ColorTextField(CONFIG config, ColorTextField button, HashMap registry)
    {
        super();
        this.button = button;
        this.registry = registry;
        this.configName = config;

        configValue = Config.getString(config);
        setText(configValue);

        if (button != null && button.getButton() == null)
            button.setButton(this);

        addKeyListener(new KeyListener()
        {
            public void keyPressed(KeyEvent e)
            {
                String keyText = KeyEvent.getKeyText(e.getKeyCode());
                if (e.isControlDown() && keyText.equals("Backspace"))
                {
                    setText("");

                }
                else if (keyText.equals("Enter"))
                {
                    setButtonText();
                }

                else if (keyText.equals("Escape"))
                {
                    setButtonText2();
                }

            }

            public void keyReleased(KeyEvent arg0)
            {}

            public void keyTyped(KeyEvent arg0)
            {
                // only allows numbers to be typed.
                if ( !Character.isDigit(arg0.getKeyChar()))
                {
                    arg0.consume();
                    return;
                }
            }
        });
        addFocusListener(new FocusListener()
        {

            public void focusGained(FocusEvent e)
            {}

            public void focusLost(FocusEvent e)
            {
                setButtonText();
            }

        });
    }

    protected void setButtonText2()
    {
        setText(configValue);
    }

    private void setButtonText()
    {
        Config.put(configName, getText());
        ((Actions) registry.get(VIZ.ACTIONS)).getNEdges().setupPalette();
        ((Actions) registry.get(VIZ.ACTIONS)).getNEdgesFill().setupPalette();
        if (button != null)
            button.setText(getText());
    }

    public void setButton(ColorTextField button)
    {
        this.button = button;
    }

    public ColorTextField getButton()
    {
        return button;
    }
}
