import java.util.Map;
import java.util.Random;

import jprime.Experiment;
import jprime.Host.IHost;
import jprime.Interface.IInterface;
import jprime.Link.ILink;
import jprime.Net.INet;
import jprime.Net.Net;
import jprime.Router.IRouter;
import jprime.TCPTraffic.ITCPTraffic;
import jprime.Traffic.ITraffic;
import jprime.database.Database;
import jprime.util.ModelInterface;

/**
 * @author Miguel A. Erazo
 */
public class PortalsExogeni extends ModelInterface{

    /**
     * @param db
     * @param exp
     */
    public PortalsExogeni(Database db, Experiment exp) {
        super(db, exp);
    }

    /**
     * @param db
     * @param expName
     */
    public PortalsExogeni(Database db, String expName) {
        super(db, expName);
    }

    /* (non-Javadoc)
     * @see jprime.ModelInterface#buildModel()
     */
    @Override
    public INet buildModel(Map<String, ModelParamValue> parameters) {
        Net top = exp.createTopNet("topnet");
        top.createShortestPath();
        Random randomGenerator = new Random();
       
        //net01 includes net0, net1 and two routers
        INet net01 = top.createNet("net01");
        //net01.createShortestPath();
       
        //net0 includes three routers
        INet net0 = net01.createNet("net0");
        //net0.createShortestPath();
       
        IRouter net0_r0 = net0.createRouter("r0");
        IInterface net0_r0_if0 = net0_r0.createInterface("if0");
        IInterface net0_r0_if1 = net0_r0.createInterface("if1");
        IInterface net0_r0_if2 = net0_r0.createInterface("if2");
       
        IRouter net0_r1 = net0.createRouter("r1");
        IInterface net0_r1_if0 = net0_r1.createInterface("if0");
        IInterface net0_r1_if1 = net0_r1.createInterface("if1");
        IInterface net0_r1_if2 = net0_r1.createInterface("if2");
       
        IRouter net0_r2 = net0.createRouter("r2");
        IInterface net0_r2_if0 = net0_r2.createInterface("if0");
        IInterface net0_r2_if1 = net0_r2.createInterface("if1");
        IInterface net0_r2_if2 = net0_r2.createInterface("if2");
       
        //connect the routers in net0
        ILink net0_r0_r1 = net0.createLink("r0_r1");
        net0_r0_r1.createInterface(net0_r0_if1);
        net0_r0_r1.createInterface(net0_r1_if2);
        net0_r0_r1.setDelay((float)0.0001);
       
        ILink net0_r1_r2 = net0.createLink("r1_r2");
        net0_r1_r2.createInterface(net0_r1_if0);
        net0_r1_r2.createInterface(net0_r2_if1);
        net0_r1_r2.setDelay((float)0.0001);
       
        ILink net0_r0_r2 = net0.createLink("r0_r2");
        net0_r0_r2.createInterface(net0_r0_if0);
        net0_r0_r2.createInterface(net0_r2_if2);
        net0_r0_r2.setDelay((float)0.0001);
       
        //net1 includes two routers and 4 hosts
        INet net1 = net01.createNet("net1");
        //net1.createShortestPath();
       
        IRouter net1_r0 = net1.createRouter("r0");
        IInterface net1_r0_if0 = net1_r0.createInterface("if0");
        IInterface net1_r0_if1 = net1_r0.createInterface("if1");
        IInterface net1_r0_if2 = net1_r0.createInterface("if2");
        IInterface net1_r0_if3 = net1_r0.createInterface("if3");
        IInterface net1_r1_emuhost1 = net1_r0.createInterface("if10");
        IInterface net1_r1_emuhost2 = net1_r0.createInterface("if11");
        IInterface net1_r1_openvpnhost = net1_r0.createInterface("if12");      
 
        IRouter net1_r1 = net1.createRouter("r1");
        IInterface net1_r1_if0 = net1_r1.createInterface("if0");
        IInterface net1_r1_if1 = net1_r1.createInterface("if1");
        IInterface net1_r1_if2 = net1_r1.createInterface("portal_10_1_2_0");
 
        IHost net1_h2 = net1.createHost("h2");
        IInterface net1_h2_if0 = net1_h2.createInterface("if0");
       
        IHost net1_h3 = net1.createHost("h3");
        IInterface net1_h3_if0 = net1_h3.createInterface("if0");
       
        IHost net1_h4 = net1.createHost("h4");
        IInterface net1_h4_if0 = net1_h4.createInterface("if0");

		IHost emuhost1 = net1.createHost("emuhost1");
		IInterface emuhost1_if0 = emuhost1.createInterface("if0");
		
		IHost emuhost2 = net1.createHost("emuhost2");
		IInterface emuhost2_if0 = emuhost2.createInterface("if0");
		
		IHost openvpnhost = net1.createHost("openvpnhost");
	    IInterface openvpn_if0 = openvpnhost.createInterface("if0");
		
		ILink emuhost1_link = net1.createLink("emuhost1-routerr1");
	    emuhost1_link.createInterface(emuhost1_if0);
        emuhost1_link.createInterface(net1_r1_emuhost1);
        emuhost1_link.setDelay((float)0.0001);

        ILink emuhost2_link = net1.createLink("emuhost2-routerr1");
        emuhost2_link.createInterface(emuhost2_if0);
        emuhost2_link.createInterface(net1_r1_emuhost2);
        emuhost2_link.setDelay((float)0.0001);

        ILink openvpnhost_link = net1.createLink("openvpnhost-routerr1");
        openvpnhost_link.createInterface(openvpn_if0);
        openvpnhost_link.createInterface(net1_r1_openvpnhost);
        openvpnhost_link.setDelay((float)0.0001);
       
        //connect the routers and hosts in net1
        ILink net1_r0_r1 = net1.createLink("r0_r1");
        net1_r0_r1.createInterface(net1_r0_if2);
        net1_r0_r1.createInterface(net1_r1_if0);
        net1_r0_r1.setDelay((float)0.0001);
       
        ILink net1_r0_h2 = net1.createLink("r0_h2");
        net1_r0_h2.createInterface(net1_r0_if0);
        net1_r0_h2.createInterface(net1_h2_if0);
        net1_r0_h2.setDelay((float)0.0001);
       
        ILink net1_r0_h3 = net1.createLink("r0_h3");
        net1_r0_h3.createInterface(net1_r0_if1);
        net1_r0_h3.createInterface(net1_h3_if0);
        net1_r0_h3.setDelay((float)0.0001);

        ILink net1_r1_h4 = net1.createLink("r1_h4");
        net1_r1_h4.createInterface(net1_r1_if1);
        net1_r1_h4.createInterface(net1_h4_if0);
        net1_r1_h4.setDelay((float)0.0001);
       
        IInterface right_portal = net1_r1_if2;
       
        //routers in net01
        IRouter net01_r4 = net01.createRouter("r4");   
        IInterface net01_r4_if0 = net01_r4.createInterface("if0");
        IInterface net01_r4_if1 = net01_r4.createInterface("if1");
        IInterface net01_r4_if2 = net01_r4.createInterface("if2");
        IInterface net01_r4_if3 = net01_r4.createInterface("if3");
       
        IRouter net01_r5 = net01.createRouter("r5");   
        IInterface net01_r5_if0 = net01_r5.createInterface("if0");
        IInterface net01_r5_if1 = net01_r5.createInterface("if1");
        IInterface net01_r5_if2 = net01_r5.createInterface("if2");
        IInterface net01_r5_if3 = net01_r5.createInterface("if3");
       
        //link router 4&5
        ILink net01_r4_r5 = net01.createLink("r4_r5");
        net01_r4_r5.createInterface(net01_r4_if1);
        net01_r4_r5.createInterface(net01_r5_if3);
        net01_r4_r5.setDelay((float)0.0001);
       
        //links from backbone net 0 to routers 4, 5, net1_r0
        ILink net01_net0_r4 = net01.createLink("net0_r4");
        net01_net0_r4.createInterface(net0_r0_if2);
        net01_net0_r4.createInterface(net01_r4_if0);
        net01_net0_r4.setDelay((float)0.0001);
       
        ILink net01_net0_r5 = net01.createLink("net0_r5");
        net01_net0_r5.createInterface(net0_r1_if1);
        net01_net0_r5.createInterface(net01_r5_if0);
        net01_net0_r5.setDelay((float)0.0001);
       
        ILink net01_net0_net1 = net01.createLink("net0_net1");
        net01_net0_net1.createInterface(net0_r2_if0);
        net01_net0_net1.createInterface(net1_r0_if3);
        net01_net0_net1.setDelay((float)0.0001);
       
        //net2 includes seven routers and seven lans
        INet net2 = top.createNet("net2");
        net2.createShortestPath();
       
        IRouter net2_r0 = net2.createRouter("r0");
        IInterface net2_r0_if0 = net2_r0.createInterface("if0");
        IInterface net2_r0_if1 = net2_r0.createInterface("if1");
        IInterface net2_r0_if2 = net2_r0.createInterface("if2");
       
        IRouter net2_r1 = net2.createRouter("r1");
        IInterface net2_r1_if0 = net2_r1.createInterface("if0");
        IInterface net2_r1_if1 = net2_r1.createInterface("if1");
        IInterface net2_r1_if2 = net2_r1.createInterface("if2");
       
        IRouter net2_r2 = net2.createRouter("r2");
        IInterface net2_r2_if0 = net2_r2.createInterface("if0");
        IInterface net2_r2_if1 = net2_r2.createInterface("if1");
        IInterface net2_r2_if2 = net2_r2.createInterface("if2");
        IInterface net2_r2_if3 = net2_r2.createInterface("if3");
       
        IRouter net2_r3 = net2.createRouter("r3");
        IInterface net2_r3_if0 = net2_r3.createInterface("if0");
        IInterface net2_r3_if1 = net2_r3.createInterface("if1");
        IInterface net2_r3_if2 = net2_r3.createInterface("if2");
        IInterface net2_r3_if3 = net2_r3.createInterface("if3");
       
        IRouter net2_r4 = net2.createRouter("r4");
        IInterface net2_r4_if0 = net2_r4.createInterface("if0");
        IInterface net2_r4_if1 = net2_r4.createInterface("if1");
       
        IRouter net2_r5 = net2.createRouter("r5");
        IInterface net2_r5_if0 = net2_r5.createInterface("if0");
        IInterface net2_r5_if1 = net2_r5.createInterface("portal_10_1_3_0"); //connects to the lan
        IInterface net2_r5_if2 = net2_r5.createInterface("if2");
        //XXX
        //we are linking a traffic portal here
        IInterface left_portal = net2_r5_if1;
       
        IRouter net2_r6 = net2.createRouter("r6");
        IInterface net2_r6_if0 = net2_r6.createInterface("if0");
        IInterface net2_r6_if1 = net2_r6.createInterface("if1");
        IInterface net2_r6_if2 = net2_r6.createInterface("if2");
        IInterface net2_r6_if3 = net2_r6.createInterface("if3");
       
        //router to router links in net2
        ILink net2_r0_r1 = net2.createLink("r0_r1");
        net2_r0_r1.createInterface(net2_r0_if1);
        net2_r0_r1.createInterface(net2_r1_if2);
        net2_r0_r1.setDelay((float)0.0001);
       
        ILink net2_r0_r2 = net2.createLink("r0_r2");
        net2_r0_r2.createInterface(net2_r0_if2);
        net2_r0_r2.createInterface(net2_r2_if0);
        net2_r0_r2.setDelay((float)0.0001);
       
        ILink net2_r2_r3 = net2.createLink("r2_r3");
        net2_r2_r3.createInterface(net2_r2_if1);
        net2_r2_r3.createInterface(net2_r3_if3);
        net2_r2_r3.setDelay((float)0.0001);
       
        ILink net2_r2_r4 = net2.createLink("r2_r4");
        net2_r2_r4.createInterface(net2_r2_if2);
        net2_r2_r4.createInterface(net2_r4_if0);
        net2_r2_r4.setDelay((float)0.0001);
       
        ILink net2_r1_r3 = net2.createLink("r1_r3");
        net2_r1_r3.createInterface(net2_r1_if1);
        net2_r1_r3.createInterface(net2_r3_if0);
        net2_r1_r3.setDelay((float)0.0001);
       
        ILink net2_r3_r5 = net2.createLink("r3_r5");
        net2_r3_r5.createInterface(net2_r3_if2);
        net2_r3_r5.createInterface(net2_r5_if0);
        net2_r3_r5.setDelay((float)0.0001);
       
        ILink net2_r5_r6 = net2.createLink("r5_r6");
        net2_r5_r6.createInterface(net2_r5_if2);
        net2_r5_r6.createInterface(net2_r6_if0);
        net2_r5_r6.setDelay((float)0.0001);
       
        //the lans in net2
        INet net23 = net2.createNet("lan0");
        net23.createShortestPath();
       
        IRouter net23_r0 = net23.createRouter("r0");
        IInterface net23_r0_if0 = net23_r0.createInterface("if0");
        IInterface net23_r0_if3 = net23_r0.createInterface("if3");
        IInterface net23_r0_if4 = net23_r0.createInterface("if4");
        IInterface net23_r0_if5 = net23_r0.createInterface("if5");
        IInterface net23_r0_if6 = net23_r0.createInterface("if6");
       
        IHost replica = null;
        IHost h[] = new IHost[43];
        IInterface iface[] = new IInterface[43];
        for(int i=1 ; i<43 ; i++){
            if(replica == null) {
                h[i] = net23.createHost("h"+i);
                iface[i] = h[i].createInterface("if0");
                replica = h[i];
            } else {
            	h[i] = net23.createHostReplica("h"+i, replica);
            }
        }
       
        //links in lan
        ILink net23_l1 = net23.createLink("l1");
        net23_l1.setDelay((float)0.0001);
        net23_l1.createInterface(net23_r0_if3);
        net23_l1.createInterface((IInterface)h[1].getChildByName("if0"));
        net23_l1.createInterface((IInterface)h[2].getChildByName("if0"));
        net23_l1.createInterface((IInterface)h[3].getChildByName("if0"));
        net23_l1.createInterface((IInterface)h[4].getChildByName("if0"));
        net23_l1.createInterface((IInterface)h[5].getChildByName("if0"));
        net23_l1.createInterface((IInterface)h[6].getChildByName("if0"));
        net23_l1.createInterface((IInterface)h[7].getChildByName("if0"));
        net23_l1.createInterface((IInterface)h[8].getChildByName("if0"));
        net23_l1.createInterface((IInterface)h[9].getChildByName("if0"));
        net23_l1.createInterface((IInterface)h[10].getChildByName("if0"));
       
        ILink net23_l2 = net23.createLink("l2");
        net23_l2.setDelay((float)0.0001);
        net23_l2.createInterface(net23_r0_if4);
        net23_l2.createInterface((IInterface)h[11].getChildByName("if0"));
        net23_l2.createInterface((IInterface)h[12].getChildByName("if0"));
        net23_l2.createInterface((IInterface)h[13].getChildByName("if0"));
        net23_l2.createInterface((IInterface)h[14].getChildByName("if0"));
        net23_l2.createInterface((IInterface)h[15].getChildByName("if0"));
        net23_l2.createInterface((IInterface)h[16].getChildByName("if0"));
        net23_l2.createInterface((IInterface)h[17].getChildByName("if0"));
        net23_l2.createInterface((IInterface)h[18].getChildByName("if0"));
        net23_l2.createInterface((IInterface)h[19].getChildByName("if0"));
        net23_l2.createInterface((IInterface)h[20].getChildByName("if0"));
       
        ILink net23_l3 = net23.createLink("l3");
        net23_l3.setDelay((float)0.0001);
        net23_l3.createInterface(net23_r0_if5);
        net23_l3.createInterface((IInterface)h[21].getChildByName("if0"));
        net23_l3.createInterface((IInterface)h[22].getChildByName("if0"));
        net23_l3.createInterface((IInterface)h[23].getChildByName("if0"));
        net23_l3.createInterface((IInterface)h[24].getChildByName("if0"));
        net23_l3.createInterface((IInterface)h[25].getChildByName("if0"));
        net23_l3.createInterface((IInterface)h[26].getChildByName("if0"));
        net23_l3.createInterface((IInterface)h[27].getChildByName("if0"));
        net23_l3.createInterface((IInterface)h[28].getChildByName("if0"));
        net23_l3.createInterface((IInterface)h[29].getChildByName("if0"));
        net23_l3.createInterface((IInterface)h[30].getChildByName("if0"));
       
        ILink net23_l4 = net23.createLink("l4");
        net23_l4.setDelay((float)0.0001);
        IInterface ACK = net23_l4.createInterface(net23_r0_if6);
        System.out.println("ACK----"+ACK.getUniqueName()+", "+ACK.deference().getUniqueName());
        net23_l4.createInterface((IInterface)h[31].getChildByName("if0"));
        net23_l4.createInterface((IInterface)h[32].getChildByName("if0"));
        net23_l4.createInterface((IInterface)h[33].getChildByName("if0"));
        net23_l4.createInterface((IInterface)h[34].getChildByName("if0"));
        net23_l4.createInterface((IInterface)h[35].getChildByName("if0"));
        net23_l4.createInterface((IInterface)h[36].getChildByName("if0"));
        net23_l4.createInterface((IInterface)h[37].getChildByName("if0"));
        net23_l4.createInterface((IInterface)h[38].getChildByName("if0"));
        net23_l4.createInterface((IInterface)h[39].getChildByName("if0"));
        net23_l4.createInterface((IInterface)h[40].getChildByName("if0"));
        net23_l4.createInterface((IInterface)h[41].getChildByName("if0"));
        net23_l4.createInterface((IInterface)h[42].getChildByName("if0"));
       
        //connect net23 with the router in net2
        ILink net2_lan0_r2 = net2.createLink("lan0_r2");
        net2_lan0_r2.setDelay((float)0.0001);
        net2_lan0_r2.createInterface(net23_r0_if0);
        net2_lan0_r2.createInterface(net2_r2_if3);
       
        //the other 6 lans and links between the lan to the router in net2
        INet net41 = net2.createNetReplica("lan1", net23);
        ILink net2_lan1_r4 = net2.createLink("lan1_r4");
        net2_lan1_r4.setDelay((float)0.0001);
        net2_lan1_r4.createInterface((IInterface)net41.getChildByName("r0").getChildByName("if0"));
        net2_lan1_r4.createInterface(net2_r4_if1);
       
        INet net31 = net2.createNetReplica("lan2", net23);
        ILink net2_lan2_r3 = net2.createLink("lan2_r3");
        net2_lan2_r3.setDelay((float)0.0001);
        net2_lan2_r3.createInterface((IInterface)net31.getChildByName("r0").getChildByName("if0"));
        net2_lan2_r3.createInterface(net2_r3_if1);
        
        INet net63 = net2.createNetReplica("lan4", net23);
        ILink net2_lan4_r6 = net2.createLink("lan4_r6");
        net2_lan4_r6.setDelay((float)0.0001);
        net2_lan4_r6.createInterface((IInterface)net63.getChildByName("r0").getChildByName("if0"));
        net2_lan4_r6.createInterface(net2_r6_if3);
       
        INet net62 = net2.createNetReplica("lan5", net23);
        ILink net2_lan5_r6 = net2.createLink("lan5_r6");
        net2_lan5_r6.setDelay((float)0.0001);
        net2_lan5_r6.createInterface((IInterface)net62.getChildByName("r0").getChildByName("if0"));
        net2_lan5_r6.createInterface(net2_r6_if2);
       
        INet net61 = net2.createNetReplica("lan6", net23);
        ILink net2_lan6_r6 = net2.createLink("lan6_r6");
        net2_lan6_r6.setDelay((float)0.0001);
        net2_lan6_r6.createInterface((IInterface)net61.getChildByName("r0").getChildByName("if0"));
        net2_lan6_r6.createInterface(net2_r6_if1);
        //end network2
       
        //net3 includes 4 routers and 5 lans
        INet net3 = top.createNet("net3");
        net3.createShortestPath();
       
        IRouter net3_r0 = net3.createRouter("r0");
        IInterface net3_r0_if0 = net3_r0.createInterface("if0");
        IInterface net3_r0_if1 = net3_r0.createInterface("if1");
        IInterface net3_r0_if2 = net3_r0.createInterface("if2");
        IInterface net3_r0_if3 = net3_r0.createInterface("if3");
       
        IRouter net3_r1 = net3.createRouter("r1");
        IInterface net3_r1_if0 = net3_r1.createInterface("if0");
        IInterface net3_r1_if1 = net3_r1.createInterface("if1");
        IInterface net3_r1_if2 = net3_r1.createInterface("if2");
        IInterface net3_r1_if3 = net3_r1.createInterface("if3");
       
        IRouter net3_r2 = net3.createRouter("r2");
        IInterface net3_r2_if0 = net3_r2.createInterface("if0");
        IInterface net3_r2_if1 = net3_r2.createInterface("if1");
        IInterface net3_r2_if2 = net3_r2.createInterface("if2");

        IRouter net3_r3 = net3.createRouter("r3");
        IInterface net3_r3_if0 = net3_r3.createInterface("if0");
        IInterface net3_r3_if1 = net3_r3.createInterface("if1");
        IInterface net3_r3_if2 = net3_r3.createInterface("if2");
        IInterface net3_r3_if3 = net3_r3.createInterface("if3");
       
        //router to router links in net3
        ILink net3_r0_r1 = net3.createLink("r0_r1");
        net3_r0_r1.setDelay((float)0.0001);
        net3_r0_r1.createInterface(net3_r0_if1);
        net3_r0_r1.createInterface(net3_r1_if3);
       
        ILink net3_r1_r2 = net3.createLink("r1_r2");
        net3_r1_r2.setDelay((float)0.0001);
        net3_r1_r2.createInterface(net3_r1_if2);
        net3_r1_r2.createInterface(net3_r2_if0);
       
        ILink net3_r2_r3 = net3.createLink("r2_r3");
        net3_r2_r3.setDelay((float)0.0001);
        net3_r2_r3.createInterface(net3_r2_if1);
        net3_r2_r3.createInterface(net3_r3_if3);
       
        ILink net3_r1_r3 = net3.createLink("r1_r3");
        net3_r1_r3.setDelay((float)0.0001);
        net3_r1_r3.createInterface(net3_r1_if1);
        net3_r1_r3.createInterface(net3_r3_if0);
       
        //5 lans composed of a router with local area networks
        INet net3_lan21 = net3.createNetReplica("lan0", net23);
        ILink net3_lan0_r0 = net3.createLink("lan0_r0");
        net3_lan0_r0.setDelay((float)0.0001);
        net3_lan0_r0.createInterface((IInterface)net3_lan21.getChildByName("r0").getChildByName("if0"));
        net3_lan0_r0.createInterface(net3_r0_if3);
       
        INet net3_lan22 = net3.createNetReplica("lan1", net23);
        ILink net3_lan1_r0 = net3.createLink("lan1_r0");
        net3_lan1_r0.setDelay((float)0.0001);
        net3_lan1_r0.createInterface((IInterface)net3_lan22.getChildByName("r0").getChildByName("if0"));
        net3_lan1_r0.createInterface(net3_r0_if2);
       
        INet net3_lan23 = net3.createNetReplica("lan2", net23);
        ILink net3_lan2_r2 = net3.createLink("lan2_r2");
        net3_lan2_r2.setDelay((float)0.0001);
        net3_lan2_r2.createInterface((IInterface)net3_lan23.getChildByName("r0").getChildByName("if0"));
        net3_lan2_r2.createInterface(net3_r2_if2);
       
        INet net3_lan24 = net3.createNetReplica("lan3", net23);
        ILink net3_lan3_r3 = net3.createLink("lan3_r3");
        net3_lan3_r3.setDelay((float)0.0001);
        net3_lan3_r3.createInterface((IInterface)net3_lan24.getChildByName("r0").getChildByName("if0"));
        net3_lan3_r3.createInterface(net3_r3_if2);
       
        INet net3_lan25 = net3.createNetReplica("lan4", net23);
        ILink net3_lan4_r3 = net3.createLink("lan4_r3");
        net3_lan4_r3.setDelay((float)0.0001);
        net3_lan4_r3.createInterface((IInterface)net3_lan25.getChildByName("r0").getChildByName("if0"));
        net3_lan4_r3.createInterface(net3_r3_if1);
        //end network3
       
        //links to connect router4 in net01 to net2
        ILink r4_net2_1 = top.createLink("r4_net2_1");
        r4_net2_1.setDelay((float)0.0001);
        r4_net2_1.createInterface(net01_r4_if2);
        r4_net2_1.createInterface(net2_r1_if0);
       
        ILink r4_net2_2 = top.createLink("r4_net2_2");
        r4_net2_2.setDelay((float)0.0001);
        r4_net2_2.createInterface(net01_r4_if3);
        r4_net2_2.createInterface(net2_r0_if0);
       
        //links to connect router5 in net01 to net3
        ILink r5_net3_1 = top.createLink("r5_net3_1");
        r5_net3_1.setDelay((float)0.0001);
        r5_net3_1.createInterface(net01_r5_if2);
        r5_net3_1.createInterface(net3_r0_if0);
       
        ILink r5_net3_2 = top.createLink("r5_net3_2");
        r5_net3_2.setDelay((float)0.0001);
        r5_net3_2.createInterface(net01_r5_if1);
        r5_net3_2.createInterface(net3_r1_if0);   
        
        // Setup the traffic portals
        left_portal.createTrafficPortal();
        left_portal.setIpAddress("10.10.1.1");
        left_portal.addReachableNetwork("10.10.1.0/24");
	
        right_portal.createTrafficPortal();       
        right_portal.setIpAddress("10.10.3.2");
        right_portal.addReachableNetwork("10.10.3.0/24");
          
        return top;
    }

}
