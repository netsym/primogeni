import java.util.ArrayList;
import java.util.Map;

import jprime.Experiment;
import jprime.Link.ILink;
import jprime.Net.INet;
import jprime.Net.Net;
import jprime.Router.IRouter;
import jprime.database.Database;
import jprime.util.ModelInterface;

/**
 * @author Nathanael Van Vorst
 */
public class PartTest extends ModelInterface{

	/**
	 * @param db
	 * @param exp
	 */
	public PartTest(Database db, Experiment exp) {
		super(db, exp, new ArrayList<ModelParam>());
	}

	/**
	 * @param db
	 * @param expName
	 */
	public PartTest(Database db, String expName) {
		super(db, expName, new ArrayList<ModelParam>());
	}

	/* (non-Javadoc)
	 * @see jprime.ModelInterface#buildModel()
	 */
	@Override
	public INet buildModel(Map<String, ModelParamValue> parameters) {
		ILink l=null;
		Net top = exp.createTopNet("topnet");
		top.createShortestPath();
		
		IRouter r1 = top.createRouter("r1");
		IRouter r2 = top.createRouter("r2");
		IRouter r3 = top.createRouter("r3");
		IRouter r4 = top.createRouter("r4");
		IRouter r5 = top.createRouter("r5");
		IRouter r6 = top.createRouter("r6");
		IRouter r7 = top.createRouter("r7");
		
		IRouter r1a = top.createRouter("r1a");
		IRouter r2a = top.createRouter("r2a");
		IRouter r3a = top.createRouter("r3a");
		IRouter r4a = top.createRouter("r4a");
		IRouter r5a = top.createRouter("r5a");
		IRouter r6a = top.createRouter("r6a");
		IRouter r7a = top.createRouter("r7a");
		
		l = top.createLink("l_1");
		l.createInterface(r1.createInterface());
		l.createInterface(r1a.createInterface());
		
		l = top.createLink("l_2");
		l.createInterface(r2.createInterface());
		l.createInterface(r2a.createInterface());
		
		l = top.createLink("l_3");
		l.createInterface(r3.createInterface());
		l.createInterface(r3a.createInterface());
		
		l = top.createLink("l_4");
		l.createInterface(r4.createInterface());
		l.createInterface(r4a.createInterface());
		
		l = top.createLink("l_5");
		l.createInterface(r5.createInterface());
		l.createInterface(r5a.createInterface());
		
		l = top.createLink("l_6");
		l.createInterface(r6.createInterface());
		l.createInterface(r6a.createInterface());
				
		l = top.createLink("l_7");
		l.createInterface(r7.createInterface());
		l.createInterface(r7a.createInterface());
		
		l = top.createLink("l_1_2");
		l.createInterface(r1.createInterface());
		l.createInterface(r2.createInterface());

		l = top.createLink("l_1_3");
		l.createInterface(r1.createInterface());
		l.createInterface(r3.createInterface());
		
		l = top.createLink("l_1_5");
		l.createInterface(r1.createInterface());
		l.createInterface(r5.createInterface());

		l = top.createLink("l_2_3");
		l.createInterface(r2.createInterface());
		l.createInterface(r3.createInterface());
		
		l = top.createLink("l_2_4");
		l.createInterface(r2.createInterface());
		l.createInterface(r4.createInterface());
		
		l = top.createLink("l_3_4");
		l.createInterface(r3.createInterface());
		l.createInterface(r4.createInterface());
		
		l = top.createLink("l_3_5");
		l.createInterface(r3.createInterface());
		l.createInterface(r5.createInterface());
		
		l = top.createLink("l_4_6");
		l.createInterface(r4.createInterface());
		l.createInterface(r6.createInterface());
		
		l = top.createLink("l_4_7");
		l.createInterface(r4.createInterface());
		l.createInterface(r7.createInterface());
		
		l = top.createLink("l_5_6");
		l.createInterface(r5.createInterface());
		l.createInterface(r6.createInterface());

		l = top.createLink("l_6_7");
		l.createInterface(r6.createInterface());
		l.createInterface(r7.createInterface());
				
		return top;
	}
}
