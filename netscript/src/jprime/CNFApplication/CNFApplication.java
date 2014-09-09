
package jprime.CNFApplication;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import jprime.ModelNodeRecord;
import jprime.variable.ModelNodeVariable;

import org.python.core.PyObject;


public class CNFApplication extends jprime.gen.CNFApplication implements jprime.CNFApplication.ICNFApplication {
	//transient
	private HashMap<Integer, Integer> contents = null;
	
	public CNFApplication(PyObject[] v, String[] s){super(v,s);}
	public CNFApplication(ModelNodeRecord rec){ super(rec); }
	public CNFApplication(jprime.IModelNode parent){ super(parent); }
	

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	
	public int getTypeId(){ return jprime.EntityFactory.CNFApplication;}
	
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
	
	
	
	public void addContentId(int cid, int size) {
		if(contents == null) {
			if (this.getCnfContentSizes() == null)
				contents = new HashMap<Integer, Integer>();
			else
				contents = decodeContentMap(this.getCnfContentSizes().toString());
		}	
		contents.put(cid, size);
		try {
			this.setAttribute(ModelNodeVariable.cnf_content_sizes(), CNFApplication.encodeContentMap(contents));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public HashMap<Integer,Integer> getContentIds() {
		if(contents == null) {
			if(this.getCnfContentSizes() == null)
				return null;
			contents = decodeContentMap(this.getCnfContentSizes().toString());
		}
		return contents;
	}

	/**
	 * Transform a string into a contentMap
	 * @param s
	 * @return
	 */
	public static HashMap<Integer, Integer> decodeContentMap(String s) {
		HashMap<Integer, Integer> rv = new HashMap<Integer, Integer>();
		if(s == ""){
			rv = null;
			
		}
		String mydelimiter = "\\[|,|\\]";
		String[] temp=s.replaceFirst("^"+mydelimiter,"").split(mydelimiter);
		for(int i=0;i<temp.length;i=i+2) {
			rv.put(Integer.parseInt(temp[i]),Integer.parseInt(temp[i+1]));
		}
		return rv;
	}

	/**
	 * Transform a contentMap into a string
	 * @param c
	 * @return
	 */
	public static String encodeContentMap(HashMap<Integer, Integer> c) {
		String rv =null;
		for(Entry<Integer, Integer> e : c.entrySet()) {
			if(rv == null) rv ="[";
			else rv+=",";
			rv+=e.getKey()+","+e.getValue();
		}
		return rv+"]";
	}
	
	/**
	 * Test for decode and encode ContentMap function
	 */
	public static void main(String[] args) throws IOException {
		String test = "[1,5000,2,3000,4,200]";
		HashMap<Integer,Integer> hm = decodeContentMap(test);
		for(Entry<Integer, Integer> me : hm.entrySet()) {
			System.out.print(me.getKey() + ": "); 
			System.out.println(me.getValue()); 
		} 
		String str = encodeContentMap(hm);
		System.out.println("The output string is :" + str);
	}

}
