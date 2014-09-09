import logging

import pyprime, sys
db = pyprime.Database()
exp=db.createExperiment(name="exp6", deleteIfExists=True)

topnet=exp.Net(name="topnet")
top1=topnet.Net(name="top1",alignment='alignment1')
#top1.ip_prefix="10.1.0.0/16"
sp=top1.routing.ShortestPath()

super1=top1.Net(name="super1",alignment='alignment2')
super1.routing.ShortestPath()

s1=super1.Net(name="sub1",alignment='alignment3')
#s1.routing.ShortestPath()

gateway1=s1.Router(name="gateway1")
gateway1.IPv4Session()
gateway1.ICMPv4Session()
gateway1.Interface(name="outside")
gateway1.outside.DropTailQueue()
gateway1.Interface(name="sib1")
gateway1.sib1.DropTailQueue()
gateway1.Interface(name="sib2")
gateway1.sib2.DropTailQueue()
gateway1.Interface(name="subsub1")
gateway1.subsub1.DropTailQueue()
gateway1.Interface(name="subsub2")
gateway1.subsub2.DropTailQueue()

sub_sub1=s1.Net(name="sub_sub1",alignment='alignment4')
sub_sub1.routing.ShortestPath()
sub_gateway=sub_sub1.Router(name="sub_gateway")
sub_gateway.IPv4Session()
sub_gateway.ICMPv4Session()
sub_gateway.Interface(name="outside")
sub_gateway.outside.DropTailQueue()
for i in range(1,3):
    h=sub_sub1.Host(name="host%i"%(i))
    h.IPv4Session()
    h.ICMPv4Session()
    j=h.Interface(name="if0")
    j.DropTailQueue()
    k=sub_gateway.Interface(name="if%i"%(i))
    k.DropTailQueue()
    sub_sub1.Link(name="l_%i"%(i),attachments=[j,k], delay="5")
    pass

sub_sub2=s1.Net(sub_sub1, name="sub_sub2")

#link subsub1 & subsub2 by gateway in sub11
s1.Link(name="l_subsub1",attachments=[gateway1.subsub1, sub_sub1.sub_gateway.outside], delay="1")
s1.Link(name="l_subsub2",attachments=[gateway1.subsub2, sub_sub2.sub_gateway.outside], delay="1")

#copy s1
s2=super1.Net(s1,name="sub2",alignment='alignment5')

#link the gateways of sub1 and sub2
super1.Link(name="l_sub1_sub2",attachments=[s1.gateway1.sib1, s2.gateway1.sib1], delay="1")

#link sub1 & sub2 by gateway in super1
gateway2=super1.Router(name='gateway2')
gateway2.IPv4Session()
gateway2.ICMPv4Session()
gateway2.Interface(name="outside")
gateway2.outside.DropTailQueue()
gateway2.Interface(name="sib1")
gateway2.sib1.DropTailQueue()
gateway2.Interface(name="sib2")
gateway2.sib2.DropTailQueue()
gateway2.Interface(name="sub1")
gateway2.sub1.DropTailQueue()
gateway2.Interface(name="sub2")
gateway2.sub2.DropTailQueue()

super1.Link(name="l_super1_sub1",attachments=[gateway2.sub1,s1.gateway1.outside])
super1.Link(name="l_super1_sub2",attachments=[gateway2.sub2,s2.gateway1.outside])

super2=top1.Net(super1,name="super2",alignment='alignment6')

#link the gateways of super1 and super2
top1.Link(name="l_super1_super2",attachments=[super1.gateway2.sib1, super2.gateway2.sib1], delay="1")

#link super1 & super2 by gateway in top1
gateway3=top1.Router(name='gateway3')
gateway3.IPv4Session()
gateway3.ICMPv4Session()
gateway3.Interface(name="outside")
gateway3.outside.DropTailQueue()
gateway3.Interface(name="sib1")
gateway3.sib1.DropTailQueue()
gateway3.Interface(name="sib2")
gateway3.sib2.DropTailQueue()
gateway3.Interface(name="super1")
gateway3.super1.DropTailQueue()
gateway3.Interface(name="super2")
gateway3.super2.DropTailQueue()

top1.Link(name="l_top1_super1",attachments=[gateway3.super1,super1.gateway2.outside])
top1.Link(name="l_top1_super2",attachments=[gateway3.super2,super2.gateway2.outside])

top2=topnet.Net(top1,name='top2',alignment='alignment7')
#top2.ip_prefix="10.2.0.0/16"
#link top1 & top2 
top1_top2=topnet.Link(name="l_top1_top2",attachments=[top1.gateway3.sib1,top2.gateway3.sib1])
#bgp=topnet.routing.BGP()
#bgp.BGPLinkType(link_type='c2p',link=top1_top2)
exp.compile("14.12.0.0")

part_str="3::1:2,2:1,3:1"
for a in sys.argv:
    if a.count("::")>0:
        part_str=a
        break
    pass
partitioning=exp.partition(part_str)
if 'normal-xml' in sys.argv or 'xml' in sys.argv:
    print "<!--",20*'*',"-->"
    print "<!-- Doing XML -->"
    print "<!--",20*'*',"-->"
    xmlprinter=pyprime.visitors.XMLVisitor(printSchema=False)
    xmlprinter.visit(exp)
    pass
if 'part-xml' in sys.argv:
    print "<!--",20*'*',"-->"
    print "<!-- Doing Partitioning XML -->"
    print "<!--",20*'*',"-->"
    xmlprinter=pyprime.visitors.PartitioningVisitor(printResourceModel=False, printSchema=False, printSymbolTable=False, printAttrs=False)
    xmlprinter.visit(partitioning)
    pass
if 'tlv' in sys.argv:
    print "<!--",20*'*',"-->"
    print "<!-- Doing TLV -->"
    print "<!--",20*'*',"-->"
    v=pyprime.visitors.TlvVisitor(dirname=".",partitioning=partitioning)
    v.encode()
    pass
