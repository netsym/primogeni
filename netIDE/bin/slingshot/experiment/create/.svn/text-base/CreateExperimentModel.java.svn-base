package slingshot.experiment.create;

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

import org.eclipse.core.resources.IProject;

/**
 * This class serves as a Model that holds the data required through the Load Experiment Wizard
 *
 * @author Eduardo Pena
 *
 */
public class CreateExperimentModel {
	/** The project that owns this experiment */
	protected IProject project = null;
	
	/** The sourceFile used for this experiment */
	protected File sourceFile = null;


	/**
	 * @return
	 */
	public File getSourceFile() {
		return sourceFile;
	}

	/**
	 * @param sourceFile
	 */
	public void setSourceFile(File sourceFile) {
		this.sourceFile = sourceFile;
	}

	/**
	 * @param project
	 */
	public void setProject(IProject project) {
		this.project = project;
	}

	/**
	 * @return
	 */
	public IProject getProject(){
		return project;
	}

	/**
	 * @return
	 */
	public String getExpName(){
		return project.getName();
	}
}
