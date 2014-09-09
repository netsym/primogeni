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
public class SetupSlaveCmd extends AbstractCmd{

	private byte[] publicKey;
	private byte[] privateKey;
	private String pvfsConfig;
	private String expname;

	public SetupSlaveCmd(int machineId, byte[] publicKey, byte[] privateKey, String pvfsConfig, String expname) {
    	super(CommandType.SETUP_SLAVE, machineId);
    	if(publicKey==null) 
    		publicKey="".getBytes();
    	if(privateKey==null) 
    		privateKey="".getBytes();
    	if(pvfsConfig==null)
    		pvfsConfig="";
    	if(expname==null || expname.length() == 0) 
    		throw new RuntimeException("Invalid expname!");
    	this.publicKey=publicKey;
    	this.privateKey=privateKey;
    	this.pvfsConfig = pvfsConfig;
    	this.expname=expname;
    		
    }

	public String getExpName() {
		return expname;
	}
	public String getPvfsConfig() {
		return pvfsConfig;
	}
	
	public byte[] getPublicKey() {
		return publicKey;
	}

	public byte[] getPrivateKey() {
		return privateKey;
	}

	@Override
    public String toString() {
        return "[SetupSlave length="+getBodyLength()+"]";
    }
	
	@Override
	public int getBodyLength()
	{
		return (Integer.SIZE*4)/8+publicKey.length+privateKey.length+pvfsConfig.length()+expname.length();
	}

}
