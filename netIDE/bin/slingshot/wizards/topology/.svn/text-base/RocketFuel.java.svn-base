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
 * This class define rocketfuel topology generator.
 * @author Hao Jiang
 */
public class RocketFuel extends Generator 
{
	public static final String GENERNAME = "rocketfuel";
	
	public static final String SEED = "seed";
	public static final String CUT = "cut";
	public static final String NETGEO = "netgeo";
	public static final String LATLONG = "latlong";
	public static final String GMLNOLABEL = "gml-no-label";
	public static final String HTMLNOMARKER = "html-no-marker";
	public static final String DMLSHOWIP = "dml-show-ip";
	
	public static final String AT_T = "AT_T";
	public static final String CABLE_WIRELESS = "Cable&amp;Wireless";
	public static final String EBONE = "Ebone";
	public static final String EXODUS = "Exodus";
	public static final String LEVEL3 = "Level3";
	public static final String SPRINT = "Sprint";
	public static final String TATACOMM = "TATACOMM";
	public static final String TELSTRA = "Telstra";
	public static final String TISCALI = "Tiscali";
	public static final String UUNET701 = "UUNET701";
	public static final String UUNET703 = "UUNET703";
	public static final String VERIO = "Verio";
	public static final String ABOVENET = "Abovenet";
	
	//private static final String EXPORT = "Export format";
	
	public RocketFuel(String inputfile, JTextArea text) 
	{
		super(inputfile, GENERNAME, text);
	}
	
	//call the generator to generate the topology
	public boolean CreateTopology()
	{
		String path = this.generatorpath;
		if (!path.endsWith(File.separator))
        	path += File.separator;
		
		String command = String.format("cd %s; %sext_getopo.pl ", path, path);
		//String command =  String.format("%sext_getopo.pl ", path);
		//random seed
		GeneratorAttri attri;
		attri = this.attrisgrp.getAttri(SEED);
	
		if (attri != null)
		{
			if (attri.getAttriValue().equals("0"))
			{
				Random random = new Random(System.currentTimeMillis());
				int seed = random.nextInt();
				command += String.format("-seed %d ", seed);
			}
			else
				command += String.format("-seed %s ", attri.getAttriValue());
		}
	
		//out put type
		command += "-type xml ";
		
		//cut level 
		attri = this.attrisgrp.getAttri(CUT);
		if (attri != null)
			command += String.format("-cut %s ", attri.getAttriValue());
		//out put file name
		command += String.format("-o %s ", this.export_file);
		
		command += "-netgeo netgeo.cache ";
		command += "-latlong latlong.cache ";
		/*
		//cache file with IP's location info
		attri = this.attrisgrp.getAttri(NETGEO);
		if (attri != null)
			command += String.format("-netgeo %s ", attri.getAttriValue());
		//cache file with city's latitude and longitude
		attri = this.attrisgrp.getAttri(LATLONG);
		if (attri != null)
			command += String.format("-latlong %s ", attri.getAttriValue());
		*/
		//no city labels in GML output
		/*
		attri = this.attrisgrp.getAttri(GMLNOLABEL);
		if (attri != null)
		{
			if (attri.getAttriValue().equals("1"))
				command += "-gml-no-label ";
		}
		// no location marker in HTML output
		attri = this.attrisgrp.getAttri(HTMLNOMARKER);
		if (attri != null)
		{
			if (attri.getAttriValue().equals("1"))
				command += "-html-no-marker ";
		}
		//assign IP address to interface in DML output
		attri = this.attrisgrp.getAttri(DMLSHOWIP);
		if (attri != null)
		{
			if (attri.getAttriValue().equals("1"))
				command += "-dml-show-ip ";
		}
		*/
		//ISP
		GeneratorAttriGrp grp = this.attrisgrp.getChildAttriGrpList().get(0);
		for (int i = 0; i < grp.getAttrisList().size(); i ++)
		{
			attri = grp.getAttrisList().get(i);
			if (!attri.getAttriValue().equals("0"))
				command += String.format("%s ", attri.getAttriValue());
		}
		
		if (ExecuteCommand(command))
		{
			//need to load the xmlfile for attach lan network
			xmlscript = new XmlScript(this.export_file + ".xml", 
					  				  this.slog);
			xmlscript.tree.Loadfile(this.export_file + ".xml");
			return true;
		}
		else 
			return false;
	}
	
	public static boolean Check(String primexDirectory)
	{
		if (primexDirectory.endsWith(File.separator))
			primexDirectory += File.separator;
		
		String ext_getopo = primexDirectory + "topology/rocketfuel/ext_getopo.pl";

		File file;
		file = new File(ext_getopo);
	
		if (file.exists())
			return true;
		else
			return false;
	}
}
