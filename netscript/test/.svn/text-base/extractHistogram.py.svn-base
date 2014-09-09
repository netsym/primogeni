import sys, csv

nodes={}
class Node(object):
    __slots__ = ['nodeType','count', 'sizes', 'ids']
    def __init__(self,nodeType):
    	self.ids = set()
        self.nodeType=nodeType
        self.count=0
        self.sizes={}
        pass
    pass
    
def parseFile(f,out):
    print "Reading CSV"
    log = csv.reader(open(f, 'rb'), delimiter=',', quotechar='|')
    for row in log:
        if row[0] == "A" or row[0] == "L" or row[0] == "F":
            #<access type>, <time>, <node type>, <dbid>, <pid>, <size>
            ntype = long(row[2])
            nid = long(row[3])
            size = long(row[5])
            n = nodes.get(ntype)
            if n is None:
            	n = Node(ntype)
            	nodes[n.nodeType]=n
            	pass
            if nid not in n.ids:
            	n.ids.add(nid)
                if n.sizes.has_key(size):
                    n.sizes[size]+=1
                    pass
                else:
                    n.sizes[size]=1
                    pass
                n.count+=1
            	pass
        pass
    print "writing hist"
    log=open("%s.hist"%(out),"w")
    log.write("#type size count\n")
    for n in nodes.values():
        for s,c in n.sizes.iteritems():
            log.write("%s %s %s\n"%(n.nodeType, s, c))
            pass
    	pass
    log.close()
    pass
parseFile(sys.argv[1],sys.argv[2])

