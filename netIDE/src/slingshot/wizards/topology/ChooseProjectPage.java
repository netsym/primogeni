package slingshot.wizards.topology;

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

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import slingshot.experiment.ProjectLoader;

/**
 * This class defines the Wizard Page that the project user wants to import the topology models.
 * @author Hao Jiang
 */
public class ChooseProjectPage extends WizardPage implements Listener
{
	public static final String pagename="ChooseTargetModelName";
	
	/**The model name**/
	Text modelnameText;

	/**
	 * The Constructor for this class.
	 * Sets the title and the description of this wizard page.
	 *
	 * @param pageName				The name of this wizard page
	 */
	protected ChooseProjectPage() 
	{
		super(pagename);
		setTitle("Choose Target Model Name");
		setDescription("Choose Target Model Name. ");
	}

	/**
	 * Creates the visual components for this Wizard Page
	 *
	 * @param parent				The parent element for this Wizard Page
	 */
	public void createControl(Composite parent)  
	{
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

        setPageComplete(canFlipToNextPage());
        setErrorMessage(null);	// should not initially have error message

        setControl(composite);

	}
	/**
	 * Handles any event that occurs inside this Wizard Page.
	 * Events may come from the text fields or the browse buttons.
	 *
	 * @param event					The event
	 */
	@Override
	public void handleEvent(Event event) 
	{
		//get the source of the event
		//Widget source = event.widget;
        setPageComplete(canFlipToNextPage());
	}

	
	
	/**
	 * Creates a container to hold the label, text field and browse button for the Project selection and the Enviroment selection Combo
	 *
	 * @param parent				The wizard Page's container
	 */
	private void createOptionsGroup(Composite parent) 
	{
		// container specification group
        final Composite containerGroup = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        containerGroup.setLayout(layout);
        containerGroup.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));
        containerGroup.setFont(parent.getFont());
              
        //create container button
        /*
        Button createProjectButton = new Button(containerGroup, SWT.PUSH);
        createProjectButton.addSelectionListener(new SelectionAdapter() 
        {
        	@Override
        	public void widgetSelected(SelectionEvent e) 
        	{
        	    WizardDialog dialog = new WizardDialog(containerGroup.getShell(),
        		            						   new CreateProjectWizard());
        	    dialog.open();
        	}
        });
        
        createProjectButton.setText("Create new Project");
        createProjectButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        createProjectButton.setFont(parent.getFont());
        setButtonLayoutData(createProjectButton);
        */
        Label modelnameLabel = new Label(containerGroup, SWT.FILL);
        modelnameLabel.setText("Model Name:");
        modelnameLabel.setFont(parent.getFont());
        
        modelnameText = new Text(containerGroup, SWT.SINGLE | SWT.BORDER);
        GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        gridData.horizontalSpan = 1;
        modelnameText.setLayoutData(gridData);
        modelnameText.setFont(parent.getFont());
        
        Label desc = new Label(containerGroup, SWT.FILL);
        desc.setText("All models will be placed in '"+ProjectLoader.GENERATED_MODELS+"' in your current workspace.");
        desc.setFont(parent.getFont());
        GridData bgridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        bgridData.horizontalSpan = 2;
        desc.setLayoutData(bgridData);
	}

	/**
	 * Adds event listeners to the components that require them
	 */
	private void addListeners()
	{
		modelnameText.addListener(SWT.Modify, this);
		modelnameText.addListener(SWT.KeyUp, this);
	}

	/**
	 * Determines whether conditions are met to flip to the next Wizard's Page
	 *
	 * @return boolean				TRUE if the user can flip to the next page, FALSE otherwise
	 */
	public boolean canFlipToNextPage()
	{
		return isValidProject();
	}

	/**
	 * Obtains the next Page for this Wizard and initializes the data it requires.
	 */
	public IWizardPage getNextPage()
	{
		((TopoWizard)getWizard()).projectmodelpath = ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString()
			+File.separator+ProjectLoader.GENERATED_MODELS+File.separator;
		((TopoWizard)getWizard()).modelname = this.modelnameText.getText();
		
		return super.getNextPage();
	}


	/**
	 * Determines whether the selected Project is valid.
	 *
	 * @return boolean				TRUE if the selected Project is valid, FALSE otherwise
	 */
	private boolean isValidProject()
	{
		
		//get model name
		String modelname = this.modelnameText.getText();
			
		
		if(modelname.length() == 0)
		{
			setErrorMessage("Model name can't be empty.");
			return false;
		}
		else
		{
			final String projectmodelpath = ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString()
			+File.separator+ProjectLoader.GENERATED_MODELS+File.separator;
	    	File file = new File(projectmodelpath + modelname + ".xml");
    		if (file.exists())
    		{
    			setErrorMessage("Model name already exists");
    			return false;
    		}
		}
		setErrorMessage(null);
		return true;
	}
}
