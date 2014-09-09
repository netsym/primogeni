package slingshot.configuration;

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


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.navigator.wizards.WizardShortcutAction;

import slingshot.Util;

/**
 * This class handles Slingshot's main configuration file. This file contains the location in the local disk of the primex installation directory and the web server.
 * This class contains static methods with basic file operations concerning the configuration .xml file.
 *
 * @author Eduardo Pena
 */
@SuppressWarnings("restriction")
public class ConfigurationHandler {

	/** The name of the configuration file on disk */
	private static final String FILENAME = "slingshot_configuration.conf";

	/** The actual file object of Slingshot's configuration file */
	private static File configFile;
	/** A String containing the path location of the Primex installation directory */
	private static String primexDirectory = "";


	/**
	 * Sets the path of the Primex installation directory
	 * @param pd				The Primex installation directory path
	 */
	public static void setPrimexDirectory(String pd){
		primexDirectory = pd;
		if (!pd.endsWith(File.separator))
			pd += File.separator;
	}

	/**
	 * Returns the path of the Primex installation directory
	 * @return String The Primex installation directory path
	 */
	public static String getPrimexDirectory(){
		if(primexDirectory.equalsIgnoreCase("")){
			loadContentsFromFile();
			System.out.println(primexDirectory);
			if(primexDirectory.equalsIgnoreCase("")) {
				checkForInitialConfiguration();
				System.err.println("Primex installation directory not set");
			}
		}
		if (!primexDirectory.endsWith(File.separator))
			primexDirectory += File.separator;
		return primexDirectory;
	}
	
	public static String getOpenVPNDirectory() {
		return getPrimexDirectory()+"/netsim/src/emuproxy/openvpn";
	}

	/**
	 * This method determines whether the Slingshot configuration .xml file exists on this computer or not.
	 *
	 * @return boolean 	TRUE if the file exists, FALSE otherwise
	 */
	public static boolean configFileExists(){
		//get the current configuration file on the disk
		loadConfigFile();
		return configFile.exists();
	}

	/**
	 * This method writes the new configuration .xml file to the disk. If the file doesn't exist yet, it creates a new configuration .xml file.
	 * If the file already exists, it deletes the previous .xml file and creates a new one.
	 */
	public static void writeConfigFile(){
		//get the current configuration file on the disk
		loadConfigFile();
		//get the contents for the file
		String contents = generateConfigFileContents();
		//set the system variables
		System.clearProperty("primex.dir");
		System.setProperty("primex.dir", ConfigurationHandler.getPrimexDirectory());
		System.clearProperty("python.path");
		System.setProperty("python.path", 
				ConfigurationHandler.getPrimexDirectory()+"/netIDE/jprime/jprime.jar:"+
				ConfigurationHandler.getPrimexDirectory()+"/netscript/lib/hsqldb.jar:");
		
		//check if the file exists
		if(!configFileExists()){
			//it doesn't exist, create a new one
			createFile(contents);
		}else{
			//it exists, delete the current file and create a new one
			deleteCurrentConfigFile();
			createFile(contents);
		}
	}

	/**
	 * This method generates the contents for a new Slingshot configuration .xml file according to the variables contained in this class.
	 *
	 * @return String			The contents for the new .xml file
	 */
	private static String generateConfigFileContents(){
		return 	"primex_dir = " + getPrimexDirectory();
	}

	/**
	 * This method loads the current Slingshot configuration .xml file on the disk.
	 */
	private static void loadConfigFile(){
		/* No path is given to get this file because this application automatically takes the blank path as its home directory, which is the product
		 * directory or the directory containing the 'workspace' directory
		 */
		configFile = new File(FILENAME);
	}

	/**
	 * This method loads the contents from the current Slingshot configuration .xml file to the variables on this class.
	 */
	private static void loadContentsFromFile(){
		//get the current file from disk
		loadConfigFile();
		//check if the file exists
		if(configFileExists()){
			//if it exists, create a parser and parse the .xml file to get the required variables
			Properties settings = new Properties();

			// try to find the configuration file
			try {
			    FileInputStream is = new FileInputStream(configFile) ;
			    settings.load(is);

				String newPrimexDir = settings.getProperty("primex_dir");
				if(!newPrimexDir.equalsIgnoreCase(""))
					primexDirectory = newPrimexDir;
					//save variables to class variables
			} catch (Exception e) {
			    System.err.println("Could not open the config file");
			}
		}
	}

	/**
	 * This method deletes the current Slingshot configuration .xml file from the disk.
	 */
	private static void deleteCurrentConfigFile(){
		try {
			configFile.delete();
		}catch(Exception e){
			//error deleting file
		}
	}

	/**
	 * This method creates a new Slingshot configuration .xml file given the new contents.
	 *
	 * @param contents			The contents for the new file (in xml format)
	 * @return boolean			TRUE if the file was correctly created, FALSE otherwise
	 */
	private static boolean createFile(String contents){
		try {
			//attempt to create the file with the given contents
			FileWriter fstream = new FileWriter(configFile);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(contents);
			out.close();
			return true;
		} catch (IOException e) {
			//something went wrong when creating file
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * This method checks the disk for the Slingshot configuration .xml file. If the file does not exist it opens a dialog asking the user whether he wants to create the file.
	 * If the user accepts to create the file, it opens the file creation wizard.
	 */
	public static void checkForInitialConfiguration(){
		//check if the file exists
		if(!ConfigurationHandler.configFileExists()){
			//if it doesn't create a Dialog and ask the user if he wants to create it
        	String title = "Configure Slingshot";
    		String message = "Slingshot preferences have not been set up yet.\n"+
    						 "These preferences need to be set in order for Slingshot to be able to compile and run experiments.\n"+
    						 "Do you want to set them now?";
    		if(Util.confirm(title, message )){
    			//open the Slingshot Configuration File Wizard
    			WizardShortcutAction configWizard = new WizardShortcutAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow(), PlatformUI.getWorkbench().getNewWizardRegistry().findWizard("slingshot.wizards.configuration.SlingshotConfig"));
    			configWizard.run();
    		}
        }
	}
}
