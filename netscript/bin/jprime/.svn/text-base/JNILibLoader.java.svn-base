package jprime;

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
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


/**
 * @author Nathanael Van Vorst
 *
 */
public class JNILibLoader {
	public static boolean load(String libname) { 
        final Class<?> c = JNILibLoader.class;
        final URL location = c.getProtectionDomain().getCodeSource().getLocation();
        final String[] suffixes={"so","jnilib","dll"};
	    try {
	        // get the class object for this class, and get the location of it
	        //jprime.Console.out.println("Opening jar at "+location.getPath());
	        ZipFile zf = new ZipFile(location.getPath());
	        for(String suffix : suffixes) {
	        	final String lib="jnilib/lib"+libname+"."+suffix;
		        // jars are just zip files, get the input stream for the lib
		        //jprime.Console.out.println("Trying to load "+lib);
		        ZipEntry entry = zf.getEntry(lib);
		        if(entry != null) {
			        //jprime.Console.out.println(lib+" is in the zip");
			        InputStream in = zf.getInputStream(entry);
			        
			        // create a temp file and an input stream for it
			        File f = File.createTempFile("JARLIB-", "-lib"+libname+"."+suffix);
			        FileOutputStream out = new FileOutputStream(f);
			        
			        // copy the lib to the temp file
			        byte[] buf = new byte[1024];
			        int len;
			        while ((len = in.read(buf)) > 0)
			            out.write(buf, 0, len);
			        in.close();
			        out.close();
			
			        // load the lib specified by its absolute path and delete it
			        System.load(f.getAbsolutePath());
			        f.delete();
			        return true;
		        }
	        }
	    } catch (Exception e) {
	    }
	    System.loadLibrary(libname);
	    return false;
	}
}
