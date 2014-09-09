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
public abstract class RoutingSphere extends jprime.ModelNode implements jprime.gen.IRoutingSphere {

	/* used to enforce the minimum/maximum child requirement */

	public void enforceChildConstraints() {
		super.enforceChildConstraints();
	getDefault_route_table().enforceChildConstraints();
	}
	public RoutingSphere(PyObject[] v, String[] s){super(v,s);}
	public RoutingSphere(ModelNodeRecord rec){ super(rec); }
	public RoutingSphere(IModelNode parent){ super(parent); }
	/**
	 * @return the interface which this node implements
	 */
	public Class<?> getNodeType() {
		return jprime.RoutingSphere.IRoutingSphere.class;
	}
	/**
	 * @param used by replicas to do a deep copy of the node.
	 */
	public jprime.ModelNode deepCopy(jprime.ModelNode parent) {
		jprime.RoutingSphere.RoutingSphereReplica c = new jprime.RoutingSphere.RoutingSphereReplica(this.getName(),(IModelNode)parent,(jprime.RoutingSphere.IRoutingSphere)this);
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1035: //RoutingSphere
			case 1036: //GhostRoutingSphere
			case 1091: //RoutingSphereAlias
			case 1092: //GhostRoutingSphereAlias
			case 1147: //RoutingSphereReplica
			case 1148: //GhostRoutingSphereReplica
			case 1203: //RoutingSphereAliasReplica
			case 1204: //GhostRoutingSphereAliasReplica
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public abstract int getTypeId();
	public final static java.util.ArrayList<Integer> attrIds=new java.util.ArrayList<Integer>();
	static {
		attrIds.add(84);
		attrIds.add(65);
	}

	/**
	 * @return The maximum number of nix vector cache entries for this sphere.
	 */
	public jprime.variable.IntegerVariable getNixVecCacheSize() {
		return (jprime.variable.IntegerVariable)getAttributeByName(ModelNodeVariable.nix_vec_cache_size());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNixVecCacheSize(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.nix_vec_cache_size());
		if(temp==null){
			temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.nix_vec_cache_size(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.IntegerVariable)){
				temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.nix_vec_cache_size(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.IntegerVariable)temp).setValue(value); }
		}
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNixVecCacheSize(long value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.nix_vec_cache_size());
		if(temp==null){
			temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.nix_vec_cache_size(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.IntegerVariable)){
				temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.nix_vec_cache_size(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.IntegerVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setNixVecCacheSize(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.nix_vec_cache_size());
		addAttr(value);
	}

	/**
	 * @return The maximum number of local dst cache entries for this sphere.
	 */
	public jprime.variable.IntegerVariable getLocalDstCacheSize() {
		return (jprime.variable.IntegerVariable)getAttributeByName(ModelNodeVariable.local_dst_cache_size());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setLocalDstCacheSize(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.local_dst_cache_size());
		if(temp==null){
			temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.local_dst_cache_size(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.IntegerVariable)){
				temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.local_dst_cache_size(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.IntegerVariable)temp).setValue(value); }
		}
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setLocalDstCacheSize(long value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.local_dst_cache_size());
		if(temp==null){
			temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.local_dst_cache_size(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.IntegerVariable)){
				temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.local_dst_cache_size(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.IntegerVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setLocalDstCacheSize(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.local_dst_cache_size());
		addAttr(value);
	}

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds() {
		return attrIds;
	}

	/**
	  * Create a new child of type jprime.RouteTable.RouteTable and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.RouteTable.IRouteTable createRouteTable() {
		return createRouteTable(null);
	}

	/**
	  * jython method to create a a new child of type jprime.RouteTable.RouteTable, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.RouteTable.IRouteTable createRouteTable(PyObject[] v, String[] n) {
		if(jprime.ModelNode.__hasReplicate(v,n,false)) {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.RouteTable.RouteTableReplica temp = new jprime.RouteTable.RouteTableReplica(v1,n);
			addChild(temp);
			return temp;
		} else {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.RouteTable.RouteTable temp = new jprime.RouteTable.RouteTable(v1,n);
			addChild(temp);
			return temp;
		}
	}

	 /**
	  * Create a new child of type jprime.RouteTable.RouteTable and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.RouteTable.IRouteTable createRouteTable(String name) {
		jprime.RouteTable.RouteTable temp = new jprime.RouteTable.RouteTable(this);
		temp.setName(name);
		addChild(temp);
		return temp;
	}

	 /**
	  * Add a new child of type jprime.RouteTable.RouteTable.
	  */
	public void addRouteTable(jprime.RouteTable.RouteTable kid) {
		addChild(kid);
	}

	 /**
	  * Create a new child of type jprime.RouteTable.RouteTableReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.RouteTable.IRouteTable createRouteTableReplica(jprime.RouteTable.IRouteTable to_replicate) {
		jprime.RouteTable.RouteTableReplica temp = new jprime.RouteTable.RouteTableReplica(null, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * jython method to create replica new child of type jprime.RouteTable.RouteTableReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.RouteTable.IRouteTable replicateRouteTable(PyObject[] v, String[] n) {
		PyObject[] v1 = new PyObject[v.length+1];
		for(int i=0;i<v.length;i++)v1[i+1]=v[i];
		v1[0]=Py.java2py(this);
		jprime.RouteTable.RouteTableReplica temp = new jprime.RouteTable.RouteTableReplica(v1,n);
		addChild(temp);
		return temp;
	}

	 /**
	  * Create a new child of type jprime.RouteTable.RouteTableReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.RouteTable.IRouteTable createRouteTableReplica(String name, jprime.RouteTable.IRouteTable to_replicate) {
		jprime.RouteTable.RouteTableReplica temp = new jprime.RouteTable.RouteTableReplica(name, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * return default routing table
	  */
	public jprime.util.ChildList<jprime.RouteTable.IRouteTable> getDefault_route_table() {
		return new jprime.util.ChildList<jprime.RouteTable.IRouteTable>(this, 1034, 0, 1);
	}

	/**
	 * @param visitor a generic visitor
	 */
	public abstract void accept(jprime.visitors.IGenericVisitor visitor);
}
