package jprime.partitioning;

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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * @author Nathanael Van Vorst
 */
public abstract class IUIDRange {

	/**
	 * @return
	 */
	public abstract long getLow();

	/**
	 * @return
	 */
	public abstract  long getHigh();

	/**
	 * @param low the low to set
	 */
	public abstract  void setLow(long low);

	/**
	 * @param high the high to set
	 */
	public abstract  void setHigh(long high);
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public final String toString() {
		return "[IUIDRange "+getLow()+","+getHigh()+"]";
	}
	
	/**
	 * @author Nathanael Van Vorst
	 *
	 */
	public static class UIDRangeComparator implements Comparator<IUIDRange> {
		public int compare (IUIDRange o1, IUIDRange o2) {
			if(o1.getLow() == o2.getLow()) return 0;
			if(o1.getLow() < o2.getLow()) return -1;
			return 1;
		}
	}
	
	public static class UIDRangeListManip<T extends IUIDRange> {
		private final UIDRangeComparator comparator = new UIDRangeComparator();
		private final IUIDRange insert_temp= new UIDRange(0,0);
		/**
		 * @param low
		 * @param high
		 * @param ranges
		 * @return
		 */
		@SuppressWarnings("unchecked")
		public void addUIDRange(long low, long high, List<T> ranges, Alignment alignment) {
			// jprime.Console.out.println("*******************\nAddding ["+low+","+high+"] to \n\t"+ranges);
			insert_temp.setLow(low);
			int insertion_point = Collections.binarySearch(ranges, insert_temp, comparator);
			boolean propagate=false;
			if(insertion_point>=0) {
				//we found a range with the same low value
				IUIDRange cur = (IUIDRange)ranges.get(insertion_point);
				//jprime.Console.out.println("\tReusing "+cur);
				if(cur.getHigh() < high) {
					cur.setHigh(high);
					propagate=true;
				}
			}
			else {
				//adding a new range entry between two values
				IUIDRange left = null, right=null;
				insertion_point=((-insertion_point)-1);
				if(insertion_point-1>=0 && ranges.size()>0)
					left=(IUIDRange)ranges.get(insertion_point-1);
				if(insertion_point<ranges.size())
					right=(IUIDRange)ranges.get(insertion_point);
				//jprime.Console.out.println("\tinsertion_point="+insertion_point+"["+ranges.size()+"], left="+left+", right="+right);
				if(left==null && right==null) {
					//jprime.Console.out.println("\tNew value");
					if(alignment==null) {
						ranges.add((T) new UIDRange(low,high));
					}
					else {
						ranges.add((T) new AlignedUIDRange(low,high,alignment));
					}
				}
				else if(left==null) {
					//jprime.Console.out.println("\tinsert at head");
					if(high >= right.getLow()-1) {
						//they overlap
						right.setLow(low);
					}
					else {
						//just need to add it
						if(alignment==null) {
							ranges.add(0,(T) new UIDRange(low,high));
						}
						else {
							ranges.add(0,(T) new AlignedUIDRange(low,high,alignment));
						}
					}
				}
				else if(right==null) {
					//jprime.Console.out.println("\tinsert at tail");
					if(low <= left.getHigh()+1) {
						//they overlap
						left.setHigh(high);
					}
					else {
						//just need to add it
						if(alignment==null) {
							ranges.add((T)new UIDRange(low,high));
						}
						else {
							ranges.add((T)new AlignedUIDRange(low,high,alignment));
						}
					}
				}
				else {
					//jprime.Console.out.println("\tinsert in middle");
					//insert in middle
					//there are four possibilities
					//1) no overlap and we just insert the range
					//2) the new range spans to overlap with both the left and ranges
					//3) the new range overlaps with the left
					//3) the new range overlaps with the right
					
					if(low>left.getHigh()+1 && high < right.getLow()-1) {
						//jprime.Console.out.println("\tcase 1 , just add it");
						if(alignment==null) {
							ranges.add(insertion_point,(T)new UIDRange(low,high));
						}
						else {
							ranges.add(insertion_point,(T)new AlignedUIDRange(low,high,alignment));
						}
					}
					else if(low<=left.getHigh()+1 && high >= right.getLow()-1) {
						//jprime.Console.out.println("\tcase 2 , update "+left);
						//case 2
						//update the left and then propagate the changes
						insertion_point--; //update index of the node to the one we are editing
						left.setHigh(high);
						propagate=true;
					}
					else if(low<=left.getHigh()+1) {
						//jprime.Console.out.println("\tcase 3 , update "+left);
						insertion_point--; //update index of the node to the one we are editing
						left.setHigh(high);
						propagate=true;
					}
					else if(high >= right.getLow()-1) {
						//jprime.Console.out.println("\tcase 4 , update "+right);
						right.setLow(low);
					}
					else {
						throw new RuntimeException("Should never see this!");
					}
				}
			}
			if(propagate) {
				//jprime.Console.out.println("\tPropigating");
				//since we updated the high value of a range, the resulting range could overlap with the ranges that follow. 
				//Thus, we need to propagate this update.
				IUIDRange cur = null, right=null;
				while(insertion_point+1 < ranges.size()) {
					cur = (IUIDRange)ranges.get(insertion_point);
					right=(IUIDRange)ranges.get(insertion_point+1);
					if(cur.getHigh() >= right.getLow()-1) {
						cur.setHigh(right.getHigh());
						Object del = ranges.remove(insertion_point+1);
						if(del instanceof AlignedUIDRange) {
							//XXX
							//Database.getDatabase().remove(del, ((PersistentUIDRange) del).alignment);
						}
					}
					else {
						break;
					}
				}
			}
			//jprime.Console.out.println("\tResult:"+ranges);
		}
	}
}
