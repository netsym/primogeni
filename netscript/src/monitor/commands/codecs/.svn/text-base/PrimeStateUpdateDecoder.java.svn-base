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

import java.util.ArrayList;

import monitor.commands.ComPartAdvertCmd;
import monitor.commands.PrimeStateExchangeCmd;
import monitor.commands.VarUpdate;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;


/**
 * @author Nathanael Van Vorst
 *
 */
public class PrimeStateUpdateDecoder implements MessageDecoder {
	private boolean logged_in=false;
	private int msg_size=-1;
	
	public PrimeStateUpdateDecoder() {
		super();
	}

	public MessageDecoderResult decodable(IoSession session, IoBuffer in) {
		//we assume that this is the ONLY protocol in the factory!
		return MessageDecoderResult.OK;
	}

	public MessageDecoderResult decode(IoSession session, IoBuffer in,
			ProtocolDecoderOutput out) throws Exception {
		if(!logged_in) {
			//expect //{0,1}{part_id}{com_id}
			if(in.remaining()<12)
				return MessageDecoderResult.NEED_DATA;
			
			int reader = in.getInt();
			int part_id = in.getInt();
			int com_id = in.getInt();
			
			out.write(new ComPartAdvertCmd(reader==1, part_id, com_id));
			
			logged_in=true;
			
			if(in.remaining()==0)
				return MessageDecoderResult.OK;
		}
		
		do {
			if(msg_size==-1) {
				if(in.remaining()>=2)
					msg_size=in.getInt();
				else
					return MessageDecoderResult.NEED_DATA;
			}
			if(in.remaining()<msg_size)
				return MessageDecoderResult.NEED_DATA;
			final long time = in.getLong();
			final long uid = in.getLong();
			final boolean forViz = in.getInt()==0?false:true;
			final boolean aggregate = in.getInt()==0?false:true;
			final int type = in.getInt();
			final int update_count = in.getInt();
			final ArrayList<VarUpdate> updates = new ArrayList<VarUpdate>(update_count);
			
			for(int i=0;i<update_count;i++) {
				updates.add(VarUpdate.parseVarUpdate(in,true));
			}
			out.write(new PrimeStateExchangeCmd(time, uid, forViz, aggregate, type, updates));
			msg_size=-1;
		} while(in.remaining()>0);
		return MessageDecoderResult.OK;
	}


	public void finishDecode(IoSession session, ProtocolDecoderOutput out)
	throws Exception {
	}
}
