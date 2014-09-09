package slingshot.model;

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

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.CoolBar;

/**
 * The toolbars for an experiment
 * 
 * @author Eduardo Pena
 */
public class ExperimentViewCoolbar {

	private String experimentName;
	private CoolBar coolbar;

	private ExperimentToolBar experimentToolBar;
	private ConsoleToolBar consoleToolBar;
	private LogToolBar logToolBar;


	public ExperimentViewCoolbar(Composite parent, String expName) {
		experimentName = expName;
		createCoolBarComposite(parent);
	}

	private void createCoolBarComposite(Composite parent) {
		coolbar = new CoolBar(parent, SWT.RESIZE);

		coolbar.setLayoutData(new GridData(GridData.FILL_BOTH | GridData.HORIZONTAL_ALIGN_END));

		experimentToolBar = new ExperimentToolBar(coolbar, experimentName);
		consoleToolBar = new ConsoleToolBar(coolbar, experimentName);
		logToolBar = new LogToolBar(coolbar, experimentName);

		coolbar.setLocked(true);

		coolbar.pack();
	}

	public ExperimentToolBar getExperimentToolBar(){
		return experimentToolBar;
	}

	public CoolBar getCoolbar() {
		return coolbar;
	}

	public void setCoolbar(CoolBar coolbar) {
		this.coolbar = coolbar;
	}

	public void showConsoleToolBar(){
		consoleToolBar.show();
		logToolBar.hide();

		coolbar.pack();
	}

	public void showLogToolBar(){
		consoleToolBar.hide();
		logToolBar.show();

		coolbar.pack();
	}
}
