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

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import prefuse.data.Graph;

/**
 * Swing menu action that loads a graph into the graph viewer.
 */
public abstract class GraphMenuAction extends AbstractAction
{
    private GraphWindow m_view;

    public GraphMenuAction(String name, String accel, GraphWindow view)
    {
        m_view = view;
        this.putValue(AbstractAction.NAME, name);
        this.putValue(AbstractAction.ACCELERATOR_KEY, KeyStroke.getKeyStroke(accel));
    }

    public void actionPerformed(ActionEvent e)
    {
        m_view.startGraph(getGraph());
    }

    protected abstract Graph getGraph();
}
