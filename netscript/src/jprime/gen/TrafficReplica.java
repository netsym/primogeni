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
public abstract class TrafficReplica extends jprime.ModelNodeReplica implements jprime.gen.ITraffic {

	/* used to enforce the minimum/maximum child requirement */

	public void enforceChildConstraints() {
		super.enforceChildConstraints();
	getTraffic_types().enforceChildConstraints();
	}
	public TrafficReplica(String name, IModelNode parent, jprime.Traffic.ITraffic referencedNode) {
		super(name,parent,referencedNode);
	}
	public TrafficReplica(ModelNodeRecord rec){ super(rec); }
	public TrafficReplica(PyObject[] v, String[] s){super(v,s);}
	/**
	 * @return the interface which this node implements
	 */
	public Class<?> getNodeType() {
		return jprime.Traffic.ITraffic.class;
	}
	/**
	 * @param used by replicas to do a deep copy of the node.
	 */
	public jprime.ModelNode deepCopy(jprime.ModelNode parent) {
		doing_deep_copy=true;
		jprime.Traffic.TrafficReplica c = new jprime.Traffic.TrafficReplica(this.getName(), (IModelNode)parent,(jprime.Traffic.ITraffic)this);
		doing_deep_copy=false;
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1129: //TrafficReplica
			case 1185: //TrafficAliasReplica
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public abstract int getTypeId();

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds() {
		return jprime.gen.Traffic.attrIds;
	}

	/**
	  * Create a new child of type jprime.TrafficType.TrafficType and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.TrafficType.ITrafficType createTrafficType() {
		return createTrafficType(null);
	}

	/**
	  * jython method to create a a new child of type jprime.TrafficType.TrafficType, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.TrafficType.ITrafficType createTrafficType(PyObject[] v, String[] n) {
		if(jprime.ModelNode.__hasReplicate(v,n,false)) {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.TrafficType.TrafficTypeReplica temp = new jprime.TrafficType.TrafficTypeReplica(v1,n);
			addChild(temp);
			return temp;
		} else {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.TrafficType.TrafficType temp = new jprime.TrafficType.TrafficType(v1,n);
			addChild(temp);
			return temp;
		}
	}

	 /**
	  * Create a new child of type jprime.TrafficType.TrafficType and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.TrafficType.ITrafficType createTrafficType(String name) {
		jprime.TrafficType.TrafficType temp = new jprime.TrafficType.TrafficType(this);
		temp.setName(name);
		addChild(temp);
		return temp;
	}

	 /**
	  * Add a new child of type jprime.TrafficType.TrafficType.
	  */
	public void addTrafficType(jprime.TrafficType.TrafficType kid) {
		addChild(kid);
	}

	 /**
	  * Create a new child of type jprime.TrafficType.TrafficTypeReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.TrafficType.ITrafficType createTrafficTypeReplica(jprime.TrafficType.ITrafficType to_replicate) {
		jprime.TrafficType.TrafficTypeReplica temp = new jprime.TrafficType.TrafficTypeReplica(null, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * jython method to create replica new child of type jprime.TrafficType.TrafficTypeReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.TrafficType.ITrafficType replicateTrafficType(PyObject[] v, String[] n) {
		PyObject[] v1 = new PyObject[v.length+1];
		for(int i=0;i<v.length;i++)v1[i+1]=v[i];
		v1[0]=Py.java2py(this);
		jprime.TrafficType.TrafficTypeReplica temp = new jprime.TrafficType.TrafficTypeReplica(v1,n);
		addChild(temp);
		return temp;
	}

	 /**
	  * Create a new child of type jprime.TrafficType.TrafficTypeReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.TrafficType.ITrafficType createTrafficTypeReplica(String name, jprime.TrafficType.ITrafficType to_replicate) {
		jprime.TrafficType.TrafficTypeReplica temp = new jprime.TrafficType.TrafficTypeReplica(name, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * return traffic types of the traffic
	  */
	public jprime.util.ChildList<jprime.TrafficType.ITrafficType> getTraffic_types() {
		return new jprime.util.ChildList<jprime.TrafficType.ITrafficType>(this, 1018, 0, 0);
	}

	/**
	  * Create a new child of type jprime.FluidTraffic.FluidTraffic and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.FluidTraffic.IFluidTraffic createFluidTraffic() {
		return createFluidTraffic(null);
	}

	/**
	  * jython method to create a a new child of type jprime.FluidTraffic.FluidTraffic, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.FluidTraffic.IFluidTraffic createFluidTraffic(PyObject[] v, String[] n) {
		if(jprime.ModelNode.__hasReplicate(v,n,false)) {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.FluidTraffic.FluidTrafficReplica temp = new jprime.FluidTraffic.FluidTrafficReplica(v1,n);
			addChild(temp);
			return temp;
		} else {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.FluidTraffic.FluidTraffic temp = new jprime.FluidTraffic.FluidTraffic(v1,n);
			addChild(temp);
			return temp;
		}
	}

	 /**
	  * Create a new child of type jprime.FluidTraffic.FluidTraffic and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.FluidTraffic.IFluidTraffic createFluidTraffic(String name) {
		jprime.FluidTraffic.FluidTraffic temp = new jprime.FluidTraffic.FluidTraffic(this);
		temp.setName(name);
		addChild(temp);
		return temp;
	}

	 /**
	  * Add a new child of type jprime.FluidTraffic.FluidTraffic.
	  */
	public void addFluidTraffic(jprime.FluidTraffic.FluidTraffic kid) {
		addChild(kid);
	}

	 /**
	  * Create a new child of type jprime.FluidTraffic.FluidTrafficReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.FluidTraffic.IFluidTraffic createFluidTrafficReplica(jprime.FluidTraffic.IFluidTraffic to_replicate) {
		jprime.FluidTraffic.FluidTrafficReplica temp = new jprime.FluidTraffic.FluidTrafficReplica(null, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * jython method to create replica new child of type jprime.FluidTraffic.FluidTrafficReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.FluidTraffic.IFluidTraffic replicateFluidTraffic(PyObject[] v, String[] n) {
		PyObject[] v1 = new PyObject[v.length+1];
		for(int i=0;i<v.length;i++)v1[i+1]=v[i];
		v1[0]=Py.java2py(this);
		jprime.FluidTraffic.FluidTrafficReplica temp = new jprime.FluidTraffic.FluidTrafficReplica(v1,n);
		addChild(temp);
		return temp;
	}

	 /**
	  * Create a new child of type jprime.FluidTraffic.FluidTrafficReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.FluidTraffic.IFluidTraffic createFluidTrafficReplica(String name, jprime.FluidTraffic.IFluidTraffic to_replicate) {
		jprime.FluidTraffic.FluidTrafficReplica temp = new jprime.FluidTraffic.FluidTrafficReplica(name, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * Create a new child of type jprime.DynamicTrafficType.DynamicTrafficType and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.DynamicTrafficType.IDynamicTrafficType createDynamicTrafficType() {
		return createDynamicTrafficType(null);
	}

	/**
	  * jython method to create a a new child of type jprime.DynamicTrafficType.DynamicTrafficType, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.DynamicTrafficType.IDynamicTrafficType createDynamicTrafficType(PyObject[] v, String[] n) {
		if(jprime.ModelNode.__hasReplicate(v,n,false)) {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.DynamicTrafficType.DynamicTrafficTypeReplica temp = new jprime.DynamicTrafficType.DynamicTrafficTypeReplica(v1,n);
			addChild(temp);
			return temp;
		} else {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.DynamicTrafficType.DynamicTrafficType temp = new jprime.DynamicTrafficType.DynamicTrafficType(v1,n);
			addChild(temp);
			return temp;
		}
	}

	 /**
	  * Create a new child of type jprime.DynamicTrafficType.DynamicTrafficType and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.DynamicTrafficType.IDynamicTrafficType createDynamicTrafficType(String name) {
		jprime.DynamicTrafficType.DynamicTrafficType temp = new jprime.DynamicTrafficType.DynamicTrafficType(this);
		temp.setName(name);
		addChild(temp);
		return temp;
	}

	 /**
	  * Add a new child of type jprime.DynamicTrafficType.DynamicTrafficType.
	  */
	public void addDynamicTrafficType(jprime.DynamicTrafficType.DynamicTrafficType kid) {
		addChild(kid);
	}

	 /**
	  * Create a new child of type jprime.DynamicTrafficType.DynamicTrafficTypeReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.DynamicTrafficType.IDynamicTrafficType createDynamicTrafficTypeReplica(jprime.DynamicTrafficType.IDynamicTrafficType to_replicate) {
		jprime.DynamicTrafficType.DynamicTrafficTypeReplica temp = new jprime.DynamicTrafficType.DynamicTrafficTypeReplica(null, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * jython method to create replica new child of type jprime.DynamicTrafficType.DynamicTrafficTypeReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.DynamicTrafficType.IDynamicTrafficType replicateDynamicTrafficType(PyObject[] v, String[] n) {
		PyObject[] v1 = new PyObject[v.length+1];
		for(int i=0;i<v.length;i++)v1[i+1]=v[i];
		v1[0]=Py.java2py(this);
		jprime.DynamicTrafficType.DynamicTrafficTypeReplica temp = new jprime.DynamicTrafficType.DynamicTrafficTypeReplica(v1,n);
		addChild(temp);
		return temp;
	}

	 /**
	  * Create a new child of type jprime.DynamicTrafficType.DynamicTrafficTypeReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.DynamicTrafficType.IDynamicTrafficType createDynamicTrafficTypeReplica(String name, jprime.DynamicTrafficType.IDynamicTrafficType to_replicate) {
		jprime.DynamicTrafficType.DynamicTrafficTypeReplica temp = new jprime.DynamicTrafficType.DynamicTrafficTypeReplica(name, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * Create a new child of type jprime.StaticTrafficType.StaticTrafficType and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.StaticTrafficType.IStaticTrafficType createStaticTrafficType() {
		return createStaticTrafficType(null);
	}

	/**
	  * jython method to create a a new child of type jprime.StaticTrafficType.StaticTrafficType, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.StaticTrafficType.IStaticTrafficType createStaticTrafficType(PyObject[] v, String[] n) {
		if(jprime.ModelNode.__hasReplicate(v,n,false)) {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.StaticTrafficType.StaticTrafficTypeReplica temp = new jprime.StaticTrafficType.StaticTrafficTypeReplica(v1,n);
			addChild(temp);
			return temp;
		} else {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.StaticTrafficType.StaticTrafficType temp = new jprime.StaticTrafficType.StaticTrafficType(v1,n);
			addChild(temp);
			return temp;
		}
	}

	 /**
	  * Create a new child of type jprime.StaticTrafficType.StaticTrafficType and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.StaticTrafficType.IStaticTrafficType createStaticTrafficType(String name) {
		jprime.StaticTrafficType.StaticTrafficType temp = new jprime.StaticTrafficType.StaticTrafficType(this);
		temp.setName(name);
		addChild(temp);
		return temp;
	}

	 /**
	  * Add a new child of type jprime.StaticTrafficType.StaticTrafficType.
	  */
	public void addStaticTrafficType(jprime.StaticTrafficType.StaticTrafficType kid) {
		addChild(kid);
	}

	 /**
	  * Create a new child of type jprime.StaticTrafficType.StaticTrafficTypeReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.StaticTrafficType.IStaticTrafficType createStaticTrafficTypeReplica(jprime.StaticTrafficType.IStaticTrafficType to_replicate) {
		jprime.StaticTrafficType.StaticTrafficTypeReplica temp = new jprime.StaticTrafficType.StaticTrafficTypeReplica(null, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * jython method to create replica new child of type jprime.StaticTrafficType.StaticTrafficTypeReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.StaticTrafficType.IStaticTrafficType replicateStaticTrafficType(PyObject[] v, String[] n) {
		PyObject[] v1 = new PyObject[v.length+1];
		for(int i=0;i<v.length;i++)v1[i+1]=v[i];
		v1[0]=Py.java2py(this);
		jprime.StaticTrafficType.StaticTrafficTypeReplica temp = new jprime.StaticTrafficType.StaticTrafficTypeReplica(v1,n);
		addChild(temp);
		return temp;
	}

	 /**
	  * Create a new child of type jprime.StaticTrafficType.StaticTrafficTypeReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.StaticTrafficType.IStaticTrafficType createStaticTrafficTypeReplica(String name, jprime.StaticTrafficType.IStaticTrafficType to_replicate) {
		jprime.StaticTrafficType.StaticTrafficTypeReplica temp = new jprime.StaticTrafficType.StaticTrafficTypeReplica(name, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * Create a new child of type jprime.DistributedTrafficType.DistributedTrafficType and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.DistributedTrafficType.IDistributedTrafficType createDistributedTrafficType() {
		return createDistributedTrafficType(null);
	}

	/**
	  * jython method to create a a new child of type jprime.DistributedTrafficType.DistributedTrafficType, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.DistributedTrafficType.IDistributedTrafficType createDistributedTrafficType(PyObject[] v, String[] n) {
		if(jprime.ModelNode.__hasReplicate(v,n,false)) {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.DistributedTrafficType.DistributedTrafficTypeReplica temp = new jprime.DistributedTrafficType.DistributedTrafficTypeReplica(v1,n);
			addChild(temp);
			return temp;
		} else {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.DistributedTrafficType.DistributedTrafficType temp = new jprime.DistributedTrafficType.DistributedTrafficType(v1,n);
			addChild(temp);
			return temp;
		}
	}

	 /**
	  * Create a new child of type jprime.DistributedTrafficType.DistributedTrafficType and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.DistributedTrafficType.IDistributedTrafficType createDistributedTrafficType(String name) {
		jprime.DistributedTrafficType.DistributedTrafficType temp = new jprime.DistributedTrafficType.DistributedTrafficType(this);
		temp.setName(name);
		addChild(temp);
		return temp;
	}

	 /**
	  * Add a new child of type jprime.DistributedTrafficType.DistributedTrafficType.
	  */
	public void addDistributedTrafficType(jprime.DistributedTrafficType.DistributedTrafficType kid) {
		addChild(kid);
	}

	 /**
	  * Create a new child of type jprime.DistributedTrafficType.DistributedTrafficTypeReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.DistributedTrafficType.IDistributedTrafficType createDistributedTrafficTypeReplica(jprime.DistributedTrafficType.IDistributedTrafficType to_replicate) {
		jprime.DistributedTrafficType.DistributedTrafficTypeReplica temp = new jprime.DistributedTrafficType.DistributedTrafficTypeReplica(null, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * jython method to create replica new child of type jprime.DistributedTrafficType.DistributedTrafficTypeReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.DistributedTrafficType.IDistributedTrafficType replicateDistributedTrafficType(PyObject[] v, String[] n) {
		PyObject[] v1 = new PyObject[v.length+1];
		for(int i=0;i<v.length;i++)v1[i+1]=v[i];
		v1[0]=Py.java2py(this);
		jprime.DistributedTrafficType.DistributedTrafficTypeReplica temp = new jprime.DistributedTrafficType.DistributedTrafficTypeReplica(v1,n);
		addChild(temp);
		return temp;
	}

	 /**
	  * Create a new child of type jprime.DistributedTrafficType.DistributedTrafficTypeReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.DistributedTrafficType.IDistributedTrafficType createDistributedTrafficTypeReplica(String name, jprime.DistributedTrafficType.IDistributedTrafficType to_replicate) {
		jprime.DistributedTrafficType.DistributedTrafficTypeReplica temp = new jprime.DistributedTrafficType.DistributedTrafficTypeReplica(name, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * Create a new child of type jprime.CNFTraffic.CNFTraffic and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.CNFTraffic.ICNFTraffic createCNFTraffic() {
		return createCNFTraffic(null);
	}

	/**
	  * jython method to create a a new child of type jprime.CNFTraffic.CNFTraffic, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.CNFTraffic.ICNFTraffic createCNFTraffic(PyObject[] v, String[] n) {
		if(jprime.ModelNode.__hasReplicate(v,n,false)) {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.CNFTraffic.CNFTrafficReplica temp = new jprime.CNFTraffic.CNFTrafficReplica(v1,n);
			addChild(temp);
			return temp;
		} else {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.CNFTraffic.CNFTraffic temp = new jprime.CNFTraffic.CNFTraffic(v1,n);
			addChild(temp);
			return temp;
		}
	}

	 /**
	  * Create a new child of type jprime.CNFTraffic.CNFTraffic and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.CNFTraffic.ICNFTraffic createCNFTraffic(String name) {
		jprime.CNFTraffic.CNFTraffic temp = new jprime.CNFTraffic.CNFTraffic(this);
		temp.setName(name);
		addChild(temp);
		return temp;
	}

	 /**
	  * Add a new child of type jprime.CNFTraffic.CNFTraffic.
	  */
	public void addCNFTraffic(jprime.CNFTraffic.CNFTraffic kid) {
		addChild(kid);
	}

	 /**
	  * Create a new child of type jprime.CNFTraffic.CNFTrafficReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.CNFTraffic.ICNFTraffic createCNFTrafficReplica(jprime.CNFTraffic.ICNFTraffic to_replicate) {
		jprime.CNFTraffic.CNFTrafficReplica temp = new jprime.CNFTraffic.CNFTrafficReplica(null, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * jython method to create replica new child of type jprime.CNFTraffic.CNFTrafficReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.CNFTraffic.ICNFTraffic replicateCNFTraffic(PyObject[] v, String[] n) {
		PyObject[] v1 = new PyObject[v.length+1];
		for(int i=0;i<v.length;i++)v1[i+1]=v[i];
		v1[0]=Py.java2py(this);
		jprime.CNFTraffic.CNFTrafficReplica temp = new jprime.CNFTraffic.CNFTrafficReplica(v1,n);
		addChild(temp);
		return temp;
	}

	 /**
	  * Create a new child of type jprime.CNFTraffic.CNFTrafficReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.CNFTraffic.ICNFTraffic createCNFTrafficReplica(String name, jprime.CNFTraffic.ICNFTraffic to_replicate) {
		jprime.CNFTraffic.CNFTrafficReplica temp = new jprime.CNFTraffic.CNFTrafficReplica(name, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * Create a new child of type jprime.CentralizedTrafficType.CentralizedTrafficType and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.CentralizedTrafficType.ICentralizedTrafficType createCentralizedTrafficType() {
		return createCentralizedTrafficType(null);
	}

	/**
	  * jython method to create a a new child of type jprime.CentralizedTrafficType.CentralizedTrafficType, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.CentralizedTrafficType.ICentralizedTrafficType createCentralizedTrafficType(PyObject[] v, String[] n) {
		if(jprime.ModelNode.__hasReplicate(v,n,false)) {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.CentralizedTrafficType.CentralizedTrafficTypeReplica temp = new jprime.CentralizedTrafficType.CentralizedTrafficTypeReplica(v1,n);
			addChild(temp);
			return temp;
		} else {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.CentralizedTrafficType.CentralizedTrafficType temp = new jprime.CentralizedTrafficType.CentralizedTrafficType(v1,n);
			addChild(temp);
			return temp;
		}
	}

	 /**
	  * Create a new child of type jprime.CentralizedTrafficType.CentralizedTrafficType and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.CentralizedTrafficType.ICentralizedTrafficType createCentralizedTrafficType(String name) {
		jprime.CentralizedTrafficType.CentralizedTrafficType temp = new jprime.CentralizedTrafficType.CentralizedTrafficType(this);
		temp.setName(name);
		addChild(temp);
		return temp;
	}

	 /**
	  * Add a new child of type jprime.CentralizedTrafficType.CentralizedTrafficType.
	  */
	public void addCentralizedTrafficType(jprime.CentralizedTrafficType.CentralizedTrafficType kid) {
		addChild(kid);
	}

	 /**
	  * Create a new child of type jprime.CentralizedTrafficType.CentralizedTrafficTypeReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.CentralizedTrafficType.ICentralizedTrafficType createCentralizedTrafficTypeReplica(jprime.CentralizedTrafficType.ICentralizedTrafficType to_replicate) {
		jprime.CentralizedTrafficType.CentralizedTrafficTypeReplica temp = new jprime.CentralizedTrafficType.CentralizedTrafficTypeReplica(null, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * jython method to create replica new child of type jprime.CentralizedTrafficType.CentralizedTrafficTypeReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.CentralizedTrafficType.ICentralizedTrafficType replicateCentralizedTrafficType(PyObject[] v, String[] n) {
		PyObject[] v1 = new PyObject[v.length+1];
		for(int i=0;i<v.length;i++)v1[i+1]=v[i];
		v1[0]=Py.java2py(this);
		jprime.CentralizedTrafficType.CentralizedTrafficTypeReplica temp = new jprime.CentralizedTrafficType.CentralizedTrafficTypeReplica(v1,n);
		addChild(temp);
		return temp;
	}

	 /**
	  * Create a new child of type jprime.CentralizedTrafficType.CentralizedTrafficTypeReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.CentralizedTrafficType.ICentralizedTrafficType createCentralizedTrafficTypeReplica(String name, jprime.CentralizedTrafficType.ICentralizedTrafficType to_replicate) {
		jprime.CentralizedTrafficType.CentralizedTrafficTypeReplica temp = new jprime.CentralizedTrafficType.CentralizedTrafficTypeReplica(name, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * Create a new child of type jprime.UDPTraffic.UDPTraffic and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.UDPTraffic.IUDPTraffic createUDPTraffic() {
		return createUDPTraffic(null);
	}

	/**
	  * jython method to create a a new child of type jprime.UDPTraffic.UDPTraffic, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.UDPTraffic.IUDPTraffic createUDPTraffic(PyObject[] v, String[] n) {
		if(jprime.ModelNode.__hasReplicate(v,n,false)) {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.UDPTraffic.UDPTrafficReplica temp = new jprime.UDPTraffic.UDPTrafficReplica(v1,n);
			addChild(temp);
			return temp;
		} else {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.UDPTraffic.UDPTraffic temp = new jprime.UDPTraffic.UDPTraffic(v1,n);
			addChild(temp);
			return temp;
		}
	}

	 /**
	  * Create a new child of type jprime.UDPTraffic.UDPTraffic and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.UDPTraffic.IUDPTraffic createUDPTraffic(String name) {
		jprime.UDPTraffic.UDPTraffic temp = new jprime.UDPTraffic.UDPTraffic(this);
		temp.setName(name);
		addChild(temp);
		return temp;
	}

	 /**
	  * Add a new child of type jprime.UDPTraffic.UDPTraffic.
	  */
	public void addUDPTraffic(jprime.UDPTraffic.UDPTraffic kid) {
		addChild(kid);
	}

	 /**
	  * Create a new child of type jprime.UDPTraffic.UDPTrafficReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.UDPTraffic.IUDPTraffic createUDPTrafficReplica(jprime.UDPTraffic.IUDPTraffic to_replicate) {
		jprime.UDPTraffic.UDPTrafficReplica temp = new jprime.UDPTraffic.UDPTrafficReplica(null, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * jython method to create replica new child of type jprime.UDPTraffic.UDPTrafficReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.UDPTraffic.IUDPTraffic replicateUDPTraffic(PyObject[] v, String[] n) {
		PyObject[] v1 = new PyObject[v.length+1];
		for(int i=0;i<v.length;i++)v1[i+1]=v[i];
		v1[0]=Py.java2py(this);
		jprime.UDPTraffic.UDPTrafficReplica temp = new jprime.UDPTraffic.UDPTrafficReplica(v1,n);
		addChild(temp);
		return temp;
	}

	 /**
	  * Create a new child of type jprime.UDPTraffic.UDPTrafficReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.UDPTraffic.IUDPTraffic createUDPTrafficReplica(String name, jprime.UDPTraffic.IUDPTraffic to_replicate) {
		jprime.UDPTraffic.UDPTrafficReplica temp = new jprime.UDPTraffic.UDPTrafficReplica(name, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * Create a new child of type jprime.SwingTCPTraffic.SwingTCPTraffic and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.SwingTCPTraffic.ISwingTCPTraffic createSwingTCPTraffic() {
		return createSwingTCPTraffic(null);
	}

	/**
	  * jython method to create a a new child of type jprime.SwingTCPTraffic.SwingTCPTraffic, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.SwingTCPTraffic.ISwingTCPTraffic createSwingTCPTraffic(PyObject[] v, String[] n) {
		if(jprime.ModelNode.__hasReplicate(v,n,false)) {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.SwingTCPTraffic.SwingTCPTrafficReplica temp = new jprime.SwingTCPTraffic.SwingTCPTrafficReplica(v1,n);
			addChild(temp);
			return temp;
		} else {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.SwingTCPTraffic.SwingTCPTraffic temp = new jprime.SwingTCPTraffic.SwingTCPTraffic(v1,n);
			addChild(temp);
			return temp;
		}
	}

	 /**
	  * Create a new child of type jprime.SwingTCPTraffic.SwingTCPTraffic and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.SwingTCPTraffic.ISwingTCPTraffic createSwingTCPTraffic(String name) {
		jprime.SwingTCPTraffic.SwingTCPTraffic temp = new jprime.SwingTCPTraffic.SwingTCPTraffic(this);
		temp.setName(name);
		addChild(temp);
		return temp;
	}

	 /**
	  * Add a new child of type jprime.SwingTCPTraffic.SwingTCPTraffic.
	  */
	public void addSwingTCPTraffic(jprime.SwingTCPTraffic.SwingTCPTraffic kid) {
		addChild(kid);
	}

	 /**
	  * Create a new child of type jprime.SwingTCPTraffic.SwingTCPTrafficReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.SwingTCPTraffic.ISwingTCPTraffic createSwingTCPTrafficReplica(jprime.SwingTCPTraffic.ISwingTCPTraffic to_replicate) {
		jprime.SwingTCPTraffic.SwingTCPTrafficReplica temp = new jprime.SwingTCPTraffic.SwingTCPTrafficReplica(null, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * jython method to create replica new child of type jprime.SwingTCPTraffic.SwingTCPTrafficReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.SwingTCPTraffic.ISwingTCPTraffic replicateSwingTCPTraffic(PyObject[] v, String[] n) {
		PyObject[] v1 = new PyObject[v.length+1];
		for(int i=0;i<v.length;i++)v1[i+1]=v[i];
		v1[0]=Py.java2py(this);
		jprime.SwingTCPTraffic.SwingTCPTrafficReplica temp = new jprime.SwingTCPTraffic.SwingTCPTrafficReplica(v1,n);
		addChild(temp);
		return temp;
	}

	 /**
	  * Create a new child of type jprime.SwingTCPTraffic.SwingTCPTrafficReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.SwingTCPTraffic.ISwingTCPTraffic createSwingTCPTrafficReplica(String name, jprime.SwingTCPTraffic.ISwingTCPTraffic to_replicate) {
		jprime.SwingTCPTraffic.SwingTCPTrafficReplica temp = new jprime.SwingTCPTraffic.SwingTCPTrafficReplica(name, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * Create a new child of type jprime.PPBPTraffic.PPBPTraffic and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.PPBPTraffic.IPPBPTraffic createPPBPTraffic() {
		return createPPBPTraffic(null);
	}

	/**
	  * jython method to create a a new child of type jprime.PPBPTraffic.PPBPTraffic, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.PPBPTraffic.IPPBPTraffic createPPBPTraffic(PyObject[] v, String[] n) {
		if(jprime.ModelNode.__hasReplicate(v,n,false)) {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.PPBPTraffic.PPBPTrafficReplica temp = new jprime.PPBPTraffic.PPBPTrafficReplica(v1,n);
			addChild(temp);
			return temp;
		} else {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.PPBPTraffic.PPBPTraffic temp = new jprime.PPBPTraffic.PPBPTraffic(v1,n);
			addChild(temp);
			return temp;
		}
	}

	 /**
	  * Create a new child of type jprime.PPBPTraffic.PPBPTraffic and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.PPBPTraffic.IPPBPTraffic createPPBPTraffic(String name) {
		jprime.PPBPTraffic.PPBPTraffic temp = new jprime.PPBPTraffic.PPBPTraffic(this);
		temp.setName(name);
		addChild(temp);
		return temp;
	}

	 /**
	  * Add a new child of type jprime.PPBPTraffic.PPBPTraffic.
	  */
	public void addPPBPTraffic(jprime.PPBPTraffic.PPBPTraffic kid) {
		addChild(kid);
	}

	 /**
	  * Create a new child of type jprime.PPBPTraffic.PPBPTrafficReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.PPBPTraffic.IPPBPTraffic createPPBPTrafficReplica(jprime.PPBPTraffic.IPPBPTraffic to_replicate) {
		jprime.PPBPTraffic.PPBPTrafficReplica temp = new jprime.PPBPTraffic.PPBPTrafficReplica(null, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * jython method to create replica new child of type jprime.PPBPTraffic.PPBPTrafficReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.PPBPTraffic.IPPBPTraffic replicatePPBPTraffic(PyObject[] v, String[] n) {
		PyObject[] v1 = new PyObject[v.length+1];
		for(int i=0;i<v.length;i++)v1[i+1]=v[i];
		v1[0]=Py.java2py(this);
		jprime.PPBPTraffic.PPBPTrafficReplica temp = new jprime.PPBPTraffic.PPBPTrafficReplica(v1,n);
		addChild(temp);
		return temp;
	}

	 /**
	  * Create a new child of type jprime.PPBPTraffic.PPBPTrafficReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.PPBPTraffic.IPPBPTraffic createPPBPTrafficReplica(String name, jprime.PPBPTraffic.IPPBPTraffic to_replicate) {
		jprime.PPBPTraffic.PPBPTrafficReplica temp = new jprime.PPBPTraffic.PPBPTrafficReplica(name, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * Create a new child of type jprime.TCPTraffic.TCPTraffic and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.TCPTraffic.ITCPTraffic createTCPTraffic() {
		return createTCPTraffic(null);
	}

	/**
	  * jython method to create a a new child of type jprime.TCPTraffic.TCPTraffic, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.TCPTraffic.ITCPTraffic createTCPTraffic(PyObject[] v, String[] n) {
		if(jprime.ModelNode.__hasReplicate(v,n,false)) {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.TCPTraffic.TCPTrafficReplica temp = new jprime.TCPTraffic.TCPTrafficReplica(v1,n);
			addChild(temp);
			return temp;
		} else {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.TCPTraffic.TCPTraffic temp = new jprime.TCPTraffic.TCPTraffic(v1,n);
			addChild(temp);
			return temp;
		}
	}

	 /**
	  * Create a new child of type jprime.TCPTraffic.TCPTraffic and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.TCPTraffic.ITCPTraffic createTCPTraffic(String name) {
		jprime.TCPTraffic.TCPTraffic temp = new jprime.TCPTraffic.TCPTraffic(this);
		temp.setName(name);
		addChild(temp);
		return temp;
	}

	 /**
	  * Add a new child of type jprime.TCPTraffic.TCPTraffic.
	  */
	public void addTCPTraffic(jprime.TCPTraffic.TCPTraffic kid) {
		addChild(kid);
	}

	 /**
	  * Create a new child of type jprime.TCPTraffic.TCPTrafficReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.TCPTraffic.ITCPTraffic createTCPTrafficReplica(jprime.TCPTraffic.ITCPTraffic to_replicate) {
		jprime.TCPTraffic.TCPTrafficReplica temp = new jprime.TCPTraffic.TCPTrafficReplica(null, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * jython method to create replica new child of type jprime.TCPTraffic.TCPTrafficReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.TCPTraffic.ITCPTraffic replicateTCPTraffic(PyObject[] v, String[] n) {
		PyObject[] v1 = new PyObject[v.length+1];
		for(int i=0;i<v.length;i++)v1[i+1]=v[i];
		v1[0]=Py.java2py(this);
		jprime.TCPTraffic.TCPTrafficReplica temp = new jprime.TCPTraffic.TCPTrafficReplica(v1,n);
		addChild(temp);
		return temp;
	}

	 /**
	  * Create a new child of type jprime.TCPTraffic.TCPTrafficReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.TCPTraffic.ITCPTraffic createTCPTrafficReplica(String name, jprime.TCPTraffic.ITCPTraffic to_replicate) {
		jprime.TCPTraffic.TCPTrafficReplica temp = new jprime.TCPTraffic.TCPTrafficReplica(name, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * Create a new child of type jprime.ICMPTraffic.ICMPTraffic and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.ICMPTraffic.IICMPTraffic createICMPTraffic() {
		return createICMPTraffic(null);
	}

	/**
	  * jython method to create a a new child of type jprime.ICMPTraffic.ICMPTraffic, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.ICMPTraffic.IICMPTraffic createICMPTraffic(PyObject[] v, String[] n) {
		if(jprime.ModelNode.__hasReplicate(v,n,false)) {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.ICMPTraffic.ICMPTrafficReplica temp = new jprime.ICMPTraffic.ICMPTrafficReplica(v1,n);
			addChild(temp);
			return temp;
		} else {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.ICMPTraffic.ICMPTraffic temp = new jprime.ICMPTraffic.ICMPTraffic(v1,n);
			addChild(temp);
			return temp;
		}
	}

	 /**
	  * Create a new child of type jprime.ICMPTraffic.ICMPTraffic and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.ICMPTraffic.IICMPTraffic createICMPTraffic(String name) {
		jprime.ICMPTraffic.ICMPTraffic temp = new jprime.ICMPTraffic.ICMPTraffic(this);
		temp.setName(name);
		addChild(temp);
		return temp;
	}

	 /**
	  * Add a new child of type jprime.ICMPTraffic.ICMPTraffic.
	  */
	public void addICMPTraffic(jprime.ICMPTraffic.ICMPTraffic kid) {
		addChild(kid);
	}

	 /**
	  * Create a new child of type jprime.ICMPTraffic.ICMPTrafficReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.ICMPTraffic.IICMPTraffic createICMPTrafficReplica(jprime.ICMPTraffic.IICMPTraffic to_replicate) {
		jprime.ICMPTraffic.ICMPTrafficReplica temp = new jprime.ICMPTraffic.ICMPTrafficReplica(null, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * jython method to create replica new child of type jprime.ICMPTraffic.ICMPTrafficReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.ICMPTraffic.IICMPTraffic replicateICMPTraffic(PyObject[] v, String[] n) {
		PyObject[] v1 = new PyObject[v.length+1];
		for(int i=0;i<v.length;i++)v1[i+1]=v[i];
		v1[0]=Py.java2py(this);
		jprime.ICMPTraffic.ICMPTrafficReplica temp = new jprime.ICMPTraffic.ICMPTrafficReplica(v1,n);
		addChild(temp);
		return temp;
	}

	 /**
	  * Create a new child of type jprime.ICMPTraffic.ICMPTrafficReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.ICMPTraffic.IICMPTraffic createICMPTrafficReplica(String name, jprime.ICMPTraffic.IICMPTraffic to_replicate) {
		jprime.ICMPTraffic.ICMPTrafficReplica temp = new jprime.ICMPTraffic.ICMPTrafficReplica(name, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * Create a new child of type jprime.PingTraffic.PingTraffic and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.PingTraffic.IPingTraffic createPingTraffic() {
		return createPingTraffic(null);
	}

	/**
	  * jython method to create a a new child of type jprime.PingTraffic.PingTraffic, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.PingTraffic.IPingTraffic createPingTraffic(PyObject[] v, String[] n) {
		if(jprime.ModelNode.__hasReplicate(v,n,false)) {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.PingTraffic.PingTrafficReplica temp = new jprime.PingTraffic.PingTrafficReplica(v1,n);
			addChild(temp);
			return temp;
		} else {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.PingTraffic.PingTraffic temp = new jprime.PingTraffic.PingTraffic(v1,n);
			addChild(temp);
			return temp;
		}
	}

	 /**
	  * Create a new child of type jprime.PingTraffic.PingTraffic and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.PingTraffic.IPingTraffic createPingTraffic(String name) {
		jprime.PingTraffic.PingTraffic temp = new jprime.PingTraffic.PingTraffic(this);
		temp.setName(name);
		addChild(temp);
		return temp;
	}

	 /**
	  * Add a new child of type jprime.PingTraffic.PingTraffic.
	  */
	public void addPingTraffic(jprime.PingTraffic.PingTraffic kid) {
		addChild(kid);
	}

	 /**
	  * Create a new child of type jprime.PingTraffic.PingTrafficReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.PingTraffic.IPingTraffic createPingTrafficReplica(jprime.PingTraffic.IPingTraffic to_replicate) {
		jprime.PingTraffic.PingTrafficReplica temp = new jprime.PingTraffic.PingTrafficReplica(null, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * jython method to create replica new child of type jprime.PingTraffic.PingTrafficReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.PingTraffic.IPingTraffic replicatePingTraffic(PyObject[] v, String[] n) {
		PyObject[] v1 = new PyObject[v.length+1];
		for(int i=0;i<v.length;i++)v1[i+1]=v[i];
		v1[0]=Py.java2py(this);
		jprime.PingTraffic.PingTrafficReplica temp = new jprime.PingTraffic.PingTrafficReplica(v1,n);
		addChild(temp);
		return temp;
	}

	 /**
	  * Create a new child of type jprime.PingTraffic.PingTrafficReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.PingTraffic.IPingTraffic createPingTrafficReplica(String name, jprime.PingTraffic.IPingTraffic to_replicate) {
		jprime.PingTraffic.PingTrafficReplica temp = new jprime.PingTraffic.PingTrafficReplica(name, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	 * @param visitor a generic visitor
	 */
	public abstract void accept(jprime.visitors.IGenericVisitor visitor);
}
