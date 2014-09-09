def create():
    print "Creating 'Campus Network'"
    print "<!--",20*'*',"-->"
    #Import necessary modules
    import pyprime
    db = pyprime.Database()
        
    campusNet = db.loadLibrary(name = "Campus Network")
    
    #Create a library with the specified name
    lib = db.createLibrary("20 Campus Network", deleteIfExists=True)
    
    #Create an empty network
    campus20 = lib.Net(name="campus20")
    
    #list of references to campus networks
    campusNets = []
    
    #Create 30 campus networks
    for i in range(10, 30) :
        net = campus20.Net(campusNet.campus, name = "Subnetwork%i" % (i), liftSymbols=True, alignment='s%i'%(i-10))
        campusNets.append(net)
        
    # ring links
    for i in range(0, 20) :
        campus20.Link(delay=pyprime.Symbol("Baser2rLink_delay"), attachments=[campusNets[i].Subnetwork0.Router_0.i_3, campusNets[(i+1) % 20].Subnetwork0.Router_0.i_4])
    
    # cross links
    for i in [0, 2, 4, 6, 8] :
        campus20.Link(delay=pyprime.Symbol("Baser2rLink_delay"), attachments=[campusNets[i].Subnetwork0.Router_0.i_5, campusNets[i+10].Subnetwork0.Router_0.i_5])
    
    # pentagon links
    for i in [0, 4, 8, 12, 16] :
        campus20.Link(delay=pyprime.Symbol("Baser2rLink_delay"), attachments=[campusNets[i].Subnetwork0.Router_0.i_5,campusNets[(i+4)%20].Subnetwork0.Router_0.i_5])
    
    #assign UIDs and routing
    lib.compile()
    
    #save library
    lib.save()
    print "<!--",20*'*',"-->"
    print "Finished"
    pass
#import cProfile
#cProfile.run('create()')
create()