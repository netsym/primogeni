package slingshot.experiment.launch;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.ManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import slingshot.experiment.PyExperiment;

/**
 * This class defines the Wizard Page that asks information to deploy the experiment.
 *
 * @author Nathanael Van Vorst, Mohammad Abu Obaida
 */
public class ProtoGeniEnvPage extends WizardPage implements Listener, EnvPage {
	private Text runtime;
	private RuntimeEnv env = null;
	//private final PyExperiment exp;
	
	/**
	 * @param pageName
	 */
	protected ProtoGeniEnvPage(String pageName, PyExperiment exp){
		super(pageName);
		//this.exp=exp;
		setTitle("Geni Slices Environment");
		setDescription("Configure the Geni slices environment to run the experiment.");
	}

	/* (non-Javadoc)
	 * @see slingshot.experiment.launch.EnvPage#getEnv()
	 */
	@Override
	public RuntimeEnv getEnv() {
		return env;
	}

	/* (non-Javadoc)
	 * @see slingshot.experiment.launch.EnvPage#setEnv(slingshot.experiment.launch.RuntimeEnv)
	 */
	@Override
	public void setEnv(RuntimeEnv env) {
		this.env=env;
	}


	private GridData getGD(int hspan, int vspan, boolean h, boolean v) {
		GridData gd = new GridData(GridData.FILL, GridData.CENTER, h, v);
		gd.horizontalSpan=hspan;
		gd.verticalSpan=vspan;
		gd.grabExcessHorizontalSpace=h;
		gd.grabExcessVerticalSpace=v;
		return gd;
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
		composite.setLayout(new GridLayout(3,true));
		composite.setFont(parent.getFont());
		parent.setBackground(toolkit.getColors().getBackground());
		parent.getParent().setBackground(toolkit.getColors().getBackground());


		toolkit.createLabel(composite, "Runtime:");
		runtime=toolkit.createText(composite, "");
		runtime.setLayoutData(getGD(1,1,true,false));
		runtime.setEnabled(true);
		toolkit.createLabel(composite, "(Seconds)");

		runtime.addListener(SWT.KeyUp, this);

		setControl(form);
		setPageComplete(false);
	}

	/**
	 * @return
	 */
	public int getRuntime() {
		try {
			return Integer.parseInt(runtime.getText());
		}
		catch(Exception e) {
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.WizardPage#canFlipToNextPage()
	 */
	public boolean canFlipToNextPage() {
		if(isPageComplete() && env.getPortalCount()>0)
			return true;
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	@Override
	public void handleEvent(Event event) {
		setPageComplete(isPageComplete());
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.WizardPage#isPageComplete()
	 */
	public boolean isPageComplete(){
		if(env ==null) {
			setErrorMessage("Go back and select an environment.");
			return false;
		}
		if (getRuntime() == 0) {
			setErrorMessage("The runtime must be a positive integer.");
			return false;
		}
		env.getEnv().runtime=getRuntime();
		setErrorMessage(null);
		return true;
	}
}
