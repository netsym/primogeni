import java.util.ArrayList;
import java.util.Map;

import jprime.Experiment;
import jprime.Host.IHost;
import jprime.Interface.IInterface;
import jprime.Link.ILink;
import jprime.Net.INet;
import jprime.Net.Net;
import jprime.Router.IRouter;
import jprime.database.Database;
import jprime.util.ModelInterface;

/**
 * @author Nathanael Van Vorst
 */
public class OpenVpnEmuDumbbell extends ModelInterface{

	/**
	 * @param db
	 * @param exp
	 */
	public OpenVpnEmuDumbbell(Database db, Experiment exp) {
		super(db, exp, new ArrayList<ModelParam>());
	}

	/**
	 * @param db
	 * @param expName
	 */
	public OpenVpnEmuDumbbell(Database db, String expName) {
		super(db, expName, new ArrayList<ModelParam>());
	}

	/* (non-Javadoc)
	 * @see jprime.ModelInterface#buildModel()
	 */
	@Override
	public INet buildModel(Map<String, ModelParamValue> parameters) {
		Net top = exp.createTopNet("topnet");
		top.createShortestPath();
		IRouter r_1 = top.createRouter("router1");
		IRouter r_2 = top.createRouter("router2");
		
		ILink l_hi= top.createLink("toplink");
		l_hi.createInterface(r_1.createInterface("r_2"));
		l_hi.createInterface(r_2.createInterface("r_1"));
		l_hi.setDelay("0.01");
		l_hi.setBandwidth("1000000000000");

		
		for(int i=0;i<3;i++) {
			IInterface r1= r_1.createInterface("ifh"+i);
			r1.setBitRate("1000000000000");
			r1.setLatency(0);
			r1.setBufferSize("64000");//bytes

			IInterface r2= r_2.createInterface("ifh"+i);
			r2.setBitRate("1000000000000");
			r2.setLatency(0);
			r2.setBufferSize("64000");//bytes

			
			IHost l = top.createHost("l"+i);
			IInterface l_if0= l.createInterface("if0");
			if(i==0) {
				l_if0.createOpenVPNEmulation();
			}
			l_if0.setBitRate("1000000000000");
			l_if0.setLatency(0);
			l_if0.setBufferSize("64000");//bytes
			
			IHost r = top.createHost("r"+i);
			IInterface r_if0= r.createInterface("if0");
			if(i==0) {
				r_if0.createOpenVPNEmulation();
			}
			r_if0.setBitRate("1000000000000");
			r_if0.setLatency(0);
			r_if0.setBufferSize("64000");//bytes

			
			ILink ll= top.createLink("ll"+i);
			ll.createInterface(l_if0);
			ll.createInterface(r1);
			ll.setDelay("0.01");
			ll.setBandwidth("1000000000000");

			ILink rl= top.createLink("rl"+i);
			rl.createInterface(r_if0);
			rl.createInterface(r2);
			rl.setDelay("0.01");
			rl.setBandwidth("1000000000000");
		} 
		return top;
	}

}
