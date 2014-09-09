package jprime.util;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jprime.Experiment;
import jprime.Net.INet;
import jprime.database.Database;

import org.python.util.PythonInterpreter;

/**
 * @author Nathanael Van Vorst
 *
 */
public class PythonModelInterface extends ModelInterface {
	private final File pythonModel;
	private static final HashMap<Integer, PythonModelInterface> models = new HashMap<Integer, PythonModelInterface>(); 
	
	public static synchronized PythonModelInterface getModel(int hashcode) {
		if(models.containsKey(hashcode)) {
			return models.remove(hashcode);
		}
		return null;
	}
	private static synchronized void addModel(PythonModelInterface m) {
		if(models.containsKey(m.hashCode()))
			throw new RuntimeException("how did this happen?");
		models.put(m.hashCode(), m);
	}
	public PythonModelInterface(Database db, Experiment exp, File pythonModel) {
		super(db, exp, new ArrayList<ModelParam>());
		this.pythonModel=pythonModel;
		addModel(this);
	}

	public PythonModelInterface(Database db, String exp_name, File pythonModel) {
		super(db, exp_name, new ArrayList<ModelParam>());
		this.pythonModel=pythonModel;
		addModel(this);
	}

	@Override
	public INet buildModel(Map<String, ModelParamValue> parameters) {
		final PythonInterpreter interp = new PythonInterpreter();
		interp.setOut(System.out);
		interp.setErr(System.err);
		interp.setIn(System.in);
		interp.exec("import sys");
		interp.exec("import jprime");
		interp.exec("import jprime.util");
		interp.exec("exp=jprime.util.PythonModelInterface.getModel("+this.hashCode()+").getExperiment();");
		interp.execfile(pythonModel.getAbsolutePath());
		final INet topnet = getExperiment().getRootNode();
		if(topnet == null) throw new RuntimeException("You didn't build a model!");
		return topnet;
	}
	
}
