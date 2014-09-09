
package jprime.CNFTransport;
import java.util.ArrayList;

import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class CNFTransport extends jprime.gen.CNFTransport implements jprime.CNFTransport.ICNFTransport {
	private ArrayList<Long> cnf_rids = null;
	
	public CNFTransport(PyObject[] v, String[] s){super(v,s);}
	public CNFTransport(ModelNodeRecord rec){ super(rec); }
	public CNFTransport(jprime.IModelNode parent){ super(parent); }

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.CNFTransport;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
	
	
	public void addCNFRouter(long rid) {
		if(cnf_rids == null) {
			if(this.getCnfRouters()== null)
			cnf_rids = new ArrayList<Long>();
			else 
			cnf_rids = decodeCNFRouterVector(this.getCnfRouters().toString());
		}
		cnf_rids.add(rid);
		try {
			this.setAttribute(jprime.gen.ModelNodeVariable.cnf_routers(), CNFTransport.encodeCNFRouterVector(cnf_rids));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public ArrayList<Long> getCNFRouterIds() {
		if(cnf_rids == null) {
			if(this.getCnfRouters() == null){
				return null;
				}
			cnf_rids = decodeCNFRouterVector(this.getCnfRouters().toString());
		}
		return cnf_rids;
	}

	/**
	 * Transform a vector of CNFRouters rid to a string
	 * @param s
	 * @return
	 */
	public static ArrayList<Long> decodeCNFRouterVector(String s) {
		ArrayList<Long> rv = new ArrayList<Long>();
		if(s == ""){
			rv = null;
		}
		String mydelimiter = "\\[|,|\\]";
		String[] temp=s.replaceFirst("^"+mydelimiter,"").split(mydelimiter);
		for(String l : temp) {
			rv.add(Long.parseLong(l));
		}
		return rv;		
	}
	
	/**
	 * Transform a string to a vector of CNFRouters' rid
	 * @param a
	 * @return
	 */
	public static String encodeCNFRouterVector(ArrayList<Long> a) {
		String rv =null;
		for(Long l : a) {
			if(rv == null) rv = "[";
			else rv+=",";
			rv+=l;
		}
		return rv+"]";
	}
	
	/**
	 * test for decode and encode RouterVector functions
	 */
	public static void main(String args[]) throws Exception {
		String test = "[1,5000,2,3000,4,200]";
		ArrayList<Long> rv = new ArrayList<Long>();
		rv = decodeCNFRouterVector(test);
		for(Long l: rv){
			System.out.println(l);
		}
		
		String str = encodeCNFRouterVector(rv);
		System.out.println("The output string is :" + str);

	}
	
}
