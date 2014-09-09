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

import java.io.FileOutputStream;
import java.nio.charset.CharacterCodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import jprime.util.NetAggPair;
import monitor.commands.AbstractCmd;
import monitor.commands.CommandType;
import monitor.commands.SetupExperimentCmd;
import monitor.util.Utils;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;
/**
 * @author Nathanael Van Vorst
 *
 */
public class SetupExperimentDecoder extends AbstractCmdDecoder {
	private int remainingTlvs;
	private long remaining;
	private boolean have_exp_header;
	private List<SetupExperimentCmd.TLVFile> tlvs;
	private SetupExperimentCmd.TLVFile curTlv;
	private Integer curTlv_header_size;
	private FileOutputStream fos;
	private String name,runtimeSymbols;
	private TreeMap<NetAggPair, Set<Integer>> aggMap;
	public SetupExperimentDecoder() {
		super(CommandType.SETUP_EXPERIMENT);
		this.remainingTlvs=0;
		this.tlvs = new ArrayList<SetupExperimentCmd.TLVFile>();
		this.curTlv=null;
		this.fos=null;
		this.have_exp_header=false;
		this.curTlv_header_size=null;
	}
	public MessageDecoderResult decode(IoSession session, IoBuffer in,
			ProtocolDecoderOutput out) throws Exception {
		//System.out.println("DECODING SetupExperimentCmd, readHeader="+readHeader);
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
				try {
					name = in.getString(in.getInt(),csd);
					runtimeSymbols = in.getString(in.getInt(),csd);
				} catch (CharacterCodingException e) {
					throw new RuntimeException(e);
				}
				//System.out.println("DECODING name="+name+", runtimeSymbols="+runtimeSymbols);
				//System.out.println("DECODING start SetupExperimentCmd aggMap");
				aggMap = new TreeMap<NetAggPair, Set<Integer>>();
				int aggs = in.getInt();
				//System.out.println("\tDECODING "+aggs+" aggs");
				for(int agg=0;agg<aggs;agg++) {
					NetAggPair p = new NetAggPair(in.getLong());
					//System.out.println("\t\ton agg pair "+agg+", netuid="+p.net_uid);
					int agg_uid_size = in.getInt();
					//System.out.println("\t\t\tDECODING "+agg_uid_size+" uids");
					for(int agg_uid=0;agg_uid < agg_uid_size;agg_uid++) {
						p.agg_uids.add(in.getLong());
					}
					int align_size= in.getInt();
					//System.out.println("\t\t\tDECODING "+align_size+" alings");
					HashSet<Integer> l = new HashSet<Integer>(align_size);
					for(int j=0;j<align_size;j++) {
						l.add(in.getInt());
					}
					aggMap.put(p, l);
				}
				//System.out.println("DECODING finished SetupExperimentCmd aggMap");
				remainingTlvs = in.getInt();
				//System.out.println("DECODING reading "+remainingTlvs+" tlvs");
				have_exp_header=true;
			}
			else {
				return MessageDecoderResult.NEED_DATA;
			}
		}
		//read in all the tlvs
		while(in.remaining()>0 && remainingTlvs>0) {
			if(curTlv == null) {
				if(curTlv_header_size == null) {
					if( in.remaining() < Integer.SIZE/8) {
						return MessageDecoderResult.NEED_DATA;
					}
					else {
						curTlv_header_size = in.getInt();
					}
				}
				if( in.remaining() < curTlv_header_size) {
					return MessageDecoderResult.NEED_DATA;					
				}
				//new tlv
				int fnlength = curTlv_header_size - (Integer.SIZE*2+Long.SIZE)/8;
				int part_id = in.getInt();
				String fn =null;
				try {
					fn = in.getString(fnlength,csd);
				} catch (CharacterCodingException e) {
					throw new RuntimeException(e);
				}
				long length=in.getLong();
				remaining=length;
				String path = Utils.BASE_EXP_DIR+"/"+name+"/"+fn;
				//System.out.println("Starting decode of tlv, part_id="+part_id+", length="+length+", path="+path);
				curTlv = new SetupExperimentCmd.TLVFile(part_id, path, length);
				fos = new FileOutputStream(curTlv.path);
			}
			remaining = curTlv.decode(fos, in, remaining);
			if(0 == remaining) {
				//we finished this one
				tlvs.add(curTlv);
				fos.close();
				fos=null;
				remaining=0;
				curTlv=null;
				curTlv_header_size = null;
				remainingTlvs--;
			}
		}
		if(remainingTlvs>0) {
			//System.out.println("DECODING read tlv, have "+remainingTlvs+" tlvs remaining");
			return MessageDecoderResult.NEED_DATA;
		}
		//we have all the tlvs, lets make the command
		AbstractCmd m = new SetupExperimentCmd(name,runtimeSymbols,tlvs,aggMap);
		readHeader = false; // reset readHeader for the next decode
		have_exp_header = false; 
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
