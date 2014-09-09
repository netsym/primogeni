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

import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.SwingUtilities;

import jprime.IModelNode;
import jprime.IModelNode.PrefuseLocation;
import jprime.Net.INet;
import jprime.variable.Dataset;
import prefuse.Display;
import prefuse.controls.ControlAdapter;
import prefuse.visual.AggregateItem;
import prefuse.visual.EdgeItem;
import prefuse.visual.NodeItem;
import prefuse.visual.VisualItem;
import slingshot.experiment.ProjectLoader;
import slingshot.experiment.PyExperiment;

/**
 * Interactive drag control that is "aggregate-aware"
 * 
 * @author Nathanael Van Vorst
 * @author Eduardo Tibau
 */
public class AggregateDragControl extends ControlAdapter {
	private VisualItem activeItem;
	protected Point2D down = new Point2D.Double();
	protected Point2D temp = new Point2D.Double();
	protected boolean dragged;

	private PyExperiment pyExp;
	private VisualItem previous;
	private Long clickTime;
	
	/**
	 * keeps track of items within an aggregate so they can be dragged
	 */
	private ArrayList<VisualItem> groupedItems = new ArrayList<VisualItem>();
	
	/**
	 * keeps track of items which have been pinned down
	 */
	private ArrayList<VisualItem> pinnedItems = new ArrayList<VisualItem>();
	
	/**
	 * Creates a new drag control that issues repaint requests as an item
	 * is dragged.
	 */
	public AggregateDragControl(PyExperiment pex) {
		pyExp = pex;
		clickTime=0L;
	}

	/**
	 * @see prefuse.controls.Control#itemEntered(prefuse.visual.VisualItem, java.awt.event.MouseEvent)
	 */
	public void itemEntered(VisualItem item, MouseEvent e) {
		activeItem = item;
		if ( !(item instanceof AggregateItem) )
			if(!groupedItems.contains(item))
				setFixed(item, true);
		IModelNode n = (IModelNode)item.get(VizVisitor.MODELNODE);
		if(null != n) {
			if(n instanceof PortalNetwork) {
				((Display)e.getComponent()).setToolTipText("This represents an external network");
			}
			else {
				((Display)e.getComponent()).setToolTipText(n.getUniqueName().toString().replaceAll(":", "."));
			}
		}
		
	}

	/**
	 * @see prefuse.controls.Control#itemExited(prefuse.visual.VisualItem, java.awt.event.MouseEvent)
	 */
	public void itemExited(VisualItem item, MouseEvent e) {
		if ( activeItem == item) {
			activeItem = null;
			if(!groupedItems.contains(item))
				setFixed(item, false);
		}
	}
	
	private int countMods(MouseEvent e) {
		final String s = MouseEvent.getMouseModifiersText(e.getModifiers());
		int rv=0;
		int i =0;
		while(i<s.length() && i >=0) {
			i=s.indexOf('+', i);
			if(i>=0) {
				rv++;
				i++;
			}
		}
		return rv;
	}
	/**
	 * @see prefuse.controls.Control#itemPressed(prefuse.visual.VisualItem, java.awt.event.MouseEvent)
	 */
	public void itemPressed(VisualItem item, MouseEvent e) {
		if (!SwingUtilities.isLeftMouseButton(e)) return;
		final long clickdiff = System.currentTimeMillis() - clickTime;
		clickTime+=clickdiff;
		final NetworkView layout = pyExp.getLayout();
		final int modcount=countMods(e);
		boolean pinClick = modcount == 1;
		dragged = false;
		Display d = (Display)e.getComponent();
		d.getAbsoluteCoordinate(e.getPoint(), down);
		
		//If pin click on a pinned node, remove it from the list and return to the normal color
		if(pinClick && pinnedItems.contains(item)){
			pinItem(item,false);
			pinClick = false;
		}

		if ( item instanceof AggregateItem ) {
			setFixed(item, true);
		}

		//reset the colors of the previously selected node
		layout.setSelectedRow(-1);
		layout.setSelectedAgg(-1);
		layout.setSelectedEdgeRow(-1);
		if(!groupedItems.contains(previous)){
			if (pyExp.selected != null) {
    			final Dataset ds = pyExp.getDataset();
    			if(ds==null) {
    				NetworkView.__getColor(pyExp.selected,pyExp.getOverlay(),item instanceof EdgeItem);
    			}
    			else {
    				NetworkView.__getColor(ds,pyExp.selected,pyExp.getOverlay());
    			}
			}
		}

		pyExp.selected = null;

		try{
			if ( item instanceof NodeItem ){
				//Show ModelNode data somewhere
				pyExp.selected = (IModelNode)item.get(VizVisitor.MODELNODE);
				pyExp.executeCommand("sel = exp.selected", true);
				layout.setSelectedRow(item.getRow());
			}else if (item instanceof EdgeItem ){
				pyExp.selected = (IModelNode)item.get(VizVisitor.MODELNODE);
				pyExp.executeCommand("sel = exp.selected", true);
				layout.setSelectedEdgeRow(item.getRow());
			}else if (item instanceof AggregateItem){
				pyExp.selected = (IModelNode)item.get(VizVisitor.MODELNODE);
				//System.out.println("selected "+item+", "+pyExp.selected.getUniqueName());
				pyExp.executeCommand("sel = exp.selected", true);
				layout.setSelectedAgg(item.getRow());
			}
			//else System.out.println("selected "+item+", "+item.getClass().getSimpleName());
		}catch(Exception ex){}

		//if pin click and not in the list, add it and change color
		if(pinClick && !pinnedItems.contains(item)){
			pinItem(item, true);
		}
		ProjectLoader.updateCommandAvailablility(pyExp.getName());
		pyExp.showAttrs();	
		
		if(previous == item) {
			if(clickdiff < 250) {
				if(pyExp.selected instanceof WrappedNet) {
					((WrappedNet)pyExp.selected).handleExpandCollapse(pyExp.getLayout());
				}
				else if(pyExp.selected instanceof INet) {
					if(pyExp.selected.getParent()!=null) {
						INet n = (INet)pyExp.selected;
						WrappedNet wn = new WrappedNet(n, true);
						n.setExtraVizInfo(wn);
						wn.handleExpandCollapse(pyExp.getLayout());
					}
				}
			}
		}
		else {
			previous = item;
		}
	}

	/**
	 * @see prefuse.controls.Control#itemReleased(prefuse.visual.VisualItem, java.awt.event.MouseEvent)
	 */
	public void itemReleased(VisualItem item, MouseEvent e) {
		if (!SwingUtilities.isLeftMouseButton(e)) return;
		if ( dragged ) {
			activeItem = null;
			setFixed(item, false);
			dragged = false;
		}            
	}

	/**
	 * @see prefuse.controls.Control#itemDragged(prefuse.visual.VisualItem, java.awt.event.MouseEvent)
	 */
	public void itemDragged(VisualItem item, MouseEvent e) {
		if (!SwingUtilities.isLeftMouseButton(e)) return;
		final int modcount=countMods(e);
		dragged = true;
		Display d = (Display)e.getComponent();
		d.getAbsoluteCoordinate(e.getPoint(), temp);
		double dx = temp.getX()-down.getX();
		double dy = temp.getY()-down.getY();

		if(modcount >= 3) {
			final NetworkView layout = pyExp.getLayout();
			final VisualItem ack = layout.at.getItem(pyExp.getRootNode().getRow());
			if(ack != null)
				item=ack;
		}
		move(item, dx, dy);
		down.setLocation(temp);
	}

	protected void setFixed(VisualItem item, boolean fixed) {
		if ( item instanceof AggregateItem ) {
			@SuppressWarnings("rawtypes")
			Iterator items = ((AggregateItem)item).items();
			while ( items.hasNext() ) {
				setFixed((VisualItem)items.next(), fixed);
			}
		} else {
			if(fixed) {
				if(!pinnedItems.contains(item)) {
					//fix the item
					item.setFixed(fixed);
				}
				//else its already pinned
			}
			else {
				if (!groupedItems.contains(item) && !pinnedItems.contains(item)) {
					item.setFixed(fixed);
				}
				//else its still needs to be fixed
			}
		}
	}
	
	protected void pinItem(VisualItem item, boolean fixed) {
		if ( item instanceof AggregateItem ) {
			if(fixed) {
				pinnedItems.add(item);
			}
			else {
				pinnedItems.remove(item);
			}
			@SuppressWarnings("rawtypes")
			Iterator items = ((AggregateItem)item).items();
			while ( items.hasNext() ) {
				pinItem((VisualItem)items.next(), fixed);
			}
		} else {
			if(fixed) {
				if(!pinnedItems.contains(item)) {
					//fix the item
					item.setFixed(fixed);
					pinnedItems.add(item);
				}
				//else its already pinned
			}
			else {
				if (pinnedItems.contains(item)) {
					item.setFixed(fixed);
					pinnedItems.remove(item);
					IModelNode node = (IModelNode)item.get(VizVisitor.MODELNODE);
					if(node != null) {
		    			final Dataset ds = pyExp.getDataset();
		    			if(ds==null) {
		    				NetworkView.__getColor(node,pyExp.getOverlay(),item instanceof EdgeItem);
		    			}
		    			else {
		    				NetworkView.__getColor(ds,node,pyExp.getOverlay());
		    			}
					}
				}
				//else its not pinned
			}
		}
	}

	protected static void move(VisualItem item, double dx, double dy) {
		if ( item instanceof AggregateItem ) {
			@SuppressWarnings("rawtypes")
			Iterator items = ((AggregateItem)item).items();
			while ( items.hasNext() ) {
				move((VisualItem)items.next(), dx, dy);
			}
		} else {
			double x = item.getX();
			double y = item.getY();
			item.setStartX(x);
			item.setStartY(y);
			item.setX(x+dx);
			item.setY(y+dy);
			item.setEndX(x+dx);
			item.setEndY(y+dy);
            final IModelNode node = (IModelNode)item.get(VizVisitor.MODELNODE);
            if(node != null) {
            	node.updateLocation(new PrefuseLocation(x+dx,y+dy));
            }
		}
	}
} // end of class AggregateDragControlNew
