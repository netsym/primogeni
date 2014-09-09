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

import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.PlatformUI;

import slingshot.Activator;
import slingshot.Util;
import slingshot.experiment.ProjectLoader;
import slingshot.experiment.ProjectLoader.ProjectState;

/**
 * 
 * The toolbar in the log window.
 * @author Eduardo Pena
 * @author Eduardo Tibau
 *
 */
public class LogToolBar implements Listener{
	private ProjectState ps;
	
	private ToolBar toolbar;

	private CoolItem logCoolItem;

	private ToolItem saveItem;
	private ToolItem clearItem;

	public LogToolBar(CoolBar coolbar, String expName) {
		this.ps = ProjectLoader.getProjectState(expName);
		createToolBarComposite(coolbar);
	}

	public ToolBar getToolbar() {
		return toolbar;
	}

	public void setToolbar(ToolBar toolbar) {
		this.toolbar = toolbar;
	}


	private void createToolBarComposite(CoolBar coolbar) {


		Image saveIcon = Activator.getImage("icons/disk.png");
		Image clearIcon = Activator.getImage("icons/page_white.png");

		//CoolItem with console
		logCoolItem = new CoolItem(coolbar, SWT.RESIZE);

		toolbar = new ToolBar(coolbar, SWT.HORIZONTAL);
		//toolbar.setLayoutData(new GridData(GridData.FILL_BOTH));

		saveItem = new ToolItem(toolbar, SWT.PUSH);
		saveItem.setImage(saveIcon);
		saveItem.setToolTipText("Save Log");

		clearItem = new ToolItem(toolbar, SWT.PUSH);
		clearItem.setImage(clearIcon);
		clearItem.setToolTipText("Clear Log");

		toolbar.pack();
		Point size = toolbar.getSize();
		logCoolItem.setControl(toolbar);
		logCoolItem.setSize(logCoolItem.computeSize(size.x, size.y));

		addListeners();
	}

	private void addListeners() {
		saveItem.addListener(SWT.Selection, this);
		clearItem.addListener(SWT.Selection, this);
	}

	@Override
	public void handleEvent(Event event) {
		Widget source = event.widget;
		if (source == saveItem) {
			handleSaveConsoleEvent();
		} else if (source == clearItem) {
			handleClearConsoleEvent();
		}
	}

	private void handleClearConsoleEvent() {
		String title = "Clear Log contents";
		String message = "Are you sure you want to clear the contents of the Log ?";
		if (Util.confirm(title, message)) {
			Text console = ps.view.logHandler.getLog();
			console.setText("");
		}
	}

	private void handleSaveConsoleEvent() {
		FileDialog fd = new FileDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.SAVE);
		fd.setText("Save Log contents to File");
		fd.setFilterExtensions(new String[] { "*.txt;*.py" });
		fd.setOverwrite(true);

		String filename = fd.open();

		if(filename != null){
			FileWriter writer = null;
			try {
				writer = new FileWriter(filename);
				Text console = ps.view.logHandler.getLog();
				writer.write(console.getText());

			} catch (IOException e) {
				System.out.println("Exception occured: File not saved!");
				System.out.println(e);
			} finally {
				try {
					writer.close();
				} catch (Exception e) {
				}
			}
		}
	}

	public void hide(){
		this.toolbar.setVisible(false);
	}

	public void show(){
		this.toolbar.setVisible(true);
	}

}
