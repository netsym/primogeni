import java.util.ArrayList;
import java.util.Map;

import jprime.Experiment;
import jprime.Net.INet;
import jprime.database.Database;
import jprime.models.BaseCampus;
import jprime.util.ModelInterface;

/**
 * @author Nathanael Van Vorst
 */
public class CampusAlgorithmic extends BaseCampus {
	/**
	 * @param db
	 * @param exp
	 */
	public CampusAlgorithmic(Database db, Experiment exp) {
		super(db,exp,new ArrayList<ModelInterface.ModelParam>());
	}

	/**
	 * @param db
	 * @param expName
	 */
	public CampusAlgorithmic(Database db, String expName) {
		super(db,expName,new ArrayList<ModelInterface.ModelParam>());
	}
	
	@Override
	public INet buildModel(Map<String, ModelParamValue> parameters) {
		parameters.put("routingType", new ModelParamValue(parameters.get("routingType").param,"ALGORITHMIC"));
		return super.buildModel(parameters);
	}
}
