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
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import jprime.Experiment;
import jprime.StatusListener;
import jprime.EmulationProtocol.IEmulationProtocol;
import jprime.Interface.IInterface;
import jprime.Net.INet;
import jprime.TrafficPortal.ITrafficPortal;
import jprime.database.Database;
/* $if false == DEBUG $ */
import jprime.database.IPersistableCache;
import jprime.database.IPersistableCache.PhaseChange;
/* $endif$ */
import jprime.partitioning.Partitioning;
import jprime.visitors.TLVVisitor;
import jprime.visitors.XMLVisitor;

/**
 * @author Nathanael Van Vorst
 * a Class to help abstract the creation of models and TLV files.
 */
public abstract class ModelInterface {
	public static enum ModelParamType {
		FILE,
		FLOAT,
		INT,
		BOOLEAN,
		STRING,
	}
	
	public static class ModelParam {
		public final ModelParamType type;
		public final String description;
		public final String name;
		public final String defaultValue;
		public final String propName;
		public ModelParam(ModelParamType type, String description, String name, String propName, String defaultValue) {
			super();
			this.type = type;
			this.description = description;
			this.name = name;
			this.defaultValue=defaultValue;
			this.propName=propName;
		}
	}
	public static class ModelParamValue {
		public final ModelParam param;
		public final String value;
		public ModelParamValue(ModelParam param, String value) {
			super();
			this.param = param;
			this.value = value;
		}
		public File asFile() {
			return new File(value);
		}
		public int asInt() {
			return Integer.parseInt(value);
		}
		public long asLong() {
			return Long.parseLong(value);
		}
		public double asDouble() {
			return Double.parseDouble(value);
		}
		public boolean asBoolean() {
			return Boolean.parseBoolean(value);
		}
		public String asString() {
			return value;
		}
	}
	
	private final List<ModelParam> parameterDefinitions;
	private final Database db;
	protected final Experiment exp;

	protected ModelInterface() {
		this.db=null;
		this.exp = null;
		this.parameterDefinitions = new ArrayList<ModelInterface.ModelParam>();
	}

	public ModelInterface(Database db, Experiment exp, List<ModelParam> parameterDefinitions) {
		this.db=db;
		this.exp = exp;
		this.parameterDefinitions = parameterDefinitions;
	}
	
	public ModelInterface(Database db, String exp_name, List<ModelParam> parameterDefinitions) {
		this.db=db;
		this.exp = db.createExperiment(exp_name,true);
		this.parameterDefinitions = parameterDefinitions;
	}
	
	public ModelInterface(Database db, Experiment exp) {
		this.db=db;
		this.exp = exp;
		this.parameterDefinitions = new ArrayList<ModelInterface.ModelParam>();
	}
	
	public ModelInterface(Database db, String exp_name) {
		this.db=db;
		this.exp = db.createExperiment(exp_name,true);
		this.parameterDefinitions = new ArrayList<ModelInterface.ModelParam>();
	}
	
	public List<ModelParam> getParameterDefinitions() {
		return parameterDefinitions;
	}
	
	/**
	 * @return the experiment associated with this model
	 */
	public final Experiment getExperiment() {
		return exp;
	}

	/**
	 * @return the database associated with this model
	 */
	public final Database getDatabase() {
		return db;
	}
	
	/**
	 * Default TLV generation for a single processor on a single host
	 * @param outputXML
	 *            whether you want xml generated
	 * @param directoryToStoreFiles
	 *            where the partition tlv and possible xml and jpg will be
	 *            stored.
	 * @throws IOException 
	 */
	public final void createTLV(boolean outputXML, String directoryToStoreFiles, Map<String, ModelParamValue> parameters) throws IOException {
		createTLV("1::1,1",outputXML,directoryToStoreFiles, null, new ArrayList<ComputeNode>(), parameters);
	}
	
	public Map<String, ModelParamValue> loadParametersFromSystemProperties() {
		HashMap<String, ModelParamValue> rv = new HashMap<String, ModelParamValue>();
		for(ModelParam p : parameterDefinitions) {
			rv.put(p.name, new ModelParamValue(p, System.getProperty(p.propName,p.defaultValue)));
		}
		return rv;
	}

	public final PartTlvPair createTLV(
			String partString,
			boolean outputXML,
			String directoryToStoreFiles,
			HashMap<Portal,String> linked,
			List<ComputeNode> computeNodes,
			Map<String, ModelParamValue> parameters
			) throws IOException {
		/* $if false == DEBUG $ */
		IPersistableCache.logPhaseChange(PhaseChange.build_model, "");
		/* $endif$ */
		buildModel(parameters);
		exp.compile(new StatusListener());
		exp.save();
		return createOutput(exp, partString, outputXML, directoryToStoreFiles, linked, computeNodes);
	}
	

	public final static PartTlvPair createOutput(
			Experiment exp, 
			String partString,
			boolean outputXML, 
			String directoryToStoreFiles, 
			HashMap<Portal,String> linked,
			List<ComputeNode> computeNodes) throws IOException {
		jprime.Console.out.println("\t***************************************");
		jprime.Console.out.println("\tLinking Traffic Portals");
		jprime.Console.out.println("\t***************************************");
		int portal_count = 0;
		Set<Portal> portals = new HashSet<Portal>();
		if(linked == null) {
			//assume its a one-compute node env
			ComputeNode cn = new ComputeNode("control_ip", "data_ip", new ArrayList<Portal>());
			linked = new HashMap<Portal, String>();
			for(IEmulationProtocol emu : exp.getEmuProtocols()) {
				if(emu instanceof ITrafficPortal) {
					Portal p = new Portal("portal_"+portal_count,"some_ip_"+portal_count,cn);
					cn.addPortal(p);
					portal_count++;
					p.setLinkedInterface((IInterface)emu.getParent());
					portals.add(p);
				}
			}
		}
		else {
			for(IEmulationProtocol emu : exp.getEmuProtocols()) {
				if(emu instanceof ITrafficPortal) {
					portal_count++;
				}
			}
			for(Entry<Portal, String> e : linked.entrySet()) {
				IInterface iface = null;
				for(IEmulationProtocol emu : exp.getEmuProtocols()) {
					if(emu.getParent().getUniqueName().toString().replaceAll(":", ".").compareTo(e.getValue().replaceAll(":", "."))==0) {
						iface=(IInterface)emu.getParent();
					}
				}
				if(iface == null) {
					System.err.println("Can find interface '"+e.getValue()+"', ifaces with traffic portals consist of:");
					for(IEmulationProtocol emu : exp.getEmuProtocols()) {
						System.err.println("\t'"+emu.getParent().getUniqueName().toString()+"'");
					}
					System.exit(1);
				}
				else {
					e.getKey().setLinkedInterface(iface);
					portals.add(e.getKey());
				}
			}
		}
		if(portal_count != portals.size()) {
			System.err.println("Specified a runtime enviornment with portals, but did not link all the portals to interfaces!");
			System.err.println("Current links:");
			for(Portal p : portals) {
				System.err.println("\t"+p.getNode().getControl_ip()+":"+p.getName()
						+" --> "+p.getLinkedInterface().getUniqueName());
			}
			System.err.println("Traffic Portals:");
			for(IEmulationProtocol emu : exp.getEmuProtocols()) {
				if(emu instanceof ITrafficPortal) {
					System.err.println("\t"+emu.getUniqueName());
				}
			}
			System.exit(1);
		}
		else {
			System.out.println("\tPortal/Interface Links:");
			for(Portal p : portals) {
				System.out.println("\t\t"+p.getNode().getControl_ip()+":"+p.getName()
						+" --> "+p.getLinkedInterface().getUniqueName());
			}	
		}
		jprime.Console.out.println("\t***************************************");
		jprime.Console.out.println("\tPartitiong Model, partString="+partString);
		jprime.Console.out.println("\t***************************************");
		Partitioning partitioning = exp.partition(partString, portals, computeNodes);
		jprime.Console.out.println("\t***************************************");
		jprime.Console.out.println("\tCreating TLV");
		jprime.Console.out.println("\t***************************************");
		Map<Integer,String> tlvs = new HashMap<Integer, String>();
		new TLVVisitor(exp.getMetadata(), partitioning, directoryToStoreFiles, exp.getName(), tlvs, outputXML);
		if(outputXML) {
			String xmlFileName = directoryToStoreFiles+"/" + exp.getName().replace(' ', '_');
			jprime.Console.out.println("\t***************************************");
			jprime.Console.out.println("\tCreating XML");
			jprime.Console.out.println("\t***************************************");
			xmlFileName = directoryToStoreFiles+"/" + exp.getName().replace(' ', '_') + ".xml";
			new XMLVisitor(exp.getMetadata(),new PrintStream(new File(xmlFileName))).print(exp);

		}
		jprime.Console.out.println("\t***************************************");
		jprime.Console.out.println("\tFinished");
		jprime.Console.out.println("\t***************************************");
		return new PartTlvPair(exp.getName(),directoryToStoreFiles,partitioning, tlvs);
	}

	
	/**
	 * Create the top most network in for this experiment and return it. 
	 * @return
	 */
	public abstract INet buildModel(Map<String, ModelParamValue> parameters);

	


}
