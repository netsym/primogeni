package jprime.ResourceIdentifier;

import java.io.Serializable;
import java.util.TreeMap;

import jprime.ModelNode;
import jprime.PersistableObject;
import jprime.variable.ModelNodeVariable;

public class EvalutedResourceID implements Serializable {
	private static final long serialVersionUID = -1761668876718422039L;
	
	private final TreeMap<Long,PersistableObject> objs;
	private int attr_name;
	private Class<?> type;
	private ResourceID rid;
	public EvalutedResourceID(ResourceID rid) {
		super();
		this.rid = rid;
		this.attr_name=-1;
		this.type=null;
		this.objs = new TreeMap<Long, PersistableObject>();
	}
	public void add(ModelNode m) {
		if(attr_name==-1 && type==null) {
			if(objs.size()==0) {
				type=m.getNodeType();
				if(!filter(m))
					objs.put(m.getDBID(),m);
				return;
			}
			else {
				throw new RuntimeException("Should never see this!");
			}
		}
		else if(type!=null) {
			if(type.isInstance(m)) {
				if(!filter(m))
					objs.put(m.getDBID(),m);
				return;
			}
			//say we are adding hosts and routers... if the router was added first it would
			//exclude all hosts... we need to check for this type of inversion
			//in this case type would be IRouter and m.getNodeType() would be Host
			try {
				if(null != type.asSubclass(m.getNodeType())) {
					type=m.getNodeType();
					if(!filter(m))
						objs.put(m.getDBID(),m);
					return;
				}
			}
			catch(ClassCastException e) {
			}
			throw new RuntimeException("The model node "+m.getUniqueName()+" is of type "+
					m.getClass().getSimpleName()+" which is not compatible with the other "+
					"nodes in the result set which are of type "+type.getSimpleName());
		}
		else {
			throw new RuntimeException("The result set is made up attributes but you tried to add a model node to it!");
		}
	}
	public void add(ModelNodeVariable v, ModelNode owner) {
		if(attr_name==-1 && type==null) {
			if(objs.size()==0) {
				attr_name=v.getDBName();
				if(!filter(owner))
					objs.put(owner.getDBID(),owner);
			}
			else {
				throw new RuntimeException("Should never see this!");
			}
		}
		else if(attr_name!=-1) {
			if(v.getDBName() == attr_name) {
				if(!filter(owner))
					objs.put(owner.getDBID(),owner);
			}
			else {
				throw new RuntimeException("The result set consists of attributes by the name "+
						ModelNodeVariable.int2name(attr_name)+" but you tried to add an attribute by the name"
						+ModelNodeVariable.int2name(v.getDBName())+"; that is not allowed!");
			}
		}
		else {
			throw new RuntimeException("The result set is made up attributes but you tried to add a model node to it!");
		}
	}
	private boolean filter(ModelNode m) {
		if(this.getResourceID().getFilter()!=null) {
			//XXX
			throw new RuntimeException("we dont support filters yet!");
		}
		return false;
	}
	public ResourceID getResourceID() {
		return rid;
	}
	public int getAttr_name() {
		return attr_name;
	}
	public Class<?> getType() {
		return type;
	}
	public TreeMap<Long,PersistableObject> getObjs() {
		return objs;
	}
	
}