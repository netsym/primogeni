print "<!--"
# import pyprime module
import pyprime
db = pyprime.Database()

# create an experiment with name
exp=db.createExperiment(name="basic example 2: 2-campus ftp",deleteIfExists=True)

# open a library with the given name; an error will be thrown if the
# specified library does not exist
lib=db.loadLibrary("campus network")

# create the top-level network
n=exp.Net(name="topnet")

# create two sub-networks by each copying from a network named
# "campus" from the library
n1=n.Net(lib.campus, name="c1", liftSymbols=True)
n2=n.Net(lib.campus, name="c2", liftSymbols=True)

# connect the gateway routers of the two sub-networks;
# "gateway_router" is an alias pre-defined in the library, which
# points to the gateway router
n.Link(attachments=[n1.gateway_router, n2.gateway_router])
# create traffic, directing FTP traffic from clients in the one campus
# network to the servers in the other campus network; "clients" and
# "servers" are sets of aliases to hosts; the sets are pre-defined in
# the library
t=n.Traffic()
t.FTP(src=n1.clients,dst=n2.servers)
t.FTP(src=n2.clients,dst=n1.servers)

# run experiment for 100 seconds
exp.run(100)
print "-->"
print "<!--",20*'*',"-->"
print "<!--",20*'*',"-->"
xmlprinter=pyprime.visitors.XMLVisitor(printSchema=False)
xmlprinter.visit(exp)

