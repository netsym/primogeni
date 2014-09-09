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

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Vector;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

/**
 * @author Miguel Erazol Erazo
 *
 * abstract class that implements basic functionality
 * of a EDSS
 */
public abstract class SymbioEDSS {
	 //The master is the process that interacts directly with either the Phy or the Simulators
	protected String masterIP;
	protected SymbioCollector collector;
	public IoSession masterSession;
	public NioSocketConnector connector;
	
	SymbioEDSS(String master_ip){
		masterIP = master_ip;
	}
	
	protected final Vector<String> getInterfacesIPs() {
		Vector<String> ips = new Vector<String>();
		try {
			Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
	
			while(e.hasMoreElements()) {
				NetworkInterface net_intf = (NetworkInterface)e.nextElement();
	            System.out.println("Net interface: " + net_intf.getName());
	
	            Enumeration<InetAddress> e2 = net_intf.getInetAddresses();
	
	            while (e2.hasMoreElements()){
	               InetAddress ip = (InetAddress) e2.nextElement();
	               System.out.println("IP address: "+ ip.toString());
	               String[] bytes = ip.toString().split("\\.");
	               if(bytes.length > 1) {
	            	   //This is an IP address and not a MAC
	            	   ips.add(ip.toString().substring(1));
	               }
	            }
			}
		}
		catch (Exception e) {
			throw new RuntimeException("Unable to get IP addresses of this host");
		}
		return ips;
	}
}