package slingshot.wizards.configuration;

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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;

import slingshot.configuration.ConfigurationHandler;

/**
 * This class defines the Wizard Page that asks for the necessary configuration parameters in the Slingshot Configuration Wizard.
 *
 * @author Eduardo Pena
 * @author Neil Goldman
 *
 */
@SuppressWarnings("restriction")
public class SlingshotConfigGetValuesPage extends WizardPage implements Listener {

    /** The text field for the primex directory parameter */
	private Text primexDirectoryField;
	public boolean isPrimexDirectoryFieldEmpty() {
		return primexDirectoryField.getText() == "";
	}

	/** The browse button for the primes directory parameter */
	private Button primexDirBrowseButton;
	
	//The current save directory for Experiments
	//TODO
	//private Text txtExpDirectory;
	
	//The browse button for the experiment directory parameter
	//private Button btnExpDirBrowseButton;

	/** The text for the primex directory parameter */
	String primexDir = "";
	/** The text for the primex directory. Set initially and never changed, this is used on wizard completion to check if the field has changed */
	public String oldPrimexDir = "";
	/** The text for the experiment directory parameter */
	//TODO
	//String experimentDir = "";

	/**
	 * The Constructor for this class.
	 * Sets the title and the description of this wizard page.
	 * Also sets the initial values for the primex directory and web server fields.
	 *
	 * @param pageName				The name of this wizard page
	 * @param selection				The user's selection before getting to this wizard page
	 * @param webServer				The initial value for the web server field
	 * @param primexDir				The initial value for the primex directory field
	 */
	protected SlingshotConfigGetValuesPage(String pageName, String primexDir, String experimentDir) {
		super(pageName);
		setTitle("Set Preferences");
		setDescription("Select the proper directories.");
		//set the initial values of web server and primex directory fields
		this.primexDir = primexDir;
		this.oldPrimexDir = primexDir;
		//TODO
		//this.experimentDir = experimentDir;
	}

	/**
	 * Handles any event that occurs inside this Wizard Page
	 * Events may come from the text fields or the browse buttons
	 */
	@Override
	public void handleEvent(Event event) {
		
		//get the source of the event
		Widget source = event.widget;
        //check whether the source of the event was the primex directory browse button
        if (source == primexDirBrowseButton)
        	handlePrimexDirBrowseButtonPressed();
        
      //check whether the source of the event was the experiment directory browse button
        //TODO
        //if (source == btnExpDirBrowseButton)
        //	handleExpDirBrowseButtonPressed();
        
        //check if all the page's conditions have been met
		setPageComplete(isPageComplete());
	}


	/**
	 * This method is called when a user clicks on the primex directory field's browse button.
	 * It opens a directoryDialog, which lets the user choose a directory from his/her local disk.
	 */
	private void handlePrimexDirBrowseButtonPressed(){
		//create a new DirectoryDialog
		DirectoryDialog directoryDialog = new DirectoryDialog(getControl().getShell());
        directoryDialog.setMessage("Please select the PRIMEX installation directory.");
        //open the Dialog and get the path of the directory selected by the user
        String dir = directoryDialog.open();
        //set the selected value to the primex directory field
        if(dir != null) {
        	primexDirectoryField.setText(dir);
        }
	}
	
	/**
	 * This method is called when a user clicks on the experiment directory field's browse button.
	 * It opens a directoryDialog, which lets the user choose a directory from his/her local disk.
	 */
	/* TODO
	private void handleExpDirBrowseButtonPressed() {
		//create a new dialog
		DirectoryDialog dialog = new DirectoryDialog(getControl().getShell());
		dialog.setMessage("Please select the save directory for experiment data.");
		//open the Dialog and get the path of the directory selected by the user
		String dir = dialog.open();
		//set the slected value to the experiment directory field
		if (dir != null)
			txtExpDirectory.setText(dir);
	}*/

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

        //create the fields
        createFieldsSelectionGroup(composite);
        //add the event listeners
        addListeners();

        //set the initial values for the web server and primex directory fields
        primexDirectoryField.setText(primexDir);

        //check if the page is complete
        //setPageComplete(isPageComplete());

        setControl(composite);
        setErrorMessage(null);// should not initially have error message
	}

	/**
	 * Creates a container to hold the labels, text fields, and browse buttons for the required parameters
	 *
	 * @param parent				The wizard Page's container
	 */
	protected final void createFieldsSelectionGroup(Composite parent){
		// container specification group
        Composite containerGroup = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 3;
        containerGroup.setLayout(layout);
        containerGroup.setLayoutData(new GridData(
                GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));
        containerGroup.setFont(parent.getFont());

        GridData textFieldGridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);

        // PRIMEX directory label
        Label primexDirLabel = new Label(containerGroup, SWT.NONE);
        primexDirLabel.setText("Primex directory:");
        primexDirLabel.setFont(parent.getFont());

        // PRIMEX directory entry field
        primexDirectoryField = new Text(containerGroup, SWT.SINGLE | SWT.BORDER);
        primexDirectoryField.addListener(SWT.Modify, this);
        primexDirectoryField.setLayoutData(textFieldGridData);
        primexDirectoryField.setFont(parent.getFont());

        // PRIMEX directory browse button
        primexDirBrowseButton = new Button(containerGroup, SWT.PUSH);
        primexDirBrowseButton.setText(IDEWorkbenchMessages.WizardImportPage_browse2);
        primexDirBrowseButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
        primexDirBrowseButton.setFont(parent.getFont());
        setButtonLayoutData(primexDirBrowseButton);        
	}

	/**
	 * Adds event listeners to the components that require them
	 */
	private void addListeners(){
		//primex directory field listeners
		primexDirectoryField.addListener(SWT.KeyUp, this);
		//primexDirectoryField.addListener(SWT.Modify, this);
		//add button listeners
		primexDirBrowseButton.addListener(SWT.Selection, this);
		
		//TODO
		//txtExpDirectory.addListener(SWT.KeyUp, this);
		//btnExpDirBrowseButton.addListner(SWT.Selection, this);
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
		//check for non-empty fields
		if(FieldsNotEmpty()){
			//set the new primex directory values to the ConfigurationHandler
			ConfigurationHandler.setPrimexDirectory(primexDirectoryField.getText());
			return true;
		}
		return false;
	}

	/**
	 * Checks for non-empty fields
	 *
	 * @return boolean				TRUE if both fields are not empty, FALSE otherwise
	 */
	private boolean FieldsNotEmpty(){
		//get the current text from the primex directory fields
		String primexDirectory = primexDirectoryField.getText();
		
		setErrorMessage(null);

		//check whether the primex directory field is empty
		if(primexDirectory.length() == 0){
			setErrorMessage("The Primex Installation directory can't be empty");
			return false;
		}
		/* TODO
		if (experimentDirectory.length() == 0){
			setErroMessage("The Experiment data directory can't be empty");
			return false;
		}*/
		setErrorMessage(null);
		return true;
	}
}
