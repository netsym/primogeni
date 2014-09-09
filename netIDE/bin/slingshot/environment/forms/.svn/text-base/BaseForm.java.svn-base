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


import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import slingshot.environment.EnvironmentFileModel;

/**
 * 
 * All environments creation/modify pages use the concept 
 * of a "form" to render the configuration information of the specific environment.
 * 
 * Each environment type will extend this class and implement the abstract methods.
 * 
 * @author Nathanael Van Vorst
 */
public abstract class BaseForm {
	
	/**
	 * Creates the visual components for this Wizard Page
	 * 
	 * @param parent	The parent element for this Wizard Page
	 * @param page		The page to put the components on		
	 * @return
	 */
	public abstract Composite createControl(Composite parent, BasePage page, Display d);
	
	/**
	 * Add this listener to all controls/fields that should be listened to.
	 * 
	 * @param l the listener
	 */
	public abstract void addListeners(Listener l);
	
	/**
	 * @return whether the wizard can advance to the next page
	 */
	public abstract boolean canFlipToNextPage();
	
	/**
	 * @return whether the page is complete
	 */
	public abstract String isPageComplete();
	
	/**
	 * Save the model configuration
	 * 
	 * @param model the model to save
	 * @param name the name of the model
	 */
	public abstract void saveDataToModel(EnvironmentFileModel model, String name);
	
	/**
	 * Load the values of the form from the file
	 * 
	 * @param configurationFile the file to get the initial values form
	 */
	public abstract EnvironmentFileModel init(IFile configurationFile);

	public void handleEvent(Event e, BasePage page) {
	}

	public void mouseDoubleClick(MouseEvent e) {
	}

	public void mouseDown(MouseEvent e) {
	}
	
	public void mouseUp(MouseEvent e) {
	}

	public void selectionChanged(SelectionChangedEvent e) {
	}
	public IWizardPage getNextPage(IWizard w) {
		return null;
	}
}
