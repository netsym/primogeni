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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import jprime.EntityFactory;
import jprime.IModelNode;
import jprime.ModelNode;
import jprime.BaseInterface.IBaseInterfaceAlias;
import jprime.Interface.IInterface;
import jprime.Link.ILink;
import jprime.Net.INet;
import jprime.Net.Net;
import jprime.Router.IRouter;
import jprime.gen.ModelNodeVariable;
import jprime.util.ChildList;
import jprime.util.IPAddressUtil;
import jprime.util.PersistentChildList;

/**
 * @author Nathanael Van Vorst
 * 
 * Assign IPAddresses
 */
public class IPAddressAssignment {
	public static class IPPrefixRoute {
		public final String prefix;
		public final long uid, iface_id;
		public final String ip;
		public IPPrefixRoute(String prefix, long uid, long iface_id, String ip) {
			super();
			this.prefix = prefix;
			this.uid = uid;
			this.iface_id = iface_id;
			this.ip = ip;
		}
	}
	private LinkedList<IPPrefixRoute> portal_rules = new LinkedList<IPAddressAssignment.IPPrefixRoute>();
	private long cur_address;
	private final IPAddressUtil.ReverseComparator rvc = new IPAddressUtil.ReverseComparator();

	/**
	 * @param topnet
	 */
	public IPAddressAssignment(Net topnet, Map<String,String> params) {
		if(params.containsKey("base_ip_address")) {
			String p = params.remove("base_ip_address");
			if(p.contains("/")) {
				cur_address=IPAddressUtil.cidr2Int(p);
			}
			else {
				cur_address=IPAddressUtil.ip2Int(p);
			}
		}
		else {
			cur_address=IPAddressUtil.ip2Int("192.168.0.0");
		}
		topnet.accept(this);
		if(portal_rules.size() >0 ) {
			String str = null;
			for( IPPrefixRoute r: portal_rules) {
				if(str == null)
					str="[";
				else
					str+=",";
				str+=r.prefix+","+r.uid+","+r.iface_id+","+r.ip;
			}
			if(str==null)
				str="[]";
			else
				str+="]";
			try {
				topnet.setAttribute(ModelNodeVariable.portal_rules(), str);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * @param node
	 */
	public void visit(ModelNode node) {
		if(node instanceof ILink  && !node.isAlias()) {
			__visit__link((ILink) node);
		}
		else if(node instanceof INet && !node.isAlias()) {
			__visit__net((INet)node);
		}
	}
	/**
	 * @param node
	 */
	private void __visit__link(ILink node) {
		long newaddr = cur_address+(long)(Math.pow(2,node.getIpPrefixLen().getValue()))-1;
		long new_ip = newaddr&IPAddressUtil.cidrs[((int)node.getIpPrefixLen().getValue())];
		node.setIpPrefixLen(32-node.getIpPrefixLen().getValue());
		node.setIpPrefix(IPAddressUtil.int2IP(new_ip)+"/"+node.getIpPrefixLen());

		//System.out.println(Long.toBinaryString(new_ip)+" :: "+(32-node.getIpPrefixLen().getValue())+" :: "+node.getIpPrefix()+" :: "+node.getUniqueName());
		//System.out.println(Long.toBinaryString(IPAddressUtil.cidrs[32-(int)node.getIpPrefixLen().getValue()]));

		ChildList<IBaseInterfaceAlias>.ChildIterator nics = node.getAttachments().enumerate();
		while(nics.hasNext()) {
			cur_address++;
			//check if last byte is 0....
			if((cur_address&0x000000ff) == 0x0) {
				cur_address++;
			}
			final IBaseInterfaceAlias nic = nics.next();
			nic.setIpAddress(IPAddressUtil.int2IP(cur_address));
			//System.out.println(Long.toBinaryString(cur_address)+" :: "+nic.deference().getUniqueName());
		}
		cur_address=newaddr+1;
	}

	/**
	 * @param node
	 */
	private void __visit__net(INet node) {
		PersistentChildList cl = node.getPersistentChildList();
		long newaddr = cur_address+(long)Math.pow(2,node.getIpPrefixLen().getValue())-1;
		long new_ip = newaddr&IPAddressUtil.cidrs[((int)node.getIpPrefixLen().getValue())];
		node.setIpPrefixLen(32-node.getIpPrefixLen().getValue());
		node.setIpPrefix(IPAddressUtil.int2IP(new_ip)+"/"+node.getIpPrefixLen());

		//System.out.println("\n"+Long.toBinaryString(new_ip)+" :: "+(32-node.getIpPrefixLen().getValue())+" :: "+node.getIpPrefix()+" :: "+node.getUniqueName());
		//System.out.println(Long.toBinaryString(IPAddressUtil.cidrs[32-(int)node.getIpPrefixLen().getValue()]));

		SortedMap<Long, List<IModelNode>> nodes = new TreeMap<Long, List<IModelNode>>(rvc);


		for(int i =0; i<cl.size();i++) {
			if(EntityFactory.isSubType(EntityFactory.Link, cl.getTypeAt(i))) {
				if(!EntityFactory.isSubType(EntityFactory.LinkAlias, cl.getTypeAt(i))) {
					ILink c = (ILink)cl.get(i);
					//jprime.Console.out.println("getIpPrefixLen dbid:"+c.getDBID()+",uid= "+c.getUID()+", len="+c.getIpPrefixLen());
					if(!nodes.containsKey(c.getIpPrefixLen().getValue())) {
						nodes.put(c.getIpPrefixLen().getValue(),new ArrayList<IModelNode>());
					}
					nodes.get(c.getIpPrefixLen().getValue()).add(c);
				}
			}
			else if(EntityFactory.isSubType(EntityFactory.Net, cl.getTypeAt(i))) {
				if(!EntityFactory.isSubType(EntityFactory.NetAlias, cl.getTypeAt(i))) {
					INet n = (INet)cl.get(i);
					//jprime.Console.out.println("getIpPrefixLen dbid:"+c.getDBID()+",uid= "+c.getUID()+", len="+((INet)c).getIpPrefixLen());
					if(!nodes.containsKey(n.getIpPrefixLen().getValue())) {
						nodes.put(n.getIpPrefixLen().getValue(),new ArrayList<IModelNode>());
					}
					nodes.get(n.getIpPrefixLen().getValue()).add(n);
				}
			}
			else if(EntityFactory.isSubType(EntityFactory.Router, cl.getTypeAt(i))) {
				if(!EntityFactory.isSubType(EntityFactory.RouterAlias, cl.getTypeAt(i))) {
					IRouter r = (IRouter)cl.get(i);
					if(r.isTrafficPortal()) {
						PersistentChildList rl = r.getPersistentChildList();
						for(int j =0; j<rl.size();j++) {
							if(EntityFactory.isSubType(EntityFactory.Interface, rl.getTypeAt(j))) {
								IInterface ri = (IInterface)rl.get(j);
								if(ri.isTrafficPortal()) {
									for(String prefix : ri.getReachableNetworks()) {
										if(ri.getIpAddress() == null) {
											throw new RuntimeException("The traffic portal "+ri.getUniqueName()+" did not have an ip set!");
										}
										portal_rules.add(new IPPrefixRoute(prefix, r.getUID(), ri.getUID(), ri.getIpAddress().toString()));
									}
								}
							}
						}
					}
				}
			}
		}

		for(List<IModelNode> l : nodes.values()) {
			for(IModelNode n : l)
				n.accept(this);
		}
		cur_address=newaddr+1;

	}
}
