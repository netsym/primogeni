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
import prefuse.util.GraphicsLib;
import prefuse.visual.AggregateItem;
import prefuse.visual.AggregateTable;
import prefuse.visual.VisualItem;

/**
 * Layout algorithm that computes a convex hull surrounding
 * aggregate items and saves it in the "_polygon" field.
 * 
 * @author Nathanael Van Vorst
 * @author Eduardo Tibau
 */
public class AggregateLayout extends Layout {
	private int m_margin = 5; // convex hull pixel margin
	private double[] m_pts;   // buffer for computing convex hulls

	public AggregateLayout(String aggrGroup) {
		super(aggrGroup);
	}

	/**
	 * @see edu.berkeley.guir.prefuse.action.Action#run(edu.berkeley.guir.prefuse.ItemRegistry, double)
	 */
	public void run(double frac) {
		int tries=10;
		while(tries>0) {
			try {
				AggregateTable aggr = (AggregateTable)m_vis.getGroup(m_group);
				if(aggr == null)  {
					tries--;
					continue;
				}
				// do we have any  to process?
				int num = aggr.getTupleCount();
				if ( num == 0 ) return;

				// update buffers
				int maxsz = 0;
				for ( @SuppressWarnings("rawtypes")
						Iterator aggrs = aggr.tuples(); aggrs.hasNext();  )
					maxsz = Math.max(maxsz, 4*2*
							((AggregateItem)aggrs.next()).getAggregateSize());
				if ( m_pts == null || maxsz > m_pts.length ) {
					m_pts = new double[maxsz];
				}

				// compute and assign convex hull for each aggregate
				@SuppressWarnings("rawtypes")
				Iterator aggrs = m_vis.visibleItems(m_group);
				while ( aggrs.hasNext() ) {
					AggregateItem aitem = (AggregateItem)aggrs.next();

					int idx = 0;
					if ( aitem.getAggregateSize() == 0 ) continue;
					VisualItem item = null;
					@SuppressWarnings("rawtypes")
					Iterator iter = aitem.items();
					while ( iter.hasNext() ) {
						item = (VisualItem)iter.next();
						if ( item.isVisible() ) {
							addPoint(m_pts, idx, item, m_margin);
							idx += 2*4;
						}
					}
					// if no aggregates are visible, do nothing
					if ( idx == 0 ) continue;

					// compute convex hull
					double[] nhull = GraphicsLib.convexHull(m_pts, idx);

					// prepare viz attribute array
					float[]  fhull = (float[])aitem.get(VisualItem.POLYGON);
					if ( fhull == null || fhull.length < nhull.length )
						fhull = new float[nhull.length];
					else if ( fhull.length > nhull.length )
						fhull[nhull.length] = Float.NaN;

					// copy hull values
					for ( int j=0; j<nhull.length; j++ )
						fhull[j] = (float)nhull[j];
					aitem.set(VisualItem.POLYGON, fhull);
					aitem.setValidated(false); // force invalidation
				}
			} catch(Exception e) {
				tries--;
				e.printStackTrace();
				continue;
			}
			return;
		}
	}

	private static void addPoint(double[] pts, int idx, 
			VisualItem item, int growth)
	{
		try {
			Rectangle2D b = item.getBounds();
			double minX = (b.getMinX())-growth, minY = (b.getMinY())-growth;
			double maxX = (b.getMaxX())+growth, maxY = (b.getMaxY())+growth;
			pts[idx]   = minX; pts[idx+1] = minY;
			pts[idx+2] = minX; pts[idx+3] = maxY;
			pts[idx+4] = maxX; pts[idx+5] = minY;
			pts[idx+6] = maxX; pts[idx+7] = maxY;
		} catch(Exception e) { }
	}

} // end of class AggregateLayout