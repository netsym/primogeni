
package jprime.TrafficType;

import jprime.IModelNode;
import jprime.ModelNodeRecord;
import jprime.variable.OpaqueVariable;
import jprime.variable.ResourceIdentifierVariable;

import org.python.core.PyObject;
public class TrafficTypeReplica extends jprime.gen.TrafficTypeReplica implements jprime.TrafficType.ITrafficType {
	public TrafficTypeReplica(PyObject[] v, String[] n){super(v,n);}
	public TrafficTypeReplica(ModelNodeRecord rec){ super(rec); }
	public TrafficTypeReplica(String name, IModelNode parent, jprime.TrafficType.ITrafficType referencedNode) {
		super(name, parent,(jprime.TrafficType.ITrafficType)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.TrafficTypeReplica;}
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
		throw new RuntimeException("Don't set the community ids for traffic -- its done during TLV encoding!");
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public final void setCommunityIds(jprime.variable.SymbolVariable value) {
		throw new RuntimeException("Don't set the community ids for traffic -- its done during TLV encoding!");
	}
	public ResourceIdentifierVariable getSrcs() {
		throw new RuntimeException("should be over ridden by child type!");
	}
	public ResourceIdentifierVariable getDsts() {
		throw new RuntimeException("should be over ridden by child type!");
	}
}
