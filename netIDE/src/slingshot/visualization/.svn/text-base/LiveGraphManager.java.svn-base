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

import org.LiveGraph.dataFile.write.DataStreamWriter;
import org.LiveGraph.dataFile.write.DataStreamWriterFactory;

import slingshot.configuration.ConfigurationHandler;

/**
 * 
 * @author Nathanael Van Vorst
 *
 */
public class LiveGraphManager {
	private final String csv_file;
	private final String data_file;
	private final String graph_file;
	private final boolean can_run;
	private String pid=null;
	private static class datum {
		public final String name;
		public String value;
		public datum(String n, String v) {
			this.name=n;
			this.value=v;
		}
	}
	private DataStreamWriter ds;
	private final TreeMap<Long,TreeMap<Integer,datum>> prev=new TreeMap<Long,TreeMap<Integer,datum>>();
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

	
	
	public LiveGraphManager(IHost h) {
		super();
		csv_file=Utils.TMP_DIR+"/host_"+h.getUID()+".csv";
		data_file=Utils.TMP_DIR+"/host_"+h.getUID()+".lgdfs";
		graph_file=Utils.TMP_DIR+"/host_"+h.getUID()+".lggs";
		this.ds = DataStreamWriterFactory.createDataWriter(csv_file, true);
		String t = "";
		t+="<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n";
		t+="<!DOCTYPE properties SYSTEM \"http://java.sun.com/dtd/properties.dtd\">\n";
		t+="<properties>\n";
		t+="<comment>Datafile Settings for "+h.getUniqueName()+"</comment>\n";
		t+="<entry key=\"ShowOnlyTailData\">0</entry>\n";
		t+="<entry key=\"DataFile\">"+csv_file+"</entry>\n";
		t+="<entry key=\"UpdateFrequency\">20</entry>\n";
		t+="<entry key=\"DoNotCacheData\">0</entry>\n";
		t+="</properties>\n";
		if(!Utils.writeToFile(data_file, t.getBytes(), null)) {
			System.out.println("Cant start live graph!");
			can_run=false;
			return;
		}
		t = "";
		t+="<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n";
		t+="<!DOCTYPE properties SYSTEM \"http://java.sun.com/dtd/properties.dtd\">\n";
		t+="<properties>\n";
		t+="<comment>Graph Settings for "+h.getUniqueName()+"</comment>\n";
		t+="<entry key=\"HGridSize\">50</entry>\n";
		t+="<entry key=\"VGridSize\">50</entry>\n";
		t+="<entry key=\"HGridColour\">c0c0c0</entry>\n";
		t+="<entry key=\"XAxisParamValue\">1</entry>\n";
		t+="<entry key=\"MaxY\">Auto</entry>\n";
		t+="<entry key=\"MaxX\">Auto</entry>\n";
		t+="<entry key=\"XAxisSeriesIndex\">0</entry>\n";
		t+="<entry key=\"MinY\">Auto</entry>\n";
		t+="<entry key=\"VGridColour\">c0c0c0</entry>\n";
		t+="<entry key=\"MinX\">Auto</entry>\n";
		t+="<entry key=\"XAxisType\">XAxis_DSNum</entry>\n";
		t+="<entry key=\"VGridType\">VGrid_None</entry>\n";
		t+="<entry key=\"HGridType\">HGrid_None</entry>\n";
		t+="<entry key=\"HighlightDataPoints\">1</entry>\n";
		t+="</properties>\n";
		if(!Utils.writeToFile(graph_file, t.getBytes(), null)) {
			System.out.println("Cant start live graph!");
			can_run=false;
			return;
		}
		else {
			can_run=true;
		}
		this.ds.setSeparator(";");
		this.ds.writeFileInfo("Data for "+h.getUniqueName());
		for(IModelNode c : h.getAllChildren()) {
			if(c instanceof IInterface) {
				TreeMap<Integer,datum> ct = new TreeMap<Integer,datum>();
				ct.put(num_in_bytes,new datum(c.getName()+"_"+ModelNodeVariable.int2name(num_in_bytes),"0"));
				ct.put(num_out_bytes,new datum(c.getName()+"_"+ModelNodeVariable.int2name(num_out_bytes),"0"));
				ct.put(num_in_packets,new datum(c.getName()+"_"+ModelNodeVariable.int2name(num_in_packets),"0"));
				ct.put(num_out_packets,new datum(c.getName()+"_"+ModelNodeVariable.int2name(num_out_packets),"0"));
				ct.put(packets_in_per_sec,new datum(c.getName()+"_"+ModelNodeVariable.int2name(packets_in_per_sec),"0"));
				ct.put(packets_out_per_sec,new datum(c.getName()+"_"+ModelNodeVariable.int2name(packets_out_per_sec),"0"));
				ct.put(bytes_in_per_sec,new datum(c.getName()+"_"+ModelNodeVariable.int2name(bytes_in_per_sec),"0"));
				ct.put(bytes_out_per_sec,new datum(c.getName()+"_"+ModelNodeVariable.int2name(bytes_out_per_sec),"0"));
				ct.put(queue_size,new datum(c.getName()+"_"+ModelNodeVariable.int2name(queue_size),"0"));
				prev.put(c.getUID(),ct);
			}
		}
		TreeMap<Integer,datum> ct = new TreeMap<Integer,datum>();
		ct.put(traffic_intensity,new datum(ModelNodeVariable.int2name(traffic_intensity),"0"));
		prev.put(h.getUID(), ct);
		ds.addDataSeries("time");
		for(TreeMap<Integer,datum> c: prev.values()) {
			for(datum d: c.values()) {
				ds.addDataSeries(d.name);
			}
		}
		ds.setDataValue("0");
		for(TreeMap<Integer,datum> c: prev.values()) {
			for(datum d: c.values()) {
				ds.setDataValue(d.value);
			}
		}
		ds.writeDataSet();
		if (ds.hadIOException()) {
	        ds.getIOException().printStackTrace();
	        ds.resetIOException();
		}
	}
	
	public void start() {
		if(!can_run) return; 
		String f=Utils.TMP_DIR+"/live_graph.sh";
		String path =getClass().getProtectionDomain().getCodeSource().getLocation().getFile()	+"../netscript/dist/lib/";
		String j1 = path+"/LiveGraph.2.0.beta01.Complete.jar";
		
		if(! (new File(j1).exists())) {
			path = ConfigurationHandler.getPrimexDirectory();
	    	if (!path.endsWith(File.separator))
	    		path += File.separator;
	    	j1=path+"netscript/lib/LiveGraph.2.0.beta01.Complete.jar";
		}
		//String j2 = path+"/SoftNetConsultUtils.2.01.slim.jar";
		//String exec="java -jar "+j1+" -dfs "+data_file+" -gs "+graph_file;		
		String exec="";
		exec+="#!/bin/bash\n";
		exec+="PROGRAM=\"java -jar "+j1+" -dfs "+data_file+" -gs "+graph_file+"\"\n";
		exec+="$PROGRAM &\n";
		exec+="PID=$!\n";
		exec+="echo $PID\n";
		if(!Utils.writeToFile(f, exec.getBytes(), null)) {
			System.out.println("Cant start live graph!");
			return;
		}
		ProcessBuilder pb = new ProcessBuilder("sh", f);
		pb.directory(new File(Utils.TMP_DIR));
		pb.redirectErrorStream(true);
		Process p=null;
		BufferedReader stdout = null;
		try {
			p =  pb.start();
			stdout = new BufferedReader(new InputStreamReader(p.getInputStream()));
		} catch (IOException e) {
			System.out.println("Cant start live graph!");
			pid=null;
			return;
		}
		try {
			p.waitFor();  // wait for process to complete
		}
		catch(InterruptedException e) {
		}
		try {
			pid = stdout.readLine();
		} catch (IOException e1) {
			pid=null;
		}
		System.out.println("Started live graph with pid:"+pid);
	}
	
	public void stop() {
		if(pid != null) {
			System.out.println("Stopping livegraph! pid="+pid);
			try {
				Runtime.getRuntime().exec("kill -9 "+pid);
			} catch (IOException e) {
				e.printStackTrace();
			}
			pid=null;
			final DataStreamWriter t = ds;
			ds=null;
			t.close();
		}
	}
	
	public void handleStateUpdate(final long node_id, final double time, final int attr_id, final String value) {
		if(ds==null) {
			System.out.println("cant write data to live graph, ds is null!");
			return;
		}
		if(prev.containsKey(node_id) && prev.get(node_id).containsKey(attr_id)) {
			prev.get(node_id).get(attr_id).value=value;
			ds.setDataValue(time);
			for(TreeMap<Integer,datum> c: prev.values()) {
				for(datum d: c.values()) {
					this.ds.setDataValue(d.value);
				}
			}
			ds.writeDataSet();
			if (ds.hadIOException()) {
		        ds.getIOException().printStackTrace();
		        ds.resetIOException();
			}
		}
	}
}
