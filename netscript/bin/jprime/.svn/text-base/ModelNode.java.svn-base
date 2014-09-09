package jprime;

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


import java.lang.ref.SoftReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;

import jprime.Interface.IInterface;
import jprime.Interface.IInterfaceAlias;
import jprime.Link.ILink;
import jprime.Net.INet;
import jprime.ResourceIdentifier.EvalutedResourceID;
import jprime.ResourceIdentifier.ResourceID;
import jprime.ResourceIdentifier.Term;
import jprime.ResourceIdentifier.Term.TermScope;
import jprime.RoutingSphere.IRoutingSphere;
import jprime.VizAggregate.IVizAggregate;
import jprime.database.ChildId;
import jprime.database.ChildIdList;
import jprime.database.PKey;
import jprime.gen.ModelNodeVariable.Tripple;
import jprime.gen.ModelNodeVariable.VarDesc;
import jprime.partitioning.Partitioning;
import jprime.partitioning.PartitioningVisitor;
import jprime.routing.IRouteVisitor;
import jprime.routing.RouteCalculationVisitor;
import jprime.util.PersistentAttrMap;
import jprime.util.PersistentChildList;
import jprime.util.PersistentChildList.Key;
import jprime.util.UniqueName;
import jprime.variable.BooleanVariable;
import jprime.variable.Dataset;
import jprime.variable.Dataset.SimpleDatum;
import jprime.variable.FloatingPointNumberVariable;
import jprime.variable.IntegerVariable;
import jprime.variable.ModelNodeVariable;
import jprime.variable.ModelNodeVariable.VizAttr;
import jprime.variable.OpaqueVariable;
import jprime.variable.StringVariable;
import jprime.variable.SymbolVariable;
import jprime.visitors.IGenericVisitor;
import jprime.visitors.IPAddressAssignment;
import jprime.visitors.TLVVisitor;
import jprime.visitors.UIDAssignmentVisitor;
import jprime.visitors.VerifyVisitor;
import jprime.visitors.XMLVisitor;

import org.python.core.Py;
import org.python.core.PyObject;

/**
 * @author Nathanael Van Vorst
 *
 */
abstract public class ModelNode extends PersistableObject implements IModelNode  {
	//transient
	private SoftReference<ModelNode> __parent=null;
	private Metadata __metadata=null;
	private PersistentChildList children=null;
	protected PersistentAttrMap attrs=null;
	private int prefuse_row = -1;
	private PrefuseLocation prefuse_loc = null;
	private boolean prefuse_is_hub = false;
	protected static boolean __check_state__ = true;
	
	//persisted
	private int db_type;
	private long dbid;
	private long db_metadata_id;
	private long db_parent_id;
	private long replica_meta_id=0;
	private long replica_id=0;
	private long attached_link_id=0;
	private int db_order;
	private long uid;
	private long offset;
	private long size;
	private String name;
	private boolean has_been_replicated;
	
	
	
	/**
	 * an internal constructor used by _all_ nodes when created without a parent
	 */
	protected ModelNode(ModelNodeRecord rec) {
		super();
		this.persistable_state=PersistableState.UNMODIFIED;//must be first thing set!
		this.mods=Modified.NOTHING.id;
		this.__metadata=rec.meta;
		this.__parent=null;
		this.db_type=getTypeId();
		this.dbid=rec.dbid;
		this.db_metadata_id=rec.db_metadata_id;
		this.db_parent_id=rec.db_parent_id;
		this.db_order=rec.db_order;
		this.replica_meta_id=rec.replica_meta_id;
		this.replica_id=rec.replica_id;
		this.attached_link_id=rec.attached_link_id;
		this.uid=rec.uid;
		this.offset=rec.offset;
		this.size=rec.size;
		this.name=rec.name;
		this.has_been_replicated=rec.has_been_replicated;
		/* $if SEPARATE_PROP_TABLE $
		this.attrs = new PersistentAttrMap(this.__metadata,getDBID());
		$else$ */
		this.attrs = rec.attrs;
		if(rec.attrs == null) {
			throw new RuntimeException("ACK!");
		}
		this.attrs.setOwner(this);
		/* $endif$ */
		this.children = new PersistentChildList(this.__metadata);
		for(ChildId c : rec.kids) {
			if(c.type>EntityFactory.START_MODEL_NODE_TYPES) {
				if(c.order==-1) {
					throw new RuntimeException("What happened?");
				}
				this.children.addKey(c.child_id, c.order, c.type);
			}
			else if(c.type==EntityFactory.ModelNodeVaribaleStart) {
				/* $if SEPARATE_PROP_TABLE $
				this.attrs.addKey(c.child_id, c.order);//the order is really the dbname!
				$else$ */
				throw new RuntimeException("somehow we thing attrs are stored in a seperate table when SEPARATE_PROP_TABLE is set to false!");
				/* $endif$ */
			}
		}
		__metadata.loaded(this);
	}

	/**
	 * an internal constructor used by _all_ nodes when created with a parent
	 */
	protected ModelNode(IModelNode parent){
		super();
		this.persistable_state=PersistableState.NEW; //must be first thing set!
		this.mods=Modified.ALL.id;
		this.__parent=null;
		this.__metadata=null;
		this.db_type=getTypeId();
		this.db_parent_id=0;
		this.db_metadata_id=0;
		this.has_been_replicated=false;
		this.db_order=-1;
		if(null!=parent) {
			this.__parent = new SoftReference<ModelNode>((ModelNode)parent);
			this.db_parent_id=this.__parent.get().getDBID();
			this.setMetadata(this.__parent.get().getMetadata());
			/* $if SEPARATE_PROP_TABLE $
			this.attrs = new PersistentAttrMap(this.getMetadata(),getDBID());
			$else$ */
			this.attrs = new PersistentAttrMap(this);
			/* $endif$ */
			this.children = new PersistentChildList(this.getMetadata());
		}
		else {
			/* $if SEPARATE_PROP_TABLE $
			this.attrs = new PersistentAttrMap(null,-1);
			$else$ */
			this.attrs = new PersistentAttrMap(this);
			/* $endif$ */
			this.children = new PersistentChildList(null);
		}
	}
	
	/** constructor for jython 
	 * @param values
	 * @param names
	 */
	protected ModelNode(PyObject[] values, String[] names) {
		super();
		this.persistable_state=PersistableState.NEW;//must be first thing set!
		this.mods=Modified.ALL.id;
		this.__parent = null;
		this.__metadata = null;
		this.db_type=getTypeId();
		this.db_parent_id=0;
		this.db_metadata_id=0;
		has_been_replicated=false;
		this.db_order=-1;
		int dif = values.length - names.length;
		boolean setName=false;
		if(dif > 2){
			throw Py.AttributeError("Incorrect number of unnamed arguments");
		}
		else if(dif == 0){
			//no op
		}		
		else if(dif == 1){
			this.__parent = new SoftReference<ModelNode>((ModelNode)values[0].__tojava__(ModelNode.class));
			this.db_parent_id=this.__parent.get().getDBID();
			this.setMetadata(this.__parent.get().getMetadata());
		}
		else if(dif == 2){
			this.__parent = new SoftReference<ModelNode>((ModelNode)values[0].__tojava__(ModelNode.class));
			this.db_parent_id=this.__parent.get().getDBID();
			this.setMetadata(this.__parent.get().getMetadata());
			this.setName(values[1].asString());
			setName=true;
		}
		
		/* $if SEPARATE_PROP_TABLE $
		this.attrs = new PersistentAttrMap(this.getMetadata(),getDBID());
		$else$ */
		this.attrs = new PersistentAttrMap(this);
		/* $endif$ */
		this.children = new PersistentChildList(this.getMetadata());
		
		
		for(int pass=0 ; pass<2 ; pass++){
			for(int i=values.length-1 ; i>=dif ; i--){
				List<Tripple> tripples = ModelNodeVariable.jythonName2Tripples(names[i-dif]);
				if(tripples != null){
					boolean called=false;
					for(Tripple t : tripples){
						if(called) break;
						Class<?> currentClass = this.getClass();
						//Iterate from the currentClass up to including modelNodeClass
						while(true){
							try {
								Method m[] = currentClass.getDeclaredMethods();
								//Look at all the methods in the current class
								for (int j = 0; j < m.length; j++){ 
									//Execute the method if the name of the method equals the one on the tripple &
									//it has only one parameter &
									//the parameter is of the same type as the on on the tripple
									if(m[j].getName().equals(t.fname) && m[j].getParameterTypes().length==1 && m[j].getParameterTypes()[0].getName().equals(t.cls.getName())){
										//If the expected parameter type is not a String
										if(!m[j].getParameterTypes()[0].getName().equals(String.class.getName())){
											if(pass==0) continue;
											processArgument(values[i], t, m[j],false);
										}
										else if(pass==0){
											processArgument(values[i], t, m[j],true);
										}
										called =true;
									}
								}
							}
							catch (Throwable e) {
								jprime.Console.err.println(e);
							}
							if(ModelNode.class.getName().equals(currentClass.getName())){
								break;
							}
							else{
								currentClass = currentClass.getSuperclass();
							}
						}
					}
				}else if(names[i-dif].equals("parent")){
					this.__parent = new SoftReference<ModelNode>((ModelNode)values[i].__tojava__(ModelNode.class));
					this.db_parent_id=this.__parent.get().getDBID();
					this.setMetadata(this.__parent.get().getMetadata());
				}else{
					throw Py.AttributeError(names[i-dif]+" is not a valid attribute");
				}
			}
			if(pass==0 && name == null && !setName){//check if name was assigned
				//jprime.Console.out.println("Name was not set.....");
				setName(null);
			}
		}
		if(__parent == null){
			throw Py.AttributeError("Must specify parent");
		}
	}
	
	protected void processArgument(PyObject value, Tripple t, Method m, boolean primitive) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		if(primitive){
			//Find out the type and try to coerce the value of the PyObject 
			//into boolean, float, int or string.
			//Finally, invoke the method with a String containing any of those types
			Class<?> paramCls = ModelNodeVariable.int2type(t.id);
			String arg=null;
			//Boolean
			if(paramCls.equals(BooleanVariable.class)){
				try{
					int temp = value.asInt();
					arg = temp==1 ? "true" : "false";
				}
				catch(Exception e){
					arg = value.asString();
				}finally{
					m.invoke(this, arg);
				}
			}
			//Float
			else if(paramCls.equals(FloatingPointNumberVariable.class)){
				try{
					arg = ""+value.asDouble();
				}
				catch(Exception e){
					arg = value.asString();
				}finally{
					m.invoke(this, arg);
				}
			}
			//Integer
			else if(paramCls.equals(IntegerVariable.class)){
				try{
					arg=""+value.asInt();
				}
				catch(Exception e){
					arg = value.asString();
				}finally{
					m.invoke(this, arg);
				}
			}
			//String or any object that can be a String (OpaqueVariable)
			else if(paramCls.equals(String.class) || paramCls.equals(StringVariable.class) || paramCls.equals(OpaqueVariable.class)){
				try{
					arg=(String)value.asString();
				}
				catch(Exception e){
					throw Py.AttributeError("Attr should be a String. fname="+t.fname+", value="+value);
				}finally{
					m.invoke(this, arg);
				}
			}
		}
		else{
			if(value.isSequenceType()){
				Iterable<PyObject> vals = value.asIterable();
				for(PyObject pobj : vals){
					ModelNode ob = (ModelNode)pobj.__tojava__(ModelNode.class);
					m.invoke(this, t.cls.cast(ob));
				}
			}else{
				ModelNode ob = (ModelNode)value.__tojava__(ModelNode.class);
				m.invoke(this, t.cls.cast(ob));
			}
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
		getMetadata().collect(this);
		super.finalize();
	}

	protected void enforceChildConstraints(){
		//will be overridden by my children
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ModelNode) {
			return this.getDBMetaID() == ((ModelNode)obj).getDBMetaID() && getDBID() ==  ((ModelNode)obj).getDBID();
		}
		return false;
	}
	
	public int compareTo(IModelNode o) {
		return (int)(getUID() - o.getUID());
	}

	/* (non-Javadoc)
	 * @see jprime.IPeristable#save()
	 */
	public synchronized void save() {
		getMetadata().save(this);
	}
	
	/* (non-Javadoc)
	 * @see jprime.PeristableObject#getPKey()
	 */
	public PKey getPKey() {
		return new PKey(db_metadata_id, dbid);
	}
	
	/**
	 * @return
	 */
	protected int getDBType() {
		return db_type;
	}
	
	
	public long getReplicaMetaId() {
		return replica_meta_id;
	}
	
	protected void setReplicaId(long replica_id) {
		if(this.replica_id != replica_id) {
			this.replica_id = replica_id;
			modified(Modified.LOCAL_PROPS);
		}
	}

	protected void setReplicaMetaId(long replica_meta_id) {
		if(this.replica_meta_id != replica_meta_id) {
			this.replica_meta_id = replica_meta_id;
			modified(Modified.LOCAL_PROPS);
		}
	}
	
	protected long getAttached_link_id() {
		return attached_link_id;
	}

	protected void setAttached_link_id(long attached_link_id) {
		if(this.attached_link_id != attached_link_id){
			this.attached_link_id = attached_link_id;
			modified(Modified.LOCAL_PROPS);
		}
	}

	protected boolean isHas_been_replicated() {
		return has_been_replicated;
	}

	protected void setHas_been_replicated(boolean has_been_replicated) {
		if(this.has_been_replicated != has_been_replicated) {
			this.has_been_replicated = has_been_replicated;
			modified(Modified.LOCAL_PROPS);
		}
	}

    /* (non-Javadoc)
     * @see java.lang.Object#clone()
     */
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

	/* (non-Javadoc)
	 * @see jprime.PersistableObject#getDBID()
	 */
	public long getDBID() {
		return dbid;
	}
	
	/**
	 * @return
	 */
	public int getDBOrder() {
		return db_order;
	}

	/* (non-Javadoc)
	 * @see jprime.PersistableObject#getChildIds()
	 */
	public synchronized ChildIdList getChildIds() {
		/* $if SEPARATE_PROP_TABLE $
		if(attrs==null)
			if(children==null)
				return new ChildIdList();
			else
				return children.getChildIds();
		return attrs.getChildIds(children.getChildIds());
		$else$ */
		if(children==null)
			return new ChildIdList();
		else
			return children.getChildIds();
		/* $endif$ */
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		try {
			return "["+this.getClass().getSimpleName()+"@"+this.oldHashCode()+"]"+getUniqueName();
		}
		catch(Exception e) {
			return super.toString();
		}
	}
	
	/**
	 * get descendant by a query string separated by '.'
	 * @param query
	 */
	public IModelNode getChildByQuery(String query){
		String[] steps = query.trim().replace(".",":").split("\\:");
		IModelNode current = this;
		for(String child : steps){
			current = current.getChildByName(child);
			if(current == null){
				return null;
			}
		}
		return current;
	}
	
	/**
	 * get descendant by a query string separated by '.'
	 * @param query
	 */
	public IModelNode get(String query){
		return this.getChildByQuery(query);
	}
	
	/**
	 * @param parent
	 */
	protected final void setParent(IModelNode parent){
		if(this.__parent == null || this.__parent.get()==null) {
			this.__parent = new SoftReference<ModelNode>((ModelNode)parent);
			this.db_parent_id=this.__parent.get().getDBID();
			this.setMetadata(this.__parent.get().getMetadata());
		}
		else if( this.__parent.get() != parent){
			throw new RuntimeException("The parent was already set!");
		}
		modified(Modified.LOCAL_PROPS);
	}
	
	/**
	 * @param m
	 */
	public void setMetadata(Metadata m) {
		if(this.__metadata==null) {
			this.__metadata=m;
			this.db_metadata_id=m.getDBID();
			this.dbid=getMetadata().getNextModelNodeDBID();
			if(this.children != null)
				this.children.setMetadata(m);
			/* $if SEPARATE_PROP_TABLE $
			if(this.attrs != null)
				this.attrs.setMetadata(m,getDBID());
			$endif$ */
			__metadata.loaded(this);
		}
		else if(this.__metadata!=m){
			throw new RuntimeException("the metadata was already assigned!");
		}
	}

	/**
	 * @param ord  the order for the parent's children list
	 */
	protected final void setOrder(int ord){
		this.db_order = ord;
		modified(Modified.LOCAL_PROPS);
	}
	
	private void setupChild(Metadata meta, ModelNode p, int order) {
		if(this.__metadata==null) {
			this.__metadata=meta;
		}
		else if(__metadata != meta) {
			throw new RuntimeException("What happened?");
		}
		if(db_parent_id ==0 ) {
			db_parent_id = p.getDBID();
			__parent = new SoftReference<ModelNode>(p);
		}
		else if(db_parent_id != p.getDBID()){
			throw new RuntimeException("What happened?");
		}
		db_order=order;
		if(name==null) {
			this.setName(null);
		}
		else {
			modified(Modified.LOCAL_PROPS);
		}
	}
	
	/**
	 * add a child to this node
	 * @param child
	 * @return
	 */
	public synchronized void addChild(ModelNode child){
		if(__check_state__ && getMetadata().getState().gt(State.PRE_COMPILED)) {
			throw new RuntimeException("Once a model is compiled its structure cannot be changed! CurrentState="+getMetadata().getState());
		}
		if(has_been_replicated) {
			throw new RuntimeException("This node has been replicated and thus you cannot structually modified it becuase the changes will not show up in the replicas!");
		}
		child.setupChild(getMetadata(),this,this.children.size());
		if(null != children.get(child.getName(),false)) {
			jprime.Console.out.println("kids, "+getAllChildren().size());
			for(ModelNode m : getAllChildren()) {
				jprime.Console.out.println("\t"+m.getUniqueName());
			}
			throw new RuntimeException("The node "+getUniqueName()+" already has a child by the name "+child.getName()+" and duplicate child names are not allowed!");
		}
		this.children.add(child);
		modified(Modified.CHILDREN);
	}
	
	/**
	 * @param used by replicas to do a deep copy of the node.
	 */
	public abstract jprime.ModelNode deepCopy(jprime.ModelNode parent);	
	

	/* (non-Javadoc)
	 * @see jprime.IModelNode#getRank(jprime.IModelNode)
	 */
	public final long getRank(IModelNode anchor) {
		if(null==anchor)
			return getUID();
		long t=getUID();
		long diff=anchor.getUID()-anchor.getSize();
		if(anchor.containsUID(t) && diff<t) {
			return (t-diff);
		}
		throw new RuntimeException("Tried to get the rank of a node using an anchor which was not an ancestor! node="+getUniqueName()+", anchor="+anchor.getUniqueName());
	}
	
	/* (non-Javadoc)
	 * @see jprime.IModelNode#getChildByRank(long, jprime.IModelNode)
	 */
	public IModelNode getChildByRank(long rank, IModelNode anchor) {
		if(this.getRank(anchor)==rank) return this;
		
		if(getMinRank(anchor)<=rank && getMaxRank(anchor)>=rank) {
			//i have this child
			for(IModelNode c : getAllChildren()) {
				IModelNode rv = c.getChildByRank(rank, anchor);
				if(rv != null) return rv;
			}
		}
		return null;
	}

	
	/* (non-Javadoc)
	 * @see jprime.IModelNode#getMinUID()
	 */
	public final long getMinUID() {
		return getMinRank(null);
	}	
	
	/* (non-Javadoc)
	 * @see jprime.IModelNode#getMinRank(jprime.IModelNode)
	 */
	public final long getMinRank(IModelNode anchor){
		if(null==anchor)
			return getUID()-getSize()+1;		
		return getRank(anchor)-getSize()+1;
	}
	
	/* (non-Javadoc)
	 * @see jprime.IModelNode#getMaxRank(jprime.IModelNode)
	 */
	public final long getMaxRank(IModelNode anchor){
		return getRank(anchor);		
	}
	
	/* (non-Javadoc)
	 * @see jprime.IModelNode#containsRank(jprime.IModelNode, long)
	 */
	public final boolean containsRank(IModelNode anchor, long rank){
		return rank <= getRank(anchor) && rank>=getMinRank(anchor);
	}
	
	/* (non-Javadoc)
	 * @see jprime.IModelNode#containsUID(long)
	 */
	public final boolean containsUID(long uid) {
		return uid <= getUID() && uid>=getMinRank(null);
	}
	
	/* (non-Javadoc)
	 * @see jprime.IModelNode#getUID()
	 */
	public final long getUID() {
		/*if(getMetadata().getState().lte(State.PRE_COMPILED)) {
			throw new RuntimeException("The model must have a state of "+State.COMPILING+" or higher to fetch the uid! CurrentState="+getMetadata().getState());
		}*/ 
		//XXXX
		/*
		return ((IntegerVariable)attrs.get(ModelNodeVariable.uid())).getValue();
		*/
		return uid;
	}
	
	/* (non-Javadoc)
	 * @see jprime.IModelNode#setUID(long)
	 */
	public final void setUID(long id) {
		if(getMetadata().getState().lt((State.COMPILING))) {
			throw new RuntimeException("The model must have a state of "+State.COMPILING+" to set the uid! CurrentState="+getMetadata().getState());
		}
		this.uid=id;
		modified(Modified.LOCAL_PROPS);
	}
	
	/**
	 * Set the name of the node
	 * @param name
	 */
	public final void setName(String _name) {
		if(null==_name) {
			_name=this.getClass().getSimpleName()+"_"+this.getDBID();
		}
		if(name==null) {
			this.name=_name;
			modified(Modified.LOCAL_PROPS);
		}
		else if(name.compareTo(_name)!=0){
			throw new RuntimeException("What happened!");
		}
	}
	
	/* (non-Javadoc)
	 * @see jprime.IModelNode#getName()
	 */
	public final String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see jprime.IModelNode#getSize()
	 */
	public final long getSize() {
		/*
		if(getMetadata().getState().lte(State.PRE_COMPILED)) {
			throw new RuntimeException("The model must have a state of "+State.COMPILING+" or higher to fetch the uid!");
		}*/
		return size;
	}
	
	/* (non-Javadoc)
	 * @see jprime.IModelNode#setSize(long)
	 */
	public final void setSize(long s) {
		if(getMetadata().getState().lt((State.COMPILING))) {
			throw new RuntimeException("The model must have a state of "+State.COMPILING+" to set the size! CurrentState="+getMetadata().getState());
		}
		this.size=s;
		modified(Modified.LOCAL_PROPS);
	}

	/* (non-Javadoc)
	 * @see jprime.IModelNode#getOffset()
	 */
	public final long getOffset() {
		if(getMetadata().getState().lte(State.PRE_COMPILED)) {
			throw new RuntimeException("The model must have a state of "+State.COMPILING+" or higher to fetch the uid!");
		}
		return offset;
	}
	
	public final void setOffset(long o) {
		if(getMetadata().getState().lt((State.COMPILING))) {
			throw new RuntimeException("The model must have a state of "+State.COMPILING+" to set the offset! CurrentState="+getMetadata().getState());
		}
		this.offset=o;
		modified(Modified.LOCAL_PROPS);
	}

	/* (non-Javadoc)
	 * @see jprime.IModelNode#getOrder()
	 */
	public final int getOrder() {
		return db_order;
	}
	
	/* (non-Javadoc)
	 * @see jprime.IModelNode#deference()
	 */
	public IModelNode deference() {
		return this;
	}
	
	
	/**
	 * @return
	 */
	public PersistentAttrMap getPersistentAttrMap() {
		return this.attrs;
	}
	
	/**
	 * @return
	 */
	public PersistentChildList getPersistentChildList() {
		return this.children;
	}
	
	/* (non-Javadoc)
	 * @see jprime.IModelNode#getAttributes()
	 */
	public Map<String,String> getAttributes() {
		return getAttributes(true);
	}
	
	/* (non-Javadoc)
	 * @see jprime.IModelNode#getVizAttributes(jprime.variable.Dataset)
	 */
	public Map<String,VizAttr> getVizAttributes(Dataset d) {
		final Map<String,VizAttr> rv = new HashMap<String,VizAttr>();
		for(VarDesc vd : ModelNodeVariable.getClsDesc(this).vars.values()) {
			if(vd.id == ModelNodeVariable.uid()) {
				if(getMetadata().getState().lte(State.PRE_COMPILED))
					rv.put(vd.name, new VizAttr("-", vd,false));
				else
					rv.put(vd.name, new VizAttr(getUID()+"", vd,false));
			}
			else if(vd.id == ModelNodeVariable.name()) {
				continue;
			}
			else if(vd.is_visualized) {
				VizAttr va = null;
				if(this.attrs.containsKey(vd.id)) {
					ModelNodeVariable a = this.attrs.get(vd.id);
					if(vd.is_stat) {
						va=new VizAttr(attrs.get(vd.id).getRuntimeValueAsString(this,d),vd,a instanceof SymbolVariable);
					}
					else {
						va=new VizAttr(attrs.get(vd.id).toString(),vd,a instanceof SymbolVariable);
					}
				}
				else {
					va=new VizAttr(vd.default_value,vd,false);
				}
				rv.put(vd.name, va);
			}
		}
		return rv;
	}
	
	/* (non-Javadoc)
	 * @see jprime.IModelNode#getAttributes(boolean)
	 */
	public Map<String,String> getAttributes(boolean includeDefaultValues) {
		final Map<String,String> rv = new HashMap<String,String>();
		if(includeDefaultValues) {
			for(VarDesc vd : ModelNodeVariable.getClsDesc(this).vars.values()) {
				if(this.attrs.containsKey(vd.id)) {
					rv.put(ModelNodeVariable.int2name(vd.id), attrs.get(vd.id).toString());
				}
				else {
					rv.put(ModelNodeVariable.int2name(vd.id), vd.default_value);
				}
			}
		}
		else {
			for(ModelNodeVariable v : attrs.values()) {
				rv.put(ModelNodeVariable.int2name(v.getDBName()), v.toString());
			}
		}
		return rv;
	}
	
	/* (non-Javadoc)
	 * @see jprime.IModelNode#setAttribute(int, java.lang.String)
	 */
	public ModelNodeVariable setAttribute(int varId, String value) throws InstantiationException, IllegalAccessException {
		ModelNodeVariable v = getAttributeByName(varId);
		if(v == null) {
			Class<? extends jprime.gen.ModelNodeVariable > cls = ModelNodeVariable.int2type(varId);
			v=(jprime.variable.ModelNodeVariable)cls.newInstance();
			v.setDBName(varId);
			v.setValueAsString(value);
			v.setOwner(this);
			this.attrs.put(varId,v);
			modified(Modified.ATTRS);
		}
		else {
			/* $if false == SEPARATE_PROP_TABLE $ */
			modified(Modified.ATTRS);
			/* $endif$ */
			v.setValueAsString(value);
		}
		//jprime.Console.out.println(this.getUniqueName()+"::"+ModelNodeVariable.int2name(v.getDBName())+"="+this.getAttributeByName(varId));
		return v;
	}
		
	/* (non-Javadoc)
	 * @see jprime.IModelNode#getAttributeValues()
	 */
	public Collection<jprime.variable.ModelNodeVariable> getAttributeValues() {
		return attrs.values();
	}
	
	public Collection<jprime.variable.ModelNodeVariable> getAllAttributeValues() {
		TreeSet<Integer> encoded_attrs=new TreeSet<Integer>();
		ArrayList<jprime.variable.ModelNodeVariable> rv = new ArrayList<ModelNodeVariable>();
		{
			ModelNode base_node = this;
			while(base_node != null) {
				for(ModelNodeVariable v : base_node.getAttributeValues()) {
					if(encoded_attrs.contains(v.getDBName())) continue;
					encoded_attrs.add(v.getDBName());
					rv.add(v);
				}
				if(base_node instanceof ModelNodeReplica)
					base_node=((ModelNodeReplica)base_node).getReplicatedNode();
				else base_node=null;
			};
		}
		return rv;
	}
	

	
	/* (non-Javadoc)
	 * @see jprime.IModelNode#getAllChildren()
	 */
	public final List<ModelNode> getAllChildren() {
		return children;		
	}
	
	/* (non-Javadoc)
	 * @see jprime.IModelNode#getMetadata()
	 */
	public final Metadata getMetadata() {
		if(this.__metadata == null) {
			throw new RuntimeException("How did this happen?");
		}
		return this.__metadata;
	}
	
	public long getDBMetaID() {
		return db_metadata_id;
	}

	/**
	 * @return the list of attribute name(hashes) this node _should_ have.
	 */
	public abstract List<Integer> getAttrIds();
	
	/* (non-Javadoc)
	 * @see jprime.IModelNode#getAlignments(jprime.partitioning.Partitioning)
	 */
	public Set<Integer> getAlignments(Partitioning p) {
		if(getMetadata().getState().lt(State.PARTITIONING)) {
			throw new RuntimeException("The model must have a state of "+State.PARTITIONING+" or higher to fetch the alignments! CurrentState="+getMetadata().getState());
		}
		return getParent().getAlignments(p);
	}
	
	/* (non-Javadoc)
	 * @see jprime.IModelNode#findNodeByUID(long)
	 */
	public IModelNode findNodeByUID(long uid) {
		if(getUID() == uid) return this;
		else if(this.containsUID(uid)) {
			for(IModelNode c : getAllChildren()) {
				IModelNode rv = c.findNodeByUID(uid);
				if(null != rv)
					return rv;
			}
		}
		return null;
	}
	
	/**
	 * @return
	 */
	public long getParentId() {
		return db_parent_id;
	}
	
	/**
	 * @return
	 */
	public long getReplicaId() {
		return replica_id;
	}
	
	/**
	 * @return
	 */
	public long getAttachedLinkId() {
		return attached_link_id;
	}
	
	/* (non-Javadoc)
	 * @see jprime.IModelNode#getParent_nofetch()
	 */
	public final IModelNode getParent_nofetch() {
		IModelNode rv=null;
		if(this.__parent ==null) {
			if(this.db_parent_id!=0) {
				//its been reclaimed
				//we need to reload it
				rv=getMetadata().getPreloadedModelNode(this.db_parent_id);
				this.__parent = new SoftReference<ModelNode>((ModelNode)rv);
			}
		}
		else {
			rv=this.__parent.get();
		}
		return rv;
	}
	
	/* (non-Javadoc)
	 * @see jprime.IModelNode#getParent()
	 */
	public final IModelNode getParent() {
		ModelNode rv = (this.__parent ==null)?null:this.__parent.get();
		if(rv==null) {
			if(this.db_parent_id==0) {
				//no parent
				return null;
			}
			//its been reclaimed
			//we need to reload it
			rv=getMetadata().loadModelNode(this.db_parent_id);
			this.__parent = new SoftReference<ModelNode>((ModelNode)rv);
		}
		if(rv == null) {
			throw new RuntimeException("XXX");
		}
    	/* $if DEBUG $
		getMetadata().logAccess(rv);
		$endif$ */
		return rv;
	}
	
	/* (non-Javadoc)
	 * @see jprime.IModelNode#getUniqueName()
	 */
	public final UniqueName getUniqueName() {
		return new UniqueName(this);
	}
	
	/* (non-Javadoc)
	 * @see jprime.IModelNode#getNumChildren()
	 */
	public final int getNumberOfChildren() {
		return getAllChildren().size();
	}
	
	/* (non-Javadoc)
	 * @see jprime.IModelNode#getChildByName(java.lang.String)
	 */
	public IModelNode getChildByName(String name){
		return children.get(name,true);
	}
	
	/* (non-Javadoc)
	 * @see jprime.IModelNode#getAttributeByName(java.lang.String)
	 */
	public jprime.variable.ModelNodeVariable getAttributeByName(String name) {
		return getAttributeByName(ModelNodeVariable.name2int(name));
	}

	/* (non-Javadoc)
	 * @see jprime.IModelNode#getAttributeByName(java.lang.String)
	 */
	public jprime.variable.ModelNodeVariable getAttributeByName(int name) {
		return attrs.get(name);
	}
	
	public String getAttributeValueByName(int name) {
		ModelNodeVariable rv = attrs.get(name);
		if(rv != null)
			return rv.toString();
		
		VarDesc v =ModelNodeVariable.getClsDesc(this).vars.get(name);
		if(v!=null)
			return v.default_value;
		throw new RuntimeException("Invalid attr for this node!");
	}

	
	/* (non-Javadoc)
	 * @see jprime.IModelNode#getRuntimeValueByName(int, jprime.variable.Dataset)
	 */
	public SimpleDatum getRuntimeValueByName(int name, Dataset ds) {
		if(ds == null || getMetadata().getState().lte(State.PRE_COMPILED))
			return null;
		return ds.getMostRecentDatum(name, getUID());
	}

	
	/* (non-Javadoc)
	 * @see jprime.IModelNode#isReplica()
	 */
	public boolean isReplica() {
		if(replica_id==0) return false;
		return replica_id != getDBID();
	}

	
	/* (non-Javadoc)
	 * @see jprime.IModelNode#isAlias()
	 */
	public boolean isAlias(){
		return false;
	}
	
	
	/* (non-Javadoc)
	 * @see jprime.IModelNode#enforceMinimumChildConstraints()
	 */
	public void enforceMinimumChildConstraints() {
		//types which have child lists will override this
	}

	
	/* (non-Javadoc)
	 * @see jprime.IModelNode#accept(jprime.visitors.IGenericVisitor)
	 */
	public abstract void accept(IGenericVisitor visitor);
	
	/* (non-Javadoc)
	 * @see jprime.IModelNode#accept(jprime.visitors.IPAddressAssignment)
	 */
	public void accept(IPAddressAssignment visitor) {
		visitor.visit(this);
	}
	
	/* (non-Javadoc)
	 * @see jprime.IModelNode#accept(jprime.visitors.UIDAssignmentVisitor)
	 */
	public void accept(UIDAssignmentVisitor visitor) {
		visitor.visit(this);
	}
	
	/* (non-Javadoc)
	 * @see jprime.IModelNode#accept(jprime.visitors.TLVVisitor)
	 */
	public void accept(TLVVisitor visitor) {
		visitor.visit(this);
	}
	
	/* (non-Javadoc)
	 * @see jprime.IModelNode#accept(jprime.routing.IRouteVisitor)
	 */
	public void accept(IRouteVisitor visitor) {
		visitor.visit(this);
	}
	
	/* (non-Javadoc)
	 * @see jprime.IModelNode#accept(jprime.visitors.VerifyVisitor)
	 */
	public void accept(VerifyVisitor visitor) {
		visitor.visit(this);
	}
	
	/* (non-Javadoc)
	 * @see jprime.IModelNode#accept(jprime.routing.RouteCalculationVisitor)
	 */
	public void accept(RouteCalculationVisitor visitor){
		visitor.visit(this);
	}
	
	/* (non-Javadoc)
	 * @see jprime.IModelNode#accept(jprime.partitioning.PartitioningVisitor)
	 */
	public void accept(PartitioningVisitor visitor) {
		visitor.visit(this);
	}

	/* (non-Javadoc)
	 * @see jprime.IModelNode#accept(jprime.visitors.XMLVisitor)
	 */
	public void accept(XMLVisitor visitor) {
		visitor.visit(this);
	}
	
	/**
	 * @param anchor
	 * @param path
	 * @return
	 */
	public static IModelNode evaluatePath(IModelNode anchor, String path) {
		if(null==path) {
			try {
				throw new NullPointerException("The path was null! anchor="+anchor.getUniqueName());
			}catch(Exception e) {
				jprime.Console.err.printStackTrace(e);
				jprime.Console.halt(-1);
			}
			throw new NullPointerException("The path was null! anchor="+anchor.getUniqueName());
		}
		//jprime.Console.out.println("evaluatePath("+anchor.getUniqueName()+","+path+")");
		String[] toks=path.split(":");
		IModelNode temp_ack = null;
		IModelNode temp = anchor;
		for(int i=0;i<toks.length;i++) {
			temp_ack=temp;
			//jprime.Console.out.println("\ttemp="+temp.getUniqueName());
			if("..".equals(toks[i])) {
				//go up
				temp=temp.getParent();
			}
			else {
				//go down
				temp=temp.getChildByName(toks[i]);
			}
			if(null==temp) {
				jprime.Console.err.println("Invalid path '"+path+"' starting from '"+anchor.getUniqueName()+", looking for toks["+i+"]:"+toks[i]);
				if(null != temp_ack) {
					jprime.Console.err.println("\tKIDS at "+temp_ack.getUniqueName());
					for(IModelNode c: temp_ack.getAllChildren()) {
						jprime.Console.err.println("\t\t\t"+c.getUniqueName());
					}
				}
				else {
					jprime.Console.err.println("\ttemp_ack is null!");
				}
				try {
					throw new RuntimeException("Invalid path '"+path+"' starting from '"+anchor.getUniqueName()+"'!");
				} catch(Exception e) {
					jprime.Console.err.printStackTrace(e);
					jprime.Console.halt(100);	
				}
			}
		}
		return temp;
	}
	
	/**
	 * find the relative path from anchor to remoteNode
	 * 
	 * @param anchor the node to start from
	 * @param remoteNode the destination node
	 * @return
	 */
	public static String relativePath(IModelNode anchor, IModelNode remoteNode) {
		if(anchor == null)
			throw new NullPointerException("anchor was null!");
		if(remoteNode == null)
			throw new NullPointerException("remoteNode was null!");
		//find the relative path from anchor to remoteNode
		//jprime.Console.out.println("Calculating rel-path from "+anchor.getUniqueName()+" to "+remoteNode.getUniqueName());

		//construct paths from the topnet
		ArrayList<IModelNode> anchor_list = new ArrayList<IModelNode>();
		ArrayList<IModelNode> remoteNode_list = new ArrayList<IModelNode>();
		IModelNode temp=anchor;
		while(temp!=null) {
			anchor_list.add(0,temp);
			temp=temp.getParent();
		}
		temp=remoteNode;
		while(temp!=null) {
			remoteNode_list.add(0,temp);
			temp=temp.getParent();
		}
		//find the deepest common ancestor of the two nodes
		int idx = 0;
		int end = anchor_list.size()>remoteNode_list.size()?remoteNode_list.size():anchor_list.size();
		for(;idx<end;idx++) {
			if(!anchor_list.get(idx).getName().equals(remoteNode_list.get(idx).getName())) {
				//we found the first node which differs
				break;
			}
		}
		
		String path=null;

		//for each no in the anchor list we need to move up the tree.
		for(int i=idx;i<anchor_list.size();i++) {
			if(path==null) {
				path="..";
			}
			else {
				path+=":..";
			}
		}
		//for each node in the remoteNode list we need to add their name to the path
		for(int i=idx;i<remoteNode_list.size();i++) {
			if(path==null) {
				//not sure if this is possible.....
				path=""+remoteNode_list.get(i).getName();
			}
			else {
				path+=":"+remoteNode_list.get(i).getName();
			}
		}
		//jprime.Console.out.println("\t** the path is "+path);
		return path;
	}
	
	public boolean hasBeenReplicated() {
		return has_been_replicated;
	}
	
	/**
	 * used to determine if there is a replicate in the args list
	 * @param v
	 * @param n
	 * @return
	 */
	public static boolean __hasReplicate(PyObject[] v, String[] n, boolean isAlaised){
		int diff = v.length-n.length;
		int idx=-1;
		if(diff >= 1) {
			idx=0;
		}
		else if(diff==0){
			for(int i =0;i<n.length;i++) {
				if(n[i].equals("replicate")) {
					idx=i;
					break;
				}
			}
		}
		if(idx>=0 && idx<v.length && v[idx]!=null) {
			try {
				if(isAlaised) {
					ModelNode m = (ModelNode)v[idx].__tojava__(ModelNodeAlias.class);
					if(m != null) {
						return true;
					}
				}
				else {
					ModelNode m = (ModelNode)v[idx].__tojava__(ModelNode.class);
					if(m != null) {
						return true;
					}
				}
			}
			catch (Exception e) {
			}
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see jprime.IModelNode#addAttr(jprime.variable.ModelNodeVariable)
	 */
	public synchronized void addAttr(ModelNodeVariable attr) {
		attr.setOwner(this);
		this.attrs.put(attr.getDBName(), attr);
		modified(Modified.ATTRS);
	}
	
	/* (non-Javadoc)
	 * @see jprime.IModelNode#evaluateResourceID(jprime.ResourceIdentifier.ResourceID, int, int, jprime.ResourceIdentifier.ResourceID.EvalutedResourceID)
	 */
	public void evaluateResourceID(ResourceID resourceid, int list_idx, int term_idx, EvalutedResourceID result) {
		List<Term> terms=resourceid.getTerms().get(list_idx);
		if(term_idx>=terms.size()) {
			//XXX throw new RuntimeException("Invalid resource identifier!");
			return;
		}
		Term t =terms.get(term_idx);
		Term n = (term_idx+1<terms.size())?terms.get(term_idx+1):null;
		//jprime.Console.out.println("{RI}Evaluting rid("+terms+") at "+getUniqueName()+", t="+t+", n="+n);
		switch(t.getScope()) {
		case DOWN:
			if(term_idx==0) {
				//this is an absolute address
				if(this.getParent()==null) {
					//im at the top
					//fall down to the NONE case
				}
				else {
					this.getParent().evaluateResourceID(resourceid,list_idx,term_idx,result);
					return;
				}
			}
		case SUB_ATTR: //in this context we look at sub-attr as '.' like a filesystem
		case NONE:
		{
			boolean matched=false;
			if(t.isClassWildcard()) {
				String kls=this.getClass().getSimpleName();
				matched=kls.equals(t.getValue()) || kls.equals(t.getValue()+"Replica");
			}
			else if(t.getScope()==TermScope.SUB_ATTR) {
				if(term_idx>0) {
					if(terms.get(term_idx).getScope()==TermScope.ATTR || terms.get(term_idx).getScope()==TermScope.SUB_ATTR) {
						throw new RuntimeException("Should never see this!");
					}
				}
				matched=true;
			}
			else {
				matched=getName().equals(t.getValue());
			}
			if(matched) {
				if(null != n) {
					if(n.getScope() == TermScope.ATTR) {
						//im what they want
						//jprime.Console.out.println("\t1matched "+this.getUniqueName());
						result.add(this);
						return;
					}
					else {
						//else one or more of my kids it wants
						//send it down to the kids
						for(ModelNode c : getAllChildren()) {
							c.evaluateResourceID(resourceid, list_idx,term_idx+1, result);
						}
					}
				}
				else {
					//jprime.Console.out.println("\t2matched "+this.getUniqueName());
					//im what they want
					result.add(this);
					return;
				}
			}
			//else i don't match do none of my kids could either!
		}
			break;
		case UP:
			if(getParent()!=null) {
				this.getParent().evaluateResourceID(resourceid,list_idx,term_idx+1,result);
			}
			else {
				throw new RuntimeException("Invalid path:"+result.getResourceID().toString());
			}
			break;
		case ATTR:
			try {
				int dbname=ModelNodeVariable.name2int(t.getValue());
				ModelNodeVariable v =this.getAttributeByName(dbname);
				if(v == null) {
					if(ModelNodeVariable.hasDefaultValue(this,dbname)) {
						throw new RuntimeException("dont handle default values yet!");
					}
					throw new RuntimeException(result.getResourceID().toString()+" referes to an valid attribute. "+
							t.getValue()+" is not a valid attribute for the type "+getClass().getSimpleName()+"!");
				}
				//jprime.Console.out.println("\t3matched "+this.getUniqueName());
				result.add(v,this);
			}
			catch (NoSuchElementException e) {
				throw new RuntimeException(result.getResourceID().toString()+" referes to an valid attribute. "+
						t.getValue()+" is an invalid attribute name!");
			}
			break;
		case RECURSE_DOWN:
			//XXX
			throw new RuntimeException("XXX not done");
			//break;
		default:
			throw new RuntimeException("Unexcepted scope type "+t.getScope());
		}
	}

	
	public IModelNode copy(String name, IModelNode parent) {
		ModelNode rv = (ModelNode) __copy(name,this,parent);
		((ModelNode)parent).addChild(rv);
		return rv;
	}
	
	public IModelNode __copy(String name, IModelNode toCopy, IModelNode parent) {
		final String type = toCopy.getClass().getSimpleName().replace(".class","").replace("Replica","");
		final ModelNode rv = (ModelNode)ModelNode.createModelNode(type, parent, name);
		for(ModelNodeVariable v: toCopy.getAllAttributeValues()) {
			if(v instanceof SymbolVariable) {
				SymbolVariable vv = new SymbolVariable(((SymbolVariable)v).getName());
				vv.attachToNode(rv, v.getDBName());
				vv.setOwner(rv);
				rv.attrs.put(v.getDBName(),vv);
			}
			else {
				try {
					Class<? extends jprime.gen.ModelNodeVariable > cls = v.getClass();
					ModelNodeVariable vv=(jprime.variable.ModelNodeVariable)cls.newInstance();
					vv.setDBName(v.getDBName());
					vv.setOwner(rv);
					rv.attrs.put(v.getDBName(),vv);
					vv.setValueAsString(v.toString());
				} catch (InstantiationException e) {
					throw new RuntimeException(e);
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				}
			}
		}
		if(toCopy instanceof ModelNodeAliasReplica)
			toCopy=((ModelNodeAliasReplica)toCopy).getReplicatedNode();
		if(this instanceof IInterfaceAlias) {
			IInterface i = (IInterface)((IInterfaceAlias)rv).deference();
			i.setAttachedLink((ILink)parent);
		}
		ArrayList<IModelNode> later=new ArrayList<IModelNode>();
		for(IModelNode c : toCopy.getAllChildren()) {
			if(c instanceof IRoutingSphere)continue; //only nets have these and they automatically create them!
			if(c instanceof IVizAggregate)continue; //only nets have these and they automatically create them!
			if(c instanceof ModelNodeAlias || c instanceof ModelNodeAliasReplica || c instanceof ILink) {
				later.add(c);
			}
			else {
				rv.addChild((ModelNode)c.__copy(c.getName(), c, rv));
			}
		}
		for(IModelNode c : later) {
			rv.addChild((ModelNode)c.__copy(c.getName(), c, rv));
		}
		return rv;
	}
	
	protected void deleteChild(ModelNode kid) {
		if(has_been_replicated)
			throw new RuntimeException("Cannot delete a child from a node which has been replicated!");
		this.children.remove(new Key(kid));
		modified(Modified.CHILDREN);
	}
	
	protected void delete_extra() {
		//if the node has extra work to do....
	}
	
	protected void delete_recurse() {
		/* $if SEPARATE_PROP_TABLE $
		for(ModelNodeVariable a : attrs.values()) {
			a.orphan();
		}
		$endif$ */
		for(ModelNode c: getAllChildren()) {
			c.delete_recurse();
		}
		children.clear();
		delete_extra();
		this.db_parent_id=0;
		this.__parent=null;
		this.orphan();
	}
	
	public void delete() {
		if(has_been_replicated)
			throw new RuntimeException("Cannot delete a node which has been replicated!");
		final ModelNode parent = ((ModelNode)getParent());
		if(parent != null)
			parent.deleteChild(this);
		delete_recurse();
	}
	
	/* (non-Javadoc)
	 * @see jprime.PersistableObject#orphan()
	 */
	public void orphan() {
		switch(this.persistable_state) {
		case UNMODIFIED:
		case MODIFIED:
			this.persistable_state=PersistableState.DEAD;
			modified(Modified.ALL);
			break;
		case NEW:
			this.persistable_state=PersistableState.ORPHAN;
			modified(Modified.ALL);
			break;
		case DEAD:
			throw new RuntimeException("does this happen? nate things this should be a no-op like orphan,  but he is not sure yet....");
		case ORPHAN:
			//no op
		}
	}
	
	/* (non-Javadoc)
	 * @see jprime.IModelNode#getRow()
	 */
	public int getRow(){
		return this.prefuse_row;
	}
	
	/* (non-Javadoc)
	 * @see jprime.IModelNode#setRow(int)
	 */
	public void setRow(int r){
		this.prefuse_row = r;
	}
	
	/* (non-Javadoc)
	 * @see jprime.IModelNode#getHub()
	 */
	public boolean getHub(){
		return this.prefuse_is_hub;
	}
	
	/* (non-Javadoc)
	 * @see jprime.IModelNode#setHub(boolean)
	 */
	public void setHub(boolean h){
		this.prefuse_is_hub = h;
	}
	
	/* (non-Javadoc)
	 * @see jprime.IModelNode#updateLocation(jprime.IModelNode.PrefuseLocation)
	 */
	public PrefuseLocation updateLocation(PrefuseLocation loc) {
		PrefuseLocation rv = prefuse_loc;
		if(loc != null) prefuse_loc = loc;
		return rv;
	}

	
	public static List<String> getHelpHeader() {
		ArrayList<String> rv = new ArrayList<String>();
		rv.add("************************************************************************");
		rv.add("************************************************************************");
		rv.add("Methods to create models nodes (Hosts, etc) come in the following forms:");
		rv.add("   create*()");
		rv.add("      Create child of type * with a generated name.");
		rv.add("   create*(String n)");
		rv.add("      Create child of type * with the name 'n'.");
		rv.add("   create*Replica(I* base)");
		rv.add("      Create child of type * with replicates base");
		rv.add("      with a generated name.");
		rv.add("   create*Replica(String n, I* base)");
		rv.add("      Create child of type * with replicates base");
		rv.add("      with the name 'n'.");
		rv.add("   new*(PyObject[], String[])");
		rv.add("      A python function to create children of type *.");
		rv.add("      Named parameters can be used to configure the child.");
		rv.add("   replicate*(PyObject[], String[])");
		rv.add("      A python function to create children which replicate *.");
		rv.add("      Named parameters to be used to configure the child.");
		rv.add("************************************************************************");
		rv.add("************************************************************************");
		return rv;
	}
	public List<String> help() {
		ArrayList<String> rv = new ArrayList<String>();
		rv.add("Common Functions:");
		rv.add("   getUID()\n"
			  +"      Get the UID of the node.");
		rv.add("   getName()\n"
			  +"      Get the name of the node.");
		rv.add("   getUniqueName()\n"
			  +"      Get the full name of the node.");
		rv.add("   getParent()\n"
			  +"      Get the parent of this node.");
		rv.add("   hasBeenReplicated()\n"
				  +"      If a node has been replicated one can no longer\n"
				  +"      structurally modify the node.");
		rv.add("   get(String cName)\n"
			  +"      Get the child with name cName.\n"+
				"      cName can specify children many levels deep by using '.'\n"+
				"      to join the names, i.e. foo.get(\"child1.child2.child3\")");
		rv.add("   getAllChildren()\n"
			  +"      Get a list of all direct children of this node.");
		rv.add("   getAttributes()\n"
				  +"      Get all of the the attributes of this node.");
		rv.add("   copy(String name, IModelNode parent)\n"
				  +"      Create a new node under parent which is a full copy of this node.");
		/*rv.add("   getAttributes(boolean inc)\n"
			  +"      Get the attributes of this node.\n"+
				"      If inc is True then possible attrs which are not set\n"+
				"      will be shown with their default values.");
		rv.add("   deference()\n"
			  +"      If this node is an Alias (i.e. a link attachment)\n"+
				"      Then this retruns the model node this if an alias of.\n"+
				"      Otherwise, is returns this object.");*/
		return rv;
	}
	
	/**
	 * Create a model node of type 'type' with name 'name' as a child of 'parent'
	 * @param type the type of model node to create
	 * @param parent the node which to create the node under
	 * @param name the name of the model node
	 * @return the newly created model node
	 */
	public static IModelNode createModelNode(String type, IModelNode parent, String name) {
		IModelNode rv = null;
		try {
			Class<?> c= Class.forName("jprime."+type.replace("Alias","")+"."+type);
			Class<?>[] argsClass = new Class<?>[] { IModelNode.class };
			Object[] args = new Object[] { parent };
			try {
				Constructor<?> ctor = c.getConstructor(argsClass);
				rv = (IModelNode)ctor.newInstance(args);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			} catch (InstantiationException e) {
				throw new RuntimeException(e);
			} catch (IllegalArgumentException e) {
				throw new RuntimeException(e);
			} catch (InvocationTargetException e) {
				throw new RuntimeException(e);
			} catch (SecurityException e) {
				throw new RuntimeException(e);
			} catch (NoSuchMethodException e) {
				throw new RuntimeException(e);
			}
		} catch (ClassNotFoundException e) {
			try {
				Class<?> c = Class.forName("jprime.routing."+type);
				Class<?>[] argsClass = new Class<?>[] { INet.class };
				Object[] args = new Object[] { (INet)parent };
				try {
					Constructor<?> ctor = c.getConstructor(argsClass);
					rv = (IModelNode)ctor.newInstance(args);
				} catch (IllegalAccessException e1) {
					throw new RuntimeException(e1);
				} catch (InstantiationException e1) {
					throw new RuntimeException(e1);
				} catch (IllegalArgumentException e1) {
					throw new RuntimeException(e1);
				} catch (InvocationTargetException e1) {
					throw new RuntimeException(e1);
				} catch (SecurityException e1) {
					throw new RuntimeException(e1);
				} catch (NoSuchMethodException e1) {
					throw new RuntimeException(e1);
				}
			} catch (ClassNotFoundException e1) {
				throw new RuntimeException(e1);
			}		
		}
		if(rv instanceof ModelNode) {
			((ModelNode)rv).setName(name);
			//jprime.Console.out.println("\t\tCreated node "+rv.getUniqueName());
		}
		return rv;
	}
}
