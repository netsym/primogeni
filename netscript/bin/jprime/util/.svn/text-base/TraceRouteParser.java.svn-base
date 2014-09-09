package jprime.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map.Entry;

import jprime.Experiment;
import jprime.Net.INet;
import jprime.database.Database;
import jprime.util.GraphOverlay.OverlayInfo;

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

/**
 * 
 * @author Nathanael Van Vorst
 *
 */
public class TraceRouteParser {
	public static final int green = -10223839; //prefuse.util.ColorLib.rgb(99,0xff,33)
	

	public static String createOverlayInfo(final File file) throws IOException {
		return createOverlayInfo(file,green,1.5);
	}
	
	public static String createOverlayInfo(final File file, final int fillcolor, final double size) throws IOException {
		StringBuffer rv = new StringBuffer();
		DataInputStream in = new DataInputStream(new FileInputStream(file));
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String str;
		while ((str = br.readLine()) != null)   {
			parseLine(rv, str, fillcolor, size);
		}
		in.close();
		return rv.toString();
	}
	
	public static String createOverlayInfo(final String file) {
		return createOverlayInfo(file,green,1.5);
	}
	
	public static String createOverlayInfo(final String file, final int fillcolor, final double size) {
		StringBuffer rv = new StringBuffer();
		String[] s = file.trim().split("\\n");
		for(String l : s) parseLine(rv, l, fillcolor, size);
		return rv.toString();
	}
	
	private static void parseLine(final StringBuffer rv, String str, final int fillcolor, final double size) {
		String[] ll =str.trim().split("\\s+");
		if(ll.length < 4) {
			if(!ll[0].startsWith("traceroute")) {
				jprime.Console.err.println("Skipping invalid traceroute line '"+str+"'. len="+ll.length);
			}
			return;
		}
		//we want to add a color info for each node and link.
		//the ip should be at index 2.
		String ip = ll[2].replaceAll("\\(", "").replaceAll("\\)", "");
		String node = "ip="+ip+",type=host,fill_color="+fillcolor+",size="+size+"\n";
		String link = "ip="+ip+",type=link,fill_color="+fillcolor+",size="+size+"\n";
		rv.append(node);
		rv.append(link);
	}
	
	
	public static void main(String[] args) throws IOException {
		
		Database db = Database.createDatabase();
		ModelInterface model = getModel(db,
				"CampusShortestPath.java",
				new File("/Users/vanvorst/Documents/workspace/primex/netscript/test/java_models/CampusShortestPath.java"),
				"/Users/vanvorst/Documents/workspace/primex/netscript/test/java_models/",
				"foobar");
		INet topnet = model.buildModel(model.loadParametersFromSystemProperties());
		HashMap<Long,OverlayInfo> overlay = getCampusOverlay(topnet);
		for(Entry<Long, OverlayInfo> e : overlay.entrySet()) {
			System.out.println(e.getKey()+" :: "+e.getValue());
		}
	}
	
	public static HashMap<Long,OverlayInfo> getCampusOverlay(INet topnet) {
		String traceroute = ""+
		"traceroute: Warning: google.com has multiple addresses; using 74.125.229.112\n"+
		"traceroute to google.com (74.125.229.112), 64 hops max, 52 byte packets\n"+
		" 1  xxx (192.1.8.6)  0.518 ms  0.200 ms  0.197 ms\n"+
		" 2  xxx (192.1.8.5)  0.518 ms  0.200 ms  0.197 ms\n"+
		" 3  xxx (192.1.8.61)  0.518 ms  0.200 ms  0.197 ms\n"+
		" 4  xxx (192.1.8.62)  0.518 ms  0.200 ms  0.197 ms\n"+
		" 5  xxx (192.1.8.37)  0.518 ms  0.200 ms  0.197 ms\n"+
		" 6  xxx (192.1.8.38)  0.518 ms  0.200 ms  0.197 ms\n"+
		" 7  xxx (192.1.8.57)  0.518 ms  0.200 ms  0.197 ms\n"+
		" 8  xxx (192.1.8.58)  0.518 ms  0.200 ms  0.197 ms\n"+
		" 9  xxx (192.1.8.141)  0.518 ms  0.200 ms  0.197 ms\n"+
		"10  xxx (192.1.8.142)  0.518 ms  0.200 ms  0.197 ms\n"+
		"11  xxx (192.1.6.141)  0.518 ms  0.200 ms  0.197 ms\n"+
		"12  xxx (192.1.6.142)  0.518 ms  0.200 ms  0.197 ms\n"+
		"13  xxx (192.1.6.161)  0.518 ms  0.200 ms  0.197 ms\n"+
		"14  xxx (192.1.6.162)  0.518 ms  0.200 ms  0.197 ms\n"+
		"15  xxx (192.1.6.1)  0.518 ms  0.200 ms  0.197 ms\n"+
		"16  xxx (192.1.6.11)  0.518 ms  0.200 ms  0.197 ms\n";
		return GraphOverlay.parse(topnet, createOverlayInfo(traceroute));
	}
	
	private static ModelInterface getModel(Database db, String fname, File file, String path, String name) throws IOException {
		Experiment exp = db.createExperiment(name, true);
		if(fname.endsWith(".java") || fname.endsWith(".class")) {
			DynamicClassManager dm = new DynamicClassManager(path);
			return dm.loadModel(fname, db, exp);
		}
		else if(fname.endsWith(".xml")) {
			return new XMLModelInterface(db,exp,file);
		}
		else if(fname.endsWith(".py")) {
			return new PythonModelInterface(db, exp, file);
		}
		else {
			throw new RuntimeException("Invalid model type! "+file.getCanonicalPath());
		}
		
	}

}
