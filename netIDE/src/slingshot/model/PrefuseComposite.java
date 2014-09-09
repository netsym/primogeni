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

import java.awt.BorderLayout;
import java.awt.Frame;

import javax.swing.JPanel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

/**
 * Wrapper for prefuse so it can be displayed in eclipse
 * 
 * @author Eduardo Tibau
 */
public class PrefuseComposite extends Composite {

	/** A panel necessary to mount prefuse on */
	public JPanel panel;
	/** A frame necessary to mount prefuse on */
	Frame frame;

	PrefuseComposite(Composite parent, GridData data) {
		super(parent, SWT.EMBEDDED | SWT.NO_BACKGROUND | SWT.RESIZE);

		setLayoutData(data);
		this.getBounds();

		//create the frame needed for Prefuse
    	frame = SWT_AWT.new_Frame(this);
        //create a panel to mount on the frame
		panel = new JPanel(new BorderLayout());
		panel.setVisible(true);
		//initialize the frame and add the panel
		frame.setLayout(new BorderLayout());
		frame.add(panel,BorderLayout.CENTER);

	}

}
