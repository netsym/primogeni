print "<!--"
# load the prime module; the scripting language is implemented as a
# python module
import pyprime

db = pyprime.Database()

# create an experiment; an experiment is identified by an arbitrary
# string name; (advanced stuff: the experiment will use the default
# schema according to the site installation; otherwise, one can use
# the named parameter "schema" to specify it)
exp=db.createExperiment(name="basic example 1: 3-host ping", deleteIfExists=True)

# create a network with name "topnet"; it's an identifier according to
# our naming criteria, so that the network could be later referenced
# simply as exp.topnet; an error occurs if the name has already been
# in use.
n=exp.Net(name="topnet")

# create an unnamed host within the topnet with one nameless interface
# (by default, there are 0 interfaces)
h1=n.Host(name="h1",nics=1)

# create a host within the topnet with two interfaces; this is done by
# creating a host without an interface, and then creating two
# interfaces in the host (this time we will give each of them a name)
h2=n.Router(name="h2")
h2.Interface(name="nic0",latency =pyprime.Symbol("param1"))
h2.Interface(name="nic1",latency =pyprime.Symbol("param1"))

# create an unbound (nameless) network interface before assigning it
# to a newly created host; an unbound interface will be created using
# the default schema (or, one could specify one; see
# pyprime.createExperiment note above)
#XXX unbound type no longer work correctly......
#if0 = exp.Interface(name="if0",latency =pyprime.Symbol("param1"))
#h3 = n.Host(name="h3",nics=[if0])
h3 = n.Host(name="h3")
if0 = h3.Interface(name="if0",latency =pyprime.Symbol("param1"))

# connect the three hosts using the reference to the host or the
# reference to the network interface; in the former case, if a host is
# specified and the host has no more unattached interfaces, an error
# will be thrown; otherwise, an arbitrary unattached interface of the
# host will be used
n.Link(name='l1',attachments=[h1,h2.nic0])
n.Link(name='l2',attachments=[h2.nic1,if0])

# create traffic for the network; direct ICMP ping from h1 to h3;
# since the routing is not specified for the network, it will use
# default routing, which is shortest path
t=n.Traffic(name="traff")
t.Ping(name="ping")#,src=h1,dst=h3)

# run the experiment for 100 seconds; by default, the results will be
# directed to stdout
exp.compile()
exp.run(100)
print "-->"

print "<!--",20*'*',"-->"
print "<!--",20*'*',"-->"

xmlprinter=pyprime.visitors.XMLVisitor(printSchema=False)
xmlprinter.visit(exp)

