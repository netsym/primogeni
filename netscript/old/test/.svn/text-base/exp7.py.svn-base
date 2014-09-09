#generated from luis
import pyprime
db=pyprime.Database()
exp = db.createExperiment( name = 'Example7' )
n = exp.Net(name='topNet' )

# Creating now a: NET object
net0 = exp.topNet.Net( overlay=None , name='net0' , alignment='a0' )

# Creating now a: HOST object
host1 = exp.topNet.net0.Host( overlay=None , name='host0' )

# Creating now a: INTERFACE object
interface2 = exp.topNet.net0.host0.Interface( overlay=None , latency=0 , name='interface1' )

# Creating now a: INTERFACE object
interface3 = exp.topNet.net0.host0.Interface( overlay=exp.topNet.net0.host0.interface1 , name='interface2' )

# Creating now a: NET object
net4 = exp.topNet.net0.Net( overlay=None , name='net1' , alignment='a1' )

# Creating now a: NET object
net5 = exp.topNet.net0.net1.Net( overlay=None , name='net2' , alignment='a2' )

# Creating now a: ROUTER object
router6 = exp.topNet.net0.net1.net2.Router( overlay=None , name='router1' )

# Creating now a: INTERFACE object
interface7 = exp.topNet.net0.net1.net2.router1.Interface( overlay=None , name='interface0' , latency=1 )

# Creating now a: HOST object
host8 = exp.topNet.net0.net1.Host( overlay=None , name='host0' )

# Creating now a: INTERFACE object
interface9 = exp.topNet.net0.net1.host0.Interface( overlay=exp.topNet.net0.host0.interface1 , name='interface1' )

# Creating now a: INTERFACE object
interface10 = exp.topNet.net0.net1.host0.Interface( overlay=exp.topNet.net0.host0.interface1 , name='interface2' )

# Creating now a: NET object
net11 = exp.topNet.Net( overlay=exp.topNet.net0.net1 , name='net3' )

# Creating now a: LINK object
exp.topNet.Link(attachments=[ exp.topNet.net0.host0.interface1 , exp.topNet.net0.net1.net2.router1.interface0 ], overlay=None , delay=0.2 )

exp.compile()

xmlprinter=pyprime.visitors.XMLVisitor(printSchema=False)
xmlprinter.visit(exp)
