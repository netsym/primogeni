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
import java.util.Map;

import jprime.Experiment;
import jprime.Net.INet;
import jprime.database.Database;

/**
 * @author Nathanael Van Vorst
 *
 */
public class XMLModelInterface extends ModelInterface {
	private final File xmlModel;
	public XMLModelInterface(Database db, Experiment exp, File xmlModel) {
		super(db, exp, new ArrayList<ModelParam>());
		this.xmlModel=xmlModel;
	}

	public XMLModelInterface(Database db, String exp_name, File xmlModel) {
		super(db, exp_name, new ArrayList<ModelParam>());
		this.xmlModel=xmlModel;
	}

	@Override
	public INet buildModel(Map<String, ModelParamValue> parameters) {
		XMLModelBuilder builder = new XMLModelBuilder(this.exp,xmlModel);
		builder.createModel();
		if(builder.getExp()==null) {
			throw new RuntimeException("Can't compile libraries....");
		}
		return builder.getExp().getRootNode();
	}
	
}
