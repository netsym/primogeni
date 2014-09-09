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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.PlatformUI;

import slingshot.Activator;
import slingshot.Util.Type;
import slingshot.experiment.ProjectLoader;
import slingshot.experiment.ProjectLoader.ProjectState;
import slingshot.spyconsole.SPyConsole;

/**
 * @author Eduardo Pena
 */
public class ConsoleToolBar implements Listener {

	private String experimentName;

	private ToolBar toolbar;

	private CoolItem consoleCoolItem;

	private ToolItem saveItem;
	private ToolItem loadItem;

	public ConsoleToolBar(CoolBar coolbar, String expName) {
		experimentName = expName;
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
		Image loadIcon = Activator.getImage("icons/folder_go.png");

		//CoolItem with console
		consoleCoolItem = new CoolItem(coolbar, SWT.RESIZE);

		toolbar = new ToolBar(coolbar, SWT.HORIZONTAL);
		//toolbar.setLayoutData(new GridData(GridData.FILL_BOTH));

		saveItem = new ToolItem(toolbar, SWT.PUSH);
		saveItem.setImage(saveIcon);
		saveItem.setToolTipText("Save to console");

		loadItem = new ToolItem(toolbar, SWT.PUSH);
		loadItem.setImage(loadIcon);
		loadItem.setToolTipText("Load pyPrime script");

		toolbar.pack();
		Point size = toolbar.getSize();
		consoleCoolItem.setControl(toolbar);
		consoleCoolItem.setSize(consoleCoolItem.computeSize(size.x, size.y));
		consoleCoolItem.setMinimumSize(consoleCoolItem.computeSize(size.x, size.y));

		addListeners();
	}

	private void addListeners() {
		saveItem.addListener(SWT.Selection, this);
		loadItem.addListener(SWT.Selection, this);
	}
	
	@Override
	public void handleEvent(Event event) {
		Widget source = event.widget;
		if (source == saveItem) {
			handleSaveConsoleEvent();
		} else if (source == loadItem) {
			handleLoadConsoleEvent();
		}	
		ProjectLoader.updateCommandAvailablility(experimentName);
	}

	private void handleSaveConsoleEvent() {
		ProjectState ps = ProjectLoader.getProjectState(experimentName);
		if(ps == null) {
			slingshot.Util.dialog(Type.ERROR, "FATAL","Couldn't find the project state!");
		}
		else {
			try {
				final Shell shell = new Shell(Display.getDefault());
				final FileDialog fileDialog = new FileDialog(shell, SWT.SAVE /* or SAVE or MULTI */);

				String fname = ps.exp.getName()+"_console.py";			
				fileDialog.setFileName(fname);

				String[] filterNames = new String[]{"*.txt; *.py"};
				String[] filterExtensions = new String[] {"*.txt;*.py"};

				fileDialog.setFilterNames(filterNames);
				fileDialog.setFilterExtensions(filterExtensions);

				String fileSelected = fileDialog.open();
				
				if (fileSelected != "" && fileSelected != null) {
					if (!fileSelected.endsWith(".py") && !fileSelected.endsWith(".txt") ) {
						fileSelected+=".py";
					}
					PrintStream out = new PrintStream(new File(fileSelected));
					
					SPyConsole pConsole = ps.view.consoleHandler.getConsole();
					out.write(pConsole.getText().getBytes());
					slingshot.Util.dialog(Type.NOTICE,"", "Saved console to '"+fileSelected+"'");
				}
			}
			catch(Exception e){
				slingshot.Util.dialog(Type.ERROR,"Error Saving console", slingshot.Util.getStackTraceAsString(e));
				
			}
		}
	}

	private void handleLoadConsoleEvent() {
		FileDialog fd = new FileDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.OPEN);
		fd.setText("Load file contents to the Console");
		fd.setFilterExtensions(new String[] { "*.txt;*.py" });
		fd.setOverwrite(true);

		String filename = fd.open();

		String data = "";

		if(filename != null){
			BufferedReader reader = null;
			try{
				reader = new BufferedReader(new FileReader(filename));

				String tmp = reader.readLine();
				while(tmp!= null){
					data += tmp+"\n";
					tmp = reader.readLine();
				}
			} catch(IOException e) {
				System.out.println("Exception occured: Couldn't load file contents");
				System.out.println(e);
			} finally{
				try{
					reader.close();
				} catch (Exception e) {}
			}
		}
		
		ProjectState ps = ProjectLoader.getProjectState(experimentName);

		SPyConsole pConsole = ps.view.consoleHandler.getConsole();
		pConsole.executeCommandSet(data);
	}

	public void hide(){
		this.toolbar.setVisible(false);
	}

	public void show(){
		this.toolbar.setVisible(true);
	}
}
