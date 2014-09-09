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
import java.io.FileOutputStream;

import monitor.commands.AbstractCmd;
import monitor.commands.CommandType;
import monitor.commands.StartGatewayCmd;
import monitor.util.Utils;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;
/**
 * @author Nathanael Van Vorst
 *
 */
public class StartGatewayDecoder extends AbstractCmdDecoder {
	private boolean have_exp_header;
	private long remaining;
	private String tarball, expname;
	private FileOutputStream fos;
	private File file;
	private byte[] buf;
	public StartGatewayDecoder() {
		super(CommandType.START_GATEWAY);
		this.have_exp_header=false;
		this.remaining=-1;
		this.tarball=null;
		this.expname=null;
		this.file=null;
		this.fos=null;
		this.buf=new byte[1024];
	}
	public MessageDecoderResult decode(IoSession session, IoBuffer in,
			ProtocolDecoderOutput out) throws Exception {
		//System.out.println("DECODING StartGateway, readHeader="+readHeader);
		// Try to skip header if not read.
		if (!readHeader) {
			in.getInt(); // Skip 'type'.
			serialNumber = in.getInt(); // Get 'sequence'.
			machineId = in.getInt(); // Get 'slaveId'.
			cmdLength = in.getInt(); // Get 'length'.
			//System.out.println("DECODING sequence=" + serialNumber+", machineId="+machineId+", cmdLength="+cmdLength+", in.remaining="+in.remaining());
			readHeader = true;
		}
		if(!have_exp_header) {
			if(in.remaining()>=cmdLength) {
				tarball = in.getString(in.getInt(),csd);
				expname = in.getString(in.getInt(),csd);
				remaining = in.getLong();
				File d = new File(Utils.BASE_EXP_DIR+"/"+expname);
				if(!d.exists()) {
					d.mkdir();
				}
				else if(!d.isDirectory()) {
					throw new RuntimeException("wtf?");
				}
				file = new File(Utils.BASE_EXP_DIR+"/"+expname+"/"+tarball);
				fos = new FileOutputStream(file);
				have_exp_header=true;
			}
			else {
				return MessageDecoderResult.NEED_DATA;
			}
		}
		//read in all the tlvs
		while(in.remaining()>0 && remaining>0) {
			int get = (remaining>1024)?1024:(int)remaining;
			get=get>in.remaining()?in.remaining():get;
			in.get(buf,0,get);
			fos.write(buf,0,get);
			remaining-=get;
		} 
		if(remaining>0) {
			return MessageDecoderResult.NEED_DATA;
		}
		fos.close();
		//we have all the tlvs, lets make the command
		AbstractCmd m = new StartGatewayCmd(file, expname);
		readHeader = false; // reset readHeader for the next decode
		have_exp_header = false; 
		this.remaining=-1;
		this.tarball=null;
		this.expname=null;
		this.file=null;
		this.fos=null;
		//deliver the new message
		m.setSerialNumber(serialNumber);
		m.setMachineId(machineId);
		//System.out.println("\tfinished cmd="+m+", in.remaining="+in.remaining());
		out.write(m);
		return MessageDecoderResult.OK;
	}    
	@Override
	protected AbstractCmd decodeBody(IoSession session, IoBuffer in, int length) {
		throw new RuntimeException("should not call this on setup exp");
	}
	public void finishDecode(IoSession session, ProtocolDecoderOutput out) throws Exception{
	}
}
