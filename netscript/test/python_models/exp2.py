topnet = exp.createTopNet("topnet")
sub1 = topnet.newNet(name="sub1")
r_s1 = sub1.newRouter(name="r")
nic1=r_s1.newInterface(name="if1", bit_rate="100000000", buffer_size="1500", latency="17", jitter_range="2", mtu="2000")
nic2=r_s1.newInterface(name="if2", bit_rate="100000000", buffer_size="1500", latency="17", jitter_range="2", mtu="2000")
nic3=r_s1.newInterface(name="if3", bit_rate="100000000", buffer_size="1500", latency="17", jitter_range="2", mtu="2000")
h1 = sub1.newHost(name="h1")
h2 = sub1.newHost(name="h2")
if1 = h1.newInterface(name="if0", bit_rate="100000000", buffer_size="1500", latency="17", jitter_range="2", mtu="2000")
if2 = h2.newInterface(name="if0", bit_rate="100000000", buffer_size="1500", latency="17", jitter_range="2", mtu="2000")

l1 = sub1.newLink(name="l1", bandwidth="100000000", delay="10", attachments=[nic1,if1])
l2 = sub1.newLink(name="l2", bandwidth="100000000", delay="10", attachments=[nic2,if2])

sub2 = topnet.newNet(sub1,name="sub2")

traffic=topnet.newTraffic()
ping1=traffic.newPingTraffic()
ping2=traffic.newPingTraffic()

f1=sub1.get("r").get("if3")
f2=sub2.get("r").get("if3")
l3 = topnet.newLink(name="l3", bandwidth="100000000", delay="10", attachments=[f1,f2])


