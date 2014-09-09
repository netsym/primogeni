package slingshot.navigator;

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


import java.text.Collator;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

/**
 * This class defines the order in which elements are displayed in the project explorer.
 *
 * @author Eduardo Pena
 */
public class CategorySorter extends ViewerSorter {

	/**
	 * Empty constructor
	 */
	public CategorySorter() {
	}

	/**
	 * One parameter constructor
	 *
	 * @param collator
	 */
	public CategorySorter(Collator collator) {
		super(collator);
	}

	/**
	 * This method determines the order in which the elements of a Slingshot project are displayed.
	 *
	 * @param viewer			The navigator viewer
	 * @param e1				The first object to compare
	 * @param e2				The second object to compare
	 * @return int				-1 if e1 should go before e2, 0 if they are the same, 1 if e1 should go after e2
	 */
	@Override
	public int compare(Viewer viewer, Object e1, Object e2){
		/*String catName1;
		String catName2;
		//get the name of the folder/file defined by e1
		if(e1 instanceof ISlingshotExperimentElement)
			catName1 = ((ISlingshotExperimentElement)e1).getText();
		else
			catName1 = ((IFile)e1).getName();
		//get the name of the folder/file defined by e2
		if(e2 instanceof ISlingshotExperimentElement)
			catName2 = ((ISlingshotExperimentElement)e2).getText();
		else
			catName2 = ((IFile)e2).getName();*/

		int result = -1;
		//determine which object goes first
		//TODO
		/*
		if(catName1.equals(SlingshotExperimentModel.NAME)){
			result = -1;
		} else if(catName2.equals(SlingshotExperimentModel.NAME)){
			result = 1;
		} else if(catName2.equals(SlingshotExperimentResults.NAME)){
			result = 1;
		}
	*/
		if(e1 instanceof IFile && e2 instanceof IFile){
			IFile f1 = (IFile) e1;
			IFile f2 = (IFile) e2;

			if(f1.getFileExtension().equalsIgnoreCase("java"))
				result = -1;
			else if(f2.getFileExtension().equalsIgnoreCase("java"))
				result = 1;
			else if(f2.getFileExtension().equalsIgnoreCase("py"))
				result = 1;
		}

		return result;
	}

}
