import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jprime.Experiment;
import jprime.Interface.IInterface;
import jprime.Link.ILink;
import jprime.Net.INet;
import jprime.Router.IRouter;
import jprime.database.Database;
import jprime.models.BaseCampus;
import jprime.util.ModelInterface;

public class LargeModel extends BaseCampus {
	
	private static final ArrayList<ModelParam> getParams() {
		ArrayList<ModelParam> rv = new ArrayList<ModelInterface.ModelParam>();
		rv.add(new ModelParam(ModelParamType.BOOLEAN, "DO_SP","DO_SP","DO_SP", "true"));
		return rv;
	}

	private boolean do_spherical=true;
	private INet baseCampus = null;
	public LargeModel(Database db, Experiment exp) {
		super(db, exp, getParams());
	}
	public LargeModel(Database db, String expName) {
		super(db, expName, getParams());
	}
	public INet buildModel(Map<String, ModelParamValue> parameters) {
		this.do_spherical=parameters.get("do_spherical").asBoolean();
		INet top = exp.createTopNet("topnet");
		top.createShortestPath();
		if(do_spherical) {
			System.out.println("Doing spherical routing!");
		}
		else {
			System.out.println("Doing global shortest path routing!");
		}
		Map<Integer,IRouter> routers = new HashMap<Integer,IRouter>();
		ArrayList<INet> src_nets=new ArrayList<INet>();
		ArrayList<INet> dst_nets=new ArrayList<INet>();
		//creating the top 16 networks
		src_nets.add(create_net_0(top,routers));
		System.out.println("create net 0");
		create_net_1(top,routers);//bad
		System.out.println("create net 1");
		create_net_2(top,routers);//bad
		System.out.println("create net 2");
		create_net_3(top,routers);//bad
		System.out.println("create net 3");
		src_nets.add(create_net_4(top,routers));
		System.out.println("create net 4");
		create_net_5(top,routers);//bad
		System.out.println("create net 5");
		create_net_6(top,routers);//bad
		System.out.println("create net 6");
		dst_nets.add(create_net_7(top,routers));
		System.out.println("create net 7");
		dst_nets.add(create_net_8(top,routers));
		System.out.println("create net 8");
		create_net_9(top,routers);//bad
		System.out.println("create net 9");
		create_net_10(top,routers);//bad
		System.out.println("create net 10");
		src_nets.add(create_net_11(top,routers));
		System.out.println("create net 11");
		src_nets.add(create_net_12(top,routers));
		System.out.println("create net 12");
		dst_nets.add(create_net_13(top,routers));
		System.out.println("create net 13");
		dst_nets.add(create_net_14(top,routers));
		System.out.println("create net 14");
		create_net_15(top,routers);
		System.out.println("create net 15");
		//linking the networks

		ILink l_70_24 = top.createLink("l_70_24");
		l_70_24.createInterface(routers.get(70).createInterface("if_24"));
		l_70_24.createInterface(routers.get(24).createInterface("if_70"));

		ILink l_70_25 = top.createLink("l_70_25");
		l_70_25.createInterface(routers.get(70).createInterface("if_25"));
		l_70_25.createInterface(routers.get(25).createInterface("if_70"));

		ILink l_70_44 = top.createLink("l_70_44");
		l_70_44.createInterface(routers.get(70).createInterface("if_44"));
		l_70_44.createInterface(routers.get(44).createInterface("if_70"));

		ILink l_70_69 = top.createLink("l_70_69");
		l_70_69.createInterface(routers.get(70).createInterface("if_69"));
		l_70_69.createInterface(routers.get(69).createInterface("if_70"));

		ILink l_100_70 = top.createLink("l_100_70");
		l_100_70.createInterface(routers.get(100).createInterface("if_70"));
		l_100_70.createInterface(routers.get(70).createInterface("if_100"));

		ILink l_239_70 = top.createLink("l_239_70");
		l_239_70.createInterface(routers.get(239).createInterface("if_70"));
		l_239_70.createInterface(routers.get(70).createInterface("if_239"));

		ILink l_280_70 = top.createLink("l_280_70");
		l_280_70.createInterface(routers.get(280).createInterface("if_70"));
		l_280_70.createInterface(routers.get(70).createInterface("if_280"));

		ILink l_290_70 = top.createLink("l_290_70");
		l_290_70.createInterface(routers.get(290).createInterface("if_70"));
		l_290_70.createInterface(routers.get(70).createInterface("if_290"));

		ILink l_330_70 = top.createLink("l_330_70");
		l_330_70.createInterface(routers.get(330).createInterface("if_70"));
		l_330_70.createInterface(routers.get(70).createInterface("if_330"));

		ILink l_361_70 = top.createLink("l_361_70");
		l_361_70.createInterface(routers.get(361).createInterface("if_70"));
		l_361_70.createInterface(routers.get(70).createInterface("if_361"));

		ILink l_364_70 = top.createLink("l_364_70");
		l_364_70.createInterface(routers.get(364).createInterface("if_70"));
		l_364_70.createInterface(routers.get(70).createInterface("if_364"));

		ILink l_392_70 = top.createLink("l_392_70");
		l_392_70.createInterface(routers.get(392).createInterface("if_70"));
		l_392_70.createInterface(routers.get(70).createInterface("if_392"));

		ILink l_393_70 = top.createLink("l_393_70");
		l_393_70.createInterface(routers.get(393).createInterface("if_70"));
		l_393_70.createInterface(routers.get(70).createInterface("if_393"));

		ILink l_403_70 = top.createLink("l_403_70");
		l_403_70.createInterface(routers.get(403).createInterface("if_70"));
		l_403_70.createInterface(routers.get(70).createInterface("if_403"));

		ILink l_415_70 = top.createLink("l_415_70");
		l_415_70.createInterface(routers.get(415).createInterface("if_70"));
		l_415_70.createInterface(routers.get(70).createInterface("if_415"));

		ILink l_88_25 = top.createLink("l_88_25");
		l_88_25.createInterface(routers.get(88).createInterface("if_25"));
		l_88_25.createInterface(routers.get(25).createInterface("if_88"));

		ILink l_89_25 = top.createLink("l_89_25");
		l_89_25.createInterface(routers.get(89).createInterface("if_25"));
		l_89_25.createInterface(routers.get(25).createInterface("if_89"));

		ILink l_123_24 = top.createLink("l_123_24");
		l_123_24.createInterface(routers.get(123).createInterface("if_24"));
		l_123_24.createInterface(routers.get(24).createInterface("if_123"));

		ILink l_317_315 = top.createLink("l_317_315");
		l_317_315.createInterface(routers.get(317).createInterface("if_315"));
		l_317_315.createInterface(routers.get(315).createInterface("if_317"));

		ILink l_386_25 = top.createLink("l_386_25");
		l_386_25.createInterface(routers.get(386).createInterface("if_25"));
		l_386_25.createInterface(routers.get(25).createInterface("if_386"));

		ILink l_386_100 = top.createLink("l_386_100");
		l_386_100.createInterface(routers.get(386).createInterface("if_100"));
		l_386_100.createInterface(routers.get(100).createInterface("if_386"));

		ILink l_387_25 = top.createLink("l_387_25");
		l_387_25.createInterface(routers.get(387).createInterface("if_25"));
		l_387_25.createInterface(routers.get(25).createInterface("if_387"));

		ILink l_391_100 = top.createLink("l_391_100");
		l_391_100.createInterface(routers.get(391).createInterface("if_100"));
		l_391_100.createInterface(routers.get(100).createInterface("if_391"));

		ILink l_391_364 = top.createLink("l_391_364");
		l_391_364.createInterface(routers.get(391).createInterface("if_364"));
		l_391_364.createInterface(routers.get(364).createInterface("if_391"));

		ILink l_391_24 = top.createLink("l_391_24");
		l_391_24.createInterface(routers.get(391).createInterface("if_24"));
		l_391_24.createInterface(routers.get(24).createInterface("if_391"));

		ILink l_391_25 = top.createLink("l_391_25");
		l_391_25.createInterface(routers.get(391).createInterface("if_25"));
		l_391_25.createInterface(routers.get(25).createInterface("if_391"));

		ILink l_403_391 = top.createLink("l_403_391");
		l_403_391.createInterface(routers.get(403).createInterface("if_391"));
		l_403_391.createInterface(routers.get(391).createInterface("if_403"));

		ILink l_415_391 = top.createLink("l_415_391");
		l_415_391.createInterface(routers.get(415).createInterface("if_391"));
		l_415_391.createInterface(routers.get(391).createInterface("if_415"));

		ILink l_492_25 = top.createLink("l_492_25");
		l_492_25.createInterface(routers.get(492).createInterface("if_25"));
		l_492_25.createInterface(routers.get(25).createInterface("if_492"));

		ILink l_494_240 = top.createLink("l_494_240");
		l_494_240.createInterface(routers.get(494).createInterface("if_240"));
		l_494_240.createInterface(routers.get(240).createInterface("if_494"));

		ILink l_494_25 = top.createLink("l_494_25");
		l_494_25.createInterface(routers.get(494).createInterface("if_25"));
		l_494_25.createInterface(routers.get(25).createInterface("if_494"));

		ILink l_495_25 = top.createLink("l_495_25");
		l_495_25.createInterface(routers.get(495).createInterface("if_25"));
		l_495_25.createInterface(routers.get(25).createInterface("if_495"));

		ILink l_495_364 = top.createLink("l_495_364");
		l_495_364.createInterface(routers.get(495).createInterface("if_364"));
		l_495_364.createInterface(routers.get(364).createInterface("if_495"));

		ILink l_523_25 = top.createLink("l_523_25");
		l_523_25.createInterface(routers.get(523).createInterface("if_25"));
		l_523_25.createInterface(routers.get(25).createInterface("if_523"));

		ILink l_525_25 = top.createLink("l_525_25");
		l_525_25.createInterface(routers.get(525).createInterface("if_25"));
		l_525_25.createInterface(routers.get(25).createInterface("if_525"));

		ILink l_530_209 = top.createLink("l_530_209");
		l_530_209.createInterface(routers.get(530).createInterface("if_209"));
		l_530_209.createInterface(routers.get(209).createInterface("if_530"));

		ILink l_534_209 = top.createLink("l_534_209");
		l_534_209.createInterface(routers.get(534).createInterface("if_209"));
		l_534_209.createInterface(routers.get(209).createInterface("if_534"));

		ILink l_535_240 = top.createLink("l_535_240");
		l_535_240.createInterface(routers.get(535).createInterface("if_240"));
		l_535_240.createInterface(routers.get(240).createInterface("if_535"));

		ILink l_537_25 = top.createLink("l_537_25");
		l_537_25.createInterface(routers.get(537).createInterface("if_25"));
		l_537_25.createInterface(routers.get(25).createInterface("if_537"));

		ILink l_537_221 = top.createLink("l_537_221");
		l_537_221.createInterface(routers.get(537).createInterface("if_221"));
		l_537_221.createInterface(routers.get(221).createInterface("if_537"));

		ILink l_240_24 = top.createLink("l_240_24");
		l_240_24.createInterface(routers.get(240).createInterface("if_24"));
		l_240_24.createInterface(routers.get(24).createInterface("if_240"));

		ILink l_240_25 = top.createLink("l_240_25");
		l_240_25.createInterface(routers.get(240).createInterface("if_25"));
		l_240_25.createInterface(routers.get(25).createInterface("if_240"));

		ILink l_362_25 = top.createLink("l_362_25");
		l_362_25.createInterface(routers.get(362).createInterface("if_25"));
		l_362_25.createInterface(routers.get(25).createInterface("if_362"));

		ILink l_367_25 = top.createLink("l_367_25");
		l_367_25.createInterface(routers.get(367).createInterface("if_25"));
		l_367_25.createInterface(routers.get(25).createInterface("if_367"));

		ILink l_377_25 = top.createLink("l_377_25");
		l_377_25.createInterface(routers.get(377).createInterface("if_25"));
		l_377_25.createInterface(routers.get(25).createInterface("if_377"));

		ILink l_352_349 = top.createLink("l_352_349");
		l_352_349.createInterface(routers.get(352).createInterface("if_349"));
		l_352_349.createInterface(routers.get(349).createInterface("if_352"));

		ILink l_514_364 = top.createLink("l_514_364");
		l_514_364.createInterface(routers.get(514).createInterface("if_364"));
		l_514_364.createInterface(routers.get(364).createInterface("if_514"));

		ILink l_514_415 = top.createLink("l_514_415");
		l_514_415.createInterface(routers.get(514).createInterface("if_415"));
		l_514_415.createInterface(routers.get(415).createInterface("if_514"));

		ILink l_358_100 = top.createLink("l_358_100");
		l_358_100.createInterface(routers.get(358).createInterface("if_100"));
		l_358_100.createInterface(routers.get(100).createInterface("if_358"));

		ILink l_359_358 = top.createLink("l_359_358");
		l_359_358.createInterface(routers.get(359).createInterface("if_358"));
		l_359_358.createInterface(routers.get(358).createInterface("if_359"));

		ILink l_361_290 = top.createLink("l_361_290");
		l_361_290.createInterface(routers.get(361).createInterface("if_290"));
		l_361_290.createInterface(routers.get(290).createInterface("if_361"));

		ILink l_361_359 = top.createLink("l_361_359");
		l_361_359.createInterface(routers.get(361).createInterface("if_359"));
		l_361_359.createInterface(routers.get(359).createInterface("if_361"));

		ILink l_361_239 = top.createLink("l_361_239");
		l_361_239.createInterface(routers.get(361).createInterface("if_239"));
		l_361_239.createInterface(routers.get(239).createInterface("if_361"));

		ILink l_361_127 = top.createLink("l_361_127");
		l_361_127.createInterface(routers.get(361).createInterface("if_127"));
		l_361_127.createInterface(routers.get(127).createInterface("if_361"));

		ILink l_416_361 = top.createLink("l_416_361");
		l_416_361.createInterface(routers.get(416).createInterface("if_361"));
		l_416_361.createInterface(routers.get(361).createInterface("if_416"));

		ILink l_419_361 = top.createLink("l_419_361");
		l_419_361.createInterface(routers.get(419).createInterface("if_361"));
		l_419_361.createInterface(routers.get(361).createInterface("if_419"));

		ILink l_420_361 = top.createLink("l_420_361");
		l_420_361.createInterface(routers.get(420).createInterface("if_361"));
		l_420_361.createInterface(routers.get(361).createInterface("if_420"));

		ILink l_373_362 = top.createLink("l_373_362");
		l_373_362.createInterface(routers.get(373).createInterface("if_362"));
		l_373_362.createInterface(routers.get(362).createInterface("if_373"));

		ILink l_373_363 = top.createLink("l_373_363");
		l_373_363.createInterface(routers.get(373).createInterface("if_363"));
		l_373_363.createInterface(routers.get(363).createInterface("if_373"));

		ILink l_364_290 = top.createLink("l_364_290");
		l_364_290.createInterface(routers.get(364).createInterface("if_290"));
		l_364_290.createInterface(routers.get(290).createInterface("if_364"));

		ILink l_364_291 = top.createLink("l_364_291");
		l_364_291.createInterface(routers.get(364).createInterface("if_291"));
		l_364_291.createInterface(routers.get(291).createInterface("if_364"));

		ILink l_364_292 = top.createLink("l_364_292");
		l_364_292.createInterface(routers.get(364).createInterface("if_292"));
		l_364_292.createInterface(routers.get(292).createInterface("if_364"));

		ILink l_364_233 = top.createLink("l_364_233");
		l_364_233.createInterface(routers.get(364).createInterface("if_233"));
		l_364_233.createInterface(routers.get(233).createInterface("if_364"));

		ILink l_364_330 = top.createLink("l_364_330");
		l_364_330.createInterface(routers.get(364).createInterface("if_330"));
		l_364_330.createInterface(routers.get(330).createInterface("if_364"));

		ILink l_364_44 = top.createLink("l_364_44");
		l_364_44.createInterface(routers.get(364).createInterface("if_44"));
		l_364_44.createInterface(routers.get(44).createInterface("if_364"));

		ILink l_364_45 = top.createLink("l_364_45");
		l_364_45.createInterface(routers.get(364).createInterface("if_45"));
		l_364_45.createInterface(routers.get(45).createInterface("if_364"));

		ILink l_364_239 = top.createLink("l_364_239");
		l_364_239.createInterface(routers.get(364).createInterface("if_239"));
		l_364_239.createInterface(routers.get(239).createInterface("if_364"));

		ILink l_364_240 = top.createLink("l_364_240");
		l_364_240.createInterface(routers.get(364).createInterface("if_240"));
		l_364_240.createInterface(routers.get(240).createInterface("if_364"));

		ILink l_364_244 = top.createLink("l_364_244");
		l_364_244.createInterface(routers.get(364).createInterface("if_244"));
		l_364_244.createInterface(routers.get(244).createInterface("if_364"));

		ILink l_364_118 = top.createLink("l_364_118");
		l_364_118.createInterface(routers.get(364).createInterface("if_118"));
		l_364_118.createInterface(routers.get(118).createInterface("if_364"));

		ILink l_364_282 = top.createLink("l_364_282");
		l_364_282.createInterface(routers.get(364).createInterface("if_282"));
		l_364_282.createInterface(routers.get(282).createInterface("if_364"));

		ILink l_364_253 = top.createLink("l_364_253");
		l_364_253.createInterface(routers.get(364).createInterface("if_253"));
		l_364_253.createInterface(routers.get(253).createInterface("if_364"));

		ILink l_420_364 = top.createLink("l_420_364");
		l_420_364.createInterface(routers.get(420).createInterface("if_364"));
		l_420_364.createInterface(routers.get(364).createInterface("if_420"));

		ILink l_456_364 = top.createLink("l_456_364");
		l_456_364.createInterface(routers.get(456).createInterface("if_364"));
		l_456_364.createInterface(routers.get(364).createInterface("if_456"));

		ILink l_478_364 = top.createLink("l_478_364");
		l_478_364.createInterface(routers.get(478).createInterface("if_364"));
		l_478_364.createInterface(routers.get(364).createInterface("if_478"));

		ILink l_367_100 = top.createLink("l_367_100");
		l_367_100.createInterface(routers.get(367).createInterface("if_100"));
		l_367_100.createInterface(routers.get(100).createInterface("if_367"));

		ILink l_367_359 = top.createLink("l_367_359");
		l_367_359.createInterface(routers.get(367).createInterface("if_359"));
		l_367_359.createInterface(routers.get(359).createInterface("if_367"));

		ILink l_367_282 = top.createLink("l_367_282");
		l_367_282.createInterface(routers.get(367).createInterface("if_282"));
		l_367_282.createInterface(routers.get(282).createInterface("if_367"));

		ILink l_368_359 = top.createLink("l_368_359");
		l_368_359.createInterface(routers.get(368).createInterface("if_359"));
		l_368_359.createInterface(routers.get(359).createInterface("if_368"));

		ILink l_372_100 = top.createLink("l_372_100");
		l_372_100.createInterface(routers.get(372).createInterface("if_100"));
		l_372_100.createInterface(routers.get(100).createInterface("if_372"));

		ILink l_372_359 = top.createLink("l_372_359");
		l_372_359.createInterface(routers.get(372).createInterface("if_359"));
		l_372_359.createInterface(routers.get(359).createInterface("if_372"));

		ILink l_372_239 = top.createLink("l_372_239");
		l_372_239.createInterface(routers.get(372).createInterface("if_239"));
		l_372_239.createInterface(routers.get(239).createInterface("if_372"));

		ILink l_372_240 = top.createLink("l_372_240");
		l_372_240.createInterface(routers.get(372).createInterface("if_240"));
		l_372_240.createInterface(routers.get(240).createInterface("if_372"));

		ILink l_428_372 = top.createLink("l_428_372");
		l_428_372.createInterface(routers.get(428).createInterface("if_372"));
		l_428_372.createInterface(routers.get(372).createInterface("if_428"));

		ILink l_377_100 = top.createLink("l_377_100");
		l_377_100.createInterface(routers.get(377).createInterface("if_100"));
		l_377_100.createInterface(routers.get(100).createInterface("if_377"));

		ILink l_377_330 = top.createLink("l_377_330");
		l_377_330.createInterface(routers.get(377).createInterface("if_330"));
		l_377_330.createInterface(routers.get(330).createInterface("if_377"));

		ILink l_377_253 = top.createLink("l_377_253");
		l_377_253.createInterface(routers.get(377).createInterface("if_253"));
		l_377_253.createInterface(routers.get(253).createInterface("if_377"));

		ILink l_415_290 = top.createLink("l_415_290");
		l_415_290.createInterface(routers.get(415).createInterface("if_290"));
		l_415_290.createInterface(routers.get(290).createInterface("if_415"));

		ILink l_415_291 = top.createLink("l_415_291");
		l_415_291.createInterface(routers.get(415).createInterface("if_291"));
		l_415_291.createInterface(routers.get(291).createInterface("if_415"));

		ILink l_415_292 = top.createLink("l_415_292");
		l_415_292.createInterface(routers.get(415).createInterface("if_292"));
		l_415_292.createInterface(routers.get(292).createInterface("if_415"));

		ILink l_415_44 = top.createLink("l_415_44");
		l_415_44.createInterface(routers.get(415).createInterface("if_44"));
		l_415_44.createInterface(routers.get(44).createInterface("if_415"));

		ILink l_415_282 = top.createLink("l_415_282");
		l_415_282.createInterface(routers.get(415).createInterface("if_282"));
		l_415_282.createInterface(routers.get(282).createInterface("if_415"));

		ILink l_421_415 = top.createLink("l_421_415");
		l_421_415.createInterface(routers.get(421).createInterface("if_415"));
		l_421_415.createInterface(routers.get(415).createInterface("if_421"));

		ILink l_489_304 = top.createLink("l_489_304");
		l_489_304.createInterface(routers.get(489).createInterface("if_304"));
		l_489_304.createInterface(routers.get(304).createInterface("if_489"));

		ILink l_498_330 = top.createLink("l_498_330");
		l_498_330.createInterface(routers.get(498).createInterface("if_330"));
		l_498_330.createInterface(routers.get(330).createInterface("if_498"));

		ILink l_568_305 = top.createLink("l_568_305");
		l_568_305.createInterface(routers.get(568).createInterface("if_305"));
		l_568_305.createInterface(routers.get(305).createInterface("if_568"));

		ILink l_238_68 = top.createLink("l_238_68");
		l_238_68.createInterface(routers.get(238).createInterface("if_68"));
		l_238_68.createInterface(routers.get(68).createInterface("if_238"));

		ILink l_238_55 = top.createLink("l_238_55");
		l_238_55.createInterface(routers.get(238).createInterface("if_55"));
		l_238_55.createInterface(routers.get(55).createInterface("if_238"));

		ILink l_239_68 = top.createLink("l_239_68");
		l_239_68.createInterface(routers.get(239).createInterface("if_68"));
		l_239_68.createInterface(routers.get(68).createInterface("if_239"));

		ILink l_239_55 = top.createLink("l_239_55");
		l_239_55.createInterface(routers.get(239).createInterface("if_55"));
		l_239_55.createInterface(routers.get(55).createInterface("if_239"));

		ILink l_239_118 = top.createLink("l_239_118");
		l_239_118.createInterface(routers.get(239).createInterface("if_118"));
		l_239_118.createInterface(routers.get(118).createInterface("if_239"));

		ILink l_239_119 = top.createLink("l_239_119");
		l_239_119.createInterface(routers.get(239).createInterface("if_119"));
		l_239_119.createInterface(routers.get(119).createInterface("if_239"));

		ILink l_349_239 = top.createLink("l_349_239");
		l_349_239.createInterface(routers.get(349).createInterface("if_239"));
		l_349_239.createInterface(routers.get(239).createInterface("if_349"));

		ILink l_428_239 = top.createLink("l_428_239");
		l_428_239.createInterface(routers.get(428).createInterface("if_239"));
		l_428_239.createInterface(routers.get(239).createInterface("if_428"));

		ILink l_240_68 = top.createLink("l_240_68");
		l_240_68.createInterface(routers.get(240).createInterface("if_68"));
		l_240_68.createInterface(routers.get(68).createInterface("if_240"));

		ILink l_240_118 = top.createLink("l_240_118");
		l_240_118.createInterface(routers.get(240).createInterface("if_118"));
		l_240_118.createInterface(routers.get(118).createInterface("if_240"));

		ILink l_240_55 = top.createLink("l_240_55");
		l_240_55.createInterface(routers.get(240).createInterface("if_55"));
		l_240_55.createInterface(routers.get(55).createInterface("if_240"));

		ILink l_240_60 = top.createLink("l_240_60");
		l_240_60.createInterface(routers.get(240).createInterface("if_60"));
		l_240_60.createInterface(routers.get(60).createInterface("if_240"));

		ILink l_330_240 = top.createLink("l_330_240");
		l_330_240.createInterface(routers.get(330).createInterface("if_240"));
		l_330_240.createInterface(routers.get(240).createInterface("if_330"));

		ILink l_349_240 = top.createLink("l_349_240");
		l_349_240.createInterface(routers.get(349).createInterface("if_240"));
		l_349_240.createInterface(routers.get(240).createInterface("if_349"));

		ILink l_428_240 = top.createLink("l_428_240");
		l_428_240.createInterface(routers.get(428).createInterface("if_240"));
		l_428_240.createInterface(routers.get(240).createInterface("if_428"));

		ILink l_304_253 = top.createLink("l_304_253");
		l_304_253.createInterface(routers.get(304).createInterface("if_253"));
		l_304_253.createInterface(routers.get(253).createInterface("if_304"));

		ILink l_44_28 = top.createLink("l_44_28");
		l_44_28.createInterface(routers.get(44).createInterface("if_28"));
		l_44_28.createInterface(routers.get(28).createInterface("if_44"));

		ILink l_244_243 = top.createLink("l_244_243");
		l_244_243.createInterface(routers.get(244).createInterface("if_243"));
		l_244_243.createInterface(routers.get(243).createInterface("if_244"));

		ILink l_244_44 = top.createLink("l_244_44");
		l_244_44.createInterface(routers.get(244).createInterface("if_44"));
		l_244_44.createInterface(routers.get(44).createInterface("if_244"));

		ILink l_249_244 = top.createLink("l_249_244");
		l_249_244.createInterface(routers.get(249).createInterface("if_244"));
		l_249_244.createInterface(routers.get(244).createInterface("if_249"));

		ILink l_253_244 = top.createLink("l_253_244");
		l_253_244.createInterface(routers.get(253).createInterface("if_244"));
		l_253_244.createInterface(routers.get(244).createInterface("if_253"));

		ILink l_252_249 = top.createLink("l_252_249");
		l_252_249.createInterface(routers.get(252).createInterface("if_249"));
		l_252_249.createInterface(routers.get(249).createInterface("if_252"));

		ILink l_343_252 = top.createLink("l_343_252");
		l_343_252.createInterface(routers.get(343).createInterface("if_252"));
		l_343_252.createInterface(routers.get(252).createInterface("if_343"));

		ILink l_508_252 = top.createLink("l_508_252");
		l_508_252.createInterface(routers.get(508).createInterface("if_252"));
		l_508_252.createInterface(routers.get(252).createInterface("if_508"));

		ILink l_329_44 = top.createLink("l_329_44");
		l_329_44.createInterface(routers.get(329).createInterface("if_44"));
		l_329_44.createInterface(routers.get(44).createInterface("if_329"));

		ILink l_330_44 = top.createLink("l_330_44");
		l_330_44.createInterface(routers.get(330).createInterface("if_44"));
		l_330_44.createInterface(routers.get(44).createInterface("if_330"));

		ILink l_330_46 = top.createLink("l_330_46");
		l_330_46.createInterface(routers.get(330).createInterface("if_46"));
		l_330_46.createInterface(routers.get(46).createInterface("if_330"));

		ILink l_343_330 = top.createLink("l_343_330");
		l_343_330.createInterface(routers.get(343).createInterface("if_330"));
		l_343_330.createInterface(routers.get(330).createInterface("if_343"));

		ILink l_344_330 = top.createLink("l_344_330");
		l_344_330.createInterface(routers.get(344).createInterface("if_330"));
		l_344_330.createInterface(routers.get(330).createInterface("if_344"));

		ILink l_469_330 = top.createLink("l_469_330");
		l_469_330.createInterface(routers.get(469).createInterface("if_330"));
		l_469_330.createInterface(routers.get(330).createInterface("if_469"));

		ILink l_381_44 = top.createLink("l_381_44");
		l_381_44.createInterface(routers.get(381).createInterface("if_44"));
		l_381_44.createInterface(routers.get(44).createInterface("if_381"));

		ILink l_425_344 = top.createLink("l_425_344");
		l_425_344.createInterface(routers.get(425).createInterface("if_344"));
		l_425_344.createInterface(routers.get(344).createInterface("if_425"));

		ILink l_425_343 = top.createLink("l_425_343");
		l_425_343.createInterface(routers.get(425).createInterface("if_343"));
		l_425_343.createInterface(routers.get(343).createInterface("if_425"));

		ILink l_477_44 = top.createLink("l_477_44");
		l_477_44.createInterface(routers.get(477).createInterface("if_44"));
		l_477_44.createInterface(routers.get(44).createInterface("if_477"));

		ILink l_478_44 = top.createLink("l_478_44");
		l_478_44.createInterface(routers.get(478).createInterface("if_44"));
		l_478_44.createInterface(routers.get(44).createInterface("if_478"));

		ILink l_481_44 = top.createLink("l_481_44");
		l_481_44.createInterface(routers.get(481).createInterface("if_44"));
		l_481_44.createInterface(routers.get(44).createInterface("if_481"));

		ILink l_481_253 = top.createLink("l_481_253");
		l_481_253.createInterface(routers.get(481).createInterface("if_253"));
		l_481_253.createInterface(routers.get(253).createInterface("if_481"));

		ILink l_482_253 = top.createLink("l_482_253");
		l_482_253.createInterface(routers.get(482).createInterface("if_253"));
		l_482_253.createInterface(routers.get(253).createInterface("if_482"));

		ILink l_484_253 = top.createLink("l_484_253");
		l_484_253.createInterface(routers.get(484).createInterface("if_253"));
		l_484_253.createInterface(routers.get(253).createInterface("if_484"));

		ILink l_511_44 = top.createLink("l_511_44");
		l_511_44.createInterface(routers.get(511).createInterface("if_44"));
		l_511_44.createInterface(routers.get(44).createInterface("if_511"));

		ILink l_512_44 = top.createLink("l_512_44");
		l_512_44.createInterface(routers.get(512).createInterface("if_44"));
		l_512_44.createInterface(routers.get(44).createInterface("if_512"));

		ILink l_572_44 = top.createLink("l_572_44");
		l_572_44.createInterface(routers.get(572).createInterface("if_44"));
		l_572_44.createInterface(routers.get(44).createInterface("if_572"));

		ILink l_573_469 = top.createLink("l_573_469");
		l_573_469.createInterface(routers.get(573).createInterface("if_469"));
		l_573_469.createInterface(routers.get(469).createInterface("if_573"));

		ILink l_422_16 = top.createLink("l_422_16");
		l_422_16.createInterface(routers.get(422).createInterface("if_16"));
		l_422_16.createInterface(routers.get(16).createInterface("if_422"));

		ILink l_610_16 = top.createLink("l_610_16");
		l_610_16.createInterface(routers.get(610).createInterface("if_16"));
		l_610_16.createInterface(routers.get(16).createInterface("if_610"));

		ILink l_611_16 = top.createLink("l_611_16");
		l_611_16.createInterface(routers.get(611).createInterface("if_16"));
		l_611_16.createInterface(routers.get(16).createInterface("if_611"));

		ILink l_43_42 = top.createLink("l_43_42");
		l_43_42.createInterface(routers.get(43).createInterface("if_42"));
		l_43_42.createInterface(routers.get(42).createInterface("if_43"));

		ILink l_421_138 = top.createLink("l_421_138");
		l_421_138.createInterface(routers.get(421).createInterface("if_138"));
		l_421_138.createInterface(routers.get(138).createInterface("if_421"));

		ILink l_431_138 = top.createLink("l_431_138");
		l_431_138.createInterface(routers.get(431).createInterface("if_138"));
		l_431_138.createInterface(routers.get(138).createInterface("if_431"));

		ILink l_230_100 = top.createLink("l_230_100");
		l_230_100.createInterface(routers.get(230).createInterface("if_100"));
		l_230_100.createInterface(routers.get(100).createInterface("if_230"));

		ILink l_282_231 = top.createLink("l_282_231");
		l_282_231.createInterface(routers.get(282).createInterface("if_231"));
		l_282_231.createInterface(routers.get(231).createInterface("if_282"));

		ILink l_290_231 = top.createLink("l_290_231");
		l_290_231.createInterface(routers.get(290).createInterface("if_231"));
		l_290_231.createInterface(routers.get(231).createInterface("if_290"));

		ILink l_291_231 = top.createLink("l_291_231");
		l_291_231.createInterface(routers.get(291).createInterface("if_231"));
		l_291_231.createInterface(routers.get(231).createInterface("if_291"));

		ILink l_292_231 = top.createLink("l_292_231");
		l_292_231.createInterface(routers.get(292).createInterface("if_231"));
		l_292_231.createInterface(routers.get(231).createInterface("if_292"));

		ILink l_249_44 = top.createLink("l_249_44");
		l_249_44.createInterface(routers.get(249).createInterface("if_44"));
		l_249_44.createInterface(routers.get(44).createInterface("if_249"));

		ILink l_253_44 = top.createLink("l_253_44");
		l_253_44.createInterface(routers.get(253).createInterface("if_44"));
		l_253_44.createInterface(routers.get(44).createInterface("if_253"));

		ILink l_286_253 = top.createLink("l_286_253");
		l_286_253.createInterface(routers.get(286).createInterface("if_253"));
		l_286_253.createInterface(routers.get(253).createInterface("if_286"));

		ILink l_343_253 = top.createLink("l_343_253");
		l_343_253.createInterface(routers.get(343).createInterface("if_253"));
		l_343_253.createInterface(routers.get(253).createInterface("if_343"));

		ILink l_344_253 = top.createLink("l_344_253");
		l_344_253.createInterface(routers.get(344).createInterface("if_253"));
		l_344_253.createInterface(routers.get(253).createInterface("if_344"));

		ILink l_463_344 = top.createLink("l_463_344");
		l_463_344.createInterface(routers.get(463).createInterface("if_344"));
		l_463_344.createInterface(routers.get(344).createInterface("if_463"));

		ILink l_463_343 = top.createLink("l_463_343");
		l_463_343.createInterface(routers.get(463).createInterface("if_343"));
		l_463_343.createInterface(routers.get(343).createInterface("if_463"));

		ILink l_175_44 = top.createLink("l_175_44");
		l_175_44.createInterface(routers.get(175).createInterface("if_44"));
		l_175_44.createInterface(routers.get(44).createInterface("if_175"));

		ILink l_448_343 = top.createLink("l_448_343");
		l_448_343.createInterface(routers.get(448).createInterface("if_343"));
		l_448_343.createInterface(routers.get(343).createInterface("if_448"));

		ILink l_448_44 = top.createLink("l_448_44");
		l_448_44.createInterface(routers.get(448).createInterface("if_44"));
		l_448_44.createInterface(routers.get(44).createInterface("if_448"));

		ILink l_448_118 = top.createLink("l_448_118");
		l_448_118.createInterface(routers.get(448).createInterface("if_118"));
		l_448_118.createInterface(routers.get(118).createInterface("if_448"));

		ILink l_448_119 = top.createLink("l_448_119");
		l_448_119.createInterface(routers.get(448).createInterface("if_119"));
		l_448_119.createInterface(routers.get(119).createInterface("if_448"));

		ILink l_449_343 = top.createLink("l_449_343");
		l_449_343.createInterface(routers.get(449).createInterface("if_343"));
		l_449_343.createInterface(routers.get(343).createInterface("if_449"));

		ILink l_449_44 = top.createLink("l_449_44");
		l_449_44.createInterface(routers.get(449).createInterface("if_44"));
		l_449_44.createInterface(routers.get(44).createInterface("if_449"));

		ILink l_449_118 = top.createLink("l_449_118");
		l_449_118.createInterface(routers.get(449).createInterface("if_118"));
		l_449_118.createInterface(routers.get(118).createInterface("if_449"));

		ILink l_449_119 = top.createLink("l_449_119");
		l_449_119.createInterface(routers.get(449).createInterface("if_119"));
		l_449_119.createInterface(routers.get(119).createInterface("if_449"));

		ILink l_456_44 = top.createLink("l_456_44");
		l_456_44.createInterface(routers.get(456).createInterface("if_44"));
		l_456_44.createInterface(routers.get(44).createInterface("if_456"));

		ILink l_456_118 = top.createLink("l_456_118");
		l_456_118.createInterface(routers.get(456).createInterface("if_118"));
		l_456_118.createInterface(routers.get(118).createInterface("if_456"));

		ILink l_456_119 = top.createLink("l_456_119");
		l_456_119.createInterface(routers.get(456).createInterface("if_119"));
		l_456_119.createInterface(routers.get(119).createInterface("if_456"));

		ILink l_469_344 = top.createLink("l_469_344");
		l_469_344.createInterface(routers.get(469).createInterface("if_344"));
		l_469_344.createInterface(routers.get(344).createInterface("if_469"));

		ILink l_469_343 = top.createLink("l_469_343");
		l_469_343.createInterface(routers.get(469).createInterface("if_343"));
		l_469_343.createInterface(routers.get(343).createInterface("if_469"));

		ILink l_469_44 = top.createLink("l_469_44");
		l_469_44.createInterface(routers.get(469).createInterface("if_44"));
		l_469_44.createInterface(routers.get(44).createInterface("if_469"));

		ILink l_44_34 = top.createLink("l_44_34");
		l_44_34.createInterface(routers.get(44).createInterface("if_34"));
		l_44_34.createInterface(routers.get(34).createInterface("if_44"));

		ILink l_282_44 = top.createLink("l_282_44");
		l_282_44.createInterface(routers.get(282).createInterface("if_44"));
		l_282_44.createInterface(routers.get(44).createInterface("if_282"));

		ILink l_6_2 = top.createLink("l_6_2");
		l_6_2.createInterface(routers.get(6).createInterface("if_2"));
		l_6_2.createInterface(routers.get(2).createInterface("if_6"));

		ILink l_12_2 = top.createLink("l_12_2");
		l_12_2.createInterface(routers.get(12).createInterface("if_2"));
		l_12_2.createInterface(routers.get(2).createInterface("if_12"));

		ILink l_14_2 = top.createLink("l_14_2");
		l_14_2.createInterface(routers.get(14).createInterface("if_2"));
		l_14_2.createInterface(routers.get(2).createInterface("if_14"));

		ILink l_19_2 = top.createLink("l_19_2");
		l_19_2.createInterface(routers.get(19).createInterface("if_2"));
		l_19_2.createInterface(routers.get(2).createInterface("if_19"));

		ILink l_26_2 = top.createLink("l_26_2");
		l_26_2.createInterface(routers.get(26).createInterface("if_2"));
		l_26_2.createInterface(routers.get(2).createInterface("if_26"));

		ILink l_38_2 = top.createLink("l_38_2");
		l_38_2.createInterface(routers.get(38).createInterface("if_2"));
		l_38_2.createInterface(routers.get(2).createInterface("if_38"));

		ILink l_39_2 = top.createLink("l_39_2");
		l_39_2.createInterface(routers.get(39).createInterface("if_2"));
		l_39_2.createInterface(routers.get(2).createInterface("if_39"));

		ILink l_77_2 = top.createLink("l_77_2");
		l_77_2.createInterface(routers.get(77).createInterface("if_2"));
		l_77_2.createInterface(routers.get(2).createInterface("if_77"));

		ILink l_80_2 = top.createLink("l_80_2");
		l_80_2.createInterface(routers.get(80).createInterface("if_2"));
		l_80_2.createInterface(routers.get(2).createInterface("if_80"));

		ILink l_111_2 = top.createLink("l_111_2");
		l_111_2.createInterface(routers.get(111).createInterface("if_2"));
		l_111_2.createInterface(routers.get(2).createInterface("if_111"));

		ILink l_116_2 = top.createLink("l_116_2");
		l_116_2.createInterface(routers.get(116).createInterface("if_2"));
		l_116_2.createInterface(routers.get(2).createInterface("if_116"));

		ILink l_120_2 = top.createLink("l_120_2");
		l_120_2.createInterface(routers.get(120).createInterface("if_2"));
		l_120_2.createInterface(routers.get(2).createInterface("if_120"));

		ILink l_133_2 = top.createLink("l_133_2");
		l_133_2.createInterface(routers.get(133).createInterface("if_2"));
		l_133_2.createInterface(routers.get(2).createInterface("if_133"));

		ILink l_134_2 = top.createLink("l_134_2");
		l_134_2.createInterface(routers.get(134).createInterface("if_2"));
		l_134_2.createInterface(routers.get(2).createInterface("if_134"));

		ILink l_184_2 = top.createLink("l_184_2");
		l_184_2.createInterface(routers.get(184).createInterface("if_2"));
		l_184_2.createInterface(routers.get(2).createInterface("if_184"));

		ILink l_265_2 = top.createLink("l_265_2");
		l_265_2.createInterface(routers.get(265).createInterface("if_2"));
		l_265_2.createInterface(routers.get(2).createInterface("if_265"));

		ILink l_268_2 = top.createLink("l_268_2");
		l_268_2.createInterface(routers.get(268).createInterface("if_2"));
		l_268_2.createInterface(routers.get(2).createInterface("if_268"));

		ILink l_326_2 = top.createLink("l_326_2");
		l_326_2.createInterface(routers.get(326).createInterface("if_2"));
		l_326_2.createInterface(routers.get(2).createInterface("if_326"));

		ILink l_334_2 = top.createLink("l_334_2");
		l_334_2.createInterface(routers.get(334).createInterface("if_2"));
		l_334_2.createInterface(routers.get(2).createInterface("if_334"));

		ILink l_378_2 = top.createLink("l_378_2");
		l_378_2.createInterface(routers.get(378).createInterface("if_2"));
		l_378_2.createInterface(routers.get(2).createInterface("if_378"));

		ILink l_334_21 = top.createLink("l_334_21");
		l_334_21.createInterface(routers.get(334).createInterface("if_21"));
		l_334_21.createInterface(routers.get(21).createInterface("if_334"));

		ILink l_335_21 = top.createLink("l_335_21");
		l_335_21.createInterface(routers.get(335).createInterface("if_21"));
		l_335_21.createInterface(routers.get(21).createInterface("if_335"));

		ILink l_378_21 = top.createLink("l_378_21");
		l_378_21.createInterface(routers.get(378).createInterface("if_21"));
		l_378_21.createInterface(routers.get(21).createInterface("if_378"));

		ILink l_334_22 = top.createLink("l_334_22");
		l_334_22.createInterface(routers.get(334).createInterface("if_22"));
		l_334_22.createInterface(routers.get(22).createInterface("if_334"));

		ILink l_335_22 = top.createLink("l_335_22");
		l_335_22.createInterface(routers.get(335).createInterface("if_22"));
		l_335_22.createInterface(routers.get(22).createInterface("if_335"));

		ILink l_378_22 = top.createLink("l_378_22");
		l_378_22.createInterface(routers.get(378).createInterface("if_22"));
		l_378_22.createInterface(routers.get(22).createInterface("if_378"));

		ILink l_334_41 = top.createLink("l_334_41");
		l_334_41.createInterface(routers.get(334).createInterface("if_41"));
		l_334_41.createInterface(routers.get(41).createInterface("if_334"));

		ILink l_335_41 = top.createLink("l_335_41");
		l_335_41.createInterface(routers.get(335).createInterface("if_41"));
		l_335_41.createInterface(routers.get(41).createInterface("if_335"));

		ILink l_289_66 = top.createLink("l_289_66");
		l_289_66.createInterface(routers.get(289).createInterface("if_66"));
		l_289_66.createInterface(routers.get(66).createInterface("if_289"));

		ILink l_289_73 = top.createLink("l_289_73");
		l_289_73.createInterface(routers.get(289).createInterface("if_73"));
		l_289_73.createInterface(routers.get(73).createInterface("if_289"));

		ILink l_289_60 = top.createLink("l_289_60");
		l_289_60.createInterface(routers.get(289).createInterface("if_60"));
		l_289_60.createInterface(routers.get(60).createInterface("if_289"));

		ILink l_334_289 = top.createLink("l_334_289");
		l_334_289.createInterface(routers.get(334).createInterface("if_289"));
		l_334_289.createInterface(routers.get(289).createInterface("if_334"));

		ILink l_335_289 = top.createLink("l_335_289");
		l_335_289.createInterface(routers.get(335).createInterface("if_289"));
		l_335_289.createInterface(routers.get(289).createInterface("if_335"));

		ILink l_272_37 = top.createLink("l_272_37");
		l_272_37.createInterface(routers.get(272).createInterface("if_37"));
		l_272_37.createInterface(routers.get(37).createInterface("if_272"));

		ILink l_282_37 = top.createLink("l_282_37");
		l_282_37.createInterface(routers.get(282).createInterface("if_37"));
		l_282_37.createInterface(routers.get(37).createInterface("if_282"));

		ILink l_429_37 = top.createLink("l_429_37");
		l_429_37.createInterface(routers.get(429).createInterface("if_37"));
		l_429_37.createInterface(routers.get(37).createInterface("if_429"));

		ILink l_521_37 = top.createLink("l_521_37");
		l_521_37.createInterface(routers.get(521).createInterface("if_37"));
		l_521_37.createInterface(routers.get(37).createInterface("if_521"));

		ILink l_522_37 = top.createLink("l_522_37");
		l_522_37.createInterface(routers.get(522).createInterface("if_37"));
		l_522_37.createInterface(routers.get(37).createInterface("if_522"));

		ILink l_179_177 = top.createLink("l_179_177");
		l_179_177.createInterface(routers.get(179).createInterface("if_177"));
		l_179_177.createInterface(routers.get(177).createInterface("if_179"));

		ILink l_180_100 = top.createLink("l_180_100");
		l_180_100.createInterface(routers.get(180).createInterface("if_100"));
		l_180_100.createInterface(routers.get(100).createInterface("if_180"));

		ILink l_296_180 = top.createLink("l_296_180");
		l_296_180.createInterface(routers.get(296).createInterface("if_180"));
		l_296_180.createInterface(routers.get(180).createInterface("if_296"));

		ILink l_272_271 = top.createLink("l_272_271");
		l_272_271.createInterface(routers.get(272).createInterface("if_271"));
		l_272_271.createInterface(routers.get(271).createInterface("if_272"));

		ILink l_327_292 = top.createLink("l_327_292");
		l_327_292.createInterface(routers.get(327).createInterface("if_292"));
		l_327_292.createInterface(routers.get(292).createInterface("if_327"));

		ILink l_629_179 = top.createLink("l_629_179");
		l_629_179.createInterface(routers.get(629).createInterface("if_179"));
		l_629_179.createInterface(routers.get(179).createInterface("if_629"));

		ILink l_630_179 = top.createLink("l_630_179");
		l_630_179.createInterface(routers.get(630).createInterface("if_179"));
		l_630_179.createInterface(routers.get(179).createInterface("if_630"));

		ILink l_106_72 = top.createLink("l_106_72");
		l_106_72.createInterface(routers.get(106).createInterface("if_72"));
		l_106_72.createInterface(routers.get(72).createInterface("if_106"));

		ILink l_107_72 = top.createLink("l_107_72");
		l_107_72.createInterface(routers.get(107).createInterface("if_72"));
		l_107_72.createInterface(routers.get(72).createInterface("if_107"));

		ILink l_225_72 = top.createLink("l_225_72");
		l_225_72.createInterface(routers.get(225).createInterface("if_72"));
		l_225_72.createInterface(routers.get(72).createInterface("if_225"));

		ILink l_73_66 = top.createLink("l_73_66");
		l_73_66.createInterface(routers.get(73).createInterface("if_66"));
		l_73_66.createInterface(routers.get(66).createInterface("if_73"));

		ILink l_416_115 = top.createLink("l_416_115");
		l_416_115.createInterface(routers.get(416).createInterface("if_115"));
		l_416_115.createInterface(routers.get(115).createInterface("if_416"));

		ILink l_419_115 = top.createLink("l_419_115");
		l_419_115.createInterface(routers.get(419).createInterface("if_115"));
		l_419_115.createInterface(routers.get(115).createInterface("if_419"));

		ILink l_420_115 = top.createLink("l_420_115");
		l_420_115.createInterface(routers.get(420).createInterface("if_115"));
		l_420_115.createInterface(routers.get(115).createInterface("if_420"));

		ILink l_588_115 = top.createLink("l_588_115");
		l_588_115.createInterface(routers.get(588).createInterface("if_115"));
		l_588_115.createInterface(routers.get(115).createInterface("if_588"));

		ILink l_593_115 = top.createLink("l_593_115");
		l_593_115.createInterface(routers.get(593).createInterface("if_115"));
		l_593_115.createInterface(routers.get(115).createInterface("if_593"));

		ILink l_127_125 = top.createLink("l_127_125");
		l_127_125.createInterface(routers.get(127).createInterface("if_125"));
		l_127_125.createInterface(routers.get(125).createInterface("if_127"));

		ILink l_127_126 = top.createLink("l_127_126");
		l_127_126.createInterface(routers.get(127).createInterface("if_126"));
		l_127_126.createInterface(routers.get(126).createInterface("if_127"));

		ILink l_416_126 = top.createLink("l_416_126");
		l_416_126.createInterface(routers.get(416).createInterface("if_126"));
		l_416_126.createInterface(routers.get(126).createInterface("if_416"));

		ILink l_419_126 = top.createLink("l_419_126");
		l_419_126.createInterface(routers.get(419).createInterface("if_126"));
		l_419_126.createInterface(routers.get(126).createInterface("if_419"));

		ILink l_546_126 = top.createLink("l_546_126");
		l_546_126.createInterface(routers.get(546).createInterface("if_126"));
		l_546_126.createInterface(routers.get(126).createInterface("if_546"));

		ILink l_588_126 = top.createLink("l_588_126");
		l_588_126.createInterface(routers.get(588).createInterface("if_126"));
		l_588_126.createInterface(routers.get(126).createInterface("if_588"));

		ILink l_595_126 = top.createLink("l_595_126");
		l_595_126.createInterface(routers.get(595).createInterface("if_126"));
		l_595_126.createInterface(routers.get(126).createInterface("if_595"));

		ILink l_130_127 = top.createLink("l_130_127");
		l_130_127.createInterface(routers.get(130).createInterface("if_127"));
		l_130_127.createInterface(routers.get(127).createInterface("if_130"));

		ILink l_547_127 = top.createLink("l_547_127");
		l_547_127.createInterface(routers.get(547).createInterface("if_127"));
		l_547_127.createInterface(routers.get(127).createInterface("if_547"));

		ILink l_127_66 = top.createLink("l_127_66");
		l_127_66.createInterface(routers.get(127).createInterface("if_66"));
		l_127_66.createInterface(routers.get(66).createInterface("if_127"));

		ILink l_428_100 = top.createLink("l_428_100");
		l_428_100.createInterface(routers.get(428).createInterface("if_100"));
		l_428_100.createInterface(routers.get(100).createInterface("if_428"));

		ILink l_428_106 = top.createLink("l_428_106");
		l_428_106.createInterface(routers.get(428).createInterface("if_106"));
		l_428_106.createInterface(routers.get(106).createInterface("if_428"));

		ILink l_428_282 = top.createLink("l_428_282");
		l_428_282.createInterface(routers.get(428).createInterface("if_282"));
		l_428_282.createInterface(routers.get(282).createInterface("if_428"));

		ILink l_595_100 = top.createLink("l_595_100");
		l_595_100.createInterface(routers.get(595).createInterface("if_100"));
		l_595_100.createInterface(routers.get(100).createInterface("if_595"));

		ILink l_595_66 = top.createLink("l_595_66");
		l_595_66.createInterface(routers.get(595).createInterface("if_66"));
		l_595_66.createInterface(routers.get(66).createInterface("if_595"));

		ILink l_145_66 = top.createLink("l_145_66");
		l_145_66.createInterface(routers.get(145).createInterface("if_66"));
		l_145_66.createInterface(routers.get(66).createInterface("if_145"));

		ILink l_160_66 = top.createLink("l_160_66");
		l_160_66.createInterface(routers.get(160).createInterface("if_66"));
		l_160_66.createInterface(routers.get(66).createInterface("if_160"));

		ILink l_245_66 = top.createLink("l_245_66");
		l_245_66.createInterface(routers.get(245).createInterface("if_66"));
		l_245_66.createInterface(routers.get(66).createInterface("if_245"));

		ILink l_422_66 = top.createLink("l_422_66");
		l_422_66.createInterface(routers.get(422).createInterface("if_66"));
		l_422_66.createInterface(routers.get(66).createInterface("if_422"));

		ILink l_610_66 = top.createLink("l_610_66");
		l_610_66.createInterface(routers.get(610).createInterface("if_66"));
		l_610_66.createInterface(routers.get(66).createInterface("if_610"));

		ILink l_611_66 = top.createLink("l_611_66");
		l_611_66.createInterface(routers.get(611).createInterface("if_66"));
		l_611_66.createInterface(routers.get(66).createInterface("if_611"));

		ILink l_624_66 = top.createLink("l_624_66");
		l_624_66.createInterface(routers.get(624).createInterface("if_66"));
		l_624_66.createInterface(routers.get(66).createInterface("if_624"));

		ILink l_84_83 = top.createLink("l_84_83");
		l_84_83.createInterface(routers.get(84).createInterface("if_83"));
		l_84_83.createInterface(routers.get(83).createInterface("if_84"));

		ILink l_106_100 = top.createLink("l_106_100");
		l_106_100.createInterface(routers.get(106).createInterface("if_100"));
		l_106_100.createInterface(routers.get(100).createInterface("if_106"));

		ILink l_283_106 = top.createLink("l_283_106");
		l_283_106.createInterface(routers.get(283).createInterface("if_106"));
		l_283_106.createInterface(routers.get(106).createInterface("if_283"));

		ILink l_290_106 = top.createLink("l_290_106");
		l_290_106.createInterface(routers.get(290).createInterface("if_106"));
		l_290_106.createInterface(routers.get(106).createInterface("if_290"));

		ILink l_334_106 = top.createLink("l_334_106");
		l_334_106.createInterface(routers.get(334).createInterface("if_106"));
		l_334_106.createInterface(routers.get(106).createInterface("if_334"));

		ILink l_610_106 = top.createLink("l_610_106");
		l_610_106.createInterface(routers.get(610).createInterface("if_106"));
		l_610_106.createInterface(routers.get(106).createInterface("if_610"));

		ILink l_107_100 = top.createLink("l_107_100");
		l_107_100.createInterface(routers.get(107).createInterface("if_100"));
		l_107_100.createInterface(routers.get(100).createInterface("if_107"));

		ILink l_283_107 = top.createLink("l_283_107");
		l_283_107.createInterface(routers.get(283).createInterface("if_107"));
		l_283_107.createInterface(routers.get(107).createInterface("if_283"));

		ILink l_334_107 = top.createLink("l_334_107");
		l_334_107.createInterface(routers.get(334).createInterface("if_107"));
		l_334_107.createInterface(routers.get(107).createInterface("if_334"));

		ILink l_224_100 = top.createLink("l_224_100");
		l_224_100.createInterface(routers.get(224).createInterface("if_100"));
		l_224_100.createInterface(routers.get(100).createInterface("if_224"));

		ILink l_225_100 = top.createLink("l_225_100");
		l_225_100.createInterface(routers.get(225).createInterface("if_100"));
		l_225_100.createInterface(routers.get(100).createInterface("if_225"));

		ILink l_283_225 = top.createLink("l_283_225");
		l_283_225.createInterface(routers.get(283).createInterface("if_225"));
		l_283_225.createInterface(routers.get(225).createInterface("if_283"));

		ILink l_334_225 = top.createLink("l_334_225");
		l_334_225.createInterface(routers.get(334).createInterface("if_225"));
		l_334_225.createInterface(routers.get(225).createInterface("if_334"));

		ILink l_335_225 = top.createLink("l_335_225");
		l_335_225.createInterface(routers.get(335).createInterface("if_225"));
		l_335_225.createInterface(routers.get(225).createInterface("if_335"));

		ILink l_281_60 = top.createLink("l_281_60");
		l_281_60.createInterface(routers.get(281).createInterface("if_60"));
		l_281_60.createInterface(routers.get(60).createInterface("if_281"));

		ILink l_293_60 = top.createLink("l_293_60");
		l_293_60.createInterface(routers.get(293).createInterface("if_60"));
		l_293_60.createInterface(routers.get(60).createInterface("if_293"));

		ILink l_294_60 = top.createLink("l_294_60");
		l_294_60.createInterface(routers.get(294).createInterface("if_60"));
		l_294_60.createInterface(routers.get(60).createInterface("if_294"));

		ILink l_332_331 = top.createLink("l_332_331");
		l_332_331.createInterface(routers.get(332).createInterface("if_331"));
		l_332_331.createInterface(routers.get(331).createInterface("if_332"));

		ILink l_334_331 = top.createLink("l_334_331");
		l_334_331.createInterface(routers.get(334).createInterface("if_331"));
		l_334_331.createInterface(routers.get(331).createInterface("if_334"));

		ILink l_335_331 = top.createLink("l_335_331");
		l_335_331.createInterface(routers.get(335).createInterface("if_331"));
		l_335_331.createInterface(routers.get(331).createInterface("if_335"));

		ILink l_435_433 = top.createLink("l_435_433");
		l_435_433.createInterface(routers.get(435).createInterface("if_433"));
		l_435_433.createInterface(routers.get(433).createInterface("if_435"));

		ILink l_435_434 = top.createLink("l_435_434");
		l_435_434.createInterface(routers.get(435).createInterface("if_434"));
		l_435_434.createInterface(routers.get(434).createInterface("if_435"));

		ILink l_435_283 = top.createLink("l_435_283");
		l_435_283.createInterface(routers.get(435).createInterface("if_283"));
		l_435_283.createInterface(routers.get(283).createInterface("if_435"));

		ILink l_559_433 = top.createLink("l_559_433");
		l_559_433.createInterface(routers.get(559).createInterface("if_433"));
		l_559_433.createInterface(routers.get(433).createInterface("if_559"));

		ILink l_559_277 = top.createLink("l_559_277");
		l_559_277.createInterface(routers.get(559).createInterface("if_277"));
		l_559_277.createInterface(routers.get(277).createInterface("if_559"));

		ILink l_623_277 = top.createLink("l_623_277");
		l_623_277.createInterface(routers.get(623).createInterface("if_277"));
		l_623_277.createInterface(routers.get(277).createInterface("if_623"));

		ILink l_623_434 = top.createLink("l_623_434");
		l_623_434.createInterface(routers.get(623).createInterface("if_434"));
		l_623_434.createInterface(routers.get(434).createInterface("if_623"));

		ILink l_100_58 = top.createLink("l_100_58");
		l_100_58.createInterface(routers.get(100).createInterface("if_58"));
		l_100_58.createInterface(routers.get(58).createInterface("if_100"));

		ILink l_100_59 = top.createLink("l_100_59");
		l_100_59.createInterface(routers.get(100).createInterface("if_59"));
		l_100_59.createInterface(routers.get(59).createInterface("if_100"));

		ILink l_282_60 = top.createLink("l_282_60");
		l_282_60.createInterface(routers.get(282).createInterface("if_60"));
		l_282_60.createInterface(routers.get(60).createInterface("if_282"));

		ILink l_290_60 = top.createLink("l_290_60");
		l_290_60.createInterface(routers.get(290).createInterface("if_60"));
		l_290_60.createInterface(routers.get(60).createInterface("if_290"));

		ILink l_291_60 = top.createLink("l_291_60");
		l_291_60.createInterface(routers.get(291).createInterface("if_60"));
		l_291_60.createInterface(routers.get(60).createInterface("if_291"));

		ILink l_292_60 = top.createLink("l_292_60");
		l_292_60.createInterface(routers.get(292).createInterface("if_60"));
		l_292_60.createInterface(routers.get(60).createInterface("if_292"));

		ILink l_100_61 = top.createLink("l_100_61");
		l_100_61.createInterface(routers.get(100).createInterface("if_61"));
		l_100_61.createInterface(routers.get(61).createInterface("if_100"));

		ILink l_433_100 = top.createLink("l_433_100");
		l_433_100.createInterface(routers.get(433).createInterface("if_100"));
		l_433_100.createInterface(routers.get(100).createInterface("if_433"));

		ILink l_434_100 = top.createLink("l_434_100");
		l_434_100.createInterface(routers.get(434).createInterface("if_100"));
		l_434_100.createInterface(routers.get(100).createInterface("if_434"));

		ILink l_421_100 = top.createLink("l_421_100");
		l_421_100.createInterface(routers.get(421).createInterface("if_100"));
		l_421_100.createInterface(routers.get(100).createInterface("if_421"));

		ILink l_421_334 = top.createLink("l_421_334");
		l_421_334.createInterface(routers.get(421).createInterface("if_334"));
		l_421_334.createInterface(routers.get(334).createInterface("if_421"));

		ILink l_421_292 = top.createLink("l_421_292");
		l_421_292.createInterface(routers.get(421).createInterface("if_292"));
		l_421_292.createInterface(routers.get(292).createInterface("if_421"));

		ILink l_421_282 = top.createLink("l_421_282");
		l_421_282.createInterface(routers.get(421).createInterface("if_282"));
		l_421_282.createInterface(routers.get(282).createInterface("if_421"));

		ILink l_422_245 = top.createLink("l_422_245");
		l_422_245.createInterface(routers.get(422).createInterface("if_245"));
		l_422_245.createInterface(routers.get(245).createInterface("if_422"));

		ILink l_431_290 = top.createLink("l_431_290");
		l_431_290.createInterface(routers.get(431).createInterface("if_290"));
		l_431_290.createInterface(routers.get(290).createInterface("if_431"));

		ILink l_431_291 = top.createLink("l_431_291");
		l_431_291.createInterface(routers.get(431).createInterface("if_291"));
		l_431_291.createInterface(routers.get(291).createInterface("if_431"));

		ILink l_431_292 = top.createLink("l_431_292");
		l_431_292.createInterface(routers.get(431).createInterface("if_292"));
		l_431_292.createInterface(routers.get(292).createInterface("if_431"));

		ILink l_431_282 = top.createLink("l_431_282");
		l_431_282.createInterface(routers.get(431).createInterface("if_282"));
		l_431_282.createInterface(routers.get(282).createInterface("if_431"));

		ILink l_521_272 = top.createLink("l_521_272");
		l_521_272.createInterface(routers.get(521).createInterface("if_272"));
		l_521_272.createInterface(routers.get(272).createInterface("if_521"));

		ILink l_522_429 = top.createLink("l_522_429");
		l_522_429.createInterface(routers.get(522).createInterface("if_429"));
		l_522_429.createInterface(routers.get(429).createInterface("if_522"));

		/*
		ITraffic traffic = top.createTraffic();
		ArrayList<IHost> srcs = new ArrayList<IHost>();
		ArrayList<IHost> dsts = new ArrayList<IHost>();
		ArrayList<IModelNode> nodes = new ArrayList<IModelNode>();
		nodes.addAll(dst_nets);
		int c = 500;
		while(nodes.size()>0 && dsts.size()<c) {
			IModelNode n = nodes.remove(0);
			if(n instanceof IHost && !(n instanceof IRouter)){
				dsts.add((IHost)n);
			}
			else if(n instanceof INet) {
				nodes.addAll(n.getAllChildren());
			}
		}
		nodes.clear();
		nodes.addAll(src_nets);
		while(nodes.size()>0 && srcs.size()<c) {
			IModelNode n = nodes.remove(0);
			if(n instanceof IHost && !(n instanceof IRouter)){
				srcs.add((IHost)n);
			}
			else if(n instanceof INet) {
				nodes.addAll(n.getAllChildren());
			}
		}
		Collections.shuffle(srcs);
		Collections.shuffle(dsts);
		while(c>0&&srcs.size()>0 && dsts.size()>0) {
			final IHost srch=srcs.remove(0);
			final IHost dsth=dsts.remove(0);
			final String srcstr=".:"+srch.getUniqueName().toString().substring(top.getUniqueName().toString().length()+1);
			final String dststr=".:"+dsth.getUniqueName().toString().substring(top.getUniqueName().toString().length()+1);
			IHTTPTraffic http = traffic.createHTTPTraffic();
			http.setStartTime("0.1");
			http.setIntervalExponential("false");
			http.setFileSize("1000000");
			http.setConnectionsPerSession(1);
			http.setNumberOfSessions(1);
			http.setSrcs(srcstr);
			http.setDsts(dststr);
			c--;
		}*/
		return top;
	}

	//creating functions to create the the top 16 networks
	public INet create_net_0(INet top, Map<Integer,IRouter> routers) {
		INet rv = top.createNet("net_0");
		if(do_spherical) rv.createShortestPath();;

		//create routers
		IRouter r_70 = rv.createRouter("r_70");
		routers.put(70,r_70);

		IRouter r_87 = rv.createRouter("r_87");
		routers.put(87,r_87);

		IRouter r_88 = rv.createRouter("r_88");
		routers.put(88,r_88);

		IRouter r_89 = rv.createRouter("r_89");
		routers.put(89,r_89);

		IRouter r_90 = rv.createRouter("r_90");
		routers.put(90,r_90);

		IRouter r_123 = rv.createRouter("r_123");
		routers.put(123,r_123);

		IRouter r_124 = rv.createRouter("r_124");
		routers.put(124,r_124);

		IRouter r_186 = rv.createRouter("r_186");
		routers.put(186,r_186);

		IRouter r_219 = rv.createRouter("r_219");
		routers.put(219,r_219);

		IRouter r_315 = rv.createRouter("r_315");
		routers.put(315,r_315);

		IRouter r_316 = rv.createRouter("r_316");
		routers.put(316,r_316);

		IRouter r_337 = rv.createRouter("r_337");
		routers.put(337,r_337);

		IRouter r_345 = rv.createRouter("r_345");
		routers.put(345,r_345);

		IRouter r_375 = rv.createRouter("r_375");
		routers.put(375,r_375);

		IRouter r_386 = rv.createRouter("r_386");
		routers.put(386,r_386);

		IRouter r_387 = rv.createRouter("r_387");
		routers.put(387,r_387);

		IRouter r_388 = rv.createRouter("r_388");
		routers.put(388,r_388);

		IRouter r_391 = rv.createRouter("r_391");
		routers.put(391,r_391);

		IRouter r_490 = rv.createRouter("r_490");
		routers.put(490,r_490);

		IRouter r_491 = rv.createRouter("r_491");
		routers.put(491,r_491);

		IRouter r_492 = rv.createRouter("r_492");
		routers.put(492,r_492);

		IRouter r_494 = rv.createRouter("r_494");
		routers.put(494,r_494);

		IRouter r_495 = rv.createRouter("r_495");
		routers.put(495,r_495);

		IRouter r_519 = rv.createRouter("r_519");
		routers.put(519,r_519);

		IRouter r_523 = rv.createRouter("r_523");
		routers.put(523,r_523);

		IRouter r_524 = rv.createRouter("r_524");
		routers.put(524,r_524);

		IRouter r_525 = rv.createRouter("r_525");
		routers.put(525,r_525);

		IRouter r_526 = rv.createRouter("r_526");
		routers.put(526,r_526);

		IRouter r_527 = rv.createRouter("r_527");
		routers.put(527,r_527);

		IRouter r_528 = rv.createRouter("r_528");
		routers.put(528,r_528);

		IRouter r_530 = rv.createRouter("r_530");
		routers.put(530,r_530);

		IRouter r_533 = rv.createRouter("r_533");
		routers.put(533,r_533);

		IRouter r_534 = rv.createRouter("r_534");
		routers.put(534,r_534);

		IRouter r_535 = rv.createRouter("r_535");
		routers.put(535,r_535);

		IRouter r_536 = rv.createRouter("r_536");
		routers.put(536,r_536);

		IRouter r_537 = rv.createRouter("r_537");
		routers.put(537,r_537);

		IRouter r_538 = rv.createRouter("r_538");
		routers.put(538,r_538);

		IRouter r_539 = rv.createRouter("r_539");
		routers.put(539,r_539);

		IRouter r_540 = rv.createRouter("r_540");
		routers.put(540,r_540);

		IRouter r_541 = rv.createRouter("r_541");
		routers.put(541,r_541);

		//create links
		ILink l_89_70 = rv.createLink("l_89_70");
		l_89_70.createInterface(routers.get(89).createInterface("if_70"));
		l_89_70.createInterface(routers.get(70).createInterface("if_89"));

		ILink l_123_70 = rv.createLink("l_123_70");
		l_123_70.createInterface(routers.get(123).createInterface("if_70"));
		l_123_70.createInterface(routers.get(70).createInterface("if_123"));

		ILink l_315_70 = rv.createLink("l_315_70");
		l_315_70.createInterface(routers.get(315).createInterface("if_70"));
		l_315_70.createInterface(routers.get(70).createInterface("if_315"));

		ILink l_316_70 = rv.createLink("l_316_70");
		l_316_70.createInterface(routers.get(316).createInterface("if_70"));
		l_316_70.createInterface(routers.get(70).createInterface("if_316"));

		ILink l_345_70 = rv.createLink("l_345_70");
		l_345_70.createInterface(routers.get(345).createInterface("if_70"));
		l_345_70.createInterface(routers.get(70).createInterface("if_345"));

		ILink l_375_70 = rv.createLink("l_375_70");
		l_375_70.createInterface(routers.get(375).createInterface("if_70"));
		l_375_70.createInterface(routers.get(70).createInterface("if_375"));

		ILink l_386_70 = rv.createLink("l_386_70");
		l_386_70.createInterface(routers.get(386).createInterface("if_70"));
		l_386_70.createInterface(routers.get(70).createInterface("if_386"));

		ILink l_387_70 = rv.createLink("l_387_70");
		l_387_70.createInterface(routers.get(387).createInterface("if_70"));
		l_387_70.createInterface(routers.get(70).createInterface("if_387"));

		ILink l_391_70 = rv.createLink("l_391_70");
		l_391_70.createInterface(routers.get(391).createInterface("if_70"));
		l_391_70.createInterface(routers.get(70).createInterface("if_391"));

		ILink l_494_70 = rv.createLink("l_494_70");
		l_494_70.createInterface(routers.get(494).createInterface("if_70"));
		l_494_70.createInterface(routers.get(70).createInterface("if_494"));

		ILink l_495_70 = rv.createLink("l_495_70");
		l_495_70.createInterface(routers.get(495).createInterface("if_70"));
		l_495_70.createInterface(routers.get(70).createInterface("if_495"));

		ILink l_519_70 = rv.createLink("l_519_70");
		l_519_70.createInterface(routers.get(519).createInterface("if_70"));
		l_519_70.createInterface(routers.get(70).createInterface("if_519"));

		ILink l_523_70 = rv.createLink("l_523_70");
		l_523_70.createInterface(routers.get(523).createInterface("if_70"));
		l_523_70.createInterface(routers.get(70).createInterface("if_523"));

		ILink l_530_70 = rv.createLink("l_530_70");
		l_530_70.createInterface(routers.get(530).createInterface("if_70"));
		l_530_70.createInterface(routers.get(70).createInterface("if_530"));

		ILink l_533_70 = rv.createLink("l_533_70");
		l_533_70.createInterface(routers.get(533).createInterface("if_70"));
		l_533_70.createInterface(routers.get(70).createInterface("if_533"));

		ILink l_534_70 = rv.createLink("l_534_70");
		l_534_70.createInterface(routers.get(534).createInterface("if_70"));
		l_534_70.createInterface(routers.get(70).createInterface("if_534"));

		ILink l_535_70 = rv.createLink("l_535_70");
		l_535_70.createInterface(routers.get(535).createInterface("if_70"));
		l_535_70.createInterface(routers.get(70).createInterface("if_535"));

		ILink l_536_70 = rv.createLink("l_536_70");
		l_536_70.createInterface(routers.get(536).createInterface("if_70"));
		l_536_70.createInterface(routers.get(70).createInterface("if_536"));

		ILink l_88_87 = rv.createLink("l_88_87");
		l_88_87.createInterface(routers.get(88).createInterface("if_87"));
		l_88_87.createInterface(routers.get(87).createInterface("if_88"));

		ILink l_89_87 = rv.createLink("l_89_87");
		l_89_87.createInterface(routers.get(89).createInterface("if_87"));
		l_89_87.createInterface(routers.get(87).createInterface("if_89"));

		ILink l_90_87 = rv.createLink("l_90_87");
		l_90_87.createInterface(routers.get(90).createInterface("if_87"));
		l_90_87.createInterface(routers.get(87).createInterface("if_90"));

		ILink l_90_88 = rv.createLink("l_90_88");
		l_90_88.createInterface(routers.get(90).createInterface("if_88"));
		l_90_88.createInterface(routers.get(88).createInterface("if_90"));

		ILink l_535_88 = rv.createLink("l_535_88");
		l_535_88.createInterface(routers.get(535).createInterface("if_88"));
		l_535_88.createInterface(routers.get(88).createInterface("if_535"));

		ILink l_536_88 = rv.createLink("l_536_88");
		l_536_88.createInterface(routers.get(536).createInterface("if_88"));
		l_536_88.createInterface(routers.get(88).createInterface("if_536"));

		ILink l_90_89 = rv.createLink("l_90_89");
		l_90_89.createInterface(routers.get(90).createInterface("if_89"));
		l_90_89.createInterface(routers.get(89).createInterface("if_90"));

		ILink l_186_89 = rv.createLink("l_186_89");
		l_186_89.createInterface(routers.get(186).createInterface("if_89"));
		l_186_89.createInterface(routers.get(89).createInterface("if_186"));

		ILink l_219_89 = rv.createLink("l_219_89");
		l_219_89.createInterface(routers.get(219).createInterface("if_89"));
		l_219_89.createInterface(routers.get(89).createInterface("if_219"));

		ILink l_337_89 = rv.createLink("l_337_89");
		l_337_89.createInterface(routers.get(337).createInterface("if_89"));
		l_337_89.createInterface(routers.get(89).createInterface("if_337"));

		ILink l_535_89 = rv.createLink("l_535_89");
		l_535_89.createInterface(routers.get(535).createInterface("if_89"));
		l_535_89.createInterface(routers.get(89).createInterface("if_535"));

		ILink l_536_89 = rv.createLink("l_536_89");
		l_536_89.createInterface(routers.get(536).createInterface("if_89"));
		l_536_89.createInterface(routers.get(89).createInterface("if_536"));

		ILink l_219_90 = rv.createLink("l_219_90");
		l_219_90.createInterface(routers.get(219).createInterface("if_90"));
		l_219_90.createInterface(routers.get(90).createInterface("if_219"));

		ILink l_337_90 = rv.createLink("l_337_90");
		l_337_90.createInterface(routers.get(337).createInterface("if_90"));
		l_337_90.createInterface(routers.get(90).createInterface("if_337"));

		ILink l_541_90 = rv.createLink("l_541_90");
		l_541_90.createInterface(routers.get(541).createInterface("if_90"));
		l_541_90.createInterface(routers.get(90).createInterface("if_541"));

		ILink l_124_123 = rv.createLink("l_124_123");
		l_124_123.createInterface(routers.get(124).createInterface("if_123"));
		l_124_123.createInterface(routers.get(123).createInterface("if_124"));

		ILink l_316_315 = rv.createLink("l_316_315");
		l_316_315.createInterface(routers.get(316).createInterface("if_315"));
		l_316_315.createInterface(routers.get(315).createInterface("if_316"));

		ILink l_345_316 = rv.createLink("l_345_316");
		l_345_316.createInterface(routers.get(345).createInterface("if_316"));
		l_345_316.createInterface(routers.get(316).createInterface("if_345"));

		ILink l_391_316 = rv.createLink("l_391_316");
		l_391_316.createInterface(routers.get(391).createInterface("if_316"));
		l_391_316.createInterface(routers.get(316).createInterface("if_391"));

		ILink l_519_316 = rv.createLink("l_519_316");
		l_519_316.createInterface(routers.get(519).createInterface("if_316"));
		l_519_316.createInterface(routers.get(316).createInterface("if_519"));

		ILink l_530_316 = rv.createLink("l_530_316");
		l_530_316.createInterface(routers.get(530).createInterface("if_316"));
		l_530_316.createInterface(routers.get(316).createInterface("if_530"));

		ILink l_534_316 = rv.createLink("l_534_316");
		l_534_316.createInterface(routers.get(534).createInterface("if_316"));
		l_534_316.createInterface(routers.get(316).createInterface("if_534"));

		ILink l_387_386 = rv.createLink("l_387_386");
		l_387_386.createInterface(routers.get(387).createInterface("if_386"));
		l_387_386.createInterface(routers.get(386).createInterface("if_387"));

		ILink l_388_386 = rv.createLink("l_388_386");
		l_388_386.createInterface(routers.get(388).createInterface("if_386"));
		l_388_386.createInterface(routers.get(386).createInterface("if_388"));

		ILink l_391_387 = rv.createLink("l_391_387");
		l_391_387.createInterface(routers.get(391).createInterface("if_387"));
		l_391_387.createInterface(routers.get(387).createInterface("if_391"));

		ILink l_491_387 = rv.createLink("l_491_387");
		l_491_387.createInterface(routers.get(491).createInterface("if_387"));
		l_491_387.createInterface(routers.get(387).createInterface("if_491"));

		ILink l_524_387 = rv.createLink("l_524_387");
		l_524_387.createInterface(routers.get(524).createInterface("if_387"));
		l_524_387.createInterface(routers.get(387).createInterface("if_524"));

		ILink l_525_387 = rv.createLink("l_525_387");
		l_525_387.createInterface(routers.get(525).createInterface("if_387"));
		l_525_387.createInterface(routers.get(387).createInterface("if_525"));

		ILink l_526_387 = rv.createLink("l_526_387");
		l_526_387.createInterface(routers.get(526).createInterface("if_387"));
		l_526_387.createInterface(routers.get(387).createInterface("if_526"));

		ILink l_527_387 = rv.createLink("l_527_387");
		l_527_387.createInterface(routers.get(527).createInterface("if_387"));
		l_527_387.createInterface(routers.get(387).createInterface("if_527"));

		ILink l_528_387 = rv.createLink("l_528_387");
		l_528_387.createInterface(routers.get(528).createInterface("if_387"));
		l_528_387.createInterface(routers.get(387).createInterface("if_528"));

		ILink l_391_388 = rv.createLink("l_391_388");
		l_391_388.createInterface(routers.get(391).createInterface("if_388"));
		l_391_388.createInterface(routers.get(388).createInterface("if_391"));

		ILink l_492_391 = rv.createLink("l_492_391");
		l_492_391.createInterface(routers.get(492).createInterface("if_391"));
		l_492_391.createInterface(routers.get(391).createInterface("if_492"));

		ILink l_494_391 = rv.createLink("l_494_391");
		l_494_391.createInterface(routers.get(494).createInterface("if_391"));
		l_494_391.createInterface(routers.get(391).createInterface("if_494"));

		ILink l_495_391 = rv.createLink("l_495_391");
		l_495_391.createInterface(routers.get(495).createInterface("if_391"));
		l_495_391.createInterface(routers.get(391).createInterface("if_495"));

		ILink l_523_391 = rv.createLink("l_523_391");
		l_523_391.createInterface(routers.get(523).createInterface("if_391"));
		l_523_391.createInterface(routers.get(391).createInterface("if_523"));

		ILink l_530_391 = rv.createLink("l_530_391");
		l_530_391.createInterface(routers.get(530).createInterface("if_391"));
		l_530_391.createInterface(routers.get(391).createInterface("if_530"));

		ILink l_491_490 = rv.createLink("l_491_490");
		l_491_490.createInterface(routers.get(491).createInterface("if_490"));
		l_491_490.createInterface(routers.get(490).createInterface("if_491"));

		ILink l_492_490 = rv.createLink("l_492_490");
		l_492_490.createInterface(routers.get(492).createInterface("if_490"));
		l_492_490.createInterface(routers.get(490).createInterface("if_492"));

		ILink l_492_491 = rv.createLink("l_492_491");
		l_492_491.createInterface(routers.get(492).createInterface("if_491"));
		l_492_491.createInterface(routers.get(491).createInterface("if_492"));

		ILink l_494_491 = rv.createLink("l_494_491");
		l_494_491.createInterface(routers.get(494).createInterface("if_491"));
		l_494_491.createInterface(routers.get(491).createInterface("if_494"));

		ILink l_495_491 = rv.createLink("l_495_491");
		l_495_491.createInterface(routers.get(495).createInterface("if_491"));
		l_495_491.createInterface(routers.get(491).createInterface("if_495"));

		ILink l_494_492 = rv.createLink("l_494_492");
		l_494_492.createInterface(routers.get(494).createInterface("if_492"));
		l_494_492.createInterface(routers.get(492).createInterface("if_494"));

		ILink l_524_492 = rv.createLink("l_524_492");
		l_524_492.createInterface(routers.get(524).createInterface("if_492"));
		l_524_492.createInterface(routers.get(492).createInterface("if_524"));

		ILink l_525_492 = rv.createLink("l_525_492");
		l_525_492.createInterface(routers.get(525).createInterface("if_492"));
		l_525_492.createInterface(routers.get(492).createInterface("if_525"));

		ILink l_526_492 = rv.createLink("l_526_492");
		l_526_492.createInterface(routers.get(526).createInterface("if_492"));
		l_526_492.createInterface(routers.get(492).createInterface("if_526"));

		ILink l_527_492 = rv.createLink("l_527_492");
		l_527_492.createInterface(routers.get(527).createInterface("if_492"));
		l_527_492.createInterface(routers.get(492).createInterface("if_527"));

		ILink l_528_492 = rv.createLink("l_528_492");
		l_528_492.createInterface(routers.get(528).createInterface("if_492"));
		l_528_492.createInterface(routers.get(492).createInterface("if_528"));

		ILink l_524_494 = rv.createLink("l_524_494");
		l_524_494.createInterface(routers.get(524).createInterface("if_494"));
		l_524_494.createInterface(routers.get(494).createInterface("if_524"));

		ILink l_525_494 = rv.createLink("l_525_494");
		l_525_494.createInterface(routers.get(525).createInterface("if_494"));
		l_525_494.createInterface(routers.get(494).createInterface("if_525"));

		ILink l_526_494 = rv.createLink("l_526_494");
		l_526_494.createInterface(routers.get(526).createInterface("if_494"));
		l_526_494.createInterface(routers.get(494).createInterface("if_526"));

		ILink l_527_494 = rv.createLink("l_527_494");
		l_527_494.createInterface(routers.get(527).createInterface("if_494"));
		l_527_494.createInterface(routers.get(494).createInterface("if_527"));

		ILink l_528_494 = rv.createLink("l_528_494");
		l_528_494.createInterface(routers.get(528).createInterface("if_494"));
		l_528_494.createInterface(routers.get(494).createInterface("if_528"));

		ILink l_527_495 = rv.createLink("l_527_495");
		l_527_495.createInterface(routers.get(527).createInterface("if_495"));
		l_527_495.createInterface(routers.get(495).createInterface("if_527"));

		ILink l_528_495 = rv.createLink("l_528_495");
		l_528_495.createInterface(routers.get(528).createInterface("if_495"));
		l_528_495.createInterface(routers.get(495).createInterface("if_528"));

		ILink l_524_523 = rv.createLink("l_524_523");
		l_524_523.createInterface(routers.get(524).createInterface("if_523"));
		l_524_523.createInterface(routers.get(523).createInterface("if_524"));

		ILink l_525_523 = rv.createLink("l_525_523");
		l_525_523.createInterface(routers.get(525).createInterface("if_523"));
		l_525_523.createInterface(routers.get(523).createInterface("if_525"));

		ILink l_537_530 = rv.createLink("l_537_530");
		l_537_530.createInterface(routers.get(537).createInterface("if_530"));
		l_537_530.createInterface(routers.get(530).createInterface("if_537"));

		ILink l_538_530 = rv.createLink("l_538_530");
		l_538_530.createInterface(routers.get(538).createInterface("if_530"));
		l_538_530.createInterface(routers.get(530).createInterface("if_538"));

		ILink l_539_530 = rv.createLink("l_539_530");
		l_539_530.createInterface(routers.get(539).createInterface("if_530"));
		l_539_530.createInterface(routers.get(530).createInterface("if_539"));

		ILink l_540_530 = rv.createLink("l_540_530");
		l_540_530.createInterface(routers.get(540).createInterface("if_530"));
		l_540_530.createInterface(routers.get(530).createInterface("if_540"));

		ILink l_537_534 = rv.createLink("l_537_534");
		l_537_534.createInterface(routers.get(537).createInterface("if_534"));
		l_537_534.createInterface(routers.get(534).createInterface("if_537"));

		ILink l_538_534 = rv.createLink("l_538_534");
		l_538_534.createInterface(routers.get(538).createInterface("if_534"));
		l_538_534.createInterface(routers.get(534).createInterface("if_538"));

		ILink l_539_534 = rv.createLink("l_539_534");
		l_539_534.createInterface(routers.get(539).createInterface("if_534"));
		l_539_534.createInterface(routers.get(534).createInterface("if_539"));

		ILink l_541_536 = rv.createLink("l_541_536");
		l_541_536.createInterface(routers.get(541).createInterface("if_536"));
		l_541_536.createInterface(routers.get(536).createInterface("if_541"));

		//attach campus networks

		INet campus_124 = null;
		IRouter campus_r_124 = null;
		if(null == baseCampus) {
			campus_124 = rv.createNet("campus_0_124");
			campus_r_124 = createCampus(campus_124,do_spherical);
			baseCampus = campus_124;
		}else {
			campus_124 = rv.createNetReplica("campus_0_124",baseCampus);
			campus_r_124 = (IRouter)campus_124.getChildByName("sub_campus_router");
		}
		ILink l_campus_124 = rv.createLink("l_campus_124");
		l_campus_124.createInterface(r_124.createInterface("if_campus_124"));
		l_campus_124.createInterface((IInterface)campus_r_124.getChildByName("if_stub"));

		INet campus_186 = null;
		IRouter campus_r_186 = null;
		if(null == baseCampus) {
			campus_186 = rv.createNet("campus_0_186");
			campus_r_186 = createCampus(campus_186,do_spherical);
		}else {
			campus_186 = rv.createNetReplica("campus_0_186",baseCampus);
			campus_r_186 = (IRouter)campus_186.getChildByName("sub_campus_router");
			System.out.println(campus_r_186);
		}
		ILink l_campus_186 = rv.createLink("l_campus_186");
		l_campus_186.createInterface(r_186.createInterface("if_campus_186"));
		l_campus_186.createInterface((IInterface)campus_r_186.getChildByName("if_stub"));

		INet campus_375 = null;
		IRouter campus_r_375 = null;
		if(null == baseCampus) {
			campus_375 = rv.createNet("campus_0_375");
			campus_r_375 = createCampus(campus_375,do_spherical);
		}else {
			campus_375 = rv.createNetReplica("campus_0_375",baseCampus);
			campus_r_375 = (IRouter)campus_375.getChildByName("sub_campus_router");
		}
		ILink l_campus_375 = rv.createLink("l_campus_375");
		l_campus_375.createInterface(r_375.createInterface("if_campus_375"));
		l_campus_375.createInterface((IInterface)campus_r_375.getChildByName("if_stub"));

		INet campus_533 = null;
		IRouter campus_r_533 = null;
		if(null == baseCampus) {
			campus_533 = rv.createNet("campus_0_533");
			campus_r_533 = createCampus(campus_533,do_spherical);
		}else {
			campus_533 = rv.createNetReplica("campus_0_533",baseCampus);
			campus_r_533 = (IRouter)campus_533.getChildByName("sub_campus_router");
		}
		ILink l_campus_533 = rv.createLink("l_campus_533");
		l_campus_533.createInterface(r_533.createInterface("if_campus_533"));
		l_campus_533.createInterface((IInterface)campus_r_533.getChildByName("if_stub"));

		INet campus_540 = null;
		IRouter campus_r_540 = null;
		if(null == baseCampus) {
			campus_540 = rv.createNet("campus_0_540");
			campus_r_540 = createCampus(campus_540,do_spherical);
		}else {
			campus_540 = rv.createNetReplica("campus_0_540",baseCampus);
			campus_r_540 = (IRouter)campus_540.getChildByName("sub_campus_router");
		}
		ILink l_campus_540 = rv.createLink("l_campus_540");
		l_campus_540.createInterface(r_540.createInterface("if_campus_540"));
		l_campus_540.createInterface((IInterface)campus_r_540.getChildByName("if_stub"));
		return rv;
	}
	public INet create_net_1(INet top, Map<Integer,IRouter> routers) {
		INet rv = top.createNet("net_1");
		if(do_spherical) rv.createShortestPath();;

		//create routers
		IRouter r_23 = rv.createRouter("r_23");
		routers.put(23,r_23);

		IRouter r_24 = rv.createRouter("r_24");
		routers.put(24,r_24);

		IRouter r_25 = rv.createRouter("r_25");
		routers.put(25,r_25);

		IRouter r_47 = rv.createRouter("r_47");
		routers.put(47,r_47);

		IRouter r_48 = rv.createRouter("r_48");
		routers.put(48,r_48);

		IRouter r_69 = rv.createRouter("r_69");
		routers.put(69,r_69);

		IRouter r_76 = rv.createRouter("r_76");
		routers.put(76,r_76);

		IRouter r_112 = rv.createRouter("r_112");
		routers.put(112,r_112);

		IRouter r_128 = rv.createRouter("r_128");
		routers.put(128,r_128);

		IRouter r_151 = rv.createRouter("r_151");
		routers.put(151,r_151);

		IRouter r_152 = rv.createRouter("r_152");
		routers.put(152,r_152);

		IRouter r_176 = rv.createRouter("r_176");
		routers.put(176,r_176);

		IRouter r_181 = rv.createRouter("r_181");
		routers.put(181,r_181);

		IRouter r_190 = rv.createRouter("r_190");
		routers.put(190,r_190);

		IRouter r_194 = rv.createRouter("r_194");
		routers.put(194,r_194);

		IRouter r_207 = rv.createRouter("r_207");
		routers.put(207,r_207);

		IRouter r_208 = rv.createRouter("r_208");
		routers.put(208,r_208);

		IRouter r_209 = rv.createRouter("r_209");
		routers.put(209,r_209);

		IRouter r_212 = rv.createRouter("r_212");
		routers.put(212,r_212);

		IRouter r_220 = rv.createRouter("r_220");
		routers.put(220,r_220);

		IRouter r_221 = rv.createRouter("r_221");
		routers.put(221,r_221);

		IRouter r_280 = rv.createRouter("r_280");
		routers.put(280,r_280);

		IRouter r_302 = rv.createRouter("r_302");
		routers.put(302,r_302);

		IRouter r_307 = rv.createRouter("r_307");
		routers.put(307,r_307);

		IRouter r_317 = rv.createRouter("r_317");
		routers.put(317,r_317);

		IRouter r_325 = rv.createRouter("r_325");
		routers.put(325,r_325);

		IRouter r_336 = rv.createRouter("r_336");
		routers.put(336,r_336);

		IRouter r_352 = rv.createRouter("r_352");
		routers.put(352,r_352);

		IRouter r_357 = rv.createRouter("r_357");
		routers.put(357,r_357);

		IRouter r_392 = rv.createRouter("r_392");
		routers.put(392,r_392);

		IRouter r_393 = rv.createRouter("r_393");
		routers.put(393,r_393);

		IRouter r_394 = rv.createRouter("r_394");
		routers.put(394,r_394);

		IRouter r_395 = rv.createRouter("r_395");
		routers.put(395,r_395);

		IRouter r_402 = rv.createRouter("r_402");
		routers.put(402,r_402);

		IRouter r_403 = rv.createRouter("r_403");
		routers.put(403,r_403);

		IRouter r_404 = rv.createRouter("r_404");
		routers.put(404,r_404);

		IRouter r_514 = rv.createRouter("r_514");
		routers.put(514,r_514);

		IRouter r_529 = rv.createRouter("r_529");
		routers.put(529,r_529);

		IRouter r_531 = rv.createRouter("r_531");
		routers.put(531,r_531);

		IRouter r_532 = rv.createRouter("r_532");
		routers.put(532,r_532);

		//create links
		ILink l_24_23 = rv.createLink("l_24_23");
		l_24_23.createInterface(routers.get(24).createInterface("if_23"));
		l_24_23.createInterface(routers.get(23).createInterface("if_24"));

		ILink l_25_23 = rv.createLink("l_25_23");
		l_25_23.createInterface(routers.get(25).createInterface("if_23"));
		l_25_23.createInterface(routers.get(23).createInterface("if_25"));

		ILink l_25_24 = rv.createLink("l_25_24");
		l_25_24.createInterface(routers.get(25).createInterface("if_24"));
		l_25_24.createInterface(routers.get(24).createInterface("if_25"));

		ILink l_47_24 = rv.createLink("l_47_24");
		l_47_24.createInterface(routers.get(47).createInterface("if_24"));
		l_47_24.createInterface(routers.get(24).createInterface("if_47"));

		ILink l_69_24 = rv.createLink("l_69_24");
		l_69_24.createInterface(routers.get(69).createInterface("if_24"));
		l_69_24.createInterface(routers.get(24).createInterface("if_69"));

		ILink l_76_24 = rv.createLink("l_76_24");
		l_76_24.createInterface(routers.get(76).createInterface("if_24"));
		l_76_24.createInterface(routers.get(24).createInterface("if_76"));

		ILink l_112_24 = rv.createLink("l_112_24");
		l_112_24.createInterface(routers.get(112).createInterface("if_24"));
		l_112_24.createInterface(routers.get(24).createInterface("if_112"));

		ILink l_128_24 = rv.createLink("l_128_24");
		l_128_24.createInterface(routers.get(128).createInterface("if_24"));
		l_128_24.createInterface(routers.get(24).createInterface("if_128"));

		ILink l_151_24 = rv.createLink("l_151_24");
		l_151_24.createInterface(routers.get(151).createInterface("if_24"));
		l_151_24.createInterface(routers.get(24).createInterface("if_151"));

		ILink l_152_24 = rv.createLink("l_152_24");
		l_152_24.createInterface(routers.get(152).createInterface("if_24"));
		l_152_24.createInterface(routers.get(24).createInterface("if_152"));

		ILink l_176_24 = rv.createLink("l_176_24");
		l_176_24.createInterface(routers.get(176).createInterface("if_24"));
		l_176_24.createInterface(routers.get(24).createInterface("if_176"));

		ILink l_181_24 = rv.createLink("l_181_24");
		l_181_24.createInterface(routers.get(181).createInterface("if_24"));
		l_181_24.createInterface(routers.get(24).createInterface("if_181"));

		ILink l_190_24 = rv.createLink("l_190_24");
		l_190_24.createInterface(routers.get(190).createInterface("if_24"));
		l_190_24.createInterface(routers.get(24).createInterface("if_190"));

		ILink l_194_24 = rv.createLink("l_194_24");
		l_194_24.createInterface(routers.get(194).createInterface("if_24"));
		l_194_24.createInterface(routers.get(24).createInterface("if_194"));

		ILink l_207_24 = rv.createLink("l_207_24");
		l_207_24.createInterface(routers.get(207).createInterface("if_24"));
		l_207_24.createInterface(routers.get(24).createInterface("if_207"));

		ILink l_212_24 = rv.createLink("l_212_24");
		l_212_24.createInterface(routers.get(212).createInterface("if_24"));
		l_212_24.createInterface(routers.get(24).createInterface("if_212"));

		ILink l_280_24 = rv.createLink("l_280_24");
		l_280_24.createInterface(routers.get(280).createInterface("if_24"));
		l_280_24.createInterface(routers.get(24).createInterface("if_280"));

		ILink l_302_24 = rv.createLink("l_302_24");
		l_302_24.createInterface(routers.get(302).createInterface("if_24"));
		l_302_24.createInterface(routers.get(24).createInterface("if_302"));

		ILink l_307_24 = rv.createLink("l_307_24");
		l_307_24.createInterface(routers.get(307).createInterface("if_24"));
		l_307_24.createInterface(routers.get(24).createInterface("if_307"));

		ILink l_325_24 = rv.createLink("l_325_24");
		l_325_24.createInterface(routers.get(325).createInterface("if_24"));
		l_325_24.createInterface(routers.get(24).createInterface("if_325"));

		ILink l_357_24 = rv.createLink("l_357_24");
		l_357_24.createInterface(routers.get(357).createInterface("if_24"));
		l_357_24.createInterface(routers.get(24).createInterface("if_357"));

		ILink l_392_24 = rv.createLink("l_392_24");
		l_392_24.createInterface(routers.get(392).createInterface("if_24"));
		l_392_24.createInterface(routers.get(24).createInterface("if_392"));

		ILink l_393_24 = rv.createLink("l_393_24");
		l_393_24.createInterface(routers.get(393).createInterface("if_24"));
		l_393_24.createInterface(routers.get(24).createInterface("if_393"));

		ILink l_394_24 = rv.createLink("l_394_24");
		l_394_24.createInterface(routers.get(394).createInterface("if_24"));
		l_394_24.createInterface(routers.get(24).createInterface("if_394"));

		ILink l_395_24 = rv.createLink("l_395_24");
		l_395_24.createInterface(routers.get(395).createInterface("if_24"));
		l_395_24.createInterface(routers.get(24).createInterface("if_395"));

		ILink l_69_25 = rv.createLink("l_69_25");
		l_69_25.createInterface(routers.get(69).createInterface("if_25"));
		l_69_25.createInterface(routers.get(25).createInterface("if_69"));

		ILink l_176_25 = rv.createLink("l_176_25");
		l_176_25.createInterface(routers.get(176).createInterface("if_25"));
		l_176_25.createInterface(routers.get(25).createInterface("if_176"));

		ILink l_194_25 = rv.createLink("l_194_25");
		l_194_25.createInterface(routers.get(194).createInterface("if_25"));
		l_194_25.createInterface(routers.get(25).createInterface("if_194"));

		ILink l_207_25 = rv.createLink("l_207_25");
		l_207_25.createInterface(routers.get(207).createInterface("if_25"));
		l_207_25.createInterface(routers.get(25).createInterface("if_207"));

		ILink l_209_25 = rv.createLink("l_209_25");
		l_209_25.createInterface(routers.get(209).createInterface("if_25"));
		l_209_25.createInterface(routers.get(25).createInterface("if_209"));

		ILink l_212_25 = rv.createLink("l_212_25");
		l_212_25.createInterface(routers.get(212).createInterface("if_25"));
		l_212_25.createInterface(routers.get(25).createInterface("if_212"));

		ILink l_307_25 = rv.createLink("l_307_25");
		l_307_25.createInterface(routers.get(307).createInterface("if_25"));
		l_307_25.createInterface(routers.get(25).createInterface("if_307"));

		ILink l_317_25 = rv.createLink("l_317_25");
		l_317_25.createInterface(routers.get(317).createInterface("if_25"));
		l_317_25.createInterface(routers.get(25).createInterface("if_317"));

		ILink l_392_25 = rv.createLink("l_392_25");
		l_392_25.createInterface(routers.get(392).createInterface("if_25"));
		l_392_25.createInterface(routers.get(25).createInterface("if_392"));

		ILink l_393_25 = rv.createLink("l_393_25");
		l_393_25.createInterface(routers.get(393).createInterface("if_25"));
		l_393_25.createInterface(routers.get(25).createInterface("if_393"));

		ILink l_394_25 = rv.createLink("l_394_25");
		l_394_25.createInterface(routers.get(394).createInterface("if_25"));
		l_394_25.createInterface(routers.get(25).createInterface("if_394"));

		ILink l_395_25 = rv.createLink("l_395_25");
		l_395_25.createInterface(routers.get(395).createInterface("if_25"));
		l_395_25.createInterface(routers.get(25).createInterface("if_395"));

		ILink l_404_25 = rv.createLink("l_404_25");
		l_404_25.createInterface(routers.get(404).createInterface("if_25"));
		l_404_25.createInterface(routers.get(25).createInterface("if_404"));

		ILink l_514_25 = rv.createLink("l_514_25");
		l_514_25.createInterface(routers.get(514).createInterface("if_25"));
		l_514_25.createInterface(routers.get(25).createInterface("if_514"));

		ILink l_531_25 = rv.createLink("l_531_25");
		l_531_25.createInterface(routers.get(531).createInterface("if_25"));
		l_531_25.createInterface(routers.get(25).createInterface("if_531"));

		ILink l_48_47 = rv.createLink("l_48_47");
		l_48_47.createInterface(routers.get(48).createInterface("if_47"));
		l_48_47.createInterface(routers.get(47).createInterface("if_48"));

		ILink l_209_208 = rv.createLink("l_209_208");
		l_209_208.createInterface(routers.get(209).createInterface("if_208"));
		l_209_208.createInterface(routers.get(208).createInterface("if_209"));

		ILink l_220_209 = rv.createLink("l_220_209");
		l_220_209.createInterface(routers.get(220).createInterface("if_209"));
		l_220_209.createInterface(routers.get(209).createInterface("if_220"));

		ILink l_336_209 = rv.createLink("l_336_209");
		l_336_209.createInterface(routers.get(336).createInterface("if_209"));
		l_336_209.createInterface(routers.get(209).createInterface("if_336"));

		ILink l_221_220 = rv.createLink("l_221_220");
		l_221_220.createInterface(routers.get(221).createInterface("if_220"));
		l_221_220.createInterface(routers.get(220).createInterface("if_221"));

		ILink l_336_221 = rv.createLink("l_336_221");
		l_336_221.createInterface(routers.get(336).createInterface("if_221"));
		l_336_221.createInterface(routers.get(221).createInterface("if_336"));

		ILink l_532_393 = rv.createLink("l_532_393");
		l_532_393.createInterface(routers.get(532).createInterface("if_393"));
		l_532_393.createInterface(routers.get(393).createInterface("if_532"));

		ILink l_403_402 = rv.createLink("l_403_402");
		l_403_402.createInterface(routers.get(403).createInterface("if_402"));
		l_403_402.createInterface(routers.get(402).createInterface("if_403"));

		ILink l_404_402 = rv.createLink("l_404_402");
		l_404_402.createInterface(routers.get(404).createInterface("if_402"));
		l_404_402.createInterface(routers.get(402).createInterface("if_404"));

		ILink l_404_403 = rv.createLink("l_404_403");
		l_404_403.createInterface(routers.get(404).createInterface("if_403"));
		l_404_403.createInterface(routers.get(403).createInterface("if_404"));

		ILink l_529_403 = rv.createLink("l_529_403");
		l_529_403.createInterface(routers.get(529).createInterface("if_403"));
		l_529_403.createInterface(routers.get(403).createInterface("if_529"));

		ILink l_531_403 = rv.createLink("l_531_403");
		l_531_403.createInterface(routers.get(531).createInterface("if_403"));
		l_531_403.createInterface(routers.get(403).createInterface("if_531"));

		ILink l_529_404 = rv.createLink("l_529_404");
		l_529_404.createInterface(routers.get(529).createInterface("if_404"));
		l_529_404.createInterface(routers.get(404).createInterface("if_529"));

		ILink l_531_404 = rv.createLink("l_531_404");
		l_531_404.createInterface(routers.get(531).createInterface("if_404"));
		l_531_404.createInterface(routers.get(404).createInterface("if_531"));

		//attach campus networks

		INet campus_48 = null;
		IRouter campus_r_48 = null;
		if(null == baseCampus) {
			campus_48 = rv.createNet("campus_1_48");
			campus_r_48 = createCampus(campus_48,do_spherical);
		}else {
			campus_48 = rv.createNetReplica("campus_1_48",baseCampus);
			campus_r_48 = (IRouter)campus_48.getChildByName("sub_campus_router");
		}
		ILink l_campus_48 = rv.createLink("l_campus_48");
		l_campus_48.createInterface(r_48.createInterface("if_campus_48"));
		l_campus_48.createInterface((IInterface)campus_r_48.getChildByName("if_stub"));

		INet campus_76 = null;
		IRouter campus_r_76 = null;
		if(null == baseCampus) {
			campus_76 = rv.createNet("campus_1_76");
			campus_r_76 = createCampus(campus_76,do_spherical);
		}else {
			campus_76 = rv.createNetReplica("campus_1_76",baseCampus);
			campus_r_76 = (IRouter)campus_76.getChildByName("sub_campus_router");
		}
		ILink l_campus_76 = rv.createLink("l_campus_76");
		l_campus_76.createInterface(r_76.createInterface("if_campus_76"));
		l_campus_76.createInterface((IInterface)campus_r_76.getChildByName("if_stub"));

		INet campus_112 = null;
		IRouter campus_r_112 = null;
		if(null == baseCampus) {
			campus_112 = rv.createNet("campus_1_112");
			campus_r_112 = createCampus(campus_112,do_spherical);
		}else {
			campus_112 = rv.createNetReplica("campus_1_112",baseCampus);
			campus_r_112 = (IRouter)campus_112.getChildByName("sub_campus_router");
		}
		ILink l_campus_112 = rv.createLink("l_campus_112");
		l_campus_112.createInterface(r_112.createInterface("if_campus_112"));
		l_campus_112.createInterface((IInterface)campus_r_112.getChildByName("if_stub"));

		INet campus_128 = null;
		IRouter campus_r_128 = null;
		if(null == baseCampus) {
			campus_128 = rv.createNet("campus_1_128");
			campus_r_128 = createCampus(campus_128,do_spherical);
		}else {
			campus_128 = rv.createNetReplica("campus_1_128",baseCampus);
			campus_r_128 = (IRouter)campus_128.getChildByName("sub_campus_router");
		}
		ILink l_campus_128 = rv.createLink("l_campus_128");
		l_campus_128.createInterface(r_128.createInterface("if_campus_128"));
		l_campus_128.createInterface((IInterface)campus_r_128.getChildByName("if_stub"));

		INet campus_151 = null;
		IRouter campus_r_151 = null;
		if(null == baseCampus) {
			campus_151 = rv.createNet("campus_1_151");
			campus_r_151 = createCampus(campus_151,do_spherical);
		}else {
			campus_151 = rv.createNetReplica("campus_1_151",baseCampus);
			campus_r_151 = (IRouter)campus_151.getChildByName("sub_campus_router");
		}
		ILink l_campus_151 = rv.createLink("l_campus_151");
		l_campus_151.createInterface(r_151.createInterface("if_campus_151"));
		l_campus_151.createInterface((IInterface)campus_r_151.getChildByName("if_stub"));

		INet campus_152 = null;
		IRouter campus_r_152 = null;
		if(null == baseCampus) {
			campus_152 = rv.createNet("campus_1_152");
			campus_r_152 = createCampus(campus_152,do_spherical);
		}else {
			campus_152 = rv.createNetReplica("campus_1_152",baseCampus);
			campus_r_152 = (IRouter)campus_152.getChildByName("sub_campus_router");
		}
		ILink l_campus_152 = rv.createLink("l_campus_152");
		l_campus_152.createInterface(r_152.createInterface("if_campus_152"));
		l_campus_152.createInterface((IInterface)campus_r_152.getChildByName("if_stub"));

		INet campus_181 = null;
		IRouter campus_r_181 = null;
		if(null == baseCampus) {
			campus_181 = rv.createNet("campus_1_181");
			campus_r_181 = createCampus(campus_181,do_spherical);
		}else {
			campus_181 = rv.createNetReplica("campus_1_181",baseCampus);
			campus_r_181 = (IRouter)campus_181.getChildByName("sub_campus_router");
		}
		ILink l_campus_181 = rv.createLink("l_campus_181");
		l_campus_181.createInterface(r_181.createInterface("if_campus_181"));
		l_campus_181.createInterface((IInterface)campus_r_181.getChildByName("if_stub"));

		INet campus_190 = null;
		IRouter campus_r_190 = null;
		if(null == baseCampus) {
			campus_190 = rv.createNet("campus_1_190");
			campus_r_190 = createCampus(campus_190,do_spherical);
		}else {
			campus_190 = rv.createNetReplica("campus_1_190",baseCampus);
			campus_r_190 = (IRouter)campus_190.getChildByName("sub_campus_router");
		}
		ILink l_campus_190 = rv.createLink("l_campus_190");
		l_campus_190.createInterface(r_190.createInterface("if_campus_190"));
		l_campus_190.createInterface((IInterface)campus_r_190.getChildByName("if_stub"));

		INet campus_208 = null;
		IRouter campus_r_208 = null;
		if(null == baseCampus) {
			campus_208 = rv.createNet("campus_1_208");
			campus_r_208 = createCampus(campus_208,do_spherical);
		}else {
			campus_208 = rv.createNetReplica("campus_1_208",baseCampus);
			campus_r_208 = (IRouter)campus_208.getChildByName("sub_campus_router");
		}
		ILink l_campus_208 = rv.createLink("l_campus_208");
		l_campus_208.createInterface(r_208.createInterface("if_campus_208"));
		l_campus_208.createInterface((IInterface)campus_r_208.getChildByName("if_stub"));

		INet campus_302 = null;
		IRouter campus_r_302 = null;
		if(null == baseCampus) {
			campus_302 = rv.createNet("campus_1_302");
			campus_r_302 = createCampus(campus_302,do_spherical);
		}else {
			campus_302 = rv.createNetReplica("campus_1_302",baseCampus);
			campus_r_302 = (IRouter)campus_302.getChildByName("sub_campus_router");
		}
		ILink l_campus_302 = rv.createLink("l_campus_302");
		l_campus_302.createInterface(r_302.createInterface("if_campus_302"));
		l_campus_302.createInterface((IInterface)campus_r_302.getChildByName("if_stub"));

		INet campus_325 = null;
		IRouter campus_r_325 = null;
		if(null == baseCampus) {
			campus_325 = rv.createNet("campus_1_325");
			campus_r_325 = createCampus(campus_325,do_spherical);
		}else {
			campus_325 = rv.createNetReplica("campus_1_325",baseCampus);
			campus_r_325 = (IRouter)campus_325.getChildByName("sub_campus_router");
		}
		ILink l_campus_325 = rv.createLink("l_campus_325");
		l_campus_325.createInterface(r_325.createInterface("if_campus_325"));
		l_campus_325.createInterface((IInterface)campus_r_325.getChildByName("if_stub"));

		INet campus_352 = null;
		IRouter campus_r_352 = null;
		if(null == baseCampus) {
			campus_352 = rv.createNet("campus_1_352");
			campus_r_352 = createCampus(campus_352,do_spherical);
		}else {
			campus_352 = rv.createNetReplica("campus_1_352",baseCampus);
			campus_r_352 = (IRouter)campus_352.getChildByName("sub_campus_router");
		}
		ILink l_campus_352 = rv.createLink("l_campus_352");
		l_campus_352.createInterface(r_352.createInterface("if_campus_352"));
		l_campus_352.createInterface((IInterface)campus_r_352.getChildByName("if_stub"));

		INet campus_357 = null;
		IRouter campus_r_357 = null;
		if(null == baseCampus) {
			campus_357 = rv.createNet("campus_1_357");
			campus_r_357 = createCampus(campus_357,do_spherical);
		}else {
			campus_357 = rv.createNetReplica("campus_1_357",baseCampus);
			campus_r_357 = (IRouter)campus_357.getChildByName("sub_campus_router");
		}
		ILink l_campus_357 = rv.createLink("l_campus_357");
		l_campus_357.createInterface(r_357.createInterface("if_campus_357"));
		l_campus_357.createInterface((IInterface)campus_r_357.getChildByName("if_stub"));

		INet campus_532 = null;
		IRouter campus_r_532 = null;
		if(null == baseCampus) {
			campus_532 = rv.createNet("campus_1_532");
			campus_r_532 = createCampus(campus_532,do_spherical);
		}else {
			campus_532 = rv.createNetReplica("campus_1_532",baseCampus);
			campus_r_532 = (IRouter)campus_532.getChildByName("sub_campus_router");
		}
		ILink l_campus_532 = rv.createLink("l_campus_532");
		l_campus_532.createInterface(r_532.createInterface("if_campus_532"));
		l_campus_532.createInterface((IInterface)campus_r_532.getChildByName("if_stub"));
		return rv;
	}
	public INet create_net_2(INet top, Map<Integer,IRouter> routers) {
		INet rv = top.createNet("net_2");
		if(do_spherical) rv.createShortestPath();;

		//create routers
		IRouter r_192 = rv.createRouter("r_192");
		routers.put(192,r_192);

		IRouter r_193 = rv.createRouter("r_193");
		routers.put(193,r_193);

		IRouter r_338 = rv.createRouter("r_338");
		routers.put(338,r_338);

		IRouter r_358 = rv.createRouter("r_358");
		routers.put(358,r_358);

		IRouter r_360 = rv.createRouter("r_360");
		routers.put(360,r_360);

		IRouter r_361 = rv.createRouter("r_361");
		routers.put(361,r_361);

		IRouter r_362 = rv.createRouter("r_362");
		routers.put(362,r_362);

		IRouter r_363 = rv.createRouter("r_363");
		routers.put(363,r_363);

		IRouter r_364 = rv.createRouter("r_364");
		routers.put(364,r_364);

		IRouter r_365 = rv.createRouter("r_365");
		routers.put(365,r_365);

		IRouter r_366 = rv.createRouter("r_366");
		routers.put(366,r_366);

		IRouter r_367 = rv.createRouter("r_367");
		routers.put(367,r_367);

		IRouter r_368 = rv.createRouter("r_368");
		routers.put(368,r_368);

		IRouter r_369 = rv.createRouter("r_369");
		routers.put(369,r_369);

		IRouter r_372 = rv.createRouter("r_372");
		routers.put(372,r_372);

		IRouter r_376 = rv.createRouter("r_376");
		routers.put(376,r_376);

		IRouter r_377 = rv.createRouter("r_377");
		routers.put(377,r_377);

		IRouter r_383 = rv.createRouter("r_383");
		routers.put(383,r_383);

		IRouter r_384 = rv.createRouter("r_384");
		routers.put(384,r_384);

		IRouter r_396 = rv.createRouter("r_396");
		routers.put(396,r_396);

		IRouter r_406 = rv.createRouter("r_406");
		routers.put(406,r_406);

		IRouter r_415 = rv.createRouter("r_415");
		routers.put(415,r_415);

		IRouter r_417 = rv.createRouter("r_417");
		routers.put(417,r_417);

		IRouter r_418 = rv.createRouter("r_418");
		routers.put(418,r_418);

		IRouter r_487 = rv.createRouter("r_487");
		routers.put(487,r_487);

		IRouter r_488 = rv.createRouter("r_488");
		routers.put(488,r_488);

		IRouter r_489 = rv.createRouter("r_489");
		routers.put(489,r_489);

		IRouter r_493 = rv.createRouter("r_493");
		routers.put(493,r_493);

		IRouter r_496 = rv.createRouter("r_496");
		routers.put(496,r_496);

		IRouter r_497 = rv.createRouter("r_497");
		routers.put(497,r_497);

		IRouter r_498 = rv.createRouter("r_498");
		routers.put(498,r_498);

		IRouter r_499 = rv.createRouter("r_499");
		routers.put(499,r_499);

		IRouter r_500 = rv.createRouter("r_500");
		routers.put(500,r_500);

		IRouter r_501 = rv.createRouter("r_501");
		routers.put(501,r_501);

		IRouter r_568 = rv.createRouter("r_568");
		routers.put(568,r_568);

		IRouter r_596 = rv.createRouter("r_596");
		routers.put(596,r_596);

		IRouter r_597 = rv.createRouter("r_597");
		routers.put(597,r_597);

		IRouter r_598 = rv.createRouter("r_598");
		routers.put(598,r_598);

		IRouter r_599 = rv.createRouter("r_599");
		routers.put(599,r_599);

		IRouter r_600 = rv.createRouter("r_600");
		routers.put(600,r_600);

		//create links
		ILink l_193_192 = rv.createLink("l_193_192");
		l_193_192.createInterface(routers.get(193).createInterface("if_192"));
		l_193_192.createInterface(routers.get(192).createInterface("if_193"));

		ILink l_338_193 = rv.createLink("l_338_193");
		l_338_193.createInterface(routers.get(338).createInterface("if_193"));
		l_338_193.createInterface(routers.get(193).createInterface("if_338"));

		ILink l_361_193 = rv.createLink("l_361_193");
		l_361_193.createInterface(routers.get(361).createInterface("if_193"));
		l_361_193.createInterface(routers.get(193).createInterface("if_361"));

		ILink l_372_193 = rv.createLink("l_372_193");
		l_372_193.createInterface(routers.get(372).createInterface("if_193"));
		l_372_193.createInterface(routers.get(193).createInterface("if_372"));

		ILink l_384_193 = rv.createLink("l_384_193");
		l_384_193.createInterface(routers.get(384).createInterface("if_193"));
		l_384_193.createInterface(routers.get(193).createInterface("if_384"));

		ILink l_596_193 = rv.createLink("l_596_193");
		l_596_193.createInterface(routers.get(596).createInterface("if_193"));
		l_596_193.createInterface(routers.get(193).createInterface("if_596"));

		ILink l_597_193 = rv.createLink("l_597_193");
		l_597_193.createInterface(routers.get(597).createInterface("if_193"));
		l_597_193.createInterface(routers.get(193).createInterface("if_597"));

		ILink l_598_193 = rv.createLink("l_598_193");
		l_598_193.createInterface(routers.get(598).createInterface("if_193"));
		l_598_193.createInterface(routers.get(193).createInterface("if_598"));

		ILink l_360_358 = rv.createLink("l_360_358");
		l_360_358.createInterface(routers.get(360).createInterface("if_358"));
		l_360_358.createInterface(routers.get(358).createInterface("if_360"));

		ILink l_361_358 = rv.createLink("l_361_358");
		l_361_358.createInterface(routers.get(361).createInterface("if_358"));
		l_361_358.createInterface(routers.get(358).createInterface("if_361"));

		ILink l_362_358 = rv.createLink("l_362_358");
		l_362_358.createInterface(routers.get(362).createInterface("if_358"));
		l_362_358.createInterface(routers.get(358).createInterface("if_362"));

		ILink l_363_358 = rv.createLink("l_363_358");
		l_363_358.createInterface(routers.get(363).createInterface("if_358"));
		l_363_358.createInterface(routers.get(358).createInterface("if_363"));

		ILink l_364_358 = rv.createLink("l_364_358");
		l_364_358.createInterface(routers.get(364).createInterface("if_358"));
		l_364_358.createInterface(routers.get(358).createInterface("if_364"));

		ILink l_365_358 = rv.createLink("l_365_358");
		l_365_358.createInterface(routers.get(365).createInterface("if_358"));
		l_365_358.createInterface(routers.get(358).createInterface("if_365"));

		ILink l_366_358 = rv.createLink("l_366_358");
		l_366_358.createInterface(routers.get(366).createInterface("if_358"));
		l_366_358.createInterface(routers.get(358).createInterface("if_366"));

		ILink l_367_358 = rv.createLink("l_367_358");
		l_367_358.createInterface(routers.get(367).createInterface("if_358"));
		l_367_358.createInterface(routers.get(358).createInterface("if_367"));

		ILink l_368_358 = rv.createLink("l_368_358");
		l_368_358.createInterface(routers.get(368).createInterface("if_358"));
		l_368_358.createInterface(routers.get(358).createInterface("if_368"));

		ILink l_369_358 = rv.createLink("l_369_358");
		l_369_358.createInterface(routers.get(369).createInterface("if_358"));
		l_369_358.createInterface(routers.get(358).createInterface("if_369"));

		ILink l_367_360 = rv.createLink("l_367_360");
		l_367_360.createInterface(routers.get(367).createInterface("if_360"));
		l_367_360.createInterface(routers.get(360).createInterface("if_367"));

		ILink l_368_360 = rv.createLink("l_368_360");
		l_368_360.createInterface(routers.get(368).createInterface("if_360"));
		l_368_360.createInterface(routers.get(360).createInterface("if_368"));

		ILink l_376_360 = rv.createLink("l_376_360");
		l_376_360.createInterface(routers.get(376).createInterface("if_360"));
		l_376_360.createInterface(routers.get(360).createInterface("if_376"));

		ILink l_377_360 = rv.createLink("l_377_360");
		l_377_360.createInterface(routers.get(377).createInterface("if_360"));
		l_377_360.createInterface(routers.get(360).createInterface("if_377"));

		ILink l_364_361 = rv.createLink("l_364_361");
		l_364_361.createInterface(routers.get(364).createInterface("if_361"));
		l_364_361.createInterface(routers.get(361).createInterface("if_364"));

		ILink l_372_361 = rv.createLink("l_372_361");
		l_372_361.createInterface(routers.get(372).createInterface("if_361"));
		l_372_361.createInterface(routers.get(361).createInterface("if_372"));

		ILink l_415_361 = rv.createLink("l_415_361");
		l_415_361.createInterface(routers.get(415).createInterface("if_361"));
		l_415_361.createInterface(routers.get(361).createInterface("if_415"));

		ILink l_417_361 = rv.createLink("l_417_361");
		l_417_361.createInterface(routers.get(417).createInterface("if_361"));
		l_417_361.createInterface(routers.get(361).createInterface("if_417"));

		ILink l_418_361 = rv.createLink("l_418_361");
		l_418_361.createInterface(routers.get(418).createInterface("if_361"));
		l_418_361.createInterface(routers.get(361).createInterface("if_418"));

		ILink l_377_362 = rv.createLink("l_377_362");
		l_377_362.createInterface(routers.get(377).createInterface("if_362"));
		l_377_362.createInterface(routers.get(362).createInterface("if_377"));

		ILink l_493_362 = rv.createLink("l_493_362");
		l_493_362.createInterface(routers.get(493).createInterface("if_362"));
		l_493_362.createInterface(routers.get(362).createInterface("if_493"));

		ILink l_496_362 = rv.createLink("l_496_362");
		l_496_362.createInterface(routers.get(496).createInterface("if_362"));
		l_496_362.createInterface(routers.get(362).createInterface("if_496"));

		ILink l_497_362 = rv.createLink("l_497_362");
		l_497_362.createInterface(routers.get(497).createInterface("if_362"));
		l_497_362.createInterface(routers.get(362).createInterface("if_497"));

		ILink l_498_362 = rv.createLink("l_498_362");
		l_498_362.createInterface(routers.get(498).createInterface("if_362"));
		l_498_362.createInterface(routers.get(362).createInterface("if_498"));

		ILink l_377_363 = rv.createLink("l_377_363");
		l_377_363.createInterface(routers.get(377).createInterface("if_363"));
		l_377_363.createInterface(routers.get(363).createInterface("if_377"));

		ILink l_487_363 = rv.createLink("l_487_363");
		l_487_363.createInterface(routers.get(487).createInterface("if_363"));
		l_487_363.createInterface(routers.get(363).createInterface("if_487"));

		ILink l_493_363 = rv.createLink("l_493_363");
		l_493_363.createInterface(routers.get(493).createInterface("if_363"));
		l_493_363.createInterface(routers.get(363).createInterface("if_493"));

		ILink l_496_363 = rv.createLink("l_496_363");
		l_496_363.createInterface(routers.get(496).createInterface("if_363"));
		l_496_363.createInterface(routers.get(363).createInterface("if_496"));

		ILink l_497_363 = rv.createLink("l_497_363");
		l_497_363.createInterface(routers.get(497).createInterface("if_363"));
		l_497_363.createInterface(routers.get(363).createInterface("if_497"));

		ILink l_498_363 = rv.createLink("l_498_363");
		l_498_363.createInterface(routers.get(498).createInterface("if_363"));
		l_498_363.createInterface(routers.get(363).createInterface("if_498"));

		ILink l_366_364 = rv.createLink("l_366_364");
		l_366_364.createInterface(routers.get(366).createInterface("if_364"));
		l_366_364.createInterface(routers.get(364).createInterface("if_366"));

		ILink l_377_364 = rv.createLink("l_377_364");
		l_377_364.createInterface(routers.get(377).createInterface("if_364"));
		l_377_364.createInterface(routers.get(364).createInterface("if_377"));

		ILink l_415_364 = rv.createLink("l_415_364");
		l_415_364.createInterface(routers.get(415).createInterface("if_364"));
		l_415_364.createInterface(routers.get(364).createInterface("if_415"));

		ILink l_489_364 = rv.createLink("l_489_364");
		l_489_364.createInterface(routers.get(489).createInterface("if_364"));
		l_489_364.createInterface(routers.get(364).createInterface("if_489"));

		ILink l_367_365 = rv.createLink("l_367_365");
		l_367_365.createInterface(routers.get(367).createInterface("if_365"));
		l_367_365.createInterface(routers.get(365).createInterface("if_367"));

		ILink l_368_365 = rv.createLink("l_368_365");
		l_368_365.createInterface(routers.get(368).createInterface("if_365"));
		l_368_365.createInterface(routers.get(365).createInterface("if_368"));

		ILink l_372_367 = rv.createLink("l_372_367");
		l_372_367.createInterface(routers.get(372).createInterface("if_367"));
		l_372_367.createInterface(routers.get(367).createInterface("if_372"));

		ILink l_377_367 = rv.createLink("l_377_367");
		l_377_367.createInterface(routers.get(377).createInterface("if_367"));
		l_377_367.createInterface(routers.get(367).createInterface("if_377"));

		ILink l_396_367 = rv.createLink("l_396_367");
		l_396_367.createInterface(routers.get(396).createInterface("if_367"));
		l_396_367.createInterface(routers.get(367).createInterface("if_396"));

		ILink l_489_367 = rv.createLink("l_489_367");
		l_489_367.createInterface(routers.get(489).createInterface("if_367"));
		l_489_367.createInterface(routers.get(367).createInterface("if_489"));

		ILink l_372_368 = rv.createLink("l_372_368");
		l_372_368.createInterface(routers.get(372).createInterface("if_368"));
		l_372_368.createInterface(routers.get(368).createInterface("if_372"));

		ILink l_377_368 = rv.createLink("l_377_368");
		l_377_368.createInterface(routers.get(377).createInterface("if_368"));
		l_377_368.createInterface(routers.get(368).createInterface("if_377"));

		ILink l_489_368 = rv.createLink("l_489_368");
		l_489_368.createInterface(routers.get(489).createInterface("if_368"));
		l_489_368.createInterface(routers.get(368).createInterface("if_489"));

		ILink l_376_369 = rv.createLink("l_376_369");
		l_376_369.createInterface(routers.get(376).createInterface("if_369"));
		l_376_369.createInterface(routers.get(369).createInterface("if_376"));

		ILink l_377_372 = rv.createLink("l_377_372");
		l_377_372.createInterface(routers.get(377).createInterface("if_372"));
		l_377_372.createInterface(routers.get(372).createInterface("if_377"));

		ILink l_417_372 = rv.createLink("l_417_372");
		l_417_372.createInterface(routers.get(417).createInterface("if_372"));
		l_417_372.createInterface(routers.get(372).createInterface("if_417"));

		ILink l_418_372 = rv.createLink("l_418_372");
		l_418_372.createInterface(routers.get(418).createInterface("if_372"));
		l_418_372.createInterface(routers.get(372).createInterface("if_418"));

		ILink l_396_376 = rv.createLink("l_396_376");
		l_396_376.createInterface(routers.get(396).createInterface("if_376"));
		l_396_376.createInterface(routers.get(376).createInterface("if_396"));

		ILink l_415_377 = rv.createLink("l_415_377");
		l_415_377.createInterface(routers.get(415).createInterface("if_377"));
		l_415_377.createInterface(routers.get(377).createInterface("if_415"));

		ILink l_384_383 = rv.createLink("l_384_383");
		l_384_383.createInterface(routers.get(384).createInterface("if_383"));
		l_384_383.createInterface(routers.get(383).createInterface("if_384"));

		ILink l_406_384 = rv.createLink("l_406_384");
		l_406_384.createInterface(routers.get(406).createInterface("if_384"));
		l_406_384.createInterface(routers.get(384).createInterface("if_406"));

		ILink l_489_415 = rv.createLink("l_489_415");
		l_489_415.createInterface(routers.get(489).createInterface("if_415"));
		l_489_415.createInterface(routers.get(415).createInterface("if_489"));

		ILink l_599_417 = rv.createLink("l_599_417");
		l_599_417.createInterface(routers.get(599).createInterface("if_417"));
		l_599_417.createInterface(routers.get(417).createInterface("if_599"));

		ILink l_599_418 = rv.createLink("l_599_418");
		l_599_418.createInterface(routers.get(599).createInterface("if_418"));
		l_599_418.createInterface(routers.get(418).createInterface("if_599"));

		ILink l_600_418 = rv.createLink("l_600_418");
		l_600_418.createInterface(routers.get(600).createInterface("if_418"));
		l_600_418.createInterface(routers.get(418).createInterface("if_600"));

		ILink l_488_487 = rv.createLink("l_488_487");
		l_488_487.createInterface(routers.get(488).createInterface("if_487"));
		l_488_487.createInterface(routers.get(487).createInterface("if_488"));

		ILink l_489_487 = rv.createLink("l_489_487");
		l_489_487.createInterface(routers.get(489).createInterface("if_487"));
		l_489_487.createInterface(routers.get(487).createInterface("if_489"));

		ILink l_493_488 = rv.createLink("l_493_488");
		l_493_488.createInterface(routers.get(493).createInterface("if_488"));
		l_493_488.createInterface(routers.get(488).createInterface("if_493"));

		ILink l_493_489 = rv.createLink("l_493_489");
		l_493_489.createInterface(routers.get(493).createInterface("if_489"));
		l_493_489.createInterface(routers.get(489).createInterface("if_493"));

		ILink l_498_489 = rv.createLink("l_498_489");
		l_498_489.createInterface(routers.get(498).createInterface("if_489"));
		l_498_489.createInterface(routers.get(489).createInterface("if_498"));

		ILink l_499_489 = rv.createLink("l_499_489");
		l_499_489.createInterface(routers.get(499).createInterface("if_489"));
		l_499_489.createInterface(routers.get(489).createInterface("if_499"));

		ILink l_500_489 = rv.createLink("l_500_489");
		l_500_489.createInterface(routers.get(500).createInterface("if_489"));
		l_500_489.createInterface(routers.get(489).createInterface("if_500"));

		ILink l_501_496 = rv.createLink("l_501_496");
		l_501_496.createInterface(routers.get(501).createInterface("if_496"));
		l_501_496.createInterface(routers.get(496).createInterface("if_501"));

		ILink l_501_499 = rv.createLink("l_501_499");
		l_501_499.createInterface(routers.get(501).createInterface("if_499"));
		l_501_499.createInterface(routers.get(499).createInterface("if_501"));

		//attach campus networks

		INet campus_192 = null;
		IRouter campus_r_192 = null;
		if(null == baseCampus) {
			campus_192 = rv.createNet("campus_2_192");
			campus_r_192 = createCampus(campus_192,do_spherical);
		}else {
			campus_192 = rv.createNetReplica("campus_2_192",baseCampus);
			campus_r_192 = (IRouter)campus_192.getChildByName("sub_campus_router");
		}
		ILink l_campus_192 = rv.createLink("l_campus_192");
		l_campus_192.createInterface(r_192.createInterface("if_campus_192"));
		l_campus_192.createInterface((IInterface)campus_r_192.getChildByName("if_stub"));

		INet campus_338 = null;
		IRouter campus_r_338 = null;
		if(null == baseCampus) {
			campus_338 = rv.createNet("campus_2_338");
			campus_r_338 = createCampus(campus_338,do_spherical);
		}else {
			campus_338 = rv.createNetReplica("campus_2_338",baseCampus);
			campus_r_338 = (IRouter)campus_338.getChildByName("sub_campus_router");
		}
		ILink l_campus_338 = rv.createLink("l_campus_338");
		l_campus_338.createInterface(r_338.createInterface("if_campus_338"));
		l_campus_338.createInterface((IInterface)campus_r_338.getChildByName("if_stub"));

		INet campus_383 = null;
		IRouter campus_r_383 = null;
		if(null == baseCampus) {
			campus_383 = rv.createNet("campus_2_383");
			campus_r_383 = createCampus(campus_383,do_spherical);
		}else {
			campus_383 = rv.createNetReplica("campus_2_383",baseCampus);
			campus_r_383 = (IRouter)campus_383.getChildByName("sub_campus_router");
		}
		ILink l_campus_383 = rv.createLink("l_campus_383");
		l_campus_383.createInterface(r_383.createInterface("if_campus_383"));
		l_campus_383.createInterface((IInterface)campus_r_383.getChildByName("if_stub"));

		INet campus_406 = null;
		IRouter campus_r_406 = null;
		if(null == baseCampus) {
			campus_406 = rv.createNet("campus_2_406");
			campus_r_406 = createCampus(campus_406,do_spherical);
		}else {
			campus_406 = rv.createNetReplica("campus_2_406",baseCampus);
			campus_r_406 = (IRouter)campus_406.getChildByName("sub_campus_router");
		}
		ILink l_campus_406 = rv.createLink("l_campus_406");
		l_campus_406.createInterface(r_406.createInterface("if_campus_406"));
		l_campus_406.createInterface((IInterface)campus_r_406.getChildByName("if_stub"));

		INet campus_500 = null;
		IRouter campus_r_500 = null;
		if(null == baseCampus) {
			campus_500 = rv.createNet("campus_2_500");
			campus_r_500 = createCampus(campus_500,do_spherical);
		}else {
			campus_500 = rv.createNetReplica("campus_2_500",baseCampus);
			campus_r_500 = (IRouter)campus_500.getChildByName("sub_campus_router");
		}
		ILink l_campus_500 = rv.createLink("l_campus_500");
		l_campus_500.createInterface(r_500.createInterface("if_campus_500"));
		l_campus_500.createInterface((IInterface)campus_r_500.getChildByName("if_stub"));

		INet campus_568 = null;
		IRouter campus_r_568 = null;
		if(null == baseCampus) {
			campus_568 = rv.createNet("campus_2_568");
			campus_r_568 = createCampus(campus_568,do_spherical);
		}else {
			campus_568 = rv.createNetReplica("campus_2_568",baseCampus);
			campus_r_568 = (IRouter)campus_568.getChildByName("sub_campus_router");
		}
		ILink l_campus_568 = rv.createLink("l_campus_568");
		l_campus_568.createInterface(r_568.createInterface("if_campus_568"));
		l_campus_568.createInterface((IInterface)campus_r_568.getChildByName("if_stub"));

		INet campus_596 = null;
		IRouter campus_r_596 = null;
		if(null == baseCampus) {
			campus_596 = rv.createNet("campus_2_596");
			campus_r_596 = createCampus(campus_596,do_spherical);
		}else {
			campus_596 = rv.createNetReplica("campus_2_596",baseCampus);
			campus_r_596 = (IRouter)campus_596.getChildByName("sub_campus_router");
		}
		ILink l_campus_596 = rv.createLink("l_campus_596");
		l_campus_596.createInterface(r_596.createInterface("if_campus_596"));
		l_campus_596.createInterface((IInterface)campus_r_596.getChildByName("if_stub"));

		INet campus_597 = null;
		IRouter campus_r_597 = null;
		if(null == baseCampus) {
			campus_597 = rv.createNet("campus_2_597");
			campus_r_597 = createCampus(campus_597,do_spherical);
		}else {
			campus_597 = rv.createNetReplica("campus_2_597",baseCampus);
			campus_r_597 = (IRouter)campus_597.getChildByName("sub_campus_router");
		}
		ILink l_campus_597 = rv.createLink("l_campus_597");
		l_campus_597.createInterface(r_597.createInterface("if_campus_597"));
		l_campus_597.createInterface((IInterface)campus_r_597.getChildByName("if_stub"));

		INet campus_598 = null;
		IRouter campus_r_598 = null;
		if(null == baseCampus) {
			campus_598 = rv.createNet("campus_2_598");
			campus_r_598 = createCampus(campus_598,do_spherical);
		}else {
			campus_598 = rv.createNetReplica("campus_2_598",baseCampus);
			campus_r_598 = (IRouter)campus_598.getChildByName("sub_campus_router");
		}
		ILink l_campus_598 = rv.createLink("l_campus_598");
		l_campus_598.createInterface(r_598.createInterface("if_campus_598"));
		l_campus_598.createInterface((IInterface)campus_r_598.getChildByName("if_stub"));

		INet campus_600 = null;
		IRouter campus_r_600 = null;
		if(null == baseCampus) {
			campus_600 = rv.createNet("campus_2_600");
			campus_r_600 = createCampus(campus_600,do_spherical);
		}else {
			campus_600 = rv.createNetReplica("campus_2_600",baseCampus);
			campus_r_600 = (IRouter)campus_600.getChildByName("sub_campus_router");
		}
		ILink l_campus_600 = rv.createLink("l_campus_600");
		l_campus_600.createInterface(r_600.createInterface("if_campus_600"));
		l_campus_600.createInterface((IInterface)campus_r_600.getChildByName("if_stub"));
		return rv;
	}
	public INet create_net_3(INet top, Map<Integer,IRouter> routers) {
		INet rv = top.createNet("net_3");
		if(do_spherical) rv.createShortestPath();;

		//create routers
		IRouter r_10 = rv.createRouter("r_10");
		routers.put(10,r_10);

		IRouter r_11 = rv.createRouter("r_11");
		routers.put(11,r_11);

		IRouter r_51 = rv.createRouter("r_51");
		routers.put(51,r_51);

		IRouter r_52 = rv.createRouter("r_52");
		routers.put(52,r_52);

		IRouter r_53 = rv.createRouter("r_53");
		routers.put(53,r_53);

		IRouter r_56 = rv.createRouter("r_56");
		routers.put(56,r_56);

		IRouter r_129 = rv.createRouter("r_129");
		routers.put(129,r_129);

		IRouter r_182 = rv.createRouter("r_182");
		routers.put(182,r_182);

		IRouter r_183 = rv.createRouter("r_183");
		routers.put(183,r_183);

		IRouter r_226 = rv.createRouter("r_226");
		routers.put(226,r_226);

		IRouter r_232 = rv.createRouter("r_232");
		routers.put(232,r_232);

		IRouter r_233 = rv.createRouter("r_233");
		routers.put(233,r_233);

		IRouter r_234 = rv.createRouter("r_234");
		routers.put(234,r_234);

		IRouter r_238 = rv.createRouter("r_238");
		routers.put(238,r_238);

		IRouter r_239 = rv.createRouter("r_239");
		routers.put(239,r_239);

		IRouter r_240 = rv.createRouter("r_240");
		routers.put(240,r_240);

		IRouter r_303 = rv.createRouter("r_303");
		routers.put(303,r_303);

		IRouter r_304 = rv.createRouter("r_304");
		routers.put(304,r_304);

		IRouter r_305 = rv.createRouter("r_305");
		routers.put(305,r_305);

		IRouter r_346 = rv.createRouter("r_346");
		routers.put(346,r_346);

		IRouter r_347 = rv.createRouter("r_347");
		routers.put(347,r_347);

		IRouter r_348 = rv.createRouter("r_348");
		routers.put(348,r_348);

		IRouter r_373 = rv.createRouter("r_373");
		routers.put(373,r_373);

		IRouter r_408 = rv.createRouter("r_408");
		routers.put(408,r_408);

		IRouter r_409 = rv.createRouter("r_409");
		routers.put(409,r_409);

		IRouter r_410 = rv.createRouter("r_410");
		routers.put(410,r_410);

		IRouter r_411 = rv.createRouter("r_411");
		routers.put(411,r_411);

		IRouter r_412 = rv.createRouter("r_412");
		routers.put(412,r_412);

		IRouter r_413 = rv.createRouter("r_413");
		routers.put(413,r_413);

		IRouter r_414 = rv.createRouter("r_414");
		routers.put(414,r_414);

		IRouter r_471 = rv.createRouter("r_471");
		routers.put(471,r_471);

		IRouter r_503 = rv.createRouter("r_503");
		routers.put(503,r_503);

		IRouter r_504 = rv.createRouter("r_504");
		routers.put(504,r_504);

		IRouter r_505 = rv.createRouter("r_505");
		routers.put(505,r_505);

		IRouter r_564 = rv.createRouter("r_564");
		routers.put(564,r_564);

		IRouter r_565 = rv.createRouter("r_565");
		routers.put(565,r_565);

		IRouter r_566 = rv.createRouter("r_566");
		routers.put(566,r_566);

		IRouter r_567 = rv.createRouter("r_567");
		routers.put(567,r_567);

		IRouter r_569 = rv.createRouter("r_569");
		routers.put(569,r_569);

		IRouter r_639 = rv.createRouter("r_639");
		routers.put(639,r_639);

		//create links
		ILink l_11_10 = rv.createLink("l_11_10");
		l_11_10.createInterface(routers.get(11).createInterface("if_10"));
		l_11_10.createInterface(routers.get(10).createInterface("if_11"));

		ILink l_56_11 = rv.createLink("l_56_11");
		l_56_11.createInterface(routers.get(56).createInterface("if_11"));
		l_56_11.createInterface(routers.get(11).createInterface("if_56"));

		ILink l_52_51 = rv.createLink("l_52_51");
		l_52_51.createInterface(routers.get(52).createInterface("if_51"));
		l_52_51.createInterface(routers.get(51).createInterface("if_52"));

		ILink l_53_51 = rv.createLink("l_53_51");
		l_53_51.createInterface(routers.get(53).createInterface("if_51"));
		l_53_51.createInterface(routers.get(51).createInterface("if_53"));

		ILink l_53_52 = rv.createLink("l_53_52");
		l_53_52.createInterface(routers.get(53).createInterface("if_52"));
		l_53_52.createInterface(routers.get(52).createInterface("if_53"));

		ILink l_233_52 = rv.createLink("l_233_52");
		l_233_52.createInterface(routers.get(233).createInterface("if_52"));
		l_233_52.createInterface(routers.get(52).createInterface("if_233"));

		ILink l_234_52 = rv.createLink("l_234_52");
		l_234_52.createInterface(routers.get(234).createInterface("if_52"));
		l_234_52.createInterface(routers.get(52).createInterface("if_234"));

		ILink l_639_52 = rv.createLink("l_639_52");
		l_639_52.createInterface(routers.get(639).createInterface("if_52"));
		l_639_52.createInterface(routers.get(52).createInterface("if_639"));

		ILink l_129_53 = rv.createLink("l_129_53");
		l_129_53.createInterface(routers.get(129).createInterface("if_53"));
		l_129_53.createInterface(routers.get(53).createInterface("if_129"));

		ILink l_182_53 = rv.createLink("l_182_53");
		l_182_53.createInterface(routers.get(182).createInterface("if_53"));
		l_182_53.createInterface(routers.get(53).createInterface("if_182"));

		ILink l_226_56 = rv.createLink("l_226_56");
		l_226_56.createInterface(routers.get(226).createInterface("if_56"));
		l_226_56.createInterface(routers.get(56).createInterface("if_226"));

		ILink l_232_56 = rv.createLink("l_232_56");
		l_232_56.createInterface(routers.get(232).createInterface("if_56"));
		l_232_56.createInterface(routers.get(56).createInterface("if_232"));

		ILink l_233_56 = rv.createLink("l_233_56");
		l_233_56.createInterface(routers.get(233).createInterface("if_56"));
		l_233_56.createInterface(routers.get(56).createInterface("if_233"));

		ILink l_234_56 = rv.createLink("l_234_56");
		l_234_56.createInterface(routers.get(234).createInterface("if_56"));
		l_234_56.createInterface(routers.get(56).createInterface("if_234"));

		ILink l_183_182 = rv.createLink("l_183_182");
		l_183_182.createInterface(routers.get(183).createInterface("if_182"));
		l_183_182.createInterface(routers.get(182).createInterface("if_183"));

		ILink l_233_232 = rv.createLink("l_233_232");
		l_233_232.createInterface(routers.get(233).createInterface("if_232"));
		l_233_232.createInterface(routers.get(232).createInterface("if_233"));

		ILink l_234_232 = rv.createLink("l_234_232");
		l_234_232.createInterface(routers.get(234).createInterface("if_232"));
		l_234_232.createInterface(routers.get(232).createInterface("if_234"));

		ILink l_234_233 = rv.createLink("l_234_233");
		l_234_233.createInterface(routers.get(234).createInterface("if_233"));
		l_234_233.createInterface(routers.get(233).createInterface("if_234"));

		ILink l_471_233 = rv.createLink("l_471_233");
		l_471_233.createInterface(routers.get(471).createInterface("if_233"));
		l_471_233.createInterface(routers.get(233).createInterface("if_471"));

		ILink l_471_234 = rv.createLink("l_471_234");
		l_471_234.createInterface(routers.get(471).createInterface("if_234"));
		l_471_234.createInterface(routers.get(234).createInterface("if_471"));

		ILink l_639_234 = rv.createLink("l_639_234");
		l_639_234.createInterface(routers.get(639).createInterface("if_234"));
		l_639_234.createInterface(routers.get(234).createInterface("if_639"));

		ILink l_239_238 = rv.createLink("l_239_238");
		l_239_238.createInterface(routers.get(239).createInterface("if_238"));
		l_239_238.createInterface(routers.get(238).createInterface("if_239"));

		ILink l_240_238 = rv.createLink("l_240_238");
		l_240_238.createInterface(routers.get(240).createInterface("if_238"));
		l_240_238.createInterface(routers.get(238).createInterface("if_240"));

		ILink l_240_239 = rv.createLink("l_240_239");
		l_240_239.createInterface(routers.get(240).createInterface("if_239"));
		l_240_239.createInterface(routers.get(239).createInterface("if_240"));

		ILink l_304_239 = rv.createLink("l_304_239");
		l_304_239.createInterface(routers.get(304).createInterface("if_239"));
		l_304_239.createInterface(routers.get(239).createInterface("if_304"));

		ILink l_373_239 = rv.createLink("l_373_239");
		l_373_239.createInterface(routers.get(373).createInterface("if_239"));
		l_373_239.createInterface(routers.get(239).createInterface("if_373"));

		ILink l_409_239 = rv.createLink("l_409_239");
		l_409_239.createInterface(routers.get(409).createInterface("if_239"));
		l_409_239.createInterface(routers.get(239).createInterface("if_409"));

		ILink l_410_239 = rv.createLink("l_410_239");
		l_410_239.createInterface(routers.get(410).createInterface("if_239"));
		l_410_239.createInterface(routers.get(239).createInterface("if_410"));

		ILink l_411_239 = rv.createLink("l_411_239");
		l_411_239.createInterface(routers.get(411).createInterface("if_239"));
		l_411_239.createInterface(routers.get(239).createInterface("if_411"));

		ILink l_564_239 = rv.createLink("l_564_239");
		l_564_239.createInterface(routers.get(564).createInterface("if_239"));
		l_564_239.createInterface(routers.get(239).createInterface("if_564"));

		ILink l_565_239 = rv.createLink("l_565_239");
		l_565_239.createInterface(routers.get(565).createInterface("if_239"));
		l_565_239.createInterface(routers.get(239).createInterface("if_565"));

		ILink l_304_240 = rv.createLink("l_304_240");
		l_304_240.createInterface(routers.get(304).createInterface("if_240"));
		l_304_240.createInterface(routers.get(240).createInterface("if_304"));

		ILink l_373_240 = rv.createLink("l_373_240");
		l_373_240.createInterface(routers.get(373).createInterface("if_240"));
		l_373_240.createInterface(routers.get(240).createInterface("if_373"));

		ILink l_412_240 = rv.createLink("l_412_240");
		l_412_240.createInterface(routers.get(412).createInterface("if_240"));
		l_412_240.createInterface(routers.get(240).createInterface("if_412"));

		ILink l_565_240 = rv.createLink("l_565_240");
		l_565_240.createInterface(routers.get(565).createInterface("if_240"));
		l_565_240.createInterface(routers.get(240).createInterface("if_565"));

		ILink l_566_240 = rv.createLink("l_566_240");
		l_566_240.createInterface(routers.get(566).createInterface("if_240"));
		l_566_240.createInterface(routers.get(240).createInterface("if_566"));

		ILink l_304_303 = rv.createLink("l_304_303");
		l_304_303.createInterface(routers.get(304).createInterface("if_303"));
		l_304_303.createInterface(routers.get(303).createInterface("if_304"));

		ILink l_305_303 = rv.createLink("l_305_303");
		l_305_303.createInterface(routers.get(305).createInterface("if_303"));
		l_305_303.createInterface(routers.get(303).createInterface("if_305"));

		ILink l_305_304 = rv.createLink("l_305_304");
		l_305_304.createInterface(routers.get(305).createInterface("if_304"));
		l_305_304.createInterface(routers.get(304).createInterface("if_305"));

		ILink l_347_304 = rv.createLink("l_347_304");
		l_347_304.createInterface(routers.get(347).createInterface("if_304"));
		l_347_304.createInterface(routers.get(304).createInterface("if_347"));

		ILink l_348_304 = rv.createLink("l_348_304");
		l_348_304.createInterface(routers.get(348).createInterface("if_304"));
		l_348_304.createInterface(routers.get(304).createInterface("if_348"));

		ILink l_373_304 = rv.createLink("l_373_304");
		l_373_304.createInterface(routers.get(373).createInterface("if_304"));
		l_373_304.createInterface(routers.get(304).createInterface("if_373"));

		ILink l_503_304 = rv.createLink("l_503_304");
		l_503_304.createInterface(routers.get(503).createInterface("if_304"));
		l_503_304.createInterface(routers.get(304).createInterface("if_503"));

		ILink l_505_304 = rv.createLink("l_505_304");
		l_505_304.createInterface(routers.get(505).createInterface("if_304"));
		l_505_304.createInterface(routers.get(304).createInterface("if_505"));

		ILink l_373_305 = rv.createLink("l_373_305");
		l_373_305.createInterface(routers.get(373).createInterface("if_305"));
		l_373_305.createInterface(routers.get(305).createInterface("if_373"));

		ILink l_347_346 = rv.createLink("l_347_346");
		l_347_346.createInterface(routers.get(347).createInterface("if_346"));
		l_347_346.createInterface(routers.get(346).createInterface("if_347"));

		ILink l_348_346 = rv.createLink("l_348_346");
		l_348_346.createInterface(routers.get(348).createInterface("if_346"));
		l_348_346.createInterface(routers.get(346).createInterface("if_348"));

		ILink l_373_347 = rv.createLink("l_373_347");
		l_373_347.createInterface(routers.get(373).createInterface("if_347"));
		l_373_347.createInterface(routers.get(347).createInterface("if_373"));

		ILink l_373_348 = rv.createLink("l_373_348");
		l_373_348.createInterface(routers.get(373).createInterface("if_348"));
		l_373_348.createInterface(routers.get(348).createInterface("if_373"));

		ILink l_503_373 = rv.createLink("l_503_373");
		l_503_373.createInterface(routers.get(503).createInterface("if_373"));
		l_503_373.createInterface(routers.get(373).createInterface("if_503"));

		ILink l_504_373 = rv.createLink("l_504_373");
		l_504_373.createInterface(routers.get(504).createInterface("if_373"));
		l_504_373.createInterface(routers.get(373).createInterface("if_504"));

		ILink l_505_373 = rv.createLink("l_505_373");
		l_505_373.createInterface(routers.get(505).createInterface("if_373"));
		l_505_373.createInterface(routers.get(373).createInterface("if_505"));

		ILink l_409_408 = rv.createLink("l_409_408");
		l_409_408.createInterface(routers.get(409).createInterface("if_408"));
		l_409_408.createInterface(routers.get(408).createInterface("if_409"));

		ILink l_410_408 = rv.createLink("l_410_408");
		l_410_408.createInterface(routers.get(410).createInterface("if_408"));
		l_410_408.createInterface(routers.get(408).createInterface("if_410"));

		ILink l_411_408 = rv.createLink("l_411_408");
		l_411_408.createInterface(routers.get(411).createInterface("if_408"));
		l_411_408.createInterface(routers.get(408).createInterface("if_411"));

		ILink l_412_408 = rv.createLink("l_412_408");
		l_412_408.createInterface(routers.get(412).createInterface("if_408"));
		l_412_408.createInterface(routers.get(408).createInterface("if_412"));

		ILink l_413_408 = rv.createLink("l_413_408");
		l_413_408.createInterface(routers.get(413).createInterface("if_408"));
		l_413_408.createInterface(routers.get(408).createInterface("if_413"));

		ILink l_414_408 = rv.createLink("l_414_408");
		l_414_408.createInterface(routers.get(414).createInterface("if_408"));
		l_414_408.createInterface(routers.get(408).createInterface("if_414"));

		ILink l_412_410 = rv.createLink("l_412_410");
		l_412_410.createInterface(routers.get(412).createInterface("if_410"));
		l_412_410.createInterface(routers.get(410).createInterface("if_412"));

		ILink l_412_411 = rv.createLink("l_412_411");
		l_412_411.createInterface(routers.get(412).createInterface("if_411"));
		l_412_411.createInterface(routers.get(411).createInterface("if_412"));

		ILink l_569_411 = rv.createLink("l_569_411");
		l_569_411.createInterface(routers.get(569).createInterface("if_411"));
		l_569_411.createInterface(routers.get(411).createInterface("if_569"));

		ILink l_569_413 = rv.createLink("l_569_413");
		l_569_413.createInterface(routers.get(569).createInterface("if_413"));
		l_569_413.createInterface(routers.get(413).createInterface("if_569"));

		ILink l_569_414 = rv.createLink("l_569_414");
		l_569_414.createInterface(routers.get(569).createInterface("if_414"));
		l_569_414.createInterface(routers.get(414).createInterface("if_569"));

		ILink l_567_503 = rv.createLink("l_567_503");
		l_567_503.createInterface(routers.get(567).createInterface("if_503"));
		l_567_503.createInterface(routers.get(503).createInterface("if_567"));

		//attach campus networks

		INet campus_10 = null;
		IRouter campus_r_10 = null;
		if(null == baseCampus) {
			campus_10 = rv.createNet("campus_3_10");
			campus_r_10 = createCampus(campus_10,do_spherical);
		}else {
			campus_10 = rv.createNetReplica("campus_3_10",baseCampus);
			campus_r_10 = (IRouter)campus_10.getChildByName("sub_campus_router");
		}
		ILink l_campus_10 = rv.createLink("l_campus_10");
		l_campus_10.createInterface(r_10.createInterface("if_campus_10"));
		l_campus_10.createInterface((IInterface)campus_r_10.getChildByName("if_stub"));

		INet campus_129 = null;
		IRouter campus_r_129 = null;
		if(null == baseCampus) {
			campus_129 = rv.createNet("campus_3_129");
			campus_r_129 = createCampus(campus_129,do_spherical);
		}else {
			campus_129 = rv.createNetReplica("campus_3_129",baseCampus);
			campus_r_129 = (IRouter)campus_129.getChildByName("sub_campus_router");
		}
		ILink l_campus_129 = rv.createLink("l_campus_129");
		l_campus_129.createInterface(r_129.createInterface("if_campus_129"));
		l_campus_129.createInterface((IInterface)campus_r_129.getChildByName("if_stub"));

		INet campus_183 = null;
		IRouter campus_r_183 = null;
		if(null == baseCampus) {
			campus_183 = rv.createNet("campus_3_183");
			campus_r_183 = createCampus(campus_183,do_spherical);
		}else {
			campus_183 = rv.createNetReplica("campus_3_183",baseCampus);
			campus_r_183 = (IRouter)campus_183.getChildByName("sub_campus_router");
		}
		ILink l_campus_183 = rv.createLink("l_campus_183");
		l_campus_183.createInterface(r_183.createInterface("if_campus_183"));
		l_campus_183.createInterface((IInterface)campus_r_183.getChildByName("if_stub"));

		INet campus_226 = null;
		IRouter campus_r_226 = null;
		if(null == baseCampus) {
			campus_226 = rv.createNet("campus_3_226");
			campus_r_226 = createCampus(campus_226,do_spherical);
		}else {
			campus_226 = rv.createNetReplica("campus_3_226",baseCampus);
			campus_r_226 = (IRouter)campus_226.getChildByName("sub_campus_router");
		}
		ILink l_campus_226 = rv.createLink("l_campus_226");
		l_campus_226.createInterface(r_226.createInterface("if_campus_226"));
		l_campus_226.createInterface((IInterface)campus_r_226.getChildByName("if_stub"));

		INet campus_504 = null;
		IRouter campus_r_504 = null;
		if(null == baseCampus) {
			campus_504 = rv.createNet("campus_3_504");
			campus_r_504 = createCampus(campus_504,do_spherical);
		}else {
			campus_504 = rv.createNetReplica("campus_3_504",baseCampus);
			campus_r_504 = (IRouter)campus_504.getChildByName("sub_campus_router");
		}
		ILink l_campus_504 = rv.createLink("l_campus_504");
		l_campus_504.createInterface(r_504.createInterface("if_campus_504"));
		l_campus_504.createInterface((IInterface)campus_r_504.getChildByName("if_stub"));

		INet campus_564 = null;
		IRouter campus_r_564 = null;
		if(null == baseCampus) {
			campus_564 = rv.createNet("campus_3_564");
			campus_r_564 = createCampus(campus_564,do_spherical);
		}else {
			campus_564 = rv.createNetReplica("campus_3_564",baseCampus);
			campus_r_564 = (IRouter)campus_564.getChildByName("sub_campus_router");
		}
		ILink l_campus_564 = rv.createLink("l_campus_564");
		l_campus_564.createInterface(r_564.createInterface("if_campus_564"));
		l_campus_564.createInterface((IInterface)campus_r_564.getChildByName("if_stub"));

		INet campus_566 = null;
		IRouter campus_r_566 = null;
		if(null == baseCampus) {
			campus_566 = rv.createNet("campus_3_566");
			campus_r_566 = createCampus(campus_566,do_spherical);
		}else {
			campus_566 = rv.createNetReplica("campus_3_566",baseCampus);
			campus_r_566 = (IRouter)campus_566.getChildByName("sub_campus_router");
		}
		ILink l_campus_566 = rv.createLink("l_campus_566");
		l_campus_566.createInterface(r_566.createInterface("if_campus_566"));
		l_campus_566.createInterface((IInterface)campus_r_566.getChildByName("if_stub"));

		INet campus_567 = null;
		IRouter campus_r_567 = null;
		if(null == baseCampus) {
			campus_567 = rv.createNet("campus_3_567");
			campus_r_567 = createCampus(campus_567,do_spherical);
		}else {
			campus_567 = rv.createNetReplica("campus_3_567",baseCampus);
			campus_r_567 = (IRouter)campus_567.getChildByName("sub_campus_router");
		}
		ILink l_campus_567 = rv.createLink("l_campus_567");
		l_campus_567.createInterface(r_567.createInterface("if_campus_567"));
		l_campus_567.createInterface((IInterface)campus_r_567.getChildByName("if_stub"));
		return rv;
	}
	public INet create_net_4(INet top, Map<Integer,IRouter> routers) {
		INet rv = top.createNet("net_4");
		if(do_spherical) rv.createShortestPath();;

		//create routers
		IRouter r_27 = rv.createRouter("r_27");
		routers.put(27,r_27);

		IRouter r_28 = rv.createRouter("r_28");
		routers.put(28,r_28);

		IRouter r_217 = rv.createRouter("r_217");
		routers.put(217,r_217);

		IRouter r_218 = rv.createRouter("r_218");
		routers.put(218,r_218);

		IRouter r_244 = rv.createRouter("r_244");
		routers.put(244,r_244);

		IRouter r_252 = rv.createRouter("r_252");
		routers.put(252,r_252);

		IRouter r_329 = rv.createRouter("r_329");
		routers.put(329,r_329);

		IRouter r_330 = rv.createRouter("r_330");
		routers.put(330,r_330);

		IRouter r_349 = rv.createRouter("r_349");
		routers.put(349,r_349);

		IRouter r_350 = rv.createRouter("r_350");
		routers.put(350,r_350);

		IRouter r_351 = rv.createRouter("r_351");
		routers.put(351,r_351);

		IRouter r_379 = rv.createRouter("r_379");
		routers.put(379,r_379);

		IRouter r_380 = rv.createRouter("r_380");
		routers.put(380,r_380);

		IRouter r_381 = rv.createRouter("r_381");
		routers.put(381,r_381);

		IRouter r_425 = rv.createRouter("r_425");
		routers.put(425,r_425);

		IRouter r_473 = rv.createRouter("r_473");
		routers.put(473,r_473);

		IRouter r_474 = rv.createRouter("r_474");
		routers.put(474,r_474);

		IRouter r_475 = rv.createRouter("r_475");
		routers.put(475,r_475);

		IRouter r_476 = rv.createRouter("r_476");
		routers.put(476,r_476);

		IRouter r_477 = rv.createRouter("r_477");
		routers.put(477,r_477);

		IRouter r_478 = rv.createRouter("r_478");
		routers.put(478,r_478);

		IRouter r_479 = rv.createRouter("r_479");
		routers.put(479,r_479);

		IRouter r_480 = rv.createRouter("r_480");
		routers.put(480,r_480);

		IRouter r_481 = rv.createRouter("r_481");
		routers.put(481,r_481);

		IRouter r_482 = rv.createRouter("r_482");
		routers.put(482,r_482);

		IRouter r_483 = rv.createRouter("r_483");
		routers.put(483,r_483);

		IRouter r_484 = rv.createRouter("r_484");
		routers.put(484,r_484);

		IRouter r_485 = rv.createRouter("r_485");
		routers.put(485,r_485);

		IRouter r_486 = rv.createRouter("r_486");
		routers.put(486,r_486);

		IRouter r_509 = rv.createRouter("r_509");
		routers.put(509,r_509);

		IRouter r_510 = rv.createRouter("r_510");
		routers.put(510,r_510);

		IRouter r_511 = rv.createRouter("r_511");
		routers.put(511,r_511);

		IRouter r_512 = rv.createRouter("r_512");
		routers.put(512,r_512);

		IRouter r_513 = rv.createRouter("r_513");
		routers.put(513,r_513);

		IRouter r_515 = rv.createRouter("r_515");
		routers.put(515,r_515);

		IRouter r_516 = rv.createRouter("r_516");
		routers.put(516,r_516);

		IRouter r_517 = rv.createRouter("r_517");
		routers.put(517,r_517);

		IRouter r_518 = rv.createRouter("r_518");
		routers.put(518,r_518);

		IRouter r_572 = rv.createRouter("r_572");
		routers.put(572,r_572);

		IRouter r_573 = rv.createRouter("r_573");
		routers.put(573,r_573);

		//create links
		ILink l_28_27 = rv.createLink("l_28_27");
		l_28_27.createInterface(routers.get(28).createInterface("if_27"));
		l_28_27.createInterface(routers.get(27).createInterface("if_28"));

		ILink l_330_28 = rv.createLink("l_330_28");
		l_330_28.createInterface(routers.get(330).createInterface("if_28"));
		l_330_28.createInterface(routers.get(28).createInterface("if_330"));

		ILink l_477_28 = rv.createLink("l_477_28");
		l_477_28.createInterface(routers.get(477).createInterface("if_28"));
		l_477_28.createInterface(routers.get(28).createInterface("if_477"));

		ILink l_478_28 = rv.createLink("l_478_28");
		l_478_28.createInterface(routers.get(478).createInterface("if_28"));
		l_478_28.createInterface(routers.get(28).createInterface("if_478"));

		ILink l_479_28 = rv.createLink("l_479_28");
		l_479_28.createInterface(routers.get(479).createInterface("if_28"));
		l_479_28.createInterface(routers.get(28).createInterface("if_479"));

		ILink l_486_28 = rv.createLink("l_486_28");
		l_486_28.createInterface(routers.get(486).createInterface("if_28"));
		l_486_28.createInterface(routers.get(28).createInterface("if_486"));

		ILink l_218_217 = rv.createLink("l_218_217");
		l_218_217.createInterface(routers.get(218).createInterface("if_217"));
		l_218_217.createInterface(routers.get(217).createInterface("if_218"));

		ILink l_244_218 = rv.createLink("l_244_218");
		l_244_218.createInterface(routers.get(244).createInterface("if_218"));
		l_244_218.createInterface(routers.get(218).createInterface("if_244"));

		ILink l_484_218 = rv.createLink("l_484_218");
		l_484_218.createInterface(routers.get(484).createInterface("if_218"));
		l_484_218.createInterface(routers.get(218).createInterface("if_484"));

		ILink l_252_244 = rv.createLink("l_252_244");
		l_252_244.createInterface(routers.get(252).createInterface("if_244"));
		l_252_244.createInterface(routers.get(244).createInterface("if_252"));

		ILink l_478_244 = rv.createLink("l_478_244");
		l_478_244.createInterface(routers.get(478).createInterface("if_244"));
		l_478_244.createInterface(routers.get(244).createInterface("if_478"));

		ILink l_481_244 = rv.createLink("l_481_244");
		l_481_244.createInterface(routers.get(481).createInterface("if_244"));
		l_481_244.createInterface(routers.get(244).createInterface("if_481"));

		ILink l_484_244 = rv.createLink("l_484_244");
		l_484_244.createInterface(routers.get(484).createInterface("if_244"));
		l_484_244.createInterface(routers.get(244).createInterface("if_484"));

		ILink l_485_244 = rv.createLink("l_485_244");
		l_485_244.createInterface(routers.get(485).createInterface("if_244"));
		l_485_244.createInterface(routers.get(244).createInterface("if_485"));

		ILink l_510_244 = rv.createLink("l_510_244");
		l_510_244.createInterface(routers.get(510).createInterface("if_244"));
		l_510_244.createInterface(routers.get(244).createInterface("if_510"));

		ILink l_511_244 = rv.createLink("l_511_244");
		l_511_244.createInterface(routers.get(511).createInterface("if_244"));
		l_511_244.createInterface(routers.get(244).createInterface("if_511"));

		ILink l_512_244 = rv.createLink("l_512_244");
		l_512_244.createInterface(routers.get(512).createInterface("if_244"));
		l_512_244.createInterface(routers.get(244).createInterface("if_512"));

		ILink l_482_252 = rv.createLink("l_482_252");
		l_482_252.createInterface(routers.get(482).createInterface("if_252"));
		l_482_252.createInterface(routers.get(252).createInterface("if_482"));

		ILink l_484_252 = rv.createLink("l_484_252");
		l_484_252.createInterface(routers.get(484).createInterface("if_252"));
		l_484_252.createInterface(routers.get(252).createInterface("if_484"));

		ILink l_330_329 = rv.createLink("l_330_329");
		l_330_329.createInterface(routers.get(330).createInterface("if_329"));
		l_330_329.createInterface(routers.get(329).createInterface("if_330"));

		ILink l_349_330 = rv.createLink("l_349_330");
		l_349_330.createInterface(routers.get(349).createInterface("if_330"));
		l_349_330.createInterface(routers.get(330).createInterface("if_349"));

		ILink l_425_330 = rv.createLink("l_425_330");
		l_425_330.createInterface(routers.get(425).createInterface("if_330"));
		l_425_330.createInterface(routers.get(330).createInterface("if_425"));

		ILink l_477_330 = rv.createLink("l_477_330");
		l_477_330.createInterface(routers.get(477).createInterface("if_330"));
		l_477_330.createInterface(routers.get(330).createInterface("if_477"));

		ILink l_479_330 = rv.createLink("l_479_330");
		l_479_330.createInterface(routers.get(479).createInterface("if_330"));
		l_479_330.createInterface(routers.get(330).createInterface("if_479"));

		ILink l_481_330 = rv.createLink("l_481_330");
		l_481_330.createInterface(routers.get(481).createInterface("if_330"));
		l_481_330.createInterface(routers.get(330).createInterface("if_481"));

		ILink l_482_330 = rv.createLink("l_482_330");
		l_482_330.createInterface(routers.get(482).createInterface("if_330"));
		l_482_330.createInterface(routers.get(330).createInterface("if_482"));

		ILink l_509_330 = rv.createLink("l_509_330");
		l_509_330.createInterface(routers.get(509).createInterface("if_330"));
		l_509_330.createInterface(routers.get(330).createInterface("if_509"));

		ILink l_572_330 = rv.createLink("l_572_330");
		l_572_330.createInterface(routers.get(572).createInterface("if_330"));
		l_572_330.createInterface(routers.get(330).createInterface("if_572"));

		ILink l_573_330 = rv.createLink("l_573_330");
		l_573_330.createInterface(routers.get(573).createInterface("if_330"));
		l_573_330.createInterface(routers.get(330).createInterface("if_573"));

		ILink l_350_349 = rv.createLink("l_350_349");
		l_350_349.createInterface(routers.get(350).createInterface("if_349"));
		l_350_349.createInterface(routers.get(349).createInterface("if_350"));

		ILink l_351_349 = rv.createLink("l_351_349");
		l_351_349.createInterface(routers.get(351).createInterface("if_349"));
		l_351_349.createInterface(routers.get(349).createInterface("if_351"));

		ILink l_380_379 = rv.createLink("l_380_379");
		l_380_379.createInterface(routers.get(380).createInterface("if_379"));
		l_380_379.createInterface(routers.get(379).createInterface("if_380"));

		ILink l_381_379 = rv.createLink("l_381_379");
		l_381_379.createInterface(routers.get(381).createInterface("if_379"));
		l_381_379.createInterface(routers.get(379).createInterface("if_381"));

		ILink l_473_380 = rv.createLink("l_473_380");
		l_473_380.createInterface(routers.get(473).createInterface("if_380"));
		l_473_380.createInterface(routers.get(380).createInterface("if_473"));

		ILink l_474_380 = rv.createLink("l_474_380");
		l_474_380.createInterface(routers.get(474).createInterface("if_380"));
		l_474_380.createInterface(routers.get(380).createInterface("if_474"));

		ILink l_475_380 = rv.createLink("l_475_380");
		l_475_380.createInterface(routers.get(475).createInterface("if_380"));
		l_475_380.createInterface(routers.get(380).createInterface("if_475"));

		ILink l_476_380 = rv.createLink("l_476_380");
		l_476_380.createInterface(routers.get(476).createInterface("if_380"));
		l_476_380.createInterface(routers.get(380).createInterface("if_476"));

		ILink l_477_380 = rv.createLink("l_477_380");
		l_477_380.createInterface(routers.get(477).createInterface("if_380"));
		l_477_380.createInterface(routers.get(380).createInterface("if_477"));

		ILink l_478_380 = rv.createLink("l_478_380");
		l_478_380.createInterface(routers.get(478).createInterface("if_380"));
		l_478_380.createInterface(routers.get(380).createInterface("if_478"));

		ILink l_479_380 = rv.createLink("l_479_380");
		l_479_380.createInterface(routers.get(479).createInterface("if_380"));
		l_479_380.createInterface(routers.get(380).createInterface("if_479"));

		ILink l_480_380 = rv.createLink("l_480_380");
		l_480_380.createInterface(routers.get(480).createInterface("if_380"));
		l_480_380.createInterface(routers.get(380).createInterface("if_480"));

		ILink l_473_381 = rv.createLink("l_473_381");
		l_473_381.createInterface(routers.get(473).createInterface("if_381"));
		l_473_381.createInterface(routers.get(381).createInterface("if_473"));

		ILink l_474_381 = rv.createLink("l_474_381");
		l_474_381.createInterface(routers.get(474).createInterface("if_381"));
		l_474_381.createInterface(routers.get(381).createInterface("if_474"));

		ILink l_475_381 = rv.createLink("l_475_381");
		l_475_381.createInterface(routers.get(475).createInterface("if_381"));
		l_475_381.createInterface(routers.get(381).createInterface("if_475"));

		ILink l_476_381 = rv.createLink("l_476_381");
		l_476_381.createInterface(routers.get(476).createInterface("if_381"));
		l_476_381.createInterface(routers.get(381).createInterface("if_476"));

		ILink l_477_381 = rv.createLink("l_477_381");
		l_477_381.createInterface(routers.get(477).createInterface("if_381"));
		l_477_381.createInterface(routers.get(381).createInterface("if_477"));

		ILink l_478_381 = rv.createLink("l_478_381");
		l_478_381.createInterface(routers.get(478).createInterface("if_381"));
		l_478_381.createInterface(routers.get(381).createInterface("if_478"));

		ILink l_479_381 = rv.createLink("l_479_381");
		l_479_381.createInterface(routers.get(479).createInterface("if_381"));
		l_479_381.createInterface(routers.get(381).createInterface("if_479"));

		ILink l_480_381 = rv.createLink("l_480_381");
		l_480_381.createInterface(routers.get(480).createInterface("if_381"));
		l_480_381.createInterface(routers.get(381).createInterface("if_480"));

		ILink l_478_477 = rv.createLink("l_478_477");
		l_478_477.createInterface(routers.get(478).createInterface("if_477"));
		l_478_477.createInterface(routers.get(477).createInterface("if_478"));

		ILink l_481_477 = rv.createLink("l_481_477");
		l_481_477.createInterface(routers.get(481).createInterface("if_477"));
		l_481_477.createInterface(routers.get(477).createInterface("if_481"));

		ILink l_482_477 = rv.createLink("l_482_477");
		l_482_477.createInterface(routers.get(482).createInterface("if_477"));
		l_482_477.createInterface(routers.get(477).createInterface("if_482"));

		ILink l_483_477 = rv.createLink("l_483_477");
		l_483_477.createInterface(routers.get(483).createInterface("if_477"));
		l_483_477.createInterface(routers.get(477).createInterface("if_483"));

		ILink l_484_478 = rv.createLink("l_484_478");
		l_484_478.createInterface(routers.get(484).createInterface("if_478"));
		l_484_478.createInterface(routers.get(478).createInterface("if_484"));

		ILink l_485_478 = rv.createLink("l_485_478");
		l_485_478.createInterface(routers.get(485).createInterface("if_478"));
		l_485_478.createInterface(routers.get(478).createInterface("if_485"));

		ILink l_481_479 = rv.createLink("l_481_479");
		l_481_479.createInterface(routers.get(481).createInterface("if_479"));
		l_481_479.createInterface(routers.get(479).createInterface("if_481"));

		ILink l_482_479 = rv.createLink("l_482_479");
		l_482_479.createInterface(routers.get(482).createInterface("if_479"));
		l_482_479.createInterface(routers.get(479).createInterface("if_482"));

		ILink l_509_481 = rv.createLink("l_509_481");
		l_509_481.createInterface(routers.get(509).createInterface("if_481"));
		l_509_481.createInterface(routers.get(481).createInterface("if_509"));

		ILink l_509_482 = rv.createLink("l_509_482");
		l_509_482.createInterface(routers.get(509).createInterface("if_482"));
		l_509_482.createInterface(routers.get(482).createInterface("if_509"));

		ILink l_517_483 = rv.createLink("l_517_483");
		l_517_483.createInterface(routers.get(517).createInterface("if_483"));
		l_517_483.createInterface(routers.get(483).createInterface("if_517"));

		ILink l_518_483 = rv.createLink("l_518_483");
		l_518_483.createInterface(routers.get(518).createInterface("if_483"));
		l_518_483.createInterface(routers.get(483).createInterface("if_518"));

		ILink l_485_484 = rv.createLink("l_485_484");
		l_485_484.createInterface(routers.get(485).createInterface("if_484"));
		l_485_484.createInterface(routers.get(484).createInterface("if_485"));

		ILink l_515_484 = rv.createLink("l_515_484");
		l_515_484.createInterface(routers.get(515).createInterface("if_484"));
		l_515_484.createInterface(routers.get(484).createInterface("if_515"));

		ILink l_517_485 = rv.createLink("l_517_485");
		l_517_485.createInterface(routers.get(517).createInterface("if_485"));
		l_517_485.createInterface(routers.get(485).createInterface("if_517"));

		ILink l_518_485 = rv.createLink("l_518_485");
		l_518_485.createInterface(routers.get(518).createInterface("if_485"));
		l_518_485.createInterface(routers.get(485).createInterface("if_518"));

		ILink l_510_509 = rv.createLink("l_510_509");
		l_510_509.createInterface(routers.get(510).createInterface("if_509"));
		l_510_509.createInterface(routers.get(509).createInterface("if_510"));

		ILink l_513_509 = rv.createLink("l_513_509");
		l_513_509.createInterface(routers.get(513).createInterface("if_509"));
		l_513_509.createInterface(routers.get(509).createInterface("if_513"));

		ILink l_511_510 = rv.createLink("l_511_510");
		l_511_510.createInterface(routers.get(511).createInterface("if_510"));
		l_511_510.createInterface(routers.get(510).createInterface("if_511"));

		ILink l_513_511 = rv.createLink("l_513_511");
		l_513_511.createInterface(routers.get(513).createInterface("if_511"));
		l_513_511.createInterface(routers.get(511).createInterface("if_513"));

		ILink l_516_515 = rv.createLink("l_516_515");
		l_516_515.createInterface(routers.get(516).createInterface("if_515"));
		l_516_515.createInterface(routers.get(515).createInterface("if_516"));

		//attach campus networks

		INet campus_27 = null;
		IRouter campus_r_27 = null;
		if(null == baseCampus) {
			campus_27 = rv.createNet("campus_4_27");
			campus_r_27 = createCampus(campus_27,do_spherical);
		}else {
			campus_27 = rv.createNetReplica("campus_4_27",baseCampus);
			campus_r_27 = (IRouter)campus_27.getChildByName("sub_campus_router");
		}
		ILink l_campus_27 = rv.createLink("l_campus_27");
		l_campus_27.createInterface(r_27.createInterface("if_campus_27"));
		l_campus_27.createInterface((IInterface)campus_r_27.getChildByName("if_stub"));

		INet campus_217 = null;
		IRouter campus_r_217 = null;
		if(null == baseCampus) {
			campus_217 = rv.createNet("campus_4_217");
			campus_r_217 = createCampus(campus_217,do_spherical);
		}else {
			campus_217 = rv.createNetReplica("campus_4_217",baseCampus);
			campus_r_217 = (IRouter)campus_217.getChildByName("sub_campus_router");
		}
		ILink l_campus_217 = rv.createLink("l_campus_217");
		l_campus_217.createInterface(r_217.createInterface("if_campus_217"));
		l_campus_217.createInterface((IInterface)campus_r_217.getChildByName("if_stub"));

		INet campus_350 = null;
		IRouter campus_r_350 = null;
		if(null == baseCampus) {
			campus_350 = rv.createNet("campus_4_350");
			campus_r_350 = createCampus(campus_350,do_spherical);
		}else {
			campus_350 = rv.createNetReplica("campus_4_350",baseCampus);
			campus_r_350 = (IRouter)campus_350.getChildByName("sub_campus_router");
		}
		ILink l_campus_350 = rv.createLink("l_campus_350");
		l_campus_350.createInterface(r_350.createInterface("if_campus_350"));
		l_campus_350.createInterface((IInterface)campus_r_350.getChildByName("if_stub"));

		INet campus_351 = null;
		IRouter campus_r_351 = null;
		if(null == baseCampus) {
			campus_351 = rv.createNet("campus_4_351");
			campus_r_351 = createCampus(campus_351,do_spherical);
		}else {
			campus_351 = rv.createNetReplica("campus_4_351",baseCampus);
			campus_r_351 = (IRouter)campus_351.getChildByName("sub_campus_router");
		}
		ILink l_campus_351 = rv.createLink("l_campus_351");
		l_campus_351.createInterface(r_351.createInterface("if_campus_351"));
		l_campus_351.createInterface((IInterface)campus_r_351.getChildByName("if_stub"));

		INet campus_486 = null;
		IRouter campus_r_486 = null;
		if(null == baseCampus) {
			campus_486 = rv.createNet("campus_4_486");
			campus_r_486 = createCampus(campus_486,do_spherical);
		}else {
			campus_486 = rv.createNetReplica("campus_4_486",baseCampus);
			campus_r_486 = (IRouter)campus_486.getChildByName("sub_campus_router");
		}
		ILink l_campus_486 = rv.createLink("l_campus_486");
		l_campus_486.createInterface(r_486.createInterface("if_campus_486"));
		l_campus_486.createInterface((IInterface)campus_r_486.getChildByName("if_stub"));

		INet campus_516 = null;
		IRouter campus_r_516 = null;
		if(null == baseCampus) {
			campus_516 = rv.createNet("campus_4_516");
			campus_r_516 = createCampus(campus_516,do_spherical);
		}else {
			campus_516 = rv.createNetReplica("campus_4_516",baseCampus);
			campus_r_516 = (IRouter)campus_516.getChildByName("sub_campus_router");
		}
		ILink l_campus_516 = rv.createLink("l_campus_516");
		l_campus_516.createInterface(r_516.createInterface("if_campus_516"));
		l_campus_516.createInterface((IInterface)campus_r_516.getChildByName("if_stub"));
		return rv;
	}
	public INet create_net_5(INet top, Map<Integer,IRouter> routers) {
		INet rv = top.createNet("net_5");
		if(do_spherical) rv.createShortestPath();;

		//create routers
		IRouter r_3 = rv.createRouter("r_3");
		routers.put(3,r_3);

		IRouter r_4 = rv.createRouter("r_4");
		routers.put(4,r_4);

		IRouter r_15 = rv.createRouter("r_15");
		routers.put(15,r_15);

		IRouter r_16 = rv.createRouter("r_16");
		routers.put(16,r_16);

		IRouter r_43 = rv.createRouter("r_43");
		routers.put(43,r_43);

		IRouter r_86 = rv.createRouter("r_86");
		routers.put(86,r_86);

		IRouter r_110 = rv.createRouter("r_110");
		routers.put(110,r_110);

		IRouter r_113 = rv.createRouter("r_113");
		routers.put(113,r_113);

		IRouter r_137 = rv.createRouter("r_137");
		routers.put(137,r_137);

		IRouter r_138 = rv.createRouter("r_138");
		routers.put(138,r_138);

		IRouter r_148 = rv.createRouter("r_148");
		routers.put(148,r_148);

		IRouter r_216 = rv.createRouter("r_216");
		routers.put(216,r_216);

		IRouter r_229 = rv.createRouter("r_229");
		routers.put(229,r_229);

		IRouter r_230 = rv.createRouter("r_230");
		routers.put(230,r_230);

		IRouter r_231 = rv.createRouter("r_231");
		routers.put(231,r_231);

		IRouter r_249 = rv.createRouter("r_249");
		routers.put(249,r_249);

		IRouter r_250 = rv.createRouter("r_250");
		routers.put(250,r_250);

		IRouter r_251 = rv.createRouter("r_251");
		routers.put(251,r_251);

		IRouter r_253 = rv.createRouter("r_253");
		routers.put(253,r_253);

		IRouter r_254 = rv.createRouter("r_254");
		routers.put(254,r_254);

		IRouter r_255 = rv.createRouter("r_255");
		routers.put(255,r_255);

		IRouter r_256 = rv.createRouter("r_256");
		routers.put(256,r_256);

		IRouter r_257 = rv.createRouter("r_257");
		routers.put(257,r_257);

		IRouter r_262 = rv.createRouter("r_262");
		routers.put(262,r_262);

		IRouter r_297 = rv.createRouter("r_297");
		routers.put(297,r_297);

		IRouter r_319 = rv.createRouter("r_319");
		routers.put(319,r_319);

		IRouter r_322 = rv.createRouter("r_322");
		routers.put(322,r_322);

		IRouter r_323 = rv.createRouter("r_323");
		routers.put(323,r_323);

		IRouter r_385 = rv.createRouter("r_385");
		routers.put(385,r_385);

		IRouter r_463 = rv.createRouter("r_463");
		routers.put(463,r_463);

		IRouter r_502 = rv.createRouter("r_502");
		routers.put(502,r_502);

		IRouter r_506 = rv.createRouter("r_506");
		routers.put(506,r_506);

		IRouter r_507 = rv.createRouter("r_507");
		routers.put(507,r_507);

		IRouter r_508 = rv.createRouter("r_508");
		routers.put(508,r_508);

		IRouter r_582 = rv.createRouter("r_582");
		routers.put(582,r_582);

		IRouter r_615 = rv.createRouter("r_615");
		routers.put(615,r_615);

		IRouter r_616 = rv.createRouter("r_616");
		routers.put(616,r_616);

		IRouter r_626 = rv.createRouter("r_626");
		routers.put(626,r_626);

		IRouter r_631 = rv.createRouter("r_631");
		routers.put(631,r_631);

		IRouter r_632 = rv.createRouter("r_632");
		routers.put(632,r_632);

		//create links
		ILink l_4_3 = rv.createLink("l_4_3");
		l_4_3.createInterface(routers.get(4).createInterface("if_3"));
		l_4_3.createInterface(routers.get(3).createInterface("if_4"));

		ILink l_230_4 = rv.createLink("l_230_4");
		l_230_4.createInterface(routers.get(230).createInterface("if_4"));
		l_230_4.createInterface(routers.get(4).createInterface("if_230"));

		ILink l_231_4 = rv.createLink("l_231_4");
		l_231_4.createInterface(routers.get(231).createInterface("if_4"));
		l_231_4.createInterface(routers.get(4).createInterface("if_231"));

		ILink l_385_4 = rv.createLink("l_385_4");
		l_385_4.createInterface(routers.get(385).createInterface("if_4"));
		l_385_4.createInterface(routers.get(4).createInterface("if_385"));

		ILink l_16_15 = rv.createLink("l_16_15");
		l_16_15.createInterface(routers.get(16).createInterface("if_15"));
		l_16_15.createInterface(routers.get(15).createInterface("if_16"));

		ILink l_86_16 = rv.createLink("l_86_16");
		l_86_16.createInterface(routers.get(86).createInterface("if_16"));
		l_86_16.createInterface(routers.get(16).createInterface("if_86"));

		ILink l_110_16 = rv.createLink("l_110_16");
		l_110_16.createInterface(routers.get(110).createInterface("if_16"));
		l_110_16.createInterface(routers.get(16).createInterface("if_110"));

		ILink l_113_16 = rv.createLink("l_113_16");
		l_113_16.createInterface(routers.get(113).createInterface("if_16"));
		l_113_16.createInterface(routers.get(16).createInterface("if_113"));

		ILink l_137_16 = rv.createLink("l_137_16");
		l_137_16.createInterface(routers.get(137).createInterface("if_16"));
		l_137_16.createInterface(routers.get(16).createInterface("if_137"));

		ILink l_148_16 = rv.createLink("l_148_16");
		l_148_16.createInterface(routers.get(148).createInterface("if_16"));
		l_148_16.createInterface(routers.get(16).createInterface("if_148"));

		ILink l_262_16 = rv.createLink("l_262_16");
		l_262_16.createInterface(routers.get(262).createInterface("if_16"));
		l_262_16.createInterface(routers.get(16).createInterface("if_262"));

		ILink l_297_16 = rv.createLink("l_297_16");
		l_297_16.createInterface(routers.get(297).createInterface("if_16"));
		l_297_16.createInterface(routers.get(16).createInterface("if_297"));

		ILink l_502_16 = rv.createLink("l_502_16");
		l_502_16.createInterface(routers.get(502).createInterface("if_16"));
		l_502_16.createInterface(routers.get(16).createInterface("if_502"));

		ILink l_506_16 = rv.createLink("l_506_16");
		l_506_16.createInterface(routers.get(506).createInterface("if_16"));
		l_506_16.createInterface(routers.get(16).createInterface("if_506"));

		ILink l_507_16 = rv.createLink("l_507_16");
		l_507_16.createInterface(routers.get(507).createInterface("if_16"));
		l_507_16.createInterface(routers.get(16).createInterface("if_507"));

		ILink l_582_16 = rv.createLink("l_582_16");
		l_582_16.createInterface(routers.get(582).createInterface("if_16"));
		l_582_16.createInterface(routers.get(16).createInterface("if_582"));

		ILink l_626_16 = rv.createLink("l_626_16");
		l_626_16.createInterface(routers.get(626).createInterface("if_16"));
		l_626_16.createInterface(routers.get(16).createInterface("if_626"));

		ILink l_216_43 = rv.createLink("l_216_43");
		l_216_43.createInterface(routers.get(216).createInterface("if_43"));
		l_216_43.createInterface(routers.get(43).createInterface("if_216"));

		ILink l_138_137 = rv.createLink("l_138_137");
		l_138_137.createInterface(routers.get(138).createInterface("if_137"));
		l_138_137.createInterface(routers.get(137).createInterface("if_138"));

		ILink l_148_138 = rv.createLink("l_148_138");
		l_148_138.createInterface(routers.get(148).createInterface("if_138"));
		l_148_138.createInterface(routers.get(138).createInterface("if_148"));

		ILink l_262_138 = rv.createLink("l_262_138");
		l_262_138.createInterface(routers.get(262).createInterface("if_138"));
		l_262_138.createInterface(routers.get(138).createInterface("if_262"));

		ILink l_582_138 = rv.createLink("l_582_138");
		l_582_138.createInterface(routers.get(582).createInterface("if_138"));
		l_582_138.createInterface(routers.get(138).createInterface("if_582"));

		ILink l_626_138 = rv.createLink("l_626_138");
		l_626_138.createInterface(routers.get(626).createInterface("if_138"));
		l_626_138.createInterface(routers.get(138).createInterface("if_626"));

		ILink l_230_229 = rv.createLink("l_230_229");
		l_230_229.createInterface(routers.get(230).createInterface("if_229"));
		l_230_229.createInterface(routers.get(229).createInterface("if_230"));

		ILink l_231_229 = rv.createLink("l_231_229");
		l_231_229.createInterface(routers.get(231).createInterface("if_229"));
		l_231_229.createInterface(routers.get(229).createInterface("if_231"));

		ILink l_319_230 = rv.createLink("l_319_230");
		l_319_230.createInterface(routers.get(319).createInterface("if_230"));
		l_319_230.createInterface(routers.get(230).createInterface("if_319"));

		ILink l_322_230 = rv.createLink("l_322_230");
		l_322_230.createInterface(routers.get(322).createInterface("if_230"));
		l_322_230.createInterface(routers.get(230).createInterface("if_322"));

		ILink l_323_230 = rv.createLink("l_323_230");
		l_323_230.createInterface(routers.get(323).createInterface("if_230"));
		l_323_230.createInterface(routers.get(230).createInterface("if_323"));

		ILink l_616_230 = rv.createLink("l_616_230");
		l_616_230.createInterface(routers.get(616).createInterface("if_230"));
		l_616_230.createInterface(routers.get(230).createInterface("if_616"));

		ILink l_631_230 = rv.createLink("l_631_230");
		l_631_230.createInterface(routers.get(631).createInterface("if_230"));
		l_631_230.createInterface(routers.get(230).createInterface("if_631"));

		ILink l_632_230 = rv.createLink("l_632_230");
		l_632_230.createInterface(routers.get(632).createInterface("if_230"));
		l_632_230.createInterface(routers.get(230).createInterface("if_632"));

		ILink l_322_231 = rv.createLink("l_322_231");
		l_322_231.createInterface(routers.get(322).createInterface("if_231"));
		l_322_231.createInterface(routers.get(231).createInterface("if_322"));

		ILink l_323_231 = rv.createLink("l_323_231");
		l_323_231.createInterface(routers.get(323).createInterface("if_231"));
		l_323_231.createInterface(routers.get(231).createInterface("if_323"));

		ILink l_616_231 = rv.createLink("l_616_231");
		l_616_231.createInterface(routers.get(616).createInterface("if_231"));
		l_616_231.createInterface(routers.get(231).createInterface("if_616"));

		ILink l_631_231 = rv.createLink("l_631_231");
		l_631_231.createInterface(routers.get(631).createInterface("if_231"));
		l_631_231.createInterface(routers.get(231).createInterface("if_631"));

		ILink l_632_231 = rv.createLink("l_632_231");
		l_632_231.createInterface(routers.get(632).createInterface("if_231"));
		l_632_231.createInterface(routers.get(231).createInterface("if_632"));

		ILink l_250_249 = rv.createLink("l_250_249");
		l_250_249.createInterface(routers.get(250).createInterface("if_249"));
		l_250_249.createInterface(routers.get(249).createInterface("if_250"));

		ILink l_251_249 = rv.createLink("l_251_249");
		l_251_249.createInterface(routers.get(251).createInterface("if_249"));
		l_251_249.createInterface(routers.get(249).createInterface("if_251"));

		ILink l_253_249 = rv.createLink("l_253_249");
		l_253_249.createInterface(routers.get(253).createInterface("if_249"));
		l_253_249.createInterface(routers.get(249).createInterface("if_253"));

		ILink l_254_249 = rv.createLink("l_254_249");
		l_254_249.createInterface(routers.get(254).createInterface("if_249"));
		l_254_249.createInterface(routers.get(249).createInterface("if_254"));

		ILink l_255_249 = rv.createLink("l_255_249");
		l_255_249.createInterface(routers.get(255).createInterface("if_249"));
		l_255_249.createInterface(routers.get(249).createInterface("if_255"));

		ILink l_256_249 = rv.createLink("l_256_249");
		l_256_249.createInterface(routers.get(256).createInterface("if_249"));
		l_256_249.createInterface(routers.get(249).createInterface("if_256"));

		ILink l_257_249 = rv.createLink("l_257_249");
		l_257_249.createInterface(routers.get(257).createInterface("if_249"));
		l_257_249.createInterface(routers.get(249).createInterface("if_257"));

		ILink l_508_251 = rv.createLink("l_508_251");
		l_508_251.createInterface(routers.get(508).createInterface("if_251"));
		l_508_251.createInterface(routers.get(251).createInterface("if_508"));

		ILink l_463_253 = rv.createLink("l_463_253");
		l_463_253.createInterface(routers.get(463).createInterface("if_253"));
		l_463_253.createInterface(routers.get(253).createInterface("if_463"));

		ILink l_502_253 = rv.createLink("l_502_253");
		l_502_253.createInterface(routers.get(502).createInterface("if_253"));
		l_502_253.createInterface(routers.get(253).createInterface("if_502"));

		ILink l_506_253 = rv.createLink("l_506_253");
		l_506_253.createInterface(routers.get(506).createInterface("if_253"));
		l_506_253.createInterface(routers.get(253).createInterface("if_506"));

		ILink l_507_253 = rv.createLink("l_507_253");
		l_507_253.createInterface(routers.get(507).createInterface("if_253"));
		l_507_253.createInterface(routers.get(253).createInterface("if_507"));

		ILink l_508_253 = rv.createLink("l_508_253");
		l_508_253.createInterface(routers.get(508).createInterface("if_253"));
		l_508_253.createInterface(routers.get(253).createInterface("if_508"));

		ILink l_616_615 = rv.createLink("l_616_615");
		l_616_615.createInterface(routers.get(616).createInterface("if_615"));
		l_616_615.createInterface(routers.get(615).createInterface("if_616"));

		//attach campus networks

		INet campus_3 = null;
		IRouter campus_r_3 = null;
		if(null == baseCampus) {
			campus_3 = rv.createNet("campus_5_3");
			campus_r_3 = createCampus(campus_3,do_spherical);
		}else {
			campus_3 = rv.createNetReplica("campus_5_3",baseCampus);
			campus_r_3 = (IRouter)campus_3.getChildByName("sub_campus_router");
		}
		ILink l_campus_3 = rv.createLink("l_campus_3");
		l_campus_3.createInterface(r_3.createInterface("if_campus_3"));
		l_campus_3.createInterface((IInterface)campus_r_3.getChildByName("if_stub"));

		INet campus_15 = null;
		IRouter campus_r_15 = null;
		if(null == baseCampus) {
			campus_15 = rv.createNet("campus_5_15");
			campus_r_15 = createCampus(campus_15,do_spherical);
		}else {
			campus_15 = rv.createNetReplica("campus_5_15",baseCampus);
			campus_r_15 = (IRouter)campus_15.getChildByName("sub_campus_router");
		}
		ILink l_campus_15 = rv.createLink("l_campus_15");
		l_campus_15.createInterface(r_15.createInterface("if_campus_15"));
		l_campus_15.createInterface((IInterface)campus_r_15.getChildByName("if_stub"));

		INet campus_86 = null;
		IRouter campus_r_86 = null;
		if(null == baseCampus) {
			campus_86 = rv.createNet("campus_5_86");
			campus_r_86 = createCampus(campus_86,do_spherical);
		}else {
			campus_86 = rv.createNetReplica("campus_5_86",baseCampus);
			campus_r_86 = (IRouter)campus_86.getChildByName("sub_campus_router");
		}
		ILink l_campus_86 = rv.createLink("l_campus_86");
		l_campus_86.createInterface(r_86.createInterface("if_campus_86"));
		l_campus_86.createInterface((IInterface)campus_r_86.getChildByName("if_stub"));

		INet campus_110 = null;
		IRouter campus_r_110 = null;
		if(null == baseCampus) {
			campus_110 = rv.createNet("campus_5_110");
			campus_r_110 = createCampus(campus_110,do_spherical);
		}else {
			campus_110 = rv.createNetReplica("campus_5_110",baseCampus);
			campus_r_110 = (IRouter)campus_110.getChildByName("sub_campus_router");
		}
		ILink l_campus_110 = rv.createLink("l_campus_110");
		l_campus_110.createInterface(r_110.createInterface("if_campus_110"));
		l_campus_110.createInterface((IInterface)campus_r_110.getChildByName("if_stub"));

		INet campus_113 = null;
		IRouter campus_r_113 = null;
		if(null == baseCampus) {
			campus_113 = rv.createNet("campus_5_113");
			campus_r_113 = createCampus(campus_113,do_spherical);
		}else {
			campus_113 = rv.createNetReplica("campus_5_113",baseCampus);
			campus_r_113 = (IRouter)campus_113.getChildByName("sub_campus_router");
		}
		ILink l_campus_113 = rv.createLink("l_campus_113");
		l_campus_113.createInterface(r_113.createInterface("if_campus_113"));
		l_campus_113.createInterface((IInterface)campus_r_113.getChildByName("if_stub"));

		INet campus_216 = null;
		IRouter campus_r_216 = null;
		if(null == baseCampus) {
			campus_216 = rv.createNet("campus_5_216");
			campus_r_216 = createCampus(campus_216,do_spherical);
		}else {
			campus_216 = rv.createNetReplica("campus_5_216",baseCampus);
			campus_r_216 = (IRouter)campus_216.getChildByName("sub_campus_router");
		}
		ILink l_campus_216 = rv.createLink("l_campus_216");
		l_campus_216.createInterface(r_216.createInterface("if_campus_216"));
		l_campus_216.createInterface((IInterface)campus_r_216.getChildByName("if_stub"));

		INet campus_250 = null;
		IRouter campus_r_250 = null;
		if(null == baseCampus) {
			campus_250 = rv.createNet("campus_5_250");
			campus_r_250 = createCampus(campus_250,do_spherical);
		}else {
			campus_250 = rv.createNetReplica("campus_5_250",baseCampus);
			campus_r_250 = (IRouter)campus_250.getChildByName("sub_campus_router");
		}
		ILink l_campus_250 = rv.createLink("l_campus_250");
		l_campus_250.createInterface(r_250.createInterface("if_campus_250"));
		l_campus_250.createInterface((IInterface)campus_r_250.getChildByName("if_stub"));

		INet campus_254 = null;
		IRouter campus_r_254 = null;
		if(null == baseCampus) {
			campus_254 = rv.createNet("campus_5_254");
			campus_r_254 = createCampus(campus_254,do_spherical);
		}else {
			campus_254 = rv.createNetReplica("campus_5_254",baseCampus);
			campus_r_254 = (IRouter)campus_254.getChildByName("sub_campus_router");
		}
		ILink l_campus_254 = rv.createLink("l_campus_254");
		l_campus_254.createInterface(r_254.createInterface("if_campus_254"));
		l_campus_254.createInterface((IInterface)campus_r_254.getChildByName("if_stub"));

		INet campus_255 = null;
		IRouter campus_r_255 = null;
		if(null == baseCampus) {
			campus_255 = rv.createNet("campus_5_255");
			campus_r_255 = createCampus(campus_255,do_spherical);
		}else {
			campus_255 = rv.createNetReplica("campus_5_255",baseCampus);
			campus_r_255 = (IRouter)campus_255.getChildByName("sub_campus_router");
		}
		ILink l_campus_255 = rv.createLink("l_campus_255");
		l_campus_255.createInterface(r_255.createInterface("if_campus_255"));
		l_campus_255.createInterface((IInterface)campus_r_255.getChildByName("if_stub"));

		INet campus_256 = null;
		IRouter campus_r_256 = null;
		if(null == baseCampus) {
			campus_256 = rv.createNet("campus_5_256");
			campus_r_256 = createCampus(campus_256,do_spherical);
		}else {
			campus_256 = rv.createNetReplica("campus_5_256",baseCampus);
			campus_r_256 = (IRouter)campus_256.getChildByName("sub_campus_router");
		}
		ILink l_campus_256 = rv.createLink("l_campus_256");
		l_campus_256.createInterface(r_256.createInterface("if_campus_256"));
		l_campus_256.createInterface((IInterface)campus_r_256.getChildByName("if_stub"));

		INet campus_257 = null;
		IRouter campus_r_257 = null;
		if(null == baseCampus) {
			campus_257 = rv.createNet("campus_5_257");
			campus_r_257 = createCampus(campus_257,do_spherical);
		}else {
			campus_257 = rv.createNetReplica("campus_5_257",baseCampus);
			campus_r_257 = (IRouter)campus_257.getChildByName("sub_campus_router");
		}
		ILink l_campus_257 = rv.createLink("l_campus_257");
		l_campus_257.createInterface(r_257.createInterface("if_campus_257"));
		l_campus_257.createInterface((IInterface)campus_r_257.getChildByName("if_stub"));

		INet campus_297 = null;
		IRouter campus_r_297 = null;
		if(null == baseCampus) {
			campus_297 = rv.createNet("campus_5_297");
			campus_r_297 = createCampus(campus_297,do_spherical);
		}else {
			campus_297 = rv.createNetReplica("campus_5_297",baseCampus);
			campus_r_297 = (IRouter)campus_297.getChildByName("sub_campus_router");
		}
		ILink l_campus_297 = rv.createLink("l_campus_297");
		l_campus_297.createInterface(r_297.createInterface("if_campus_297"));
		l_campus_297.createInterface((IInterface)campus_r_297.getChildByName("if_stub"));

		INet campus_319 = null;
		IRouter campus_r_319 = null;
		if(null == baseCampus) {
			campus_319 = rv.createNet("campus_5_319");
			campus_r_319 = createCampus(campus_319,do_spherical);
		}else {
			campus_319 = rv.createNetReplica("campus_5_319",baseCampus);
			campus_r_319 = (IRouter)campus_319.getChildByName("sub_campus_router");
		}
		ILink l_campus_319 = rv.createLink("l_campus_319");
		l_campus_319.createInterface(r_319.createInterface("if_campus_319"));
		l_campus_319.createInterface((IInterface)campus_r_319.getChildByName("if_stub"));

		INet campus_385 = null;
		IRouter campus_r_385 = null;
		if(null == baseCampus) {
			campus_385 = rv.createNet("campus_5_385");
			campus_r_385 = createCampus(campus_385,do_spherical);
		}else {
			campus_385 = rv.createNetReplica("campus_5_385",baseCampus);
			campus_r_385 = (IRouter)campus_385.getChildByName("sub_campus_router");
		}
		ILink l_campus_385 = rv.createLink("l_campus_385");
		l_campus_385.createInterface(r_385.createInterface("if_campus_385"));
		l_campus_385.createInterface((IInterface)campus_r_385.getChildByName("if_stub"));

		INet campus_615 = null;
		IRouter campus_r_615 = null;
		if(null == baseCampus) {
			campus_615 = rv.createNet("campus_5_615");
			campus_r_615 = createCampus(campus_615,do_spherical);
		}else {
			campus_615 = rv.createNetReplica("campus_5_615",baseCampus);
			campus_r_615 = (IRouter)campus_615.getChildByName("sub_campus_router");
		}
		ILink l_campus_615 = rv.createLink("l_campus_615");
		l_campus_615.createInterface(r_615.createInterface("if_campus_615"));
		l_campus_615.createInterface((IInterface)campus_r_615.getChildByName("if_stub"));
		return rv;
	}
	public INet create_net_6(INet top, Map<Integer,IRouter> routers) {
		INet rv = top.createNet("net_6");
		if(do_spherical) rv.createShortestPath();;

		//create routers
		IRouter r_7 = rv.createRouter("r_7");
		routers.put(7,r_7);

		IRouter r_8 = rv.createRouter("r_8");
		routers.put(8,r_8);

		IRouter r_9 = rv.createRouter("r_9");
		routers.put(9,r_9);

		IRouter r_49 = rv.createRouter("r_49");
		routers.put(49,r_49);

		IRouter r_50 = rv.createRouter("r_50");
		routers.put(50,r_50);

		IRouter r_54 = rv.createRouter("r_54");
		routers.put(54,r_54);

		IRouter r_55 = rv.createRouter("r_55");
		routers.put(55,r_55);

		IRouter r_67 = rv.createRouter("r_67");
		routers.put(67,r_67);

		IRouter r_68 = rv.createRouter("r_68");
		routers.put(68,r_68);

		IRouter r_109 = rv.createRouter("r_109");
		routers.put(109,r_109);

		IRouter r_150 = rv.createRouter("r_150");
		routers.put(150,r_150);

		IRouter r_164 = rv.createRouter("r_164");
		routers.put(164,r_164);

		IRouter r_165 = rv.createRouter("r_165");
		routers.put(165,r_165);

		IRouter r_166 = rv.createRouter("r_166");
		routers.put(166,r_166);

		IRouter r_167 = rv.createRouter("r_167");
		routers.put(167,r_167);

		IRouter r_168 = rv.createRouter("r_168");
		routers.put(168,r_168);

		IRouter r_169 = rv.createRouter("r_169");
		routers.put(169,r_169);

		IRouter r_170 = rv.createRouter("r_170");
		routers.put(170,r_170);

		IRouter r_172 = rv.createRouter("r_172");
		routers.put(172,r_172);

		IRouter r_174 = rv.createRouter("r_174");
		routers.put(174,r_174);

		IRouter r_175 = rv.createRouter("r_175");
		routers.put(175,r_175);

		IRouter r_189 = rv.createRouter("r_189");
		routers.put(189,r_189);

		IRouter r_195 = rv.createRouter("r_195");
		routers.put(195,r_195);

		IRouter r_196 = rv.createRouter("r_196");
		routers.put(196,r_196);

		IRouter r_210 = rv.createRouter("r_210");
		routers.put(210,r_210);

		IRouter r_211 = rv.createRouter("r_211");
		routers.put(211,r_211);

		IRouter r_301 = rv.createRouter("r_301");
		routers.put(301,r_301);

		IRouter r_306 = rv.createRouter("r_306");
		routers.put(306,r_306);

		IRouter r_314 = rv.createRouter("r_314");
		routers.put(314,r_314);

		IRouter r_320 = rv.createRouter("r_320");
		routers.put(320,r_320);

		IRouter r_324 = rv.createRouter("r_324");
		routers.put(324,r_324);

		IRouter r_424 = rv.createRouter("r_424");
		routers.put(424,r_424);

		IRouter r_448 = rv.createRouter("r_448");
		routers.put(448,r_448);

		IRouter r_449 = rv.createRouter("r_449");
		routers.put(449,r_449);

		IRouter r_456 = rv.createRouter("r_456");
		routers.put(456,r_456);

		IRouter r_469 = rv.createRouter("r_469");
		routers.put(469,r_469);

		IRouter r_575 = rv.createRouter("r_575");
		routers.put(575,r_575);

		IRouter r_576 = rv.createRouter("r_576");
		routers.put(576,r_576);

		IRouter r_577 = rv.createRouter("r_577");
		routers.put(577,r_577);

		IRouter r_578 = rv.createRouter("r_578");
		routers.put(578,r_578);

		//create links
		ILink l_8_7 = rv.createLink("l_8_7");
		l_8_7.createInterface(routers.get(8).createInterface("if_7"));
		l_8_7.createInterface(routers.get(7).createInterface("if_8"));

		ILink l_9_8 = rv.createLink("l_9_8");
		l_9_8.createInterface(routers.get(9).createInterface("if_8"));
		l_9_8.createInterface(routers.get(8).createInterface("if_9"));

		ILink l_169_8 = rv.createLink("l_169_8");
		l_169_8.createInterface(routers.get(169).createInterface("if_8"));
		l_169_8.createInterface(routers.get(8).createInterface("if_169"));

		ILink l_50_49 = rv.createLink("l_50_49");
		l_50_49.createInterface(routers.get(50).createInterface("if_49"));
		l_50_49.createInterface(routers.get(49).createInterface("if_50"));

		ILink l_164_50 = rv.createLink("l_164_50");
		l_164_50.createInterface(routers.get(164).createInterface("if_50"));
		l_164_50.createInterface(routers.get(50).createInterface("if_164"));

		ILink l_165_50 = rv.createLink("l_165_50");
		l_165_50.createInterface(routers.get(165).createInterface("if_50"));
		l_165_50.createInterface(routers.get(50).createInterface("if_165"));

		ILink l_166_50 = rv.createLink("l_166_50");
		l_166_50.createInterface(routers.get(166).createInterface("if_50"));
		l_166_50.createInterface(routers.get(50).createInterface("if_166"));

		ILink l_167_50 = rv.createLink("l_167_50");
		l_167_50.createInterface(routers.get(167).createInterface("if_50"));
		l_167_50.createInterface(routers.get(50).createInterface("if_167"));

		ILink l_168_50 = rv.createLink("l_168_50");
		l_168_50.createInterface(routers.get(168).createInterface("if_50"));
		l_168_50.createInterface(routers.get(50).createInterface("if_168"));

		ILink l_169_50 = rv.createLink("l_169_50");
		l_169_50.createInterface(routers.get(169).createInterface("if_50"));
		l_169_50.createInterface(routers.get(50).createInterface("if_169"));

		ILink l_55_54 = rv.createLink("l_55_54");
		l_55_54.createInterface(routers.get(55).createInterface("if_54"));
		l_55_54.createInterface(routers.get(54).createInterface("if_55"));

		ILink l_67_55 = rv.createLink("l_67_55");
		l_67_55.createInterface(routers.get(67).createInterface("if_55"));
		l_67_55.createInterface(routers.get(55).createInterface("if_67"));

		ILink l_68_55 = rv.createLink("l_68_55");
		l_68_55.createInterface(routers.get(68).createInterface("if_55"));
		l_68_55.createInterface(routers.get(55).createInterface("if_68"));

		ILink l_109_55 = rv.createLink("l_109_55");
		l_109_55.createInterface(routers.get(109).createInterface("if_55"));
		l_109_55.createInterface(routers.get(55).createInterface("if_109"));

		ILink l_150_55 = rv.createLink("l_150_55");
		l_150_55.createInterface(routers.get(150).createInterface("if_55"));
		l_150_55.createInterface(routers.get(55).createInterface("if_150"));

		ILink l_170_55 = rv.createLink("l_170_55");
		l_170_55.createInterface(routers.get(170).createInterface("if_55"));
		l_170_55.createInterface(routers.get(55).createInterface("if_170"));

		ILink l_172_55 = rv.createLink("l_172_55");
		l_172_55.createInterface(routers.get(172).createInterface("if_55"));
		l_172_55.createInterface(routers.get(55).createInterface("if_172"));

		ILink l_189_55 = rv.createLink("l_189_55");
		l_189_55.createInterface(routers.get(189).createInterface("if_55"));
		l_189_55.createInterface(routers.get(55).createInterface("if_189"));

		ILink l_195_55 = rv.createLink("l_195_55");
		l_195_55.createInterface(routers.get(195).createInterface("if_55"));
		l_195_55.createInterface(routers.get(55).createInterface("if_195"));

		ILink l_196_55 = rv.createLink("l_196_55");
		l_196_55.createInterface(routers.get(196).createInterface("if_55"));
		l_196_55.createInterface(routers.get(55).createInterface("if_196"));

		ILink l_301_55 = rv.createLink("l_301_55");
		l_301_55.createInterface(routers.get(301).createInterface("if_55"));
		l_301_55.createInterface(routers.get(55).createInterface("if_301"));

		ILink l_306_55 = rv.createLink("l_306_55");
		l_306_55.createInterface(routers.get(306).createInterface("if_55"));
		l_306_55.createInterface(routers.get(55).createInterface("if_306"));

		ILink l_314_55 = rv.createLink("l_314_55");
		l_314_55.createInterface(routers.get(314).createInterface("if_55"));
		l_314_55.createInterface(routers.get(55).createInterface("if_314"));

		ILink l_320_55 = rv.createLink("l_320_55");
		l_320_55.createInterface(routers.get(320).createInterface("if_55"));
		l_320_55.createInterface(routers.get(55).createInterface("if_320"));

		ILink l_324_55 = rv.createLink("l_324_55");
		l_324_55.createInterface(routers.get(324).createInterface("if_55"));
		l_324_55.createInterface(routers.get(55).createInterface("if_324"));

		ILink l_424_55 = rv.createLink("l_424_55");
		l_424_55.createInterface(routers.get(424).createInterface("if_55"));
		l_424_55.createInterface(routers.get(55).createInterface("if_424"));

		ILink l_68_67 = rv.createLink("l_68_67");
		l_68_67.createInterface(routers.get(68).createInterface("if_67"));
		l_68_67.createInterface(routers.get(67).createInterface("if_68"));

		ILink l_172_68 = rv.createLink("l_172_68");
		l_172_68.createInterface(routers.get(172).createInterface("if_68"));
		l_172_68.createInterface(routers.get(68).createInterface("if_172"));

		ILink l_195_68 = rv.createLink("l_195_68");
		l_195_68.createInterface(routers.get(195).createInterface("if_68"));
		l_195_68.createInterface(routers.get(68).createInterface("if_195"));

		ILink l_196_68 = rv.createLink("l_196_68");
		l_196_68.createInterface(routers.get(196).createInterface("if_68"));
		l_196_68.createInterface(routers.get(68).createInterface("if_196"));

		ILink l_306_68 = rv.createLink("l_306_68");
		l_306_68.createInterface(routers.get(306).createInterface("if_68"));
		l_306_68.createInterface(routers.get(68).createInterface("if_306"));

		ILink l_314_68 = rv.createLink("l_314_68");
		l_314_68.createInterface(routers.get(314).createInterface("if_68"));
		l_314_68.createInterface(routers.get(68).createInterface("if_314"));

		ILink l_424_68 = rv.createLink("l_424_68");
		l_424_68.createInterface(routers.get(424).createInterface("if_68"));
		l_424_68.createInterface(routers.get(68).createInterface("if_424"));

		ILink l_174_169 = rv.createLink("l_174_169");
		l_174_169.createInterface(routers.get(174).createInterface("if_169"));
		l_174_169.createInterface(routers.get(169).createInterface("if_174"));

		ILink l_175_169 = rv.createLink("l_175_169");
		l_175_169.createInterface(routers.get(175).createInterface("if_169"));
		l_175_169.createInterface(routers.get(169).createInterface("if_175"));

		ILink l_456_169 = rv.createLink("l_456_169");
		l_456_169.createInterface(routers.get(456).createInterface("if_169"));
		l_456_169.createInterface(routers.get(169).createInterface("if_456"));

		ILink l_575_169 = rv.createLink("l_575_169");
		l_575_169.createInterface(routers.get(575).createInterface("if_169"));
		l_575_169.createInterface(routers.get(169).createInterface("if_575"));

		ILink l_175_174 = rv.createLink("l_175_174");
		l_175_174.createInterface(routers.get(175).createInterface("if_174"));
		l_175_174.createInterface(routers.get(174).createInterface("if_175"));

		ILink l_210_175 = rv.createLink("l_210_175");
		l_210_175.createInterface(routers.get(210).createInterface("if_175"));
		l_210_175.createInterface(routers.get(175).createInterface("if_210"));

		ILink l_456_175 = rv.createLink("l_456_175");
		l_456_175.createInterface(routers.get(456).createInterface("if_175"));
		l_456_175.createInterface(routers.get(175).createInterface("if_456"));

		ILink l_469_175 = rv.createLink("l_469_175");
		l_469_175.createInterface(routers.get(469).createInterface("if_175"));
		l_469_175.createInterface(routers.get(175).createInterface("if_469"));

		ILink l_575_175 = rv.createLink("l_575_175");
		l_575_175.createInterface(routers.get(575).createInterface("if_175"));
		l_575_175.createInterface(routers.get(175).createInterface("if_575"));

		ILink l_576_175 = rv.createLink("l_576_175");
		l_576_175.createInterface(routers.get(576).createInterface("if_175"));
		l_576_175.createInterface(routers.get(175).createInterface("if_576"));

		ILink l_577_175 = rv.createLink("l_577_175");
		l_577_175.createInterface(routers.get(577).createInterface("if_175"));
		l_577_175.createInterface(routers.get(175).createInterface("if_577"));

		ILink l_578_175 = rv.createLink("l_578_175");
		l_578_175.createInterface(routers.get(578).createInterface("if_175"));
		l_578_175.createInterface(routers.get(175).createInterface("if_578"));

		ILink l_211_210 = rv.createLink("l_211_210");
		l_211_210.createInterface(routers.get(211).createInterface("if_210"));
		l_211_210.createInterface(routers.get(210).createInterface("if_211"));

		ILink l_577_456 = rv.createLink("l_577_456");
		l_577_456.createInterface(routers.get(577).createInterface("if_456"));
		l_577_456.createInterface(routers.get(456).createInterface("if_577"));

		//attach campus networks

		INet campus_7 = null;
		IRouter campus_r_7 = null;
		if(null == baseCampus) {
			campus_7 = rv.createNet("campus_6_7");
			campus_r_7 = createCampus(campus_7,do_spherical);
		}else {
			campus_7 = rv.createNetReplica("campus_6_7",baseCampus);
			campus_r_7 = (IRouter)campus_7.getChildByName("sub_campus_router");
		}
		ILink l_campus_7 = rv.createLink("l_campus_7");
		l_campus_7.createInterface(r_7.createInterface("if_campus_7"));
		l_campus_7.createInterface((IInterface)campus_r_7.getChildByName("if_stub"));

		INet campus_9 = null;
		IRouter campus_r_9 = null;
		if(null == baseCampus) {
			campus_9 = rv.createNet("campus_6_9");
			campus_r_9 = createCampus(campus_9,do_spherical);
		}else {
			campus_9 = rv.createNetReplica("campus_6_9",baseCampus);
			campus_r_9 = (IRouter)campus_9.getChildByName("sub_campus_router");
		}
		ILink l_campus_9 = rv.createLink("l_campus_9");
		l_campus_9.createInterface(r_9.createInterface("if_campus_9"));
		l_campus_9.createInterface((IInterface)campus_r_9.getChildByName("if_stub"));

		INet campus_49 = null;
		IRouter campus_r_49 = null;
		if(null == baseCampus) {
			campus_49 = rv.createNet("campus_6_49");
			campus_r_49 = createCampus(campus_49,do_spherical);
		}else {
			campus_49 = rv.createNetReplica("campus_6_49",baseCampus);
			campus_r_49 = (IRouter)campus_49.getChildByName("sub_campus_router");
		}
		ILink l_campus_49 = rv.createLink("l_campus_49");
		l_campus_49.createInterface(r_49.createInterface("if_campus_49"));
		l_campus_49.createInterface((IInterface)campus_r_49.getChildByName("if_stub"));

		INet campus_54 = null;
		IRouter campus_r_54 = null;
		if(null == baseCampus) {
			campus_54 = rv.createNet("campus_6_54");
			campus_r_54 = createCampus(campus_54,do_spherical);
		}else {
			campus_54 = rv.createNetReplica("campus_6_54",baseCampus);
			campus_r_54 = (IRouter)campus_54.getChildByName("sub_campus_router");
		}
		ILink l_campus_54 = rv.createLink("l_campus_54");
		l_campus_54.createInterface(r_54.createInterface("if_campus_54"));
		l_campus_54.createInterface((IInterface)campus_r_54.getChildByName("if_stub"));

		INet campus_109 = null;
		IRouter campus_r_109 = null;
		if(null == baseCampus) {
			campus_109 = rv.createNet("campus_6_109");
			campus_r_109 = createCampus(campus_109,do_spherical);
		}else {
			campus_109 = rv.createNetReplica("campus_6_109",baseCampus);
			campus_r_109 = (IRouter)campus_109.getChildByName("sub_campus_router");
		}
		ILink l_campus_109 = rv.createLink("l_campus_109");
		l_campus_109.createInterface(r_109.createInterface("if_campus_109"));
		l_campus_109.createInterface((IInterface)campus_r_109.getChildByName("if_stub"));

		INet campus_150 = null;
		IRouter campus_r_150 = null;
		if(null == baseCampus) {
			campus_150 = rv.createNet("campus_6_150");
			campus_r_150 = createCampus(campus_150,do_spherical);
		}else {
			campus_150 = rv.createNetReplica("campus_6_150",baseCampus);
			campus_r_150 = (IRouter)campus_150.getChildByName("sub_campus_router");
		}
		ILink l_campus_150 = rv.createLink("l_campus_150");
		l_campus_150.createInterface(r_150.createInterface("if_campus_150"));
		l_campus_150.createInterface((IInterface)campus_r_150.getChildByName("if_stub"));

		INet campus_164 = null;
		IRouter campus_r_164 = null;
		if(null == baseCampus) {
			campus_164 = rv.createNet("campus_6_164");
			campus_r_164 = createCampus(campus_164,do_spherical);
		}else {
			campus_164 = rv.createNetReplica("campus_6_164",baseCampus);
			campus_r_164 = (IRouter)campus_164.getChildByName("sub_campus_router");
		}
		ILink l_campus_164 = rv.createLink("l_campus_164");
		l_campus_164.createInterface(r_164.createInterface("if_campus_164"));
		l_campus_164.createInterface((IInterface)campus_r_164.getChildByName("if_stub"));

		INet campus_165 = null;
		IRouter campus_r_165 = null;
		if(null == baseCampus) {
			campus_165 = rv.createNet("campus_6_165");
			campus_r_165 = createCampus(campus_165,do_spherical);
		}else {
			campus_165 = rv.createNetReplica("campus_6_165",baseCampus);
			campus_r_165 = (IRouter)campus_165.getChildByName("sub_campus_router");
		}
		ILink l_campus_165 = rv.createLink("l_campus_165");
		l_campus_165.createInterface(r_165.createInterface("if_campus_165"));
		l_campus_165.createInterface((IInterface)campus_r_165.getChildByName("if_stub"));

		INet campus_166 = null;
		IRouter campus_r_166 = null;
		if(null == baseCampus) {
			campus_166 = rv.createNet("campus_6_166");
			campus_r_166 = createCampus(campus_166,do_spherical);
		}else {
			campus_166 = rv.createNetReplica("campus_6_166",baseCampus);
			campus_r_166 = (IRouter)campus_166.getChildByName("sub_campus_router");
		}
		ILink l_campus_166 = rv.createLink("l_campus_166");
		l_campus_166.createInterface(r_166.createInterface("if_campus_166"));
		l_campus_166.createInterface((IInterface)campus_r_166.getChildByName("if_stub"));

		INet campus_167 = null;
		IRouter campus_r_167 = null;
		if(null == baseCampus) {
			campus_167 = rv.createNet("campus_6_167");
			campus_r_167 = createCampus(campus_167,do_spherical);
		}else {
			campus_167 = rv.createNetReplica("campus_6_167",baseCampus);
			campus_r_167 = (IRouter)campus_167.getChildByName("sub_campus_router");
		}
		ILink l_campus_167 = rv.createLink("l_campus_167");
		l_campus_167.createInterface(r_167.createInterface("if_campus_167"));
		l_campus_167.createInterface((IInterface)campus_r_167.getChildByName("if_stub"));

		INet campus_168 = null;
		IRouter campus_r_168 = null;
		if(null == baseCampus) {
			campus_168 = rv.createNet("campus_6_168");
			campus_r_168 = createCampus(campus_168,do_spherical);
		}else {
			campus_168 = rv.createNetReplica("campus_6_168",baseCampus);
			campus_r_168 = (IRouter)campus_168.getChildByName("sub_campus_router");
		}
		ILink l_campus_168 = rv.createLink("l_campus_168");
		l_campus_168.createInterface(r_168.createInterface("if_campus_168"));
		l_campus_168.createInterface((IInterface)campus_r_168.getChildByName("if_stub"));

		INet campus_170 = null;
		IRouter campus_r_170 = null;
		if(null == baseCampus) {
			campus_170 = rv.createNet("campus_6_170");
			campus_r_170 = createCampus(campus_170,do_spherical);
		}else {
			campus_170 = rv.createNetReplica("campus_6_170",baseCampus);
			campus_r_170 = (IRouter)campus_170.getChildByName("sub_campus_router");
		}
		ILink l_campus_170 = rv.createLink("l_campus_170");
		l_campus_170.createInterface(r_170.createInterface("if_campus_170"));
		l_campus_170.createInterface((IInterface)campus_r_170.getChildByName("if_stub"));

		INet campus_189 = null;
		IRouter campus_r_189 = null;
		if(null == baseCampus) {
			campus_189 = rv.createNet("campus_6_189");
			campus_r_189 = createCampus(campus_189,do_spherical);
		}else {
			campus_189 = rv.createNetReplica("campus_6_189",baseCampus);
			campus_r_189 = (IRouter)campus_189.getChildByName("sub_campus_router");
		}
		ILink l_campus_189 = rv.createLink("l_campus_189");
		l_campus_189.createInterface(r_189.createInterface("if_campus_189"));
		l_campus_189.createInterface((IInterface)campus_r_189.getChildByName("if_stub"));

		INet campus_211 = null;
		IRouter campus_r_211 = null;
		if(null == baseCampus) {
			campus_211 = rv.createNet("campus_6_211");
			campus_r_211 = createCampus(campus_211,do_spherical);
		}else {
			campus_211 = rv.createNetReplica("campus_6_211",baseCampus);
			campus_r_211 = (IRouter)campus_211.getChildByName("sub_campus_router");
		}
		ILink l_campus_211 = rv.createLink("l_campus_211");
		l_campus_211.createInterface(r_211.createInterface("if_campus_211"));
		l_campus_211.createInterface((IInterface)campus_r_211.getChildByName("if_stub"));

		INet campus_301 = null;
		IRouter campus_r_301 = null;
		if(null == baseCampus) {
			campus_301 = rv.createNet("campus_6_301");
			campus_r_301 = createCampus(campus_301,do_spherical);
		}else {
			campus_301 = rv.createNetReplica("campus_6_301",baseCampus);
			campus_r_301 = (IRouter)campus_301.getChildByName("sub_campus_router");
		}
		ILink l_campus_301 = rv.createLink("l_campus_301");
		l_campus_301.createInterface(r_301.createInterface("if_campus_301"));
		l_campus_301.createInterface((IInterface)campus_r_301.getChildByName("if_stub"));

		INet campus_320 = null;
		IRouter campus_r_320 = null;
		if(null == baseCampus) {
			campus_320 = rv.createNet("campus_6_320");
			campus_r_320 = createCampus(campus_320,do_spherical);
		}else {
			campus_320 = rv.createNetReplica("campus_6_320",baseCampus);
			campus_r_320 = (IRouter)campus_320.getChildByName("sub_campus_router");
		}
		ILink l_campus_320 = rv.createLink("l_campus_320");
		l_campus_320.createInterface(r_320.createInterface("if_campus_320"));
		l_campus_320.createInterface((IInterface)campus_r_320.getChildByName("if_stub"));

		INet campus_324 = null;
		IRouter campus_r_324 = null;
		if(null == baseCampus) {
			campus_324 = rv.createNet("campus_6_324");
			campus_r_324 = createCampus(campus_324,do_spherical);
		}else {
			campus_324 = rv.createNetReplica("campus_6_324",baseCampus);
			campus_r_324 = (IRouter)campus_324.getChildByName("sub_campus_router");
		}
		ILink l_campus_324 = rv.createLink("l_campus_324");
		l_campus_324.createInterface(r_324.createInterface("if_campus_324"));
		l_campus_324.createInterface((IInterface)campus_r_324.getChildByName("if_stub"));

		INet campus_576 = null;
		IRouter campus_r_576 = null;
		if(null == baseCampus) {
			campus_576 = rv.createNet("campus_6_576");
			campus_r_576 = createCampus(campus_576,do_spherical);
		}else {
			campus_576 = rv.createNetReplica("campus_6_576",baseCampus);
			campus_r_576 = (IRouter)campus_576.getChildByName("sub_campus_router");
		}
		ILink l_campus_576 = rv.createLink("l_campus_576");
		l_campus_576.createInterface(r_576.createInterface("if_campus_576"));
		l_campus_576.createInterface((IInterface)campus_r_576.getChildByName("if_stub"));

		INet campus_578 = null;
		IRouter campus_r_578 = null;
		if(null == baseCampus) {
			campus_578 = rv.createNet("campus_6_578");
			campus_r_578 = createCampus(campus_578,do_spherical);
		}else {
			campus_578 = rv.createNetReplica("campus_6_578",baseCampus);
			campus_r_578 = (IRouter)campus_578.getChildByName("sub_campus_router");
		}
		ILink l_campus_578 = rv.createLink("l_campus_578");
		l_campus_578.createInterface(r_578.createInterface("if_campus_578"));
		l_campus_578.createInterface((IInterface)campus_r_578.getChildByName("if_stub"));
		return rv;
	}
	public INet create_net_7(INet top, Map<Integer,IRouter> routers) {
		INet rv = top.createNet("net_7");
		if(do_spherical) rv.createShortestPath();;

		//create routers
		IRouter r_42 = rv.createRouter("r_42");
		routers.put(42,r_42);

		IRouter r_44 = rv.createRouter("r_44");
		routers.put(44,r_44);

		IRouter r_45 = rv.createRouter("r_45");
		routers.put(45,r_45);

		IRouter r_46 = rv.createRouter("r_46");
		routers.put(46,r_46);

		IRouter r_117 = rv.createRouter("r_117");
		routers.put(117,r_117);

		IRouter r_118 = rv.createRouter("r_118");
		routers.put(118,r_118);

		IRouter r_119 = rv.createRouter("r_119");
		routers.put(119,r_119);

		IRouter r_342 = rv.createRouter("r_342");
		routers.put(342,r_342);

		IRouter r_343 = rv.createRouter("r_343");
		routers.put(343,r_343);

		IRouter r_344 = rv.createRouter("r_344");
		routers.put(344,r_344);

		IRouter r_356 = rv.createRouter("r_356");
		routers.put(356,r_356);

		IRouter r_439 = rv.createRouter("r_439");
		routers.put(439,r_439);

		IRouter r_440 = rv.createRouter("r_440");
		routers.put(440,r_440);

		IRouter r_441 = rv.createRouter("r_441");
		routers.put(441,r_441);

		IRouter r_442 = rv.createRouter("r_442");
		routers.put(442,r_442);

		IRouter r_443 = rv.createRouter("r_443");
		routers.put(443,r_443);

		IRouter r_444 = rv.createRouter("r_444");
		routers.put(444,r_444);

		IRouter r_445 = rv.createRouter("r_445");
		routers.put(445,r_445);

		IRouter r_446 = rv.createRouter("r_446");
		routers.put(446,r_446);

		IRouter r_447 = rv.createRouter("r_447");
		routers.put(447,r_447);

		IRouter r_450 = rv.createRouter("r_450");
		routers.put(450,r_450);

		IRouter r_451 = rv.createRouter("r_451");
		routers.put(451,r_451);

		IRouter r_452 = rv.createRouter("r_452");
		routers.put(452,r_452);

		IRouter r_453 = rv.createRouter("r_453");
		routers.put(453,r_453);

		IRouter r_454 = rv.createRouter("r_454");
		routers.put(454,r_454);

		IRouter r_455 = rv.createRouter("r_455");
		routers.put(455,r_455);

		IRouter r_457 = rv.createRouter("r_457");
		routers.put(457,r_457);

		IRouter r_458 = rv.createRouter("r_458");
		routers.put(458,r_458);

		IRouter r_459 = rv.createRouter("r_459");
		routers.put(459,r_459);

		IRouter r_460 = rv.createRouter("r_460");
		routers.put(460,r_460);

		IRouter r_461 = rv.createRouter("r_461");
		routers.put(461,r_461);

		IRouter r_462 = rv.createRouter("r_462");
		routers.put(462,r_462);

		IRouter r_464 = rv.createRouter("r_464");
		routers.put(464,r_464);

		IRouter r_465 = rv.createRouter("r_465");
		routers.put(465,r_465);

		IRouter r_466 = rv.createRouter("r_466");
		routers.put(466,r_466);

		IRouter r_467 = rv.createRouter("r_467");
		routers.put(467,r_467);

		IRouter r_468 = rv.createRouter("r_468");
		routers.put(468,r_468);

		IRouter r_570 = rv.createRouter("r_570");
		routers.put(570,r_570);

		IRouter r_571 = rv.createRouter("r_571");
		routers.put(571,r_571);

		IRouter r_574 = rv.createRouter("r_574");
		routers.put(574,r_574);

		//create links
		ILink l_44_42 = rv.createLink("l_44_42");
		l_44_42.createInterface(routers.get(44).createInterface("if_42"));
		l_44_42.createInterface(routers.get(42).createInterface("if_44"));

		ILink l_45_42 = rv.createLink("l_45_42");
		l_45_42.createInterface(routers.get(45).createInterface("if_42"));
		l_45_42.createInterface(routers.get(42).createInterface("if_45"));

		ILink l_46_42 = rv.createLink("l_46_42");
		l_46_42.createInterface(routers.get(46).createInterface("if_42"));
		l_46_42.createInterface(routers.get(42).createInterface("if_46"));

		ILink l_45_44 = rv.createLink("l_45_44");
		l_45_44.createInterface(routers.get(45).createInterface("if_44"));
		l_45_44.createInterface(routers.get(44).createInterface("if_45"));

		ILink l_46_44 = rv.createLink("l_46_44");
		l_46_44.createInterface(routers.get(46).createInterface("if_44"));
		l_46_44.createInterface(routers.get(44).createInterface("if_46"));

		ILink l_117_44 = rv.createLink("l_117_44");
		l_117_44.createInterface(routers.get(117).createInterface("if_44"));
		l_117_44.createInterface(routers.get(44).createInterface("if_117"));

		ILink l_118_44 = rv.createLink("l_118_44");
		l_118_44.createInterface(routers.get(118).createInterface("if_44"));
		l_118_44.createInterface(routers.get(44).createInterface("if_118"));

		ILink l_119_44 = rv.createLink("l_119_44");
		l_119_44.createInterface(routers.get(119).createInterface("if_44"));
		l_119_44.createInterface(routers.get(44).createInterface("if_119"));

		ILink l_343_44 = rv.createLink("l_343_44");
		l_343_44.createInterface(routers.get(343).createInterface("if_44"));
		l_343_44.createInterface(routers.get(44).createInterface("if_343"));

		ILink l_439_44 = rv.createLink("l_439_44");
		l_439_44.createInterface(routers.get(439).createInterface("if_44"));
		l_439_44.createInterface(routers.get(44).createInterface("if_439"));

		ILink l_440_44 = rv.createLink("l_440_44");
		l_440_44.createInterface(routers.get(440).createInterface("if_44"));
		l_440_44.createInterface(routers.get(44).createInterface("if_440"));

		ILink l_441_44 = rv.createLink("l_441_44");
		l_441_44.createInterface(routers.get(441).createInterface("if_44"));
		l_441_44.createInterface(routers.get(44).createInterface("if_441"));

		ILink l_442_44 = rv.createLink("l_442_44");
		l_442_44.createInterface(routers.get(442).createInterface("if_44"));
		l_442_44.createInterface(routers.get(44).createInterface("if_442"));

		ILink l_443_44 = rv.createLink("l_443_44");
		l_443_44.createInterface(routers.get(443).createInterface("if_44"));
		l_443_44.createInterface(routers.get(44).createInterface("if_443"));

		ILink l_446_44 = rv.createLink("l_446_44");
		l_446_44.createInterface(routers.get(446).createInterface("if_44"));
		l_446_44.createInterface(routers.get(44).createInterface("if_446"));

		ILink l_447_44 = rv.createLink("l_447_44");
		l_447_44.createInterface(routers.get(447).createInterface("if_44"));
		l_447_44.createInterface(routers.get(44).createInterface("if_447"));

		ILink l_452_44 = rv.createLink("l_452_44");
		l_452_44.createInterface(routers.get(452).createInterface("if_44"));
		l_452_44.createInterface(routers.get(44).createInterface("if_452"));

		ILink l_453_44 = rv.createLink("l_453_44");
		l_453_44.createInterface(routers.get(453).createInterface("if_44"));
		l_453_44.createInterface(routers.get(44).createInterface("if_453"));

		ILink l_454_44 = rv.createLink("l_454_44");
		l_454_44.createInterface(routers.get(454).createInterface("if_44"));
		l_454_44.createInterface(routers.get(44).createInterface("if_454"));

		ILink l_458_44 = rv.createLink("l_458_44");
		l_458_44.createInterface(routers.get(458).createInterface("if_44"));
		l_458_44.createInterface(routers.get(44).createInterface("if_458"));

		ILink l_459_44 = rv.createLink("l_459_44");
		l_459_44.createInterface(routers.get(459).createInterface("if_44"));
		l_459_44.createInterface(routers.get(44).createInterface("if_459"));

		ILink l_571_44 = rv.createLink("l_571_44");
		l_571_44.createInterface(routers.get(571).createInterface("if_44"));
		l_571_44.createInterface(routers.get(44).createInterface("if_571"));

		ILink l_570_45 = rv.createLink("l_570_45");
		l_570_45.createInterface(routers.get(570).createInterface("if_45"));
		l_570_45.createInterface(routers.get(45).createInterface("if_570"));

		ILink l_571_45 = rv.createLink("l_571_45");
		l_571_45.createInterface(routers.get(571).createInterface("if_45"));
		l_571_45.createInterface(routers.get(45).createInterface("if_571"));

		ILink l_574_45 = rv.createLink("l_574_45");
		l_574_45.createInterface(routers.get(574).createInterface("if_45"));
		l_574_45.createInterface(routers.get(45).createInterface("if_574"));

		ILink l_570_46 = rv.createLink("l_570_46");
		l_570_46.createInterface(routers.get(570).createInterface("if_46"));
		l_570_46.createInterface(routers.get(46).createInterface("if_570"));

		ILink l_571_46 = rv.createLink("l_571_46");
		l_571_46.createInterface(routers.get(571).createInterface("if_46"));
		l_571_46.createInterface(routers.get(46).createInterface("if_571"));

		ILink l_574_46 = rv.createLink("l_574_46");
		l_574_46.createInterface(routers.get(574).createInterface("if_46"));
		l_574_46.createInterface(routers.get(46).createInterface("if_574"));

		ILink l_118_117 = rv.createLink("l_118_117");
		l_118_117.createInterface(routers.get(118).createInterface("if_117"));
		l_118_117.createInterface(routers.get(117).createInterface("if_118"));

		ILink l_119_117 = rv.createLink("l_119_117");
		l_119_117.createInterface(routers.get(119).createInterface("if_117"));
		l_119_117.createInterface(routers.get(117).createInterface("if_119"));

		ILink l_343_118 = rv.createLink("l_343_118");
		l_343_118.createInterface(routers.get(343).createInterface("if_118"));
		l_343_118.createInterface(routers.get(118).createInterface("if_343"));

		ILink l_344_118 = rv.createLink("l_344_118");
		l_344_118.createInterface(routers.get(344).createInterface("if_118"));
		l_344_118.createInterface(routers.get(118).createInterface("if_344"));

		ILink l_439_118 = rv.createLink("l_439_118");
		l_439_118.createInterface(routers.get(439).createInterface("if_118"));
		l_439_118.createInterface(routers.get(118).createInterface("if_439"));

		ILink l_440_118 = rv.createLink("l_440_118");
		l_440_118.createInterface(routers.get(440).createInterface("if_118"));
		l_440_118.createInterface(routers.get(118).createInterface("if_440"));

		ILink l_441_118 = rv.createLink("l_441_118");
		l_441_118.createInterface(routers.get(441).createInterface("if_118"));
		l_441_118.createInterface(routers.get(118).createInterface("if_441"));

		ILink l_442_118 = rv.createLink("l_442_118");
		l_442_118.createInterface(routers.get(442).createInterface("if_118"));
		l_442_118.createInterface(routers.get(118).createInterface("if_442"));

		ILink l_443_118 = rv.createLink("l_443_118");
		l_443_118.createInterface(routers.get(443).createInterface("if_118"));
		l_443_118.createInterface(routers.get(118).createInterface("if_443"));

		ILink l_444_118 = rv.createLink("l_444_118");
		l_444_118.createInterface(routers.get(444).createInterface("if_118"));
		l_444_118.createInterface(routers.get(118).createInterface("if_444"));

		ILink l_445_118 = rv.createLink("l_445_118");
		l_445_118.createInterface(routers.get(445).createInterface("if_118"));
		l_445_118.createInterface(routers.get(118).createInterface("if_445"));

		ILink l_446_118 = rv.createLink("l_446_118");
		l_446_118.createInterface(routers.get(446).createInterface("if_118"));
		l_446_118.createInterface(routers.get(118).createInterface("if_446"));

		ILink l_447_118 = rv.createLink("l_447_118");
		l_447_118.createInterface(routers.get(447).createInterface("if_118"));
		l_447_118.createInterface(routers.get(118).createInterface("if_447"));

		ILink l_450_118 = rv.createLink("l_450_118");
		l_450_118.createInterface(routers.get(450).createInterface("if_118"));
		l_450_118.createInterface(routers.get(118).createInterface("if_450"));

		ILink l_451_118 = rv.createLink("l_451_118");
		l_451_118.createInterface(routers.get(451).createInterface("if_118"));
		l_451_118.createInterface(routers.get(118).createInterface("if_451"));

		ILink l_452_118 = rv.createLink("l_452_118");
		l_452_118.createInterface(routers.get(452).createInterface("if_118"));
		l_452_118.createInterface(routers.get(118).createInterface("if_452"));

		ILink l_453_118 = rv.createLink("l_453_118");
		l_453_118.createInterface(routers.get(453).createInterface("if_118"));
		l_453_118.createInterface(routers.get(118).createInterface("if_453"));

		ILink l_454_118 = rv.createLink("l_454_118");
		l_454_118.createInterface(routers.get(454).createInterface("if_118"));
		l_454_118.createInterface(routers.get(118).createInterface("if_454"));

		ILink l_455_118 = rv.createLink("l_455_118");
		l_455_118.createInterface(routers.get(455).createInterface("if_118"));
		l_455_118.createInterface(routers.get(118).createInterface("if_455"));

		ILink l_457_118 = rv.createLink("l_457_118");
		l_457_118.createInterface(routers.get(457).createInterface("if_118"));
		l_457_118.createInterface(routers.get(118).createInterface("if_457"));

		ILink l_458_118 = rv.createLink("l_458_118");
		l_458_118.createInterface(routers.get(458).createInterface("if_118"));
		l_458_118.createInterface(routers.get(118).createInterface("if_458"));

		ILink l_459_118 = rv.createLink("l_459_118");
		l_459_118.createInterface(routers.get(459).createInterface("if_118"));
		l_459_118.createInterface(routers.get(118).createInterface("if_459"));

		ILink l_343_119 = rv.createLink("l_343_119");
		l_343_119.createInterface(routers.get(343).createInterface("if_119"));
		l_343_119.createInterface(routers.get(119).createInterface("if_343"));

		ILink l_344_119 = rv.createLink("l_344_119");
		l_344_119.createInterface(routers.get(344).createInterface("if_119"));
		l_344_119.createInterface(routers.get(119).createInterface("if_344"));

		ILink l_439_119 = rv.createLink("l_439_119");
		l_439_119.createInterface(routers.get(439).createInterface("if_119"));
		l_439_119.createInterface(routers.get(119).createInterface("if_439"));

		ILink l_440_119 = rv.createLink("l_440_119");
		l_440_119.createInterface(routers.get(440).createInterface("if_119"));
		l_440_119.createInterface(routers.get(119).createInterface("if_440"));

		ILink l_441_119 = rv.createLink("l_441_119");
		l_441_119.createInterface(routers.get(441).createInterface("if_119"));
		l_441_119.createInterface(routers.get(119).createInterface("if_441"));

		ILink l_442_119 = rv.createLink("l_442_119");
		l_442_119.createInterface(routers.get(442).createInterface("if_119"));
		l_442_119.createInterface(routers.get(119).createInterface("if_442"));

		ILink l_443_119 = rv.createLink("l_443_119");
		l_443_119.createInterface(routers.get(443).createInterface("if_119"));
		l_443_119.createInterface(routers.get(119).createInterface("if_443"));

		ILink l_444_119 = rv.createLink("l_444_119");
		l_444_119.createInterface(routers.get(444).createInterface("if_119"));
		l_444_119.createInterface(routers.get(119).createInterface("if_444"));

		ILink l_445_119 = rv.createLink("l_445_119");
		l_445_119.createInterface(routers.get(445).createInterface("if_119"));
		l_445_119.createInterface(routers.get(119).createInterface("if_445"));

		ILink l_446_119 = rv.createLink("l_446_119");
		l_446_119.createInterface(routers.get(446).createInterface("if_119"));
		l_446_119.createInterface(routers.get(119).createInterface("if_446"));

		ILink l_447_119 = rv.createLink("l_447_119");
		l_447_119.createInterface(routers.get(447).createInterface("if_119"));
		l_447_119.createInterface(routers.get(119).createInterface("if_447"));

		ILink l_452_119 = rv.createLink("l_452_119");
		l_452_119.createInterface(routers.get(452).createInterface("if_119"));
		l_452_119.createInterface(routers.get(119).createInterface("if_452"));

		ILink l_453_119 = rv.createLink("l_453_119");
		l_453_119.createInterface(routers.get(453).createInterface("if_119"));
		l_453_119.createInterface(routers.get(119).createInterface("if_453"));

		ILink l_454_119 = rv.createLink("l_454_119");
		l_454_119.createInterface(routers.get(454).createInterface("if_119"));
		l_454_119.createInterface(routers.get(119).createInterface("if_454"));

		ILink l_455_119 = rv.createLink("l_455_119");
		l_455_119.createInterface(routers.get(455).createInterface("if_119"));
		l_455_119.createInterface(routers.get(119).createInterface("if_455"));

		ILink l_458_119 = rv.createLink("l_458_119");
		l_458_119.createInterface(routers.get(458).createInterface("if_119"));
		l_458_119.createInterface(routers.get(119).createInterface("if_458"));

		ILink l_459_119 = rv.createLink("l_459_119");
		l_459_119.createInterface(routers.get(459).createInterface("if_119"));
		l_459_119.createInterface(routers.get(119).createInterface("if_459"));

		ILink l_343_342 = rv.createLink("l_343_342");
		l_343_342.createInterface(routers.get(343).createInterface("if_342"));
		l_343_342.createInterface(routers.get(342).createInterface("if_343"));

		ILink l_344_342 = rv.createLink("l_344_342");
		l_344_342.createInterface(routers.get(344).createInterface("if_342"));
		l_344_342.createInterface(routers.get(342).createInterface("if_344"));

		ILink l_356_343 = rv.createLink("l_356_343");
		l_356_343.createInterface(routers.get(356).createInterface("if_343"));
		l_356_343.createInterface(routers.get(343).createInterface("if_356"));

		ILink l_442_343 = rv.createLink("l_442_343");
		l_442_343.createInterface(routers.get(442).createInterface("if_343"));
		l_442_343.createInterface(routers.get(343).createInterface("if_442"));

		ILink l_445_343 = rv.createLink("l_445_343");
		l_445_343.createInterface(routers.get(445).createInterface("if_343"));
		l_445_343.createInterface(routers.get(343).createInterface("if_445"));

		ILink l_446_343 = rv.createLink("l_446_343");
		l_446_343.createInterface(routers.get(446).createInterface("if_343"));
		l_446_343.createInterface(routers.get(343).createInterface("if_446"));

		ILink l_447_343 = rv.createLink("l_447_343");
		l_447_343.createInterface(routers.get(447).createInterface("if_343"));
		l_447_343.createInterface(routers.get(343).createInterface("if_447"));

		ILink l_457_343 = rv.createLink("l_457_343");
		l_457_343.createInterface(routers.get(457).createInterface("if_343"));
		l_457_343.createInterface(routers.get(343).createInterface("if_457"));

		ILink l_458_343 = rv.createLink("l_458_343");
		l_458_343.createInterface(routers.get(458).createInterface("if_343"));
		l_458_343.createInterface(routers.get(343).createInterface("if_458"));

		ILink l_459_343 = rv.createLink("l_459_343");
		l_459_343.createInterface(routers.get(459).createInterface("if_343"));
		l_459_343.createInterface(routers.get(343).createInterface("if_459"));

		ILink l_460_343 = rv.createLink("l_460_343");
		l_460_343.createInterface(routers.get(460).createInterface("if_343"));
		l_460_343.createInterface(routers.get(343).createInterface("if_460"));

		ILink l_461_343 = rv.createLink("l_461_343");
		l_461_343.createInterface(routers.get(461).createInterface("if_343"));
		l_461_343.createInterface(routers.get(343).createInterface("if_461"));

		ILink l_462_343 = rv.createLink("l_462_343");
		l_462_343.createInterface(routers.get(462).createInterface("if_343"));
		l_462_343.createInterface(routers.get(343).createInterface("if_462"));

		ILink l_464_343 = rv.createLink("l_464_343");
		l_464_343.createInterface(routers.get(464).createInterface("if_343"));
		l_464_343.createInterface(routers.get(343).createInterface("if_464"));

		ILink l_465_343 = rv.createLink("l_465_343");
		l_465_343.createInterface(routers.get(465).createInterface("if_343"));
		l_465_343.createInterface(routers.get(343).createInterface("if_465"));

		ILink l_466_343 = rv.createLink("l_466_343");
		l_466_343.createInterface(routers.get(466).createInterface("if_343"));
		l_466_343.createInterface(routers.get(343).createInterface("if_466"));

		ILink l_467_343 = rv.createLink("l_467_343");
		l_467_343.createInterface(routers.get(467).createInterface("if_343"));
		l_467_343.createInterface(routers.get(343).createInterface("if_467"));

		ILink l_468_343 = rv.createLink("l_468_343");
		l_468_343.createInterface(routers.get(468).createInterface("if_343"));
		l_468_343.createInterface(routers.get(343).createInterface("if_468"));

		ILink l_356_344 = rv.createLink("l_356_344");
		l_356_344.createInterface(routers.get(356).createInterface("if_344"));
		l_356_344.createInterface(routers.get(344).createInterface("if_356"));

		ILink l_445_344 = rv.createLink("l_445_344");
		l_445_344.createInterface(routers.get(445).createInterface("if_344"));
		l_445_344.createInterface(routers.get(344).createInterface("if_445"));

		ILink l_447_344 = rv.createLink("l_447_344");
		l_447_344.createInterface(routers.get(447).createInterface("if_344"));
		l_447_344.createInterface(routers.get(344).createInterface("if_447"));

		ILink l_458_344 = rv.createLink("l_458_344");
		l_458_344.createInterface(routers.get(458).createInterface("if_344"));
		l_458_344.createInterface(routers.get(344).createInterface("if_458"));

		ILink l_459_344 = rv.createLink("l_459_344");
		l_459_344.createInterface(routers.get(459).createInterface("if_344"));
		l_459_344.createInterface(routers.get(344).createInterface("if_459"));

		ILink l_460_344 = rv.createLink("l_460_344");
		l_460_344.createInterface(routers.get(460).createInterface("if_344"));
		l_460_344.createInterface(routers.get(344).createInterface("if_460"));

		ILink l_461_344 = rv.createLink("l_461_344");
		l_461_344.createInterface(routers.get(461).createInterface("if_344"));
		l_461_344.createInterface(routers.get(344).createInterface("if_461"));

		ILink l_462_344 = rv.createLink("l_462_344");
		l_462_344.createInterface(routers.get(462).createInterface("if_344"));
		l_462_344.createInterface(routers.get(344).createInterface("if_462"));

		ILink l_464_344 = rv.createLink("l_464_344");
		l_464_344.createInterface(routers.get(464).createInterface("if_344"));
		l_464_344.createInterface(routers.get(344).createInterface("if_464"));

		ILink l_465_344 = rv.createLink("l_465_344");
		l_465_344.createInterface(routers.get(465).createInterface("if_344"));
		l_465_344.createInterface(routers.get(344).createInterface("if_465"));

		ILink l_466_344 = rv.createLink("l_466_344");
		l_466_344.createInterface(routers.get(466).createInterface("if_344"));
		l_466_344.createInterface(routers.get(344).createInterface("if_466"));

		ILink l_467_344 = rv.createLink("l_467_344");
		l_467_344.createInterface(routers.get(467).createInterface("if_344"));
		l_467_344.createInterface(routers.get(344).createInterface("if_467"));

		ILink l_468_344 = rv.createLink("l_468_344");
		l_468_344.createInterface(routers.get(468).createInterface("if_344"));
		l_468_344.createInterface(routers.get(344).createInterface("if_468"));

		ILink l_457_445 = rv.createLink("l_457_445");
		l_457_445.createInterface(routers.get(457).createInterface("if_445"));
		l_457_445.createInterface(routers.get(445).createInterface("if_457"));

		ILink l_457_446 = rv.createLink("l_457_446");
		l_457_446.createInterface(routers.get(457).createInterface("if_446"));
		l_457_446.createInterface(routers.get(446).createInterface("if_457"));

		//attach campus networks

		INet campus_450 = null;
		IRouter campus_r_450 = null;
		if(null == baseCampus) {
			campus_450 = rv.createNet("campus_7_450");
			campus_r_450 = createCampus(campus_450,do_spherical);
		}else {
			campus_450 = rv.createNetReplica("campus_7_450",baseCampus);
			campus_r_450 = (IRouter)campus_450.getChildByName("sub_campus_router");
		}
		ILink l_campus_450 = rv.createLink("l_campus_450");
		l_campus_450.createInterface(r_450.createInterface("if_campus_450"));
		l_campus_450.createInterface((IInterface)campus_r_450.getChildByName("if_stub"));

		INet campus_451 = null;
		IRouter campus_r_451 = null;
		if(null == baseCampus) {
			campus_451 = rv.createNet("campus_7_451");
			campus_r_451 = createCampus(campus_451,do_spherical);
		}else {
			campus_451 = rv.createNetReplica("campus_7_451",baseCampus);
			campus_r_451 = (IRouter)campus_451.getChildByName("sub_campus_router");
		}
		ILink l_campus_451 = rv.createLink("l_campus_451");
		l_campus_451.createInterface(r_451.createInterface("if_campus_451"));
		l_campus_451.createInterface((IInterface)campus_r_451.getChildByName("if_stub"));
		return rv;
	}
	public INet create_net_8(INet top, Map<Integer,IRouter> routers) {
		INet rv = top.createNet("net_8");
		if(do_spherical) rv.createShortestPath();;

		//create routers
		IRouter r_1 = rv.createRouter("r_1");
		routers.put(1,r_1);

		IRouter r_2 = rv.createRouter("r_2");
		routers.put(2,r_2);

		IRouter r_20 = rv.createRouter("r_20");
		routers.put(20,r_20);

		IRouter r_21 = rv.createRouter("r_21");
		routers.put(21,r_21);

		IRouter r_22 = rv.createRouter("r_22");
		routers.put(22,r_22);

		IRouter r_40 = rv.createRouter("r_40");
		routers.put(40,r_40);

		IRouter r_41 = rv.createRouter("r_41");
		routers.put(41,r_41);

		IRouter r_75 = rv.createRouter("r_75");
		routers.put(75,r_75);

		IRouter r_131 = rv.createRouter("r_131");
		routers.put(131,r_131);

		IRouter r_132 = rv.createRouter("r_132");
		routers.put(132,r_132);

		IRouter r_139 = rv.createRouter("r_139");
		routers.put(139,r_139);

		IRouter r_140 = rv.createRouter("r_140");
		routers.put(140,r_140);

		IRouter r_141 = rv.createRouter("r_141");
		routers.put(141,r_141);

		IRouter r_153 = rv.createRouter("r_153");
		routers.put(153,r_153);

		IRouter r_162 = rv.createRouter("r_162");
		routers.put(162,r_162);

		IRouter r_163 = rv.createRouter("r_163");
		routers.put(163,r_163);

		IRouter r_197 = rv.createRouter("r_197");
		routers.put(197,r_197);

		IRouter r_227 = rv.createRouter("r_227");
		routers.put(227,r_227);

		IRouter r_241 = rv.createRouter("r_241");
		routers.put(241,r_241);

		IRouter r_247 = rv.createRouter("r_247");
		routers.put(247,r_247);

		IRouter r_248 = rv.createRouter("r_248");
		routers.put(248,r_248);

		IRouter r_258 = rv.createRouter("r_258");
		routers.put(258,r_258);

		IRouter r_273 = rv.createRouter("r_273");
		routers.put(273,r_273);

		IRouter r_274 = rv.createRouter("r_274");
		routers.put(274,r_274);

		IRouter r_279 = rv.createRouter("r_279");
		routers.put(279,r_279);

		IRouter r_289 = rv.createRouter("r_289");
		routers.put(289,r_289);

		IRouter r_295 = rv.createRouter("r_295");
		routers.put(295,r_295);

		IRouter r_321 = rv.createRouter("r_321");
		routers.put(321,r_321);

		IRouter r_339 = rv.createRouter("r_339");
		routers.put(339,r_339);

		IRouter r_340 = rv.createRouter("r_340");
		routers.put(340,r_340);

		IRouter r_341 = rv.createRouter("r_341");
		routers.put(341,r_341);

		IRouter r_353 = rv.createRouter("r_353");
		routers.put(353,r_353);

		IRouter r_354 = rv.createRouter("r_354");
		routers.put(354,r_354);

		IRouter r_355 = rv.createRouter("r_355");
		routers.put(355,r_355);

		IRouter r_374 = rv.createRouter("r_374");
		routers.put(374,r_374);

		IRouter r_382 = rv.createRouter("r_382");
		routers.put(382,r_382);

		IRouter r_397 = rv.createRouter("r_397");
		routers.put(397,r_397);

		IRouter r_398 = rv.createRouter("r_398");
		routers.put(398,r_398);

		IRouter r_399 = rv.createRouter("r_399");
		routers.put(399,r_399);

		IRouter r_400 = rv.createRouter("r_400");
		routers.put(400,r_400);

		IRouter r_614 = rv.createRouter("r_614");
		routers.put(614,r_614);

		IRouter r_617 = rv.createRouter("r_617");
		routers.put(617,r_617);

		//create links
		ILink l_2_1 = rv.createLink("l_2_1");
		l_2_1.createInterface(routers.get(2).createInterface("if_1"));
		l_2_1.createInterface(routers.get(1).createInterface("if_2"));

		ILink l_20_2 = rv.createLink("l_20_2");
		l_20_2.createInterface(routers.get(20).createInterface("if_2"));
		l_20_2.createInterface(routers.get(2).createInterface("if_20"));

		ILink l_40_2 = rv.createLink("l_40_2");
		l_40_2.createInterface(routers.get(40).createInterface("if_2"));
		l_40_2.createInterface(routers.get(2).createInterface("if_40"));

		ILink l_41_2 = rv.createLink("l_41_2");
		l_41_2.createInterface(routers.get(41).createInterface("if_2"));
		l_41_2.createInterface(routers.get(2).createInterface("if_41"));

		ILink l_75_2 = rv.createLink("l_75_2");
		l_75_2.createInterface(routers.get(75).createInterface("if_2"));
		l_75_2.createInterface(routers.get(2).createInterface("if_75"));

		ILink l_131_2 = rv.createLink("l_131_2");
		l_131_2.createInterface(routers.get(131).createInterface("if_2"));
		l_131_2.createInterface(routers.get(2).createInterface("if_131"));

		ILink l_132_2 = rv.createLink("l_132_2");
		l_132_2.createInterface(routers.get(132).createInterface("if_2"));
		l_132_2.createInterface(routers.get(2).createInterface("if_132"));

		ILink l_139_2 = rv.createLink("l_139_2");
		l_139_2.createInterface(routers.get(139).createInterface("if_2"));
		l_139_2.createInterface(routers.get(2).createInterface("if_139"));

		ILink l_163_2 = rv.createLink("l_163_2");
		l_163_2.createInterface(routers.get(163).createInterface("if_2"));
		l_163_2.createInterface(routers.get(2).createInterface("if_163"));

		ILink l_197_2 = rv.createLink("l_197_2");
		l_197_2.createInterface(routers.get(197).createInterface("if_2"));
		l_197_2.createInterface(routers.get(2).createInterface("if_197"));

		ILink l_227_2 = rv.createLink("l_227_2");
		l_227_2.createInterface(routers.get(227).createInterface("if_2"));
		l_227_2.createInterface(routers.get(2).createInterface("if_227"));

		ILink l_241_2 = rv.createLink("l_241_2");
		l_241_2.createInterface(routers.get(241).createInterface("if_2"));
		l_241_2.createInterface(routers.get(2).createInterface("if_241"));

		ILink l_247_2 = rv.createLink("l_247_2");
		l_247_2.createInterface(routers.get(247).createInterface("if_2"));
		l_247_2.createInterface(routers.get(2).createInterface("if_247"));

		ILink l_248_2 = rv.createLink("l_248_2");
		l_248_2.createInterface(routers.get(248).createInterface("if_2"));
		l_248_2.createInterface(routers.get(2).createInterface("if_248"));

		ILink l_258_2 = rv.createLink("l_258_2");
		l_258_2.createInterface(routers.get(258).createInterface("if_2"));
		l_258_2.createInterface(routers.get(2).createInterface("if_258"));

		ILink l_274_2 = rv.createLink("l_274_2");
		l_274_2.createInterface(routers.get(274).createInterface("if_2"));
		l_274_2.createInterface(routers.get(2).createInterface("if_274"));

		ILink l_279_2 = rv.createLink("l_279_2");
		l_279_2.createInterface(routers.get(279).createInterface("if_2"));
		l_279_2.createInterface(routers.get(2).createInterface("if_279"));

		ILink l_289_2 = rv.createLink("l_289_2");
		l_289_2.createInterface(routers.get(289).createInterface("if_2"));
		l_289_2.createInterface(routers.get(2).createInterface("if_289"));

		ILink l_295_2 = rv.createLink("l_295_2");
		l_295_2.createInterface(routers.get(295).createInterface("if_2"));
		l_295_2.createInterface(routers.get(2).createInterface("if_295"));

		ILink l_321_2 = rv.createLink("l_321_2");
		l_321_2.createInterface(routers.get(321).createInterface("if_2"));
		l_321_2.createInterface(routers.get(2).createInterface("if_321"));

		ILink l_339_2 = rv.createLink("l_339_2");
		l_339_2.createInterface(routers.get(339).createInterface("if_2"));
		l_339_2.createInterface(routers.get(2).createInterface("if_339"));

		ILink l_355_2 = rv.createLink("l_355_2");
		l_355_2.createInterface(routers.get(355).createInterface("if_2"));
		l_355_2.createInterface(routers.get(2).createInterface("if_355"));

		ILink l_374_2 = rv.createLink("l_374_2");
		l_374_2.createInterface(routers.get(374).createInterface("if_2"));
		l_374_2.createInterface(routers.get(2).createInterface("if_374"));

		ILink l_382_2 = rv.createLink("l_382_2");
		l_382_2.createInterface(routers.get(382).createInterface("if_2"));
		l_382_2.createInterface(routers.get(2).createInterface("if_382"));

		ILink l_397_2 = rv.createLink("l_397_2");
		l_397_2.createInterface(routers.get(397).createInterface("if_2"));
		l_397_2.createInterface(routers.get(2).createInterface("if_397"));

		ILink l_398_2 = rv.createLink("l_398_2");
		l_398_2.createInterface(routers.get(398).createInterface("if_2"));
		l_398_2.createInterface(routers.get(2).createInterface("if_398"));

		ILink l_399_2 = rv.createLink("l_399_2");
		l_399_2.createInterface(routers.get(399).createInterface("if_2"));
		l_399_2.createInterface(routers.get(2).createInterface("if_399"));

		ILink l_400_2 = rv.createLink("l_400_2");
		l_400_2.createInterface(routers.get(400).createInterface("if_2"));
		l_400_2.createInterface(routers.get(2).createInterface("if_400"));

		ILink l_21_20 = rv.createLink("l_21_20");
		l_21_20.createInterface(routers.get(21).createInterface("if_20"));
		l_21_20.createInterface(routers.get(20).createInterface("if_21"));

		ILink l_22_20 = rv.createLink("l_22_20");
		l_22_20.createInterface(routers.get(22).createInterface("if_20"));
		l_22_20.createInterface(routers.get(20).createInterface("if_22"));

		ILink l_40_21 = rv.createLink("l_40_21");
		l_40_21.createInterface(routers.get(40).createInterface("if_21"));
		l_40_21.createInterface(routers.get(21).createInterface("if_40"));

		ILink l_41_21 = rv.createLink("l_41_21");
		l_41_21.createInterface(routers.get(41).createInterface("if_21"));
		l_41_21.createInterface(routers.get(21).createInterface("if_41"));

		ILink l_75_21 = rv.createLink("l_75_21");
		l_75_21.createInterface(routers.get(75).createInterface("if_21"));
		l_75_21.createInterface(routers.get(21).createInterface("if_75"));

		ILink l_131_21 = rv.createLink("l_131_21");
		l_131_21.createInterface(routers.get(131).createInterface("if_21"));
		l_131_21.createInterface(routers.get(21).createInterface("if_131"));

		ILink l_132_21 = rv.createLink("l_132_21");
		l_132_21.createInterface(routers.get(132).createInterface("if_21"));
		l_132_21.createInterface(routers.get(21).createInterface("if_132"));

		ILink l_139_21 = rv.createLink("l_139_21");
		l_139_21.createInterface(routers.get(139).createInterface("if_21"));
		l_139_21.createInterface(routers.get(21).createInterface("if_139"));

		ILink l_163_21 = rv.createLink("l_163_21");
		l_163_21.createInterface(routers.get(163).createInterface("if_21"));
		l_163_21.createInterface(routers.get(21).createInterface("if_163"));

		ILink l_197_21 = rv.createLink("l_197_21");
		l_197_21.createInterface(routers.get(197).createInterface("if_21"));
		l_197_21.createInterface(routers.get(21).createInterface("if_197"));

		ILink l_227_21 = rv.createLink("l_227_21");
		l_227_21.createInterface(routers.get(227).createInterface("if_21"));
		l_227_21.createInterface(routers.get(21).createInterface("if_227"));

		ILink l_241_21 = rv.createLink("l_241_21");
		l_241_21.createInterface(routers.get(241).createInterface("if_21"));
		l_241_21.createInterface(routers.get(21).createInterface("if_241"));

		ILink l_247_21 = rv.createLink("l_247_21");
		l_247_21.createInterface(routers.get(247).createInterface("if_21"));
		l_247_21.createInterface(routers.get(21).createInterface("if_247"));

		ILink l_248_21 = rv.createLink("l_248_21");
		l_248_21.createInterface(routers.get(248).createInterface("if_21"));
		l_248_21.createInterface(routers.get(21).createInterface("if_248"));

		ILink l_258_21 = rv.createLink("l_258_21");
		l_258_21.createInterface(routers.get(258).createInterface("if_21"));
		l_258_21.createInterface(routers.get(21).createInterface("if_258"));

		ILink l_274_21 = rv.createLink("l_274_21");
		l_274_21.createInterface(routers.get(274).createInterface("if_21"));
		l_274_21.createInterface(routers.get(21).createInterface("if_274"));

		ILink l_279_21 = rv.createLink("l_279_21");
		l_279_21.createInterface(routers.get(279).createInterface("if_21"));
		l_279_21.createInterface(routers.get(21).createInterface("if_279"));

		ILink l_289_21 = rv.createLink("l_289_21");
		l_289_21.createInterface(routers.get(289).createInterface("if_21"));
		l_289_21.createInterface(routers.get(21).createInterface("if_289"));

		ILink l_295_21 = rv.createLink("l_295_21");
		l_295_21.createInterface(routers.get(295).createInterface("if_21"));
		l_295_21.createInterface(routers.get(21).createInterface("if_295"));

		ILink l_321_21 = rv.createLink("l_321_21");
		l_321_21.createInterface(routers.get(321).createInterface("if_21"));
		l_321_21.createInterface(routers.get(21).createInterface("if_321"));

		ILink l_339_21 = rv.createLink("l_339_21");
		l_339_21.createInterface(routers.get(339).createInterface("if_21"));
		l_339_21.createInterface(routers.get(21).createInterface("if_339"));

		ILink l_355_21 = rv.createLink("l_355_21");
		l_355_21.createInterface(routers.get(355).createInterface("if_21"));
		l_355_21.createInterface(routers.get(21).createInterface("if_355"));

		ILink l_374_21 = rv.createLink("l_374_21");
		l_374_21.createInterface(routers.get(374).createInterface("if_21"));
		l_374_21.createInterface(routers.get(21).createInterface("if_374"));

		ILink l_382_21 = rv.createLink("l_382_21");
		l_382_21.createInterface(routers.get(382).createInterface("if_21"));
		l_382_21.createInterface(routers.get(21).createInterface("if_382"));

		ILink l_397_21 = rv.createLink("l_397_21");
		l_397_21.createInterface(routers.get(397).createInterface("if_21"));
		l_397_21.createInterface(routers.get(21).createInterface("if_397"));

		ILink l_398_21 = rv.createLink("l_398_21");
		l_398_21.createInterface(routers.get(398).createInterface("if_21"));
		l_398_21.createInterface(routers.get(21).createInterface("if_398"));

		ILink l_399_21 = rv.createLink("l_399_21");
		l_399_21.createInterface(routers.get(399).createInterface("if_21"));
		l_399_21.createInterface(routers.get(21).createInterface("if_399"));

		ILink l_400_21 = rv.createLink("l_400_21");
		l_400_21.createInterface(routers.get(400).createInterface("if_21"));
		l_400_21.createInterface(routers.get(21).createInterface("if_400"));

		ILink l_40_22 = rv.createLink("l_40_22");
		l_40_22.createInterface(routers.get(40).createInterface("if_22"));
		l_40_22.createInterface(routers.get(22).createInterface("if_40"));

		ILink l_41_22 = rv.createLink("l_41_22");
		l_41_22.createInterface(routers.get(41).createInterface("if_22"));
		l_41_22.createInterface(routers.get(22).createInterface("if_41"));

		ILink l_75_22 = rv.createLink("l_75_22");
		l_75_22.createInterface(routers.get(75).createInterface("if_22"));
		l_75_22.createInterface(routers.get(22).createInterface("if_75"));

		ILink l_131_22 = rv.createLink("l_131_22");
		l_131_22.createInterface(routers.get(131).createInterface("if_22"));
		l_131_22.createInterface(routers.get(22).createInterface("if_131"));

		ILink l_132_22 = rv.createLink("l_132_22");
		l_132_22.createInterface(routers.get(132).createInterface("if_22"));
		l_132_22.createInterface(routers.get(22).createInterface("if_132"));

		ILink l_139_22 = rv.createLink("l_139_22");
		l_139_22.createInterface(routers.get(139).createInterface("if_22"));
		l_139_22.createInterface(routers.get(22).createInterface("if_139"));

		ILink l_163_22 = rv.createLink("l_163_22");
		l_163_22.createInterface(routers.get(163).createInterface("if_22"));
		l_163_22.createInterface(routers.get(22).createInterface("if_163"));

		ILink l_197_22 = rv.createLink("l_197_22");
		l_197_22.createInterface(routers.get(197).createInterface("if_22"));
		l_197_22.createInterface(routers.get(22).createInterface("if_197"));

		ILink l_227_22 = rv.createLink("l_227_22");
		l_227_22.createInterface(routers.get(227).createInterface("if_22"));
		l_227_22.createInterface(routers.get(22).createInterface("if_227"));

		ILink l_241_22 = rv.createLink("l_241_22");
		l_241_22.createInterface(routers.get(241).createInterface("if_22"));
		l_241_22.createInterface(routers.get(22).createInterface("if_241"));

		ILink l_247_22 = rv.createLink("l_247_22");
		l_247_22.createInterface(routers.get(247).createInterface("if_22"));
		l_247_22.createInterface(routers.get(22).createInterface("if_247"));

		ILink l_248_22 = rv.createLink("l_248_22");
		l_248_22.createInterface(routers.get(248).createInterface("if_22"));
		l_248_22.createInterface(routers.get(22).createInterface("if_248"));

		ILink l_258_22 = rv.createLink("l_258_22");
		l_258_22.createInterface(routers.get(258).createInterface("if_22"));
		l_258_22.createInterface(routers.get(22).createInterface("if_258"));

		ILink l_274_22 = rv.createLink("l_274_22");
		l_274_22.createInterface(routers.get(274).createInterface("if_22"));
		l_274_22.createInterface(routers.get(22).createInterface("if_274"));

		ILink l_279_22 = rv.createLink("l_279_22");
		l_279_22.createInterface(routers.get(279).createInterface("if_22"));
		l_279_22.createInterface(routers.get(22).createInterface("if_279"));

		ILink l_289_22 = rv.createLink("l_289_22");
		l_289_22.createInterface(routers.get(289).createInterface("if_22"));
		l_289_22.createInterface(routers.get(22).createInterface("if_289"));

		ILink l_295_22 = rv.createLink("l_295_22");
		l_295_22.createInterface(routers.get(295).createInterface("if_22"));
		l_295_22.createInterface(routers.get(22).createInterface("if_295"));

		ILink l_321_22 = rv.createLink("l_321_22");
		l_321_22.createInterface(routers.get(321).createInterface("if_22"));
		l_321_22.createInterface(routers.get(22).createInterface("if_321"));

		ILink l_339_22 = rv.createLink("l_339_22");
		l_339_22.createInterface(routers.get(339).createInterface("if_22"));
		l_339_22.createInterface(routers.get(22).createInterface("if_339"));

		ILink l_355_22 = rv.createLink("l_355_22");
		l_355_22.createInterface(routers.get(355).createInterface("if_22"));
		l_355_22.createInterface(routers.get(22).createInterface("if_355"));

		ILink l_374_22 = rv.createLink("l_374_22");
		l_374_22.createInterface(routers.get(374).createInterface("if_22"));
		l_374_22.createInterface(routers.get(22).createInterface("if_374"));

		ILink l_382_22 = rv.createLink("l_382_22");
		l_382_22.createInterface(routers.get(382).createInterface("if_22"));
		l_382_22.createInterface(routers.get(22).createInterface("if_382"));

		ILink l_397_22 = rv.createLink("l_397_22");
		l_397_22.createInterface(routers.get(397).createInterface("if_22"));
		l_397_22.createInterface(routers.get(22).createInterface("if_397"));

		ILink l_398_22 = rv.createLink("l_398_22");
		l_398_22.createInterface(routers.get(398).createInterface("if_22"));
		l_398_22.createInterface(routers.get(22).createInterface("if_398"));

		ILink l_399_22 = rv.createLink("l_399_22");
		l_399_22.createInterface(routers.get(399).createInterface("if_22"));
		l_399_22.createInterface(routers.get(22).createInterface("if_399"));

		ILink l_400_22 = rv.createLink("l_400_22");
		l_400_22.createInterface(routers.get(400).createInterface("if_22"));
		l_400_22.createInterface(routers.get(22).createInterface("if_400"));

		ILink l_41_40 = rv.createLink("l_41_40");
		l_41_40.createInterface(routers.get(41).createInterface("if_40"));
		l_41_40.createInterface(routers.get(40).createInterface("if_41"));

		ILink l_132_41 = rv.createLink("l_132_41");
		l_132_41.createInterface(routers.get(132).createInterface("if_41"));
		l_132_41.createInterface(routers.get(41).createInterface("if_132"));

		ILink l_241_41 = rv.createLink("l_241_41");
		l_241_41.createInterface(routers.get(241).createInterface("if_41"));
		l_241_41.createInterface(routers.get(41).createInterface("if_241"));

		ILink l_247_41 = rv.createLink("l_247_41");
		l_247_41.createInterface(routers.get(247).createInterface("if_41"));
		l_247_41.createInterface(routers.get(41).createInterface("if_247"));

		ILink l_258_41 = rv.createLink("l_258_41");
		l_258_41.createInterface(routers.get(258).createInterface("if_41"));
		l_258_41.createInterface(routers.get(41).createInterface("if_258"));

		ILink l_289_41 = rv.createLink("l_289_41");
		l_289_41.createInterface(routers.get(289).createInterface("if_41"));
		l_289_41.createInterface(routers.get(41).createInterface("if_289"));

		ILink l_339_41 = rv.createLink("l_339_41");
		l_339_41.createInterface(routers.get(339).createInterface("if_41"));
		l_339_41.createInterface(routers.get(41).createInterface("if_339"));

		ILink l_614_41 = rv.createLink("l_614_41");
		l_614_41.createInterface(routers.get(614).createInterface("if_41"));
		l_614_41.createInterface(routers.get(41).createInterface("if_614"));

		ILink l_140_139 = rv.createLink("l_140_139");
		l_140_139.createInterface(routers.get(140).createInterface("if_139"));
		l_140_139.createInterface(routers.get(139).createInterface("if_140"));

		ILink l_141_139 = rv.createLink("l_141_139");
		l_141_139.createInterface(routers.get(141).createInterface("if_139"));
		l_141_139.createInterface(routers.get(139).createInterface("if_141"));

		ILink l_153_140 = rv.createLink("l_153_140");
		l_153_140.createInterface(routers.get(153).createInterface("if_140"));
		l_153_140.createInterface(routers.get(140).createInterface("if_153"));

		ILink l_163_162 = rv.createLink("l_163_162");
		l_163_162.createInterface(routers.get(163).createInterface("if_162"));
		l_163_162.createInterface(routers.get(162).createInterface("if_163"));

		ILink l_274_273 = rv.createLink("l_274_273");
		l_274_273.createInterface(routers.get(274).createInterface("if_273"));
		l_274_273.createInterface(routers.get(273).createInterface("if_274"));

		ILink l_340_339 = rv.createLink("l_340_339");
		l_340_339.createInterface(routers.get(340).createInterface("if_339"));
		l_340_339.createInterface(routers.get(339).createInterface("if_340"));

		ILink l_341_339 = rv.createLink("l_341_339");
		l_341_339.createInterface(routers.get(341).createInterface("if_339"));
		l_341_339.createInterface(routers.get(339).createInterface("if_341"));

		ILink l_355_340 = rv.createLink("l_355_340");
		l_355_340.createInterface(routers.get(355).createInterface("if_340"));
		l_355_340.createInterface(routers.get(340).createInterface("if_355"));

		ILink l_355_341 = rv.createLink("l_355_341");
		l_355_341.createInterface(routers.get(355).createInterface("if_341"));
		l_355_341.createInterface(routers.get(341).createInterface("if_355"));

		ILink l_354_353 = rv.createLink("l_354_353");
		l_354_353.createInterface(routers.get(354).createInterface("if_353"));
		l_354_353.createInterface(routers.get(353).createInterface("if_354"));

		ILink l_355_353 = rv.createLink("l_355_353");
		l_355_353.createInterface(routers.get(355).createInterface("if_353"));
		l_355_353.createInterface(routers.get(353).createInterface("if_355"));

		ILink l_617_355 = rv.createLink("l_617_355");
		l_617_355.createInterface(routers.get(617).createInterface("if_355"));
		l_617_355.createInterface(routers.get(355).createInterface("if_617"));

		//attach campus networks

		INet campus_1 = null;
		IRouter campus_r_1 = null;
		if(null == baseCampus) {
			campus_1 = rv.createNet("campus_8_1");
			campus_r_1 = createCampus(campus_1,do_spherical);
		}else {
			campus_1 = rv.createNetReplica("campus_8_1",baseCampus);
			campus_r_1 = (IRouter)campus_1.getChildByName("sub_campus_router");
		}
		ILink l_campus_1 = rv.createLink("l_campus_1");
		l_campus_1.createInterface(r_1.createInterface("if_campus_1"));
		l_campus_1.createInterface((IInterface)campus_r_1.getChildByName("if_stub"));

		INet campus_141 = null;
		IRouter campus_r_141 = null;
		if(null == baseCampus) {
			campus_141 = rv.createNet("campus_8_141");
			campus_r_141 = createCampus(campus_141,do_spherical);
		}else {
			campus_141 = rv.createNetReplica("campus_8_141",baseCampus);
			campus_r_141 = (IRouter)campus_141.getChildByName("sub_campus_router");
		}
		ILink l_campus_141 = rv.createLink("l_campus_141");
		l_campus_141.createInterface(r_141.createInterface("if_campus_141"));
		l_campus_141.createInterface((IInterface)campus_r_141.getChildByName("if_stub"));

		INet campus_153 = null;
		IRouter campus_r_153 = null;
		if(null == baseCampus) {
			campus_153 = rv.createNet("campus_8_153");
			campus_r_153 = createCampus(campus_153,do_spherical);
		}else {
			campus_153 = rv.createNetReplica("campus_8_153",baseCampus);
			campus_r_153 = (IRouter)campus_153.getChildByName("sub_campus_router");
		}
		ILink l_campus_153 = rv.createLink("l_campus_153");
		l_campus_153.createInterface(r_153.createInterface("if_campus_153"));
		l_campus_153.createInterface((IInterface)campus_r_153.getChildByName("if_stub"));

		INet campus_162 = null;
		IRouter campus_r_162 = null;
		if(null == baseCampus) {
			campus_162 = rv.createNet("campus_8_162");
			campus_r_162 = createCampus(campus_162,do_spherical);
		}else {
			campus_162 = rv.createNetReplica("campus_8_162",baseCampus);
			campus_r_162 = (IRouter)campus_162.getChildByName("sub_campus_router");
		}
		ILink l_campus_162 = rv.createLink("l_campus_162");
		l_campus_162.createInterface(r_162.createInterface("if_campus_162"));
		l_campus_162.createInterface((IInterface)campus_r_162.getChildByName("if_stub"));

		INet campus_273 = null;
		IRouter campus_r_273 = null;
		if(null == baseCampus) {
			campus_273 = rv.createNet("campus_8_273");
			campus_r_273 = createCampus(campus_273,do_spherical);
		}else {
			campus_273 = rv.createNetReplica("campus_8_273",baseCampus);
			campus_r_273 = (IRouter)campus_273.getChildByName("sub_campus_router");
		}
		ILink l_campus_273 = rv.createLink("l_campus_273");
		l_campus_273.createInterface(r_273.createInterface("if_campus_273"));
		l_campus_273.createInterface((IInterface)campus_r_273.getChildByName("if_stub"));

		INet campus_354 = null;
		IRouter campus_r_354 = null;
		if(null == baseCampus) {
			campus_354 = rv.createNet("campus_8_354");
			campus_r_354 = createCampus(campus_354,do_spherical);
		}else {
			campus_354 = rv.createNetReplica("campus_8_354",baseCampus);
			campus_r_354 = (IRouter)campus_354.getChildByName("sub_campus_router");
		}
		ILink l_campus_354 = rv.createLink("l_campus_354");
		l_campus_354.createInterface(r_354.createInterface("if_campus_354"));
		l_campus_354.createInterface((IInterface)campus_r_354.getChildByName("if_stub"));

		INet campus_614 = null;
		IRouter campus_r_614 = null;
		if(null == baseCampus) {
			campus_614 = rv.createNet("campus_8_614");
			campus_r_614 = createCampus(campus_614,do_spherical);
		}else {
			campus_614 = rv.createNetReplica("campus_8_614",baseCampus);
			campus_r_614 = (IRouter)campus_614.getChildByName("sub_campus_router");
		}
		ILink l_campus_614 = rv.createLink("l_campus_614");
		l_campus_614.createInterface(r_614.createInterface("if_campus_614"));
		l_campus_614.createInterface((IInterface)campus_r_614.getChildByName("if_stub"));

		INet campus_617 = null;
		IRouter campus_r_617 = null;
		if(null == baseCampus) {
			campus_617 = rv.createNet("campus_8_617");
			campus_r_617 = createCampus(campus_617,do_spherical);
		}else {
			campus_617 = rv.createNetReplica("campus_8_617",baseCampus);
			campus_r_617 = (IRouter)campus_617.getChildByName("sub_campus_router");
		}
		ILink l_campus_617 = rv.createLink("l_campus_617");
		l_campus_617.createInterface(r_617.createInterface("if_campus_617"));
		l_campus_617.createInterface((IInterface)campus_r_617.getChildByName("if_stub"));
		return rv;
	}
	public INet create_net_9(INet top, Map<Integer,IRouter> routers) {
		INet rv = top.createNet("net_9");
		if(do_spherical) rv.createShortestPath();;

		//create routers
		IRouter r_5 = rv.createRouter("r_5");
		routers.put(5,r_5);

		IRouter r_6 = rv.createRouter("r_6");
		routers.put(6,r_6);

		IRouter r_12 = rv.createRouter("r_12");
		routers.put(12,r_12);

		IRouter r_14 = rv.createRouter("r_14");
		routers.put(14,r_14);

		IRouter r_19 = rv.createRouter("r_19");
		routers.put(19,r_19);

		IRouter r_26 = rv.createRouter("r_26");
		routers.put(26,r_26);

		IRouter r_35 = rv.createRouter("r_35");
		routers.put(35,r_35);

		IRouter r_36 = rv.createRouter("r_36");
		routers.put(36,r_36);

		IRouter r_37 = rv.createRouter("r_37");
		routers.put(37,r_37);

		IRouter r_38 = rv.createRouter("r_38");
		routers.put(38,r_38);

		IRouter r_39 = rv.createRouter("r_39");
		routers.put(39,r_39);

		IRouter r_62 = rv.createRouter("r_62");
		routers.put(62,r_62);

		IRouter r_77 = rv.createRouter("r_77");
		routers.put(77,r_77);

		IRouter r_80 = rv.createRouter("r_80");
		routers.put(80,r_80);

		IRouter r_108 = rv.createRouter("r_108");
		routers.put(108,r_108);

		IRouter r_111 = rv.createRouter("r_111");
		routers.put(111,r_111);

		IRouter r_116 = rv.createRouter("r_116");
		routers.put(116,r_116);

		IRouter r_120 = rv.createRouter("r_120");
		routers.put(120,r_120);

		IRouter r_133 = rv.createRouter("r_133");
		routers.put(133,r_133);

		IRouter r_134 = rv.createRouter("r_134");
		routers.put(134,r_134);

		IRouter r_177 = rv.createRouter("r_177");
		routers.put(177,r_177);

		IRouter r_178 = rv.createRouter("r_178");
		routers.put(178,r_178);

		IRouter r_180 = rv.createRouter("r_180");
		routers.put(180,r_180);

		IRouter r_184 = rv.createRouter("r_184");
		routers.put(184,r_184);

		IRouter r_185 = rv.createRouter("r_185");
		routers.put(185,r_185);

		IRouter r_191 = rv.createRouter("r_191");
		routers.put(191,r_191);

		IRouter r_213 = rv.createRouter("r_213");
		routers.put(213,r_213);

		IRouter r_265 = rv.createRouter("r_265");
		routers.put(265,r_265);

		IRouter r_266 = rv.createRouter("r_266");
		routers.put(266,r_266);

		IRouter r_268 = rv.createRouter("r_268");
		routers.put(268,r_268);

		IRouter r_271 = rv.createRouter("r_271");
		routers.put(271,r_271);

		IRouter r_311 = rv.createRouter("r_311");
		routers.put(311,r_311);

		IRouter r_312 = rv.createRouter("r_312");
		routers.put(312,r_312);

		IRouter r_326 = rv.createRouter("r_326");
		routers.put(326,r_326);

		IRouter r_327 = rv.createRouter("r_327");
		routers.put(327,r_327);

		IRouter r_378 = rv.createRouter("r_378");
		routers.put(378,r_378);

		IRouter r_520 = rv.createRouter("r_520");
		routers.put(520,r_520);

		IRouter r_629 = rv.createRouter("r_629");
		routers.put(629,r_629);

		IRouter r_630 = rv.createRouter("r_630");
		routers.put(630,r_630);

		//create links
		ILink l_6_5 = rv.createLink("l_6_5");
		l_6_5.createInterface(routers.get(6).createInterface("if_5"));
		l_6_5.createInterface(routers.get(5).createInterface("if_6"));

		ILink l_36_35 = rv.createLink("l_36_35");
		l_36_35.createInterface(routers.get(36).createInterface("if_35"));
		l_36_35.createInterface(routers.get(35).createInterface("if_36"));

		ILink l_37_35 = rv.createLink("l_37_35");
		l_37_35.createInterface(routers.get(37).createInterface("if_35"));
		l_37_35.createInterface(routers.get(35).createInterface("if_37"));

		ILink l_62_37 = rv.createLink("l_62_37");
		l_62_37.createInterface(routers.get(62).createInterface("if_37"));
		l_62_37.createInterface(routers.get(37).createInterface("if_62"));

		ILink l_108_37 = rv.createLink("l_108_37");
		l_108_37.createInterface(routers.get(108).createInterface("if_37"));
		l_108_37.createInterface(routers.get(37).createInterface("if_108"));

		ILink l_185_37 = rv.createLink("l_185_37");
		l_185_37.createInterface(routers.get(185).createInterface("if_37"));
		l_185_37.createInterface(routers.get(37).createInterface("if_185"));

		ILink l_213_37 = rv.createLink("l_213_37");
		l_213_37.createInterface(routers.get(213).createInterface("if_37"));
		l_213_37.createInterface(routers.get(37).createInterface("if_213"));

		ILink l_266_37 = rv.createLink("l_266_37");
		l_266_37.createInterface(routers.get(266).createInterface("if_37"));
		l_266_37.createInterface(routers.get(37).createInterface("if_266"));

		ILink l_271_37 = rv.createLink("l_271_37");
		l_271_37.createInterface(routers.get(271).createInterface("if_37"));
		l_271_37.createInterface(routers.get(37).createInterface("if_271"));

		ILink l_520_37 = rv.createLink("l_520_37");
		l_520_37.createInterface(routers.get(520).createInterface("if_37"));
		l_520_37.createInterface(routers.get(37).createInterface("if_520"));

		ILink l_178_177 = rv.createLink("l_178_177");
		l_178_177.createInterface(routers.get(178).createInterface("if_177"));
		l_178_177.createInterface(routers.get(177).createInterface("if_178"));

		ILink l_180_177 = rv.createLink("l_180_177");
		l_180_177.createInterface(routers.get(180).createInterface("if_177"));
		l_180_177.createInterface(routers.get(177).createInterface("if_180"));

		ILink l_191_180 = rv.createLink("l_191_180");
		l_191_180.createInterface(routers.get(191).createInterface("if_180"));
		l_191_180.createInterface(routers.get(180).createInterface("if_191"));

		ILink l_311_180 = rv.createLink("l_311_180");
		l_311_180.createInterface(routers.get(311).createInterface("if_180"));
		l_311_180.createInterface(routers.get(180).createInterface("if_311"));

		ILink l_312_180 = rv.createLink("l_312_180");
		l_312_180.createInterface(routers.get(312).createInterface("if_180"));
		l_312_180.createInterface(routers.get(180).createInterface("if_312"));

		ILink l_327_180 = rv.createLink("l_327_180");
		l_327_180.createInterface(routers.get(327).createInterface("if_180"));
		l_327_180.createInterface(routers.get(180).createInterface("if_327"));

		ILink l_629_180 = rv.createLink("l_629_180");
		l_629_180.createInterface(routers.get(629).createInterface("if_180"));
		l_629_180.createInterface(routers.get(180).createInterface("if_629"));

		ILink l_630_180 = rv.createLink("l_630_180");
		l_630_180.createInterface(routers.get(630).createInterface("if_180"));
		l_630_180.createInterface(routers.get(180).createInterface("if_630"));

		//attach campus networks

		INet campus_5 = null;
		IRouter campus_r_5 = null;
		if(null == baseCampus) {
			campus_5 = rv.createNet("campus_9_5");
			campus_r_5 = createCampus(campus_5,do_spherical);
		}else {
			campus_5 = rv.createNetReplica("campus_9_5",baseCampus);
			campus_r_5 = (IRouter)campus_5.getChildByName("sub_campus_router");
		}
		ILink l_campus_5 = rv.createLink("l_campus_5");
		l_campus_5.createInterface(r_5.createInterface("if_campus_5"));
		l_campus_5.createInterface((IInterface)campus_r_5.getChildByName("if_stub"));

		INet campus_12 = null;
		IRouter campus_r_12 = null;
		if(null == baseCampus) {
			campus_12 = rv.createNet("campus_9_12");
			campus_r_12 = createCampus(campus_12,do_spherical);
		}else {
			campus_12 = rv.createNetReplica("campus_9_12",baseCampus);
			campus_r_12 = (IRouter)campus_12.getChildByName("sub_campus_router");
		}
		ILink l_campus_12 = rv.createLink("l_campus_12");
		l_campus_12.createInterface(r_12.createInterface("if_campus_12"));
		l_campus_12.createInterface((IInterface)campus_r_12.getChildByName("if_stub"));

		INet campus_14 = null;
		IRouter campus_r_14 = null;
		if(null == baseCampus) {
			campus_14 = rv.createNet("campus_9_14");
			campus_r_14 = createCampus(campus_14,do_spherical);
		}else {
			campus_14 = rv.createNetReplica("campus_9_14",baseCampus);
			campus_r_14 = (IRouter)campus_14.getChildByName("sub_campus_router");
		}
		ILink l_campus_14 = rv.createLink("l_campus_14");
		l_campus_14.createInterface(r_14.createInterface("if_campus_14"));
		l_campus_14.createInterface((IInterface)campus_r_14.getChildByName("if_stub"));

		INet campus_19 = null;
		IRouter campus_r_19 = null;
		if(null == baseCampus) {
			campus_19 = rv.createNet("campus_9_19");
			campus_r_19 = createCampus(campus_19,do_spherical);
		}else {
			campus_19 = rv.createNetReplica("campus_9_19",baseCampus);
			campus_r_19 = (IRouter)campus_19.getChildByName("sub_campus_router");
		}
		ILink l_campus_19 = rv.createLink("l_campus_19");
		l_campus_19.createInterface(r_19.createInterface("if_campus_19"));
		l_campus_19.createInterface((IInterface)campus_r_19.getChildByName("if_stub"));

		INet campus_26 = null;
		IRouter campus_r_26 = null;
		if(null == baseCampus) {
			campus_26 = rv.createNet("campus_9_26");
			campus_r_26 = createCampus(campus_26,do_spherical);
		}else {
			campus_26 = rv.createNetReplica("campus_9_26",baseCampus);
			campus_r_26 = (IRouter)campus_26.getChildByName("sub_campus_router");
		}
		ILink l_campus_26 = rv.createLink("l_campus_26");
		l_campus_26.createInterface(r_26.createInterface("if_campus_26"));
		l_campus_26.createInterface((IInterface)campus_r_26.getChildByName("if_stub"));

		INet campus_36 = null;
		IRouter campus_r_36 = null;
		if(null == baseCampus) {
			campus_36 = rv.createNet("campus_9_36");
			campus_r_36 = createCampus(campus_36,do_spherical);
		}else {
			campus_36 = rv.createNetReplica("campus_9_36",baseCampus);
			campus_r_36 = (IRouter)campus_36.getChildByName("sub_campus_router");
		}
		ILink l_campus_36 = rv.createLink("l_campus_36");
		l_campus_36.createInterface(r_36.createInterface("if_campus_36"));
		l_campus_36.createInterface((IInterface)campus_r_36.getChildByName("if_stub"));

		INet campus_38 = null;
		IRouter campus_r_38 = null;
		if(null == baseCampus) {
			campus_38 = rv.createNet("campus_9_38");
			campus_r_38 = createCampus(campus_38,do_spherical);
		}else {
			campus_38 = rv.createNetReplica("campus_9_38",baseCampus);
			campus_r_38 = (IRouter)campus_38.getChildByName("sub_campus_router");
		}
		ILink l_campus_38 = rv.createLink("l_campus_38");
		l_campus_38.createInterface(r_38.createInterface("if_campus_38"));
		l_campus_38.createInterface((IInterface)campus_r_38.getChildByName("if_stub"));

		INet campus_39 = null;
		IRouter campus_r_39 = null;
		if(null == baseCampus) {
			campus_39 = rv.createNet("campus_9_39");
			campus_r_39 = createCampus(campus_39,do_spherical);
		}else {
			campus_39 = rv.createNetReplica("campus_9_39",baseCampus);
			campus_r_39 = (IRouter)campus_39.getChildByName("sub_campus_router");
		}
		ILink l_campus_39 = rv.createLink("l_campus_39");
		l_campus_39.createInterface(r_39.createInterface("if_campus_39"));
		l_campus_39.createInterface((IInterface)campus_r_39.getChildByName("if_stub"));

		INet campus_62 = null;
		IRouter campus_r_62 = null;
		if(null == baseCampus) {
			campus_62 = rv.createNet("campus_9_62");
			campus_r_62 = createCampus(campus_62,do_spherical);
		}else {
			campus_62 = rv.createNetReplica("campus_9_62",baseCampus);
			campus_r_62 = (IRouter)campus_62.getChildByName("sub_campus_router");
		}
		ILink l_campus_62 = rv.createLink("l_campus_62");
		l_campus_62.createInterface(r_62.createInterface("if_campus_62"));
		l_campus_62.createInterface((IInterface)campus_r_62.getChildByName("if_stub"));

		INet campus_77 = null;
		IRouter campus_r_77 = null;
		if(null == baseCampus) {
			campus_77 = rv.createNet("campus_9_77");
			campus_r_77 = createCampus(campus_77,do_spherical);
		}else {
			campus_77 = rv.createNetReplica("campus_9_77",baseCampus);
			campus_r_77 = (IRouter)campus_77.getChildByName("sub_campus_router");
		}
		ILink l_campus_77 = rv.createLink("l_campus_77");
		l_campus_77.createInterface(r_77.createInterface("if_campus_77"));
		l_campus_77.createInterface((IInterface)campus_r_77.getChildByName("if_stub"));

		INet campus_80 = null;
		IRouter campus_r_80 = null;
		if(null == baseCampus) {
			campus_80 = rv.createNet("campus_9_80");
			campus_r_80 = createCampus(campus_80,do_spherical);
		}else {
			campus_80 = rv.createNetReplica("campus_9_80",baseCampus);
			campus_r_80 = (IRouter)campus_80.getChildByName("sub_campus_router");
		}
		ILink l_campus_80 = rv.createLink("l_campus_80");
		l_campus_80.createInterface(r_80.createInterface("if_campus_80"));
		l_campus_80.createInterface((IInterface)campus_r_80.getChildByName("if_stub"));

		INet campus_108 = null;
		IRouter campus_r_108 = null;
		if(null == baseCampus) {
			campus_108 = rv.createNet("campus_9_108");
			campus_r_108 = createCampus(campus_108,do_spherical);
		}else {
			campus_108 = rv.createNetReplica("campus_9_108",baseCampus);
			campus_r_108 = (IRouter)campus_108.getChildByName("sub_campus_router");
		}
		ILink l_campus_108 = rv.createLink("l_campus_108");
		l_campus_108.createInterface(r_108.createInterface("if_campus_108"));
		l_campus_108.createInterface((IInterface)campus_r_108.getChildByName("if_stub"));

		INet campus_111 = null;
		IRouter campus_r_111 = null;
		if(null == baseCampus) {
			campus_111 = rv.createNet("campus_9_111");
			campus_r_111 = createCampus(campus_111,do_spherical);
		}else {
			campus_111 = rv.createNetReplica("campus_9_111",baseCampus);
			campus_r_111 = (IRouter)campus_111.getChildByName("sub_campus_router");
		}
		ILink l_campus_111 = rv.createLink("l_campus_111");
		l_campus_111.createInterface(r_111.createInterface("if_campus_111"));
		l_campus_111.createInterface((IInterface)campus_r_111.getChildByName("if_stub"));

		INet campus_116 = null;
		IRouter campus_r_116 = null;
		if(null == baseCampus) {
			campus_116 = rv.createNet("campus_9_116");
			campus_r_116 = createCampus(campus_116,do_spherical);
		}else {
			campus_116 = rv.createNetReplica("campus_9_116",baseCampus);
			campus_r_116 = (IRouter)campus_116.getChildByName("sub_campus_router");
		}
		ILink l_campus_116 = rv.createLink("l_campus_116");
		l_campus_116.createInterface(r_116.createInterface("if_campus_116"));
		l_campus_116.createInterface((IInterface)campus_r_116.getChildByName("if_stub"));

		INet campus_120 = null;
		IRouter campus_r_120 = null;
		if(null == baseCampus) {
			campus_120 = rv.createNet("campus_9_120");
			campus_r_120 = createCampus(campus_120,do_spherical);
		}else {
			campus_120 = rv.createNetReplica("campus_9_120",baseCampus);
			campus_r_120 = (IRouter)campus_120.getChildByName("sub_campus_router");
		}
		ILink l_campus_120 = rv.createLink("l_campus_120");
		l_campus_120.createInterface(r_120.createInterface("if_campus_120"));
		l_campus_120.createInterface((IInterface)campus_r_120.getChildByName("if_stub"));

		INet campus_133 = null;
		IRouter campus_r_133 = null;
		if(null == baseCampus) {
			campus_133 = rv.createNet("campus_9_133");
			campus_r_133 = createCampus(campus_133,do_spherical);
		}else {
			campus_133 = rv.createNetReplica("campus_9_133",baseCampus);
			campus_r_133 = (IRouter)campus_133.getChildByName("sub_campus_router");
		}
		ILink l_campus_133 = rv.createLink("l_campus_133");
		l_campus_133.createInterface(r_133.createInterface("if_campus_133"));
		l_campus_133.createInterface((IInterface)campus_r_133.getChildByName("if_stub"));

		INet campus_134 = null;
		IRouter campus_r_134 = null;
		if(null == baseCampus) {
			campus_134 = rv.createNet("campus_9_134");
			campus_r_134 = createCampus(campus_134,do_spherical);
		}else {
			campus_134 = rv.createNetReplica("campus_9_134",baseCampus);
			campus_r_134 = (IRouter)campus_134.getChildByName("sub_campus_router");
		}
		ILink l_campus_134 = rv.createLink("l_campus_134");
		l_campus_134.createInterface(r_134.createInterface("if_campus_134"));
		l_campus_134.createInterface((IInterface)campus_r_134.getChildByName("if_stub"));

		INet campus_178 = null;
		IRouter campus_r_178 = null;
		if(null == baseCampus) {
			campus_178 = rv.createNet("campus_9_178");
			campus_r_178 = createCampus(campus_178,do_spherical);
		}else {
			campus_178 = rv.createNetReplica("campus_9_178",baseCampus);
			campus_r_178 = (IRouter)campus_178.getChildByName("sub_campus_router");
		}
		ILink l_campus_178 = rv.createLink("l_campus_178");
		l_campus_178.createInterface(r_178.createInterface("if_campus_178"));
		l_campus_178.createInterface((IInterface)campus_r_178.getChildByName("if_stub"));

		INet campus_184 = null;
		IRouter campus_r_184 = null;
		if(null == baseCampus) {
			campus_184 = rv.createNet("campus_9_184");
			campus_r_184 = createCampus(campus_184,do_spherical);
		}else {
			campus_184 = rv.createNetReplica("campus_9_184",baseCampus);
			campus_r_184 = (IRouter)campus_184.getChildByName("sub_campus_router");
		}
		ILink l_campus_184 = rv.createLink("l_campus_184");
		l_campus_184.createInterface(r_184.createInterface("if_campus_184"));
		l_campus_184.createInterface((IInterface)campus_r_184.getChildByName("if_stub"));

		INet campus_185 = null;
		IRouter campus_r_185 = null;
		if(null == baseCampus) {
			campus_185 = rv.createNet("campus_9_185");
			campus_r_185 = createCampus(campus_185,do_spherical);
		}else {
			campus_185 = rv.createNetReplica("campus_9_185",baseCampus);
			campus_r_185 = (IRouter)campus_185.getChildByName("sub_campus_router");
		}
		ILink l_campus_185 = rv.createLink("l_campus_185");
		l_campus_185.createInterface(r_185.createInterface("if_campus_185"));
		l_campus_185.createInterface((IInterface)campus_r_185.getChildByName("if_stub"));

		INet campus_191 = null;
		IRouter campus_r_191 = null;
		if(null == baseCampus) {
			campus_191 = rv.createNet("campus_9_191");
			campus_r_191 = createCampus(campus_191,do_spherical);
		}else {
			campus_191 = rv.createNetReplica("campus_9_191",baseCampus);
			campus_r_191 = (IRouter)campus_191.getChildByName("sub_campus_router");
		}
		ILink l_campus_191 = rv.createLink("l_campus_191");
		l_campus_191.createInterface(r_191.createInterface("if_campus_191"));
		l_campus_191.createInterface((IInterface)campus_r_191.getChildByName("if_stub"));

		INet campus_213 = null;
		IRouter campus_r_213 = null;
		if(null == baseCampus) {
			campus_213 = rv.createNet("campus_9_213");
			campus_r_213 = createCampus(campus_213,do_spherical);
		}else {
			campus_213 = rv.createNetReplica("campus_9_213",baseCampus);
			campus_r_213 = (IRouter)campus_213.getChildByName("sub_campus_router");
		}
		ILink l_campus_213 = rv.createLink("l_campus_213");
		l_campus_213.createInterface(r_213.createInterface("if_campus_213"));
		l_campus_213.createInterface((IInterface)campus_r_213.getChildByName("if_stub"));

		INet campus_265 = null;
		IRouter campus_r_265 = null;
		if(null == baseCampus) {
			campus_265 = rv.createNet("campus_9_265");
			campus_r_265 = createCampus(campus_265,do_spherical);
		}else {
			campus_265 = rv.createNetReplica("campus_9_265",baseCampus);
			campus_r_265 = (IRouter)campus_265.getChildByName("sub_campus_router");
		}
		ILink l_campus_265 = rv.createLink("l_campus_265");
		l_campus_265.createInterface(r_265.createInterface("if_campus_265"));
		l_campus_265.createInterface((IInterface)campus_r_265.getChildByName("if_stub"));

		INet campus_266 = null;
		IRouter campus_r_266 = null;
		if(null == baseCampus) {
			campus_266 = rv.createNet("campus_9_266");
			campus_r_266 = createCampus(campus_266,do_spherical);
		}else {
			campus_266 = rv.createNetReplica("campus_9_266",baseCampus);
			campus_r_266 = (IRouter)campus_266.getChildByName("sub_campus_router");
		}
		ILink l_campus_266 = rv.createLink("l_campus_266");
		l_campus_266.createInterface(r_266.createInterface("if_campus_266"));
		l_campus_266.createInterface((IInterface)campus_r_266.getChildByName("if_stub"));

		INet campus_268 = null;
		IRouter campus_r_268 = null;
		if(null == baseCampus) {
			campus_268 = rv.createNet("campus_9_268");
			campus_r_268 = createCampus(campus_268,do_spherical);
		}else {
			campus_268 = rv.createNetReplica("campus_9_268",baseCampus);
			campus_r_268 = (IRouter)campus_268.getChildByName("sub_campus_router");
		}
		ILink l_campus_268 = rv.createLink("l_campus_268");
		l_campus_268.createInterface(r_268.createInterface("if_campus_268"));
		l_campus_268.createInterface((IInterface)campus_r_268.getChildByName("if_stub"));

		INet campus_311 = null;
		IRouter campus_r_311 = null;
		if(null == baseCampus) {
			campus_311 = rv.createNet("campus_9_311");
			campus_r_311 = createCampus(campus_311,do_spherical);
		}else {
			campus_311 = rv.createNetReplica("campus_9_311",baseCampus);
			campus_r_311 = (IRouter)campus_311.getChildByName("sub_campus_router");
		}
		ILink l_campus_311 = rv.createLink("l_campus_311");
		l_campus_311.createInterface(r_311.createInterface("if_campus_311"));
		l_campus_311.createInterface((IInterface)campus_r_311.getChildByName("if_stub"));

		INet campus_312 = null;
		IRouter campus_r_312 = null;
		if(null == baseCampus) {
			campus_312 = rv.createNet("campus_9_312");
			campus_r_312 = createCampus(campus_312,do_spherical);
		}else {
			campus_312 = rv.createNetReplica("campus_9_312",baseCampus);
			campus_r_312 = (IRouter)campus_312.getChildByName("sub_campus_router");
		}
		ILink l_campus_312 = rv.createLink("l_campus_312");
		l_campus_312.createInterface(r_312.createInterface("if_campus_312"));
		l_campus_312.createInterface((IInterface)campus_r_312.getChildByName("if_stub"));

		INet campus_326 = null;
		IRouter campus_r_326 = null;
		if(null == baseCampus) {
			campus_326 = rv.createNet("campus_9_326");
			campus_r_326 = createCampus(campus_326,do_spherical);
		}else {
			campus_326 = rv.createNetReplica("campus_9_326",baseCampus);
			campus_r_326 = (IRouter)campus_326.getChildByName("sub_campus_router");
		}
		ILink l_campus_326 = rv.createLink("l_campus_326");
		l_campus_326.createInterface(r_326.createInterface("if_campus_326"));
		l_campus_326.createInterface((IInterface)campus_r_326.getChildByName("if_stub"));

		INet campus_520 = null;
		IRouter campus_r_520 = null;
		if(null == baseCampus) {
			campus_520 = rv.createNet("campus_9_520");
			campus_r_520 = createCampus(campus_520,do_spherical);
		}else {
			campus_520 = rv.createNetReplica("campus_9_520",baseCampus);
			campus_r_520 = (IRouter)campus_520.getChildByName("sub_campus_router");
		}
		ILink l_campus_520 = rv.createLink("l_campus_520");
		l_campus_520.createInterface(r_520.createInterface("if_campus_520"));
		l_campus_520.createInterface((IInterface)campus_r_520.getChildByName("if_stub"));
		return rv;
	}
	public INet create_net_10(INet top, Map<Integer,IRouter> routers) {
		INet rv = top.createNet("net_10");
		if(do_spherical) rv.createShortestPath();;

		//create routers
		IRouter r_17 = rv.createRouter("r_17");
		routers.put(17,r_17);

		IRouter r_18 = rv.createRouter("r_18");
		routers.put(18,r_18);

		IRouter r_71 = rv.createRouter("r_71");
		routers.put(71,r_71);

		IRouter r_72 = rv.createRouter("r_72");
		routers.put(72,r_72);

		IRouter r_73 = rv.createRouter("r_73");
		routers.put(73,r_73);

		IRouter r_74 = rv.createRouter("r_74");
		routers.put(74,r_74);

		IRouter r_78 = rv.createRouter("r_78");
		routers.put(78,r_78);

		IRouter r_79 = rv.createRouter("r_79");
		routers.put(79,r_79);

		IRouter r_81 = rv.createRouter("r_81");
		routers.put(81,r_81);

		IRouter r_82 = rv.createRouter("r_82");
		routers.put(82,r_82);

		IRouter r_114 = rv.createRouter("r_114");
		routers.put(114,r_114);

		IRouter r_115 = rv.createRouter("r_115");
		routers.put(115,r_115);

		IRouter r_125 = rv.createRouter("r_125");
		routers.put(125,r_125);

		IRouter r_126 = rv.createRouter("r_126");
		routers.put(126,r_126);

		IRouter r_130 = rv.createRouter("r_130");
		routers.put(130,r_130);

		IRouter r_136 = rv.createRouter("r_136");
		routers.put(136,r_136);

		IRouter r_146 = rv.createRouter("r_146");
		routers.put(146,r_146);

		IRouter r_147 = rv.createRouter("r_147");
		routers.put(147,r_147);

		IRouter r_149 = rv.createRouter("r_149");
		routers.put(149,r_149);

		IRouter r_154 = rv.createRouter("r_154");
		routers.put(154,r_154);

		IRouter r_155 = rv.createRouter("r_155");
		routers.put(155,r_155);

		IRouter r_161 = rv.createRouter("r_161");
		routers.put(161,r_161);

		IRouter r_187 = rv.createRouter("r_187");
		routers.put(187,r_187);

		IRouter r_188 = rv.createRouter("r_188");
		routers.put(188,r_188);

		IRouter r_202 = rv.createRouter("r_202");
		routers.put(202,r_202);

		IRouter r_204 = rv.createRouter("r_204");
		routers.put(204,r_204);

		IRouter r_206 = rv.createRouter("r_206");
		routers.put(206,r_206);

		IRouter r_214 = rv.createRouter("r_214");
		routers.put(214,r_214);

		IRouter r_242 = rv.createRouter("r_242");
		routers.put(242,r_242);

		IRouter r_263 = rv.createRouter("r_263");
		routers.put(263,r_263);

		IRouter r_264 = rv.createRouter("r_264");
		routers.put(264,r_264);

		IRouter r_267 = rv.createRouter("r_267");
		routers.put(267,r_267);

		IRouter r_269 = rv.createRouter("r_269");
		routers.put(269,r_269);

		IRouter r_278 = rv.createRouter("r_278");
		routers.put(278,r_278);

		IRouter r_308 = rv.createRouter("r_308");
		routers.put(308,r_308);

		IRouter r_542 = rv.createRouter("r_542");
		routers.put(542,r_542);

		IRouter r_547 = rv.createRouter("r_547");
		routers.put(547,r_547);

		IRouter r_633 = rv.createRouter("r_633");
		routers.put(633,r_633);

		//create links
		ILink l_18_17 = rv.createLink("l_18_17");
		l_18_17.createInterface(routers.get(18).createInterface("if_17"));
		l_18_17.createInterface(routers.get(17).createInterface("if_18"));

		ILink l_73_18 = rv.createLink("l_73_18");
		l_73_18.createInterface(routers.get(73).createInterface("if_18"));
		l_73_18.createInterface(routers.get(18).createInterface("if_73"));

		ILink l_72_71 = rv.createLink("l_72_71");
		l_72_71.createInterface(routers.get(72).createInterface("if_71"));
		l_72_71.createInterface(routers.get(71).createInterface("if_72"));

		ILink l_73_71 = rv.createLink("l_73_71");
		l_73_71.createInterface(routers.get(73).createInterface("if_71"));
		l_73_71.createInterface(routers.get(71).createInterface("if_73"));

		ILink l_74_71 = rv.createLink("l_74_71");
		l_74_71.createInterface(routers.get(74).createInterface("if_71"));
		l_74_71.createInterface(routers.get(71).createInterface("if_74"));

		ILink l_82_72 = rv.createLink("l_82_72");
		l_82_72.createInterface(routers.get(82).createInterface("if_72"));
		l_82_72.createInterface(routers.get(72).createInterface("if_82"));

		ILink l_147_72 = rv.createLink("l_147_72");
		l_147_72.createInterface(routers.get(147).createInterface("if_72"));
		l_147_72.createInterface(routers.get(72).createInterface("if_147"));

		ILink l_149_72 = rv.createLink("l_149_72");
		l_149_72.createInterface(routers.get(149).createInterface("if_72"));
		l_149_72.createInterface(routers.get(72).createInterface("if_149"));

		ILink l_202_72 = rv.createLink("l_202_72");
		l_202_72.createInterface(routers.get(202).createInterface("if_72"));
		l_202_72.createInterface(routers.get(72).createInterface("if_202"));

		ILink l_206_72 = rv.createLink("l_206_72");
		l_206_72.createInterface(routers.get(206).createInterface("if_72"));
		l_206_72.createInterface(routers.get(72).createInterface("if_206"));

		ILink l_263_72 = rv.createLink("l_263_72");
		l_263_72.createInterface(routers.get(263).createInterface("if_72"));
		l_263_72.createInterface(routers.get(72).createInterface("if_263"));

		ILink l_264_72 = rv.createLink("l_264_72");
		l_264_72.createInterface(routers.get(264).createInterface("if_72"));
		l_264_72.createInterface(routers.get(72).createInterface("if_264"));

		ILink l_278_72 = rv.createLink("l_278_72");
		l_278_72.createInterface(routers.get(278).createInterface("if_72"));
		l_278_72.createInterface(routers.get(72).createInterface("if_278"));

		ILink l_633_72 = rv.createLink("l_633_72");
		l_633_72.createInterface(routers.get(633).createInterface("if_72"));
		l_633_72.createInterface(routers.get(72).createInterface("if_633"));

		ILink l_78_73 = rv.createLink("l_78_73");
		l_78_73.createInterface(routers.get(78).createInterface("if_73"));
		l_78_73.createInterface(routers.get(73).createInterface("if_78"));

		ILink l_81_73 = rv.createLink("l_81_73");
		l_81_73.createInterface(routers.get(81).createInterface("if_73"));
		l_81_73.createInterface(routers.get(73).createInterface("if_81"));

		ILink l_82_73 = rv.createLink("l_82_73");
		l_82_73.createInterface(routers.get(82).createInterface("if_73"));
		l_82_73.createInterface(routers.get(73).createInterface("if_82"));

		ILink l_146_73 = rv.createLink("l_146_73");
		l_146_73.createInterface(routers.get(146).createInterface("if_73"));
		l_146_73.createInterface(routers.get(73).createInterface("if_146"));

		ILink l_147_73 = rv.createLink("l_147_73");
		l_147_73.createInterface(routers.get(147).createInterface("if_73"));
		l_147_73.createInterface(routers.get(73).createInterface("if_147"));

		ILink l_149_73 = rv.createLink("l_149_73");
		l_149_73.createInterface(routers.get(149).createInterface("if_73"));
		l_149_73.createInterface(routers.get(73).createInterface("if_149"));

		ILink l_202_73 = rv.createLink("l_202_73");
		l_202_73.createInterface(routers.get(202).createInterface("if_73"));
		l_202_73.createInterface(routers.get(73).createInterface("if_202"));

		ILink l_204_73 = rv.createLink("l_204_73");
		l_204_73.createInterface(routers.get(204).createInterface("if_73"));
		l_204_73.createInterface(routers.get(73).createInterface("if_204"));

		ILink l_206_73 = rv.createLink("l_206_73");
		l_206_73.createInterface(routers.get(206).createInterface("if_73"));
		l_206_73.createInterface(routers.get(73).createInterface("if_206"));

		ILink l_242_73 = rv.createLink("l_242_73");
		l_242_73.createInterface(routers.get(242).createInterface("if_73"));
		l_242_73.createInterface(routers.get(73).createInterface("if_242"));

		ILink l_263_73 = rv.createLink("l_263_73");
		l_263_73.createInterface(routers.get(263).createInterface("if_73"));
		l_263_73.createInterface(routers.get(73).createInterface("if_263"));

		ILink l_264_73 = rv.createLink("l_264_73");
		l_264_73.createInterface(routers.get(264).createInterface("if_73"));
		l_264_73.createInterface(routers.get(73).createInterface("if_264"));

		ILink l_269_73 = rv.createLink("l_269_73");
		l_269_73.createInterface(routers.get(269).createInterface("if_73"));
		l_269_73.createInterface(routers.get(73).createInterface("if_269"));

		ILink l_278_73 = rv.createLink("l_278_73");
		l_278_73.createInterface(routers.get(278).createInterface("if_73"));
		l_278_73.createInterface(routers.get(73).createInterface("if_278"));

		ILink l_308_73 = rv.createLink("l_308_73");
		l_308_73.createInterface(routers.get(308).createInterface("if_73"));
		l_308_73.createInterface(routers.get(73).createInterface("if_308"));

		ILink l_633_73 = rv.createLink("l_633_73");
		l_633_73.createInterface(routers.get(633).createInterface("if_73"));
		l_633_73.createInterface(routers.get(73).createInterface("if_633"));

		ILink l_79_78 = rv.createLink("l_79_78");
		l_79_78.createInterface(routers.get(79).createInterface("if_78"));
		l_79_78.createInterface(routers.get(78).createInterface("if_79"));

		ILink l_82_81 = rv.createLink("l_82_81");
		l_82_81.createInterface(routers.get(82).createInterface("if_81"));
		l_82_81.createInterface(routers.get(81).createInterface("if_82"));

		ILink l_115_114 = rv.createLink("l_115_114");
		l_115_114.createInterface(routers.get(115).createInterface("if_114"));
		l_115_114.createInterface(routers.get(114).createInterface("if_115"));

		ILink l_125_115 = rv.createLink("l_125_115");
		l_125_115.createInterface(routers.get(125).createInterface("if_115"));
		l_125_115.createInterface(routers.get(115).createInterface("if_125"));

		ILink l_126_115 = rv.createLink("l_126_115");
		l_126_115.createInterface(routers.get(126).createInterface("if_115"));
		l_126_115.createInterface(routers.get(115).createInterface("if_126"));

		ILink l_130_115 = rv.createLink("l_130_115");
		l_130_115.createInterface(routers.get(130).createInterface("if_115"));
		l_130_115.createInterface(routers.get(115).createInterface("if_130"));

		ILink l_136_115 = rv.createLink("l_136_115");
		l_136_115.createInterface(routers.get(136).createInterface("if_115"));
		l_136_115.createInterface(routers.get(115).createInterface("if_136"));

		ILink l_161_115 = rv.createLink("l_161_115");
		l_161_115.createInterface(routers.get(161).createInterface("if_115"));
		l_161_115.createInterface(routers.get(115).createInterface("if_161"));

		ILink l_214_115 = rv.createLink("l_214_115");
		l_214_115.createInterface(routers.get(214).createInterface("if_115"));
		l_214_115.createInterface(routers.get(115).createInterface("if_214"));

		ILink l_267_115 = rv.createLink("l_267_115");
		l_267_115.createInterface(routers.get(267).createInterface("if_115"));
		l_267_115.createInterface(routers.get(115).createInterface("if_267"));

		ILink l_542_115 = rv.createLink("l_542_115");
		l_542_115.createInterface(routers.get(542).createInterface("if_115"));
		l_542_115.createInterface(routers.get(115).createInterface("if_542"));

		ILink l_547_115 = rv.createLink("l_547_115");
		l_547_115.createInterface(routers.get(547).createInterface("if_115"));
		l_547_115.createInterface(routers.get(115).createInterface("if_547"));

		ILink l_126_125 = rv.createLink("l_126_125");
		l_126_125.createInterface(routers.get(126).createInterface("if_125"));
		l_126_125.createInterface(routers.get(125).createInterface("if_126"));

		ILink l_161_126 = rv.createLink("l_161_126");
		l_161_126.createInterface(routers.get(161).createInterface("if_126"));
		l_161_126.createInterface(routers.get(126).createInterface("if_161"));

		ILink l_542_126 = rv.createLink("l_542_126");
		l_542_126.createInterface(routers.get(542).createInterface("if_126"));
		l_542_126.createInterface(routers.get(126).createInterface("if_542"));

		ILink l_155_154 = rv.createLink("l_155_154");
		l_155_154.createInterface(routers.get(155).createInterface("if_154"));
		l_155_154.createInterface(routers.get(154).createInterface("if_155"));

		ILink l_188_187 = rv.createLink("l_188_187");
		l_188_187.createInterface(routers.get(188).createInterface("if_187"));
		l_188_187.createInterface(routers.get(187).createInterface("if_188"));

		ILink l_206_188 = rv.createLink("l_206_188");
		l_206_188.createInterface(routers.get(206).createInterface("if_188"));
		l_206_188.createInterface(routers.get(188).createInterface("if_206"));

		//attach campus networks

		INet campus_17 = null;
		IRouter campus_r_17 = null;
		if(null == baseCampus) {
			campus_17 = rv.createNet("campus_10_17");
			campus_r_17 = createCampus(campus_17,do_spherical);
		}else {
			campus_17 = rv.createNetReplica("campus_10_17",baseCampus);
			campus_r_17 = (IRouter)campus_17.getChildByName("sub_campus_router");
		}
		ILink l_campus_17 = rv.createLink("l_campus_17");
		l_campus_17.createInterface(r_17.createInterface("if_campus_17"));
		l_campus_17.createInterface((IInterface)campus_r_17.getChildByName("if_stub"));

		INet campus_74 = null;
		IRouter campus_r_74 = null;
		if(null == baseCampus) {
			campus_74 = rv.createNet("campus_10_74");
			campus_r_74 = createCampus(campus_74,do_spherical);
		}else {
			campus_74 = rv.createNetReplica("campus_10_74",baseCampus);
			campus_r_74 = (IRouter)campus_74.getChildByName("sub_campus_router");
		}
		ILink l_campus_74 = rv.createLink("l_campus_74");
		l_campus_74.createInterface(r_74.createInterface("if_campus_74"));
		l_campus_74.createInterface((IInterface)campus_r_74.getChildByName("if_stub"));

		INet campus_79 = null;
		IRouter campus_r_79 = null;
		if(null == baseCampus) {
			campus_79 = rv.createNet("campus_10_79");
			campus_r_79 = createCampus(campus_79,do_spherical);
		}else {
			campus_79 = rv.createNetReplica("campus_10_79",baseCampus);
			campus_r_79 = (IRouter)campus_79.getChildByName("sub_campus_router");
		}
		ILink l_campus_79 = rv.createLink("l_campus_79");
		l_campus_79.createInterface(r_79.createInterface("if_campus_79"));
		l_campus_79.createInterface((IInterface)campus_r_79.getChildByName("if_stub"));

		INet campus_114 = null;
		IRouter campus_r_114 = null;
		if(null == baseCampus) {
			campus_114 = rv.createNet("campus_10_114");
			campus_r_114 = createCampus(campus_114,do_spherical);
		}else {
			campus_114 = rv.createNetReplica("campus_10_114",baseCampus);
			campus_r_114 = (IRouter)campus_114.getChildByName("sub_campus_router");
		}
		ILink l_campus_114 = rv.createLink("l_campus_114");
		l_campus_114.createInterface(r_114.createInterface("if_campus_114"));
		l_campus_114.createInterface((IInterface)campus_r_114.getChildByName("if_stub"));

		INet campus_136 = null;
		IRouter campus_r_136 = null;
		if(null == baseCampus) {
			campus_136 = rv.createNet("campus_10_136");
			campus_r_136 = createCampus(campus_136,do_spherical);
		}else {
			campus_136 = rv.createNetReplica("campus_10_136",baseCampus);
			campus_r_136 = (IRouter)campus_136.getChildByName("sub_campus_router");
		}
		ILink l_campus_136 = rv.createLink("l_campus_136");
		l_campus_136.createInterface(r_136.createInterface("if_campus_136"));
		l_campus_136.createInterface((IInterface)campus_r_136.getChildByName("if_stub"));

		INet campus_146 = null;
		IRouter campus_r_146 = null;
		if(null == baseCampus) {
			campus_146 = rv.createNet("campus_10_146");
			campus_r_146 = createCampus(campus_146,do_spherical);
		}else {
			campus_146 = rv.createNetReplica("campus_10_146",baseCampus);
			campus_r_146 = (IRouter)campus_146.getChildByName("sub_campus_router");
		}
		ILink l_campus_146 = rv.createLink("l_campus_146");
		l_campus_146.createInterface(r_146.createInterface("if_campus_146"));
		l_campus_146.createInterface((IInterface)campus_r_146.getChildByName("if_stub"));

		INet campus_154 = null;
		IRouter campus_r_154 = null;
		if(null == baseCampus) {
			campus_154 = rv.createNet("campus_10_154");
			campus_r_154 = createCampus(campus_154,do_spherical);
		}else {
			campus_154 = rv.createNetReplica("campus_10_154",baseCampus);
			campus_r_154 = (IRouter)campus_154.getChildByName("sub_campus_router");
		}
		ILink l_campus_154 = rv.createLink("l_campus_154");
		l_campus_154.createInterface(r_154.createInterface("if_campus_154"));
		l_campus_154.createInterface((IInterface)campus_r_154.getChildByName("if_stub"));

		INet campus_155 = null;
		IRouter campus_r_155 = null;
		if(null == baseCampus) {
			campus_155 = rv.createNet("campus_10_155");
			campus_r_155 = createCampus(campus_155,do_spherical);
		}else {
			campus_155 = rv.createNetReplica("campus_10_155",baseCampus);
			campus_r_155 = (IRouter)campus_155.getChildByName("sub_campus_router");
		}
		ILink l_campus_155 = rv.createLink("l_campus_155");
		l_campus_155.createInterface(r_155.createInterface("if_campus_155"));
		l_campus_155.createInterface((IInterface)campus_r_155.getChildByName("if_stub"));

		INet campus_187 = null;
		IRouter campus_r_187 = null;
		if(null == baseCampus) {
			campus_187 = rv.createNet("campus_10_187");
			campus_r_187 = createCampus(campus_187,do_spherical);
		}else {
			campus_187 = rv.createNetReplica("campus_10_187",baseCampus);
			campus_r_187 = (IRouter)campus_187.getChildByName("sub_campus_router");
		}
		ILink l_campus_187 = rv.createLink("l_campus_187");
		l_campus_187.createInterface(r_187.createInterface("if_campus_187"));
		l_campus_187.createInterface((IInterface)campus_r_187.getChildByName("if_stub"));

		INet campus_204 = null;
		IRouter campus_r_204 = null;
		if(null == baseCampus) {
			campus_204 = rv.createNet("campus_10_204");
			campus_r_204 = createCampus(campus_204,do_spherical);
		}else {
			campus_204 = rv.createNetReplica("campus_10_204",baseCampus);
			campus_r_204 = (IRouter)campus_204.getChildByName("sub_campus_router");
		}
		ILink l_campus_204 = rv.createLink("l_campus_204");
		l_campus_204.createInterface(r_204.createInterface("if_campus_204"));
		l_campus_204.createInterface((IInterface)campus_r_204.getChildByName("if_stub"));

		INet campus_214 = null;
		IRouter campus_r_214 = null;
		if(null == baseCampus) {
			campus_214 = rv.createNet("campus_10_214");
			campus_r_214 = createCampus(campus_214,do_spherical);
		}else {
			campus_214 = rv.createNetReplica("campus_10_214",baseCampus);
			campus_r_214 = (IRouter)campus_214.getChildByName("sub_campus_router");
		}
		ILink l_campus_214 = rv.createLink("l_campus_214");
		l_campus_214.createInterface(r_214.createInterface("if_campus_214"));
		l_campus_214.createInterface((IInterface)campus_r_214.getChildByName("if_stub"));

		INet campus_242 = null;
		IRouter campus_r_242 = null;
		if(null == baseCampus) {
			campus_242 = rv.createNet("campus_10_242");
			campus_r_242 = createCampus(campus_242,do_spherical);
		}else {
			campus_242 = rv.createNetReplica("campus_10_242",baseCampus);
			campus_r_242 = (IRouter)campus_242.getChildByName("sub_campus_router");
		}
		ILink l_campus_242 = rv.createLink("l_campus_242");
		l_campus_242.createInterface(r_242.createInterface("if_campus_242"));
		l_campus_242.createInterface((IInterface)campus_r_242.getChildByName("if_stub"));

		INet campus_267 = null;
		IRouter campus_r_267 = null;
		if(null == baseCampus) {
			campus_267 = rv.createNet("campus_10_267");
			campus_r_267 = createCampus(campus_267,do_spherical);
		}else {
			campus_267 = rv.createNetReplica("campus_10_267",baseCampus);
			campus_r_267 = (IRouter)campus_267.getChildByName("sub_campus_router");
		}
		ILink l_campus_267 = rv.createLink("l_campus_267");
		l_campus_267.createInterface(r_267.createInterface("if_campus_267"));
		l_campus_267.createInterface((IInterface)campus_r_267.getChildByName("if_stub"));

		INet campus_269 = null;
		IRouter campus_r_269 = null;
		if(null == baseCampus) {
			campus_269 = rv.createNet("campus_10_269");
			campus_r_269 = createCampus(campus_269,do_spherical);
		}else {
			campus_269 = rv.createNetReplica("campus_10_269",baseCampus);
			campus_r_269 = (IRouter)campus_269.getChildByName("sub_campus_router");
		}
		ILink l_campus_269 = rv.createLink("l_campus_269");
		l_campus_269.createInterface(r_269.createInterface("if_campus_269"));
		l_campus_269.createInterface((IInterface)campus_r_269.getChildByName("if_stub"));

		INet campus_308 = null;
		IRouter campus_r_308 = null;
		if(null == baseCampus) {
			campus_308 = rv.createNet("campus_10_308");
			campus_r_308 = createCampus(campus_308,do_spherical);
		}else {
			campus_308 = rv.createNetReplica("campus_10_308",baseCampus);
			campus_r_308 = (IRouter)campus_308.getChildByName("sub_campus_router");
		}
		ILink l_campus_308 = rv.createLink("l_campus_308");
		l_campus_308.createInterface(r_308.createInterface("if_campus_308"));
		l_campus_308.createInterface((IInterface)campus_r_308.getChildByName("if_stub"));
		return rv;
	}
	public INet create_net_11(INet top, Map<Integer,IRouter> routers) {
		INet rv = top.createNet("net_11");
		if(do_spherical) rv.createShortestPath();;

		//create routers
		IRouter r_29 = rv.createRouter("r_29");
		routers.put(29,r_29);

		IRouter r_30 = rv.createRouter("r_30");
		routers.put(30,r_30);

		IRouter r_31 = rv.createRouter("r_31");
		routers.put(31,r_31);

		IRouter r_32 = rv.createRouter("r_32");
		routers.put(32,r_32);

		IRouter r_33 = rv.createRouter("r_33");
		routers.put(33,r_33);

		IRouter r_34 = rv.createRouter("r_34");
		routers.put(34,r_34);

		IRouter r_127 = rv.createRouter("r_127");
		routers.put(127,r_127);

		IRouter r_235 = rv.createRouter("r_235");
		routers.put(235,r_235);

		IRouter r_236 = rv.createRouter("r_236");
		routers.put(236,r_236);

		IRouter r_237 = rv.createRouter("r_237");
		routers.put(237,r_237);

		IRouter r_416 = rv.createRouter("r_416");
		routers.put(416,r_416);

		IRouter r_419 = rv.createRouter("r_419");
		routers.put(419,r_419);

		IRouter r_420 = rv.createRouter("r_420");
		routers.put(420,r_420);

		IRouter r_427 = rv.createRouter("r_427");
		routers.put(427,r_427);

		IRouter r_428 = rv.createRouter("r_428");
		routers.put(428,r_428);

		IRouter r_437 = rv.createRouter("r_437");
		routers.put(437,r_437);

		IRouter r_438 = rv.createRouter("r_438");
		routers.put(438,r_438);

		IRouter r_543 = rv.createRouter("r_543");
		routers.put(543,r_543);

		IRouter r_544 = rv.createRouter("r_544");
		routers.put(544,r_544);

		IRouter r_545 = rv.createRouter("r_545");
		routers.put(545,r_545);

		IRouter r_546 = rv.createRouter("r_546");
		routers.put(546,r_546);

		IRouter r_585 = rv.createRouter("r_585");
		routers.put(585,r_585);

		IRouter r_586 = rv.createRouter("r_586");
		routers.put(586,r_586);

		IRouter r_587 = rv.createRouter("r_587");
		routers.put(587,r_587);

		IRouter r_588 = rv.createRouter("r_588");
		routers.put(588,r_588);

		IRouter r_589 = rv.createRouter("r_589");
		routers.put(589,r_589);

		IRouter r_590 = rv.createRouter("r_590");
		routers.put(590,r_590);

		IRouter r_591 = rv.createRouter("r_591");
		routers.put(591,r_591);

		IRouter r_592 = rv.createRouter("r_592");
		routers.put(592,r_592);

		IRouter r_593 = rv.createRouter("r_593");
		routers.put(593,r_593);

		IRouter r_594 = rv.createRouter("r_594");
		routers.put(594,r_594);

		IRouter r_595 = rv.createRouter("r_595");
		routers.put(595,r_595);

		IRouter r_601 = rv.createRouter("r_601");
		routers.put(601,r_601);

		IRouter r_603 = rv.createRouter("r_603");
		routers.put(603,r_603);

		IRouter r_604 = rv.createRouter("r_604");
		routers.put(604,r_604);

		IRouter r_605 = rv.createRouter("r_605");
		routers.put(605,r_605);

		IRouter r_606 = rv.createRouter("r_606");
		routers.put(606,r_606);

		IRouter r_607 = rv.createRouter("r_607");
		routers.put(607,r_607);

		IRouter r_608 = rv.createRouter("r_608");
		routers.put(608,r_608);

		IRouter r_609 = rv.createRouter("r_609");
		routers.put(609,r_609);

		//create links
		ILink l_30_29 = rv.createLink("l_30_29");
		l_30_29.createInterface(routers.get(30).createInterface("if_29"));
		l_30_29.createInterface(routers.get(29).createInterface("if_30"));

		ILink l_31_29 = rv.createLink("l_31_29");
		l_31_29.createInterface(routers.get(31).createInterface("if_29"));
		l_31_29.createInterface(routers.get(29).createInterface("if_31"));

		ILink l_32_29 = rv.createLink("l_32_29");
		l_32_29.createInterface(routers.get(32).createInterface("if_29"));
		l_32_29.createInterface(routers.get(29).createInterface("if_32"));

		ILink l_127_30 = rv.createLink("l_127_30");
		l_127_30.createInterface(routers.get(127).createInterface("if_30"));
		l_127_30.createInterface(routers.get(30).createInterface("if_127"));

		ILink l_428_30 = rv.createLink("l_428_30");
		l_428_30.createInterface(routers.get(428).createInterface("if_30"));
		l_428_30.createInterface(routers.get(30).createInterface("if_428"));

		ILink l_546_30 = rv.createLink("l_546_30");
		l_546_30.createInterface(routers.get(546).createInterface("if_30"));
		l_546_30.createInterface(routers.get(30).createInterface("if_546"));

		ILink l_590_30 = rv.createLink("l_590_30");
		l_590_30.createInterface(routers.get(590).createInterface("if_30"));
		l_590_30.createInterface(routers.get(30).createInterface("if_590"));

		ILink l_603_30 = rv.createLink("l_603_30");
		l_603_30.createInterface(routers.get(603).createInterface("if_30"));
		l_603_30.createInterface(routers.get(30).createInterface("if_603"));

		ILink l_604_30 = rv.createLink("l_604_30");
		l_604_30.createInterface(routers.get(604).createInterface("if_30"));
		l_604_30.createInterface(routers.get(30).createInterface("if_604"));

		ILink l_605_30 = rv.createLink("l_605_30");
		l_605_30.createInterface(routers.get(605).createInterface("if_30"));
		l_605_30.createInterface(routers.get(30).createInterface("if_605"));

		ILink l_606_30 = rv.createLink("l_606_30");
		l_606_30.createInterface(routers.get(606).createInterface("if_30"));
		l_606_30.createInterface(routers.get(30).createInterface("if_606"));

		ILink l_607_30 = rv.createLink("l_607_30");
		l_607_30.createInterface(routers.get(607).createInterface("if_30"));
		l_607_30.createInterface(routers.get(30).createInterface("if_607"));

		ILink l_127_31 = rv.createLink("l_127_31");
		l_127_31.createInterface(routers.get(127).createInterface("if_31"));
		l_127_31.createInterface(routers.get(31).createInterface("if_127"));

		ILink l_428_31 = rv.createLink("l_428_31");
		l_428_31.createInterface(routers.get(428).createInterface("if_31"));
		l_428_31.createInterface(routers.get(31).createInterface("if_428"));

		ILink l_585_31 = rv.createLink("l_585_31");
		l_585_31.createInterface(routers.get(585).createInterface("if_31"));
		l_585_31.createInterface(routers.get(31).createInterface("if_585"));

		ILink l_590_31 = rv.createLink("l_590_31");
		l_590_31.createInterface(routers.get(590).createInterface("if_31"));
		l_590_31.createInterface(routers.get(31).createInterface("if_590"));

		ILink l_603_31 = rv.createLink("l_603_31");
		l_603_31.createInterface(routers.get(603).createInterface("if_31"));
		l_603_31.createInterface(routers.get(31).createInterface("if_603"));

		ILink l_605_31 = rv.createLink("l_605_31");
		l_605_31.createInterface(routers.get(605).createInterface("if_31"));
		l_605_31.createInterface(routers.get(31).createInterface("if_605"));

		ILink l_606_31 = rv.createLink("l_606_31");
		l_606_31.createInterface(routers.get(606).createInterface("if_31"));
		l_606_31.createInterface(routers.get(31).createInterface("if_606"));

		ILink l_607_31 = rv.createLink("l_607_31");
		l_607_31.createInterface(routers.get(607).createInterface("if_31"));
		l_607_31.createInterface(routers.get(31).createInterface("if_607"));

		ILink l_127_32 = rv.createLink("l_127_32");
		l_127_32.createInterface(routers.get(127).createInterface("if_32"));
		l_127_32.createInterface(routers.get(32).createInterface("if_127"));

		ILink l_428_32 = rv.createLink("l_428_32");
		l_428_32.createInterface(routers.get(428).createInterface("if_32"));
		l_428_32.createInterface(routers.get(32).createInterface("if_428"));

		ILink l_590_32 = rv.createLink("l_590_32");
		l_590_32.createInterface(routers.get(590).createInterface("if_32"));
		l_590_32.createInterface(routers.get(32).createInterface("if_590"));

		ILink l_594_32 = rv.createLink("l_594_32");
		l_594_32.createInterface(routers.get(594).createInterface("if_32"));
		l_594_32.createInterface(routers.get(32).createInterface("if_594"));

		ILink l_603_32 = rv.createLink("l_603_32");
		l_603_32.createInterface(routers.get(603).createInterface("if_32"));
		l_603_32.createInterface(routers.get(32).createInterface("if_603"));

		ILink l_604_32 = rv.createLink("l_604_32");
		l_604_32.createInterface(routers.get(604).createInterface("if_32"));
		l_604_32.createInterface(routers.get(32).createInterface("if_604"));

		ILink l_605_32 = rv.createLink("l_605_32");
		l_605_32.createInterface(routers.get(605).createInterface("if_32"));
		l_605_32.createInterface(routers.get(32).createInterface("if_605"));

		ILink l_606_32 = rv.createLink("l_606_32");
		l_606_32.createInterface(routers.get(606).createInterface("if_32"));
		l_606_32.createInterface(routers.get(32).createInterface("if_606"));

		ILink l_607_32 = rv.createLink("l_607_32");
		l_607_32.createInterface(routers.get(607).createInterface("if_32"));
		l_607_32.createInterface(routers.get(32).createInterface("if_607"));

		ILink l_34_33 = rv.createLink("l_34_33");
		l_34_33.createInterface(routers.get(34).createInterface("if_33"));
		l_34_33.createInterface(routers.get(33).createInterface("if_34"));

		ILink l_127_34 = rv.createLink("l_127_34");
		l_127_34.createInterface(routers.get(127).createInterface("if_34"));
		l_127_34.createInterface(routers.get(34).createInterface("if_127"));

		ILink l_419_34 = rv.createLink("l_419_34");
		l_419_34.createInterface(routers.get(419).createInterface("if_34"));
		l_419_34.createInterface(routers.get(34).createInterface("if_419"));

		ILink l_420_34 = rv.createLink("l_420_34");
		l_420_34.createInterface(routers.get(420).createInterface("if_34"));
		l_420_34.createInterface(routers.get(34).createInterface("if_420"));

		ILink l_428_34 = rv.createLink("l_428_34");
		l_428_34.createInterface(routers.get(428).createInterface("if_34"));
		l_428_34.createInterface(routers.get(34).createInterface("if_428"));

		ILink l_595_34 = rv.createLink("l_595_34");
		l_595_34.createInterface(routers.get(595).createInterface("if_34"));
		l_595_34.createInterface(routers.get(34).createInterface("if_595"));

		ILink l_601_34 = rv.createLink("l_601_34");
		l_601_34.createInterface(routers.get(601).createInterface("if_34"));
		l_601_34.createInterface(routers.get(34).createInterface("if_601"));

		ILink l_416_127 = rv.createLink("l_416_127");
		l_416_127.createInterface(routers.get(416).createInterface("if_127"));
		l_416_127.createInterface(routers.get(127).createInterface("if_416"));

		ILink l_419_127 = rv.createLink("l_419_127");
		l_419_127.createInterface(routers.get(419).createInterface("if_127"));
		l_419_127.createInterface(routers.get(127).createInterface("if_419"));

		ILink l_420_127 = rv.createLink("l_420_127");
		l_420_127.createInterface(routers.get(420).createInterface("if_127"));
		l_420_127.createInterface(routers.get(127).createInterface("if_420"));

		ILink l_427_127 = rv.createLink("l_427_127");
		l_427_127.createInterface(routers.get(427).createInterface("if_127"));
		l_427_127.createInterface(routers.get(127).createInterface("if_427"));

		ILink l_428_127 = rv.createLink("l_428_127");
		l_428_127.createInterface(routers.get(428).createInterface("if_127"));
		l_428_127.createInterface(routers.get(127).createInterface("if_428"));

		ILink l_438_127 = rv.createLink("l_438_127");
		l_438_127.createInterface(routers.get(438).createInterface("if_127"));
		l_438_127.createInterface(routers.get(127).createInterface("if_438"));

		ILink l_546_127 = rv.createLink("l_546_127");
		l_546_127.createInterface(routers.get(546).createInterface("if_127"));
		l_546_127.createInterface(routers.get(127).createInterface("if_546"));

		ILink l_593_127 = rv.createLink("l_593_127");
		l_593_127.createInterface(routers.get(593).createInterface("if_127"));
		l_593_127.createInterface(routers.get(127).createInterface("if_593"));

		ILink l_236_235 = rv.createLink("l_236_235");
		l_236_235.createInterface(routers.get(236).createInterface("if_235"));
		l_236_235.createInterface(routers.get(235).createInterface("if_236"));

		ILink l_237_235 = rv.createLink("l_237_235");
		l_237_235.createInterface(routers.get(237).createInterface("if_235"));
		l_237_235.createInterface(routers.get(235).createInterface("if_237"));

		ILink l_419_236 = rv.createLink("l_419_236");
		l_419_236.createInterface(routers.get(419).createInterface("if_236"));
		l_419_236.createInterface(routers.get(236).createInterface("if_419"));

		ILink l_420_236 = rv.createLink("l_420_236");
		l_420_236.createInterface(routers.get(420).createInterface("if_236"));
		l_420_236.createInterface(routers.get(236).createInterface("if_420"));

		ILink l_419_237 = rv.createLink("l_419_237");
		l_419_237.createInterface(routers.get(419).createInterface("if_237"));
		l_419_237.createInterface(routers.get(237).createInterface("if_419"));

		ILink l_420_237 = rv.createLink("l_420_237");
		l_420_237.createInterface(routers.get(420).createInterface("if_237"));
		l_420_237.createInterface(routers.get(237).createInterface("if_420"));

		ILink l_428_416 = rv.createLink("l_428_416");
		l_428_416.createInterface(routers.get(428).createInterface("if_416"));
		l_428_416.createInterface(routers.get(416).createInterface("if_428"));

		ILink l_585_416 = rv.createLink("l_585_416");
		l_585_416.createInterface(routers.get(585).createInterface("if_416"));
		l_585_416.createInterface(routers.get(416).createInterface("if_585"));

		ILink l_590_416 = rv.createLink("l_590_416");
		l_590_416.createInterface(routers.get(590).createInterface("if_416"));
		l_590_416.createInterface(routers.get(416).createInterface("if_590"));

		ILink l_593_416 = rv.createLink("l_593_416");
		l_593_416.createInterface(routers.get(593).createInterface("if_416"));
		l_593_416.createInterface(routers.get(416).createInterface("if_593"));

		ILink l_594_416 = rv.createLink("l_594_416");
		l_594_416.createInterface(routers.get(594).createInterface("if_416"));
		l_594_416.createInterface(routers.get(416).createInterface("if_594"));

		ILink l_428_419 = rv.createLink("l_428_419");
		l_428_419.createInterface(routers.get(428).createInterface("if_419"));
		l_428_419.createInterface(routers.get(419).createInterface("if_428"));

		ILink l_590_419 = rv.createLink("l_590_419");
		l_590_419.createInterface(routers.get(590).createInterface("if_419"));
		l_590_419.createInterface(routers.get(419).createInterface("if_590"));

		ILink l_594_419 = rv.createLink("l_594_419");
		l_594_419.createInterface(routers.get(594).createInterface("if_419"));
		l_594_419.createInterface(routers.get(419).createInterface("if_594"));

		ILink l_428_420 = rv.createLink("l_428_420");
		l_428_420.createInterface(routers.get(428).createInterface("if_420"));
		l_428_420.createInterface(routers.get(420).createInterface("if_428"));

		ILink l_590_420 = rv.createLink("l_590_420");
		l_590_420.createInterface(routers.get(590).createInterface("if_420"));
		l_590_420.createInterface(routers.get(420).createInterface("if_590"));

		ILink l_594_420 = rv.createLink("l_594_420");
		l_594_420.createInterface(routers.get(594).createInterface("if_420"));
		l_594_420.createInterface(routers.get(420).createInterface("if_594"));

		ILink l_428_427 = rv.createLink("l_428_427");
		l_428_427.createInterface(routers.get(428).createInterface("if_427"));
		l_428_427.createInterface(routers.get(427).createInterface("if_428"));

		ILink l_438_428 = rv.createLink("l_438_428");
		l_438_428.createInterface(routers.get(438).createInterface("if_428"));
		l_438_428.createInterface(routers.get(428).createInterface("if_438"));

		ILink l_588_428 = rv.createLink("l_588_428");
		l_588_428.createInterface(routers.get(588).createInterface("if_428"));
		l_588_428.createInterface(routers.get(428).createInterface("if_588"));

		ILink l_595_428 = rv.createLink("l_595_428");
		l_595_428.createInterface(routers.get(595).createInterface("if_428"));
		l_595_428.createInterface(routers.get(428).createInterface("if_595"));

		ILink l_438_437 = rv.createLink("l_438_437");
		l_438_437.createInterface(routers.get(438).createInterface("if_437"));
		l_438_437.createInterface(routers.get(437).createInterface("if_438"));

		ILink l_595_438 = rv.createLink("l_595_438");
		l_595_438.createInterface(routers.get(595).createInterface("if_438"));
		l_595_438.createInterface(routers.get(438).createInterface("if_595"));

		ILink l_608_438 = rv.createLink("l_608_438");
		l_608_438.createInterface(routers.get(608).createInterface("if_438"));
		l_608_438.createInterface(routers.get(438).createInterface("if_608"));

		ILink l_609_438 = rv.createLink("l_609_438");
		l_609_438.createInterface(routers.get(609).createInterface("if_438"));
		l_609_438.createInterface(routers.get(438).createInterface("if_609"));

		ILink l_544_543 = rv.createLink("l_544_543");
		l_544_543.createInterface(routers.get(544).createInterface("if_543"));
		l_544_543.createInterface(routers.get(543).createInterface("if_544"));

		ILink l_545_544 = rv.createLink("l_545_544");
		l_545_544.createInterface(routers.get(545).createInterface("if_544"));
		l_545_544.createInterface(routers.get(544).createInterface("if_545"));

		ILink l_546_544 = rv.createLink("l_546_544");
		l_546_544.createInterface(routers.get(546).createInterface("if_544"));
		l_546_544.createInterface(routers.get(544).createInterface("if_546"));

		ILink l_585_544 = rv.createLink("l_585_544");
		l_585_544.createInterface(routers.get(585).createInterface("if_544"));
		l_585_544.createInterface(routers.get(544).createInterface("if_585"));

		ILink l_589_544 = rv.createLink("l_589_544");
		l_589_544.createInterface(routers.get(589).createInterface("if_544"));
		l_589_544.createInterface(routers.get(544).createInterface("if_589"));

		ILink l_590_544 = rv.createLink("l_590_544");
		l_590_544.createInterface(routers.get(590).createInterface("if_544"));
		l_590_544.createInterface(routers.get(544).createInterface("if_590"));

		ILink l_594_544 = rv.createLink("l_594_544");
		l_594_544.createInterface(routers.get(594).createInterface("if_544"));
		l_594_544.createInterface(routers.get(544).createInterface("if_594"));

		ILink l_546_545 = rv.createLink("l_546_545");
		l_546_545.createInterface(routers.get(546).createInterface("if_545"));
		l_546_545.createInterface(routers.get(545).createInterface("if_546"));

		ILink l_585_546 = rv.createLink("l_585_546");
		l_585_546.createInterface(routers.get(585).createInterface("if_546"));
		l_585_546.createInterface(routers.get(546).createInterface("if_585"));

		ILink l_589_546 = rv.createLink("l_589_546");
		l_589_546.createInterface(routers.get(589).createInterface("if_546"));
		l_589_546.createInterface(routers.get(546).createInterface("if_589"));

		ILink l_590_546 = rv.createLink("l_590_546");
		l_590_546.createInterface(routers.get(590).createInterface("if_546"));
		l_590_546.createInterface(routers.get(546).createInterface("if_590"));

		ILink l_591_546 = rv.createLink("l_591_546");
		l_591_546.createInterface(routers.get(591).createInterface("if_546"));
		l_591_546.createInterface(routers.get(546).createInterface("if_591"));

		ILink l_592_546 = rv.createLink("l_592_546");
		l_592_546.createInterface(routers.get(592).createInterface("if_546"));
		l_592_546.createInterface(routers.get(546).createInterface("if_592"));

		ILink l_593_546 = rv.createLink("l_593_546");
		l_593_546.createInterface(routers.get(593).createInterface("if_546"));
		l_593_546.createInterface(routers.get(546).createInterface("if_593"));

		ILink l_586_585 = rv.createLink("l_586_585");
		l_586_585.createInterface(routers.get(586).createInterface("if_585"));
		l_586_585.createInterface(routers.get(585).createInterface("if_586"));

		ILink l_587_585 = rv.createLink("l_587_585");
		l_587_585.createInterface(routers.get(587).createInterface("if_585"));
		l_587_585.createInterface(routers.get(585).createInterface("if_587"));

		ILink l_588_585 = rv.createLink("l_588_585");
		l_588_585.createInterface(routers.get(588).createInterface("if_585"));
		l_588_585.createInterface(routers.get(585).createInterface("if_588"));

		ILink l_590_586 = rv.createLink("l_590_586");
		l_590_586.createInterface(routers.get(590).createInterface("if_586"));
		l_590_586.createInterface(routers.get(586).createInterface("if_590"));

		ILink l_594_586 = rv.createLink("l_594_586");
		l_594_586.createInterface(routers.get(594).createInterface("if_586"));
		l_594_586.createInterface(routers.get(586).createInterface("if_594"));

		ILink l_590_587 = rv.createLink("l_590_587");
		l_590_587.createInterface(routers.get(590).createInterface("if_587"));
		l_590_587.createInterface(routers.get(587).createInterface("if_590"));

		ILink l_594_587 = rv.createLink("l_594_587");
		l_594_587.createInterface(routers.get(594).createInterface("if_587"));
		l_594_587.createInterface(routers.get(587).createInterface("if_594"));

		ILink l_590_588 = rv.createLink("l_590_588");
		l_590_588.createInterface(routers.get(590).createInterface("if_588"));
		l_590_588.createInterface(routers.get(588).createInterface("if_590"));

		ILink l_595_593 = rv.createLink("l_595_593");
		l_595_593.createInterface(routers.get(595).createInterface("if_593"));
		l_595_593.createInterface(routers.get(593).createInterface("if_595"));

		//attach campus networks

		INet campus_33 = null;
		IRouter campus_r_33 = null;
		if(null == baseCampus) {
			campus_33 = rv.createNet("campus_11_33");
			campus_r_33 = createCampus(campus_33,do_spherical);
		}else {
			campus_33 = rv.createNetReplica("campus_11_33",baseCampus);
			campus_r_33 = (IRouter)campus_33.getChildByName("sub_campus_router");
		}
		ILink l_campus_33 = rv.createLink("l_campus_33");
		l_campus_33.createInterface(r_33.createInterface("if_campus_33"));
		l_campus_33.createInterface((IInterface)campus_r_33.getChildByName("if_stub"));

		INet campus_437 = null;
		IRouter campus_r_437 = null;
		if(null == baseCampus) {
			campus_437 = rv.createNet("campus_11_437");
			campus_r_437 = createCampus(campus_437,do_spherical);
		}else {
			campus_437 = rv.createNetReplica("campus_11_437",baseCampus);
			campus_r_437 = (IRouter)campus_437.getChildByName("sub_campus_router");
		}
		ILink l_campus_437 = rv.createLink("l_campus_437");
		l_campus_437.createInterface(r_437.createInterface("if_campus_437"));
		l_campus_437.createInterface((IInterface)campus_r_437.getChildByName("if_stub"));

		INet campus_543 = null;
		IRouter campus_r_543 = null;
		if(null == baseCampus) {
			campus_543 = rv.createNet("campus_11_543");
			campus_r_543 = createCampus(campus_543,do_spherical);
		}else {
			campus_543 = rv.createNetReplica("campus_11_543",baseCampus);
			campus_r_543 = (IRouter)campus_543.getChildByName("sub_campus_router");
		}
		ILink l_campus_543 = rv.createLink("l_campus_543");
		l_campus_543.createInterface(r_543.createInterface("if_campus_543"));
		l_campus_543.createInterface((IInterface)campus_r_543.getChildByName("if_stub"));

		INet campus_591 = null;
		IRouter campus_r_591 = null;
		if(null == baseCampus) {
			campus_591 = rv.createNet("campus_11_591");
			campus_r_591 = createCampus(campus_591,do_spherical);
		}else {
			campus_591 = rv.createNetReplica("campus_11_591",baseCampus);
			campus_r_591 = (IRouter)campus_591.getChildByName("sub_campus_router");
		}
		ILink l_campus_591 = rv.createLink("l_campus_591");
		l_campus_591.createInterface(r_591.createInterface("if_campus_591"));
		l_campus_591.createInterface((IInterface)campus_r_591.getChildByName("if_stub"));

		INet campus_592 = null;
		IRouter campus_r_592 = null;
		if(null == baseCampus) {
			campus_592 = rv.createNet("campus_11_592");
			campus_r_592 = createCampus(campus_592,do_spherical);
		}else {
			campus_592 = rv.createNetReplica("campus_11_592",baseCampus);
			campus_r_592 = (IRouter)campus_592.getChildByName("sub_campus_router");
		}
		ILink l_campus_592 = rv.createLink("l_campus_592");
		l_campus_592.createInterface(r_592.createInterface("if_campus_592"));
		l_campus_592.createInterface((IInterface)campus_r_592.getChildByName("if_stub"));

		INet campus_601 = null;
		IRouter campus_r_601 = null;
		if(null == baseCampus) {
			campus_601 = rv.createNet("campus_11_601");
			campus_r_601 = createCampus(campus_601,do_spherical);
		}else {
			campus_601 = rv.createNetReplica("campus_11_601",baseCampus);
			campus_r_601 = (IRouter)campus_601.getChildByName("sub_campus_router");
		}
		ILink l_campus_601 = rv.createLink("l_campus_601");
		l_campus_601.createInterface(r_601.createInterface("if_campus_601"));
		l_campus_601.createInterface((IInterface)campus_r_601.getChildByName("if_stub"));

		INet campus_608 = null;
		IRouter campus_r_608 = null;
		if(null == baseCampus) {
			campus_608 = rv.createNet("campus_11_608");
			campus_r_608 = createCampus(campus_608,do_spherical);
		}else {
			campus_608 = rv.createNetReplica("campus_11_608",baseCampus);
			campus_r_608 = (IRouter)campus_608.getChildByName("sub_campus_router");
		}
		ILink l_campus_608 = rv.createLink("l_campus_608");
		l_campus_608.createInterface(r_608.createInterface("if_campus_608"));
		l_campus_608.createInterface((IInterface)campus_r_608.getChildByName("if_stub"));

		INet campus_609 = null;
		IRouter campus_r_609 = null;
		if(null == baseCampus) {
			campus_609 = rv.createNet("campus_11_609");
			campus_r_609 = createCampus(campus_609,do_spherical);
		}else {
			campus_609 = rv.createNetReplica("campus_11_609",baseCampus);
			campus_r_609 = (IRouter)campus_609.getChildByName("sub_campus_router");
		}
		ILink l_campus_609 = rv.createLink("l_campus_609");
		l_campus_609.createInterface(r_609.createInterface("if_campus_609"));
		l_campus_609.createInterface((IInterface)campus_r_609.getChildByName("if_stub"));
		return rv;
	}
	public INet create_net_12(INet top, Map<Integer,IRouter> routers) {
		INet rv = top.createNet("net_12");
		if(do_spherical) rv.createShortestPath();;

		//create routers
		IRouter r_65 = rv.createRouter("r_65");
		routers.put(65,r_65);

		IRouter r_66 = rv.createRouter("r_66");
		routers.put(66,r_66);

		IRouter r_83 = rv.createRouter("r_83");
		routers.put(83,r_83);

		IRouter r_85 = rv.createRouter("r_85");
		routers.put(85,r_85);

		IRouter r_104 = rv.createRouter("r_104");
		routers.put(104,r_104);

		IRouter r_105 = rv.createRouter("r_105");
		routers.put(105,r_105);

		IRouter r_106 = rv.createRouter("r_106");
		routers.put(106,r_106);

		IRouter r_107 = rv.createRouter("r_107");
		routers.put(107,r_107);

		IRouter r_156 = rv.createRouter("r_156");
		routers.put(156,r_156);

		IRouter r_157 = rv.createRouter("r_157");
		routers.put(157,r_157);

		IRouter r_158 = rv.createRouter("r_158");
		routers.put(158,r_158);

		IRouter r_171 = rv.createRouter("r_171");
		routers.put(171,r_171);

		IRouter r_222 = rv.createRouter("r_222");
		routers.put(222,r_222);

		IRouter r_223 = rv.createRouter("r_223");
		routers.put(223,r_223);

		IRouter r_224 = rv.createRouter("r_224");
		routers.put(224,r_224);

		IRouter r_225 = rv.createRouter("r_225");
		routers.put(225,r_225);

		IRouter r_281 = rv.createRouter("r_281");
		routers.put(281,r_281);

		IRouter r_293 = rv.createRouter("r_293");
		routers.put(293,r_293);

		IRouter r_294 = rv.createRouter("r_294");
		routers.put(294,r_294);

		IRouter r_331 = rv.createRouter("r_331");
		routers.put(331,r_331);

		IRouter r_333 = rv.createRouter("r_333");
		routers.put(333,r_333);

		IRouter r_435 = rv.createRouter("r_435");
		routers.put(435,r_435);

		IRouter r_548 = rv.createRouter("r_548");
		routers.put(548,r_548);

		IRouter r_549 = rv.createRouter("r_549");
		routers.put(549,r_549);

		IRouter r_550 = rv.createRouter("r_550");
		routers.put(550,r_550);

		IRouter r_551 = rv.createRouter("r_551");
		routers.put(551,r_551);

		IRouter r_552 = rv.createRouter("r_552");
		routers.put(552,r_552);

		IRouter r_553 = rv.createRouter("r_553");
		routers.put(553,r_553);

		IRouter r_554 = rv.createRouter("r_554");
		routers.put(554,r_554);

		IRouter r_555 = rv.createRouter("r_555");
		routers.put(555,r_555);

		IRouter r_556 = rv.createRouter("r_556");
		routers.put(556,r_556);

		IRouter r_557 = rv.createRouter("r_557");
		routers.put(557,r_557);

		IRouter r_558 = rv.createRouter("r_558");
		routers.put(558,r_558);

		IRouter r_559 = rv.createRouter("r_559");
		routers.put(559,r_559);

		IRouter r_560 = rv.createRouter("r_560");
		routers.put(560,r_560);

		IRouter r_561 = rv.createRouter("r_561");
		routers.put(561,r_561);

		IRouter r_562 = rv.createRouter("r_562");
		routers.put(562,r_562);

		IRouter r_563 = rv.createRouter("r_563");
		routers.put(563,r_563);

		IRouter r_581 = rv.createRouter("r_581");
		routers.put(581,r_581);

		IRouter r_623 = rv.createRouter("r_623");
		routers.put(623,r_623);

		//create links
		ILink l_66_65 = rv.createLink("l_66_65");
		l_66_65.createInterface(routers.get(66).createInterface("if_65"));
		l_66_65.createInterface(routers.get(65).createInterface("if_66"));

		ILink l_104_66 = rv.createLink("l_104_66");
		l_104_66.createInterface(routers.get(104).createInterface("if_66"));
		l_104_66.createInterface(routers.get(66).createInterface("if_104"));

		ILink l_105_66 = rv.createLink("l_105_66");
		l_105_66.createInterface(routers.get(105).createInterface("if_66"));
		l_105_66.createInterface(routers.get(66).createInterface("if_105"));

		ILink l_106_66 = rv.createLink("l_106_66");
		l_106_66.createInterface(routers.get(106).createInterface("if_66"));
		l_106_66.createInterface(routers.get(66).createInterface("if_106"));

		ILink l_107_66 = rv.createLink("l_107_66");
		l_107_66.createInterface(routers.get(107).createInterface("if_66"));
		l_107_66.createInterface(routers.get(66).createInterface("if_107"));

		ILink l_224_66 = rv.createLink("l_224_66");
		l_224_66.createInterface(routers.get(224).createInterface("if_66"));
		l_224_66.createInterface(routers.get(66).createInterface("if_224"));

		ILink l_225_66 = rv.createLink("l_225_66");
		l_225_66.createInterface(routers.get(225).createInterface("if_66"));
		l_225_66.createInterface(routers.get(66).createInterface("if_225"));

		ILink l_560_66 = rv.createLink("l_560_66");
		l_560_66.createInterface(routers.get(560).createInterface("if_66"));
		l_560_66.createInterface(routers.get(66).createInterface("if_560"));

		ILink l_562_66 = rv.createLink("l_562_66");
		l_562_66.createInterface(routers.get(562).createInterface("if_66"));
		l_562_66.createInterface(routers.get(66).createInterface("if_562"));

		ILink l_563_66 = rv.createLink("l_563_66");
		l_563_66.createInterface(routers.get(563).createInterface("if_66"));
		l_563_66.createInterface(routers.get(66).createInterface("if_563"));

		ILink l_85_83 = rv.createLink("l_85_83");
		l_85_83.createInterface(routers.get(85).createInterface("if_83"));
		l_85_83.createInterface(routers.get(83).createInterface("if_85"));

		ILink l_549_85 = rv.createLink("l_549_85");
		l_549_85.createInterface(routers.get(549).createInterface("if_85"));
		l_549_85.createInterface(routers.get(85).createInterface("if_549"));

		ILink l_551_85 = rv.createLink("l_551_85");
		l_551_85.createInterface(routers.get(551).createInterface("if_85"));
		l_551_85.createInterface(routers.get(85).createInterface("if_551"));

		ILink l_105_104 = rv.createLink("l_105_104");
		l_105_104.createInterface(routers.get(105).createInterface("if_104"));
		l_105_104.createInterface(routers.get(104).createInterface("if_105"));

		ILink l_106_104 = rv.createLink("l_106_104");
		l_106_104.createInterface(routers.get(106).createInterface("if_104"));
		l_106_104.createInterface(routers.get(104).createInterface("if_106"));

		ILink l_107_104 = rv.createLink("l_107_104");
		l_107_104.createInterface(routers.get(107).createInterface("if_104"));
		l_107_104.createInterface(routers.get(104).createInterface("if_107"));

		ILink l_106_105 = rv.createLink("l_106_105");
		l_106_105.createInterface(routers.get(106).createInterface("if_105"));
		l_106_105.createInterface(routers.get(105).createInterface("if_106"));

		ILink l_107_105 = rv.createLink("l_107_105");
		l_107_105.createInterface(routers.get(107).createInterface("if_105"));
		l_107_105.createInterface(routers.get(105).createInterface("if_107"));

		ILink l_171_105 = rv.createLink("l_171_105");
		l_171_105.createInterface(routers.get(171).createInterface("if_105"));
		l_171_105.createInterface(routers.get(105).createInterface("if_171"));

		ILink l_222_105 = rv.createLink("l_222_105");
		l_222_105.createInterface(routers.get(222).createInterface("if_105"));
		l_222_105.createInterface(routers.get(105).createInterface("if_222"));

		ILink l_223_105 = rv.createLink("l_223_105");
		l_223_105.createInterface(routers.get(223).createInterface("if_105"));
		l_223_105.createInterface(routers.get(105).createInterface("if_223"));

		ILink l_224_105 = rv.createLink("l_224_105");
		l_224_105.createInterface(routers.get(224).createInterface("if_105"));
		l_224_105.createInterface(routers.get(105).createInterface("if_224"));

		ILink l_225_105 = rv.createLink("l_225_105");
		l_225_105.createInterface(routers.get(225).createInterface("if_105"));
		l_225_105.createInterface(routers.get(105).createInterface("if_225"));

		ILink l_224_106 = rv.createLink("l_224_106");
		l_224_106.createInterface(routers.get(224).createInterface("if_106"));
		l_224_106.createInterface(routers.get(106).createInterface("if_224"));

		ILink l_281_106 = rv.createLink("l_281_106");
		l_281_106.createInterface(routers.get(281).createInterface("if_106"));
		l_281_106.createInterface(routers.get(106).createInterface("if_281"));

		ILink l_293_106 = rv.createLink("l_293_106");
		l_293_106.createInterface(routers.get(293).createInterface("if_106"));
		l_293_106.createInterface(routers.get(106).createInterface("if_293"));

		ILink l_294_106 = rv.createLink("l_294_106");
		l_294_106.createInterface(routers.get(294).createInterface("if_106"));
		l_294_106.createInterface(routers.get(106).createInterface("if_294"));

		ILink l_549_106 = rv.createLink("l_549_106");
		l_549_106.createInterface(routers.get(549).createInterface("if_106"));
		l_549_106.createInterface(routers.get(106).createInterface("if_549"));

		ILink l_550_106 = rv.createLink("l_550_106");
		l_550_106.createInterface(routers.get(550).createInterface("if_106"));
		l_550_106.createInterface(routers.get(106).createInterface("if_550"));

		ILink l_551_106 = rv.createLink("l_551_106");
		l_551_106.createInterface(routers.get(551).createInterface("if_106"));
		l_551_106.createInterface(routers.get(106).createInterface("if_551"));

		ILink l_556_106 = rv.createLink("l_556_106");
		l_556_106.createInterface(routers.get(556).createInterface("if_106"));
		l_556_106.createInterface(routers.get(106).createInterface("if_556"));

		ILink l_559_106 = rv.createLink("l_559_106");
		l_559_106.createInterface(routers.get(559).createInterface("if_106"));
		l_559_106.createInterface(routers.get(106).createInterface("if_559"));

		ILink l_623_106 = rv.createLink("l_623_106");
		l_623_106.createInterface(routers.get(623).createInterface("if_106"));
		l_623_106.createInterface(routers.get(106).createInterface("if_623"));

		ILink l_281_107 = rv.createLink("l_281_107");
		l_281_107.createInterface(routers.get(281).createInterface("if_107"));
		l_281_107.createInterface(routers.get(107).createInterface("if_281"));

		ILink l_294_107 = rv.createLink("l_294_107");
		l_294_107.createInterface(routers.get(294).createInterface("if_107"));
		l_294_107.createInterface(routers.get(107).createInterface("if_294"));

		ILink l_551_107 = rv.createLink("l_551_107");
		l_551_107.createInterface(routers.get(551).createInterface("if_107"));
		l_551_107.createInterface(routers.get(107).createInterface("if_551"));

		ILink l_559_107 = rv.createLink("l_559_107");
		l_559_107.createInterface(routers.get(559).createInterface("if_107"));
		l_559_107.createInterface(routers.get(107).createInterface("if_559"));

		ILink l_623_107 = rv.createLink("l_623_107");
		l_623_107.createInterface(routers.get(623).createInterface("if_107"));
		l_623_107.createInterface(routers.get(107).createInterface("if_623"));

		ILink l_157_156 = rv.createLink("l_157_156");
		l_157_156.createInterface(routers.get(157).createInterface("if_156"));
		l_157_156.createInterface(routers.get(156).createInterface("if_157"));

		ILink l_158_156 = rv.createLink("l_158_156");
		l_158_156.createInterface(routers.get(158).createInterface("if_156"));
		l_158_156.createInterface(routers.get(156).createInterface("if_158"));

		ILink l_158_157 = rv.createLink("l_158_157");
		l_158_157.createInterface(routers.get(158).createInterface("if_157"));
		l_158_157.createInterface(routers.get(157).createInterface("if_158"));

		ILink l_555_157 = rv.createLink("l_555_157");
		l_555_157.createInterface(routers.get(555).createInterface("if_157"));
		l_555_157.createInterface(routers.get(157).createInterface("if_555"));

		ILink l_560_157 = rv.createLink("l_560_157");
		l_560_157.createInterface(routers.get(560).createInterface("if_157"));
		l_560_157.createInterface(routers.get(157).createInterface("if_560"));

		ILink l_562_157 = rv.createLink("l_562_157");
		l_562_157.createInterface(routers.get(562).createInterface("if_157"));
		l_562_157.createInterface(routers.get(157).createInterface("if_562"));

		ILink l_563_157 = rv.createLink("l_563_157");
		l_563_157.createInterface(routers.get(563).createInterface("if_157"));
		l_563_157.createInterface(routers.get(157).createInterface("if_563"));

		ILink l_555_158 = rv.createLink("l_555_158");
		l_555_158.createInterface(routers.get(555).createInterface("if_158"));
		l_555_158.createInterface(routers.get(158).createInterface("if_555"));

		ILink l_560_158 = rv.createLink("l_560_158");
		l_560_158.createInterface(routers.get(560).createInterface("if_158"));
		l_560_158.createInterface(routers.get(158).createInterface("if_560"));

		ILink l_562_158 = rv.createLink("l_562_158");
		l_562_158.createInterface(routers.get(562).createInterface("if_158"));
		l_562_158.createInterface(routers.get(158).createInterface("if_562"));

		ILink l_563_158 = rv.createLink("l_563_158");
		l_563_158.createInterface(routers.get(563).createInterface("if_158"));
		l_563_158.createInterface(routers.get(158).createInterface("if_563"));

		ILink l_548_224 = rv.createLink("l_548_224");
		l_548_224.createInterface(routers.get(548).createInterface("if_224"));
		l_548_224.createInterface(routers.get(224).createInterface("if_548"));

		ILink l_552_224 = rv.createLink("l_552_224");
		l_552_224.createInterface(routers.get(552).createInterface("if_224"));
		l_552_224.createInterface(routers.get(224).createInterface("if_552"));

		ILink l_555_224 = rv.createLink("l_555_224");
		l_555_224.createInterface(routers.get(555).createInterface("if_224"));
		l_555_224.createInterface(routers.get(224).createInterface("if_555"));

		ILink l_559_224 = rv.createLink("l_559_224");
		l_559_224.createInterface(routers.get(559).createInterface("if_224"));
		l_559_224.createInterface(routers.get(224).createInterface("if_559"));

		ILink l_293_225 = rv.createLink("l_293_225");
		l_293_225.createInterface(routers.get(293).createInterface("if_225"));
		l_293_225.createInterface(routers.get(225).createInterface("if_293"));

		ILink l_551_225 = rv.createLink("l_551_225");
		l_551_225.createInterface(routers.get(551).createInterface("if_225"));
		l_551_225.createInterface(routers.get(225).createInterface("if_551"));

		ILink l_559_225 = rv.createLink("l_559_225");
		l_559_225.createInterface(routers.get(559).createInterface("if_225"));
		l_559_225.createInterface(routers.get(225).createInterface("if_559"));

		ILink l_331_281 = rv.createLink("l_331_281");
		l_331_281.createInterface(routers.get(331).createInterface("if_281"));
		l_331_281.createInterface(routers.get(281).createInterface("if_331"));

		ILink l_331_293 = rv.createLink("l_331_293");
		l_331_293.createInterface(routers.get(331).createInterface("if_293"));
		l_331_293.createInterface(routers.get(293).createInterface("if_331"));

		ILink l_331_294 = rv.createLink("l_331_294");
		l_331_294.createInterface(routers.get(331).createInterface("if_294"));
		l_331_294.createInterface(routers.get(294).createInterface("if_331"));

		ILink l_333_331 = rv.createLink("l_333_331");
		l_333_331.createInterface(routers.get(333).createInterface("if_331"));
		l_333_331.createInterface(routers.get(331).createInterface("if_333"));

		ILink l_559_435 = rv.createLink("l_559_435");
		l_559_435.createInterface(routers.get(559).createInterface("if_435"));
		l_559_435.createInterface(routers.get(435).createInterface("if_559"));

		ILink l_581_435 = rv.createLink("l_581_435");
		l_581_435.createInterface(routers.get(581).createInterface("if_435"));
		l_581_435.createInterface(routers.get(435).createInterface("if_581"));

		ILink l_623_435 = rv.createLink("l_623_435");
		l_623_435.createInterface(routers.get(623).createInterface("if_435"));
		l_623_435.createInterface(routers.get(435).createInterface("if_623"));

		ILink l_549_548 = rv.createLink("l_549_548");
		l_549_548.createInterface(routers.get(549).createInterface("if_548"));
		l_549_548.createInterface(routers.get(548).createInterface("if_549"));

		ILink l_550_548 = rv.createLink("l_550_548");
		l_550_548.createInterface(routers.get(550).createInterface("if_548"));
		l_550_548.createInterface(routers.get(548).createInterface("if_550"));

		ILink l_551_548 = rv.createLink("l_551_548");
		l_551_548.createInterface(routers.get(551).createInterface("if_548"));
		l_551_548.createInterface(routers.get(548).createInterface("if_551"));

		ILink l_552_549 = rv.createLink("l_552_549");
		l_552_549.createInterface(routers.get(552).createInterface("if_549"));
		l_552_549.createInterface(routers.get(549).createInterface("if_552"));

		ILink l_553_549 = rv.createLink("l_553_549");
		l_553_549.createInterface(routers.get(553).createInterface("if_549"));
		l_553_549.createInterface(routers.get(549).createInterface("if_553"));

		ILink l_554_549 = rv.createLink("l_554_549");
		l_554_549.createInterface(routers.get(554).createInterface("if_549"));
		l_554_549.createInterface(routers.get(549).createInterface("if_554"));

		ILink l_555_549 = rv.createLink("l_555_549");
		l_555_549.createInterface(routers.get(555).createInterface("if_549"));
		l_555_549.createInterface(routers.get(549).createInterface("if_555"));

		ILink l_552_550 = rv.createLink("l_552_550");
		l_552_550.createInterface(routers.get(552).createInterface("if_550"));
		l_552_550.createInterface(routers.get(550).createInterface("if_552"));

		ILink l_553_550 = rv.createLink("l_553_550");
		l_553_550.createInterface(routers.get(553).createInterface("if_550"));
		l_553_550.createInterface(routers.get(550).createInterface("if_553"));

		ILink l_554_550 = rv.createLink("l_554_550");
		l_554_550.createInterface(routers.get(554).createInterface("if_550"));
		l_554_550.createInterface(routers.get(550).createInterface("if_554"));

		ILink l_555_550 = rv.createLink("l_555_550");
		l_555_550.createInterface(routers.get(555).createInterface("if_550"));
		l_555_550.createInterface(routers.get(550).createInterface("if_555"));

		ILink l_552_551 = rv.createLink("l_552_551");
		l_552_551.createInterface(routers.get(552).createInterface("if_551"));
		l_552_551.createInterface(routers.get(551).createInterface("if_552"));

		ILink l_553_551 = rv.createLink("l_553_551");
		l_553_551.createInterface(routers.get(553).createInterface("if_551"));
		l_553_551.createInterface(routers.get(551).createInterface("if_553"));

		ILink l_554_551 = rv.createLink("l_554_551");
		l_554_551.createInterface(routers.get(554).createInterface("if_551"));
		l_554_551.createInterface(routers.get(551).createInterface("if_554"));

		ILink l_555_551 = rv.createLink("l_555_551");
		l_555_551.createInterface(routers.get(555).createInterface("if_551"));
		l_555_551.createInterface(routers.get(551).createInterface("if_555"));

		ILink l_556_552 = rv.createLink("l_556_552");
		l_556_552.createInterface(routers.get(556).createInterface("if_552"));
		l_556_552.createInterface(routers.get(552).createInterface("if_556"));

		ILink l_557_552 = rv.createLink("l_557_552");
		l_557_552.createInterface(routers.get(557).createInterface("if_552"));
		l_557_552.createInterface(routers.get(552).createInterface("if_557"));

		ILink l_558_552 = rv.createLink("l_558_552");
		l_558_552.createInterface(routers.get(558).createInterface("if_552"));
		l_558_552.createInterface(routers.get(552).createInterface("if_558"));

		ILink l_556_555 = rv.createLink("l_556_555");
		l_556_555.createInterface(routers.get(556).createInterface("if_555"));
		l_556_555.createInterface(routers.get(555).createInterface("if_556"));

		ILink l_560_555 = rv.createLink("l_560_555");
		l_560_555.createInterface(routers.get(560).createInterface("if_555"));
		l_560_555.createInterface(routers.get(555).createInterface("if_560"));

		ILink l_561_555 = rv.createLink("l_561_555");
		l_561_555.createInterface(routers.get(561).createInterface("if_555"));
		l_561_555.createInterface(routers.get(555).createInterface("if_561"));

		ILink l_563_555 = rv.createLink("l_563_555");
		l_563_555.createInterface(routers.get(563).createInterface("if_555"));
		l_563_555.createInterface(routers.get(555).createInterface("if_563"));

		ILink l_561_560 = rv.createLink("l_561_560");
		l_561_560.createInterface(routers.get(561).createInterface("if_560"));
		l_561_560.createInterface(routers.get(560).createInterface("if_561"));

		ILink l_562_561 = rv.createLink("l_562_561");
		l_562_561.createInterface(routers.get(562).createInterface("if_561"));
		l_562_561.createInterface(routers.get(561).createInterface("if_562"));

		ILink l_563_561 = rv.createLink("l_563_561");
		l_563_561.createInterface(routers.get(563).createInterface("if_561"));
		l_563_561.createInterface(routers.get(561).createInterface("if_563"));

		//attach campus networks

		INet campus_65 = null;
		IRouter campus_r_65 = null;
		if(null == baseCampus) {
			campus_65 = rv.createNet("campus_12_65");
			campus_r_65 = createCampus(campus_65,do_spherical);
		}else {
			campus_65 = rv.createNetReplica("campus_12_65",baseCampus);
			campus_r_65 = (IRouter)campus_65.getChildByName("sub_campus_router");
		}
		ILink l_campus_65 = rv.createLink("l_campus_65");
		l_campus_65.createInterface(r_65.createInterface("if_campus_65"));
		l_campus_65.createInterface((IInterface)campus_r_65.getChildByName("if_stub"));

		INet campus_171 = null;
		IRouter campus_r_171 = null;
		if(null == baseCampus) {
			campus_171 = rv.createNet("campus_12_171");
			campus_r_171 = createCampus(campus_171,do_spherical);
		}else {
			campus_171 = rv.createNetReplica("campus_12_171",baseCampus);
			campus_r_171 = (IRouter)campus_171.getChildByName("sub_campus_router");
		}
		ILink l_campus_171 = rv.createLink("l_campus_171");
		l_campus_171.createInterface(r_171.createInterface("if_campus_171"));
		l_campus_171.createInterface((IInterface)campus_r_171.getChildByName("if_stub"));

		INet campus_222 = null;
		IRouter campus_r_222 = null;
		if(null == baseCampus) {
			campus_222 = rv.createNet("campus_12_222");
			campus_r_222 = createCampus(campus_222,do_spherical);
		}else {
			campus_222 = rv.createNetReplica("campus_12_222",baseCampus);
			campus_r_222 = (IRouter)campus_222.getChildByName("sub_campus_router");
		}
		ILink l_campus_222 = rv.createLink("l_campus_222");
		l_campus_222.createInterface(r_222.createInterface("if_campus_222"));
		l_campus_222.createInterface((IInterface)campus_r_222.getChildByName("if_stub"));

		INet campus_223 = null;
		IRouter campus_r_223 = null;
		if(null == baseCampus) {
			campus_223 = rv.createNet("campus_12_223");
			campus_r_223 = createCampus(campus_223,do_spherical);
		}else {
			campus_223 = rv.createNetReplica("campus_12_223",baseCampus);
			campus_r_223 = (IRouter)campus_223.getChildByName("sub_campus_router");
		}
		ILink l_campus_223 = rv.createLink("l_campus_223");
		l_campus_223.createInterface(r_223.createInterface("if_campus_223"));
		l_campus_223.createInterface((IInterface)campus_r_223.getChildByName("if_stub"));

		INet campus_333 = null;
		IRouter campus_r_333 = null;
		if(null == baseCampus) {
			campus_333 = rv.createNet("campus_12_333");
			campus_r_333 = createCampus(campus_333,do_spherical);
		}else {
			campus_333 = rv.createNetReplica("campus_12_333",baseCampus);
			campus_r_333 = (IRouter)campus_333.getChildByName("sub_campus_router");
		}
		ILink l_campus_333 = rv.createLink("l_campus_333");
		l_campus_333.createInterface(r_333.createInterface("if_campus_333"));
		l_campus_333.createInterface((IInterface)campus_r_333.getChildByName("if_stub"));

		INet campus_557 = null;
		IRouter campus_r_557 = null;
		if(null == baseCampus) {
			campus_557 = rv.createNet("campus_12_557");
			campus_r_557 = createCampus(campus_557,do_spherical);
		}else {
			campus_557 = rv.createNetReplica("campus_12_557",baseCampus);
			campus_r_557 = (IRouter)campus_557.getChildByName("sub_campus_router");
		}
		ILink l_campus_557 = rv.createLink("l_campus_557");
		l_campus_557.createInterface(r_557.createInterface("if_campus_557"));
		l_campus_557.createInterface((IInterface)campus_r_557.getChildByName("if_stub"));

		INet campus_558 = null;
		IRouter campus_r_558 = null;
		if(null == baseCampus) {
			campus_558 = rv.createNet("campus_12_558");
			campus_r_558 = createCampus(campus_558,do_spherical);
		}else {
			campus_558 = rv.createNetReplica("campus_12_558",baseCampus);
			campus_r_558 = (IRouter)campus_558.getChildByName("sub_campus_router");
		}
		ILink l_campus_558 = rv.createLink("l_campus_558");
		l_campus_558.createInterface(r_558.createInterface("if_campus_558"));
		l_campus_558.createInterface((IInterface)campus_r_558.getChildByName("if_stub"));

		INet campus_581 = null;
		IRouter campus_r_581 = null;
		if(null == baseCampus) {
			campus_581 = rv.createNet("campus_12_581");
			campus_r_581 = createCampus(campus_581,do_spherical);
		}else {
			campus_581 = rv.createNetReplica("campus_12_581",baseCampus);
			campus_r_581 = (IRouter)campus_581.getChildByName("sub_campus_router");
		}
		ILink l_campus_581 = rv.createLink("l_campus_581");
		l_campus_581.createInterface(r_581.createInterface("if_campus_581"));
		l_campus_581.createInterface((IInterface)campus_r_581.getChildByName("if_stub"));
		return rv;
	}
	public INet create_net_13(INet top, Map<Integer,IRouter> routers) {
		INet rv = top.createNet("net_13");
		if(do_spherical) rv.createShortestPath();;

		//create routers
		IRouter r_57 = rv.createRouter("r_57");
		routers.put(57,r_57);

		IRouter r_58 = rv.createRouter("r_58");
		routers.put(58,r_58);

		IRouter r_59 = rv.createRouter("r_59");
		routers.put(59,r_59);

		IRouter r_60 = rv.createRouter("r_60");
		routers.put(60,r_60);

		IRouter r_61 = rv.createRouter("r_61");
		routers.put(61,r_61);

		IRouter r_91 = rv.createRouter("r_91");
		routers.put(91,r_91);

		IRouter r_92 = rv.createRouter("r_92");
		routers.put(92,r_92);

		IRouter r_93 = rv.createRouter("r_93");
		routers.put(93,r_93);

		IRouter r_94 = rv.createRouter("r_94");
		routers.put(94,r_94);

		IRouter r_95 = rv.createRouter("r_95");
		routers.put(95,r_95);

		IRouter r_96 = rv.createRouter("r_96");
		routers.put(96,r_96);

		IRouter r_97 = rv.createRouter("r_97");
		routers.put(97,r_97);

		IRouter r_98 = rv.createRouter("r_98");
		routers.put(98,r_98);

		IRouter r_99 = rv.createRouter("r_99");
		routers.put(99,r_99);

		IRouter r_101 = rv.createRouter("r_101");
		routers.put(101,r_101);

		IRouter r_102 = rv.createRouter("r_102");
		routers.put(102,r_102);

		IRouter r_103 = rv.createRouter("r_103");
		routers.put(103,r_103);

		IRouter r_135 = rv.createRouter("r_135");
		routers.put(135,r_135);

		IRouter r_159 = rv.createRouter("r_159");
		routers.put(159,r_159);

		IRouter r_160 = rv.createRouter("r_160");
		routers.put(160,r_160);

		IRouter r_275 = rv.createRouter("r_275");
		routers.put(275,r_275);

		IRouter r_276 = rv.createRouter("r_276");
		routers.put(276,r_276);

		IRouter r_277 = rv.createRouter("r_277");
		routers.put(277,r_277);

		IRouter r_283 = rv.createRouter("r_283");
		routers.put(283,r_283);

		IRouter r_284 = rv.createRouter("r_284");
		routers.put(284,r_284);

		IRouter r_285 = rv.createRouter("r_285");
		routers.put(285,r_285);

		IRouter r_286 = rv.createRouter("r_286");
		routers.put(286,r_286);

		IRouter r_287 = rv.createRouter("r_287");
		routers.put(287,r_287);

		IRouter r_288 = rv.createRouter("r_288");
		routers.put(288,r_288);

		IRouter r_328 = rv.createRouter("r_328");
		routers.put(328,r_328);

		IRouter r_433 = rv.createRouter("r_433");
		routers.put(433,r_433);

		IRouter r_434 = rv.createRouter("r_434");
		routers.put(434,r_434);

		IRouter r_472 = rv.createRouter("r_472");
		routers.put(472,r_472);

		IRouter r_579 = rv.createRouter("r_579");
		routers.put(579,r_579);

		IRouter r_580 = rv.createRouter("r_580");
		routers.put(580,r_580);

		IRouter r_602 = rv.createRouter("r_602");
		routers.put(602,r_602);

		IRouter r_634 = rv.createRouter("r_634");
		routers.put(634,r_634);

		IRouter r_635 = rv.createRouter("r_635");
		routers.put(635,r_635);

		IRouter r_637 = rv.createRouter("r_637");
		routers.put(637,r_637);

		IRouter r_638 = rv.createRouter("r_638");
		routers.put(638,r_638);

		//create links
		ILink l_58_57 = rv.createLink("l_58_57");
		l_58_57.createInterface(routers.get(58).createInterface("if_57"));
		l_58_57.createInterface(routers.get(57).createInterface("if_58"));

		ILink l_59_57 = rv.createLink("l_59_57");
		l_59_57.createInterface(routers.get(59).createInterface("if_57"));
		l_59_57.createInterface(routers.get(57).createInterface("if_59"));

		ILink l_60_57 = rv.createLink("l_60_57");
		l_60_57.createInterface(routers.get(60).createInterface("if_57"));
		l_60_57.createInterface(routers.get(57).createInterface("if_60"));

		ILink l_61_57 = rv.createLink("l_61_57");
		l_61_57.createInterface(routers.get(61).createInterface("if_57"));
		l_61_57.createInterface(routers.get(57).createInterface("if_61"));

		ILink l_60_58 = rv.createLink("l_60_58");
		l_60_58.createInterface(routers.get(60).createInterface("if_58"));
		l_60_58.createInterface(routers.get(58).createInterface("if_60"));

		ILink l_91_58 = rv.createLink("l_91_58");
		l_91_58.createInterface(routers.get(91).createInterface("if_58"));
		l_91_58.createInterface(routers.get(58).createInterface("if_91"));

		ILink l_92_58 = rv.createLink("l_92_58");
		l_92_58.createInterface(routers.get(92).createInterface("if_58"));
		l_92_58.createInterface(routers.get(58).createInterface("if_92"));

		ILink l_93_58 = rv.createLink("l_93_58");
		l_93_58.createInterface(routers.get(93).createInterface("if_58"));
		l_93_58.createInterface(routers.get(58).createInterface("if_93"));

		ILink l_94_58 = rv.createLink("l_94_58");
		l_94_58.createInterface(routers.get(94).createInterface("if_58"));
		l_94_58.createInterface(routers.get(58).createInterface("if_94"));

		ILink l_95_58 = rv.createLink("l_95_58");
		l_95_58.createInterface(routers.get(95).createInterface("if_58"));
		l_95_58.createInterface(routers.get(58).createInterface("if_95"));

		ILink l_96_58 = rv.createLink("l_96_58");
		l_96_58.createInterface(routers.get(96).createInterface("if_58"));
		l_96_58.createInterface(routers.get(58).createInterface("if_96"));

		ILink l_97_58 = rv.createLink("l_97_58");
		l_97_58.createInterface(routers.get(97).createInterface("if_58"));
		l_97_58.createInterface(routers.get(58).createInterface("if_97"));

		ILink l_98_58 = rv.createLink("l_98_58");
		l_98_58.createInterface(routers.get(98).createInterface("if_58"));
		l_98_58.createInterface(routers.get(58).createInterface("if_98"));

		ILink l_99_58 = rv.createLink("l_99_58");
		l_99_58.createInterface(routers.get(99).createInterface("if_58"));
		l_99_58.createInterface(routers.get(58).createInterface("if_99"));

		ILink l_101_58 = rv.createLink("l_101_58");
		l_101_58.createInterface(routers.get(101).createInterface("if_58"));
		l_101_58.createInterface(routers.get(58).createInterface("if_101"));

		ILink l_60_59 = rv.createLink("l_60_59");
		l_60_59.createInterface(routers.get(60).createInterface("if_59"));
		l_60_59.createInterface(routers.get(59).createInterface("if_60"));

		ILink l_91_59 = rv.createLink("l_91_59");
		l_91_59.createInterface(routers.get(91).createInterface("if_59"));
		l_91_59.createInterface(routers.get(59).createInterface("if_91"));

		ILink l_92_59 = rv.createLink("l_92_59");
		l_92_59.createInterface(routers.get(92).createInterface("if_59"));
		l_92_59.createInterface(routers.get(59).createInterface("if_92"));

		ILink l_93_59 = rv.createLink("l_93_59");
		l_93_59.createInterface(routers.get(93).createInterface("if_59"));
		l_93_59.createInterface(routers.get(59).createInterface("if_93"));

		ILink l_94_59 = rv.createLink("l_94_59");
		l_94_59.createInterface(routers.get(94).createInterface("if_59"));
		l_94_59.createInterface(routers.get(59).createInterface("if_94"));

		ILink l_95_59 = rv.createLink("l_95_59");
		l_95_59.createInterface(routers.get(95).createInterface("if_59"));
		l_95_59.createInterface(routers.get(59).createInterface("if_95"));

		ILink l_96_59 = rv.createLink("l_96_59");
		l_96_59.createInterface(routers.get(96).createInterface("if_59"));
		l_96_59.createInterface(routers.get(59).createInterface("if_96"));

		ILink l_97_59 = rv.createLink("l_97_59");
		l_97_59.createInterface(routers.get(97).createInterface("if_59"));
		l_97_59.createInterface(routers.get(59).createInterface("if_97"));

		ILink l_98_59 = rv.createLink("l_98_59");
		l_98_59.createInterface(routers.get(98).createInterface("if_59"));
		l_98_59.createInterface(routers.get(59).createInterface("if_98"));

		ILink l_99_59 = rv.createLink("l_99_59");
		l_99_59.createInterface(routers.get(99).createInterface("if_59"));
		l_99_59.createInterface(routers.get(59).createInterface("if_99"));

		ILink l_101_59 = rv.createLink("l_101_59");
		l_101_59.createInterface(routers.get(101).createInterface("if_59"));
		l_101_59.createInterface(routers.get(59).createInterface("if_101"));

		ILink l_102_59 = rv.createLink("l_102_59");
		l_102_59.createInterface(routers.get(102).createInterface("if_59"));
		l_102_59.createInterface(routers.get(59).createInterface("if_102"));

		ILink l_103_59 = rv.createLink("l_103_59");
		l_103_59.createInterface(routers.get(103).createInterface("if_59"));
		l_103_59.createInterface(routers.get(59).createInterface("if_103"));

		ILink l_61_60 = rv.createLink("l_61_60");
		l_61_60.createInterface(routers.get(61).createInterface("if_60"));
		l_61_60.createInterface(routers.get(60).createInterface("if_61"));

		ILink l_91_60 = rv.createLink("l_91_60");
		l_91_60.createInterface(routers.get(91).createInterface("if_60"));
		l_91_60.createInterface(routers.get(60).createInterface("if_91"));

		ILink l_94_60 = rv.createLink("l_94_60");
		l_94_60.createInterface(routers.get(94).createInterface("if_60"));
		l_94_60.createInterface(routers.get(60).createInterface("if_94"));

		ILink l_99_60 = rv.createLink("l_99_60");
		l_99_60.createInterface(routers.get(99).createInterface("if_60"));
		l_99_60.createInterface(routers.get(60).createInterface("if_99"));

		ILink l_102_60 = rv.createLink("l_102_60");
		l_102_60.createInterface(routers.get(102).createInterface("if_60"));
		l_102_60.createInterface(routers.get(60).createInterface("if_102"));

		ILink l_103_60 = rv.createLink("l_103_60");
		l_103_60.createInterface(routers.get(103).createInterface("if_60"));
		l_103_60.createInterface(routers.get(60).createInterface("if_103"));

		ILink l_135_60 = rv.createLink("l_135_60");
		l_135_60.createInterface(routers.get(135).createInterface("if_60"));
		l_135_60.createInterface(routers.get(60).createInterface("if_135"));

		ILink l_283_60 = rv.createLink("l_283_60");
		l_283_60.createInterface(routers.get(283).createInterface("if_60"));
		l_283_60.createInterface(routers.get(60).createInterface("if_283"));

		ILink l_284_60 = rv.createLink("l_284_60");
		l_284_60.createInterface(routers.get(284).createInterface("if_60"));
		l_284_60.createInterface(routers.get(60).createInterface("if_284"));

		ILink l_285_60 = rv.createLink("l_285_60");
		l_285_60.createInterface(routers.get(285).createInterface("if_60"));
		l_285_60.createInterface(routers.get(60).createInterface("if_285"));

		ILink l_286_60 = rv.createLink("l_286_60");
		l_286_60.createInterface(routers.get(286).createInterface("if_60"));
		l_286_60.createInterface(routers.get(60).createInterface("if_286"));

		ILink l_287_60 = rv.createLink("l_287_60");
		l_287_60.createInterface(routers.get(287).createInterface("if_60"));
		l_287_60.createInterface(routers.get(60).createInterface("if_287"));

		ILink l_288_60 = rv.createLink("l_288_60");
		l_288_60.createInterface(routers.get(288).createInterface("if_60"));
		l_288_60.createInterface(routers.get(60).createInterface("if_288"));

		ILink l_92_61 = rv.createLink("l_92_61");
		l_92_61.createInterface(routers.get(92).createInterface("if_61"));
		l_92_61.createInterface(routers.get(61).createInterface("if_92"));

		ILink l_99_61 = rv.createLink("l_99_61");
		l_99_61.createInterface(routers.get(99).createInterface("if_61"));
		l_99_61.createInterface(routers.get(61).createInterface("if_99"));

		ILink l_101_61 = rv.createLink("l_101_61");
		l_101_61.createInterface(routers.get(101).createInterface("if_61"));
		l_101_61.createInterface(routers.get(61).createInterface("if_101"));

		ILink l_602_99 = rv.createLink("l_602_99");
		l_602_99.createInterface(routers.get(602).createInterface("if_99"));
		l_602_99.createInterface(routers.get(99).createInterface("if_602"));

		ILink l_159_101 = rv.createLink("l_159_101");
		l_159_101.createInterface(routers.get(159).createInterface("if_101"));
		l_159_101.createInterface(routers.get(101).createInterface("if_159"));

		ILink l_160_101 = rv.createLink("l_160_101");
		l_160_101.createInterface(routers.get(160).createInterface("if_101"));
		l_160_101.createInterface(routers.get(101).createInterface("if_160"));

		ILink l_638_101 = rv.createLink("l_638_101");
		l_638_101.createInterface(routers.get(638).createInterface("if_101"));
		l_638_101.createInterface(routers.get(101).createInterface("if_638"));

		ILink l_160_159 = rv.createLink("l_160_159");
		l_160_159.createInterface(routers.get(160).createInterface("if_159"));
		l_160_159.createInterface(routers.get(159).createInterface("if_160"));

		ILink l_275_160 = rv.createLink("l_275_160");
		l_275_160.createInterface(routers.get(275).createInterface("if_160"));
		l_275_160.createInterface(routers.get(160).createInterface("if_275"));

		ILink l_637_160 = rv.createLink("l_637_160");
		l_637_160.createInterface(routers.get(637).createInterface("if_160"));
		l_637_160.createInterface(routers.get(160).createInterface("if_637"));

		ILink l_277_276 = rv.createLink("l_277_276");
		l_277_276.createInterface(routers.get(277).createInterface("if_276"));
		l_277_276.createInterface(routers.get(276).createInterface("if_277"));

		ILink l_283_277 = rv.createLink("l_283_277");
		l_283_277.createInterface(routers.get(283).createInterface("if_277"));
		l_283_277.createInterface(routers.get(277).createInterface("if_283"));

		ILink l_328_277 = rv.createLink("l_328_277");
		l_328_277.createInterface(routers.get(328).createInterface("if_277"));
		l_328_277.createInterface(routers.get(277).createInterface("if_328"));

		ILink l_433_277 = rv.createLink("l_433_277");
		l_433_277.createInterface(routers.get(433).createInterface("if_277"));
		l_433_277.createInterface(routers.get(277).createInterface("if_433"));

		ILink l_434_277 = rv.createLink("l_434_277");
		l_434_277.createInterface(routers.get(434).createInterface("if_277"));
		l_434_277.createInterface(routers.get(277).createInterface("if_434"));

		ILink l_472_277 = rv.createLink("l_472_277");
		l_472_277.createInterface(routers.get(472).createInterface("if_277"));
		l_472_277.createInterface(routers.get(277).createInterface("if_472"));

		ILink l_579_277 = rv.createLink("l_579_277");
		l_579_277.createInterface(routers.get(579).createInterface("if_277"));
		l_579_277.createInterface(routers.get(277).createInterface("if_579"));

		ILink l_580_277 = rv.createLink("l_580_277");
		l_580_277.createInterface(routers.get(580).createInterface("if_277"));
		l_580_277.createInterface(routers.get(277).createInterface("if_580"));

		ILink l_433_283 = rv.createLink("l_433_283");
		l_433_283.createInterface(routers.get(433).createInterface("if_283"));
		l_433_283.createInterface(routers.get(283).createInterface("if_433"));

		ILink l_434_283 = rv.createLink("l_434_283");
		l_434_283.createInterface(routers.get(434).createInterface("if_283"));
		l_434_283.createInterface(routers.get(283).createInterface("if_434"));

		ILink l_634_433 = rv.createLink("l_634_433");
		l_634_433.createInterface(routers.get(634).createInterface("if_433"));
		l_634_433.createInterface(routers.get(433).createInterface("if_634"));

		ILink l_634_434 = rv.createLink("l_634_434");
		l_634_434.createInterface(routers.get(634).createInterface("if_434"));
		l_634_434.createInterface(routers.get(434).createInterface("if_634"));

		ILink l_635_634 = rv.createLink("l_635_634");
		l_635_634.createInterface(routers.get(635).createInterface("if_634"));
		l_635_634.createInterface(routers.get(634).createInterface("if_635"));

		//attach campus networks

		INet campus_135 = null;
		IRouter campus_r_135 = null;
		if(null == baseCampus) {
			campus_135 = rv.createNet("campus_13_135");
			campus_r_135 = createCampus(campus_135,do_spherical);
		}else {
			campus_135 = rv.createNetReplica("campus_13_135",baseCampus);
			campus_r_135 = (IRouter)campus_135.getChildByName("sub_campus_router");
		}
		ILink l_campus_135 = rv.createLink("l_campus_135");
		l_campus_135.createInterface(r_135.createInterface("if_campus_135"));
		l_campus_135.createInterface((IInterface)campus_r_135.getChildByName("if_stub"));

		INet campus_275 = null;
		IRouter campus_r_275 = null;
		if(null == baseCampus) {
			campus_275 = rv.createNet("campus_13_275");
			campus_r_275 = createCampus(campus_275,do_spherical);
		}else {
			campus_275 = rv.createNetReplica("campus_13_275",baseCampus);
			campus_r_275 = (IRouter)campus_275.getChildByName("sub_campus_router");
		}
		ILink l_campus_275 = rv.createLink("l_campus_275");
		l_campus_275.createInterface(r_275.createInterface("if_campus_275"));
		l_campus_275.createInterface((IInterface)campus_r_275.getChildByName("if_stub"));

		INet campus_276 = null;
		IRouter campus_r_276 = null;
		if(null == baseCampus) {
			campus_276 = rv.createNet("campus_13_276");
			campus_r_276 = createCampus(campus_276,do_spherical);
		}else {
			campus_276 = rv.createNetReplica("campus_13_276",baseCampus);
			campus_r_276 = (IRouter)campus_276.getChildByName("sub_campus_router");
		}
		ILink l_campus_276 = rv.createLink("l_campus_276");
		l_campus_276.createInterface(r_276.createInterface("if_campus_276"));
		l_campus_276.createInterface((IInterface)campus_r_276.getChildByName("if_stub"));

		INet campus_284 = null;
		IRouter campus_r_284 = null;
		if(null == baseCampus) {
			campus_284 = rv.createNet("campus_13_284");
			campus_r_284 = createCampus(campus_284,do_spherical);
		}else {
			campus_284 = rv.createNetReplica("campus_13_284",baseCampus);
			campus_r_284 = (IRouter)campus_284.getChildByName("sub_campus_router");
		}
		ILink l_campus_284 = rv.createLink("l_campus_284");
		l_campus_284.createInterface(r_284.createInterface("if_campus_284"));
		l_campus_284.createInterface((IInterface)campus_r_284.getChildByName("if_stub"));

		INet campus_285 = null;
		IRouter campus_r_285 = null;
		if(null == baseCampus) {
			campus_285 = rv.createNet("campus_13_285");
			campus_r_285 = createCampus(campus_285,do_spherical);
		}else {
			campus_285 = rv.createNetReplica("campus_13_285",baseCampus);
			campus_r_285 = (IRouter)campus_285.getChildByName("sub_campus_router");
		}
		ILink l_campus_285 = rv.createLink("l_campus_285");
		l_campus_285.createInterface(r_285.createInterface("if_campus_285"));
		l_campus_285.createInterface((IInterface)campus_r_285.getChildByName("if_stub"));

		INet campus_287 = null;
		IRouter campus_r_287 = null;
		if(null == baseCampus) {
			campus_287 = rv.createNet("campus_13_287");
			campus_r_287 = createCampus(campus_287,do_spherical);
		}else {
			campus_287 = rv.createNetReplica("campus_13_287",baseCampus);
			campus_r_287 = (IRouter)campus_287.getChildByName("sub_campus_router");
		}
		ILink l_campus_287 = rv.createLink("l_campus_287");
		l_campus_287.createInterface(r_287.createInterface("if_campus_287"));
		l_campus_287.createInterface((IInterface)campus_r_287.getChildByName("if_stub"));

		INet campus_288 = null;
		IRouter campus_r_288 = null;
		if(null == baseCampus) {
			campus_288 = rv.createNet("campus_13_288");
			campus_r_288 = createCampus(campus_288,do_spherical);
		}else {
			campus_288 = rv.createNetReplica("campus_13_288",baseCampus);
			campus_r_288 = (IRouter)campus_288.getChildByName("sub_campus_router");
		}
		ILink l_campus_288 = rv.createLink("l_campus_288");
		l_campus_288.createInterface(r_288.createInterface("if_campus_288"));
		l_campus_288.createInterface((IInterface)campus_r_288.getChildByName("if_stub"));

		INet campus_328 = null;
		IRouter campus_r_328 = null;
		if(null == baseCampus) {
			campus_328 = rv.createNet("campus_13_328");
			campus_r_328 = createCampus(campus_328,do_spherical);
		}else {
			campus_328 = rv.createNetReplica("campus_13_328",baseCampus);
			campus_r_328 = (IRouter)campus_328.getChildByName("sub_campus_router");
		}
		ILink l_campus_328 = rv.createLink("l_campus_328");
		l_campus_328.createInterface(r_328.createInterface("if_campus_328"));
		l_campus_328.createInterface((IInterface)campus_r_328.getChildByName("if_stub"));

		INet campus_472 = null;
		IRouter campus_r_472 = null;
		if(null == baseCampus) {
			campus_472 = rv.createNet("campus_13_472");
			campus_r_472 = createCampus(campus_472,do_spherical);
		}else {
			campus_472 = rv.createNetReplica("campus_13_472",baseCampus);
			campus_r_472 = (IRouter)campus_472.getChildByName("sub_campus_router");
		}
		ILink l_campus_472 = rv.createLink("l_campus_472");
		l_campus_472.createInterface(r_472.createInterface("if_campus_472"));
		l_campus_472.createInterface((IInterface)campus_r_472.getChildByName("if_stub"));

		INet campus_579 = null;
		IRouter campus_r_579 = null;
		if(null == baseCampus) {
			campus_579 = rv.createNet("campus_13_579");
			campus_r_579 = createCampus(campus_579,do_spherical);
		}else {
			campus_579 = rv.createNetReplica("campus_13_579",baseCampus);
			campus_r_579 = (IRouter)campus_579.getChildByName("sub_campus_router");
		}
		ILink l_campus_579 = rv.createLink("l_campus_579");
		l_campus_579.createInterface(r_579.createInterface("if_campus_579"));
		l_campus_579.createInterface((IInterface)campus_r_579.getChildByName("if_stub"));

		INet campus_580 = null;
		IRouter campus_r_580 = null;
		if(null == baseCampus) {
			campus_580 = rv.createNet("campus_13_580");
			campus_r_580 = createCampus(campus_580,do_spherical);
		}else {
			campus_580 = rv.createNetReplica("campus_13_580",baseCampus);
			campus_r_580 = (IRouter)campus_580.getChildByName("sub_campus_router");
		}
		ILink l_campus_580 = rv.createLink("l_campus_580");
		l_campus_580.createInterface(r_580.createInterface("if_campus_580"));
		l_campus_580.createInterface((IInterface)campus_r_580.getChildByName("if_stub"));

		INet campus_602 = null;
		IRouter campus_r_602 = null;
		if(null == baseCampus) {
			campus_602 = rv.createNet("campus_13_602");
			campus_r_602 = createCampus(campus_602,do_spherical);
		}else {
			campus_602 = rv.createNetReplica("campus_13_602",baseCampus);
			campus_r_602 = (IRouter)campus_602.getChildByName("sub_campus_router");
		}
		ILink l_campus_602 = rv.createLink("l_campus_602");
		l_campus_602.createInterface(r_602.createInterface("if_campus_602"));
		l_campus_602.createInterface((IInterface)campus_r_602.getChildByName("if_stub"));

		INet campus_635 = null;
		IRouter campus_r_635 = null;
		if(null == baseCampus) {
			campus_635 = rv.createNet("campus_13_635");
			campus_r_635 = createCampus(campus_635,do_spherical);
		}else {
			campus_635 = rv.createNetReplica("campus_13_635",baseCampus);
			campus_r_635 = (IRouter)campus_635.getChildByName("sub_campus_router");
		}
		ILink l_campus_635 = rv.createLink("l_campus_635");
		l_campus_635.createInterface(r_635.createInterface("if_campus_635"));
		l_campus_635.createInterface((IInterface)campus_r_635.getChildByName("if_stub"));

		INet campus_637 = null;
		IRouter campus_r_637 = null;
		if(null == baseCampus) {
			campus_637 = rv.createNet("campus_13_637");
			campus_r_637 = createCampus(campus_637,do_spherical);
		}else {
			campus_637 = rv.createNetReplica("campus_13_637",baseCampus);
			campus_r_637 = (IRouter)campus_637.getChildByName("sub_campus_router");
		}
		ILink l_campus_637 = rv.createLink("l_campus_637");
		l_campus_637.createInterface(r_637.createInterface("if_campus_637"));
		l_campus_637.createInterface((IInterface)campus_r_637.getChildByName("if_stub"));

		INet campus_638 = null;
		IRouter campus_r_638 = null;
		if(null == baseCampus) {
			campus_638 = rv.createNet("campus_13_638");
			campus_r_638 = createCampus(campus_638,do_spherical);
		}else {
			campus_638 = rv.createNetReplica("campus_13_638",baseCampus);
			campus_r_638 = (IRouter)campus_638.getChildByName("sub_campus_router");
		}
		ILink l_campus_638 = rv.createLink("l_campus_638");
		l_campus_638.createInterface(r_638.createInterface("if_campus_638"));
		l_campus_638.createInterface((IInterface)campus_r_638.getChildByName("if_stub"));
		return rv;
	}
	public INet create_net_14(INet top, Map<Integer,IRouter> routers) {
		INet rv = top.createNet("net_14");
		if(do_spherical) rv.createShortestPath();;

		//create routers
		IRouter r_63 = rv.createRouter("r_63");
		routers.put(63,r_63);

		IRouter r_64 = rv.createRouter("r_64");
		routers.put(64,r_64);

		IRouter r_121 = rv.createRouter("r_121");
		routers.put(121,r_121);

		IRouter r_122 = rv.createRouter("r_122");
		routers.put(122,r_122);

		IRouter r_144 = rv.createRouter("r_144");
		routers.put(144,r_144);

		IRouter r_145 = rv.createRouter("r_145");
		routers.put(145,r_145);

		IRouter r_173 = rv.createRouter("r_173");
		routers.put(173,r_173);

		IRouter r_198 = rv.createRouter("r_198");
		routers.put(198,r_198);

		IRouter r_199 = rv.createRouter("r_199");
		routers.put(199,r_199);

		IRouter r_200 = rv.createRouter("r_200");
		routers.put(200,r_200);

		IRouter r_201 = rv.createRouter("r_201");
		routers.put(201,r_201);

		IRouter r_203 = rv.createRouter("r_203");
		routers.put(203,r_203);

		IRouter r_205 = rv.createRouter("r_205");
		routers.put(205,r_205);

		IRouter r_215 = rv.createRouter("r_215");
		routers.put(215,r_215);

		IRouter r_228 = rv.createRouter("r_228");
		routers.put(228,r_228);

		IRouter r_259 = rv.createRouter("r_259");
		routers.put(259,r_259);

		IRouter r_260 = rv.createRouter("r_260");
		routers.put(260,r_260);

		IRouter r_261 = rv.createRouter("r_261");
		routers.put(261,r_261);

		IRouter r_270 = rv.createRouter("r_270");
		routers.put(270,r_270);

		IRouter r_298 = rv.createRouter("r_298");
		routers.put(298,r_298);

		IRouter r_299 = rv.createRouter("r_299");
		routers.put(299,r_299);

		IRouter r_300 = rv.createRouter("r_300");
		routers.put(300,r_300);

		IRouter r_309 = rv.createRouter("r_309");
		routers.put(309,r_309);

		IRouter r_310 = rv.createRouter("r_310");
		routers.put(310,r_310);

		IRouter r_313 = rv.createRouter("r_313");
		routers.put(313,r_313);

		IRouter r_318 = rv.createRouter("r_318");
		routers.put(318,r_318);

		IRouter r_421 = rv.createRouter("r_421");
		routers.put(421,r_421);

		IRouter r_422 = rv.createRouter("r_422");
		routers.put(422,r_422);

		IRouter r_423 = rv.createRouter("r_423");
		routers.put(423,r_423);

		IRouter r_431 = rv.createRouter("r_431");
		routers.put(431,r_431);

		IRouter r_521 = rv.createRouter("r_521");
		routers.put(521,r_521);

		IRouter r_522 = rv.createRouter("r_522");
		routers.put(522,r_522);

		IRouter r_583 = rv.createRouter("r_583");
		routers.put(583,r_583);

		IRouter r_610 = rv.createRouter("r_610");
		routers.put(610,r_610);

		IRouter r_611 = rv.createRouter("r_611");
		routers.put(611,r_611);

		IRouter r_622 = rv.createRouter("r_622");
		routers.put(622,r_622);

		IRouter r_624 = rv.createRouter("r_624");
		routers.put(624,r_624);

		IRouter r_625 = rv.createRouter("r_625");
		routers.put(625,r_625);

		IRouter r_627 = rv.createRouter("r_627");
		routers.put(627,r_627);

		IRouter r_628 = rv.createRouter("r_628");
		routers.put(628,r_628);

		//create links
		ILink l_64_63 = rv.createLink("l_64_63");
		l_64_63.createInterface(routers.get(64).createInterface("if_63"));
		l_64_63.createInterface(routers.get(63).createInterface("if_64"));

		ILink l_144_64 = rv.createLink("l_144_64");
		l_144_64.createInterface(routers.get(144).createInterface("if_64"));
		l_144_64.createInterface(routers.get(64).createInterface("if_144"));

		ILink l_198_64 = rv.createLink("l_198_64");
		l_198_64.createInterface(routers.get(198).createInterface("if_64"));
		l_198_64.createInterface(routers.get(64).createInterface("if_198"));

		ILink l_205_64 = rv.createLink("l_205_64");
		l_205_64.createInterface(routers.get(205).createInterface("if_64"));
		l_205_64.createInterface(routers.get(64).createInterface("if_205"));

		ILink l_228_64 = rv.createLink("l_228_64");
		l_228_64.createInterface(routers.get(228).createInterface("if_64"));
		l_228_64.createInterface(routers.get(64).createInterface("if_228"));

		ILink l_259_64 = rv.createLink("l_259_64");
		l_259_64.createInterface(routers.get(259).createInterface("if_64"));
		l_259_64.createInterface(routers.get(64).createInterface("if_259"));

		ILink l_260_64 = rv.createLink("l_260_64");
		l_260_64.createInterface(routers.get(260).createInterface("if_64"));
		l_260_64.createInterface(routers.get(64).createInterface("if_260"));

		ILink l_298_64 = rv.createLink("l_298_64");
		l_298_64.createInterface(routers.get(298).createInterface("if_64"));
		l_298_64.createInterface(routers.get(64).createInterface("if_298"));

		ILink l_299_64 = rv.createLink("l_299_64");
		l_299_64.createInterface(routers.get(299).createInterface("if_64"));
		l_299_64.createInterface(routers.get(64).createInterface("if_299"));

		ILink l_300_64 = rv.createLink("l_300_64");
		l_300_64.createInterface(routers.get(300).createInterface("if_64"));
		l_300_64.createInterface(routers.get(64).createInterface("if_300"));

		ILink l_309_64 = rv.createLink("l_309_64");
		l_309_64.createInterface(routers.get(309).createInterface("if_64"));
		l_309_64.createInterface(routers.get(64).createInterface("if_309"));

		ILink l_313_64 = rv.createLink("l_313_64");
		l_313_64.createInterface(routers.get(313).createInterface("if_64"));
		l_313_64.createInterface(routers.get(64).createInterface("if_313"));

		ILink l_422_64 = rv.createLink("l_422_64");
		l_422_64.createInterface(routers.get(422).createInterface("if_64"));
		l_422_64.createInterface(routers.get(64).createInterface("if_422"));

		ILink l_610_64 = rv.createLink("l_610_64");
		l_610_64.createInterface(routers.get(610).createInterface("if_64"));
		l_610_64.createInterface(routers.get(64).createInterface("if_610"));

		ILink l_611_64 = rv.createLink("l_611_64");
		l_611_64.createInterface(routers.get(611).createInterface("if_64"));
		l_611_64.createInterface(routers.get(64).createInterface("if_611"));

		ILink l_627_64 = rv.createLink("l_627_64");
		l_627_64.createInterface(routers.get(627).createInterface("if_64"));
		l_627_64.createInterface(routers.get(64).createInterface("if_627"));

		ILink l_628_64 = rv.createLink("l_628_64");
		l_628_64.createInterface(routers.get(628).createInterface("if_64"));
		l_628_64.createInterface(routers.get(64).createInterface("if_628"));

		ILink l_122_121 = rv.createLink("l_122_121");
		l_122_121.createInterface(routers.get(122).createInterface("if_121"));
		l_122_121.createInterface(routers.get(121).createInterface("if_122"));

		ILink l_203_122 = rv.createLink("l_203_122");
		l_203_122.createInterface(routers.get(203).createInterface("if_122"));
		l_203_122.createInterface(routers.get(122).createInterface("if_203"));

		ILink l_261_122 = rv.createLink("l_261_122");
		l_261_122.createInterface(routers.get(261).createInterface("if_122"));
		l_261_122.createInterface(routers.get(122).createInterface("if_261"));

		ILink l_270_122 = rv.createLink("l_270_122");
		l_270_122.createInterface(routers.get(270).createInterface("if_122"));
		l_270_122.createInterface(routers.get(122).createInterface("if_270"));

		ILink l_318_122 = rv.createLink("l_318_122");
		l_318_122.createInterface(routers.get(318).createInterface("if_122"));
		l_318_122.createInterface(routers.get(122).createInterface("if_318"));

		ILink l_421_122 = rv.createLink("l_421_122");
		l_421_122.createInterface(routers.get(421).createInterface("if_122"));
		l_421_122.createInterface(routers.get(122).createInterface("if_421"));

		ILink l_422_122 = rv.createLink("l_422_122");
		l_422_122.createInterface(routers.get(422).createInterface("if_122"));
		l_422_122.createInterface(routers.get(122).createInterface("if_422"));

		ILink l_431_122 = rv.createLink("l_431_122");
		l_431_122.createInterface(routers.get(431).createInterface("if_122"));
		l_431_122.createInterface(routers.get(122).createInterface("if_431"));

		ILink l_583_122 = rv.createLink("l_583_122");
		l_583_122.createInterface(routers.get(583).createInterface("if_122"));
		l_583_122.createInterface(routers.get(122).createInterface("if_583"));

		ILink l_610_122 = rv.createLink("l_610_122");
		l_610_122.createInterface(routers.get(610).createInterface("if_122"));
		l_610_122.createInterface(routers.get(122).createInterface("if_610"));

		ILink l_611_122 = rv.createLink("l_611_122");
		l_611_122.createInterface(routers.get(611).createInterface("if_122"));
		l_611_122.createInterface(routers.get(122).createInterface("if_611"));

		ILink l_624_122 = rv.createLink("l_624_122");
		l_624_122.createInterface(routers.get(624).createInterface("if_122"));
		l_624_122.createInterface(routers.get(122).createInterface("if_624"));

		ILink l_145_144 = rv.createLink("l_145_144");
		l_145_144.createInterface(routers.get(145).createInterface("if_144"));
		l_145_144.createInterface(routers.get(144).createInterface("if_145"));

		ILink l_205_145 = rv.createLink("l_205_145");
		l_205_145.createInterface(routers.get(205).createInterface("if_145"));
		l_205_145.createInterface(routers.get(145).createInterface("if_205"));

		ILink l_260_145 = rv.createLink("l_260_145");
		l_260_145.createInterface(routers.get(260).createInterface("if_145"));
		l_260_145.createInterface(routers.get(145).createInterface("if_260"));

		ILink l_299_145 = rv.createLink("l_299_145");
		l_299_145.createInterface(routers.get(299).createInterface("if_145"));
		l_299_145.createInterface(routers.get(145).createInterface("if_299"));

		ILink l_421_145 = rv.createLink("l_421_145");
		l_421_145.createInterface(routers.get(421).createInterface("if_145"));
		l_421_145.createInterface(routers.get(145).createInterface("if_421"));

		ILink l_431_145 = rv.createLink("l_431_145");
		l_431_145.createInterface(routers.get(431).createInterface("if_145"));
		l_431_145.createInterface(routers.get(145).createInterface("if_431"));

		ILink l_627_145 = rv.createLink("l_627_145");
		l_627_145.createInterface(routers.get(627).createInterface("if_145"));
		l_627_145.createInterface(routers.get(145).createInterface("if_627"));

		ILink l_628_145 = rv.createLink("l_628_145");
		l_628_145.createInterface(routers.get(628).createInterface("if_145"));
		l_628_145.createInterface(routers.get(145).createInterface("if_628"));

		ILink l_200_199 = rv.createLink("l_200_199");
		l_200_199.createInterface(routers.get(200).createInterface("if_199"));
		l_200_199.createInterface(routers.get(199).createInterface("if_200"));

		ILink l_201_199 = rv.createLink("l_201_199");
		l_201_199.createInterface(routers.get(201).createInterface("if_199"));
		l_201_199.createInterface(routers.get(199).createInterface("if_201"));

		ILink l_215_200 = rv.createLink("l_215_200");
		l_215_200.createInterface(routers.get(215).createInterface("if_200"));
		l_215_200.createInterface(routers.get(200).createInterface("if_215"));

		ILink l_421_200 = rv.createLink("l_421_200");
		l_421_200.createInterface(routers.get(421).createInterface("if_200"));
		l_421_200.createInterface(routers.get(200).createInterface("if_421"));

		ILink l_431_200 = rv.createLink("l_431_200");
		l_431_200.createInterface(routers.get(431).createInterface("if_200"));
		l_431_200.createInterface(routers.get(200).createInterface("if_431"));

		ILink l_422_201 = rv.createLink("l_422_201");
		l_422_201.createInterface(routers.get(422).createInterface("if_201"));
		l_422_201.createInterface(routers.get(201).createInterface("if_422"));

		ILink l_610_201 = rv.createLink("l_610_201");
		l_610_201.createInterface(routers.get(610).createInterface("if_201"));
		l_610_201.createInterface(routers.get(201).createInterface("if_610"));

		ILink l_611_201 = rv.createLink("l_611_201");
		l_611_201.createInterface(routers.get(611).createInterface("if_201"));
		l_611_201.createInterface(routers.get(201).createInterface("if_611"));

		ILink l_310_309 = rv.createLink("l_310_309");
		l_310_309.createInterface(routers.get(310).createInterface("if_309"));
		l_310_309.createInterface(routers.get(309).createInterface("if_310"));

		ILink l_422_421 = rv.createLink("l_422_421");
		l_422_421.createInterface(routers.get(422).createInterface("if_421"));
		l_422_421.createInterface(routers.get(421).createInterface("if_422"));

		ILink l_423_421 = rv.createLink("l_423_421");
		l_423_421.createInterface(routers.get(423).createInterface("if_421"));
		l_423_421.createInterface(routers.get(421).createInterface("if_423"));

		ILink l_431_422 = rv.createLink("l_431_422");
		l_431_422.createInterface(routers.get(431).createInterface("if_422"));
		l_431_422.createInterface(routers.get(422).createInterface("if_431"));

		ILink l_522_422 = rv.createLink("l_522_422");
		l_522_422.createInterface(routers.get(522).createInterface("if_422"));
		l_522_422.createInterface(routers.get(422).createInterface("if_522"));

		ILink l_622_422 = rv.createLink("l_622_422");
		l_622_422.createInterface(routers.get(622).createInterface("if_422"));
		l_622_422.createInterface(routers.get(422).createInterface("if_622"));

		ILink l_431_423 = rv.createLink("l_431_423");
		l_431_423.createInterface(routers.get(431).createInterface("if_423"));
		l_431_423.createInterface(routers.get(423).createInterface("if_431"));

		ILink l_610_431 = rv.createLink("l_610_431");
		l_610_431.createInterface(routers.get(610).createInterface("if_431"));
		l_610_431.createInterface(routers.get(431).createInterface("if_610"));

		ILink l_611_431 = rv.createLink("l_611_431");
		l_611_431.createInterface(routers.get(611).createInterface("if_431"));
		l_611_431.createInterface(routers.get(431).createInterface("if_611"));

		ILink l_625_431 = rv.createLink("l_625_431");
		l_625_431.createInterface(routers.get(625).createInterface("if_431"));
		l_625_431.createInterface(routers.get(431).createInterface("if_625"));

		ILink l_610_521 = rv.createLink("l_610_521");
		l_610_521.createInterface(routers.get(610).createInterface("if_521"));
		l_610_521.createInterface(routers.get(521).createInterface("if_610"));

		ILink l_610_522 = rv.createLink("l_610_522");
		l_610_522.createInterface(routers.get(610).createInterface("if_522"));
		l_610_522.createInterface(routers.get(522).createInterface("if_610"));

		ILink l_611_522 = rv.createLink("l_611_522");
		l_611_522.createInterface(routers.get(611).createInterface("if_522"));
		l_611_522.createInterface(routers.get(522).createInterface("if_611"));

		ILink l_622_610 = rv.createLink("l_622_610");
		l_622_610.createInterface(routers.get(622).createInterface("if_610"));
		l_622_610.createInterface(routers.get(610).createInterface("if_622"));

		//attach campus networks

		INet campus_63 = null;
		IRouter campus_r_63 = null;
		if(null == baseCampus) {
			campus_63 = rv.createNet("campus_14_63");
			campus_r_63 = createCampus(campus_63,do_spherical);
		}else {
			campus_63 = rv.createNetReplica("campus_14_63",baseCampus);
			campus_r_63 = (IRouter)campus_63.getChildByName("sub_campus_router");
		}
		ILink l_campus_63 = rv.createLink("l_campus_63");
		l_campus_63.createInterface(r_63.createInterface("if_campus_63"));
		l_campus_63.createInterface((IInterface)campus_r_63.getChildByName("if_stub"));

		INet campus_121 = null;
		IRouter campus_r_121 = null;
		if(null == baseCampus) {
			campus_121 = rv.createNet("campus_14_121");
			campus_r_121 = createCampus(campus_121,do_spherical);
		}else {
			campus_121 = rv.createNetReplica("campus_14_121",baseCampus);
			campus_r_121 = (IRouter)campus_121.getChildByName("sub_campus_router");
		}
		ILink l_campus_121 = rv.createLink("l_campus_121");
		l_campus_121.createInterface(r_121.createInterface("if_campus_121"));
		l_campus_121.createInterface((IInterface)campus_r_121.getChildByName("if_stub"));

		INet campus_198 = null;
		IRouter campus_r_198 = null;
		if(null == baseCampus) {
			campus_198 = rv.createNet("campus_14_198");
			campus_r_198 = createCampus(campus_198,do_spherical);
		}else {
			campus_198 = rv.createNetReplica("campus_14_198",baseCampus);
			campus_r_198 = (IRouter)campus_198.getChildByName("sub_campus_router");
		}
		ILink l_campus_198 = rv.createLink("l_campus_198");
		l_campus_198.createInterface(r_198.createInterface("if_campus_198"));
		l_campus_198.createInterface((IInterface)campus_r_198.getChildByName("if_stub"));

		INet campus_203 = null;
		IRouter campus_r_203 = null;
		if(null == baseCampus) {
			campus_203 = rv.createNet("campus_14_203");
			campus_r_203 = createCampus(campus_203,do_spherical);
		}else {
			campus_203 = rv.createNetReplica("campus_14_203",baseCampus);
			campus_r_203 = (IRouter)campus_203.getChildByName("sub_campus_router");
		}
		ILink l_campus_203 = rv.createLink("l_campus_203");
		l_campus_203.createInterface(r_203.createInterface("if_campus_203"));
		l_campus_203.createInterface((IInterface)campus_r_203.getChildByName("if_stub"));

		INet campus_215 = null;
		IRouter campus_r_215 = null;
		if(null == baseCampus) {
			campus_215 = rv.createNet("campus_14_215");
			campus_r_215 = createCampus(campus_215,do_spherical);
		}else {
			campus_215 = rv.createNetReplica("campus_14_215",baseCampus);
			campus_r_215 = (IRouter)campus_215.getChildByName("sub_campus_router");
		}
		ILink l_campus_215 = rv.createLink("l_campus_215");
		l_campus_215.createInterface(r_215.createInterface("if_campus_215"));
		l_campus_215.createInterface((IInterface)campus_r_215.getChildByName("if_stub"));

		INet campus_228 = null;
		IRouter campus_r_228 = null;
		if(null == baseCampus) {
			campus_228 = rv.createNet("campus_14_228");
			campus_r_228 = createCampus(campus_228,do_spherical);
		}else {
			campus_228 = rv.createNetReplica("campus_14_228",baseCampus);
			campus_r_228 = (IRouter)campus_228.getChildByName("sub_campus_router");
		}
		ILink l_campus_228 = rv.createLink("l_campus_228");
		l_campus_228.createInterface(r_228.createInterface("if_campus_228"));
		l_campus_228.createInterface((IInterface)campus_r_228.getChildByName("if_stub"));

		INet campus_259 = null;
		IRouter campus_r_259 = null;
		if(null == baseCampus) {
			campus_259 = rv.createNet("campus_14_259");
			campus_r_259 = createCampus(campus_259,do_spherical);
		}else {
			campus_259 = rv.createNetReplica("campus_14_259",baseCampus);
			campus_r_259 = (IRouter)campus_259.getChildByName("sub_campus_router");
		}
		ILink l_campus_259 = rv.createLink("l_campus_259");
		l_campus_259.createInterface(r_259.createInterface("if_campus_259"));
		l_campus_259.createInterface((IInterface)campus_r_259.getChildByName("if_stub"));

		INet campus_261 = null;
		IRouter campus_r_261 = null;
		if(null == baseCampus) {
			campus_261 = rv.createNet("campus_14_261");
			campus_r_261 = createCampus(campus_261,do_spherical);
		}else {
			campus_261 = rv.createNetReplica("campus_14_261",baseCampus);
			campus_r_261 = (IRouter)campus_261.getChildByName("sub_campus_router");
		}
		ILink l_campus_261 = rv.createLink("l_campus_261");
		l_campus_261.createInterface(r_261.createInterface("if_campus_261"));
		l_campus_261.createInterface((IInterface)campus_r_261.getChildByName("if_stub"));

		INet campus_270 = null;
		IRouter campus_r_270 = null;
		if(null == baseCampus) {
			campus_270 = rv.createNet("campus_14_270");
			campus_r_270 = createCampus(campus_270,do_spherical);
		}else {
			campus_270 = rv.createNetReplica("campus_14_270",baseCampus);
			campus_r_270 = (IRouter)campus_270.getChildByName("sub_campus_router");
		}
		ILink l_campus_270 = rv.createLink("l_campus_270");
		l_campus_270.createInterface(r_270.createInterface("if_campus_270"));
		l_campus_270.createInterface((IInterface)campus_r_270.getChildByName("if_stub"));

		INet campus_298 = null;
		IRouter campus_r_298 = null;
		if(null == baseCampus) {
			campus_298 = rv.createNet("campus_14_298");
			campus_r_298 = createCampus(campus_298,do_spherical);
		}else {
			campus_298 = rv.createNetReplica("campus_14_298",baseCampus);
			campus_r_298 = (IRouter)campus_298.getChildByName("sub_campus_router");
		}
		ILink l_campus_298 = rv.createLink("l_campus_298");
		l_campus_298.createInterface(r_298.createInterface("if_campus_298"));
		l_campus_298.createInterface((IInterface)campus_r_298.getChildByName("if_stub"));

		INet campus_300 = null;
		IRouter campus_r_300 = null;
		if(null == baseCampus) {
			campus_300 = rv.createNet("campus_14_300");
			campus_r_300 = createCampus(campus_300,do_spherical);
		}else {
			campus_300 = rv.createNetReplica("campus_14_300",baseCampus);
			campus_r_300 = (IRouter)campus_300.getChildByName("sub_campus_router");
		}
		ILink l_campus_300 = rv.createLink("l_campus_300");
		l_campus_300.createInterface(r_300.createInterface("if_campus_300"));
		l_campus_300.createInterface((IInterface)campus_r_300.getChildByName("if_stub"));

		INet campus_310 = null;
		IRouter campus_r_310 = null;
		if(null == baseCampus) {
			campus_310 = rv.createNet("campus_14_310");
			campus_r_310 = createCampus(campus_310,do_spherical);
		}else {
			campus_310 = rv.createNetReplica("campus_14_310",baseCampus);
			campus_r_310 = (IRouter)campus_310.getChildByName("sub_campus_router");
		}
		ILink l_campus_310 = rv.createLink("l_campus_310");
		l_campus_310.createInterface(r_310.createInterface("if_campus_310"));
		l_campus_310.createInterface((IInterface)campus_r_310.getChildByName("if_stub"));

		INet campus_313 = null;
		IRouter campus_r_313 = null;
		if(null == baseCampus) {
			campus_313 = rv.createNet("campus_14_313");
			campus_r_313 = createCampus(campus_313,do_spherical);
		}else {
			campus_313 = rv.createNetReplica("campus_14_313",baseCampus);
			campus_r_313 = (IRouter)campus_313.getChildByName("sub_campus_router");
		}
		ILink l_campus_313 = rv.createLink("l_campus_313");
		l_campus_313.createInterface(r_313.createInterface("if_campus_313"));
		l_campus_313.createInterface((IInterface)campus_r_313.getChildByName("if_stub"));

		INet campus_318 = null;
		IRouter campus_r_318 = null;
		if(null == baseCampus) {
			campus_318 = rv.createNet("campus_14_318");
			campus_r_318 = createCampus(campus_318,do_spherical);
		}else {
			campus_318 = rv.createNetReplica("campus_14_318",baseCampus);
			campus_r_318 = (IRouter)campus_318.getChildByName("sub_campus_router");
		}
		ILink l_campus_318 = rv.createLink("l_campus_318");
		l_campus_318.createInterface(r_318.createInterface("if_campus_318"));
		l_campus_318.createInterface((IInterface)campus_r_318.getChildByName("if_stub"));

		INet campus_583 = null;
		IRouter campus_r_583 = null;
		if(null == baseCampus) {
			campus_583 = rv.createNet("campus_14_583");
			campus_r_583 = createCampus(campus_583,do_spherical);
		}else {
			campus_583 = rv.createNetReplica("campus_14_583",baseCampus);
			campus_r_583 = (IRouter)campus_583.getChildByName("sub_campus_router");
		}
		ILink l_campus_583 = rv.createLink("l_campus_583");
		l_campus_583.createInterface(r_583.createInterface("if_campus_583"));
		l_campus_583.createInterface((IInterface)campus_r_583.getChildByName("if_stub"));

		INet campus_625 = null;
		IRouter campus_r_625 = null;
		if(null == baseCampus) {
			campus_625 = rv.createNet("campus_14_625");
			campus_r_625 = createCampus(campus_625,do_spherical);
		}else {
			campus_625 = rv.createNetReplica("campus_14_625",baseCampus);
			campus_r_625 = (IRouter)campus_625.getChildByName("sub_campus_router");
		}
		ILink l_campus_625 = rv.createLink("l_campus_625");
		l_campus_625.createInterface(r_625.createInterface("if_campus_625"));
		l_campus_625.createInterface((IInterface)campus_r_625.getChildByName("if_stub"));
		return rv;
	}
	public INet create_net_15(INet top, Map<Integer,IRouter> routers) {
		INet rv = top.createNet("net_15");
		if(do_spherical) rv.createShortestPath();;

		//create routers
		IRouter r_13 = rv.createRouter("r_13");
		routers.put(13,r_13);

		IRouter r_84 = rv.createRouter("r_84");
		routers.put(84,r_84);

		IRouter r_100 = rv.createRouter("r_100");
		routers.put(100,r_100);

		IRouter r_142 = rv.createRouter("r_142");
		routers.put(142,r_142);

		IRouter r_143 = rv.createRouter("r_143");
		routers.put(143,r_143);

		IRouter r_179 = rv.createRouter("r_179");
		routers.put(179,r_179);

		IRouter r_243 = rv.createRouter("r_243");
		routers.put(243,r_243);

		IRouter r_245 = rv.createRouter("r_245");
		routers.put(245,r_245);

		IRouter r_246 = rv.createRouter("r_246");
		routers.put(246,r_246);

		IRouter r_272 = rv.createRouter("r_272");
		routers.put(272,r_272);

		IRouter r_282 = rv.createRouter("r_282");
		routers.put(282,r_282);

		IRouter r_290 = rv.createRouter("r_290");
		routers.put(290,r_290);

		IRouter r_291 = rv.createRouter("r_291");
		routers.put(291,r_291);

		IRouter r_292 = rv.createRouter("r_292");
		routers.put(292,r_292);

		IRouter r_296 = rv.createRouter("r_296");
		routers.put(296,r_296);

		IRouter r_332 = rv.createRouter("r_332");
		routers.put(332,r_332);

		IRouter r_334 = rv.createRouter("r_334");
		routers.put(334,r_334);

		IRouter r_335 = rv.createRouter("r_335");
		routers.put(335,r_335);

		IRouter r_359 = rv.createRouter("r_359");
		routers.put(359,r_359);

		IRouter r_370 = rv.createRouter("r_370");
		routers.put(370,r_370);

		IRouter r_371 = rv.createRouter("r_371");
		routers.put(371,r_371);

		IRouter r_389 = rv.createRouter("r_389");
		routers.put(389,r_389);

		IRouter r_390 = rv.createRouter("r_390");
		routers.put(390,r_390);

		IRouter r_401 = rv.createRouter("r_401");
		routers.put(401,r_401);

		IRouter r_405 = rv.createRouter("r_405");
		routers.put(405,r_405);

		IRouter r_407 = rv.createRouter("r_407");
		routers.put(407,r_407);

		IRouter r_426 = rv.createRouter("r_426");
		routers.put(426,r_426);

		IRouter r_429 = rv.createRouter("r_429");
		routers.put(429,r_429);

		IRouter r_430 = rv.createRouter("r_430");
		routers.put(430,r_430);

		IRouter r_432 = rv.createRouter("r_432");
		routers.put(432,r_432);

		IRouter r_436 = rv.createRouter("r_436");
		routers.put(436,r_436);

		IRouter r_470 = rv.createRouter("r_470");
		routers.put(470,r_470);

		IRouter r_584 = rv.createRouter("r_584");
		routers.put(584,r_584);

		IRouter r_612 = rv.createRouter("r_612");
		routers.put(612,r_612);

		IRouter r_613 = rv.createRouter("r_613");
		routers.put(613,r_613);

		IRouter r_618 = rv.createRouter("r_618");
		routers.put(618,r_618);

		IRouter r_619 = rv.createRouter("r_619");
		routers.put(619,r_619);

		IRouter r_620 = rv.createRouter("r_620");
		routers.put(620,r_620);

		IRouter r_621 = rv.createRouter("r_621");
		routers.put(621,r_621);

		IRouter r_636 = rv.createRouter("r_636");
		routers.put(636,r_636);

		//create links
		ILink l_334_84 = rv.createLink("l_334_84");
		l_334_84.createInterface(routers.get(334).createInterface("if_84"));
		l_334_84.createInterface(routers.get(84).createInterface("if_334"));

		ILink l_390_84 = rv.createLink("l_390_84");
		l_390_84.createInterface(routers.get(390).createInterface("if_84"));
		l_390_84.createInterface(routers.get(84).createInterface("if_390"));

		ILink l_143_100 = rv.createLink("l_143_100");
		l_143_100.createInterface(routers.get(143).createInterface("if_100"));
		l_143_100.createInterface(routers.get(100).createInterface("if_143"));

		ILink l_282_100 = rv.createLink("l_282_100");
		l_282_100.createInterface(routers.get(282).createInterface("if_100"));
		l_282_100.createInterface(routers.get(100).createInterface("if_282"));

		ILink l_290_100 = rv.createLink("l_290_100");
		l_290_100.createInterface(routers.get(290).createInterface("if_100"));
		l_290_100.createInterface(routers.get(100).createInterface("if_290"));

		ILink l_291_100 = rv.createLink("l_291_100");
		l_291_100.createInterface(routers.get(291).createInterface("if_100"));
		l_291_100.createInterface(routers.get(100).createInterface("if_291"));

		ILink l_292_100 = rv.createLink("l_292_100");
		l_292_100.createInterface(routers.get(292).createInterface("if_100"));
		l_292_100.createInterface(routers.get(100).createInterface("if_292"));

		ILink l_432_100 = rv.createLink("l_432_100");
		l_432_100.createInterface(routers.get(432).createInterface("if_100"));
		l_432_100.createInterface(routers.get(100).createInterface("if_432"));

		ILink l_618_100 = rv.createLink("l_618_100");
		l_618_100.createInterface(routers.get(618).createInterface("if_100"));
		l_618_100.createInterface(routers.get(100).createInterface("if_618"));

		ILink l_619_100 = rv.createLink("l_619_100");
		l_619_100.createInterface(routers.get(619).createInterface("if_100"));
		l_619_100.createInterface(routers.get(100).createInterface("if_619"));

		ILink l_620_100 = rv.createLink("l_620_100");
		l_620_100.createInterface(routers.get(620).createInterface("if_100"));
		l_620_100.createInterface(routers.get(100).createInterface("if_620"));

		ILink l_621_100 = rv.createLink("l_621_100");
		l_621_100.createInterface(routers.get(621).createInterface("if_100"));
		l_621_100.createInterface(routers.get(100).createInterface("if_621"));

		ILink l_143_142 = rv.createLink("l_143_142");
		l_143_142.createInterface(routers.get(143).createInterface("if_142"));
		l_143_142.createInterface(routers.get(142).createInterface("if_143"));

		ILink l_282_143 = rv.createLink("l_282_143");
		l_282_143.createInterface(routers.get(282).createInterface("if_143"));
		l_282_143.createInterface(routers.get(143).createInterface("if_282"));

		ILink l_290_143 = rv.createLink("l_290_143");
		l_290_143.createInterface(routers.get(290).createInterface("if_143"));
		l_290_143.createInterface(routers.get(143).createInterface("if_290"));

		ILink l_291_143 = rv.createLink("l_291_143");
		l_291_143.createInterface(routers.get(291).createInterface("if_143"));
		l_291_143.createInterface(routers.get(143).createInterface("if_291"));

		ILink l_292_143 = rv.createLink("l_292_143");
		l_292_143.createInterface(routers.get(292).createInterface("if_143"));
		l_292_143.createInterface(routers.get(143).createInterface("if_292"));

		ILink l_401_143 = rv.createLink("l_401_143");
		l_401_143.createInterface(routers.get(401).createInterface("if_143"));
		l_401_143.createInterface(routers.get(143).createInterface("if_401"));

		ILink l_282_179 = rv.createLink("l_282_179");
		l_282_179.createInterface(routers.get(282).createInterface("if_179"));
		l_282_179.createInterface(routers.get(179).createInterface("if_282"));

		ILink l_290_179 = rv.createLink("l_290_179");
		l_290_179.createInterface(routers.get(290).createInterface("if_179"));
		l_290_179.createInterface(routers.get(179).createInterface("if_290"));

		ILink l_291_179 = rv.createLink("l_291_179");
		l_291_179.createInterface(routers.get(291).createInterface("if_179"));
		l_291_179.createInterface(routers.get(179).createInterface("if_291"));

		ILink l_292_179 = rv.createLink("l_292_179");
		l_292_179.createInterface(routers.get(292).createInterface("if_179"));
		l_292_179.createInterface(routers.get(179).createInterface("if_292"));

		ILink l_296_179 = rv.createLink("l_296_179");
		l_296_179.createInterface(routers.get(296).createInterface("if_179"));
		l_296_179.createInterface(routers.get(179).createInterface("if_296"));

		ILink l_245_243 = rv.createLink("l_245_243");
		l_245_243.createInterface(routers.get(245).createInterface("if_243"));
		l_245_243.createInterface(routers.get(243).createInterface("if_245"));

		ILink l_246_243 = rv.createLink("l_246_243");
		l_246_243.createInterface(routers.get(246).createInterface("if_243"));
		l_246_243.createInterface(routers.get(243).createInterface("if_246"));

		ILink l_246_245 = rv.createLink("l_246_245");
		l_246_245.createInterface(routers.get(246).createInterface("if_245"));
		l_246_245.createInterface(routers.get(245).createInterface("if_246"));

		ILink l_282_245 = rv.createLink("l_282_245");
		l_282_245.createInterface(routers.get(282).createInterface("if_245"));
		l_282_245.createInterface(routers.get(245).createInterface("if_282"));

		ILink l_290_245 = rv.createLink("l_290_245");
		l_290_245.createInterface(routers.get(290).createInterface("if_245"));
		l_290_245.createInterface(routers.get(245).createInterface("if_290"));

		ILink l_291_245 = rv.createLink("l_291_245");
		l_291_245.createInterface(routers.get(291).createInterface("if_245"));
		l_291_245.createInterface(routers.get(245).createInterface("if_291"));

		ILink l_292_245 = rv.createLink("l_292_245");
		l_292_245.createInterface(routers.get(292).createInterface("if_245"));
		l_292_245.createInterface(routers.get(245).createInterface("if_292"));

		ILink l_405_245 = rv.createLink("l_405_245");
		l_405_245.createInterface(routers.get(405).createInterface("if_245"));
		l_405_245.createInterface(routers.get(245).createInterface("if_405"));

		ILink l_407_245 = rv.createLink("l_407_245");
		l_407_245.createInterface(routers.get(407).createInterface("if_245"));
		l_407_245.createInterface(routers.get(245).createInterface("if_407"));

		ILink l_436_245 = rv.createLink("l_436_245");
		l_436_245.createInterface(routers.get(436).createInterface("if_245"));
		l_436_245.createInterface(routers.get(245).createInterface("if_436"));

		ILink l_470_245 = rv.createLink("l_470_245");
		l_470_245.createInterface(routers.get(470).createInterface("if_245"));
		l_470_245.createInterface(routers.get(245).createInterface("if_470"));

		ILink l_584_245 = rv.createLink("l_584_245");
		l_584_245.createInterface(routers.get(584).createInterface("if_245"));
		l_584_245.createInterface(routers.get(245).createInterface("if_584"));

		ILink l_282_272 = rv.createLink("l_282_272");
		l_282_272.createInterface(routers.get(282).createInterface("if_272"));
		l_282_272.createInterface(routers.get(272).createInterface("if_282"));

		ILink l_290_272 = rv.createLink("l_290_272");
		l_290_272.createInterface(routers.get(290).createInterface("if_272"));
		l_290_272.createInterface(routers.get(272).createInterface("if_290"));

		ILink l_291_272 = rv.createLink("l_291_272");
		l_291_272.createInterface(routers.get(291).createInterface("if_272"));
		l_291_272.createInterface(routers.get(272).createInterface("if_291"));

		ILink l_292_272 = rv.createLink("l_292_272");
		l_292_272.createInterface(routers.get(292).createInterface("if_272"));
		l_292_272.createInterface(routers.get(272).createInterface("if_292"));

		ILink l_334_282 = rv.createLink("l_334_282");
		l_334_282.createInterface(routers.get(334).createInterface("if_282"));
		l_334_282.createInterface(routers.get(282).createInterface("if_334"));

		ILink l_335_282 = rv.createLink("l_335_282");
		l_335_282.createInterface(routers.get(335).createInterface("if_282"));
		l_335_282.createInterface(routers.get(282).createInterface("if_335"));

		ILink l_359_282 = rv.createLink("l_359_282");
		l_359_282.createInterface(routers.get(359).createInterface("if_282"));
		l_359_282.createInterface(routers.get(282).createInterface("if_359"));

		ILink l_429_282 = rv.createLink("l_429_282");
		l_429_282.createInterface(routers.get(429).createInterface("if_282"));
		l_429_282.createInterface(routers.get(282).createInterface("if_429"));

		ILink l_430_282 = rv.createLink("l_430_282");
		l_430_282.createInterface(routers.get(430).createInterface("if_282"));
		l_430_282.createInterface(routers.get(282).createInterface("if_430"));

		ILink l_432_282 = rv.createLink("l_432_282");
		l_432_282.createInterface(routers.get(432).createInterface("if_282"));
		l_432_282.createInterface(routers.get(282).createInterface("if_432"));

		ILink l_334_290 = rv.createLink("l_334_290");
		l_334_290.createInterface(routers.get(334).createInterface("if_290"));
		l_334_290.createInterface(routers.get(290).createInterface("if_334"));

		ILink l_335_290 = rv.createLink("l_335_290");
		l_335_290.createInterface(routers.get(335).createInterface("if_290"));
		l_335_290.createInterface(routers.get(290).createInterface("if_335"));

		ILink l_359_290 = rv.createLink("l_359_290");
		l_359_290.createInterface(routers.get(359).createInterface("if_290"));
		l_359_290.createInterface(routers.get(290).createInterface("if_359"));

		ILink l_429_290 = rv.createLink("l_429_290");
		l_429_290.createInterface(routers.get(429).createInterface("if_290"));
		l_429_290.createInterface(routers.get(290).createInterface("if_429"));

		ILink l_430_290 = rv.createLink("l_430_290");
		l_430_290.createInterface(routers.get(430).createInterface("if_290"));
		l_430_290.createInterface(routers.get(290).createInterface("if_430"));

		ILink l_334_291 = rv.createLink("l_334_291");
		l_334_291.createInterface(routers.get(334).createInterface("if_291"));
		l_334_291.createInterface(routers.get(291).createInterface("if_334"));

		ILink l_335_291 = rv.createLink("l_335_291");
		l_335_291.createInterface(routers.get(335).createInterface("if_291"));
		l_335_291.createInterface(routers.get(291).createInterface("if_335"));

		ILink l_359_291 = rv.createLink("l_359_291");
		l_359_291.createInterface(routers.get(359).createInterface("if_291"));
		l_359_291.createInterface(routers.get(291).createInterface("if_359"));

		ILink l_429_291 = rv.createLink("l_429_291");
		l_429_291.createInterface(routers.get(429).createInterface("if_291"));
		l_429_291.createInterface(routers.get(291).createInterface("if_429"));

		ILink l_430_291 = rv.createLink("l_430_291");
		l_430_291.createInterface(routers.get(430).createInterface("if_291"));
		l_430_291.createInterface(routers.get(291).createInterface("if_430"));

		ILink l_334_292 = rv.createLink("l_334_292");
		l_334_292.createInterface(routers.get(334).createInterface("if_292"));
		l_334_292.createInterface(routers.get(292).createInterface("if_334"));

		ILink l_335_292 = rv.createLink("l_335_292");
		l_335_292.createInterface(routers.get(335).createInterface("if_292"));
		l_335_292.createInterface(routers.get(292).createInterface("if_335"));

		ILink l_359_292 = rv.createLink("l_359_292");
		l_359_292.createInterface(routers.get(359).createInterface("if_292"));
		l_359_292.createInterface(routers.get(292).createInterface("if_359"));

		ILink l_429_292 = rv.createLink("l_429_292");
		l_429_292.createInterface(routers.get(429).createInterface("if_292"));
		l_429_292.createInterface(routers.get(292).createInterface("if_429"));

		ILink l_430_292 = rv.createLink("l_430_292");
		l_430_292.createInterface(routers.get(430).createInterface("if_292"));
		l_430_292.createInterface(routers.get(292).createInterface("if_430"));

		ILink l_432_292 = rv.createLink("l_432_292");
		l_432_292.createInterface(routers.get(432).createInterface("if_292"));
		l_432_292.createInterface(routers.get(292).createInterface("if_432"));

		ILink l_334_332 = rv.createLink("l_334_332");
		l_334_332.createInterface(routers.get(334).createInterface("if_332"));
		l_334_332.createInterface(routers.get(332).createInterface("if_334"));

		ILink l_389_334 = rv.createLink("l_389_334");
		l_389_334.createInterface(routers.get(389).createInterface("if_334"));
		l_389_334.createInterface(routers.get(334).createInterface("if_389"));

		ILink l_426_334 = rv.createLink("l_426_334");
		l_426_334.createInterface(routers.get(426).createInterface("if_334"));
		l_426_334.createInterface(routers.get(334).createInterface("if_426"));

		ILink l_612_334 = rv.createLink("l_612_334");
		l_612_334.createInterface(routers.get(612).createInterface("if_334"));
		l_612_334.createInterface(routers.get(334).createInterface("if_612"));

		ILink l_613_334 = rv.createLink("l_613_334");
		l_613_334.createInterface(routers.get(613).createInterface("if_334"));
		l_613_334.createInterface(routers.get(334).createInterface("if_613"));

		ILink l_389_335 = rv.createLink("l_389_335");
		l_389_335.createInterface(routers.get(389).createInterface("if_335"));
		l_389_335.createInterface(routers.get(335).createInterface("if_389"));

		ILink l_612_335 = rv.createLink("l_612_335");
		l_612_335.createInterface(routers.get(612).createInterface("if_335"));
		l_612_335.createInterface(routers.get(335).createInterface("if_612"));

		ILink l_613_335 = rv.createLink("l_613_335");
		l_613_335.createInterface(routers.get(613).createInterface("if_335"));
		l_613_335.createInterface(routers.get(335).createInterface("if_613"));

		ILink l_370_359 = rv.createLink("l_370_359");
		l_370_359.createInterface(routers.get(370).createInterface("if_359"));
		l_370_359.createInterface(routers.get(359).createInterface("if_370"));

		ILink l_371_359 = rv.createLink("l_371_359");
		l_371_359.createInterface(routers.get(371).createInterface("if_359"));
		l_371_359.createInterface(routers.get(359).createInterface("if_371"));

		ILink l_390_389 = rv.createLink("l_390_389");
		l_390_389.createInterface(routers.get(390).createInterface("if_389"));
		l_390_389.createInterface(routers.get(389).createInterface("if_390"));

		ILink l_636_621 = rv.createLink("l_636_621");
		l_636_621.createInterface(routers.get(636).createInterface("if_621"));
		l_636_621.createInterface(routers.get(621).createInterface("if_636"));

		//attach campus networks

		INet campus_142 = null;
		IRouter campus_r_142 = null;
		if(null == baseCampus) {
			campus_142 = rv.createNet("campus_15_142");
			campus_r_142 = createCampus(campus_142,do_spherical);
		}else {
			campus_142 = rv.createNetReplica("campus_15_142",baseCampus);
			campus_r_142 = (IRouter)campus_142.getChildByName("sub_campus_router");
		}
		ILink l_campus_142 = rv.createLink("l_campus_142");
		l_campus_142.createInterface(r_142.createInterface("if_campus_142"));
		l_campus_142.createInterface((IInterface)campus_r_142.getChildByName("if_stub"));

		INet campus_370 = null;
		IRouter campus_r_370 = null;
		if(null == baseCampus) {
			campus_370 = rv.createNet("campus_15_370");
			campus_r_370 = createCampus(campus_370,do_spherical);
		}else {
			campus_370 = rv.createNetReplica("campus_15_370",baseCampus);
			campus_r_370 = (IRouter)campus_370.getChildByName("sub_campus_router");
		}
		ILink l_campus_370 = rv.createLink("l_campus_370");
		l_campus_370.createInterface(r_370.createInterface("if_campus_370"));
		l_campus_370.createInterface((IInterface)campus_r_370.getChildByName("if_stub"));

		INet campus_371 = null;
		IRouter campus_r_371 = null;
		if(null == baseCampus) {
			campus_371 = rv.createNet("campus_15_371");
			campus_r_371 = createCampus(campus_371,do_spherical);
		}else {
			campus_371 = rv.createNetReplica("campus_15_371",baseCampus);
			campus_r_371 = (IRouter)campus_371.getChildByName("sub_campus_router");
		}
		ILink l_campus_371 = rv.createLink("l_campus_371");
		l_campus_371.createInterface(r_371.createInterface("if_campus_371"));
		l_campus_371.createInterface((IInterface)campus_r_371.getChildByName("if_stub"));

		INet campus_401 = null;
		IRouter campus_r_401 = null;
		if(null == baseCampus) {
			campus_401 = rv.createNet("campus_15_401");
			campus_r_401 = createCampus(campus_401,do_spherical);
		}else {
			campus_401 = rv.createNetReplica("campus_15_401",baseCampus);
			campus_r_401 = (IRouter)campus_401.getChildByName("sub_campus_router");
		}
		ILink l_campus_401 = rv.createLink("l_campus_401");
		l_campus_401.createInterface(r_401.createInterface("if_campus_401"));
		l_campus_401.createInterface((IInterface)campus_r_401.getChildByName("if_stub"));

		INet campus_405 = null;
		IRouter campus_r_405 = null;
		if(null == baseCampus) {
			campus_405 = rv.createNet("campus_15_405");
			campus_r_405 = createCampus(campus_405,do_spherical);
		}else {
			campus_405 = rv.createNetReplica("campus_15_405",baseCampus);
			campus_r_405 = (IRouter)campus_405.getChildByName("sub_campus_router");
		}
		ILink l_campus_405 = rv.createLink("l_campus_405");
		l_campus_405.createInterface(r_405.createInterface("if_campus_405"));
		l_campus_405.createInterface((IInterface)campus_r_405.getChildByName("if_stub"));

		INet campus_407 = null;
		IRouter campus_r_407 = null;
		if(null == baseCampus) {
			campus_407 = rv.createNet("campus_15_407");
			campus_r_407 = createCampus(campus_407,do_spherical);
		}else {
			campus_407 = rv.createNetReplica("campus_15_407",baseCampus);
			campus_r_407 = (IRouter)campus_407.getChildByName("sub_campus_router");
		}
		ILink l_campus_407 = rv.createLink("l_campus_407");
		l_campus_407.createInterface(r_407.createInterface("if_campus_407"));
		l_campus_407.createInterface((IInterface)campus_r_407.getChildByName("if_stub"));

		INet campus_426 = null;
		IRouter campus_r_426 = null;
		if(null == baseCampus) {
			campus_426 = rv.createNet("campus_15_426");
			campus_r_426 = createCampus(campus_426,do_spherical);
		}else {
			campus_426 = rv.createNetReplica("campus_15_426",baseCampus);
			campus_r_426 = (IRouter)campus_426.getChildByName("sub_campus_router");
		}
		ILink l_campus_426 = rv.createLink("l_campus_426");
		l_campus_426.createInterface(r_426.createInterface("if_campus_426"));
		l_campus_426.createInterface((IInterface)campus_r_426.getChildByName("if_stub"));

		INet campus_436 = null;
		IRouter campus_r_436 = null;
		if(null == baseCampus) {
			campus_436 = rv.createNet("campus_15_436");
			campus_r_436 = createCampus(campus_436,do_spherical);
		}else {
			campus_436 = rv.createNetReplica("campus_15_436",baseCampus);
			campus_r_436 = (IRouter)campus_436.getChildByName("sub_campus_router");
		}
		ILink l_campus_436 = rv.createLink("l_campus_436");
		l_campus_436.createInterface(r_436.createInterface("if_campus_436"));
		l_campus_436.createInterface((IInterface)campus_r_436.getChildByName("if_stub"));

		INet campus_470 = null;
		IRouter campus_r_470 = null;
		if(null == baseCampus) {
			campus_470 = rv.createNet("campus_15_470");
			campus_r_470 = createCampus(campus_470,do_spherical);
		}else {
			campus_470 = rv.createNetReplica("campus_15_470",baseCampus);
			campus_r_470 = (IRouter)campus_470.getChildByName("sub_campus_router");
		}
		ILink l_campus_470 = rv.createLink("l_campus_470");
		l_campus_470.createInterface(r_470.createInterface("if_campus_470"));
		l_campus_470.createInterface((IInterface)campus_r_470.getChildByName("if_stub"));

		INet campus_584 = null;
		IRouter campus_r_584 = null;
		if(null == baseCampus) {
			campus_584 = rv.createNet("campus_15_584");
			campus_r_584 = createCampus(campus_584,do_spherical);
		}else {
			campus_584 = rv.createNetReplica("campus_15_584",baseCampus);
			campus_r_584 = (IRouter)campus_584.getChildByName("sub_campus_router");
		}
		ILink l_campus_584 = rv.createLink("l_campus_584");
		l_campus_584.createInterface(r_584.createInterface("if_campus_584"));
		l_campus_584.createInterface((IInterface)campus_r_584.getChildByName("if_stub"));

		INet campus_618 = null;
		IRouter campus_r_618 = null;
		if(null == baseCampus) {
			campus_618 = rv.createNet("campus_15_618");
			campus_r_618 = createCampus(campus_618,do_spherical);
		}else {
			campus_618 = rv.createNetReplica("campus_15_618",baseCampus);
			campus_r_618 = (IRouter)campus_618.getChildByName("sub_campus_router");
		}
		ILink l_campus_618 = rv.createLink("l_campus_618");
		l_campus_618.createInterface(r_618.createInterface("if_campus_618"));
		l_campus_618.createInterface((IInterface)campus_r_618.getChildByName("if_stub"));

		INet campus_619 = null;
		IRouter campus_r_619 = null;
		if(null == baseCampus) {
			campus_619 = rv.createNet("campus_15_619");
			campus_r_619 = createCampus(campus_619,do_spherical);
		}else {
			campus_619 = rv.createNetReplica("campus_15_619",baseCampus);
			campus_r_619 = (IRouter)campus_619.getChildByName("sub_campus_router");
		}
		ILink l_campus_619 = rv.createLink("l_campus_619");
		l_campus_619.createInterface(r_619.createInterface("if_campus_619"));
		l_campus_619.createInterface((IInterface)campus_r_619.getChildByName("if_stub"));

		INet campus_620 = null;
		IRouter campus_r_620 = null;
		if(null == baseCampus) {
			campus_620 = rv.createNet("campus_15_620");
			campus_r_620 = createCampus(campus_620,do_spherical);
		}else {
			campus_620 = rv.createNetReplica("campus_15_620",baseCampus);
			campus_r_620 = (IRouter)campus_620.getChildByName("sub_campus_router");
		}
		ILink l_campus_620 = rv.createLink("l_campus_620");
		l_campus_620.createInterface(r_620.createInterface("if_campus_620"));
		l_campus_620.createInterface((IInterface)campus_r_620.getChildByName("if_stub"));

		INet campus_636 = null;
		IRouter campus_r_636 = null;
		if(null == baseCampus) {
			campus_636 = rv.createNet("campus_15_636");
			campus_r_636 = createCampus(campus_636,do_spherical);
		}else {
			campus_636 = rv.createNetReplica("campus_15_636",baseCampus);
			campus_r_636 = (IRouter)campus_636.getChildByName("sub_campus_router");
		}
		ILink l_campus_636 = rv.createLink("l_campus_636");
		l_campus_636.createInterface(r_636.createInterface("if_campus_636"));
		l_campus_636.createInterface((IInterface)campus_r_636.getChildByName("if_stub"));
		return rv;
	}
	
	public IRouter createCampus(INet top, boolean haveTopLevelRoutingProtocol) {
		IRouter rv = top.createRouter("sub_campus_router");
		top.createShortestPath();
		rv.createInterface("if_stub");
		INet base = top.createNet("sub_campus_0");
		IRouter r = super.createCampus(base, haveTopLevelRoutingProtocol, do_spherical?RoutingType.SHORTEST_PATH_L123:RoutingType.SHORTEST_PATH_L1);
		ILink l = top.createLink("link_0");
		l.createInterface(rv.createInterface("c0"));
		l.createInterface(r.createInterface("up"));

		
		//XXX
		for(int i=1;i<2;i++) {
			INet c = top.createNetReplica("sub_campus_"+i,base);
			l = top.createLink("link_"+i);
			l.createInterface(rv.createInterface("c"+i));
			l.createInterface((IInterface)c.getChildByName("net01").getChildByName("r4").get("up"));
		}
		return rv;
	}
}
