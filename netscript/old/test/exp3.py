print "<!--"
# import pyprime module
import pyprime, sys
print "XXX: pyprime needs some work before we can pull off this exp!" 
sys.exit()

db = pyprime.Database()


# open a library with the given name
lib=db.Library("campus network")

# create an experiment with name
exp=db.createExperiment(name="basic example 3: 20-campus ftp in parallel",deleteIfExists=True)


# create the top-level network
n=exp.Net(name="topnet")

# create an empty traffic
t=n.Traffic()

# create 20 campuses, link them as a ring, assign each campus as an
# alignment (each alignment can potentially run on a separate
# processor), customize routing within each campus network, and
# specify FTP traffic between adjacent campuses
gw=[]
for i in range(0,20):
    cur=n.Net(lib.campus, name="c%i"%(i), alignment='a%i'%(i), liftSymbols=True)
    gw.append(cur.gateway_router)

    # create customized routing for the network (OSPF in this case);
    # if not specified, the default behavior is to use shortest path;
    # hello_interval is an OSPF parameter; it's the time (in seconds)
    # between sending hello packets among OSPF neighbors
    print "cur:",cur
    r=cur.getRoutingNode()
    #print type(r),":",dir(r)
    print "r:",r
    r.OSPF(hello_interval=3)

    if i>0:
        # the previous campus network can be directly referenced using
        # getattr(n,"name"), which is a python function
        prev=getattr(n,"c%i"%(i-1))

        # connect an unattached network interface of the two hosts
        n.Link(attachments=[cur.gateway_router,prev.gateway_router])
        
        # create FTP traffic from clients in the current campus to the
        # servers in both current and previous campuses
        t.FTP(src=cur.clients, dst=prev.servers)

# connect the first and last campus and set the traffic between them
n.Link(attachments=[n.c19.gateway_router, n.c0.gateway_router])
t.FTP(src=n.c19.clients, dst=n.c0.servers)

# create customized routing for the top-level network (BGP in this
# case); the BGP parameter "speakers" is set to be the list containing
# the gateway router from each campus network
r=n.Routing()
r.BGP(speakers=gw)

# bind an execution evironment of the given name with the experiment;
# the execution environment must previously exist (defined by the
# resource owner)
#env = exp.ExecEnv("mylab")
# run the experiment for 100 seconds in parallel on the first 10 hosts
# defined in the execution environment
exp.run()#time=100, hosts=env.hosts[0:10])

print "-->"
print "<!--",20*'*',"-->"
print "<!--",20*'*',"-->"
xmlprinter=pyprime.visitors.XMLVisitor(printSchema=False)
xmlprinter.visit(exp)
