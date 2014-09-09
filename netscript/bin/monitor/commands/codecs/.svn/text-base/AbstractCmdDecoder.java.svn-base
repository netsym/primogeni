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

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

import monitor.commands.AbstractCmd;
import monitor.commands.CommandType;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;


/**
 * @author Nathanael Van Vorst
 *
 */
public abstract class AbstractCmdDecoder implements MessageDecoder {
	protected static final CharsetDecoder csd = Charset.forName("UTF-8").newDecoder();
    protected final int cmdType;
    protected int serialNumber, machineId, cmdLength;
    protected boolean readHeader;

    protected AbstractCmdDecoder(CommandType cmdType) {
        this.cmdType = cmdType.getType();
    }

    public MessageDecoderResult decodable(IoSession session, IoBuffer in) {
        // Return NEED_DATA if the whole header is not read yet.
        if (in.remaining() < AbstractCmd.HEADER_LENGTH) {
            return MessageDecoderResult.NEED_DATA;
        }

        // Return OK if type and bodyLength matches.
        if (cmdType == in.getInt()) {
            return MessageDecoderResult.OK;
        }

        // Return NOT_OK if not matches.
        return MessageDecoderResult.NOT_OK;
    }

    public MessageDecoderResult decode(IoSession session, IoBuffer in,
            ProtocolDecoderOutput out) throws Exception {
    	//if(Utils.DEBUG)System.out.println("DECODING msg of type "+CommandType.fromInt(cmdType)+", readHeader="+readHeader);
        // Try to skip header if not read.
        if (!readHeader) {
            in.getInt(); // Skip 'type'.
            serialNumber = in.getInt(); // Get 'sequence'.
            machineId = in.getInt(); // Get 'slaveId'.
            cmdLength = in.getInt(); // Get 'length'.
            //System.out.println("sequence=" + serialNumber+", machineId="+machineId+", cmdLength="+cmdLength+", in.remaining="+in.remaining());
            readHeader = true;
        }
        //if(Utils.DEBUG)System.out.println("DECODING read header, (in.remaining()["+in.remaining()+"] < cmdLength["+cmdLength+"])==>"+(in.remaining() < cmdLength));
        // Return NEED_DATA if the body is not fully read.
        if(in.remaining() < cmdLength) {
        	//if(Utils.DEBUG)System.out.println("not enough data");
            return MessageDecoderResult.NEED_DATA;
        }
        // Try to decode body
        AbstractCmd m = decodeBody(session, in, cmdLength);
        if (m == null) {
        	//something bad happened
        	//if(Utils.DEBUG)System.out.println("\tsomething bad happened");
        	return MessageDecoderResult.NOT_OK;
        }
        readHeader = false; // reset readHeader for the next decode
        
        //deliver the new message
        m.setSerialNumber(serialNumber);
        m.setMachineId(machineId);
        //if(Utils.DEBUG)System.out.println("\tfinished cmd="+m+", in.remaining="+in.remaining());
        out.write(m);
        return MessageDecoderResult.OK;
    }

    /**
     * @return <tt>null</tt> if the whole body is not read yet
     */
    protected abstract AbstractCmd decodeBody(IoSession session,
            IoBuffer in, int length);
}
