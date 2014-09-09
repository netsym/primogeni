package slingshot.navigator;

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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Eduardo Pena
 *
 */
public class FileOperations {
	public static void copyDirectory(File oldDir, File newDir) throws IOException {
		if (!newDir.exists())  
		newDir.mkdir();
	 
		File[] children = oldDir.listFiles();
		for (File sourceChild : children) {
			String name = sourceChild.getName();
			File destChild = new File(newDir, name);
			if (sourceChild.isDirectory()) {
				copyDirectory(sourceChild, destChild);
			}
			else
				copyFile(sourceChild, destChild);
		}
	}
	public static void copyFile(File source, File dest) throws IOException {
		if(!dest.exists())
		    dest.createNewFile();
		
		InputStream in = null;
		OutputStream out = null;
		  
		try {
			in = new FileInputStream(source);
			out = new FileOutputStream(dest);
			
			byte[] buf = new byte[1024];
			int len;
		      
			while((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
				}
			}
		  
		  finally {
			  in.close();
			  out.close();
			  }
		  }
	public static boolean delete(File resource) throws IOException { 
		System.out.println("\n\nDelete "+resource.getAbsolutePath());
		if(resource.isDirectory()) {
			File[] childFiles = resource.listFiles();
			for(File child : childFiles) {
				delete(child);
			}
						
		}
		return resource.delete();
	}
}
