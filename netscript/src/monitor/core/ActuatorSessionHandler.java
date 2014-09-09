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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import monitor.util.Utils;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

/**
 * @author Miguel Erazol Erazo
 *
 */
public class ActuatorSessionHandler extends IoHandlerAdapter {
	//private final SymbioActuator actuator;
	
	ActuatorSessionHandler(SymbioActuator actuator){
		//this.actuator = actuator;
	}
	
	@Override
	public void sessionClosed(IoSession session) throws Exception {
        FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(new File("/tmp/pgc_debug_msg"),true);
			BufferedWriter bufferFileWriter  = new BufferedWriter(fileWriter);
			fileWriter.append("ActuatorSessionHandler.java GOT_EXCEPTION");
	        bufferFileWriter.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

    	super.sessionClosed(session);
    	//actuator.sessionClosed(session);
    	//Obaida
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
   		//actuator.askedToBeMaster(session);
	}
	
    @Override
	public void messageSent(IoSession session, Object message) throws Exception {
    	super.messageSent(session, message);
    	//if(Utils.DEBUG)System.out.println("Sent message!" +message.getClass().getSimpleName());
	}

	@Override
    public void messageReceived(IoSession session, Object message) throws Exception {
		super.messageReceived(session, message);
		System.out.println("messageReceived: A new symbiotic message was received!");
		// We can start operations here since this node is continuously listening for new messages
    	//  This is true for master as for slaves
    	//AbstractCmd msg = (AbstractCmd)message;
    	//if(Utils.DEBUG)System.out.println("Received:" + msg);
    	//System.out.println("Received:" + msg);
    	//exec.processCmd(msg);
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
    	super.exceptionCaught(session, cause);
        // close the connection on exceptional situation
    	if(Utils.DEBUG)System.out.println("An un-caught expection was enountered! in "+this.getClass().getSimpleName());
    	cause.printStackTrace();
    	//actuator.shutdown();
    }
}
