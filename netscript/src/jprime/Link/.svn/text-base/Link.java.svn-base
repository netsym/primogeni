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

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import jprime.IModelNode;
import jprime.ModelNode;
import jprime.ModelNodeRecord;
import jprime.BaseInterface.BaseInterfaceAlias;
import jprime.BaseInterface.IBaseInterface;
import jprime.BaseInterface.IBaseInterfaceAlias;
import jprime.GhostInterface.GhostInterfaceAlias;
import jprime.GhostInterface.IGhostInterface;
import jprime.Interface.IInterface;
import jprime.Interface.InterfaceAlias;
import jprime.partitioning.Partitioning;
import jprime.partitioning.PartitioningVisitor;
import jprime.routing.IRouteVisitor;
import jprime.variable.Dataset;
import jprime.variable.Dataset.SimpleDatum;
import jprime.variable.ModelNodeVariable;
import jprime.visitors.IVizVisitor;
import jprime.visitors.VerifyVisitor;

import org.python.core.PyObject;
/**
 * @author Nathanael Van Vorst
 */
public class Link extends jprime.gen.Link implements jprime.Link.ILink {
	protected TreeSet<Integer> alignments;
	protected Set<Integer> rows = null;
	
	public Link(PyObject[] v, String[] s){super(v,s);}
	
	/**
	 * 
	 */
	public Link(ModelNodeRecord rec){
		super(rec);
		alignments=null;
	}
	
	/**
	 * @param parent
	 */
	public Link(jprime.IModelNode parent){
		super(parent);
		alignments=null;
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.Link; }
	
	/* (non-Javadoc)
	 * @see jprime.gen.Link#accept(jprime.visitors.IGenericVisitor)
	 */
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
	
	/* (non-Javadoc)
	 * @see jprime.IModelNode#accept(jprime.routing.VerifyRoutingVisitor)
	 */
	public void accept(VerifyVisitor visitor) {
		visitor.visit(this);
	}

	/* (non-Javadoc)
	 * @see jprime.IModelNode#accept(jprime.routing.IRouteVisitor)
	 */
	public void accept(IRouteVisitor visitor) {
		visitor.visit(this);
	}
	
	/* (non-Javadoc)
	 * @see jprime.IModelNode#accept(jprime.routing.VizVisitor)
	 */
	public void accept(IVizVisitor visitor){
		visitor.visit(this);
	}
	
	/* (non-Javadoc)
	 * @see jprime.ModelNode#accept(jprime.partitioning.PartitioningVisitor)
	 */
	public void accept(PartitioningVisitor visitor) {
		visitor.visit(this);
	}
	
	/* (non-Javadoc)
	 * @see jprime.IAlignedNode#resetAlignments()
	 */
	public void resetAlignments() {
		this.alignments=null;
	}
	
	/* (non-Javadoc)
	 * @see jprime.IAlignedNode#setAlignment(jprime.partitioning.Partitioning, int)
	 */
	public void setAlignment(Partitioning p, int alignment) {
		throw new RuntimeException("Cannot call this on nodes of type "+this.getClass().getSimpleName());
	}
	
	/* (non-Javadoc)
	 * @see jprime.ModelNode#getAlignments(jprime.partitioning.Partitioning)
	 */
	public Set<Integer> getAlignments(Partitioning p) {
		if(alignments == null) {
			alignments=new TreeSet<Integer>();
			for(ModelNode c : getAllChildren()) {
				alignments.addAll(c.getAlignments(p));
			}
		}
		return alignments;
	}
	
	public void addBaseInterface(jprime.BaseInterface.IBaseInterface iface){
		jprime.Console.out.println("Attaching "+iface.getUniqueName()+", type="+iface.getClass().getCanonicalName());
		if(iface instanceof jprime.Interface.Interface) {
			this.createInterface((jprime.Interface.Interface)iface);
		}
		else if(iface instanceof jprime.Interface.InterfaceReplica) {
			this.createInterface((jprime.Interface.InterfaceReplica)iface);
		}
		else {
			throw new RuntimeException("did not expect type "+iface.getClass().getCanonicalName());
		}
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
	
	/* (non-Javadoc)
	 * @see jprime.gen.Link#createBaseInterface(org.python.core.PyObject[], java.lang.String[])
	 */
	@Override
	public IBaseInterface createBaseInterface(PyObject[] v, String[] n) {
		IBaseInterface rv= super.createBaseInterface(v, n);
		checkAttachement(rv);
		return rv;
	}

	/* (non-Javadoc)
	 * @see jprime.gen.Link#createBaseInterface(java.lang.String, jprime.BaseInterface.IBaseInterface)
	 */
	@Override
	public IBaseInterface createBaseInterface(String name,
			IBaseInterface to_alias) {
		checkAttachement(to_alias);
		return super.createBaseInterface(name, to_alias);
	}

	/* (non-Javadoc)
	 * @see jprime.gen.Link#addBaseInterfaceAlias(jprime.BaseInterface.BaseInterfaceAlias)
	 */
	@Override
	public void addBaseInterfaceAlias(BaseInterfaceAlias kid) {
		checkAttachement(kid);
		super.addBaseInterfaceAlias(kid);
	}


	/* (non-Javadoc)
	 * @see jprime.gen.Link#createInterface(org.python.core.PyObject[], java.lang.String[])
	 */
	@Override
	public IInterface createInterface(PyObject[] v, String[] n) {
		IInterface rv = super.createInterface(v, n);
		checkAttachement(rv);
		return rv;
	}

	/* (non-Javadoc)
	 * @see jprime.gen.Link#createInterface(java.lang.String, jprime.Interface.IInterface)
	 */
	@Override
	public IInterface createInterface(String name, IInterface to_alias) {
		checkAttachement(to_alias);
		return super.createInterface(name, to_alias);
	}

	/* (non-Javadoc)
	 * @see jprime.gen.Link#addInterfaceAlias(jprime.Interface.InterfaceAlias)
	 */
	@Override
	public void addInterfaceAlias(InterfaceAlias kid) {
		checkAttachement(kid);
		super.addInterfaceAlias(kid);
	}

	/* (non-Javadoc)
	 * @see jprime.gen.Link#createGhostInterface(org.python.core.PyObject[], java.lang.String[])
	 */
	@Override
	public IGhostInterface createGhostInterface(PyObject[] v, String[] n) {
		IGhostInterface rv = super.createGhostInterface(v, n);
		checkAttachement(rv);
		return rv;
	}

	/* (non-Javadoc)
	 * @see jprime.gen.Link#createGhostInterface(java.lang.String, jprime.GhostInterface.IGhostInterface)
	 */
	@Override
	public IGhostInterface createGhostInterface(String name,
			IGhostInterface to_alias) {
		checkAttachement(to_alias);
		return super.createGhostInterface(name, to_alias);
	}

	/* (non-Javadoc)
	 * @see jprime.gen.Link#addGhostInterfaceAlias(jprime.GhostInterface.GhostInterfaceAlias)
	 */
	@Override
	public void addGhostInterfaceAlias(GhostInterfaceAlias kid) {
		checkAttachement(kid);
		super.addGhostInterfaceAlias(kid);
	}

	protected void checkAttachement(IBaseInterface l) {
		long pid = this.getParentId();
		IModelNode m = l.deference().getParent();
		while(m != null ) {
			if(m.getDBID() == pid) {
				//the attachment is either a sibling of a child of a sibling
				return;
			}
			m = m.getParent();
		}
		throw new RuntimeException("The interface "+l.deference().getUniqueName()+" is outside the network containing the link "+this.getUniqueName()+".");
	}
	
	public void setHubRows(Set<Integer> rows) {
		this.rows=rows;	
	}
	
	public Set<Integer> getHubRows() {
		return rows;
	}
	
	protected static void help(List<String> rv) {
		rv.add("Link:");
		rv.add("   getUID()\n"
				  +"      Get the UID of the node.");
		rv.add("   createInterface(IInterface iface)\n"
				  +"      Attach inteface iface to the link.");
		rv.add("   newInterface(PyObject[], String[])\n"+
				  "   A python function to attach interfaces to this link.\n");
		rv.add("   Property Setter/Getters:\n");		
		rv.add("      getAttachments()\n");
		rv.add("      setBandwidth(float)\n");
		rv.add("      getBandwidth()\n");
		rv.add("      setDelay(float)\n");
		rv.add("      getDelay()\n");
		rv.add("      getIpPrefix()\n");
		
	}
	
	public List<String> help() {
		List<String> rv=super.help();
		help(rv);
		return rv;
	}
	
	public double getTrafficIntensity(Dataset d) {
		double max=0.0;
		try {
			for(IBaseInterfaceAlias nica : this.getAttachments()) {
				final IInterface nic = (IInterface)nica.deference();
				long data=0; double time=0;
				final SimpleDatum in = nic.getRuntimeValueByName(ModelNodeVariable.num_in_bytes(), d);
				final SimpleDatum out = nic.getRuntimeValueByName(ModelNodeVariable.num_in_bytes(), d);
				if(out != null) {
					if(out.prev == null) {
						data=Long.parseLong(out.value);
						time=1;
					}
					else {
						data=Long.parseLong(out.value)-Long.parseLong(out.prev.value);
						time=out.time-out.prev.time;
					}
				}
				if(in != null) {
					if(in.prev == null) {
						data+=Long.parseLong(in.value);
						if(time == 0) time=1;
					}
					else {
						data+=Long.parseLong(in.value)-Long.parseLong(in.prev.value);
						double t = in.time-in.prev.time;
						if(time > t) time=t;
					}
				}
				if(data>0) {
					double b = Math.min(Double.parseDouble(nic.getAttributeValueByName(ModelNodeVariable.bit_rate())),
							Double.parseDouble(getAttributeValueByName(ModelNodeVariable.bandwidth())));
					b = data/time;
					if(b>1) return 1;
					if(b > max) max=b;
				}
			}
		}catch(Exception e){
			//e.printStackTrace();
		}
		return max;			
	}
}
