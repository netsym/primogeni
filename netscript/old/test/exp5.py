import pyprime, sys
db = pyprime.Database()
exp=db.createExperiment(name="exp5", deleteIfExists=True)
n=exp.Net(name="topnet")

s1=n.Net(name="sub1")

h11=s1.Host(name="host1")
h11.Interface(name="if0")
h11.Interface(name="if1")

h12=s1.Host(name="host2")
h12.Interface(name="if0")
h12.Interface(name="if1")

r11=s1.Router(name="router1")
r11.Interface(name="nic0")
r11.Interface(name="nic1")
r11.Interface(name="nic2")

s1.Link(name="link1",attachments=[r11.nic0,h11.if0], delay="1")
s1.Link(name="link2",attachments=[r11.nic1,h12.if0], delay=pyprime.Symbol("link_delay"))
s1.Link(name="link3",attachments=[h11.if1,h12.if1])

s2=n.Net(s1,name="sub2")
n.Link(name="link0",attachments=[s1.router1.nic2,s2.router1.nic2])

if False:
    print "h21.if0.rank(h21.if0)=",h21.if0.rank(h21.if0)
    print "h21.if0.rank()=",h21.if0.rank()
    print '-'*10
    print "h21.if0.rank(s2)=",h21.if0.rank(s2)
    print "r21.nic2.rank(s2)=",r21.nic2.rank(s2)
    print '-'*10
    print "h11.nic2.rank(s1)=",h11.if0.rank(s1)
    print "r11.nic2.rank(s1)=",r11.nic2.rank(s1)
    print '-'*10

exp.compile()

if 'xml' in sys.argv:
    print "<!--",20*'*',"-->"
    print "<!-- Doing XML -->"
    print "<!--",20*'*',"-->"
    xmlprinter=pyprime.visitors.XMLVisitor(printSchema=False)
    xmlprinter.visit(exp)
    pass
elif 'tlv' in sys.argv:
    print "<!--",20*'*',"-->"
    print "<!-- Doing TLV -->"
    print "<!--",20*'*',"-->"
    for partition in exp.partitions:
        tmp=open("partition_%i.tlv"%(partition.id),'w')
        pyprime.visitors.CPPVisitor(file=tmp,partition=partition)
        tmp.close()
        pass
    pass
else:
    print "<!--",20*'*',"-->"
    print "<!-- tlv nor xml was chosen! -->"
    print "<!--",20*'*',"-->"
    pass

