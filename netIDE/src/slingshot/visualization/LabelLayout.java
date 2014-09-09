package slingshot.visualization;

/*
 * Copyright (c) 2011 Florida International University.
 *
 * Permission is hereby granted, free of charge, to any individual or
 * institution obtaining a copy of this software and associated
 * documentation files (the "software"), to use, copy, modify, and
 * distribute without restriction.
 *
 * The software is provided "as is", without warranty of any kind,
 * express or implied, including but not limited to the warranties of
 * merchantability, fitness for a particular purpose and
 * non-infringement.  In no event shall Florida International
 * University be liable for any claim, damages or other liability,
 * whether in an action of contract, tort or otherwise, arising from,
 * out of or in connection with the software or the use or other
 * dealings in the software.
 *
 * This software is developed and maintained by
 *
 *   Modeling and Networking Systems Research Group
 *   School of Computing and Information Sciences
 *   Florida International University
 *   Miami, Florida 33199, USA
 *
 * You can find our research at http://www.primessf.net/.
 */

import java.awt.geom.Rectangle2D;
import java.util.Iterator;

import prefuse.action.layout.Layout;
import prefuse.visual.VisualItem;
import prefuse.visual.DecoratorItem;

/**
 * Set label positions. Labels are assumed to be DecoratorItem instances,
 * decorating their respective nodes. The layout simply gets the bounds
 * of the decorated node and assigns the label coordinates to the center
 * of those bounds.
 * 
 * @author Eduardo Tibau
 */
public class LabelLayout extends Layout {
	//The minimum scale amount at which to render text-labels
	public static final float MIN_SCALE_VIEW = 0.7f; 
	
    public LabelLayout(String group) {
        super(group);
    }
	@SuppressWarnings("rawtypes")
    public void run(double frac) {  
    	Iterator iter = m_vis.items(m_group);
        while ( iter.hasNext() ) {
        	if (m_vis.getDisplayCount() == 0)
            	break;
        	
            DecoratorItem decorator = (DecoratorItem)iter.next();
            
            if (decorator.getDecoratedItem().isExpanded() == false) {
            	decorator.setVisible(false);
            	continue;  
            } 
            try {
	            if (m_vis.getDisplay(0).getScale() * decorator.getSize() <= LabelLayout.MIN_SCALE_VIEW) {
	            	decorator.setVisible(false);  
	            }
	            else {
		            decorator.setVisible(true);
		            VisualItem decoratedItem = decorator.getDecoratedItem();
		            Rectangle2D bounds = decoratedItem.getBounds();
		            
		            double x = bounds.getCenterX();
		            double y = bounds.getCenterY();
		
		            setX(decorator, null, x);
		            setY(decorator, null, y);
		        }
            }
            //Our first iteration (maybe even several) should actually throw this exception,
			//the display just hasn't been created yet, no worries
            catch (ArrayIndexOutOfBoundsException e) { }
            catch (IndexOutOfBoundsException e) { } 
        }
    }
}
