package slingshot.popup;

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

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.navigator.wizards.WizardShortcutAction;
import org.eclipse.ui.navigator.CommonActionProvider;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;
import org.eclipse.ui.navigator.ICommonMenuConstants;
import org.eclipse.ui.navigator.ICommonViewerWorkbenchSite;
import org.eclipse.ui.navigator.WizardActionGroup;

import slingshot.experiment.create.CreateExperimentWizard;
import slingshot.experiment.structure.ISlingshotExperimentElement;
import slingshot.experiment.structure.SlingshotExperimentFolder;
import slingshot.experiment.structure.SlingshotExperimentParent;
import slingshot.wizards.creation.SlingshotNewFileWizard;
import slingshot.wizards.creation.SlingshotNewFolderWizard;

/**
 * This class handles the New item in the project explorer pop-up menu, which allows the user to create new projects and files.
 *
 * @author Eduardo Pena
 *
 */
@SuppressWarnings("restriction")
public class SlingshotNewActionProvider extends CommonActionProvider {

	/** The name for the 'new' menu item */
	private static final String NEW_MENU_NAME = "common.new.menu";

	/** An action group for the new wizard actions */
    private WizardActionGroup newWizardActionGroup;

    /** The Wizard Shortcut Action for file creation */
    private WizardShortcutAction newFileAction;
    private WizardShortcutAction newFolderAction;
    /** The Wizard Shortcut Action for experiment creation */
    private WizardShortcutAction newExperimentAction;
    @SuppressWarnings("unused")
	private WizardShortcutAction newLibraryAction;

    private boolean contribute = false;

    /**
     * The initialization method for the action provider.
     * Here the wizard shortcut actions are initialized.
     */
    @Override
    public void init(ICommonActionExtensionSite anExtensionSite) {

        if (anExtensionSite.getViewSite() instanceof ICommonViewerWorkbenchSite) {
            IWorkbenchWindow window = ((ICommonViewerWorkbenchSite) anExtensionSite.getViewSite()).getWorkbenchWindow();

            //initialize the action group
            newWizardActionGroup = new WizardActionGroup(window, PlatformUI.getWorkbench().getNewWizardRegistry(), WizardActionGroup.TYPE_NEW, anExtensionSite.getContentService());
            //initialize the Wizard Shortcut actions
            newFolderAction = new WizardShortcutAction(window, PlatformUI.getWorkbench().getNewWizardRegistry().findWizard(SlingshotNewFolderWizard.ID));
            newFileAction = new WizardShortcutAction(window, PlatformUI.getWorkbench().getNewWizardRegistry().findWizard(SlingshotNewFileWizard.ID));
            newExperimentAction = new WizardShortcutAction(window, PlatformUI.getWorkbench().getNewWizardRegistry().findWizard(CreateExperimentWizard.ID));
            //XXX newLibraryAction = new WizardShortcutAction(window, PlatformUI.getWorkbench().getNewWizardRegistry().findWizard(CreateLibraryWizard.ID));
            contribute = true;
        }
    }

    /**
     * This method prepares the 'new' menu to be displayed, depending on which item was right-clicked.
     */
    @Override
    public void fillContextMenu(IMenuManager menu) {
        IMenuManager submenu = new MenuManager("New", NEW_MENU_NAME);

        if(!contribute) {
            return;
        }

        // fill the menu from the commonWizard contributions
        newWizardActionGroup.setContext(getContext());
        newWizardActionGroup.fillContextMenu(submenu);


        //get the item that was clicked
        TreeSelection sel = (TreeSelection)getContext().getSelection();
        if(!sel.isEmpty()){
        	Object clickedElement = (sel.getPaths()[0]).getLastSegment();
        	//Add the wizard shortcut actions to the 'new' menu, depending on which item was clicked
            if(clickedElement instanceof ISlingshotExperimentElement){
            	if(clickedElement instanceof SlingshotExperimentParent || clickedElement instanceof SlingshotExperimentFolder){
            		submenu.add(newFolderAction);
            		submenu.add(newFileAction);
            		submenu.add(newExperimentAction);
            	}
            }
        }
        else{
        	submenu.add(newExperimentAction);
        	//XXX submenu.add(newLibraryAction);
        }
        // append the submenu after the GROUP_NEW group.
        menu.insertAfter(ICommonMenuConstants.GROUP_NEW, submenu);
    }

	@Override
    public void dispose() {
        super.dispose();
    }


}
