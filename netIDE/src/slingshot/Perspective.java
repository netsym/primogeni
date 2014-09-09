package slingshot;

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

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import slingshot.navigator.CustomNavigator;

/**
 * This class controls the default perspective of the application. This perspective consists of the project explorer and the workbench where files are opened.
 *
 * @author Eduardo Pena
 */
public class Perspective implements IPerspectiveFactory {

	/** The ID for this perspective, used for reference in the plugin.xml */
	public static final String ID = "slingshot.perspective";

	@Override
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(true);

		//Add the project explorer view to the perspective
		layout.addView(CustomNavigator.ID, IPageLayout.LEFT, 0.25f, editorArea);
		layout.getViewLayout(CustomNavigator.ID).setCloseable(false);

	}
}
