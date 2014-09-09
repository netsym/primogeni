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
public abstract class HostReplica extends jprime.ProtocolGraph.ProtocolGraphReplica implements jprime.gen.IHost {

	/* used to enforce the minimum/maximum child requirement */

	public void enforceChildConstraints() {
		super.enforceChildConstraints();
	getNics().enforceChildConstraints();
	}
	public HostReplica(String name, IModelNode parent, jprime.Host.IHost referencedNode) {
		super(name,parent,referencedNode);
	}
	public HostReplica(ModelNodeRecord rec){ super(rec); }
	public HostReplica(PyObject[] v, String[] s){super(v,s);}
	/**
	 * @return the interface which this node implements
	 */
	public Class<?> getNodeType() {
		return jprime.Host.IHost.class;
	}
	/**
	 * @param used by replicas to do a deep copy of the node.
	 */
	public jprime.ModelNode deepCopy(jprime.ModelNode parent) {
		doing_deep_copy=true;
		jprime.Host.HostReplica c = new jprime.Host.HostReplica(this.getName(), (IModelNode)parent,(jprime.Host.IHost)this);
		doing_deep_copy=false;
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1144: //HostReplica
			case 1145: //RouterReplica
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
	 * @return host's seed
	 */
	public jprime.variable.IntegerVariable getHostSeed() {
		jprime.variable.IntegerVariable temp = (jprime.variable.IntegerVariable)getAttributeByName(ModelNodeVariable.host_seed());
		if(null!=temp) return temp;
		return (jprime.variable.IntegerVariable)this.getReplicatedNode().getAttributeByName(ModelNodeVariable.host_seed());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setHostSeed(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.host_seed());
		if(temp==null){
			temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.host_seed(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.IntegerVariable)){
				temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.host_seed(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.IntegerVariable)temp).setValue(value); }
		}
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setHostSeed(long value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.host_seed());
		if(temp==null){
			temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.host_seed(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.IntegerVariable)){
				temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.host_seed(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.IntegerVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setHostSeed(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.host_seed());
		addAttr(value);
	}

	/**
	 * @return host's time skew (ratio of host time over absolute time)
	 */
	public jprime.variable.FloatingPointNumberVariable getTimeSkew() {
		jprime.variable.FloatingPointNumberVariable temp = (jprime.variable.FloatingPointNumberVariable)getAttributeByName(ModelNodeVariable.time_skew());
		if(null!=temp) return temp;
		return (jprime.variable.FloatingPointNumberVariable)this.getReplicatedNode().getAttributeByName(ModelNodeVariable.time_skew());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setTimeSkew(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.time_skew());
		if(temp==null){
			temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.time_skew(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.FloatingPointNumberVariable)){
				temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.time_skew(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.FloatingPointNumberVariable)temp).setValue(value); }
		}
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setTimeSkew(float value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.time_skew());
		if(temp==null){
			temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.time_skew(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.FloatingPointNumberVariable)){
				temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.time_skew(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.FloatingPointNumberVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setTimeSkew(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.time_skew());
		addAttr(value);
	}

	/**
	 * @return host's time offset (relative to absolute time zero)
	 */
	public jprime.variable.OpaqueVariable getTimeOffset() {
		jprime.variable.OpaqueVariable temp = (jprime.variable.OpaqueVariable)getAttributeByName(ModelNodeVariable.time_offset());
		if(null!=temp) return temp;
		return (jprime.variable.OpaqueVariable)this.getReplicatedNode().getAttributeByName(ModelNodeVariable.time_offset());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setTimeOffset(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.time_offset());
		if(temp==null){
			temp=new jprime.variable.OpaqueVariable(jprime.gen.ModelNodeVariable.time_offset(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.OpaqueVariable)){
				temp=new jprime.variable.OpaqueVariable(jprime.gen.ModelNodeVariable.time_offset(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.OpaqueVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setTimeOffset(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.time_offset());
		addAttr(value);
	}

	/**
	 * @return max  [ for each nic: (bits_out/bit_rate) ]
	 */
	public jprime.variable.FloatingPointNumberVariable getTrafficIntensity() {
		jprime.variable.FloatingPointNumberVariable temp = (jprime.variable.FloatingPointNumberVariable)getAttributeByName(ModelNodeVariable.traffic_intensity());
		if(null!=temp) return temp;
		return (jprime.variable.FloatingPointNumberVariable)this.getReplicatedNode().getAttributeByName(ModelNodeVariable.traffic_intensity());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setTrafficIntensity(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.traffic_intensity());
		if(temp==null){
			temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.traffic_intensity(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.FloatingPointNumberVariable)){
				temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.traffic_intensity(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.FloatingPointNumberVariable)temp).setValue(value); }
		}
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setTrafficIntensity(float value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.traffic_intensity());
		if(temp==null){
			temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.traffic_intensity(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.FloatingPointNumberVariable)){
				temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.traffic_intensity(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.FloatingPointNumberVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setTrafficIntensity(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.traffic_intensity());
		addAttr(value);
	}

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds() {
		return jprime.gen.Host.attrIds;
	}

	/**
	  * Create a new child of type jprime.Interface.Interface and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.Interface.IInterface createInterface() {
		return createInterface(null);
	}

	/**
	  * jython method to create a a new child of type jprime.Interface.Interface, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.Interface.IInterface createInterface(PyObject[] v, String[] n) {
		if(jprime.ModelNode.__hasReplicate(v,n,false)) {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.Interface.InterfaceReplica temp = new jprime.Interface.InterfaceReplica(v1,n);
			addChild(temp);
			return temp;
		} else {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.Interface.Interface temp = new jprime.Interface.Interface(v1,n);
			addChild(temp);
			return temp;
		}
	}

	 /**
	  * Create a new child of type jprime.Interface.Interface and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.Interface.IInterface createInterface(String name) {
		jprime.Interface.Interface temp = new jprime.Interface.Interface(this);
		temp.setName(name);
		addChild(temp);
		return temp;
	}

	 /**
	  * Add a new child of type jprime.Interface.Interface.
	  */
	public void addInterface(jprime.Interface.Interface kid) {
		addChild(kid);
	}

	 /**
	  * Create a new child of type jprime.Interface.InterfaceReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.Interface.IInterface createInterfaceReplica(jprime.Interface.IInterface to_replicate) {
		jprime.Interface.InterfaceReplica temp = new jprime.Interface.InterfaceReplica(null, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * jython method to create replica new child of type jprime.Interface.InterfaceReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.Interface.IInterface replicateInterface(PyObject[] v, String[] n) {
		PyObject[] v1 = new PyObject[v.length+1];
		for(int i=0;i<v.length;i++)v1[i+1]=v[i];
		v1[0]=Py.java2py(this);
		jprime.Interface.InterfaceReplica temp = new jprime.Interface.InterfaceReplica(v1,n);
		addChild(temp);
		return temp;
	}

	 /**
	  * Create a new child of type jprime.Interface.InterfaceReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.Interface.IInterface createInterfaceReplica(String name, jprime.Interface.IInterface to_replicate) {
		jprime.Interface.InterfaceReplica temp = new jprime.Interface.InterfaceReplica(name, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * return network interfaces on this host
	  */
	public jprime.util.ChildList<jprime.Interface.IInterface> getNics() {
		return new jprime.util.ChildList<jprime.Interface.IInterface>(this, 1007, 0, 0);
	}

	/**
	 * @param visitor a generic visitor
	 */
	public abstract void accept(jprime.visitors.IGenericVisitor visitor);
}
