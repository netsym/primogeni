import logging

import pyprime, sys
db = pyprime.Database()
exp=db.createExperiment(name="exp_multi_edges", deleteIfExists=True)

topnet=exp.Net(name="topnet")
topnet.routing.ShortestPath()

top1=topnet.Net(name="top1",alignment='alignment1')
top1.routing.ShortestPath()

sub1=top1.Net(name="sub1", alignment='alignment2')
sub1.routing.ShortestPath()

gateway1=sub1.Router(name="gateway1")
gateway1.Interface(name="sib")
gateway1.Interface(name="outside")
gateway1.Interface(name="if0")
gateway1.Interface(name="if1")

gateway2=sub1.Router(name="gateway2")
gateway2.Interface(name="sib")
gateway2.Interface(name="outside")
gateway2.Interface(name="if0")
gateway2.Interface(name="if1")

h1=sub1.Host(name="h1")
h1.Interface(name="if0")
h2=sub1.Host(name="h2")
h2.Interface(name="if0")
h3=sub1.Host(name="h3")
h3.Interface(name="if0")
h4=sub1.Host(name="h4")
h4.Interface(name="if0")


sub1.Link(name="h1_g1",attachments=[h1.if0,gateway1.if0], delay="5")
sub1.Link(name="h2_g1",attachments=[h2.if0,gateway1.if1], delay="5")
sub1.Link(name="h3_g2",attachments=[h3.if0,gateway2.if0], delay="5")
sub1.Link(name="h4_g2",attachments=[h4.if0,gateway2.if1], delay="5")
sub1.Link(name="g1_g2",attachments=[gateway1.sib,gateway2.sib], delay="5")

gateway3=top1.Router(name="gateway3")
gateway3.Interface(name="outside")
gateway3.Interface(name="if0")

gateway4=top1.Router(name="gateway4")
gateway4.Interface(name="outside")
gateway4.Interface(name="if0")

top1.Link(name="g1_g3",attachments=[sub1.gateway1.outside,gateway3.if0], delay="5")
top1.Link(name="g2_g4",attachments=[sub1.gateway2.outside,gateway4.if0], delay="5")

top2=topnet.Net(top1, name="top2")

gateway5=topnet.Router(name="gateway5")
gateway5.Interface(name="sib")
gateway5.Interface(name="if0")
gateway5.Interface(name="if1")
gateway6=topnet.Router(name="gateway6")
gateway6.Interface(name="sib")
gateway6.Interface(name="if0")
gateway6.Interface(name="if1")


gw3_gw5=topnet.Link(name="gw3_gw5",attachments=[top1.gateway3.outside,gateway5.if0])
gw4_gw5=topnet.Link(name="gw4_gw5",attachments=[top1.gateway4.outside,gateway5.if1])
gw6_gw3=topnet.Link(name="gw6_gw3",attachments=[gateway6.if0,top2.gateway3.outside])
gw6_gw4=topnet.Link(name="gw6_gw4",attachments=[gateway6.if1,top2.gateway4.outside])
gw5_gw6=topnet.Link(name="gw5_gw6",attachments=[gateway5.sib, gateway6.sib])


exp.compile()
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
