import java.util.ArrayList;
import java.util.Map;

import jprime.Experiment;
import jprime.Interface.IInterface;
import jprime.Link.ILink;
import jprime.Net.INet;
import jprime.Router.IRouter;
import jprime.database.Database;
import jprime.models.BaseCampus;
import jprime.util.ModelInterface;

public class HubSpokeModel extends ModelInterface {
	private static final ArrayList<ModelParam> getParams() {
		ArrayList<ModelParam> rv = new ArrayList<ModelInterface.ModelParam>();
		rv.add(new ModelParam(ModelParamType.INT, "# of subnetworks","NN","NN", "8"));
		rv.add(new ModelParam(ModelParamType.FLOAT, "# of campuses per network","NCPN","NCPN", "25"));
		return rv;
	}
	public HubSpokeModel(Database db, Experiment exp) {
		super(db, exp, getParams());
	}
	public INet buildModel(Map<String, ModelParamValue> parameters) {
		final int NN = parameters.get("NN").asInt();
		final int NCPN = parameters.get("NCPN").asInt();

		//create the top most network
		final INet topnet   = exp.createTopNet("topnet");
		topnet.createShortestPath();
		final IRouter topr = topnet.createRouter("topr");
		
		INet base=null, base_campus=null;
		for(int nn =0; nn< NN; nn++) {
			if(base==null) {
				jprime.Console.out.println("Create base net "+nn+" [real]");
				base = topnet.createNet("net_"+nn);
				base.createShortestPath();
				final IRouter r = base.createRouter("r");
				{
					final ILink topl = topnet.createLink("top_"+nn);
					topl.attachInterface(topr.createInterface("down_"+nn));
					topl.attachInterface(r.createInterface("up"));
					topl.setBandwidth("10000000");
					topl.setDelay("0.02");
				}
				for(int c =0; c< NCPN; c++) {
					if(base_campus == null) {
						jprime.Console.out.println("\tCreate campus "+c+" [real]");
						base_campus = base.createNet("campus_"+nn+"_"+c);
						base_campus.createShortestPath();
						final IRouter cr = base_campus.createRouter("cr");
						final ILink l = base.createLink("base_"+c);
						l.attachInterface(r.createInterface("down_"+c));
						l.attachInterface(cr.createInterface("up"));
						l.setBandwidth("10000000");
						l.setDelay("0.005");
						BaseCampus.attachCampus(base_campus, cr);
					}
					else {
						jprime.Console.out.println("\tCreate campus "+c+" [replica]");
						final INet net = base.createNetReplica("campus_"+nn+"_"+c, base_campus);
						final ILink l = base.createLink("base_"+c);
						final IInterface nic = (IInterface)net.get("cr.up");
						l.attachInterface(r.createInterface("down_"+c));
						l.attachInterface(nic);
						l.setBandwidth("10000000");
						l.setDelay("0.005");
					}
				}
			}
			else {
				jprime.Console.out.println("Create base net "+nn+" [replica]");
				final INet n = topnet.createNetReplica("net_"+nn,base);
				final IRouter r = (IRouter)n.getChildByName("r");
				final ILink l = topnet.createLink("top_"+nn);
				l.attachInterface(topr.createInterface("down_"+nn));
				l.attachInterface((IInterface)r.getChildByName("up"));
				l.setBandwidth("10000000");
				l.setDelay("0.01");
			}
		}
		return topnet;
	}
}
