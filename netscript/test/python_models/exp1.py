top = exp.createTopNet("top")
h1 = top.newHost(name="h1")
h1.newInterface(name="if0")

h2 = top.newRouter(name="h2")
h2.newInterface(name="nic0")
h2.newInterface(name="nic1")

h3 = top.newHost(name="h3")
h3.newInterface(name="if0")

top.newLink(name="l1",attachments=[h1.get("if0"),h2.get("nic0")])
top.newLink(name="l2",attachments=[h2.get("nic1"),h3.get("if0")])

t=top.newTraffic()
ping=t.newPingTraffic()

ping.fromTo(h1, h3)