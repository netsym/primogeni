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
public abstract class CentralizedTrafficTypeReplica extends jprime.DynamicTrafficType.DynamicTrafficTypeReplica implements jprime.gen.ICentralizedTrafficType {

	/* used to enforce the minimum/maximum child requirement */

	public void enforceChildConstraints() {
		super.enforceChildConstraints();

	}
	public CentralizedTrafficTypeReplica(String name, IModelNode parent, jprime.CentralizedTrafficType.ICentralizedTrafficType referencedNode) {
		super(name,parent,referencedNode);
	}
	public CentralizedTrafficTypeReplica(ModelNodeRecord rec){ super(rec); }
	public CentralizedTrafficTypeReplica(PyObject[] v, String[] s){super(v,s);}
	/**
	 * @return the interface which this node implements
	 */
	public Class<?> getNodeType() {
		return jprime.CentralizedTrafficType.ICentralizedTrafficType.class;
	}
	/**
	 * @param used by replicas to do a deep copy of the node.
	 */
	public jprime.ModelNode deepCopy(jprime.ModelNode parent) {
		doing_deep_copy=true;
		jprime.CentralizedTrafficType.CentralizedTrafficTypeReplica c = new jprime.CentralizedTrafficType.CentralizedTrafficTypeReplica(this.getName(), (IModelNode)parent,(jprime.CentralizedTrafficType.ICentralizedTrafficType)this);
		doing_deep_copy=false;
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1134: //CentralizedTrafficTypeReplica
			case 1135: //SwingTCPTrafficReplica
			case 1190: //CentralizedTrafficTypeAliasReplica
			case 1191: //SwingTCPTrafficAliasReplica
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public abstract int getTypeId();

	/**
	 * @return XXX
	 */
	public ResourceIdentifierVariable getSrcs() {
		ResourceIdentifierVariable temp = (ResourceIdentifierVariable)getAttributeByName(ModelNodeVariable.srcs());
		if(null!=temp) return temp;
		return (ResourceIdentifierVariable)this.getReplicatedNode().getAttributeByName(ModelNodeVariable.srcs());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setSrcs(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.srcs());
		if(temp==null){
			temp=new ResourceIdentifierVariable(jprime.gen.ModelNodeVariable.srcs(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof ResourceIdentifierVariable)){
				temp=new ResourceIdentifierVariable(jprime.gen.ModelNodeVariable.srcs(),value);
				addAttr(temp);
			}
			else { ((ResourceIdentifierVariable)temp).setValue(value); }
		}
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setSrcs(jprime.ResourceIdentifier.ResourceID value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.srcs());
		if(temp==null){
			temp=new ResourceIdentifierVariable(jprime.gen.ModelNodeVariable.srcs(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof ResourceIdentifierVariable)){
				temp=new ResourceIdentifierVariable(jprime.gen.ModelNodeVariable.srcs(),value);
				addAttr(temp);
			}
			else { ((ResourceIdentifierVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setSrcs(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.srcs());
		addAttr(value);
	}

	/**
	 * @return XXX
	 */
	public ResourceIdentifierVariable getDsts() {
		ResourceIdentifierVariable temp = (ResourceIdentifierVariable)getAttributeByName(ModelNodeVariable.dsts());
		if(null!=temp) return temp;
		return (ResourceIdentifierVariable)this.getReplicatedNode().getAttributeByName(ModelNodeVariable.dsts());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setDsts(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.dsts());
		if(temp==null){
			temp=new ResourceIdentifierVariable(jprime.gen.ModelNodeVariable.dsts(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof ResourceIdentifierVariable)){
				temp=new ResourceIdentifierVariable(jprime.gen.ModelNodeVariable.dsts(),value);
				addAttr(temp);
			}
			else { ((ResourceIdentifierVariable)temp).setValue(value); }
		}
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setDsts(jprime.ResourceIdentifier.ResourceID value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.dsts());
		if(temp==null){
			temp=new ResourceIdentifierVariable(jprime.gen.ModelNodeVariable.dsts(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof ResourceIdentifierVariable)){
				temp=new ResourceIdentifierVariable(jprime.gen.ModelNodeVariable.dsts(),value);
				addAttr(temp);
			}
			else { ((ResourceIdentifierVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setDsts(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.dsts());
		addAttr(value);
	}

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds() {
		return jprime.gen.CentralizedTrafficType.attrIds;
	}

	/**
	 * @param visitor a generic visitor
	 */
	public abstract void accept(jprime.visitors.IGenericVisitor visitor);
}
