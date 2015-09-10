package slingshot.experiment;

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


import java.awt.BorderLayout;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import jprime.DynamicTrafficFactory;
import jprime.Experiment;
import jprime.IModelNode;
import jprime.ModelNode;
import jprime.State;
import jprime.StatusListener;
import jprime.EmulationProtocol.IEmulationProtocol;
import jprime.Host.IHost;
import jprime.Interface.IInterface;
import jprime.Link.ILink;
import jprime.Net.INet;
import jprime.Net.Net;
import jprime.OpenVPNEmulation.IOpenVPNEmulation;
import jprime.TrafficPortal.ITrafficPortal;
import jprime.partitioning.Alignment;
import jprime.partitioning.Partition;
import jprime.partitioning.Partitioning;
import jprime.util.ComputeNode;
import jprime.util.GraphOverlay.OverlayInfo;
import jprime.util.PartTlvPair;
import jprime.util.Portal;
import jprime.variable.Dataset;
import jprime.visitors.TLVVisitor;
import monitor.commands.SetupContainerCmd;
import monitor.commands.StateExchangeCmd;
import monitor.commands.VarUpdate;
import monitor.commands.SetupContainerCmd.NIC;
import monitor.core.ExpRunner;
import monitor.core.IController;
import monitor.core.IExpListenter;
import monitor.core.LocalController;
import monitor.core.LocalEmulatedController; //zzz_LOCAL_EMULATED
import monitor.core.Provisioner;
import monitor.deployers.ProtoGeniDeployer;
import monitor.provisoners.PreAllocatedProvisioner;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import prefuse.Display;
import prefuse.Visualization;
import prefuse.util.GraphicsLib;
import prefuse.util.display.DisplayLib;
import prefuse.controls.PanControl;
import prefuse.controls.WheelZoomControl;
import prefuse.controls.ZoomControl;
import prefuse.controls.ZoomToFitControl;
import prefuse.data.Edge;
import prefuse.data.Node;
import prefuse.visual.AggregateItem;
import prefuse.visual.DecoratorItem;
import prefuse.visual.EdgeItem;
import prefuse.visual.VisualItem;
import prefuse.visual.sort.ItemSorter;
import slingshot.Application;
import slingshot.Util;
import slingshot.Util.Type;
import slingshot.configuration.ConfigurationHandler;
import slingshot.environment.configuration.EnvironmentConfiguration;
import slingshot.environment.configuration.RemoteClusterConfiguration;
import slingshot.experiment.ProjectLoader.ProjectState;
import slingshot.experiment.compile.CompileWizard;
import slingshot.model.LogHandler;
import slingshot.model.ModelView;
import slingshot.model.PrimeAttrTreeBuilder;
import slingshot.model.PrimeTree;
import slingshot.spyconsole.SPyConsole;
import slingshot.visualization.AggregateDragControl;
import slingshot.visualization.LiveGraphManager;
import slingshot.visualization.NetworkView;
import slingshot.visualization.ResetRowVisitor;
import slingshot.visualization.VizVisitor;

/**
 * 
 * This class wraps the state associated with a jPRIME experiment and
 * provides the ability to  visualize, deploy, and monitor an experiment.
 * 
 * @author Nathanael Van Vorst
 * @author Eduardo Tibau
 * 
 */
public class PyExperiment implements IExpListenter {
	private static class ExpThread {
		/**
		 * the local controller 
		 */
		public IController exp_controller;
		public final ExpRunner expRunner;
		public final LocalController local_controller;
		public final LocalEmulatedController local_emulated_controller;//zzz_LOCAL_EMULATED
		public final Dataset dataset;
		public List<ComputeNode> computeNodes;
		public Partitioning parting;
		public Double base_real_time;

		public HashMap<Integer, ArrayList<IHost>> emuNodes;
		public ExpThread (LocalController local_controller, Dataset dataset) {
			this.expRunner=null;
			this.local_controller=local_controller;
			this.local_emulated_controller=null;//zzz
			this.dataset=dataset;
			this.computeNodes=null;
			this.parting=null;
			this.exp_controller=null;
			this.emuNodes=null;
			this.base_real_time=null;
		}
		public ExpThread (LocalEmulatedController local_emulated_controller, Dataset dataset) { //zzz
			this.expRunner=null;
			this.local_controller=null;
			this.local_emulated_controller=local_emulated_controller;	
			this.dataset=dataset;
			this.computeNodes=null;
			this.parting=null;
			this.exp_controller=null;
			this.emuNodes=null;
			this.base_real_time=null;
		}
		
		
		public ExpThread (ExpRunner expRunner, Dataset dataset) {
			this.expRunner=expRunner;
			this.local_controller=null;
			this.local_emulated_controller=null;//zzz
			this.dataset=dataset;
			this.computeNodes=null;
			this.parting=null;
			this.exp_controller=null;
			this.emuNodes=null;
		}
		public void forceStop() {
			if(expRunner == null) {
				local_controller.shutdown(false, false);
			}
			else {
				expRunner.forceStop();
			}
		}
	}
	
	/**
	 * The jython console
	 */
	private SPyConsole console=null;

	/**
	 * the model view in the gui
	 */
	protected ModelView modelViewCache=null;
	
	/**
	 * the jPRIME experiment 
	 */
	protected Experiment exp;
	
	/**
	 * a thread which monitors the execution of an experiment 
	 */
	protected ExpThread expRunner;
	
	/**
	 * the log console in the gui 
	 */
	protected LogHandler logTab=null;
	
	/**
	 * a reference to the location of where intermediate compilation/results are stored
	 */
	protected IFolder resultsFolder;
	
	/**
	 * used to center the prefuse visualization
	 */
	protected ZoomToFitControl zoomToFitControl;

	/**
	 * the last time a state update was received 
	 */
	protected double lastUpdate=0;

	/**
	 *  the evironment that the experiment is currently deployed to
	 */
	protected EnvironmentConfiguration envConf;

	/**
	 * the model node which is currently selected in the prefuse gui 
	 */
	public IModelNode selected;
	
	/**
	 * a wrapper for the tool which plots/monitor state updates of a specific model node in real-time
	 */
	public LiveGraphManager livegraph=null;

	/**
	 * whether this experiment has been vizualized yet 
	 */
	protected boolean visualized=false;
	
	/**
	 * the current prefuse layout 
	 */
	protected NetworkView layout;
	private int depth=2;
	
	/**
	 *  the actual prefuse graph display
	 */
	protected Display display;
	
	/**
	 * override size, shape, color or nodes/links
	 */
	protected HashMap<Long,OverlayInfo> overlay=null;

	/**
	 * whether this project has been saved (i.e. new and not in database) 
	 */
	protected boolean beenSaved=false;
	
	
    public final TreeMap<Long,Boolean> expandedNets = new TreeMap<Long,Boolean>();
    
    private HashMap<Long,IModelNode> areaOfInterest = new HashMap<Long,IModelNode>();
    
	/**
	 * The constructor
	 * 
	 * @param expName the name of the experiment
	 */
	public PyExperiment(String expName){
		Experiment temp = null;
		try{
			temp = Application.db.loadExperiment(expName);
		}catch(Exception e){
		}
		if ( temp == null ){
			exp = Application.db.createExperiment(expName, true);
		}else{
			exp = temp;
			beenSaved=true;
			switch(exp.getState()) {
			case CLOSED:
				throw new RuntimeException("The exp has the sate CLOSED! How did this happen?");
			case PRE_COMPILED:
				//no op
				break;
			case COMPILED:
			case COMPILING:
			case PARTITIONED:
			case PARTITIONING:
			case RUNNING:
			case RUNNING_DETACHED:
			case SETTING_UP:
			case SETUP:
			case STARTING:
				exp.setState(State.COMPILED);
			}
		}
		expRunner=null;
	}
	
	public HashMap<Long,OverlayInfo> getOverlay() {
		return overlay;
	}
	public void setOverlay(HashMap<Long,OverlayInfo> o) {
		this.overlay=o;
	}

	/**
	 * @return whether this project has been saved (i.e. new and not in database) 
	 */
	public boolean hasBeenSaved() {
		return beenSaved;
	}

	/** 
	 * cleanup resources associated with this experiment
	 * @param p
	 */
	public void cleanup(ProjectState p) {
		Net topnet = getRootNode();
		new ResetRowVisitor(topnet);
		layout.cleanup();
		display.removeAll();
		if(p ==null)
			p = ProjectLoader.getProjectState(this.getName());
		if(p!=null) {
			p.view.prefuse.panel.removeAll();
		}
	}
	
	/**
	 * @return
	 */
	public void setTime(Double sim) {
		if(null == modelViewCache) {
			try {
				modelViewCache = ProjectLoader.getProjectState(getName()).view;
			} catch(Exception e) {
				modelViewCache=null;
			}
		}
		if(null == modelViewCache) return;
		if(sim != null && expRunner != null) {
			double real = System.currentTimeMillis()/1000.0;
			if(expRunner.base_real_time == null) {
				expRunner.base_real_time=real - sim;
			}
			real-=expRunner.base_real_time;
			modelViewCache.setTime(real,sim);
		}
		else {
			modelViewCache.setTime(-1, -1);
		}
	}
	
	/**
	 * @return
	 */
	public PrimeTree getAttrTree() {
		if(null == modelViewCache) {
			try {
				modelViewCache = ProjectLoader.getProjectState(getName()).view;
			} catch(Exception e) {
				modelViewCache=null;
			}
		}
		if(null != modelViewCache) {
			return modelViewCache.getAttrTree();
		}
		return null;
	}
	
	/**
	 * @return
	 */
	public Experiment getExperiment(){
		return this.exp;
	}

	/**
	 * @param e
	 */
	public void setExperiment(Experiment e){
		this.exp = e;
	}

	/**
	 * @return
	 */
	public ZoomToFitControl getZoomToFitControl() {
		return zoomToFitControl;
	}

	/* (non-Javadoc)
	 * @see monitor.core.IExpListenter#getController()
	 */
	@Override
	public IController getController() {
		if(expRunner!=null)
			return expRunner.exp_controller;
		return null;
	}

	/* (non-Javadoc)
	 * @see monitor.core.IExpListenter#setController(monitor.core.IController)
	 */
	@Override
	public void setController(IController c) {
		if(expRunner!=null) {
			expRunner.exp_controller=c;
		}
		else {
			try {
				throw new RuntimeException("set the controller when no exp was running!");
			} catch(Exception e) {
				Util.dialog(Type.ERROR, "Error setting controller!",Util.getStackTraceAsString(e));
			}
		}
	}

	public void setCompileInfo(Partitioning parting, List<ComputeNode> computeNodes) {
		if(expRunner!=null) {
			expRunner.parting=parting;
			expRunner.computeNodes=computeNodes;
		}
		else {
			try {
				throw new RuntimeException("set the compile info when no exp was running!");
			} catch(Exception e) {
				Util.dialog(Type.ERROR, "Error setting compile info!",Util.getStackTraceAsString(e));
			}
		}
	}
	public void setEmuHosts(HashMap<Integer, ArrayList<IHost>> emuNodes) {
		if(expRunner!=null) {
			expRunner.emuNodes=emuNodes;
		}
		else {
			try {
				throw new RuntimeException("set the emunodes when no exp was running!");
			} catch(Exception e) {
				Util.dialog(Type.ERROR, "Error setting emuNodes!",Util.getStackTraceAsString(e));
			}
		}
	}

	/**
	 * @return
	 */
	public EnvironmentConfiguration getEnvConf() {
		return envConf;
	}

	/**
	 * @param envConf
	 */
	public void setEnvConf(EnvironmentConfiguration envConf) {
		this.envConf = envConf;
	}

	/**
	 * @return
	 */
	public IFolder getResultsFolder() {
		return resultsFolder;
	}

	/**
	 * @param resultsFolder
	 */
	public void setResultsFolder(IFolder resultsFolder) {
		this.resultsFolder = resultsFolder;
	}

	/**
	 * @param name
	 * @return
	 */
	public static Experiment loadExperiment(String name){
		return Application.db.loadExperiment(name);
	}
	
	/**
	 * 
	 */
	public void startViz(){
		startVisualization();
	}
	
	/**
	 * 
	 */
	public void centerViz(){
	    Visualization vis = display.getVisualization();
	    Rectangle2D bounds = vis.getBounds(Visualization.ALL_ITEMS);
	    GraphicsLib.expand(bounds, 50 + (int)(1/display.getScale()));
	    DisplayLib.fitViewToBounds(display, bounds, 500);
	}
	
	/**
	 * 
	 */
	public void focusViz(){
	    focus();
	}
	
	/**
	 * 
	 */
	public void startVisualization(){
		boolean do_async=false;
		try {
			if(PlatformUI.getWorkbench() != null && 
					PlatformUI.getWorkbench().getActiveWorkbenchWindow() != null &&
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell() != null) {
				final Shell dialogshell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
				startVisualizationWithProgress(dialogshell);
			}
			else {
				do_async=true;
			}
		} catch(Exception e) {
			do_async=true;
		}
		if(do_async) {
			 PlatformUI.getWorkbench().getDisplay().asyncExec( new Runnable() {
				 @Override
				 public void run() {
						final Shell dialogshell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
						assert(dialogshell!= null);
						startVisualizationWithProgress(dialogshell);
				 }
			 });
		}
	}
	
	public void startVisualizationWithProgress(final Shell dialogshell){
		ProgressMonitorDialog dialog = new ProgressMonitorDialog(dialogshell); 
		try {
			dialog.run(true, false, new IRunnableWithProgress(){ 
				public void run(final IProgressMonitor monitor) { 
					monitor.beginTask("Loading...",1000);
					Runnable r = new Runnable() {
						@Override
						public void run() {
							startVisualization(dialogshell, monitor);
						}
					};
					final Thread t= new Thread(r);
					t.start();
					try {
						t.join();
					} catch (InterruptedException e) {
					}
					monitor.done(); 
				}
			});
		} catch (InvocationTargetException e) {
		} catch (InterruptedException e) { }
	}
	
	public void selectNode(IModelNode node) {
		if(node instanceof INet) {
			selected=node;
			executeCommand("sel = exp.selected", true);
			layout.setSelectedAgg(node.getRow());
			layout.setSelectedRow(-1);
			layout.setSelectedEdgeRow(-1);
			showAttrs();
		}
		else if(node instanceof IHost) {
			selected=node;
			executeCommand("sel = exp.selected", true);
			layout.setSelectedRow(node.getRow());
			layout.setSelectedEdgeRow(-1);
			layout.setSelectedAgg(-1);
			showAttrs();
			focus();
		}
		else if(node instanceof ILink) {
			selected=node;
			executeCommand("sel = exp.selected", true);
			layout.setSelectedEdgeRow(node.getRow());
			layout.setSelectedRow(-1);
			layout.setSelectedAgg(-1);
			showAttrs();
			focus();
		}
	}
	
	public void focus() {
		IModelNode node = selected;
		while(! (node instanceof IHost || node instanceof ILink || node instanceof INet) && node != null) {
			node = node.getParent();
		}
		if(node != null && node.getRow()>-1) {
			VisualItem vi = null;
			if(node instanceof IHost) {
				Node n = layout.g.getNode(node.getRow());
				vi = layout.getVisualItem(NetworkView.NODES, n);
			}
			else if(node instanceof ILink) {
				Edge e = layout.g.getEdge(node.getRow());
				vi = layout.getVisualItem(NetworkView.EDGES, e);
			}
			else if(node instanceof INet) {
				vi = layout.at.getItem(node.getRow());
			}
			if(null != vi) {
				//Rectangle2D d = layout.getBounds(Visualization.FOCUS_ITEMS);
				double x=vi.getBounds().getCenterX();
				double y=vi.getBounds().getCenterY();
				display.animatePanAndZoomToAbs(new Point2D.Double(x,y), 5, 500);
			}
		}
	}
	
	public int getVizDepth() {
		return depth;
	}
	
	public void setVizDepth(int depth) {
		this.depth=depth;
	}
	
	public synchronized HashMap<Long,IModelNode> getAreaOfInterest() {
		return areaOfInterest;
	}
	
	/**
	 * 
	 */
	private synchronized void startVisualization(Shell dialogshell, IProgressMonitor monitor){
		if(this.exp == null){
			this.println("exp is not set");
			return;
		}

		ProjectState p = ProjectLoader.getProjectState(this.getName());
		Net topnet = getRootNode();
		if(topnet == null) {
			exp.createTopNet("topnet");
			topnet = getRootNode();
		}
		//this.println("exp is "+exp.getName()+", "+topnet.getUniqueName());
		try {
			if(isVisualized()){
				cleanup(p);
			}
			
			final HashMap<Long,IModelNode> newAreaOfInterest = new HashMap<Long,IModelNode>();
			layout = new NetworkView(topnet, this, dialogshell, depth, monitor, newAreaOfInterest);
			display = new Display(layout);
			
			// set up the display

			display.setBounds(p.view.prefuse.panel.getBounds());
			display.pan(p.view.prefuse.panel.getWidth()/2, p.view.prefuse.panel.getHeight()/2);
			display.setHighQuality(true);

			//display.addControlListener(new FocusControl(1));
			display.addControlListener(new AggregateDragControl(this));
			display.addControlListener(new ZoomControl());
			display.addControlListener(new PanControl());
			display.addControlListener(new WheelZoomControl());
			zoomToFitControl = new ZoomToFitControl(Visualization.ALL_ITEMS, 50, 500, prefuse.controls.Control.RIGHT_MOUSE_BUTTON);

			display.addControlListener(zoomToFitControl);
			display.setItemSorter(new MyItemSorter());

			p.view.prefuse.panel.add(display, BorderLayout.CENTER);
			setVisualized(true);
			IController con =getController();
			if(con != null) {
				Set<IModelNode> add = new TreeSet<IModelNode>();
				for(Entry<Long, IModelNode> e : newAreaOfInterest.entrySet()) {
					IModelNode rm = areaOfInterest.remove(e.getKey());
					if(rm == null) {
						add.add(e.getValue());
					}
				}
				if(areaOfInterest.size()>0) {
					con.removeFromAreaOfInterest(areaOfInterest.values());
				}
				//else System.out.println("nothing to remove from AOI");
				if(add.size()>0) {
					con.addToAreaOfInterest(add);
				}
				//else System.out.println("nothing to add to AOI");
				areaOfInterest=newAreaOfInterest;
			}
			else {
				areaOfInterest=newAreaOfInterest;
			}
			
			
		} catch(Exception e) {
			Util.dialog(Type.ERROR, "Error starting viz!", Util.getStackTraceAsString(e));
		}
	}
	
	/**
	 * @author vanvorst
	 * We need to sort the items so when click on a point we return sub-nets before networks
	 * 
	 * 
	 */
	public static class MyItemSorter extends ItemSorter {
		/* (non-Javadoc)
		 * @see prefuse.visual.sort.ItemSorter#score(prefuse.visual.VisualItem)
		 */
		@Override
		public int score(VisualItem item) {
	        int type = ITEM;
	        if ( item instanceof EdgeItem ) {
	            type = EDGE;
	        } else if ( item instanceof AggregateItem ) {
	            type = AGGREGATE;
	        } else if ( item instanceof DecoratorItem ) {
	            type = DECORATOR;
	        }
	        switch(type) {
	        case DECORATOR:
	        	return 1000001;
	        case ITEM:
	        case EDGE:
	        	return 1000000;
	        case AGGREGATE:
	        	return (Integer)item.get(VizVisitor.DEPTH);
	        }
	        return 0;
		}
		
	}
	
	/**
	 * 
	 */
	public void pauseViz(){
		layout.cancel("force");
	}

	/**
	 * 
	 */
	public void resumeViz(){
		layout.run("force");
	}

	/**
	 * @return
	 */
	public boolean isVisualized() {
		return visualized;
	}

	/**
	 * @param visualized
	 */
	private void setVisualized(boolean visualized) {
		this.visualized = visualized;
	}

	/**
	 * 
	 */
	public void showAttrs(){
		try {
			org.eclipse.swt.widgets.Display.getDefault().asyncExec(new PrimeAttrTreeBuilder(ProjectLoader.getProjectState(this.getName())));
		} catch(Exception e) {}
	}

	/**
	 * @return
	 */
	public NetworkView getLayout() {
		return layout;
	}

	/**
	 * @param layout
	 */
	public void setLayout(NetworkView layout) {
		this.layout = layout;
	}

	/**
	 * @param console
	 */
	public void setConsole(SPyConsole console) {
		this.console = console;
	}
	

	public SPyConsole getConsole() {
		return this.console;
	}


	/**
	 * @param cmd
	 * @param silent
	 */
	public void executeCommand(String cmd, boolean silent){
		if(console != null) {
			if(silent)
				console.exec(cmd);
			else
				console.executeCommand(cmd);
		}
	}

	/**
	 * @param netName
	 * @return
	 */
	public Net createTopNet(String netName){
		return exp.createTopNet(netName);
	}

	/**
	 * @return
	 */
	public Net getRootNode(){
		 Net rv = null;
		 try {
			rv= exp.getRootNode();
		} catch(IndexOutOfBoundsException e) {
			//can happen because the python consoel tries to get the root not too early
		}
		return rv;
	}

	/**
	 * 
	 */
	public void compile(final String prefix) {//zzz_compile
		if(exp.getState().lte(State.PRE_COMPILED)){
			ProgressMonitorDialog dialog = new ProgressMonitorDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell()); 
			try {
				dialog.run(true, false, new IRunnableWithProgress(){ 
					public void run(final IProgressMonitor monitor) { 
						monitor.beginTask("Compiling experiment '" + exp.getName()+"'", 100);
						Runnable r = new Runnable() {
							@Override
							public void run() {
								HashMap<String, String> params = new HashMap<String, String>();
								params.put("base_ip_address", prefix);
								exp.compile(new SlinghotStatusListener(monitor), params);
								System.out.println("GreatCompile zzz");

								monitor.done(); 
							}
						};
						Thread t= new Thread(r);
						t.start();
						try {
							t.join();
						} catch (InterruptedException e) {
						}
						ProjectLoader.updateCommandAvailablility(exp.getName());
					} 
				});
			} catch (InvocationTargetException e) {
			} catch (InterruptedException e) { }
		}
	}


	/**
	 * @param partString
	 * @param directoryToStoreFiles
	 * @return
	 * @throws IOException
	 */
	public final PartTlvPair partition(String partString, String directoryToStoreFiles, Set<Portal> portals, List<ComputeNode> computeNodes) throws IOException {
		if(exp == null) {
			throw new RuntimeException("What happened? exp is null!");
		}
		if(getState().lt(State.COMPILED)) {
			final Shell shell = new Shell(org.eclipse.swt.widgets.Display.getDefault());
			CompileWizard cw = new CompileWizard();
			WizardDialog configdlg = new WizardDialog (shell, cw);
    		configdlg.open();
    		String ipprefix = cw.getIPPrefix();
    		if(ipprefix != null) {
    			compile(ipprefix);
    		}
    		else {
    			throw new RuntimeException("What happened?");
    		}
		}
		ProjectState ps = ProjectLoader.getProjectState(this.getName());
		logTab=ps.view.logHandler;
		println("\t***************************************");
		println("\tPartitiong Model, partString="+partString);
		println("\t***************************************");
		if(portals == null)
			portals = new HashSet<Portal>();
		if(computeNodes == null)
			computeNodes = new ArrayList<ComputeNode>();
		Partitioning partitioning = exp.partition(partString, portals, computeNodes);
		
		
//		//zzz start emulated node inquiry 
//	    System.out.println ("ZZZ Root Node:"+partitioning.getTopnet());
//		//find the hosts...
//		HashMap<Long, IHost> vms = new HashMap<Long,IHost>();
//		for(IEmulationProtocol p : exp.getEmuProtocols()) {
//			if(p instanceof ITrafficPortal) {
//				IHost h = (IHost)p.getParent().getParent();
//				for(IModelNode c : h.getAllChildren()) {
//					try {
//						if(c instanceof IInterface) {
//							//portalRoutes.addAll(((IInterface)c).getReachableNetworks());
//						}
//					} catch(Exception e) {}
//				}
//			}
////			else if(p instanceof IOpenVPNEmulation) {
////				IHost h = (IHost)p.getParent().getParent();
////				vpns.put(h.getUID(),h);
////			}
//			else {
//				IHost h = (IHost)p.getParent().getParent();
//				System.out.println("PyExperiment:  h.getUID()= "+h.getUID()+", h = "+h);
//				vms.put(h.getUID(),h);
//			}
//		}
//		//setup the machine map
////		for(IHost h : vms.values()) {
////			Set<Integer> pid = h.getAlignments(parting.partitioning);
////			if (pid.size() != 1)
////				throw new RuntimeException("Should never happen!");
////			for(Integer i : pid) {
////				Alignment a = parting.partitioning.findAlignment(i);
////				if(a == null)
////					throw new RuntimeException("Should never happen!");
////				if(!vmMap.containsKey(a.getPartId())) {
////					vmMap.put(a.getPartId(), new ArrayList<IHost>());
////				}
////				//if(Utils.DEBUG)System.out.println("\tAdding "+node.getUniqueName()+" to part "+a.getPartId());
////				vmMap.get(a.getPartId()).add(h);
////			}
////		}
//	    
//		//zzz_end 
		
		println("\t***************************************");
		println("\tCreating TLV");
		println("\t***************************************");
		Map<Integer,String> tlvs = new HashMap<Integer, String>();
		new TLVVisitor(exp.getMetadata(), partitioning, directoryToStoreFiles, exp.getName(), tlvs, true);
		System.out.println("\n  TLV file location:" +tlvs+"\n");
		println("\t***************************************");
		println("\tFinished");
		println("\t***************************************");
		return new PartTlvPair(exp.getName(),directoryToStoreFiles,partitioning, tlvs);
	}

	/**
	 * @return
	 */
	public String getName() {
		return exp.getName();
	}

	/**
	 * 
	 */
	public void save(boolean with_dialog){
		if(with_dialog) {
			ProgressMonitorDialog dialog = new ProgressMonitorDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell()); 
			try {
				dialog.run(true, false, new IRunnableWithProgress(){ 
					public void run(final IProgressMonitor monitor) { 
						monitor.beginTask("Saving experiment '" + exp.getName()+"'", exp.countInCoreObjs());
						Runnable r = new Runnable() {
							@Override
							public void run() {
								exp.save(new SlinghotStatusListener(monitor,exp.countInCoreObjs()));
							}
						};
						Thread t= new Thread(r);
						t.start();
						try {
							t.join();
						} catch (InterruptedException e) {
						}
						monitor.done(); 
					} 
				});
			} catch (InvocationTargetException e) {
			} catch (InterruptedException e) { }
		}
		else {
			exp.save(new StatusListener());
		}
		ProjectLoader.updateCommandAvailablility(exp.getName());
		beenSaved=true;
	}
	
	/**
	 * @return
	 */
	public State getState() {
		if(exp==null)
			return State.CLOSED;
		return exp.getState();
	}

	/**
	 * @param state
	 */
	public void setState(State state) {
		this.exp.setState(state);
		ProjectLoader.updateCommandAvailablility(exp.getName());
	}

	/**
	 * @return
	 */
	public IProject getProject(){
		return ResourcesPlugin.getWorkspace().getRoot().getProject(exp.getName());
	}


	/* (non-Javadoc)
	 * @see monitor.core.IExpListenter#finishedExperiment(boolean)
	 */
	@Override
	public void finishedExperiment(boolean failed) {
		setTime(null);
		this.println("The experiment "+exp.getName()+" has finished running. State="+(failed?"FAILURE":"SUCCESS"));
		try {
			if(failed) {
				if(logTab!=null)
					Util.dialog(Type.ERROR,"", "'"+exp.getName()+"' terminated, but it did not run to a successful completion.");
			}
			else {
				if(logTab!=null)
					Util.dialog(Type.NOTICE,"","'"+exp.getName()+"' has successfully terminated.");
			}
		}catch(Exception e) {
		}
		this.setState(State.PARTITIONED);
		this.expRunner=null;
		ProjectLoader.updateCommandAvailablility(exp.getName());
	}

	/* (non-Javadoc)
	 * @see monitor.core.IExpListenter#println(java.lang.String)
	 */
	@Override
	public void println(String msg) {
		if(logTab != null) {
			logTab.printLineToLog(msg);
		}
		else {
			System.out.println(msg);
		}
	}

	/* (non-Javadoc)
	 * @see monitor.core.IExpListenter#handleStateUpdate(monitor.commands.StateExchangeCmd)
	 */
	@Override
	public void handleStateUpdate(StateExchangeCmd update) {
		if(expRunner ==null || expRunner.dataset == null) {
			println("Cannot update the state! the dataset was null!");
			return;
		}
		//System.out.println("Update for "+update.getUid()+" time="+update.getTime());
		//for(VarUpdate v : update.getUpdates()) {
		//	System.out.println("\t"+jprime.variable.ModelNodeVariable.int2name(v.var_id)+" --> "+v.asString());
		//}
		for(VarUpdate u : update.getUpdates())
			expRunner.dataset.addDatum(update.getUid(), u.var_id, update.getTime(), u.asString());

		if(update.getTime()-lastUpdate>=1.0) {
			//hack just to make sure the buttons get grey out properly
			ProjectLoader.updateCommandAvailablility(exp.getName());
			lastUpdate=update.getTime();
		}
		setTime(expRunner.dataset.getCurTime());
		//println("pyexp handleStateUpdate:"+u);
		PrimeTree attrTree = getAttrTree();
		try {
			if(attrTree != null) {
				if(attrTree.hasUID(update.getUid())) {
					showAttrs();
				}
			}
		} catch(Exception e) {}

		try {
			if(livegraph != null) {
				for(VarUpdate u : update.getUpdates())
					livegraph.handleStateUpdate(update.getUid(), update.getTime(), u.var_id, u.asString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			println("Error applying state update "+update+" to live graph!");
		}
	}

	/**
	 * @return
	 */
	public HashMap<Integer, ArrayList<IHost>> getEmuNodeMap() {
		if(expRunner!=null)
			return expRunner.emuNodes;
		return null;
	}

	/**
	 * @return
	 */
	public Partitioning getPartitioning() {
		if(expRunner!=null)
			return expRunner.parting;
		return null;
	}

	/**
	 * @return
	 */
	public List<ComputeNode> getComputeNodes() {
		if(expRunner!=null)
			return expRunner.computeNodes;
		return null;
	}

	
	/**
	 * @return
	 */
	public Dataset getDataset() {
		if(expRunner!=null)
			return expRunner.dataset;
		return null;
	}

	
	/**
	 * 
	 */
	public void terminateExperiment() {
		if(this.expRunner != null) {
			this.println("terminating "+exp.getName());
			this.expRunner.forceStop();
		}
		else {
			setState(State.COMPILED);
			ProjectLoader.updateCommandAvailablility(exp.getName());
		}
	}

	/**
	 * @param ec
	 * @param resultsPath
	 */
	public void launchExperiment(EnvironmentConfiguration ec, final String resultsPath) {
		if(this.expRunner == null) {
			Dataset dataset=new Dataset();
			
			setState(State.SETTING_UP);
			ProjectLoader.updateCommandAvailablility(exp.getName());
			Provisioner p=null;
			if(logTab!=null) {
				ProjectState ps = ProjectLoader.getProjectState(this.getName());
				logTab = ps.view.logHandler;
			}
			if(logTab!=null) {
				logTab.showLogTab();
			}
			switch(ec.getType()) {
			case LOCAL:
			{
				setState(State.PARTITIONED);
				PartTlvPair parting;
				try {
					parting = this.partition(ec.getPartitioningString(), resultsPath, new HashSet<Portal>(), new ArrayList<ComputeNode>());
					this.setState(jprime.State.SETUP);
					final LocalController lc = new LocalController(
							exp,parting,
							ec.runtime,
							ec.numProcs,
							ec.emuRatio,
							this,
							ConfigurationHandler.getPrimexDirectory(),
							true,
							ec.runtimeSymbolMap,
							resultsPath);
					lc.addToAreaOfInterest(getAreaOfInterest().values());
					this.expRunner = new ExpThread(lc, dataset);
					this.expRunner.exp_controller = expRunner.local_controller;
					this.expRunner.local_controller.start();
					this.expRunner.parting=parting.partitioning;
				} catch (IOException e) {
					println("Error setting up local run!"+e.toString());
					setState(State.PARTITIONED);
					return;
				}
			}
			break;
			case LOCAL_EMULATED: //zzz_LOCAL_EMULATED
			{
				setState(State.PARTITIONED);
				PartTlvPair parting;
				try {
					parting = this.partition(ec.getPartitioningString(), resultsPath, new HashSet<Portal>(), new ArrayList<ComputeNode>());
					this.setState(jprime.State.SETUP);
					final LocalEmulatedController lec = new LocalEmulatedController(
							exp,parting,
							ec.runtime,
							ec.numProcs,
							ec.emuRatio,
							this,
							ConfigurationHandler.getPrimexDirectory(),
							true,
							ec.runtimeSymbolMap,
							resultsPath);
					lec.addToAreaOfInterest(getAreaOfInterest().values());
					this.expRunner = new ExpThread(lec, dataset);
					this.expRunner.exp_controller = expRunner.local_emulated_controller;
					this.expRunner.local_emulated_controller.start();
					this.expRunner.parting=parting.partitioning;
					
					//zzz
					//System.out.println ("Before the null pointer");
					//List<ComputeNode> tempNodeList = expRunner.computeNodes;
					//System.out.println (expRunner.computeNodes);
					//print list
//					if (tempNodeList.isEmpty()==true){
//						System.out.println("No Content");
//					}
//					else{
//						System.out.println ("PyExperiment: Nodes in model:"+tempNodeList.size());
//						for(int i = 0; i < tempNodeList.size(); i++) {
//				            System.out.println(tempNodeList.get(i).toString());
//						}
//					}

					
				} catch (IOException e) {
					println("Error setting up local emulated run!"+e.toString());
					setState(State.PARTITIONED);
					return;
				}
			}
			break;
			
			
			case PROTO_GENI:
			case REMOTE_CLUSTER:
			{
				RemoteClusterConfiguration rc = (RemoteClusterConfiguration) ec;
				p = new PreAllocatedProvisioner(rc.getMaster(),rc.getSlaves());
				final CluterExpStarter ces = new CluterExpStarter(this, ec, p, resultsPath);
				final ExpRunner er = new ExpRunner(exp, ec.runtime, this, ces,getAreaOfInterest().values());
				this.expRunner = new ExpThread(er, dataset) ;
				this.expRunner.expRunner.start();
			}
				break;
			default:
				throw new RuntimeException("invalid envionrment!");
			}
			ProjectLoader.updateCommandAvailablility(exp.getName());
		}
		else {
			if(exp.getState() != State.RUNNING) {
				finishedExperiment(true);
				launchExperiment(ec,resultsPath);
			}
			else {
				if(logTab!=null) {
					Util.dialog(Type.NOTICE,"", "'"+exp.getName()+"' is already running!");
				}
				else {					
					this.println( "'"+exp.getName()+"' is already running!");
				}
			}
		}
	}
	
	public DynamicTrafficFactory createDynamicTrafficFactory(INet owner, String base_name) {
		if(owner == null || getController() ==null || getPartitioning()==null)
			throw new RuntimeException("the exp must be running!");
		return new DynamicTrafficFactory(owner, base_name, getController(), getPartitioning());
	}

	public DynamicTrafficFactory createDynamicTrafficFactory(INet owner) {
		if(owner == null || getController() ==null || getPartitioning()==null)
			throw new RuntimeException("the exp must be running!");
		return new DynamicTrafficFactory(owner, getController(), getPartitioning());
	}

	public DynamicTrafficFactory createDynamicTrafficFactory(String base_name) {
		if(getRootNode() == null || getController() ==null || getPartitioning()==null)
			throw new RuntimeException("the exp must be running!");
		return new DynamicTrafficFactory(getRootNode(), base_name, getController(), getPartitioning());
	}

	public DynamicTrafficFactory createDynamicTrafficFactory() {
		if(getRootNode() == null || getController() ==null || getPartitioning()==null)
			throw new RuntimeException("the exp must be running!");
		return new DynamicTrafficFactory(getRootNode(), getController(), getPartitioning());
	}


}
