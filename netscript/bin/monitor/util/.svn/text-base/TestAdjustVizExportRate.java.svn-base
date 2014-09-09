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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

import jprime.Experiment;
import jprime.Host.IHost;
import jprime.Net.INet;
import jprime.database.Database;
import jprime.partitioning.Partitioning;
import jprime.util.ComputeNode;
import jprime.util.DynamicClassManager;
import jprime.util.GlobalProperties;
import jprime.util.ModelInterface;
import jprime.util.NetAggPair;
import jprime.util.PartTlvPair;
import jprime.util.Portal;
import jprime.util.PythonModelInterface;
import jprime.util.XMLModelInterface;
import monitor.commands.SetupExperimentCmd;
import monitor.commands.StateExchangeCmd;
import monitor.core.IController;
import monitor.core.IExpListenter;
import monitor.core.RemoteController;


/**
 * @author Nathanael Van Vorst
 * 
 */
public class TestAdjustVizExportRate {
	public static final boolean CONTROL=Boolean.parseBoolean(System.getProperty("CONTROL","true"));
	public static final int MAX_OBJ_RATE=Integer.parseInt(System.getProperty("MAX_OBJ_RATE","50000"));
	

	public static long calcVEI(long aoi_size) {
		long rv = (long)((1000.0*aoi_size)/MAX_OBJ_RATE);
		return Math.max(50,rv);
		//return Math.max(Utils.VEI,rv);
	}
	
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
			System.err.println("Usage: java [properties] monitor.util.TestAdjustVizExportRate <model name> <model file> <time> <master> <slave>");
			System.exit(0);
		}
		String model_name = args[0];
		String model_file = args[1];
		int runtime = Integer.parseInt(args[2]);
		ComputeNode master = new ComputeNode(args[3], args[3], new ArrayList<Portal>());
		master.setPartitionId(null);
		ArrayList<ComputeNode> slaves = new ArrayList<ComputeNode>();
		slaves.add(new ComputeNode(args[4], args[4], new ArrayList<Portal>()));


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
		PartTlvPair parting = model.createTLV(GlobalProperties.PART_STR, false, GlobalProperties.OUT_DIR, new HashMap<Portal,String>(), slaves, model.loadParametersFromSystemProperties());
		System.out.println("Running, master="+master.getControl_ip()+", slave="+slaves.get(0).getControl_ip());
		run(parting,getAOI(model.getExperiment().getRootNode()),master,slaves,runtime);
	}
	
	private static final class TPListener extends Thread implements IExpListenter {
		private final long[] large_aoi,delta;
		private RemoteController rc;
		private final int runtime;
		private BufferedWriter out=null;
		private Long real_start=null,sim_start=null;
		private long objs=0, states=0;

		public TPListener(long[] large_aoi, long[] delta, int runtime) {
			super();
			this.large_aoi = large_aoi;
			this.delta = delta;
			this.runtime=runtime;
			try {
				this.out = new BufferedWriter(new FileWriter(new File(GlobalProperties.OUT_DIR+"/controller.csv")));
				this.out.write("#real_time_start, real_time_end, sim_time_start, sim_time_end, obj_count, state_count\n");
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		
		public void setRC(RemoteController rc) {
			this.rc=rc;
		}
		
		public synchronized void close() {
			try {
				if(null != out) {
					out.close();
					out=null;
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(100);
			}
		}
		private synchronized void write(long sim_time, int state_count) {
			if(sim_start==null) {
				//first
				sim_start=sim_time;
				real_start=System.currentTimeMillis();
				objs=1;
				states=state_count;
			}
			else if(sim_start < sim_time) {
				//start new batch....
				//#real_time_start, real_time_end, sim_time_start, sim_time_end, obj_count, state_count\n
				objs++;
				states+=state_count;
				try {
					out.write(real_start+","+System.currentTimeMillis()+","+sim_start+","+sim_time+","+objs+","+states+"\n");
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				sim_start=sim_time;
				real_start=System.currentTimeMillis();
				objs=1;
				states=state_count;
			}
			else {
				objs++;
				states+=state_count;
			}
		}
		public void handleStateUpdate(StateExchangeCmd update) {
			write(update.getTime(),update.getUpdates().size());
		}
		public void finishedExperiment(boolean failed) {
			close();
			Runtime.getRuntime().exit(0);
		}
		public void println(String str) {
			System.out.println(str);
		}
		public IController getController() {
			return null;
		}
		public void setController(IController c) {
		}
		public void setCompileInfo(Partitioning parting,
				List<ComputeNode> computeNodes) {
		}
		public void setEmuHosts(HashMap<Integer, ArrayList<IHost>> emuNodes) {
			if(emuNodes != null && emuNodes.size()>0)
				throw new RuntimeException("wtf?");
		}
		public void run () {
			long cur_aoi = large_aoi.length;
			rc.addToAreaOfInterest(large_aoi);
			rc.startExperiment(runtime);
			double base=System.currentTimeMillis();
			if(CONTROL) {
				long new_vei=calcVEI(cur_aoi);
				rc.adjustVizExportRate(new_vei);
				System.out.println("started exp, aoi_size="+cur_aoi+", vei="+new_vei);
			}
			boolean down=true;
			for(int i=0;i<runtime+5;i++)
			try {
				if(i % 5 == 0) {
					System.out.println("["+0+","+(runtime+5)+"]:"+((System.currentTimeMillis()-base)/1000.0));
					if(down) {
						cur_aoi-=delta.length;
						if(CONTROL) {
							long new_vei=calcVEI(cur_aoi);
							rc.adjustVizExportRate(new_vei);
							System.out.println("\tChanging AIO, new size="+cur_aoi+", vei="+new_vei);
						}
						else {
							System.out.println("\tChanging AIO, new size="+cur_aoi);
						}
						rc.removeFromAreaOfInterest(delta);
						down=false;
					}
					else {
						cur_aoi+=delta.length;
						if(CONTROL) {
							long new_vei=calcVEI(cur_aoi);
							rc.adjustVizExportRate(new_vei);
							System.out.println("\tChanging AIO, new size="+cur_aoi+", vei="+new_vei);
						}
						else {
							System.out.println("\tChanging AIO, new size="+cur_aoi);
						}
						rc.addToAreaOfInterest(delta);
						down=true;
					}
				}
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			try {
				close();
				rc.shutdown(true, false);
			} catch (Exception e) {
			}
			Runtime.getRuntime().exit(0);
		}
		
	}
	
	private static void run(PartTlvPair parting, long[] aoi,ComputeNode master,ArrayList<ComputeNode> slaves, int runtime) {
		long[] delta = new long[(int)(0.75*aoi.length)];
		TreeSet<Long> delta_c = new TreeSet<Long>();
		Random rand = new Random();
		while(delta_c.size() < delta.length ) {
			delta_c.add(aoi[rand.nextInt(aoi.length)]);
		}
		int i =0;
		for(long l : delta_c) {
			delta[i]=l;
			i++;
		}
		
		TPListener l = new TPListener(aoi,delta,runtime);
		System.out.println("master.partid="+master.getPartitionId()+",slave.partid="+slaves.get(0).getPartitionId());
		final RemoteController rc = new RemoteController(
				parting.partitioning,
				new HashMap<Integer, ArrayList<IHost>>(),
				master.getControl_ip(),
				500,
				parting.expName,
				l);
		l.setRC(rc);
		try {
			rc.connect(master, slaves, null);
		} catch (GeneralSecurityException e1) {
			throw new RuntimeException(e1);
		} catch (IOException e1) {
			throw new RuntimeException(e1);
		}
		rc.sendCommand(new SetupExperimentCmd(parting.expName,new HashMap<String,String>(),parting.tlvs, NetAggPair.getAggMap(parting.partitioning)));
		while(rc.getNumOutstandingCommands()>0 && rc.getFailures()==0) {
			//wait for all commands to finish...
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}
		System.out.println("Done setting up exp");
		l.start();
	}
	
	private static void getAOI(INet r, TreeSet<Long> uids) {
		for(INet sub : r.getSubnets()) {
			getAOI(sub,uids);
		}
		for(IHost h : r.getHosts()) {
			uids.add(h.getUID());
		}
	}
	
	private static long[] getAOI(INet topnet) {
		TreeSet<Long> uids = new TreeSet<Long>();
		getAOI(topnet,uids);
		long[] rv = new long[(int)(uids.size())];
		int i=0;
		for(long l : uids) {
			if(i >= rv.length) break;
			rv[i]=l;
			i++;
		}
		return rv;
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
