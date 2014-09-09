package slingshot;

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


import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import jprime.Console.ErrorDialogMaker;
import jprime.database.Database;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jface.window.Window;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import slingshot.Util.ScrollableDialog;
import slingshot.Util.Type;
import slingshot.configuration.PickWorkspaceDialog;
import slingshot.experiment.ProjectLoader;
import slingshot.natures.ExperimentNature;
import slingshot.projects.SlingshotProjectCreator;

/**
 * This class controls all aspects of the application's execution
 * This class was generated automatically by eclipse's RCP tool.
 *
 * @author Eduardo Pena
 *
 */
public class Application implements IApplication {

	public static Database db = null;

	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.IApplicationContext)
	 */
	public Object start(IApplicationContext context) {
		Display display = PlatformUI.createDisplay();
		
		try {
			//Current workspace location
			Location instanceLoc = Platform.getInstanceLocation();
			
			//Did the user ask to remember the last user-defined workspace location
			boolean remember = PickWorkspaceDialog.isRememberWorkspace();
			
			//get the last used ws (workspace) location
			String lastUsedWs = PickWorkspaceDialog.getLastSetWorkspaceDirectory();
			
			//If we need to "remember" but have no last used ws, we have nothing to remember
			if (remember && (lastUsedWs == null || lastUsedWs.length() == 0))
				remember = false;
			
			//Check if our ws location is still OK
			if (remember) {
				// if there's any problem whatsoever with the ws,
				//force a dialog which in will tell them what's bad 
				String ret = PickWorkspaceDialog.checkWorkspaceDirectory(Display.getDefault().getActiveShell(),
						lastUsedWs, false, false);
				if (ret != null) {
					remember = false;
				}
			}
			
			//If we don't remember the ws, show the dialog
			if (!remember) {
				PickWorkspaceDialog pwd = new PickWorkspaceDialog(false);
				int pick = pwd.open();
				
				//if the user cancelled, we can't do anything as we need a ws
				//so in this case tell, them and exit
				if (pick == Window.CANCEL) {
					if (pwd.getSelectedWorkspaceLocation() == null) {
						Util.dialog(Type.ERROR,"",
								"The application can not start without a workspace root and will now exit.");
						try {
							PlatformUI.getWorkbench().close();
						} catch (Exception err) {
							
						}
						System.exit(0);
						return IApplication.EXIT_OK;
					}
				}
				else {
					//tell Eclipse what the selected location was and continue
					instanceLoc.set(new URL("file", null, pwd.getSelectedWorkspaceLocation()), false);					
				}	
			}
			else
				instanceLoc.set(new URL("file", null, PickWorkspaceDialog.getLastSetWorkspaceDirectory()), false);					
			
			//ConfigurationHandler.checkForInitialConfiguration();
			
			final String dbloc = ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString();
			Map<String,String> dbparms = new HashMap<String, String>();
			dbparms.put("DB_PATH", dbloc);
			jprime.Console.setErrorDialogMaker(new ErrorDialogMaker() {
				public String getStackTraceAsString(Exception e) {
					return Util.getStackTraceAsString(e);
				}
				public void errorDialog(String title, final String message) {
					ScrollableDialog dialog = new ScrollableDialog(Display.getDefault().getActiveShell(), Type.ERROR, title, message);
					dialog.open();
				}
			});
			db=Database.createDatabase(dbparms);
			System.out.println("Using database "+db.getDbType().getURL());
			createGeneratedModels();

			int returnCode = PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor());
			if (returnCode == PlatformUI.RETURN_RESTART)
				return IApplication.EXIT_RESTART;
			else
				return IApplication.EXIT_OK;
			
		} catch(Exception e) {
			Util.dialog(Type.ERROR, "Error Starting Slingshot:", Util.getStackTraceAsString(e));
			return IApplication.EXIT_OK;
		}
		finally {
			display.dispose();
		}

	}
	
	private static void createGeneratedModels() {
		try {
			SlingshotProjectCreator.createProject(ProjectLoader.GENERATED_MODELS, null, ExperimentNature.NATURE_ID);
		} catch(Exception e){}		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplication#stop()
	 */
	public void stop() {
		final IWorkbench workbench = PlatformUI.getWorkbench();
		if (workbench == null)
			return;
		final Display display = workbench.getDisplay();
		display.syncExec(new Runnable() {
			public void run() {
				if (!display.isDisposed())
					workbench.close();
			}
		});
	}
}
