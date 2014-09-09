package slingshot.experiment.launch;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.ManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import slingshot.environment.configuration.LocalConfiguration;

/**
 * This class defines the Wizard Page that asks information to deploy the experiment.
 *
 * @author Nathanael Van Vorst
 */
public class LocalEnvPage extends WizardPage implements Listener, EnvPage {
	private Text runtime, procs;
	private Spinner emuScale;
	private Label emuRatio;
	private RuntimeEnv env;
	
	/**
	 * @param pageName
	 */
	protected LocalEnvPage(String pageName){
		super(pageName);
		setTitle("Local Environment");
		setDescription("Configure the local environment to run the experiment.");
	}

	private GridData getGD(int hspan, int vspan, boolean h, boolean v) {
		GridData gd = new GridData(GridData.FILL, GridData.CENTER, h, v);
		gd.horizontalSpan=hspan;
		gd.verticalSpan=vspan;
		gd.grabExcessHorizontalSpace=h;
		gd.grabExcessVerticalSpace=v;
		return gd;
	}
	
	/**
	 * @return the env
	 */
	public RuntimeEnv getEnv() {
		return env;
	}

	/**
	 * @param env the env to set
	 */
	public void setEnv(RuntimeEnv env) {
		this.env = env;
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

		toolkit.createLabel(composite, "#Processors:");
		procs=toolkit.createText(composite, "1");
		procs.setLayoutData(getGD(1,1,true,false));
		toolkit.createLabel(composite, "");
		procs.setEnabled(true);
		procs.setVisible(true);

		toolkit.createLabel(composite, "Pace Simulation Speed:");
		emuScale = new Spinner(composite,SWT.NONE);
		emuScale.setMinimum(0);
		emuScale.setMaximum(100);
		emuScale.setIncrement(1);
		emuScale.setSelection(0);
		emuScale.setLayoutData(getGD(1,1,true,false));
		emuRatio=toolkit.createLabel(composite, "(No pacing)");
		emuRatio.setLayoutData(getGD(1,1,true,false));
		emuScale.setEnabled(true);
		emuScale.setVisible(true);
		emuRatio.setEnabled(true);
		emuRatio.setVisible(true);
		
		runtime.addListener(SWT.KeyUp, this);
		emuScale.addListener(SWT.MouseUp, this);
		procs.addListener(SWT.KeyUp, this);

		setControl(form);
		setPageComplete(false);
	}

	/**
	 * @return
	 */
	private int getRuntime() {
		try {
			return Integer.parseInt(runtime.getText());
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * @return
	 */
	private int getProcs() {
		try {
			return Integer.parseInt(procs.getText());
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * @return
	 */
	private double getEmuRatio() {
		try {
			int s = emuScale.getSelection();
			if(s==0) return 0;
			return 1.0/((double)s);
		} catch (Exception e) {
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.WizardPage#canFlipToNextPage()
	 */
	public boolean canFlipToNextPage() {
		isPageComplete();
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	@Override
	public void handleEvent(Event event) {
		int s = emuScale.getSelection();
		if(s==emuScale.getMinimum()) {
			emuRatio.setText("(No pacing)");
		}
		else if(s==1) {
			emuRatio.setText("(Real time)");
		}
		else {
			emuRatio.setText("("+s+"x slow down)");
		}
		setPageComplete(isPageComplete());
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.WizardPage#isPageComplete()
	 */
	public boolean isPageComplete(){
		if(env ==null) {
			setErrorMessage("Go back and choose an environment!");
			return false;
		}
		if (getRuntime() == 0) {
			setErrorMessage("The runtime must be a positive integer.");
			return false;
		}
		if(getProcs()==0) {
			setErrorMessage("The number of processors must be a positive integer.");
			return false;
		}
		LocalConfiguration e = (LocalConfiguration)env.getEnv();
		e.emuRatio=getEmuRatio();
		e.runtime=getRuntime();
		e.numProcs=getProcs();
		setErrorMessage(null);
		return true;
	}
}
