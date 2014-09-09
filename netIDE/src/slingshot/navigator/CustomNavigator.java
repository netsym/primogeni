package slingshot.navigator;

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

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.ui.navigator.CommonNavigator;

/**
 * This class defines the Project Explorer.
 * Here the Project Explorer is told where to get the information it displays.
 *
 * @author Eduardo Pena
 *
 */
public class CustomNavigator extends CommonNavigator{

	/** The project explorer's ID for reference in the plugin.xml */
	public static final String ID = "slingshot.navigator.view";

	/**
	 * The empty constructor
	 */
	public CustomNavigator(){
		super();
	}

	/**
	 * This method tells the project explorer to get its information from the workspace assigned to this application.
	 */
	protected Object getInitialInput() {
		return ResourcesPlugin.getWorkspace().getRoot();
	}
	
}
