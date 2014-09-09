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


/**
 * @author Nathanael Van Vorst
 *
 */
public class ComPartAdvertCmd extends AbstractCmd{
	private final int part_id;
	private final int com_id;
	private final boolean reader;
	
	public ComPartAdvertCmd(boolean reader,int part_id, int com_id) { //only used by the prime session handler
    	super(CommandType.COM_PART_ADVERT_CMD);
    	this.reader=reader;
    	this.com_id=com_id;
    	this.part_id=part_id;
	}

	/**
	 * @return the reader
	 */
	public boolean isReader() {
		return reader;
	}

	/**
	 * @return the part_id
	 */
	public int getPart_id() {
		return part_id;
	}



	/**
	 * @return the com_id
	 */
	public int getCom_id() {
		return com_id;
	}



	@Override
	public String toString() {	
		return "[ComPartAdvert reader="+reader+", part="+part_id+", com="+com_id+"]";
	}

	@Override
	public int getBodyLength()
	{
		return (Integer.SIZE*3)/8;
	}
}
