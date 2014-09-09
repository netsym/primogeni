package monitor.commands.codecs;

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
import java.nio.charset.CharacterCodingException;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import jprime.util.NetAggPair;
import monitor.commands.CommandType;
import monitor.commands.SetupExperimentCmd;
import monitor.commands.SetupExperimentCmd.TLVFile;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
/**
 * @author Nathanael Van Vorst
 *
 * @param <T>>
 */
public class SetupExperimentEncoder<T extends SetupExperimentCmd> extends AbstractCmdEncoder<T> {
	public SetupExperimentEncoder() {
		super(CommandType.SETUP_EXPERIMENT);
	}
	@Override
	protected void encodeBody(IoSession session, T message, IoBuffer out) {
		try {
			out.putInt(message.getName().length());
			//jprime.Console.out.println("---inserting name length" + message.getName().length());
			try {
				out.putString(message.getName(),message.getName().length(),cse);
				//jprime.Console.out.println("---inserting name: " + message.getName() + " length=" + 
				//		message.getName().length());
			} catch (CharacterCodingException e) {
				throw new RuntimeException(e);
			}
			out.putInt(message.getRuntimeSymbols().length());
			//jprime.Console.out.println("---inserting runtime symbols length=" + message.getRuntimeSymbols().length());
			try {
				out.putString(message.getRuntimeSymbols(),message.getRuntimeSymbols().length(),cse);
				//jprime.Console.out.println("---inserting  runtime symbols" + message.getRuntimeSymbols() + 
				//		" length=" + message.getRuntimeSymbols().length());
			} catch (CharacterCodingException e) {
				throw new RuntimeException(e);
			}
			TreeMap<NetAggPair, Set<Integer>> tm = message.getAggMap();
			out.putInt(tm.size());
			//jprime.Console.out.println("---inserting size of tree map: " + tm.size());
			for(Entry<NetAggPair, Set<Integer>> e : tm.entrySet()) {
				out.putLong(e.getKey().net_uid);
				//jprime.Console.out.println("\t---inserting uid" + e.getKey().net_uid);
				out.putInt(e.getKey().agg_uids.size());
				//jprime.Console.out.println("\t---inserting agg_uids size" + e.getKey().agg_uids.size());
				for(long l : e.getKey().agg_uids) {
					//jprime.Console.out.println("\t---inserting key" + l);
					out.putLong(l);
				}
				out.putInt(e.getValue().size());
				//jprime.Console.out.println("\t---inserting align size" + e.getValue().size());
				for(int i : e.getValue()) {
					out.putInt(i);
					//jprime.Console.out.println("\t---inserting value" + i);
				}
			}
			out.putInt(message.getTlvs().size());
			//jprime.Console.out.println("---inserting tlv size=" + message.getTlvs().size());
			for(TLVFile tlv : message.getTlvs()) {
				tlv.encode(out);
			}
		}catch(Exception e) {
			//System.out.println(e);
			throw new RuntimeException(e);
		}
	}
}
