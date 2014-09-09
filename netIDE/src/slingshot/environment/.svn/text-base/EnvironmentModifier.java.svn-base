package slingshot.environment;

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
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.ide.undo.CreateFileOperation;

/**
 * Create/Modify/Delete environments. 
 * 
 * @author Nathanael Van Vorst
 * @author Neil Goldman
 */
public class EnvironmentModifier {
	/**
	 * Deletes an environment from the workspace meeting the given parameters. 
	 * If the environment that is deleted is a PrimoGENI environment we will also try to deallocate the slice.
	 * 
	 * @param modelType A String representing the environment's ID
	 * e.g. EnvironmentFileModel.primoGENI_environment
	 * 
	 * @param A String representing the environment model's filename
	 * in the workspace this would appear as [modelname].env
	 * modelName can contain the .env extension, or not, it doesn't matter
	 */
	public static void deleteEnvironment(String modelType, String modelName) {
		try {
			getConfigurationProject().refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (CoreException e1) {
		}
		try {
			Class.forName("monitor.provisoners.ProtoGENICaller");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		IFolder envFolder;
		IProject newProject = getConfigurationProject();
		envFolder = newProject.getFolder(modelType);
		
		IFile file;
		if (modelName.endsWith(".env"))
			file = envFolder.getFile(modelName);
		else
			file = envFolder.getFile(modelName+".env");
		try {
			file.delete(false, null);
		} catch (CoreException e) {
			System.err.println("Could not delete file "+file.getName());
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 
	 * The the environment from a file.
	 * @param modelType
	 * @param modelName
	 * @return
	 * @throws CoreException
	 * @throws IOException
	 */
	public static Properties loadEnv(String modelType, String modelName) throws CoreException, IOException {
		IProject newProject = getConfigurationProject();
		IFolder envFolder = newProject.getFolder(modelType);
		IFile file;
		if (modelName.endsWith(".env"))
			file = envFolder.getFile(modelName);
		else
			file = envFolder.getFile(modelName+".env");
		Properties settings = new Properties();
		InputStream is = file.getContents();
		settings.load(is);
		return settings;
	}
	
	/*
	 */
	
	/**
	 * Store the environment configuration in the workspace
	 * 
	 * How to create an EnvironmentFileModel to pass
	 * model = new EnvironmentFileModel();
	 * model.name = "myName";
	 * model.environment = EnvironmentFileModel.primoGENI_environment;
	 * 
	 * //now to the environment specific variables
	 * //only fill in the ones needed for the environment you're saving
	 * //the rest are omitted automatically
	 * model.PrimoGENI_certificateFileLocation = "C:/tmp/yourCertificate.pem";
	 * model.PrimoGENI_passphrase = "password";
	 * model.numMachines = 50;
	 * EnvironmentModifier.createEnvironment(model)
	 * 
	 * @param model the model to store
	 * @return whether or not it was successfull
	 */
	public static IFile createEnvironment(EnvironmentFileModel model) {
		IFolder destinationFolder = getEnvironmentFolder(model);
		
		IFile file;
		if (model.name.endsWith(".env"))
			file = destinationFolder.getFile(model.name);
		else
			file = destinationFolder.getFile(model.name+".env");
		
		ByteArrayInputStream is = new ByteArrayInputStream(model.getInitialFileContents().getBytes());
		createFile(file, is);
		
		return file;
	}
	
	
	/**
	 * This method checks whether the environments folder for the selected environment exists.
	 *
	 * @return String	The name of the newly created folder
	 */
	public static IFolder getEnvironmentFolder(EnvironmentFileModel model){
		//get the results folder from this project
		IProject newProject = getConfigurationProject();

		//create the virtual resource for the new experiment results folder
		IFolder environmentFolder = newProject.getFolder(model.environment.str);
		//attempt to create the physical folder
		try{
			EnvironmentModifier.createFolder(environmentFolder);
		}catch(CoreException e){
			System.err.println("Error creating specific environment folder.");
			e.printStackTrace();
		}

		return environmentFolder;
	}
	
	/**
	 * Get the project configuration
	 * 
	 * @return
	 */
	public static IProject getConfigurationProject(){
		IProject newProject = ResourcesPlugin.getWorkspace().getRoot().getProject("Configuration");
		if(!newProject.exists()){
			try {
				newProject.create(null);
			} catch (CoreException e) {
				System.err.println("Error creating the configuration project");
				e.printStackTrace();
			}
		}
		if(!newProject.isOpen()){
        	try {
				newProject.open(null);
			} catch (CoreException e) {
				System.err.println("Error opening the configuration project");
				e.printStackTrace();
			}
        }
		return newProject;
	}
	
	/**
	 * Creates the given virtual IFile as a physical file in the Workspace.
	 *
	 * @param file					The virtual file to be created
	 * @param in					The data to write into the new file
	 * @return						TRUE if the file creation was succesful, FALSE otherwise
	 */
	public static boolean createFile(IFile file, InputStream in){
		CreateFileOperation op = new CreateFileOperation(file,null,null,"file creation");
		try{
			op.execute(null, null);
			file.setContents(in, true, false, null);
		} catch(ExecutionException e){
			//error creating new file
			System.err.println("error creating new file");
			e.printStackTrace();
			return false;
		} catch (CoreException e) {
			//error setting contents
			System.err.println("error setting contents");
			e.printStackTrace();
		}
		return true;
	}

    /**
     * Create a new physical folder on disk from an IFolder
     *
     * @param folder					The virtual folder to be created
     * @throws CoreException
     */
    public static void createFolder(IFolder folder) throws CoreException {
    	//get the parent of the folder
        IContainer parent = folder.getParent();
        //create the parent folder recursively if it doesn't exist
        if (parent instanceof IFolder) {
            createFolder((IFolder) parent);
        }
        //create the folder if it doesn't exist
        if (!folder.exists()) {
            folder.create(false, true, null);
        }
    }
}
