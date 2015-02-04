package slingshot.visualization;

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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.TreeMap;

import jprime.IModelNode;
import jprime.Host.IHost;
import jprime.Interface.IInterface;
import jprime.gen.ModelNodeVariable;
import monitor.util.Utils;

import datawriter.DataWriter;
import datawriter.DataStreamWriter;

import slingshot.configuration.ConfigurationHandler;

/**
 * @author Renan Santana
 * @author Nathanael Van Vorst
 *
 */
public class LiveGraphManager {
	private final String csv_file;
	private String pid2;
	
	private static class datum {
		public final String name;
		public String value;
		public datum(String n, String v) {
			this.name=n;
			this.value=v;
		}
	}
	
	private DataStreamWriter ds;
	
	// dataSets contains dataSet 
	private final TreeMap<Long,TreeMap<Integer,datum>> dataSets = new TreeMap<Long,TreeMap<Integer,datum>>();
	// this is the dataSet
	private static final int num_in_bytes = ModelNodeVariable.num_in_bytes();
	private static final int num_out_bytes = ModelNodeVariable.num_out_bytes();
	private static final int num_in_packets = ModelNodeVariable.num_in_packets();
	private static final int num_out_packets = ModelNodeVariable.num_out_packets();
	private static final int queue_size = ModelNodeVariable.queue_size();
	private static final int traffic_intensity = ModelNodeVariable.traffic_intensity();
	private static final int packets_in_per_sec = ModelNodeVariable.packets_in_per_sec();
	private static final int packets_out_per_sec = ModelNodeVariable.packets_out_per_sec();
	private static final int bytes_in_per_sec = ModelNodeVariable.bytes_in_per_sec();
	private static final int bytes_out_per_sec = ModelNodeVariable.bytes_out_per_sec();

	/*
	 * setup the settings for the plotter for when it opens and 
	 * store these settings in a file
	 * */ 
	public LiveGraphManager(IHost h) {
		super();
		
		csv_file = "/home/renan/Desktop/temp.csv";
//		csv_file = Utils.TMP_DIR + "/host_" + h.getUID() + ".csv";
		
		// Live-Graph: data writer object
		try {
			this.ds = DataWriter.createDataWriter(csv_file, true);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		for(IModelNode c : h.getAllChildren()) {
			if(c instanceof IInterface) {
				TreeMap<Integer,datum> dataSet = new TreeMap<Integer,datum>();
				
				dataSet.put(num_in_bytes,new datum(c.getName()+"_"+ModelNodeVariable.int2name(num_in_bytes),"0"));
				dataSet.put(num_out_bytes,new datum(c.getName()+"_"+ModelNodeVariable.int2name(num_out_bytes),"0"));
				dataSet.put(num_in_packets,new datum(c.getName()+"_"+ModelNodeVariable.int2name(num_in_packets),"0"));
				dataSet.put(num_out_packets,new datum(c.getName()+"_"+ModelNodeVariable.int2name(num_out_packets),"0"));
				dataSet.put(packets_in_per_sec,new datum(c.getName()+"_"+ModelNodeVariable.int2name(packets_in_per_sec),"0"));
				dataSet.put(packets_out_per_sec,new datum(c.getName()+"_"+ModelNodeVariable.int2name(packets_out_per_sec),"0"));
				dataSet.put(bytes_in_per_sec,new datum(c.getName()+"_"+ModelNodeVariable.int2name(bytes_in_per_sec),"0"));
				dataSet.put(bytes_out_per_sec,new datum(c.getName()+"_"+ModelNodeVariable.int2name(bytes_out_per_sec),"0"));
				dataSet.put(queue_size,new datum(c.getName()+"_"+ModelNodeVariable.int2name(queue_size),"0"));
				dataSets.put(c.getUID(),dataSet);
			}
		}
		
		TreeMap<Integer,datum> dataSet = new TreeMap<Integer,datum>();
		dataSet.put(traffic_intensity,new datum(ModelNodeVariable.int2name(traffic_intensity),"0"));
		dataSets.put(h.getUID(), dataSet);
		
		// from the dataSets set-up the data series: name 
		ds.addDataSeries("time");
		ds.addDataSeries(h.getParent().getName());
		for(TreeMap<Integer,datum> c: dataSets.values()) {
			for(datum d: c.values()) {
				ds.addDataSeries(d.name);
			}
		}
		
		ds.writeLabelSet();
		if (ds.hadIOException()) {
	        ds.getIOException().printStackTrace();
	        ds.resetIOException();
		}
		
		// from the dataSets set-up the data series: value
		ds.setDataValue("0");
		for(TreeMap<Integer,datum> c: dataSets.values()) {
			for(datum d: c.values()) {
				ds.setDataValue(d.value);
			}
		}
		
		// writeDataSet flushes the cache to the data stream and prepares for
		// processing of the next dataset
		ds.writeDataSet();
		if (ds.hadIOException()) {
	        ds.getIOException().printStackTrace();
	        ds.resetIOException();
		}
	}
	
	/*
	 * start LiveGraph as a separate OS process on the machine and use
	 * command line params to configure the plotter to use settings done before
	 * start up
	 * */
	public void start() {
		
		String shell_path = Utils.TMP_DIR + "/live_graph2.sh";
		String path = getClass().getProtectionDomain().getCodeSource().getLocation().getFile() + "../netscript/dist/lib/";
		String jar = path + "/Live-Graph.jar";
		
		if(!(new File(jar).exists())) {
			path = ConfigurationHandler.getPrimexDirectory();
	    	if (!path.endsWith(File.separator))
	    		path += File.separator;
	    	jar = path + "netscript/lib/Live-Graph.jar";
		}
		
		String script = "";
		script += "#!/bin/bash\n";
		script += "PROGRAM=\"java -jar " + jar + " " + csv_file + "\"\n";
		script += "$PROGRAM &\n";
		script += "PID=$!\n";
		script += "echo $PID\n";
		
		if(!Utils.writeToFile(shell_path, script.getBytes(), null)) {
			System.out.println("Cant start live graph!");
			return;
		}
		
		ProcessBuilder pb2 = new ProcessBuilder("sh", shell_path);
		pb2.directory(new File(Utils.TMP_DIR));
		pb2.redirectErrorStream(true);
		Process p2 = null;
		BufferedReader stdout2 = null;
		
		try {
			p2 =  pb2.start();
			stdout2 = new BufferedReader(new InputStreamReader(p2.getInputStream()));
		} catch (IOException e) {
			System.out.println("Cant start live graph!");
			pid2 = null;
			return;
		}
		
		try { p2.waitFor(); }
		catch(InterruptedException e) { }
		
		try { pid2 = stdout2.readLine(); } 
		catch (IOException e1) { pid2 = null; }
		
		System.out.println("Started live graph with pid: " + pid2);
	}
	
	public void stop() {
		if(pid2 != null) {
			System.out.println("Stopping livegraph! pid: " + pid2);
			
			try { Runtime.getRuntime().exec("kill -9 "+pid2); } 
			catch (IOException e) { e.printStackTrace(); }
			
			pid2 = null;
			final DataStreamWriter t = ds;
			ds = null;
			t.close();
		}
	}
	
	public void handleStateUpdate(final long node_id, final double time, final int attr_id, final String value) {
		if(ds == null) {
			System.out.println("Cant write data to live graph, ds is null!");
			return;
		}
		
		if(dataSets.containsKey(node_id) && dataSets.get(node_id).containsKey(attr_id)) {
			dataSets.get(node_id).get(attr_id).value = value;
			ds.setDataValue(time);
			
			for(TreeMap<Integer,datum> c: dataSets.values()) {
				for(datum d: c.values())
					this.ds.setDataValue(d.value);
			}
			
			ds.writeDataSet();
			if (ds.hadIOException()) {
		        ds.getIOException().printStackTrace();
		        ds.resetIOException();
			}
		}
	}
}

