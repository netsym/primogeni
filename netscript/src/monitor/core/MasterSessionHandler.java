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

import monitor.commands.AbstractCmd;
import monitor.util.Utils;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;


/**
 * @author Nathanael Van Vorst 
 *
 */
public class MasterSessionHandler extends IoHandlerAdapter
{
    //private final static Logger LOGGER = LoggerFactory.getLogger(PrimoSessionHandler.class);
    private final Monitor monitor;
    private final MasterCommandExec exec;
    
    public MasterSessionHandler(Monitor monitor){
    	this.monitor=monitor;
    	this.exec = new MasterCommandExec(monitor);
    	this.exec.start();
    }
    
    @Override
	public void sessionClosed(IoSession session) throws Exception {
    	super.sessionClosed(session);
    	monitor.sessionClosed(session);
    	
    	//OBAIDA
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
		super.sessionIdle(session, status);
	}

	@Override
    public void sessionCreated(IoSession session) throws Exception {
		super.sessionCreated(session);
    }

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		super.sessionOpened(session);
   		monitor.askedToBeMaster(session);
	}
	
    @Override
	public void messageSent(IoSession session, Object message) throws Exception {
    	super.messageSent(session, message);
    	//if(Utils.DEBUG)System.out.println("Sent message!" +message.getClass().getSimpleName());
	}

	@Override
    public void messageReceived(IoSession session, Object message) throws Exception {
		super.messageReceived(session, message);
    	// We can start operations here since this node is continuously listening for new messages
    	//  This is true for master as for slaves
    	AbstractCmd msg = (AbstractCmd)message;
    	//if(Utils.DEBUG)System.out.println("Received:" + msg);
    	exec.processCmd(msg);
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
    	super.exceptionCaught(session, cause);
        // close the connection on exceptional situation
    	if(Utils.DEBUG)System.out.println("An un-caught expection was enountered! in "+this.getClass().getSimpleName());
    	cause.printStackTrace();
    	monitor.shutdown();
    }
}