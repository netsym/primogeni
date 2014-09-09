package jprime;

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

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import jprime.Host.IHost;
import jprime.Interface.IInterface;
import jprime.Net.INet;
import jprime.database.PKey;


/**
 * @author Nathanael Van Vorst
 */
public class EmulationCommand extends PersistableObject implements Comparable<EmulationCommand> { 
	public static int BLOCK=0;
	public static int USE_RUNTIME=-1;
	private static ScriptEngine engine = null;
	
	//Transient
	private SoftReference<IHost> __parent = null;
	private String process_cmd = null;
	private long maxRuntime=-1;
	private Metadata meta;
	
	//persisted
	protected long dbid;
	protected long metadata_id;
	protected long parent_id;
	private String raw_cmd;
	private String outputSuffix;
	private long delay;
	private long raw_maxRuntime;
	private boolean block;
	private boolean checkReturnCode;
	
	/**
	 * Used when loaded from the DB
	 */
	public EmulationCommand(Metadata meta, long dbid, IHost parent, String raw_cmd,
			String outputSuffix, long delay, long raw_maxRuntime, boolean block, boolean checkReturnCode) {
		super();
		if(raw_cmd == null || raw_cmd.length()==0)
			throw new RuntimeException("Null or empty emulation command!");
		this.checkReturnCode=checkReturnCode;
		this.meta=meta;
		this.metadata_id=meta.getDBID();
		this.dbid = dbid;
		if(parent == null) {
			this.__parent=null;
			this.parent_id = 0;
		}
		else {
			this.__parent = new SoftReference<IHost>(parent);
			this.parent_id = parent.getDBID();
		}
		this.raw_cmd = raw_cmd;
		this.outputSuffix = outputSuffix;
		this.delay = delay;
		this.raw_maxRuntime = raw_maxRuntime;
		this.block = block;	
		if(dbid==0) {
			this.dbid=meta.getNextModelNodeDBID();
			this.persistable_state=PersistableState.NEW;
			this.mods=Modified.ALL.id;
		}
		else {
			this.persistable_state=PersistableState.UNMODIFIED;
			this.mods=Modified.NOTHING.id;
		}
		if(parent!=null)
			parent.__addEmulationCommand(this);
		
		meta.loaded(this);
	}
	
	/**
	 * Used when loaded from the DB
	 */
	public EmulationCommand(Metadata meta, long dbid, Experiment parent, String raw_cmd,
			String outputSuffix, long delay, long raw_maxRuntime, boolean block, boolean checkReturnCode) {
		super();
		if(raw_cmd == null || raw_cmd.length()==0)
			throw new RuntimeException("Null or empty emulation command!");
		this.checkReturnCode=checkReturnCode;
		this.meta=meta;
		this.metadata_id=meta.getDBID();
		this.dbid = dbid;
		if(parent == null) {
			this.__parent=null;
			this.parent_id = 0;
		}
		else {
			this.__parent = null;
			this.parent_id = 0;
		}
		this.raw_cmd = raw_cmd;
		this.outputSuffix = outputSuffix;
		this.delay = delay;
		this.raw_maxRuntime = raw_maxRuntime;
		this.block = block;	
		if(dbid==0) {
			this.dbid=meta.getNextModelNodeDBID();
			this.persistable_state=PersistableState.NEW;
			this.mods=Modified.ALL.id;
		}
		else {
			this.persistable_state=PersistableState.UNMODIFIED;
			this.mods=Modified.NOTHING.id;
		}
		if(parent!=null)
			parent.__addEmulationCommand(this);
		
		meta.loaded(this);
	}
	
	/**
	 * @param parent
	 * @param cmd
	 * @param outputSuffix
	 * @param delay
	 * @param maxRuntime
	 * @param unit
	 */
	public EmulationCommand(IHost parent, String cmd, String outputSuffix, long delay, long maxRuntime, TimeUnit unit, boolean checkReturnCode) {
		this(parent,cmd,outputSuffix,delay,maxRuntime,false,unit,checkReturnCode);
	}

	/**
	 * @param exp
	 * @param cmd
	 * @param outputSuffix
	 * @param delay
	 * @param maxRuntime
	 * @param unit
	 */
	public EmulationCommand(Experiment exp, String cmd, String outputSuffix, long delay, long maxRuntime, TimeUnit unit, boolean checkReturnCode) {
		this(exp,cmd,outputSuffix,delay,maxRuntime,false,unit,checkReturnCode);
		exp.__addEmulationCommand(this);
	}
	
	/**
	 * @param parent
	 * @param cmd
	 * @param outputSuffix
	 * @param delay
	 * @param maxRuntime
	 * @param block
	 * @param unit
	 */
	public EmulationCommand(IHost parent, String cmd, String outputSuffix, long delay, long maxRuntime, boolean block, TimeUnit unit, boolean checkReturnCode) {
		this(parent.getMetadata(), cmd,outputSuffix,delay,maxRuntime,block,unit,checkReturnCode);
		this.parent_id=parent.getDBID();
		this.__parent=new SoftReference<IHost>(parent);
		parent.__addEmulationCommand(this);
	}

	/**
	 * @param exp
	 * @param cmd
	 * @param outputSuffix
	 * @param delay
	 * @param maxRuntime
	 * @param block
	 * @param unit
	 */
	public EmulationCommand(Experiment exp, String cmd, String outputSuffix, long delay, long maxRuntime, boolean block, TimeUnit unit, boolean checkReturnCode) {
		this(exp.getMetadata(), cmd,outputSuffix,delay,maxRuntime,block,unit,checkReturnCode);
		this.parent_id=0;
		this.__parent=null;
	}

	
	/**
	 * @param parent
	 * @param cmd
	 * @param outputSuffix
	 * @param delay
	 * @param maxRuntime
	 * @param block
	 * @param unit
	 */
	private EmulationCommand(Metadata meta, String cmd, String outputSuffix, long delay, long maxRuntime, boolean block, TimeUnit unit, boolean checkReturnCode) {
		super();
		this.checkReturnCode=checkReturnCode;
		this.raw_cmd = cmd;
		if(raw_cmd == null || raw_cmd.length()==0)
			throw new RuntimeException("Null or empty emulation command!");
		this.outputSuffix = outputSuffix==null?"":outputSuffix;
		switch(unit) {
		case DAYS:
			this.delay = delay*24*60*60*60*1000;
			this.raw_maxRuntime=maxRuntime*24*60*60*60*1000;
			break;
		case HOURS:
			this.delay = delay*60*60*1000;
			this.raw_maxRuntime=maxRuntime*60*60*1000;
			break;
		case MINUTES:
			this.delay = delay*60*1000;
			this.raw_maxRuntime=maxRuntime*60*1000;
			break;
		case SECONDS:
			this.delay = delay*1000;
			this.raw_maxRuntime=maxRuntime*1000;
			break;
		case MILLISECONDS:
			this.delay = delay;
			this.raw_maxRuntime=maxRuntime;
			break;
		case MICROSECONDS:
			this.delay = delay>=1000?(delay/1000):0;
			this.raw_maxRuntime=maxRuntime>=1000?(maxRuntime/1000):0;
			break;
		case NANOSECONDS:
			this.delay = delay>=1000000?(delay/1000000):0;
			this.raw_maxRuntime=maxRuntime>=1000000?(maxRuntime/1000000):0;
			break;
		default:
			throw new RuntimeException("unknown unit "+unit);
		}
		this.block=block;
		this.meta=meta;
		this.metadata_id=meta.getDBID();
		this.dbid=meta.getNextModelNodeDBID();
		this.persistable_state=PersistableState.NEW;
		this.mods=Modified.ALL.id;
		meta.loaded(this);
	}

	@Override
	protected void finalize() throws Throwable {
		meta.collect(this);
		super.finalize();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof EmulationCommand) {
			return this.getMetadataId() == ((EmulationCommand)obj).getMetadataId() && getDBID() ==  ((EmulationCommand)obj).getDBID();
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see jprime.IPeristable#getMetadata()
	 */
	public Metadata getMetadata() {
		return meta;
	}
	
	/* (non-Javadoc)
	 * @see jprime.IPeristable#save()
	 */
	public synchronized void save() {
		meta.save(this);
	}

	/* (non-Javadoc)
	 * @see jprime.PeristableObject#getPKey()
	 */
	public PKey getPKey() {
		return new PKey(metadata_id, dbid);
	}
	
	/**
	 * @return
	 */
	public String getRawCmd() {
		return raw_cmd;
	}
	
	/**
	 * @return
	 */
	public String getCmd() {
		return process_cmd;
	}
	
	/**
	 * @return
	 */
	public String getOutputSuffix() {
		return outputSuffix;
	}
	
	/**
	 * @return
	 */
	public long getDelay() {
		return delay;
	}
	
	/**
	 * @return
	 */
	public long getMaxRuntime() {
		return maxRuntime;
	}
	
	/**
	 * @return
	 */
	public long getRawMaxRuntime() {
		return raw_maxRuntime;
	}
	
	/**
	 * @return
	 */
	public boolean isBlocking() {
		return block;
	}
	
	/**
	 * @return
	 */
	public boolean shouldCheckReturnCode() {
		return checkReturnCode;
	}
	
	/**
	 * @return
	 */
	public IHost getParent() {
		if(__parent==null || __parent.get()==null) {
			if(parent_id==0)
				return null;
			__parent=new SoftReference<IHost>((IHost)meta.loadModelNode(parent_id));
		}
		return __parent.get();
	}
	
	/**
	 * @return
	 */
	public long getDBID() {
		return dbid;
	}
	
	
	/**
	 * @return
	 */
	public long getMetadataId() {
		return metadata_id;
	}

	/**
	 * @return
	 */
	public long getParentId() {
		return parent_id;
	}
	
	/* (non-Javadoc)
	 * @see jprime.IPeristable#getTypeId()
	 */
	public int getTypeId() {
		return EntityFactory.EmulationCommand;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		//if(process_cmd==null)
			return "[(RAW)Cmd='"+raw_cmd+"' suffix='"+outputSuffix+"', delay="+delay+"ms, isBlocking="+block+", maxRuntime="+raw_maxRuntime+"]";
		//else
		//	return "[Cmd='"+process_cmd+"' suffix='"+outputSuffix+"', delay="+delay+"ms, isBlocking="+block+", maxRuntime="+maxRuntime+"]";
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(EmulationCommand o) {
		if(o.delay == delay) {
			int c = raw_cmd.compareTo(o.raw_cmd);
			if(c==0) {
				c=outputSuffix.compareTo(o.outputSuffix);
				if(c == 0) {
					if(block == o.block)
						return 0;
					else if(block)
						return 1;
					return -1;
				}
			}
			return c;
		}
		else if(delay < o.delay) {
			return -1;
		}
		return 1;
	}
	
	public void delete() {
		final IHost p = getParent();
		if(p != null)
			p.deleteEmulationCommand(this);
		parent_id=0;
		__parent=null;
		orphan();
	}
	
	/* (non-Javadoc)
	 * @see jprime.PersistableObject#orphan()
	 */
	public void orphan() {
		switch(this.persistable_state) {
		case UNMODIFIED:
		case MODIFIED:
			this.persistable_state=PersistableState.DEAD;
			modified(Modified.ALL);
			break;
		case NEW:
			this.persistable_state=PersistableState.ORPHAN;
			modified(Modified.ALL);
			break;
		case DEAD:
			throw new RuntimeException("does this happen? nate things this should be a no-op like orphan,  but he is not sure yet....");
		case ORPHAN:
			//no op
		}
	}
	
	/**
	 * @param topnet
	 * @param runtime
	 */
	public void evaluateCmd(INet topnet, int runtime) {
		//jprime.Console.out.println("evaluating "+this);
		this.process_cmd = processIDs(processRuntime(raw_cmd, runtime, getParent()), topnet, getParent());
		if(raw_maxRuntime<0) maxRuntime=runtime*1000-delay;
		else maxRuntime=raw_maxRuntime;
		if(maxRuntime>0 && (runtime*1000) < (maxRuntime+delay)) {
			jprime.Console.out.println("The emulation command "+this+" would have run long; the max runtime was adjusted so it does not.");
			maxRuntime=maxRuntime-delay;
		}
		//jprime.Console.out.println("\tresult="+this);
	}
	
	/**
	 * @param e
	 * @return
	 */
	private static int eval(String e) {
		try {
			if(engine==null) {
				ScriptEngineManager mgr = new ScriptEngineManager();
				engine = mgr.getEngineByName("JavaScript");
			}
			Object v =engine.eval(e);
			if(v instanceof Double) {
				return ((Double)v).intValue();
			}
			else if(v instanceof Integer) {
				return (Integer)v;
			}
			else {
				return Integer.MIN_VALUE;
			}
		} catch (ScriptException e1) {
			return Integer.MIN_VALUE;
		}
	}
	  
	/**
	 * @param raw_cmd
	 * @param runtime
	 * @param parent
	 * @return
	 */
	private static String processRuntime(String raw_cmd, int runtime, IModelNode parent) {
		//jprime.Console.out.println("\tevaluating runtimes in "+raw_cmd);
		String process_cmd=raw_cmd.replaceAll("$$RUNTIME", ""+runtime);
		ArrayList<int[]> subs=new ArrayList<int[]>();
		int idx = raw_cmd.indexOf("$${");
		int end;
		while(idx>0) {
			end=idx+3;
			while(end<raw_cmd.length()) {
				char c = raw_cmd.charAt(end);
				if(c=='}') break;
				end++;
			}
			if(end>=raw_cmd.length())end=raw_cmd.length();
			else end++;
			subs.add(new int[]{idx,end});
			if(end<raw_cmd.length()) {
				idx = raw_cmd.indexOf("$${",end);
			}
			else {
				break;
			}
		}
		if(subs.size()>0) {
			end=0;
			process_cmd="";
			for(int[] s: subs) {
				final Integer q = eval(raw_cmd.substring(s[0]+3,s[1]-1).replaceAll("RUNTIME", ""+runtime));
				//jprime.Console.out.println("\t\tfound runtime "+raw_cmd.substring(s[0],s[1])+", q="+q);
				if(end==0) {
					if(s[0]>0)
						process_cmd+=raw_cmd.substring(0,s[0]);
				}
				else {
					process_cmd+=raw_cmd.substring(end,s[0]);
				}
				if(q == Integer.MIN_VALUE) {
					jprime.Console.out.println("The emulation command "+raw_cmd+", attached to "+parent.getUniqueName()
							+" contains the an invalid runtime("+
							raw_cmd.substring(s[0]+3,s[1]-1).replaceAll("RUNTIME", ""+runtime)
							+"). Using runtime instead!");
					process_cmd+=runtime;
				}
				else {
					process_cmd+=q;
				}
				end=s[1];
			}
			if(end<raw_cmd.length())
				process_cmd+=raw_cmd.substring(end);
		}
		else {
			process_cmd=raw_cmd;
		}
		//jprime.Console.out.println("\t\trv="+process_cmd);
		return process_cmd;
	}
	
	/**
	 * @param raw_cmd
	 * @param topnet
	 * @param parent
	 * @return
	 */
	private static String processIDs(String raw_cmd, INet topnet, IModelNode parent) {
		//jprime.Console.out.println("\tevaluating ids in "+raw_cmd);
		String process_cmd=null;
		ArrayList<int[]> subs=new ArrayList<int[]>();
		int idx = raw_cmd.indexOf("$$");
		int end;
		while(idx>0) {
			end=idx+2;
			while(end<raw_cmd.length()) {
				char c = raw_cmd.charAt(end);
				if(Character.isLetter(c)||Character.isDigit(c)||c==':'||c=='_'||c=='-') {
					end++;
				}
				else break;
			}
			if(end>=raw_cmd.length())end=raw_cmd.length();
			else end++;
			subs.add(new int[]{idx,end});
			if(end<raw_cmd.length()) {
				idx = raw_cmd.indexOf("$$",end);
			}
			else {
				break;
			}
		}
		if(subs.size()>0) {
			end=0;
			process_cmd="";
			for(int[] s: subs) {
				final String q = raw_cmd.substring(s[0]+topnet.getName().length()+3,s[1]).replace(":", ".");
				//jprime.Console.out.println("\t\tfound id "+raw_cmd.substring(s[0],s[1])+", q="+q);
				if(end==0) {
					if(s[0]>0)
						process_cmd+=" "+raw_cmd.substring(0,s[0]);
				}
				else {
					process_cmd+=" "+raw_cmd.substring(end,s[0]);
				}
				IModelNode n=topnet.getChildByQuery(q);
				if(n==null) {
					jprime.Console.out.println("The emulation command "+raw_cmd+", attached to "+parent.getUniqueName()
							+" contains the an ID which refers to no known interface (ID='"
							+raw_cmd.substring(s[0],s[1])+"'). Using 0.0.0.0 instead!");
					process_cmd+="0.0.0.0";
				}
				else if(!(n instanceof IInterface)) {
					jprime.Console.out.println("The emulation command "+raw_cmd+", attached to "+parent.getUniqueName()
							+" contains the an ID which refers to a "+n.getClass().getSimpleName()
							+" when only interfaces are allowed (ID='"+raw_cmd.substring(s[0],s[1])+"'). Using 0.0.0.0 instead!");
					process_cmd+="0.0.0.0";
				}
				else {
					process_cmd+=" "+((IInterface)n).getIpAddress();
				}
				end=s[1];
			}
			if(end<raw_cmd.length())
				process_cmd+=" "+raw_cmd.substring(end);
		}
		else {
			process_cmd=raw_cmd;
		}
		//jprime.Console.out.println("\t\trv="+process_cmd);
		return process_cmd;
	}	
}
