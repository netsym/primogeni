package jprime.ResourceIdentifier;

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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import jprime.ModelNode;
import jprime.PersistableObject;
import jprime.visitors.TLVVisitor;
import jprime.visitors.TLVVisitor.TLVType;



/**
 * @author Nathanael Van Vorst
 * 
 * A Resource Identifier specifies a set of Model Entities which can be filtered.
 * The ResourceID consists of two parts:
 * 
 * 1) a path; the path is constructed by a list of terms (which may be wild-cards)
 * 2) a filter; the filter reduces the set of elements specified by the path
 * 
 * We can pre-evaluate a ResourceID at a compile time OR allow the ResourceID to be
 * evaluate at run-time. 
 * 
 */
public class ResourceID implements Serializable {
	
	private static final long serialVersionUID = -8104027538957270486L;
	//the paths
	private List<List<Term>> terms;
	//the filter
	private Filter filter;
	//if the string is pre-evaluated then we will store it here as to avoid re-evaluating it
	private String compiledStr;
	//used to cache the anchor of the compiled string
	private String anchor_path;
	
	/**
	 * For internal use
	 */
	protected ResourceID() {
		super();
		this.terms=null;
		this.filter=null;
		this.compiledStr=null;
		this.anchor_path=null;
	}
	
	/**
	 * Create an RI from a string
	 * 
	 * @param a RI path
	 */
	public ResourceID(String str) {
		this(str,false);
	}
	
	/**
	 * Create an RI from a compiled str
	 * 
	 * @param str if isCompiledString then str should be a RI path, otherwise it should be a packed str created using 'compile'
	 * @param isCompiledString what is str
	 */
	public ResourceID(String str, boolean isCompiledString) {
		super();
		this.terms=null;
		this.filter=null;
		this.compiledStr=null;
		if(isCompiledString) {
			this.compiledStr=str;
			throw new RuntimeException("Parse anchor_path out of compiled string!");
			//this.anchor_path=null;
		}
		else {
			int idx=parseString(str,0, false);
			if(idx != str.length()) {
				throw new RuntimeException("Error parsing RI '"+str+"' near "+idx+" '"+str.charAt(idx)+"'"+"!");
			}
			//jprime.Console.out.println("parsed '"+str+"' to "+this.toString());
		}
	}
	

	/**
	 * @param terms
	 * @param filter
	 */
	public ResourceID(List<List<Term>> terms, Filter filter) {
		super();
		this.terms = terms;
		this.filter = filter;
		this.compiledStr=null;
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof ResourceID) {
			if(compiledStr == null && ((ResourceID)obj).compiledStr==null) {
				ResourceID r = (ResourceID)obj;
				if(anchor_path != null && r.anchor_path != null) {
					if(filter != null && r.filter != null) {
						if(terms != null && r.terms != null) {
							if(terms.size()==r.terms.size()) {
								for(int i =0;i<terms.size();i++) {
									final List<Term> l1 = terms.get(i);
									final List<Term> l2 = r.terms.get(i);
									if(l1.size()==l2.size()) {
										for(int j=0;j<l1.size();j++) {
											if(!l1.get(j).equals(l2.get(j))) {
												return false;
											}
										}
									}
									else {
										return false;
									}
								}
								return true;
							}
						}
					}
				}
				return false;
			}
			else if(compiledStr != null && ((ResourceID)obj).compiledStr!=null) {
				return compiledStr.equals(((ResourceID)obj).compiledStr);
			}
			else {
				throw new RuntimeException("What to do?");
			}
		}
		return false;
	}
	
	/**
	 * Evaluate the ResourceID using 'anchor' as the location
	 * in the model from which to start the evaluation
	 * 
	 * @param anchor the node from which to calculate relative ids
	 * @param path_to_anchor the path from where this resource id is embeded to the anchor
	 */
	public void evaluate(ModelNode anchor, String path_to_anchor) {
		if(compiledStr==null) {
			int count=0;
			//jprime.Console.out.println("[RI]\tEvaluating '"+this.toString()+"' from '"+anchor.getUniqueName()+"'");
			//compile the terms/filter into a set of relative ids
			this.anchor_path=path_to_anchor;
			EvalutedResourceID results=new EvalutedResourceID(this);
			for(int i=0;i<terms.size();i++) {
				count+=terms.get(i).size();
				anchor.evaluateResourceID(this,i,0,results);
			}
			if(count==0) {
				//empty set
				compiledStr=TLVVisitor.makeTLV(TLVType.LONG, "2".length(), "2");
				this.terms=null;
				this.filter=null;
			}
			else {
				//pack the result set into a string...
				boolean is_var=results.getType()==null;
				String temp;
				if(is_var) {
					compiledStr=TLVVisitor.makeTLV(TLVType.LONG, "1".length(), "1");
					temp = ""+results.getAttr_name();
					compiledStr+=TLVVisitor.makeTLV(TLVType.LONG, temp.length(), temp);
				}
				else {
					compiledStr=TLVVisitor.makeTLV(TLVType.LONG, "0".length(), "0");
					temp = ""+results.getType().getSimpleName().substring(1);
					compiledStr+=TLVVisitor.makeTLV(TLVType.STRING, temp.length(), temp);
				}
				//jprime.Console.out.println("[RI]\tmatched "+results.getObjs().size()+" objs, is_var="+is_var);
				for(Entry<Long, PersistableObject> e : results.getObjs().entrySet()) {
					PersistableObject n = e.getValue();
					//jprime.Console.out.println("[RI]\t\tmatched "+((ModelNode)n).getUniqueName());
					//jprime.Console.out.println("[RI]\t\t\tCompiled "+((ModelNode)n).getUID()+" to "+
					//		((ModelNode)n).getRank(anchor)+" from "+
					//		anchor.getUniqueName()+"("+anchor.getUID()+", size="+anchor.getSize()+")");
					temp=""+((ModelNode)n).getRank(anchor);
					compiledStr+=TLVVisitor.makeTLV(TLVType.LONG, temp.length(), temp);
				}
				this.terms=null;
				this.filter=null;
			}
		}
		else if(!anchor_path.equals(path_to_anchor)) {
			throw new RuntimeException("Tried to compile the same resource id from different anchors!");
		}
	}
	
	/**
	 * @return the path
	 */
	public List<Term> getTerms(int idx) {
		return terms.get(idx);
	}
	
	/**
	 * @return the path
	 */
	public List<List<Term>> getTerms() {
		return terms;
	}

	/** 
	 * @return the filter
	 */
	public Filter getFilter() {
		return filter;
	}

	/**
	 * @return the evaluated string
	 */
	public String getCompiledStr() {
		return compiledStr;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if(compiledStr == null) {
			if(terms==null)
				return "<Empty RI>";
			String rv = "";
			if(terms.size()>1)
				rv="{";
			for(int i=0;i<terms.size();i++) {
				if(i>0) rv+=",";
				for(Term t : terms.get(i)) {
					rv+=t.toString();
				}
			}
			if(terms.size()>1)
				rv+="}";
			if(filter!=null)
				rv+="["+filter.toString()+"]";
			return rv;
		}
		return compiledStr;
	}
	
	/**
	 * @return
	 */
	public String packToTLV() {
		String temp=null;
		String value=null;
		if(compiledStr==null) {
			int count=0;
			for(int i=0;i<terms.size();i++) {
				count+=terms.get(i).size();
			}
			if(count==0) {
				//empty set
				compiledStr=TLVVisitor.makeTLV(TLVType.LONG, "2".length(), "2");
				this.terms=null;
				this.filter=null;
				value = TLVVisitor.makeTLV(TLVType.LONG,"1".length(),"1");
				value += compiledStr;
			}
			else {
				value = TLVVisitor.makeTLV(TLVType.LONG,"0".length(),"0");
				//encode the # of paths in the set
				temp = ""+terms.size();
				value += TLVVisitor.makeTLV(TLVType.STRING,temp.length(),temp);
				
				//encode the paths
				for(List<Term> l : terms) {
					temp = "";
					for(Term t : l) {
						String temp1=TLVVisitor.makeTLV(TLVType.STRING,t.getScope().toString().length(),t.getScope().toString());
						temp1+=TLVVisitor.makeTLV(TLVType.STRING,t.getValue().length(),t.getValue());
						temp += TLVVisitor.makeTLV(TLVType.LIST,temp1.length(),temp1);
					}
					value += TLVVisitor.makeTLV(TLVType.LIST,temp.length(),temp);
				}
				
				//encode the filter
				if(filter != null)
					value+=filter.packToTLV();
			}
		}
		else{
			value = TLVVisitor.makeTLV(TLVType.LONG,"1".length(),"1");
			value += compiledStr;
		}
		return TLVVisitor.makeTLV(TLVType.RESOURCE_ID,value.length(),value);
	}
	
	/**
	 * @return
	 */
	public String getCompiledString() {
		if(compiledStr==null) return "<null>";
		return compiledStr;
	}
	
	/** 
	 * Used to load an empty resource ID
	 * 
	 * @param str
	 * @param idx
	 * @param called_from_filter
	 * @return
	 */
	protected int parseString(String str, int idx, boolean called_from_filter) {
		if(terms != null || filter != null || compiledStr != null) {
			throw new RuntimeException("Can only parse into an empty ResorceID!");
		}
		if(idx>=str.length()) {
			throw new RuntimeException("invalid idx "+idx);
		}
		terms = new ArrayList<List<Term>>();
		idx=eatWhiteSpace(str, idx);
		boolean is_set=false;
		//jprime.Console.out.println("Parsing RI, str='"+str.substring(idx)+"'");
		if(str.charAt(idx)=='{') {
			is_set=true;
			idx++;
			//jprime.Console.out.println("\t is set, str='"+str.substring(idx)+"'");
		}
		int i;
		while(true) {
			ArrayList<Term> tl=new ArrayList<Term>();
			//jprime.Console.out.println("\tParsing terms, str='"+str.substring(idx)+"'");
			while(true) {
				i=idx;
				Term t = new Term();
				idx=t.parseString(str, idx);
				if(i == idx) {
					break;
				}
				else {
					tl.add(t);
				}
			}
			terms.add(tl);
			//jprime.Console.out.println("\tFinished terms, str='"+str.substring(idx)+"'");
			if(is_set) {
				if(str.charAt(idx)==',') {
					idx++;
				}
				else if(str.charAt(idx)=='}') {
					is_set=true;
					idx++;
					break;
				}
			}
			else {
				break;
			}
		}
		if(!called_from_filter) {
			if(terms.size()==0) {
				throw new RuntimeException("Resource IDs must have atleast one term in the path. '"+str+"' does not!");
			}
			if(idx < str.length()) {
				if(str.charAt(idx)=='[') {
					this.filter=new Filter();
					idx=this.filter.parseString(str, idx);
				}
			}
		}
		//jprime.Console.out.println("\tterms="+terms);
		return idx;
	}
	
	/**
	 * Consume white space chatacters
	 * 
	 * @param str
	 * @param idx
	 * @return the new idx
	 */
	protected static int eatWhiteSpace(String str, int idx) {
		while(idx<str.length() && Character.isWhitespace(str.charAt(idx))) {
			idx++;
		}
		return idx;
	}

	/**
	 * Used for testing
	 * @param args
	 */
	public static void main(String[] args) {
		//test the parsing of Resource IDs
		
		String[] good_strs = {
				":topnet:net1",
				":topnet:net1:net3:h1:if0",
				":topnet:net1:net3:h1:if0@ip_address",
				"..:h1:if0@ip_address",
				".:h1:if0@ip_address",
				"{.:host1,:topnet:?Host}",
				"{.:host1,:topnet:?Host}[a > 0 && b > 1]",
				"{host1:if0@ip_address,host3:if0@ip_address}",
				"{host1:if0@ip_address}",
				"h1:if0@ip_address",
				":topnet:?Net",
				":topnet:?Net:?Net:?Host",
				":topnet//?Host",
				":topnet//?Net:h1",
				":topnet:net1//?Host",
				":topnet:net1//?Host:?Interface@ip_address",
				":topnet:net1//?Host[?Interface@bandwidth>100e6]",
				":topnet//?Host[a && b || c&&d>100e6]",
				":topnet//?Host[a && (e || f) || c&&d>100e6]",
				":topnet//?Host[a && (e || f) && c&&d>100e6]",
				":topnet:net1//?Host@pkts[snaplen=64]",
				":topnet:net1//?Host:?Interface@pkts[?Interface@bandwdith>100e6 && ?Interface@pkts.snaplen=64 && ?Interface@pkts.BPF=\"ip[9]=0x11\"]",
				":topnet:net1//?Host:?Interface@pkts[?Interface@bandwdith>100e6 && ?Interface@pkts.snaplen=64 && ?Interface@pkts.BPF=\"an escaped string-\\\"the string\\\"-\"]"
		};
		
		for(String gs: good_strs) {
			jprime.Console.out.println("*************************");
			jprime.Console.out.println("parsing '"+gs+"'");
			//try {
				ResourceID ri=new ResourceID(gs);
				jprime.Console.out.println("\t->'"+ri.toString()+"'");
			/*}
			catch(Exception e) {
				jprime.Console.out.println("\tError parsing\n"+e);
				e.printStackTrace(jprime.Console.out);
			}*/
		}
	}	
}
