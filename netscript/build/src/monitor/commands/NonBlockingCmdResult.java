package monitor.commands;

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

/**
 * @author Nathanael Van Vorst
 *
 */
public class NonBlockingCmdResult extends AbstractCmd{
    private int returnCode;
    private String msg;
    
    public NonBlockingCmdResult(int returnCode, String msg) {
    	super(CommandType.NON_BLOCKING_CMD_RESULT);
    	this.returnCode=returnCode;
    	this.msg=msg;
    	if(msg == null)
    		msg="";
    }
    
    public NonBlockingCmdResult(int serialnum, int returnCode, String msg) {
    	super(CommandType.NON_BLOCKING_CMD_RESULT);
    	super.setSerialNumber(serialnum);
    	this.returnCode=returnCode;
    	this.msg=msg;
    	if(msg == null)
    		msg="";
    }
    
    public NonBlockingCmdResult(AbstractCmd cmd, int returnCode, String msg) {
    	super(CommandType.NON_BLOCKING_CMD_RESULT);
    	super.setSerialNumber(cmd.getSerialNumber());
    	this.returnCode=returnCode;
    	this.msg=msg;
    	if(msg == null)
    		msg="";
    }
    
	public int getReturnCode() {
		return returnCode;
	}

	public String getMsg() {
		return msg;
	}
	
	@Override
    public String toString() {
        return "[NoneBlockingCmdResult serialNumber="+getSerialNumber()+", returnCode="+getReturnCode()+", msg='"+msg+"']";
    }
    
	@Override
	public int getBodyLength()
	{
		return (Integer.SIZE*2)/8+msg.length();
	}
	
}
