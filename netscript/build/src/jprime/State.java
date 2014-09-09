package jprime;

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
 */
public enum State {
    CLOSED(0,"CLOSED"),
    PRE_COMPILED(1,"Pre Compiled"),
    COMPILING(2,"Compiling"),
    COMPILED(3,"Compiled"),
    PARTITIONING(4,"Paritioning"),
    PARTITIONED(5,"Parititoned"),
    SETTING_UP(6,"Setting Up Experiment"),
    SETUP(7,"Setting Up Experiment"),
    STARTING(8,"Starting Experiment"),
    RUNNING(9,"Running"),
    RUNNING_DETACHED(10,"Running in Detached Mode");
	
    /**
	 * XXX
     * @param val
     * @param str
     */
    State(int val, String str){ 
    	this.val=val;
    	this.str=str;
    }
    public final int val;
    public final String str;
    
	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return str;
	}
	/**
	 * XXX
	 * @param s
	 * @return
	 */
	boolean equal(State s) { 
		return s.val==val;
	}
	/**
	 * XXX
	 * @param v
	 * @return
	 */
	boolean equal(int v) { 
		return val==v;
	}
	/**
	 * XXX
	 * @param s
	 * @return
	 */
	public boolean lt(State s) { 
		return val < s.val;
	}
	/**
	 * XXX
	 * @param s
	 * @return
	 */
	public boolean lte(State s) { 
		return val <= s.val;
	}
	/**
	 * XXX
	 * @param s
	 * @return
	 */
	public boolean gt(State s) { 
		return val > s.val;
	}
	/**
	 * XXX
	 * @param s
	 * @return
	 */
	public boolean gte(State s) { 
		return val >= s.val;
	}
	/**
	 * @param i
	 * @return
	 */
	public static State fromInt(int i) {
		for(State s : State.values()) {
			if(s.equal(i)) {
				return s;
			}
		}
		throw new RuntimeException("Invalid state "+i);
	}
}
