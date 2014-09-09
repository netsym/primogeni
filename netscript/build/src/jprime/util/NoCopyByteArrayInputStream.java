package jprime.util;

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

import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;

/**
 * @author Nathanael Van Vorst
 *
 */
public class NoCopyByteArrayInputStream {
	private ByteBuffer bb;
	private byte[] buf=null;
    public NoCopyByteArrayInputStream(final int size) {
        this.buf = new byte[size];
        this.bb=ByteBuffer.wrap(buf);
    }
    public NoCopyByteArrayInputStream(final byte[] b) {
        this.buf = b;
        this.bb=ByteBuffer.wrap(buf);
    }
    public ByteBuffer getByteBuffer(int sz){
    	if(buf.length<sz) {
        	System.out.println("Growing output buf to "+(2*buf.length)+" bytes.");
            buf = new byte[Math.max(sz, 2 * buf.length )];
            bb=ByteBuffer.wrap(buf);
    	}
    	bb.clear();
    	bb.position(bb.capacity()-sz);
    	return bb.slice();
    }
    public ByteBuffer getSlice(){
    	return bb.slice();
    }
    public ByteArrayInputStream getInputStream() {
    	return new ByteArrayInputStream(buf, bb.position(), bb.remaining());
    }

}