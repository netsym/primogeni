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

import java.util.HashMap;


/**
 * @author Nathanael Van VorstTing Li
 */
public enum BGPRelationShipType{
	EXTRA(1),
	S2SDOWN(2),
	C2P(3),	
	P2P(4),
	P2C(5),
	S2SUP(6);
	
	private static final HashMap<Integer, BGPRelationShipType> __map__ = new HashMap<Integer, BGPRelationShipType>();
	private final int val;
	BGPRelationShipType(int v) { this.val=v;}
	public final int getTypeId() {return val;}
	
	public static BGPRelationShipType fromInt(Integer v) {
		if(__map__.size()==0) {
			for(BGPRelationShipType i : BGPRelationShipType.values()) {
				__map__.put(i.val,i);
			}
		}
		return __map__.get(v);
	}
	
}
