package slingshot.wizards.topology;

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

import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Hao Jiang
 */
public class TopoWizardDialog extends WizardDialog {

	public TopoWizardDialog(Shell parentShell, IWizard newWizard) {
		super(parentShell, newWizard);
	}
	
	@Override
	protected void nextPressed() {
		IWizardPage currentPage = getCurrentPage();
		Point location = currentPage.getControl().getLocation();
		Point toDisplay = currentPage.getControl().toDisplay(location);
		IWizardPage nextPage = currentPage.getNextPage();
		
		int prevWidth = 650;
		int prevHeight = 900;

		if(nextPage ==  getWizard().getPage(GenAttriPage.pagename)) {
			//use default
		}
		else if(nextPage ==  getWizard().getPage(ChooseProjectPage.pagename)) {
			prevHeight=300;
		}
		else if(nextPage ==  getWizard().getPage(AttachLanPage.pagename)) {
			//use default
		}

		// Positioning in the middle of screen:
		int x = toDisplay.x - (prevWidth - currentPage.getControl().getBounds().width) / 2;
		int y = toDisplay.y - (prevHeight - currentPage.getControl().getBounds().height) / 2;

		getShell().setBounds(x, y, prevWidth, prevHeight);
		if (nextPage != null)
			showPage(nextPage);
			
	}

	@Override
	protected void backPressed() {
		IWizardPage currentPage = getCurrentPage();
		Point location = currentPage.getControl().getLocation();
		Point toDisplay = currentPage.getControl().toDisplay(location);
		IWizardPage previousPage = currentPage.getPreviousPage();

		int prevWidth = 650;
		int prevHeight = 900;

		if(previousPage ==  getWizard().getPage(GenAttriPage.pagename)) {
			//use default
		}
		else if(previousPage ==  getWizard().getPage(ChooseProjectPage.pagename)) {
			prevHeight=300;
		}
		else if(previousPage ==  getWizard().getPage(AttachLanPage.pagename)) {
			//use default
		}
		// Positioning in the middle of screen:
		int x = toDisplay.x - (prevWidth - currentPage.getControl().getBounds().width) / 2;
		int y = toDisplay.y - (prevHeight - currentPage.getControl().getBounds().height) / 2;

		getShell().setBounds(x, y, prevWidth, prevHeight);
		if (previousPage != null)
			showPage(previousPage);
	}
}
