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
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import slingshot.environment.EnvironmentFileModel;
import slingshot.environment.EnvironmentModifier;
import slingshot.environment.creation.CreateEnvironmentFirstPage.ExistingEnv;

/**
 * 
 * The base wizard page for all environments.
 * 
 * @author Nathanael Van Vorst
 *
 */
public class BasePage  extends WizardPage implements Listener, MouseListener, ISelectionChangedListener{
	protected final ExistingEnv env;
	protected final BaseForm form;
	protected EnvironmentFileModel modelfile;
	protected BasePage(String pageName, String title, String desc, BaseForm form, ExistingEnv env) {
		super(pageName);
		setTitle(title);
		setDescription(desc);
		this.form=form;
		this.env=env;
		this.modelfile=null;
	}

	/**
	 * Checks that all fields are filled up and sets the correct error message if some aren't.
	 * Sets the completed field on the wizard class when all the information is entered and the wizard can be completed.
	 *
	 * @return boolean				TRUE if the page is complete, FALSE otherwise
	 */
	public boolean isPageComplete()
	{
		String err = form.isPageComplete();
		if(err == null) {
			setErrorMessage(null);
			return true;
		}
		else {
			setErrorMessage(err);
			return false;
		}
	}

	/**
	 * Determines whether conditions are met to flip to the next Wizard's Page
	 *
	 * @return boolean				TRUE if the user can flip to the next page, FALSE otherwise
	 */
	public boolean canFlipToNextPage(){
		return form.canFlipToNextPage();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.WizardPage#getNextPage()
	 */
	@Override
	public IWizardPage getNextPage() {
		return form.getNextPage(getWizard());
	}

	/**
	 * Saves the necessary Data to the wizard Model.
	 * The parameters inputed by the user in this page are saved in the wizard object.
	 */
	protected IFile saveDataToModel() {
		this.modelfile = new EnvironmentFileModel();
		form.saveDataToModel(modelfile, env.name);
		return EnvironmentModifier.createEnvironment(modelfile);
	}
	
	/**
	 * 
	 */
	public void init(){
		if(env.file != null) {
			form.init(env.file);
	        setPageComplete(isPageComplete());
	        setErrorMessage(null);	// should not initially have error message
		}
	}
	
	@Override
	public void createControl(Composite parent) {
		initializeDialogUnits(parent);
		Display d = Display.getCurrent();
        Composite composite = form.createControl(parent, this, d);
        form.addListeners(this);
        setPageComplete(isPageComplete());
	    // set the composite as the control for this page
	    setControl(composite);
	    // should not initially have error message
	    setErrorMessage(null);
		if(env.file != null)
			init();

	}
	
	

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.MouseListener#mouseDoubleClick(org.eclipse.swt.events.MouseEvent)
	 */
	@Override
	public void mouseDoubleClick(MouseEvent e) {
		form.mouseDoubleClick(e);
		setPageComplete(isPageComplete());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.MouseListener#mouseDown(org.eclipse.swt.events.MouseEvent)
	 */
	@Override
	public void mouseDown(MouseEvent e) {
		form.mouseDown(e);
		setPageComplete(isPageComplete());
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.MouseListener#mouseUp(org.eclipse.swt.events.MouseEvent)
	 */
	@Override
	public void mouseUp(MouseEvent e) {
		form.mouseUp(e);
		setPageComplete(isPageComplete());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
	 */
	@Override
	public void selectionChanged(SelectionChangedEvent e) {
		form.selectionChanged(e);
		setPageComplete(isPageComplete());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	@Override
	public void handleEvent(Event e) {
		form.handleEvent(e, this);
		setPageComplete(isPageComplete());
	}
	
	/**
	 * @param b
	 */
	public void mySetButtonLayoutData(Button b) {
		setButtonLayoutData(b);
	}
}
