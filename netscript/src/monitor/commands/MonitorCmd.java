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

import java.util.Date;


/**
 * @author Nathanael Van Vorst
 *
 */
public class MonitorCmd extends AbstractCmd{
	private String cmd;
	private long delay;
	private long maxRuntime;
	private boolean blocking;
	
	public MonitorCmd(String cmd, int part_id) {
		this(cmd, part_id, 0, 0, true);
	}
	
	public MonitorCmd(String cmd, int part_id, Date d, boolean blocking) {
		this(cmd, part_id, d, 0,blocking);
	}
	
	public MonitorCmd(String cmd, int part_id, long delay, boolean blocking) {
		this(cmd, part_id, delay, 0, blocking);
	}
	
	public MonitorCmd(String cmd, int part_id, Date d, long runtime, boolean blocking) {
		super(CommandType.MONITOR_CMD, part_id);
		this.cmd = cmd;
		Date now = new Date();
		if(d.compareTo(now) <=0 ) {
			this.delay=0;
		}
		else {
			this.delay = d.getTime()-now.getTime();
		}
		this.maxRuntime=runtime;
	}
	
	public MonitorCmd(String cmd, int part_id, long delay, long runtime, boolean blocking) {
		super(CommandType.MONITOR_CMD, part_id);
		this.cmd = cmd;
		this.delay=delay;
		this.maxRuntime=runtime;
	}
		
	public void setBlocking(boolean blocking) {
		this.blocking=blocking;
	}
	
	public boolean isBlocking() {
		return blocking;
	}
	
	public void setMaxRuntime(long maxRuntime) {
		this.maxRuntime=maxRuntime;
	}
	
	public long getMaxRuntime() {
		return maxRuntime;
	}
	
	public void setDelay(long delay) {
		this.delay=delay;
	}
	
	public long getDelay() {
		return delay;
	}
	
	public String getCmd() {
		return cmd;
	}

	@Override
    public String toString() {
        return "[MonitorCmd cmd='"+cmd+"', machineId="+getMachineId()+"]";
    }
    
	@Override
	public int getBodyLength()
	{
		return (Integer.SIZE*2+Long.SIZE*2)/8+cmd.length();
	}

}
