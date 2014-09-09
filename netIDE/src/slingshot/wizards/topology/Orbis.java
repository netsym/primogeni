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

import javax.swing.JTextArea;

/**
 * This class define Orbis topology generator.
 * @author Hao Jiang
 */
public class Orbis extends Generator
{
	public static final String GENERNAME = "Orbis";
	
	public static final String K = "k";
	public static final String N = "N";
	public static final String R = "r";
	public static final String HS = "HS";
	public static final String BWDist = "BWDist";
	public static final String BWMin = "BWMin";
	public static final String BWMax = "BWMax";
	
	public Orbis(String inputfile, JTextArea txtarea)
	{
		super(inputfile, GENERNAME, txtarea);
	}
	
	public void GenarateImportfile()
	{
		String topopath = "";
		if (!this.generatorpath.endsWith(File.separator))
				topopath += this.generatorpath + File.separator + "input.topo";
		else
			topopath += this.generatorpath + "input.topo";
		FileOperation.copyFile(topopath, this.import_file);
	}
	
	public boolean CreateTopology()
	{
		GenarateImportfile();
		
		String path = this.generatorpath;
		if (!path.endsWith(File.separator))
        	path += File.separator;
		
		String command = String.format("cd %s; %sscaleTopology.bash ", path, path);
		
		command += this.import_file + " ";

		GeneratorAttri attri;
		attri = this.attrisgrp.getAttri(K);
		if (attri != null)
			command += String.format("%s ", attri.getAttriValue());
		attri = this.attrisgrp.getAttri(N);
		if (attri != null)	
			command += String.format("%s ", attri.getAttriValue());
		attri = this.attrisgrp.getAttri(R);
		if (attri != null)
		{
			if (attri.getAttriValue().equals("1"));
			command += "r ";
		}
		
		command += "> " + this.export_file;
		
		if (ExecuteCommand(command))
		{
			this.slog.append("Successful generate the topology\n");
			xmlscript = new XmlScript(this.export_file, this.slog);
			attri = this.attrisgrp.getAttri(R);
			
			GeneratorAttri attrihs, attribwdist, attribwmin, attribwmax;
			XmlScript.BWAssginType BWtype;
			float BWmin, BWmax;
			
			attrihs = this.attrisgrp.getAttri(HS);
			attribwdist = this.attrisgrp.getAttri(BWDist);
			attribwmin = this.attrisgrp.getAttri(BWMin);
			attribwmax = this.attrisgrp.getAttri(BWMax);
			
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
			int hs = Integer.parseInt(attrihs.getAttriValue());
			if (attri.getAttriValue().equals("0"))
				xmlscript.Orbis2Xml(XmlScript.TopologyType.AS_LEVEL,
							  hs,
						      BWtype, BWmin, BWmax);
			else
				xmlscript.Orbis2Xml(XmlScript.TopologyType.ROUTER_LEVEL,
							  hs,
							  BWtype, BWmin, BWmax);
			
			return true;
		}
		else 
			return false;
	}
	
	public static boolean Check(String primexDirectory)
	{
		if (primexDirectory.endsWith(File.separator))
			primexDirectory += File.separator;
		
		String dkDist = primexDirectory + "topology/orbis/";
		String dkMetrics = primexDirectory + "topology/orbis/dkMetrics";
		String dkRescale = primexDirectory + "topology/orbis/dkRescale";
		String dkRewire = primexDirectory + "topology/orbis/dkRewire";
		String dkTopoGen0k = primexDirectory + "topology/orbis/dkTopoGen0k";
		String dkTopoGen1k = primexDirectory + "topology/orbis/dkTopoGen1k";
		String dkTopoGen2k = primexDirectory + "topology/orbis/dkTopoGen2k";
		String scaleTopology = primexDirectory + "topology/orbis/scaleTopology.bash";
		String input = primexDirectory + "topology/orbis/input.topo";
			
		File file1, file2, file3, file4, file5, file6, file7, file8, file9;
		file1 = new File(dkDist);
		file2 = new File(dkMetrics);
		file3 = new File(dkRescale);
		file4 = new File(dkRewire);
		file5 = new File(dkTopoGen0k);
		file6 = new File(dkTopoGen1k);
		file7 = new File(dkTopoGen2k);
		file8 = new File(scaleTopology);
		file9 = new File(input);
		if (file1.exists() && file2.exists() && file3.exists() &&
			file4.exists() && file5.exists() && file6.exists() &&
			file7.exists() && file8.exists() && file9.exists())
			return true;
		else
			return false;
	}
}
