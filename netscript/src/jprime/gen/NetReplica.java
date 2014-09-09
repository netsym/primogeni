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
public abstract class NetReplica extends jprime.ModelNodeReplica implements jprime.gen.INet {

	/* used to enforce the minimum/maximum child requirement */

	public void enforceChildConstraints() {
		super.enforceChildConstraints();
	getSubnets().enforceChildConstraints();	getHosts().enforceChildConstraints();	getLinks().enforceChildConstraints();	getPlaceholders().enforceChildConstraints();	getRsphere().enforceChildConstraints();	getMonitors().enforceChildConstraints();	getAggregates().enforceChildConstraints();	getTraffics().enforceChildConstraints();
	}
	public NetReplica(String name, IModelNode parent, jprime.Net.INet referencedNode) {
		super(name,parent,referencedNode);
	}
	public NetReplica(ModelNodeRecord rec){ super(rec); }
	public NetReplica(PyObject[] v, String[] s){super(v,s);}
	/**
	 * @return the interface which this node implements
	 */
	public Class<?> getNodeType() {
		return jprime.Net.INet.class;
	}
	/**
	 * @param used by replicas to do a deep copy of the node.
	 */
	public jprime.ModelNode deepCopy(jprime.ModelNode parent) {
		doing_deep_copy=true;
		jprime.Net.NetReplica c = new jprime.Net.NetReplica(this.getName(), (IModelNode)parent,(jprime.Net.INet)this);
		doing_deep_copy=false;
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1121: //NetReplica
			case 1177: //NetAliasReplica
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public abstract int getTypeId();

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
	 * @return ip prefix of this network
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
	 * @return a list of content id and uid. i.e. [1,100,2,200] --> [ cid:1, uid:100],[ cid:2,uid:200]
	 */
	public jprime.variable.OpaqueVariable getCnfContentIds() {
		jprime.variable.OpaqueVariable temp = (jprime.variable.OpaqueVariable)getAttributeByName(ModelNodeVariable.cnf_content_ids());
		if(null!=temp) return temp;
		return (jprime.variable.OpaqueVariable)this.getReplicatedNode().getAttributeByName(ModelNodeVariable.cnf_content_ids());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setCnfContentIds(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.cnf_content_ids());
		if(temp==null){
			temp=new jprime.variable.OpaqueVariable(jprime.gen.ModelNodeVariable.cnf_content_ids(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.OpaqueVariable)){
				temp=new jprime.variable.OpaqueVariable(jprime.gen.ModelNodeVariable.cnf_content_ids(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.OpaqueVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setCnfContentIds(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.cnf_content_ids());
		addAttr(value);
	}

	/**
	 * @return a list of rules in the form [ip_prefix->(router_uid,iface_uid,iface_ip)]. They will automatically be compiled into the topnet.
	 */
	public jprime.variable.OpaqueVariable getPortalRules() {
		jprime.variable.OpaqueVariable temp = (jprime.variable.OpaqueVariable)getAttributeByName(ModelNodeVariable.portal_rules());
		if(null!=temp) return temp;
		return (jprime.variable.OpaqueVariable)this.getReplicatedNode().getAttributeByName(ModelNodeVariable.portal_rules());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setPortalRules(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.portal_rules());
		if(temp==null){
			temp=new jprime.variable.OpaqueVariable(jprime.gen.ModelNodeVariable.portal_rules(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.OpaqueVariable)){
				temp=new jprime.variable.OpaqueVariable(jprime.gen.ModelNodeVariable.portal_rules(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.OpaqueVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setPortalRules(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.portal_rules());
		addAttr(value);
	}

	/**
	 * @return The RID of the cnf controller.
	 */
	public jprime.variable.IntegerVariable getControllerRid() {
		jprime.variable.IntegerVariable temp = (jprime.variable.IntegerVariable)getAttributeByName(ModelNodeVariable.controller_rid());
		if(null!=temp) return temp;
		return (jprime.variable.IntegerVariable)this.getReplicatedNode().getAttributeByName(ModelNodeVariable.controller_rid());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setControllerRid(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.controller_rid());
		if(temp==null){
			temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.controller_rid(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.IntegerVariable)){
				temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.controller_rid(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.IntegerVariable)temp).setValue(value); }
		}
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setControllerRid(long value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.controller_rid());
		if(temp==null){
			temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.controller_rid(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.IntegerVariable)){
				temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.controller_rid(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.IntegerVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setControllerRid(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.controller_rid());
		addAttr(value);
	}

	/**
	 * @return A comma separated list of its child sphere's edge iface and owning_host rank pairs
	 */
	public jprime.variable.OpaqueVariable getSubEdgeIfaces() {
		jprime.variable.OpaqueVariable temp = (jprime.variable.OpaqueVariable)getAttributeByName(ModelNodeVariable.sub_edge_ifaces());
		if(null!=temp) return temp;
		return (jprime.variable.OpaqueVariable)this.getReplicatedNode().getAttributeByName(ModelNodeVariable.sub_edge_ifaces());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setSubEdgeIfaces(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.sub_edge_ifaces());
		if(temp==null){
			temp=new jprime.variable.OpaqueVariable(jprime.gen.ModelNodeVariable.sub_edge_ifaces(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.OpaqueVariable)){
				temp=new jprime.variable.OpaqueVariable(jprime.gen.ModelNodeVariable.sub_edge_ifaces(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.OpaqueVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setSubEdgeIfaces(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.sub_edge_ifaces());
		addAttr(value);
	}

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds() {
		return jprime.gen.Net.attrIds;
	}

	/**
	  * Create a new child of type jprime.Net.Net and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.Net.INet createNet() {
		return createNet(null);
	}

	/**
	  * jython method to create a a new child of type jprime.Net.Net, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.Net.INet createNet(PyObject[] v, String[] n) {
		if(jprime.ModelNode.__hasReplicate(v,n,false)) {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.Net.NetReplica temp = new jprime.Net.NetReplica(v1,n);
			addChild(temp);
			return temp;
		} else {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.Net.Net temp = new jprime.Net.Net(v1,n);
			addChild(temp);
			return temp;
		}
	}

	 /**
	  * Create a new child of type jprime.Net.Net and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.Net.INet createNet(String name) {
		jprime.Net.Net temp = new jprime.Net.Net(this);
		temp.setName(name);
		addChild(temp);
		return temp;
	}

	 /**
	  * Add a new child of type jprime.Net.Net.
	  */
	public void addNet(jprime.Net.Net kid) {
		addChild(kid);
	}

	 /**
	  * Create a new child of type jprime.Net.NetReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.Net.INet createNetReplica(jprime.Net.INet to_replicate) {
		jprime.Net.NetReplica temp = new jprime.Net.NetReplica(null, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * jython method to create replica new child of type jprime.Net.NetReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.Net.INet replicateNet(PyObject[] v, String[] n) {
		PyObject[] v1 = new PyObject[v.length+1];
		for(int i=0;i<v.length;i++)v1[i+1]=v[i];
		v1[0]=Py.java2py(this);
		jprime.Net.NetReplica temp = new jprime.Net.NetReplica(v1,n);
		addChild(temp);
		return temp;
	}

	 /**
	  * Create a new child of type jprime.Net.NetReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.Net.INet createNetReplica(String name, jprime.Net.INet to_replicate) {
		jprime.Net.NetReplica temp = new jprime.Net.NetReplica(name, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * return sub-networks defined within this network
	  */
	public jprime.util.ChildList<jprime.Net.INet> getSubnets() {
		return new jprime.util.ChildList<jprime.Net.INet>(this, 1009, 0, 0);
	}

	/**
	  * Create a new child of type jprime.Host.Host and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.Host.IHost createHost() {
		return createHost(null);
	}

	/**
	  * jython method to create a a new child of type jprime.Host.Host, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.Host.IHost createHost(PyObject[] v, String[] n) {
		if(jprime.ModelNode.__hasReplicate(v,n,false)) {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.Host.HostReplica temp = new jprime.Host.HostReplica(v1,n);
			addChild(temp);
			return temp;
		} else {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.Host.Host temp = new jprime.Host.Host(v1,n);
			addChild(temp);
			return temp;
		}
	}

	 /**
	  * Create a new child of type jprime.Host.Host and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.Host.IHost createHost(String name) {
		jprime.Host.Host temp = new jprime.Host.Host(this);
		temp.setName(name);
		addChild(temp);
		return temp;
	}

	 /**
	  * Add a new child of type jprime.Host.Host.
	  */
	public void addHost(jprime.Host.Host kid) {
		addChild(kid);
	}

	 /**
	  * Create a new child of type jprime.Host.HostReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.Host.IHost createHostReplica(jprime.Host.IHost to_replicate) {
		jprime.Host.HostReplica temp = new jprime.Host.HostReplica(null, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * jython method to create replica new child of type jprime.Host.HostReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.Host.IHost replicateHost(PyObject[] v, String[] n) {
		PyObject[] v1 = new PyObject[v.length+1];
		for(int i=0;i<v.length;i++)v1[i+1]=v[i];
		v1[0]=Py.java2py(this);
		jprime.Host.HostReplica temp = new jprime.Host.HostReplica(v1,n);
		addChild(temp);
		return temp;
	}

	 /**
	  * Create a new child of type jprime.Host.HostReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.Host.IHost createHostReplica(String name, jprime.Host.IHost to_replicate) {
		jprime.Host.HostReplica temp = new jprime.Host.HostReplica(name, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * return hosts defined within this network
	  */
	public jprime.util.ChildList<jprime.Host.IHost> getHosts() {
		return new jprime.util.ChildList<jprime.Host.IHost>(this, 1032, 0, 0);
	}

	/**
	  * Create a new child of type jprime.Router.Router and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.Router.IRouter createRouter() {
		return createRouter(null);
	}

	/**
	  * jython method to create a a new child of type jprime.Router.Router, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.Router.IRouter createRouter(PyObject[] v, String[] n) {
		if(jprime.ModelNode.__hasReplicate(v,n,false)) {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.Router.RouterReplica temp = new jprime.Router.RouterReplica(v1,n);
			addChild(temp);
			return temp;
		} else {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.Router.Router temp = new jprime.Router.Router(v1,n);
			addChild(temp);
			return temp;
		}
	}

	 /**
	  * Create a new child of type jprime.Router.Router and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.Router.IRouter createRouter(String name) {
		jprime.Router.Router temp = new jprime.Router.Router(this);
		temp.setName(name);
		addChild(temp);
		return temp;
	}

	 /**
	  * Add a new child of type jprime.Router.Router.
	  */
	public void addRouter(jprime.Router.Router kid) {
		addChild(kid);
	}

	 /**
	  * Create a new child of type jprime.Router.RouterReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.Router.IRouter createRouterReplica(jprime.Router.IRouter to_replicate) {
		jprime.Router.RouterReplica temp = new jprime.Router.RouterReplica(null, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * jython method to create replica new child of type jprime.Router.RouterReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.Router.IRouter replicateRouter(PyObject[] v, String[] n) {
		PyObject[] v1 = new PyObject[v.length+1];
		for(int i=0;i<v.length;i++)v1[i+1]=v[i];
		v1[0]=Py.java2py(this);
		jprime.Router.RouterReplica temp = new jprime.Router.RouterReplica(v1,n);
		addChild(temp);
		return temp;
	}

	 /**
	  * Create a new child of type jprime.Router.RouterReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.Router.IRouter createRouterReplica(String name, jprime.Router.IRouter to_replicate) {
		jprime.Router.RouterReplica temp = new jprime.Router.RouterReplica(name, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * Create a new child of type jprime.Link.Link and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.Link.ILink createLink() {
		return createLink(null);
	}

	/**
	  * jython method to create a a new child of type jprime.Link.Link, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.Link.ILink createLink(PyObject[] v, String[] n) {
		if(jprime.ModelNode.__hasReplicate(v,n,false)) {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.Link.LinkReplica temp = new jprime.Link.LinkReplica(v1,n);
			addChild(temp);
			return temp;
		} else {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.Link.Link temp = new jprime.Link.Link(v1,n);
			addChild(temp);
			return temp;
		}
	}

	 /**
	  * Create a new child of type jprime.Link.Link and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.Link.ILink createLink(String name) {
		jprime.Link.Link temp = new jprime.Link.Link(this);
		temp.setName(name);
		addChild(temp);
		return temp;
	}

	 /**
	  * Add a new child of type jprime.Link.Link.
	  */
	public void addLink(jprime.Link.Link kid) {
		addChild(kid);
	}

	 /**
	  * Create a new child of type jprime.Link.LinkReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.Link.ILink createLinkReplica(jprime.Link.ILink to_replicate) {
		jprime.Link.LinkReplica temp = new jprime.Link.LinkReplica(null, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * jython method to create replica new child of type jprime.Link.LinkReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.Link.ILink replicateLink(PyObject[] v, String[] n) {
		PyObject[] v1 = new PyObject[v.length+1];
		for(int i=0;i<v.length;i++)v1[i+1]=v[i];
		v1[0]=Py.java2py(this);
		jprime.Link.LinkReplica temp = new jprime.Link.LinkReplica(v1,n);
		addChild(temp);
		return temp;
	}

	 /**
	  * Create a new child of type jprime.Link.LinkReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.Link.ILink createLinkReplica(String name, jprime.Link.ILink to_replicate) {
		jprime.Link.LinkReplica temp = new jprime.Link.LinkReplica(name, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * return links defined within this network
	  */
	public jprime.util.ChildList<jprime.Link.ILink> getLinks() {
		return new jprime.util.ChildList<jprime.Link.ILink>(this, 1005, 0, 0);
	}

	/**
	  * Create a new child of type jprime.PlaceHolder.PlaceHolder and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.PlaceHolder.IPlaceHolder createPlaceHolder() {
		return createPlaceHolder(null);
	}

	/**
	  * jython method to create a a new child of type jprime.PlaceHolder.PlaceHolder, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.PlaceHolder.IPlaceHolder createPlaceHolder(PyObject[] v, String[] n) {
		if(jprime.ModelNode.__hasReplicate(v,n,false)) {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.PlaceHolder.PlaceHolderReplica temp = new jprime.PlaceHolder.PlaceHolderReplica(v1,n);
			addChild(temp);
			return temp;
		} else {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.PlaceHolder.PlaceHolder temp = new jprime.PlaceHolder.PlaceHolder(v1,n);
			addChild(temp);
			return temp;
		}
	}

	 /**
	  * Create a new child of type jprime.PlaceHolder.PlaceHolder and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.PlaceHolder.IPlaceHolder createPlaceHolder(String name) {
		jprime.PlaceHolder.PlaceHolder temp = new jprime.PlaceHolder.PlaceHolder(this);
		temp.setName(name);
		addChild(temp);
		return temp;
	}

	 /**
	  * Add a new child of type jprime.PlaceHolder.PlaceHolder.
	  */
	public void addPlaceHolder(jprime.PlaceHolder.PlaceHolder kid) {
		addChild(kid);
	}

	 /**
	  * Create a new child of type jprime.PlaceHolder.PlaceHolderReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.PlaceHolder.IPlaceHolder createPlaceHolderReplica(jprime.PlaceHolder.IPlaceHolder to_replicate) {
		jprime.PlaceHolder.PlaceHolderReplica temp = new jprime.PlaceHolder.PlaceHolderReplica(null, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * jython method to create replica new child of type jprime.PlaceHolder.PlaceHolderReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.PlaceHolder.IPlaceHolder replicatePlaceHolder(PyObject[] v, String[] n) {
		PyObject[] v1 = new PyObject[v.length+1];
		for(int i=0;i<v.length;i++)v1[i+1]=v[i];
		v1[0]=Py.java2py(this);
		jprime.PlaceHolder.PlaceHolderReplica temp = new jprime.PlaceHolder.PlaceHolderReplica(v1,n);
		addChild(temp);
		return temp;
	}

	 /**
	  * Create a new child of type jprime.PlaceHolder.PlaceHolderReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.PlaceHolder.IPlaceHolder createPlaceHolderReplica(String name, jprime.PlaceHolder.IPlaceHolder to_replicate) {
		jprime.PlaceHolder.PlaceHolderReplica temp = new jprime.PlaceHolder.PlaceHolderReplica(name, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * return a node to hold the place of children which exist on remote partitions
	  */
	public jprime.util.ChildList<jprime.PlaceHolder.IPlaceHolder> getPlaceholders() {
		return new jprime.util.ChildList<jprime.PlaceHolder.IPlaceHolder>(this, 1037, 0, 0);
	}

	/**
	  * Create a new child of type jprime.RoutingSphere.RoutingSphere and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.RoutingSphere.IRoutingSphere createRoutingSphere() {
		return createRoutingSphere(null);
	}

	/**
	  * jython method to create a a new child of type jprime.RoutingSphere.RoutingSphere, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.RoutingSphere.IRoutingSphere createRoutingSphere(PyObject[] v, String[] n) {
		if(jprime.ModelNode.__hasReplicate(v,n,false)) {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.RoutingSphere.RoutingSphereReplica temp = new jprime.RoutingSphere.RoutingSphereReplica(v1,n);
			addChild(temp);
			return temp;
		} else {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.RoutingSphere.RoutingSphere temp = new jprime.RoutingSphere.RoutingSphere(v1,n);
			addChild(temp);
			return temp;
		}
	}

	 /**
	  * Create a new child of type jprime.RoutingSphere.RoutingSphere and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.RoutingSphere.IRoutingSphere createRoutingSphere(String name) {
		jprime.RoutingSphere.RoutingSphere temp = new jprime.RoutingSphere.RoutingSphere(this);
		temp.setName(name);
		addChild(temp);
		return temp;
	}

	 /**
	  * Add a new child of type jprime.RoutingSphere.RoutingSphere.
	  */
	public void addRoutingSphere(jprime.RoutingSphere.RoutingSphere kid) {
		addChild(kid);
	}

	 /**
	  * Create a new child of type jprime.RoutingSphere.RoutingSphereReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.RoutingSphere.IRoutingSphere createRoutingSphereReplica(jprime.RoutingSphere.IRoutingSphere to_replicate) {
		jprime.RoutingSphere.RoutingSphereReplica temp = new jprime.RoutingSphere.RoutingSphereReplica(null, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * jython method to create replica new child of type jprime.RoutingSphere.RoutingSphereReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.RoutingSphere.IRoutingSphere replicateRoutingSphere(PyObject[] v, String[] n) {
		PyObject[] v1 = new PyObject[v.length+1];
		for(int i=0;i<v.length;i++)v1[i+1]=v[i];
		v1[0]=Py.java2py(this);
		jprime.RoutingSphere.RoutingSphereReplica temp = new jprime.RoutingSphere.RoutingSphereReplica(v1,n);
		addChild(temp);
		return temp;
	}

	 /**
	  * Create a new child of type jprime.RoutingSphere.RoutingSphereReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.RoutingSphere.IRoutingSphere createRoutingSphereReplica(String name, jprime.RoutingSphere.IRoutingSphere to_replicate) {
		jprime.RoutingSphere.RoutingSphereReplica temp = new jprime.RoutingSphere.RoutingSphereReplica(name, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * return routing sphere defined within this network
	  */
	public jprime.util.ChildList<jprime.RoutingSphere.IRoutingSphere> getRsphere() {
		return new jprime.util.ChildList<jprime.RoutingSphere.IRoutingSphere>(this, 1035, 1, 1);
	}

	/**
	  * Create a new child of type jprime.GhostRoutingSphere.GhostRoutingSphere and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.GhostRoutingSphere.IGhostRoutingSphere createGhostRoutingSphere() {
		return createGhostRoutingSphere(null);
	}

	/**
	  * jython method to create a a new child of type jprime.GhostRoutingSphere.GhostRoutingSphere, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.GhostRoutingSphere.IGhostRoutingSphere createGhostRoutingSphere(PyObject[] v, String[] n) {
		if(jprime.ModelNode.__hasReplicate(v,n,false)) {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.GhostRoutingSphere.GhostRoutingSphereReplica temp = new jprime.GhostRoutingSphere.GhostRoutingSphereReplica(v1,n);
			addChild(temp);
			return temp;
		} else {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.GhostRoutingSphere.GhostRoutingSphere temp = new jprime.GhostRoutingSphere.GhostRoutingSphere(v1,n);
			addChild(temp);
			return temp;
		}
	}

	 /**
	  * Create a new child of type jprime.GhostRoutingSphere.GhostRoutingSphere and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.GhostRoutingSphere.IGhostRoutingSphere createGhostRoutingSphere(String name) {
		jprime.GhostRoutingSphere.GhostRoutingSphere temp = new jprime.GhostRoutingSphere.GhostRoutingSphere(this);
		temp.setName(name);
		addChild(temp);
		return temp;
	}

	 /**
	  * Add a new child of type jprime.GhostRoutingSphere.GhostRoutingSphere.
	  */
	public void addGhostRoutingSphere(jprime.GhostRoutingSphere.GhostRoutingSphere kid) {
		addChild(kid);
	}

	 /**
	  * Create a new child of type jprime.GhostRoutingSphere.GhostRoutingSphereReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.GhostRoutingSphere.IGhostRoutingSphere createGhostRoutingSphereReplica(jprime.GhostRoutingSphere.IGhostRoutingSphere to_replicate) {
		jprime.GhostRoutingSphere.GhostRoutingSphereReplica temp = new jprime.GhostRoutingSphere.GhostRoutingSphereReplica(null, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * jython method to create replica new child of type jprime.GhostRoutingSphere.GhostRoutingSphereReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.GhostRoutingSphere.IGhostRoutingSphere replicateGhostRoutingSphere(PyObject[] v, String[] n) {
		PyObject[] v1 = new PyObject[v.length+1];
		for(int i=0;i<v.length;i++)v1[i+1]=v[i];
		v1[0]=Py.java2py(this);
		jprime.GhostRoutingSphere.GhostRoutingSphereReplica temp = new jprime.GhostRoutingSphere.GhostRoutingSphereReplica(v1,n);
		addChild(temp);
		return temp;
	}

	 /**
	  * Create a new child of type jprime.GhostRoutingSphere.GhostRoutingSphereReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.GhostRoutingSphere.IGhostRoutingSphere createGhostRoutingSphereReplica(String name, jprime.GhostRoutingSphere.IGhostRoutingSphere to_replicate) {
		jprime.GhostRoutingSphere.GhostRoutingSphereReplica temp = new jprime.GhostRoutingSphere.GhostRoutingSphereReplica(name, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * Create a new child of type jprime.Monitor.Monitor and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.Monitor.IMonitor createMonitor() {
		return createMonitor(null);
	}

	/**
	  * jython method to create a a new child of type jprime.Monitor.Monitor, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.Monitor.IMonitor createMonitor(PyObject[] v, String[] n) {
		if(jprime.ModelNode.__hasReplicate(v,n,false)) {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.Monitor.MonitorReplica temp = new jprime.Monitor.MonitorReplica(v1,n);
			addChild(temp);
			return temp;
		} else {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.Monitor.Monitor temp = new jprime.Monitor.Monitor(v1,n);
			addChild(temp);
			return temp;
		}
	}

	 /**
	  * Create a new child of type jprime.Monitor.Monitor and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.Monitor.IMonitor createMonitor(String name) {
		jprime.Monitor.Monitor temp = new jprime.Monitor.Monitor(this);
		temp.setName(name);
		addChild(temp);
		return temp;
	}

	 /**
	  * Add a new child of type jprime.Monitor.Monitor.
	  */
	public void addMonitor(jprime.Monitor.Monitor kid) {
		addChild(kid);
	}

	 /**
	  * Create a new child of type jprime.Monitor.MonitorReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.Monitor.IMonitor createMonitorReplica(jprime.Monitor.IMonitor to_replicate) {
		jprime.Monitor.MonitorReplica temp = new jprime.Monitor.MonitorReplica(null, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * jython method to create replica new child of type jprime.Monitor.MonitorReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.Monitor.IMonitor replicateMonitor(PyObject[] v, String[] n) {
		PyObject[] v1 = new PyObject[v.length+1];
		for(int i=0;i<v.length;i++)v1[i+1]=v[i];
		v1[0]=Py.java2py(this);
		jprime.Monitor.MonitorReplica temp = new jprime.Monitor.MonitorReplica(v1,n);
		addChild(temp);
		return temp;
	}

	 /**
	  * Create a new child of type jprime.Monitor.MonitorReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.Monitor.IMonitor createMonitorReplica(String name, jprime.Monitor.IMonitor to_replicate) {
		jprime.Monitor.MonitorReplica temp = new jprime.Monitor.MonitorReplica(name, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * return data monitors
	  */
	public jprime.util.ChildList<jprime.Monitor.IMonitor> getMonitors() {
		return new jprime.util.ChildList<jprime.Monitor.IMonitor>(this, 1016, 0, 0);
	}

	/**
	  * Create a new child of type jprime.Aggregate.Aggregate and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.Aggregate.IAggregate createAggregate() {
		return createAggregate(null);
	}

	/**
	  * jython method to create a a new child of type jprime.Aggregate.Aggregate, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.Aggregate.IAggregate createAggregate(PyObject[] v, String[] n) {
		if(jprime.ModelNode.__hasReplicate(v,n,false)) {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.Aggregate.AggregateReplica temp = new jprime.Aggregate.AggregateReplica(v1,n);
			addChild(temp);
			return temp;
		} else {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.Aggregate.Aggregate temp = new jprime.Aggregate.Aggregate(v1,n);
			addChild(temp);
			return temp;
		}
	}

	 /**
	  * Create a new child of type jprime.Aggregate.Aggregate and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.Aggregate.IAggregate createAggregate(String name) {
		jprime.Aggregate.Aggregate temp = new jprime.Aggregate.Aggregate(this);
		temp.setName(name);
		addChild(temp);
		return temp;
	}

	 /**
	  * Add a new child of type jprime.Aggregate.Aggregate.
	  */
	public void addAggregate(jprime.Aggregate.Aggregate kid) {
		addChild(kid);
	}

	 /**
	  * Create a new child of type jprime.Aggregate.AggregateReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.Aggregate.IAggregate createAggregateReplica(jprime.Aggregate.IAggregate to_replicate) {
		jprime.Aggregate.AggregateReplica temp = new jprime.Aggregate.AggregateReplica(null, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * jython method to create replica new child of type jprime.Aggregate.AggregateReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.Aggregate.IAggregate replicateAggregate(PyObject[] v, String[] n) {
		PyObject[] v1 = new PyObject[v.length+1];
		for(int i=0;i<v.length;i++)v1[i+1]=v[i];
		v1[0]=Py.java2py(this);
		jprime.Aggregate.AggregateReplica temp = new jprime.Aggregate.AggregateReplica(v1,n);
		addChild(temp);
		return temp;
	}

	 /**
	  * Create a new child of type jprime.Aggregate.AggregateReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.Aggregate.IAggregate createAggregateReplica(String name, jprime.Aggregate.IAggregate to_replicate) {
		jprime.Aggregate.AggregateReplica temp = new jprime.Aggregate.AggregateReplica(name, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * return aggregate statistics
	  */
	public jprime.util.ChildList<jprime.Aggregate.IAggregate> getAggregates() {
		return new jprime.util.ChildList<jprime.Aggregate.IAggregate>(this, 1014, 0, 0);
	}

	/**
	  * Create a new child of type jprime.VizAggregate.VizAggregate and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.VizAggregate.IVizAggregate createVizAggregate() {
		return createVizAggregate(null);
	}

	/**
	  * jython method to create a a new child of type jprime.VizAggregate.VizAggregate, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.VizAggregate.IVizAggregate createVizAggregate(PyObject[] v, String[] n) {
		if(jprime.ModelNode.__hasReplicate(v,n,false)) {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.VizAggregate.VizAggregateReplica temp = new jprime.VizAggregate.VizAggregateReplica(v1,n);
			addChild(temp);
			return temp;
		} else {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.VizAggregate.VizAggregate temp = new jprime.VizAggregate.VizAggregate(v1,n);
			addChild(temp);
			return temp;
		}
	}

	 /**
	  * Create a new child of type jprime.VizAggregate.VizAggregate and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.VizAggregate.IVizAggregate createVizAggregate(String name) {
		jprime.VizAggregate.VizAggregate temp = new jprime.VizAggregate.VizAggregate(this);
		temp.setName(name);
		addChild(temp);
		return temp;
	}

	 /**
	  * Add a new child of type jprime.VizAggregate.VizAggregate.
	  */
	public void addVizAggregate(jprime.VizAggregate.VizAggregate kid) {
		addChild(kid);
	}

	 /**
	  * Create a new child of type jprime.VizAggregate.VizAggregateReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.VizAggregate.IVizAggregate createVizAggregateReplica(jprime.VizAggregate.IVizAggregate to_replicate) {
		jprime.VizAggregate.VizAggregateReplica temp = new jprime.VizAggregate.VizAggregateReplica(null, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * jython method to create replica new child of type jprime.VizAggregate.VizAggregateReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.VizAggregate.IVizAggregate replicateVizAggregate(PyObject[] v, String[] n) {
		PyObject[] v1 = new PyObject[v.length+1];
		for(int i=0;i<v.length;i++)v1[i+1]=v[i];
		v1[0]=Py.java2py(this);
		jprime.VizAggregate.VizAggregateReplica temp = new jprime.VizAggregate.VizAggregateReplica(v1,n);
		addChild(temp);
		return temp;
	}

	 /**
	  * Create a new child of type jprime.VizAggregate.VizAggregateReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.VizAggregate.IVizAggregate createVizAggregateReplica(String name, jprime.VizAggregate.IVizAggregate to_replicate) {
		jprime.VizAggregate.VizAggregateReplica temp = new jprime.VizAggregate.VizAggregateReplica(name, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * Create a new child of type jprime.Traffic.Traffic and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.Traffic.ITraffic createTraffic() {
		return createTraffic(null);
	}

	/**
	  * jython method to create a a new child of type jprime.Traffic.Traffic, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.Traffic.ITraffic createTraffic(PyObject[] v, String[] n) {
		if(jprime.ModelNode.__hasReplicate(v,n,false)) {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.Traffic.TrafficReplica temp = new jprime.Traffic.TrafficReplica(v1,n);
			addChild(temp);
			return temp;
		} else {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.Traffic.Traffic temp = new jprime.Traffic.Traffic(v1,n);
			addChild(temp);
			return temp;
		}
	}

	 /**
	  * Create a new child of type jprime.Traffic.Traffic and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.Traffic.ITraffic createTraffic(String name) {
		jprime.Traffic.Traffic temp = new jprime.Traffic.Traffic(this);
		temp.setName(name);
		addChild(temp);
		return temp;
	}

	 /**
	  * Add a new child of type jprime.Traffic.Traffic.
	  */
	public void addTraffic(jprime.Traffic.Traffic kid) {
		addChild(kid);
	}

	 /**
	  * Create a new child of type jprime.Traffic.TrafficReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.Traffic.ITraffic createTrafficReplica(jprime.Traffic.ITraffic to_replicate) {
		jprime.Traffic.TrafficReplica temp = new jprime.Traffic.TrafficReplica(null, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * jython method to create replica new child of type jprime.Traffic.TrafficReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.Traffic.ITraffic replicateTraffic(PyObject[] v, String[] n) {
		PyObject[] v1 = new PyObject[v.length+1];
		for(int i=0;i<v.length;i++)v1[i+1]=v[i];
		v1[0]=Py.java2py(this);
		jprime.Traffic.TrafficReplica temp = new jprime.Traffic.TrafficReplica(v1,n);
		addChild(temp);
		return temp;
	}

	 /**
	  * Create a new child of type jprime.Traffic.TrafficReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.Traffic.ITraffic createTrafficReplica(String name, jprime.Traffic.ITraffic to_replicate) {
		jprime.Traffic.TrafficReplica temp = new jprime.Traffic.TrafficReplica(name, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * return traffic defined within this network
	  */
	public jprime.util.ChildList<jprime.Traffic.ITraffic> getTraffics() {
		return new jprime.util.ChildList<jprime.Traffic.ITraffic>(this, 1017, 0, 1);
	}

	/**
	 * @param visitor a generic visitor
	 */
	public abstract void accept(jprime.visitors.IGenericVisitor visitor);
}
