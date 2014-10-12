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

import jprime.State;
import jprime.Host.IHost;

import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.PlatformUI;

import slingshot.Activator;
import slingshot.Util;
import slingshot.Util.Type;
import slingshot.experiment.IExpStateListener;
import slingshot.experiment.ProjectLoader;
import slingshot.experiment.ProjectLoader.ProjectState;
import slingshot.experiment.commands.ExperimentCompileHandler;
import slingshot.experiment.commands.ExperimentRunHandler;
import slingshot.visualization.LiveGraphManager;
import slingshot.visualization.OverlayWizard;
import slingshot.visualization.OverlayWizard.OverlayWizardDialog;

/**
 * @author Eduardo Pena
 * @author Nathanael Van Vorst
 *
 */
public class ExperimentToolBar implements Listener, IExpStateListener {

	private ProjectState ps;

	private ToolBar toolbar;

	private ToolItem startVizItem;
	private ToolItem pause_resumeVizItem;
	private ToolItem focusVizItem;
	private ToolItem centerVizItem;
	private ToolItem addOverlay;
	private ToolItem clearOverlay;
	private ToolItem compileItem;
	private ToolItem runItem;
	private ToolItem graphItem;

	Image pauseVizIcon;
	Image resumeVizIcon;
        //Image refreshVizIcon;

	public ExperimentToolBar(CoolBar coolbar, String expName) {
		this.ps = ProjectLoader.getProjectState(expName);
		createToolBarComposite(coolbar);
		ps.setExpStateListener(this);
	}

	public ToolBar getToolbar() {
		return toolbar;
	}

	public void setToolbar(ToolBar toolbar) {
		this.toolbar = toolbar;
	}

	private void createToolBarComposite(CoolBar coolbar) {


		Image startVizIcon = Activator.getImage("icons/refresh_viz.png");
		//refreshVizIcon = Activator.getImage("icons/refresh_viz.png");
		pauseVizIcon = Activator.getImage("icons/pause_viz.png");
		resumeVizIcon = Activator.getImage("icons/play_viz.png");
	        Image focusVizIcon = Activator.getImage("icons/viz.png"); // ZZZ
	        Image centerVizIcon = Activator.getImage("icons/center_viz.gif"); // ZZZ
		Image overlayAddIcon = Activator.getImage("icons/overlayAdd.png");
		Image overlayDeleteIcon = Activator.getImage("icons/overlayClear.png");
		Image compileIcon = Activator.getImage("icons/brick.png");
		Image runIcon = Activator.getImage("icons/run_exc.gif");
		Image graphIcon = Activator.getImage("icons/graph.png");

		//CoolItem with console
		CoolItem consoleCoolItem = new CoolItem(coolbar, SWT.RESIZE);

		toolbar = new ToolBar(coolbar, SWT.HORIZONTAL);
		//toolbar.setLayoutData(new GridData(GridData.FILL_BOTH));

		startVizItem = new ToolItem(toolbar, SWT.PUSH);
		startVizItem.setImage(startVizIcon);
		startVizItem.setToolTipText("Start Visualization");


		pause_resumeVizItem = new ToolItem(toolbar, SWT.PUSH);
		pause_resumeVizItem.setImage(pauseVizIcon);
		pause_resumeVizItem.setToolTipText("Pause / Resume Visualization");
		
		focusVizItem = new ToolItem(toolbar, SWT.PUSH);
		focusVizItem.setImage(focusVizIcon);
		focusVizItem.setToolTipText("Focus Visualization");

		centerVizItem = new ToolItem(toolbar, SWT.PUSH);
		centerVizItem.setImage(centerVizIcon);
		centerVizItem.setToolTipText("Center Visualization");

		addOverlay = new ToolItem(toolbar, SWT.PUSH);
		addOverlay.setImage(overlayAddIcon);
		addOverlay.setToolTipText("Add Graph Overlay");

		clearOverlay = new ToolItem(toolbar, SWT.PUSH);
		clearOverlay.setImage(overlayDeleteIcon);
		clearOverlay.setToolTipText("Clear Overlays");

		/*ToolItem separator = */new ToolItem(toolbar, SWT.SEPARATOR);


		compileItem = new ToolItem(toolbar, SWT.PUSH);
		compileItem.setImage(compileIcon);
		compileItem.setToolTipText("Compile Experiment");

		runItem = new ToolItem(toolbar, SWT.PUSH);
		runItem.setImage(runIcon);
		runItem.setToolTipText("Run Experimet");
		
		graphItem = new ToolItem(toolbar, SWT.PUSH);
		graphItem.setImage(graphIcon);
		graphItem.setToolTipText("Start LiveGraph");

		runItem.setEnabled(false);
		graphItem.setEnabled(false);

		if (ps.exp.getState().gte(State.PRE_COMPILED)) {
		    //startVizItem.setImage(refreshVizIcon);
		    //focusItem.setEnabled(true);
		}
		else {
			pause_resumeVizItem.setEnabled(false);
		}
		toolbar.pack();
		Point size = toolbar.getSize();
		consoleCoolItem.setControl(toolbar);
		consoleCoolItem.setSize(consoleCoolItem.computeSize(size.x, size.y));
		consoleCoolItem.setMinimumSize(consoleCoolItem.computeSize(size.x, size.y));

		updateButtons(ps.exp.getState());
		addListeners();
	}

	private void addListeners() {
		startVizItem.addListener(SWT.Selection, this);
		pause_resumeVizItem.addListener(SWT.Selection, this);
		focusVizItem.addListener(SWT.Selection, this);
		centerVizItem.addListener(SWT.Selection, this);
		compileItem.addListener(SWT.Selection, this);
		runItem.addListener(SWT.Selection, this);
		graphItem.addListener(SWT.Selection, this);
		addOverlay.addListener(SWT.Selection, this);
		clearOverlay.addListener(SWT.Selection, this);
	}

	@Override
	public void handleEvent(Event event) {
		Widget source = event.widget;
		if (source == startVizItem) {
			handleStartVizEvent();
		} else if (source == compileItem) {
			handleCompileEvent();
		} else if (source == runItem) {
			handleRunEvent();
		} else if (source == pause_resumeVizItem){
			handlePauseResumeVizEvent();
		} else if (source == focusVizItem){
			handleFocusVizEvent();
		} else if (source == centerVizItem){
			handleCenterVizEvent();
		} else if (source == graphItem){
			handleGraphEvent();
		} else if (source == addOverlay){
			handleAddOverlay();
		} else if (source == clearOverlay){
			handleClearOverlay();
		}
		ProjectLoader.updateCommandAvailablility(ps);
	}
	
	private void handleClearOverlay() {
		if(Util.question("", "Are you sure you want to clear all overlays?")) {
			ps.exp.setOverlay(null);
		}		
	}
	
	private void handleAddOverlay() {
		final OverlayWizard ow = new OverlayWizard();
		final OverlayWizardDialog owd = new OverlayWizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),ow);
		owd.open();
	}
	
	private void handleGraphEvent() {
		//System.out.println("1handleGraphEvent "+((ps.exp.selected==null)?"NULL":ps.exp.selected.getUniqueName()));
		if(ps.exp.selected != null) {
			if(ps.exp.selected instanceof IHost) {
			if(ps.exp.livegraph != null) {
				ps.exp.livegraph.stop();
			}
			System.out.println("Creating new livegraph");
			ps.exp.livegraph = new  LiveGraphManager((IHost)ps.exp.selected);
			System.out.println("starting livegraph");
			ps.exp.livegraph.start();
			System.out.println("started livegraph");
			}
			else {
				String title = "Unable To Monitor Node";
				String message = "Cannot monitor node: " + ps.exp.selected.getUniqueName();
				Util.dialog(Type.ERROR, title, message);
				ErrorDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
						title, message, null);
			}
		}
	}
	
	public void resetVizIcon(boolean show_pause) {
		if(show_pause){
			pause_resumeVizItem.setImage(pauseVizIcon);
		}
		else{
			pause_resumeVizItem.setImage(resumeVizIcon);
		}

	}
	
	private void handleStartVizEvent() {
		try {
			ps.exp.expandedNets.clear();
		} catch(Exception e) {}
		ps.exp.executeCommand("startViz()", true);
		//startVizItem.setImage(refreshVizIcon);
		pause_resumeVizItem.setImage(pauseVizIcon);
		pause_resumeVizItem.setEnabled(true);
	}

	private void handleFocusVizEvent() {
		try {
		    //ps.exp.expandedNets.clear();
		    ps.exp.focusViz();
		} catch(Exception e) {}
	}

	private void handleCenterVizEvent() {
		try {
		    //ps.exp.expandedNets.clear();
		    ps.exp.centerViz();
		} catch(Exception e) {}
	}

	private void handlePauseResumeVizEvent() {
		//if its resumed
		if(pause_resumeVizItem.getImage().equals(pauseVizIcon)){
			ps.exp.pauseViz();
			pause_resumeVizItem.setImage(resumeVizIcon);
		}
		else{
			ps.exp.resumeViz();
			pause_resumeVizItem.setImage(pauseVizIcon);
		}
	}

	private void handleCompileEvent() {
		ExperimentCompileHandler compileHandler = new ExperimentCompileHandler();
		ps.exp = compileHandler.compileExperiment();
	}


	private void handleRunEvent() {
		ExperimentRunHandler runHandler = new ExperimentRunHandler();
		ps.exp = runHandler.runExperiment();
	}
	
	public void updateButtons(State state) {
		try {
			//System.out.println("handling state update:"+state);
			switch(state) {
			case PRE_COMPILED:
				compileItem.setEnabled(true);
				runItem.setEnabled(false);
				graphItem.setEnabled(false);
				addOverlay.setEnabled(true);
				clearOverlay.setEnabled(ps != null && ps.exp!=null && ps.exp.getOverlay()!=null);
				break;
			case COMPILING:
			case COMPILED:
			case PARTITIONED:
			case PARTITIONING:
				compileItem.setEnabled(false);
				runItem.setEnabled(true);
				graphItem.setEnabled(false);
				addOverlay.setEnabled(true);
				clearOverlay.setEnabled(ps != null && ps.exp!=null && ps.exp.getOverlay()!=null);
				break;
			case CLOSED:
				compileItem.setEnabled(false);
				runItem.setEnabled(false);
				graphItem.setEnabled(false);
				addOverlay.setEnabled(false);
				clearOverlay.setEnabled(false);
				break;
			case SETTING_UP:
			case SETUP:
			case STARTING:
			case RUNNING:
			case RUNNING_DETACHED:
				compileItem.setEnabled(false);
				runItem.setEnabled(false);
				graphItem.setEnabled(true);
				addOverlay.setEnabled(true);
				clearOverlay.setEnabled(ps != null && ps.exp!=null && ps.exp.getOverlay()!=null);
				break;
			}
		}catch(Exception e) { }
		
	}
	

	public void enableCompileItem(){
		compileItem.setEnabled(true);
	}

	public void disableCompileItem(){
		compileItem.setEnabled(false);
	}

	public void enableRunItem(){
		runItem.setEnabled(true);
	}
	
	public void disableRunItem(){
		runItem.setEnabled(false);
	}

	public void setVisible(Boolean bool){
		toolbar.setSize(0, 0);
		this.toolbar.setVisible(bool);
	}
}
