package jprime.util;

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

import jprime.IModelNode;
import jprime.ModelNode;

/**
 * @author Nathanael Van Vorst
 * 
 * Represent the fully qualified name of a node
 *
 */
public class UniqueName {
	private final IModelNode node;
	public UniqueName(IModelNode node) {
		this.node=node;
	}
	public String toString() {
		String rv =null;
		IModelNode cur=node,p=null;
		while(null!=cur) {
			String n = cur.getName();
			if(n==null)n="[null]";
			if(null==rv) {
				rv=n;
			}
			else {
				rv = n+":"+rv;
			}
			p=cur.getParent_nofetch();
			if(p==null && ((ModelNode)cur).getParentId()!=0) {
				rv = "[not loaded!]"+":"+rv;				
			}
			cur=p;
		}
		return rv;
	}
}
