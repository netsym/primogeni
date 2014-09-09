package jprime.RoutingSphere;

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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import jprime.IModelNode;
import jprime.Interface.IInterface;

/**
 * @author Nathanael Van Vorst
 *
 */
public class EdgeInterfacePair implements Serializable{
	private static final long serialVersionUID = 475917277673061113L;
	/*
	 * For the below calculations, recall that:
	 * 
	 * old: rank(N,A) = uid(N)-uid(A)+size(A)+offset(A)
	 * rank(N,A) = uid(N)-uid(A)+size(A)
	 * 
	 * which implies
	 * 
	 * old: uid(N) = rank(N,A)+uid(A)-size(A)-offset(A)
	 * uid(N) = rank(N,A)+uid(A)-size(A)
	 * 
	 */
	public final long iface_min_rank;
	public final long iface_max_rank;
	public final long host_min_rank;
	public final long host_max_rank;
	public EdgeInterfacePair(IInterface iface, IModelNode anchor) {
		iface_min_rank=iface.getMinRank(anchor);
		iface_max_rank=iface.getMaxRank(anchor);
		host_min_rank=iface.getParent().getMinRank(anchor);
		host_max_rank=iface.getParent().getMaxRank(anchor);
	}
	public EdgeInterfacePair(EdgeInterfacePair o, IModelNode anchor, long old_diff) {
		//jprime.Console.out.println("orig anchor="+o.orig_anchor.getUniqueName());
		//jprime.Console.out.println("anchor="+anchor.getUniqueName());
		//long old_diff = o.orig_anchor.getUID()-o.orig_anchor.getSize();
		long new_diff = anchor.getUID()-anchor.getSize();
		//jprime.Console.out.println("old_diff="+old_diff+", new_diff="+new_diff);
		long iface_min_uid=o.iface_min_rank+old_diff;
		long iface_max_uid=o.iface_max_rank+old_diff;
		//jprime.Console.out.println("iface_min_uid="+iface_min_uid+", iface_max_uid="+iface_max_uid);
		long host_min_uid=o.host_min_rank+old_diff;
		long host_max_uid=o.host_max_rank+old_diff;
		iface_min_rank=iface_min_uid-new_diff;
		iface_max_rank=iface_max_uid-new_diff;
		host_min_rank=host_min_uid-new_diff;
		host_max_rank=host_max_uid-new_diff;
	}
	public long getIfaceUID(IModelNode anchor) {
		//return iface_max_rank+anchor.getUID()-anchor.getSize()-anchor.getOffset();
		return iface_max_rank+anchor.getUID()-anchor.getSize();
	}
	public long getHostUID(IModelNode anchor) {
		//return host_max_rank+anchor.getUID()-anchor.getSize()-anchor.getOffset();
		return host_max_rank+anchor.getUID()-anchor.getSize();
	}
	public long getIfaceMaxRank(IModelNode anchor, IModelNode newAnchor) {
		//jprime.Console.out.println("origAnchor="+orig_anchor.getUniqueName()+", getIfaceUID(orig_anchor)="+getIfaceUID(orig_anchor)+", iface_max_rank="+iface_max_rank);
		//jprime.Console.out.println("anchor="+anchor.getUniqueName()+", getIfaceUID(anchor)="+getIfaceUID(anchor)+", iface_max_rank="+iface_max_rank);
		//jprime.Console.out.println("newAnchor="+newAnchor.getUniqueName());
		return getRank(getIfaceUID(anchor),newAnchor);
	}		
	public long getRank(long node_uid, IModelNode anchor) {
		//long rank = node_uid-anchor.getUID()+anchor.getSize()+anchor.getOffset();
		long rank = node_uid-anchor.getUID()+anchor.getSize();
		//jprime.Console.out.println("anchor uid="+anchor.getUID()+", anchor size="+anchor.getSize()+", anchor offset="+anchor.getOffset());
		//jprime.Console.out.println("node="+node_uid+", anchor="+anchor.getUniqueName()+", orig_anchor="+orig_anchor.getUniqueName());
		//jprime.Console.out.println("rank="+rank+", max_rank="+iface_max_rank);
		if(rank>0) {
			return rank;
		}
		throw new RuntimeException("Tried to get the rank of a node using an anchor which was not an ancestor! node="+node_uid
				+", anchor="+anchor.getUniqueName()+"["+anchor.getMinUID()+","+anchor.getUID()+"]");
	}

	public static String toBytes(List<EdgeInterfacePair> l) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(l.size()*4) ;
		ObjectOutput out;
		try {
			out = new ObjectOutputStream(bos);
			out.writeObject(l);
			out.close();
		} catch (IOException e) {
			jprime.Console.err.printStackTrace(e);
			jprime.Console.halt(100);
		}
		final String rv = new sun.misc.BASE64Encoder().encode(bos.toByteArray());
		return rv;
	}

	@SuppressWarnings("unchecked")
	public static List<EdgeInterfacePair> fromBytes(String str) {
		List<EdgeInterfacePair> rv = null;
		try {
			byte[] b= new sun.misc.BASE64Decoder().decodeBuffer(str);
			if(b.length>0) {
				ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(b));
				rv = (LinkedList<EdgeInterfacePair>) in.readObject();
				in.close();
			}
			else {
				rv = new LinkedList<EdgeInterfacePair>();
			}
		} catch (Exception e) {
			jprime.Console.err.printStackTrace(e);
			//jprime.Console.halt(100);
			rv = new LinkedList<EdgeInterfacePair>();
		}
		return rv;
	}
}