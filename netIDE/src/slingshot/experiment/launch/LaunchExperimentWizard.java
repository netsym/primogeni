package slingshot.experiment.launch;

import java.util.Map;
import java.util.Map.Entry;

import jprime.EmulationProtocol.IEmulationProtocol;
import jprime.TrafficPortal.ITrafficPortal;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import slingshot.experiment.PyExperiment;
import slingshot.wizards.configuration.EditDefaults;

/**
 * This class defines the Wizard Page that asks for data related to launching an experiment
 *
 * @author Nathanael Van Vorst
 */
public class LaunchExperimentWizard extends Wizard implements INewWizard  {
	public static final String ID = "slingshot.wizards.launch.LaunchExperiment";
	/** The name displayed on top of the Wizard's window */
	private static final String WIZARD_NAME = "Launch Experiment";
	private final PyExperiment exp;
	private EnvironmentPicker envPicker=null; 
	private SymbolsPage symbolsPage=null;
	private LocalEnvPage localEnvPage=null;
	private ProtoGeniEnvPage protoGeniEnvPage=null;
	private RemoteEnvPage remoteEnvPage=null;
	private PortalIfaceLinker portalIfaceLinker=null;
	private final IProject configProject;
	private final String resultsPath;

	/**
	 * The Constructor for this class.
	 * It sets the Wizard Name
	 */
	public LaunchExperimentWizard(PyExperiment exp) {
		super();
		setWindowTitle(WIZARD_NAME);
		this.exp=exp;
		this.configProject = ResourcesPlugin.getWorkspace().getRoot().getProject("Configuration");
		this.resultsPath =  ResourcesPlugin.getWorkspace().getRoot().getProject(exp.getName()).getLocation().toOSString();
	}

	/**
	 * Adds the pages that the Wizard will display.
	 */
	@Override
	public void addPages(){
		super.addPages();
		envPicker = new EnvironmentPicker(WIZARD_NAME, exp, configProject);
		symbolsPage = new SymbolsPage(WIZARD_NAME,exp);
		localEnvPage = new LocalEnvPage(WIZARD_NAME);
		protoGeniEnvPage = new ProtoGeniEnvPage(WIZARD_NAME,exp);
		remoteEnvPage = new RemoteEnvPage(WIZARD_NAME,exp);
		portalIfaceLinker = new PortalIfaceLinker(WIZARD_NAME, exp);
		addPage(envPicker);
		addPage(symbolsPage);
		addPage(localEnvPage);
		addPage(protoGeniEnvPage);
		addPage(remoteEnvPage);
		addPage(portalIfaceLinker);
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
	 * @see org.eclipse.jface.wizard.Wizard#getPreviousPage(org.eclipse.jface.wizard.IWizardPage)
	 */
	@Override
	public IWizardPage getPreviousPage(IWizardPage page) {
		if(page == envPicker) {
			return null;
		}
		else if(page == symbolsPage) {
			return envPicker;
		}
		else if(page == localEnvPage) {
			return symbolsPage;
		}
		else if(page == protoGeniEnvPage) {
			return symbolsPage;
		}
		else if(page == remoteEnvPage) {
			return symbolsPage;
		}
		else if(page == portalIfaceLinker) {
			switch(envPicker.getRuntimeEnv().type) {
			case PROTO_GENI:
				return protoGeniEnvPage;
			case REMOTE_CLUSTER:
				return remoteEnvPage;
			case LOCAL:
				throw new RuntimeException("how did this happen?");
			default:
				throw new RuntimeException("how did this happen?");
			}
		}
		throw new RuntimeException("how did this happen?");
	}

	private EnvPage next(RuntimeEnv env) {
		switch(env.type) {
		case LOCAL:
			return localEnvPage;
		case PROTO_GENI:
			return protoGeniEnvPage;
		case REMOTE_CLUSTER:
			return remoteEnvPage;
		default:
			throw new RuntimeException("how did this happen?");
		}
	}
	
	private int modelPortalCount() {
		int rv = 0;
		for (IEmulationProtocol c : exp.getExperiment().getEmuProtocols()) {
			if(c instanceof ITrafficPortal)
				rv++;
		}
		return rv;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#getNextPage(org.eclipse.jface.wizard.IWizardPage)
	 */
	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		if(page == envPicker) {
			if(exp.getExperiment().getMetadata().getSymbolTable().getSymbolMap().size()>0)
				return symbolsPage;
		}
		else if(page == symbolsPage) {
			//fall through
		}
		else if(page == localEnvPage) {
			return null;
		}
		else if(page == protoGeniEnvPage) {
			if(modelPortalCount()>0) {
				portalIfaceLinker.setEc(((EnvPage)page).getEnv().getEnv());
				return portalIfaceLinker;
			}
		}
		else if(page == remoteEnvPage) {
			if(modelPortalCount()>0) {
				portalIfaceLinker.setEc(((EnvPage)page).getEnv().getEnv());
				return portalIfaceLinker;
			}
		}
		else {
			throw new RuntimeException("how did this happen?");
		}
		if(envPicker.isPageComplete()) {
			final EnvPage n = next(envPicker.getRuntimeEnv());
			n.setEnv(envPicker.getRuntimeEnv());
			return (IWizardPage)n;
		}
		else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#canFinish()
	 */
	public boolean canFinish(){
		if(envPicker.isPageComplete()) {
			final EnvPage n = next(envPicker.getRuntimeEnv());
			if(n.isPageComplete()) {
				if(modelPortalCount()>0) {
					return portalIfaceLinker.isPageComplete();
				}
				else {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Method called as soon as the Wizard is finished.
	 * Launches an experiment to the selected environment
	 *
	 * @return boolean				TRUE if the Wizard finished correctly, FALSE otherwise
	 */
	@Override
	public boolean performFinish() {
		final RuntimeEnv env = envPicker.getRuntimeEnv();
		final Map<String, String> m = symbolsPage.getSymbolMapping();
		for(Entry<String, String> e : EditDefaults.getDefaultValues(resultsPath).entrySet()) {
			m.put(e.getKey(), e.getValue());
		}
		env.getEnv().runtimeSymbolMap = m;
		exp.setEnvConf(env.getEnv());
		return true;
	}
}
