package jprime.util;

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

import jprime.IModelNode;
import jprime.Host.IHost;
import jprime.StaticTrafficType.IStaticTrafficType;

/**
 * @author Nathanael Van Vorst
 *
 */
public interface IExperimentController {

	public abstract boolean sendHostCommand(String cmd, IHost h, Date whenToRun, boolean checkReturnCode);

	public abstract boolean sendHostCommand(String cmd, IHost h, long delay, boolean checkReturnCode);
	
	public abstract void startDynamicTraffic(IStaticTrafficType dynamicTraffic);

	public abstract void setRuntimeAttribute(IModelNode node, int varId, String value);

	public abstract void setRuntimeAttribute(IModelNode node, int varId, String value, int com_id, int part_id);
	
	public int getExperimentRuntime();
}