package slingshot;

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

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import slingshot.configuration.ConfigurationHandler;

/**
 * This class was generated automatically by eclipse's RCP tool.
 * Here some of the application properties are defined before the window is opened.
 * You can decide whether to show some of the visual components given by the RCP tool, like:
 * <ul>
 * 	<li>Menu Bar</li>
 *  <li>Cool Bar (Eclipse's toolbar)</li>
 *  <li>Perspective Bar</li>
 *  <li>Status Bar</li>
 *  <li>etc...</li>
 * </ul>
 * There are also some application visual preferences that can be set with the setPreferences method.
 *
 *
 * @author Eduardo Pena
 *
 */
public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

    public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        super(configurer);
    }

    public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
        return new ApplicationActionBarAdvisor(configurer);
    }

    /**
     * This method is called before the application window is opened.
     * Here you can set which of eclipse's visual elements you want to show in your application.
     */
    public void preWindowOpen() {

    	IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        setPreferences();
        //set the application title
        configurer.setTitle("Slingshot");
        //handle the menu bar
        configurer.setShowMenuBar(true);
        //handle the cool bar (eclipse's toolbar)
        configurer.setShowCoolBar(true);
        //handle the status line
        configurer.setShowStatusLine(false);
        //handle the perspective bar
        configurer.setShowPerspectiveBar(true);

    }
    
    public void postWindowOpen() {
    	//set python.path. this is very important
		System.setProperty("python.path",
				ConfigurationHandler.getPrimexDirectory()+"/netIDE/jprime/jprime.jar:"+
				ConfigurationHandler.getPrimexDirectory()+"/netscript/lib/hsqldb.jar:");

    }

    /**
     * This method is called in the preWindowOpen() method.
     * Here you can configure the look and feel of your application by setting preferences of the application.
     */
    private void setPreferences(){
    	IPreferenceStore preferences = PlatformUI.getPreferenceStore();

    	//shows the perspective bar in the top right portion of the GUI
        preferences.setDefault("DOCK_PERSPECTIVE_BAR", "TOP_RIGHT");
        //shows the new curved-style tabs
        preferences.setDefault("SHOW_TRADITIONAL_STYLE_TABS", false);
        //enables the text for the perspective icons in the perspective bar
        preferences.setDefault("SHOW_TEXT_ON_PERSPECTIVE_BAR", true);
    }
}
