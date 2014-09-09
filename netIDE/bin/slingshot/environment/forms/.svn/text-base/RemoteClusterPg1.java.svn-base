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


import java.util.ArrayList;
import java.util.LinkedList;

import jprime.util.ComputeNode;
import jprime.util.Portal;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

import slingshot.environment.EnvType;
import slingshot.environment.EnvironmentFileModel;
import slingshot.environment.forms.FormWizard.FormWizardDialog;

/**
 * 
 * 
 * @author Nathanael Van Vorst
 *
 */
public class RemoteClusterPg1 extends BaseForm {
	private Text ips;
	private Shell shell;
	private BasePage page;
	
	/**
	 * 
	 */
	public RemoteClusterPg1() {
		
	}

	/* (non-Javadoc)
	 * @see slingshot.environment.forms.BaseForm#createControl(org.eclipse.swt.widgets.Composite, slingshot.environment.forms.BasePage)
	 */
	public Composite createControl(Composite parent, BasePage page, Display d) {
		this.page=page;
		this.shell = parent.getShell();
		FormToolkit toolkit = new FormToolkit(d);
		Form form = toolkit.createForm(parent);
		Composite composite = form.getBody();
		composite.setLayout(new FillLayout());
        composite.setFont(parent.getFont());
		
		parent.setBackground(toolkit.getColors().getBackground());
		parent.getParent().setBackground(toolkit.getColors().getBackground());

		
		GridData gd = null;
		
		Section sec = toolkit.createSection(composite, Section.DESCRIPTION | Section.TITLE_BAR);
		sec.setLayout(new FillLayout(SWT.CENTER));
		sec.setText("IP addresses of compute nodes");
		sec.setDescription("Each IP address will be used as a compute node.");

		
		Composite sec_client = toolkit.createComposite(sec, SWT.WRAP);
		sec_client.setLayout(new GridLayout());
		
		ips = toolkit.createText(sec_client, "", SWT.MULTI);
		gd = new GridData(SWT.FILL, SWT.FILL,true,true);
		gd.horizontalSpan=2;
		ips.setLayoutData(gd);
		ips.setEnabled(true);
		ips.setEditable(true);
		
		sec.setClient(sec_client);
		//composite.setSize(400,400);
        return composite;
	}
	
	/* (non-Javadoc)
	 * @see slingshot.environment.forms.BaseForm#addListeners(org.eclipse.swt.widgets.Listener)
	 */
	public void addListeners(Listener l) {
		ips.addListener(SWT.CHANGED, l);
	}

	/* (non-Javadoc)
	 * @see slingshot.environment.forms.BaseForm#init(org.eclipse.core.resources.IFile)
	 */
	@Override
	public EnvironmentFileModel init(IFile configurationFile) {
		throw new RuntimeException("don't call this");
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.WizardPage#isPageComplete()
	 */
	public String isPageComplete(){
		if(ips.getText().length()>0 && ips.getText().split("\\s+").length>0) {
			return null;
		}
		return "Enter 1 or more IP addresses";
	}

	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
	 */
	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		//no op
	}


	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.MouseListener#mouseUp(org.eclipse.swt.events.MouseEvent)
	 */
	@Override
	public void mouseUp(MouseEvent event) {
		//no op
	}		
	
	/* (non-Javadoc)
	 * @see slingshot.environment.forms.BaseForm#handleEvent(org.eclipse.swt.widgets.Event, slingshot.environment.forms.BasePage)
	 */
	public void handleEvent(Event e, BasePage page){
		String msg = isPageComplete();
		if(msg == null || msg.length()==0) {
			page.setPageComplete(true);
		}
		else {
			page.setErrorMessage(msg);
			page.setPageComplete(false);
		}
	}
	
	public void openNextPage() {
		FormWizardDialog dialog = new FormWizardDialog(shell, new FormWizard(page.env,2,null));
		dialog.open();
	}
	
	/* (non-Javadoc)
	 * @see slingshot.environment.forms.BaseForm#saveDataToModel(slingshot.environment.EnvironmentFileModel, java.lang.String)
	 */
	public void saveDataToModel(EnvironmentFileModel model, String name) {
		String[] temp = ips.getText().split("\\s+");
		if(name != null) model.name = name;
		model.environment = EnvType.REMOTE_CLUSTER;
		model.nodes = new LinkedList<ComputeNode>();
		for(int i =0;i<temp.length;i++) {
			if(temp[i].length()>0) {
				model.nodes.add(new ComputeNode(temp[i], temp[i], new ArrayList<Portal>()));
			}
		}
		model.linked_env_type=null;
		model.linked_env_name="";
		model.slice_name="";

		
		System.out.println("save data to model "+model.name+" file="+name);
	}

	@Override
	public boolean canFlipToNextPage() {
		// TODO Auto-generated method stub
		return false;
	}
}
