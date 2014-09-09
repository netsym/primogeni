package slingshot.wizards.creation;

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

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.IStructuredSelection;

import slingshot.experiment.structure.ISlingshotExperimentElement;
import slingshot.experiment.structure.SlingshotExperimentFile;
import slingshot.experiment.structure.SlingshotExperimentFolder;
import slingshot.experiment.structure.SlingshotExperimentParent;
import slingshot.experiment.structure.SlingshotExperimentRunFolder;
import slingshot.wizards.widgets.MyWizardNewFileCreationPage;

/**
 * @author Eduardo Pena
 *
 */
public class WizardNewFileCreationPage extends MyWizardNewFileCreationPage {

	/**
	 * The Constructor for this class.
	 * Sets the title and the description of this wizard page.
	 * Sets the extension of the file to be created.
	 * Gets the project selected by the user prior to opening the wizard and sets the destination folder for the new file.
	 *
	 * @param selection				The user's selection before opening the wizard
	 * @param title					The title for this Wizard Page
	 * @param description			The description for this Wizard Page
	 * @param fextension			The file extension for the new Source file to be created
	 */
	public WizardNewFileCreationPage(IStructuredSelection selection, String title, String description) {
		super("New File", selection);
		//get the element selected by the user
		Object element = selection.getFirstElement();

		if(element != null){
			//check if the element selected was a project
			IPath path = null;
			if(element instanceof ISlingshotExperimentElement){
				if(element instanceof SlingshotExperimentFile){
					SlingshotExperimentFile f = (SlingshotExperimentFile) element;
					path = f.getPath();
				}
				else{
					if(element instanceof SlingshotExperimentParent){
						SlingshotExperimentParent p = (SlingshotExperimentParent) element;
						path = p.getProject().getFullPath();
					}
					else{
						if(element instanceof SlingshotExperimentFolder){
							SlingshotExperimentFolder f = (SlingshotExperimentFolder) element;
							path = f.getPath().append(f.getName());
						}
						else if(element instanceof SlingshotExperimentRunFolder){
							SlingshotExperimentRunFolder f = (SlingshotExperimentRunFolder) element;
							path = f.getProject().getFullPath().append(f.getText());
						}
					}
				}
			}
			setContainerFullPath(path);
		}
		setTitle(title);
		setDescription(description);
	}

	/**
	 * Sets the initial contents for the new Slingshot Source file.
	 *
	 * @return InputStream					The contents of the file
	 */
	@Override
	protected InputStream getInitialContents(){
		String pyTemplate = "";
		return new ByteArrayInputStream(pyTemplate.getBytes());
	}
}
