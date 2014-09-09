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
public class EmuDumbbell extends ModelInterface{
	private static final ArrayList<ModelParam> getParams() {
		ArrayList<ModelParam> rv = new ArrayList<ModelInterface.ModelParam>();
		rv.add(new ModelParam(ModelParamType.INT, "BANDWIDTH","BANDWIDTH","BANDWIDTH", "100"));
		rv.add(new ModelParam(ModelParamType.FLOAT, "DELAY","DELAY","DELAY", "0.001"));
		return rv;
	}

	public EmuDumbbell(Database db, Experiment exp) {
		super(db, exp, getParams());
	}
	public EmuDumbbell(Database db, String expName) {
		super(db, expName,getParams());
	}
	public INet buildModel(Map<String, ModelParamValue> parameters) {
		final long bandwidth = parameters.get("BANDWIDTH").asLong()*1000000;
		final double delay = parameters.get("DELAY").asDouble();
		Net top = exp.createTopNet("topnet");
		top.createShortestPath();
		
		IRouter left   = top.createRouter("l");
		IInterface l_r = left.createInterface("router");
		IInterface l1  = left.createInterface("h1l");
		IInterface l2  = left.createInterface("h2l");
		
		IRouter right  = top.createRouter("r");
		IInterface r_l = right.createInterface("router");
		IInterface r1  = right.createInterface("h1r");
		IInterface r2  = right.createInterface("h2r");

		IHost h1l = top.createHost("h1l");
		IHost h2l = top.createHost("h2l");
		IHost h1r = top.createHost("h1r");
		IHost h2r = top.createHost("h2r");
		IInterface h1l_nic= h1l.createInterface("if0");
		IInterface h2l_nic= h2l.createInterface("if0");
		IInterface h1r_nic= h1r.createInterface("if0");
		IInterface h2r_nic= h2r.createInterface("if0");

		
		
		for(IInterface nic : new IInterface[]{l_r,l1,l2,r_l,r1,r2,h1l_nic,h2l_nic,h1r_nic,h2r_nic}) {
			nic.setBitRate("1000000000"); //1G
	        nic.setLatency("0.0");
	        nic.setBufferSize("65535");//64k
		}
		
		
		for(String[] p : new String[][]{{"l.h1l","h1l.if0"},
										{"l.h2l","h2l.if0"},	
										{"r.h1r","h1r.if0"},	
										{"r.h2r","h2r.if0"},
										{"r.router","l.router"}}) {
			ILink l= top.createLink();
			l.attachInterface((IInterface)top.get(p[0]));
			l.attachInterface((IInterface)top.get(p[1]));
			if(p[0].contains("router")) {
				l.setBandwidth(bandwidth);
				l.setDelay(""+delay);
			}
			else {
				l.setBandwidth("1000000000");//1G
				l.setDelay("0.00001");
			}
		}
		h1l.enableEmulation(true);
		h1r.enableEmulation(true);

		return top;
	}
}
