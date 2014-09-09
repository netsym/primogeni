

import pyprime
db=pyprime.Database()
exp = db.createExperiment( name = 'Example1' )
n = exp.Net(name='topNet' )
n.routing.ShortestPath()

# Creating now a: NET object
net1 = exp.topNet.Net( overlay=None , name='net0' )
net1.routing.ShortestPath()

# Creating now a: ROUTER object
router2 = exp.topNet.net0.Router( overlay=None , name='router0' )
router2.ICMPv4Session()
router2.IPv4Session()

# Creating now a: INTERFACE object
interface3 = exp.topNet.net0.router0.Interface( overlay=None , name='interface0' , bit_rate=1000000000 , latency=0.000001 )
interface3.DropTailQueue()

# Creating now a: INTERFACE object
interface4 = exp.topNet.net0.router0.Interface( overlay=exp.topNet.net0.router0.interface0 , name='interface1' )

# Creating now a: INTERFACE object
interface5 = exp.topNet.net0.router0.Interface( overlay=exp.topNet.net0.router0.interface0 , name='interface2' )

# Creating now a: ROUTER object
router6 = exp.topNet.net0.Router( overlay=None , name='router1' )
router6.ICMPv4Session()
router6.IPv4Session()

# Creating now a: INTERFACE object
interface7 = exp.topNet.net0.router1.Interface( overlay=exp.topNet.net0.router0.interface0 , name='interface0' )

# Creating now a: INTERFACE object
interface8 = exp.topNet.net0.router1.Interface( overlay=exp.topNet.net0.router0.interface0 , name='interface1' )

# Creating now a: INTERFACE object
interface9 = exp.topNet.net0.router1.Interface( overlay=exp.topNet.net0.router0.interface0 , name='interface2' )

# Creating now a: ROUTER object
router10 = exp.topNet.net0.Router( overlay=None , name='router2' )
router10.ICMPv4Session()
router10.IPv4Session()

# Creating now a: INTERFACE object
interface11 = exp.topNet.net0.router2.Interface( overlay=exp.topNet.net0.router0.interface0 , name='interface0' )

# Creating now a: INTERFACE object
interface12 = exp.topNet.net0.router2.Interface( overlay=exp.topNet.net0.router0.interface0 , name='interface1' )

# Creating now a: INTERFACE object
interface13 = exp.topNet.net0.router2.Interface( overlay=exp.topNet.net0.router0.interface0 , name='interface2' )

# Creating now a: NET object
net14 = exp.topNet.Net( overlay=None , name='net1' )
net14.routing.ShortestPath()

# Creating now a: ROUTER object
router15 = exp.topNet.net1.Router( overlay=None , name='router0' )
router15.ICMPv4Session()
router15.IPv4Session()

# Creating now a: INTERFACE object
interface16 = exp.topNet.net1.router0.Interface( overlay=exp.topNet.net0.router0.interface0 , name='interface0' )

# Creating now a: INTERFACE object
interface17 = exp.topNet.net1.router0.Interface( overlay=exp.topNet.net0.router0.interface0 , name='interface1' )

# Creating now a: INTERFACE object
interface18 = exp.topNet.net1.router0.Interface( overlay=exp.topNet.net0.router0.interface0 , name='interface2' )

# Creating now a: INTERFACE object
interface19 = exp.topNet.net1.router0.Interface( overlay=exp.topNet.net0.router0.interface0 , name='interface3' )

# Creating now a: ROUTER object
router20 = exp.topNet.net1.Router( overlay=None , name='router1' )
router20.ICMPv4Session()
router20.IPv4Session()

# Creating now a: INTERFACE object
interface21 = exp.topNet.net1.router1.Interface( overlay=exp.topNet.net0.router0.interface0 , name='interface0' )

# Creating now a: INTERFACE object
interface22 = exp.topNet.net1.router1.Interface( overlay=exp.topNet.net0.router0.interface0 , name='interface1' )

# Creating now a: INTERFACE object
interface23 = exp.topNet.net1.router1.Interface( overlay=exp.topNet.net0.router0.interface0 , name='interface2' )

# Creating now a: HOST object
host24 = exp.topNet.net1.Host( overlay=None , name='host2' )
host24.ICMPv4Session()
host24.IPv4Session()
host24.TCPMaster()

# Creating now a: INTERFACE object
interface25 = exp.topNet.net1.host2.Interface( overlay=exp.topNet.net0.router0.interface0 , name='interface0' )

# Creating now a: HOST object
host26 = exp.topNet.net1.Host( overlay=exp.topNet.net1.host2 , name='host3' )

# Creating now a: HOST object
host27 = exp.topNet.net1.Host( overlay=exp.topNet.net1.host2 , name='host4' )

# Creating now a: HOST object
host28 = exp.topNet.net1.Host( overlay=exp.topNet.net1.host2 , name='host5' )

# Creating now a: NET object
net29 = exp.topNet.Net( overlay=None , name='net2' )
net29.routing.ShortestPath()

# Creating now a: ROUTER object
router30 = exp.topNet.net2.Router( overlay=None , name='router0' )
router30.ICMPv4Session()
router30.IPv4Session()

# Creating now a: INTERFACE object
interface31 = exp.topNet.net2.router0.Interface( overlay=None , name='interface0' , bit_rate=100000000 , latency=0.0001 )
interface31.DropTailQueue()

# Creating now a: INTERFACE object
interface32 = exp.topNet.net2.router0.Interface( overlay=exp.topNet.net2.router0.interface0 , name='interface1' )

# Creating now a: INTERFACE object
interface33 = exp.topNet.net2.router0.Interface( overlay=exp.topNet.net2.router0.interface0 , name='interface2' )

# Creating now a: ROUTER object
router34 = exp.topNet.net2.Router( overlay=None , name='router1' )
router34.ICMPv4Session()
router34.IPv4Session()

# Creating now a: INTERFACE object
interface35 = exp.topNet.net2.router1.Interface( overlay=exp.topNet.net2.router0.interface0 , name='interface0' )

# Creating now a: INTERFACE object
interface36 = exp.topNet.net2.router1.Interface( overlay=exp.topNet.net2.router0.interface0 , name='interface1' )

# Creating now a: INTERFACE object
interface37 = exp.topNet.net2.router1.Interface( overlay=exp.topNet.net2.router0.interface0 , name='interface2' )

# Creating now a: ROUTER object
router38 = exp.topNet.net2.Router( overlay=None , name='router2' )
router38.ICMPv4Session()
router38.IPv4Session()

# Creating now a: INTERFACE object
interface39 = exp.topNet.net2.router2.Interface( overlay=exp.topNet.net2.router0.interface0 , name='interface0' )

# Creating now a: INTERFACE object
interface40 = exp.topNet.net2.router2.Interface( overlay=exp.topNet.net2.router0.interface0 , name='interface1' )

# Creating now a: INTERFACE object
interface41 = exp.topNet.net2.router2.Interface( overlay=exp.topNet.net2.router0.interface0 , name='interface2' )

# Creating now a: INTERFACE object
interface42 = exp.topNet.net2.router2.Interface( overlay=None , name='interface3' , bit_rate=10000000 , latency=0.001 )
interface42.DropTailQueue()

# Creating now a: ROUTER object
router43 = exp.topNet.net2.Router( overlay=None , name='router3' )
router43.ICMPv4Session()
router43.IPv4Session()

# Creating now a: INTERFACE object
interface44 = exp.topNet.net2.router3.Interface( overlay=exp.topNet.net2.router0.interface0 , name='interface0' )

# Creating now a: INTERFACE object
interface45 = exp.topNet.net2.router3.Interface( overlay=exp.topNet.net2.router0.interface0 , name='interface1' )

# Creating now a: INTERFACE object
interface46 = exp.topNet.net2.router3.Interface( overlay=exp.topNet.net2.router0.interface0 , name='interface2' )

# Creating now a: INTERFACE object
interface47 = exp.topNet.net2.router3.Interface( overlay=exp.topNet.net2.router0.interface0 , name='interface3' )

# Creating now a: ROUTER object
router48 = exp.topNet.net2.Router( overlay=None , name='router4' )
router48.ICMPv4Session()
router48.IPv4Session()

# Creating now a: INTERFACE object
interface49 = exp.topNet.net2.router4.Interface( overlay=exp.topNet.net2.router0.interface0 , name='interface0' )

# Creating now a: INTERFACE object
interface50 = exp.topNet.net2.router4.Interface( overlay=exp.topNet.net2.router2.interface3 , name='interface1' )

# Creating now a: ROUTER object
router51 = exp.topNet.net2.Router( overlay=None , name='router5' )
router51.ICMPv4Session()
router51.IPv4Session()

# Creating now a: INTERFACE object
interface52 = exp.topNet.net2.router5.Interface( overlay=exp.topNet.net2.router0.interface0 , name='interface0' )

# Creating now a: INTERFACE object
interface53 = exp.topNet.net2.router5.Interface( overlay=exp.topNet.net2.router2.interface3 , name='interface1' )

# Creating now a: INTERFACE object
interface54 = exp.topNet.net2.router5.Interface( overlay=exp.topNet.net2.router0.interface0 , name='interface2' )

# Creating now a: ROUTER object
router55 = exp.topNet.net2.Router( overlay=None , name='router6' )
router55.ICMPv4Session()
router55.IPv4Session()

# Creating now a: INTERFACE object
interface56 = exp.topNet.net2.router6.Interface( overlay=exp.topNet.net2.router0.interface0 , name='interface0' )

# Creating now a: INTERFACE object
interface57 = exp.topNet.net2.router6.Interface( overlay=exp.topNet.net2.router2.interface3 , name='interface1' )

# Creating now a: INTERFACE object
interface58 = exp.topNet.net2.router6.Interface( overlay=exp.topNet.net2.router2.interface3 , name='interface2' )

# Creating now a: INTERFACE object
interface59 = exp.topNet.net2.router6.Interface( overlay=exp.topNet.net2.router2.interface3 , name='interface3' )

# Creating now a: HOST object
host60 = exp.topNet.net2.Host( overlay=None , name='host7' )
host60.ICMPv4Session()
host60.IPv4Session()
host60.TCPMaster()

# Creating now a: INTERFACE object
interface61 = exp.topNet.net2.host7.Interface( overlay=exp.topNet.net2.router2.interface3 , name='interface0' )

# Creating now a: HOST object
host62 = exp.topNet.net2.Host( overlay=exp.topNet.net2.host7 , name='host8' )

# Creating now a: HOST object
host63 = exp.topNet.net2.Host( overlay=exp.topNet.net2.host7 , name='host9' )

# Creating now a: HOST object
host64 = exp.topNet.net2.Host( overlay=exp.topNet.net2.host7 , name='host10' )

# Creating now a: HOST object
host65 = exp.topNet.net2.Host( overlay=exp.topNet.net2.host7 , name='host11' )

# Creating now a: HOST object
host66 = exp.topNet.net2.Host( overlay=exp.topNet.net2.host7 , name='host12' )

# Creating now a: HOST object
host67 = exp.topNet.net2.Host( overlay=exp.topNet.net2.host7 , name='host13' )

# Creating now a: HOST object
host68 = exp.topNet.net2.Host( overlay=exp.topNet.net2.host7 , name='host14' )

# Creating now a: HOST object
host69 = exp.topNet.net2.Host( overlay=exp.topNet.net2.host7 , name='host15' )

# Creating now a: HOST object
host70 = exp.topNet.net2.Host( overlay=exp.topNet.net2.host7 , name='host16' )

# Creating now a: HOST object
host71 = exp.topNet.net2.Host( overlay=exp.topNet.net2.host7 , name='host17' )

# Creating now a: HOST object
host72 = exp.topNet.net2.Host( overlay=exp.topNet.net2.host7 , name='host18' )

# Creating now a: HOST object
host73 = exp.topNet.net2.Host( overlay=exp.topNet.net2.host7 , name='host19' )

# Creating now a: HOST object
host74 = exp.topNet.net2.Host( overlay=exp.topNet.net2.host7 , name='host20' )

# Creating now a: NET object
net75 = exp.topNet.Net( overlay=None , name='net3' )
net75.routing.ShortestPath()

# Creating now a: ROUTER object
router76 = exp.topNet.net3.Router( overlay=None , name='router0' )
router76.ICMPv4Session()
router76.IPv4Session()

# Creating now a: INTERFACE object
interface77 = exp.topNet.net3.router0.Interface( overlay=exp.topNet.net2.router0.interface0 , name='interface0' )

# Creating now a: INTERFACE object
interface78 = exp.topNet.net3.router0.Interface( overlay=exp.topNet.net2.router0.interface0 , name='interface1' )

# Creating now a: INTERFACE object
interface79 = exp.topNet.net3.router0.Interface( overlay=exp.topNet.net2.router2.interface3 , name='interface2' )

# Creating now a: INTERFACE object
interface80 = exp.topNet.net3.router0.Interface( overlay=exp.topNet.net2.router2.interface3 , name='interface3' )

# Creating now a: ROUTER object
router81 = exp.topNet.net3.Router( overlay=None , name='router1' )
router81.ICMPv4Session()
router81.IPv4Session()

# Creating now a: INTERFACE object
interface82 = exp.topNet.net3.router1.Interface( overlay=exp.topNet.net2.router0.interface0 , name='interface0' )

# Creating now a: INTERFACE object
interface83 = exp.topNet.net3.router1.Interface( overlay=exp.topNet.net2.router0.interface0 , name='interface1' )

# Creating now a: INTERFACE object
interface84 = exp.topNet.net3.router1.Interface( overlay=exp.topNet.net2.router0.interface0 , name='interface2' )

# Creating now a: INTERFACE object
interface85 = exp.topNet.net3.router1.Interface( overlay=exp.topNet.net2.router0.interface0 , name='interface3' )

# Creating now a: ROUTER object
router86 = exp.topNet.net3.Router( overlay=None , name='router2' )
router86.ICMPv4Session()
router86.IPv4Session()

# Creating now a: INTERFACE object
interface87 = exp.topNet.net3.router2.Interface( overlay=exp.topNet.net2.router0.interface0 , name='interface0' )

# Creating now a: INTERFACE object
interface88 = exp.topNet.net3.router2.Interface( overlay=exp.topNet.net2.router0.interface0 , name='interface1' )

# Creating now a: INTERFACE object
interface89 = exp.topNet.net3.router2.Interface( overlay=exp.topNet.net2.router2.interface3 , name='interface2' )

# Creating now a: ROUTER object
router90 = exp.topNet.net3.Router( overlay=None , name='router3' )
router90.ICMPv4Session()
router90.IPv4Session()

# Creating now a: INTERFACE object
interface91 = exp.topNet.net3.router3.Interface( overlay=exp.topNet.net2.router0.interface0 , name='interface0' )

# Creating now a: INTERFACE object
interface92 = exp.topNet.net3.router3.Interface( overlay=exp.topNet.net2.router2.interface3 , name='interface1' )

# Creating now a: INTERFACE object
interface93 = exp.topNet.net3.router3.Interface( overlay=exp.topNet.net2.router2.interface3 , name='interface2' )

# Creating now a: INTERFACE object
interface94 = exp.topNet.net3.router3.Interface( overlay=exp.topNet.net2.router0.interface0 , name='interface3' )

# Creating now a: HOST object
host95 = exp.topNet.net3.Host( overlay=exp.topNet.net2.host7 , name='host4' )

# Creating now a: HOST object
host96 = exp.topNet.net3.Host( overlay=exp.topNet.net2.host7 , name='host5' )

# Creating now a: HOST object
host97 = exp.topNet.net3.Host( overlay=exp.topNet.net2.host7 , name='host6' )

# Creating now a: HOST object
host98 = exp.topNet.net3.Host( overlay=exp.topNet.net2.host7 , name='host7' )

# Creating now a: HOST object
host99 = exp.topNet.net3.Host( overlay=exp.topNet.net2.host7 , name='host8' )

# Creating now a: HOST object
host100 = exp.topNet.net3.Host( overlay=exp.topNet.net2.host7 , name='host9' )

# Creating now a: HOST object
host101 = exp.topNet.net3.Host( overlay=exp.topNet.net2.host7 , name='host10' )

# Creating now a: HOST object
host102 = exp.topNet.net3.Host( overlay=exp.topNet.net2.host7 , name='host11' )

# Creating now a: HOST object
host103 = exp.topNet.net3.Host( overlay=exp.topNet.net2.host7 , name='host12' )

# Creating now a: HOST object
host104 = exp.topNet.net3.Host( overlay=exp.topNet.net2.host7 , name='host13' )

# Creating now a: ROUTER object
router105 = exp.topNet.Router( overlay=None , name='router4' )
router105.ICMPv4Session()
router105.IPv4Session()

# Creating now a: INTERFACE object
interface106 = exp.topNet.router4.Interface( overlay=exp.topNet.net0.router0.interface0 , name='interface0' )

# Creating now a: INTERFACE object
interface107 = exp.topNet.router4.Interface( overlay=exp.topNet.net0.router0.interface0 , name='interface1' )

# Creating now a: INTERFACE object
interface108 = exp.topNet.router4.Interface( overlay=exp.topNet.net2.router0.interface0 , name='interface2' )

# Creating now a: INTERFACE object
interface109 = exp.topNet.router4.Interface( overlay=exp.topNet.net2.router0.interface0 , name='interface3' )

# Creating now a: ROUTER object
router110 = exp.topNet.Router( overlay=None , name='router5' )
router110.ICMPv4Session()
router110.IPv4Session()

# Creating now a: INTERFACE object
interface111 = exp.topNet.router5.Interface( overlay=exp.topNet.net0.router0.interface0 , name='interface0' )

# Creating now a: INTERFACE object
interface112 = exp.topNet.router5.Interface( overlay=exp.topNet.net2.router0.interface0 , name='interface1' )

# Creating now a: INTERFACE object
interface113 = exp.topNet.router5.Interface( overlay=exp.topNet.net2.router0.interface0 , name='interface2' )

# Creating now a: INTERFACE object
interface114 = exp.topNet.router5.Interface( overlay=exp.topNet.net0.router0.interface0 , name='interface3' )

# Creating now a: LINK object
exp.topNet.net0.Link(attachments=[ exp.topNet.net0.router0.interface1 , exp.topNet.net0.router1.interface2 ], overlay=None , delay=0.01 )

# Creating now a: LINK object
exp.topNet.net0.Link(attachments=[ exp.topNet.net0.router1.interface0 , exp.topNet.net0.router2.interface1 ], overlay=None , delay=0.01 )

# Creating now a: LINK object
exp.topNet.net0.Link(attachments=[ exp.topNet.net0.router0.interface0 , exp.topNet.net0.router2.interface2 ], overlay=None , delay=0.01 )

# Creating now a: LINK object
exp.topNet.net1.Link(attachments=[ exp.topNet.net1.router0.interface2 , exp.topNet.net1.router1.interface0 ], overlay=None , delay=0.001 )

# Creating now a: LINK object
exp.topNet.net1.Link(attachments=[ exp.topNet.net1.router0.interface0 , exp.topNet.net1.host2.interface0 ], overlay=None , delay=0.001 )

# Creating now a: LINK object
exp.topNet.net1.Link(attachments=[ exp.topNet.net1.router0.interface1 , exp.topNet.net1.host3.interface0 ], overlay=None , delay=0.001 )

# Creating now a: LINK object
exp.topNet.net1.Link(attachments=[ exp.topNet.net1.router1.interface1 , exp.topNet.net1.host4.interface0 ], overlay=None , delay=0.001 )

# Creating now a: LINK object
exp.topNet.net1.Link(attachments=[ exp.topNet.net1.router1.interface2 , exp.topNet.net1.host5.interface0 ], overlay=None , delay=0.001 )

# Creating now a: LINK object
exp.topNet.net2.Link(attachments=[ exp.topNet.net2.router0.interface1 , exp.topNet.net2.router1.interface2 ], overlay=None , delay=0.001 )

# Creating now a: LINK object
exp.topNet.net2.Link(attachments=[ exp.topNet.net2.router0.interface2 , exp.topNet.net2.router2.interface0 ], overlay=None , delay=0.001 )

# Creating now a: LINK object
exp.topNet.net2.Link(attachments=[ exp.topNet.net2.router2.interface1 , exp.topNet.net2.router3.interface3 ], overlay=None , delay=0.01 )

# Creating now a: LINK object
exp.topNet.net2.Link(attachments=[ exp.topNet.net2.router2.interface2 , exp.topNet.net2.router4.interface0 ], overlay=None , delay=0.005 )

# Creating now a: LINK object
exp.topNet.net2.Link(attachments=[ exp.topNet.net2.router2.interface3 , exp.topNet.net2.host7.interface0 , exp.topNet.net2.host8.interface0 ], overlay=None , delay=0.003 )

# Creating now a: LINK object
exp.topNet.net2.Link(attachments=[ exp.topNet.net2.router4.interface1 , exp.topNet.net2.host9.interface0 , exp.topNet.net2.host10.interface0 ], overlay=None , delay=0.001 )

# Creating now a: LINK object
exp.topNet.net2.Link(attachments=[ exp.topNet.net2.router1.interface1 , exp.topNet.net2.router3.interface0 ], overlay=None , delay=0.001 )

# Creating now a: LINK object
exp.topNet.net2.Link(attachments=[ exp.topNet.net2.router3.interface1 , exp.topNet.net2.host11.interface0 , exp.topNet.net2.host12.interface0 ], overlay=None , delay=0.001 )

# Creating now a: LINK object
exp.topNet.net2.Link(attachments=[ exp.topNet.net2.router3.interface2 , exp.topNet.net2.router5.interface0 ], overlay=None , delay=0.001 )

# Creating now a: LINK object
exp.topNet.net2.Link(attachments=[ exp.topNet.net2.router5.interface1 , exp.topNet.net2.host13.interface0 , exp.topNet.net2.host14.interface0 ], overlay=None , delay=0.001 )

# Creating now a: LINK object
exp.topNet.net2.Link(attachments=[ exp.topNet.net2.router5.interface2 , exp.topNet.net2.router6.interface0 ], overlay=None , delay=0.001 )

# Creating now a: LINK object
exp.topNet.net2.Link(attachments=[ exp.topNet.net2.router6.interface3 , exp.topNet.net2.host15.interface0 , exp.topNet.net2.host16.interface0 ], overlay=None , delay=0.001 )

# Creating now a: LINK object
exp.topNet.net2.Link(attachments=[ exp.topNet.net2.router6.interface2 , exp.topNet.net2.host17.interface0 , exp.topNet.net2.host18.interface0 ], overlay=None , delay=0.001 )

# Creating now a: LINK object
exp.topNet.net2.Link(attachments=[ exp.topNet.net2.router6.interface1 , exp.topNet.net2.host19.interface0 , exp.topNet.net2.host20.interface0 ], overlay=None , delay=0.001 )

# Creating now a: LINK object
exp.topNet.net3.Link(attachments=[ exp.topNet.net3.router0.interface3 , exp.topNet.net3.host4.interface0 , exp.topNet.net3.host5.interface0 ], overlay=None , delay=0.001 )

# Creating now a: LINK object
exp.topNet.net3.Link(attachments=[ exp.topNet.net3.router0.interface2 , exp.topNet.net3.host6.interface0 , exp.topNet.net3.host7.interface0 ], overlay=None , delay=0.001 )

# Creating now a: LINK object
exp.topNet.net3.Link(attachments=[ exp.topNet.net3.router0.interface1 , exp.topNet.net3.router1.interface3 ], overlay=None , delay=0.01 )

# Creating now a: LINK object
exp.topNet.net3.Link(attachments=[ exp.topNet.net3.router1.interface2 , exp.topNet.net3.router2.interface0 ], overlay=None , delay=0.003 )

# Creating now a: LINK object
exp.topNet.net3.Link(attachments=[ exp.topNet.net3.router2.interface1 , exp.topNet.net3.router3.interface3 ], overlay=None , delay=0.003 )

# Creating now a: LINK object
exp.topNet.net3.Link(attachments=[ exp.topNet.net3.router1.interface1 , exp.topNet.net3.router3.interface0 ], overlay=None , delay=0.003 )

# Creating now a: LINK object
exp.topNet.net3.Link(attachments=[ exp.topNet.net3.router2.interface2 , exp.topNet.net3.host8.interface0 , exp.topNet.net3.host9.interface0 ], overlay=None , delay=0.001 )

# Creating now a: LINK object
exp.topNet.net3.Link(attachments=[ exp.topNet.net3.router3.interface2 , exp.topNet.net3.host10.interface0 , exp.topNet.net3.host11.interface0 ], overlay=None , delay=0.001 )

# Creating now a: LINK object
exp.topNet.net3.Link(attachments=[ exp.topNet.net3.router3.interface1 , exp.topNet.net3.host12.interface0 , exp.topNet.net3.host13.interface0 ], overlay=None , delay=0.001 )

# Creating now a: LINK object
exp.topNet.Link(attachments=[ exp.topNet.router4.interface1 , exp.topNet.router5.interface3 ], overlay=None , delay=0.005 )

# Creating now a: LINK object
exp.topNet.Link(attachments=[ exp.topNet.net0.router0.interface2 , exp.topNet.router4.interface0 ], overlay=None , delay=0.01 )

# Creating now a: LINK object
exp.topNet.Link(attachments=[ exp.topNet.net0.router1.interface1 , exp.topNet.router5.interface0 ], overlay=None , delay=0.01 )

# Creating now a: LINK object
exp.topNet.Link(attachments=[ exp.topNet.net0.router2.interface0 , exp.topNet.net1.router0.interface3 ], overlay=None , delay=0.01 )

# Creating now a: LINK object
exp.topNet.Link(attachments=[ exp.topNet.router4.interface2 , exp.topNet.net2.router1.interface0 ], overlay=None , delay=0.005 )

# Creating now a: LINK object
exp.topNet.Link(attachments=[ exp.topNet.router4.interface3 , exp.topNet.net2.router0.interface0 ], overlay=None , delay=0.005 )

# Creating now a: LINK object
exp.topNet.Link(attachments=[ exp.topNet.router5.interface2 , exp.topNet.net3.router0.interface0 ], overlay=None , delay=0.005 )

# Creating now a: LINK object
exp.topNet.Link(attachments=[ exp.topNet.router5.interface1 , exp.topNet.net3.router1.interface0 ], overlay=None , delay=0.005 )


#Warning: The node with key =  graphics  and parent  router  is not supported or the key is wrong
#Warning: The node with key =  graphics  and parent  router  is not supported or the key is wrong
#Warning: The node with key =  route  and parent  host  is not supported or the key is wrong
#Warning: The node with key =  route  and parent  host  is not supported or the key is wrong
#Warning: The node with key =  route  and parent  host  is not supported or the key is wrong
#Warning: The node with key =  route  and parent  host  is not supported or the key is wrong
#Warning: The node with key =  nhi_route  and parent  host  is not supported or the key is wrong
#Warning: The node with key =  nhi_route  and parent  host  is not supported or the key is wrong
#Warning: The node with key =  nhi_route  and parent  host  is not supported or the key is wrong
#Warning: The node with key =  nhi_route  and parent  host  is not supported or the key is wrong
#Warning: The node with key =  nhi_route  and parent  host  is not supported or the key is wrong
#Warning: The node with key =  nhi_route  and parent  host  is not supported or the key is wrong
#Warning: The node with key =  nhi_route  and parent  host  is not supported or the key is wrong
#Warning: The node with key =  nhi_route  and parent  host  is not supported or the key is wrong
#Warning: The node with key =  nhi_route  and parent  host  is not supported or the key is wrong
#Warning: The node with key =  nhi_route  and parent  host  is not supported or the key is wrong
#Warning: The node with key =  nhi_route  and parent  host  is not supported or the key is wrong
#Warning: The node with key =  nhi_route  and parent  host  is not supported or the key is wrong
#Warning: The node with key =  nhi_route  and parent  host  is not supported or the key is wrong
#Warning: The node with key =  nhi_route  and parent  host  is not supported or the key is wrong
#Warning: The node with key =  nhi_route  and parent  host  is not supported or the key is wrong
#Warning: The node with key =  nhi_route  and parent  host  is not supported or the key is wrong
#Warning: The node with key =  nhi_route  and parent  host  is not supported or the key is wrong
#Warning: The node with key =  nhi_route  and parent  host  is not supported or the key is wrong
#Warning: The node with key =  nhi_route  and parent  host  is not supported or the key is wrong
#Warning: The node with key =  nhi_route  and parent  host  is not supported or the key is wrong
#Warning: The node with key =  nhi_route  and parent  host  is not supported or the key is wrong
#Warning: The node with key =  nhi_route  and parent  host  is not supported or the key is wrong
#Warning: The node with key =  nhi_route  and parent  host  is not supported or the key is wrong
#Warning: The node with key =  nhi_route  and parent  host  is not supported or the key is wrong


exp.compile("14.12.0.0")
part_str="1::1:1"
partitioning=exp.partition(part_str)
xmlprinter=pyprime.visitors.XMLVisitor(printSchema=False)
xmlprinter.visit(exp)
v=pyprime.visitors.TlvVisitor(dirname=".",partitioning=partitioning)
v.encode()
