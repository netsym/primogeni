package slingshot.experiment.structure;

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

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.graphics.Image;

/**
 * This interface provides a wrapper for all the elements inside a Slingshot project, including the project itself.
 *
 * @author Eduardo Pena
 * @version 1.0.0
 *
 */
public interface ISlingshotExperimentElement{
	/** returns the image to display for this project element */
	public Image getImage();
	/** returns the children of this project element */
    public Object[] getChildren();
    /** returns the text to display for this project element */
    public String getText();
    /** returns whether this project element has childrens */
    public boolean hasChildren();
    /** returns the Slingshot project this element belongs to */
    public IProject getProject();
    /** returns the parent of this project element */
    public Object getParent();
}
