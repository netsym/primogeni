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
import java.util.Stack;

import jprime.ResourceIdentifier.Term.TermScope;
import jprime.visitors.TLVVisitor;
import jprime.visitors.TLVVisitor.TLVType;

/**
 * @author Nathanael Van Vorst
 * 
 * this class is used to represent a filter for a resource identifier
 *
 */
public class Filter implements Serializable {
	private static final long serialVersionUID = 1435879361319433653L;

	/**
	 * @author Nathanael Van Vorst
	 *
	 * The different type of operations available in a filter
	 */
	public static enum FilterOp {
		AND("&&"),
		OR("||"),
		SET("="),
		GT(">"),
		LT("<"),
		GTE(">="),
		LTE("<="),
		EQ("=="),
		NE("!="),
		VALUE("");
		public final String str;
		FilterOp(String str) {
			this.str=str;
		}
		public String toString() {
			return str;
		}
	}
	//the left operand; if op is VALUE then this is a resouce id otherwise its a filter
	private Object left;
	//the op
	private FilterOp op;
	//the right operand
	private Filter right;
	
	/**
	 * used internally
	 */
	protected Filter() {
		super();
		this.left = null;
		this.op = null;
		this.right = null;
	}
	
	/**
	 * @param left
	 * @param op
	 * @param right
	 */
	public Filter(Filter left, FilterOp op, Filter right) {
		super();
		this.left = left;
		this.op = op;
		this.right = right;
	}
	
	/**
	 * @param value
	 */
	public Filter(ResourceID value) {
		super();
		this.left = value;
		this.op = FilterOp.VALUE;
		this.right = null;
	}
	
	
	/**
	 * @return the ResourceID value if op==FilterOP.VALUE and null otherwise
	 */
	public ResourceID getValue() {
		if(op == FilterOp.VALUE)
			return (ResourceID)left;
		return null;
	}

	/**
	 * @return the left operand as a Filter if op!=FilterOP.VALUE and null otherwise
	 */
	public Filter getLeft() {
		if(op == FilterOp.VALUE)
			return null;
		return (Filter)left;
	}
	
	
	/**
	 * @return get the operation of the filter
	 */
	public FilterOp getOp() {
		return op;
	}
	
	
	/**
	 * @return get the right operand if it e
	 */
	public Filter getRight() {
		return right;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if(op == FilterOp.VALUE) {
			return left.toString();
		}
		return "("+left.toString()+" "+op.str+" "+right.toString()+")";
	}
	
	/**
	 * If this filter was created using the default constructor you can load it with a sub-string 'str' starting at 'idx'
	 * @param str
	 * @param idx
	 * @return
	 */
	public int parseString(String str, int idx) {
		if(left != null) {
			throw new RuntimeException("Can only parse into an empty Filter!");
		}
		//if(idx<str.length()) jprime.Console.out.println("Parsing Filter:"+str.substring(idx));
		idx=ResourceID.eatWhiteSpace(str, idx);
		if(idx>=str.length() || str.charAt(idx)!='[') {
			throw new RuntimeException("Error parsing resource id '"+str+"'near character "+idx+" '"+str.charAt(idx)+"'"+"!");
		}
		else {
			idx++;
		}
		ArrayList<Object> toks = new ArrayList<Object>();
		int temp=0;
		do {
			temp=idx;
			
			//eat up white space
			idx=ResourceID.eatWhiteSpace(str, idx);
			if(idx>=str.length()) {
				break;
			}
			
			//try to eat a string
			idx=eatString(toks,str,idx);

			//eat up white space
			idx=ResourceID.eatWhiteSpace(str, idx);
			if(idx>=str.length()) {
				break;
			}
			
			//try eat op
			idx = eatOP(toks, str, idx);
			//eat up white space
			idx=ResourceID.eatWhiteSpace(str, idx);
			if(idx>=str.length()) {
				break;
			}
			
			//try to eat a RI
			idx=eatRI(toks, str, idx);

			//eat up white space
			idx=ResourceID.eatWhiteSpace(str, idx);
			if(idx>=str.length()) {
				break;
			}
		}
		while(temp < idx && idx<str.length());
		
		if(idx>=str.length()) {
			throw new RuntimeException("Error parsing resource id '"+str+"'! ack="+toks+", idx="+idx+", len="+str.length());
		}
		else if(str.charAt(idx) != ']') {
			throw new RuntimeException("Error parsing resource id '"+str+"'near character "+idx+" '"+str.charAt(idx)+"'"+"!");
		}
		idx++;
		
		//we have parsed the filter into a set of toks... now we need to build the tree
		Filter rv = parseFilter(processParens(toks,str),str);
		if(toks.size()!=0) {
			throw new RuntimeException("error parsing '"+str+"', toks="+toks);
		}
		this.left=rv.left;
		this.op=rv.op;
		this.right=rv.right;
		return idx;
	}
	
	/**
	 * Try to parse a quoted string from 'str' starting at idx and add it to toks
	 * 
	 * @param toks
	 * @param str
	 * @param idx
	 * @return the new index
	 */
	private static int eatString(ArrayList<Object> toks, String str, int idx) {
		//jprime.Console.out.println("\n\nBefore eating string idx="+idx+", str='"+str.substring(idx)+"'");
		if(str.charAt(idx) =='"') {
			int i=idx;
			boolean escape=false, stop=false;
			idx++;
			while(!stop && idx<str.length()) {
				switch(str.charAt(idx)) {
				case '\\':
					escape=true;
					break;
				case '"':
					if(!escape) {
						stop=true;
					}
					escape=false;
					break;
				default:
					escape=false;
				}
				idx++;
			}
			if(idx>=str.length()) {
				throw new RuntimeException("Error parsing resource id '"+str);
			}
			toks.add(str.substring(i, idx));
		}
		//jprime.Console.out.println("After eating string idx="+idx+", str='"+str.substring(idx)+"'");
		return idx;
	}

	/**
	 * Try to parse a filter operator from 'str' starting at idx and add it to toks
	 * 
	 * @param toks
	 * @param str
	 * @param idx
	 * @return the new index
	 */
	private static int eatOP(ArrayList<Object> toks, String str, int idx) {
		//try to each an op
		//jprime.Console.out.println("\n\nBefore eating op idx="+idx+", str='"+str.substring(idx)+"'");
		switch(str.charAt(idx)) {
		case '<':
			if(idx+1<str.length() && str.charAt(idx+1)=='=') {
				idx+=2;
				toks.add(FilterOp.LTE);
			}
			else {
				idx++;
				toks.add(FilterOp.LT);
			}
			break;
		case '>':
			if(idx+1<str.length() && str.charAt(idx+1)=='=') {
				idx+=2;
				toks.add(FilterOp.GTE);
			}
			else {
				idx++;
				toks.add(FilterOp.GT);
			}
			break;
		case '=':
			if(idx+1<str.length() && str.charAt(idx+1)=='=') {
				idx+=2;
				toks.add(FilterOp.EQ);
			}
			else {
				idx++;
				toks.add(FilterOp.SET);
			}
			break;
		case '!':
			if(idx+1<str.length() && str.charAt(idx+1)=='=') {
				idx+=2;
				toks.add(FilterOp.NE);
			}
			else {
				throw new RuntimeException("Error parsing resource id '"+str+"'near character "+idx+" '"+str.charAt(idx)+"'"+"!");
			}
			break;
		case '&':
			if(idx+1<str.length() && str.charAt(idx+1)=='&') {
				idx+=2;
				toks.add(FilterOp.AND);
			}
			else {
				throw new RuntimeException("Error parsing resource id '"+str+"'near character "+idx+" '"+str.charAt(idx)+"'"+"!");
			}
			break;
		case '|':
			if(idx+1<str.length() && str.charAt(idx+1)=='|') {
				idx+=2;
				toks.add(FilterOp.OR);
			}
			else {
				throw new RuntimeException("Error parsing resource id '"+str+"'near character "+idx+" '"+str.charAt(idx)+"'"+"!");
			}
			break;
		case '(':
		case ')':
			toks.add(new Character(str.charAt(idx)));
			idx++;
			break;
		default:
			break;
		}
		//jprime.Console.out.println("After eating op idx="+idx+", str='"+str.substring(idx)+"'");
		return idx;
	}

	/**
	 * Try to parse a resource identifier from 'str' starting at idx and add it to toks
	 * 
	 * @param toks
	 * @param str
	 * @param idx
	 * @return the new index
	 */
	private static int eatRI(ArrayList<Object> toks, String str, int idx) {
		//jprime.Console.out.println("\n\nBefore parsing RI idx="+idx+", str='"+str.substring(idx)+"'");
		int i =idx;
		ResourceID t = new ResourceID();
		idx = t.parseString(str, idx, true);
		if(idx>i) {
			toks.add(t);
			//jprime.Console.out.println("\tparsed RI "+t.toString());
			idx=ResourceID.eatWhiteSpace(str, idx);
		}
		//jprime.Console.out.println("After parsing RI idx="+idx+", str='"+str.substring(idx)+"'");
		return idx;
	}
	
	/**
	 * 
	 * groups expressions which are surrounded with parens
	 * 
	 * @param toks
	 * @param str
	 * @return
	 */
	private static ArrayList<Object> processParens(ArrayList<Object> toks, String str) {
		/*
		 * toks contains:
		 *   - ResourceID
		 *   - Character (only left/right parens)
		 *   - String (only the value of a set statement)
		 *   - FilterOp
		 *   - Filter
		 */
		
		ArrayList<Object> newToks=new ArrayList<Object>();
		Stack<ArrayList<Object>> ls = new Stack<ArrayList<Object>>();
		ls.add(newToks);
		while(toks.size()>0) {
			Object o = toks.remove(0);
			if(o instanceof Character) {
				char c = ((Character)o);
				if(c == '(') {
					ls.push(new ArrayList<Object>());
				}
				else if(c==')') {
					if(ls.size()>1) {
						newToks.add(ls.pop());
					}
				}
			}
			else {
				ls.peek().add(o);
			}
		}
		if(ls.size()!=1) {
			throw  new RuntimeException("Parenthis mismatch error with str '"+str+"'");
		}
		return processOPs(newToks,str);
	}

	/**
	 * 
	 * bind >,<,=,etc before and/or
	 * 
	 * @param toks
	 * @param str
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static ArrayList<Object> processOPs(ArrayList<Object> toks, String str) {
		int i=0;
		while(i+2<toks.size()) {
			Object l=toks.remove(i);
			Object op=toks.remove(i);
			Object r=toks.remove(i);
			if(op instanceof FilterOp) {
				switch((FilterOp)op) {
				case AND:
				case OR:
					if(r instanceof ArrayList) {
						toks.add(i,processOPs((ArrayList<Object>)r,str));
					}
					else {
						toks.add(i,r);
					}
					toks.add(i,op);
					if(l instanceof ArrayList) {
						toks.add(i,processOPs((ArrayList<Object>)l,str));
					}
					else {
						toks.add(i,l);
					}
					i++;
					break;
				case EQ:
				case GT:
				case GTE:
				case LT:
				case LTE:
				case NE:
				case SET:
					ArrayList<Object> t=new ArrayList<Object>();
					t.add(l);
					t.add(op);
					t.add(r);
					toks.add(i,t);
					break;
				default:
					throw new RuntimeException("Unexpected filter op "+op);
				}
			}
			else {
				if(r instanceof ArrayList) {
					toks.add(i,processOPs((ArrayList<Object>)r,str));
				}
				else {
					toks.add(i,r);
				}
				if(op instanceof ArrayList) {
					toks.add(i,processOPs((ArrayList<Object>)op,str));
				}
				else {
					toks.add(i,op);
				}
				if(l instanceof ArrayList) {
					toks.add(i,processOPs((ArrayList<Object>)l,str));
				}
				else {
					toks.add(i,l);
				}
				i++;
			}
		}
		return toks;
	}

	
	/**
	 * Group tokens into a filter expression
	 * 
	 * @param toks
	 * @param str
	 * @return return the new filter
	 */
	@SuppressWarnings("unchecked")
	private static Filter parseFilter(ArrayList<Object> toks, String str) {
		/*
		 * toks contains:
		 *   - ResourceID
		 *   - String (only the value of a set statement)
		 *   - FilterOp
		 *   - Filter
		 *   - List<Object>
		 */
		
		if(toks.size()==0){
			return null;
		}
		//jprime.Console.out.println("Parsing filter. toks="+toks);
		Filter left=null,right=null;
		FilterOp op=null;
		Object temp = toks.get(0);
		
		if(temp instanceof ArrayList) {
			left=parseFilter((ArrayList<Object>)temp,str);
			toks.remove(0);
		}
		else if (temp instanceof ResourceID) {
			left = new Filter((ResourceID)temp);
			toks.remove(0);
		}
		else if (temp instanceof String) {
			ArrayList<List<Term>> ts = new ArrayList<List<Term>>();
			ts.add(new ArrayList<Term>());
			ts.get(0).add(new Term(TermScope.NONE,false,(String)temp));
			left = new Filter(new ResourceID(ts,null));
			toks.remove(0);
		}
		else if (temp instanceof Filter) {
			left = (Filter)temp;
			toks.remove(0);
		}
		else {
			throw new RuntimeException("error parsing '"+str+"', temp="+temp.getClass());
		}
		//jprime.Console.out.println("parsed left to be "+left);
		if(toks.size()==0) {
			//no op or right
			return left;
		}	
		temp=toks.remove(0);
		if(temp instanceof FilterOp) {
			op = (FilterOp)temp;
		}
		else {
			throw new RuntimeException("error parsing '"+str+"', temp="+temp);
		}
		//jprime.Console.out.println("parsed op to be "+op);
		
		if(toks.size()==0){
			throw new RuntimeException("error parsing '"+str+"'");
		}
		if(op == FilterOp.OR) {
			right=parseFilter(toks,str);
		}
		else {
			temp=toks.get(0);
			if(temp instanceof ArrayList) {
				right=parseFilter((ArrayList<Object>)temp,str);
				toks.remove(0);
			}
			else if (temp instanceof ResourceID) {
				right = new Filter((ResourceID)temp);
				toks.remove(0);
			}
			else if (temp instanceof String) {
				ArrayList<List<Term>> ts = new ArrayList<List<Term>>();
				ts.add(new ArrayList<Term>());
				ts.get(0).add(new Term(TermScope.NONE,false,(String)temp));
				right = new Filter(new ResourceID(ts,null));
				toks.remove(0);
			}
			else if (temp instanceof Filter) {
				right = (Filter)temp;
				toks.remove(0);
			}
			else {
				throw new RuntimeException("error parsing '"+str+"'");
			}			
		}
		//jprime.Console.out.println("parsed right to be "+right);
		toks.add(0,new Filter(left,op,right));
		if(toks.size()==1)
			return (Filter)toks.remove(0);
		return parseFilter(toks, str);
	}
	
	/**
	 * @return
	 */
	public String packToTLV() {
		if(getValue()!=null) {
			return getValue().packToTLV();
		}
		else {
			String temp=TLVVisitor.makeTLV(TLVType.STRING,getOp().str.length(),getOp().str);
			if(getLeft()!=null) {
				temp+=getLeft().packToTLV();
				if(getRight()!=null)
					temp+=getRight().packToTLV();
			}
			return TLVVisitor.makeTLV(TLVType.LIST,temp.length(),temp);
		}
	}

}
