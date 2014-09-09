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
 * This class define BRITE topology generator.
 * @author Hao Jiang
 */
public class BRITE extends Generator
{
	public static final String SEED = "seed";
	public static final String GENERNAME = "BRITE";
	public static final String EXPORT = "Export format";
	public static final String AS = "AS";
	public static final String ROUTER = "Router";
	public static final String TOPDOWN = "Top-Down";
	public static final String BOTTOMUP = "Bottom-Up";
	
	public static final String WAXMAN = "Waxman";
	public static final String BA = "Barabasi";
	public static final String BA2 = "BA2";
	public static final String GLP = "GLP";
	
	public static final String NAME = "Name";
	public static final String N = "N";
	public static final String HS = "HS";
	public static final String LS = "LS";
	public static final String NODEPLACEMENT = "NodePlacement";
	public static final String GROWTHTYPE = "GrowthType";
	public static final String M = "m";
	public static final String BWDIST = "BWDist";
	public static final String BWMIN = "BWMin";
	public static final String BWMAX = "BWMax";
	public static final String ALPHA = "alpha";
	public static final String BETA = "beta";
	public static final String P = "p";
	public static final String Q = "q";
	
	public static final String EDGECONN = "edgeConn";
	public static final String K = "k";
	public static final String BWINTER = "BWInter";
	public static final String BWINTERMIN = "BWInterMin";
	public static final String BWINTERMAX = "BWInterMax";
	public static final String BWINTRA = "BWIntra";
	public static final String BWINTRAMIN = "BWIntraMin";
	public static final String BWINTRAMAX = "BWIntraMax";
	public static final String DUPLICATION = "Duplication";
	
	public static final String GROUPING = "Grouping";
	public static final String ASSIGNTYPE = "AssignType";
	public static final String NUMAS = "NumAS";
	
	private List<String> vaerrsstrlist;
	
	private long seed;
	//Initialize functions
	public BRITE(String inputfile, JTextArea txtarea) 
	{
		super(inputfile, GENERNAME, txtarea);
		vaerrsstrlist = new ArrayList<String>();
		seed = 0;
	}
	

	public List<String> getVaErrsMessages()
	{
		return this.vaerrsstrlist;
	}
	
	//Generator the Import file based on user input xml file
	public void GenarateImportfile()
	{
		GeneratorAttriGrp grp;
		String tmpstr = "";
		
		//write import file
		try
		{
		    // Create file 
		    FileWriter fstream = new FileWriter(this.import_file);
		    BufferedWriter out = new BufferedWriter(fstream);
		    
		    out.write("BriteConfig\n\n");
		    //Import
		    List<GeneratorAttriGrp> childattrislist = this.attrisgrp.getChildAttriGrpList();
		    grp = childattrislist.get(0);
		   
		    //AS || Router
		    if (grp.getAttriGrpName().contains(AS) || 
		    	grp.getAttriGrpName().contains(ROUTER))
		    	WriteFlagTopo(out, grp, false, null, null, null);
		    //TopDown
		    if (grp.getAttriGrpName().equals(TOPDOWN))
		    	WriteTopDownTopo(out, grp);
		    //ButtomUp
		    if (grp.getAttriGrpName().equals(BOTTOMUP))
		    	WriteButtomUpTopo(out, grp);
		    
		    //Export
		    //attrislist = this.Exportattrisgrp.getAttrisList();
		    out.write("BeginOutput\n");
		    //write Brite output
		    tmpstr = String.format("\t%s = %s \t\t#%s\n", "BRITE", "1", "#1/0=enable/disable output in BRITE format");
            out.write(tmpstr);
            /*
		    for (i = 0; i < attrislist.size(); i++)
		    {
		    	attri = (GeneratorAttri)attrislist.get(i);
		    	if (!attri.getAttriValue().equals(""))
		    	{
		    		tmpstr = String.format("\t%s = %s \t\t#%s\n", attri.getAttriName(),
		    				               attri.getAttriValue(), attri.getAttriDescri());
		    		out.write(tmpstr);
		    	}
		    }
		    */
			out.write("EndOutput\n\n");
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
	
	private void WriteFlagTopo(BufferedWriter out, GeneratorAttriGrp attrisgrp, 
			                  boolean topdown, 
			                  String BWDist, String BWMin, String BWMax) throws IOException
	{
		GeneratorAttriGrp grp = null;
		GeneratorAttri attri = null;
		int i;
		if (attrisgrp.getAttri(SEED) != null)
			seed = Long.parseLong((attrisgrp.getAttri(SEED)).getAttriValue()); 
		
		out.write("BeginModel\n");
		if (attrisgrp.getAttriGrpName().contains(AS))
		{
			//BA
			if (attrisgrp.getChildAttriGrpList().size() == 0)
				out.write("\tName =  4		 #Router Barabasi-Albert=2, AS Barabasi-Albert =4\n");
			else
			{
				grp = (GeneratorAttriGrp)attrisgrp.getChildAttriGrpList().get(0);
				//Waxman
				if (grp.getAttriGrpName().contains(WAXMAN))
					out.write("\tName = 3		 #Router Waxman = 1, AS Waxman = 3\n");
				//BA2
				if (grp.getAttriGrpName().contains(BA2))
					out.write("\tName = 10		 #Router Barabasi-Albert=9, AS Barabasi-Albert =10\n");
				//GLP
				if (grp.getAttriGrpName().contains(GLP))
					out.write("\tName = 12 		 #Router GLP=11, AS GLP=12\n");
					
			}
		}
		else if (attrisgrp.getAttriGrpName().contains(ROUTER))
		{
			if (attrisgrp.getChildAttriGrpList().size() == 0)
				//BA
				out.write("\tName =  2		 #Router Barabasi-Albert=2, AS Barabasi-Albert =4\n");
			else
			{
				grp = (GeneratorAttriGrp)attrisgrp.getChildAttriGrpList().get(0);
				//Waxman
				if (grp.getAttriGrpName().contains(WAXMAN))
					out.write("\tName =  1		 #Router Waxman = 1, AS Waxman = 3\n");
				//BA2
				if (grp.getAttriGrpName().contains(BA2))
					out.write("\tName =  9		 #Router Barabasi-Albert=9, AS Barabasi-Albert =10\n");
				//GLP
				if (grp.getAttriGrpName().contains(GLP))
					out.write("\tName = 11 		 #Router GLP=11, AS GLP=12\n");
			}
		}
		
		for (i = 0; i < attrisgrp.getAttrisList().size(); i++)
	    {
	    	attri = (GeneratorAttri)attrisgrp.getAttrisList().get(i);
	    	if (!attri.getAttriValue().equals(""))
	    	{
	    		String tmpstr = String.format("\t%s = %s \t\t#%s\n", attri.getAttriName(),
	    				               attri.getAttriValue(), attri.getAttriDescri());
	    		out.write(tmpstr);
	    	}
	    }
		
		if (grp != null)
		{
			for (i = 0; i< grp.getAttrisList().size(); i++)
			{
				attri = (GeneratorAttri)grp.getAttrisList().get(i);
		    	if (!attri.getAttriValue().equals(""))
		    	{
		    		String tmpstr = String.format("\t%s = %s \t\t#%s\n", attri.getAttriName(),
		    				               attri.getAttriValue(), attri.getAttriDescri());
		    		out.write(tmpstr);
		    	}	
			}
		}
		if (topdown)
		{
			out.write(String.format("\tBWDist = %s \t\t#Constant = 1, Uniform =2, HeavyTailed = 3, Exponential =4\n", 
					                BWDist));
			out.write(String.format("\tBWMin = %s \t\t#The minimum bandwidth assigned to an edge\n", 
					                BWMin));
			out.write(String.format("\tBWMax = %s \t\t#The maximum bandwidth assigned to an edge\n",
					                BWMax));
		}
		out.write("EndModel\n\n");
	}
	
	private void WriteTopDownTopo(BufferedWriter out, GeneratorAttriGrp attrisgrp) throws IOException
	{
		GeneratorAttriGrp grp = null;
		GeneratorAttri attri = null;
		int i;
		
		out.write("BeginModel\n");
		out.write("\tName = 5		 #Top Down = 5\n");
		
		String BWInter = null, BWInterMin = null, BWInterMax = null;
		String BWIntra = null, BWIntraMin = null, BWIntraMax = null;
		
		if (attrisgrp.getAttri(SEED) != null)
			seed = Long.parseLong((attrisgrp.getAttri(SEED)).getAttriValue());
		
		for (i = 0; i < attrisgrp.getAttrisList().size(); i++)
	    {
	    	attri = (GeneratorAttri)attrisgrp.getAttrisList().get(i);
	    	if (!attri.getAttriValue().equals(""))
	    	{
	    		String tmpstr = String.format("\t%s = %s \t\t#%s\n", attri.getAttriName(),
	    				               attri.getAttriValue(), attri.getAttriDescri());
	    		out.write(tmpstr);
	    		
	    		if (attri.getAttriName().equals(BWINTRA))
	    			BWIntra = attri.getAttriValue();
	    		if (attri.getAttriName().equals(BWINTRAMIN))
	    			BWIntraMin = attri.getAttriValue();
	    		if (attri.getAttriName().equals(BWINTRAMAX))
	    			BWIntraMax = attri.getAttriValue();
	    		
	    		if (attri.getAttriName().equals(BWINTER))
	    			BWInter = attri.getAttriValue();
	    		if (attri.getAttriName().equals(BWINTERMIN))
	    			BWInterMin = attri.getAttriValue();
	    		if (attri.getAttriName().equals(BWINTERMAX))
	    			BWInterMax = attri.getAttriValue();
	    	}
	    }
		out.write("EndModel\n\n");
		
		//write AS
		grp = attrisgrp.getChildAttriGrpList().get(0);
		WriteFlagTopo(out, grp, true, BWInter, BWInterMin, BWInterMax);
		
		//write Router
		grp = attrisgrp.getChildAttriGrpList().get(1);
		WriteFlagTopo(out, grp, true, BWIntra, BWIntraMin, BWIntraMax);
	}
	
	private void WriteButtomUpTopo(BufferedWriter out, GeneratorAttriGrp attrisgrp) throws IOException
	{
		GeneratorAttriGrp grp = null;
		GeneratorAttri attri = null;
		int i;
		
		String BWInter = null, BWInterMin = null, BWInterMax = null;
		
		if (attrisgrp.getAttri(SEED) != null)
			seed = Long.parseLong((attrisgrp.getAttri(SEED)).getAttriValue());
		
		out.write("BeginModel\n");
		out.write("\tName = 6		 #Bottom Up  = 6\n");
		
		for (i = 0; i < attrisgrp.getAttrisList().size(); i++)
	    {
	    	attri = (GeneratorAttri)attrisgrp.getAttrisList().get(i);
	    	if (!attri.getAttriValue().equals(""))
	    	{
	    		String tmpstr = String.format("\t%s = %s \t\t#%s\n", attri.getAttriName(),
	    				               attri.getAttriValue(), attri.getAttriDescri());
	    		out.write(tmpstr);
	    	}
	    	
	    	if (attri.getAttriName().equals(BWINTER))
    			BWInter = attri.getAttriValue();
    		if (attri.getAttriName().equals(BWINTERMIN))
    			BWInterMin = attri.getAttriValue();
    		if (attri.getAttriName().equals(BWINTERMAX))
    			BWInterMax = attri.getAttriValue();
	    }
		out.write("EndModel\n\n");
		
		//write Router
		grp = attrisgrp.getChildAttriGrpList().get(0);
		WriteFlagTopo(out, grp, true, BWInter, BWInterMin, BWInterMax);
	}
	
	//call the generator to generate the topology
	public boolean CreateTopology()
	{
		GenarateImportfile();
		
		//String command1 = String.format("sh %smake_seed_file.sh", this.generatorpath);
		//generate seed file
		//if (!ExecuteCommand(command1))
			//return false;
		
		//String seedfile = this.generatorpath + "seed_file";
		/*
		File seedfile = null;
		
		try
		{
		    seedfile = File.createTempFile("seed_file", "");
			FileWriter fstream = new FileWriter(seedfile);
			BufferedWriter out = new BufferedWriter(fstream);
			
			out.write("PLACES 4667413275407560996	# used when placing nodes on the plane\n");
			out.write("CONNECT 33949306459122155	# used when interconnecting nodes\n");
			out.write("EDGE_CONN 9039194316870123313	# used in the edge connection method of top down hier\n");
			out.write("GROUPING -5364443968085952173	# used when deciding which routers to group into an AS in bottom up hier\n");
		    out.write("ASSIGNMENT -9072057906802981032	# used when deciding how many routers to group into an AS in bottom up hier\n");
			out.write("BANDWIDTH 6959567826015411091	# used when assigning bandwidths\n");
			
			out.close();
			seedfile.deleteOnExit();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return false;
		}
		*/
		//random seed
		
		String command2 = String.format("java -cp %sdist/brite.jar brite.main.Brite %s %s %d", 
										this.generatorpath, 
										this.import_file, this.export_file, this.seed);
		
		if (ExecuteCommand(command2))
		{
			xmlscript = new XmlScript(this.export_file + ".brite", 
										  this.slog);
			List<GeneratorAttriGrp> childattrislist = this.attrisgrp.getChildAttriGrpList();
			GeneratorAttriGrp grp = childattrislist.get(0);
			   
		    //AS || Router
		    if (grp.getAttriGrpName().contains(AS) || 
		    	grp.getAttriGrpName().contains(ROUTER))
		    {
		    	xmlscript.Brite2XmlFlat();
		    }
		    //TopDown || ButtomUp
		    else if (grp.getAttriGrpName().contains(TOPDOWN) ||
		    		 grp.getAttriGrpName().contains(BOTTOMUP))
		    {
		    	xmlscript.Brite2XmlHierarchical();
		    }
		    
			return true;
		}
		else
			return false;
	}
	
	public static boolean Check(String primexDirectory)
	{
		if (primexDirectory.endsWith(File.separator))
			primexDirectory += File.separator;
		
		String brite= primexDirectory + "topology/brite/dist/brite.jar";
		String make_seed_file = primexDirectory + "topology/brite/make_seed_file.sh";
		
		File file1, file2;
		file1 = new File(brite);
		file2 = new File(make_seed_file);
		
		if (file1.exists() && file2.exists())
			return true;
		else
			return false;
	}
	
}
