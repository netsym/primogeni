package slingshot.navigator;

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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import slingshot.experiment.structure.SlingshotExperimentFile;

/**
 * This is the action class that opens a file in the workbench.
 *
 * @author Eduardo Pena
 * @version 1.0.0
 *
 */
public class OpenFAction extends Action {

	//private final String XMLEditor = "slingshot.XMLeditor";
	private final String DefaultEditor = "slingshot.DefaultEditor";

	/** The Selection provider that should contain the selected file to be opened */
	private ISelectionProvider provider;
	/** The file to be opened */
	private IFile file;

	/**
	 * The constructor for this action
	 *
	 * @param p
	 * @param selectionProvider
	 */
	public OpenFAction(IWorkbenchPage p, ISelectionProvider selectionProvider) {
		setText("Open");
		provider = selectionProvider;
	}

	/**
	 * This method checks whether the caller of the action is a file
	 *
	 * @return boolean			TRUE if the caller is a file, FALSE otherwise
	 */
	public boolean isEnabled(){
		ISelection selection = provider.getSelection();
		//check if something was selected
		if(!selection.isEmpty()){
			IStructuredSelection sSelection = (IStructuredSelection) selection;
			//check if th selection was a single file
			if(sSelection.size() == 1 && sSelection.getFirstElement() instanceof SlingshotExperimentFile){
				//save the file to this class and return true
				SlingshotExperimentFile f = (SlingshotExperimentFile) sSelection.getFirstElement();
				file = ResourcesPlugin.getWorkspace().getRoot().getFile(f.getPath().append(f.getName()));
				return true;
			}
		}
		return false;
	}

	/**
	 * This method executes the action and attempts to open the file in the Workbench
	 */
	public void run(){
		IWorkbench workbench = PlatformUI.getWorkbench();
		try {
			IDE.setDefaultEditor(file, DefaultEditor);
			IDE.openEditor(workbench.getActiveWorkbenchWindow().getActivePage(), file);

		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}
}
