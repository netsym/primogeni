package slingshot.experiment.commands;

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

import java.io.File;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;

import jprime.visitors.XMLVisitor;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import slingshot.Util;
import slingshot.Util.Type;
import slingshot.experiment.ProjectLoader;


/**
 * @author Nathanael Van Vorst
 *
 */
public class ExperimentExportModelHandler extends ExperimentCommandHandler {

	public static final String ID ="experiment.exportModel.handler";

	private String fname=null;
	/* (non-Javadoc)
	 * @see slingshot.experiment.commands.ExperimentCommandHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		if(updateCurrentExperiment()) {
			final Shell shell = new Shell(Display.getDefault());
			final FileDialog fileDialog = new FileDialog(shell, SWT.SAVE /* or SAVE or MULTI */);

			ProjectLoader.updateCommandAvailablility(curExp);
			//ExportModelFileWizard emfw = new ExportModelFileWizard();

			String modelFileName = curExp.exp.getName()+".xml";			
			fileDialog.setFileName(modelFileName);

			String[] filterNames = new String[]{"XML Files"};
			String[] filterExtensions = new String[] {"*.xml"};
			//String filterPath = "/";
			//dialog.setFilterPath (filterPath);

			fileDialog.setFilterNames(filterNames);
			fileDialog.setFilterExtensions(filterExtensions);

			final String fileSelected = fileDialog.open();
			fname=null;
			
			if (fileSelected != "" && fileSelected != null) {
				System.out.println("fname="+fileSelected);
				{
					File temp = new File(fileSelected);
					if(temp.exists()) {
						if(!Util.confirm("Overwrite?", "Do you want to overwrite \""+fileSelected+"\"?"))
							return null;
					}
				}
				ProgressMonitorDialog dialog = new ProgressMonitorDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell()); 
				try {
					dialog.run(true, false, new IRunnableWithProgress(){ 
						public void run(final IProgressMonitor monitor) { 
							monitor.beginTask("Exporting '" + curExp.exp.getName()+"'", 100);
							Runnable r = new Runnable() {
								@Override
								public void run() {
									monitor.worked(20);
									try {
										if (!fileSelected.endsWith(".xml")) {
											setFileName(fileSelected+".xml");
										}
										else {
											setFileName(fileSelected);
										}	
										new XMLVisitor(curExp.exp.getExperiment().getMetadata(),new PrintStream(new File(getFileName())))
										.print(curExp.exp.getExperiment());
									}
									catch(Exception e) {
										Util.dialog(Type.ERROR,
												"Encountered unexpected exception while exporting:",Util.getStackTraceAsString(e));
									}
									monitor.worked(50);
									 PlatformUI.getWorkbench().getDisplay().syncExec( new Runnable() {
										 @Override
										 public void run() {
											 try {
												 ResourcesPlugin.getWorkspace().getRoot().refreshLocal(IResource.DEPTH_INFINITE, null);
											 }
											 catch(Exception e) { }
										 }
									 });	
									monitor.worked(30);
								}
							};
							Thread t= new Thread(r);
							t.start();
							try {
								t.join();
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							monitor.done(); 
						} 
					});
				} catch (InvocationTargetException e) {
				} catch (InterruptedException e) { }
				if(fname==null) {
					Util.dialog(Type.NOTICE,"",
					"Error exporting experiment.");
				}
				else {
					Util.dialog(Type.NOTICE,"","Exported experiment to '"+fname+"'");
				}
			}
		}
		else {
			Util.dialog(Type.NOTICE,"",
					"Error exporting experiment.");
		}
		return null;
	}

	protected void setFileName(String name) {
		this.fname=name;
	}
	protected String getFileName() {
		return this.fname;
	}
	
	/* (non-Javadoc)
	 * @see slingshot.experiment.commands.ExperimentCommandHandler#isEnabled()
	 */
	@Override
	public boolean isEnabled() {
		//System.out.println(ID+" notified!");
		if(updateCurrentExperiment()) {
			setBaseEnabled(true);
			return true;
		}
		setBaseEnabled(false);
		return false;
	}
}
