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

/**
 * @author Nathanael Van Vorst
 *
 */
public class EntityFactory extends jprime.gen.EntityFactory {
	public static final int Experiment=1;
	public static final int Metadata=2;
	public static final int SymbolTable=3;
	public static final int BGPLinkType = 4;
	public static final int EmulationCommand = 5;
	public static final int StaticRoutingProtocolStart=6;
	public static final int RouteEntry=7;
	public static final int BGP=8;
	public static final int ShortestPath=9;
	public static final int AlgorithmicRouting=10;
	public static final int StaticRoutingProtocolEND=11;
	
	//XXX 50 through 60 are used by TLV types
	//see jprime.gen.TLV
	
	public static final int ModelNodeVaribaleStart=99;
	public static final int BooleanVariable=100;
	public static final int FloatingPointNumberVariable=101;
	public static final int IntegerVariable=102;
	public static final int ListVariable=103;
	public static final int OpaqueVariable=104;
	public static final int ResourceIdentifierVariable=105;
	public static final int StringVariable=106;
	public static final int SymbolVariable=107;
	public static final int ModelNodeVaribaleEnd=108;
	
	public static final int START_MODEL_NODE_TYPES=999; //we start generating ids at 1000
	
	
	
	
	public static jprime.routing.StaticRoutingProtocol createStaticRoutingProtocol(int type_id, ModelNodeRecord rec){
		switch(type_id){
		case BGP: return new jprime.routing.BGP(rec);
		case ShortestPath: return new jprime.routing.ShortestPath(rec);
		case AlgorithmicRouting: return new jprime.routing.AlgorithmicRouting(rec); 
		default:
			throw new RuntimeException("invalid type id!");
		}
	}
	
	
	public static String getString(int type_id){
		switch(type_id){

			case 0: return new String("Experiment");
			case 1: return new String("Library");
			case 2: return new String("Metadata");
			case 3: return new String("SymbolTable");
			case 4: return new String("BGPLinkType ");
			case 5: return new String("EmulationCommand ");
			case 7: return new String("RouteEntry");
			case 8: return new String("BGP");
			case 9: return new String("ShortestPath");
			case 10: return new String("AlgorithmicRouting");
			case 100: return new String("BooleanVariable");
			case 101: return new String("FloatingPointNumberVariable");
			case 102: return new String("IntegerVariable");
			case 103: return new String("ListVariable");
			case 104: return new String("OpaqueVariable");
			case 105: return new String("ResourceIdentifierVariable");
			case 106: return new String("StringVariable");
			case 107: return new String("SymbolVariable");
			default:
				return jprime.gen.EntityFactory.getString(type_id);
		}
	}
}