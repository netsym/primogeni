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
 * Create a container with id 'containerid' on the specified machine. Then
 * add the specified virtual interfaces with the specified names to the container
 * 
 */
public class SetupContainerCmd extends AbstractCmd{
	public static class NIC {
		private long id;
		private String mac;
		private String ip;
		public NIC(long id, String ip) {
			super();
			this.id = id;
			this.ip = ip;
			this.mac=null;
			for (int i = 40; i >= 0; i -= 8) {
				if(mac==null)mac="";
				else mac+=":";
				String s= Long.toHexString((id >> i & 0xFF));
				if(s.length()==1)
					s="0"+s;
				mac+=s;
			}
		}
		
		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public String getMac() {
			return mac;
		}

		public void setMac(String mac) {
			this.mac = mac;
		}

		public String getIp() {
			return ip;
		}

		public void setIp(String ip) {
			this.ip = ip;
		}

		@Override
		public String toString() {
			return "[id=" + id + ", ip="+ip+", mac="+mac+"]";
		}
		int getLength() {
			return (Integer.SIZE+Long.SIZE)/8 + ip.length();
		}
	};
	private long containerId;
	private ArrayList<NIC> nics;
	private int length;
	
    public SetupContainerCmd(long containerId) {
    	super(CommandType.SETUP_CONTAINER);
    	this.containerId = containerId;
    	this.nics = new ArrayList<NIC>();
    	this.length=(Integer.SIZE+Long.SIZE)/8;
    }
    
    public SetupContainerCmd(int machineId, long containerId, ArrayList<NIC> nics) {
    	super(CommandType.SETUP_CONTAINER, machineId);
    	this.containerId = containerId;
    	this.nics = nics;
    	this.length=(Integer.SIZE+Long.SIZE)/8;
    	for(NIC n:nics) {
    		length+=n.getLength();
    	}
    }
    
    public void setContainerId(long containerId) {
		this.containerId = containerId;
	}

	public void addNic(long id, String ip) {
		NIC n = new NIC(id,ip);
		this.nics.add(n);
		length+=n.getLength();
	}

	public long getContainerId() {
		return containerId;
	}

	public ArrayList<NIC> getNics() {
		return nics;
	}

	@Override
	public String toString() {
		String rv="[SetupContainer cid=" + containerId + ", nics=";
		for(int i=0;i<nics.size();i++) {
			if(i>0)rv+=",";
			rv+=nics.get(i).toString();
		}
		rv+="]";
		return rv;
	}
	
	@Override
	public int getBodyLength()
	{
		return length;
	}

}
