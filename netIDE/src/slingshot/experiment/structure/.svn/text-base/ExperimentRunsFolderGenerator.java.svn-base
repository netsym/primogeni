package slingshot.experiment.structure;

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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

/**
 * This class handles the Experiment Results folder created after an experiment is loaded.
 * This class contains basic functions to create the Folder and handle its contents.
 *
 * TODO: add the contents of the folder
 *
 * @author Eduardo Pena
 */
public class ExperimentRunsFolderGenerator {

	public static final String NAME = "Run";
	/** The project this experiment belongs to */
	IProject project;
	/** The name of the results folder to be created for this experiment */
	String folderName;

	/**
	 * Constructor method for this class. Automatically generates the name for the results folder.
	 *
	 * @param project			The project this experiment belongs to
	 * @param expName			The name of this experiment
	 */
	public ExperimentRunsFolderGenerator(IProject project){
		//save class variables
		this.project = project;
		//generate the name for the results folder
		createExperimentResultsFolderName();
	}

	/**
	 * This method generates the name for the results folder. The name is created by concatenating the name of the experiment and the date and time
	 * when the experiment was loaded.
	 */
	private void createExperimentResultsFolderName(){
		//get the current date and time
		Calendar cal = Calendar.getInstance();
	    SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyyHHmm");
	    //concatenate the date and time to the name of the experiment
	    folderName =  NAME+ "-" + sdf.format(cal.getTime());
	}

	/**
	 * This method creates the new results folder for this experiment.
	 *
	 * @param folder			The IFolder resource to be created as a physical folder on disk
	 * @throws CoreException	Exception if the create operation fails
	 */
	private void createFolder(IFolder folder) throws CoreException {
		//check if the folder already exists on disk
        if (!folder.exists()) {
            folder.create(false, true, null);
        }
    }

	/**
	 * This method creates the virtual experiment results folder resource in the workspace and then creates the physical folder on disk
	 *
	 * @return String			The name of the newly created folder
	 */
	public IFolder createExperimentResultsFolder(){
		//create the virtual resource for the new experiment results folder
		IFolder expResultsFolder = project.getFolder(folderName);
		//attempt to create the physical folder
		try{
			createFolder(expResultsFolder);
		}catch(CoreException e){
			System.out.println("Error creating experiment results folder.");
			e.printStackTrace();
		}
		return expResultsFolder;
	}

}
