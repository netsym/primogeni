
package jprime.CNFTransport;

import java.util.ArrayList;
import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class CNFTransportAliasReplica extends jprime.gen.CNFTransportAliasReplica implements jprime.CNFTransport.ICNFTransportAlias {
	public CNFTransportAliasReplica(ModelNodeRecord rec){ super(rec); }
	public CNFTransportAliasReplica(PyObject[] v, String[] s){super(v,s);}
	public CNFTransportAliasReplica(String name, IModelNode parent, jprime.ModelNodeAlias referencedNode) {
		super(name, parent,referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.CNFTransportAliasReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}

	public void addCNFRouter(long rid) {
		((ICNFTransport)deference()).addCNFRouter(rid);
	}
	
	public ArrayList<Long> getCNFRouterIds() {
		return ((ICNFTransport)deference()).getCNFRouterIds();
	}
}
