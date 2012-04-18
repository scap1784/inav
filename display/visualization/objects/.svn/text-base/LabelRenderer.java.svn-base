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

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.awt.geom.RoundRectangle2D;

import prefuse.Constants;
import prefuse.render.AbstractShapeRenderer;
import prefuse.util.ColorLib;
import prefuse.util.GraphicsLib;
import prefuse.visual.VisualItem;

/**
 * Renderer that draws a label, which consists of a text string, an image, or both.
 * <p>
 * When created using the default constructor, the renderer attempts to use text from the "label"
 * field. To use a different field, use the appropriate constructor or use the
 * {@link #setTextField(String)} method. To perform custom String selection, subclass this Renderer
 * and override the {@link #getText(VisualItem)} method. When the text field is <code>null</code>,
 * no text label will be shown. Labels can span multiple lines of text, determined by the presence
 * of newline characters ('\n') within the text string.
 * </p>
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class LabelRenderer extends AbstractShapeRenderer
{

    protected String           m_labelName    = "label";

    protected int              m_xAlign       = Constants.CENTER;
    protected int              m_yAlign       = Constants.CENTER;
    protected int              m_hTextAlign   = Constants.CENTER;
    protected int              m_vTextAlign   = Constants.CENTER;

    protected int              m_horizBorder  = 2;
    protected int              m_vertBorder   = 0;
    protected int              m_arcWidth     = 0;
    protected int              m_arcHeight    = 0;

    protected int              m_maxTextWidth = -1;

    /** Transform used to scale and position images */
    AffineTransform            m_transform    = new AffineTransform();

    /** The holder for the currently computed bounding box */
    protected RectangularShape m_bbox         = new Rectangle2D.Double();
    protected Point2D          m_pt           = new Point2D.Double();    // temp point
    protected Font             m_font;                                   // temp font holder
    protected String           m_text;                                   // label text

    private boolean            simpleRender   = false;

    /**
     * Create a new LabelRenderer. By default the field "label" is used as the field name for
     * looking up text, and no image is used.
     */
    public LabelRenderer()
    {}

    /**
     * Create a new LabelRenderer. Draws a text label using the given text data field and does not
     * draw an image.
     * 
     * @param textField
     *            the data field for the text label.
     */
    public LabelRenderer(String textField)
    {
        this.setTextField(textField);
    }

    // ------------------------------------------------------------------------

    /**
     * Rounds the corners of the bounding rectangle in which the text string is rendered. This will
     * only be seen if either the stroke or fill color is non-transparent.
     * 
     * @param arcWidth
     *            the width of the curved corner
     * @param arcHeight
     *            the height of the curved corner
     */
    public void setRoundedCorner(int arcWidth, int arcHeight)
    {
        if ((arcWidth == 0 || arcHeight == 0) && !(m_bbox instanceof Rectangle2D))
        {
            m_bbox = new Rectangle2D.Double();
        }
        else
        {
            if ( !(m_bbox instanceof RoundRectangle2D))
                m_bbox = new RoundRectangle2D.Double();
            ((RoundRectangle2D) m_bbox).setRoundRect(0, 0, 10, 10, arcWidth, arcHeight);
            m_arcWidth = arcWidth;
            m_arcHeight = arcHeight;
        }
    }

    /**
     * Get the field name to use for text labels.
     * 
     * @return the data field for text labels, or null for no text
     */
    public String getTextField()
    {
        return m_labelName;
    }

    /**
     * Set the field name to use for text labels.
     * 
     * @param textField
     *            the data field for text labels, or null for no text
     */
    public void setTextField(String textField)
    {
        m_labelName = textField;
    }

    /**
     * Sets the maximum width that should be allowed of the text label. A value of -1 specifies no
     * limit (this is the default).
     * 
     * @param maxWidth
     *            the maximum width of the text or -1 for no limit
     */
    public void setMaxTextWidth(int maxWidth)
    {
        m_maxTextWidth = maxWidth;
    }

    /**
     * Returns the text to draw. Subclasses can override this class to perform custom text
     * selection.
     * 
     * @param item
     *            the item to represent as a <code>String</code>
     * @return a <code>String</code> to draw
     */
    protected String getText(VisualItem item)
    {
        String s = null;
        if (item.canGetString(m_labelName))
        {
            return item.getString(m_labelName);
        }
        return s;
    }

    // ------------------------------------------------------------------------
    // Rendering

    /**
     * @see prefuse.render.AbstractShapeRenderer#getRawShape(prefuse.visual.VisualItem)
     */
    protected Shape getRawShape(VisualItem item)
    {
        m_text = getText(item);
        double size = item.getSize();

        // get text dimensions
        m_font = item.getFont();

        // get bounding box dimensions
        double w = 80, h = 12;

        // get the top-left point, using the current alignment settings
        getAlignedPoint(m_pt, item, w, h, m_xAlign, m_yAlign);

        if (m_bbox instanceof RoundRectangle2D)
        {
            RoundRectangle2D rr = (RoundRectangle2D) m_bbox;
            rr.setRoundRect(m_pt.getX(), m_pt.getY(), w, h, size * m_arcWidth, size * m_arcHeight);
        }
        else
        {
            m_bbox.setFrame(m_pt.getX(), m_pt.getY(), w, h);
        }
        return m_bbox;
    }

    /**
     * Helper method, which calculates the top-left co-ordinate of an item given the item's
     * alignment.
     */
    protected static void getAlignedPoint(Point2D p, VisualItem item, double w, double h,
                                          int xAlign, int yAlign)
    {
        double x = item.getX(), y = item.getY();
        if (Double.isNaN(x) || Double.isInfinite(x))
            x = 0; // safety check
        if (Double.isNaN(y) || Double.isInfinite(y))
            y = 0; // safety check

        if (xAlign == Constants.CENTER)
        {
            x = x - (w / 2);
        }
        else if (xAlign == Constants.RIGHT)
        {
            x = x - w;
        }
        if (yAlign == Constants.CENTER)
        {
            y = y - (h / 2);
        }
        else if (yAlign == Constants.BOTTOM)
        {
            y = y - h;
        }
        p.setLocation(x, y);
    }

    /**
     * @see prefuse.render.Renderer#render(java.awt.Graphics2D, prefuse.visual.VisualItem)
     */
    public void render(Graphics2D g, VisualItem item)
    {
        RectangularShape shape = (RectangularShape) getShape(item);
        if (shape == null)
            return;

        // fill the shape, if requested
        int type = getRenderType(item);
        if (type == RENDER_TYPE_FILL || type == RENDER_TYPE_DRAW_AND_FILL)
            GraphicsLib.paint(g,
                              item,
                              simpleRender != true ? shape
                                                  : new Rectangle2D.Double(shape.getX(),
                                                                           shape.getY(),
                                                                           shape.getWidth(),
                                                                           shape.getHeight()),
                              getStroke(item),
                              RENDER_TYPE_FILL);

        double size = item.getSize();
        boolean useInt = 1.5 > Math.max(g.getTransform().getScaleX(), g.getTransform().getScaleY());

        double x = shape.getMinX() + size * m_horizBorder;
        double y = shape.getMinY() + size * m_vertBorder;

        // render text
        int textColor = item.getTextColor();
        if (m_text != null && ColorLib.alpha(textColor) > 0)
        {
            g.setPaint(ColorLib.getColor(textColor));
            g.setFont(m_font);
            FontMetrics fm = DEFAULT_GRAPHICS.getFontMetrics(m_font);

            // compute available width
            double tw = shape.getWidth() - 2 * size * m_horizBorder;
            tw += 4;

            // compute starting y-coordinate
            y += 10;

            if ( !simpleRender)
                drawString(g, fm, m_text, useInt, x, y, tw);
        }

        // draw border
        if ( !simpleRender)
            if (type == RENDER_TYPE_DRAW || type == RENDER_TYPE_DRAW_AND_FILL)
            {
                GraphicsLib.paint(g, item, shape, getStroke(item), RENDER_TYPE_DRAW);
            }
    }

    private final void drawString(Graphics2D g, FontMetrics fm, String text, boolean useInt,
                                  double x, double y, double w)
    {
        // compute the x-coordinate
        double tx = x + (w - fm.stringWidth(text)) / 2;

        // use integer precision unless zoomed-in
        // results in more stable drawing
        if (useInt)
        {
            g.drawString(text, (int) tx, (int) y);
        }
        else
        {
            g.drawString(text, (float) tx, (float) y);
        }
    }

    // ------------------------------------------------------------------------

    /**
     * Get the horizontal alignment of this node with respect to its x, y coordinates.
     * 
     * @return the horizontal alignment, one of {@link prefuse.Constants#LEFT},
     *         {@link prefuse.Constants#RIGHT}, or {@link prefuse.Constants#CENTER}.
     */
    public int getHorizontalAlignment()
    {
        return m_xAlign;
    }

    /**
     * Get the vertical alignment of this node with respect to its x, y coordinates.
     * 
     * @return the vertical alignment, one of {@link prefuse.Constants#TOP},
     *         {@link prefuse.Constants#BOTTOM}, or {@link prefuse.Constants#CENTER}.
     */
    public int getVerticalAlignment()
    {
        return m_yAlign;
    }

    /**
     * Set the horizontal alignment of this node with respect to its x, y coordinates.
     * 
     * @param align
     *            the horizontal alignment, one of {@link prefuse.Constants#LEFT},
     *            {@link prefuse.Constants#RIGHT}, or {@link prefuse.Constants#CENTER}.
     */
    public void setHorizontalAlignment(int align)
    {
        m_xAlign = align;
    }

    /**
     * Set the vertical alignment of this node with respect to its x, y coordinates.
     * 
     * @param align
     *            the vertical alignment, one of {@link prefuse.Constants#TOP},
     *            {@link prefuse.Constants#BOTTOM}, or {@link prefuse.Constants#CENTER}.
     */
    public void setVerticalAlignment(int align)
    {
        m_yAlign = align;
    }

    /**
     * Returns the amount of padding in pixels between the content and the border of this item along
     * the horizontal dimension.
     * 
     * @return the horizontal padding
     */
    public int getHorizontalPadding()
    {
        return m_horizBorder;
    }

    /**
     * Sets the amount of padding in pixels between the content and the border of this item along
     * the horizontal dimension.
     * 
     * @param xpad
     *            the horizontal padding to set
     */
    public void setHorizontalPadding(int xpad)
    {
        m_horizBorder = xpad;
    }

    /**
     * Returns the amount of padding in pixels between the content and the border of this item along
     * the vertical dimension.
     * 
     * @return the vertical padding
     */
    public int getVerticalPadding()
    {
        return m_vertBorder;
    }

    public void simpleRender(boolean b)
    {
        simpleRender = b;
    }

}
