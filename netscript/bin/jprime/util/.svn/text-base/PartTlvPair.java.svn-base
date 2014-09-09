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

import java.util.Map;

import jprime.partitioning.Partitioning;

/**
 * @author Nathanael Van Vorst
 * a Class to help abstract the creation of models and TLV files.
 */
public class PartTlvPair {
	public final String expName;
	public final String resultsDir;
	public final Partitioning partitioning;
	public final Map<Integer,String> tlvs;		
	public PartTlvPair(String expName,String resultsDir, Partitioning partitioning, Map<Integer,String> tlvs) {
		super();
		this.expName=expName;
		this.resultsDir=resultsDir;
		this.partitioning = partitioning;
		this.tlvs = tlvs;
	}
}