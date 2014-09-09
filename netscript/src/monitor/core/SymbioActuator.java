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

import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.GeneralSecurityException;

import monitor.commands.CodecFactory;
import monitor.util.Utils;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

/**
 * @author Miguel Erazol Erazo
 *
 */
public class SymbioActuator extends SymbioEDSS{
	private final ActuatorSessionHandler handler;
	
	SymbioActuator(String master_ip){
		super(master_ip);
		handler = new ActuatorSessionHandler(this);
	}
	
	private void start() {
		//Connect to master
		connect();
	}
	
	private boolean connect() {
		//Connect to master and return the result of the attempt
		if(this.masterSession == null) {
			//Verify that masterIP is not null or empty
			if(masterIP == null || masterIP.length() == 0) {
				throw new RuntimeException("Invalid master ip!");
			}
			// Socket for sending to other nodes
			connector = new NioSocketConnector();
			
			// Configure the service
			connector.getFilterChain().addLast(
					"codec", new ProtocolCodecFilter(new CodecFactory()));
			
			// Set handler for the connector socket
			connector.setHandler(this.handler);
			
			// Connect to master
			System.out.println("Connecting to master at " + masterIP + ":" + Utils.SYMBIO_ACTUATOR_PORT);
			ConnectFuture future = connector.connect(new InetSocketAddress(
					masterIP, Utils.SYMBIO_ACTUATOR_PORT));
			future.awaitUninterruptibly();
			this.masterSession = future.getSession();
			System.out.println("SimEDSS Connected to master! connected=" + this.masterSession.isConnected());
		} else {
			throw new RuntimeException("Already connected to master!");
		}
		
		// Socket for sending to other nodes
		this.connector = new NioSocketConnector();
		
		return true;
	}

	public static void main(String[] args) throws IOException, GeneralSecurityException {
		String master_ip = args[0];
		SymbioActuator actuator = new SymbioActuator(master_ip);
		actuator.start();
		return;
	}	
}
