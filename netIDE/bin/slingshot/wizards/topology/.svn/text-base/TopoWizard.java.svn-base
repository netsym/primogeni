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
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.wizard.Wizard;


/**
 * This is the Wizard handler class for the generate topology models.
 * @author Hao Jiang
 */
public class TopoWizard extends Wizard //implements INewWizard
{
	String projectmodelpath;
	
	//model name
	String modelname;
	
	//profile path
	String profile;
	
	//class to translate generator output to xml file
	XmlScript xmlsrcipt;
	
	//generator name list
	List<String> geners;
	
	// wizard page
	ChooseProjectPage chosepropage;
	GenAttriPage generattripage;
	AttachLanPage attachlanpage;
	
	StatusDlg dlg;
	
	public TopoWizard(String profile, List<String> geners) 
	{
		super();
		setWindowTitle("Topology Wizard");
		this.profile = profile;
		this.geners = geners;
		//project = null;
	}
	
	@Override
	public void addPages() 
	{
		chosepropage = new ChooseProjectPage();
		chosepropage.setPageComplete(false);
		addPage(chosepropage);
		
		generattripage = new GenAttriPage(this.profile, this.geners);
		generattripage.setPageComplete(false);
		addPage(generattripage);
		
		attachlanpage = new AttachLanPage();
		addPage(attachlanpage);
	}
	
	@Override
	public boolean canFinish()
	{
		if (this.getContainer().getCurrentPage() == generattripage)
		{
			if (generattripage.isPageComplete() &&
				!generattripage.attachlan.getSelection())
				return true;
			else
				return false;
		}
		else if (this.getContainer().getCurrentPage() == attachlanpage)
			return true;
		else
			return false;
	}

	@Override
	public boolean performFinish() 
	{
    	File file = new File(projectmodelpath);
		if (!file.exists())
		{
			file.mkdir();
			System.out.println("Project Model folder does not exist -- defaulting to /tmp");
			projectmodelpath="/tmp";
			//return false;
		}
		
		if (!projectmodelpath.endsWith(File.separator))
			projectmodelpath += File.separator;
		
		
		//write xml file into project Models folder here
		if (this.getContainer().getCurrentPage() == generattripage)
			generattripage.PerformFinish();
		else if (this.getContainer().getCurrentPage() == attachlanpage)
			attachlanpage.PerformFinish();
		
		
		if (xmlsrcipt != null)
			xmlsrcipt.WriteXml(projectmodelpath + this.modelname + ".xml");
		try {
			ResourcesPlugin.getWorkspace().getRoot().refreshLocal(IResource.DEPTH_INFINITE, null);
		}
		catch(Exception e) { }
		return true;
	}
}
