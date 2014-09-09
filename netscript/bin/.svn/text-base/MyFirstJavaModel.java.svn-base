import java.util.ArrayList;
import java.util.Map;

import jprime.Experiment;
import jprime.Host.IHost;
import jprime.Interface.IInterface;
import jprime.Link.ILink;
import jprime.Net.INet;
import jprime.Router.IRouter;
import jprime.database.Database;
import jprime.util.ModelInterface;

public class MyFirstJavaModel extends ModelInterface {
    public MyFirstJavaModel(Database db, Experiment exp) {
		super(db, exp, new ArrayList<ModelParam>());
    }

    public INet buildModel(Map<String, ModelParamValue> parameters) {
		INet topnet   = exp.createTopNet("top");

		INet left_net = topnet.createNet("left");

		IHost h1 = left_net.createHost("h1");
		IInterface if1 = h1.createInterface("if0");
	
		IHost h2 = left_net.createHost("h2");
		IInterface if2 = h2.createInterface("if0");
	
		IHost h3 = left_net.createHost("h3");
		IInterface if3 = h3.createInterface("if0");
	
		IHost h4 = left_net.createHost("h4");
		IInterface if4 = h4.createInterface("if0");
	
		IRouter r = left_net.createRouter("r");
	
		ILink l1 = left_net.createLink();
		l1.attachInterface(if1);
		l1.attachInterface(r.createInterface("if1"));
	
		ILink l2 = left_net.createLink();
		l2.attachInterface(if2);
		l2.attachInterface(r.createInterface("if2"));
	
		ILink l3 = left_net.createLink();
		l3.attachInterface(if3);
		l3.attachInterface(r.createInterface("if3"));
	
		ILink l4 = left_net.createLink();
		l4.attachInterface(if4);
		l4.attachInterface(r.createInterface("if4"));

		INet right_net = (INet)left_net.copy("right",topnet);

		ILink toplink = topnet.createLink("toplink");
		toplink.attachInterface(((IRouter)left_net.get("r")).createInterface("if0"));
		toplink.attachInterface(((IRouter)right_net.get("r")).createInterface("if0"));

		return topnet;
    }
}
