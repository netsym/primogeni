import java.util.ArrayList;
import java.util.Map;

import jprime.Experiment;
import jprime.StatusListener;
import jprime.Interface.IInterface;
import jprime.Net.INet;
import jprime.database.Database;
import jprime.models.BaseCampus;
import jprime.util.ModelInterface;

/**
 * @author Nathanael Van Vorst
 */
public class CampusShortestPath extends BaseCampus {
	/**
	 * @param db
	 * @param exp
	 */
	public CampusShortestPath(Database db, Experiment exp) {
		super(db,exp,new ArrayList<ModelInterface.ModelParam>());
	}

	/**
	 * @param db
	 * @param expName
	 */
	public CampusShortestPath(Database db, String expName) {
		super(db, expName,new ArrayList<ModelInterface.ModelParam>());
	}

	/* (non-Javadoc)
	 * @see jprime.models.BaseCampus#buildModel()
	 */
	@Override
	public INet buildModel(Map<String, ModelParamValue> parameters) {
		parameters.put("routingType", new ModelParamValue(parameters.get("routingType").param,"SHORTEST_PATH_L123"));
		INet topnet = super.buildModel(parameters);
		exp.compile(new StatusListener());
		
		IInterface iface = topnet.findInterfaceWithIp("192.1.8.53"); //topnet:net01:net0:r0:if2
		if(iface == null) {
			System.out.println("Couldn't find it");
		}
		else {
			System.out.println("Found "+iface.getUniqueName());
		}
		return topnet;
	}
	
	
}
