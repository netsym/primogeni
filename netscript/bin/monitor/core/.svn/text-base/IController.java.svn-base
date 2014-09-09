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

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import jprime.Experiment;
import jprime.IModelNode;
import jprime.Host.IHost;
import jprime.StaticTrafficType.IStaticTrafficType;
import jprime.util.ComputeNode;
import jprime.util.IExperimentController;
import monitor.commands.AbstractCmd;
import monitor.commands.StateExchangeCmd;

import org.apache.mina.core.session.IoSession;

/**
 * @author Nathanael Van Vorst
 *
 */
public interface IController extends IExperimentController {

	public abstract HashMap<Integer, ArrayList<IHost>> getEmulatedHost2machineMap();

	public abstract int getFailures();

	public abstract String getExpName();

	public abstract IExpListenter getListener();

	public abstract int getNumOutstandingCommands();

	public abstract int getNumOutstandingNonBlockingCommands();

	public abstract int getNumOutstandingBlockingCommands();

	public abstract void connect(final ComputeNode master,
			final List<ComputeNode> slaves, final String keystore)
			throws GeneralSecurityException, IOException;

	public abstract void shutdown(boolean sendShutdownCommand, boolean failed);

	public abstract void shutdown(IoSession session);

	public abstract void shutdown(IoSession session, Throwable cause);

	public abstract boolean sendHostCommand(String cmd, IHost h, Date whenToRun, boolean checkReturnCode);

	public abstract boolean sendHostCommand(String cmd, IHost h, long delay, boolean checkReturnCode);

	public abstract boolean sendHostCommand(String cmd, long hid,
			int machineid, Date whenToRun, boolean checkReturnCode, long runtime);

	public abstract boolean sendHostCommand(String cmd, long hid,
			int machineid, Date whenToRun, boolean checkReturnCode);

	public abstract boolean sendHostCommand(String cmd, long hid,
			int machineid, long delay, boolean checkReturnCode, long runtime);

	public abstract boolean sendHostCommand(String cmd, long hid,
			int machineid, boolean checkReturnCode, long delay);

	public abstract boolean sendCommand(AbstractCmd cmd);

	public abstract void adjustVizExportRate(long newRate);
	
	public abstract Integer getMasterId();

	public abstract boolean startExperiment(int runtime);

	public abstract ExpRunner runExperiment(final Experiment exp,
			final int runtime, ExpRunner runner);

	public abstract void cleanupExperiment(final Experiment exp,
			IExpListenter listener);

	public abstract void handleStateUpdate(StateExchangeCmd update);
	
	public abstract void setRuntimeAttribute(IModelNode node, int varId, String value);

	public abstract void setRuntimeAttribute(IModelNode node, int varId, String value, int com_id, int part_id);
	
	public abstract int getExperimentRuntime();
	
	public abstract void startDynamicTraffic(IStaticTrafficType dynamicTraffic);
	
	public abstract void addToAreaOfInterest(IModelNode m);
	public abstract void addToAreaOfInterest(Collection<IModelNode> ms);
	
	public abstract void removeFromAreaOfInterest(IModelNode m);
	public abstract void removeFromAreaOfInterest(Collection<IModelNode> ms);
	
	
}