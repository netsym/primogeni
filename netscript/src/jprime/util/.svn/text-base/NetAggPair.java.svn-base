package jprime.util;

import java.util.LinkedList;
import java.util.Set;
import java.util.TreeMap;

import jprime.Aggregate.IAggregate;
import jprime.Net.INet;
import jprime.partitioning.Partitioning;

public class NetAggPair implements Comparable<NetAggPair> {
	public final long net_uid;
	public final LinkedList<Long> agg_uids;

	public NetAggPair(long net_uid) {
		super();
		this.net_uid = net_uid;
		this.agg_uids = new LinkedList<Long>();
	}

	public int compareTo(NetAggPair o) {
		return (int)(net_uid-o.net_uid);
	}
	
	public static TreeMap<NetAggPair, Set<Integer>> getAggMap(Partitioning parting) {
		final TreeMap<NetAggPair, Set<Integer>> rv = new TreeMap<NetAggPair, Set<Integer>>();
		final LinkedList<INet> nets = new LinkedList<INet>();
		nets.add(parting.getTopnet());
		while(nets.size()>0) {
			final INet n = nets.poll();
			final NetAggPair p= new NetAggPair(n.getUID());
			final Set<Integer> l = n.getAlignments(parting);
			for(final IAggregate a : n.getAggregates()) {
				p.agg_uids.add(a.getUID());
			}
			rv.put(p,l);
			nets.addAll(n.getSubnets());
		}
		return rv;
	}
}

