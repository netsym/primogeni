package slingshot.projects;

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

import java.net.URI;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * This class controls the Project Creation process
 *
 * @author Eduardo Pena
 *
 */
public class SlingshotProjectCreator {
    /**
     * Create a new Slingshot Project in the workspace
     *
     * @param projectName				The name for the new Slingshot Project
     * @param location					The location where the project is going to be created
     * @return IProject					The created Slingshot Project
     */
    public static IProject createProject(String projectName, URI location, String natureId) {
    	//make sure that the name of the project is not empty
        Assert.isNotNull(projectName);
        Assert.isTrue(projectName.trim().length() > 0);
        //create the base project
        IProject project = createBaseProject(projectName, location);
        try {
        	//add the Slingshot project nature
            addNature(project, natureId);
            //add the Folder paths for the children of the Slingshot Project
        } catch (CoreException e) {
            e.printStackTrace();
            project = null;
        }
        return project;
    }

    /**
     * Create a basic, empty Slingshot Project
     *
     * @param projectName				The name for the new Slingshot Project
     * @param location					The location where the project is going to be created
     * @return IProject					The created Slingshot Project
     */
    private static IProject createBaseProject(String projectName, URI location) {
        // it is acceptable to use the ResourcesPlugin class
        IProject newProject = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
        //check if the project exists
        if (!newProject.exists()) {
        	//get the new location and project description
            URI projectLocation = location;
            IProjectDescription desc = newProject.getWorkspace().newProjectDescription(newProject.getName());
            if (location != null && ResourcesPlugin.getWorkspace().getRoot().getLocationURI().equals(location)) {
                projectLocation = null;
            }
            desc.setLocationURI(projectLocation);
            //attempt to create the project
            try {
                newProject.create(desc, null);
                if(!newProject.isOpen()){
                	newProject.open(null);
                }
            } catch (CoreException e) {
                e.printStackTrace();
            }
        }
        return newProject;
    }

    /**
     * Add the Slingshot Nature to the newly created project
     *
     * @param project					The Project where the nature will be added
     * @throws CoreException
     */
    private static void addNature(IProject project, String natureId) throws CoreException {
    	//check whether the project has a nature
        if (!project.hasNature(natureId)) {
            IProjectDescription description = project.getDescription();
            String[] prevNatures = description.getNatureIds();
            String[] newNatures = new String[prevNatures.length + 1];
            System.arraycopy(prevNatures, 0, newNatures, 0, prevNatures.length);
            newNatures[prevNatures.length] = natureId;
            description.setNatureIds(newNatures);
            IProgressMonitor monitor = null;
            project.setDescription(description, monitor);
        }
    }

}
