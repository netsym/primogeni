package jprime.ICMPTraffic;

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
import jprime.ModelNodeRecord;

import org.python.core.PyObject;

/**
 * @author Nathanael Van Vorst
 *
 */
public class ICMPTrafficAlias extends jprime.gen.ICMPTrafficAlias implements jprime.ICMPTraffic.IICMPTrafficAlias {
	public ICMPTrafficAlias(PyObject[] v, String[] n){super(v,n);}
	public ICMPTrafficAlias(ModelNodeRecord rec){ super(rec); }
	public ICMPTrafficAlias(IModelNode parent, jprime.ICMPTraffic.IICMPTraffic referencedNode) {
		super(parent,(jprime.ICMPTraffic.IICMPTraffic)referencedNode);
	}
	public ICMPTrafficAlias(IModelNode parent){
		super(parent);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.ICMPTrafficAlias; }
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specifIc code here (if any)
}
