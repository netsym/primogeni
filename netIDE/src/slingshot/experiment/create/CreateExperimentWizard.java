package slingshot.experiment.create;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import slingshot.experiment.ProjectLoader;
import slingshot.natures.ExperimentNature;
import slingshot.projects.SlingshotProjectCreator;

/**
 * This is the Wizard handler class for the Load Experiment Wizard.
 * The purpose of this Wizard is to load an Experiment from user selected source files and configuration file.
 *
 * @author Eduardo Pena
 */
public class CreateExperimentWizard extends Wizard implements INewWizard {
	public static final String ID = "slingshot.wizards.create.experiment";
	/** The name displayed on top of the Wizard's window */
	private static final String WIZARD_NAME = "Create Experiment";

	/** The workbench in use when the wizard is called */
	IWorkbench _workbench;
	/** The user's selection when the wizard is called */
	IStructuredSelection _selection;

	/** The Wizard Page that asks for the Project that will own the experiment */
	protected CreateExpWizardPage page;

	/** The model to save the relocalConsolequired data */
	CreateExperimentModel model;

	/**
	 * The Constructor for this class.
	 * It sets the Wizard Name and creates a new Model.
	 */
	public CreateExperimentWizard() {
		setWindowTitle(WIZARD_NAME);
		model = new CreateExperimentModel();
		//TODO: prompt for primex dir
	}

	/**
	 * Adds the pages that the Wizard will display.
	 */
	@Override
	public void addPages(){
		super.addPages();
		//create and add the wizard page
		page = new CreateExpWizardPage(WIZARD_NAME,_selection);
		addPage(page);
	}

	/**
	 * Method called when the Wizard is first created.
	 * Gets the workbench and the user's selection.
	 *
	 * @param workbench				The current Workbench
	 * @param selection				The user's selection
	 */
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		_workbench = workbench;
		_selection = selection;
    }

	public boolean canFinish(){
		// based on the environment return the right flag
		return page.isPageComplete();
	}

	/**
	 * Method called as soon as the Wizard is finished.
	 * Creates and loads the new Slingshot Experiment.
	 *
	 * @return boolean				TRUE if the Wizard finished correctly, FALSE otherwise
	 */
	@Override
	public boolean performFinish() {
		//create the new experiment project
		String expName = page.getExperimentName();
		IProject project = SlingshotProjectCreator.createProject(expName, null, ExperimentNature.NATURE_ID);
		model.setProject(project);
		//retrieve the selected file
		String filePath = page.getModelFilePath();
		File file = new File(filePath);
		model.setSourceFile(file);
		//give the current experiment model to the ExperimentLoader so it can load the experiment
		ProjectLoader.createExperiment(model);
		return true;
	}
}