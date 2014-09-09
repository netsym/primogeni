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
 * This class defines a Slingshot Project.
 *
 * @author Eduardo Pena
 */
public class SlingshotExperimentParent extends SlingshotExperimentElement {

	/** The Slingshot Project resource in the workspace */
	private IProject _project;

	/**
	 * The constructor of the Slingshot Project.
	 *
	 * @param iProject					The project in the workspace
	 */
    public SlingshotExperimentParent(IProject iProject) {
        super(null, iProject.getName(),"icons/folder_experiment.png");
    	_project = iProject;
    }

    /**
     * This method returns the resource defining this Slingshot project.
     *
     * @return IProject					The project resource
     */
	@Override
	public IProject getProject() {
		return _project;
	}

	/**
	 * This method obtains the children of this Slingshot project.
	 *
	 * @param iProject					The project resource
	 * @return ArrayList<Object>		The children of this Slingshot project
	 */
	@Override
	protected ArrayList<Object> initializeChildren(IProject iProject) {
    	ArrayList<Object> children = new ArrayList<Object>();

    	//attempt to get the resources inside the folder
		IResource[] members = {};
		try {
			 members= iProject.members();
		} catch (CoreException e) {
		}

		//check for .py and .java files and add them to the children ArrayList
		for(IResource r : members){
			if(r instanceof IFile){
				String ext = ((IFile)r).getFileExtension();
				String imagePath;

				if(ext == null){
					imagePath = "icons/page_white.png";
					children.add(new SlingshotExperimentFile(this, r.getName(), imagePath, iProject.getFullPath()));
				}
				else{
					if(ext.equalsIgnoreCase("java")){
						imagePath = "icons/page_white_cup.png";
					} else if(ext.equalsIgnoreCase("xml")){
						imagePath = "icons/page_white_xml.png";
					} else if(ext.equalsIgnoreCase("py")){
						imagePath = "icons/page_white_python.png";
					} else if(ext.equalsIgnoreCase("meta")){
						imagePath = "icons/page_white_gear.png";
					} else if(ext.equalsIgnoreCase("txt")){
						imagePath = "icons/page_white_text.png";
					} else{
						imagePath = "icons/page_white.png";
					}
					if(!ext.equalsIgnoreCase("project"))
						children.add(new SlingshotExperimentFile(this, r.getName(), imagePath, iProject.getFullPath()));
				}
			}
			else if(r instanceof IFolder){
				IFolder folder = (IFolder) r;
				if(folder.getName().contains(ExperimentRunsFolderGenerator.NAME)){
					children.add(new SlingshotExperimentRunFolder(this, folder.getName()));
				} else{
					children.add(new SlingshotExperimentFolder(this, folder.getName(), iProject.getFullPath()));
				}
			}
		}

        return children;
    }
}
