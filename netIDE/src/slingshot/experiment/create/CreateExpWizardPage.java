package slingshot.experiment.create;

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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;

import slingshot.experiment.ProjectLoader;

/**
 * This class defines the Wizard Page that asks for the Project that will contain the new Experiment.
 *
 * @author Eduardo Pena
 */
@SuppressWarnings("restriction")
public class CreateExpWizardPage extends WizardPage implements Listener {

	/** The browse Button for the container field */
	private Button modelBrowseButton;
	/** The name field */
	private Text experimentNameField;
	/** The model selection combo box */
	private Text modelField;

	private String experimentName = "";
	private String modelFilePath = "";

	/**
	 * The Constructor for this class.
	 * Sets the title and the description of this wizard page.
	 *
	 * @param pageName				The name of this wizard page
	 */
	protected CreateExpWizardPage(String pageName, IStructuredSelection selection) {
		super(pageName);
		setTitle("Create Experiment");
		setDescription("Specify the new Experiment name. Select a model file from your file system to create an experiment with a defined Model, or leave the field empty to create an empty Experiment.");
	}

	/**
	 * Handles any event that occurs inside this Wizard Page.
	 * Events may come from the text fields or the browse buttons.
	 *
	 * @param event					The event
	 */
	@Override
	public void handleEvent(Event event) {
		//get the source of the event
		Widget source = event.widget;
		//check if the source was the browse button
        if (source == modelBrowseButton) {
			handleModelBrowseButtonPressed();
		}
        setPageComplete(isPageComplete());
	}

	/**
	 * This method is called when a user clicks on the container browse button.
	 * It opens a Dialog that lets the user choose a project from the Workspace.
	 */
	private void handleModelBrowseButtonPressed(){
		//create the file selection Dialog
		FileDialog fileDialog = new FileDialog(getControl().getShell());
		fileDialog.setFilterExtensions(new String[] {"*.py;*.xml;*.java;*.class"});

		//get the path of the file selected by the user
		String t = ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString()+
				File.separator + ProjectLoader.GENERATED_MODELS;		
		//System.out.println("FILTER PATH:"+t);
		fileDialog.setFilterPath(t);
		//TODO: LIU: Eclipse has problem with with FileDialog on OS X Mavericks.
		//See https://bugs.eclipse.org/bugs/show_bug.cgi?id=420682 for details.
		//Need to upgrade Eclipse to remove the bug
		
		modelFilePath = fileDialog.open();
        if(modelFilePath == null) {
        	modelFilePath = "";
        }    		
    	//check if the path is not null
    	if (modelFilePath.length() > 0 ) {
    		//get the file name from that path
    		String fileName = modelFilePath.substring(modelFilePath.lastIndexOf('/')+1);
    		setErrorMessage(null);
    		modelField.setText(fileName);
    	}
	}
	/**
	 * Creates the visual components for this Wizard Page
	 *
	 * @param parent				The parent element for this Wizard Page
	 */
	@Override
	public void createControl(Composite parent) {
		initializeDialogUnits(parent);
		//Create the container and set the Layout
        Composite composite = new Composite(parent, SWT.NULL);
        composite.setLayout(new GridLayout());
        composite.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_FILL | GridData.HORIZONTAL_ALIGN_FILL));
        composite.setSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        composite.setFont(parent.getFont());

        //create the project text field, browse button component, and the environment combo
        createOptionsGroup(composite);

        //add the event listeners
        addListeners();

        setErrorMessage(null);	// should not initially have error message
        setPageComplete(isPageComplete());

        setControl(composite);

	}

	/**
	 * Creates a container to hold the label, text field and browse button for the Project selection and the Enviroment selection Combo
	 *
	 * @param parent				The wizard Page's container
	 */
	private void createOptionsGroup(Composite parent) {
		// container specification group
        Composite containerGroup = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 3;
        containerGroup.setLayout(layout);
        containerGroup.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));
        containerGroup.setFont(parent.getFont());

        GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);

		GridData separatorData = new GridData(GridData.FILL_HORIZONTAL);
		separatorData.horizontalSpan = 3;

		// experiment name label
		Label nameLabel = new Label(containerGroup, SWT.NONE);
		nameLabel.setText("Experiment Name:");
		nameLabel.setFont(parent.getFont());

		// experiment name entry field
		experimentNameField = new Text(containerGroup, SWT.SINGLE | SWT.BORDER);
		experimentNameField.addListener(SWT.Modify, this);
		experimentNameField.setLayoutData(data);
		experimentNameField.setFont(parent.getFont());

        // Horizontal separator line
		Label separator1 = new Label(containerGroup, SWT.SEPARATOR
				| SWT.HORIZONTAL | SWT.LINE_SOLID);
		separator1.setLayoutData(separatorData);

		// container label
        Label resourcesLabel = new Label(containerGroup, SWT.NONE);
        resourcesLabel.setText("Model file:");
        resourcesLabel.setFont(parent.getFont());

        // container name entry field
        modelField = new Text(containerGroup, SWT.SINGLE | SWT.BORDER);
        modelField.setLayoutData(data);
        modelField.setFont(parent.getFont());

        // container browse button
        modelBrowseButton = new Button(containerGroup, SWT.PUSH);
        modelBrowseButton.setText(IDEWorkbenchMessages.WizardImportPage_browse2);
        modelBrowseButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
        modelBrowseButton.setFont(parent.getFont());
        setButtonLayoutData(modelBrowseButton);
	}

	/**
	 * Adds event listeners to the components that require them
	 */
	private void addListeners(){
		experimentNameField.addListener(SWT.KeyUp, this);
		experimentNameField.addListener(SWT.Modify, this);

		modelField.addListener(SWT.KeyUp, this);
		modelField.addListener(SWT.Modify, this);

		modelBrowseButton.addListener(SWT.Selection, this);
	}

	/**
	 * Determines whether this page has been completed
	 *
	 * @return boolean TRUE if the page is complete, FALSE otherwise
	 */
	public boolean isPageComplete() {
		// check whether everything is OK to complete the page and if it is save
		// the data in this Wizard to the model
		if(isFileValid() && isValidName()){
			saveData();
			return true;
		}
		return false;
	}

	/**
	 * Determines whether conditions are met to flip to the next Wizard's Page
	 *
	 * @return boolean				TRUE if the user can flip to the next page, FALSE otherwise
	 */
	public boolean canFlipToNextPage(){
		return false;
	}

	/**
	 * Saves the data obtained from this Wizard Page to the LoadExperimentWizard model.
	 *
	 */
	private void saveData(){
		experimentName = experimentNameField.getText();
	}

	/**
	 * Determines whether the Source Files and Configuration File selected by
	 * the user are Valid.
	 *
	 * @return boolean TRUE if the files are valid, FALSE otherwise
	 */
	private boolean isFileValid() {
		// get the source files and configuration file
		String model = modelField.getText();

		File f1 = new File(model);
		File f2 = new File(modelFilePath);

		if( !model.isEmpty()){
			if( !(f1.exists() || f2.exists()) ){
				setErrorMessage("Specified Model file does not exist in the file system.");
				return false;
			}
		}
		setErrorMessage(null);
		return true;
	}

	/**
	 * Determines whether the selected name for the New Experiment is valid. An
	 * Experiment with the selected name must not exist in the workspace.
	 *
	 * @return boolean TRUE if the name is valid, FALSE otherwise
	 */
	private boolean isValidName() {
		String text = experimentNameField.getText();

		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		// check for empty names
		if (text.length() == 0) {
			setErrorMessage("Experiment name can't be empty");
			return false;
		} else{
			for(IProject p : projects){
				if(p.getName().equalsIgnoreCase(text)){
					setErrorMessage("Experiment already exists!");
					return false;
					//we can never have to projects with the same name!
				}
			}
		}
		setErrorMessage(null);
		return true;

	}

	/**
	 * @return
	 */
	public String getExperimentName(){
		return experimentName;
	}

	/**
	 * @return
	 */
	public String getModelFilePath(){
		return modelFilePath;
	}

}
