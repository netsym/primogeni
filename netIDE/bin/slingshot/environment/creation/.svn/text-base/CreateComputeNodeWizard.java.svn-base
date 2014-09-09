package slingshot.environment.creation;

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


import jprime.util.ComputeNode;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

/**
 *
 * @author Nathanael Van Vorst
 */
public class CreateComputeNodeWizard extends Wizard implements INewWizard {
	public static class ComputeNodeWizardDialog extends WizardDialog {
		private CreateComputeNodeWizard w;
		
		public ComputeNodeWizardDialog(Shell parentShell, CreateComputeNodeWizard newWizard) {
			super(parentShell, newWizard);
			w=newWizard;
		}
		
		public ComputeNode getComputeNode() {
			return w.getComputeNode();
		}
		
	}
	
	/** The id from the plugin.xml */
	public static final String ID = "slingshot.wizards.file.configuration";

	/** The name displayed on top of the Wizard's window */
	private static final String WIZARD_NAME = "Compute Node Editor";

	public CreateEditComputeNodePage myPage;
	private ComputeNode node=null;
	
	/**
	 * The Constructor for this class.
	 * It only sets the Wizard Name.
	 */
	public CreateComputeNodeWizard(ComputeNode n) {
		setTitleBarColor(new RGB(2,43,43));
		setWindowTitle(WIZARD_NAME);
		myPage = new CreateEditComputeNodePage(n);
	}

	/**
	 * Adds the pages that the Wizard will display.
	 */
	@Override
	public void addPages(){
		super.addPages();
		addPage(myPage);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#canFinish()
	 */
	@Override
	public boolean canFinish(){
		return myPage.isPageComplete();
	}
	
	/**
	 * Method called when the Wizard is first created.
	 * Gets the workbench and the user's selection.
	 *
	 * @param workbench				The current Workbench
	 * @param selection				The user's selection
	 */
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		//no op
	}

	/**
	 * Method called as soon as the Wizard is finished.
	 * Finds the Project where the file must be created, creates a new Configuration File in the correct folder and opens the new file in the Editor.
	 *
	 * @return boolean 				TRUE if the Wizard finished correctly, FALSE otherwise
	 */
	@Override
	public boolean performFinish() {
		node = myPage.getComputeNode();
		return true;
	}
	
	public ComputeNode getComputeNode() {
		return node;
	}
}