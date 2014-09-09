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
public class HostCmd extends AbstractCmd{
	private String cmd;
	private Long hostId;
	private long delay;
	private long maxRuntime;
	private boolean checkReturnCode;
	
	public HostCmd(String cmd, long h, int part_id, Date d, boolean checkReturnCode) {
		this(cmd, h, part_id, d, checkReturnCode, 0);
	}
	public HostCmd(String cmd, long h, int part_id, Date d, boolean checkReturnCode, long runtime) {
		super(CommandType.HOST_CMD, part_id);
		this.checkReturnCode=checkReturnCode;
		this.cmd = cmd;
		this.hostId =h;
		Date now = new Date();
		if(d.compareTo(now) <=0 ) {
			this.delay=0;
		}
		else {
			this.delay = d.getTime()-now.getTime();
		}
		this.maxRuntime=runtime;
	}
	public HostCmd(String cmd, long h, int part_id, long delay, boolean checkReturnCode) {
		this(cmd, h, part_id, delay, checkReturnCode, 0);
	}
	public HostCmd(String cmd, long h, int part_id, long delay, boolean checkReturnCode, long runtime) {
		super(CommandType.HOST_CMD, part_id);
		this.checkReturnCode=checkReturnCode;
		this.cmd = cmd;
		this.hostId =h;
		this.delay=delay;
		this.maxRuntime=runtime;
	}
	
	public HostCmd(String cmd, long h, boolean checkReturnCode) {
		super(CommandType.HOST_CMD);
		this.cmd = cmd;
		this.hostId =h;
		this.delay=0;
		this.checkReturnCode=checkReturnCode;
	}

	
	public boolean shouldCheckReturnCode() {
		return checkReturnCode;
	}
	
	public void setCheckReturnCode(boolean checkReturnCode) {
		this.checkReturnCode = checkReturnCode;
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

	public Long getHostId() {
		return hostId;
	}

	@Override
    public String toString() {
        return "[HostCmd hostId="+hostId+", cmd='"+cmd+"', machineId="+getMachineId()+"]";
    }
    
	@Override
	public int getBodyLength()
	{
		return (Integer.SIZE+Long.SIZE*3)/8+cmd.length();
	}

}
