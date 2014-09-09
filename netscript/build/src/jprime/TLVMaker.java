package jprime;

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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import jprime.database.Database;
import jprime.util.ComputeNode;
import jprime.util.DynamicClassManager;
import jprime.util.GlobalProperties;
import jprime.util.ModelInterface;
import jprime.util.Portal;
import jprime.util.PythonModelInterface;
import jprime.util.XMLModelInterface;


/**
 * @author Nathanael Van Vorst
 * 
 */
public class TLVMaker {
	private static enum Action {
		create("create"),
		load("load"),
		delete("delete"),
		deploy("deploy"),
		;
		final String str;
		Action(String str) {
			this.str=str;
		}
		public static Action fromString(String s) {
			for(Action a: values())
				if(a.str.compareToIgnoreCase(s)==0)
					return a;
			throw new RuntimeException("invalid action:"+s);
		}
	}
	
	/**
	 * 
	 */
	private static void usage() {
		final String prefix="     ";
		System.err.println("Usage: java [properties] jprime.TLVMaker <action> <model name> <model file>");
		System.err.println("  Properties:");
		GlobalProperties.info(prefix, System.err);
		System.err.println(prefix+"RUNTIME_ENV_FILE   ; a file containing the runtime environment specification, defaults to 'null'.");
		System.err.println(prefix+"RUNTIME_ENV        ; a string which defines a master and slave compute nodes, defaults to 'null'.");
		System.err.println(prefix+"   The following regex defines the compute node string: (\\[control_ip,data_ip(,portal_name,portal_ip)*\\])+");
		System.err.println(prefix+"     1.Example of a master with two slaves, each with no traffic portals:");
		System.err.println(prefix+"        [c_master,d_master],[c_slave1,d_slave1],[c_slave2,d_slave2");
		System.err.println(prefix+"     2.Example of a master with two slaves, each with a traffic portal:");
		System.err.println(prefix+"        [c_master,d_master],[c_slave1,d_slave1,eth2,10.10.2.2],[c_slave2,d_slave2,eth3,10.10.2.3]");
		System.err.println(prefix+"     3.Example of a master with two slaves, one slave with two traffic portals:");
		System.err.println(prefix+"        [c_master,d_master],[c_slave1,d_slave1,eth2,10.10.2.2,eth3,10.10.2.3],[c_slave2,d_slave2]");
		System.err.println(prefix+"PORTAL_LINKS       ; a string which which portals are connected to which portals, defaults to 'null'.");
		System.err.println(prefix+"   The following regex defines the compute node string: (control_ip:portal_name,interface_name)+");
		System.err.println(prefix+"     Example portal String associated with Ex.2 above:");
		System.err.println(prefix+"        c_slave1:eth2,topnet.host1.if0,c_slave2:eth3,topnet.sub1.h1.if2");
		System.err.println(prefix+"     Example portal String associated with Ex.3 above:");
		System.err.println(prefix+"        c_slave1:eth2,topnet.host1.if0,c_slave1:eth3,topnet.sub1.h1.if2");
		System.err.println("  Actions:");
		System.err.println(prefix+"create             ; creates a new experiment in the database\n"+
					       prefix+"                     and produces tlvs for model exection.");
		System.err.println(prefix+"load               ; loads a previous experiment from the database\n"+
				          prefix+"                      and produces tlvs for model exection.");
		System.err.println(prefix+"delete             ; removes an experiment from the db.");
		//System.err.println("     deploy  ; creates a new exp, a tlv, and deploys to primo geni using the number of machines specified in the part_str.");
		System.err.println("  The <model file> must end in .xml, .class, .java, .xml, or .py");
		System.exit(0);
	}
	
	public static Set<ComputeNode> getComputeNodes() {
		String nodeStr=null;
		String env = System.getProperty("RUNTIME_ENV_FILE", null);
		if(env!= null) {
			Properties settings = new Properties();
			//Load the data from our config file
			File configFile = new File(env);
			FileReader in;
			try {
				in = new FileReader(configFile);
				settings.load(in);
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			nodeStr = settings.getProperty("nodes", "");
		}
		else {
			nodeStr = System.getProperty("RUNTIME_ENV", null);
		}
		if(nodeStr != null) {
			nodeStr = nodeStr.replaceAll("\\s", "");
			if(nodeStr.length()>0) {
				Set<ComputeNode> rv = new HashSet<ComputeNode>();
				rv.addAll(ComputeNode.fromString(nodeStr));
				if(rv.size()>0) {
					return rv;
				}
			}
		}
		return null;
	}
	
	public static HashMap<Portal,String> getPortals(Set<ComputeNode> computeNodes) {
		HashMap<Portal,String> linked=new HashMap<Portal,String>();
		if(null != computeNodes) {
			HashMap<String,ComputeNode> nodemap=new HashMap<String,ComputeNode>();
			for(ComputeNode cn : computeNodes) {
				nodemap.put(cn.getControl_ip(),cn);
			}
			String pl = System.getProperty("PORTAL_LINKS",null);
			if(pl != null && pl.length()>0){
				pl = pl.replaceAll("\\s", "");
				String[] t = pl.split(",");
				for(int i=0;i<t.length-1;) {
					String[] tt = t[i].split(":");
					if(tt.length != 2) {
						System.err.println("Invalid portal id found in portal link string: "+t[i]);
						System.exit(1);
					}
					ComputeNode cn = nodemap.get(tt[0]);
					if(cn == null) {
						System.err.println("Can find compute node with control ip "+tt[0]);
						System.exit(1);
					}
					Portal p = null;
					for(Portal pp : cn.getPortals()) {
						if(pp.getName().compareTo(tt[1])==0) {
							p=pp;
							break;
						}
					}
					if(p == null) {
						System.err.println("Can find portal "+tt[1]+" in the compute node with control ip "+tt[0]);
						System.exit(1);
					}
					linked.put(p, t[i+1]);
					i+=2;
				}
			}
		}
		return linked;
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
		/* $if DEBUG $
           	System.out.println("The DEBUG pre-processor macro is set!");
           $endif$ */
		
		//try {
			if(args.length<2) {
				usage();
			}
			
			Action action = Action.fromString(args[0]);
			String name = args[1];
			
			Database db = Database.createDatabase();
			ModelInterface model = null;
			
			System.out.println("model name="+name+", action="+action);
			
			
			long start = System.currentTimeMillis();
			
			switch(action) {
			case create:
			{
				if(args.length!=3) {
					usage();
				}
				File file = new File(args[2]);
				if(!file.exists()) {
					throw new RuntimeException("the file does not exist!");
				}
				String fname = file.getName();
				String path = file.getAbsolutePath().replace(fname, "");
				System.out.println("path="+path);
				System.out.println("fname="+fname);
				
				model=getModel(db, fname, file, path, name);  //err3

				boolean instageni_xen=true;
				if (instageni_xen==true)
				{
					
					
					
				}
				
				//ERROR I got  Exception in thread "main" java.lang.NullPointerException
				//at jprime.util.DynamicClassManager.compile(DynamicClassManager.java:136)
				//at jprime.util.DynamicClassManager.loadModel(DynamicClassManager.java:109)
				///at jprime.TLVMaker.getModel(TLVMaker.java:313)
				//at jprime.TLVMaker.main(TLVMaker.java:229)

				
				
				model.getExperiment().save();
				
				Set<ComputeNode> cn = getComputeNodes();
				List<ComputeNode> cnl = new ArrayList<ComputeNode>();
				if(cn != null)
					cnl.addAll(cn);
				HashMap<Portal,String> linked = getPortals(cn);
				
				model.createTLV(GlobalProperties.PART_STR, GlobalProperties.CREATE_XML, GlobalProperties.OUT_DIR, linked, cnl,model.loadParametersFromSystemProperties());
				//new EmulationCommandVisitor(t.geExperiment().getRootNode(), 100);
				db.save(model.getExperiment(),null);
			}
				break;
			case delete:
			{
				Experiment exp = db.loadExperiment(name);
				db.remove(exp);
			}
				break;
			case deploy:
				/*
					ModelInterface t = model.createModel(db);
					PartTlvPair parting = t.createTLV(part_str, createXml, createGraphviz, outdir);
					Provisioner p=null;
					if(Boolean.parseBoolean(System.getProperty("USE_PRIMO", "false"))) {
						p = new ProtoGeni(Utils.EXPERIMENT_NAME, Utils.PASSPHRASE, 
							Utils.SLINGSHOT_PRIMOGENI_FOLDER + "/encrypted.pem", parting.partitioning.getProcessingNodes().size() + 1);
					}
					else {
						ArrayList<String> temp = new ArrayList<String>();
						temp.add("131.94.135.227");//vm1;
						temp.add("131.94.135.232");//vm2;
						temp.add("131.94.135.233");//vm3;
						p = new CannedProvisioner(temp);
					}
					Controller c = Controller.deploy(parting, p);
					System.out.println("STarting exp");
					c.startExperiment(100);
					System.out.println("STarted exp c.getEmulatedHost2machineMap().size="+c.getEmulatedHost2machineMap().size());
					//XXX
					for(Entry<Integer, ArrayList<IHost>> e : c.getEmulatedHost2machineMap().entrySet()) {
						System.out.println("Sending cmds to hosts on machine  "+e.getKey());
						for(IHost h : e.getValue()) {
							System.out.println("\tSending cmd to host "+h.getUniqueName()+", uid="+h.getUID());
							c.sendHostCommand("echo 'im echoing in container "+h.getUID()+". Woot.' > /tmp/it_works", h.getUID(), e.getKey());
						}
					}
					*/
				throw new RuntimeException("Deploy is not currently supported.");
				//break;
			case load:
			{
				Set<ComputeNode> cn = getComputeNodes();
				List<ComputeNode> cnl = new ArrayList<ComputeNode>();
				if(cn != null)
					cnl.addAll(cn);
				HashMap<Portal,String> linked = getPortals(cn);

				Experiment exp = db.loadExperiment(name);
				if(exp == null) {
					throw new RuntimeException("Unable to load experiment "+name);
				}
				ModelInterface.createOutput(exp, GlobalProperties.PART_STR, GlobalProperties.CREATE_XML, GlobalProperties.OUT_DIR, linked, cnl);
			}
				break;
			default:
				throw new RuntimeException("Unknown action "+action);
			}
			
			double r = ((double)(System.currentTimeMillis()-start))/1000.0;
			System.out.println("\t***************************************");
			System.out.println("\tRuntime:"+r+" seconds.");
			System.out.println("\t***************************************");
		/*} catch(Exception e) {
			jprime.Console.err.printStackTrace(e);
			jprime.Console.halt(100);
		}*/
	}
	
	private static ModelInterface getModel(Database db, String fname, File file, String path, String name) throws IOException {
		Experiment exp = db.createExperiment(name, true);
		if(fname.endsWith(".java") || fname.endsWith(".class")) {
			
			
			
			DynamicClassManager dm;//=null;
			dm = new DynamicClassManager(path);
			
//			try {
//				dm = new DynamicClassManager(path);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				System.out.println("     jprime/TlvMaker.java: (Exception caught) It could be: java.lang.NullPointerException \n       dm = new DynamicClassManager(path) ***FAILED*** path is="+path);
//			}
			
			//ERROR I got  Exception in thread "main" java.lang.NullPointerException
			//at jprime.util.DynamicClassManager.compile(DynamicClassManager.java:136)
			//at jprime.util.DynamicClassManager.loadModel(DynamicClassManager.java:109)
			///at jprime.TLVMaker.getModel(TLVMaker.java:313)
			//at jprime.TLVMaker.main(TLVMaker.java:229)

//			if(dm==null)
//				System.out.println("jprime/TLVMaker.java: dm=null, but it shouldnt be.");
//			System.out.println("dm has meaningful parameters.path="+path+", dm="+dm);
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
