package jprime.visitors;

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

import java.io.IOException;
import java.io.PrintStream;

import jprime.EmulationCommand;
import jprime.Experiment;
import jprime.IModelNode;
import jprime.Metadata;
import jprime.ModelNode;
import jprime.ModelNodeAlias;
import jprime.ModelNodeReplica;
import jprime.Host.IHost;
import jprime.Net.INet;
import jprime.RoutingSphere.RoutingSphere;
/* $if false == DEBUG $ */
import jprime.database.IPersistableCache;
/* $endif$ */
import jprime.database.IPersistableCache.PhaseChange;
import jprime.routing.StaticRoutingProtocol;
import jprime.variable.ModelNodeVariable;
import jprime.variable.SymbolVariable;

/**
 *export a jPrime model to the xPrime xml format
 * @author Nathanael Van Vorst
 */
public class XMLVisitor {
	private static final String TAB="\t";
	protected final PrintStream out;
	protected String tab;

	public XMLVisitor(Metadata meta,PrintStream out) {
    	/* $if DEBUG $
		meta.logPhaseChange(PhaseChange.start_xml);
		$else$ */
		IPersistableCache.logPhaseChange(PhaseChange.start_xml, "");
		/* $endif$ */
		this.out=out;
		this.tab=TAB;
	}

	/**
	 * @param partitioning
	 * @throws IOException 
	 */
	public void print(Experiment exp) throws IOException {
		out.print("<?xml version=\"1.0\" ?>\n");
		out.print("<model xmlns:xsi=\"http://www.w3.org/2001/XMLSchema_instance\" xsi:noNamespaceSchemaLocation=\"primex.xsd\">\n");
		visit(exp.getRootNode());
		out.print("</model>\n");
	}
	
	/**
	 * @param n
	 */
	public void visit(IModelNode n) {
		if(n instanceof RoutingSphere) {
			out.print(tab+"<!-- RoutingSphere name="+n.getName()+" numRoutes="+((RoutingSphere)n).getRouteTable().getPermRouteEntries().size()+"  -->\n");
			return;
		}
		else if(n.isReplica()) {
			String p = ModelNode.relativePath(n, ((ModelNodeReplica)n).getReplicatedNode());
			if(p.startsWith("..:"))
				p=p.substring(3);
			out.print(tab+"<replica name=\""+n.getName()+"\" path=\""+p+"\" />\n");
		}
		else if(n.isAlias()) {
			out.print(tab+"<ref name=\""+n.getName()+"\" path=\""+((ModelNodeAlias)n).getAliasPath().substring(3)+"\" />\n");
		}
		else {
			final String type =  n.getClass().getSimpleName().replace("Alias", "").replace("Replica","");
			out.print(tab+"<node name=\""+n.getName()+"\" type=\""+type+"\" >\n");
			String t=tab;
			tab+=TAB;
			for(ModelNodeVariable  a : n.getAttributeValues()) {
				if(a.getDBName() == ModelNodeVariable.name()
						|| a.getDBName() == ModelNodeVariable.alias_path()
						|| a.getDBName() == ModelNodeVariable.offset()
						|| a.getDBName() == ModelNodeVariable.ip_address()
						|| a.getDBName() == ModelNodeVariable.ip_prefix()
						|| a.getDBName() == ModelNodeVariable.ip_prefix_len()
						|| a.getDBName() == ModelNodeVariable.size()
						|| a.getDBName() == ModelNodeVariable.size()						
						|| a.getDBName() == ModelNodeVariable.uid()) continue;
				if(ModelNodeVariable.shouldEncodeAttr(a.getDBName())) {
					out.print(tab+"<attribute name=\""+ModelNodeVariable.int2name(a.getDBName())+"\" value=\""+a.toString()+"\" symbol=\""+(a instanceof SymbolVariable)+"\" />\n");
				}
			}
			if(n instanceof IHost) {
				IHost h = (IHost)n;
				if(null != h.getEmulationCommands()) {
					for(EmulationCommand e : h.getEmulationCommands()) {
						out.print(tab+"<emucmd outputSuffix=\""+e.getOutputSuffix()+"\""+
								" delay=\""+e.getDelay()+"\""+
								" maxRuntime=\""+e.getRawMaxRuntime()+"\""+
								" block=\""+e.isBlocking()+"\""+
								" checkReturnCode=\""+e.shouldCheckReturnCode()+"\""+
								" cmd=\""+e.getRawCmd()+"\" />\n");
					}
				}
			}
			for(IModelNode c : n.getAllChildren()) {
				c.accept(this);
			}
			if(n instanceof INet) {
				final StaticRoutingProtocol p = ((INet)n).getStaticRoutingProtocol();
				if(null != p) {
					final String type1 =  p.getClass().getSimpleName().replace("Alias", "").replace("Replica","");
					out.print(tab+"<node name=\""+n.getDBID()+"\" type=\""+type1+"\" />\n");
				}
			}
			tab=t;
			out.print(tab+"</node>\n");			
		}
	}
}
