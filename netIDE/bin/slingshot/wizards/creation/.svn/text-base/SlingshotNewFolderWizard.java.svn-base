package slingshot.wizards.creation;

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

import org.eclipse.core.resources.IFolder;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

/**
 * @author Eduardo Pena
 *
 */
public class SlingshotNewFolderWizard extends Wizard implements INewWizard {

	/** The id from the plugin.xml */
	public static final String ID = "slingshot.wizards.folder";

	/** The name displayed on top of the Wizard's window */
	private static final String WIZARD_NAME = "New Folder";

	/** The page for the Python source file creation */
	private WizardNewFolderCreationPage folderPage;

	/** The workbench in use when the wizard is called */
	//private IWorkbench workbench;
	
	/** The user's selection when the wizard is called */
	private IStructuredSelection selection;

	public SlingshotNewFolderWizard() {
		setWindowTitle(WIZARD_NAME);
	}

	/**
	 * Adds the pages that the Wizard will display.
	 */
	@Override
	public void addPages(){
		super.addPages();
		folderPage = new WizardNewFolderCreationPage(selection, "New Folder", "Create a new Folder in the workspace.");
		addPage(folderPage);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		//this.workbench = workbench;
		this.selection = selection;

	}

	@Override
	public boolean performFinish() {
		IFolder folder = folderPage.createNewFolder();
		return folder != null;
	}

}
