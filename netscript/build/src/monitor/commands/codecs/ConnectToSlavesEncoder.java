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

import monitor.commands.CommandType;
import monitor.commands.ConnectToSlavesCmd;
import monitor.core.NodeConfig;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;


/**
 * @author Nathanael Van Vorst
 *
 * @param <T>
 */
public class ConnectToSlavesEncoder<T extends ConnectToSlavesCmd> extends AbstractCmdEncoder<T> {
	public ConnectToSlavesEncoder() {
        super(CommandType.CONNNECT_SLAVES);
    }

    @Override
    protected void encodeBody(IoSession session, T message, IoBuffer out) {
    	out.putInt(message.getSlaveConfigs().size());
    	if(message.getMasterConfig()==null)
    		throw new RuntimeException("Invalid ConnectToSlaveCommands!");
    	try {
	    	message.getMasterConfig().encode(out);
	    	for(NodeConfig nc : message.getSlaveConfigs()) {
	    		nc.encode(out);
	    	}
		} catch (CharacterCodingException e) {
			throw new RuntimeException(e);
		}
    }
    public void dispose() throws Exception {
    }
}