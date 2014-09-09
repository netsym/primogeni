import logging

import pyprime, sys
db = pyprime.Database()
exp=db.createExperiment(name="exp7", deleteIfExists=True)

topnet=exp.Net(name="topnet")
top1=topnet.Net(name="top1",alignment='alignment1')
sp=top1.routing.ShortestPath()

gateway1=top1.Router(name="gateway1")
gateway1.IPv4Session()
gateway1.ICMPv4Session()
gateway1.Interface(name="sib")
gateway1.sib.DropTailQueue()
for i in range(1,3):
    h=top1.Host(name="host%i"%(i))
    h.IPv4Session()
    h.ICMPv4Session()
    j=h.Interface(name="if0")
    j.DropTailQueue()
    k=gateway1.Interface(name="if%i"%(i))
    k.DropTailQueue()
    top1.Link(name="l_%i"%(i),attachments=[j,k], delay="5")
    pass

top2=topnet.Net(top1, name="top2")

gateway=topnet.Router(name='gateway')
gateway.IPv4Session()
gateway.ICMPv4Session()
gateway.Interface(name="top1")
gateway.top1.DropTailQueue()
gateway.Interface(name="top2")
gateway.top2.DropTailQueue()

#link top1 & top2 by gateway
top1_gw=topnet.Link(name="top1_gw",attachments=[top1.gateway1.sib,gateway.top1])
gw_top2=topnet.Link(name="gw_top2",attachments=[gateway.top2, top2.gateway1.sib])
top1_top2=topnet.Link(name="top1_top2",attachments=[top1.gateway1.sib,top2.gateway1.sib])
topnet.routing.ShortestPath()
#bgp=topnet.routing.BGP()
#bgp.BGPLinkType(link_type='c2p',link=top1_top2)

t=topnet.Traffic()
t.PingTraffic(srcs="[18]",dsts="[26]",count=1)
t.PingTraffic(srcs="[26]",dsts="[46]",count=1)
#t.PingTraffic(srcs="[26]",dsts="[54]",count=1)
#"[1,2]"

exp.compile("14.12.0.0")

part_str="1::1:1"

partitioning=exp.partition(part_str)
xmlprinter=pyprime.visitors.XMLVisitor(printSchema=False)
xmlprinter.visit(exp)
v=pyprime.visitors.TlvVisitor(dirname=".",partitioning=partitioning)
v.encode()