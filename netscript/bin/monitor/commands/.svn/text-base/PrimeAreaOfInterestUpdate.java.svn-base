package monitor.commands;

import java.util.ArrayList;


public class PrimeAreaOfInterestUpdate extends AbstractCmd{
	public final ArrayList<Long> uids;
	public final boolean add;
	public PrimeAreaOfInterestUpdate(long uid, boolean add) {
		super(CommandType.PRIME_AREA_OF_INTEREST_UPDATE);
		this.uids = new ArrayList<Long>(1);
		this.uids.add(uid);
		this.add = add;
	}
	public PrimeAreaOfInterestUpdate(long[] uids, boolean add) {
		super(CommandType.PRIME_AREA_OF_INTEREST_UPDATE);
		this.uids = new ArrayList<Long>(uids.length);
		for(long uid : uids)
			this.uids.add(uid);
		this.add = add;
	}
	public PrimeAreaOfInterestUpdate(ArrayList<Long> uids, boolean add) {
		super(CommandType.PRIME_AREA_OF_INTEREST_UPDATE);
		this.uids = uids;
		this.add = add;
	}

	/* (non-Javadoc)
	 * @see monitor.commands.AbstractCmd#getBodyLength()
	 */
	@Override
	public int getBodyLength() {
		return (Long.SIZE*uids.size()+ 2*Integer.SIZE)/8;
	}
	public String toString() {
		return "[PrimeAreaOfInterestUpdate add="+add+" #uids="+uids.size()+"]";
	}
}