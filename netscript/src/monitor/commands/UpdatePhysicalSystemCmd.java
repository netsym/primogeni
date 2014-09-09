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
 * @author Miguel Erazol Erazo
 *
 */
public class UpdatePhysicalSystemCmd extends AbstractCmd
{
	private int pathIndex;
	private int linkBandwidth;
	private int linkRtt;
	
	public UpdatePhysicalSystemCmd(int path_index, int bandwidth, int rtt){
		super(CommandType.SEND_UPDATE_TO_ACTUATOR_CMD, 1);
		this.setPathIndex(path_index);
		this.linkBandwidth = bandwidth;
		this.linkRtt = rtt;
	}
	
	public String toString() {
        return "[UpdateBytesFromAppCmd linkBandwidth=" + linkBandwidth +
        	", linkRtt=" + linkRtt + "]";
    }
	
	@Override
	public int getBodyLength()
	{
		return (Integer.SIZE*4)/8;
	}

	public void setLinkBandwidth(int link_bandwidth) {
		this.linkBandwidth = link_bandwidth;
	}

	public int getLinkBandwidth() {
		return linkBandwidth;
	}

	public void setLinkRtt(int linkRtt) {
		this.linkRtt = linkRtt;
	}

	public int getLinkRtt() {
		return linkRtt;
	}

	public void setPathIndex(int pathIndex) {
		this.pathIndex = pathIndex;
	}

	public int getPathIndex() {
		return pathIndex;
	}
}
