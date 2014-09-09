package slingshot.experiment;

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

import jprime.StatusListener;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * A simple class which allows jPRIME to update progress 
 * of some operations and display them via an eclipse progress monitor.
 * 
 * @author Nathanael Van Vorst
 *
 */
public class SlinghotStatusListener extends StatusListener {
	private final IProgressMonitor monitor;
	private int remaining;
	
	/**
	 * @param monitor
	 * @param remaining
	 */
	public SlinghotStatusListener(IProgressMonitor monitor, int remaining) {
		super();
		this.remaining=remaining;
		this.monitor = monitor;
	}
	
	/**
	 * @param monitor
	 */
	public SlinghotStatusListener(IProgressMonitor monitor) {
		super();
		this.remaining=100;
		this.monitor = monitor;
	}

	/* (non-Javadoc)
	 * @see jprime.StatusListener#finsihed(int)
	 */
	@Override
	public void finsihed(int percent) {
		if(remaining<percent) {
			percent=remaining;
			if(percent<0)
				percent=0;
		}
		if(percent>0) {
			monitor.worked(percent);
			remaining-=percent;
		}
	}
}
