import java.util.ArrayList;
import java.util.Map;

import jprime.Experiment;
import jprime.Interface.IInterface;
import jprime.Link.ILink;
import jprime.Net.INet;
import jprime.Router.IRouter;
import jprime.database.Database;
import jprime.util.ModelInterface;

/**
 * @author Nathanael Van Vorst
 */
public class CacheTest extends ModelInterface{
	private static final ArrayList<ModelParam> getParams() {
		ArrayList<ModelParam> rv = new ArrayList<ModelInterface.ModelParam>();
		rv.add(new ModelParam(ModelParamType.INT, "DIAMETER","DIAMETER","DIAMETER", "1"));
		return rv;
	}
	/**
	 * @param db
	 * @param exp
	 */
	public CacheTest(Database db, Experiment exp) {
		super(db, exp, getParams());
	}

	/**
	 * @param db
	 * @param expName
	 */
	public CacheTest(Database db, String expName) {
		super(db, expName, getParams());
	}

	/* (non-Javadoc)
	 * @see jprime.ModelInterface#buildModel()
	 */
	@Override
	public INet buildModel(Map<String, ModelParamValue> parameters) {
		final int diameter=parameters.get("DIAMETER").asInt();
		if(diameter<=0) {
			System.out.println("Invalid diameter "+diameter);
			System.exit(100);
		}
		INet top = exp.createTopNet("top");
		INet net2 = top.createNet("net2");
		INet net3 = net2.createNet("net3");
		INet net4 = net3.createNet("net4");
		top.createShortestPath();
		net2.createShortestPath();
		net3.createShortestPath();
		net4.createShortestPath();
		
		makeRing(top,diameter);
		makeRing(net2,diameter);
		makeRing(net3,diameter);
		makeRing(net4,diameter);

		linkRings(top,top,net2,diameter);
		linkRings(net2,net2,net3,diameter);
		linkRings(net3,net3,net4,diameter);
		
		return top;
	}
	private void makeRing(INet parent, int diameter) {
		INet ring=parent;
		//INet ring=parent.createNet("ring");
		//ring.createShortestPath();
		IRouter first=null;
		IRouter prev=null;
		for(int i=0;i<4*diameter;i++) {
			IRouter r = ring.createRouter("r_"+i);
			if(first==null) first=r;

			if(prev!=null) {
				IInterface rr = prev.createInterface("right");
				IInterface ll = r.createInterface("left");
				ILink l = ring.createLink("l_LR_"+r.getName()+"_"+prev.getName());
				l.createInterface(ll);
				l.createInterface(rr);
				System.out.println("linking "+ll.getUniqueName()+" <--> "+rr.getUniqueName());
				System.out.println("\tl:"+l.getUniqueName());
			}
			prev=r;
		}
		IInterface ll = prev.createInterface("right");
		IInterface rr = first.createInterface("left");
		ILink l = ring.createLink("l_LR_"+first.getName()+"_"+prev.getName());
		l.createInterface(ll);
		l.createInterface(rr);
		System.out.println("linking "+ll.getUniqueName()+" <--> "+rr.getUniqueName());
		System.out.println("\tl:"+l.getUniqueName());
	}
	
	private void linkRings(INet parent, INet up, INet down, int diameter) {
		for(int i=0;i<diameter;i++) {
			ILink l = parent.createLink("l_UD_d"+i+"_u"+(3*diameter+i));
			IRouter upr=(IRouter)up.getChildByName("r_"+i);
			IRouter downr=(IRouter)down.getChildByName("r_"+(3*diameter+i));
			IInterface d = (IInterface)upr.createInterface("down");
			IInterface u = (IInterface)downr.createInterface("up");

			
			l.createInterface(d);
			l.createInterface(u);
			System.out.println("linking "+d.getUniqueName()+" <--> "+u.getUniqueName());
			System.out.println("\tl:"+l.getUniqueName());
		}
	}
}
