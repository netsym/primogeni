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

//import java.util.ArrayList;

//import AggregateItem;
//import Edge;
//import JModelNode;
//import NetworkVisitor;
//import Node;
//import VisualItem;
import java.awt.BasicStroke;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeSet;

import jprime.IModelNode;
import jprime.ModelNode;
import jprime.Host.IHost;
import jprime.Interface.IInterface;
import jprime.Link.ILink;
import jprime.Net.INet;
import jprime.Net.Net;
import jprime.Router.IRouter;
import jprime.visitors.IVizVisitor;

import org.eclipse.core.runtime.IProgressMonitor;

import prefuse.data.Edge;
import prefuse.data.Node;
import prefuse.visual.AggregateItem;
import prefuse.visual.VisualItem;

/**
 * @author Nathanael Van Vorst
 * @author Eduardo Tibau
 *
 */
public class VizVisitor implements IVizVisitor{
	private final static BasicStroke stroke = new BasicStroke(2);
	public static final String DEPTH = "nodeDepth";
	public static final String MODELNODE = "modelNode";
	public static final String STROKE_COLOR = "strokeColor";
	public static final String FILL_COLOR = "fillColor";
	private NetworkView view; 
	private int depth=0;
	private final int initialDepth;
	private final IProgressMonitor monitor;

	public static class LINK  {
		final ILink link;
		final INet owner;
		public LINK(ILink link, INet owner) {
			super();
			this.link = link;
			this.owner = owner;
		}
	}
	private LinkedList<LINK> links = new LinkedList<LINK>();
	private final HashMap<Long,IModelNode> uids;
	
	public VizVisitor(Net topnet, NetworkView v, int initialDepth, IProgressMonitor monitor, HashMap<Long,IModelNode> uids){
		this.initialDepth=initialDepth;
		this.view = v;
		this.monitor=monitor;
		this.uids=uids;
		if(null!=monitor)monitor.worked(1);
		topnet.accept(this);
		if(topnet.getParent()!=null) {
			AggregateItem parent = (AggregateItem)view.at.getItem(topnet.getParent().getRow());
			AggregateItem net = (AggregateItem)view.at.getItem(topnet.getRow());
			parent.addItem(net);
		}
		for(LINK l : links) {
			if(null!=monitor)monitor.worked(1);
			addLink(l);
		}
	}

	public void visit(IModelNode node) {
		if(null != monitor)
			monitor.worked(1);
		depth++;
		final IModelNode deNode = node.deference();
		if(!deNode.isAlias()) {
			if(deNode instanceof INet){
				__visit__net((INet) deNode);
			}
			else if((deNode instanceof IHost)){
				if(deNode instanceof IRouter && ((IRouter)deNode).isTrafficPortal()) {
					final Node router = addNetworkEntity(node);
					IModelNode net = deNode;
					while( ! ( net instanceof INet )) {
						net = net.getParent();
					}
					ArrayList<String> portal_nets = null;
					for( IInterface i : ((IRouter)deNode).getNics()) {
						if(i.isTrafficPortal()) {
							if(portal_nets == null)
								portal_nets = new ArrayList<String>();
							portal_nets.addAll(i.getReachableNetworks());
						}
					}
					if(portal_nets != null) {
						final PortalNetwork p = new PortalNetwork((INet)net, portal_nets);
						final Node pn = addNetworkEntity(p);
						Edge e = view.g.addEdge(router, pn);
						e.set(MODELNODE, p);
					}
				}
				else {
					addNetworkEntity(node);
				}
			}
			else if(deNode instanceof ILink) {
				links.add(new LINK((ILink)deNode,(INet)deNode.getParent()));
			}
		}
		depth--;
	}

	private void __visit__net(INet node) {
		if(depth < initialDepth && (!view.expandedNets.containsKey(node.getDBID()) || view.expandedNets.get(node.getDBID()))) {
			addNet(node);
			for(ModelNode n : node.getAllChildren()){
				visit(n);
			}
			addToView(node);
			view.expandedNets.remove(node.getDBID());
		}
		else if(view.expandedNets.containsKey(node.getDBID()) && view.expandedNets.get(node.getDBID())) {
			addNet(node);
			for(ModelNode n : node.getAllChildren()){
				visit(n);
			}
			addToView(node);
		}
		else {
			WrappedNet w = new WrappedNet((INet)node, false);
			addNetworkEntity(w);
			for(ModelNode n : node.getAllChildren()){
				if(n instanceof ILink && !n.isAlias()) {
					links.add(new LINK((ILink)n, (INet)w.getParent()));
				}
			}
		}
	}	

	private VisualItem addNet(INet node){
		VisualItem vi = view.at.addItem();
		node.setRow(vi.getRow());
		vi.set(MODELNODE, node);
		vi.set(DEPTH, depth);
		return vi;
	}	

	public void addToView(INet node){
		AggregateItem ai = (AggregateItem)view.at.getItem(node.getRow());
		for(ModelNode n : node.getAllChildren()){
			if(n.getRow()==-1)
				continue;
			if(n instanceof INet){
				WrappedNet wn = (WrappedNet)((INet) n).getExtraVizInfo();
				if(wn==null) {
					//its expanded
					ai.addItem((VisualItem)view.at.getItem(n.getRow()));
				}
				else {
					//its collapsed
					VisualItem vi = view.getVisualItem(NetworkView.NODES, view.g.getNode(wn.getRow()));
					ai.addItem(vi);
				}
			}
			else if(n instanceof IRouter || n instanceof IHost){
				VisualItem vi = view.getVisualItem(NetworkView.NODES, view.g.getNode(n.getRow()));
				ai.addItem(vi);
			}
			else if(n instanceof ILink && !n.isAlias()){
				links.add(new LINK((ILink)n,node));
			}
		}
	}

	private Node addNetworkEntity(IModelNode node){
		uids.put(node.getDBID(),node);
		Node n = null;
		if(node.getRow()==-1){
			n = view.g.addNode();
			node.setRow(n.getRow());
			n.set(MODELNODE,node);
		}else{
			n=view.g.getNode(node.getRow());
		}
		VisualItem vi = view.getVisualItem(NetworkView.NODES, view.g.getNode(n.getRow()));
		if(vi != null) {
			vi.setStroke(stroke);
			if(node instanceof PortalNetwork) {
				IModelNode owner = node.getParent();
				AggregateItem ai = (AggregateItem)view.at.getItem(owner.getRow());
				ai.addItem(vi);
			}
		}
		return n;
	}

	private IModelNode getAttachment(IModelNode iface) {
		IModelNode owner = iface.getParent();
		while(owner != null && owner.getRow()==-1){
			owner=owner.getParent();
		}
		if(owner==null)
			throw new RuntimeException("Couldn't find an owner of "+iface.getName()+" which is rendered!");	
		return owner;
	}

	private void addLink(LINK l){
		if (l.link.getRow()==-1){
			HashMap<Long,IModelNode> attachments = new HashMap<Long,IModelNode>();
			for(IModelNode n : l.link.getAllChildren()){
				IModelNode a = getAttachment(n.deference());
				attachments.put(a.getDBID(),a);
			}
			if(attachments.size()<2){
				return;
			}
			else {
				if(attachments.size()==2){
					IModelNode endpoints[] = new IModelNode[2];
					{
						int i=0;
						for(IModelNode n : attachments.values()) {
							endpoints[i]=n;
							i++;
						}
					}

					Node n0 = view.g.getNode(endpoints[0].getRow());
					Node n1 = view.g.getNode(endpoints[1].getRow());

					if(n0==null)
						throw new RuntimeException("State of "+endpoints[0].getName()+" was null!");
					if(n1==null)
						throw new RuntimeException("State of "+endpoints[1].getName()+" was null!");

					Edge e = view.g.addEdge(n0, n1);
					l.link.setRow(e.getRow());
					e.set(MODELNODE, l.link);
				}
				else{
					TreeSet<Integer> rows = new TreeSet<Integer>();
					l.link.setHub(true);
					Node h = view.g.addNode(); //hub?
					l.link.setRow(h.getRow());
					h.set(MODELNODE,l.link);
					for(IModelNode n : attachments.values()){
						Node a = view.g.getNode(n.getRow());
						if(a==null)
							throw new RuntimeException("State of "+n.getName()+" was null!");

						Edge e = view.g.addEdge(h, a);
						e.set(MODELNODE, l.link);
						rows.add(e.getRow());
					}
					l.link.setHubRows(rows);
				}
			}
		}
		if(l.link.getHub()){
			VisualItem vi = view.getVisualItem(NetworkView.NODES, view.g.getNode(l.link.getRow()));
			AggregateItem ai = (AggregateItem)view.at.getItem(l.owner.getRow());
			ai.addItem(vi);
		}
	}
}
