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


import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Properties;

import jprime.util.ComputeNode;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;

/**
 *  This class marshalls environment configurations to/from files stored within the slingshot workspace.
 *  
 * @author Nathanael Van Vorst
 * @author Neil Goldman
 */
public class EnvironmentFileModel {
	public String name = "";
	public EnvType environment = null;

	/** ************************ **/
	/** REMOTE CLUSTER VARIABLES **/
	/** ************************ **/
	public LinkedList<ComputeNode> nodes = null;
	public String linked_env_name = "";
	public EnvType linked_env_type = null;
	public String slice_name="";


	/**
	 * Return the contents of this object as a string to be stored in the file
	 * 
	 * @return
	 */
	public String getInitialFileContents(){
		if(environment == null)
			throw new RuntimeException("the environment type is null!");
		if(name.endsWith(".env")) name=name.substring(0,name.length()-4);
		String contents = "environment = "+environment.str + "\nname = "+name+"\n";
		switch(environment) {
		case PROTO_GENI:
		case REMOTE_CLUSTER:
			contents +=
			"\nnodes = "+ComputeNode.toString(nodes) +
			"\nlinked_env_name = "+linked_env_name+
			"\nlinked_env_type = "+((linked_env_type==null)?"":linked_env_type.str)+
			"\nslice_name = "+((slice_name==null)?"":slice_name)+ "\n";;
			break;
		default:
			throw new RuntimeException("Unknown enviornment type!");
		}
		return contents;
	}


	/**
	 * Read in the configuration from a file
	 * 
	 * @param configFile
	 * @return
	 * @throws CoreException
	 * @throws IOException
	 */
	public static EnvironmentFileModel fromFile(IFile configFile) throws CoreException, IOException {
		//set up the variables
		//Get our environment file first
		Properties settings = new Properties();
		//Load the data from our config file
		InputStream is = configFile.getContents();
		settings.load(is);

		EnvironmentFileModel rv = new EnvironmentFileModel();
		rv.environment = EnvType.fromString(settings.getProperty("environment"));
		rv.name = settings.getProperty("name");
		if(rv.name.endsWith(".env")) rv.name=rv.name.substring(0,rv.name.length()-4);
		switch(rv.environment) {
		case PROTO_GENI:
		case REMOTE_CLUSTER:
			try {
				rv.nodes = ComputeNode.fromString(settings.getProperty("nodes").replaceAll("\\s", ""));
				if(rv.nodes.size()<=1) {
					throw new RuntimeException("XXX");
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("The enviornment seems to be corrupted; I was unable to read the 'master or slaves nodes' config from it. Please check the enviornment and try again.");
			}
			try {
				rv.linked_env_name = settings.getProperty("linked_env_name");
			} catch (Exception e) {
				rv.linked_env_name="";
			}
			try {
				rv.linked_env_type = EnvType.fromString(settings.getProperty("linked_env_type"));
			} catch (Exception e) {
				rv.linked_env_type=null;
			}
			try {
				rv.slice_name = settings.getProperty("slice_name");
			} catch (Exception e) {
				rv.slice_name="";
			}
			break;
		default:
			throw new RuntimeException("invalid enviornment!");
		}
		return rv;
	}	
}
