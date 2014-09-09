package monitor.commands;

import java.io.File;

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
 * start a openvpn/ssfgwd on the machine with the correct id
 *
 */
public class StartGatewayCmd extends AbstractCmd{
	private final File config;
	private final String tarball;
	private final String expname;

    public StartGatewayCmd(int machineId,File config,String expname) {
    	super(CommandType.START_GATEWAY,machineId);
    	this.config=config;
    	if(!config.exists())
    		throw new RuntimeException(config.getAbsolutePath()+" does not exist!");
    	if(!config.isFile())
    		throw new RuntimeException(config.getAbsolutePath()+" is not a file!");
    	if(!config.getName().endsWith(".tgz"))
    		throw new RuntimeException(config.getAbsolutePath()+" is not a zipped tarball! The file must end in '.tgz'!");
    	this.tarball=config.getName();
    	this.expname=expname;
	}
    
    public StartGatewayCmd(File config,String expname) {
    	this(-1,config,expname);
	}
    
    public File getConfig() {
    	return config;
    }

    public String getTarball() {
		return tarball;
	}
    
    public String getServerName() {
    	return tarball.replace(".tgz", "");
	}
    
    public String getExpName() {
    	return expname;
    }

	@Override
    public String toString() {
        return "[StartGatewayCmd tarball="+tarball+"["+config.length()+"] expname="+expname+"]";
    }
    
	@Override
	public int getBodyLength()
	{
		//length of the two strings, the two strings, and the length of the file
		return expname.length()+tarball.length()+(2*Integer.SIZE+Long.SIZE)/8;
	}

}
