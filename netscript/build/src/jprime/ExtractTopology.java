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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeSet;

import jprime.BaseInterface.IBaseInterfaceAlias;
import jprime.Host.IHost;
import jprime.Interface.IInterface;
import jprime.Link.ILink;
import jprime.Net.INet;
import jprime.Router.IRouter;
import jprime.database.Database;
import jprime.gen.ModelNodeVariable.ClsDesc;
import jprime.gen.ModelNodeVariable.VarDesc;
import jprime.util.DynamicClassManager;
import jprime.util.GlobalProperties;
import jprime.util.ModelInterface;
import jprime.util.PythonModelInterface;
import jprime.util.XMLModelInterface;
import jprime.variable.FloatingPointNumberVariable;
import jprime.variable.ModelNodeVariable;


/**
 * @author Nathanael Van Vorst
 * 
 */
public class ExtractTopology {
	private static final boolean tulip = Boolean.parseBoolean(System.getProperty("TULIP", "true"));
	private static enum Action {
		create("create"),
		load("load"),
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
		System.err.println("Usage: java [properties] jprime.ExtractTopology create <model name> <model file> <topology file>");
		System.err.println("Usage: java [properties] jprime.ExtractTopology load <model name> <topology file>");
		System.err.println("  Properties:");
		GlobalProperties.info(prefix, System.err);
		System.err.println(prefix+"TULIP   ; if set to true this will export a tulip topology, defaults to 'false'");
		System.err.println("  -- The <model file> must end in .xml, .class, .java, .xml, or .py");
		System.err.println("  -- The <topolofy file> will contain the topology.");
		System.exit(0);
	}
	
	private static class net {
		long hosts, nets, dbid, pid;
		Long totalhosts, totalNets;
		public net(INet n) {
			hosts=n.getHosts().size();
			nets=n.getSubnets().size();
			dbid=n.getDBID();
			pid=n.getParentId();
			totalhosts=null;
		}
		public long getTotalHosts() {
			if(totalhosts == null) {
				net n = networks.get(pid);
				if(n == null) {
					totalhosts=hosts;
				}
				else {
					totalhosts=hosts+n.getTotalHosts();
				}
			}
			return totalhosts;
		}
		public long getTotalNets() {
			if(totalNets == null) {
				net n = networks.get(pid);
				if(n == null) {
					totalNets=nets;
				}
				else {
					totalNets=nets+n.getTotalNets();
				}
			}
			return totalNets;
		}
	}
	private static final long counts[]={0,0,0,0};
	private static final HashMap<Long,net> networks = new HashMap<Long, ExtractTopology.net>();
	private static final int pad_len=64;
	
	private static double getBandwidth(ILink l) {
		FloatingPointNumberVariable f = l.getBandwidth();
		if(f != null)
			return f.getValue();
		ClsDesc cls = ModelNodeVariable.getClsDesc((ModelNode)l);
		VarDesc v = cls.vars.get(ModelNodeVariable.bandwidth());
		return Double.parseDouble(v.default_value);
	}


	private static double getDelay(ILink l) {
		FloatingPointNumberVariable f = l.getDelay();
		if(f != null)
			return f.getValue();
		ClsDesc cls = ModelNodeVariable.getClsDesc((ModelNode)l);
		VarDesc v = cls.vars.get(ModelNodeVariable.delay());
		return Double.parseDouble(v.default_value);
	}

	private static double getBandwidth(IInterface i) {
		FloatingPointNumberVariable f = i.getBitRate();
		if(f != null)
			return f.getValue();
		ClsDesc cls = ModelNodeVariable.getClsDesc((ModelNode)i);
		VarDesc v = cls.vars.get(ModelNodeVariable.bit_rate());
		return Double.parseDouble(v.default_value);
	}

	private static void writeTopo(IModelNode node, OutputStreamWriter out) throws IOException {
		if(node instanceof INet) {
			counts[0]++;
			networks.put(node.getDBID(), new net((INet)node));
			for(IModelNode c : node.getAllChildren())
				writeTopo(c, out);
		}
		else if(node instanceof IRouter) {
			counts[1]++;
			out.write("router "+node.getDBID()+" "+node.getParentId()+" "+((IRouter)node).getNics().size()+"\n");
		}
		else if(node instanceof IHost) {
			counts[2]++;
			out.write("host "+node.getDBID()+" "+node.getParentId()+" "+" "+((IHost)node).getNics().size()+"\n");
		}
		else if(node instanceof ILink) {
			counts[3]++;
			out.write("link "+node.getDBID());
			String a ="";
			
			double b = getBandwidth(((ILink)node));
			
			for(IBaseInterfaceAlias nic : ((ILink)node).getAttachments()) {
				IInterface c = (IInterface)nic.deference();
				a+=" "+c.getParentId();
				double bb =getBandwidth(c);
				if(bb < b) b=bb;
			}
			out.write(" "+b+" "+getDelay((ILink)node)+a+"\n");
		}
	}
	
	private static void finishTopo(OutputStreamWriter out, String fname) throws IOException {
		for(net n : networks.values()) {
			out.write("net "+n.dbid+" "+n.pid+" "+n.hosts+" "+n.getTotalHosts()+" "+n.nets+" "+n.getTotalNets()+"\n");
		}
		out.close();
		String c = "counts";
		for(int i=0;i<4;i++)
			c+=" "+counts[i];
		if(pad_len < c.getBytes().length)
			throw new RuntimeException("unable to write counts to head of file. Allocated "+pad_len+" bytes but the counts take "+c.getBytes().length+" bytes.");
		RandomAccessFile f = new RandomAccessFile(new File(fname), "rw");
		f.seek(0);
		f.write(c.getBytes());
		f.close();
	}

	private static class tulip_node implements Comparable<tulip_node>{
		private static long _id=0;
		public final long id,dbid;
		public final int type; 
		public tulip_node(IRouter h) {
			this.id=_id;
			_id++;
			this.dbid=h.getDBID();
			this.type=1;
		}
		public tulip_node(IHost h) {
			this.id=_id;
			_id++;
			this.dbid=h.getDBID();
			this.type=0;
		}
		public tulip_node(ILink l) {
			this.id=_id;
			_id++;
			this.dbid=l.getDBID();
			this.type=2;
		}
		public int compareTo(tulip_node o) {
			return (int)(id-o.id);
		}
	}
	
	private static class tulip_hub extends tulip_node{
		public final LinkedList<tulip_link> links;
		public tulip_hub(ILink l) {
			super(l);
			links = new LinkedList<ExtractTopology.tulip_link>();
		}
	}

	private static class tulip_link implements Comparable<tulip_link> {
		private static long _id=0;
		public final long id,dbid,src,dst;
		public final double bandwidth, delay;
		public tulip_link(long dbid, long src, long dst, double bandwidth, double delay) {
			this.dbid=dbid;
			this.id=_id;
			_id++;
			this.src=src;
			this.dst=dst;
			this.delay=delay;
			this.bandwidth=bandwidth;
		}
		public int compareTo(tulip_link o) {
			return (int)(id - o.id);
		}
	}
	
	private static final HashMap<Long,tulip_node> node_id_map = new HashMap<Long, tulip_node>();
	private static final HashMap<Long,tulip_link> edge_id_map = new HashMap<Long, tulip_link>();
	private static final TreeSet<tulip_node> nodes = new TreeSet<ExtractTopology.tulip_node>();
	private static final TreeSet<tulip_link> edges = new TreeSet<ExtractTopology.tulip_link>();
	
	private static void writeTulipTopo(INet topnet, OutputStreamWriter out) throws IOException {
		LinkedList<INet> nets = new LinkedList<INet>();
		nets.add(topnet);
		while(nets.size()>0) {
			final INet n=nets.remove();
			for(IModelNode c : n.getAllChildren()) {
				if(c instanceof INet) {
					nets.add((INet)c);
				}
				else if(c instanceof IRouter) {
					tulip_node tn = new tulip_node((IRouter)c);
					node_id_map.put(tn.dbid, tn);
					nodes.add(tn);
				}
				else if(c instanceof IHost) {
					tulip_node tn = new tulip_node((IHost)c);
					node_id_map.put(tn.dbid, tn);
					nodes.add(tn);
				}
				else if(c instanceof ILink) {
					ILink l = (ILink)c;
					final double delay = getDelay(l);
					long h[] = new long[l.getAttachments().size()];
					int i =0;
					double bandwidth = getBandwidth(l);
					for(IBaseInterfaceAlias nic : l.getAttachments()) {
						IInterface cc = (IInterface)nic.deference();
						final double b =getBandwidth(cc);
						if(b < bandwidth) bandwidth=b;
						h[i++]=cc.getParent().getDBID();
					}

					if(l.getAttachments().size()==2) {
						tulip_link tl = new tulip_link(l.getDBID(), h[0], h[1],bandwidth,delay);
						edge_id_map.put(tl.dbid,tl);
						edges.add(tl);
					}
					else {
						//add a hub
						tulip_hub tn = new tulip_hub((ILink)c);
						node_id_map.put(tn.dbid, tn);
						nodes.add(tn);
						
						for(long src : h) {
							tulip_link tl = new tulip_link(l.getDBID(), src, tn.dbid, bandwidth,delay);
							edges.add(tl);
							tn.links.add(tl);
						}
					}
				}
			}
		}
		out.write("(comments \"This file was generated by jPRIME.\")\n");
		out.write("(nb_nodes "+tulip_node._id+")\n");
		out.write(";(nodes <node_id> <node_id> ...)\n");
		out.write("(nodes 0.."+(tulip_node._id-1)+" )\n");
		out.write("(nb_edges "+edges.size()+")\n");
		out.write(";(edge <edge_id> <source_id> <target_id>)\n");

		for(tulip_link e : edges) {
			final tulip_node src = node_id_map.get(e.src);
			final tulip_node dst = node_id_map.get(e.dst);
			out.write("(edge "+(e.id)+" "+src.id+" "+dst.id+")\n");
		}
		writeTulipClusters(topnet, out,1);
		writeTulipProps(topnet, out);

	}

	private static long writeTulipClusters(INet net, OutputStreamWriter out, long id) throws IOException {
		out.write("(cluster "+id+"\n");
		out.write("; "+net.getUniqueName()+"\n");
		TreeSet<Long> nodes = new TreeSet<Long>();
		TreeSet<Long> edges = new TreeSet<Long>();

		LinkedList<ILink> links = new LinkedList<ILink>();
		LinkedList<INet> nets = new LinkedList<INet>();
		for(IModelNode c : net.getAllChildren()) {
			if(c instanceof INet) {
				nets.add((INet)c);
			}
			else if(c instanceof IRouter) {
				nodes.add(node_id_map.get(c.getDBID()).id);
			}
			else if(c instanceof IHost) {
				nodes.add(node_id_map.get(c.getDBID()).id);
			}
			else if(c instanceof ILink) {
				links.add((ILink)c);
			}
		}
		for(ILink l : links) {
			final tulip_link e = edge_id_map.get(l.getDBID());
			if(e == null) {
				//must be a hub
				final tulip_hub h = (tulip_hub)node_id_map.get(l.getDBID());
				if(h == null) {
					throw new RuntimeException("how did this happen?");
				}
				if(nodes.contains(h.id)) {
					for(tulip_link ll : h.links) {
						final tulip_node src = node_id_map.get(ll.src);
						if(nodes.contains(src.id)) {
							edges.add(ll.id);
						}
					}
				}
			}
			else {
				final tulip_node src = node_id_map.get(e.src);
				final tulip_node dst = node_id_map.get(e.dst);
				if(nodes.contains(src.id) && nodes.contains(dst.id)) {
					edges.add(e.id);
				}
			}
		}
		
		out.write("(nodes");
		for(long n : nodes) {
			out.write(" "+n);
		}
		out.write(" )\n");
		
		out.write("(edges");
		for(Long e : edges)
			out.write(" "+e);
		out.write(" )\n");
		
		while(nets.size()>0) {
			id = writeTulipClusters(nets.remove(), out, id+1);
		}
		out.write(" )\n");
		return id;
	}
	
	private static void writeTulipProps(INet net, OutputStreamWriter out) throws IOException {
		final ClsDesc cls = ModelNodeVariable.getClsDesc(jprime.Link.Link.class);
		final double default_delay=Double.parseDouble(cls.vars.get(ModelNodeVariable.delay()).default_value);
		final double default_bandwidth=Double.parseDouble(cls.vars.get(ModelNodeVariable.bandwidth()).default_value);

		//-------
		out.write("(property 0 string \"type\"\n");
		out.write("(default \"host\" \"link\" )\n");
		for(tulip_node n : nodes) {
			switch(n.type) {
			case 1:
				out.write("(node "+n.id+" \"router\" )\n");
				break;		
			case 2:
				out.write("(node "+n.id+" \"hub\" )\n");
				break;
			}
		}
		out.write(" )\n");
		
		//-------
		out.write("(property 0 string \"dbid\"\n");
		out.write("(default \"0\" \"0\" )\n");
		for(tulip_node n : nodes) {
			out.write("(node "+n.id+" \""+n.dbid+"\" )\n");
		}
		for(tulip_link e : edges) {
			out.write("(edge "+e.id+" \""+e.dbid+"\" )\n");
		}
		out.write(" )\n");
		
		//-------		
		out.write("(property 0 double \"delay\"\n");
		out.write("(default \"0\" \""+default_delay+"\" )\n");
		for(tulip_link e : edges) {
			out.write("(edge "+e.id+" \""+e.delay+"\" )\n");
		}
		out.write(" )\n");
		
		//-------		
		out.write("(property 0 double \"bandwidth\"\n");
		out.write("(default \"0\" \""+default_bandwidth+"\" )\n");
		for(tulip_link e : edges) {
			out.write("(edge "+e.id+" \""+e.bandwidth+"\" )\n");
		}
		out.write(" )\n");
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
		if(args.length<3) {
			usage();
		}

		Action action = Action.fromString(args[0]);
		String name = args[1];

		Database db = Database.createDatabase();
		ModelInterface model = null;

		System.out.println("model name="+name+", action="+action);

		String pad="";
		for(int i=0;i<pad_len;i++)
			pad+=' ';
		pad+='\n';

		switch(action) {
		case create:
		{
			if(args.length!=4) {
				usage();
			}
			File file = new File(args[2]);
			if(!file.exists()) {
				throw new RuntimeException(args[2]+" does not exist!");
			}
			String fname = file.getName();
			String path = file.getAbsolutePath().replace(fname, "");
			model=getModel(db, fname, file, path, name);
			model.buildModel(model.loadParametersFromSystemProperties());
			model.getExperiment().save();
			OutputStreamWriter out = new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(args[3])));
			if(tulip) {
				out.write("(tlp \"2.3\"\n");
				writeTulipTopo(model.getExperiment().getRootNode(), out);
				out.write(")\n");
				out.close();
			}
			else {
				out.write(pad);
				writeTopo(model.getExperiment().getRootNode(), out);
				finishTopo(out, args[3]);
			}
			db.save(model.getExperiment(),null);
		}
		break;
		case load:
		{
			OutputStreamWriter out = new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(args[2])));
			Experiment exp = db.loadExperiment(name);
			if(tulip) {
				out.write("(tlp \"2.3\"\n");
				writeTulipTopo(exp.getRootNode(), out);
				out.write(")\n");
				out.close();
			}
			else {
				out.write(pad);
				writeTopo(exp.getRootNode(), out);
				finishTopo(out, args[2]);
			}
		}
		break;
		default:
			throw new RuntimeException("Unknown action "+action);
		}			
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
