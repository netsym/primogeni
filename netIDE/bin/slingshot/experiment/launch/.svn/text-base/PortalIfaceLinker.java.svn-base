package slingshot.experiment.launch;

import java.util.HashSet;
import java.util.Set;

import jprime.Experiment;
import jprime.EmulationProtocol.IEmulationProtocol;
import jprime.Interface.IInterface;
import jprime.TrafficPortal.ITrafficPortal;
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
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.forms.ManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

import slingshot.environment.configuration.EnvironmentConfiguration;
import slingshot.environment.configuration.RemoteClusterConfiguration;
import slingshot.experiment.PyExperiment;

/**
 * This class defines the Wizard Page that asks information to deploy the experiment.
 *
 * @author Nathanael Van Vorst
 */
public class PortalIfaceLinker extends WizardPage implements ISelectionChangedListener, MouseListener {
	//needed because we cann't directly hash a model node
	public static class Nic {
		final IInterface nic;
		public Nic(IInterface nic) {
			super();
			this.nic=nic;
		}
	}
	private Set<Portal> portals =new HashSet<Portal>(); 
	private Set<Nic> nics =new HashSet<Nic>(); 
	private Set<Portal> linked = new HashSet<Portal>(); 
	private TableViewer portalList;
	private TableViewer nicList;
	private TableViewer linkedList;
	private Button link, unlink;
	private Portal sel_linked = null;
	private Nic sel_nic = null;
	private Portal sel_portal = null;
	private final PyExperiment exp;
	private EnvironmentConfiguration ec;
	
	/**
	 * @param pageName
	 */
	protected PortalIfaceLinker(String pageName, PyExperiment exp){
		super(pageName);
		this.ec=null;
		this.exp = exp;
		setTitle("Link Traffic Protals.");
		setDescription("Connect simulated interfaces to traffic portals.");
	}
	
	/**
	 * @return the ec
	 */
	public EnvironmentConfiguration getEc() {
		return ec;
	}
	
	/**
	 * @param ec the ec to set
	 */
	public void setEc(EnvironmentConfiguration ec) {
		if(this.ec != null)
			this.ec.portal_links = null;
		this.ec = ec;
		clear();
		init(ec,exp.getExperiment());
	}
	
	private void clear() {
		for(Nic n : nics)
			nicList.remove(n);
		for(Portal p : portals)
			portalList.remove(p);
		nics.clear();
		portals.clear();
		linked.clear();
		ec.portal_links=new HashSet<Portal>(); 
	}
	private void init(EnvironmentConfiguration config, Experiment exp) {
		for(IEmulationProtocol p : exp.getEmuProtocols()) {
			if(p instanceof ITrafficPortal) {
				Nic n = new Nic((IInterface)p.getParent());
				nicList.add(n);
				nics.add(n);
			}
		}
		
		if(config instanceof RemoteClusterConfiguration) {
			RemoteClusterConfiguration rc = (RemoteClusterConfiguration)config;
			for(ComputeNode cn : rc.getSlaves()) {
				for(Portal p : cn.getPortals()) {
					portals.add(p);
					portalList.add(p);
				}
			}
		}
	}



	public Set<Portal> getLinking()  {
		return linked;
	}

	private GridData getGD(int hspan, int vspan, boolean h, boolean v) {
		GridData gd = new GridData(GridData.FILL, GridData.FILL, h, v);
		gd.horizontalSpan=hspan;
		gd.verticalSpan=vspan;
		gd.grabExcessHorizontalSpace=h;
		gd.grabExcessVerticalSpace=v;
		return gd;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		initializeDialogUnits(parent);
		ManagedForm mf = new ManagedForm(parent);
		FormToolkit toolkit = mf.getToolkit();
		ScrolledForm form = mf.getForm();
		Composite composite = form.getBody();
		composite.setLayout(new GridLayout());
		composite.setFont(parent.getFont());
		parent.setBackground(toolkit.getColors().getBackground());
		parent.getParent().setBackground(toolkit.getColors().getBackground());

		TableViewerColumn viewerColumn=null;
		
		Section sec0_ = toolkit.createSection(composite, Section.DESCRIPTION | Section.TITLE_BAR);
		sec0_.setLayoutData(getGD(1,1,true,true));
		sec0_.setText("");
		sec0_.setDescription("Select a portal and interface and click 'link' to connect them.");
		Composite sec0 = toolkit.createComposite(sec0_, SWT.NONE);
		sec0.setLayout(new GridLayout(3,false));
		sec0_.setClient(sec0);

		Section sec1_ = toolkit.createSection(sec0, Section.TITLE_BAR);
		sec1_.setLayoutData(getGD(1,1,true,true));
		sec1_.setText("Portals");
		Composite sec1 = toolkit.createComposite(sec1_, SWT.NONE);
		sec1.setLayout(new GridLayout(1,true));
		sec1_.setClient(sec1);

		Section sec3_ = toolkit.createSection(sec0, Section.TITLE_BAR);
		sec3_.setLayoutData(getGD(2,2,true,true));
		sec3_.setText("Interface/Portal Pair");
		Composite sec3 = toolkit.createComposite(sec3_, SWT.NONE);
		sec3.setLayout(new GridLayout(1,true));
		sec3_.setClient(sec3);

		Section sec2_ = toolkit.createSection(sec0, Section.TITLE_BAR);
		sec2_.setLayoutData(getGD(1,1,true,true));
		sec2_.setText("Interfaces");
		Composite sec2 = toolkit.createComposite(sec2_, SWT.NONE);
		sec2.setLayout(new GridLayout(1,true));
		sec2_.setClient(sec2);

		toolkit.paintBordersFor(sec0);
		toolkit.paintBordersFor(sec1);
		toolkit.paintBordersFor(sec2);
		toolkit.paintBordersFor(sec3);
		
		
		portalList = new TableViewer(sec1, SWT.FILL);
		portalList.getTable().setHeaderVisible(true);
		portalList.getTable().setLinesVisible(true);
		portalList.getTable().setLayout(new FillLayout());
		portalList.getTable().setLayoutData(getGD(1,1,true,true));
		portalList.setContentProvider(new ArrayContentProvider());
		viewerColumn = new TableViewerColumn(portalList, SWT.FILL);
		viewerColumn.getColumn().setWidth(100);
		viewerColumn.getColumn().setResizable(true);
		viewerColumn.getColumn().setText("Compute Node");
		viewerColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				Portal portal = (Portal)element;
				return portal.getNode().getControl_ip();
			};

			public Image getImage(Object element) {
				return null;
			}
		}
		);
		viewerColumn = new TableViewerColumn(portalList, SWT.FILL);
		viewerColumn.getColumn().setWidth(100);
		viewerColumn.getColumn().setResizable(true);
		viewerColumn.getColumn().setText("Portal Name");
		viewerColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				Portal portal = (Portal)element;
				return portal.getName();
			};

			public Image getImage(Object element) {
				return null;
			}
		}
		);
		viewerColumn = new TableViewerColumn(portalList, SWT.FILL);
		viewerColumn.getColumn().setWidth(100);
		viewerColumn.getColumn().setResizable(true);
		viewerColumn.getColumn().setText("Portal IP");
		viewerColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				Portal portal = (Portal)element;
				return portal.getIP();
			};

			public Image getImage(Object element) {
				return null;
			}
		}
		);
		portalList.setInput(sec1);
		
		
		nicList = new TableViewer(sec2, SWT.FILL);
		nicList.getTable().setHeaderVisible(true);
		nicList.getTable().setLinesVisible(true);
		nicList.getTable().setLayout(new FillLayout());
		nicList.getTable().setLayoutData(getGD(1,1,true,true));
		nicList.setContentProvider(new ArrayContentProvider());
		viewerColumn = new TableViewerColumn(nicList, SWT.FILL);
		viewerColumn.getColumn().setWidth(100);
		viewerColumn.getColumn().setResizable(true);
		viewerColumn.getColumn().setText("IP Address");
		viewerColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((Nic)element).nic.getIpAddress().toString();
			};

			public Image getImage(Object element) {
				return null;
			}
		}
		);
		viewerColumn = new TableViewerColumn(nicList, SWT.FILL);
		viewerColumn.getColumn().setWidth(150);
		viewerColumn.getColumn().setResizable(true);
		viewerColumn.getColumn().setText("Name");
		viewerColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((Nic)element).nic.getUniqueName().toString();
			};

			public Image getImage(Object element) {
				return null;
			}
		}
		);
		nicList.setInput(sec2);
		
		linkedList = new TableViewer(sec3, SWT.FILL);
		linkedList.getTable().setHeaderVisible(true);
		linkedList.getTable().setLinesVisible(true);
		linkedList.getTable().setLayout(new FillLayout());
		linkedList.getTable().setLayoutData(getGD(1,1,true,true));
		linkedList.setContentProvider(new ArrayContentProvider());

		
		viewerColumn = new TableViewerColumn(linkedList, SWT.FILL);
		viewerColumn.getColumn().setWidth(100);
		viewerColumn.getColumn().setResizable(true);
		viewerColumn.getColumn().setText("Compute Node");
		viewerColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				Portal portal = (Portal)element;
				return portal.getNode().getControl_ip();
			};

			public Image getImage(Object element) {
				return null;
			}
		}
		);
		viewerColumn = new TableViewerColumn(linkedList, SWT.FILL);
		viewerColumn.getColumn().setWidth(100);
		viewerColumn.getColumn().setResizable(true);
		viewerColumn.getColumn().setText("Portal Name");
		viewerColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				Portal portal = (Portal)element;
				return portal.getName();
			};

			public Image getImage(Object element) {
				return null;
			}
		}
		);
		viewerColumn = new TableViewerColumn(linkedList, SWT.FILL);
		viewerColumn.getColumn().setWidth(100);
		viewerColumn.getColumn().setResizable(true);
		viewerColumn.getColumn().setText("Interface IP");
		viewerColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				Portal portal = (Portal)element;
				return portal.getLinkedInterface().getIpAddress().toString();
			};

			public Image getImage(Object element) {
				return null;
			}
		}
		);		
		viewerColumn = new TableViewerColumn(linkedList, SWT.FILL);
		viewerColumn.getColumn().setWidth(100);
		viewerColumn.getColumn().setResizable(true);
		viewerColumn.getColumn().setText("Interface Name");
		viewerColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				Portal portal = (Portal)element;
				return portal.getLinkedInterface().getUniqueName().toString();
			};

			public Image getImage(Object element) {
				return null;
			}
		}
		);
		linkedList.setInput(sec3);

		
		
		link=toolkit.createButton(sec2, "Link", SWT.PUSH);
		unlink=toolkit.createButton(sec3, "Un-link", SWT.PUSH);
		
		link.addMouseListener(this);
		unlink.addMouseListener(this);
		nicList.addSelectionChangedListener(this);
		portalList.addSelectionChangedListener(this);
		linkedList.addSelectionChangedListener(this);
		setControl(composite);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.WizardPage#canFlipToNextPage()
	 */
	public boolean canFlipToNextPage() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
	 */
	public void selectionChanged(SelectionChangedEvent event) {
		Object src = event.getSource();
		if(src instanceof TableViewer) {
			TableViewer v = (TableViewer)src;
			TableItem[] sel = v.getTable().getSelection();
			if(v == linkedList) {
				if(sel.length==1) {
					sel_linked = (Portal)sel[0].getData();
				}
				else {
					sel_linked=null;
				}
			}
			else if(v == nicList) {
				if(sel.length==1) {
					sel_nic = (Nic)sel[0].getData();
				}
				else {
					sel_nic=null;
				}
			}
			else if(v == portalList) {
				if(sel.length==1) {
					sel_portal = (Portal)sel[0].getData();
				}
				else {
					sel_portal=null;
				}
			}
		}
		setPageComplete(isPageComplete());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.WizardPage#isPageComplete()
	 */
	public boolean isPageComplete(){
		if(ec == null) {
			setErrorMessage("Go Back and choose an environment!");
			return false;
		}
		if(sel_portal != null && sel_nic == null) {
			setErrorMessage("Choose a interface to link the portal with.");
			return false;
		}
		if(sel_portal == null && sel_nic != null) {
			setErrorMessage("Choose a portal to link the interface with.");
			return false;
		}
		if(nics.size()>0) {
			setErrorMessage("Must link each interface with a portal.");
			return false;
		}
		ec.portal_links = getLinking();
		setErrorMessage(null);
		return true;
	}
	
	@Override
	public void mouseDoubleClick(MouseEvent e) {
	}

	@Override
	public void mouseDown(MouseEvent e) {
	}

	@Override
	public void mouseUp(MouseEvent event) {
		if(event.widget == link) {
			if(sel_nic != null && sel_portal != null) {
				Portal p = sel_portal;
				p.setLinkedInterface(sel_nic.nic);
				portals.remove(sel_portal);
				portalList.remove(sel_portal);
				nics.remove(sel_nic);
				nicList.remove(sel_nic);
				linked.add(p);
				linkedList.add(p);
				sel_portal=null;
				sel_nic=null;
			}
		}
		else if(event.widget == unlink) {
			if(sel_linked != null) {
				Nic nic = new Nic(sel_linked.getLinkedInterface());
				sel_linked.setLinkedInterface(null);
				portals.add(sel_linked);
				portalList.add(sel_linked);
				nics.add(nic);
				nicList.add(nic);
				linked.remove(sel_linked);
				linkedList.remove(sel_linked);
				sel_linked=null;
			}
		}
		setPageComplete(isPageComplete());
	}
}
