package jprime.ResourceIdentifier;

import java.io.Serializable;

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


/**
 * @author Nathanael Van Vorst
 * 
 * This is a element of a path in a resource identifier
 *
 */
public class Term implements Serializable {
	private static final long serialVersionUID = -3957798248974887102L;

	/**
	 * @author Nathanael Van Vorst
	 * 
	 * Each term has a scope which must be one of the following
	 */
	public static enum TermScope {
		UP(".."),
		DOWN(":"),
		RECURSE_DOWN("//"),
		SUB_ATTR("."),
		ATTR("@"),
		NONE("");
		public final String str;
		TermScope(String str) {
			this.str=str;
		}
		public String toString() {
			return str;
		}
	}
	//the scope of the term
	private TermScope scope;
	//whether this term is a wild-card
	private boolean isClassWildcard;
	//the value of the term
	private String value;
	
	/**
	 * internal constructor
	 */
	protected Term() {
		super();
		this.scope = null;
		this.isClassWildcard = false;
		this.value = null;
	}

	/**
	 * @param scope
	 * @param isClassWildcard
	 * @param value
	 */
	public Term(TermScope scope, boolean isClassWildcard, String value) {
		super();
		this.scope = scope;
		this.isClassWildcard = isClassWildcard;
		this.value = value;
	}

	/**
	 * @return the scope
	 */
	public TermScope getScope() {
		return scope;
	}

	/**
	 * @return whether this term is a wild-card
	 */
	public boolean isClassWildcard() {
		return isClassWildcard;
	}

	/**
	 * @return the value of this term
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * if the term was created using the internal constructor then is can be loaded from the sub-string 'str' staring at idx
	 * @param str
	 * @param idx
	 * @return
	 */
	public int parseString(String str, int idx) {
		if(scope!=null || value != null) {
			throw new RuntimeException("Can only parse into an empty Term!");
		}
		int orig_idx=idx;
		idx=ResourceID.eatWhiteSpace(str, idx);
		if(idx>=str.length()) {
			return orig_idx;
		}
		//find scope
		switch(str.charAt(idx)) {
		case '@':
			scope=TermScope.ATTR;
			idx++;
			break;
		case ':':
			scope=TermScope.DOWN;
			idx++;
			break;
		case '/':
			if(idx+1<str.length()) {
				if(str.charAt(idx+1)=='/') {
					scope=TermScope.RECURSE_DOWN;
					idx+=2;
				}
				else {
					return orig_idx;
				}
			}
			else {
				return orig_idx;
			}
			break;
		case '.':
			if(idx+1<str.length()) {
				if(str.charAt(idx+1)=='.') {
					scope=TermScope.UP;
					idx+=2;
				}
				else {
					idx++;
					scope=TermScope.SUB_ATTR;
				}
			}
			else {
				idx++;
				scope=TermScope.SUB_ATTR;
			}
			break;
		case '[':
		case ']':
		case '(':
		case ')':
		case '{':
		case '}':
		case ',':
			return orig_idx;
		default:
			scope=TermScope.NONE;
		}
		idx=ResourceID.eatWhiteSpace(str, idx);
		//check if its wild
		if(str.charAt(idx)=='?') {
			if(isClassWildcard) {
				throw new RuntimeException("Error parsing resource id '"+str+"'near character "+idx+" '"+str.charAt(idx)+"'"+"!");
			}
			this.isClassWildcard=true;
			idx++;
		}
		idx=ResourceID.eatWhiteSpace(str, idx);
		//get the value
		int start=idx;
		while(idx<str.length()) {
			switch(str.charAt(idx)) {
			case '@':
			case ':':
			case '/':
			case '.':
			case '[':
			case ']':
			case '(':
			case ')':
			case '{':
			case '}':
			case ',':
			case '<':
			case '>':
			case '=':
			case '!':
			case '&':
			case '|':
			case '"':
				if(idx>start) {
					value=str.substring(start, idx);
					return ResourceID.eatWhiteSpace(str, idx);
				}
				else if(scope == TermScope.UP || scope == TermScope.SUB_ATTR) {
					value="";
					return ResourceID.eatWhiteSpace(str, idx);
				}
				else {
					return orig_idx;
				}
			default:
				if(Character.isWhitespace(str.charAt(idx))) {
					if(idx>start) {
						value=str.substring(start, idx);
						return ResourceID.eatWhiteSpace(str, idx);
					}
					else if(scope == TermScope.UP) {
						value="";
					}
					else {
						//only .. can have an empty term!
						throw new RuntimeException("Error parsing resource id '"+str+"'near character "+idx+" '"+str.charAt(idx)+"'"+"!");
					}					
				}
				else {
					idx++;
				}
			}		
		}
		value=str.substring(start, idx);
		return ResourceID.eatWhiteSpace(str, idx);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return scope.str+(isClassWildcard?"?":"")+value;
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof Term) {
			Term t = (Term)obj;
			if(t.isClassWildcard == isClassWildcard) {
				if(t.value == value) {
					if(t.scope == scope) {
						return true;
					}
				}
			}
		}
		return false;
	}

}
