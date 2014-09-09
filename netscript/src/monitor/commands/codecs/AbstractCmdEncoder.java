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
import java.nio.charset.CharsetEncoder;

import monitor.commands.AbstractCmd;
import monitor.commands.CommandType;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;


/**
 * @author Nathanael Van Vorst
 *
 * @param <T>
 */
public abstract class AbstractCmdEncoder<T extends AbstractCmd> implements MessageEncoder<T> {
	protected static final CharsetEncoder cse = Charset.forName("UTF-8").newEncoder();
    private final int cmdType;

    protected AbstractCmdEncoder(CommandType cmdType) {
        this.cmdType = cmdType.getType();
    }

    public void encode(IoSession session, T message, ProtocolEncoderOutput out) throws Exception {
        IoBuffer buf = IoBuffer.allocate(AbstractCmd.HEADER_LENGTH);
        buf.setAutoExpand(true); // Enable auto-expand for easier encoding

        // Encode a header
        buf.putInt(cmdType);
        buf.putInt(message.getSerialNumber());
        buf.putInt(message.getMachineId());
        buf.putInt(message.getBodyLength());

        // Encode a body
        encodeBody(session, message, buf);
        buf.flip();
        out.write(buf);
    }

    protected abstract void encodeBody(IoSession session, T message, IoBuffer out);
}
