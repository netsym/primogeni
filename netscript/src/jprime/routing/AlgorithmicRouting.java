package jprime.routing;

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

import jprime.EntityFactory;
import jprime.IModelNode;
import jprime.ModelNodeRecord;
import jprime.Net.INet;

/**
 * @author Nathanael Van Vorst
 * @author Ting Li
 */
public class AlgorithmicRouting extends StaticRoutingProtocol {
	/**
	 * Used to load from db
	 */
	public AlgorithmicRouting(ModelNodeRecord rec) {
		super(rec.meta,rec.dbid,EntityFactory.BGP,rec.db_parent_id);
	}
	
	/**
	 * @param parent
	 */
	public AlgorithmicRouting(INet parent) {
		super(parent,EntityFactory.AlgorithmicRouting);
	}
	
	/* (non-Javadoc)
	 * @see jprime.routing.StaticRoutingProtocol#isASLevelProtocol()
	 */
	public boolean isASLevelProtocol() {
		return false;
	}
	
	/* (non-Javadoc)
	 * @see jprime.routing.StaticRoutingProtocol#isAlgorithmicProtocol()
	 */
	public boolean isAlgorithmicProtocol() {
		return true;
	}
	
	/* (non-Javadoc)
	 * @see jprime.routing.StaticRoutingProtocol#getRouteVisitor(jprime.Net.Net)
	 */
	public IRouteVisitor getRouteVisitor(INet net) {
		return new AlgorithmicRoutingVisitor(net);
	}
	
	/* (non-Javadoc)
	 * @see jprime.routing.StaticRoutingProtocol#deepCopy(jprime.Net.INet)
	 */
	public StaticRoutingProtocol deepCopy(INet parent) {
		return new AlgorithmicRouting(parent);
	}
	
	public IModelNode __copy(String name, IModelNode toCopy, IModelNode parent) {
		return new AlgorithmicRouting((INet)parent);
	}
	
	public int getTypeId() {
		return EntityFactory.AlgorithmicRouting;
	}


}
