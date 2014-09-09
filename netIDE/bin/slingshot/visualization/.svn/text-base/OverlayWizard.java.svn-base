package slingshot.visualization;

import java.util.HashMap;

import jprime.util.GraphOverlay;
import jprime.util.GraphOverlay.OverlayInfo;
import jprime.util.TraceRouteParser;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import slingshot.Util;
import slingshot.Util.Type;
import slingshot.experiment.ProjectLoader;
import slingshot.experiment.PyExperiment;

/**
 * This class defines the Wizard Page that asks for data related to launching an experiment
 *
 * @author Nathanael Van Vorst
 */
public class OverlayWizard extends Wizard implements INewWizard  {
	public static class OverlayWizardDialog extends WizardDialog {
		public OverlayWizardDialog(Shell parentShell, OverlayWizard newWizard) {
			super(parentShell, newWizard);
		}
	}

	/** The name displayed on top of the Wizard's window */
	private static final String WIZARD_NAME = "Add Overlays";
	
	private AddOverlay pageOne; 

	/**
	 * The Constructor for this class.
	 * It sets the Wizard Name
	 */
	public OverlayWizard() {
		setWindowTitle(WIZARD_NAME);
	}

	/**
	 * Adds the pages that the Wizard will display.
	 */
	@Override
	public void addPages(){
		super.addPages();
		//create and add the wizard pages
		pageOne = new AddOverlay(WIZARD_NAME);
		addPage(pageOne);
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
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#canFinish()
	 */
	public boolean canFinish(){
		return pageOne.isPageComplete();
	}
	
	/**
	 * Method called as soon as the Wizard is finished.
	 * Launches an experiment to the selected environment
	 *
	 * @return boolean				TRUE if the Wizard finished correctly, FALSE otherwise
	 */
	@Override
	public boolean performFinish() {
		try {
			String data = pageOne.getOverlayData();
			if(data != null && data.length()>0) {
				if(pageOne.isTraceRoute()) {
					data = TraceRouteParser.createOverlayInfo(data,pageOne.getColor(),1.5);
				}
				PyExperiment pyexp = ProjectLoader.getProjectState(ProjectLoader.getCurrentExpName()).exp;
				HashMap<Long,OverlayInfo> oldo = pyexp.getOverlay();
				HashMap<Long,OverlayInfo> newo = GraphOverlay.parse(pyexp.getRootNode(), data);
				if(oldo == null) {
					pyexp.setOverlay(newo);
				}
				else {
					HashMap<Long,OverlayInfo> t = new HashMap<Long, GraphOverlay.OverlayInfo>();
					t.putAll(oldo);
					t.putAll(newo);
					pyexp.setOverlay(t);
				}
			}
		} catch (Exception e) {
			Util.dialog(Type.ERROR,
					"Encountered unexpected exception while parsing overlay data.",Util.getStackTraceAsString(e));
			return false;
		}
		return true;
	}
}
