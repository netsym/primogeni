/* ------------------------- */
/* ------------------------- */
/*         WARNING: */
/*  THIS FILE IS GENERATED! */
/*        DO NOT EDIT! */
/* ------------------------- */
/* ------------------------- */


package jprime.gen;

import jprime.*;
import jprime.variable.*;
import jprime.ModelNodeRecord;
import org.python.core.PyObject;
import org.python.core.Py;
public abstract class ProtocolGraphReplica extends jprime.ModelNodeReplica implements jprime.gen.IProtocolGraph {

	/* used to enforce the minimum/maximum child requirement */

	public void enforceChildConstraints() {
		super.enforceChildConstraints();
	getCfg_sess().enforceChildConstraints();
	}
	public ProtocolGraphReplica(String name, IModelNode parent, jprime.ProtocolGraph.IProtocolGraph referencedNode) {
		super(name,parent,referencedNode);
	}
	public ProtocolGraphReplica(ModelNodeRecord rec){ super(rec); }
	public ProtocolGraphReplica(PyObject[] v, String[] s){super(v,s);}
	/**
	 * @return the interface which this node implements
	 */
	public Class<?> getNodeType() {
		return jprime.ProtocolGraph.IProtocolGraph.class;
	}
	/**
	 * @param used by replicas to do a deep copy of the node.
	 */
	public jprime.ModelNode deepCopy(jprime.ModelNode parent) {
		doing_deep_copy=true;
		jprime.ProtocolGraph.ProtocolGraphReplica c = new jprime.ProtocolGraph.ProtocolGraphReplica(this.getName(), (IModelNode)parent,(jprime.ProtocolGraph.IProtocolGraph)this);
		doing_deep_copy=false;
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1143: //ProtocolGraphReplica
			case 1144: //HostReplica
			case 1145: //RouterReplica
			case 1199: //ProtocolGraphAliasReplica
			case 1200: //HostAliasReplica
			case 1201: //RouterAliasReplica
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public abstract int getTypeId();

	/**
	 * @return whether protocol sessions should be created on demand
	 */
	public jprime.variable.BooleanVariable getAutomaticProtocolSessionCreation() {
		jprime.variable.BooleanVariable temp = (jprime.variable.BooleanVariable)getAttributeByName(ModelNodeVariable.automatic_protocol_session_creation());
		if(null!=temp) return temp;
		return (jprime.variable.BooleanVariable)this.getReplicatedNode().getAttributeByName(ModelNodeVariable.automatic_protocol_session_creation());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setAutomaticProtocolSessionCreation(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.automatic_protocol_session_creation());
		if(temp==null){
			temp=new jprime.variable.BooleanVariable(jprime.gen.ModelNodeVariable.automatic_protocol_session_creation(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.BooleanVariable)){
				temp=new jprime.variable.BooleanVariable(jprime.gen.ModelNodeVariable.automatic_protocol_session_creation(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.BooleanVariable)temp).setValue(value); }
		}
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setAutomaticProtocolSessionCreation(boolean value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.automatic_protocol_session_creation());
		if(temp==null){
			temp=new jprime.variable.BooleanVariable(jprime.gen.ModelNodeVariable.automatic_protocol_session_creation(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.BooleanVariable)){
				temp=new jprime.variable.BooleanVariable(jprime.gen.ModelNodeVariable.automatic_protocol_session_creation(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.BooleanVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setAutomaticProtocolSessionCreation(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.automatic_protocol_session_creation());
		addAttr(value);
	}

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds() {
		return jprime.gen.ProtocolGraph.attrIds;
	}

	/**
	  * Create a new child of type jprime.ProtocolSession.ProtocolSession and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.ProtocolSession.IProtocolSession createProtocolSession() {
		return createProtocolSession(null);
	}

	/**
	  * jython method to create a a new child of type jprime.ProtocolSession.ProtocolSession, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.ProtocolSession.IProtocolSession createProtocolSession(PyObject[] v, String[] n) {
		if(jprime.ModelNode.__hasReplicate(v,n,false)) {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.ProtocolSession.ProtocolSessionReplica temp = new jprime.ProtocolSession.ProtocolSessionReplica(v1,n);
			addChild(temp);
			return temp;
		} else {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.ProtocolSession.ProtocolSession temp = new jprime.ProtocolSession.ProtocolSession(v1,n);
			addChild(temp);
			return temp;
		}
	}

	 /**
	  * Create a new child of type jprime.ProtocolSession.ProtocolSession and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.ProtocolSession.IProtocolSession createProtocolSession(String name) {
		jprime.ProtocolSession.ProtocolSession temp = new jprime.ProtocolSession.ProtocolSession(this);
		temp.setName(name);
		addChild(temp);
		return temp;
	}

	 /**
	  * Add a new child of type jprime.ProtocolSession.ProtocolSession.
	  */
	public void addProtocolSession(jprime.ProtocolSession.ProtocolSession kid) {
		addChild(kid);
	}

	 /**
	  * Create a new child of type jprime.ProtocolSession.ProtocolSessionReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.ProtocolSession.IProtocolSession createProtocolSessionReplica(jprime.ProtocolSession.IProtocolSession to_replicate) {
		jprime.ProtocolSession.ProtocolSessionReplica temp = new jprime.ProtocolSession.ProtocolSessionReplica(null, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * jython method to create replica new child of type jprime.ProtocolSession.ProtocolSessionReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.ProtocolSession.IProtocolSession replicateProtocolSession(PyObject[] v, String[] n) {
		PyObject[] v1 = new PyObject[v.length+1];
		for(int i=0;i<v.length;i++)v1[i+1]=v[i];
		v1[0]=Py.java2py(this);
		jprime.ProtocolSession.ProtocolSessionReplica temp = new jprime.ProtocolSession.ProtocolSessionReplica(v1,n);
		addChild(temp);
		return temp;
	}

	 /**
	  * Create a new child of type jprime.ProtocolSession.ProtocolSessionReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.ProtocolSession.IProtocolSession createProtocolSessionReplica(String name, jprime.ProtocolSession.IProtocolSession to_replicate) {
		jprime.ProtocolSession.ProtocolSessionReplica temp = new jprime.ProtocolSession.ProtocolSessionReplica(name, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * return Protocol sessions defined in the protocol graph
	  */
	public jprime.util.ChildList<jprime.ProtocolSession.IProtocolSession> getCfg_sess() {
		return new jprime.util.ChildList<jprime.ProtocolSession.IProtocolSession>(this, 1039, 0, 0);
	}

	/**
	  * Create a new child of type jprime.CNFTransport.CNFTransport and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.CNFTransport.ICNFTransport createCNFTransport() {
		return createCNFTransport(null);
	}

	/**
	  * jython method to create a a new child of type jprime.CNFTransport.CNFTransport, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.CNFTransport.ICNFTransport createCNFTransport(PyObject[] v, String[] n) {
		if(jprime.ModelNode.__hasReplicate(v,n,false)) {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.CNFTransport.CNFTransportReplica temp = new jprime.CNFTransport.CNFTransportReplica(v1,n);
			addChild(temp);
			return temp;
		} else {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.CNFTransport.CNFTransport temp = new jprime.CNFTransport.CNFTransport(v1,n);
			addChild(temp);
			return temp;
		}
	}

	 /**
	  * Create a new child of type jprime.CNFTransport.CNFTransport and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.CNFTransport.ICNFTransport createCNFTransport(String name) {
		jprime.CNFTransport.CNFTransport temp = new jprime.CNFTransport.CNFTransport(this);
		temp.setName(name);
		addChild(temp);
		return temp;
	}

	 /**
	  * Add a new child of type jprime.CNFTransport.CNFTransport.
	  */
	public void addCNFTransport(jprime.CNFTransport.CNFTransport kid) {
		addChild(kid);
	}

	 /**
	  * Create a new child of type jprime.CNFTransport.CNFTransportReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.CNFTransport.ICNFTransport createCNFTransportReplica(jprime.CNFTransport.ICNFTransport to_replicate) {
		jprime.CNFTransport.CNFTransportReplica temp = new jprime.CNFTransport.CNFTransportReplica(null, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * jython method to create replica new child of type jprime.CNFTransport.CNFTransportReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.CNFTransport.ICNFTransport replicateCNFTransport(PyObject[] v, String[] n) {
		PyObject[] v1 = new PyObject[v.length+1];
		for(int i=0;i<v.length;i++)v1[i+1]=v[i];
		v1[0]=Py.java2py(this);
		jprime.CNFTransport.CNFTransportReplica temp = new jprime.CNFTransport.CNFTransportReplica(v1,n);
		addChild(temp);
		return temp;
	}

	 /**
	  * Create a new child of type jprime.CNFTransport.CNFTransportReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.CNFTransport.ICNFTransport createCNFTransportReplica(String name, jprime.CNFTransport.ICNFTransport to_replicate) {
		jprime.CNFTransport.CNFTransportReplica temp = new jprime.CNFTransport.CNFTransportReplica(name, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * Create a new child of type jprime.STCPMaster.STCPMaster and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.STCPMaster.ISTCPMaster createSTCPMaster() {
		return createSTCPMaster(null);
	}

	/**
	  * jython method to create a a new child of type jprime.STCPMaster.STCPMaster, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.STCPMaster.ISTCPMaster createSTCPMaster(PyObject[] v, String[] n) {
		if(jprime.ModelNode.__hasReplicate(v,n,false)) {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.STCPMaster.STCPMasterReplica temp = new jprime.STCPMaster.STCPMasterReplica(v1,n);
			addChild(temp);
			return temp;
		} else {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.STCPMaster.STCPMaster temp = new jprime.STCPMaster.STCPMaster(v1,n);
			addChild(temp);
			return temp;
		}
	}

	 /**
	  * Create a new child of type jprime.STCPMaster.STCPMaster and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.STCPMaster.ISTCPMaster createSTCPMaster(String name) {
		jprime.STCPMaster.STCPMaster temp = new jprime.STCPMaster.STCPMaster(this);
		temp.setName(name);
		addChild(temp);
		return temp;
	}

	 /**
	  * Add a new child of type jprime.STCPMaster.STCPMaster.
	  */
	public void addSTCPMaster(jprime.STCPMaster.STCPMaster kid) {
		addChild(kid);
	}

	 /**
	  * Create a new child of type jprime.STCPMaster.STCPMasterReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.STCPMaster.ISTCPMaster createSTCPMasterReplica(jprime.STCPMaster.ISTCPMaster to_replicate) {
		jprime.STCPMaster.STCPMasterReplica temp = new jprime.STCPMaster.STCPMasterReplica(null, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * jython method to create replica new child of type jprime.STCPMaster.STCPMasterReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.STCPMaster.ISTCPMaster replicateSTCPMaster(PyObject[] v, String[] n) {
		PyObject[] v1 = new PyObject[v.length+1];
		for(int i=0;i<v.length;i++)v1[i+1]=v[i];
		v1[0]=Py.java2py(this);
		jprime.STCPMaster.STCPMasterReplica temp = new jprime.STCPMaster.STCPMasterReplica(v1,n);
		addChild(temp);
		return temp;
	}

	 /**
	  * Create a new child of type jprime.STCPMaster.STCPMasterReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.STCPMaster.ISTCPMaster createSTCPMasterReplica(String name, jprime.STCPMaster.ISTCPMaster to_replicate) {
		jprime.STCPMaster.STCPMasterReplica temp = new jprime.STCPMaster.STCPMasterReplica(name, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * Create a new child of type jprime.UDPMaster.UDPMaster and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.UDPMaster.IUDPMaster createUDPMaster() {
		return createUDPMaster(null);
	}

	/**
	  * jython method to create a a new child of type jprime.UDPMaster.UDPMaster, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.UDPMaster.IUDPMaster createUDPMaster(PyObject[] v, String[] n) {
		if(jprime.ModelNode.__hasReplicate(v,n,false)) {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.UDPMaster.UDPMasterReplica temp = new jprime.UDPMaster.UDPMasterReplica(v1,n);
			addChild(temp);
			return temp;
		} else {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.UDPMaster.UDPMaster temp = new jprime.UDPMaster.UDPMaster(v1,n);
			addChild(temp);
			return temp;
		}
	}

	 /**
	  * Create a new child of type jprime.UDPMaster.UDPMaster and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.UDPMaster.IUDPMaster createUDPMaster(String name) {
		jprime.UDPMaster.UDPMaster temp = new jprime.UDPMaster.UDPMaster(this);
		temp.setName(name);
		addChild(temp);
		return temp;
	}

	 /**
	  * Add a new child of type jprime.UDPMaster.UDPMaster.
	  */
	public void addUDPMaster(jprime.UDPMaster.UDPMaster kid) {
		addChild(kid);
	}

	 /**
	  * Create a new child of type jprime.UDPMaster.UDPMasterReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.UDPMaster.IUDPMaster createUDPMasterReplica(jprime.UDPMaster.IUDPMaster to_replicate) {
		jprime.UDPMaster.UDPMasterReplica temp = new jprime.UDPMaster.UDPMasterReplica(null, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * jython method to create replica new child of type jprime.UDPMaster.UDPMasterReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.UDPMaster.IUDPMaster replicateUDPMaster(PyObject[] v, String[] n) {
		PyObject[] v1 = new PyObject[v.length+1];
		for(int i=0;i<v.length;i++)v1[i+1]=v[i];
		v1[0]=Py.java2py(this);
		jprime.UDPMaster.UDPMasterReplica temp = new jprime.UDPMaster.UDPMasterReplica(v1,n);
		addChild(temp);
		return temp;
	}

	 /**
	  * Create a new child of type jprime.UDPMaster.UDPMasterReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.UDPMaster.IUDPMaster createUDPMasterReplica(String name, jprime.UDPMaster.IUDPMaster to_replicate) {
		jprime.UDPMaster.UDPMasterReplica temp = new jprime.UDPMaster.UDPMasterReplica(name, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * Create a new child of type jprime.SymbioSimAppProt.SymbioSimAppProt and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.SymbioSimAppProt.ISymbioSimAppProt createSymbioSimAppProt() {
		return createSymbioSimAppProt(null);
	}

	/**
	  * jython method to create a a new child of type jprime.SymbioSimAppProt.SymbioSimAppProt, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.SymbioSimAppProt.ISymbioSimAppProt createSymbioSimAppProt(PyObject[] v, String[] n) {
		if(jprime.ModelNode.__hasReplicate(v,n,false)) {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.SymbioSimAppProt.SymbioSimAppProtReplica temp = new jprime.SymbioSimAppProt.SymbioSimAppProtReplica(v1,n);
			addChild(temp);
			return temp;
		} else {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.SymbioSimAppProt.SymbioSimAppProt temp = new jprime.SymbioSimAppProt.SymbioSimAppProt(v1,n);
			addChild(temp);
			return temp;
		}
	}

	 /**
	  * Create a new child of type jprime.SymbioSimAppProt.SymbioSimAppProt and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.SymbioSimAppProt.ISymbioSimAppProt createSymbioSimAppProt(String name) {
		jprime.SymbioSimAppProt.SymbioSimAppProt temp = new jprime.SymbioSimAppProt.SymbioSimAppProt(this);
		temp.setName(name);
		addChild(temp);
		return temp;
	}

	 /**
	  * Add a new child of type jprime.SymbioSimAppProt.SymbioSimAppProt.
	  */
	public void addSymbioSimAppProt(jprime.SymbioSimAppProt.SymbioSimAppProt kid) {
		addChild(kid);
	}

	 /**
	  * Create a new child of type jprime.SymbioSimAppProt.SymbioSimAppProtReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.SymbioSimAppProt.ISymbioSimAppProt createSymbioSimAppProtReplica(jprime.SymbioSimAppProt.ISymbioSimAppProt to_replicate) {
		jprime.SymbioSimAppProt.SymbioSimAppProtReplica temp = new jprime.SymbioSimAppProt.SymbioSimAppProtReplica(null, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * jython method to create replica new child of type jprime.SymbioSimAppProt.SymbioSimAppProtReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.SymbioSimAppProt.ISymbioSimAppProt replicateSymbioSimAppProt(PyObject[] v, String[] n) {
		PyObject[] v1 = new PyObject[v.length+1];
		for(int i=0;i<v.length;i++)v1[i+1]=v[i];
		v1[0]=Py.java2py(this);
		jprime.SymbioSimAppProt.SymbioSimAppProtReplica temp = new jprime.SymbioSimAppProt.SymbioSimAppProtReplica(v1,n);
		addChild(temp);
		return temp;
	}

	 /**
	  * Create a new child of type jprime.SymbioSimAppProt.SymbioSimAppProtReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.SymbioSimAppProt.ISymbioSimAppProt createSymbioSimAppProtReplica(String name, jprime.SymbioSimAppProt.ISymbioSimAppProt to_replicate) {
		jprime.SymbioSimAppProt.SymbioSimAppProtReplica temp = new jprime.SymbioSimAppProt.SymbioSimAppProtReplica(name, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * Create a new child of type jprime.TCPMaster.TCPMaster and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.TCPMaster.ITCPMaster createTCPMaster() {
		return createTCPMaster(null);
	}

	/**
	  * jython method to create a a new child of type jprime.TCPMaster.TCPMaster, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.TCPMaster.ITCPMaster createTCPMaster(PyObject[] v, String[] n) {
		if(jprime.ModelNode.__hasReplicate(v,n,false)) {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.TCPMaster.TCPMasterReplica temp = new jprime.TCPMaster.TCPMasterReplica(v1,n);
			addChild(temp);
			return temp;
		} else {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.TCPMaster.TCPMaster temp = new jprime.TCPMaster.TCPMaster(v1,n);
			addChild(temp);
			return temp;
		}
	}

	 /**
	  * Create a new child of type jprime.TCPMaster.TCPMaster and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.TCPMaster.ITCPMaster createTCPMaster(String name) {
		jprime.TCPMaster.TCPMaster temp = new jprime.TCPMaster.TCPMaster(this);
		temp.setName(name);
		addChild(temp);
		return temp;
	}

	 /**
	  * Add a new child of type jprime.TCPMaster.TCPMaster.
	  */
	public void addTCPMaster(jprime.TCPMaster.TCPMaster kid) {
		addChild(kid);
	}

	 /**
	  * Create a new child of type jprime.TCPMaster.TCPMasterReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.TCPMaster.ITCPMaster createTCPMasterReplica(jprime.TCPMaster.ITCPMaster to_replicate) {
		jprime.TCPMaster.TCPMasterReplica temp = new jprime.TCPMaster.TCPMasterReplica(null, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * jython method to create replica new child of type jprime.TCPMaster.TCPMasterReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.TCPMaster.ITCPMaster replicateTCPMaster(PyObject[] v, String[] n) {
		PyObject[] v1 = new PyObject[v.length+1];
		for(int i=0;i<v.length;i++)v1[i+1]=v[i];
		v1[0]=Py.java2py(this);
		jprime.TCPMaster.TCPMasterReplica temp = new jprime.TCPMaster.TCPMasterReplica(v1,n);
		addChild(temp);
		return temp;
	}

	 /**
	  * Create a new child of type jprime.TCPMaster.TCPMasterReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.TCPMaster.ITCPMaster createTCPMasterReplica(String name, jprime.TCPMaster.ITCPMaster to_replicate) {
		jprime.TCPMaster.TCPMasterReplica temp = new jprime.TCPMaster.TCPMasterReplica(name, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * Create a new child of type jprime.ProbeSession.ProbeSession and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.ProbeSession.IProbeSession createProbeSession() {
		return createProbeSession(null);
	}

	/**
	  * jython method to create a a new child of type jprime.ProbeSession.ProbeSession, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.ProbeSession.IProbeSession createProbeSession(PyObject[] v, String[] n) {
		if(jprime.ModelNode.__hasReplicate(v,n,false)) {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.ProbeSession.ProbeSessionReplica temp = new jprime.ProbeSession.ProbeSessionReplica(v1,n);
			addChild(temp);
			return temp;
		} else {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.ProbeSession.ProbeSession temp = new jprime.ProbeSession.ProbeSession(v1,n);
			addChild(temp);
			return temp;
		}
	}

	 /**
	  * Create a new child of type jprime.ProbeSession.ProbeSession and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.ProbeSession.IProbeSession createProbeSession(String name) {
		jprime.ProbeSession.ProbeSession temp = new jprime.ProbeSession.ProbeSession(this);
		temp.setName(name);
		addChild(temp);
		return temp;
	}

	 /**
	  * Add a new child of type jprime.ProbeSession.ProbeSession.
	  */
	public void addProbeSession(jprime.ProbeSession.ProbeSession kid) {
		addChild(kid);
	}

	 /**
	  * Create a new child of type jprime.ProbeSession.ProbeSessionReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.ProbeSession.IProbeSession createProbeSessionReplica(jprime.ProbeSession.IProbeSession to_replicate) {
		jprime.ProbeSession.ProbeSessionReplica temp = new jprime.ProbeSession.ProbeSessionReplica(null, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * jython method to create replica new child of type jprime.ProbeSession.ProbeSessionReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.ProbeSession.IProbeSession replicateProbeSession(PyObject[] v, String[] n) {
		PyObject[] v1 = new PyObject[v.length+1];
		for(int i=0;i<v.length;i++)v1[i+1]=v[i];
		v1[0]=Py.java2py(this);
		jprime.ProbeSession.ProbeSessionReplica temp = new jprime.ProbeSession.ProbeSessionReplica(v1,n);
		addChild(temp);
		return temp;
	}

	 /**
	  * Create a new child of type jprime.ProbeSession.ProbeSessionReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.ProbeSession.IProbeSession createProbeSessionReplica(String name, jprime.ProbeSession.IProbeSession to_replicate) {
		jprime.ProbeSession.ProbeSessionReplica temp = new jprime.ProbeSession.ProbeSessionReplica(name, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * Create a new child of type jprime.IPv4Session.IPv4Session and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.IPv4Session.IIPv4Session createIPv4Session() {
		return createIPv4Session(null);
	}

	/**
	  * jython method to create a a new child of type jprime.IPv4Session.IPv4Session, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.IPv4Session.IIPv4Session createIPv4Session(PyObject[] v, String[] n) {
		if(jprime.ModelNode.__hasReplicate(v,n,false)) {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.IPv4Session.IPv4SessionReplica temp = new jprime.IPv4Session.IPv4SessionReplica(v1,n);
			addChild(temp);
			return temp;
		} else {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.IPv4Session.IPv4Session temp = new jprime.IPv4Session.IPv4Session(v1,n);
			addChild(temp);
			return temp;
		}
	}

	 /**
	  * Create a new child of type jprime.IPv4Session.IPv4Session and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.IPv4Session.IIPv4Session createIPv4Session(String name) {
		jprime.IPv4Session.IPv4Session temp = new jprime.IPv4Session.IPv4Session(this);
		temp.setName(name);
		addChild(temp);
		return temp;
	}

	 /**
	  * Add a new child of type jprime.IPv4Session.IPv4Session.
	  */
	public void addIPv4Session(jprime.IPv4Session.IPv4Session kid) {
		addChild(kid);
	}

	 /**
	  * Create a new child of type jprime.IPv4Session.IPv4SessionReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.IPv4Session.IIPv4Session createIPv4SessionReplica(jprime.IPv4Session.IIPv4Session to_replicate) {
		jprime.IPv4Session.IPv4SessionReplica temp = new jprime.IPv4Session.IPv4SessionReplica(null, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * jython method to create replica new child of type jprime.IPv4Session.IPv4SessionReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.IPv4Session.IIPv4Session replicateIPv4Session(PyObject[] v, String[] n) {
		PyObject[] v1 = new PyObject[v.length+1];
		for(int i=0;i<v.length;i++)v1[i+1]=v[i];
		v1[0]=Py.java2py(this);
		jprime.IPv4Session.IPv4SessionReplica temp = new jprime.IPv4Session.IPv4SessionReplica(v1,n);
		addChild(temp);
		return temp;
	}

	 /**
	  * Create a new child of type jprime.IPv4Session.IPv4SessionReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.IPv4Session.IIPv4Session createIPv4SessionReplica(String name, jprime.IPv4Session.IIPv4Session to_replicate) {
		jprime.IPv4Session.IPv4SessionReplica temp = new jprime.IPv4Session.IPv4SessionReplica(name, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * Create a new child of type jprime.ApplicationSession.ApplicationSession and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.ApplicationSession.IApplicationSession createApplicationSession() {
		return createApplicationSession(null);
	}

	/**
	  * jython method to create a a new child of type jprime.ApplicationSession.ApplicationSession, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.ApplicationSession.IApplicationSession createApplicationSession(PyObject[] v, String[] n) {
		if(jprime.ModelNode.__hasReplicate(v,n,false)) {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.ApplicationSession.ApplicationSessionReplica temp = new jprime.ApplicationSession.ApplicationSessionReplica(v1,n);
			addChild(temp);
			return temp;
		} else {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.ApplicationSession.ApplicationSession temp = new jprime.ApplicationSession.ApplicationSession(v1,n);
			addChild(temp);
			return temp;
		}
	}

	 /**
	  * Create a new child of type jprime.ApplicationSession.ApplicationSession and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.ApplicationSession.IApplicationSession createApplicationSession(String name) {
		jprime.ApplicationSession.ApplicationSession temp = new jprime.ApplicationSession.ApplicationSession(this);
		temp.setName(name);
		addChild(temp);
		return temp;
	}

	 /**
	  * Add a new child of type jprime.ApplicationSession.ApplicationSession.
	  */
	public void addApplicationSession(jprime.ApplicationSession.ApplicationSession kid) {
		addChild(kid);
	}

	 /**
	  * Create a new child of type jprime.ApplicationSession.ApplicationSessionReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.ApplicationSession.IApplicationSession createApplicationSessionReplica(jprime.ApplicationSession.IApplicationSession to_replicate) {
		jprime.ApplicationSession.ApplicationSessionReplica temp = new jprime.ApplicationSession.ApplicationSessionReplica(null, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * jython method to create replica new child of type jprime.ApplicationSession.ApplicationSessionReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.ApplicationSession.IApplicationSession replicateApplicationSession(PyObject[] v, String[] n) {
		PyObject[] v1 = new PyObject[v.length+1];
		for(int i=0;i<v.length;i++)v1[i+1]=v[i];
		v1[0]=Py.java2py(this);
		jprime.ApplicationSession.ApplicationSessionReplica temp = new jprime.ApplicationSession.ApplicationSessionReplica(v1,n);
		addChild(temp);
		return temp;
	}

	 /**
	  * Create a new child of type jprime.ApplicationSession.ApplicationSessionReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.ApplicationSession.IApplicationSession createApplicationSessionReplica(String name, jprime.ApplicationSession.IApplicationSession to_replicate) {
		jprime.ApplicationSession.ApplicationSessionReplica temp = new jprime.ApplicationSession.ApplicationSessionReplica(name, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * Create a new child of type jprime.RoutingProtocol.RoutingProtocol and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.RoutingProtocol.IRoutingProtocol createRoutingProtocol() {
		return createRoutingProtocol(null);
	}

	/**
	  * jython method to create a a new child of type jprime.RoutingProtocol.RoutingProtocol, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.RoutingProtocol.IRoutingProtocol createRoutingProtocol(PyObject[] v, String[] n) {
		if(jprime.ModelNode.__hasReplicate(v,n,false)) {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.RoutingProtocol.RoutingProtocolReplica temp = new jprime.RoutingProtocol.RoutingProtocolReplica(v1,n);
			addChild(temp);
			return temp;
		} else {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.RoutingProtocol.RoutingProtocol temp = new jprime.RoutingProtocol.RoutingProtocol(v1,n);
			addChild(temp);
			return temp;
		}
	}

	 /**
	  * Create a new child of type jprime.RoutingProtocol.RoutingProtocol and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.RoutingProtocol.IRoutingProtocol createRoutingProtocol(String name) {
		jprime.RoutingProtocol.RoutingProtocol temp = new jprime.RoutingProtocol.RoutingProtocol(this);
		temp.setName(name);
		addChild(temp);
		return temp;
	}

	 /**
	  * Add a new child of type jprime.RoutingProtocol.RoutingProtocol.
	  */
	public void addRoutingProtocol(jprime.RoutingProtocol.RoutingProtocol kid) {
		addChild(kid);
	}

	 /**
	  * Create a new child of type jprime.RoutingProtocol.RoutingProtocolReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.RoutingProtocol.IRoutingProtocol createRoutingProtocolReplica(jprime.RoutingProtocol.IRoutingProtocol to_replicate) {
		jprime.RoutingProtocol.RoutingProtocolReplica temp = new jprime.RoutingProtocol.RoutingProtocolReplica(null, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * jython method to create replica new child of type jprime.RoutingProtocol.RoutingProtocolReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.RoutingProtocol.IRoutingProtocol replicateRoutingProtocol(PyObject[] v, String[] n) {
		PyObject[] v1 = new PyObject[v.length+1];
		for(int i=0;i<v.length;i++)v1[i+1]=v[i];
		v1[0]=Py.java2py(this);
		jprime.RoutingProtocol.RoutingProtocolReplica temp = new jprime.RoutingProtocol.RoutingProtocolReplica(v1,n);
		addChild(temp);
		return temp;
	}

	 /**
	  * Create a new child of type jprime.RoutingProtocol.RoutingProtocolReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.RoutingProtocol.IRoutingProtocol createRoutingProtocolReplica(String name, jprime.RoutingProtocol.IRoutingProtocol to_replicate) {
		jprime.RoutingProtocol.RoutingProtocolReplica temp = new jprime.RoutingProtocol.RoutingProtocolReplica(name, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * Create a new child of type jprime.CNFApplication.CNFApplication and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.CNFApplication.ICNFApplication createCNFApplication() {
		return createCNFApplication(null);
	}

	/**
	  * jython method to create a a new child of type jprime.CNFApplication.CNFApplication, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.CNFApplication.ICNFApplication createCNFApplication(PyObject[] v, String[] n) {
		if(jprime.ModelNode.__hasReplicate(v,n,false)) {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.CNFApplication.CNFApplicationReplica temp = new jprime.CNFApplication.CNFApplicationReplica(v1,n);
			addChild(temp);
			return temp;
		} else {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.CNFApplication.CNFApplication temp = new jprime.CNFApplication.CNFApplication(v1,n);
			addChild(temp);
			return temp;
		}
	}

	 /**
	  * Create a new child of type jprime.CNFApplication.CNFApplication and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.CNFApplication.ICNFApplication createCNFApplication(String name) {
		jprime.CNFApplication.CNFApplication temp = new jprime.CNFApplication.CNFApplication(this);
		temp.setName(name);
		addChild(temp);
		return temp;
	}

	 /**
	  * Add a new child of type jprime.CNFApplication.CNFApplication.
	  */
	public void addCNFApplication(jprime.CNFApplication.CNFApplication kid) {
		addChild(kid);
	}

	 /**
	  * Create a new child of type jprime.CNFApplication.CNFApplicationReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.CNFApplication.ICNFApplication createCNFApplicationReplica(jprime.CNFApplication.ICNFApplication to_replicate) {
		jprime.CNFApplication.CNFApplicationReplica temp = new jprime.CNFApplication.CNFApplicationReplica(null, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * jython method to create replica new child of type jprime.CNFApplication.CNFApplicationReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.CNFApplication.ICNFApplication replicateCNFApplication(PyObject[] v, String[] n) {
		PyObject[] v1 = new PyObject[v.length+1];
		for(int i=0;i<v.length;i++)v1[i+1]=v[i];
		v1[0]=Py.java2py(this);
		jprime.CNFApplication.CNFApplicationReplica temp = new jprime.CNFApplication.CNFApplicationReplica(v1,n);
		addChild(temp);
		return temp;
	}

	 /**
	  * Create a new child of type jprime.CNFApplication.CNFApplicationReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.CNFApplication.ICNFApplication createCNFApplicationReplica(String name, jprime.CNFApplication.ICNFApplication to_replicate) {
		jprime.CNFApplication.CNFApplicationReplica temp = new jprime.CNFApplication.CNFApplicationReplica(name, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * Create a new child of type jprime.CBR.CBR and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.CBR.ICBR createCBR() {
		return createCBR(null);
	}

	/**
	  * jython method to create a a new child of type jprime.CBR.CBR, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.CBR.ICBR createCBR(PyObject[] v, String[] n) {
		if(jprime.ModelNode.__hasReplicate(v,n,false)) {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.CBR.CBRReplica temp = new jprime.CBR.CBRReplica(v1,n);
			addChild(temp);
			return temp;
		} else {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.CBR.CBR temp = new jprime.CBR.CBR(v1,n);
			addChild(temp);
			return temp;
		}
	}

	 /**
	  * Create a new child of type jprime.CBR.CBR and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.CBR.ICBR createCBR(String name) {
		jprime.CBR.CBR temp = new jprime.CBR.CBR(this);
		temp.setName(name);
		addChild(temp);
		return temp;
	}

	 /**
	  * Add a new child of type jprime.CBR.CBR.
	  */
	public void addCBR(jprime.CBR.CBR kid) {
		addChild(kid);
	}

	 /**
	  * Create a new child of type jprime.CBR.CBRReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.CBR.ICBR createCBRReplica(jprime.CBR.ICBR to_replicate) {
		jprime.CBR.CBRReplica temp = new jprime.CBR.CBRReplica(null, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * jython method to create replica new child of type jprime.CBR.CBRReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.CBR.ICBR replicateCBR(PyObject[] v, String[] n) {
		PyObject[] v1 = new PyObject[v.length+1];
		for(int i=0;i<v.length;i++)v1[i+1]=v[i];
		v1[0]=Py.java2py(this);
		jprime.CBR.CBRReplica temp = new jprime.CBR.CBRReplica(v1,n);
		addChild(temp);
		return temp;
	}

	 /**
	  * Create a new child of type jprime.CBR.CBRReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.CBR.ICBR createCBRReplica(String name, jprime.CBR.ICBR to_replicate) {
		jprime.CBR.CBRReplica temp = new jprime.CBR.CBRReplica(name, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * Create a new child of type jprime.SwingServer.SwingServer and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.SwingServer.ISwingServer createSwingServer() {
		return createSwingServer(null);
	}

	/**
	  * jython method to create a a new child of type jprime.SwingServer.SwingServer, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.SwingServer.ISwingServer createSwingServer(PyObject[] v, String[] n) {
		if(jprime.ModelNode.__hasReplicate(v,n,false)) {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.SwingServer.SwingServerReplica temp = new jprime.SwingServer.SwingServerReplica(v1,n);
			addChild(temp);
			return temp;
		} else {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.SwingServer.SwingServer temp = new jprime.SwingServer.SwingServer(v1,n);
			addChild(temp);
			return temp;
		}
	}

	 /**
	  * Create a new child of type jprime.SwingServer.SwingServer and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.SwingServer.ISwingServer createSwingServer(String name) {
		jprime.SwingServer.SwingServer temp = new jprime.SwingServer.SwingServer(this);
		temp.setName(name);
		addChild(temp);
		return temp;
	}

	 /**
	  * Add a new child of type jprime.SwingServer.SwingServer.
	  */
	public void addSwingServer(jprime.SwingServer.SwingServer kid) {
		addChild(kid);
	}

	 /**
	  * Create a new child of type jprime.SwingServer.SwingServerReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.SwingServer.ISwingServer createSwingServerReplica(jprime.SwingServer.ISwingServer to_replicate) {
		jprime.SwingServer.SwingServerReplica temp = new jprime.SwingServer.SwingServerReplica(null, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * jython method to create replica new child of type jprime.SwingServer.SwingServerReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.SwingServer.ISwingServer replicateSwingServer(PyObject[] v, String[] n) {
		PyObject[] v1 = new PyObject[v.length+1];
		for(int i=0;i<v.length;i++)v1[i+1]=v[i];
		v1[0]=Py.java2py(this);
		jprime.SwingServer.SwingServerReplica temp = new jprime.SwingServer.SwingServerReplica(v1,n);
		addChild(temp);
		return temp;
	}

	 /**
	  * Create a new child of type jprime.SwingServer.SwingServerReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.SwingServer.ISwingServer createSwingServerReplica(String name, jprime.SwingServer.ISwingServer to_replicate) {
		jprime.SwingServer.SwingServerReplica temp = new jprime.SwingServer.SwingServerReplica(name, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * Create a new child of type jprime.SwingClient.SwingClient and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.SwingClient.ISwingClient createSwingClient() {
		return createSwingClient(null);
	}

	/**
	  * jython method to create a a new child of type jprime.SwingClient.SwingClient, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.SwingClient.ISwingClient createSwingClient(PyObject[] v, String[] n) {
		if(jprime.ModelNode.__hasReplicate(v,n,false)) {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.SwingClient.SwingClientReplica temp = new jprime.SwingClient.SwingClientReplica(v1,n);
			addChild(temp);
			return temp;
		} else {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.SwingClient.SwingClient temp = new jprime.SwingClient.SwingClient(v1,n);
			addChild(temp);
			return temp;
		}
	}

	 /**
	  * Create a new child of type jprime.SwingClient.SwingClient and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.SwingClient.ISwingClient createSwingClient(String name) {
		jprime.SwingClient.SwingClient temp = new jprime.SwingClient.SwingClient(this);
		temp.setName(name);
		addChild(temp);
		return temp;
	}

	 /**
	  * Add a new child of type jprime.SwingClient.SwingClient.
	  */
	public void addSwingClient(jprime.SwingClient.SwingClient kid) {
		addChild(kid);
	}

	 /**
	  * Create a new child of type jprime.SwingClient.SwingClientReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.SwingClient.ISwingClient createSwingClientReplica(jprime.SwingClient.ISwingClient to_replicate) {
		jprime.SwingClient.SwingClientReplica temp = new jprime.SwingClient.SwingClientReplica(null, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * jython method to create replica new child of type jprime.SwingClient.SwingClientReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.SwingClient.ISwingClient replicateSwingClient(PyObject[] v, String[] n) {
		PyObject[] v1 = new PyObject[v.length+1];
		for(int i=0;i<v.length;i++)v1[i+1]=v[i];
		v1[0]=Py.java2py(this);
		jprime.SwingClient.SwingClientReplica temp = new jprime.SwingClient.SwingClientReplica(v1,n);
		addChild(temp);
		return temp;
	}

	 /**
	  * Create a new child of type jprime.SwingClient.SwingClientReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.SwingClient.ISwingClient createSwingClientReplica(String name, jprime.SwingClient.ISwingClient to_replicate) {
		jprime.SwingClient.SwingClientReplica temp = new jprime.SwingClient.SwingClientReplica(name, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * Create a new child of type jprime.HTTPClient.HTTPClient and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.HTTPClient.IHTTPClient createHTTPClient() {
		return createHTTPClient(null);
	}

	/**
	  * jython method to create a a new child of type jprime.HTTPClient.HTTPClient, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.HTTPClient.IHTTPClient createHTTPClient(PyObject[] v, String[] n) {
		if(jprime.ModelNode.__hasReplicate(v,n,false)) {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.HTTPClient.HTTPClientReplica temp = new jprime.HTTPClient.HTTPClientReplica(v1,n);
			addChild(temp);
			return temp;
		} else {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.HTTPClient.HTTPClient temp = new jprime.HTTPClient.HTTPClient(v1,n);
			addChild(temp);
			return temp;
		}
	}

	 /**
	  * Create a new child of type jprime.HTTPClient.HTTPClient and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.HTTPClient.IHTTPClient createHTTPClient(String name) {
		jprime.HTTPClient.HTTPClient temp = new jprime.HTTPClient.HTTPClient(this);
		temp.setName(name);
		addChild(temp);
		return temp;
	}

	 /**
	  * Add a new child of type jprime.HTTPClient.HTTPClient.
	  */
	public void addHTTPClient(jprime.HTTPClient.HTTPClient kid) {
		addChild(kid);
	}

	 /**
	  * Create a new child of type jprime.HTTPClient.HTTPClientReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.HTTPClient.IHTTPClient createHTTPClientReplica(jprime.HTTPClient.IHTTPClient to_replicate) {
		jprime.HTTPClient.HTTPClientReplica temp = new jprime.HTTPClient.HTTPClientReplica(null, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * jython method to create replica new child of type jprime.HTTPClient.HTTPClientReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.HTTPClient.IHTTPClient replicateHTTPClient(PyObject[] v, String[] n) {
		PyObject[] v1 = new PyObject[v.length+1];
		for(int i=0;i<v.length;i++)v1[i+1]=v[i];
		v1[0]=Py.java2py(this);
		jprime.HTTPClient.HTTPClientReplica temp = new jprime.HTTPClient.HTTPClientReplica(v1,n);
		addChild(temp);
		return temp;
	}

	 /**
	  * Create a new child of type jprime.HTTPClient.HTTPClientReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.HTTPClient.IHTTPClient createHTTPClientReplica(String name, jprime.HTTPClient.IHTTPClient to_replicate) {
		jprime.HTTPClient.HTTPClientReplica temp = new jprime.HTTPClient.HTTPClientReplica(name, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * Create a new child of type jprime.HTTPServer.HTTPServer and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.HTTPServer.IHTTPServer createHTTPServer() {
		return createHTTPServer(null);
	}

	/**
	  * jython method to create a a new child of type jprime.HTTPServer.HTTPServer, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.HTTPServer.IHTTPServer createHTTPServer(PyObject[] v, String[] n) {
		if(jprime.ModelNode.__hasReplicate(v,n,false)) {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.HTTPServer.HTTPServerReplica temp = new jprime.HTTPServer.HTTPServerReplica(v1,n);
			addChild(temp);
			return temp;
		} else {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.HTTPServer.HTTPServer temp = new jprime.HTTPServer.HTTPServer(v1,n);
			addChild(temp);
			return temp;
		}
	}

	 /**
	  * Create a new child of type jprime.HTTPServer.HTTPServer and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.HTTPServer.IHTTPServer createHTTPServer(String name) {
		jprime.HTTPServer.HTTPServer temp = new jprime.HTTPServer.HTTPServer(this);
		temp.setName(name);
		addChild(temp);
		return temp;
	}

	 /**
	  * Add a new child of type jprime.HTTPServer.HTTPServer.
	  */
	public void addHTTPServer(jprime.HTTPServer.HTTPServer kid) {
		addChild(kid);
	}

	 /**
	  * Create a new child of type jprime.HTTPServer.HTTPServerReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.HTTPServer.IHTTPServer createHTTPServerReplica(jprime.HTTPServer.IHTTPServer to_replicate) {
		jprime.HTTPServer.HTTPServerReplica temp = new jprime.HTTPServer.HTTPServerReplica(null, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * jython method to create replica new child of type jprime.HTTPServer.HTTPServerReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.HTTPServer.IHTTPServer replicateHTTPServer(PyObject[] v, String[] n) {
		PyObject[] v1 = new PyObject[v.length+1];
		for(int i=0;i<v.length;i++)v1[i+1]=v[i];
		v1[0]=Py.java2py(this);
		jprime.HTTPServer.HTTPServerReplica temp = new jprime.HTTPServer.HTTPServerReplica(v1,n);
		addChild(temp);
		return temp;
	}

	 /**
	  * Create a new child of type jprime.HTTPServer.HTTPServerReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.HTTPServer.IHTTPServer createHTTPServerReplica(String name, jprime.HTTPServer.IHTTPServer to_replicate) {
		jprime.HTTPServer.HTTPServerReplica temp = new jprime.HTTPServer.HTTPServerReplica(name, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * Create a new child of type jprime.ICMPv4Session.ICMPv4Session and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.ICMPv4Session.IICMPv4Session createICMPv4Session() {
		return createICMPv4Session(null);
	}

	/**
	  * jython method to create a a new child of type jprime.ICMPv4Session.ICMPv4Session, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.ICMPv4Session.IICMPv4Session createICMPv4Session(PyObject[] v, String[] n) {
		if(jprime.ModelNode.__hasReplicate(v,n,false)) {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.ICMPv4Session.ICMPv4SessionReplica temp = new jprime.ICMPv4Session.ICMPv4SessionReplica(v1,n);
			addChild(temp);
			return temp;
		} else {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.ICMPv4Session.ICMPv4Session temp = new jprime.ICMPv4Session.ICMPv4Session(v1,n);
			addChild(temp);
			return temp;
		}
	}

	 /**
	  * Create a new child of type jprime.ICMPv4Session.ICMPv4Session and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.ICMPv4Session.IICMPv4Session createICMPv4Session(String name) {
		jprime.ICMPv4Session.ICMPv4Session temp = new jprime.ICMPv4Session.ICMPv4Session(this);
		temp.setName(name);
		addChild(temp);
		return temp;
	}

	 /**
	  * Add a new child of type jprime.ICMPv4Session.ICMPv4Session.
	  */
	public void addICMPv4Session(jprime.ICMPv4Session.ICMPv4Session kid) {
		addChild(kid);
	}

	 /**
	  * Create a new child of type jprime.ICMPv4Session.ICMPv4SessionReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.ICMPv4Session.IICMPv4Session createICMPv4SessionReplica(jprime.ICMPv4Session.IICMPv4Session to_replicate) {
		jprime.ICMPv4Session.ICMPv4SessionReplica temp = new jprime.ICMPv4Session.ICMPv4SessionReplica(null, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * jython method to create replica new child of type jprime.ICMPv4Session.ICMPv4SessionReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.ICMPv4Session.IICMPv4Session replicateICMPv4Session(PyObject[] v, String[] n) {
		PyObject[] v1 = new PyObject[v.length+1];
		for(int i=0;i<v.length;i++)v1[i+1]=v[i];
		v1[0]=Py.java2py(this);
		jprime.ICMPv4Session.ICMPv4SessionReplica temp = new jprime.ICMPv4Session.ICMPv4SessionReplica(v1,n);
		addChild(temp);
		return temp;
	}

	 /**
	  * Create a new child of type jprime.ICMPv4Session.ICMPv4SessionReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.ICMPv4Session.IICMPv4Session createICMPv4SessionReplica(String name, jprime.ICMPv4Session.IICMPv4Session to_replicate) {
		jprime.ICMPv4Session.ICMPv4SessionReplica temp = new jprime.ICMPv4Session.ICMPv4SessionReplica(name, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	 * @param visitor a generic visitor
	 */
	public abstract void accept(jprime.visitors.IGenericVisitor visitor);
}
