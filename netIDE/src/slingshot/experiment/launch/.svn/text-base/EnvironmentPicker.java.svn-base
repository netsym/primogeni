package slingshot.experiment.launch;

import jprime.EmulationProtocol.IEmulationProtocol;
import jprime.TrafficPortal.ITrafficPortal;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.ManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import slingshot.environment.EnvType;
import slingshot.experiment.PyExperiment;

/**
 * This class defines the Wizard Page that asks information to deploy the experiment.
 *
 * @author Nathanael Van Vorst
 */
public class EnvironmentPicker extends WizardPage implements Listener, ISelectionChangedListener {
	private TableViewer environmentCombo;
	private RuntimeEnv sel_env = null;
	private IProject configProject;
	private final PyExperiment exp;

	/**
	 * @param pageName
	 */
	protected EnvironmentPicker(String pageName, PyExperiment exp, IProject configProject){
		super(pageName);
		setTitle("Environment Type:");
		setDescription("Select the type of execution environment to run the experiment.");
		this.configProject=configProject;
		this.exp=exp;
	}
	
	

	public RuntimeEnv getRuntimeEnv() {
		return sel_env;
	}


	private GridData getGD(int hspan, int vspan, boolean h, boolean v) {
		GridData gd = new GridData(GridData.FILL, GridData.FILL, h, v);
		gd.horizontalSpan=hspan;
		gd.verticalSpan=vspan;
		gd.grabExcessHorizontalSpace=h;
		gd.grabExcessVerticalSpace=v;
		return gd;
	}

	private int modelPortalCount() {
		int rv = 0;
		for (IEmulationProtocol c : exp.getExperiment().getEmuProtocols()) {
			if(c instanceof ITrafficPortal)
				rv++;
		}
		return rv;
	}

	private int modelEmuCount() {
		return exp.getExperiment().getEmuProtocols().size();
	}

	private int envPortalCount() {
		if(sel_env == null || null == sel_env.getEnv()) {
			return 0;
		}
		return sel_env.getPortalCount();
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


		Table t = toolkit.createTable(composite, SWT.FILL);
		t.setLayoutData(getGD(1,1,true,true));
		t.setLayout(new FillLayout());
		toolkit.paintBordersFor(composite);
		environmentCombo = new TableViewer(t);
		environmentCombo.setContentProvider(new ArrayContentProvider());
		TableViewerColumn viewerColumn = new TableViewerColumn(environmentCombo, SWT.NONE);
		viewerColumn.getColumn().setWidth(300);
		viewerColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((RuntimeEnv)element).name;
			};

			public Image getImage(Object element) {
				return PlatformUI.getWorkbench().getSharedImages()
				.getImage(ISharedImages.IMG_OBJ_ELEMENT);
			};
		}

		);
		environmentCombo.setInput(composite);
		environmentCombo.add(new RuntimeEnv(EnvType.LOCAL,null));
		for (EnvType env : EnvType.values()) {
			IFolder environmentFolder = configProject.getFolder(env.str);

			if (environmentFolder.exists()) {
				IResource[] members = {};
				try {
					members = environmentFolder.members();
				} 
				catch (CoreException e) { }

				for (IResource r : members) {
					if (r instanceof IFile) {
						IFile file = (IFile) r;
						//System.out.println(file.toString());
						if (file.getFileExtension().equals("env")) {
							environmentCombo.add(new RuntimeEnv(env,file));
						}
					}
				}
			}
		}
		environmentCombo.addSelectionChangedListener(this);
		setControl(form);
		setPageComplete(false);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.WizardPage#canFlipToNextPage()
	 */
	public boolean canFlipToNextPage() {
		return isPageComplete();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	@Override
	public void handleEvent(Event event) {
		setPageComplete(isPageComplete());
	}

	public void selectionChanged(SelectionChangedEvent event) {
		Object src = event.getSource();
		if(src instanceof TableViewer) {
			TableViewer v = (TableViewer)src;
			TableItem[] sel = v.getTable().getSelection();
			if(sel.length==1) {
				if(sel[0]!=null) {
					sel_env = (RuntimeEnv)sel[0].getData();
				}
			}
		}
		setPageComplete(isPageComplete());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.WizardPage#isPageComplete()
	 */
	public boolean isPageComplete(){
		if(sel_env ==null) {
			setErrorMessage("Select a runtime Enviornment.");
			return false;
		}
		switch(sel_env.type) {
		case REMOTE_CLUSTER:
		case PROTO_GENI: {
			if(envPortalCount()<modelPortalCount()) {
				setErrorMessage("The environment '"+sel_env.name+"' does not have enought portals. The model requires "+
						modelPortalCount()+" but the enviornment only provides "+envPortalCount()+".");
				return false;
			}
		}
		break;
		case LOCAL:
			if(modelEmuCount()!=0) {
				setErrorMessage("The environment '"+sel_env.name+"' does not support emulated hosts and the model requires "+modelEmuCount()+" of them.");
				return false;
			}
			if(modelPortalCount()!=0) {
				setErrorMessage("The environment '"+sel_env.name+"' does not support portals and the model requires "+modelPortalCount()+" of them.");
				return false;
			}
			break;
		default:
			throw new RuntimeException("Unknown enviornment!");
		}
		setErrorMessage(null);
		return true;
	}
	
	
}
