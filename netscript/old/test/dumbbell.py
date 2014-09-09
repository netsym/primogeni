# load the prime module
import pyprime
#connect to the database
db = pyprime.Database()

# create an experiment; an experiment is identified by an arbitrary string name;
exp=db.createExperiment(name="dumbbell", deleteIfExists=True)

# create a network with name "topnet"; it's an identifier according to
topnet=exp.Net(name="topnet")

# create hosts
h1=topnet.Host(name="h1")
h1.Interface(name="if0",latency ="0.001", bit_rate="100000000")
h2=topnet.Host(h1,name="h2",is_emulated=True)
h3=topnet.Host(h1,name="h3")
h4=topnet.Host(h1,name="h4",is_emulated=True)

# create r1
r1=topnet.Router(name="r1")
r1.Interface(name="if_h1",latency ="0.001", bit_rate="100000000")
r1.Interface(r1.if_h1,name="if_h2")
r1.Interface(r1.if_h1,name="if_r2")

#link r1 with h1,h2
topnet.Link(name='link_h1_r1',attachments=[h1.if0,r1.if_h1], delay="5", bandwidth="100000000")
topnet.Link(name='link_h2_r1',attachments=[h2.if0,r1.if_h2], delay="5", bandwidth="100000000")


# create r2
r2=topnet.Router(name="r2")
r2.Interface(name="if_h3",latency ="0.001", bit_rate="100000000")
r2.Interface(r2.if_h3,name="if_h4")
r2.Interface(r2.if_h3,name="if_r1")

#link r2 with h3,h4
topnet.Link(name='link_h3_r2',attachments=[h3.if0,r2.if_h3], delay="5", bandwidth="100000000")
topnet.Link(name='link_h4_r2',attachments=[h4.if0,r2.if_h4], delay="5", bandwidth="100000000")

#link r1 and r2
topnet.Link(name='link_r1_r2',attachments=[r1.if_r2,r2.if_r1], delay="5", bandwidth="10000000")

exp.compile()

print "<!--",20*'*',"-->"

xmlprinter=pyprime.visitors.XMLVisitor(printSchema=False)
xmlprinter.visit(exp)

