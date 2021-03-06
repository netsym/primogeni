package slingshot.environment.forms;

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


import monitor.util.ManifestParser;
//import monitor.util.ManifestParserGeniv3;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import slingshot.environment.creation.CreateEnvironmentFirstPage.ExistingEnv;

/**
 * This class defines the Wizard which allows environments to be created or edited.
 *
 * @author Nathanael Van Vorst
 */
public class FormWizard extends Wizard implements INewWizard {
	public static class FormWizardDialog extends WizardDialog {
		public FormWizardDialog(Shell parentShell, FormWizard newWizard) {
			super(parentShell, newWizard);
		}
	}

	/** The name displayed on top of the Wizard's window */
	private static final String WIZARD_NAME = "Create/Edit Environment";

	protected final BasePage page;
	private final ExistingEnv env;
	private final int idx;
	
	/**
	 * The Constructor for this class.
	 * It only sets the Wizard Name.
	 */
	public FormWizard(ExistingEnv env, int page_idx, Object extra) {
		this.env=env;
		this.idx=page_idx;
		setTitleBarColor(new RGB(2,43,43));
		setWindowTitle(WIZARD_NAME);
		String title;
		if(env.file==null) {
			title="Create a new "+env.type.str+" enviornment.";
		}
		else {
			title="Edit a "+env.type.str+" enviornment.";
		}
		switch(env.type) {
		case PROTO_GENI:
			switch(idx) {
			case 0://edit
			case 1:
				if(env.file==null)
					page = new BasePage("Import", title, null, new ProtoGENIPg1(), env);
				else
					page = new BasePage("Edit", title, null, new ProtoGENIPg3(), env);
				break;
			case 2:
				page = new BasePage("Map", "Map GENI Resources", null, new ProtoGENIPg2((ManifestParser)extra), env); 
				break;
			case 3:
				page = new BasePage("Edit", "Edit Env", null, new ProtoGENIPg3(), env); 
				break;
//			case 4:
//				page = new BasePage("Map", "Map GENI Resources", null, new ProtoGENIPg2((ManifestParserGeniv3)extra), env); 
//				break;
			default:
				throw new RuntimeException("XXX");
			}
			break;
		case REMOTE_CLUSTER:
			switch(idx) {
			case 0://edit
				page = new BasePage(env.name, title, null, new RemoteClusterPg2(), env);
				break;
			case 1:
				page = new BasePage(env.name, title, null, new RemoteClusterPg1(), env);
			break;
			case 2:
				page = new BasePage(env.name, title, null, new RemoteClusterPg2(), env);
				break;
			default:
				throw new RuntimeException("XXX");
			}
			break;
		default:
			throw new RuntimeException("Expected env type "+env.type);
		}
	}

	/**
	 * Adds the pages that the Wizard will display.
	 */
	@Override
	public void addPages(){
		super.addPages();
		addPage(page);
	}
	
	

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#canFinish()
	 */
	@Override
	public boolean canFinish(){
		return page.isPageComplete();
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
		if(page.form instanceof ProtoGENIPg1) {
			((ProtoGENIPg1)page.form).openNextPage();
		}
		else if(page.form instanceof RemoteClusterPg1) {
			this.env.file = page.saveDataToModel();
			((RemoteClusterPg1)page.form).openNextPage();
		}
		else if(page.form instanceof ProtoGENIPg2) {
			((ProtoGENIPg2)page.form).openNextPage();
		}
		else {
			this.env.file = page.saveDataToModel();
			return env.file != null;
		}
		return true;
	}	
}