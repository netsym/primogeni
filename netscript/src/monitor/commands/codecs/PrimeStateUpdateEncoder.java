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

import monitor.commands.CommandType;
import monitor.commands.PrimeStateExchangeCmd;
import monitor.commands.VarUpdate;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;


/**
 * @author Nathanael Van Vorst
 *
 * @param <T>
 */
public class PrimeStateUpdateEncoder implements MessageEncoder<PrimeStateExchangeCmd> {
	public PrimeStateUpdateEncoder() {
        super();
    }
    public void encode(IoSession session, PrimeStateExchangeCmd message, ProtocolEncoderOutput out) throws Exception {
    	int bs=message.getBodyLength();
        IoBuffer buf = IoBuffer.allocate(bs+Integer.SIZE/4);
        buf.setAutoExpand(true); // Enable auto-expand for easier encoding
        buf.putInt(CommandType.PRIME_STATE_EXCHANGE.getType());
    	buf.putInt(bs);
    	buf.putLong(message.getTime());
        buf.putLong(message.getUid());
    	buf.putInt(message.getCmdType());
    	buf.putInt(message.getUpdates().size());
    	for(VarUpdate u : message.getUpdates())
    		u.encode(buf,true);
        buf.flip();
        out.write(buf);
    }
    
    public void dispose() throws Exception {
    }
}