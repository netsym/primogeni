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
import java.util.List;

import jprime.Experiment;
import jprime.database.Database;
import jprime.gen.ModelNodeVariable;
import jprime.visitors.TLVVisitor.TLVType;
import monitor.commands.CodecFactory;
import monitor.commands.ComPartAdvertCmd;
import monitor.commands.PrimeStateExchangeCmd;
import monitor.commands.StateExchangeCmd;
import monitor.commands.VarUpdate;
import monitor.commands.VarUpdate.BoolUpdate;
import monitor.commands.VarUpdate.StringUpdate;
import monitor.core.IPrimeStateListener;
import monitor.core.PrimeSessionHandler;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;


public class TestSet implements IPrimeStateListener {
	private final int prime_port;
	private final PrimeSessionHandler prime_handler;
	//private final Dataset dataset;
	private final Experiment exp;
	private NioSocketAcceptor prime_acceptor;
	private IoSession session=null;
	private boolean did_set_0=false;
	private boolean did_set_1=false;
	private int updates=0;
	
	private TestSet(int prime_port, String expname) {
		Database db = Database.createDatabase();
		this.exp = db.loadExperiment(expname);
		if(exp == null) {
			throw new RuntimeException("couldn't find the exp by the name "+expname);
		}
		//this.dataset=new Dataset(exp.getMetadata(),new Date(System.currentTimeMillis()));
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
		if(advert.isReader()) {
			this.session=session;
			did_set_0=false;
			did_set_1=false;
			updates=0;
		}
	}


	/* (non-Javadoc)
	 * @see monitor.core.IPrimeStateListener#handleStateUpdate(monitor.commands.PrimeStateExchangeCmd)
	 */
	public void handleStateUpdate(PrimeStateExchangeCmd update, int com_id) {
		if(update.getUid() == 106L) {
			updates++;
			System.out.println("Update for "+update.getUid()+" time="+update.getTime());
			for(VarUpdate v : update.getUpdates()) {
				System.out.println("\t"+jprime.variable.ModelNodeVariable.int2name(v.var_id)+" --> "+v.asString());
			}
		}
		//for(VarUpdate u : update.getUpdates())
		//	dataset.addDatum(update.getUid(), dataset.getMetadata().getNextModelNodeDBID(), u.var_id, update.getTime(), u.asString());
		if(session!=null && updates >5) {
			if(!did_set_0) {
				System.out.println("\n\nTuring off interface top:left:r:if0 uid=106.");
				List<VarUpdate> u = new ArrayList<VarUpdate>();
				u.add(new BoolUpdate(ModelNodeVariable.is_on(),TLVType.BOOL,false));
				session.write(new PrimeStateExchangeCmd(0, 106L,false, false, StateExchangeCmd.SET, u));
				
				//fetch new value
				u = new ArrayList<VarUpdate>();
				u.add(new BoolUpdate(ModelNodeVariable.is_on(),TLVType.BOOL,false));
				session.write(new PrimeStateExchangeCmd(0, 106L,false, false, StateExchangeCmd.FETCH, u));
				did_set_0=true;
				this.updates=0;
			}
			else if(!did_set_1) {
				System.out.println("\n\nTuring on interface top:left:r:if0 uid=106.");
				List<VarUpdate> u = new ArrayList<VarUpdate>();
				u.add(new StringUpdate(ModelNodeVariable.is_on(),TLVType.STRING,"true"));
				session.write(new PrimeStateExchangeCmd(0, 106L,false, false, StateExchangeCmd.SET, u));

				//fetch new value
				u = new ArrayList<VarUpdate>();
				u.add(new BoolUpdate(ModelNodeVariable.is_on(),TLVType.BOOL,false));
				session.write(new PrimeStateExchangeCmd(0, 106L,false, false, StateExchangeCmd.FETCH, u));
				did_set_1=true;
			}
			
		}
	}
	
	/**
	 * @param args
	 * @throws IOException
	 * @throws GeneralSecurityException
	 */
	public static void main(String[] args) throws IOException, GeneralSecurityException {
		TestSet m=null;
		if(args.length==0) {
			System.out.println("you must specify the experiment name");
			return;
		}
		else {
			m = new TestSet(Utils.PRIME_PORT,args[0]);
		}
		m.listen();
		if(Utils.DEBUG)System.out.println("Listening on port "+Utils.PRIME_PORT+"!");
	}

}
