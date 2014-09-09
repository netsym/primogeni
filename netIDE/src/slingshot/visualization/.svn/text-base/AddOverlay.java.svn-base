package slingshot.visualization;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;

import prefuse.util.ColorLib;
import slingshot.Util;
import slingshot.Util.Type;

/**
 * This class defines the Wizard Page that asks information to deploy the experiment.
 *
 * @author Nathanael Van Vorst
 */
public class AddOverlay extends WizardPage implements Listener {
	private Text overlay_data, color;
	private Button rawOverlay, traceRoute, browse, chooseColor;
	private Shell shell;
	private Display display;
	/**
	 * @param pageName
	 */
	protected AddOverlay(String pageName){
		super(pageName);
		setTitle("Add Graph Overlay:");
		setDescription("Either paste the overlay data into the text field or select the file with the overlay data and choose the data type.");
	}
	
	public String getOverlayData() {
		return overlay_data.getText();
	}
	
	public boolean isTraceRoute() {
		return traceRoute.getSelection();
	}
	
	public int getColor() {	
		return ColorLib.rgb(color.getBackground().getRGB().red,color.getBackground().getRGB().green,color.getBackground().getRGB().blue);
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
		shell = parent.getShell();
		display = parent.getDisplay();
		FormToolkit toolkit = new FormToolkit(Display.getCurrent());
		Form form = toolkit.createForm(parent);
		Composite composite = form.getBody();
		composite.setLayout(new GridLayout(3,true));
		composite.setFont(parent.getFont());
		parent.setBackground(toolkit.getColors().getBackground());
		parent.getParent().setBackground(toolkit.getColors().getBackground());

		initializeDialogUnits(parent);
		
		
        GridData gridData = new GridData();
        gridData.grabExcessHorizontalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;
        gridData.grabExcessVerticalSpace = true; // Layout vertically, too! 
        gridData.verticalAlignment = GridData.FILL;
        gridData.horizontalSpan=3;

        overlay_data = new Text(composite, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
        overlay_data.setLayoutData(gridData);
        overlay_data.setText("");

        rawOverlay=toolkit.createButton(composite, "Raw Overlay Data", SWT.CHECK);
        rawOverlay.setLayoutData(getGD(1,1,false,false));
        rawOverlay.setEnabled(true);
        rawOverlay.setSelection(false);
        rawOverlay.setLayoutData(getGD(1, 1, false,false));

        browse=toolkit.createButton(composite, "Load overlay data from file.", SWT.PUSH);
        browse.setLayoutData(getGD(2, 1, true,false));
        
        
        traceRoute=toolkit.createButton(composite, "Traceroute Data", SWT.CHECK);
        traceRoute.setLayoutData(getGD(1,1,true,false));
        traceRoute.setEnabled(true);
        traceRoute.setSelection(true);
        traceRoute.setLayoutData(getGD(1, 1, true,false));

        color = toolkit.createText(composite, "", SWT.BORDER | SWT.MULTI);
        color.setBackground(new Color(display, new RGB(0x32, 0xcd, 0x32)));
        color.setLayoutData(getGD(1, 1, true,false));
        chooseColor = toolkit.createButton(composite, "Change Color", SWT.PUSH | SWT.BORDER);
        chooseColor.setLayoutData(getGD(1, 1, true,false));
        
		rawOverlay.addListener(SWT.MouseUp, this);
		traceRoute.addListener(SWT.MouseUp, this);
		overlay_data.addListener(SWT.KeyUp, this);
		browse.addListener(SWT.MouseUp, this);
		chooseColor.addListener(SWT.MouseUp, this);
		
		setControl(composite);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.WizardPage#getNextPage()
	 */
	public IWizardPage getNextPage() {
		//no next page
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.WizardPage#canFlipToNextPage()
	 */
	public boolean canFlipToNextPage() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	@Override
	public void handleEvent(Event event) {
        if(event.widget == rawOverlay) {
			if(rawOverlay.getSelection()) {
		        rawOverlay.setSelection(true);
		        traceRoute.setSelection(false);
			}
			else {
		        rawOverlay.setSelection(false);
		        traceRoute.setSelection(true);
			}
        }
        else  if(event.widget == traceRoute) {
			if(traceRoute.getSelection()) {
		        rawOverlay.setSelection(false);
		        traceRoute.setSelection(true);
			}
			else {
		        rawOverlay.setSelection(true);
		        traceRoute.setSelection(false);
			}
        }
        else if(event.widget == browse) {
			FileDialog fileDialog = new FileDialog(Display.getDefault().getActiveShell());
			String f = fileDialog.open();
	        //get the file name from that path
			if(f != null && f.length()>0) {
				File ff = new File(f);
				if(ff.exists() && ff.isFile()) {
					try {
						StringBuffer rv = new StringBuffer();
						DataInputStream in = new DataInputStream(new FileInputStream(ff));
						BufferedReader br = new BufferedReader(new InputStreamReader(in));
						String str;
						while ((str = br.readLine()) != null)   {
							rv.append(str+"\n");
						}
						in.close();
						overlay_data.setText(rv.toString());
					} catch (FileNotFoundException e) {
						Util.dialog(Type.ERROR, "Error opening file "+f, Util.getStackTraceAsString(e));
					} catch (IOException e) {
						Util.dialog(Type.ERROR, "Error reading file "+f, Util.getStackTraceAsString(e));
					}
				}
			}
        }
        else if(event.widget == chooseColor) {
            ColorDialog cd = new ColorDialog(shell);
            cd.setText("Choose a Color");
            cd.setRGB(color.getBackground().getRGB());
            RGB newColor = cd.open();
            if (newColor == null) {
              return;
            }
            color.setBackground(new Color(display, newColor));
        }
		setPageComplete(isPageComplete());
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.WizardPage#isPageComplete()
	 */
	public boolean isPageComplete(){
		if(!rawOverlay.getSelection() && !traceRoute.getSelection()) {
			setErrorMessage("Must specify whether the overlay is from traceroute or is 'raw' overlay data");
			return false;
		}
		else if(overlay_data.getText() == null || overlay_data.getText().length()==0) {
			setErrorMessage("Must enter overlay data.");
		}
		setErrorMessage(null);
		return true;
	}
}
