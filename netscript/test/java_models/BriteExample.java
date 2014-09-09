import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import jprime.Experiment;
import jprime.TrafficFactory;
import jprime.TrafficFactory.ArrivalProcess;
import jprime.Host.IHost;
import jprime.Interface.IInterface;
import jprime.Link.ILink;
import jprime.Net.INet;
import jprime.Router.IRouter;
import jprime.database.Database;
import jprime.models.BaseCampus;
import jprime.util.ModelInterface;
import jprime.util.XMLModelBuilder;

public class BriteExample extends ModelInterface {
	private static final ArrayList<ModelParam> getParams() {
		ArrayList<ModelParam> rv = new ArrayList<ModelInterface.ModelParam>();
		rv.add(new ModelParam(ModelParamType.FILE, "working directory.", "CWD", "CWD", ""));
		rv.add(new ModelParam(ModelParamType.FILE, "Backbone network's XML","XML","XML", "brite_example.xml"));
		return rv;
	}
	public BriteExample(Database db, Experiment exp) {
		super(db, exp,getParams());
	}
	public BriteExample(Database db, String expName) {
		super(db, expName,getParams());
	}
	
	public INet buildModel(Map<String, ModelParamValue> parameters) {
		//load, parse, and instantiate the xml model
		XMLModelBuilder xml = new XMLModelBuilder(exp,
				parameters.get("CWD").asString()+"/"+parameters.get("XML").asString());
		xml.createModel();
		//get the topmost network
		INet topnet = exp.getRootNode();
		INet base_campus = null;
		for(final INet as : topnet.getSubnets()) {
			IRouter campus_router = null;
			if(base_campus == null) {
				//create the base campus from which we will replicate others
				base_campus = as.createNet("campus");
				base_campus.createShortestPath();
				campus_router = base_campus.createRouter("campus_router");
				campus_router.createInterface("up");
				BaseCampus.attachCampus(base_campus, campus_router);
			}
			else {
				//copy the base campus and get its "campus_router"
				campus_router =  (IRouter)as.createNetReplica(base_campus).get("campus_router");
			}
			//find a router and attach our campus to it
			for(IHost h : as.getHosts()) {
				if(h instanceof IRouter) {
					final ILink l = as.createLink();
					l.attachInterface((IInterface)campus_router.get("up"));
					l.attachInterface(h.createInterface());
					break;
				}
			}
		}
		
		final Random r = new Random();
		final TrafficFactory tf = new TrafficFactory(topnet);
		for(final INet as : topnet.getSubnets()) {
			//create TCP traffic
			tf.createSimulatedTCP(
					r.nextInt(10), //random start time between 0 and 10
					ArrivalProcess.CONSTANT, 5, 50,//50 flows will be started at 5 flows per second
					5000000, //each flow we will transwer 5MB
					as, //randomly choose srcs from this network
					topnet.getSubnets().get(r.nextInt(topnet.getSubnets().size())) //randomly choose dsts from this network
					);
		}
		return topnet;
	}
}
