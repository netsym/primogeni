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

import java.io.File;
import java.util.Random;

import javax.swing.JTextArea;

/**
 * This class define Inet topology generator.
 * @author Hao Jiang
 */
public class Inet extends Generator
{
	public static final String GENERNAME = "Inet";
	
	public static final String N = "N";
	public static final String k = "k";
	public static final String n = "n";
	public static final String SEED = "seed";
	public static final String OF = "output file";
	
	public Inet(String inputfile, JTextArea text) 
	{
		super(inputfile, GENERNAME, text);
	}
	
	public boolean CreateTopology()
	{
		String path = this.generatorpath;
		if (!path.endsWith(File.separator))
        	path += File.separator;
		
		//String command = String.format("cd %s; %sinet ", path, path);
		String command = String.format("%sinet ", path);
		GeneratorAttri attri;
		
		attri = this.attrisgrp.getAttri(N);
		if (attri != null)
			command += String.format("-n %s ", attri.getAttriValue());
		attri = this.attrisgrp.getAttri(k);
		if (attri != null)	
			command += String.format("-d %s ", attri.getAttriValue());
		attri = this.attrisgrp.getAttri(n);
		if (attri != null)
			command += String.format("-p %s ", attri.getAttriValue());
		attri = this.attrisgrp.getAttri(SEED);
		if (attri != null)
		{
			if (attri.getAttriValue().equals("0"))
			{
				Random random = new Random(System.currentTimeMillis());
				int seed = random.nextInt();
				command += String.format("-s %d ", seed);
			}
			else
				command += String.format("-s %s ", attri.getAttriValue());
		}
			
		//attri = this.attrisgrp.getAttri(OF);
		//if (attri != null)
			//command += String.format("-f %s ", attri.getAttriValue());
		
		command += "> " + this.export_file;
		
		if (ExecuteCommand(command))
		{
			this.slog.append("Successful generate the topology\n");
			xmlscript = new XmlScript(this.export_file, this.slog);
			
			GeneratorAttri attribwdist, attribwmin, attribwmax;
			XmlScript.BWAssginType BWtype;
			float BWmin, BWmax;
			
			attribwdist = this.attrisgrp.getAttri("BWDist");
			attribwmin = this.attrisgrp.getAttri("BWMin");
			attribwmax = this.attrisgrp.getAttri("BWMax");
			
			switch(Integer.parseInt(attribwdist.getAttriValue()))
			{
			case 1:
				BWtype = XmlScript.BWAssginType.CONSTANT;
				break;
			case 2:
				BWtype = XmlScript.BWAssginType.UNIFORM;
				break;
			case 3:
				BWtype = XmlScript.BWAssginType.HEAVYTAILED;
				break;
			case 4:
				BWtype = XmlScript.BWAssginType.EXPONENTIAL;
				break;
			default:
				BWtype = XmlScript.BWAssginType.CONSTANT;
			}
			
			BWmin = Float.parseFloat(attribwmin.getAttriValue());
			BWmax = Float.parseFloat(attribwmax.getAttriValue());
			
			xmlscript.Inet2Xml(BWtype, BWmin, BWmax);
			return true;
		}
		else 
			return false;
	}
	
	public static boolean Check(String primexDirectory)
	{
		if (primexDirectory.endsWith(File.separator))
			primexDirectory += File.separator;
		
		String inet = primexDirectory + "/topology/inet/inet";
		
		File file;
		file = new File(inet);
		
		if (file.exists())
			return true;
		else
			return false;
	}
}
