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

import java.util.ArrayList;

/**
 * @author Nathanael Van Vorst
 *
 */
public class StartExperimentCmd extends AbstractCmd{
	private final int runtime;
	private final String name;
	private final String debugLevel;
	private final String vpngateway;	
	private ArrayList<String> portal_args=new ArrayList<String>(); //not transmitted --set at the master...
	private ArrayList<SetupExperimentCmd.TLVFile> tlvs=null;

    public StartExperimentCmd(int runtime, String name, String debugLevel, String vpngateway) {
    	super(CommandType.START_EXPERIMENT);
		this.runtime = runtime;
		this.name = name;
		this.debugLevel = debugLevel;
		this.vpngateway=vpngateway;
	}
    
    public StartExperimentCmd(int runtime, String name, String vpngateway) {
    	this(runtime,name,"OFF",vpngateway);
	}
    
    public ArrayList<SetupExperimentCmd.TLVFile> getTlvs() {
		return tlvs;
	}

	public void setTlvs(ArrayList<SetupExperimentCmd.TLVFile> tlvs) {
		this.tlvs = tlvs;
	}

	public int getRuntime() {
		return runtime;
	}

	public String getName() {
		return name;
	}

	public String getVPNGateway() {
		return vpngateway;
	}
	
	public String getDebugLevel() {
		return debugLevel;
	}

	public ArrayList<String> getPortalArgs() {
		return portal_args;
	}

	public void setPortalArgs(ArrayList<String> portal_args) {
		this.portal_args = portal_args;
	}

	@Override
    public String toString() {
        return "[StartExperimentCmd runtime="+runtime+", name="+name+", debug="+debugLevel+"]";
    }
    
	@Override
	public int getBodyLength()
	{
		return (Integer.SIZE*4)/8+debugLevel.length()+name.length()+vpngateway.length();
	}

}
