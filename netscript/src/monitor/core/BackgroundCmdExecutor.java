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

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import monitor.commands.HostCmd;
import monitor.commands.MonitorCmd;
import monitor.commands.StartExperimentCmd;
import monitor.commands.StartGatewayCmd;
import monitor.util.Utils;

/**
 * @author Nathanael Van Vorst
 * 
 */
public class BackgroundCmdExecutor {
	private final Monitor monitor;
    private final ScheduledExecutorService scheduler;
    private final int maxOutstanding;
    private int outstandingCmds;
    
	public BackgroundCmdExecutor(Monitor monitor) {
		this(monitor,Utils.MAX_OUTSTANDING_CMDS);
	}
	
	public BackgroundCmdExecutor(Monitor monitor, int maxOutstandingCommands) {
		this.monitor = monitor;
		this.maxOutstanding=maxOutstandingCommands;
		this.outstandingCmds=0;
		this.scheduler = Executors.newScheduledThreadPool(maxOutstandingCommands);
	}
	
	protected Monitor getMonitor() {
		return monitor;
	}
	
	protected synchronized void commandStarted(int commandId, String msg) {
		monitor.commandFinished(true, commandId, 0, msg);
	}
	protected synchronized void commandFinished(int commandId, int status, String msg) {
		outstandingCmds--;
		monitor.commandFinished(false, commandId, status, msg);
	}
	
	public synchronized void schedule(MonitorCmd cmd) {
		final BackgroundTask b = new BackgroundTask(this, cmd);
		if(cmd.isBlocking()) {
			b.run();
		}
		else {
			if(outstandingCmds+1<maxOutstanding) {
				outstandingCmds++;
				if(Utils.DEBUG)System.out.println("["+Utils.getDateTime()+"]Scheduling command to run in  "+cmd.getDelay()+" ms -- "+cmd.toString());
				scheduler.schedule(b, cmd.getDelay(), TimeUnit.MILLISECONDS);
			}
			else {
				monitor.commandFinished(false,cmd.getSerialNumber(), 100, "We can only support "+maxOutstanding+" backgrounded commands and we currently have"+outstandingCmds);
			}
		}
	}
	public synchronized void schedule(StartGatewayCmd cmd) {
		final BackgroundTask b = new BackgroundTask(this, cmd);
		b.run();
	}	
	public synchronized void schedule(HostCmd cmd) {
		if(outstandingCmds+1<maxOutstanding) {
			outstandingCmds++;
			if(Utils.DEBUG)System.out.println("["+Utils.getDateTime()+"]Scheduling command to runin  "+cmd.getDelay()+" ms -- "+cmd.toString());
			scheduler.schedule(new BackgroundTask(this, cmd), cmd.getDelay(), TimeUnit.MILLISECONDS);
		}
		else {
			monitor.commandFinished(false,cmd.getSerialNumber(), 100, "We can only support "+maxOutstanding+" backgrounded commands and we currently have"+outstandingCmds);
		}
	}
	
	public synchronized void schedule(StartExperimentCmd cmd) {
		if(outstandingCmds+1<maxOutstanding) {
			outstandingCmds++;
			scheduler.schedule(new BackgroundTask(this, cmd), 1, TimeUnit.MILLISECONDS);
		}
		else {
			monitor.commandFinished(false,cmd.getSerialNumber(), 100, "We can only support "+maxOutstanding+" backgrounded commands and we currently have"+outstandingCmds);
		}
	}
}
