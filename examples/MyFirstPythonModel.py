topnet = exp.createTopNet("top")
		
left_net = topnet.createNet("left")

h1 = left_net.createHost("h1")
if1 = h1.createInterface("if0")

h2 = left_net.createHost("h2")
if2 = h2.createInterface("if0")

h3 = left_net.createHost("h3")
if3 = h3.createInterface("if0")

h4 = left_net.createHost("h4")
if4 = h4.createInterface("if0")

r = left_net.createRouter("r")

l1 = left_net.createLink()
l1.attachInterface(if1)
l1.attachInterface(r.createInterface("if1"))

l2 = left_net.createLink()
l2.attachInterface(if2)
l2.attachInterface(r.createInterface("if2"))

l3 = left_net.createLink()
l3.attachInterface(if3)
l3.attachInterface(r.createInterface("if3"))

l4 = left_net.createLink()
l4.attachInterface(if4)
l4.attachInterface(r.createInterface("if4"))
		
right_net = left_net.copy("right",topnet)

toplink = topnet.createLink("toplink")
toplink.attachInterface(left_net.get("r").createInterface("if0"))
toplink.attachInterface(right_net.get("r").createInterface("if0"))
