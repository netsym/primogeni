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
import java.util.ArrayList;
import java.util.HashSet;

import monitor.commands.CodecFactory;
import monitor.commands.ComPartAdvertCmd;
import monitor.commands.PrimeAreaOfInterestUpdate;
import monitor.commands.PrimeStateExchangeCmd;
import monitor.core.IPrimeStateListener;
import monitor.core.PrimeSessionHandler;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;


public class TestAOI implements IPrimeStateListener {
	private final static long[] AOI_START = new long[]{26,116,201,224,247,270,293,316,339,362,385,408,431,454,477,500,523,546,569,592,615,638,661};
	private final static long[] AOI_ADD = new long[]{615,638,661};
	private final static long[] AOI_REMOVE = new long[]{15348,15371,15394,15417,15440,15463};
	private final int prime_port;
	private final PrimeSessionHandler prime_handler;
	private NioSocketAcceptor prime_acceptor;
	private final HashSet<IoSession> readerSessions;
	private final HashSet<IoSession> writerSessions;
	private final int communityCount;
	private final ArrayList<Long> aoi;
	private TestAOI(int prime_port, int communityCount, ArrayList<Long> aoi) {
		this.prime_port=prime_port;
		this.communityCount=communityCount;
		this.aoi=aoi;
		this.prime_handler=new PrimeSessionHandler(this);
		this.prime_acceptor = null;	
		this.readerSessions = new HashSet<IoSession>();
		this.writerSessions = new HashSet<IoSession>();
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
	public void handleAdvert(ComPartAdvertCmd advert, final IoSession session) {
		System.out.println("Got advert "+advert+" in session "+session.getId()+"["+session+"]");
		if(advert.isReader()) {
			readerSessions.add(session);
		}
		else {
			writerSessions.add(session);
		}
		if(advert.getCom_id() != (int)((Integer)session.getAttribute(PrimeSessionHandler.COM_ID))) {
			throw new RuntimeException("wtf?");
		}
		if(writerSessions.size()==communityCount && readerSessions.size()==communityCount) {
			System.out.println("Got all the reader and writer connections. Sending initial AOI.");
			//everyone is connected
			for(IoSession s : readerSessions) {
				s.write(new PrimeAreaOfInterestUpdate(aoi, true));
			}
			new Thread() {
				@Override
				public void run() {
					ArrayList<Long> add = new ArrayList<Long>();
					ArrayList<Long> remove = new ArrayList<Long>();
					for(long  l : AOI_REMOVE) remove.add(l);
					for(long  l : AOI_ADD) add.add(l);
					try {
						Thread.sleep(15*1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}//20seconds
					System.out.println("\n\n\nUpdating AOI.");
					for(IoSession s : readerSessions) {
						s.write(new PrimeAreaOfInterestUpdate(remove, false));
						s.write(new PrimeAreaOfInterestUpdate(add, true));
					}
				}
			}.start();
		}
	}
	
	/* (non-Javadoc)
	 * @see monitor.core.IPrimeStateListener#handleStateUpdate(monitor.commands.PrimeStateExchangeCmd)
	 */
	public void handleStateUpdate(PrimeStateExchangeCmd update, int com_id) {
		if(update.forViz()) {
			System.out.println("Update for "+update.getUid()+" time="+update.getTime()+", #updates="+update.getUpdates().size());
			//for(VarUpdate v : update.getUpdates()) {
			//	System.out.println("\t"+jprime.variable.ModelNodeVariable.int2name(v.var_id)+" --> "+v.asString());
			//}
			System.out.flush();
		}
	}
	
	/**
	 * @param args
	 * @throws IOException
	 * @throws GeneralSecurityException
	 */
	public static void main(String[] args) throws IOException, GeneralSecurityException {
		ArrayList<Long> aoi = new ArrayList<Long>();
		for( long l: AOI_START) {
			aoi.add(l);
		}
		TestAOI m=new TestAOI(Utils.PRIME_PORT, 1, aoi);
		m.listen();
		System.out.println("Listening on port "+Utils.PRIME_PORT+"! (this is coded for a 1 part, 4 thread parting of TestMonitors.java)");
	}

}
