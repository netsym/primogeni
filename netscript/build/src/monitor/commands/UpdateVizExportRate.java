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
 * Create a container with id 'containerid' on the specified machine. Then
 * add the specified virtual interfaces with the specified names to the container
 * 
 */
public class UpdateVizExportRate extends AbstractCmd {
	public final long new_rate;
	public UpdateVizExportRate(long new_rate) {
		super(CommandType.VIZ_EXPORT_RATE_UPDATE);
		this.new_rate=new_rate;
	}
	public UpdateVizExportRate dup() {
		return new UpdateVizExportRate(new_rate);
	}

	/* (non-Javadoc)
	 * @see monitor.commands.AbstractCmd#getBodyLength()
	 */
	@Override
	public int getBodyLength() {
		return Long.SIZE/8;
	}

	
	@Override
	public String toString() {
		return "[UpdateVizExportRate new_rate="+new_rate+"]";
	}
	
}
