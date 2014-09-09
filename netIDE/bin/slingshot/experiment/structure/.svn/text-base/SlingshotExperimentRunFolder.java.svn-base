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

import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

/**
 * This class defines the Experiment Results Folders of a Slingshot project.
 *
 * @author Eduardo Pena
 * @version 1.0.0
 *
 */
public class SlingshotExperimentRunFolder extends SlingshotExperimentElement{

	/** The name of this experiment results folder */
	private String name;

	/**
	 * The constructor of this project element
	 *
	 * @param parent			The parent of this experiment results folder
	 * @param name				The name for this new experiment results folder
	 */
	public SlingshotExperimentRunFolder(ISlingshotExperimentElement parent,
			String name) {
		super(parent, name, "icons/folder_go.png");
		this.name = name;
	}

	/**
     * This method obtains the children of this Experiment Results folder.
     *
     * TODO: Define what the experiment results folder contains to be able to finish this method correctly, right now it only accepts .xml files as children
     *
     * @param iProject				The project that contains this Experiment Results folder
     * @return ArrayList<Object>	The children of this Experiment Results folder
     */
	@Override
	protected ArrayList<Object> initializeChildren(IProject iProject) {
		ArrayList<Object> children = new ArrayList<Object>();

		//get the Experiment Results folder from the project
    	IProject project = getProject();
		IFolder folder = project.getFolder(name);
		//attempt to get the resources inside this Experiment Results folder
		IResource[] members = {};
		try {
			 members= folder.members();
		} catch (CoreException e) {
			e.printStackTrace();
		}
		//check for .xml files and add them to the children ArrayList
		for(IResource r : members){
			System.out.println(r.toString());
			if( r instanceof IFile){
				if(r.getFileExtension() != null){
					if(r.getFileExtension().equalsIgnoreCase("xml") || r.getFileExtension().equalsIgnoreCase("tlv"))
						children.add(r);
				}
			}
		}
		System.out.println();
        return children;
	}

}
