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
public abstract class LinkReplica extends jprime.ModelNodeReplica implements jprime.gen.ILink {

	/* used to enforce the minimum/maximum child requirement */

	public void enforceChildConstraints() {
		super.enforceChildConstraints();
	getAttachments().enforceChildConstraints();
	}
	public LinkReplica(String name, IModelNode parent, jprime.Link.ILink referencedNode) {
		super(name,parent,referencedNode);
	}
	public LinkReplica(ModelNodeRecord rec){ super(rec); }
	public LinkReplica(PyObject[] v, String[] s){super(v,s);}
	/**
	 * @return the interface which this node implements
	 */
	public Class<?> getNodeType() {
		return jprime.Link.ILink.class;
	}
	/**
	 * @param used by replicas to do a deep copy of the node.
	 */
	public jprime.ModelNode deepCopy(jprime.ModelNode parent) {
		doing_deep_copy=true;
		jprime.Link.LinkReplica c = new jprime.Link.LinkReplica(this.getName(), (IModelNode)parent,(jprime.Link.ILink)this);
		doing_deep_copy=false;
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1117: //LinkReplica
			case 1173: //LinkAliasReplica
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public abstract int getTypeId();

	/**
	 * @return link delay
	 */
	public jprime.variable.FloatingPointNumberVariable getDelay() {
		jprime.variable.FloatingPointNumberVariable temp = (jprime.variable.FloatingPointNumberVariable)getAttributeByName(ModelNodeVariable.delay());
		if(null!=temp) return temp;
		return (jprime.variable.FloatingPointNumberVariable)this.getReplicatedNode().getAttributeByName(ModelNodeVariable.delay());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setDelay(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.delay());
		if(temp==null){
			temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.delay(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.FloatingPointNumberVariable)){
				temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.delay(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.FloatingPointNumberVariable)temp).setValue(value); }
		}
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setDelay(float value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.delay());
		if(temp==null){
			temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.delay(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.FloatingPointNumberVariable)){
				temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.delay(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.FloatingPointNumberVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setDelay(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.delay());
		addAttr(value);
	}

	/**
	 * @return link bandwidth
	 */
	public jprime.variable.FloatingPointNumberVariable getBandwidth() {
		jprime.variable.FloatingPointNumberVariable temp = (jprime.variable.FloatingPointNumberVariable)getAttributeByName(ModelNodeVariable.bandwidth());
		if(null!=temp) return temp;
		return (jprime.variable.FloatingPointNumberVariable)this.getReplicatedNode().getAttributeByName(ModelNodeVariable.bandwidth());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setBandwidth(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.bandwidth());
		if(temp==null){
			temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.bandwidth(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.FloatingPointNumberVariable)){
				temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.bandwidth(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.FloatingPointNumberVariable)temp).setValue(value); }
		}
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setBandwidth(float value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.bandwidth());
		if(temp==null){
			temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.bandwidth(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.FloatingPointNumberVariable)){
				temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.bandwidth(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.FloatingPointNumberVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setBandwidth(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.bandwidth());
		addAttr(value);
	}

	/**
	 * @return The n in W.X.Y.Z/n
	 */
	public jprime.variable.IntegerVariable getIpPrefixLen() {
		jprime.variable.IntegerVariable temp = (jprime.variable.IntegerVariable)getAttributeByName(ModelNodeVariable.ip_prefix_len());
		if(null!=temp) return temp;
		return (jprime.variable.IntegerVariable)this.getReplicatedNode().getAttributeByName(ModelNodeVariable.ip_prefix_len());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setIpPrefixLen(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.ip_prefix_len());
		if(temp==null){
			temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.ip_prefix_len(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.IntegerVariable)){
				temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.ip_prefix_len(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.IntegerVariable)temp).setValue(value); }
		}
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setIpPrefixLen(long value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.ip_prefix_len());
		if(temp==null){
			temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.ip_prefix_len(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.IntegerVariable)){
				temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.ip_prefix_len(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.IntegerVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setIpPrefixLen(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.ip_prefix_len());
		addAttr(value);
	}

	/**
	 * @return ip prefix of this Link
	 */
	public jprime.variable.OpaqueVariable getIpPrefix() {
		jprime.variable.OpaqueVariable temp = (jprime.variable.OpaqueVariable)getAttributeByName(ModelNodeVariable.ip_prefix());
		if(null!=temp) return temp;
		return (jprime.variable.OpaqueVariable)this.getReplicatedNode().getAttributeByName(ModelNodeVariable.ip_prefix());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setIpPrefix(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.ip_prefix());
		if(temp==null){
			temp=new jprime.variable.OpaqueVariable(jprime.gen.ModelNodeVariable.ip_prefix(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.OpaqueVariable)){
				temp=new jprime.variable.OpaqueVariable(jprime.gen.ModelNodeVariable.ip_prefix(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.OpaqueVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setIpPrefix(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.ip_prefix());
		addAttr(value);
	}

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds() {
		return jprime.gen.Link.attrIds;
	}

	/**
	  * Create a new child of type jprime.BaseInterface.BaseInterfaceAlias, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.BaseInterface.IBaseInterface createBaseInterface(jprime.BaseInterface.IBaseInterface to_alias) {
		return createBaseInterface(null,to_alias);
	}

	/**
	  * jython method to create a a new child of type jprime.BaseInterface.BaseInterfaceAlias, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.BaseInterface.IBaseInterface createBaseInterface(PyObject[] v, String[] n) {
		if(jprime.ModelNode.__hasReplicate(v,n,true)) {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.BaseInterface.BaseInterfaceAliasReplica temp = new jprime.BaseInterface.BaseInterfaceAliasReplica(v1,n);
			addChild(temp);
			return temp;
		} else {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.BaseInterface.BaseInterfaceAlias temp = new jprime.BaseInterface.BaseInterfaceAlias(v1,n);
			addChild(temp);
			return temp;
		}
	}

	/**
	  * Create a new child of type jprime.BaseInterface.BaseInterfaceAlias, which points to to_alias, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.BaseInterface.IBaseInterface createBaseInterface(String name, jprime.BaseInterface.IBaseInterface to_alias) {
		jprime.BaseInterface.BaseInterfaceAlias temp = new jprime.BaseInterface.BaseInterfaceAlias(this,to_alias);
		temp.setName(name);
		addChild(temp);
		return temp;
	}

	/**
	  * Add a new child of type jprime.BaseInterface.BaseInterfaceAlias
	  */
	public void addBaseInterfaceAlias(jprime.BaseInterface.BaseInterfaceAlias kid) {
		addChild(kid);
	}

	/**
	  * return network interfaces attached to this link
	  */
	public jprime.util.ChildList<jprime.BaseInterface.IBaseInterfaceAlias> getAttachments() {
		return new jprime.util.ChildList<jprime.BaseInterface.IBaseInterfaceAlias>(this, 1006, 0, 0);
	}

	/**
	  * Create a new child of type jprime.Interface.InterfaceAlias, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.Interface.IInterface createInterface(jprime.Interface.IInterface to_alias) {
		return createInterface(null,to_alias);
	}

	/**
	  * jython method to create a a new child of type jprime.Interface.InterfaceAlias, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.Interface.IInterface createInterface(PyObject[] v, String[] n) {
		if(jprime.ModelNode.__hasReplicate(v,n,true)) {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.Interface.InterfaceAliasReplica temp = new jprime.Interface.InterfaceAliasReplica(v1,n);
			addChild(temp);
			return temp;
		} else {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.Interface.InterfaceAlias temp = new jprime.Interface.InterfaceAlias(v1,n);
			addChild(temp);
			return temp;
		}
	}

	/**
	  * Create a new child of type jprime.Interface.InterfaceAlias, which points to to_alias, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.Interface.IInterface createInterface(String name, jprime.Interface.IInterface to_alias) {
		jprime.Interface.InterfaceAlias temp = new jprime.Interface.InterfaceAlias(this,to_alias);
		temp.setName(name);
		addChild(temp);
		return temp;
	}

	/**
	  * Add a new child of type jprime.Interface.InterfaceAlias
	  */
	public void addInterfaceAlias(jprime.Interface.InterfaceAlias kid) {
		addChild(kid);
	}

	/**
	  * Create a new child of type jprime.GhostInterface.GhostInterfaceAlias, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.GhostInterface.IGhostInterface createGhostInterface(jprime.GhostInterface.IGhostInterface to_alias) {
		return createGhostInterface(null,to_alias);
	}

	/**
	  * jython method to create a a new child of type jprime.GhostInterface.GhostInterfaceAlias, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.GhostInterface.IGhostInterface createGhostInterface(PyObject[] v, String[] n) {
		if(jprime.ModelNode.__hasReplicate(v,n,true)) {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.GhostInterface.GhostInterfaceAliasReplica temp = new jprime.GhostInterface.GhostInterfaceAliasReplica(v1,n);
			addChild(temp);
			return temp;
		} else {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.GhostInterface.GhostInterfaceAlias temp = new jprime.GhostInterface.GhostInterfaceAlias(v1,n);
			addChild(temp);
			return temp;
		}
	}

	/**
	  * Create a new child of type jprime.GhostInterface.GhostInterfaceAlias, which points to to_alias, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.GhostInterface.IGhostInterface createGhostInterface(String name, jprime.GhostInterface.IGhostInterface to_alias) {
		jprime.GhostInterface.GhostInterfaceAlias temp = new jprime.GhostInterface.GhostInterfaceAlias(this,to_alias);
		temp.setName(name);
		addChild(temp);
		return temp;
	}

	/**
	  * Add a new child of type jprime.GhostInterface.GhostInterfaceAlias
	  */
	public void addGhostInterfaceAlias(jprime.GhostInterface.GhostInterfaceAlias kid) {
		addChild(kid);
	}

	/**
	 * @param visitor a generic visitor
	 */
	public abstract void accept(jprime.visitors.IGenericVisitor visitor);
}
