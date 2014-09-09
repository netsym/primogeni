package slingshot.commands;
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
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import slingshot.configuration.ConfigurationHandler;
import slingshot.experiment.ProjectLoader;
import slingshot.wizards.configuration.SlingshotConfig;
import slingshot.wizards.topology.BRITE;
import slingshot.wizards.topology.GT_ITM;
import slingshot.wizards.topology.Inet;
import slingshot.wizards.topology.Orbis;
import slingshot.wizards.topology.RocketFuel;
import slingshot.wizards.topology.TopoWizard;
import slingshot.wizards.topology.TopoWizardDialog;

/**
 * This class handles the topology operation from the File->New option in the main menu.
 *
 * @author Nathanael Van Vorst
 */
public class TopologyHandler extends AbstractHandler {
	private static final String FILENAME = "Generators Profile.xml";
	private static final String TOPOLOGY = "topology";
	
	
	/**
	 * The constructor.
	 */
	public TopologyHandler() 
	{
	}
	
	/* (non-Javadoc)
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		Shell shell = window.getShell();
		
		//ConfiguwebServerPathre all paths
		/*
    	String workspace_path = Platform.getInstanceLocation().getURL().getPath();
    	String ws_topo_path = workspace_path + TOPOLOGY + File.separator;
    	String ws_conf_file = ws_topo_path + FILENAME;
    	
    	String src_topo_path = getClass().getProtectionDomain().getCodeSource().
    	                       getLocation().getPath() + TOPOLOGY + File.separator;
    	String src_conf_file = src_topo_path + FILENAME;
    	*/
		File file;
    	String primexDirectory = ConfigurationHandler.getPrimexDirectory();
    	if (!primexDirectory.endsWith(File.separator))
    		primexDirectory += File.separator;
    	
    	//check if configure file exists
    	String conf_file = primexDirectory + TOPOLOGY + File.separator + FILENAME;
    	file = new File(conf_file);
    	if (!file.exists())
    	{
    		JOptionPane.showMessageDialog(null, "primex Directory is not set properly");
    		WizardDialog configdlg = new WizardDialog (shell, new SlingshotConfig());
    		configdlg.open();
    		return null;
    	}
    	
    	List<String> geners = new ArrayList<String>();
    	if (BRITE.Check(primexDirectory))
    		geners.add(BRITE.GENERNAME);
    	if (GT_ITM.Check(primexDirectory))
    		geners.add(GT_ITM.GENERNAME);
    	if (Inet.Check(primexDirectory))
    		geners.add(Inet.GENERNAME);
    	if (Orbis.Check(primexDirectory))
    		geners.add(Orbis.GENERNAME);
    	if (RocketFuel.Check(primexDirectory))
    		geners.add(RocketFuel.GENERNAME);
    		
		String projectmodelpath = ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString()
			+File.separator+ProjectLoader.GENERATED_MODELS+File.separator;

    	file = new File(projectmodelpath);
		if (!file.exists())
		{
			System.out.println("Creating "+projectmodelpath);
			file.mkdir();
		}
		if (!file.exists()) {
    		JOptionPane.showMessageDialog(null, "The directory "+projectmodelpath+" does not exist and is not able to be created!");
    		WizardDialog configdlg = new WizardDialog (shell, new SlingshotConfig());
    		configdlg.open();
    		return null;
		}

    	
		TopoWizardDialog dialog = new TopoWizardDialog(shell, 
							                   new TopoWizard(conf_file, geners));
		dialog.setPageSize(650, 300);
		dialog.open();
		
		return null;
	}
}