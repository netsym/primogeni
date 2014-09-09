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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import monitor.commands.CommandType;
import monitor.commands.StartGatewayCmd;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
/**
 * @author Nathanael Van Vorst
 *
 * @param <T>>
 */
public class StartGatewayEncoder<T extends StartGatewayCmd> extends AbstractCmdEncoder<T> {
    public StartGatewayEncoder() {
        super(CommandType.START_GATEWAY);
    }
    @Override
    protected void encodeBody(IoSession session, T message, IoBuffer out) {
    	try {
    		//the file name
	    	out.putInt(message.getTarball().length());
			out.putString(message.getTarball(),message.getTarball().length(),cse);
	    	out.putInt(message.getExpName().length());
			out.putString(message.getExpName(),message.getExpName().length(),cse);

			//the file size
			final File f = message.getConfig();
			final long length=f.length();
			out.putLong(length);
			
			//write the file in chunks of 1024 bytes
			byte[] buf=new byte[1024];
			long offset=0;
			int numRead=0;
			InputStream is = new FileInputStream(f);
			while (offset <length
					&& (numRead=is.read(buf, 0, 1024)) >= 0) {
				//System.out.println("Read "+numRead+" "+offset+"/"+length);
				//read numRead bytes into buf
				offset += numRead;
				out.put(buf, 0, numRead);
			}
			is.close();
    	} catch (FileNotFoundException e) {
    		throw new RuntimeException(e);
    	} catch (IOException e) {
    		throw new RuntimeException(e);
		}
    }
}
