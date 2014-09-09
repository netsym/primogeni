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

import monitor.commands.AbstractCmd;
import monitor.commands.BlockingCmdResult;
import monitor.commands.CommandType;
import monitor.commands.NonBlockingCmdResult;
import monitor.commands.StateExchangeCmd;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;


/**
 * @author Nathanael Van Vorst
 *
 */
public class ControllerSessionHandler extends IoHandlerAdapter
{
    //private final static Logger LOGGER = LoggerFactory.getLogger(PrimoSessionHandler.class);
    private final RemoteController controller;
    
    public ControllerSessionHandler(RemoteController controller){
    	this.controller=controller;
    }
    
    
    @Override
	public void sessionClosed(IoSession session) throws Exception {
        FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(new File("/tmp/pgc_debug_msg"),true);
			BufferedWriter bufferFileWriter  = new BufferedWriter(fileWriter);
			fileWriter.append("\n\nControllerSessionHandler.java sessionClosed(IoSession session) invoked.");
	        bufferFileWriter.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

    	super.sessionClosed(session); //TARGET
		try {
			fileWriter = new FileWriter(new File("/tmp/pgc_debug_msg"),true);
			BufferedWriter bufferFileWriter  = new BufferedWriter(fileWriter);
			fileWriter.append("\nControllerSessionHandler.java sessionClosed(IoSession session) invoked-2.");
	        bufferFileWriter.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

    	controller.shutdown(session);
    	//error here //OBAIDA 
    	
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
		super.sessionIdle(session, status);
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		super.sessionOpened(session);
	}
	
	@Override
    public void sessionCreated(IoSession session) throws Exception {
		super.sessionCreated(session);
    }   
    
    @Override
	public void messageSent(IoSession session, Object message) throws Exception {
    	//if(Utils.DEBUG)System.out.println("Sent message!" +message.getClass().getSimpleName());
	}

	@Override
    public void messageReceived(IoSession session, Object message) throws Exception {
		super.messageReceived(session, message);
    	// We can start operations here since this node is continuously listening for new messages
    	//  This is true for master as for slaves
    	
    	AbstractCmd msg = (AbstractCmd)message;

    	/*
    	if(Utils.DEBUG)System.out.println("new message RX in Primo Controller: msgType=" + msg.getCommandType()
    			+"["+msg.getClass().getSimpleName()+"]"
    			+ " " + message.toString() + 
    			" remote address=" + session.getRemoteAddress().toString() + 
    			" local address=" + session.getLocalAddress().toString()+"\nMSG="+msg);
    			*/
    	if(msg instanceof BlockingCmdResult) {
    		controller.commandFinished(CommandType.BLOCKING_CMD_RESULT, msg.getSerialNumber(), ((BlockingCmdResult)msg).getReturnCode(),  ((BlockingCmdResult)msg).getMsg());
    	}
    	else if(msg instanceof NonBlockingCmdResult) {
    		controller.commandFinished(CommandType.NON_BLOCKING_CMD_RESULT, msg.getSerialNumber(), ((NonBlockingCmdResult)msg).getReturnCode(),  ((NonBlockingCmdResult)msg).getMsg());
    	}
    	else if(msg instanceof StateExchangeCmd) {
    		controller.handleStateUpdate((StateExchangeCmd)msg);
    	}
    	else {
    		throw new RuntimeException("Should never see this!");
    	}
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause)  throws Exception {
    	super.exceptionCaught(session, cause);
    	System.out.println("exceptionCaught");
    	cause.printStackTrace();
    	controller.shutdown(session, cause);
    	//ERROR
    }
}