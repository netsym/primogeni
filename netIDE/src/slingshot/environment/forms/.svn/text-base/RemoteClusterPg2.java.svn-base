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


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import jprime.util.ComputeNode;
import jprime.util.Portal;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

import slingshot.environment.EnvType;
import slingshot.environment.EnvironmentFileModel;
import slingshot.environment.creation.CreateComputeNodeWizard;
import slingshot.environment.creation.CreateComputeNodeWizard.ComputeNodeWizardDialog;

/**
 * 
 * The form of primo geni environments.
 * 
 * @author Nathanael Van Vorst
 *
 */
public class RemoteClusterPg2 extends BaseForm {
	protected TableViewer viewer;
	protected Set<ComputeNode> slaves;
	protected Button addSlave, deleteSlave, editSlave;
	protected ComputeNode cur_slave=null;
	protected Text txtMaster_control_ip, txtMaster_data_ip;
	protected String linked_env_name="", slice_name=""; 
	protected EnvType linked_env_type;
	protected Shell shell;
	protected Section s0;
	protected Label slice_name_lbl;
	protected Text slice,name;

	/**
	 * 
	 */
	public RemoteClusterPg2() {
		slaves = new HashSet<ComputeNode>();
	}
	
	private GridData getGD(int hspan, int vspan, boolean h, boolean v) {
		GridData gd = new GridData(GridData.FILL, GridData.CENTER, h, v);
		gd.horizontalSpan=hspan;
		gd.verticalSpan=vspan;
		gd.grabExcessHorizontalSpace=h;
		gd.grabExcessVerticalSpace=v;
		return gd;
	}
	
	/* (non-Javadoc)
	 * @see slingshot.environment.forms.BaseForm#createControl(org.eclipse.swt.widgets.Composite, slingshot.environment.forms.BasePage)
	 */
	public Composite createControl(Composite parent, BasePage page, Display d) {
		this.shell = parent.getShell();
		FormToolkit toolkit = new FormToolkit(d);
		Form form = toolkit.createForm(parent);
		Composite composite = form.getBody();
		composite.setLayout(new FillLayout());
        composite.setFont(parent.getFont());
		
		parent.setBackground(toolkit.getColors().getBackground());
		parent.getParent().setBackground(toolkit.getColors().getBackground());
        
		s0 = toolkit.createSection(composite, Section.TITLE_BAR);
		s0.setLayout(new GridLayout(2,false));
		s0.setText(""); 
		s0.setDescription("");
		Composite s0_client = toolkit.createComposite(s0, SWT.WRAP);
		s0.setClient(s0_client);
		s0_client.setLayout(new GridLayout(2,false));
		this.slice_name_lbl = toolkit.createLabel(s0_client, "Slice/Sliver URN:");
		this.slice = toolkit.createText(s0_client, "", SWT.READ_ONLY);
		this.slice.setLayoutData(getGD(1,1,true,false));
		toolkit.createLabel(s0_client, "Enviornment Name:");
		this.name = toolkit.createText(s0_client, "", SWT.READ_ONLY);
		this.name.setLayoutData(getGD(1,1,true,false));
		
		//***************
		// Section 1
		//***************
		Section s1 = toolkit.createSection(s0_client, Section.DESCRIPTION | Section.TITLE_BAR);
		s1.setLayoutData(getGD(1,1,true,true));
		s1.setLayout(new FillLayout());
		s1.setText("Master"); 
		s1.setDescription("The control and simulation IPs for the master compute node");
		Composite s1_client = toolkit.createComposite(s1, SWT.WRAP);
		s1_client.setLayout(new GridLayout(2,false));
		s1.setClient(s1_client);
		toolkit.createLabel(s1_client, "Control");
		txtMaster_control_ip = toolkit.createText(s1_client, "0.0.0.0");
		txtMaster_control_ip.setLayoutData(getGD(1,1,true,false));
		toolkit.createLabel(s1_client, "Simulation");
		txtMaster_data_ip = toolkit.createText(s1_client, "0.0.0.0");
		txtMaster_data_ip.setLayoutData(getGD(1,1,true,false));

		//***************
		// Section 2
		//***************
		Section s2 = toolkit.createSection(s0_client, Section.DESCRIPTION | Section.TITLE_BAR);
		s2.setLayoutData(getGD(1,1,true,true));
		s2.setLayout(new FillLayout());
		s2.setText("Slaves");
		s2.setDescription("The control and simulation IPs for the slave compute node");

		Composite s2_client = toolkit.createComposite(s2, SWT.WRAP);
		s2_client.setLayout(new GridLayout(3,true));
		s2.setClient(s2_client);
		viewer = new TableViewer(s2_client, SWT.FILL);
		viewer.getTable().setHeaderVisible(true);
		viewer.getTable().setLinesVisible(true);
		viewer.getTable().setLayout(new FillLayout());
		viewer.getTable().setLayoutData(getGD(3,1,true,true));
		viewer.setContentProvider(new ArrayContentProvider());
		toolkit.paintBordersFor(s2_client);
		this.addSlave = toolkit.createButton(s2_client, "Add", SWT.PUSH);
		this.addSlave.setLayoutData(getGD(1,1,true,false));
		this.editSlave = toolkit.createButton(s2_client, "Edit", SWT.PUSH);
		this.editSlave.setLayoutData(getGD(1,1,true,false));
		this.deleteSlave = toolkit.createButton(s2_client, "Delete", SWT.PUSH);
		this.deleteSlave.setLayoutData(getGD(1,1,true,false));
		editSlave.setEnabled(false);
		deleteSlave.setEnabled(false);
		
		TableViewerColumn viewerColumn=null;
		
		viewerColumn = new TableViewerColumn(viewer, SWT.FILL);
		viewerColumn.getColumn().setText("Control");
		viewerColumn.getColumn().setWidth(100);
		viewerColumn.getColumn().setResizable(true);
		viewerColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				ComputeNode c = (ComputeNode)element;
				return c.getControl_ip();
			};
			public Image getImage(Object element) {
				return null;
			}
		}
		);
		viewerColumn = new TableViewerColumn(viewer, SWT.FILL);
		viewerColumn.getColumn().setText("Simulation");
		viewerColumn.getColumn().setWidth(100);
		viewerColumn.getColumn().setResizable(true);
		viewerColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				ComputeNode c = (ComputeNode)element;
				return c.getData_ip();
			};
			public Image getImage(Object element) {
				return null;
			}
		}
		);
		viewerColumn = new TableViewerColumn(viewer, SWT.FILL);
		viewerColumn.getColumn().setText("# of Portals");
		viewerColumn.getColumn().setWidth(75);
		viewerColumn.getColumn().setResizable(true);
		viewerColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				ComputeNode c = (ComputeNode)element;
				return ""+c.getPortals().size();
			};
			public Image getImage(Object element) {
				return null;
			}
		}
		);
		viewer.setInput(composite);
		
		return composite;
	}
	
	/* (non-Javadoc)
	 * @see slingshot.environment.forms.BaseForm#addListeners(org.eclipse.swt.widgets.Listener)
	 */
	public void addListeners(Listener l) {
		txtMaster_control_ip.addListener(SWT.KeyUp, l);
		txtMaster_data_ip.addListener(SWT.KeyUp, l);
		addSlave.addMouseListener((MouseListener)l);
		editSlave.addMouseListener((MouseListener)l);
		deleteSlave.addMouseListener((MouseListener)l);
		viewer.addSelectionChangedListener((ISelectionChangedListener)l);
	}
	
	
	/* (non-Javadoc)
	 * @see slingshot.environment.forms.BaseForm#canFlipToNextPage()
	 */
	public boolean canFlipToNextPage() {
		return false;
	}
	
	/* (non-Javadoc)
	 * @see slingshot.environment.forms.BaseForm#isPageComplete()
	 */
	public String isPageComplete() {
		if (txtMaster_control_ip.getText().length() <= 0 || txtMaster_data_ip.getText().length() <= 0) {
			return "You must enter both a data and control ip for the master compute node.";
		}
		if (slaves.size()<1) {
			return "You must add at least one slave!";
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see slingshot.environment.forms.BaseForm#saveDataToModel(slingshot.environment.EnvironmentFileModel, java.lang.String)
	 */
	public void saveDataToModel(EnvironmentFileModel model, String name) {
		if(name != null) model.name = name;
		model.environment = EnvType.REMOTE_CLUSTER;
		model.nodes = new LinkedList<ComputeNode>();
		model.nodes.add(new ComputeNode(txtMaster_control_ip.getText(), txtMaster_data_ip.getText(), new ArrayList<Portal>()));
		model.nodes.addAll(slaves);
		model.linked_env_type=linked_env_type;
		model.linked_env_name=linked_env_name;
		model.slice_name=slice_name;
	}
	
	/* (non-Javadoc)
	 * @see slingshot.environment.forms.BaseForm#init(org.eclipse.core.resources.IFile)
	 */
	public EnvironmentFileModel init(IFile configurationFile) {
		try {
			EnvironmentFileModel model = EnvironmentFileModel.fromFile(configurationFile);
			s0.setText((model.environment == EnvType.PROTO_GENI?"ProtoGENI":"Remote Cluster")+" Enviornment");
			slice.setText(model.slice_name != null?model.slice_name:"");
			if(model.environment == EnvType.REMOTE_CLUSTER) {
				slice.setVisible(false);
				slice_name_lbl.setVisible(false);
			}
			name.setText(model.name);
			if(model.nodes.size()>0) {
				txtMaster_control_ip.setText(model.nodes.get(0).getControl_ip());
				txtMaster_data_ip.setText(model.nodes.get(0).getData_ip());
			}
			linked_env_type=model.linked_env_type;
			linked_env_name=model.linked_env_name==null?"":model.linked_env_name;
			slice_name=model.slice_name==null?"":model.slice_name;
			for(ComputeNode c : model.nodes.subList(1, model.nodes.size())) {
				viewer.add(c);
				slaves.add(c);
			}
			return model;
		} catch (CoreException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	
	public void selectionChanged(SelectionChangedEvent event) {
		Object src = event.getSource();
		if(src instanceof TableViewer) {
			TableViewer v = (TableViewer)src;
			TableItem[] sel = v.getTable().getSelection();
			if(sel.length==1) {
				cur_slave = (ComputeNode)sel[0].getData();
				deleteSlave.setEnabled(true);
				editSlave.setEnabled(true);
			}
			else {
				cur_slave = null;
				deleteSlave.setEnabled(false);
				editSlave.setEnabled(false);
			}
		}
	}
	
	public void mouseUp(MouseEvent event) {
		if(event.widget == addSlave) {
			ComputeNodeWizardDialog dialog = new ComputeNodeWizardDialog(shell, 
					new CreateComputeNodeWizard(null));
			//dialog.setPageSize(650, 300);
			dialog.open();
			ComputeNode n = dialog.getComputeNode();
			if(n!=null) {
				slaves.add(n);
				viewer.add(n);
			}
		}
		else if(event.widget == editSlave) {
			if(cur_slave == null) {
				deleteSlave.setEnabled(false);
				editSlave.setEnabled(false);
			}
			else {
				ComputeNodeWizardDialog dialog = new ComputeNodeWizardDialog(shell, 
						new CreateComputeNodeWizard(cur_slave));
				//dialog.setPageSize(650, 300);
				dialog.open();
				viewer.refresh(cur_slave);
			}
		}
		else if(event.widget == deleteSlave) {
			if(cur_slave != null) {
				slaves.remove(cur_slave);
				viewer.remove(cur_slave);
			}
			cur_slave=null;
			deleteSlave.setEnabled(false);
			editSlave.setEnabled(false);
		}
	}	

}
