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
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Set;
import jprime.util.ComputeNode;
import jprime.util.Portal;
import monitor.util.ManifestParser;
import monitor.util.ManifestParser.GeniNode;
import monitor.util.ManifestParser.GeniNodeLink;
import monitor.util.ManifestParser.NIC;
import monitor.util.ManifestParser.NIC_ref;





import org.eclipse.core.resources.IFile;
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
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

import slingshot.Util;
import slingshot.Util.Type;
import slingshot.environment.EnvironmentFileModel;
import slingshot.environment.EnvironmentModifier;
import slingshot.environment.creation.CreateEnvironmentFirstPage.ExistingEnv;
import slingshot.environment.forms.FormWizard.FormWizardDialog;

/**
 * 
 * 
 * @author Nathanael Van Vorst, MOhammad Abu Obaida
 *
 */
public class ProtoGENIPg2 extends BaseForm {
	public static boolean isPrimoImage(String image) {
		final String t = image.toLowerCase();
		if(t.contains("prime") || t.contains("primo") || t.contains("pgc")|| t.contains("insta")|| t.contains("image")|| t.contains("fiu")) {
			return true;
		}
		return false;
	}
	public static class CNic {
		public final NIC node;
		public boolean is_data, is_portal;
		public CNic(NIC node) {
			super();
			this.node = node;
			this.is_data = false;
			this.is_portal = false;
		}
		public String ip() {
			return node.ref.attrs.get("IP");
		}
		public String mac() {
			return node.ref.attrs.get("MAC");
		}
		public String component_id() {
			return node.component_id;
		}
	}
	public static class CNode {
		public final GeniNode node;
		public final Set<CNic> nics = new HashSet<ProtoGENIPg2.CNic>();
		public final boolean is_primo;
		public boolean is_master, is_slave, is_used, have_data;
		public CNode(GeniNode node) {
			super();
			this.node = node;
			this.is_primo = isPrimoImage(disk_image()); //obaida_vvi
			this.is_master = false;
			this.is_slave = false;
			this.is_used = is_master || is_slave;
			this.have_data=false;
		}
		public String hostname() {
			return node.attrs.get("hostname");
		}
		public String disk_image() {
			return node.attrs.get("disk_image");
		}
		public String virtual_id() {
			return node.attrs.get("virtual_id");
		}
	}
	private int slave_count=0,master_count=0;
	private CNode sel_node=null;
	private CNic sel_nic=null;
	private final ManifestParser mp;
	private Set<CNode> nodes = new HashSet<ProtoGENIPg2.CNode>();
	private TableViewer nodeList, ifaceList;
	private Button is_master, is_slave, is_used, have_data, is_primo;//node
	private Text hostname, virtual_id, disk_image;//node
	private Button is_data, is_portal;//node
	private Text component_id, ip, mac;//nic
	private Shell shell;
	private BasePage page;
	
	
	
	/**
	 * 
	 */
	public ProtoGENIPg2(ManifestParser mp) {
		this.mp=mp;
	}


	
	
	/* (non-Javadoc)
	 * @see slingshot.environment.forms.BaseForm#isPageComplete()
	 */
	public String isPageComplete() {
		if(master_count!=1) {
			return "Must select a node to be the master";
		}
		else if(slave_count<1) {
			return "Must select atleast one node to be a slave";
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see slingshot.environment.forms.BaseForm#canFlipToNextPage()
	 */
	public boolean canFlipToNextPage(){
		return isPageComplete() == null;
	}

	/* (non-Javadoc)
	 * @see slingshot.environment.forms.BaseForm#saveDataToModel(slingshot.environment.EnvironmentFileModel, java.lang.String)
	 */
	public void saveDataToModel(EnvironmentFileModel model, String name) {
		throw new RuntimeException("don't call this");
	}
	
	private GridData getGD(int hspan, int vspan) {
		GridData gd = new GridData(GridData.FILL, GridData.CENTER, true, true);
		gd.horizontalSpan=hspan;
		gd.verticalSpan=vspan;
		gd.grabExcessHorizontalSpace=true;
		gd.grabExcessVerticalSpace=true;
		return gd;
	}
	
	/* (non-Javadoc)
	 * @see slingshot.environment.forms.BaseForm#createControl(org.eclipse.swt.widgets.Composite, slingshot.environment.forms.BasePage)
	 */
	public Composite createControl(Composite parent, BasePage page, Display d) {
		//JOptionPane.showMessageDialog(null,"ALERT MESSAGE","TITLE",JOptionPane.WARNING_MESSAGE);


		this.page=page;
		this.shell = parent.getShell();
		FormToolkit toolkit = new FormToolkit(d);
		Form form = toolkit.createForm(parent);
		Composite composite = form.getBody();
		composite.setLayout(new GridLayout(2,true));
		composite.setFont(parent.getFont());
		parent.setBackground(toolkit.getColors().getBackground());
		parent.getParent().setBackground(toolkit.getColors().getBackground());
		TableViewerColumn viewerColumn=null;

		Section sec1_ = toolkit.createSection(composite, Section.DESCRIPTION | Section.TITLE_BAR);
		GridData gd = new GridData(GridData.FILL, GridData.FILL, true, true);
		gd.horizontalSpan=1;
		gd.grabExcessHorizontalSpace=true;
		gd.grabExcessVerticalSpace=true;
		gd.verticalSpan=2;
		sec1_.setLayoutData(gd);
		
		sec1_.setText("Geni Nodes");
		sec1_.setDescription("Select a node and it will be displayed on the right.");
		Composite sec1 = toolkit.createComposite(sec1_, SWT.NONE);

		sec1.setLayout(new FillLayout());
		toolkit.paintBordersFor(sec1);
		
		nodeList = new TableViewer(sec1, SWT.FILL);
		nodeList.getTable().setHeaderVisible(true);
		nodeList.getTable().setLinesVisible(true);
		nodeList.getTable().setLayout(new FillLayout());
		nodeList.setContentProvider(new ArrayContentProvider());
		
		viewerColumn = new TableViewerColumn(nodeList, SWT.FILL);
		viewerColumn.getColumn().setText("Status");                               //GENI Nodes Column 1 header
		viewerColumn.getColumn().setWidth(100);
		viewerColumn.getColumn().setResizable(true);
		viewerColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				CNode c = (CNode)element;
				String s = "[Not Used]";
				if(c.is_master) s= "[Master]";
				else if(c.is_slave) s= "[Slave]";
				return s;
			};

			public Image getImage(Object element) {
				return null;
			}
		}
		);
		viewerColumn = new TableViewerColumn(nodeList, SWT.FILL);
		viewerColumn.getColumn().setText("Hostname");                              //GENI Nodes Column 2 header
		viewerColumn.getColumn().setWidth(150);
		viewerColumn.getColumn().setResizable(true);
		viewerColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				CNode c = (CNode)element;
				return c.hostname();
			};

			
			
			
			public Image getImage(Object element) {
				return null;
			}
		}
		);
		viewerColumn = new TableViewerColumn(nodeList, SWT.FILL);
		viewerColumn.getColumn().setText("# of NICS");
		viewerColumn.getColumn().setWidth(75);
		viewerColumn.getColumn().setResizable(true);
		viewerColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				CNode c = (CNode)element;
				return ""+c.nics.size();
			};

			public Image getImage(Object element) {
				return null;
			}
		}
		);


		sec1_.setClient(sec1);
		nodeList.setInput(sec1);

		Section sec2_ = toolkit.createSection(composite, Section.TITLE_BAR);
		sec2_.setLayoutData(getGD(1,1));
		sec2_.setLayout(new FillLayout());
		sec2_.setText("Node Info");
		Composite sec2 = toolkit.createComposite(sec2_, SWT.NONE);
		sec2.setLayout(new GridLayout(4,false));
		toolkit.createLabel(sec2, "Host Name:");
		hostname=toolkit.createText(sec2, "", SWT.READ_ONLY);
		hostname.setLayoutData(getGD(4,1));
		
		toolkit.createLabel(sec2, "Node name:");
		virtual_id=toolkit.createText(sec2, "", SWT.READ_ONLY);
		virtual_id.setLayoutData(getGD(3,1));

		toolkit.createLabel(sec2, "Disk Image:");
		disk_image=toolkit.createText(sec2, "", SWT.READ_ONLY);
		disk_image.setLayoutData(getGD(2,1));
		is_primo=toolkit.createButton(sec2, "PrimoGENI Image", SWT.CHECK);

		is_master = toolkit.createButton(sec2, "Master", SWT.CHECK);
		is_master.setLayoutData(getGD(1,1));
		
		is_slave = toolkit.createButton(sec2, "Slave", SWT.CHECK);
		is_slave.setLayoutData(getGD(1,1));
		
		have_data = toolkit.createButton(sec2, "Have Sim Network", SWT.CHECK);
		have_data.setLayoutData(getGD(1,1));
		
		is_used = toolkit.createButton(sec2, "Used", SWT.CHECK);
		is_used.setLayoutData(getGD(1,1));

		
		ifaceList = new TableViewer(sec2, SWT.FILL);
		ifaceList.getTable().setHeaderVisible(true);
		ifaceList.getTable().setLinesVisible(true);
		ifaceList.getTable().setLayout(new FillLayout());
		ifaceList.getTable().setLayoutData(getGD(4,3));
		ifaceList.setContentProvider(new ArrayContentProvider());
		toolkit.paintBordersFor(sec2);
		viewerColumn = new TableViewerColumn(ifaceList, SWT.NONE);
		viewerColumn.getColumn().setWidth(100);
		viewerColumn.getColumn().setResizable(true);
		viewerColumn.getColumn().setText("Type");
		viewerColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				CNic c = (CNic)element;
				String s = "[Not Used]";
				if(c.is_data) s= "[Sim]";
				else if(c.is_portal) s= "[Portal]";
				return s;
			};

			public Image getImage(Object element) {
				return null;
			}
		}
		);
		viewerColumn = new TableViewerColumn(ifaceList, SWT.NONE);
		viewerColumn.getColumn().setWidth(100);
		viewerColumn.getColumn().setResizable(true);
		viewerColumn.getColumn().setText("Name");
		viewerColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				CNic c = (CNic)element;
				return c.component_id();
			};

			public Image getImage(Object element) {
				return null;
			}
		}
		);

		sec2_.setClient(sec2);
		ifaceList.setInput(sec2);

		Section sec3_ = toolkit.createSection(composite, Section.TITLE_BAR);
		sec3_.setLayoutData(getGD(1,1));
		sec3_.setLayout(new FillLayout());
		sec3_.setText("Interface Info");
		Composite sec3 = toolkit.createComposite(sec3_, SWT.NONE);
		sec3.setLayout(new GridLayout(3,true));

		toolkit.createLabel(sec3, "Component Id");
		component_id=toolkit.createText(sec3, "", SWT.READ_ONLY);
		component_id.setLayoutData(getGD(2,1));

		toolkit.createLabel(sec3, "IP");
		ip=toolkit.createText(sec3, "", SWT.READ_ONLY);
		ip.setLayoutData(getGD(2,1));

		toolkit.createLabel(sec3, "MAC");
		mac=toolkit.createText(sec3, "", SWT.READ_ONLY);
		mac.setLayoutData(getGD(2,1));

		is_data = toolkit.createButton(sec3, "Simulation Traffic", SWT.CHECK);
		is_data.setLayoutData(getGD(1,1));
		is_portal = toolkit.createButton(sec3, "Real Traffic", SWT.CHECK);
		is_portal.setLayoutData(getGD(1,1));
		
		sec3_.setClient(sec3);
		
		HashMap<GeniNode,CNode> nm = new HashMap<ManifestParser.GeniNode, ProtoGENIPg2.CNode>();
		for(GeniNode n : mp.getNodes()) {
			CNode c = new CNode(n);
			nm.put(n,c);
			for(NIC nic : n.nics) {
				c.nics.add(new CNic(nic));
			}
		}
		
		//find the link with the most primo nodes on it....
		HashMap<GeniNodeLink,HashSet<CNode>> links = new HashMap<ManifestParser.GeniNodeLink, HashSet<CNode>>();
		Integer min=null,max=null;
		for(GeniNodeLink l : mp.getLinks()) {
			HashSet<CNode> t = new HashSet<CNode>();
			for(NIC_ref n : l.refs) {
				if(n.nic != null && n.nic.parent != null) {
					final CNode c = nm.get(n.nic.parent);
					if(c != null && c.is_primo)
						t.add(c);
				}
			}
			if(t.size()>0) {
				if(min==null || min>t.size())min=t.size();
				if(max==null || max<t.size())max=t.size();
				links.put(l,t);
			}
		}
		if(min != max) {
			//filter out the links which have fewer than the max number....
			HashSet<GeniNodeLink> del = new HashSet<ManifestParser.GeniNodeLink>();
			for(Entry<GeniNodeLink, HashSet<CNode>> e : links.entrySet()) {
				if(e.getValue().size() < max) del.add(e.getKey());
			}
			for(GeniNodeLink l : del)
				links.remove(l);
		}
		if(links.size()==1) {
			//assume all nics attached to this link are simulated
			//assume others are data or portal
			CNode master = null;
			for(Entry<GeniNodeLink, HashSet<CNode>> e : links.entrySet()) {
				for(CNode c : e.getValue()) {
					c.is_slave=true;
					c.is_used=true;
					if(master == null || master.nics.size() > c.nics.size()) {
						master=c;
						master.is_master=true;
						master.is_slave=false;
						this.master_count=1;
					}
					else {
						this.slave_count++;
					}
					CNic sim=null;
					for(CNic nic : c.nics) {
						for(NIC_ref r : e.getKey().refs) {
							if(r.nic == nic.node) {
								sim=nic;
								break;
							}
						}
						if(sim!=null)
							break;
					}	
					sim.is_portal=false;
					sim.is_data=true;
					for(CNic nic : c.nics) {
						if(nic != sim) {
							nic.is_portal=true;
							nic.is_data=false;
						}
					}
				}
			}
		}
		else {
			//we can't know what they want here....
			Util.dialog(Type.WARNING, "Automatic Compute Node Assignment Failed",
					"Unable to determine which nodes are meant to be slaves, masters, or non-PrimoGENI nodes. You must manually assign them.");
		}
		for(CNode c : nm.values()) {
			nodes.add(c);
			nodeList.add(c);
		}
		return composite;
	}

	/* (non-Javadoc)
	 * @see slingshot.environment.forms.BaseForm#addListeners(org.eclipse.swt.widgets.Listener)
	 */
	public void addListeners(Listener l) {
		nodeList.addSelectionChangedListener((ISelectionChangedListener)l);
		ifaceList.addSelectionChangedListener((ISelectionChangedListener)l);
		is_master.addMouseListener((MouseListener)l);
		is_slave.addMouseListener((MouseListener)l);
		is_used.addMouseListener((MouseListener)l);
		is_data.addMouseListener((MouseListener)l);
		is_portal.addMouseListener((MouseListener)l);
	}

	/* (non-Javadoc)
	 * @see slingshot.environment.forms.BaseForm#init(org.eclipse.core.resources.IFile)
	 */
	@Override
	public EnvironmentFileModel init(IFile configurationFile) {
		throw new RuntimeException("don't call this");
	}

	public void openNextPage() {
		ExistingEnv newenv = new ExistingEnv(page.env.name,page.env.type, createEnvironmentFileModel());
		FormWizardDialog dialog = new FormWizardDialog(shell, 
				new FormWizard(newenv,3,null));
		dialog.open();
		page.env.file = newenv.file;
	}

	/* (non-Javadoc)
	 * @see slingshot.environment.forms.BaseForm#mouseUp(org.eclipse.swt.events.MouseEvent)
	 */
	@Override
	public void mouseUp(MouseEvent event) {
		//handle add/delete
		if(event.widget == is_master || event.widget == is_slave || event.widget == is_used) {
			handleNodeButton((Button)event.widget);
		}
		else if(event.widget == is_data || event.widget == is_portal) {
			handleNicButton((Button)event.widget);
		}
		updateNode(sel_node, false);
	}
	
	/* (non-Javadoc)
	 * @see slingshot.environment.forms.BaseForm#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
	 */
	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		Object src = event.getSource();
		if(src instanceof TableViewer) {
			TableViewer v = (TableViewer)src;
			TableItem[] sel = v.getTable().getSelection();
			if(v == nodeList) {
				CNode s = null;
				if(sel.length==1) {
					s = (CNode)sel[0].getData();
				}
				if(s != sel_node) {
					sel_node = s;
					updateNode(sel_node,true);
				}
			}
			else if(v == ifaceList) {
				if(sel_node == null) {
					sel_nic=null;
					updateNic(null);
				}
				else {
					CNic s = null;
					if(sel.length==1) {
						s = (CNic)sel[0].getData();
					}
					if(s != sel_nic) {
						sel_nic = s;
						updateNic(sel_nic);
					}
				}
			}
		}
	}
	
	private void handleNodeButton(Button b) {
		if(sel_node==null) return;
		if(!sel_node.is_primo) return;
		if(b == is_master) {
			if(b.getSelection()) {
				//make sure no others are master....
				for(CNode c : nodes) {
					c.is_master=false;
					nodeList.refresh(c);
				}
			}
			sel_node.is_slave=false;
			sel_node.is_master=b.getSelection();
			sel_node.is_used=true;
		}
		else if(b == is_slave)  {
			sel_node.is_master=false;
			sel_node.is_slave=b.getSelection();
			sel_node.is_used=true;
		}
		else if(b == is_used) {
			sel_node.is_master=false;
			sel_node.is_slave=false;
			sel_node.is_used=b.getSelection();
		}
		master_count=slave_count=0;
		//update slave/master counts
		for(CNode c : nodes) {
			if(c.is_master) master_count++;
			else if(c.is_slave) slave_count++;
		}
	}
	private void handleNicButton(Button b) {
		if(sel_node==null) return;
		if(!sel_node.is_primo) return;
		if(sel_nic==null) return;
		if(b == is_data) {
			if(b.getSelection()) {
				//make sure no others are data....
				for(CNic nic : sel_node.nics) {
					nic.is_data=false;
					ifaceList.refresh(nic);
				}
			}
			sel_nic.is_data=b.getSelection();
			sel_nic.is_portal=false;
		}
		else if(b == is_portal) {
			sel_nic.is_portal=b.getSelection();
			sel_nic.is_data=false;
		}
		sel_node.have_data=false;
		for(CNic nic : sel_node.nics) {
			if(nic.is_data)sel_node.have_data=true;
		}
	}

	private void updateNode(CNode n, boolean updateNics) {
		if(n == null) {
			sel_nic=null;
			is_master.setSelection(false);
			is_slave.setSelection(false);
			have_data.setSelection(false);
			is_used.setSelection(false);
			hostname.setText("");
			virtual_id.setText("");
			disk_image.setText("");
			is_used.setEnabled(false);
			is_master.setEnabled(false);
			is_slave.setEnabled(false);
			have_data.setEnabled(false);
			is_primo.setEnabled(false);
			is_primo.setSelection(false);
			ifaceList.refresh();
		}
		else {
			is_master.setSelection(n.is_master);
			is_slave.setSelection(n.is_slave);
			have_data.setSelection(n.have_data);
			is_used.setSelection(n.is_used);
			hostname.setText(n.hostname());
			virtual_id.setText(n.virtual_id());
			disk_image.setText(n.disk_image());
			is_used.setEnabled(n.is_primo);
			is_master.setEnabled(n.is_primo);
			is_slave.setEnabled(n.is_primo);
			have_data.setEnabled(false);
			is_primo.setEnabled(false);
			is_primo.setSelection(n.is_primo);

			nodeList.refresh(sel_node);
			if(updateNics) {
				ifaceList.refresh();
				for(CNic nic : n.nics)
					ifaceList.add(nic);
			}
		}
		updateNic(sel_nic);
	}
	private void updateNic(CNic n) {
		if(n == null) {
			component_id.setText("");
			ip.setText("");
			mac.setText("");
			is_data.setSelection(false);
			is_portal.setSelection(false);
			is_data.setEnabled(false);
			is_portal.setEnabled(false);
		}
		else {
			component_id.setText(n.component_id());
			ip.setText(n.ip());
			mac.setText(n.mac());
			is_data.setSelection(n.is_data);
			is_portal.setSelection(n.is_portal);
			is_data.setEnabled(true);
			is_portal.setEnabled(true);
			ifaceList.refresh(sel_nic);
		}
	}
	
	public IFile createEnvironmentFileModel() {
		EnvironmentFileModel rv = new EnvironmentFileModel();
		rv.name=page.env.name;
		rv.environment=page.env.type;
		//convert nodes into new env
		rv.nodes = new LinkedList<ComputeNode>();
		rv.nodes.add(null);//hold place for master
		for(CNode c : nodes) {
			String cip=c.hostname(), dip=c.hostname();
			ArrayList<Portal> portals=new ArrayList<Portal>(c.nics.size());
			for(CNic nic : c.nics) {
				if(nic.is_data) {
					dip=nic.ip();
				}
				else if(nic.is_portal) {
					portals.add(new Portal(nic.component_id(), nic.ip()));
				}
			}
			ComputeNode cn = new ComputeNode(cip, dip, portals);
			if(c.is_master) {
				rv.slice_name = c.node.attrs.get("sliver_urn");
				rv.nodes.set(0, cn);
			}
			else if(c.is_slave){
				rv.nodes.add(cn);
			}
		}
		return EnvironmentModifier.createEnvironment(rv);
	}

}
