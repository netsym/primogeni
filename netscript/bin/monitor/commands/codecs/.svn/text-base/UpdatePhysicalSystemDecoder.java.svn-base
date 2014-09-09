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


import monitor.commands.AbstractCmd;
import monitor.commands.CommandType;
import monitor.commands.UpdatePhysicalSystemCmd;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

/**
 * @author Miguel Erazol Erazo
 *
 */
public class UpdatePhysicalSystemDecoder extends AbstractCmdDecoder
{
	public UpdatePhysicalSystemDecoder() {
        super(CommandType.SEND_UPDATE_TO_ACTUATOR_CMD);
    }
	
	public void finishDecode(IoSession arg0, ProtocolDecoderOutput arg1)
			throws Exception {}

	@Override
	protected AbstractCmd decodeBody(IoSession session, IoBuffer in, int length) {
		int index = in.getInt();
		int bandwidth = in.getInt();
		int rtt = in.getInt();
		System.out.println("decoding: index=" + index + " bandwidth=" + bandwidth + " rtt=" + rtt );
		
		UpdatePhysicalSystemCmd cmd = new UpdatePhysicalSystemCmd(index, bandwidth, rtt);
    	return cmd;
	}
}
