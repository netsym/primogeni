package slingshot.environment.creation;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

import slingshot.Util;
import slingshot.environment.EnvType;
import slingshot.environment.EnvironmentModifier;
import slingshot.environment.forms.FormWizard;
import slingshot.environment.forms.FormWizard.FormWizardDialog;

/**
 * This class defines the Wizard Page that allows the user to choose the type of the new environment.
 *
 * @author Eduardo Pena
 * @author Nathanael Van Vorst
 * @author Neil Goldman
 */
public class CreateEnvironmentFirstPage extends WizardPage implements Listener, MouseListener, ISelectionChangedListener {
	public static class ExistingEnv {
		public final String name;
		public final EnvType type;
		public IFile file;
		public ExistingEnv(String name, EnvType type, IFile file) {
			super();
			this.name = name;
			this.type = type;
			this.file = file;
		}
		public String toString() {
			return "("+type.str+") "+name;
		}
	}
	private Set<ExistingEnv> existing = new HashSet<CreateEnvironmentFirstPage.ExistingEnv>();
	private ExistingEnv selected_existing_env=null;
	private EnvType selected_env_type=null;
	private TableViewer existing_viewer, env_viewer;
	private Button add, delete, edit;
	private Text name;
	private Shell shell;
	/**
	 * @param pageName
	 */
	protected CreateEnvironmentFirstPage(String pageName) {
		super(pageName);

		setTitle("Create/Edit Runtime Enviornments");
		setDescription("Select the name and type of the execution environment to run the experiments.");
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		this.shell = parent.getShell();
		initializeDialogUnits(parent);
		
		FormToolkit toolkit = new FormToolkit(Display.getCurrent());
		Form form = toolkit.createForm(parent);
		Composite composite = form.getBody();
		org.eclipse.swt.graphics.Point size = composite.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		FillLayout layout = new FillLayout(SWT.VERTICAL);
		layout.marginHeight=2;
		layout.marginWidth=2;
		layout.spacing=5;
		composite.setLayout(layout);
        composite.setSize(size);
        composite.setFont(parent.getFont());
		
		parent.setBackground(toolkit.getColors().getBackground());
		parent.getParent().setBackground(toolkit.getColors().getBackground());

		GridData gd = null;
		Table t = null;
		TableViewerColumn viewerColumn = null;
		
		//***************
		// Section 1
		//***************
		Section s1 = toolkit.createSection(composite, Section.DESCRIPTION | Section.TITLE_BAR);
		s1.setText("Add new Enviornments"); 
		s1.setDescription("Create the name of an enviornment & choose a type then press 'add'.");
		Composite s1_client = toolkit.createComposite(s1, SWT.WRAP);
		s1_client.setLayout(new GridLayout(2,false));
		toolkit.createLabel(s1_client, "Name");
		name = toolkit.createText(s1_client, "");
		t = toolkit.createTable(s1_client, SWT.NULL);
		gd = new GridData(SWT.FILL, SWT.FILL,true,false);
		gd.horizontalSpan=2;
		gd.verticalSpan=2;
		gd.widthHint = 100;
		name.setLayoutData(gd);
		gd = new GridData(SWT.FILL, SWT.FILL,true,false);
		gd.horizontalSpan=2;
		gd.verticalSpan=2;
		gd.heightHint = 50;
		gd.widthHint = 100;
		t.setLayoutData(gd);
		toolkit.paintBordersFor(s1_client);
	
		env_viewer = new TableViewer(t);
		env_viewer.setContentProvider(new ArrayContentProvider());
		viewerColumn = new TableViewerColumn(env_viewer, SWT.NONE);
		viewerColumn.getColumn().setWidth(300);
		viewerColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((EnvType)element).str;
			};

			public Image getImage(Object element) {
				return PlatformUI.getWorkbench().getSharedImages()
						.getImage(ISharedImages.IMG_OBJ_ELEMENT);
			};
		}

		);
		env_viewer.setInput(composite);
		add = toolkit.createButton(s1_client, "Add", SWT.PUSH);
		s1.setClient(s1_client);
		name.addListener(SWT.KeyUp, this);
		add.addMouseListener(this);
		env_viewer.addSelectionChangedListener(this);
		
		//***************
		// Section 2
		//***************
		Section s2 = toolkit.createSection(composite, Section.DESCRIPTION | Section.TITLE_BAR);
		s2.setText("Existing Enviornmments");
		s2.setDescription("Select an enviornment to 'delete' or 'edit'");

		Composite s2_client = toolkit.createComposite(s2, SWT.WRAP);
		s2_client.setLayout(new GridLayout(2,true));
		t = toolkit.createTable(s2_client, SWT.NULL);
		gd = new GridData(SWT.FILL, SWT.FILL,true,false);
		gd.horizontalSpan=2;
		gd.verticalSpan=4;
		gd.heightHint = 100;
		gd.widthHint = 100;
		t.setLayoutData(gd);
		toolkit.paintBordersFor(s2_client);
		edit = toolkit.createButton(s2_client, "Edit", SWT.PUSH);
		delete = toolkit.createButton(s2_client, "Delete", SWT.PUSH);
		edit.setEnabled(false);
		delete.setEnabled(false);
		s2.setClient(s2_client);
		
		existing_viewer = new TableViewer(t);
		existing_viewer.setContentProvider(new ArrayContentProvider());
		viewerColumn = new TableViewerColumn(existing_viewer, SWT.NONE);
		viewerColumn.getColumn().setWidth(300);
		viewerColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((ExistingEnv)element).toString();
			};

			public Image getImage(Object element) {
				return PlatformUI.getWorkbench().getSharedImages()
						.getImage(ISharedImages.IMG_OBJ_ELEMENT);
			};
		}

		);
		existing_viewer.setInput(composite);
		existing_viewer.addSelectionChangedListener(this);
		edit.addMouseListener(this);
		delete.addMouseListener(this);


		IProject configProject = ResourcesPlugin.getWorkspace().getRoot().getProject("Configuration");
		
		//load up env types
		for(EnvType e : EnvType.values()) {
			if(e != EnvType.LOCAL)
				env_viewer.add(e);
		}

		//load up the existing envs
		for (String env : EnvType.strings) {
			IFolder environmentFolder = configProject.getFolder(env);

			if (environmentFolder.exists()) {
				IResource[] members = {};
				try {
					members = environmentFolder.members();
				} 
				catch (CoreException e) { }
				
				for (IResource r : members) {
					if (r instanceof IFile) {
						IFile file = (IFile) r;
						if (file.getFileExtension().equals("env")) {
							ExistingEnv e = new ExistingEnv(file.getName().substring(0,file.getName().length()-4), EnvType.fromString(env), file);
							existing_viewer.add(e);
							existing.add(e);
						}
					}
				}
			}
		}
		
		setErrorMessage(null);	// should not initially have error message
		setControl(composite);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.WizardPage#isPageComplete()
	 */
	public boolean isPageComplete(){
		if(name.getText().length()>0 && selected_env_type != null) {
			add.setEnabled(true);
		}
		else {
			add.setEnabled(false);
		}
		if(selected_existing_env ==  null) {
			edit.setEnabled(false);
			delete.setEnabled(false);
		}
		else {
			edit.setEnabled(true);
			delete.setEnabled(true);
		}
		return true;
	}

	/**
	 * @return
	 */
	private boolean isValidName(EnvType t){
		// check for empty names
		if(name.getText().length() == 0){
			setErrorMessage("The Name field can't be empty.");
			return false;
		}

		for(ExistingEnv e : existing) {
			if(e.name.compareTo(name.getText())==0) {
				setErrorMessage("Enviornment names must be unique. An enviornment of type "+e.type.str+" with that name already exists.");
				return false;
			}
		}
		setErrorMessage(null);
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	@Override
	public void handleEvent(Event event) {
		//no op
		setPageComplete(isPageComplete());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.MouseListener#mouseDoubleClick(org.eclipse.swt.events.MouseEvent)
	 */
	@Override
	public void mouseDoubleClick(MouseEvent e) {
		//no op
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.MouseListener#mouseDown(org.eclipse.swt.events.MouseEvent)
	 */
	@Override
	public void mouseDown(MouseEvent e) {
		//no op
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
			if(v == existing_viewer) {
				if(sel.length==1) {
					selected_existing_env=(ExistingEnv)sel[0].getData();
					edit.setEnabled(true);
					delete.setEnabled(true);
				}
				else {
					selected_existing_env=null;
					edit.setEnabled(false);
					delete.setEnabled(false);
				}
			}
			else if(v == env_viewer){
				if(sel.length==1) {
					selected_env_type=(EnvType)sel[0].getData();
					add.setEnabled(true);
				}
				else {
					selected_env_type=null;
					add.setEnabled(false);
				}
			}
		}
		setPageComplete(isPageComplete());

	}


	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.MouseListener#mouseUp(org.eclipse.swt.events.MouseEvent)
	 */
	@Override
	public void mouseUp(MouseEvent event) {
		if(event.widget == add) {
			if(selected_env_type != null && name.getText().length()>0) {
				if(isValidName(selected_env_type)) {
					ExistingEnv new_env = new ExistingEnv(name.getText(), selected_env_type, null);
					FormWizardDialog dialog = new FormWizardDialog(shell, new FormWizard(new_env,1,null));
					dialog.open();
					if(new_env.file != null) {
						existing.add(new_env);
						existing_viewer.add(new_env);
						name.setText("");
						add.setEnabled(false);
					}
				}
			}
		}
		else if(event.widget == edit) {
			if(selected_existing_env != null) {
				FormWizardDialog dialog = new FormWizardDialog(shell, new FormWizard(selected_existing_env,0,null));
				//dialog.setPageSize(650, 300);
				dialog.open();
				existing_viewer.refresh(selected_existing_env);
			}
			delete.setEnabled(false);
			edit.setEnabled(false);
		}
		else if(event.widget == delete) {
			if(selected_existing_env != null) {
				String title = "Delete environment?";
				String message = "Are you sure you would like to delete '" +selected_existing_env.toString()+"' ?";
				if (Util.confirm(title, message)) {
					EnvironmentModifier.deleteEnvironment(selected_existing_env.type.str, selected_existing_env.name);
					existing.remove(selected_existing_env);
					existing_viewer.remove(selected_existing_env);
				}
			}
			delete.setEnabled(false);
			edit.setEnabled(false);
		}
		
	}	
}
