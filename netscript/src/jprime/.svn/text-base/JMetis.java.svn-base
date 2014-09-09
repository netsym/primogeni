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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map.Entry;

import jprime.partitioning.PartitioningVisitor.PartitioningNode;
import jprime.partitioning.PartitioningVisitor.PartitioningRelationship;

/**
 * @author Nathanael Van Vorst
 * 
 * a Metis file config file and partition the graph using metis via JNI
 */
public class JMetis {
	private static boolean inited;
	static{
		inited=false;
	}
    public static class Partitioning {
    	public int edges_cut;
    	public final int[] parting;
    	public Partitioning(int num_veriticies) {
    		edges_cut=-1;
    		parting = new int[num_veriticies];
    	}
    }
	public static enum GraphType {
		NoWeights("0",0),
		EdgeWeights("1",1),
		NodeWeights("10",2),
		NodeAndEdgeWeights("11",3);

		public final String value;
		public final int int_value;
		GraphType(String value, int int_value){
			this.value=value;
			this.int_value=int_value;
		}
		@Override
		public String toString() {
			return value;
		}
		public boolean equals(String c) {
			return c.equals(value);
		}
		public static GraphType fromString(String s){
			for(GraphType g : GraphType.values()) {
				if(g.equals(s))
					return g;
			}
			return NoWeights;
		}
	}
	
	private final GraphType gtype;
	private final int numNodes;
	private final int numEdges;
	private final int numNodeWeights;
	private final int[] xadj; 
	private final int[] adjncy;
	private final int[] node_weights;
	private final int[] edge_weights;
	private final ArrayList<PartitioningNode> nodes;

	
	public JMetis(int node_count, int edge_count, ArrayList<PartitioningNode> nodes) {
		if(!inited) {
			inited=true;
			JNILibLoader.load("metis");
	    }
		this.gtype=GraphType.NodeAndEdgeWeights;
		this.numNodeWeights=1;
		
		this.numNodes = node_count;
		this.numEdges = edge_count;
		this.xadj=new int[numNodes+1];
		this.xadj[numNodes]=2*numEdges;
		this.adjncy=new int[2*numEdges];
		this.node_weights = new int[numNodes*numNodeWeights];
		this.edge_weights = new int[2*numEdges];
		this.nodes = nodes;
		//jprime.Console.out.println("There are "+numNodes+"("+numNodeWeights+")nodes, "+numEdges+" edges("+numEdgeWeights+"), and the type is "+gtype.value);
		
		if(numNodes != nodes.size()) {
			throw new RuntimeException("numNodes["+numNodes+"] != nodes.size()["+nodes.size()+"]");
		}
		
		int xadj_idx=0, adjncy_idx=0;
		
		for(PartitioningNode n: nodes) {
			xadj[xadj_idx]=adjncy_idx;
			if(numNodeWeights == 1) {
				node_weights[xadj_idx]=n.getWeight();
			}
			else {
				throw new RuntimeException("How did this happen?");
			}
			for(Entry<Integer, PartitioningRelationship> r : n.links.entrySet()) {
				adjncy[adjncy_idx]=r.getKey();
				edge_weights[adjncy_idx]=r.getValue().getWeight();
				adjncy_idx++;
			}
			xadj_idx++;
		}
		if(xadj_idx+1 != xadj.length)
			throw new RuntimeException("xadj_idx["+xadj_idx+"] != xadj.length["+xadj.length+"]");
		if(adjncy_idx != adjncy.length)
			throw new RuntimeException("adjncy_idx["+adjncy_idx+"] != adjncy.length["+adjncy.length+"]");
		/*
		jprime.Console.out.println("xadj["+xadj.length+"]:");
		for(int i : xadj) {
			jprime.Console.out.print(i+",");
		}
		jprime.Console.out.println("\nadjncy["+adjncy.length+"]:");
		for(int i : adjncy) {
			jprime.Console.out.print(i+",");
		}
		jprime.Console.out.println("\nnode_weights["+node_weights.length+"]:");
		for(int i : node_weights) {
			jprime.Console.out.print(i+",");
		}
		jprime.Console.out.println("\nedge_weights["+edge_weights.length+"]:");
		for(int i : edge_weights) {
			jprime.Console.out.print(i+",");
		}
		jprime.Console.out.print("\n");
		*/
	}
	
	/**
	 * @param filename the metis graph file
	 * @param edge_weight_proj since there can only be one edge weight we need to combine them into a single value.  The vector tells us how to add them together.
	 * @throws IOException
	 */
	public JMetis(String filename) throws IOException {
		this.nodes=null;
		if(!inited) {
			inited=true;
			System.loadLibrary("metis");
	    }
		BufferedReader f =  new BufferedReader(new FileReader(new File(filename)));
		String l = f.readLine().trim();
		String[] config = l.split(" ");
		switch(config.length) {
		case 2:
			gtype = GraphType.NoWeights;
			numNodes=Integer.parseInt(config[0]);
			numEdges=Integer.parseInt(config[1]);
			numNodeWeights=0;
			break;
		case 3:
			numNodes=Integer.parseInt(config[0]);
			numEdges=Integer.parseInt(config[1]);
			gtype = GraphType.fromString(config[2]);
			switch(gtype) {
			case EdgeWeights:
			case NoWeights:
				numNodeWeights=0;
				break;
			case NodeWeights:
			case NodeAndEdgeWeights:
				numNodeWeights=1;
				break;
			default:
				throw new RuntimeException("unknown type");
			}
			break;
		case 4:
			numNodes=Integer.parseInt(config[0]);
			numEdges=Integer.parseInt(config[1]);
			gtype = GraphType.fromString(config[2]);
			numNodeWeights=Integer.parseInt(config[3]);
			break;
		default:
			throw new RuntimeException("Invalid config line '"+l+"'! expected [num vert] [num edges] [type]? [num vert weights]? ");
		}
		xadj=new int[numNodes+1];
		xadj[numNodes]=2*numEdges;
		adjncy=new int[2*numEdges];
		switch(gtype) {
			case NoWeights:
				edge_weights = new int[0];
				node_weights = new int[0];
				break;
			case EdgeWeights:
				edge_weights = new int[2*numEdges];
				node_weights = new int[0];
				break;
			case NodeWeights:
				edge_weights = new int[0];
				node_weights = new int[numNodes*numNodeWeights];
				break;
			case NodeAndEdgeWeights:
				node_weights = new int[numNodes*numNodeWeights];
				edge_weights = new int[2*numEdges];
				break;
			default:
				throw new RuntimeException("unknown type "+gtype);
		}
		//jprime.Console.out.println("There are "+numNodes+"("+numNodeWeights+")nodes, "+numEdges+" edges, and the type is "+gtype.value);
		parse(f);
	}
	
	/**
	 * @param f
	 * @throws IOException
	 */
	private void parse(BufferedReader f) throws IOException {
		int node_id=0;
		int node_weight_idx=0;
		int adj_idx=0;
		String l = f.readLine();
		while(null != l) {
			//jprime.Console.out.println("["+node_id+"]="+l);
			String[] temp_line = l.trim().split(" ");
			//read in node weights
			int idx=0;
			for(; idx<numNodeWeights;idx++) {
				node_weights[node_weight_idx]=Integer.parseInt(temp_line[idx]);
				node_weight_idx++;
			}
			xadj[node_id]=adj_idx;
			//read in edges
			for(;idx<temp_line .length;idx++) {
				adjncy[adj_idx]=Integer.parseInt(temp_line[idx])-1;//the file should store node id starting at 1
				if(gtype == GraphType.EdgeWeights || gtype == GraphType.NodeAndEdgeWeights) {//read in edge weights
					edge_weights[adj_idx]=Integer.parseInt(temp_line[idx]);
					idx++;
					adj_idx++;
				}
			}
			node_id++;
			l = f.readLine();
		}
	}
	
	/**
	 * @param force_PartGraphRecursive force the use of PartGraphRecursive
	 * @param force_PartGraphKway  force the use of PartGraphKway 
	 * @param num_vertices The number of vertices in the graph
	 * @param xadj the index into adjncy for each node
	 * @param adjncy the adjcy list for all nodes
	 * @param vwgt weights for the ndoes
	 * @param adjwgt edge wieghts
	 * @param wgtflag Used to indicate if the graph is weighted. this is the int_value of the GraphType
	 * @param nparts number of parts we want to graph to be partitioned into
	 * @param options see manual
	 * @param partioned_nodes an ints, where the index is the node_id and the value is part_id
	 * @return the number of edges that were cut
	 */
	private native int PartitionGraph(
			boolean force_PartGraphRecursive,
			boolean force_PartGraphKway,
			int num_vertices,
			int[] xadj,
			int[] adjncy,
			int[] vwgt,
			int[] adjwgt,
			int wgtflag,
			//int numflag -- we will always start from 0
			int nparts,
			int[] options,
			//int* edgecut -- we will return this
			int[] partioned_nodes
	);
	
	
	/**
	 * @param force_PartGraphRecursive force the use of PartGraphRecursive
	 * @param force_PartGraphKway  force the use of PartGraphKway 
	 * @param num_vertices The number of vertices in the graph
	 * @param num_constraints The number of constraints. This should be greater than one and smaller than 15.
	 * @param xadj the index into adjncy for each node
	 * @param adjncy the adjcy list for all nodes
	 * @param vwgt weights for the ndoes
	 * @param adjwgt edge wieghts
	 * @param wgtflag Used to indicate if the graph is weighted. this is the int_value of the GraphType
	 * @param nparts number of parts we want to graph to be partitioned into
	 * @param options see manual
	 * @param partioned_nodes an ints, where the index is the node_id and the value is part_id
	 * @return the number of edges that were cut
	 */
	private native int mcPartitionGraph(
			boolean force_PartGraphRecursive,
			boolean force_PartGraphKway,
			int num_vertices,
			int num_constraints,
			int[] xadj,
			int[] adjncy,
			int[] vwgt,
			int[] adjwgt,
			int wgtflag,
			//int numflag -- we will always start from 0
			int nparts,
			int[] options,
			//int* edgecut -- we will return this
			int[] partioned_nodes
	);

	/**
	 * @param force_PartGraphRecursive force the use of PartGraphRecursive
	 * @param force_PartGraphKway  force the use of PartGraphKway 
	 * @param num_vertices The number of vertices in the graph
	 * @param xadj the index into adjncy for each node
	 * @param adjncy the adjcy list for all nodes
	 * @param vwgt weights for the ndoes
	 * @param adjwgt edge wieghts
	 * @param wgtflag Used to indicate if the graph is weighted. this is the int_value of the GraphType
	 * @param nparts number of parts we want to graph to be partitioned into
	 * @param part_weights The number of constraints. This should be greater than one and smaller than 15.
	 * @param options see manual
	 * @param partioned_nodes an ints, where the index is the node_id and the value is part_id
	 * @return the number of edges that were cut
	 */
	private native int wPartitionGraph(
			boolean force_PartGraphRecursive,
			boolean force_PartGraphKway,
			int num_vertices,
			int[] xadj,
			int[] adjncy,
			int[] vwgt,
			int[] adjwgt,
			int wgtflag,
			//int numflag -- we will always start from 0
			int nparts,
			float[] part_weights,
			int[] options,
			//int* edgecut -- we will return this
			int[] partioned_nodes
	);
	
	public Partitioning partitionGraph(int nparts) {
		return partitionGraph(nparts, false,false);
	}
	
	public Partitioning partitionGraph(int nparts,	boolean force_PartGraphRecursive, boolean force_PartGraphKway) {
		if(force_PartGraphRecursive && force_PartGraphKway)
			throw new RuntimeException("force_PartGraphRecursive and force_PartGraphKway cannot both be true!");
		Partitioning rv = new Partitioning(numNodes);
		if(numNodeWeights>1) {
			//jprime.Console.out.println("mcPartitionGraph");
			rv.edges_cut = mcPartitionGraph(force_PartGraphRecursive,force_PartGraphKway,numNodes,numNodeWeights,xadj,adjncy,node_weights,edge_weights,gtype.int_value,nparts,new int[]{0},rv.parting);
		}
		else {
			//jprime.Console.out.println("PartitionGraph");
			rv.edges_cut = PartitionGraph(force_PartGraphRecursive,force_PartGraphKway,numNodes,xadj,adjncy,node_weights,edge_weights,gtype.int_value,nparts,new int[]{0},rv.parting);
		}
		return rv;
	}
	
	public Partitioning partitionGraph(int nparts, float[] weights) {
		return partitionGraph(nparts, weights, false,false);
	}
	
	public Partitioning partitionGraph(int nparts, float[] weights,	boolean force_PartGraphRecursive, boolean force_PartGraphKway) {
		if(force_PartGraphRecursive && force_PartGraphKway)
			throw new RuntimeException("force_PartGraphRecursive and force_PartGraphKway cannot both be true!");
		if(numNodeWeights>1)
			throw new RuntimeException("A weighted partitioning can only have 1 wieght per node. Found "+numNodeWeights+"!");
		Partitioning rv = new Partitioning(numNodes);
		//jprime.Console.out.println("wPartitionGraph");
		rv.edges_cut = wPartitionGraph(force_PartGraphRecursive,force_PartGraphKway,numNodes,xadj,adjncy,node_weights,edge_weights,gtype.int_value,nparts,weights,new int[]{0},rv.parting);
		return rv;
	}
	
	public ArrayList<PartitioningNode> getNodes() {
		return nodes;
	}
	
	/**
	 * @param argv
	 * @throws IOException
	 */
	public static void main(String argv[]) throws IOException {
		if(argv.length != 2) {
			jprime.Console.err.println("Usage: jprime.JMetis <num parts> <metis file>");
			jprime.Console.halt(100);
		}
		jprime.Console.out.println("num parts="+argv[0]+", in="+argv[1]+", out="+argv[1]+".part");
		JMetis graph = new JMetis(argv[1]);
		jprime.Console.out.println("Partitioning....");
		Partitioning rv = graph.partitionGraph(Integer.parseInt(argv[0]));
		jprime.Console.out.println("Cut "+rv.edges_cut+" edges");
		for(int i=0;i<rv.parting.length;i++) {
			jprime.Console.out.println("\t["+(i+1)+"]"+rv.parting[i]);
		}
	}

}