package monitor.commands;

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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import jprime.util.NetAggPair;

import org.apache.mina.core.buffer.IoBuffer;

/**
 * @author Nathanael Van Vorst
 * 
 * XXX nate will do this one after the break.
 * 
 * -- there are some issues with tranfering a large file....
 */
public class SetupExperimentCmd extends AbstractCmd{
	public static class TLVFile implements Comparable<TLVFile> {
		private static final CharsetEncoder cse = Charset.forName("UTF-8").newEncoder();
		public int part_id;
		public String filename;
		public String path;
		public long length;
		public TLVFile(int part_id, String fn, long length) throws IOException  {
			this.part_id=part_id;
			this.path=fn;
			File file = new File(path);
			//System.out.println("Creating file "+file.getPath());
			if(file.getParentFile() !=null)
				file.getParentFile().mkdirs();
			file.createNewFile();
			this.filename=file.getName();
			this.length=length;
		}
		public TLVFile(int part_id, String fn) {
			super();
			this.part_id=part_id;
			File file = new File(fn);
			this.filename = file.getName();
			this.path = file.getAbsolutePath();
			this.length = file.length();
		}

		public long getFileSize() {
			return length;
		}

		public long getLength() {
			return length+(Long.SIZE+Integer.SIZE)/8+filename.length();
		}
		
		public long decode(FileOutputStream fos, IoBuffer in, long remaining) {
			//System.out.println("Decoding tlv, part_id="+part_id+", length="+length+", path="+path);
			try {
				byte[] buf=new byte[1024];
				while(remaining>0 && in.remaining()>0) {
					int get = (remaining>1024)?1024:(int)remaining;
					get=get>in.remaining()?in.remaining():get;
					//System.out.println("\tgetting "+get+" bytes");
					in.get(buf,0,get);
					fos.write(buf,0,get);
					remaining-=get;
				} 
				//System.out.println("\tProgress:"+remaining+"/"+length);
				return remaining;
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		
		public void encode(IoBuffer out) {
			//System.out.println("Encoding tlv, part_id="+part_id+", length="+length+", path="+path);
			out.putInt(filename.length()+(Integer.SIZE*2+Long.SIZE)/8);
			out.putInt(part_id);
			try {
				out.putString(filename,filename.length(),cse);
			} catch (CharacterCodingException e) {
				throw new RuntimeException(e);
			}
			out.putLong(length);
			//write the file in chunks of 1024 bytes
			byte[] buf=new byte[1024];
			int offset=0, numRead=0;
			try {
				InputStream is = new FileInputStream(path);
				while (offset <length
						&& (numRead=is.read(buf, 0, 1024)) >= 0) {
					//System.out.println("Read "+numRead+" "+offset+"/"+length);
					//read numRead bytes into buf
					offset += numRead;
					out.put(buf, 0, numRead);
				}
				//System.out.println("Read "+numRead+" "+offset+"/"+length);
				is.close();
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		public int compareTo(TLVFile o) {
			if(part_id == o.part_id)
				return 0;
			else if(part_id < o.part_id)
				return -1;
			return 1;
		}
	}
	private List<TLVFile> tlvs;
	private String name;
	private String runtimeSymbols;
	private TreeMap<NetAggPair, Set<Integer> > aggMap;
	
	public SetupExperimentCmd(String name, String runtimeSymbols, List<TLVFile> tlvs, TreeMap<NetAggPair, Set<Integer> > aggMap ) {
		super(CommandType.SETUP_EXPERIMENT);
		this.name=name;
		this.runtimeSymbols=runtimeSymbols;
		this.tlvs=tlvs;
		this.aggMap=aggMap;
		if(aggMap==null) {
			throw new RuntimeException("wtf?");
		}
	}
	public SetupExperimentCmd(String name, String runtimeSymbols, Map<Integer,String> tlv_fns, TreeMap<NetAggPair, Set<Integer> > aggMap) {
		this(name,runtimeSymbols,toList(tlv_fns),aggMap);
	}
	public SetupExperimentCmd(String name, Map<String,String> runtimeVariables, List<TLVFile> tlvs, TreeMap<NetAggPair, Set<Integer> > aggMap) {
		this(name,makeRuntimeSymbols(runtimeVariables),tlvs,aggMap);
	}
	public SetupExperimentCmd(String name, Map<String,String> runtimeVariables, Map<Integer,String> tlv_fns, TreeMap<NetAggPair, Set<Integer> > aggMap) {
		this(name,makeRuntimeSymbols(runtimeVariables),toList(tlv_fns),aggMap);
	}
	private static List<SetupExperimentCmd.TLVFile> toList(Map<Integer,String> tlv_fns) {
		List<TLVFile> tlvs=new ArrayList<SetupExperimentCmd.TLVFile>();
		if(tlv_fns != null) {
			for(Entry<Integer, String> e : tlv_fns.entrySet()) {
				TLVFile t =new TLVFile(e.getKey(), e.getValue()); 
				tlvs.add(t);
			}
		}
		return tlvs;
	}
	private static String makeRuntimeSymbols(Map<String,String> runtimeSymbols) {
		StringBuffer rv = new StringBuffer();
		boolean first=true;
		for(Entry<String, String> e : runtimeSymbols.entrySet()) {
			if(e.getKey().contains("::")) {
				if(first) {
					rv.append(".defaults\n");
					first=false;
				}
				rv.append(e.getKey()+"="+e.getValue()+"\n");
			}
		}
		first=true;
		for(Entry<String, String> e : runtimeSymbols.entrySet()) {
			if(!e.getKey().contains("::")) {
				if(first) {
					rv.append(".symbols\n");
					first=false;
				}
				rv.append(e.getKey()+"="+e.getValue()+"\n");
			}
		}
		return rv.toString();
	}

	public TreeMap<NetAggPair, Set<Integer> > getAggMap() {
		return aggMap;
	}
	
	public String getName() {
		return name;
	}
	
	public List<TLVFile> getTlvs() {
		return tlvs;
	}
	
	public String getRuntimeSymbols() {
		return runtimeSymbols;
	}

	@Override
	public String toString() {
		return "[SetupExperimentCmd #tlvs="+tlvs.size()+"]";
	}

	@Override
	public int getBodyLength()
	{/*
		int rv=(Integer.SIZE*4)/8+name.length()+runtimeSymbols.length();
		for(Entry<NetAggPair, Set<Integer>> e : aggMap.entrySet()) {
			rv+=(Integer.SIZE*(2+e.getValue().size())+Long.SIZE*(e.getKey().agg_uids.size()))/8;
		}
		return rv;
	*/	
		int rv = (Integer.SIZE*4)/8 + //header
				Integer.SIZE/8 + name.length() + //name length + length 
				Integer.SIZE/8 + runtimeSymbols.length();
		for(Entry<NetAggPair, Set<Integer>> e : aggMap.entrySet()) {
			//rv += ( Integer.SIZE*( 2+e.getValue().size() )
				//	+ Long.SIZE*( e.getKey().agg_uids.size() ) 
				  //) / 8;
			rv += ( (Long.SIZE) + 
					(Integer.SIZE) + (e.getKey().agg_uids.size())*(Long.SIZE) +
					(Integer.SIZE) + (e.getValue().size())*(Integer.SIZE)
					) / 8;		
		}
		return rv;
		
	}

}
