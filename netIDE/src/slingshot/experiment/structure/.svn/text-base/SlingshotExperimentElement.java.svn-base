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

import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.graphics.Image;

import slingshot.Activator;

/**
 * This class defines any Slingshot Project element.
 * Specific Slingshot Project elements should extend from this class and implement unimplemented methods.
 *
 * @author Eduardo Pena
 *
 */
public abstract class SlingshotExperimentElement implements ISlingshotExperimentElement {

	/** The image corresponding to this Slingshot project element */
	private Image _image;
	/** The text corresponding to this Slingshot project element */
	private String _name;
	/** The path for the image corresponding to this Slingshot project element */
	private String _imagePath;
	/** The parent of this Slingshot project element */
	private ISlingshotExperimentElement _parent;
	/** The children of this Slingshot project element */
	private ArrayList<Object> _children;


	/**
	 * The constructor for the Slingshot project element.
	 *
	 * @param parent				The parent of this Slingshot project element
	 * @param name					The name of this Slingshot project element
	 * @param imagePath				The path for the image corresponding to this Slingsot project element
	 */
	public SlingshotExperimentElement(ISlingshotExperimentElement parent, String name, String imagePath){
		_parent = parent;
		_name = name;
		_imagePath = imagePath;
	}

	/**
	 * This method obtains the children of this project element.
	 *
	 * @return Object[]				An array containing the children of this Slingshot project element
	 */
	@Override
	public Object[] getChildren() {
		_children = initializeChildren(getProject());
		return _children.toArray();
	}

	/**
	 * This method obtains the image of this project element based on the image path.
	 *
	 * @return Image				The image corresponding to this Slingshot project element
	 */
	@Override
	public Image getImage() {
		if(_image == null)
			_image = Activator.getImage(_imagePath);
		return _image;
	}

	/**
	 * This method obtains the parent of this Slingshot project element.
	 *
	 * @return Object				The parent of this Slingshot project element
	 */
	@Override
	public Object getParent() {
		return  _parent;
	}

	/**
	 * This method obtains the Slingshot project this element belongs to.
	 *
	 * @return IProject				The project this element belongs to
	 */
	@Override
	public IProject getProject() {
		return ((SlingshotExperimentElement) getParent()).getProject();
	}

	/**
	 * This method obtains the label to be displayed for this Slingshot project element.
	 *
	 * @return String				The label for this Slingshot project element.
	 */
	@Override
	public String getText() {
		return _name;
	}

	/**
	 * This method calculates whether this Slingshot project element has children.
	 *
	 * @return boolean				TRUE if this element has children, FALSE otherwise
	 */
	@Override
	public boolean hasChildren() {
		_children = initializeChildren(getProject());
		return !_children.isEmpty();
	}

	/** This method obtains the children of this Slingshot project element.
	 *
	 * @param project				The Slingshot project this element belongs to
	 * @return ArrayList<Object>	An ArrayList containing the children of this element.
	 */
	protected abstract ArrayList<Object> initializeChildren(IProject project);

}
