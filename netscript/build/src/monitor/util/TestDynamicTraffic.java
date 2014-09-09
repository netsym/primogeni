package monitor.util;

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
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import jprime.DynamicTrafficFactory;
import jprime.Experiment;
import jprime.IModelNode;
import jprime.Host.IHost;
import jprime.Net.INet;
import jprime.database.Database;
import jprime.gen.ModelNodeVariable;
import jprime.partitioning.Partitioning;
import jprime.util.ComputeNode;
import jprime.util.DynamicClassManager;
import jprime.util.GlobalProperties;
import jprime.util.IExperimentController;
import jprime.util.ModelInterface;
import jprime.util.PartTlvPair;
import jprime.util.Portal;
import jprime.util.PythonModelInterface;
import jprime.util.XMLModelInterface;
import monitor.commands.StateExchangeCmd;
import monitor.commands.VarUpdate;
import monitor.core.IController;
import monitor.core.IExpListenter;
import monitor.core.LocalController;


/**
 * @author Nathanael Van Vorst
 * 
 */
public class TestDynamicTraffic implements IExpListenter {
	private IController controller=null;
	/**
	 * @param args
	 * @throws IOException 
	 * @throws GeneralSecurityException 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static void main(String[] args) throws IOException {
		if(args.length!=5) {
			System.err.println("Usage: java [properties] monitor.util.TestDynamicTraffic <model name> <model file> <runtime> <src node> <dst node>");
			System.exit(0);
		}
		String model_name = args[0];
		String model_file = args[1];
		int runtime = Integer.parseInt(args[2]);
		String src = args[3];
		String dst = args[4];

		Database db = Database.createDatabase();
		ModelInterface model = null;

		System.out.println("model_name="+model_name+", model_file="+model_file+", runtime="+runtime);

		File file = new File(model_file);
		if(!file.exists()) {
			throw new RuntimeException("the file does not exist!");
		}	
		String fname = file.getName();
		String path = file.getAbsolutePath().replace(fname, "");
		System.out.println("path="+path);
		System.out.println("fname="+fname);
		model=getModel(db, fname, file, path, model_name);
		PartTlvPair parting = model.createTLV(GlobalProperties.PART_STR, false, GlobalProperties.OUT_DIR, new HashMap<Portal,String>(), new ArrayList<ComputeNode>(), model.loadParametersFromSystemProperties());
		new TestDynamicTraffic().run(model.getExperiment(),parting,getAOI(model.getExperiment().getRootNode()),runtime,src,dst);
	}
	
	private void run(Experiment exp, PartTlvPair parting, Collection<IModelNode> aoi, int runtime, String src, String dst) {
		File ack = new File(Utils.NETSIM_DIR);
		if(ack.exists() && ack.isDirectory()) {
			System.out.println("Using "+Utils.NETSIM_DIR+" as the primex dir.");
		}
		else {
			System.out.println("Invalid primex dir '"+Utils.NETSIM_DIR+"'. use -DnetsimDir=<correct dir>.");
		}
		LocalController lc = new LocalController(exp, parting, runtime, 1, (double)1.0, this, 
				Utils.NETSIM_DIR, true, new HashMap<String,String>(), GlobalProperties.OUT_DIR);
		lc.start();
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
		}
		
		IHost src_h = (IHost)exp.getRootNode().get(src);
		IHost dst_h = (IHost)exp.getRootNode().get(dst);
		lc.addToAreaOfInterest(aoi);
		lc.adjustVizExportRate(2000);
		for(int i=0;i<5;i++) {
			System.out.println("Sending dynamic traffic "+i);
			DynamicTrafficFactory df = new DynamicTrafficFactory(exp.getRootNode(), (IExperimentController)lc, parting.partitioning);
			df.createSimulatedTCP(4+10*i, 100000000, src_h, dst_h);
			
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	private static void getAOI(INet r, TreeMap<Long,IModelNode> uids) {
		for(INet sub : r.getSubnets()) {
			getAOI(sub,uids);
		}
		for(IHost h : r.getHosts()) {
			if(h.getName().equals("h1") || h.getName().equals("h2"))
			uids.put(h.getUID(),h);
		}
	}
	
	private static Collection<IModelNode>  getAOI(INet topnet) {
		TreeMap<Long,IModelNode> uids = new TreeMap<Long,IModelNode>();
		getAOI(topnet,uids);
		return uids.values();
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

	public void handleStateUpdate(StateExchangeCmd update) {
		if(update.forViz()) {
			boolean f = false;
			String out="",in="";
			for(VarUpdate e : update.getUpdates()) {
				if(e.var_id == ModelNodeVariable.packets_out_per_sec()){
					f=true;
					out=e.asString();
				}
				else if(e.var_id == ModelNodeVariable.packets_in_per_sec()){
					f=true;
					in=e.asString();
				}
			}
			if(f && (out.equals("0.0")==false || in.equals("0.0")==false)) {
				System.out.println(update.getUid()+" in="+in+", out="+out);
			}
		}
	}

	public void finishedExperiment(boolean failed) {
		System.out.println("finished exp, failed="+failed);
		System.exit(0);
	}

	public void println(String str) {
		System.out.println(str);
	}

	public IController getController() {
		return controller;
	}

	public void setController(IController c) {
		controller=c;
	}

	public void setCompileInfo(Partitioning parting,
			List<ComputeNode> computeNodes) {
		throw new RuntimeException("ACK");
	}

	public void setEmuHosts(HashMap<Integer, ArrayList<IHost>> emuNodes) {
		throw new RuntimeException("ACK");
	}
}
