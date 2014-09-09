
package jprime.TrafficType;

import jprime.ModelNodeRecord;
import jprime.State;
import jprime.variable.OpaqueVariable;
import jprime.variable.ResourceIdentifierVariable;

import org.python.core.PyObject;
public class TrafficType extends jprime.gen.TrafficType implements jprime.TrafficType.ITrafficType {
	public TrafficType(PyObject[] v, String[] n){super(v,n);}
	public TrafficType(ModelNodeRecord rec){ super(rec); }
	public TrafficType(jprime.IModelNode parent){
		super(parent);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.TrafficType;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
	
	/**
	 * @return A comma separated list of the ids of the communities in this partition
	 */
	public final OpaqueVariable getCommunityIds() {
		throw new RuntimeException("Don't get the community ids for traffic -- they are not available until TLV encoding!");
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public final void setCommunityIds(String value) {
		if(getMetadata().getState().lte(State.COMPILING)) {
			throw new RuntimeException("Don't set the community ids for traffic -- its done during TLV encoding!");
		}
		super.setCommunityIds(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public final void setCommunityIds(jprime.variable.SymbolVariable value) {
		if(getMetadata().getState().lte(State.COMPILING)) {
			throw new RuntimeException("Don't set the community ids for traffic -- its done during TLV encoding!");
		}
		super.setCommunityIds(value);
	}

	public ResourceIdentifierVariable getSrcs() {
		throw new RuntimeException("should be over ridden by child type!");
	}
	public ResourceIdentifierVariable getDsts() {
		throw new RuntimeException("should be over ridden by child type!");
	}

}
