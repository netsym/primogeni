package monitor.util;

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

import jprime.Experiment;
import jprime.database.Database;
import jprime.variable.Dataset;
import monitor.commands.CodecFactory;
import monitor.commands.ComPartAdvertCmd;
import monitor.commands.PrimeStateExchangeCmd;
import monitor.commands.VarUpdate;
import monitor.core.IPrimeStateListener;
import monitor.core.PrimeSessionHandler;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;


/**
 * @author Nathanael Van Vorst
 *
 * This class is used for testing...
 * 
 * it listens for state updates from the simulator (which is expected to not be connect to a controller)
 */
public class PrimeStateServer implements IPrimeStateListener {
	private final int prime_port;
	private final PrimeSessionHandler prime_handler;
	private final Dataset dataset;
	private final Experiment exp;
	private NioSocketAcceptor prime_acceptor;
	
	private PrimeStateServer(int prime_port, String expname) {
		if(expname != null) {
			Database db = Database.createDatabase();
			this.exp = db.loadExperiment(expname);
			if(exp == null) {
				throw new RuntimeException("couldn't find the exp by the name "+expname);
			}
			this.dataset=new Dataset();
		}
		else {
			this.exp=null;
			this.dataset=null;
		}
		this.prime_port=prime_port;
		this.prime_handler=new PrimeSessionHandler(this);
		this.prime_acceptor = null;
		
	}
	

	private synchronized void listen() throws IOException, GeneralSecurityException {
		if(this.prime_acceptor != null) {
			throw new RuntimeException("Already listening!");
		}
		// Socket for receiving messages
		this.prime_acceptor = new NioSocketAcceptor();

		// Add codecs and a filter to the the Filter chain
		prime_acceptor.getFilterChain().addLast("codec",
				new ProtocolCodecFilter(
						new CodecFactory(true)));

		// Set the handler
		prime_acceptor.setHandler(this.prime_handler);

		// Bind the socket to a port
		prime_acceptor.bind(new InetSocketAddress(this.prime_port));
	}

	/* (non-Javadoc)
	 * @see monitor.core.IPrimeStateListener#handleAdvert(monitor.commands.ComPartAdvertCmd)
	 */
	public void handleAdvert(ComPartAdvertCmd advert, IoSession session) {
		System.out.println("Got advert "+advert+" in session "+session.getId()+"["+session+"]");
	}


	/* (non-Javadoc)
	 * @see monitor.core.IPrimeStateListener#handleStateUpdate(monitor.commands.PrimeStateExchangeCmd)
	 */
	public void handleStateUpdate(PrimeStateExchangeCmd update, int com_id) {
		System.out.println(update);
		if(dataset!=null) {
			for(VarUpdate u : update.getUpdates())
				dataset.addDatum(update.getUid(), u.var_id, update.getTime(), u.asString());
		}
	}
	
	/**
	 * @param args
	 * @throws IOException
	 * @throws GeneralSecurityException
	 */
	public static void main(String[] args) throws IOException, GeneralSecurityException {
		PrimeStateServer m=null;
		if(args.length==0) {
			System.out.println("Not saving data!");
			m = new PrimeStateServer(Utils.PRIME_PORT,null);
		}
		else {
			System.out.println("Saving data for experiment "+args[0]);
			m = new PrimeStateServer(Utils.PRIME_PORT,args[0]);
		}
		m.listen();
		System.out.println("Listening on port "+Utils.PRIME_PORT+"!");
	}

}
