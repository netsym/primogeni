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
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * @author Nathanael Van Vorst
 *
 */
public class NoCopyByteArrayOutputStream extends OutputStream {
	private ByteBuffer bb;
	private byte[] buf;
    public NoCopyByteArrayOutputStream(int initSize) {
    	this.buf = new byte[initSize];
        this.bb = ByteBuffer.wrap(buf);
        this.bb.clear();
    }
    public boolean haveSpace(int sz) {
    	return bb.remaining() - sz >= 64;
    }
    public int size() {
    	return bb.position();
    }
    public ByteArrayInputStream getBufferAsByteArrayInputStream() {
    	return new ByteArrayInputStream(buf,0,bb.position());
    }
	@Override
	public void write(int b) throws IOException {
		//if( ((byte)(b & 0xff)) != ((byte)b)) {
		//	throw new RuntimeException("fsadfsd");
		//}
		bb.put((byte)b);
	}
	public ByteBuffer getByteBuffer() {
		return ByteBuffer.wrap(buf,0,bb.position());
	}
	
	public void clear() {
		bb.clear();
	}
	public ByteArrayInputStream slice(int offset, int len) {
		return new ByteArrayInputStream(buf, offset, len);
	}
	
	/* (non-Javadoc)
	 * @see java.io.OutputStream#write(byte[], int, int)
	 */
	@Override
	public void write(byte[] src, int offset, int length) throws IOException {
		bb.put(src,offset,length);
	}
	/* (non-Javadoc)
	 * @see java.io.OutputStream#write(byte[])
	 */
	@Override
	public void write(byte[] b) throws IOException {
		bb.put(b);
	}
}