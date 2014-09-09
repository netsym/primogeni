package jprime.visitors;

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
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

import jprime.IModelNode;
import jprime.Metadata;
import jprime.ModelNode;
import jprime.ModelNodeReplica;
import jprime.SymbolTable;
import jprime.Aggregate.IAggregate;
import jprime.BaseInterface.IBaseInterface;
import jprime.BaseInterface.IBaseInterfaceAlias;
import jprime.Interface.IInterface;
import jprime.Link.ILink;
import jprime.Monitor.IMonitor;
import jprime.Net.INet;
import jprime.RouteTable.IRouteTable;
import jprime.RoutingSphere.IRoutingSphere;
import jprime.Traffic.ITraffic;
import jprime.TrafficType.ITrafficType;
import jprime.database.IPersistableCache.PhaseChange;
/* $if false == DEBUG $ */
import jprime.database.IPersistableCache;
/* $endif$ */
import jprime.gen.EntityFactory;
import jprime.gen.TLV;
import jprime.partitioning.Alignment;
import jprime.partitioning.AlignmentForwardingEntry;
import jprime.partitioning.IUIDRange;
import jprime.partitioning.Partition;
import jprime.partitioning.Partitioning;
import jprime.partitioning.Partitioning.IPAlignment;
import jprime.routing.IRouteEntry;
import jprime.util.IPAddressUtil;
import jprime.variable.BooleanVariable;
import jprime.variable.FloatingPointNumberVariable;
import jprime.variable.IntegerVariable;
import jprime.variable.ListVariable;
import jprime.variable.ModelNodeVariable;
import jprime.variable.OpaqueVariable;
import jprime.variable.ResourceIdentifierVariable;
import jprime.variable.StringVariable;
import jprime.variable.SymbolVariable;

/**
 * @author Nathanael Van Vorst
 * 
 * Each partition is encoded as follows:
 *     <start file>
 *       [partition info]
 *       [model nodes]
 *     <end file>
 *  --------------------------------
 *  --------------------------------
 *  Partition info in encoded as follows:
 *     <start partition map>
 *       [partition id]
 *       [topnet uid]
 *       [total # communities ]
 *       [total number of community forwarding entries]
 *       [num communities in this part]
 *       [community id in this partition]
 *       [community id in this partition]
 *          ...
 *          ...
 *     <end partition map>
 *     <start communities> //this is not a tlv -- its just to help format this comment
 *       <start community>
 *         [community id]
 *         [number of uids that follow]
 *       <end community>
 *       [low uid][high_uid]
 *       [low uid][high_uid]
 *       ...
 *       ...
 *       <start community>
 *         [community id]
 *         [number of uids that follow]
 *       <end community>
 *       [low uid][high_uid]
 *       [low uid][high_uid]
 *       ...
 *       ...
 *     <end communities> //this is not a tlv -- its just to help format this comment
 *     <start forwarding entries> //this is not a tlv -- its just to help format this comment
 *        [[src com_id][dst_min com_id][dst_max com_id][next_hop com_id]]
 *        [[src com_id][dst_min com_id][dst_max com_id][next_hop com_id]]
 *        ...
 *        ...
 *     <end forwarding entries> //this is not a tlv -- its just to help format this comment
 *  --------------------------------
 *  --------------------------------
 *  The symbol table is encoded as followsModelNodes are encoded as follows:
 *  --------------------------------
 *  --------------------------------
 *  ModelNodes are encoded as follows:
 *     <start modelnode>
 *       [uid]
 *       [parent uid]
 *       [replica id]
 *       [type]
 *       [attributes (ModelNodeVariables)]
 *       [# route entries that follow -- this is only there for route tables]
 *     <end modelnode>
 *     
 *   If it is a Route table a list of route entries will follow the ModelNode
 *  --------------------------------
 *  --------------------------------
 *  Ghost Nodes are encoded as follows
 *     <start modelnode>
 *       [uid]
 *       [parent uid]
 *       [remote partition id]
 *       [remote communituy id]
 *       [attributes (ModelNodeVariables)]
 *     <end modelnode>
 *  --------------------------------
 *  --------------------------------
 *  ModelNodeVariables
 *     <start modelnodevariable>
 *       [type id]
 *       [value]
 *     <end modelnodevariable>
 *  --------------------------------
 *  --------------------------------
 *   Route Entries are encoded as follows:
 *     <start routeentry>
 *       [src min]
 *       [src max]
 *       [dst min]
 *       [dst max]
 *       [outboundiface]
 *       [numberofbits]
 *       [nexthopid]
 *       [edge_iface]
 *       [bus_idx]
 *       [numofbitsbus]
 *       [cost]
 *     <end routeentry>
 */
public class TLVVisitor extends jprime.gen.TLV {
	private final String TAB="\t";
	private static final boolean DEBUG=false;
	public static enum Action { ENCODE_CROSS_ALGINED, ENCODE_GHOST, ENCODE_REAL, ENCODE_SPHERE, ENCODE_SPHERE_STOP, NO_ENCODE };
	public static enum TLVType {
		STRING(StringVariable.getClassTypeId()),
		LONG(IntegerVariable.getClassTypeId()),
		FLOAT(FloatingPointNumberVariable.getClassTypeId()),
		BOOL(BooleanVariable.getClassTypeId()),
		LIST(ListVariable.getClassTypeId()),
		OPAQUE(OpaqueVariable.getClassTypeId()),
		SYMBOL(SymbolVariable.getClassTypeId()),
		RESOURCE_ID(ResourceIdentifierVariable.getClassTypeId()),
		PROPERTY(TLV.PROPERTY),
		SYMBOL_TABLE(TLV.SYMBOL_TABLE),
		SYMBOL_TABLE_ENTRY(TLV.SYMBOL_TABLE_ENTRY),
		PARTITION(TLV.PARTITION),
		COMMUNITY(TLV.COMMUNITY),
		ROUTE_TABLE(TLV.ROUTE_TABLE),
		ROUTE_ENTRY(TLV.ROUTE_ENTRY),
		GHOST_NODE(TLV.GHOST_NODE),
		GENERIC_NODE(TLV.GENERIC_NODE);

		public final String str;
		public final int type;

		/**
		 * @param type
		 */
		private TLVType(int type) {
			this.type=type;
			this.str=getType(type);
		}

		/**
		 * @param type
		 * @return
		 */
		private static String getType(Integer type) {
			String t = type.toString();
			while(t.length()<4) {
				t="0"+t;
			}
			if(t.length()>4) {
				throw new RuntimeException("The c++ model builder expects all tlv types be encoded as a two character string. '"+t+"' isnt!");
			}
			return t;
		}

		/**
		 * @return
		 */
		public static TLVType fromInt(int type) {
			for(TLVType t :  TLVType.values()) {
				if(t.type==type)
					return t;
			}
			throw new RuntimeException("Unknown type "+type);
		}
	}

	public static class Node {
		final Partition processingNode;
		final String fileName;
		final BufferedWriter file, xml;
		final Map<Long, Map<Long, ModelNode>> replicaMap = new HashMap<Long, Map<Long, ModelNode>>();
		final Map<Long, TreeSet<Long>> encodedForSpheres = new HashMap<Long, TreeSet<Long>>();
		final int idx;
		public Node(Partition processingNode, String base, boolean doXML, int idx) throws IOException {
			this.processingNode=processingNode;
			this.idx=idx;
			this.fileName=base+"part_"+processingNode.getId()+".tlv";
			this.file = new BufferedWriter(new FileWriter(this.fileName));
			if(doXML) {
				xml = new BufferedWriter(new FileWriter(base+"part_"+processingNode.getId()+".xml"));
			}
			else {
				xml=null;
			}
		}

	}

	private final Stack<Action[]> todo = new Stack<Action[]>();
	private final Partitioning partitioning;
	private final ArrayList<Node> nodes;
	private final boolean doXML;
	private final Stack<String> tabs= new Stack<String>();

	/**
	 * @param topnet
	 * @throws IOException 
	 */
	public TLVVisitor(Metadata meta, Partitioning partitioning, String dir, String model_name, Map<Integer,String> tlvs, boolean doXML) throws IOException {
    	/* $if DEBUG $
		meta.logPhaseChange(PhaseChange.start_tlv_encode);
		$else$ */
		IPersistableCache.logPhaseChange(PhaseChange.start_tlv_encode, "");
		/* $endif$ */
		this.partitioning=partitioning;
		this.nodes=new ArrayList<Node>(partitioning.getProcessingNodes().size());
		this.doXML=doXML;

		if(model_name == null)
			model_name="";
		else if(model_name.length()>0)
			model_name+="_";

		final String base=dir+"/"+model_name;



		int q=0;
		for(Partition p: partitioning.getProcessingNodes().values()) {
			nodes.add(new Node(p, base, doXML,q));
			tlvs.put(nodes.get(q).processingNode.getId(), nodes.get(q).fileName);
			if(DEBUG){
				jprime.Console.out.println("********************************");
				jprime.Console.out.println("********************************");
				jprime.Console.out.println("********************************");
				jprime.Console.out.println("Encoding "+model_name+" partition "+nodes.get(q).processingNode.getId()+" to file \""+nodes.get(q).fileName
						+"\". Alignments="+nodes.get(q).processingNode.getAlginments());
			}
			q++;
		}

		Action b = Action.ENCODE_REAL;
		if(partitioning.getTopnet().getAlignments(partitioning).size()>1)
			b=Action.ENCODE_CROSS_ALGINED;
		Action a[]=new Action[nodes.size()];

		if(doXML) {
			tabs.add(new String(TAB));
			for(Node pn : nodes) {
				pn.xml.write("<?xml version=\"1.0\" ?>\n");
				pn.xml.write("<partitioned_model alignments=\""+pn.processingNode.getAlginments()+"\">\n");
			}
		}
		for(Node n : nodes) {
			a[n.idx]=b;
			encodePartitionMap(n);
			encodeSymbolTable(n);
		}
		todo.push(a);
		visit(partitioning.getTopnet());

		for(Node pn : nodes) {
			pn.file.close();
			if(doXML) {
				pn.xml.write("</partitioned_model>\n");
				pn.xml.close();
			}
		}
	}

	/**
	 * @param n
	 */
	public void visit(ModelNode n) {
		//jprime.Console.out.println("Visited "+n.getUniqueName()+"["+n.getClass().getSimpleName()+"] todo.peek()="+todo.peek());
		/**
		 * check If the parent was cross aligned and take appropriate action
		 */
		Action[] cur = todo.peek();
		Action[] next = new Action[cur.length];
		for(Node pn : nodes) {
			switch(cur[pn.idx]) {
			case ENCODE_CROSS_ALGINED:
				//my parent was cross-aligned so I may need to be a ghost....
				if(n instanceof INet && !n.isAlias()) {
					Set<Integer> aligns = ((INet)n).getAlignments(partitioning);
					int contains = pn.processingNode.countContainedAlignments(aligns);
					if(contains==aligns.size()) {
						next[pn.idx]=Action.ENCODE_REAL;
					}
					else if(contains==0){
						//this net doesn't exist in this alignment
						if(!((INet)n).isRoutingSphere() && !((INet)n).haveSubSpheres()) {
							/* for routing to work we need to recurse until
							 * (1) the net which is a routing sphere
							 * or
							 * (2) the net is not a sphere and has no
							 *     sub-nets which are spheres
							 */
							next[pn.idx]=Action.ENCODE_GHOST;
						}
						else {
							//it must not be a routing sphere BUT has a sub-net which is a routing sphere 
							//jprime.Console.out.println("NATE "+n.getUniqueName()+" would have been a ghost net, but not its not....");
							next[pn.idx]=Action.ENCODE_SPHERE;
						}
					}
					else {
						next[pn.idx]=Action.ENCODE_CROSS_ALGINED;
					}
				}
				else if((n instanceof IAggregate || n instanceof IMonitor) && !n.isAlias()) {
					//if we get here that means our parent network is encoded here, we need to be as well.
					next[pn.idx]=Action.ENCODE_CROSS_ALGINED;
				}
				else if(n instanceof ITraffic && !n.isAlias()) {
					//traffics must always be real
					next[pn.idx]=Action.ENCODE_REAL;
				}
				else if(n instanceof ILink && !n.isAlias()) {
					Set<Integer> aligns = ((ILink)n).getAlignments(partitioning);
					int contains = pn.processingNode.countContainedAlignments(aligns);
					if(contains==aligns.size()) {
						next[pn.idx]=Action.ENCODE_REAL;
					}
					else if(contains==0){
						//this link doesn't exist in this alignment
						next[pn.idx]=Action.ENCODE_GHOST;
					}
					else {
						//this link is cross-aligned
						next[pn.idx]=Action.ENCODE_CROSS_ALGINED;
					}
				}
				else if(n instanceof IRoutingSphere && !n.isAlias()) {
					Set<Integer> aligns = n.getAlignments(partitioning);
					int contains = pn.processingNode.countContainedAlignments(aligns);
					if(contains == 0 ) {
						next[pn.idx]=Action.ENCODE_GHOST;
					}
					else {
						//if sphere is cross-aligned it should be encoded as real
						next[pn.idx]=Action.ENCODE_REAL;
					}
				}
				else if(n.isAlias()) {
					Set<Integer> aligns = n.deference().getAlignments(partitioning);
					int contains = pn.processingNode.countContainedAlignments(aligns);
					if(contains==aligns.size()) {
						//this node contains all of the nodes....
						next[pn.idx]=Action.ENCODE_REAL;
					}
					else {
						//this node doesn't exist in this alignment or its only partially contained,
						//but my parent does so I should be ghosted
						next[pn.idx]=Action.ENCODE_GHOST;
					}
				}
				else {
					Set<Integer> aligns = n.deference().getAlignments(partitioning);
					int contains = pn.processingNode.countContainedAlignments(aligns);
					if(contains==aligns.size()) {
						//this node contains all of the nodes....
						next[pn.idx]=Action.ENCODE_REAL;
					}
					else {
						//this node doesn't exist in this alignment or its only partially contained,
						//but my parent does so I should be ghosted
						next[pn.idx]=Action.ENCODE_GHOST;
					}
				}
				break;
			case ENCODE_SPHERE:
				//my parent was encoded because it was a routing sphere or one of its kids was a sphere
				if(n instanceof INet && !n.isAlias()) {
					Set<Integer> aligns = ((INet)n).getAlignments(partitioning);
					int contains = pn.processingNode.countContainedAlignments(aligns);
					if(contains==0){
						if(!((INet)n).isRoutingSphere() && !((INet)n).haveSubSpheres()) {
							next[pn.idx]=Action.ENCODE_SPHERE_STOP;
						}
						else {
							next[pn.idx]=Action.ENCODE_SPHERE;
						}
					}
					else {
						throw new RuntimeException("How did this happen?");
					}
				}
				else {
					next[pn.idx]=Action.ENCODE_GHOST;
				}
				break;
			case ENCODE_REAL:
				//the parent was completely contained so we only need to worry about aliases...
				if(n.isAlias()) {
					Set<Integer> aligns = n.deference().getAlignments(partitioning);
					int contains = pn.processingNode.countContainedAlignments(aligns);
					if(contains==aligns.size()) {
						//this node contains all of the nodes....
						next[pn.idx]=Action.ENCODE_REAL;
					}
					else if(contains==0){
						//this node which this alias points to doesn't exist in this alignment
						next[pn.idx]=Action.ENCODE_GHOST;
					}
					else {
						next[pn.idx]=Action.ENCODE_REAL;
					}
				}
				else {
					//since my parent was completely within this alignment, I must be!
					next[pn.idx]=Action.ENCODE_REAL;
				}
				break;
			case ENCODE_SPHERE_STOP:
			case ENCODE_GHOST:
			case NO_ENCODE:
				next[pn.idx]=Action.NO_ENCODE;
				break;
			default:
				throw new RuntimeException("Unknown action type "+todo.peek());
			}
		}
		todo.add(next);
		if(doXML) {
			for(Node pn : nodes) {
				switch(next[pn.idx]) {
				case ENCODE_CROSS_ALGINED:
				case ENCODE_SPHERE:
				case ENCODE_SPHERE_STOP:
				case ENCODE_REAL:
				case ENCODE_GHOST:
					try {
						pn.xml.write(tabs.peek()+"<node type=\""+n.getClass().getSimpleName()+"\" uid=\""+n.getUID()+"\" aligns=\""+n.getAlignments(partitioning)+"\" >\n");
						pn.xml.write(tabs.peek()+TAB+"<unqiueName value=\""+n.getUniqueName()+"\" />\n");
						if(n instanceof INet) {
							pn.xml.write(tabs.peek()+TAB+"<partDebug isSphere=\""+((INet)n).isRoutingSphere()+"\" haveSubSpheres=\""+((INet)n).haveSubSpheres()+"\" />\n");
						}
						if(n instanceof ModelNodeReplica) {
							IModelNode r = ((ModelNodeReplica) n).getReplicatedNode();
							if(r!=null)
								pn.xml.write(tabs.peek()+TAB+"<replicate value=\""+r.getUniqueName()+"\" />\n");
						}
					} catch (IOException e) {
						jprime.Console.err.printStackTrace(e);
					}
					break;
				case NO_ENCODE:
					break;
				default:
					throw new RuntimeException("Unknown action type "+next[pn.idx]);
				}
			}
			tabs.add(tabs.peek()+TAB);
		}
		for(Node pn : nodes) {
			switch(next[pn.idx]) {
			case ENCODE_CROSS_ALGINED:
			case ENCODE_SPHERE_STOP:
			case ENCODE_SPHERE:
			case ENCODE_REAL:
				//we need to keep a hard reference to these in case the DB tries to flush them!
				ResourceIdentifierVariable s=null,d=null;
				if(n instanceof ITrafficType) {
					//we need to evaluate the src/dst lists
					s = ((ITrafficType)n).getSrcs();
					d = ((ITrafficType)n).getDsts();
					if(null != s)
						s.evaluate((ModelNode)n.getParent().getParent(), ModelNode.relativePath(n,n.getParent().getParent()));
					if(null != d)
						d.evaluate((ModelNode)n.getParent().getParent(), ModelNode.relativePath(n,n.getParent().getParent()));
				}
				else if(n instanceof IAggregate) {
					//we need to evaluate what to aggregate
					s = ((IAggregate)n).getToAggregate();
					if(null != s)
						s.evaluate((ModelNode)n.getParent(), ModelNode.relativePath(n,n.getParent()));
				}
				else if(n instanceof IMonitor) {
					//we need to evaluate what to monitor
					s = ((IMonitor)n).getToMonitor();
					if(null != s)
						s.evaluate((ModelNode)n.getParent(), ModelNode.relativePath(n,n.getParent()));
				}
				encode(next[pn.idx],pn,n);
				break;
			case ENCODE_GHOST:
				if(n instanceof IBaseInterfaceAlias) {
					//we only need to encode ghosts for interfaces.
					encodeGhost(pn,n,(ModelNode)n.deference());
				}
				else if(n instanceof IRoutingSphere) {
					//we only need to encode ghosts for interfaces.
					encodeGhost(pn,n,(ModelNode)n);
				}
				else {
					//we put in a place holder....
					encodePlaceHolder(pn,n,(ModelNode)n.deference());
				}
				break;
			case NO_ENCODE:
				if(DEBUG)jprime.Console.out.println("\tNot encoding "+n.getUniqueName()+", aligns="+n.getAlignments(partitioning));
				break;
			default:
				throw new RuntimeException("Unknown action type "+next[pn.idx]);
			}
		}
		for(ModelNode c : n.getAllChildren()) {
			c.accept(this);
		}
		if(doXML) {
			tabs.pop();
			for(Node pn : nodes) {
				switch(next[pn.idx]) {
				case ENCODE_CROSS_ALGINED:
				case ENCODE_SPHERE:
				case ENCODE_REAL:
				case ENCODE_GHOST:
				case ENCODE_SPHERE_STOP:
					try {
						pn.xml.write(tabs.peek()+"</node>\n");
					} catch (IOException e) {
						jprime.Console.err.printStackTrace(e);
					}
					break;
				case NO_ENCODE:
					break;
				default:
					throw new RuntimeException("Unknown action type "+next[pn.idx]);
				}
			}
		}
		todo.pop();
	}

	/**
	 * @param n
	 */
	private void encode(Action a, Node pn, ModelNode n) {
		if(a == Action.ENCODE_SPHERE || a == Action.ENCODE_SPHERE_STOP) {
			if(!pn.encodedForSpheres.containsKey(n.getMetadata().getDBID())) {
				pn.encodedForSpheres.put(n.getMetadata().getDBID(), new TreeSet<Long>());
			}
			pn.encodedForSpheres.get(n.getMetadata().getDBID()).add(n.getUID());
		}
		ModelNode replica=null;
		if(n.isReplica()) {
			replica = ((ModelNodeReplica)n).getReplicatedNode();
		}
		if(replica != null) {
			boolean makeReal=false;
			//check if this node's replica is in this partition....
			//if its not, make this replica real
			if(replica.getMetadata() != n.getMetadata()) {
				//its from another exp or library....
				makeReal=true;
			}
			Set<Integer> aligns = replica.getAlignments(partitioning);
			int contains = pn.processingNode.countContainedAlignments(aligns);

			if(contains == aligns.size()) {
				//its already here.....
				if(pn.encodedForSpheres.containsKey(replica.getMetadata().getDBID())) {
					if(pn.encodedForSpheres.get(replica.getMetadata().getDBID()).contains(replica.getUID())) {
						//the replicate is only partially encoded
						makeReal=true;
					}
					else {
						//the replicate was fully encoded
						makeReal=false;
					}
				}
				else {
					//the replicate was fully encoded
					makeReal=false;
				}
			}
			else {
				//we need to make this node real because this node isn't in this partition.
				makeReal=true;
			}
			if(makeReal) {
				//if there was a previous node which replicated the same node we could replicate that node instead.
				if(pn.replicaMap.containsKey(replica.getMetadata().getDBID())) {
					if(pn.replicaMap.get(replica.getMetadata().getDBID()).containsKey(replica.getUID())) {
						//we can replica replicaMap[meta id][uid] instead
						replica = pn.replicaMap.get(replica.getMetadata().getDBID()).get(replica.getUID());
						makeReal=false;
					}
				}
				else {
					pn.replicaMap.put(replica.getMetadata().getDBID(), new HashMap<Long, ModelNode>());
				}
				if(makeReal) {
					if(a != Action.ENCODE_SPHERE || a == Action.ENCODE_SPHERE_STOP) {
						pn.replicaMap.get(replica.getMetadata().getDBID()).put(replica.getUID(), n);
					}
					replica=null;
				}
			}
			//else the node which this replicates is in this partition...its okay
		}
		if(n.isReplica()) {
			if(replica==null) {
				encodeRealNode(pn,n,true);
			}
			else {
				encodeReplica(pn,(ModelNodeReplica)n, replica);
			}
		}
		else {
			encodeRealNode(pn,n,false);
		}
	}


	/**
	 * @param type
	 * @param length
	 * @param value
	 */
	private void writeTLV(Node pn, TLVType type, int length, String value) {
		try {
			pn.file.write(type.str);
			pn.file.write(Integer.toString(length));
			pn.file.write('\n');
			pn.file.write(value);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @param type
	 * @param length
	 * @param value
	 * @return
	 */
	public static String makeTLV(TLVType type, int length, String value) {
		return type.str+length+"\n"+value;
	}

	/**
	 * @param range
	 * @return
	 */
	private String encodeUIDRange(IUIDRange range) {
		String l = "";
		l+=makeTLV(TLVType.LONG,Long.toString(range.getLow()).length(),Long.toString(range.getLow()));
		l+=makeTLV(TLVType.LONG,Long.toString(range.getHigh()).length(),Long.toString(range.getHigh()));
		return makeTLV(TLVType.LIST,l.length(),l);
	}


	/**
	 * @param p
	 * 
	 *  Partition info in encoded as follows:
	 *     <start partition map>
	 *       [partition id]
	 *       [topnet uid]
	 *       [total # communities ]
	 *       [total number of community forwarding entries]
	 *       [total number of ip map entries]
	 *       [num communities in this part]
	 *          ...
	 *          ...
	 *     <end partition map>
	 *     <start communities> //this is not a tlv -- its just to help format this comment
	 *       <start community>
	 *         [community id]
	 *         [partition id]
	 *         [number of uids that follow]
	 *       <end community>
	 *       [low uid][high_uid]
	 *       [low uid][high_uid]
	 *       ...
	 *       ...
	 *       <start community>
	 *         [community id]
	 *         [partition id]
	 *         [number of uids that follow]
	 *       <end community>
	 *       [low uid][high_uid]
	 *       [low uid][high_uid]
	 *       ...
	 *       ...
	 *     <end communities> //this is not a tlv -- its just to help format this comment
	 *     <start forwarding entries> //this is not a tlv -- its just to help format this comment
	 *        [[src com_id][dst_min com_id][dst_max com_id][next_hop com_id]]
	 *        [[src com_id][dst_min com_id][dst_max com_id][next_hop com_id]]
	 *        ...
	 *        ...
	 *     <end forwarding entries> //this is not a tlv -- its just to help format this comment
	 *     <start ip map entries> //this is not a tlv -- its just to help format this comment
	 *        [[low ip][high ip][community]]
	 *        [[low ip][high ip][community]]
	 *        ...
	 *        ...
	 *     <end ip map  entries> //this is not a tlv -- its just to help format this comment
	 * @throws IOException 
	 */
	private void encodePartitionMap(Node pn) {
		String t1 = null,t2=null,t3=null;
		if(doXML) {
			t1=tabs.peek()+TAB;
			t2=t1+TAB;
			t3=t2+TAB;
			try {
				pn.xml.write(tabs.peek()+"<partitionMap>\n");
			} catch (IOException e) {
				jprime.Console.err.printStackTrace(e);
			}
		}
		{
			if(DEBUG)jprime.Console.out.println("\tEncoding PartitionMap ["+pn.processingNode.getId()+","
					+partitioning.getTopnet().getUID()+","
					+partitioning.getTotalNumberOfAlignments()+","
					+pn.processingNode.getAlginments().size()+","
					+partitioning.getAlignmentForwardingEntries().size()+"]!");
			String tlv=makeTLV(TLVType.LONG,Integer.toString(pn.processingNode.getId()).length(),Integer.toString(pn.processingNode.getId()));
			tlv+=makeTLV(TLVType.LONG,Long.toString(partitioning.getTopnet().getUID()).length(),Long.toString(partitioning.getTopnet().getUID()));
			tlv+=makeTLV(TLVType.LONG,Long.toString(partitioning.getTotalNumberOfAlignments()).length(),Long.toString(partitioning.getTotalNumberOfAlignments()));
			tlv+=makeTLV(TLVType.LONG,Long.toString(partitioning.getAlignmentForwardingEntries().size()).length(),Long.toString(partitioning.getAlignmentForwardingEntries().size()));
			tlv+=makeTLV(TLVType.LONG,Long.toString(partitioning.getIPAlignments().size()).length(),Long.toString(partitioning.getIPAlignments().size()));
			tlv+=makeTLV(TLVType.LONG,Long.toString(pn.processingNode.getAlginments().size()).length(),Long.toString(pn.processingNode.getAlginments().size()));
			writeTLV(pn,TLVType.PARTITION, tlv.length(), tlv);
		}
		{
			if(DEBUG)jprime.Console.out.println("\tEncoding communities!");
			if(doXML) {
				try {
					pn.xml.write(t1+"<communities>\n");
				} catch (IOException e) {
					jprime.Console.err.printStackTrace(e);
				}
			}

			for(Partition p : partitioning.getProcessingNodes().values()) {
				if(p.getAlginments().size()==0) {
					throw new RuntimeException("should never see this!");
				}
				else {
					for(Alignment c : p.getAlginments()) {
						if(doXML) {
							try {
								pn.xml.write(t2+"<community id=\""+c.getAlignId()+"\" partId=\""+c.getPartId()+"\" ranges=\""+c.getUIDRanges().size()+"\">\n");
							} catch (IOException e) {
								jprime.Console.err.printStackTrace(e);
							}
						}
						if(DEBUG)jprime.Console.out.println("\t\tEncoding community ["+c.getAlignId()+","+c.getUIDRanges().size()+"]");
						String community=makeTLV(TLVType.LONG,Integer.toString(c.getAlignId()).length(),Integer.toString(c.getAlignId()));
						community+=makeTLV(TLVType.LONG,Integer.toString(c.getPartId()).length(),Integer.toString(c.getPartId()));
						community+=makeTLV(TLVType.LONG,Integer.toString(c.getUIDRanges().size()).length(),Integer.toString(c.getUIDRanges().size()));
						writeTLV(pn,TLVType.COMMUNITY, community.length(), community);

						for(IUIDRange uid : c.getUIDRanges()) {
							if(DEBUG)jprime.Console.out.println("\t\t\t"+uid);
							try {
								pn.file.write(encodeUIDRange(uid));
							} catch (IOException e) {
								jprime.Console.err.printStackTrace(e);
							}
							if(doXML) {
								try {
									pn.xml.write(t3+"<uidRange low=\""+uid.getLow()+"\" high=\""+uid.getHigh()+"\" />\n");
								} catch (IOException e) {
									jprime.Console.err.printStackTrace(e);
								}
							}
						}
						if(doXML) {
							try {
								pn.xml.write(t2+"</community>\n");
							} catch (IOException e) {
								jprime.Console.err.printStackTrace(e);
							}
						}
					}
				}
			}
			if(doXML) {
				try {
					pn.xml.write(t1+"</communities>\n");
				} catch (IOException e) {
					jprime.Console.err.printStackTrace(e);
				}
			}
		}
		{
			if(DEBUG)jprime.Console.out.println("\tEncoding community forwarding entries!");
			if(doXML) {
				try {
					pn.xml.write(t1+"<commityForwardingEntries>\n");
				} catch (IOException e) {
					jprime.Console.err.printStackTrace(e);
				}
			}
			for(AlignmentForwardingEntry r : partitioning.getAlignmentForwardingEntries()) {
				String l = "";
				l+=makeTLV(TLVType.LONG,Long.toString(r.getSrcAlignId()).length(),Long.toString(r.getSrcAlignId()));
				l+=makeTLV(TLVType.LONG,Long.toString(r.getDstMinAlignId()).length(),Long.toString(r.getDstMinAlignId()));
				l+=makeTLV(TLVType.LONG,Long.toString(r.getDstMaxAlignId()).length(),Long.toString(r.getDstMaxAlignId()));
				l+=makeTLV(TLVType.LONG,Long.toString(r.getNextHop()).length(),Long.toString(r.getNextHop()));
				if(DEBUG)jprime.Console.out.println("\t\t"+r);
				writeTLV(pn,TLVType.LIST,l.length(),l);
				if(doXML) {
					try {
						pn.xml.write(t2+"<fwdEntry src=\""+r.getSrcAlignId()+"\""+
								" dstMin=\""+r.getDstMinAlignId()+"\""+
								" dstMax=\""+r.getDstMaxAlignId()+"\""+
								" nextHop=\""+r.getNextHop()+"\""+
						" />\n");
					} catch (IOException e) {
						jprime.Console.err.printStackTrace(e);
					}
				}
			}
			if(doXML) {
				try {
					pn.xml.write(t1+"</commityForwardingEntries>\n");
				} catch (IOException e) {
					jprime.Console.err.printStackTrace(e);
				}
			}
		}
		{
			if(DEBUG)jprime.Console.out.println("\tEncoding ip alignment map!");
			if(doXML) {
				try {
					pn.xml.write(t1+"<ipAlignments>\n");
				} catch (IOException e) {
					jprime.Console.err.printStackTrace(e);
				}
			}
			for(IPAlignment ipa : partitioning.getIPAlignments()) {
				String l = "";
				l+=makeTLV(TLVType.LONG,Long.toString(ipa.low).length(),Long.toString(ipa.low));
				l+=makeTLV(TLVType.LONG,Long.toString(ipa.high).length(),Long.toString(ipa.high));
				l+=makeTLV(TLVType.LONG,Long.toString(ipa.com_id).length(),Long.toString(ipa.com_id));
				if(DEBUG)jprime.Console.out.println("\t\t"+ipa);
				writeTLV(pn,TLVType.LIST,l.length(),l);
				if(doXML) {
					try {
						pn.xml.write(t2+"<align low=\""+IPAddressUtil.int2IP(ipa.low)+"\""+
								" high=\""+IPAddressUtil.int2IP(ipa.high)+"\""+
								" destCom=\""+ipa.com_id+"\""+
						" />\n");
					} catch (IOException e) {
						jprime.Console.err.printStackTrace(e);
					}
				}
			}
			if(doXML) {
				try {
					pn.xml.write(t1+"</ipAlignments>\n");
				} catch (IOException e) {
					jprime.Console.err.printStackTrace(e);
				}
			}
		}
		if(doXML) {
			try {
				pn.xml.write(tabs.peek()+"</partitionMap>\n");
			} catch (IOException e) {
				jprime.Console.err.printStackTrace(e);
			}
		}
	}


	/**
	 * @param st
	 * @throws IOException 
	 */
	private void encodeSymbolTable(Node pn) throws IOException {
		final SymbolTable st = partitioning.getTopnet().getMetadata().getSymbolTable();
		String temp = Integer.toString(st.getSymbolMap().size());
		temp=makeTLV(TLVType.LONG,temp.length(),temp);
		writeTLV(pn, TLVType.SYMBOL_TABLE,temp.length(),temp);
		if(doXML) pn.xml.write(tabs.peek()+"<symbolTable>\n");

		for(final String sym : st.getSymbolMap().keySet()) {
			if(doXML) pn.xml.write(tabs.peek()+"\t<"+sym+" />\n");
			writeTLV(pn, TLVType.SYMBOL_TABLE_ENTRY,sym.length(),sym);
		}
		if(doXML) pn.xml.write(tabs.peek()+"</symbolTable>\n");
	}
	
	private void encodeRouteEntries(Node pn, IRouteTable rt) {
		if(doXML) {
			try {
				pn.xml.write(tabs.peek()+TAB+"<!-- #route entries = "+rt.getPermRouteEntries().size()+" -->\n");
				/* uncomment to print out REs
				for(IRouteEntry re : rt.getPermRouteEntries()) {
					pn.xml.write(tabs.peek()+TAB+re.toString()+"\n");
				}*/
			} catch (IOException e) {
				jprime.Console.err.printStackTrace(e);
			}
		}
		String temp;
		String entry;
		for(IRouteEntry re : rt.getPermRouteEntries()) {
			//src min
			temp=Long.toString(re.getSrcMin());
			entry=makeTLV(TLVType.LONG,temp.length(),temp);

			//src max
			temp=Long.toString(re.getSrcMax());
			entry+=makeTLV(TLVType.LONG,temp.length(),temp);

			//dst min
			temp=Long.toString(re.getDstMin());
			entry+=makeTLV(TLVType.LONG,temp.length(),temp);

			//dst max
			temp=Long.toString(re.getDstMax());
			entry+=makeTLV(TLVType.LONG,temp.length(),temp);

			//[outboundiface]
			temp=Long.toString(re.getOutboundIface());
			entry+=makeTLV(TLVType.LONG,temp.length(),temp);

			//[owninghost]
			temp=Long.toString(re.getOwningHost());
			entry+=makeTLV(TLVType.LONG,temp.length(),temp);

			//[numberofbits]
			temp=Long.toString(re.getNumOfBits());
			entry+=makeTLV(TLVType.LONG,temp.length(),temp);

			//[nexthopid]
			temp=Long.toString(re.getNextHopId());
			entry+=makeTLV(TLVType.LONG,temp.length(),temp);

			//[edge_iface]
			if(re.getEdgeIface()) {
				temp="1";
			}
			else {
				temp="0";
			}
			entry+=makeTLV(TLVType.BOOL,temp.length(),temp);

			//[bus_idx]
			temp=Long.toString(re.getBusIdx());
			entry+=makeTLV(TLVType.LONG,temp.length(),temp);

			//[numofbitsbus]
			temp=Long.toString(re.getNumOfBitsBus());
			entry+=makeTLV(TLVType.LONG,temp.length(),temp);

			//[cost]
			temp=Long.toString(re.getCost());
			entry+=makeTLV(TLVType.LONG,temp.length(),temp);
			writeTLV(pn, TLVType.ROUTE_ENTRY,entry.length(),entry);
		}
	}

	/**
	 * encode an attribute
	 */
	private String encodeAttr(Node pn, TLVType t, int id, String value) {
		switch(t) {
		case LIST:
		case BOOL:
		case FLOAT:
		case LONG:
		case OPAQUE:
		case STRING:
		case RESOURCE_ID:
		case SYMBOL:
			if(doXML) {
				try {
					pn.xml.write(tabs.peek()+"<attr name=\""+ModelNodeVariable.int2name(id)+"\" >"+value+"</attr>\n");
				} catch (IOException e) {
					jprime.Console.err.printStackTrace(e);
				}
			}
			String attr=makeTLV(TLVType.LONG,Integer.toString(id).length(),Integer.toString(id));
			attr+= makeTLV(t,value.length(),value);
			return makeTLV(TLVType.PROPERTY,attr.length(),attr);
		default:
			throw new RuntimeException("Unknown expected type "+t);
		}
	}

	/**
	 * 
	 * The node should _not_ be a replica. It will go through and 
	 * 
	 * @param n
	 */
	private void encodeRealNode(Node pn, ModelNode n, boolean asRealNode) {
		if(DEBUG) {
			if(asRealNode)jprime.Console.out.println("\tEncoding replica as real version of ["+n.getClass().getSimpleName()+"]"+n.getUniqueName()+"("+n.getUID()+"), aligns="+n.getAlignments(partitioning));
			else jprime.Console.out.println("\tEncoding real version of ["+n.getClass().getSimpleName()+"]"+n.getUniqueName()+"("+n.getUID()+"), aligns="+n.getAlignments(partitioning));
		}
		if(doXML) {
			try {
				pn.xml.write(tabs.peek()+"<action function=\"encodeRealNode\" value=\""+todo.peek()[pn.idx]+"\" encode=\""+(asRealNode?"replicaAsReal":"real")+"\" />\n");
			} catch (IOException e) {
				jprime.Console.err.printStackTrace(e);
			}
		}

		//if(DEBUG && n instanceof IRouteTable)jprime.Console.out.println("\tNumRoutes="+((IRouteTable)n).getRouteEntries().size()+" isReplica="+n.isReplica());
		String temp;

		//uid
		temp=Long.toString(n.getUID());
		String model_node=makeTLV(TLVType.LONG,temp.length(),temp);

		//parent uid
		if(n.getParent()==null) {
			temp="0";
		}
		else {
			temp=Long.toString(n.getParent().getUID());
		}
		model_node+=makeTLV(TLVType.LONG,temp.length(),temp);

		//replica id
		temp="0";
		model_node+=makeTLV(TLVType.LONG,temp.length(),temp);

		//type
		temp=Integer.toString(n.getTypeId());
		model_node+=makeTLV(TLVType.LONG,temp.length(),temp);

		//attrs
		model_node+=encodeAttr(pn, TLVType.STRING,ModelNodeVariable.name(),n.getName());
		model_node+=encodeAttr(pn, TLVType.LONG,ModelNodeVariable.offset(),Long.toString(n.getOffset()));
		model_node+=encodeAttr(pn, TLVType.LONG,ModelNodeVariable.size(),Long.toString(n.getSize()));
		if(asRealNode) {
			TreeSet<Integer> encoded_attrs=new TreeSet<Integer>();
			List<ModelNode> to_encode = new ArrayList<ModelNode>();
			{
				ModelNode base_node = n;
				to_encode.add(base_node);
				while(base_node instanceof ModelNodeReplica) {
					base_node=((ModelNodeReplica)base_node).getReplicatedNode();
					to_encode.add(base_node);
				}
			}
			for(ModelNode base_node : to_encode) {
				for(ModelNodeVariable v : base_node.getAttributeValues()) {
					if(ModelNodeVariable.shouldEncodeAttr(v.getDBName())) {
						if(!encoded_attrs.contains(v.getDBName())) {
							encoded_attrs.add(v.getDBName());
							model_node+=v.encodeTLV();
							if(doXML) {
								try {
									pn.xml.write(tabs.peek()+"<attr name=\""+ModelNodeVariable.int2name(v.getDBName())+"\" >"+v.toString()+"</attr>\n");
								} catch (IOException e) {
									jprime.Console.err.printStackTrace(e);
								}
							}
						}
					}
				}
			}
		}
		else {
			for(ModelNodeVariable v : n.getAttributeValues()) {
				if(ModelNodeVariable.shouldEncodeAttr(v.getDBName())) {
					model_node+=v.encodeTLV();
					if(doXML) {
						try {
							pn.xml.write(tabs.peek()+"<attr name=\""+ModelNodeVariable.int2name(v.getDBName())+"\" >"+v.toString()+"</attr>\n");
						} catch (IOException e) {
							jprime.Console.err.printStackTrace(e);
						}
					}
				}
			}
		}
		if(n instanceof ITrafficType) {
			//encode the community id list
			temp=null;
			for(int ai : ((ITrafficType)n).getAlignments(this.partitioning)) {
				Alignment a= partitioning.findAlignment(ai);
				if(temp==null) temp="["+a.getAlignId();
				else temp+=","+a.getAlignId();
			}
			if(temp!=null) { 
				temp+="]";
			}
			else throw new RuntimeException("How did this happen");
			model_node+=encodeAttr(pn, TLVType.OPAQUE,ModelNodeVariable.community_ids(),temp);
			writeTLV(pn, TLVType.GENERIC_NODE,model_node.length(),model_node);
		}
		else if(n instanceof IRouteTable) {
			//# route entries
			IRouteTable rt = null;
			rt =(IRouteTable)n;
			temp=Integer.toString(rt.getPermRouteEntries().size());
			model_node+=makeTLV(TLVType.LONG,temp.length(),temp);
			writeTLV(pn, TLVType.ROUTE_TABLE,model_node.length(),model_node);
			//route entries
			encodeRouteEntries(pn, rt);
		}
		else {
			writeTLV(pn, TLVType.GENERIC_NODE,model_node.length(),model_node);
		}
	}
	
	/**
	 * @param n
	 * @param to_replicate
	 */
	private void encodeReplica(Node pn, ModelNodeReplica n, ModelNode to_replicate) {
		if(DEBUG)jprime.Console.out.println("\tEncoding replica version of ["+n.getClass().getSimpleName()+"]"+n.getUniqueName()+"("+n.getUID()+"), aligns="+n.getAlignments(partitioning));
		if(doXML) {
			try {
				pn.xml.write(tabs.peek()+"<action function=\"encodeReplica\" value=\""+todo.peek()[pn.idx]+"\" encode=\"replica\" />\n");
				pn.xml.write(tabs.peek()+"<toReplicate value=\""+to_replicate.getUniqueName()+"\" />\n");
			} catch (IOException e) {
				jprime.Console.err.printStackTrace(e);
			}
		}
		String temp;

		//uid
		temp=Long.toString(n.getUID());
		String model_node=makeTLV(TLVType.LONG,temp.length(),temp);

		//parent uid
		if(n.getParent()==null) {
			temp="0";
		}
		else {
			temp=Long.toString(n.getParent().getUID());
		}
		model_node+=makeTLV(TLVType.LONG,temp.length(),temp);

		//replica id
		temp=Long.toString(to_replicate.getUID());
		model_node+=makeTLV(TLVType.LONG,temp.length(),temp);

		//type
		temp=Integer.toString(n.getTypeId());
		model_node+=makeTLV(TLVType.LONG,temp.length(),temp);

		//attrs
		model_node+=encodeAttr(pn, TLVType.STRING,ModelNodeVariable.name(),n.getName());
		model_node+=encodeAttr(pn, TLVType.LONG,ModelNodeVariable.offset(),Long.toString(n.getOffset()));
		model_node+=encodeAttr(pn, TLVType.LONG,ModelNodeVariable.size(),Long.toString(n.getSize()));
		for(ModelNodeVariable v : n.getAttributeValues()) {
			model_node+=v.encodeTLV();
			if(doXML) {
				try {
					pn.xml.write(tabs.peek()+"<attr name=\""+ModelNodeVariable.int2name(v.getDBName())+"\" >"+v.toString()+"</attr>\n");
				} catch (IOException e) {
					jprime.Console.err.printStackTrace(e);
				}
			}
		}
		if(n instanceof ITrafficType) {
			//encode the community id list
			temp=null;
			for(int ai : ((ITrafficType)n).getAlignments(this.partitioning)) {
				Alignment a= partitioning.findAlignment(ai);
				if(temp==null) temp="["+a.getAlignId();
				else temp+=","+a.getAlignId();
			}
			if(temp!=null) { 
				temp+="]";
			}
			else throw new RuntimeException("How did this happen");
			model_node+=encodeAttr(pn, TLVType.OPAQUE,ModelNodeVariable.community_ids(),temp);
		}
		writeTLV(pn, TLVType.GENERIC_NODE,model_node.length(),model_node);
	}

	/**
	 * @param n
	 */
	private void encodeGhost(Node pn, ModelNode n, ModelNode ghost) {
		if(DEBUG)jprime.Console.out.println("\tEncoding ghost of ["+ghost.getClass().getSimpleName()+"]"+ghost.getUniqueName()+"("+ghost.getUID()+")"+", aligns="+ghost.getAlignments(partitioning)+" at "+n.getUniqueName());
		if(doXML) {
			try {
				pn.xml.write(tabs.peek()+"<action function=\"encodeGhost\" value=\""+todo.peek()[pn.idx]+"\" encode=\"ghost\" />\n");
			} catch (IOException e) {
				jprime.Console.err.printStackTrace(e);
			}
		}
		String temp;

		//uid
		temp=Long.toString(n.getUID());
		String model_node=makeTLV(TLVType.LONG,temp.length(),temp);

		//parent uid
		if(n.getParent()==null) {
			temp="0";
		}
		else {
			temp=Long.toString(n.getParent().getUID());
		}
		model_node+=makeTLV(TLVType.LONG,temp.length(),temp);

		//replica id
		temp="0";
		model_node+=makeTLV(TLVType.LONG,temp.length(),temp);

		{
			if(ghost instanceof IInterface) {
				//type
				temp=Integer.toString(EntityFactory.GhostInterface);
				model_node+=makeTLV(TLVType.LONG,temp.length(),temp);

				//special attrs
				//real uid
				temp=Long.toString(ghost.getUID());
				model_node+=encodeAttr(pn, TLVType.LONG,ModelNodeVariable.real_uid(),temp);

				//[remote communituy id]
				Set<Integer> rv = ghost.getAlignments(partitioning);
				if(rv.size()!=1) {
					throw new RuntimeException("Unexpected number of alignments for "+ghost.getUniqueName()+", alignments="+rv);
				}
				Alignment a= partitioning.findAlignment(rv.iterator().next());
				temp=Long.toString(a.getAlignId());
				model_node+=encodeAttr(pn, TLVType.LONG,ModelNodeVariable.community_id(),temp);

				//need the ip and mac
				if(ghost instanceof IBaseInterface) {
					model_node+=encodeAttr(pn, TLVType.STRING,ModelNodeVariable.ip_address(),((IBaseInterface)ghost).getIpAddress().getValue());
				}			
			}
			else if(ghost instanceof IRoutingSphere) {
				//type
				temp=Integer.toString(EntityFactory.GhostRoutingSphere);
				model_node+=makeTLV(TLVType.LONG,temp.length(),temp);

				//[remote communituy id]
				Set<Integer> rv = ghost.getAlignments(partitioning);
				if(rv.size()==0) {
					throw new RuntimeException("Unexpected number of alignments for "+ghost.getUniqueName()+", alignments="+rv);
				}
				Alignment a= partitioning.findAlignment(rv.iterator().next());
				temp=Long.toString(a.getAlignId());
				model_node+=encodeAttr(pn, TLVType.LONG,ModelNodeVariable.community_id(),temp);
			}
			else {
				throw new RuntimeException("We can't ghost nodes of type "+n.getClass().getSimpleName());
			}
		}
		{
			//standard attrs
			model_node+=encodeAttr(pn, TLVType.STRING,ModelNodeVariable.name(),n.getName());
			model_node+=encodeAttr(pn, TLVType.LONG,ModelNodeVariable.offset(),Long.toString(n.getOffset()));
			model_node+=encodeAttr(pn, TLVType.LONG,ModelNodeVariable.size(),Long.toString(n.getSize()));

		}
		writeTLV(pn, TLVType.GHOST_NODE,model_node.length(),model_node);
	}

	/**
	 * @param n
	 */
	private void encodePlaceHolder(Node pn, ModelNode n, ModelNode ghost) {
		if(DEBUG)jprime.Console.out.println("\tEncoding ghost of ["+ghost.getClass().getSimpleName()+"]"+ghost.getUniqueName()+"("+ghost.getUID()+")"+", aligns="+ghost.getAlignments(partitioning)+" at "+n.getUniqueName());
		if(doXML) {
			try {
				pn.xml.write(tabs.peek()+"<action function=\"encodePlaceHolder\" value=\""+todo.peek()[pn.idx]+"\" encode=\"placeHolder\" />\n");
			} catch (IOException e) {
				jprime.Console.err.printStackTrace(e);
			}
		}
		String temp;

		//uid
		temp=Long.toString(n.getUID());
		String model_node=makeTLV(TLVType.LONG,temp.length(),temp);

		//parent uid
		if(n.getParent()==null) {
			temp="0";
		}
		else {
			temp=Long.toString(n.getParent().getUID());
		}
		model_node+=makeTLV(TLVType.LONG,temp.length(),temp);

		//replica id
		temp="0";
		model_node+=makeTLV(TLVType.LONG,temp.length(),temp);

		//type
		temp=Integer.toString(EntityFactory.PlaceHolder);
		model_node+=makeTLV(TLVType.LONG,temp.length(),temp);

		//attrs
		model_node+=encodeAttr(pn, TLVType.STRING,ModelNodeVariable.name(),n.getName());
		model_node+=encodeAttr(pn, TLVType.LONG,ModelNodeVariable.offset(),Long.toString(n.getOffset()));
		model_node+=encodeAttr(pn, TLVType.LONG,ModelNodeVariable.size(),Long.toString(n.getSize()));

		writeTLV(pn, TLVType.GENERIC_NODE,model_node.length(),model_node);
	}

}
