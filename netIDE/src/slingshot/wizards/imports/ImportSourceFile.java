package slingshot.wizards.imports;

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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.ide.undo.CreateFileOperation;

/**
 * This is the Wizard handler class for the Import Source File Wizard.
 * The purpose of this Wizard is to import a Source File from the user's Hard Disk to a given Project in the Workspace.
 *
 * @author Eduardo Pena
 * @version 1.0.0
 *
 */
public class ImportSourceFile extends Wizard implements INewWizard {

	/** The name displayed on top of the Wizard's window */
	private static final String WIZARD_NAME = "Import Model";

	/** The workbench in use when the wizard is called */
	IWorkbench _workbench;
	/** The user's selection when the wizard is called */
	IStructuredSelection _selection;

	/** The Wizard Page that asks for the import's source and destination */
	ImportSourceFilePage importPage;

	/**
	 * The Constructor for this class.
	 * It only sets the Wizard Name.
	 */
	public ImportSourceFile() {
		setWindowTitle(WIZARD_NAME);
	}

	/**
	 * Adds the pages that the Wizard will display.
	 */
	@Override
	public void addPages(){
		super.addPages();

		importPage = new ImportSourceFilePage(WIZARD_NAME);

		addPage(importPage);

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

	/**
	 * Method called as soon as the Wizard is finished.
	 * Copies the Source File from the Hard disk to the selected Project in the Workspace
	 *
	 * @return boolean 				TRUE if the Wizard finished correctly, FALSE otherwise
	 */
	@Override
	public boolean performFinish() {
		//get the source and destination from the Import Page
		File source = importPage.getSourceFile();
		IFile dest = importPage.getDestinationFile();

		//attempt to get the data to copy
		InputStream in = null;
		try {
			in = new FileInputStream(source);

		} catch (FileNotFoundException e) {
			//error reading file
			System.out.println("error reading file");
			e.printStackTrace();
			return false;
		}
		//create the new file with the read data in the selected Project in the Workspace
		return createFile(dest,in);
	}

	/**
	 * Creates the given virtual IFile as a physical file in the Workspace.
	 *
	 * @param file					The virtual file to be created
	 * @param in					The data to write into the new file
	 * @return						TRUE if the file creation was succesful, FALSE otherwise
	 */
	private boolean createFile(IFile file, InputStream in){
		CreateFileOperation op = new CreateFileOperation(file,null,null,"file creation");
		try{
			op.execute(null, null);
			file.setContents(in, true, false, null);
		} catch(ExecutionException e){
			//error creating new file
			System.out.println("error creating new file");
			e.printStackTrace();
			return false;
		} catch (CoreException e) {
			//error setting contents
			System.out.println("error setting contents");
			e.printStackTrace();
		}
		return true;
	}

}
