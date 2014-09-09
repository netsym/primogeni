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
import monitor.commands.AreaOfInterestUpdate;
import monitor.commands.CommandType;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
/**
 * @author Nathanael Van Vorst
 *
 */
public class AreaOfInterestUpdateDecoder extends AbstractCmdDecoder {
    public AreaOfInterestUpdateDecoder() {
        super(CommandType.AREA_OF_INTEREST_UPDATE);
    }
    @Override
    protected AbstractCmd decodeBody(IoSession session, IoBuffer in, int length) {
    	boolean add = in.getShort()==0?false:true;
    	long[] uids = new long[in.getInt()]; 
    	for(int i=0;i<uids.length;i	++)
    		uids[i]=in.getLong();
    	return new AreaOfInterestUpdate(uids,add);
    }
    
    public void finishDecode(IoSession session, ProtocolDecoderOutput out) throws Exception{
    }
}
