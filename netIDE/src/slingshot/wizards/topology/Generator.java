package slingshot.wizards.topology;

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
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

import javax.swing.SwingConstants;
import javax.swing.JTextArea;

/**
 * This class define topology generator.
 * @author Hao Jiang
 */
public class Generator 
{
	//profile xml file
	//protected String profile;
	
	//user input xml file 
	//protected String inputfile;
	
	//generator name
	protected String generatorname;
	
	//the path of the generator
	protected String generatorpath;
	
	//generator import file
	protected String import_file;
	//generator export file
	protected String export_file;

	//List of import attributes group
	protected GeneratorAttriGrp attrisgrp;
	
	//List of export attributes group
	//protected GeneratorAttriGrp Exportattrisgrp;
	
	//Errors Message
	protected List<String> exerrstrlist;
	
	protected JTextArea slog;
	
	//project source folder path to input topology
	//protected String projectpath;
	
	public XmlScript xmlscript;
	
	public Process proc;
	
	public void setGeneratorName(String name)
	{
		this.generatorname = name;
	}
	public String getGeneratorName()
	{
		return this.generatorname;
	}
	
	public void setGeneratorPath(String path)
	{
		this.generatorpath = path;
	}
	public String getGeneratorPath()
	{
		return this.generatorpath;
	}
	
	public void setImportfilePath(String path)
	{
		this.import_file = path;
	}
	public String getImportfilePath()
	{
		return this.import_file;
	}
	
	public void setExportfilePath(String path)
	{
		this.export_file = path;
	}
	public String getExportfilePath()
	{
		return this.export_file;
	}
	
	public List<String> getExErrsMessages()
	{
		return this.exerrstrlist;
	}
	
	public GeneratorAttriGrp getAttrisGrp()
	{
		return this.attrisgrp;
	}
	/*
	public GeneratorAttriGrp getExportAttrisGrp()
	{
		return this.Exportattrisgrp;
	}
	*/
	//initialize functions
	public Generator()
	{
		//this.inputfile = "";
		this.generatorname = "";
		this.generatorpath = "";
		this.import_file = "";
		this.export_file = "";
		this.attrisgrp = new GeneratorAttriGrp("Attributes");
		//this.Exportattrisgrp = new GeneratorAttriGrp("Export");
		this.exerrstrlist = new ArrayList<String>();
		
		this.xmlscript = null;
		slog = null;
	}
		
	//Initialize the generator from input xml file
	public Generator(String inputfile, String genername, JTextArea txtarea)
	{
		this();
		
		List<Generator> generlist;
		generlist = XmlFunction.ReadXmlfile(inputfile);		
		if (generlist != null)
		{
			Generator gener;
			//find the generator with the give name
			for (int i = 0; i < generlist.size(); i++)
			{
				gener = generlist.get(i);
				if (gener.getGeneratorName().equals(genername))
				{
					CopyGenerator(gener);
					
					
					//String path = Platform.getInstanceLocation().getURL().getPath();
			    	//path += "Topology";
			    	
					//gener.generatorpath = path + gener.generatorpath;
					//gener.import_file = path + gener.import_file;
					//gener.export_file = path + gener.export_file;
					
					break;
				}
			}
		}
		
		//this.projectpath = projectpath;
		
		this.slog = txtarea;
	}
	
	public void CopyGenerator(Generator gener)
	{
		if (gener != null)
		{
			//this.inputfile = gener.getInputfile();
			this.generatorname = gener.getGeneratorName();
			this.generatorpath = gener.getGeneratorPath();
			this.import_file = gener.getImportfilePath();
			this.export_file = gener.getExportfilePath();
			this.attrisgrp = gener.getAttrisGrp();
			//this.Exportattrisgrp = gener.getExportAttrisGrp();
		}
	}
	
	//write the generator to a xml file
	public void WriteXML(String file)
	{
		List<Generator> list;
		list = new ArrayList<Generator>();
		list.add(this);
		XmlFunction.WriteXML(file, list);
	}
	
	public void KillProcess() 
	{
		if (proc != null) 
			proc.destroy();
	}
	
	//execute a linux  command
	public boolean ExecuteCommand(String execStr)
	{
	    //Runtime runtime = Runtime.getRuntime();
	    String outInfo="";
	    String tmp;
	    	  
	    InputStream errstream = null;
	    try
	    {
	    	//execute linux command
	        String[] args = new String[] {"sh", "-c", execStr};
	        
	        ProcessBuilder pb = new ProcessBuilder(args);
	        pb.redirectErrorStream(true);
	        
	    	//execute linux command 
	        //XXX set timer to kill this process in case it dies
	        
	        Process proc = pb.start();
	        proc.getOutputStream().close();
	        
	        errstream = proc.getErrorStream();
	        BufferedReader errstreambr = new BufferedReader(new InputStreamReader(errstream));
	       
	        InputStream inputstream = proc.getInputStream();
	        BufferedReader inputstreambr = new BufferedReader(new InputStreamReader(inputstream)); 
	        
	        String line;
	        while ((line = inputstreambr.readLine())!= null) 
	        {
	        	slog.append(line+"\n");
	        	
	        	Rectangle rect = slog.getVisibleRect();
	        	int a = slog.getScrollableBlockIncrement(rect, SwingConstants.VERTICAL, 1);
	        	rect.setLocation((int)rect.getX(), (int)rect.getY()+a);
	        	slog.scrollRectToVisible(rect);
	        	
	        	System.out.println(line);
	        }
  		
			while ((line = errstreambr.readLine())!=null) 
			{
				slog.append(line+"\n");
			
				Rectangle rect = slog.getVisibleRect();
				int a = slog.getScrollableUnitIncrement(rect, SwingConstants.VERTICAL, 1);
				slog.scrollRectToVisible(new Rectangle((int) rect.getX(), (int) rect.getY()+a, (int) rect.getWidth(), (int)rect.getHeight()));
				
				System.out.println(line);
			}
			//slog.paintImmediately(slog.getVisibleRect());
	  
	        try
	        {
	        	if (proc.waitFor() != 0)
	        	{
	        		tmp = "exit value = " + proc.exitValue();
	        		this.exerrstrlist.add(tmp);
	        		System.err.println("exit value = " + proc.exitValue());
	        		return false;
	        	}
	        }
	        catch (InterruptedException e)
	        {
	        	//this.exerrstrlist.add(e.toString());
	        	System.err.print(e);
	        	//e.printStackTrace();
	        	return false;
	        }
	    }
	    catch (IOException e)
	    {
	    	tmp = "exec error: " + e.getMessage();
	    	//this.exerrstrlist.add(tmp);
	    	System.out.println("exec error: " + e.getMessage());
	    	//e.printStackTrace();
	    	
	    	return false;
	    }
	    
	    
	    if (!outInfo.equals(""))
	    	this.exerrstrlist.add(outInfo);
	   
	    return true;
	}
	
	//Generate the import file for the generator will be implicated by the children class
	public void GenarateImportfile()
	{
		
	}
	
	//Create topology will be implicated by the children class
	public boolean CreateTopology()
	{
		return true;
	}

	//Copy export file into project folder
	/*
	public void ConvertintoProject(String modelname)
	{
		if (this.export_file !=null &&
			this.projectpath != null)
		{
			if (!this.projectpath.endsWith(File.separator))
				this.projectpath += File.separator;
			
			FileOperation.copyFile(this.export_file + ".xml", this.projectpath + modelname + ".xml");
		}			
	}
	*/
}