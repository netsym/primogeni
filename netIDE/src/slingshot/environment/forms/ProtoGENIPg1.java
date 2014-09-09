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

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import monitor.util.ManifestParser;
//import monitor.util.ManifestParserGeniv3;

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
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
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
import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;

//import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Combo;

import slingshot.Util;
import slingshot.Util.Type;
import slingshot.environment.EnvironmentFileModel;
import slingshot.environment.forms.FormWizard.FormWizardDialog;

/**
 * 
 * The form of local environments.
 * 
 * @author Nathanael Van Vorst
 * 
 */
@SuppressWarnings("restriction")
public class ProtoGENIPg1 extends BaseForm {
	protected Set<String> files = new HashSet<String>();
	private String selected = null;
	private TableViewer viewer;
	private TableViewerColumn viewerColumn;
	private Button add, delete, browse;
	private Text manifest;// ,manifest2;
	private Shell shell;
	private BasePage page;
	private Combo ManifestVersionList;
	private int ParserChoice;// selected version of Manifest

	/**
	 * 
	 */
	public ProtoGENIPg1() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * slingshot.environment.forms.BaseForm#createControl(org.eclipse.swt.widgets
	 * .Composite, slingshot.environment.forms.BasePage)
	 */
	public Composite createControl(Composite parent, BasePage page, Display d) {
		this.page = page;
		this.shell = parent.getShell();
		FormToolkit toolkit = new FormToolkit(d);
		Form form = toolkit.createForm(parent);
		Composite composite = form.getBody();
		composite.setLayout(new FillLayout());
		composite.setFont(parent.getFont());

		parent.setBackground(toolkit.getColors().getBackground());
		parent.getParent().setBackground(toolkit.getColors().getBackground());

		GridData gd = null;
		Table t = null;

		Section sec = toolkit.createSection(composite, Section.DESCRIPTION
				| Section.TITLE_BAR);
		sec.setLayout(new FillLayout(SWT.CENTER));
		sec.setText("Manifests");
		sec.setDescription("All manifests in the following list will be converted into a ProtoGENI enviornment.");

		Composite sec_client = toolkit.createComposite(sec, SWT.WRAP);
		sec_client.setLayout(new GridLayout(4, false));
		t = toolkit.createTable(sec_client, SWT.FILL);
		gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd.horizontalSpan = 4;
		gd.verticalSpan = 4;
		t.setLayoutData(gd);
		toolkit.paintBordersFor(sec_client);
		Label l = toolkit.createLabel(sec_client, "Manifest:");
		gd = new GridData();
		gd.horizontalSpan = 1;
		l.setLayoutData(gd);
		manifest = toolkit.createText(sec_client, "");
		gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		gd.horizontalSpan = 2;
		manifest.setLayoutData(gd);
		browse = toolkit.createButton(sec_client,
				IDEWorkbenchMessages.WizardImportPage_browse2, SWT.PUSH);
		gd = new GridData();
		gd.horizontalSpan = 1;
		browse.setLayoutData(gd);
		page.mySetButtonLayoutData(browse);

		// Label l2 = toolkit.createLabel(sec_client,
		// "Select Geni Manifest Version");
		// gd = new GridData();
		// gd.horizontalSpan=4;
		// l2.setLayoutData(gd);
		String SupportedGeniManifestVersions[] = { "Geni Rspec v2.0", "Geni Rspec v3" };
		// String SupportedGeniManifestVersions[]=
		// {"Geni Rspec v2.0","Geni Rspec v3","Option1-All Aggregate",
		// "Exogeni FLukes Rspec"};
		ManifestVersionList = new Combo(shell, SWT.DROP_DOWN | SWT.READ_ONLY);
		for (int i = 0; i < SupportedGeniManifestVersions.length; i++) {
			ManifestVersionList.add(SupportedGeniManifestVersions[i]);
		}
		ManifestVersionList.select(0);

		// gd = new GridData(SWT.FILL, SWT.FILL,true,false);
		// gd.horizontalSpan=4;
		// ManifestVersionList.setLayoutData(gd);

		viewer = new TableViewer(t);
		viewer.setContentProvider(new ArrayContentProvider());
		viewerColumn = new TableViewerColumn(viewer, SWT.NONE);
		viewerColumn.getColumn().setWidth(300);
		viewerColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				String f = (String) element;
				return f.substring(f.lastIndexOf("/") + 1);
			};

			public Image getImage(Object element) {
				return PlatformUI.getWorkbench().getSharedImages()
						.getImage(ISharedImages.IMG_OBJ_ELEMENT);
			}
		}

		);
		viewer.setInput(composite);

		add = toolkit.createButton(sec_client, "Add", SWT.PUSH);
		delete = toolkit.createButton(sec_client, "Delete", SWT.PUSH);
		// ManifestVersionList=toolkit.createCombo

		add.setEnabled(false);
		delete.setEnabled(false);

		sec.setClient(sec_client);
		composite.setSize(400, 400);
		return composite;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * slingshot.environment.forms.BaseForm#addListeners(org.eclipse.swt.widgets
	 * .Listener)
	 */
	public void addListeners(Listener l) {
		viewer.addSelectionChangedListener((ISelectionChangedListener) l);
		add.addMouseListener((MouseListener) l);
		delete.addMouseListener((MouseListener) l);
		browse.addMouseListener((MouseListener) l);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * slingshot.environment.forms.BaseForm#init(org.eclipse.core.resources.
	 * IFile)
	 */
	@Override
	public EnvironmentFileModel init(IFile configurationFile) {
		throw new RuntimeException("don't call this");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.WizardPage#isPageComplete()
	 */
	public String isPageComplete() {
		delete.setEnabled(selected != null);
		if (manifest.getText().length() > 0) {
			File file = new File(manifest.getText());
			if (!file.exists()) {
				add.setEnabled(false);
				return "Certificate does not exist.";
			} else {
				add.setEnabled(true);
			}
		} else {
			add.setEnabled(false);
		}
		if (files.size() == 0)
			return "Must enter at least one manifest to import.";
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(
	 * org.eclipse.jface.viewers.SelectionChangedEvent)
	 */
	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		Object src = event.getSource();
		if (src instanceof TableViewer) {
			TableViewer v = (TableViewer) src;
			TableItem[] sel = v.getTable().getSelection();
			if (sel.length == 1) {
				selected = (String) sel[0].getData();
				delete.setEnabled(true);
				manifest.setText(selected);
			} else {
				selected = null;
				delete.setEnabled(false);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.MouseListener#mouseUp(org.eclipse.swt.events.
	 * MouseEvent)
	 */
	@Override
	public void mouseUp(MouseEvent event) {
		if (event.widget == add) {
			if (manifest.getText().length() > 0) {
				File file = new File(manifest.getText());
				if (file.exists()) {
					if (files.add(manifest.getText())) {
						viewer.add(manifest.getText());
					}
				}
			}
			manifest.setText("");
			add.setEnabled(false);

			ParserChoice = ManifestVersionList.getSelectionIndex();

		} else if (event.widget == delete) {
			if (selected != null) {
				files.remove(selected);
				viewer.remove(selected);
			}
			selected = null;
			delete.setEnabled(false);
		} else if (event.widget == browse) {
			// create the file selection Dialog
			FileDialog fileDialog = new FileDialog(shell);
			fileDialog.setFilterExtensions(new String[] { "*.rspec", "*.xml" });

			// get the path of the file selected by the user
			String f = fileDialog.open();
			// get the file name from that path
			if (f == null) {
				f = "";
			}
			manifest.setText(f);
			add.setEnabled(f.length() > 0);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * slingshot.environment.forms.BaseForm#handleEvent(org.eclipse.swt.widgets
	 * .Event, slingshot.environment.forms.BasePage)
	 */
	public void handleEvent(Event e, BasePage page) {
		// no op
	}

	// OBAIDA xx<>xx SENDING FILES to MANIFEST PARSER in JPRIME to parse geni
	// nodes and links

	public void openNextPage() {
		ArrayList<File> filelist = new ArrayList<File>();
		for(String f : files) {
			filelist.add(new File(f));
		}
		//if v2
		//ManifestParser p;
		
		ManifestParser p2;
		//ManifestParserGeniv3 p3;
		
		//read DropDown list menu
        //int x=0;
		
//		if (x==0)
//		  {
		   
		   //ParserChoice=2;
		   int manifest_version=ParserChoice+2;//parser choise is ascertained with Mouseup event
			
			p2 = new ManifestParser(filelist,manifest_version);
			
			if(p2.getParseErrors().size()>0) {
				String message = "";
				for(String e : p2.getParseErrors()) {
					message+=e+"\n";
				}
				Util.dialog(Type.ERROR, "WARNING: There were problems parsing 1 or more xml elements in the manifest(s). The errors follow:", message);
			}
			FormWizardDialog dialog = new FormWizardDialog(shell, new FormWizard(page.env,2,p2));
			dialog.open();
			
//			
//		  }
//		else
//			{
//			p3 = new ManifestParserGeniv3(filelist);
//			if(p3.getParseErrors().size()>0) {
//				String message = "";
//				for(String e : p3.getParseErrors()) {
//					message+=e+"\n";
//				}
//				Util.dialog(Type.ERROR, "WARNING: There were problems parsing 1 or more xml elements in the manifest(s). The errors follow:", message);
//			}
//			FormWizardDialog dialog = new FormWizardDialog(shell, new FormWizard(page.env,4,p3));  //OBAIDA 4= geniv3 in FormWizard.java
//			dialog.open();
//			
//			}
		//if v3
		
//		if(p.getParseErrors().size()>0) {
//			String message = "";
//			for(String e : p.getParseErrors()) {
//				message+=e+"\n";
//			}
//			Util.dialog(Type.ERROR, "WARNING: There were problems parsing 1 or more xml elements in the manifest(s). The errors follow:", message);
//		}
//		FormWizardDialog dialog = new FormWizardDialog(shell, new FormWizard(page.env,2,p));
//		dialog.open();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * slingshot.environment.forms.BaseForm#saveDataToModel(slingshot.environment
	 * .EnvironmentFileModel, java.lang.String)
	 */
	public void saveDataToModel(EnvironmentFileModel model, String name) {
	}

	@Override
	public boolean canFlipToNextPage() {
		// TODO Auto-generated method stub
		return false;
	}
}
