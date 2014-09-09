package jprime.Link;

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

import java.util.Collection;
import java.util.List;
import java.util.Set;

import jprime.IModelNode;
import jprime.ModelNodeRecord;
import jprime.partitioning.Partitioning;
import jprime.variable.Dataset;

import org.python.core.PyObject;
/**
 * @author Nathanael Van Vorst
 *
 */
public class LinkAlias extends jprime.gen.LinkAlias implements jprime.Link.ILinkAlias {
	public LinkAlias(PyObject[] v, String[] s){super(v,s);}
	
	/**
	 * 
	 */
	public LinkAlias(ModelNodeRecord rec){ super(rec); }
	
	public LinkAlias(IModelNode parent){ super(parent); }
	
	/**
	 * @param parent
	 * @param referencedNode
	 */
	public LinkAlias(IModelNode parent, jprime.Link.ILink referencedNode) {
		super(parent,(jprime.Link.ILink)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.LinkAlias; }
	
	/* (non-Javadoc)
	 * @see jprime.gen.LinkAlias#accept(jprime.visitors.IGenericVisitor)
	 */
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
	
	/* (non-Javadoc)
	 * @see jprime.Link.ILink#addNodeAlignments(java.util.SortedSet)
	 */
	public void addNodeAlignments(Collection<String> to_add) {
		throw new RuntimeException("Cant add alignemnts to link aliases!");
	}
	
	/* (non-Javadoc)
	 * @see jprime.IAlignedNode#resetAlignments()
	 */
	public void resetAlignments() {
		((ILink)deference()).resetAlignments();
	}

	/* (non-Javadoc)
	 * @see jprime.IAlignedNode#setAlignment(jprime.partitioning.Partitioning, int)
	 */
	public void setAlignment(Partitioning p, int alignment) {
		throw new RuntimeException("Cannot call this on nodes of type "+this.getClass().getSimpleName());
	}
	
	/* (non-Javadoc)
	 * @see jprime.ModelNodeAlias#getAlignments(jprime.partitioning.Partitioning)
	 */
	public Set<Integer> getAlignments(Partitioning p) {
		return ((ILink)deference()).getAlignments(p);
	}
	
	public jprime.Interface.IInterface attachInterface(jprime.Interface.IInterface to_alias) {
		return createInterface(to_alias);
	}

	public jprime.Interface.IInterface attachInterface(PyObject[] v, String[] n){
		return createInterface(v,n);
	}

	public jprime.Interface.IInterface attachInterface(String name, jprime.Interface.IInterface to_alias){
		return createInterface(name,to_alias);
	}
	
	public void setHubRows(Set<Integer> rows) {
		((ILink)deference()).setHubRows(rows);
	}
	
	public Set<Integer> getHubRows() {
		return ((ILink)deference()).getHubRows();
	}

	
	public List<String> help() {
		List<String> rv=super.help();
		Link.help(rv);
		return rv;
	}	

	public double getTrafficIntensity(Dataset d) {
		return ((ILink)deference()).getTrafficIntensity(d);
	}
}
