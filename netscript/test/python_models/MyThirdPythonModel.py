#create the top most network
topnet = exp.createTopNet("top")
topnet.createShortestPath();

left_net = topnet.createNet("left")
left_net.createShortestPath();

h1 = left_net.createHost("h1")
if1 = h1.createInterface("if0")

h2 = left_net.createHost("h2")
if2 = h2.createInterface("if0")

h3 = left_net.createHost("h3")
if3 = h3.createInterface("if0")
#make h3 in both the left and right nets emulated
h3.enableEmulation();

h4 = left_net.createHost("h4")
if4 = h4.createInterface("if0")

r = left_net.createRouter("r")

l1 = left_net.createLink()
l1.createInterface(if1)
l1.createInterface(r.createInterface("if1"))

l2 = left_net.createLink()
l2.createInterface(if2)
l2.createInterface(r.createInterface("if2"))

l3 = left_net.createLink()
l3.createInterface(if3)
l3.createInterface(r.createInterface("if3"))

l4 = left_net.createLink()
l4.createInterface(if4)
l4.createInterface(r.createInterface("if4"))
		
#create the right network
right_net = left_net.copy("right_net",topnet)

#link the left and right networks
toplink = topnet.createLink("toplink")
toplink.createInterface(left_net.get("r").createInterface("if0"))
toplink.createInterface(right_net.get("r").createInterface("if0"))

#add simulated traffic
right_h2 = right_net.get("h1");
trafficFactory = jprime.TrafficFactory(topnet)
trafficFactory.createSimulatedTCP(10, 100000000, h1, right_h2)
trafficFactory.createSimulatedTCP(13, 100000000, h2, right_h2)


#add emulated traffic
right_h3 = right_net.get("h3");
trafficFactory.createEmulatedTCP(5, 100000000, right_h3, h3);

