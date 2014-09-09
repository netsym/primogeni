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

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

import slingshot.experiment.structure.ISlingshotExperimentElement;

/**
 * This class provides the labels and images that are displayed in the project explorer for each project element.
 *
 * @author Eduardo Pena
 */
public class LabelProvider implements ILabelProvider {


	/**
	 * Returns the image for the given element to be displayed in the project explorer
	 *
	 * @param element		The element in need of an image
	 * @return Image		The image to be displayed for this element
	 */
	@Override
	public Image getImage(Object element) {
        Image image = null;
        //if its an element of our Slingshot project structure, just call the getImage method for that element
        if (element instanceof ISlingshotExperimentElement) {
            image = ((ISlingshotExperimentElement)element).getImage();
        }
        // else ignore the element
        return image;
	}

	/**
	 * Returns the label for the given element to be displayed in the project explorer
	 *
	 * @param element			The element in need of text
	 * @return String			The label to be displayed for this element
	 */
	@Override
	public String getText(Object element) {
		String text = "";
		//if its an element of our Slingshot project structure, just call the getText method for that element
		if (element instanceof ISlingshotExperimentElement) {
            text = ((ISlingshotExperimentElement)element).getText();
        }
	    // else ignore the element
	    return text;
	}

	@Override
	public void addListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

}
