package slingshot.experiment.compile;

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

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.ManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

/**
 * This class defines the Wizard Page that asks for the necessary configuration parameters in the Slingshot Configuration Wizard.
 *
 * @author Nathanael Van Vorst
 *
 */
public class CompilePage extends WizardPage implements Listener {
	private Text ip_prefix;
	
	protected CompilePage(String pageName) {
		super(pageName);
		setTitle("Set IPPrefix");
		setDescription("Enter IPPrefix from which IPs will be assigned in this experiment.");
	}
	
	/**
	 * Handles any event that occurs inside this Wizard Page
	 * Events may come from the text fields or the browse buttons
	 */
	@Override
	public void handleEvent(Event event) {
		setPageComplete(isPageComplete());
	}

	/**
	 * Creates the visual components for this Wizard Page
	 *
	 * @param parent				The parent element for this Wizard Page
	 */
	@Override
	public void createControl(Composite parent) {
		initializeDialogUnits(parent);
		ManagedForm mf = new ManagedForm(parent);
		FormToolkit toolkit = mf.getToolkit();
		ScrolledForm form = mf.getForm();
		Composite composite = form.getBody();
		composite.setLayout(new GridLayout(2,false));
		composite.setFont(parent.getFont());
		parent.setBackground(toolkit.getColors().getBackground());
		parent.getParent().setBackground(toolkit.getColors().getBackground());

		
		toolkit.createLabel(composite, "IPPrefix:");
		ip_prefix = toolkit.createText(composite, "192.1.0.0/16", SWT.NONE);
        ip_prefix.setEditable(true);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan=1;
		ip_prefix.setLayoutData(gd);
		ip_prefix.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				isPageComplete();
			}
		});
		setControl(form);
        setErrorMessage(null);
	}
	
	/**
	 * Determines whether conditions are met to flip to the next Wizard's Page
	 *
	 * @return boolean				TRUE if the user can flip to the next page, FALSE otherwise
	 */
	public boolean canFlipToNextPage(){
		// no next page for this path through the wizard
		return false;
	}

	/**
	 * Determines whether this page has been completed
	 *
	 * @return boolean				TRUE if the page is complete, FALSE otherwise
	 */
	public boolean isPageComplete(){
        setErrorMessage(null);
		String t = getIPPrefix();
		String[] cidr = t.split("/");
		if(cidr.length == 2) {
			try {
				int l = Integer.parseInt(cidr[1]);
				if(l <32 && l>0) {
					String[] octets = cidr[0].split("\\.");
					if(octets.length == 4) {
						boolean rv = true;
						for(int i=0;i<4; i++) {
							l = Integer.parseInt(octets[i]);
							if(l>255 || l<0) {
								rv=false;
								break;
							}
						}
						if(rv) {
							return true;
						}
					}
				}
			} catch(NumberFormatException e) {}
		}
        setErrorMessage("The prefix must have the form 'A.B.C.D/E' where A,B,C,D are in [0,255) and E is in (0,32). 192.1.0.0/16 is an example of a valid prefix.");
        return false;
	}
	
	public String getIPPrefix() {
		return ip_prefix.getText();
	}

}
