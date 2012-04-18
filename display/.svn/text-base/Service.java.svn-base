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

//import org.eclipse.swt.SWT;
//import org.eclipse.swt.graphics.Image;
//import org.eclipse.swt.widgets.Display;
//import org.eclipse.swt.widgets.Event;
//import org.eclipse.swt.widgets.Listener;
//import org.eclipse.swt.widgets.Menu;
//import org.eclipse.swt.widgets.MenuItem;
//import org.eclipse.swt.widgets.Shell;
//import org.eclipse.swt.widgets.Tray;
//import org.eclipse.swt.widgets.TrayItem;
//
//
//public class Service
//{
//    public Service()
//    {
//        final Display display = new Display();
//        final Shell shell = new Shell();
//        
//        
//        final Tray tray = Display.getDefault().getSystemTray();
//
//        // Creates a new tray item (displayed as an icon)
//        final TrayItem item = new TrayItem(tray, 0);
//        final Image img = new Image(display, getClass().getResourceAsStream("icon.png"));
//        item.setToolTipText("INAV");
//        item.setImage(img);
//        // The tray item can only receive:
//        // Selection (single left click)
//        // DefaultSelection (double left click)
//        // MenuDetect (right click) events
//
//        item.addListener(SWT.MenuDetect, new Listener()
//        {
//            public void handleEvent(Event event)
//            {
//                Display.getDefault().dispose();
//                System.exit(0);
//
//            };
//        });
//        item.addListener(SWT.Selection, new Listener()
//        {
//            public void handleEvent(Event event)
//            {
//                // Style must be pop up
//                Menu m = new Menu(new Shell(event.display), SWT.POP_UP);
//
//                // Creates a new menu item that terminates the program
//                // when selected
//                MenuItem exit = new MenuItem(m, SWT.NONE);
//                exit.setText("Exit with - exit(0)");
//                exit.addListener(SWT.Selection, new Listener()
//                {
//                    public void handleEvent(Event event)
//                    {
//                        Display.getDefault().dispose();
//                        System.exit(0);
//                    }
//                });
//                // We need to make the menu visible
//                m.setVisible(true);
//            };
//        });
//        
//        while ( !shell.isDisposed())
//        {
//            if ( !display.readAndDispatch())
//                display.sleep();
//        }
//        display.dispose();
//    }
//    
//}
