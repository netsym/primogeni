
package jprime.CNFTransport;

import java.util.ArrayList;
import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class CNFTransportAlias extends jprime.gen.CNFTransportAlias implements jprime.CNFTransport.ICNFTransportAlias {
	public CNFTransportAlias(ModelNodeRecord rec){ super(rec); }
	public CNFTransportAlias(PyObject[] v, String[] s){super(v,s);}
	public CNFTransportAlias(IModelNode parent){
		super(parent);
	}
	public CNFTransportAlias(IModelNode parent, jprime.CNFTransport.ICNFTransport referencedNode) {
		super(parent,(jprime.CNFTransport.ICNFTransport)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.CNFTransportAlias;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
	public void addCNFRouter(long rid) {
		((ICNFTransport)deference()).addCNFRouter(rid);
	}
	
	public ArrayList<Long> getCNFRouterIds() {
		return ((ICNFTransport)deference()).getCNFRouterIds();
	}

}
