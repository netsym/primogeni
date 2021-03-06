package jprime.util;

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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import jprime.Experiment;
import jprime.database.Database;

/**
 * @author Nathanael Van Vorst
 *
 */
public class DynamicClassManager extends ClassLoader {
	private final Collection<String> classpath;
	private final String path;

	public DynamicClassManager(String path) {
		super();
		this.path=path;
		this.classpath=new HashSet<String>();
	}
	public DynamicClassManager(ClassLoader parent, Collection<String> classpath, String path) {
		super(parent);
		this.path=path;
		this.classpath=classpath;
	}
	/* (non-Javadoc)
	 * @see java.lang.ClassLoader#findClass(java.lang.String)
	 */
	@Override
	public Class<?> findClass(String name) {
		byte[] b = loadClassData(name);
		if(b==null) {
			return null;
		}
		return defineClass(name, b, 0, b.length);
	}

	private byte[] loadClassData(String name) {
		File f =new File(path+"/"+name+".class");
		if(f.exists()) {
			try {
				InputStream is = new FileInputStream(f);
				byte[] bytes = new byte[(int) f.length()];
				int offset = 0;
				int numRead = 0;
				while (offset < bytes.length
						&& (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
					offset += numRead;
				}
				if (offset < bytes.length) {
					jprime.Console.err.println("Could not completely read file "+f.getName());
					is.close();
					return null;
				}
				// Close the input stream and return bytes
				is.close();
				return bytes;
			} catch (FileNotFoundException e) {
			} catch (IOException e) {
			}
		}
		return null;
	}

	public ModelInterface loadModel(String name, Database db, Experiment exp) {
		name=compile(name);               //ERR2
		ModelInterface obj = null;
		try {
			Class<?> c = Class.forName(name, true,this);
			Class<?>[] argsClass = new Class<?>[] { Database.class, Experiment.class};
			Object[] args = new Object[] { db, exp };
			try {
				Constructor<?> ctor = c.getConstructor(argsClass);
				obj = (ModelInterface)ctor.newInstance(args);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return obj;
	}

	private String compile(String name) {
		name=name.replace(".java","").replace(".class", "").replace(path, "");
		File cls =new File(path+"/"+name+".class");
		File java =new File(path+"/"+name+".java");
		//System.out.println("  DynamicClassManager.java: Compiling ="+name);
		
		if(java.exists()) {
			if(cls.exists())
				cls.delete();
				JavaCompiler compiler = ToolProvider.getSystemJavaCompiler(); //ERROR.

				
				StandardJavaFileManager fileManager=null;
				try {
					fileManager = compiler.getStandardFileManager(null, null, null);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("  DynamicClassManager.java  Error loading FileManager. "+fileManager+"Exception="+e);
				}
				//**********************
				
//System.out.println("  DynamicClassManager.java, Compiler="+compiler+"  filemanager="+fileManager); //OBAIDA-- IF compiler is null then system has no native  java (openjdk-6jdk for ubuntu) installed.
//				Exception in thread "main" java.lang.NullPointerException
//				at jprime.util.DynamicClassManager.compile(DynamicClassManager.java:158)
//				at jprime.util.DynamicClassManager.loadModel(DynamicClassManager.java:109)
//				at jprime.TLVMaker.getModel(TLVMaker.java:352)
//				at jprime.TLVMaker.main(TLVMaker.java:230)

				
				
				
				List<String> options = new ArrayList<String>();
				if(classpath.size()>0) {
					options.add("-classpath");
					StringBuilder sb = new StringBuilder();
					for (String s : classpath)
						sb.append(s).append(File.pathSeparator);
					options.add(sb.toString());
				}
				StringWriter s = new StringWriter();
				Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjects(java);
				CompilationTask task = compiler.getTask(s, fileManager,null, options, null, compilationUnits);
				task.call();				
				if(s.getBuffer().length()>0) {
					jprime.Console.errorDialog("Error compiling '"+java+"':", s.toString());
				}
		}
		return name;
	}
}