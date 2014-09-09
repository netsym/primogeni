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

import jprime.database.ChildIdList;
import jprime.database.PKey;

/**
 * @author Nathanael Van Vorst
 */
public abstract class PersistableObject {
	public static enum PersistableState {
		NEW,//need to add to db
		MODIFIED,//has been added to db but needs to be udpated
		UNMODIFIED,//the version in the db matches this one
		ORPHAN,//the node is not in the database and SHOULD NOT be added
		DEAD;//the node was added to the DB but its needs to be deleted!
	}
	public static enum Modified {
		NOTHING(1), //its all saved
		ATTRS(2), //only attrs modidified
		CHILDREN(4), //only children modified
		LOCAL_PROPS(8), //only local props (i.e. name uid, size, etc)
		ALL(16), //local_props, attrs, and children possible modified (i.e. new)
		;
		public final int id;
		Modified(int id) { this.id=id; }
	};
	protected PersistableState persistable_state;
	protected int mods;
	public boolean cached=false;
	
	/**
	 * @return whats modified
	 */
	public int whatsModified() {
		return mods;
	}
	
	/**
	 * @return whether this object needs to be saved!
	 */
	public PersistableState getPersistableState() {
		return persistable_state;
	}
	
	/**
	 * used by db to set the object as saved!
	 */
	public void saved() {
		switch(this.persistable_state) {
		case ORPHAN:
		case UNMODIFIED:
			//no up;
			mods=Modified.NOTHING.id;
			break;
		case MODIFIED:
		case NEW:
			//the data is in the DB
			mods=Modified.NOTHING.id;
			this.persistable_state=PersistableState.UNMODIFIED;
			break;
		case DEAD:
			//its been removed
			this.persistable_state=PersistableState.ORPHAN;
			break;
		default:
		{
			try {
				throw new RuntimeException("How did this happen?");
			}catch(Exception e) {
				jprime.Console.err.printStackTrace(e);
			}
			throw new UnsupportedOperationException();
		}
		}
	}
		
	/**
	 * @return
	 */
	public abstract PKey getPKey();
	
	/**
	 * @return the list of child ids (if any)
	 */
	public ChildIdList getChildIds() {
		return new ChildIdList();
	}
	
	/**
	 * get the unique integer that each type uses to identify itself
	 * @return
	 */
	public abstract int getTypeId();

	
	/**
	 * @return
	 */
	public abstract long getDBID();
	
	/**
	 * 
	 */
	public abstract void save();

	/**
	 * 
	 */
	public abstract Metadata getMetadata();
	
	public synchronized void modified(Modified what) {
		switch(this.persistable_state) {
		case UNMODIFIED:
			mods=what.id;
			this.persistable_state=PersistableState.MODIFIED;
			getMetadata().modified(this);
			break;
		case MODIFIED:
			mods|=what.id;
			break;
		case NEW:
			//no up;
			break;
		case DEAD:
		case ORPHAN:
			getMetadata().modified(this);
		}
	}
	
	/* (non-Javadoc)
	 * @see jprime.IPeristable#orphan()
	 */
	public void orphan() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean equals(Object obj) {
		throw new RuntimeException("The subclass should have overridden this!");
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#oldHashCode()
	 */
	@Override
	public int hashCode() {
		throw new RuntimeException("Due to the nature of our database scheme we do not support the oldHashCode function. Use equals() to test object equality!");
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getClass().getSimpleName()+"@[DBID:"+getDBID()+",MID:"+getMetadata().getDBID()+",Hash:"+oldHashCode()+"]";
	}

	public final int oldHashCode() {
		return super.hashCode();
	}
	
	public boolean hasBeenReplicated() {
		return false;
	}
}