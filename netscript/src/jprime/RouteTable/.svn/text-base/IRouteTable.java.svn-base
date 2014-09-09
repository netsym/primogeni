package jprime.RouteTable;

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


import java.util.LinkedList;

import jprime.routing.RouteEntry;
import jprime.routing.TempRouteEntry;
import jprime.util.PersistentRouteEntryList;

/**
 * @author Nathanael Van Vorst
 *
 */
public interface IRouteTable extends jprime.gen.IRouteTable {

	/**
	 * @return
	 */
	public PersistentRouteEntryList getPermRouteEntries();
	
	/**
	 * @return
	 */
	public LinkedList<TempRouteEntry> getTempRouteEntries();
	
	/**
	 * @return
	 */
	public RouteEntry lookupPermRoute(long srcMin, long srcMax, long dstMin, long dstMax);
	
	/**
	 * 
	 */
	public void clearTempRoutes();
	
	/**
	 * If this is a replica we need to this routing table real
	 */
	public void makeReal();
	
}
