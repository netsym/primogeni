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

import java.util.ArrayList;
import java.util.Iterator;

import jprime.Experiment;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;

import slingshot.Application;
import slingshot.Util;
import slingshot.Util.Type;
import slingshot.experiment.ProjectLoader;
import slingshot.experiment.structure.ISlingshotExperimentElement;
import slingshot.experiment.structure.SlingshotExperimentFile;
import slingshot.experiment.structure.SlingshotExperimentFolder;
import slingshot.experiment.structure.SlingshotExperimentParent;
import slingshot.experiment.structure.SlingshotExperimentRunFolder;

/**
 * This class handles the Delete operation when a right click action is detected
 * on an item in the project explorer.
 *
 * Last Modified: 08/09/2010
 *
 * @author Nathanael Van Vorst
 * @author Eduardo Pena
 *
 */
public class DeleteHandler extends AbstractHandler implements IHandler {

	/**
	 * This method creates a Dialog window asking the user to confirm whether he
	 * wants to delete the selected resource.
	 *
	 * @param type
	 *            The type of the resource being deleted (conf.xml file, source
	 *            file, folder, etc.)
	 * @param resourceName
	 *            The name of the resource being deleted
	 * @return boolean TRUE if the user confirmed the resource deletion, FALSE
	 *         otherwise
	 */
	private boolean confirmSingleResourceDelete(String type, String resourceName) {
		String title = "Delete " + type;
		String message = "Are you sure you want to permanently delete the "+ type + " (" + resourceName + ") ?";
		return Util.confirm(title, message);
	}

	/**
	 * This method creates a Dialog window asking the user to confirm whether he
	 * wants to delete the selected resource.
	 *
	 * @param type
	 *            The type of the resource being deleted (conf.xml file, source
	 *            file, folder, etc.)
	 * @param resourceName
	 *            The name of the resource being deleted
	 * @return boolean TRUE if the user confirmed the resource deletion, FALSE
	 *         otherwise
	 */
	private boolean confirmMultipleResourceDelete(int numExperiments, int numFolders, int numFiles) {
		boolean notfirst = false;
		String title = "Delete Multiple Resources";
		String message = "Are you sure you want to delete";
		if(numExperiments > 0){
			message += " "+ numExperiments + " experiment(s)";
			notfirst = true;
		}
		if(numFolders > 0){
			if(notfirst)
				message += " and";
			else
				notfirst = true;
			message += " "+ numFolders + " folder(s)";
		}
		if(numFiles > 0){
			if(notfirst)
				message += " and";
			message += " "+ numFiles + " file(s)";
		}
		message += " from your workspace?";
		return Util.confirm(title, message);
	}


	/**
	 * This method executes the delete Action, first it determines what type of
	 * resource was selected for deletion and then it calls the
	 * confirmResourceDelete() method, which asks the user to confirm whether he
	 * wants to delete the resource. If the user confirms the selected resource
	 * is deleted from the workspace and the hard drive.
	 *
	 * @param event
	 *            The event that caused this action to execute
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		// Obtain the element that was selected when the action was triggered.
		IStructuredSelection selection = (IStructuredSelection) PlatformUI
				.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.getSelection();

		@SuppressWarnings("rawtypes")
		Iterator iterator = selection.iterator();
		ArrayList<Object> selectedItems = new ArrayList<Object>();

		while (iterator.hasNext()) {
			Object element = iterator.next();
			if (element instanceof ISlingshotExperimentElement)
				selectedItems.add(element);
		}

		//DELETE SINGLE RESOURCES
		if (selectedItems.size() == 1) {
			Object selectedItem = selectedItems.get(0);

			deleteObject(selectedItem,true);
		}
		//DELETE MULTIPLE RESOURCES
		else if (selectedItems.size() > 1) {
			int filecounter = 0;
			int foldercounter = 0;
			int experimentcounter = 0;

			for(Object r : selectedItems){
				if(r instanceof SlingshotExperimentFile)
					filecounter++;
				else if(r instanceof SlingshotExperimentParent)
					experimentcounter++;
				else if(r instanceof SlingshotExperimentFolder)
					foldercounter++;
			}

			if(confirmMultipleResourceDelete(experimentcounter, foldercounter, filecounter)){
				//if the user confirms, delete all the selected resources
				for(Object r : selectedItems){
					//delete files first
					deleteObject(r,false);
				}
			}
		}
		return null;
	}


	/**
	 * Deletes the given object (if it exists). If confirmation is true then a confirmation dialog is given before the object is deleted.
	 * @param o
	 * @param confirmation
	 */
	public void deleteObject(Object o, boolean confirmation){
		// Check if the resource is a Slingshot Project Element
		if (o instanceof ISlingshotExperimentElement) {
			final String projectpath=((ISlingshotExperimentElement)o).getProject().getFullPath().toOSString();
			// Check if the resource is the project itself
			// Check if the resource is a File
			if (o instanceof SlingshotExperimentFile) {
				// get the file resource
				SlingshotExperimentFile f = (SlingshotExperimentFile) o;
				IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(f.getPath().append(f.getName()));
				// open the confirm Dialog
				if(confirmation){
					if (!confirmSingleResourceDelete("File", file.getName()))
						return;
				}
				// if user confirms, attempt to delete file
				try {
					file.delete(true, false, null);
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}else if (o instanceof SlingshotExperimentParent) {
				if(("/"+ProjectLoader.GENERATED_MODELS).compareTo(projectpath)==0) {
					Util.dialog(Type.ERROR,"",
							"You cannot delete "+ProjectLoader.GENERATED_MODELS+"!");
					return;
				}
				// get the project resource
				IProject p = ((SlingshotExperimentParent) o).getProject();
				final String pname=p.getName();
				if(ProjectLoader.getProjectState(pname)!=null){
					Util.dialog(Type.ERROR,
							"",
							"You cannot delete '"+pname+"' because it is currently open!");
					return;
				}
				// open the confirm Dialog
				if(confirmation){
					if (!confirmSingleResourceDelete("Project", pname))
						return;
				}
				// if user confirms, attempt to delete project from
				// workspace and hard disk
				try {
					p.delete(true, true, null);
					IResource[] project = { p };
					ResourcesPlugin.getWorkspace().delete(project,true, null);
				} catch (CoreException e) {
					e.printStackTrace();
				}
				ProjectLoader.closeExp(pname,true);
				try {
					Experiment exp = Application.db.loadExperiment(pname);
					Application.db.remove(exp);
					System.out.println("Database entry deleted");
				} catch(Exception e) {
					//e.printStackTrace();
					System.err.println("Database entry could not be found for deletion.");
					System.err.println("Experiment: "+ pname);
					//e.printStackTrace();
				}
			}
			// Check if the resource is an Experiment Results Folder
			else if (o instanceof SlingshotExperimentFolder) {
				// get the folder resource from the project
				SlingshotExperimentFolder f = (SlingshotExperimentFolder) o;
				IFolder folder = ResourcesPlugin.getWorkspace().getRoot().getFolder(f.getPath().append(f.getName()));
				// open the confirm Dialog
				if(confirmation){
					if (!confirmSingleResourceDelete("Results Folder", folder.getName()))
						return;
				}
				// if user confirms, attempt to delete folder from
				// workspace and hard disk
				try {
					folder.delete(true, true, null);
					IResource[] fr = { folder };
					ResourcesPlugin.getWorkspace().delete(fr, true, null);
					System.out.println("Workspace folder deleted.");
				} catch (CoreException e) {
					System.err.println("Workspace folder could not be deleted");
					System.err.println("Folder: "+ folder.getName());
					e.printStackTrace();
				}
			}
			// Check if the resource is a project's Source Folder
			else if (o instanceof SlingshotExperimentRunFolder)
				System.out.println("Runs folder can't be deleted!");
		}
		// Check if the resource if an unknown resource
		else {
			System.out.println("I don't know what this resource is");
		}
	}

}
