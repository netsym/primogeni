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
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

/**
 * @author Eduardo Pena
 */
public class SlingshotExperimentFolder extends SlingshotExperimentElement {

	private String name;
	private IPath path;

	public SlingshotExperimentFolder(ISlingshotExperimentElement parent,
			String name, IPath path) {
		super(parent, name, "icons/folder.png");
		setName(name);
		setPath(path);
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public IPath getPath() {
		return path;
	}

	public void setPath(IPath path) {
		this.path = path;
	}

	@Override
	protected ArrayList<Object> initializeChildren(IProject project) {

		ArrayList<Object> children = new ArrayList<Object>();

		//get the Experiment Results folder from the project
		IFolder folder = ResourcesPlugin.getWorkspace().getRoot().getFolder(path.append(name));
		//attempt to get the resources inside this Experiment Results folder
		IResource[] members = {};
		try {
			 members= folder.members();
		} catch (CoreException e) {
			e.printStackTrace();
		}
		//check for .py and .java files and add them to the children ArrayList
		for(IResource r : members){
			if(r instanceof IFile){
				String ext = ((IFile)r).getFileExtension();
				String imagePath;

				if(ext == null){
					imagePath = "icons/page_white.png";
					children.add(new SlingshotExperimentFile(this, r.getName(), imagePath, path.append(name)));
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
						children.add(new SlingshotExperimentFile(this, r.getName(), imagePath, path.append(name)));
				}
			}
			else if(r instanceof IFolder){
				IFolder f = (IFolder) r;
				children.add(new SlingshotExperimentFolder(this, f.getName(), path.append(name)));
			}
		}
        return children;
	}

}
