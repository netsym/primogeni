package slingshot.spyconsole;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.eclipse.core.resources.IFile;

/**
 * This action will display the FileChooser dialog and allow the user
 * to select a Jython script. Once selected, the Jython script is entered into
 * the console.
 *
 * @author Jeff Davies
 * @version 1.0
 */

public class ScriptLoader{

   SPyConsole console = null;
   static String driver_dir = System.getProperty("user.dir");

   /** Used to remember the last directory accessed by the user */
   static File lastDirectoryAccessed = new File(driver_dir,"../UserCode/");

   public ScriptLoader(SPyConsole con) {
      console = con;
   }
   
   public void loadScript(IFile file) {
      // OK. We have a valid file. Lets read it into a String object
	  if(file != null){
		  String data = loadFile(file);
	   	  console.executeCommandSet(data);
	  }
	  else{
		  System.out.println("Invalid File");
	  }
      //console.isReady();
      //DataManager.startVisualization();
   }


   /**
    * Read in the specified file and return its contents as a String
    * @param script The script as a File
    * @return String The contents of the specified script
    */
   private String loadFile(IFile script) {
		if( script == null ) return null;
		String line;
		StringBuffer data = new StringBuffer();
		System.out.println("Loading JPython script [ " + script.getName() + " ]");
		try {
		    InputStream fileContents;
		    fileContents = script.getContents();
		    BufferedReader br = new BufferedReader(new InputStreamReader(fileContents));
			while((line = br.readLine()) != null) {
				//System.out.println("--- " + line);
				data.append(line + "\r\n");
			}
		} catch (Exception e) {
		}
		return data.toString();
	}
}
