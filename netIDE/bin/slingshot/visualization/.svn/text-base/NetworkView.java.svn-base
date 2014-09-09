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

import java.awt.Color;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;

import jprime.IModelNode;
import jprime.Host.IHost;
import jprime.Link.ILink;
import jprime.Net.INet;
import jprime.Net.Net;
import jprime.gen.ModelNodeVariable;
import jprime.util.Gradient;
import jprime.util.GraphOverlay.OverlayInfo;
import jprime.variable.Dataset;
import jprime.variable.Dataset.SimpleDatum;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Shell;

import prefuse.Constants;
import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.assignment.ColorAction;
import prefuse.action.assignment.ShapeAction;
import prefuse.activity.Activity;
import prefuse.data.Graph;
import prefuse.data.Schema;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.LabelRenderer;
import prefuse.render.PolygonRenderer;
import prefuse.render.Renderer;
import prefuse.render.ShapeRenderer;
import prefuse.util.ColorLib;
import prefuse.util.ColorMap;
import prefuse.util.FontLib;
import prefuse.util.PrefuseLib;
import prefuse.visual.AggregateTable;
import prefuse.visual.VisualItem;
import prefuse.visual.expression.InGroupPredicate;
import slingshot.experiment.PyExperiment;

/**
 * 
 * This class uses the AggregateLayout class to compute bounding
 * polygons for each aggregate and the AggregateDragControlNew to
 * enable drags of both nodes and node aggregates.
 * 
 * @author Nathanael Van Vorst
 * @author Eduardo Tibau
 * 
 */
public class NetworkView extends Visualization {
	public final Shell dialogshell;
	private final static ColorMap heat_map;
	static {
		Color foo[] = Gradient.createMultiGradient(new Color[]{new Color(153,204,255), Color.green, Color.yellow, Color.orange}, 500);
		int a[] = new int[foo.length];
		for(int i=0;i<foo.length;i++) {
			a[i]=foo[i].getRGB();
		}
		heat_map = new ColorMap(a,(double)0.0,(double)1.0);
	}

    public static final String GRAPH = "graph";
    public static final String NODES = "graph.nodes";
    public static final String EDGES = "graph.edges";
    public static final String AGGR = "aggregates";
    public static final String AGG_ID = "id";
    public static final String NODE_TYPE = "n_type";
    public static final String EDGE_TYPE = "e_type";
    public static final String LT_P2P = "lt_p2p";
    public static final String LT_MULTIP = "lt_multip";
    public static final String HUB = "Hub";

    public static final String EDGE_DECORATORS = "edgeDeco";
    public static final String NODE_DECORATORS = "nodeDeco";
    public static final String AGGR_DECORATORS = "aggrDeco";

    public static final String SELECTED = "selected";

    public final PyExperiment pyexp;
    public Graph g;
    public AggregateTable at;
    
    private ActionList layout;
    private ActionList force;
    private ActionList colors;
    private ActionList repaint;
    
    private int selectedRow = -1;
    private int selectedEdgeEdge = -1;
    private int selectedAgg = -1;
    
    public final TreeMap<Long,Boolean> expandedNets;
    public final int depth;
    
    public int getSelectedRow() {
		return selectedRow;
	}

	public void setSelectedRow(int selectedRow) {
		this.selectedRow = selectedRow;
	}
	
    public int getSelectedEdgeRow() {
		return selectedEdgeEdge;
	}

	public void setSelectedEdgeRow(int selectedEdgeEdge) {
		this.selectedEdgeEdge = selectedEdgeEdge;
	}

    public int getSelectedAgg() {
		return selectedAgg;
	}

	public void setSelectedAgg(int selectedAgg) {
		this.selectedAgg = selectedAgg;
	}
	
	public NetworkView(Net topnet, PyExperiment pyexp, Shell dialogshell, int depth, IProgressMonitor monitor, HashMap<Long,IModelNode> uids) {
		this.pyexp=pyexp;
		this.expandedNets=pyexp.expandedNets;
		this.dialogshell=dialogshell;
		this.depth=depth;
        // initialize display and data
        initDataGroups(topnet, monitor,uids);
        
        // set up the renderers
        // draw the nodes as basic shapes
        Renderer nodeR = new ShapeRenderer(20);
        // draw aggregates as polygons with curved edges
        PolygonRenderer polyR = new PolygonRenderer(Constants.POLY_TYPE_CURVE);
        polyR.setCurveSlack(0.15f);
        
        DefaultRendererFactory drf = new DefaultRendererFactory();
        drf.setDefaultRenderer(nodeR);
        drf.add("ingroup('aggregates')", polyR);
        drf.add(new InGroupPredicate(NODE_DECORATORS), new NodeLabelRenderer());
        //drf.add(new InGroupPredicate(EDGE_DECORATORS), new EdgeLabelRenderer());
        //drf.add(new InGroupPredicate(AGGR_DECORATORS), new LabelRenderer(AGG_ID));
        setRendererFactory(drf);
        
        Schema sch = PrefuseLib.getVisualItemSchema();
        sch.setDefault(VisualItem.INTERACTIVE, null);
        sch.setDefault(VisualItem.TEXTCOLOR, ColorLib.gray(85));
        sch.setDefault(VisualItem.FONT, FontLib.getFont("Verdana",8));
        addDecorators(NODE_DECORATORS, NODES, sch);
            
        // Color routine
        this.colors = new ActionList(Activity.INFINITY);
        colors.add(new NodeFillColorAction(NODES,pyexp));
        colors.add(new NodeStrokeColorAction(NODES,pyexp));
        colors.add(new NodeShapeAction(NODES,pyexp));
        colors.add(new EdgeStrokeColorAction(EDGES,pyexp));
        colors.add(new AggStrokeColorAction(AGGR,pyexp));
        colors.add(new SizeAction(NODES,pyexp));
        colors.add(new SizeAction(EDGES,pyexp));
       
        // Force routine
        this.force = new ActionList(Activity.INFINITY);
        //this makes the graph 'blow up'
        force.add(new ForceDirectedLayout(GRAPH, false, false));
        
        //this makes the graph NOT 'blow up' -- it pauses though
        //force.add(new ForceDirectedLayout(GRAPH, false, true));
        
        // Layout routine
        this.layout = new ActionList(Activity.INFINITY);
        layout.add(new AggregateLayout(AGGR));
        layout.add(new LabelLayout(NODE_DECORATORS));
        
        // Repaint routine
        this.repaint = new ActionList(Activity.INFINITY);
        repaint.add(new RepaintAction());
        
        putAction("color",colors);
        putAction("force", force);
        putAction("layout", layout);
        putAction("repaint", repaint);
        
        run("layout");
        run("force");
        run("color");
        run("repaint");
    }
	
	public void cleanup() {
		this.cancel("layout");
		this.cancel("force");
		this.cancel("color");
		this.cancel("repaint");
		this.reset();
		at.clear();
		g.clear();
	}
    
    private void initDataGroups(final Net topnet, IProgressMonitor monitor, HashMap<Long,IModelNode> uids) {
    	
        TableAdapter nodes = new TableAdapter();
        TableAdapter edges = new TableAdapter();
        
        nodes.addColumn(VizVisitor.MODELNODE, IModelNode.class, null);
        
        edges.addColumn(Graph.DEFAULT_SOURCE_KEY, int.class, -1);
        edges.addColumn(Graph.DEFAULT_TARGET_KEY, int.class, -1);
        edges.addColumn(VizVisitor.MODELNODE, IModelNode.class, null);
    	
    	g = new Graph(nodes,edges,false);
    	
        // add visual data groups
        addGraph(GRAPH, g);
        setInteractive(EDGES, null, false);

        
        at = addAggregates(AGGR);
        at.addColumn(VisualItem.POLYGON, float[].class);
        at.addColumn(VizVisitor.MODELNODE, IModelNode.class, null);
        at.addColumn(VizVisitor.DEPTH, Integer.class, null);
        new VizVisitor(topnet, this, depth, monitor,uids);
    }    
    
    class NodeLabelRenderer extends LabelRenderer{
    	public NodeLabelRenderer() {
    		super();
    	}
    	
    	@Override
    	protected String getText(VisualItem item) {
    		String name = ((IModelNode)item.get(VizVisitor.MODELNODE)).getName();
    		return name;
    	}
    }

    class EdgeLabelRenderer extends LabelRenderer{
    	public EdgeLabelRenderer() {
    		super();
    	}
    	
    	@Override
    	protected String getText(VisualItem item) {
    		String name = ((IModelNode)item.get(VizVisitor.MODELNODE)).getName();
    		return name;
    	}
    }

    class NodeShapeAction extends ShapeAction {
    	final PyExperiment pyexp;
    	public NodeShapeAction(String group, PyExperiment exp) {
    		super(group);
    		this.pyexp=exp;
    	}
    	
    	@Override
    	public int getShape(VisualItem item) {
    		IModelNode node = (IModelNode)item.get(VizVisitor.MODELNODE);
    		if(node!=null)
    			return __getShape(node,pyexp.getOverlay());
    		System.err.println("no node for coloring!");
    		return Constants.SHAPE_STAR;
    	}
    }

    class NodeFillColorAction extends ColorAction {
    	final PyExperiment pyexp;
        public NodeFillColorAction(String group, PyExperiment exp) {
            super(group, VisualItem.FILLCOLOR);
        	this.pyexp=exp;
        }
        
        public int getColor(VisualItem item) {
    		IModelNode node = (IModelNode)item.get(VizVisitor.MODELNODE);
    		if(node!=null) {
    			final Dataset d = pyexp.getDataset();
    			if(d==null)
    				return __getColor(node,pyexp.getOverlay(),false);
    			return __getColor(d,node,pyexp.getOverlay());
    		}
    		System.err.println("no node for coloring!");
    		return ColorLib.gray(180);
        }
    }

    class NodeStrokeColorAction extends ColorAction {
    	final PyExperiment pyexp;
        public NodeStrokeColorAction(String group, PyExperiment exp) {
            super(group, VisualItem.STROKECOLOR);
        	this.pyexp=exp;
        }
        
        public int getColor(VisualItem item) {
        	int row = item.getRow();
        	if(row == getSelectedRow()){
        		return ColorLib.rgb(255,89, 26);
        	}
        	else{
        		IModelNode node = (IModelNode)item.get(VizVisitor.MODELNODE);
        		if(node!=null) {
        			if(node instanceof ILink && ((ILink)node).getHub()) {
        				final Set<Integer> rows = ((ILink)node).getHubRows();
        				if(rows != null && rows.contains(getSelectedEdgeRow()) || node.getRow() == getSelectedRow()) {
                			return ColorLib.rgb(255,89, 26);
        				}
        			}
        			return __getStrokeColor(node,pyexp.getOverlay());
        		}
        		System.err.println("no node for stroking!");
        		return ColorLib.gray(180);
        	}
        }
    }

    class EdgeStrokeColorAction extends ColorAction {
    	final PyExperiment pyexp;
        public EdgeStrokeColorAction(String group, PyExperiment exp) {
            super(group, VisualItem.STROKECOLOR);
        	this.pyexp=exp;
        }
        
        public int getColor(VisualItem item) {
        	try{
        		if(item.getRow() == getSelectedEdgeRow()) {
            		//IModelNode node = (IModelNode)item.get(VizVisitor.MODELNODE);
        			return ColorLib.rgb(255,89, 26);
        		}
        		final IModelNode node = (IModelNode)item.get(VizVisitor.MODELNODE);
        		if(node!=null) {
        			if(node instanceof ILink && ((ILink)node).getHub()) {
        				final Set<Integer> rows = ((ILink)node).getHubRows();
        				if(rows != null && rows.contains(getSelectedEdgeRow()) || node.getRow() == getSelectedRow()) {
                			return ColorLib.rgb(255,89, 26);
        				}
        			}
        			final Dataset d = pyexp.getDataset();
        			if(d==null)
        				return __getColor(node,pyexp.getOverlay(),true);
        			return __getColor(d,node,pyexp.getOverlay());
        		}
        	}catch(Exception e){
        		e.printStackTrace();
        	}
        	return ColorLib.gray(75);
        }
    }

    class AggStrokeColorAction extends ColorAction {
    	final PyExperiment pyexp;
        public AggStrokeColorAction(String group, PyExperiment exp) {
            super(group, VisualItem.STROKECOLOR);
        	this.pyexp=exp;
        }
        
        public int getColor(VisualItem item) {
        	try{
        		int row = item.getRow();
        		if(row == getSelectedAgg()) {
        			return ColorLib.rgb(255,89, 26);
        		}
        		IModelNode node = (IModelNode)item.get(VizVisitor.MODELNODE);
        		if(node!=null) {
        			final Dataset d = pyexp.getDataset();
        			if(d==null)
        				return __getColor(node,pyexp.getOverlay(), false);
        			return __getColor(d,node,pyexp.getOverlay());
        		}
        	}catch(Exception e){
        		e.printStackTrace();
        	}
        	return ColorLib.gray(75);
        }
    }
    
    
    class SizeAction extends prefuse.action.assignment.SizeAction {
    	final PyExperiment pyexp;
        public SizeAction(String group, PyExperiment exp) {
        	super(group);
        	this.pyexp=exp;
        }
        
		@Override
		public double getSize(VisualItem item) {
			double rv = super.getSize(item);
    		IModelNode node = (IModelNode)item.get(VizVisitor.MODELNODE);
    		if(node!=null) {
    			if(pyexp.getOverlay() != null) {
    				final OverlayInfo i = pyexp.getOverlay().get(node.getUID());
    				if(i!=null && null != i.size)
    					return i.size;
    			}
        		if(node instanceof IHost && ((IHost)node).hasEmulationProtocol()) {
        			rv*=1.3;
        		}
        		else if(node instanceof WrappedNet && !((WrappedNet)node).expanded) {
        			rv *= 1.5;
        		}
        		else if(node instanceof ILink){
        			if(((ILink)node).getTrafficIntensity(pyexp.getDataset())>0.0) {
            			rv*=1.5;
        			}
        		}
    		}
			return rv;
		}
		
    }

	protected static int __getColor(Dataset d, IModelNode node, HashMap<Long,OverlayInfo> overlay) {
		if(overlay != null) {
			final OverlayInfo i = overlay.get(node.getUID());
			if(i!=null && null != i.fill_color) {
				return i.fill_color;
			}
		}
		if(node instanceof IHost){
			IHost h = (IHost)node;
			SimpleDatum sd = h.getRuntimeValueByName(ModelNodeVariable.traffic_intensity(), d);
			if(sd == null)
				return heat_map.getColor(0);
			return heat_map.getColor(Float.parseFloat(sd.value));
		}else if(node instanceof ILink && node.getHub()){
			final double t=((ILink)node).getTrafficIntensity(d);
			if(t==0.0)
				return ColorLib.rgb(77,30,7);
			return heat_map.getColor(t);
		}else if(node instanceof ILink){
			final double t=((ILink)node).getTrafficIntensity(d);
			if(t==0.0)
				return ColorLib.rgb(75,75,75);
			return heat_map.getColor(t);
		}
		else if(node instanceof WrappedNet) {
			SimpleDatum sd = ((WrappedNet)node).getTrafficIntensity(d);
			if(sd == null)
				return ColorLib.rgb(200,200,200);
			return heat_map.getColor(Float.parseFloat(sd.value));
		}
		else if(node instanceof INet){
			return ColorLib.rgb(200,200,200);
		}
		return ColorLib.rgb(75,75,75);
	}
	
	protected static int __getColor(IModelNode node, HashMap<Long,OverlayInfo> overlay, boolean from_edge) {
		if(overlay != null) {
			final OverlayInfo i = overlay.get(node.getUID());
			if(i!=null && null != i.fill_color)
				return i.fill_color;
		}
		if(node instanceof IHost){
			return heat_map.getColor(0.0);
		}else if(node instanceof ILink && node.getHub()){
			return ColorLib.rgb(77,30,7);
		}else if(node instanceof ILink){
			return ColorLib.rgb(75,75,75);
		}else if(node instanceof INet){
			return ColorLib.rgb(200,200,200);
		}
		else if(node instanceof PortalNetwork) {
			if(from_edge)
				return ColorLib.rgb(75,75,75);
			return heat_map.getColor(0.0);
		}
		return ColorLib.rgb(75,75,75);
	}
	
	protected static int __getStrokeColor(IModelNode node, HashMap<Long,OverlayInfo> overlay) {
		if(overlay != null) {
			final OverlayInfo i = overlay.get(node.getUID());
			if(i!=null && null != i.stroke_color)
				return i.stroke_color;
		}
		if((node instanceof jprime.Host.IHost && ((jprime.Host.IHost)node).hasEmulationProtocol()) || node instanceof PortalNetwork ) {
			return ColorLib.rgb(147,112,219);
		}
		return ColorLib.gray(180);
	}
	
	protected static int __getShape(IModelNode node, HashMap<Long,OverlayInfo> overlay) {
		if(overlay != null) {
			final OverlayInfo i = overlay.get(node.getUID());
			if(i!=null && null != i.shape)
				return i.shape;
		}
		if(node instanceof jprime.Router.IRouter) {
			return Constants.SHAPE_ELLIPSE;
		}
		else if(node instanceof jprime.Host.IHost) {
			return Constants.SHAPE_RECTANGLE;
		}
		else if(node instanceof jprime.Link.ILink) {
			return Constants.SHAPE_DIAMOND;
		}
		else if(node instanceof WrappedNet && !((WrappedNet)node).expanded) {
			return Constants.SHAPE_HEXAGON;
		}
		else if(node instanceof PortalNetwork) {
			return Constants.SHAPE_TRIANGLE_UP;
		}
		
		return Constants.SHAPE_NONE;
	}

    
} 



