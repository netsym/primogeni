import pyprime,sys
db=pyprime.Database()
exp = db.createExperiment( name = 'Campus' )
n = exp.Net(name='topNet' )

#net01 includes net0, net1 and two routers
net01 = exp.topNet.Net(overlay=None, name='net01' )
net01.routing.ShortestPath()

#net0 includes three routers
net0 = net01.Net(overlay=None, name='net0' )
net0.routing.ShortestPath()

net0_r0=net0.Router(name="r0")
net0_r0.Interface(name="if0")
net0_r0.Interface(name="if1")
net0_r0.Interface(name="if2")
net0_r0.Interface(name="if3")
net0_r0.Interface(name="if4")
net0_r0.Interface(name="if5")
net0_r0.Interface(name="if6")
net0_r0.Interface(name="if7")

net0_r1=net0.Router(name="r1")
net0_r1.Interface(name="if0")
net0_r1.Interface(name="if1")
net0_r1.Interface(name="if2")

net0_r2=net0.Router(name="r2")
net0_r2.Interface(name="if0")
net0_r2.Interface(name="if1")
net0_r2.Interface(name="if2")

#connect the routers in net0
net0.Link(name="r0_r1",attachments=[net0_r0.if1, net0_r1.if2], delay="1")
net0.Link(name="r1_r2",attachments=[net0_r1.if0, net0_r2.if1], delay="1")
net0.Link(name="r0_r2",attachments=[net0_r0.if0, net0_r2.if2], delay="1")

#net1 includes two routers and 4 hosts
net1 = net01.Net(overlay=None, name='net1' )
net1.routing.ShortestPath()

net1_r0=net1.Router(name="r0")
net1_r0.Interface(name="if0")
net1_r0.Interface(name="if1")
net1_r0.Interface(name="if2")
net1_r0.Interface(name="if3")

net1_r1=net1.Router(name="r1")
net1_r1.Interface(name="if0")
net1_r1.Interface(name="if1")
net1_r1.Interface(name="if2")

net1_h2=net1.Host(name="h2")
net1_h2.Interface(name="if0")

net1_h3=net1.Host(name="h3")
net1_h3.Interface(name="if0")

net1_h4=net1.Host(name="h4")
net1_h4.Interface(name="if0")

net1_h5=net1.Host(name="h5")
net1_h5.Interface(name="if0")

#connect the routers and hosts in net1
net1.Link(name="r0_r1",attachments=[net1_r0.if2, net1_r1.if0], delay="1")
net1.Link(name="r0_h2",attachments=[net1_r0.if0, net1_h2.if0], delay="1")
net1.Link(name="r0_h3",attachments=[net1_r0.if1, net1_h3.if0], delay="1")
net1.Link(name="r1_h4",attachments=[net1_r1.if1, net1_h4.if0], delay="1")
net1.Link(name="r1_h5",attachments=[net1_r1.if2, net1_h5.if0], delay="1")

#routers in net01
net01_r4=net01.Router(name="r4")
net01_r4.Interface(name="if0")
net01_r4.Interface(name="if1")
net01_r4.Interface(name="if2")
net01_r4.Interface(name="if3")

net01_r5=net01.Router(name="r5")
net01_r5.Interface(name="if0")
net01_r5.Interface(name="if1")
net01_r5.Interface(name="if2")
net01_r5.Interface(name="if3")

#link router4&5
net01.Link(name="r4_r5",attachments=[net01_r4.if1, net01_r5.if3], delay="1")

#links from backbone net 0 to routers 4, 5, net1_r0
net01.Link(name="net0_r4",attachments=[net0_r0.if2, net01_r4.if0], delay="1")
net01.Link(name="net0_r5",attachments=[net0_r1.if1, net01_r5.if0], delay="1")
net01.Link(name="net0_net1",attachments=[net0_r2.if0, net1_r0.if3], delay="1")

#net2 includes seven routers and seven lans
net2 = exp.topNet.Net(overlay=None, name='net2' )
net2.routing.ShortestPath()

net2_r0=net2.Router(name="r0")
net2_r0.Interface(name="if0")
net2_r0.Interface(name="if1")
net2_r0.Interface(name="if2")

net2_r1=net2.Router(name="r1")
net2_r1.Interface(name="if0")
net2_r1.Interface(name="if1")
net2_r1.Interface(name="if2")

net2_r2=net2.Router(name="r2")
net2_r2.Interface(name="if0")
net2_r2.Interface(name="if1")
net2_r2.Interface(name="if2")
net2_r2.Interface(name="if3")

net2_r3=net2.Router(name="r3")
net2_r3.Interface(name="if0")
net2_r3.Interface(name="if1")
net2_r3.Interface(name="if2")
net2_r3.Interface(name="if3")

net2_r4=net2.Router(name="r4")
net2_r4.Interface(name="if0")
net2_r4.Interface(name="if1")

net2_r5=net2.Router(name="r5")
net2_r5.Interface(name="if0")
net2_r5.Interface(name="if1")
net2_r5.Interface(name="if2")

net2_r6=net2.Router(name="r6")
net2_r6.Interface(name="if0")
net2_r6.Interface(name="if1")
net2_r6.Interface(name="if2")
net2_r6.Interface(name="if3")

#router to router links in net2
net2.Link(name="r0_r1",attachments=[net2_r0.if1, net2_r1.if2], delay="1")
net2.Link(name="r0_r2",attachments=[net2_r0.if2, net2_r2.if0], delay="1")
net2.Link(name="r2_r3",attachments=[net2_r2.if1, net2_r3.if3], delay="1")
net2.Link(name="r2_r4",attachments=[net2_r2.if2, net2_r4.if0], delay="1")
net2.Link(name="r1_r3",attachments=[net2_r1.if1, net2_r3.if0], delay="1")
net2.Link(name="r3_r5",attachments=[net2_r3.if2, net2_r5.if0], delay="1")
net2.Link(name="r5_r6",attachments=[net2_r5.if2, net2_r6.if0], delay="1")

#the lans in net2
net23 = net2.Net(overlay=None, name='lan0' )
net23.routing.ShortestPath()

net23_r0=net23.Router(name="r0")
net23_r0.Interface(name="if0")
net23_r0.Interface(name="if3")
net23_r0.Interface(name="if4")
net23_r0.Interface(name="if5")
net23_r0.Interface(name="if6")

replica=None
for i in range(1,43):
    if None is replica:
        h=net23.Host(name="h%i"%(i))
        h.Interface(name="if0")
        replica=h
        pass
    else:
        h=net23.Host(overlay=replica, name="h%i"%(i))
        pass
    pass

#links in lan
net23.Link(name="l1",attachments=[net23_r0.if3, 
                                      net23.h1.if0,net23.h2.if0,net23.h3.if0,net23.h4.if0,net23.h5.if0,
                                      net23.h6.if0,net23.h7.if0,net23.h8.if0,net23.h9.if0,net23.h10.if0], 
                                      delay="1")
net23.Link(name="l2",attachments=[net23_r0.if4, 
                                      net23.h11.if0,net23.h12.if0,net23.h13.if0,net23.h14.if0,net23.h15.if0,
                                      net23.h16.if0,net23.h17.if0,net23.h18.if0,net23.h19.if0,net23.h20.if0], 
                                      delay="1")
net23.Link(name="l3",attachments=[net23_r0.if5, 
                                      net23.h21.if0,net23.h22.if0,net23.h23.if0,net23.h24.if0,net23.h25.if0,
                                      net23.h26.if0,net23.h27.if0,net23.h28.if0,net23.h29.if0,net23.h30.if0], 
                                      delay="1")
net23.Link(name="l4",attachments=[net23_r0.if6, 
                                      net23.h31.if0,net23.h32.if0,net23.h33.if0,net23.h34.if0,net23.h35.if0,
                                      net23.h36.if0,net23.h37.if0,net23.h38.if0,net23.h39.if0,net23.h40.if0,
                                      net23.h41.if0,net23.h42.if0], 
                                      delay="1")

#connect net23 with the router in net2
net2.Link(name="lan0_r2",attachments=[net23_r0.if0, net2_r2.if3], delay="1")

#the other 6 lans and links between the lan to the router in net2
net41=net2.Net(net23, name='lan1')
net2.Link(name="lan1_r4",attachments=[net41.r0.if0, net2_r4.if1], delay="1")

net31=net2.Net(net23, name='lan2')
net2.Link(name="lan2_r3",attachments=[net31.r0.if0, net2_r3.if1], delay="1")

net51=net2.Net(net23, name='lan3')
net2.Link(name="lan3_r5",attachments=[net51.r0.if0, net2_r5.if1], delay="1")

net63=net2.Net(net23, name='lan4')
net2.Link(name="lan4_r6",attachments=[net63.r0.if0, net2_r6.if3], delay="1")

net62=net2.Net(net23, name='lan5')
net2.Link(name="lan5_r6",attachments=[net62.r0.if0, net2_r6.if2], delay="1")

net61=net2.Net(net23, name='lan6')
net2.Link(name="lan6_r6",attachments=[net61.r0.if0, net2_r6.if1], delay="1")
#end network2

#net3 includes 4 routers and 5 lans
net3 = exp.topNet.Net(overlay=None, name='net3' )
net3.routing.ShortestPath()

net3_r0=net3.Router(name="r0")
net3_r0.Interface(name="if0")
net3_r0.Interface(name="if1")
net3_r0.Interface(name="if2")
net3_r0.Interface(name="if3")

net3_r1=net3.Router(name="r1")
net3_r1.Interface(name="if0")
net3_r1.Interface(name="if1")
net3_r1.Interface(name="if2")
net3_r1.Interface(name="if3")

net3_r2=net3.Router(name="r2")
net3_r2.Interface(name="if0")
net3_r2.Interface(name="if1")
net3_r2.Interface(name="if2")

net3_r3=net3.Router(name="r3")
net3_r3.Interface(name="if0")
net3_r3.Interface(name="if1")
net3_r3.Interface(name="if2")
net3_r3.Interface(name="if3")

#router to router links in net3
net3.Link(name="r0_r1",attachments=[net3_r0.if1, net3_r1.if3], delay="1")
net3.Link(name="r1_r2",attachments=[net3_r1.if2, net3_r2.if0], delay="1")
net3.Link(name="r2_r3",attachments=[net3_r2.if1, net3_r3.if3], delay="1")
net3.Link(name="r1_r3",attachments=[net3_r1.if1, net3_r3.if0], delay="1")

# 5 lans composed of a router with local area networks
net3_lan21=net3.Net(net23, name='lan0')
net3.Link(name="lan0_r0",attachments=[net3_lan21.r0.if0, net3_r0.if3], delay="1")

net3_lan22=net3.Net(net23, name='lan1')
net3.Link(name="lan1_r0",attachments=[net3_lan22.r0.if0, net3_r0.if2], delay="1")

net3_lan23=net3.Net(net23, name='lan2')
net3.Link(name="lan2_r2",attachments=[net3_lan23.r0.if0, net3_r2.if2], delay="1")

net3_lan24=net3.Net(net23, name='lan3')
net3.Link(name="lan3_r3",attachments=[net3_lan24.r0.if0, net3_r3.if2], delay="1")

net3_lan25=net3.Net(net23, name='lan4')
net3.Link(name="lan4_r3",attachments=[net3_lan25.r0.if0, net3_r3.if1], delay="1")
#end network3

#links to connect router4 in net01 to net2
exp.topNet.Link(name="r4_net2_1",attachments=[net01_r4.if2, net2_r1.if0], delay="1")
exp.topNet.Link(name="r4_net2_2",attachments=[net01_r4.if3, net2_r0.if0], delay="1")

#links to connect router5 in net01 to net3

exp.topNet.Link(name="r5_net3_1",attachments=[net01_r5.if2, net3_r0.if0], delay="1")
exp.topNet.Link(name="r5_net3_2",attachments=[net01_r5.if1, net3_r1.if0], delay="1")


exp.compile("14.12.0.0")

part_str="1::1:1"

partitioning=exp.partition(part_str)
xmlprinter=pyprime.visitors.XMLVisitor(printSchema=False)
xmlprinter.visit(exp)
v=pyprime.visitors.TlvVisitor(dirname=".",partitioning=partitioning)
v.encode()
