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
public abstract class AbstractCmd {
    public static final int HEADER_LENGTH = (4*Integer.SIZE)/8;
    private int serialnumber;
    private int machineId;
    private final CommandType cmdType;
    
    protected AbstractCmd(CommandType cmdType) {
    	this(cmdType,-1);
    }
    
    protected AbstractCmd(CommandType cmdType, int machineId) {
    	this.cmdType=cmdType;
    	this.serialnumber=0;
    	this.machineId=machineId;
    }
    
    public int getSerialNumber() {
        return serialnumber;
    }

    public void setSerialNumber(int serialnumber) {
        this.serialnumber = serialnumber;
    }

    public CommandType getCommandType() {
        return cmdType;
    }
    
    public int getMachineId() {
		return machineId;
	}

	public void setMachineId(int machineId) {
		this.machineId = machineId;
	}

	public abstract int getBodyLength();
}