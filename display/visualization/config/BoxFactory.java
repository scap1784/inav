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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import network.NetworkWriter;
import prefuse.data.Tuple;
import prefuse.data.tuple.TupleSet;
import prefuse.visual.tuple.TableNodeItem;
import visualization.Actions;
import visualization.GraphRenderer;
import visualization.GraphWindow;
import visualization.Visualization;
import visualization.config.Config.BOX;
import visualization.config.Config.COLOR;
import visualization.config.Config.CONFIG;
import visualization.config.Config.NETWORK;
import visualization.config.Config.VIZ;
import visualization.objects.ColorButton;
import visualization.objects.ColorTextField;
import visualization.objects.Slider;

public class BoxFactory implements TextFields
{
    private static final String REFRESH_TIME = "RefreshTime";
    private static final String EDGE_LIFE = "EdgeLife";
    private HashMap<Object, Object> registry;
    private RightPane               rightpane;

    // private boolean isConnected = false;

    private JTextArea               serverField;
    private JTextArea               portField;
    private JButton                 connectButton;

    private ColorButton             colorZeroButton;
    private ColorButton             colorLowButton;
    private ColorButton             colorMidButton;
    private ColorButton             colorHighButton;

    private ColorTextField          colorLowField;
    private ColorTextField          colorLowMidField;
    private ColorTextField          colorMidField;
    private ColorTextField          colorHighField;

    protected Slider                graphRefreshSlider;
    protected Slider                edgeLifeSlider;

    private Slider                  renderDepthSlider;
    private Slider                  clusterSize;
    private JCheckBox               enableTextValue;

    private JButton                 searchButton;
    private JTextField              searchField;

    public BoxFactory(HashMap<Object, Object> registry, RightPane rightpane)
    {
        this.registry = registry;
        this.rightpane = rightpane;
    }

    private static void setupDimensions(Box box, int height, String title)
    {
        setupDimensions(box, 200, height, title);
    }

    private static void setupDimensions(JComponent box, int width, int height, String title)
    {
        if (title != "")
            box.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                                                           title));
        box.setMinimumSize(new Dimension(width, height));
        box.setPreferredSize(new Dimension(width, height));
        box.setMaximumSize(new Dimension(width, height));
    }

    protected Box serverBox()
    {
        Box box = new Box(BoxLayout.Y_AXIS);
        setupDimensions(box, 72, "Server");

        Box serverBox = new Box(BoxLayout.X_AXIS);
        serverBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        setupDimensions(serverBox, 15, "");

        serverField = new JTextArea(Config.getString(CONFIG.SERVER));
        serverField.addKeyListener(new KeyListener()
        {
            public void keyPressed(KeyEvent e)
            {
                String keyText = KeyEvent.getKeyText(e.getKeyCode());
                if (e.isControlDown() && keyText.equals("Backspace"))
                {
                    serverField.setText("");
                }
                else if (keyText.equals("Enter"))
                {
                    e.consume();
                    if ( !rightpane.getConnected())
                    {
                        rightpane.tryToConnect(serverField, portField, connectButton);
                        Config.put(CONFIG.SERVER, serverField.getText());
                        Config.put(CONFIG.PORT, portField.getText());
                    }
                }

                else if (keyText.equals("Escape"))
                {
                    serverField.setText(Config.getString(CONFIG.SERVER));
                }

            }

            public void keyReleased(KeyEvent arg0)
            {}

            public void keyTyped(KeyEvent e)
            {}
        });

        Box portBox = new Box(BoxLayout.X_AXIS);

        JTextArea portText = new JTextArea("Port:");
        portText.setEditable(false);
        portText.setPreferredSize(new Dimension(30, 12));

        portField = new JTextArea(Config.getString(CONFIG.PORT));
        portField.setPreferredSize(new Dimension(500, 12));
        portField.addKeyListener(new KeyListener()
        {
            public void keyPressed(KeyEvent e)
            {
                String keyText = KeyEvent.getKeyText(e.getKeyCode());
                if (e.isControlDown() && keyText.equals("Backspace"))
                {
                    portField.setText("");

                }
                else if (keyText.equals("Enter"))
                {
                    e.consume();
                    if ( !rightpane.getConnected())
                    {
                        rightpane.tryToConnect(serverField, portField, connectButton);
                        Config.put(CONFIG.SERVER, serverField.getText());
                        Config.put(CONFIG.PORT, portField.getText());
                    }
                }

                else if (keyText.equals("Escape"))
                {
                    portField.setText(Config.getString(CONFIG.PORT));
                }

            }

            public void keyReleased(KeyEvent arg0)
            {}

            public void keyTyped(KeyEvent e)
            {
                // only allows numbers to be typed.
                if ( !Character.isDigit(e.getKeyChar()))
                    e.consume();

                if (Character.isDigit(e.getKeyChar()) &&
                    Integer.parseInt(portField.getText() + e.getKeyChar()) > 65535)
                    e.consume();
            }
        });

        // ////////////////////////////////////
        Box connectBox = new Box(BoxLayout.X_AXIS);
        // connectBox.setAlignmentX(Component.CENTER_ALIGNMENT);

        connectButton = new JButton("Connect");
        connectButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
        connectButton.setPreferredSize(new Dimension(100, 15));
        connectButton.setBackground(Color.WHITE);
        connectButton.setForeground(Color.DARK_GRAY);
        connectButton.addMouseListener(new MouseListener()
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
                if ( !rightpane.getConnected())
                {
                    rightpane.tryToConnect(serverField, portField, connectButton);
                    Config.put(CONFIG.SERVER, serverField.getText());
                    Config.put(CONFIG.PORT, portField.getText());
                }
                else
                {
                    rightpane.tryToDisconnect(connectButton);
                }
            }
        });

        serverBox.add(serverField);
        box.add(serverBox);

        portBox.add(portText);
        portBox.add(portField);
        box.add(portBox);

        connectBox.add(Box.createHorizontalGlue());
        connectBox.add(connectButton);

        box.add(connectBox);
        box.add(Box.createVerticalGlue());

        return box;
    }

    protected Box colorBox()
    {
        Box box = new Box(BoxLayout.Y_AXIS);
        setupDimensions(box, 140, "Bandwidth (B/s)");

        // ////////////////////////////////////
        Box numbers0Box = new Box(BoxLayout.X_AXIS);

        colorZeroButton = new ColorButton(COLOR.ZEROCOLOR, registry);
        numbers0Box.add(Box.createHorizontalStrut(5));
        numbers0Box.add(colorZeroButton);
        numbers0Box.add(Box.createHorizontalStrut(10));

        JTextField colorZeroField0 = new JTextField("0 - 1");
        colorZeroField0.setBorder(null);
        colorZeroField0.setEditable(false);
        colorZeroField0.setBackground(Color.white);

        numbers0Box.add(colorZeroField0);

        // ////////////////////////////////////
        Box numbers1Box = new Box(BoxLayout.X_AXIS);

        colorLowButton = new ColorButton(COLOR.LOWCOLOR, registry);
        numbers1Box.add(Box.createHorizontalStrut(5));
        numbers1Box.add(colorLowButton);
        numbers1Box.add(Box.createHorizontalStrut(10));

        JTextField colorZeroField1 = new JTextField("1  - ");
        colorZeroField1.setBorder(null);
        colorZeroField1.setEditable(false);
        colorZeroField1.setBackground(Color.white);
        numbers1Box.add(colorZeroField1);

        colorLowField = new ColorTextField(CONFIG.COLORLOWTEXT, colorLowMidField, registry);
        colorLowField.setBorder(null);
        colorLowField.setColumns(12);

        numbers1Box.add(colorLowField);

        // ////////////////////////////////////
        Box numbers2Box = new Box(BoxLayout.X_AXIS);

        colorMidButton = new ColorButton(COLOR.MIDCOLOR, registry);
        numbers2Box.add(Box.createHorizontalStrut(5));
        numbers2Box.add(colorMidButton);
        numbers2Box.add(Box.createHorizontalStrut(10));

        colorLowMidField = new ColorTextField(CONFIG.COLORLOWTEXT, colorLowField, registry);
        colorLowMidField.setBorder(null);
        colorLowMidField.setBackground(Color.white);
        colorLowMidField.setColumns(4);
        numbers2Box.add(colorLowMidField);

        JTextField colorLowMidField2 = new JTextField(" - ");
        colorLowMidField2.setBorder(null);
        colorLowMidField2.setEditable(false);
        colorLowMidField2.setBackground(Color.white);
        numbers2Box.add(colorLowMidField2);

        colorMidField = new ColorTextField(CONFIG.COLORMIDTEXT, colorHighField, registry);
        colorMidField.setBorder(null);
        colorMidField.setColumns(5);
        numbers2Box.add(colorMidField);

        // ////////////////////////////////////
        Box numbers3Box = new Box(BoxLayout.X_AXIS);

        colorHighButton = new ColorButton(COLOR.HIGHCOLOR, registry);
        numbers3Box.add(Box.createHorizontalStrut(5));
        numbers3Box.add(colorHighButton);
        numbers3Box.add(Box.createHorizontalStrut(10));

        colorHighField = new ColorTextField(CONFIG.COLORMIDTEXT, colorMidField, registry);
        colorHighField.setBorder(null);
        colorHighField.setColumns(5);
        numbers3Box.add(colorHighField);

        JTextField colorMidHighField23 = new JTextField("- ");
        colorMidHighField23.setBorder(null);
        colorMidHighField23.setEditable(false);
        colorMidHighField23.setBackground(Color.white);
        numbers3Box.add(colorMidHighField23);

        JTextField colorMidHighField2 = new JTextField("inf.");
        colorMidHighField2.setBorder(null);
        colorMidHighField2.setEditable(false);
        colorMidHighField2.setBackground(Color.white);
        numbers3Box.add(colorMidHighField2);

        // ////////////////////////////////////
        Box valueBox = new Box(BoxLayout.X_AXIS);
        valueBox.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton automateButton = new JButton("Automatic");
        automateButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
        automateButton.setPreferredSize(new Dimension(100, 15));
        automateButton.setBackground(Color.WHITE);
        automateButton.setForeground(Color.DARK_GRAY);
        automateButton.addMouseListener(new MouseListener()
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
                if (registry.get(VIZ.RENDERER) != null)
                {
                    int maxWeight = ((GraphRenderer) registry.get(VIZ.RENDERER)).getMaxEdgeWeight();

                    if (maxWeight == 0)
                        return;

                    String lowValue = (Integer.toString(maxWeight / 4));
                    String highValue = (Integer.toString(maxWeight / 4 * 3));

                    Config.put(CONFIG.COLORLOWTEXT, lowValue);
                    Config.put(CONFIG.COLORMIDTEXT, highValue);

                    colorLowField.setText(lowValue);
                    colorLowMidField.setText(lowValue);
                    colorMidField.setText(highValue);
                    colorHighField.setText(highValue);

                    ((Actions) registry.get(VIZ.ACTIONS)).getNEdges().setupPalette();
                    ((Actions) registry.get(VIZ.ACTIONS)).getNEdgesFill().setupPalette();
                }
            }
        });

        JCheckBox prettyModeCheckBox = new JCheckBox("");
        prettyModeCheckBox.setAlignmentX(Component.RIGHT_ALIGNMENT);
        prettyModeCheckBox.setPreferredSize(new Dimension(20, 15));
        prettyModeCheckBox.setBackground(Color.WHITE);
        prettyModeCheckBox.setForeground(Color.DARK_GRAY);
        prettyModeCheckBox.addMouseListener(new MouseListener()
        {
            private boolean value = false;

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
                value = !value;
                ((Visualization) registry.get(VIZ.VISUALIZATION)).setPrettyMode(value);
            }
        });

        JButton resetButton = new JButton("Reset");
        resetButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
        resetButton.setPreferredSize(new Dimension(70, 15));
        resetButton.setBackground(Color.WHITE);
        resetButton.setForeground(Color.DARK_GRAY);
        resetButton.addMouseListener(new MouseListener()
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
                if (registry.get(VIZ.RENDERER) != null)
                {
                    ((GraphRenderer) registry.get(VIZ.RENDERER)).resetMaxEdgeWeight();

                    Config.put(CONFIG.SERVER, serverField.getText());
                    Config.put(CONFIG.PORT, portField.getText());

                    colorZeroButton.onResetSetColor(Config.getColor(COLOR.ZEROCOLOR));
                    colorLowButton.onResetSetColor(Config.getColor(COLOR.LOWCOLOR));
                    colorMidButton.onResetSetColor(Config.getColor(COLOR.MIDCOLOR));
                    colorHighButton.onResetSetColor(Config.getColor(COLOR.HIGHCOLOR));

                    colorLowField.setText(Config.getString(CONFIG.COLORLOWTEXT));
                    colorLowMidField.setText(Config.getString(CONFIG.COLORLOWTEXT));
                    colorMidField.setText(Config.getString(CONFIG.COLORMIDTEXT));
                    colorHighField.setText(Config.getString(CONFIG.COLORMIDTEXT));

                    // renderDepthSlider.setValue((int)(Integer)config.get(Config.RENDERDEPTH));
                    // clusterSize.setValue((int)(Integer)config.get(Config.ANCHORCLUSTERSIZE));

                    Actions actions = ((Actions) registry.get(VIZ.ACTIONS));

                    actions.getNEdges().setupPalette();
                    actions.getNEdgesFill().setupPalette();

                    // TODO this is where i add more "reset to default"
                    // things...
                }
            }
        });

        valueBox.add(resetButton);
        valueBox.add(Box.createHorizontalGlue());
        valueBox.add(prettyModeCheckBox);
        valueBox.add(Box.createHorizontalGlue());
        valueBox.add(automateButton);

        // ////////////////////////////////////
        box.add(numbers0Box);
        box.add(Box.createVerticalStrut(10));
        box.add(numbers1Box);
        box.add(Box.createVerticalStrut(10));
        box.add(numbers2Box);
        box.add(Box.createVerticalStrut(10));
        box.add(numbers3Box);
        box.add(Box.createVerticalStrut(3));
        box.add(valueBox);

        return box;
    }

    protected Box refreshBox()
    {
        Box box = new Box(BoxLayout.Y_AXIS);
        setupDimensions(box, 70, "Refresh Rates");

        // ////////////////////////////////////
        Box graphBox = new Box(BoxLayout.X_AXIS);
        graphBox.setBackground(Color.WHITE);

        graphRefreshSlider = new Slider("Graph", 1, 30, 1);
        graphRefreshSlider.getSlider().addMouseListener(new MouseListener()
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
                writeGraphRefresh();
            }
        });
        graphRefreshSlider.getSlider().addKeyListener(new KeyListener()
        {
            public void keyPressed(KeyEvent e)
            {}

            public void keyReleased(KeyEvent e)
            {
                writeGraphRefresh();
            }

            public void keyTyped(KeyEvent e)
            {}
        });
        graphRefreshSlider.setBackground(Color.WHITE);
        graphRefreshSlider.setLabelWidth(51);
        graphRefreshSlider.setFieldWidth(25);
        graphBox.add(graphRefreshSlider);

        // ////////////////////////////////////
        Box edgeLifeBox = new Box(BoxLayout.X_AXIS);
        edgeLifeBox.setBackground(Color.WHITE);

        edgeLifeSlider = new Slider("Edge Life", 1, 360, 1);
        edgeLifeSlider.getSlider().addMouseListener(new MouseListener()
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
                writeGraphEdgeLife();
            }
        });
        edgeLifeSlider.getSlider().addKeyListener(new KeyListener()
        {
            public void keyPressed(KeyEvent e)
            {}

            public void keyReleased(KeyEvent e)
            {
               writeGraphEdgeLife();
            }

            public void keyTyped(KeyEvent e)
            {}
        });
        edgeLifeSlider.setBackground(Color.WHITE);
        edgeLifeSlider.setFieldWidth(25);
        edgeLifeBox.add(edgeLifeSlider);

        box.add(graphBox);
        box.add(edgeLifeBox);
        return box;
    }

    protected Box alertBox()
    {
        Box box = new Box(BoxLayout.Y_AXIS);
        setupDimensions(box, 44, "Alerts");

        Box alertBox = new Box(BoxLayout.X_AXIS);
        alertBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        setupDimensions(alertBox, 15, "");

        JTextField enableText = new JTextField("Alerts enabled:");
        enableText.setBorder(null);
        enableText.setEditable(false);
        enableText.setBackground(Color.white);

        enableTextValue = new JCheckBox("", (Boolean) Config.getBoolean(CONFIG.ALERTVALUE));
        enableTextValue.setPreferredSize(new Dimension(20, 15));
        enableTextValue.setBackground(Color.WHITE);
        enableTextValue.setForeground(Color.DARK_GRAY);
        enableTextValue.addMouseListener(new MouseListener()
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
                boolean value = ((JCheckBox) e.getSource()).isSelected();
                Config.put(CONFIG.ALERTVALUE, value);
                ((GraphRenderer) registry.get(VIZ.RENDERER)).alertValueEnabled = value;
            }
        });

        JButton setupButton = new JButton("Setup");
        setupButton.setPreferredSize(new Dimension(70, 15));
        setupButton.setBackground(Color.WHITE);
        setupButton.setForeground(Color.DARK_GRAY);
        setupButton.addMouseListener(new MouseListener()
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
                setupAlerts();
            }
        });

        alertBox.add(enableText);
        alertBox.add(enableTextValue);
        alertBox.add(Box.createHorizontalStrut(16));
        alertBox.add(setupButton);
        box.add(alertBox);

        return box;
    }

    protected Box searchBox()
    {
        Box box = new Box(BoxLayout.Y_AXIS);
        setupDimensions(box, 60, "Search");

        Box searchBox = new Box(BoxLayout.X_AXIS);
        searchBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        setupDimensions(searchBox, 17, "");

        searchField = new JTextField(Config.getString(CONFIG.SEARCHFIELDTEXT));
        searchField.addKeyListener(new KeyListener()
        {
            HashMap<TableNodeItem, Boolean> preSearchGraphState = new HashMap<TableNodeItem, Boolean>(0);
            
            public void keyPressed(KeyEvent e)
            {
                String keyText = KeyEvent.getKeyText(e.getKeyCode());
                if (e.isControlDown() && keyText.equals("Backspace"))
                {
                    String text = searchField.getText();
                    int offset = 1;
                    if (text.endsWith("."))
                        offset = 0;
                    searchField.setText(text.substring(0, text.lastIndexOf(".")+offset));
                    e.consume();
                }
                else if (keyText.equals("Enter"))
                {
                    e.consume();
                    Iterator<Entry<TableNodeItem, Boolean>>  hashIterator = preSearchGraphState.entrySet().iterator();
                    TupleSet focusGroup = ((GraphWindow) registry.get(VIZ.GRAPHWINDOW)).m_vis().getFocusGroup(prefuse.Visualization.FOCUS_ITEMS);
                    while (hashIterator.hasNext())
                    {
                        Entry<TableNodeItem, Boolean> tempItem = hashIterator.next();
                        if (tempItem.getValue() == false)
                        {
                            focusGroup.removeTuple((Tuple) tempItem.getKey());
                        }
                    }
                    preSearchGraphState = new HashMap<TableNodeItem, Boolean>(0);
                    if ( !rightpane.getConnected())
                    {
                        // tryToConnect();
                        // ((Config)registry.get(Config.CONFIG)).put(Config.SERVERFIELDTEXT,
                        // serverField.getText());
                        // ((Config)registry.get(Config.CONFIG)).put(Config.PORTFIELDTEXT,
                        // portField.getText());
                    }
                }

                else if (keyText.equals("Escape"))
                {
                    Iterator<Entry<TableNodeItem, Boolean>>  hashIterator = preSearchGraphState.entrySet().iterator();
                    TupleSet focusGroup = ((GraphWindow) registry.get(VIZ.GRAPHWINDOW)).m_vis().getFocusGroup(prefuse.Visualization.FOCUS_ITEMS);
                    while (hashIterator.hasNext())
                    {
                        Entry<TableNodeItem, Boolean> tempItem = hashIterator.next();
                        if (tempItem.getValue() == false)
                        {
                            focusGroup.removeTuple((Tuple) tempItem.getKey());
                        }
                    }
                    preSearchGraphState = new HashMap<TableNodeItem, Boolean>(0);
                    searchField.setText(Config.getString(CONFIG.SEARCHFIELDTEXT));
                    searchField.getParent().requestFocus();
                }
                else
                {
                    prefuse.Visualization m_vis = (prefuse.Visualization) ((GraphWindow) registry.get(VIZ.GRAPHWINDOW)).m_vis();
                    synchronized (m_vis)
                    {
                        // figure out the nodes that are currently locked
                        Iterator<TableNodeItem> lockedNodeIterator = m_vis.items(prefuse.Visualization.FOCUS_ITEMS);
                        while (lockedNodeIterator.hasNext())
                        {
                            TableNodeItem node = lockedNodeIterator.next();
                            if (!preSearchGraphState.containsKey(node))
                                    preSearchGraphState.put(node, true);
                        }
                        
                        // lock the ones we are looking for...
                        // TODO: maybe use something besides locking them?? somehow change thier color....
                        Iterator<TableNodeItem> nodeIterator = m_vis.items(TextFields.NODES);
                        while (nodeIterator.hasNext())
                        {
                            TableNodeItem node = nodeIterator.next();
                            int IP = node.getInt("nodeKey");
                            String ipString = GraphRenderer.ipToStringWithDots(IP);
                            TupleSet focusGroup = m_vis.getFocusGroup(prefuse.Visualization.FOCUS_ITEMS);
                            if (ipString.startsWith(searchField.getText()))
                            {
                                if (!preSearchGraphState.containsKey(node))
                                    preSearchGraphState.put(node, false);
                                focusGroup.addTuple(m_vis.getVisualItem(TextFields.NODES, (Tuple) node ));
                            }
                            else
                            {
                                Boolean wasInPreGraph = preSearchGraphState.get(node);
                                if (wasInPreGraph != null && wasInPreGraph == false)
                                    focusGroup.removeTuple((Tuple) node);
                            }
                            System.out.println(ipString);
                        }    
                    }
                }
            }

            public void keyReleased(KeyEvent arg0)
            {}

            public void keyTyped(KeyEvent e)
            {}
        });
        searchField.addFocusListener(new FocusListener()
        {

            public void focusGained(FocusEvent e)
            {
                if (searchField.getText().equals(Config.CLICK_TO_EDIT))
                    searchField.setText("");
            }

            public void focusLost(FocusEvent e)
            { }

        });

        Box searchButtonBox = new Box(BoxLayout.X_AXIS);
        // connectBox.setAlignmentX(Component.CENTER_ALIGNMENT);

        searchButton = new JButton("Find");
        searchButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
        searchButton.setPreferredSize(new Dimension(90, 15));
        searchButton.setBackground(Color.WHITE);
        searchButton.setForeground(Color.DARK_GRAY);
        searchButton.addMouseListener(new MouseListener()
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
                prefuse.Visualization m_vis = (prefuse.Visualization) ((GraphWindow) registry.get(VIZ.GRAPHWINDOW)).m_vis();
                synchronized (m_vis)
                {
                    Iterator<TableNodeItem> nodeIterator = m_vis.items(TextFields.NODES);
                    while (nodeIterator.hasNext())
                    {
                        TableNodeItem node = nodeIterator.next();
                        int IP = node.getInt("nodeKey");
                        String ipString = GraphRenderer.ipToStringWithDots(IP);
                        if (searchField.getText().equals(ipString))
                            m_vis.getFocusGroup(prefuse.Visualization.FOCUS_ITEMS).addTuple(m_vis.getVisualItem(TextFields.NODES, (Tuple) node ));
                        System.out.println(ipString);
                    }    
                }
            }
        });

        searchBox.add(searchField);
        box.add(searchBox);

        searchButtonBox.add(Box.createHorizontalGlue());
        searchButtonBox.add(searchButton);
        searchButtonBox.add(Box.createHorizontalStrut(1));

        box.add(searchButtonBox);
        box.add(Box.createVerticalGlue());

        return box;
    }

    // private Box generalBox()
    // {
    // Box box = new Box(BoxLayout.Y_AXIS);
    // setupDimensions(box, 80, "General");
    //
    // //////////////////////////////////////
    // Box graphBox = new Box(BoxLayout.X_AXIS);
    // graphBox.setBackground(Color.WHITE);
    //
    // renderDepthSlider = new inavSlider("Render Depth", 1, 30,
    // (int)(Integer)config.get(Config.RENDERDEPTH));
    // renderDepthSlider.getSlider().addMouseListener(new MouseListener()
    // {
    // public void mouseClicked(MouseEvent e)
    // { }
    // public void mouseEntered(MouseEvent e)
    // { }
    // public void mouseExited(MouseEvent e)
    // { }
    // public void mousePressed(MouseEvent e)
    // { }
    // public void mouseReleased(MouseEvent e)
    // {
    // // config.put(Config.RENDERDEPTH, renderDepthSlider.getValue() );
    // }
    // });
    // renderDepthSlider.setBackground(Color.WHITE);
    // graphBox.add(renderDepthSlider);
    //
    //
    // //////////////////////////////////////
    // Box edgeLifeBox = new Box(BoxLayout.X_AXIS);
    // edgeLifeBox.setBackground(Color.WHITE);
    //
    // clusterSize = new inavSlider("Cluster Size", 1, 99,
    // (int)(Integer)config.get(Config.ANCHORCLUSTERSIZE));
    // clusterSize.getSlider().addMouseListener(new MouseListener()
    // {
    // public void mouseClicked(MouseEvent e)
    // { }
    // public void mouseEntered(MouseEvent e)
    // { }
    // public void mouseExited(MouseEvent e)
    // { }
    // public void mousePressed(MouseEvent e)
    // { }
    // public void mouseReleased(MouseEvent e)
    // {
    // config.put(Config.ANCHORCLUSTERSIZE, clusterSize.getValue());
    // ((GraphRenderer)registry.get(RENDERER)).clusterSize =
    // (Integer)clusterSize.getValue();
    // }
    // });
    // clusterSize.setBackground(Color.WHITE);
    // clusterSize.setLabelWidth(77);
    // edgeLifeBox.add(clusterSize);
    //
    // box.add(graphBox);
    // box.add(edgeLifeBox);
    // return box;
    // }

    protected Box IPBox()
    {
        Box box = new Box(BoxLayout.Y_AXIS);
        setupDimensions(box, 50, "IP Address");

        // ////////////////////////////////////
        Box IPBox = new Box(BoxLayout.X_AXIS);
        IPBox.setBackground(Color.WHITE);

        JTextArea ipText = new JTextArea("  ");
        ipText.setEditable(false);
        registry.put(BOX.IP, ipText);
        IPBox.add(ipText);

        box.add(IPBox);

        return box;
    }

    protected Box DNSBox()
    {
        Box box = new Box(BoxLayout.Y_AXIS);
        setupDimensions(box, 50, "DNS");

        // ////////////////////////////////////
        Box DNSBox = new Box(BoxLayout.X_AXIS);
        DNSBox.setBackground(Color.WHITE);

        JTextArea dnsText = new JTextArea("  ");
        dnsText.setEditable(false);
        registry.put(BOX.DNS, dnsText);
        DNSBox.add(dnsText);
        box.add(DNSBox);

        return box;
    }

    protected Box performanceBox()
    {
        Box box = new Box(BoxLayout.Y_AXIS);
        box.setAlignmentX(Component.CENTER_ALIGNMENT);
        setupDimensions(box, 100, "Performance (B/s)");

        // ////////////////////////////////////
        Box speedBox = new Box(BoxLayout.X_AXIS);
        speedBox.setBackground(Color.WHITE);

        JTextField speedLabel = new JTextField("Total: ");
        speedLabel.setEditable(false);
        speedLabel.setBorder(null);
        speedLabel.setBackground(Color.WHITE);
        speedBox.add(speedLabel);

        JTextArea speedText = new JTextArea(" ");
        speedText.setEditable(false);
        speedText.setPreferredSize(new Dimension(72, 30));
        registry.put(BOX.SPEED, speedText);
        speedBox.add(speedText);
        box.add(speedBox);

        // ////////////////////////////////////
        Box inSpeedBox = new Box(BoxLayout.X_AXIS);
        inSpeedBox.setBackground(Color.WHITE);

        JTextField inSpeedLabel = new JTextField("Inbound: ");
        inSpeedLabel.setEditable(false);
        inSpeedLabel.setBorder(null);
        inSpeedLabel.setBackground(Color.WHITE);
        inSpeedBox.add(inSpeedLabel);

        JTextArea inSpeedText = new JTextArea(" ");
        inSpeedText.setEditable(false);
        inSpeedText.setPreferredSize(new Dimension(90, 30));
        registry.put(BOX.INSPEED, inSpeedText);
        inSpeedBox.add(inSpeedText);
        box.add(inSpeedBox);

        // ////////////////////////////////////
        Box outSpeedBox = new Box(BoxLayout.X_AXIS);
        outSpeedBox.setBackground(Color.WHITE);

        JTextField outSpeedLabel = new JTextField("Outbound: ");
        outSpeedLabel.setEditable(false);
        outSpeedLabel.setBorder(null);
        outSpeedLabel.setBackground(Color.WHITE);
        outSpeedBox.add(outSpeedLabel);

        JTextArea outSpeedText = new JTextArea(" ");
        outSpeedText.setEditable(false);
        outSpeedText.setPreferredSize(new Dimension(100, 30));
        registry.put(BOX.OUTSPEED, outSpeedText);
        outSpeedBox.add(outSpeedText);
        box.add(outSpeedBox);

        // ////////////////////////////////////
        Box connectionsBox = new Box(BoxLayout.X_AXIS);
        connectionsBox.setBackground(Color.WHITE);

        JTextField connectionsLabel = new JTextField("Connections: ");
        connectionsLabel.setEditable(false);
        connectionsLabel.setBorder(null);
        connectionsLabel.setBackground(Color.WHITE);
        connectionsBox.add(connectionsLabel);

        JTextArea connectionsText = new JTextArea("");
        connectionsText.setEditable(false);
        connectionsText.setPreferredSize(new Dimension(116, 30));
        registry.put(BOX.CONNECTIONS, connectionsText);
        connectionsBox.add(connectionsText);
        box.add(connectionsBox);

        return box;
    }

    public void setupAlerts()
    {}
    
    private void writeGraphRefresh()
    {
        // config data send request.
        NetworkWriter.writeData(_CONFIG,
                       "<" + REFRESH_TIME + ">" +
                       graphRefreshSlider.getValue() +
                       "</" + REFRESH_TIME + ">");
    }
    
    private void writeGraphEdgeLife()
    {
        // config data send request.
        NetworkWriter.writeData(_CONFIG,
                       "<" + EDGE_LIFE + ">" +
                       edgeLifeSlider.getValue() +
                       "</" + EDGE_LIFE + ">");
    }
}
