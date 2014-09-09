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

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.swt.widgets.Button;

/**
 * @author Hao Jiang
 */
public class AttachLanPage extends WizardPage implements Listener
{
	public static final String pagename="AttachLanPage";

	private Spinner minsubnetnum;
	private Spinner maxsubnetnum;
	private Button duplicate;
	
	private Spinner minirtnum;
	private Spinner maxrtnum;
	
	private Spinner minihostnum;
	private Spinner maxhostnum;
	
	private Spinner rtminbandwidth;
	private Spinner rtmaxbandwidth;
	private Spinner rtminlatency;
	private Spinner rtmaxlatency;
	private Combo rtbwdist;
	private Combo rtltdist;
	
	private Spinner hminbandwidth;
	private Spinner hmaxbandwidth;
	private Spinner hminlatency;
	private Spinner hmaxlatency;
	private Combo hostbwdist;
	private Combo hostltdist;
	
	private Text descriText;
	private Text text;
	
	private XmlScript xmlscript;
	private String genername;
	private Composite container;
	/**
	 * Create the wizard.
	 */
	public AttachLanPage() 
	{
		super(pagename);
		setTitle("Attach subnet to topology");
		setDescription("Specify attributes for subnets to be attached to the routers in the generated topology");
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) 
	{
		container = new Composite(parent, SWT.NULL);
		
		container.setLayout(new GridLayout());
		
		createOptionsGroup0(container);
        createOptionsGroup1(container);
        createOptionsGroup2(container);
        createOptionsGroup3(container); 
        createOptionsGroup4(container); 
        createOptionsGroup5(container);

        InitWidgets();
		
        addListeners();
        
		setControl(container);
	}
	
	private void createOptionsGroup0(Composite parent)
	{
		// container specification group
        Group group = new Group(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        group.setText("Topology information");
        group.setLayout(layout);
        group.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));
        group.setFont(parent.getFont());       
           
        text = new Text(group, SWT.READ_ONLY | SWT.MULTI);
		text.setEnabled(false);
		GridData gd_text = new GridData(SWT.FILL, SWT.LEFT, true, false, 1, 1);
		gd_text.heightHint = 35;
		text.setLayoutData(gd_text);
		text.setFont(parent.getFont());
	}
	
	private void createOptionsGroup1(Composite parent)
	{
		Group group = new Group(parent, SWT.NONE);
        GridLayout layout = new GridLayout(2, false);
        group.setLayout(layout);
        group.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.FILL_HORIZONTAL));
        group.setFont(parent.getFont());
        group.setText("Number of subnets");
        
        GridData data;
        Label minisubnetnumlabel = new Label(group, SWT.FILL);
        minisubnetnumlabel.setText("Mininum:");
        minisubnetnumlabel.setFont(parent.getFont());
        
        minsubnetnum = new Spinner(group, SWT.NONE);
        data = new GridData(GridData.FILL_HORIZONTAL);
        minsubnetnum.setLayoutData(data);
        
        Label maxsubnetnumlabel = new Label(group, SWT.FILL);
        maxsubnetnumlabel.setText("Maximum:");
        maxsubnetnumlabel.setFont(parent.getFont());
        
        maxsubnetnum = new Spinner(group, SWT.NONE);
        data = new GridData(GridData.FILL_HORIZONTAL);
        maxsubnetnum.setLayoutData(data);
        
        duplicate = new Button(group, SWT.CHECK);
        duplicate.setText("Automatic duplicate subnet");
        data = new GridData(GridData.FILL_HORIZONTAL);
        data.verticalAlignment = SWT.CENTER;
        data.horizontalSpan = 2;
        duplicate.setLayoutData(data);
	}
	
	private void createOptionsGroup2(Composite parent) 
	{	
	    Group group = new Group(parent, SWT.NULL);
        GridLayout layout = new GridLayout(2, false);
        group.setLayout(layout);
        group.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.FILL_HORIZONTAL));
        group.setFont(parent.getFont());
        group.setText("Number of routers within each subnet");
        
        GridData data;
        Label minrtnumlabel = new Label(group, SWT.FILL);
        minrtnumlabel.setText("Mininum:");
        minrtnumlabel.setFont(parent.getFont());
        
        minirtnum = new Spinner(group, SWT.NONE);
        data = new GridData(GridData.FILL_HORIZONTAL);
        minirtnum.setLayoutData(data);

        Label maxrtlabel = new Label(group, SWT.FILL);
        maxrtlabel.setText("Maximum:");
        maxrtlabel.setFont(parent.getFont());
        
        maxrtnum = new Spinner(group, SWT.NONE);
        data = new GridData(GridData.FILL_HORIZONTAL);
        maxrtnum.setLayoutData(data);
	}
	
	private void createOptionsGroup3(Composite parent) 
	{	
	    Group group = new Group(parent, SWT.NULL);
        GridLayout layout = new GridLayout(2, false);
        group.setLayout(layout);
        group.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.FILL_HORIZONTAL));
        group.setFont(parent.getFont());
        group.setText("Number of hosts attached to router");
        
        GridData data;
        Label minhlabel = new Label(group, SWT.FILL);
        minhlabel.setText("Minimum:");
        minhlabel.setFont(parent.getFont());
        
        minihostnum = new Spinner(group, SWT.NONE);
        data = new GridData(GridData.FILL_HORIZONTAL);
        minihostnum.setLayoutData(data);

        Label maxhlabel = new Label(group, SWT.FILL);
        maxhlabel.setText("Maximum:");
        maxhlabel.setFont(parent.getFont());
        
        maxhostnum = new Spinner(group, SWT.NONE);
        data = new GridData(GridData.FILL_HORIZONTAL);
        maxhostnum.setLayoutData(data);
	}
	
	private void createOptionsGroup4(Composite parent) 
	{
	    SashForm sashForm1 = new SashForm(parent, SWT.HORIZONTAL);
        sashForm1.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
           
        createRTBandwidth(sashForm1);
        createRTLatency(sashForm1);
  
        SashForm sashForm2 = new SashForm(parent, SWT.HORIZONTAL);
        sashForm2.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        createHostBandwidth(sashForm2);
        createHostLatency(sashForm2);
	}
	
	private void createRTBandwidth(SashForm parent)
	{
        Group group = new Group(parent, SWT.NONE);
        GridLayout layout = new GridLayout(2, false);
        group.setLayout(layout);
        group.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.FILL_HORIZONTAL));
        group.setFont(parent.getFont());
        group.setText("Router-to-router link bandwidth (Kb/s)");
        
        GridData data;
        Label minrtbandwidthlabel = new Label(group, SWT.FILL);
        minrtbandwidthlabel.setText("Minimum:");
        minrtbandwidthlabel.setFont(parent.getFont());
        
        rtminbandwidth = new Spinner(group, SWT.NONE);
        data = new GridData(GridData.FILL_HORIZONTAL);
        rtminbandwidth.setLayoutData(data);
        
        Label maxrtbandwidthlabel = new Label(group, SWT.FILL);
        maxrtbandwidthlabel.setText("Maximum:");
        maxrtbandwidthlabel.setFont(parent.getFont());
        
        rtmaxbandwidth = new Spinner(group, SWT.NONE);
        data = new GridData(GridData.FILL_HORIZONTAL);
        rtmaxbandwidth.setLayoutData(data);
        
        Label bwassignmethod = new Label(group, SWT.FILL);
        bwassignmethod.setText("Assignment method:");
        bwassignmethod.setFont(parent.getFont());
        
        rtbwdist = new Combo(group, SWT.NULL | SWT.READ_ONLY);
        data = new GridData(GridData.FILL_HORIZONTAL);
        rtbwdist.setLayoutData(data);
	}
	
	private void createRTLatency(SashForm parent)
	{
        Group group = new Group(parent, SWT.NONE);
        GridLayout layout = new GridLayout(2, false);
        group.setLayout(layout);
        group.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.FILL_HORIZONTAL));
        group.setFont(parent.getFont());
        group.setText("Router-to-router link latency (milliseconds)");
        
        GridData data;
        Label minrtlatencylabel = new Label(group, SWT.FILL);
        minrtlatencylabel.setText("Minimum:");
        minrtlatencylabel.setFont(parent.getFont());
        
        rtminlatency = new Spinner(group, SWT.NONE);
        data = new GridData(GridData.FILL_HORIZONTAL);
        rtminlatency.setLayoutData(data);
        
        Label maxrtlatencylabel = new Label(group, SWT.FILL);
        maxrtlatencylabel.setText("Maximum:");
        maxrtlatencylabel.setFont(parent.getFont());
        
        rtmaxlatency = new Spinner(group, SWT.NONE);
        data = new GridData(GridData.FILL_HORIZONTAL);
        rtmaxlatency.setLayoutData(data);
        
        Label ltassignmethod = new Label(group, SWT.FILL);
        ltassignmethod.setText("Assignment method:");
        ltassignmethod.setFont(parent.getFont());
        
        rtltdist = new Combo(group, SWT.NULL | SWT.READ_ONLY);
        data = new GridData(GridData.FILL_HORIZONTAL);
        rtltdist.setLayoutData(data);
	}
	
	private void createHostBandwidth(SashForm parent)
	{
        Group group = new Group(parent, SWT.NONE);
        GridLayout layout = new GridLayout(2, false);
        group.setLayout(layout);
        group.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.FILL_HORIZONTAL));
        group.setFont(parent.getFont());
        group.setText("Host link bandwidth (Kb/s)");
        
        Label minhbandwidthlabel = new Label(group, SWT.FILL);
        minhbandwidthlabel.setText("Minimum:");
        minhbandwidthlabel.setFont(parent.getFont());
        
        GridData data;
        hminbandwidth = new Spinner(group, SWT.NONE);
        data = new GridData(GridData.FILL_HORIZONTAL);
        hminbandwidth.setLayoutData(data);
        
        Label maxhbandwidthlabel = new Label(group, SWT.FILL);
        maxhbandwidthlabel.setText("Maximum:");
        maxhbandwidthlabel.setFont(parent.getFont());
        
        hmaxbandwidth = new Spinner(group, SWT.NONE);
        data = new GridData(GridData.FILL_HORIZONTAL);
        hmaxbandwidth.setLayoutData(data);
        
        Label latencyassignmethod = new Label(group, SWT.FILL);
        latencyassignmethod.setText("Assignment method:");
        latencyassignmethod.setFont(parent.getFont());
        
        hostbwdist = new Combo(group, SWT.NULL | SWT.READ_ONLY);
        data = new GridData(GridData.FILL_HORIZONTAL);
        hostbwdist.setLayoutData(data);
	}
	
	private void createHostLatency(SashForm parent)
	{
		Group group = new Group(parent, SWT.NONE);
        GridLayout layout = new GridLayout(2, false);
        group.setLayout(layout);
        group.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.FILL_HORIZONTAL));
        group.setFont(parent.getFont());
        group.setText("Host link latency (milliseconds)");
        
        GridData data;
        Label minhlatencylabel = new Label(group, SWT.FILL);
        minhlatencylabel.setText("Minimum:");
        minhlatencylabel.setFont(parent.getFont());
        
        hminlatency = new Spinner(group, SWT.NONE);
        data = new GridData(GridData.FILL_HORIZONTAL);
        hminlatency.setLayoutData(data);
        
        Label maxhostlatencylabel = new Label(group, SWT.FILL);
        maxhostlatencylabel.setText("Maximum:");
        maxhostlatencylabel.setFont(parent.getFont());
        
        hmaxlatency = new Spinner(group, SWT.NONE);
        data = new GridData(GridData.FILL_HORIZONTAL);
        hmaxlatency.setLayoutData(data);
        
        Label ltassignmethod = new Label(group, SWT.FILL);
        ltassignmethod.setText("Assignment method:");
        ltassignmethod.setFont(parent.getFont());
        
        hostltdist = new Combo(group, SWT.NULL | SWT.READ_ONLY);
        data = new GridData(GridData.FILL_HORIZONTAL);
        hostltdist.setLayoutData(data);
	}
	
	private void createOptionsGroup5(Composite parent)
	{
		// container specification group
        Composite containerGroup = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        containerGroup.setLayout(layout);
        containerGroup.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));
        containerGroup.setFont(parent.getFont());

        GridData data;
        
        Label describeLabel = new Label(containerGroup, SWT.FILL);
        data = new GridData(GridData.FILL_HORIZONTAL);
        describeLabel.setText("Description:");
        describeLabel.setLayoutData(data);
        describeLabel.setFont(parent.getFont());
           
        descriText = new Text(containerGroup, 
        		 			  SWT.WRAP | SWT.MULTI | SWT.READ_ONLY | SWT.BORDER | SWT.V_SCROLL);
        data = new GridData(GridData.FILL_BOTH);
        data.minimumHeight = 50;
        descriText.setLayoutData(data);
        descriText.setFont(parent.getFont());
	}
		
	public void InitWidgets()
	{
		//subnet num 0 - router number
		minsubnetnum.setMinimum(0);
		//subnetnum.setMaximum(xmlscript.tree.getRouterNum()); // is it infinite?
		minsubnetnum.setSelection(25);
		minsubnetnum.addModifyListener(new SpinnerModified(minsubnetnum, maxsubnetnum));
		
		maxsubnetnum.setMinimum(0);
		maxsubnetnum.setSelection(25);
		maxsubnetnum.addModifyListener(new SpinnerModified(minsubnetnum, maxsubnetnum));
		
		duplicate.setSelection(false);
		
		//router num 1-100
		minirtnum.setMinimum(1);
		//minirtnum.setMaximum(100);
		minirtnum.setSelection(1);
		minirtnum.addModifyListener(new SpinnerModified(minirtnum, maxrtnum));
		
		maxrtnum.setMinimum(1);
		//maxrtnum.setMaximum(100);
		maxrtnum.setSelection(5);
		maxrtnum.addModifyListener(new SpinnerModified(minirtnum, maxrtnum));
		
		//host num 1-100
		minihostnum.setMinimum(1);
		//minihostnum.setMaximum(100);
		minihostnum.setSelection(1);
		minihostnum.addModifyListener(new SpinnerModified(minihostnum, maxhostnum));
		
		maxhostnum.setMinimum(1);
		//maxhostnum.setMaximum(100);
		maxhostnum.setSelection(5);
		maxhostnum.addModifyListener(new SpinnerModified(minihostnum, maxhostnum));
		
		//router bandwidth
		rtminbandwidth.setMinimum(10);
		rtminbandwidth.setMaximum(100000000);
		rtminbandwidth.setSelection(100000);
		rtminbandwidth.setIncrement(10);
		rtminbandwidth.addModifyListener(new SpinnerModified(rtminbandwidth, rtmaxbandwidth));
		
		rtmaxbandwidth.setMinimum(10);
		rtmaxbandwidth.setMaximum(100000000);
		rtmaxbandwidth.setSelection(100000);
		rtmaxbandwidth.setIncrement(10);
		rtmaxbandwidth.addModifyListener(new SpinnerModified(rtminbandwidth, rtmaxbandwidth));
		
		//router latency
		rtminlatency.setDigits(3);
		rtminlatency.setMinimum(1);
		rtminlatency.setMaximum(100000);
		rtminlatency.setSelection(1000);
		rtminlatency.addModifyListener(new SpinnerModified(rtminlatency, rtmaxlatency));
		
		rtmaxlatency.setDigits(3);
		rtmaxlatency.setMinimum(1);
		rtmaxlatency.setMaximum(100000);
		rtmaxlatency.setSelection(1000);
		rtmaxlatency.addModifyListener(new SpinnerModified(rtminlatency, rtmaxlatency));
		
		//host bandwidth
		hminbandwidth.setMinimum(10);
		hminbandwidth.setMaximum(100000000);
		hminbandwidth.setSelection(10000);
		hminbandwidth.setIncrement(10);
		hminbandwidth.addModifyListener(new SpinnerModified(hminbandwidth, hmaxbandwidth));
		
		hmaxbandwidth.setMinimum(10);
		hmaxbandwidth.setMaximum(100000000);
		hmaxbandwidth.setSelection(10000);
		hmaxbandwidth.setIncrement(10);
		hmaxbandwidth.addModifyListener(new SpinnerModified(hminbandwidth, hmaxbandwidth));
		
		//host latency
		hminlatency.setDigits(3);
		hminlatency.setMinimum(1);
		hminlatency.setMaximum(1000000);
		hminlatency.setSelection(1000);
		hminlatency.addModifyListener(new SpinnerModified(hminlatency, hmaxlatency));
		
		hmaxlatency.setDigits(3);
		hmaxlatency.setMinimum(1);
		hmaxlatency.setMaximum(100000);
		hmaxlatency.setSelection(1000);
		hmaxlatency.addModifyListener(new SpinnerModified(hminlatency, hmaxlatency));
		
		
		String[] combo = { "CONSTANT", "UNIFORM", "HEAVYTAILED", "EXPONENTIAL" };
		for (int i = 0; i < combo.length; i++)
		{	
			rtbwdist.add(combo[i]);
			rtltdist.add(combo[i]);
			hostbwdist.add(combo[i]);
			hostltdist.add(combo[i]);
		}
		
		rtbwdist.select(0);
		rtltdist.select(0);
		hostbwdist.select(0);
		hostltdist.select(0);
	}
	
	private void addListeners()
	{
		minsubnetnum.addListener(SWT.FOCUSED, this);
		maxsubnetnum.addListener(SWT.FOCUSED, this);
		duplicate.addListener(SWT.FOCUSED, this);

		minirtnum.addListener(SWT.FOCUSED, this);
		maxrtnum.addListener(SWT.FOCUSED, this);
		
		minihostnum.addListener(SWT.FOCUSED, this);
		maxhostnum.addListener(SWT.FOCUSED, this);
		
		rtminbandwidth.addListener(SWT.FOCUSED, this);
		rtmaxbandwidth.addListener(SWT.FOCUSED, this);
		rtminlatency.addListener(SWT.FOCUSED, this);
		rtmaxlatency.addListener(SWT.FOCUSED, this);
		rtbwdist.addListener(SWT.FOCUSED, this);
		rtltdist.addListener(SWT.FOCUSED, this);
		
		hminbandwidth.addListener(SWT.FOCUSED, this);
		hmaxbandwidth.addListener(SWT.FOCUSED, this);
		hminlatency.addListener(SWT.FOCUSED, this);
		hmaxlatency.addListener(SWT.FOCUSED, this);
		hostbwdist.addListener(SWT.FOCUSED, this);
		hostltdist.addListener(SWT.FOCUSED, this);
	}
	
	private final class SpinnerModified implements ModifyListener
	{
		Spinner maxSpinner;
		Spinner minSpinner;
		
		SpinnerModified(Spinner min, Spinner max)
		{
			super();
			this.minSpinner = min;
			this.maxSpinner = max;
		}

		@Override
		public void modifyText(ModifyEvent e) 
		{
			// TODO Auto-generated method stub
			Spinner spinner = (Spinner)e.widget;
			
			int selection = spinner.getSelection();
			if (spinner.equals(minSpinner))
			{
				if (selection > maxSpinner.getSelection())
					spinner.setSelection(maxSpinner.getSelection());
			}
			else if (spinner.equals(maxSpinner))
			{
				if (selection < minSpinner.getSelection())
					spinner.setSelection(minSpinner.getSelection());
			}
		}
	}

	@Override
	public void handleEvent(Event event) 
	{
		// TODO Auto-generated method stub
		//get the source of the event
		Widget source = event.widget;
		//check if the source was the browse button
		if (source != null)
		{
			descriText.setText("");
	        if (source == minsubnetnum) 
	        {
				descriText.setText("The minimum number of subnet to be attached to network");
			}
	        else if (source == maxsubnetnum)
	        {
	        	descriText.setText("The maximum number of subnet to be attached to network");
	        }
	        else if (source == duplicate)
	        {
	        	descriText.setText("Enable or disable automatic duplication function");
	        }
	        else if (source == minirtnum)
	        {
	        	descriText.setText("The minimum number of routers within each subnet");
	        }
	        else if (source == maxrtnum)
	        {
	        	descriText.setText("The maximum number of routers within each subnet");
	        }
	        else if (source == minihostnum)
	        {
	        	descriText.setText("The minimum number of hosts attach to router");
	        }
	        else if (source == maxhostnum)
	        {
	        	descriText.setText("The maximum number of hosts attach to router");
	        }
	        else if (source == rtminbandwidth)
	        {
	        	descriText.setText("The minimum bandwidth of router to router link (Kb/s)");
	        }
	        else if (source == rtmaxbandwidth)
	        {
	        	descriText.setText("The maximum bandwidth of router to router link (Kb/s)");
	        }
	        else if (source == rtminlatency)
	        {
	        	descriText.setText("The minimum latency of router to router link (milliseconds)");
	        }
	        else if (source == rtmaxlatency)
	        {
	        	descriText.setText("The maximum latency of router to router link (milliseconds)");
	        }
	        else if (source == rtbwdist)
	        {
	        	descriText.setText("The bandwidth assginment method for router to router link");
	        }
	        else if (source == rtltdist)
	        {
	        	descriText.setText("The latency assginment method for router to router link");
	        }
	        else if (source == hminbandwidth)
	        {
	        	descriText.setText("The minimum bandwidth of host link (Kb/s)");
	        }
	        else if (source == hmaxbandwidth)
	        {
	        	descriText.setText("The maximum bandwidth of host link (Kb/s)");
	        }
	        else if (source == hminlatency)
	        {
	        	descriText.setText("The minimum latency of host link (millisecond)");
	        }
	        else if (source == hmaxlatency)
	        {
	        	descriText.setText("The maximum latency of host link (millisecond)");
	        }
	        else if (source == hostbwdist)
	        {
	        	descriText.setText("The bandwidth assginment method for host link");
	        }
	        else if (source == hostltdist)
	        {
	        	descriText.setText("The latency assginment method for host link");
	        }
		}
	}
	
	public void SetAttri(String genname, XmlScript script)
	{
		this.genername = genname;
		this.xmlscript = script;
		
		if (this.xmlscript != null)
		{
			String str = String.format("Generated by: %s \nNumber of subnets: %d; number of routers: %d; number of links: %d ", 
										this.genername, 
										xmlscript.getXmlTree().getNetNum() +  xmlscript.getXmlTree().getReplicaNum(), 
										xmlscript.getXmlTree().getRouterNum() + xmlscript.getXmlTree().rrtnum,
										xmlscript.getXmlTree().getLinkNum() + xmlscript.getXmlTree().rlinknum);
			text.setText(str);
		}
		else 
			text.setText("Topology is not generated successfully");
	}
	
	public void PerformFinish()
	{
		int minsubnetnum = this.minsubnetnum.getSelection();
		int maxsubnetnum = this.maxsubnetnum.getSelection();
		
		int minrtnum = this.minirtnum.getSelection();
		int maxrtnum = this.maxrtnum.getSelection();
		
		int minhostnum = this.minihostnum.getSelection();
		int maxhostnum = this.maxhostnum.getSelection();
		
		float minrtbandwidth = this.rtminbandwidth.getSelection() * 1000;
		float maxrtbandwidth = this.rtmaxbandwidth.getSelection() * 1000;
		
		float minrtlatency = (float)(this.rtminlatency.getSelection() / Math.pow(10, this.rtminlatency.getDigits())) / 1000;
		float maxrtlatency = (float)(this.rtmaxlatency.getSelection() / Math.pow(10, this.rtmaxlatency.getDigits())) / 1000;
		
		float minhbandwidth = this.hminbandwidth.getSelection() * 1000;
		float maxhbandwidth = this.hmaxbandwidth.getSelection() * 1000;
		
		float minhlatency = (float)(this.hminlatency.getSelection() / Math.pow(10, this.hminlatency.getDigits())) / 1000;
		float maxhlatency = (float)(this.hmaxlatency.getSelection() / Math.pow(10, this.hmaxlatency.getDigits())) / 1000;
		
		String[] combo = { "CONSTANT", "UNIFORM", "HEAVYTAILED", "EXPONENTIAL" };
		
		XmlScript.BWAssginType rtbwdist;
		if (this.rtbwdist.getText().equals(combo[0]))
			rtbwdist = XmlScript.BWAssginType.CONSTANT;
		else if (this.rtbwdist.getText().equals(combo[1]))
			rtbwdist = XmlScript.BWAssginType.UNIFORM;
		else if (this.rtbwdist.getText().equals(combo[1]))
			rtbwdist = XmlScript.BWAssginType.HEAVYTAILED;
		else 
			rtbwdist = XmlScript.BWAssginType.EXPONENTIAL;
			
		XmlScript.BWAssginType rtltdist;
		if (this.rtltdist.getText().equals(combo[0]))
			rtltdist = XmlScript.BWAssginType.CONSTANT;
		else if (this.rtltdist.getText().equals(combo[1]))
			rtltdist = XmlScript.BWAssginType.UNIFORM;
		else if (this.rtltdist.getText().equals(combo[1]))
			rtltdist = XmlScript.BWAssginType.HEAVYTAILED;
		else 
			rtltdist = XmlScript.BWAssginType.EXPONENTIAL;
		
		XmlScript.BWAssginType hbwdist;
		if (this.hostbwdist.getText().equals(combo[0]))
			hbwdist = XmlScript.BWAssginType.CONSTANT;
		else if (this.hostbwdist.getText().equals(combo[1]))
			hbwdist = XmlScript.BWAssginType.UNIFORM;
		else if (this.hostbwdist.getText().equals(combo[1]))
			hbwdist = XmlScript.BWAssginType.HEAVYTAILED;
		else
			hbwdist = XmlScript.BWAssginType.EXPONENTIAL;
		
		XmlScript.BWAssginType hltdist;
		if (this.hostltdist.getText().equals(combo[0]))
			hltdist = XmlScript.BWAssginType.CONSTANT;
		else if (this.hostltdist.getText().equals(combo[1]))
			hltdist = XmlScript.BWAssginType.UNIFORM;
		else if (this.hostltdist.getText().equals(combo[1]))
			hltdist = XmlScript.BWAssginType.HEAVYTAILED;
		else 
			hltdist = XmlScript.BWAssginType.EXPONENTIAL;
		
		if (xmlscript != null)
		xmlscript.AttachLanNet(minsubnetnum, maxsubnetnum, duplicate.getSelection(), 
				               minrtnum, maxrtnum, 
				               minhostnum, maxhostnum, 
				               minrtbandwidth, maxrtbandwidth, rtbwdist, 
				               minrtlatency, maxrtlatency, rtltdist, 
				               minhbandwidth, maxhbandwidth, hbwdist, 
				               minhlatency, maxhlatency, hltdist);
	}
}
