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

import java.io.*;
import java.util.*;

import javax.swing.JTextArea;

/**
 * @author Hao Jiang
 */
public class GT_ITM extends Generator
{
	public static final String GENERNAME = "GT-ITM";
	
	public static final String FLAT = "flat random";
	public static final String TRANSIT_STUB = "transit-stub";
	
	public static final String PURE_RANDOM = "Pure Random";
	public static final String WAXMAN1 = "Waxman 1";
	public static final String WAXMAN2 = "Waxman 2";
	public static final String DOAR_LESLIE = "Doar-Leslie";
	public static final String EXPONENTIAL = "Exponential";
	public static final String LOCALITY = "Locality";
	
	public static final String TOP_LEVEL = "top-level";
	public static final String TRANSIT_DOMAIN = "transit domain";
	public static final String STUB_DOMAIN = "stub domain";
	
	public static final String SEED = "seed";
	public static final String N = "n";
	public static final String SCALE = "scale";
	public static final String ALPHA = "alpha";
	public static final String BETA = "beta";
	public static final String GAMMA = "gamma";
	
	public static final String STUBSTRANSIT = "# stubs/transit";
	public static final String T_S = "# t-s edges";
	public static final String S_S = "# s-s edges";
	
	public static final String TOPO_TYPE = "Topology type";
	
	public static final String BWDist = "BWDist";
	public static final String BWMin = "BWMin";
	public static final String BWMax = "BWMax";
	
	private List<String> vaerrsstrlist;
	
	//Initialize functions
	public GT_ITM(String inputfile, JTextArea txtarea) 
	{
		super(inputfile, GENERNAME, txtarea);
		vaerrsstrlist = new ArrayList<String>();
	}
	

	public List<String> getVaErrsMessages()
	{
		return this.vaerrsstrlist;
	}
	
	//Generator the Import file based on user input xml file
	public void GenarateImportfile()
	{		
		//write import file
		try
		{
		    // Create file 
		    FileWriter fstream = new FileWriter(this.import_file);
		    BufferedWriter out = new BufferedWriter(fstream);
		    
		    GeneratorAttriGrp grp = null;
			 
		    //Import
		    List<GeneratorAttriGrp> childattrislist = this.attrisgrp.getChildAttriGrpList();
		    grp = childattrislist.get(0);
		   
		    //flat random grpah
		    if (grp.getAttriGrpName().equals(FLAT))
		    	WriteFlagTopo(out, grp, false);
		    if (grp.getAttriGrpName().equals(TRANSIT_STUB))
		    	WriteTransitStubTopo(out, grp);
		    	
		    //Close the output stream
		    out.close();
		}
		catch (Exception e)
		{
			//Catch exception if any
		    System.err.println("Error: " + e.getMessage());
		    e.printStackTrace();
		    this.exerrstrlist.add("Error: " + e.getMessage());
		}
	}
	
	private void WriteTransitStubTopo(BufferedWriter out, GeneratorAttriGrp attrisgrp)
			throws IOException
	{
		GeneratorAttri attri = null;
		
		String seed, stubstrans, t_s, s_s;
		attri = attrisgrp.getAttri(SEED);
		if (attri.getAttriValue().equals("0"))
		{
			Random random = new Random(System.currentTimeMillis());
			seed = String.valueOf(random.nextInt());
		}
		else
			seed = attri.getAttriValue();
		
		attri = attrisgrp.getAttri(STUBSTRANSIT);
		stubstrans = attri.getAttriValue();
		
		attri = attrisgrp.getAttri(T_S);
		t_s = attri.getAttriValue();
		
		attri = attrisgrp.getAttri(S_S);
		s_s = attri.getAttriValue();
		
		out.write("# <method keyword> <number of graphs> [<initial seed>]\n");
		out.write("# <# stubs/trans node> <#rand. t-s edges> <#rand. s-s edges>\n");
		out.write("# <n> <scale> <edgemethod> <alpha> [<beta>] [<gamma>]\n");
		
		String tmp;
		tmp = String.format("ts 1 %s\n", seed);
		out.write(tmp);
		tmp = String.format("%s %s %s\n", stubstrans, t_s, s_s);
		out.write(tmp);
		
		List<GeneratorAttriGrp> childattrislist = attrisgrp.getChildAttriGrpList();
		for (GeneratorAttriGrp grp : childattrislist)
			WriteFlagTopo(out, grp, true);
		
	}
	
	//create flag topology import file
	private void WriteFlagTopo(BufferedWriter out, GeneratorAttriGrp attrisgrp, 
			                   boolean call)
			throws IOException
	{
		GeneratorAttri attri = null;
		GeneratorAttriGrp grp = null;
		
		String tmp;
		
		if (!call)
		{
			attri = attrisgrp.getAttri(SEED);
			String seed;
			if (attri.getAttriValue().equals("0"))
			{
				Random random = new Random(System.currentTimeMillis());
				seed = String.valueOf(random.nextInt());
			}
			else
				seed = attri.getAttriValue();
			
			
			out.write("# <method keyword> <number of graphs> [<initial seed>]\n");
			out.write("# <n> <scale> <edgemethod> <alpha> [<beta>] [<gamma>]\n");
			tmp = String.format("geo 1 %s\n", seed);
			out.write("geo 1\n");
		}
		
		String n, scale, alpha;
		String type;
		
		attri = attrisgrp.getAttri(N);
		n = attri.getAttriValue();
		
		attri = attrisgrp.getAttri(SCALE);
		scale = attri.getAttriValue();
		
		attri = attrisgrp.getAttri(ALPHA);
		alpha = attri.getAttriValue();
		
		if (attrisgrp.getChildAttriGrpList().size() == 0)
			type = "3";
		else
		{
			grp = attrisgrp.getChildAttriGrpList().get(0);
			if (grp.getAttriGrpName().equals(WAXMAN1))
				type = "1";
			else if (grp.getAttriGrpName().equals(WAXMAN2))
				type = "2";
			else if (grp.getAttriGrpName().equals(DOAR_LESLIE))
				type = "4";
			else if (grp.getAttriGrpName().equals(EXPONENTIAL))
				type = "5";
			else if (grp.getAttriGrpName().equals(LOCALITY))
				type = "6";
			else
				type = "-1";
		}
		
		tmp = String.format("%s %s %s %s", n, scale, type, alpha);
		
		if (grp != null)
		{
			List<GeneratorAttri> attrislist = grp.getAttrisList();
			for (GeneratorAttri a : attrislist)
			{
				tmp += " " + a.getAttriValue();
			}
		}
		out.write(tmp + "\n");
	}
	
	
	//call the generator to generate the topology
	public boolean CreateTopology()
	{
		GenarateImportfile();
		
		String path = this.generatorpath;
		if (!path.endsWith(File.separator))
        	path += File.separator;
		
		String tmpfile = this.import_file + "-0.gb";
		String command1 = String.format("%sitm %s;%ssgb2alt %s %s", 
										path, this.import_file,
										path, tmpfile, this.export_file);
		//String command2 = String.format("%ssgb2alt %s %s", path, tmpfile, this.export_file);
		
		//translate gt-itm format will return false but still generate the file
		ExecuteCommand(command1);
		{			   
			//ExecuteCommand(command2);
			
			this.xmlscript = new XmlScript(this.export_file, this.slog);
			
			List<GeneratorAttriGrp> childattrislist = this.attrisgrp.getChildAttriGrpList();
			GeneratorAttriGrp grp = childattrislist.get(0);
			
			GeneratorAttri attritopo, attribwdist, attribwmin, attribwmax;
			XmlScript.BWAssginType BWtype;
			float BWmin, BWmax;
			
			attribwdist = grp.getAttri(BWDist);
			attribwmin = grp.getAttri(BWMin);
			attribwmax = grp.getAttri(BWMax);
			
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
			
			attritopo = grp.getAttri(TOPO_TYPE);
			if (grp.getAttriGrpName().equals(FLAT))
			{
				if (attritopo.getAttriValue().equals("2"))
					xmlscript.gtitm2XmlFlat(XmlScript.TopologyType.AS_LEVEL,
								            BWtype, BWmin, BWmax);
				else
					xmlscript.gtitm2XmlFlat(XmlScript.TopologyType.ROUTER_LEVEL,
							                BWtype, BWmin, BWmax);
			}
			else 
			{
				xmlscript.gtitm2XmlHierarchical(BWtype, BWmin, BWmax);
			}
			
			return true;
		}
	}
	
	public static boolean Check(String primexDirectory)
	{
		if (primexDirectory.endsWith(File.separator))
			primexDirectory += File.separator;
		
		String itm = primexDirectory + "topology/gt-itm/bin/itm";
		String sgb2alt = primexDirectory + "topology/gt-itm/bin/sgb2alt";
		
		File file1, file2;
		file1 = new File(itm);
		file2 = new File(sgb2alt);
		
		if (file1.exists() && file2.exists())
			return true;
		else
			return false;
	}
}
