package slingshot.wizards.imports;

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

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
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

import slingshot.wizards.widgets.MyContainerSelectionDialog;

/**
 * This class defines the Wizard Page that asks for the Source and Destination of the File the user wants to import.
 *
 * @author Eduardo Pena
 * @version 1.0.0
 *
 */
@SuppressWarnings("restriction")
public class ImportSourceFilePage extends WizardPage implements Listener{

	/** The path of the selected source file */
	private String sourceFilePath;

	/** The selected source file */
	private File sourceFile;
	/** The selected destination file */
	private IFile destinationFile;

	/** The source file field */
	private Text sourceField;
	/** The destination file field */
	private Text destinationField;

	/** The source field browse button */
	private Button sourceBrowseButton;
	/** The destination field browse button */
	private Button destinationBrowseButton;

	/** The selected destination project */
	IProject destinationProject;

	/**
	 * The Constructor for this class.
	 * Sets the title and the description of this wizard page.
	 *
	 * @param pageName				The name of this wizard page
	 */
	protected ImportSourceFilePage(String pageName) {
		super(pageName);
		setTitle("Import Model");
		setDescription("Select the Slingshot Experiment Project.");
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
		//check if the source was the destination browse button
        if (source == destinationBrowseButton)
			handleDestinationBrowseButtonPressed();
        //check if the source was the source browse button
        if (source == sourceBrowseButton)
			handleSourceBrowseButtonPressed();
        setPageComplete(isPageComplete());
	}

	/**
	 * This method is called when a user clicks on the destination browse button.
	 * It opens a Dialog that lets the user choose a project from the Workspace.
	 */
	private void handleDestinationBrowseButtonPressed(){
		//get the path of a project selected by the user
		IPath path =  queryForContainer(null,"Select the Slingshot Project","Project Selection");
		//check if the path is not null
		if(path != null){
			destinationProject = getProjectFromPath(path);
			destinationField.setText(destinationProject.getName());
		}

	}

	/**
	 * This method is called when a user clicks on the source browse button.
	 * It opens a Dialog that lets the user choose a file from his/her computer.
	 */
	private void handleSourceBrowseButtonPressed(){
		//create the file selection Dialog
		FileDialog fileDialog = new FileDialog(getControl().getShell());
		fileDialog.setFilterExtensions(new String[] {"*.py;*.xml;*.java", "*"});

		//get the path of the file selected by the user
        sourceFilePath = fileDialog.open();
        if(sourceFilePath == null)
        	sourceFilePath = "";
    	//check if the path is not null
    	if (sourceFilePath.length() > 0 ){
    		//get the file name from that path
    		String fileName = sourceFilePath.substring(sourceFilePath.lastIndexOf('/')+1);
    		setErrorMessage(null);
    		sourceField.setText(fileName);
    	}
	}

	/**
	 * This method opens the dialog with the projects in the Workspace and lets the user choose one.
	 *
	 * @param initialSelection		The selection the user had before opening the dialog
	 * @param msg					A message displayed on the dialog to the user
	 * @param title					The title of this dialog
	 * @return IPath				The path of the selected project
	 */
	private IPath queryForContainer(IContainer initialSelection, String msg, String title) {
		//create and set the new dialog
        MyContainerSelectionDialog dialog = new MyContainerSelectionDialog(getControl().getShell(), initialSelection,false, msg);
		dialog.setTitle(title);
        dialog.showClosedProjects(false);
        dialog.open();
        Object[] result = dialog.getResult();
        if (result != null && result.length == 1) {
            return (IPath) result[0];
        }
        return null;
    }

	/**
	 * Returns an IProject from a given path.
	 *
	 * @param path					The path of the Project
	 * @return IProject				The IProject obtained from the path
	 */
	private IProject getProjectFromPath(IPath path){
		//get all Projects from the workspace
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		//find the one that is defined by the given path
		for(IProject p : projects){
			String pathString = path.toString();
			if(pathString.contains(p.getName())){
				return p;
			}
		}
		return null;
	}

	/**
	 * Returns an IProject from a given Project name.
	 *
	 * @param name					The name of the Project
	 * @return IProject				The IProject obtained from the name
	 */
	private IProject getProjectFromName(String name){
		//get all Projects from the workspace
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		//find the one defined by the given name
		for(IProject p : projects){
			if(p.getName().equalsIgnoreCase(name)){
				return p;
			}
		}
		return null;
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

        //create the text field and browse button for source and destination
        createSelectionGroup(composite);

        //add the event listeners
        addListeners();

        setPageComplete(canFlipToNextPage());
        setErrorMessage(null);	// should not initially have error message

        setControl(composite);

	}

	/**
	 * Creates a container to hold the label, text field and browse button for the source selection and destination project selection
	 *
	 * @param parent				The wizard Page's container
	 */
	private void createSelectionGroup(Composite parent){
		// container specification group
        Composite containerGroup = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 3;
        containerGroup.setLayout(layout);
        containerGroup.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));
        containerGroup.setFont(parent.getFont());

        // container label (source)
        Label resourcesLabel = new Label(containerGroup, SWT.NONE);
        resourcesLabel.setText("Model File:");
        resourcesLabel.setFont(parent.getFont());

        // container name entry field (source)
        sourceField = new Text(containerGroup, SWT.SINGLE | SWT.BORDER);
        GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
        sourceField.setLayoutData(data);
        sourceField.setFont(parent.getFont());

        // container browse button (source)
        sourceBrowseButton = new Button(containerGroup, SWT.PUSH);
        sourceBrowseButton.setText(IDEWorkbenchMessages.WizardImportPage_browse2);
        sourceBrowseButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
        sourceBrowseButton.setFont(parent.getFont());
        setButtonLayoutData(sourceBrowseButton);

        // container label (destination)
        Label resourcesLabelDest = new Label(containerGroup, SWT.NONE);
        resourcesLabelDest.setText("Destination Project:");
        resourcesLabelDest.setFont(parent.getFont());

        // container name entry field (destination)
        destinationField = new Text(containerGroup, SWT.SINGLE | SWT.BORDER);
        GridData dataDest = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
        destinationField.setLayoutData(dataDest);
        destinationField.setFont(parent.getFont());

        // container browse button (destination)
        destinationBrowseButton = new Button(containerGroup, SWT.PUSH);
        destinationBrowseButton.setText(IDEWorkbenchMessages.WizardImportPage_browse2);
        destinationBrowseButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
        destinationBrowseButton.setFont(parent.getFont());
        setButtonLayoutData(destinationBrowseButton);

	}


	/**
	 * Adds event listeners to the components that require them
	 */
	private void addListeners(){
		destinationField.addListener(SWT.KeyUp, this);
		destinationField.addListener(SWT.Modify, this);
		destinationBrowseButton.addListener(SWT.Selection, this);

		sourceField.addListener(SWT.KeyUp, this);
		sourceField.addListener(SWT.Modify, this);
		sourceBrowseButton.addListener(SWT.Selection, this);
	}

	/**
	 * Determines whether conditions are met to flip to the next Wizard's Page
	 *
	 * @return boolean				TRUE if the user can flip to the next page, FALSE otherwise
	 */
	public boolean canFlipToNextPage(){
		//there is no next step on this wizard
		return false;
	}

	/**
	 * Determines whether the selected destination is valid.
	 *
	 * @return boolean				TRUE if the selected destination is valid, FALSE otherwise
	 */
	private boolean isValidDestination(){
		//get the text from the destination field
		String text = destinationField.getText();
		//check for empty field
		if(text.length() == 0){
			setErrorMessage("Project name can't be empty.");
			return false;
		}
		else{
			//get the selected Project
			destinationProject = getProjectFromName(text);
			if(destinationProject == null){
				setErrorMessage("The specified project does not exist.");
				return false;
			}
			else{
				//create a virtual IFile in the Source foler of the selected Project
				destinationFile = destinationProject.getFile(sourceField.getText());
				if(destinationFile == null)
					return false;
				return true;
			}
		}


	}

	/**
	 * Determines whether the selected source is valid.
	 *
	 * @return boolean				TRUE if the selected source is valid, FALSE otherwise
	 */
	private boolean isValidSource(){
		//get a File from the source file path
		sourceFile = new File(sourceFilePath);
		//check if its not null
		if(sourceFile == null)
			return false;
		return true;
	}

	/**
	 * Determines whether this page has been completed
	 *
	 * @return boolean				TRUE if the page is complete, FALSE otherwise
	 */
	public boolean isPageComplete(){
		if(isValidDestination() && isValidSource()){
			return true;
		}
		return false;
	}

	/**
	 * Returns the selected source File.
	 * @return File					The selected source File
	 */
	public File getSourceFile(){
    	return sourceFile;
    }

	/**
	 * Returns the selected destination File.
	 * @return IFile				The selected destination IFile
	 */
    public IFile getDestinationFile(){
    	return destinationFile;
    }

}
