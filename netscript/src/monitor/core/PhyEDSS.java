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

import java.net.InetSocketAddress;

import monitor.commands.AbstractCmd;
import monitor.commands.CodecFactory;
import monitor.commands.UpdateBytesFromAppCmd;
import monitor.util.Utils;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

/**
 * @author Miguel Erazol Erazo
 *
 * This class allows the physical system to collect and
 * send traffic-related data to the simulation which may be
 * running in a PrimoGENI or remote-cluster environment
 */

public class PhyEDSS extends SymbioEDSS implements SymbioEDSSIntf
{
	/**
	 * @param args
	 */

	private final PhyEDSSender sender;
	private final PhyEDSSSessionHandler handler;
	
	PhyEDSS (String master_ip) {
		super(master_ip);
		masterIP = master_ip;
		collector = new PhyEDSSCollector(this);
		sender = new PhyEDSSender();
		//master_handler = new 
		connector = null;
		masterSession = null;
		handler = new PhyEDSSSessionHandler(this);
	}
	
	public boolean connect() {
		//Connect to master and return the result of the attempt
		if(this.masterSession == null) {
			//Verify that masterIP is not null or empty
			if(masterIP == null || masterIP.length() == 0) {
				throw new RuntimeException("Invalid master ip!");
			}
			// Socket for sending to other nodes
			this.connector = new NioSocketConnector();
			
			// Configure the service
			connector.getFilterChain().addLast(
					"codec", new ProtocolCodecFilter(new CodecFactory()));
			
			// Set handler for the connector socket
			connector.setHandler(this.handler);
			
			// Connect to master
			System.out.println("Connecting to master at " + masterIP + ":" + Utils.MASTER_PORT);
			ConnectFuture future = connector.connect(new InetSocketAddress(
					masterIP, Utils.MASTER_PORT));
			future.awaitUninterruptibly();
			this.masterSession = future.getSession();
			System.out.println("Connected to master! connected=" + this.masterSession.isConnected());
			
		} else {
			throw new RuntimeException("Already connected to master!");
		}
		
		// Socket for sending to other nodes
		this.connector = new NioSocketConnector();
		
		return true;
	}
	
	public boolean start() throws InterruptedException {
		//Connect to master
		connect();
		
		//Start the collector
		collector.start();
		
		//Start the sender
		sender.start();
		
		return true;
	}
	
	public boolean sendCommand(AbstractCmd cmd) {
		cmd.setSerialNumber(Utils.nextSerial());
		//XXX Insert this command to outstandingBlockingCommands structure? (check the 'controller' code)
		masterSession.write(cmd);
		return true;
	}
	
	//public void getBytesTransmitted(int bytes_from_app, String ip) {
	public void getDataFromSystems(String update, String source) {
		int bytes_from_app = Integer.parseInt(update);
		String ip = source;
		System.out.println("\tReceived: " + bytes_from_app + " from IP: " + ip);
		//Create an updateFromBytesCmd
		//XXX we will not use here the IP reported from the app, instead we will find out the private IP
		//	assigned in Emulab assuming only one application lives in this physical machine...will we use
		//	virtual machines in the future in the physical system?
		String emulab_private_ip_interface = null;
		
		for(String phy_ip: this.getInterfacesIPs()){
			if(phy_ip.split("\\.")[0].equals("10")){
				System.out.println("\tEmulab's interface:" + phy_ip);
				emulab_private_ip_interface = phy_ip;
			}
		}
		
		if(emulab_private_ip_interface == null)
			throw new RuntimeException("Could not get host's interface ip");
		
		UpdateBytesFromAppCmd update_cmd = new UpdateBytesFromAppCmd(bytes_from_app, emulab_private_ip_interface);
		
		//Send the new command to master using the mastersession
		sendCommand(update_cmd);
	}
	
	public static void main(String[] args)
	{
		System.out.println("master IP:" + args[0]);
		System.out.flush();
		String master_ip = args[0];
		PhyEDSS phy_edss = new PhyEDSS(master_ip);
		try {
			phy_edss.start();
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new RuntimeException("Could not start services");
		}
		return;
	}	
}
