
print "Creating 'Campus Network'"
print "<!--",20*'*',"-->"
#import pyprime module
import pyprime
db = pyprime.Database()

#Create a library with the specified name
lib = db.createLibrary("Campus Network", deleteIfExists=True)

#Create an empty network
print "fasdfsdF"
campus = lib.Net(name="campus")
print "fasdfsdF"

#-------Local Net-------------------
#Create a local network 
localNet = lib.Net(name="LocalNet")
#Create router for local network
router = localNet.Router(name="LocalRouter")
#Attach interfaces to router
router.Interface( bit_rate=pyprime.Symbol("Baseas2asInterface_bit_rate"), latency=pyprime.Symbol("Baseas2asInterface_latency"), name="i_0")
for i in range(3, 7):
    router.Interface( bit_rate=pyprime.Symbol("BaseLanInterface_bit_rate"), latency=pyprime.Symbol("BaseLanInterface_latency"), name="i_%i" % (i))

#Create a collection of clients
#XXX localNet.Collection(name="clients")
#temporary list to hold ifaces
ifaces=[]
#Create 42 identical hosts
for i in range(1,43) :
    host = localNet.Host(name="h_%i" % (i))
    host.Interface( bit_rate=pyprime.Symbol("BaseLanInterface_bit_rate"), latency=pyprime.Symbol("BaseLanInterface_latency"), name="i_0")
    ifaces.append(host.i_0)
    #XXX localNet.clients.append(host)

#Attach interfaces using links
a=ifaces[0:10]+[router.i_3]
link = localNet.Link( delay=pyprime.Symbol("BaseLanLink_delay"), name="NATE", attachments=a)

localNet.Link( delay=pyprime.Symbol("BaseLanLink_delay"), attachments=ifaces[10:21]+[router.i_4])
localNet.Link( delay=pyprime.Symbol("BaseLanLink_delay"), attachments=ifaces[21:31]+[router.i_5])
localNet.Link( delay=pyprime.Symbol("BaseLanLink_delay"), attachments=ifaces[31:]+[router.i_6])

#end of local network

#-------Network 3-------------------
#Create subnetwork 3
net3 = campus.Net(name = "Subnetwork3")
#XXX net3.Collection(name= "clients")

#Create 4 routers in this subnetwork
for i in range(0, 4) :
    router = net3.Router(name = "Router_%i" % (i))
    if i == 2 :
        for j in range(0, 3) :
            router.Interface( bit_rate=pyprime.Symbol("Baseas2asInterface_bit_rate"), latency=pyprime.Symbol("Baseas2asInterface_latency"), name = "i_%i" % (j))
    else :
        for j in range(0, 4) :
            router.Interface( bit_rate=pyprime.Symbol("Baseas2asInterface_bit_rate"), latency=pyprime.Symbol("Baseas2asInterface_latency"), name = "i_%i" % (j))

#Link the newly created routers together
net3.Link( delay=pyprime.Symbol("Baser2rLink_delay"), attachments=[net3.Router_0.i_0, net3.Router_1.i_3])
net3.Link( delay=pyprime.Symbol("Baser2rLink_delay"), attachments=[net3.Router_1.i_2, net3.Router_2.i_0])
net3.Link( delay=pyprime.Symbol("Baser2rLink_delay"), attachments=[net3.Router_2.i_1, net3.Router_3.i_3])
net3.Link( delay=pyprime.Symbol("Baser2rLink_delay"), attachments=[net3.Router_1.i_1, net3.Router_3.i_0])

#Create 5 local area networks
for i in range(21, 26) :
    local = net3.Net(lib.LocalNet, name = "Local_%i" % (i))
    #net3.clients.extend(local.clients)
    #XXX for ack in local.clients:
    #XXX     net3.clients.append(ack)
    #XXX     pass
    pass

#Attach local networks with previously created routers
net3.Link( delay=pyprime.Symbol("Baser2rLink_delay"), attachments=[net3.Local_21.LocalRouter.i_0, net3.Router_0.i_3])
net3.Link( delay=pyprime.Symbol("Baser2rLink_delay"), attachments=[net3.Local_22.LocalRouter.i_0, net3.Router_0.i_2])
net3.Link( delay=pyprime.Symbol("Baser2rLink_delay"), attachments=[net3.Local_23.LocalRouter.i_0, net3.Router_2.i_2])
net3.Link( delay=pyprime.Symbol("Baser2rLink_delay"), attachments=[net3.Local_24.LocalRouter.i_0, net3.Router_3.i_2])
net3.Link( delay=pyprime.Symbol("Baser2rLink_delay"), attachments=[net3.Local_25.LocalRouter.i_0, net3.Router_3.i_1])

#-------Network 2-------------------
#Create subnetwork 2
net2 = campus.Net(name = "Subnetwork2")
#XXX net2.Collection(name="clients")

#Create 7 routers in this subnetwork
for i in range(0, 7) :
    router = net2.Router(name = "Router_%i" % (i))
    if i == 4 :
        for j in range(0, 2) :
            router.Interface( bit_rate=pyprime.Symbol("Baseas2asInterface_bit_rate"), latency=pyprime.Symbol("Baseas2asInterface_latency"), name = "i_%i" % (j))
    elif i==0 or i==1 or i==5 :
        for j in range(0, 3) :
            router.Interface( bit_rate=pyprime.Symbol("Baseas2asInterface_bit_rate"), latency=pyprime.Symbol("Baseas2asInterface_latency"), name = "i_%i" % (j))
    else :
        for j in range(0, 4) :
            router.Interface( bit_rate=pyprime.Symbol("Baseas2asInterface_bit_rate"), latency=pyprime.Symbol("Baseas2asInterface_latency"), name = "i_%i" % (j))

#Link the newly created routers together
net2.Link( delay=pyprime.Symbol("Baser2rLink_delay"), attachments=[net2.Router_0.i_1, net2.Router_1.i_2])
net2.Link( delay=pyprime.Symbol("Baser2rLink_delay"), attachments=[net2.Router_0.i_2, net2.Router_2.i_0])
net2.Link( delay=pyprime.Symbol("Baser2rLink_delay"), attachments=[net2.Router_2.i_1, net2.Router_3.i_3])
net2.Link( delay=pyprime.Symbol("Baser2rLink_delay"), attachments=[net2.Router_2.i_2, net2.Router_4.i_0])
net2.Link( delay=pyprime.Symbol("Baser2rLink_delay"), attachments=[net2.Router_1.i_1, net2.Router_3.i_0])
net2.Link( delay=pyprime.Symbol("Baser2rLink_delay"), attachments=[net2.Router_3.i_2, net2.Router_5.i_0])
net2.Link( delay=pyprime.Symbol("Baser2rLink_delay"), attachments=[net2.Router_5.i_2, net2.Router_6.i_0])

#Create 5 local area networks
for i in [23, 41, 31, 51, 63, 62, 61] :
    local = net2.Net(lib.LocalNet, name = "Local_%i" % (i))
    #XXX net2.clients.extend(local.clients)

#Attach local networks with previously created routers
net2.Link( delay=pyprime.Symbol("Baser2rLink_delay"), attachments=[net2.Local_23.LocalRouter.i_0, net2.Router_2.i_3])
net2.Link( delay=pyprime.Symbol("Baser2rLink_delay"), attachments=[net2.Local_41.LocalRouter.i_0, net2.Router_4.i_1])
net2.Link( delay=pyprime.Symbol("Baser2rLink_delay"), attachments=[net2.Local_31.LocalRouter.i_0, net2.Router_3.i_1])
net2.Link( delay=pyprime.Symbol("Baser2rLink_delay"), attachments=[net2.Local_51.LocalRouter.i_0, net2.Router_5.i_1])
net2.Link( delay=pyprime.Symbol("Baser2rLink_delay"), attachments=[net2.Local_63.LocalRouter.i_0, net2.Router_6.i_3])
net2.Link( delay=pyprime.Symbol("Baser2rLink_delay"), attachments=[net2.Local_62.LocalRouter.i_0, net2.Router_6.i_2])
net2.Link( delay=pyprime.Symbol("Baser2rLink_delay"), attachments=[net2.Local_61.LocalRouter.i_0, net2.Router_6.i_1])

#-------Network 1-------------------
#Create subnetwork 1
net1 = campus.Net(name = "Subnetwork1")

#Create 2 routers in this subnetwork
for i in range(0, 2) :
    router = net1.Router(name = "Router_%i" % (i))
    if i == 0 :
        for j in range(0, 4) :
            router.Interface( bit_rate=pyprime.Symbol("Baseas2asInterface_bit_rate"), latency=pyprime.Symbol("Baseas2asInterface_latency"), name = "i_%i" % (j))    
    else :
        for j in range(0, 3) :
            router.Interface( bit_rate=pyprime.Symbol("Baseas2asInterface_bit_rate"), latency=pyprime.Symbol("Baseas2asInterface_latency"), name = "i_%i" % (j))

#Create a collection of servers
#XXX net1.Collection(name="servers")

#Create Servers
for i in range(2, 6) :
    host = net1.Host(name = "Server_%i" % (i))
    host.Interface( bit_rate=pyprime.Symbol("Baseas2asInterface_bit_rate"), latency=pyprime.Symbol("Baseas2asInterface_latency"), name = "i_0")
    #XXX net1.servers.append(host)

#Attach servers and routers together
net1.Link( delay=pyprime.Symbol("BaseLanLink_delay"), attachments=[net1.Router_0.i_2, net1.Router_1.i_0])
net1.Link( delay=pyprime.Symbol("BaseLanLink_delay"), attachments=[net1.Router_0.i_0, net1.Server_2.i_0])
net1.Link( delay=pyprime.Symbol("BaseLanLink_delay"), attachments=[net1.Router_0.i_1, net1.Server_3.i_0])
net1.Link( delay=pyprime.Symbol("BaseLanLink_delay"), attachments=[net1.Router_1.i_1, net1.Server_4.i_0])
net1.Link( delay=pyprime.Symbol("BaseLanLink_delay"), attachments=[net1.Router_1.i_2, net1.Server_5.i_0])

#-------Network 0-------------------
#Create subnetwork 0
net0 = campus.Net(name = "Subnetwork0")

#Create 3 routers
for i in range(0, 3) :
    router = net0.Router(name = "Router_%i" % (i))
    if i == 0:
        for j in range(0, 8) :
            router.Interface( bit_rate=pyprime.Symbol("Baseas2asInterface_bit_rate"), latency=pyprime.Symbol("Baseas2asInterface_latency"), name = "i_%i" % (j))    
    else :
        for j in range(0, 3) :
            router.Interface( bit_rate=pyprime.Symbol("Baseas2asInterface_bit_rate"), latency=pyprime.Symbol("Baseas2asInterface_latency"), name = "i_%i" % (j))
            
#Link routers together
net0.Link( delay=pyprime.Symbol("Baser2rLink_delay"), attachments=[net0.Router_0.i_1, net0.Router_1.i_2])
net0.Link( delay=pyprime.Symbol("Baser2rLink_delay"), attachments=[net0.Router_1.i_0, net0.Router_2.i_1])
net0.Link( delay=pyprime.Symbol("Baser2rLink_delay"), attachments=[net0.Router_0.i_0, net0.Router_2.i_2])

#-------Network 01-------------------
#Create subnetwork 01
net01 = campus.Net(name = "Subnetwork01")

#Create 3 routers
for i in [4, 5] :
    router = net01.Router(name = "Router_%i" % (i))
    if i == 4 :
        for j in [0,1] :
            router.Interface( bit_rate=pyprime.Symbol("Baseas2asInterface_bit_rate"), latency=pyprime.Symbol("Baseas2asInterface_latency"), name = "i_%i" % (j))    
        for j in [2,3] :
            router.Interface( bit_rate=pyprime.Symbol("Baseas2asInterface_bit_rate"), latency=pyprime.Symbol("Baseas2asInterface_latency"), name = "i_%i" % (j))
    else:     
        for j in [0,3] :
            router.Interface( bit_rate=pyprime.Symbol("Baseas2asInterface_bit_rate"), latency=pyprime.Symbol("Baseas2asInterface_latency"), name = "i_%i" % (j))    
        for j in [1,2] :
            router.Interface( bit_rate=pyprime.Symbol("Baseas2asInterface_bit_rate"), latency=pyprime.Symbol("Baseas2asInterface_latency"), name = "i_%i" % (j))

#Link these 2 routers together
net01.Link( delay=pyprime.Symbol("Baser2rLink_delay"), attachments = [net01.Router_4.i_1, net01.Router_5.i_3])
            
# links from backbone net 0 to routers 4, 5, 1:0
campus.Link( delay=pyprime.Symbol("Baser2rLink_delay"), attachments=[campus.Subnetwork0.Router_0.i_2, net01.Router_4.i_0])
campus.Link( delay=pyprime.Symbol("Baser2rLink_delay"), attachments=[campus.Subnetwork0.Router_1.i_1, net01.Router_5.i_0])
campus.Link( delay=pyprime.Symbol("Baser2rLink_delay"), attachments=[campus.Subnetwork0.Router_2.i_0, campus.Subnetwork1.Router_0.i_3])

# links to connect router 4 to net 2
campus.Link( delay=pyprime.Symbol("Baser2rLink_delay"), attachments=[net01.Router_4.i_2, campus.Subnetwork2.Router_1.i_0])
campus.Link( delay=pyprime.Symbol("Baser2rLink_delay"), attachments=[net01.Router_4.i_3, campus.Subnetwork2.Router_0.i_0])

# links to connect router 5 to network3
campus.Link( delay=pyprime.Symbol("Baser2rLink_delay"), attachments=[net01.Router_5.i_2, campus.Subnetwork3.Router_0.i_0])
campus.Link( delay=pyprime.Symbol("Baser2rLink_delay"), attachments=[net01.Router_5.i_1, campus.Subnetwork3.Router_1.i_0])

#Group all client together and make servers visible
#XXX campus.Collection(name="clients")
#XXX campus.Collection(name="servers")
#XXX campus.clients.extend(campus.Subnetwork3.clients)
#XXX campus.clients.extend(campus.Subnetwork2.clients)
#XXX  campus.servers.extend(campus.Subnetwork1.servers)
#XXX no idea what it should be
campus.Alias(campus.Subnetwork0.Router_2,name="gateway_router")

#assign UIDs and routing
lib.compile()

#Save library
lib.save()

print "<!--",20*'*',"-->"
print "Finished"
