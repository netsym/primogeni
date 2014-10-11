package slingshot.environment;

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
 * An enumeration of all the types of environments we can deploy exerpiments to.
 * 
 * @author Nathanael Van Vorst
 */
public enum EnvType {
	/**
	 * A Linux/Unix cluster with MPI and the Meta-Controller 
	 */
	REMOTE_CLUSTER("Remote Cluster"),
	
	/**
	 * For use with generic ProtoGENI manifest
	 */
//	/PROTO_GENI("ProtoGENI"),
	PROTO_GENI("Geni Slices"),
	/**
	 * Use local sim -- restricted functionality
	 */
	LOCAL("Local Simulator"),	
	;
	public final String str;
	private EnvType(String str) {
		this.str=str;
	}
	private final static HashMap<String,EnvType> types=new HashMap<String, EnvType>();
	public static final String[] strings;
	static {
		strings = new String[values().length];
		int i=0;
		for(EnvType e : values()) {
			types.put(e.str,e);
			strings[i]=e.str;
			i++;
		}
	}
	/**
	 * Get environment enum from its string name.
	 * 
	 * @param str
	 * @return
	 */
	public static EnvType fromString(String str) {
		EnvType rv = types.get(str);
		if(rv == null) {
			throw new RuntimeException("Invalid type:"+str);
		}
		return rv;
	}
}