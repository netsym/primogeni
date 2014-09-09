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


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import jprime.util.ComputeNode;
import jprime.util.Portal;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

/**
 * 
 * The base wizard page for all environments.
 * 
 * @author Nathanael Van Vorst
 *
 */
public class CreateEditComputeNodePage  extends WizardPage implements Listener, MouseListener, ISelectionChangedListener {
	private final ComputeNode orig_node;
	private final Set<Portal> portals=new HashSet<Portal>();
	private TableViewer viewer;
	private Button addPortal, deletePortal;
	private Text control_ip, data_ip, portal_name, portal_ip;
	private Portal cur_portal = null;
	
	public CreateEditComputeNodePage(ComputeNode node) {
		super("Compute Node");
		if(node == null) {
			setTitle("Create a new compute node");
		}
		else {
			setTitle("Edit a new compute node");
		}
		this.orig_node=node;
		setDescription("Portals are used to connect the virtual network to real networks.");
	}

	@Override
	public void createControl(Composite parent) {
		initializeDialogUnits(parent);
		
		FormToolkit toolkit = new FormToolkit(Display.getCurrent());
		Form form = toolkit.createForm(parent);
		Composite composite = form.getBody();
		org.eclipse.swt.graphics.Point size = composite.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		FillLayout layout = new FillLayout(SWT.HORIZONTAL);
		layout.marginHeight=2;
		layout.marginWidth=2;
		layout.spacing=5;
		composite.setLayout(layout);
        composite.setSize(size);
        composite.setFont(parent.getFont());
		
		parent.setBackground(toolkit.getColors().getBackground());
		parent.getParent().setBackground(toolkit.getColors().getBackground());
        
		//***************
		// Section 1
		//***************
		Section s1 = toolkit.createSection(composite, Section.DESCRIPTION | Section.TITLE_BAR);
		s1.setText("IPs"); 
		s1.setDescription("The control and simulation IPs for the compute node");
		Composite s1_client = toolkit.createComposite(s1, SWT.WRAP);
		s1_client.setLayout(new GridLayout(2,false));
		toolkit.createLabel(s1_client, "Control");
		control_ip = toolkit.createText(s1_client, orig_node==null?"0.0.0.0":orig_node.getControl_ip());
		toolkit.createLabel(s1_client, "Data");
		data_ip = toolkit.createText(s1_client, orig_node==null?"0.0.0.0":orig_node.getData_ip());
		s1.setClient(s1_client);
		control_ip.addListener(SWT.KeyUp, this);
		data_ip.addListener(SWT.KeyUp, this);
		
		//***************
		// Section 2
		//***************
		Section s2 = toolkit.createSection(composite, Section.DESCRIPTION | Section.TITLE_BAR);
		s2.setText("Portals");
		s2.setDescription("Connections to external (real) networks");

		Composite s2_client = toolkit.createComposite(s2, SWT.WRAP);
		s2_client.setLayout(new GridLayout(2,false));
		Table t = toolkit.createTable(s2_client, SWT.NULL);
		GridData gd = new GridData(SWT.FILL, SWT.FILL,true,false);
		gd.horizontalSpan=2;
		gd.heightHint = 40;
		gd.widthHint = 100;
		t.setLayoutData(gd);
		toolkit.paintBordersFor(s2_client);
		
		toolkit.createLabel(s2_client, "Name");
		portal_name = toolkit.createText(s2_client, "");
		toolkit.createLabel(s2_client, "IP");
		portal_ip = toolkit.createText(s2_client, "");
		this.addPortal = toolkit.createButton(s2_client, "Add", SWT.PUSH);
		this.deletePortal = toolkit.createButton(s2_client, "Remove", SWT.PUSH);
		addPortal.setEnabled(false);
		deletePortal.setEnabled(false);
		addPortal.addMouseListener(this);
		deletePortal.addMouseListener(this);		
		portal_ip.addListener(SWT.KeyUp, this);
		portal_name.addListener(SWT.KeyUp, this);
		s2.setClient(s2_client);
		
		viewer = new TableViewer(t);
		viewer.setContentProvider(new ArrayContentProvider());
		TableViewerColumn viewerColumn = new TableViewerColumn(viewer, SWT.NONE);
		viewerColumn.getColumn().setWidth(300);
		viewerColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				Portal p = (Portal)element;
				return p.getName()+" --> "+p.getIP();
			};

			public Image getImage(Object element) {
				return PlatformUI.getWorkbench().getSharedImages()
						.getImage(ISharedImages.IMG_OBJ_ELEMENT);
			};
		}

		);
		viewer.setInput(composite);
		viewer.addPostSelectionChangedListener(this);
		
		if(orig_node!=null) {
			for(Portal p : orig_node.getPortals()) {
				portals.add(p);
				viewer.add(p);
			}
		}
		
        setPageComplete(isPageComplete());
	    // set the composite as the control for this page
	    setControl(composite);
	    // should not initially have error message
	    setErrorMessage(null);
	}

	/**
	 * Checks that all fields are filled up and sets the correct error message if some aren't.
	 * Sets the completed field on the wizard class when all the information is entered and the wizard can be completed.
	 *
	 * @return boolean				TRUE if the page is complete, FALSE otherwise
	 */
	public boolean isPageComplete()
	{
		setErrorMessage(null);
		deletePortal.setEnabled(cur_portal!=null);
		if(cur_portal==null && portal_ip.getText().length()>0&& portal_name.getText().length()>0) {
			addPortal.setEnabled(true);
			setErrorMessage("To save either add the portal or clear the name/ip fields.");
			return false;
		}
		else if(cur_portal!=null){
			if(portal_ip.getText().compareTo(cur_portal.getIP())!=0) {
				if(portal_ip.getText().length()>0) {
					cur_portal.setIP(portal_ip.getText());
				}
				else {
					setErrorMessage("The portal IP cannot be empty!");
					return false;
				}
			}
			else if(portal_name.getText().compareTo(cur_portal.getName())!=0) {
				if(portal_name.getText().length()>0) {
					cur_portal.setName(portal_name.getText());
				}
				else {
					setErrorMessage("The portal name cannot be empty!");
					return false;
				}
			}
			viewer.refresh(cur_portal);
		}
		if(control_ip.getText().length()==0) {
			setErrorMessage("Set the control ip.");
			return false;
		}
		if(data_ip.getText().length()==0) {
			setErrorMessage("Set the data ip.");
			return false;
		}
		return true;
	}

	/**
	 * Determines whether conditions are met to flip to the next Wizard's Page
	 *
	 * @return boolean				TRUE if the user can flip to the next page, FALSE otherwise
	 */
	public boolean canFlipToNextPage(){
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.MouseListener#mouseDoubleClick(org.eclipse.swt.events.MouseEvent)
	 */
	@Override
	public void mouseDoubleClick(MouseEvent e) {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.MouseListener#mouseDown(org.eclipse.swt.events.MouseEvent)
	 */
	@Override
	public void mouseDown(MouseEvent e) {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.MouseListener#mouseUp(org.eclipse.swt.events.MouseEvent)
	 */
	@Override
	public void mouseUp(MouseEvent event) {
		//handle add/delete
		boolean reset=false;
		if(event.widget == addPortal) {
			//check portal values....
			if(portal_ip.getText().length()==0) {
				setErrorMessage("The portal IP cannot be empty!");
			}
			else if(portal_name.getText().length()==0) {
				setErrorMessage("The portal name cannot be empty!");
			}
			else {
				Portal p = new Portal(portal_name.getText(), portal_ip.getText());
				portals.add(p);
				viewer.add(p);
			}
			reset=true;
		}
		else if(event.widget == deletePortal) {
			if(cur_portal!=null) {
				portals.remove(cur_portal);
				viewer.remove(cur_portal);
			}
			reset=true;
		}
		if(reset) {
			cur_portal=null;
			portal_name.setText("");
			portal_ip.setText("");
			addPortal.setEnabled(false);
			deletePortal.setEnabled(false);
			setPageComplete(isPageComplete());
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
	 */
	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		Object src = event.getSource();
		if(src instanceof TableViewer) {
			TableViewer v = (TableViewer)src;
			TableItem[] sel = v.getTable().getSelection();
			if(sel.length==1) {
				addPortal.setEnabled(false);
				deletePortal.setEnabled(true);
				Portal p = (Portal)sel[0].getData();
				portal_name.setText(p.getName());
				portal_ip.setText(p.getIP());
				cur_portal=p;
			}
			else {
				cur_portal=null;
				portal_name.setText("");
				portal_ip.setText("");
				addPortal.setEnabled(false);
				deletePortal.setEnabled(false);
				setPageComplete(isPageComplete());
			}
		}
		setPageComplete(isPageComplete());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	@Override
	public void handleEvent(Event event) {
		setPageComplete(isPageComplete());
	}
	
	public ComputeNode getComputeNode() {
		ComputeNode rv =null;
		if(orig_node == null) {
			rv = new ComputeNode(control_ip.getText(), data_ip.getText(), new ArrayList<Portal>(portals.size()));
		}
		else {
			rv=orig_node;
			rv.setControl_ip(control_ip.getText());
			rv.setData_ip(data_ip.getText());
			rv.getPortals().clear();
		}
		for(Portal p : portals) {
			rv.addPortal(p);
		}
		return rv;
	}

}
