import java.util.ArrayList;
import java.util.Map;

import jprime.Experiment;
import jprime.Interface.IInterface;
import jprime.Link.ILink;
import jprime.Link.Link;
import jprime.Net.INet;
import jprime.Net.Net;
import jprime.Router.IRouter;
import jprime.database.Database;
import jprime.routing.BGP;
import jprime.routing.BGPRelationShipType;
import jprime.util.ModelInterface;

/**
 * @author Nathanael Van Vorst
 */
public class SimpleBGP extends ModelInterface{

	/**
	 * @param db
	 * @param exp
	 */
	public SimpleBGP(Database db, Experiment exp) {
		super(db, exp, new ArrayList<ModelParam>());
	}

	/**
	 * @param db
	 * @param expName
	 */
	public SimpleBGP(Database db, String expName) {
		super(db, expName, new ArrayList<ModelParam>());
	}

	/* (non-Javadoc)
	 * @see jprime.ModelInterface#buildModel()
	 */
	@Override
	public INet buildModel(Map<String, ModelParamValue> parameters) {
		Net top = exp.createTopNet("topnet");
		//top.createShortestPath();
		BGP bgp=top.createBGP();
		
		IRouter r[] = new IRouter[9];
		for(int i=1 ; i<9 ; i++){
			INet sub=top.createNet("n"+i);
			r[i] = sub.createRouter("r"+i);		
		}
	
		IInterface r1_r2= r[1].createInterface("if0");
		IInterface r1_r8= r[1].createInterface("if1");
		
		IInterface r2_r1= r[2].createInterface("if0");
		IInterface r2_r3= r[2].createInterface("if1");
		IInterface r2_r5= r[2].createInterface("if2");
		
		IInterface r3_r2= r[3].createInterface("if0");
		IInterface r3_r4= r[3].createInterface("if1");
		IInterface r3_r6= r[3].createInterface("if2");
		
		IInterface r4_r3= r[4].createInterface("if0");
		IInterface r4_r6= r[4].createInterface("if1");
		
		IInterface r5_r2= r[5].createInterface("if0");
		IInterface r5_r7= r[5].createInterface("if1");
		
		IInterface r6_r3= r[6].createInterface("if0");
		IInterface r6_r4= r[6].createInterface("if1");
		IInterface r6_r7= r[6].createInterface("if2");
		
		IInterface r7_r5= r[7].createInterface("if0");
		IInterface r7_r6= r[7].createInterface("if1");
		IInterface r7_r8= r[7].createInterface("if2");
		
		IInterface r8_r1= r[8].createInterface("if0");
		IInterface r8_r7= r[8].createInterface("if1");
		
		ILink l_r1_r2= top.createLink("r1_r2");
		l_r1_r2.createInterface(r1_r2);
		l_r1_r2.createInterface(r2_r1);	
		
		ILink l_r1_r8= top.createLink("r1_r8");
		l_r1_r8.createInterface(r1_r8);
		l_r1_r8.createInterface(r8_r1);
	
		ILink l_r2_r3= top.createLink("r2_r3");
		l_r2_r3.createInterface(r2_r3);
		l_r2_r3.createInterface(r3_r2);
		
		ILink l_r2_r5= top.createLink("r2_r5");
		l_r2_r5.createInterface(r2_r5);
		l_r2_r5.createInterface(r5_r2);
		
		ILink l_r3_r4= top.createLink("r3_r4");
		l_r3_r4.createInterface(r3_r4);
		l_r3_r4.createInterface(r4_r3);
		
		ILink l_r3_r6= top.createLink("r3_r6");
		l_r3_r6.createInterface(r3_r6);
		l_r3_r6.createInterface(r6_r3);
		
		ILink l_r4_r6= top.createLink("r4_r6");
		l_r4_r6.createInterface(r4_r6);
		l_r4_r6.createInterface(r6_r4);
		
		ILink l_r5_r7= top.createLink("r5_r7");
		l_r5_r7.createInterface(r5_r7);
		l_r5_r7.createInterface(r7_r5);
		
		ILink l_r6_r7= top.createLink("r6_r7");
		l_r6_r7.createInterface(r6_r7);
		l_r6_r7.createInterface(r7_r6);
		
		ILink l_r7_r8= top.createLink("r7_r8");
		l_r7_r8.createInterface(r7_r8);
		l_r7_r8.createInterface(r8_r7);


		bgp.createLinkType((Link)l_r1_r2, BGPRelationShipType.C2P, true);
		bgp.createLinkType((Link)l_r1_r8, BGPRelationShipType.C2P, true);	
		bgp.createLinkType((Link)l_r2_r3, BGPRelationShipType.P2P, true);
		bgp.createLinkType((Link)l_r2_r5, BGPRelationShipType.C2P, true);	
		bgp.createLinkType((Link)l_r3_r4, BGPRelationShipType.P2P, true);
		bgp.createLinkType((Link)l_r3_r6, BGPRelationShipType.C2P, true);
		bgp.createLinkType((Link)l_r4_r6, BGPRelationShipType.C2P, true);
		bgp.createLinkType((Link)l_r5_r7, BGPRelationShipType.C2P, true);
		bgp.createLinkType((Link)l_r6_r7, BGPRelationShipType.P2P, false);
		bgp.createLinkType((Link)l_r7_r8, BGPRelationShipType.P2P, true);
		
		
		return top;
	}

}
