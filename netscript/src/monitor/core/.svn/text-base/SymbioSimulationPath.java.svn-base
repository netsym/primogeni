package monitor.core;

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
 * @author Miguel Erazol Erazo
 * This class models a path in the symbiotic simulation context
 */
public class SymbioSimulationPath {
	public class LinkStaticProperties {
		public LinkStaticProperties(double tx_rate, double propagation_delay) {
			this.txRate = tx_rate;
			this.propagationDelay = propagation_delay;
		}
		private final double txRate;
		private final double propagationDelay;
		public double getTxRate() { return txRate; }
		public double getPropdelay() { return propagationDelay; }
	}
	
	private final HashMap<Integer, LinkStaticProperties> uid_txrate_propdelay;
	private final int index;
	private final String dummyNetMachineIP;
	
	SymbioSimulationPath(int index, String ip){
		this.index = index;
		this.dummyNetMachineIP = ip;
		uid_txrate_propdelay = new HashMap<Integer, LinkStaticProperties>();
	}
	
	public void addLink(int uid, double tx_rate, double propagation_delay){
		uid_txrate_propdelay.put(new Integer(uid), new LinkStaticProperties(tx_rate, propagation_delay));
	}
	
	public HashMap<Integer, LinkStaticProperties> getUidTxratePropDelay(){
		return uid_txrate_propdelay;
	}
	
	public String getMachineIP(){
		return dummyNetMachineIP;
	}
	
	public int getPathIndex(){
		return index;
	}
}
