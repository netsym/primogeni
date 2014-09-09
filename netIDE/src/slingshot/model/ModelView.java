package slingshot.model;

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



import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.contexts.IContextActivation;
import org.eclipse.ui.contexts.IContextService;
import org.eclipse.ui.part.ViewPart;

import slingshot.Activator;
import slingshot.Util;
import slingshot.Util.Type;
import slingshot.experiment.ProjectLoader;
import slingshot.experiment.ProjectLoader.ProjectState;


/**
 * Wraps the Prefuse Composite and the an attribute tree into a GUI component.
 * @author Nathanael Van Vorst
 * @author Eduardo Tibau
 */
public class ModelView extends ViewPart{
	public static String ID = "slingshot.views.model";
	public static final String VIEW_CONTEXT_ID = "slingshot.modelViewContext";

	private boolean disposing=false;
	private String experiment = "";

	public PythonConsoleHandler consoleHandler;
	public LogHandler logHandler;
	public PrefuseComposite prefuse;

	private CTabFolder tabFolder;
	private CTabItem consoleTab;
	private CTabItem logTab;
	private ExperimentViewCoolbar coolbar;
	private PrimeTree attrTree;
	private Spinner depth;
	private Button up;
	private IContextActivation context_activation;
	private Label simTime=null, slowdown=null;
	private String simTime_s="--", slowdownOverall_s="--", slowdownRecent_s="--";
	private double sim_time_cur=0, real_time_cur=0, sim_time_recent=0, real_time_recent=0;
	private class ACK implements Runnable {
		private boolean waiting=false;
		boolean iswaiting() {
			synchronized(this) {
				try {
					return waiting;
				} finally {
					waiting = true;
				}
			}
		}
	    public void run() {
	    	if(real_time_cur <= 0.0000001) {
	    		slowdownOverall_s="0.0";
	    	}
	    	else {
	    		slowdownOverall_s=String.format("%.3f",(sim_time_cur/real_time_cur));
	    	}
	    	final double dr = real_time_cur-real_time_recent;
	    	final double ds = sim_time_cur-sim_time_recent;
	    	if(dr <= 0 || ds<=0) {
	    		slowdownRecent_s=slowdownOverall_s;
	    	}
	    	else {
	    		slowdownRecent_s=String.format("%.3f",ds/dr);
	    	}
	    	if(simTime_s != null && slowdownOverall_s!=null && slowdownRecent_s!=null) {
	    		simTime.setText(simTime_s);
	    		slowdown.setText(slowdownOverall_s+" ("+slowdownRecent_s+")");
	    	}
	    	else {
	    		simTime.setText("--");
	    		slowdown.setText("--");
	    	}
			synchronized(this) {
				waiting = false;
			}
	    }
	}
	private ACK ack = new ACK();

	@Override
	public void createPartControl(Composite parent) {

		this.setPartName(experiment);
		context_activation=null;
		//Create the container and set the Layout
		Composite containerGroup = new Composite(parent, SWT.RESIZE);
		containerGroup.setLayout(new GridLayout(1,true));
		containerGroup.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL | GridData.VERTICAL_ALIGN_FILL | GridData.GRAB_VERTICAL));
		containerGroup.setFont(parent.getFont());

		SashForm hSashForm = new SashForm(containerGroup, SWT.VERTICAL);
		hSashForm.setLayoutData(new GridData(GridData.FILL_BOTH));
		hSashForm.SASH_WIDTH = 5;

		SashForm topSashForm = new SashForm(hSashForm, SWT.HORIZONTAL);
		topSashForm.setLayoutData(new GridData(GridData.FILL_BOTH));

		topSashForm.SASH_WIDTH = 5;

		Composite prefuseContainer = new Composite(topSashForm, SWT.RESIZE | SWT.BORDER);
		prefuseContainer.setLayout(new GridLayout(1,true));
		prefuseContainer.setLayoutData(new GridData(GridData.FILL_BOTH));
		prefuseContainer.setFont(parent.getFont());

		GridData prefuseData = new GridData(GridData.FILL_BOTH);
		prefuse = new PrefuseComposite(prefuseContainer,prefuseData);

		Composite attrContainer = new Composite(topSashForm, SWT.RESIZE | SWT.BORDER);
		attrContainer.setLayout(new GridLayout(4,true));
		attrContainer.setLayoutData(new GridData(GridData.FILL_BOTH));
		attrContainer.setFont(parent.getFont());

		final ProjectState ps = ProjectLoader.getProjectState(experiment);
		if(ps==null) {
			try {
				throw new RuntimeException("HERE!!!");
			}catch(Exception e) {
				Util.dialog(Type.ERROR, "Couldn't find project state for "+experiment, Util.getStackTraceAsString(e));
				return;
			}
		}
		attrTree = new PrimeTree(attrContainer, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL,null,ps.exp); 
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan=5;
		attrTree.setLayoutData(gd);
		attrTree.setLinesVisible(true);
		attrTree.setHeaderVisible(true);

		TreeColumn column1 = new TreeColumn(attrTree.tree, SWT.LEFT);
		column1.setText("Attribute");
		column1.setWidth(140);

		TreeColumn column2 = new TreeColumn(attrTree.tree, SWT.LEFT);	
		column2.setText("Value/Type");
		column2.setWidth(160);
		
		
		Label l = new Label(attrContainer, SWT.NONE);
		l.setText("View Depth");
		gd = new GridData(SWT.CENTER);
		gd.horizontalSpan=1;
		l.setLayoutData(gd);
		depth = new Spinner(attrContainer,SWT.BORDER);// Scale(attrContainer,SWT.BORDER);
		depth.setMinimum(2);
		depth.setMaximum(100);
		depth.setIncrement(1);
		depth.setSelection(2);
		depth.setVisible(true);
		depth.setEnabled(true);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan=1;
		depth.setLayoutData(gd);
		depth.addListener(SWT.MouseUp, new Listener() {
			@Override
			public void handleEvent(Event event) {
				if(depth.getSelection()!=ps.exp.getVizDepth()) {
					ps.exp.setVizDepth(depth.getSelection());
					ps.exp.executeCommand("startViz()", true);
					coolbar.getExperimentToolBar().resetVizIcon(true);
				}
			}
		});
		depth.addListener(SWT.Modify, new Listener() {
			@Override
			public void handleEvent(Event event) {
				if(depth.getSelection()!=ps.exp.getVizDepth()) {
					ps.exp.setVizDepth(depth.getSelection());
					ps.exp.executeCommand("startViz()", true);
					coolbar.getExperimentToolBar().resetVizIcon(true);
				}
			}
		});
		up = new Button(attrContainer, SWT.ARROW);
		up.setImage(Activator.getImage("icons/arrow_up.png"));
		up.setToolTipText("Change focus to parent.");
		gd = new GridData(GridData.HORIZONTAL_ALIGN_CENTER);
		gd.horizontalSpan=1;
		up.setLayoutData(gd);
		up.addListener(SWT.MouseUp, new Listener() {
			@Override
			public void handleEvent(Event event) {
				if(ps.exp.selected != null) {
					ps.exp.executeCommand("selectNode(sel.getParent())", true);
				}
			}
		});
		l = new Label(attrContainer, SWT.NONE);
		l.setText("");
		gd = new GridData(GridData.HORIZONTAL_ALIGN_CENTER);
		gd.horizontalSpan=1;
		l.setLayoutData(gd);
		
		Composite timeContainer = new Composite(attrContainer, SWT.RESIZE | SWT.BORDER);
		gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		gd.horizontalSpan=4;
		timeContainer.setLayoutData(gd);
		timeContainer.setLayout(new GridLayout(5,false));
		timeContainer.setFont(parent.getFont());
		
		l = new Label(timeContainer, SWT.NONE);
		l.setText("Time:");
		gd = new GridData(GridData.HORIZONTAL_ALIGN_END);
		gd.horizontalSpan=1;
		gd.grabExcessHorizontalSpace=false;
		gd.widthHint=35;
		l.setLayoutData(gd);

		simTime = new Label(timeContainer, SWT.NONE);
		gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.FILL_HORIZONTAL);
		gd.horizontalSpan=1;
		gd.grabExcessHorizontalSpace=false;
		gd.widthHint=60;
		simTime.setLayoutData(gd);
		simTime.setText("--");
		
		l = new Label(timeContainer, SWT.NONE);
		l.setText("Pacing:");
		gd = new GridData(GridData.HORIZONTAL_ALIGN_END);
		gd.horizontalSpan=1;
		gd.grabExcessHorizontalSpace=false;
		gd.widthHint=40;
		l.setLayoutData(gd);

		slowdown = new Label(timeContainer, SWT.NONE);
		gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.FILL_HORIZONTAL);
		gd.horizontalSpan=2;
		gd.grabExcessHorizontalSpace=true;
		slowdown.setLayoutData(gd);
		slowdown.setText("--");

		int[] weights = {75,25};
		topSashForm.setWeights(weights);

		Composite bottomContainer = new Composite(hSashForm, SWT.RESIZE );
		bottomContainer.setLayout(new GridLayout(1,true));
		bottomContainer.setLayoutData(new GridData(GridData.FILL_BOTH));
		bottomContainer.setFont(parent.getFont());

		tabFolder = new CTabFolder(bottomContainer, SWT.BORDER | SWT.RESIZE);
		tabFolder.setLayout(new GridLayout(1,true));
		tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));

		tabFolder.setSimple(false);
		tabFolder.setTabHeight(34);
		//tabFolder.setMinimizeVisible(true);
		//tabFolder.setMaximizeVisible(true);

		consoleTab = new CTabItem(tabFolder, SWT.NONE);
		Image consoleImage = Activator.getImage("icons/console_view.gif");
		consoleTab.setText("Console");
		consoleTab.setImage(consoleImage);

		logTab = new CTabItem(tabFolder, SWT.NONE);
		Image logImage = Activator.getImage("icons/page_white_text.png");
		logTab.setText("Log");
		logTab.setImage(logImage);

		GridData consoleData = new GridData(GridData.FILL_BOTH);

		consoleHandler = new PythonConsoleHandler(tabFolder, consoleData, experiment);

		logHandler = new LogHandler(tabFolder, consoleData,logTab);

		consoleTab.setControl(consoleHandler.getConsoleComposite());
		logTab.setControl(logHandler.getLogComposite());

		tabFolder.setSelection(consoleTab);
		coolbar = new ExperimentViewCoolbar(tabFolder,experiment);
		tabFolder.setTopRight(coolbar.getCoolbar());


		coolbar.showConsoleToolBar();

		tabFolder.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				if(tabFolder.getSelection().equals(consoleTab))
					coolbar.showConsoleToolBar();
				else if(tabFolder.getSelection().equals(logTab))
					coolbar.showLogToolBar();
				consoleHandler.getConsoleComposite().redraw();
				logHandler.getLogComposite().redraw();
			}
		});

		activateContext();
	}

	public void setTime(double real, double sim) {
		if(sim < 0 || real < 0) {
			simTime_s=null;
			slowdownOverall_s=null;
			slowdownRecent_s=null;
		}
		else {
			simTime_s=String.format("%.3f",sim);
			sim_time_cur=sim;
			real_time_cur=real;
			if(sim-sim_time_recent>=5 || real_time_recent >= real_time_cur) {
				sim_time_recent=sim;
				real_time_recent=real;
			}
		}
		if(!ack.iswaiting()) {
			Display.getDefault().asyncExec(ack);
		}
	}
	
	public void showLogTab() {
		tabFolder.setSelection(logTab);
	}


	@Override
	public void dispose() {
		if(!this.disposing) {
			/*try {
				ProjectState p = ProjectLoader.getProjectState(this.experiment);
				if(p != null && p.getState().gt(State.PARTITIONED)) {
					String title = "The experiment is still running!";
					String message = "Are you sure want to close "+p.exp.getName()+"? It appears to be running!";
					if(!Util.confirm(title, message)) {
						this.disposing=false;
						return;
					}
				}
			}
			catch(Exception e) {
				this.disposing=false;
			}*/
			this.disposing=true;
			ProjectLoader.closeExp(this.experiment, false);
			super.dispose();
			this.disposing=false;
		}
	}



	public void closeView() {
		experiment = null;
		consoleHandler.close();
		consoleHandler=null;
		logHandler=null;
		prefuse=null;
		tabFolder=null;
		consoleTab=null;
		logTab=null;
		coolbar=null;
		attrTree=null;
		if(context_activation!=null) {
			IContextService contextService = (IContextService)getSite().getService(IContextService.class);
			contextService.deactivateContext(context_activation);
		}
		this.getViewSite().getPage().hideView(this); 
		this.dispose();
	}

	private void activateContext() {
		IContextService contextService = (IContextService)getSite().getService(IContextService.class);
		context_activation=contextService.activateContext(VIEW_CONTEXT_ID);
	}

	@Override
	public void setFocus() {
		ProjectLoader.updateCommandAvailablility(this.experiment);
		consoleHandler.getConsole().requestFocus();
		if(logHandler != null) {
			logHandler.focus();
		}
	}

	@Override
	public void init(IViewSite site) throws PartInitException {
		super.init(site);

		experiment = getViewSite().getSecondaryId();
	}

	public CTabItem getLogTab() {
		return logTab;
	}

	public PrimeTree getAttrTree() {
		return attrTree;
	}
}
