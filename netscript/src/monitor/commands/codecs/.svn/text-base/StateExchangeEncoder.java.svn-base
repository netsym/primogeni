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
import monitor.commands.StateExchangeCmd;
import monitor.commands.VarUpdate;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;


/**
 * @author Nathanael Van Vorst
 *
 * @param <T>
 */
public class StateExchangeEncoder<T extends StateExchangeCmd> extends AbstractCmdEncoder<T> {
	public StateExchangeEncoder() {
        super(CommandType.STATE_EXCHANGE_CMD);
    }

    @Override
    protected void encodeBody(IoSession session, T message, IoBuffer out) {
    	out.putLong(message.getUid());
    	out.putShort((short)(message.forViz()?1:0));
    	out.putShort((short)(message.aggregate()?1:0));
    	out.putLong(message.getTime());
    	out.putInt(message.getCmdType());
    	out.putInt(message.getCommunity_id());
    	out.putInt(message.getUpdates().size());
    	for(VarUpdate u : message.getUpdates())
    		u.encode(out,false);
    }
    public void dispose() throws Exception {
    }
}