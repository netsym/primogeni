print "<!--"
import pyprime, sys

print "XXX: this exp is totally wacked....... nate needs to redo it!!"
sys.exit()

db = pyprime.Database()
exp=db.createExperiment(name="test overlays", deleteIfExists=True)

topnet=exp.Net(name="topnet")

super_network1=topnet.Net(name="super_network1")
#subnet 1 will have two subnets
n1=super_network1.Net(name="subnet1")

#create s1
s1=n1.Net(name="sub_sub1")
r1=s1.Router(name="r1")
r1.Interface(name="ex1")
r1.Interface(name="ex2")
for i in range(1,5):
    h=s1.Host(name="h%i"%(i))
    s1.Link(name="link_%i"%(i),attachments=[r1.Interface(name="if%i"%(i)),h.Interface(name="if0")])
    pass
s2=n1.Net(s1,name="sub_sub2")
r_1_2=n1.Router(name="r2")
for i in range(1,5):
    h=getattr(s2,"h%i"%(i))
    if0=h.if0
    n1.Link(name="link_h_%i"%(i),attachments=[r_1_2.Interface(name="if_h_%i"%(i)),if0])
    pass
n1.Link(name="link_1_2",attachments=[n1.sub_sub1.r1.ex1,r_1_2.Interface(name="if_s1")])
n1.Link(name="link_1_2",attachments=[n1.sub_sub2.r1.ex1,r_1_2.Interface(name="if_s2")])
r_1_2.Interface(name="external")

#copy subnet1
n2=super_network1.Net(n1,name="subnet2")
r_super.Router(name="super_router")
super_network1.Link(name="link_1_2",attachments=[n1.s1.super_router.external,r_super.Interface(name="if_super1")])
super_network1.Link(name="link_1_2",attachments=[n1.s1.super_router.external,r_super.Interface(name="if_super2")])

n1.Link(name="link_1_2",attachments=[n1.s1.r1.external,r_super.Interface(name="if_subnet1")])
n1.Link(name="link_1_2",attachments=[n1.s2.r1.external,r_1_2.Interface(name="if_s2")])
r_1_2.Interface(name="external")

super_network1.Link(name="link_1_2",attachments=[n1.s2.r_1_2.external,n2.s2.r_1_2.external])

for i in range(2,4):
    c=n1.Net(s1,name="s%i"%(i))
    for j in range(0,i):
        h=c.Host(name='host_%i_%i'%(i,j))
        e=h.Interface(name='if_%i_%i'%(i,j))
        s=c.r1.Interface(name='if_%i_%i'%(i,j))
        c.Link(name='link_%i_%i'%(i,j), attachments=[s,e])
        pass
    pass

n1.Link(name="l_%s_%s"%(n1.s1.name,n1.s2.name),attachments=[n1.s1.r1.nic2,n1.s2.r1.nic2])
n1.Link(name="l_%s_%s"%(n1.s2.name,n1.s3.name),attachments=[n1.s2.r1.nic3,n1.s3.r1.nic2])
n1.Link(name="l_%s_%s"%(n1.s3.name,n1.s1.name),attachments=[n1.s3.r1.nic3,n1.s1.r1.nic3])

lr=n1.Router(name="localrouter")
lr.Interface(name="if0")
lr.Interface(name="if1")
lr.Interface(name="if2")
lr.Interface(name="if3")
lr.Interface(name="if4")
lr.Interface(name="if5")
n1.Link(name='link_ACK',attachments=[lr.if0,s1.r1.Interface(name='iface_ACK')])

# copy n1 to n2
n2=super_network1.Net(n1,name="subnet2")
super_network1.Link(name="ACK",attachments=[n1.localrouter.if1,n2.localrouter.if1])

#super_network1.Alias(super_network1.subnet1.localrouter,name="router1")
#super_network1.Alias(super_network1.subnet2.localrouter,name="router2")

#copy super_network1 to super_network2
super_network2 = topnet.Net(super_network1,name="super_network2")


#topnet.Link(attachments=[super_network1.router1().if3,super_network2.router1().if3])
#topnet.Link(attachments=[super_network1.router2().if4,super_network2.router2().if4])


exp.run()#time=100, hosts=env.hosts[0:10])

print "-->"
print "<!--",20*'*',"-->"
print "<!--",20*'*',"-->"
xmlprinter=pyprime.visitors.XMLVisitor(printSchema=False)
xmlprinter.visit(exp)
#pyprime.visitors.CPPVisitor(root=exp.topnet)
print "<!--",20*'*',"-->"

# run the experiment for 100 seconds; by default, the results will be
# directed to stdout
exp.run(100)
print "-->"

print "<!--",20*'*',"-->"
print "<!--",20*'*',"-->"
