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

import java.util.HashMap;

/**
 * @author Nathanael Van Vorst
 *
 */
public enum CommandType {
	BLOCKING_CMD_RESULT(96),
	NON_BLOCKING_CMD_RESULT(97),
	CONNNECT_SLAVES(98),
	SETUP_SLAVES(99),
	SETUP_CONTAINER(100),
	SETUP_EXPERIMENT(101),
	START_EXPERIMENT(102),
	START_GATEWAY(103),
	SETUP_SLAVE(104),
	HOST_CMD(105),
	MONITOR_CMD(106),
	SHUTOWN_CMD(107),
	STATE_EXCHANGE_CMD(108),
	COM_PART_ADVERT_CMD(109),
	CREATE_DYNAMIC_MODEL_NODE(110),
	AREA_OF_INTEREST_UPDATE(111),
	VIZ_EXPORT_RATE_UPDATE(112),
	UPDATE_BYTES_FROM_APP_CMD(113),
	SEND_UPDATE_TO_ACTUATOR_CMD(114),
	//these commands are only sent/received on the Prime simulator channel
	//the values must be synchronized with the simulator (by hand)
	PRIME_STATE_EXCHANGE(1000),
	PRIME_CREATE_NODE(1001),
	PRIME_AREA_OF_INTEREST_UPDATE(1002),
	PRIME_VIZ_EXPORT_RATE_UPDATE(1003)
	;
	
	private final int v;
	private static final HashMap<Integer, CommandType> map;
	static {
		map = new HashMap<Integer, CommandType>();
		for(CommandType ct : values()) {
			map.put(ct.v,ct);
		}
	}
	
	private CommandType(int val){
		this.v = val;
	}
	
	public int getType() {
		return this.v;
	}
	
	public static CommandType fromInt(int i) {
		return map.get(i);
	}	
}
