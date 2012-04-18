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

import prefuse.action.layout.graph.ForceDirectedLayout;
import prefuse.data.Node;
import prefuse.util.force.DragForce;
import prefuse.util.force.EulerIntegrator;
import prefuse.util.force.ForceSimulator;
import prefuse.util.force.NBodyForce;
import prefuse.util.force.SpringForce;
import prefuse.visual.EdgeItem;
import prefuse.visual.VisualItem;
import visualization.config.TextFields;

public class ForceLayout extends ForceDirectedLayout implements TextFields
{

    public ForceLayout(boolean enforceBounds)
    {
        super(GRAPH, enforceBounds, false);

        ForceSimulator fsim = new ForceSimulator(new EulerIntegrator());
        fsim.addForce(new NBodyForce( -0.4f, 800f, NBodyForce.DEFAULT_MAX_THETA));
        fsim.addForce(new SpringForce(1e-5f, 40f));
        fsim.addForce(new DragForce());
        setForceSimulator(fsim);
    }

    /**
     * Get the mass value associated with the given node. Subclasses should override this method to
     * perform custom mass assignment.
     * 
     * @param n
     *            the node for which to compute the mass value
     * @return the mass value for the node. By default, all items are given a mass value of 1.0.
     */
    public float getMassValue(VisualItem n)
    {
        return Math.max(1.0f, (float) ((Node) n.getSourceTuple()).getInDegree());
        // return 1.0f;
    }

    /**
     * Get the spring length for the given edge. Subclasses should override this method to perform
     * custom spring length assignment.
     * 
     * @param e
     *            the edge for which to compute the spring length
     * @return the spring length for the edge. A return value of -1 means to ignore this method and
     *         use the global default.
     */
    public float getSpringLength(EdgeItem e)
    {
        // return Float.parseFloat(e.getSourceTuple().getString("weight"));
        return -1.f;
    }

    /**
     * Get the spring coefficient for the given edge, which controls the tension or strength of the
     * spring. Subclasses should override this method to perform custom spring tension assignment.
     * 
     * @param e
     *            the edge for which to compute the spring coefficient.
     * @return the spring coefficient for the edge. A return value of -1 means to ignore this method
     *         and use the global default.
     */
    public float getSpringCoefficient(EdgeItem e)
    {
        return -1.f;
    }

    /**
     * Set the referrer item to use to set x or y coordinates that are initialized to NaN.
     * 
     * @param referrer
     *            the referrer item to use.
     * @see prefuse.util.PrefuseLib#setX(VisualItem, VisualItem, double)
     * @see prefuse.util.PrefuseLib#setY(VisualItem, VisualItem, double)
     */
    public void setReferrer(VisualItem referrer)
    {
        this.referrer = referrer;
    }

}