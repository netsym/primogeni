import sys, csv

nodemap={}
type2str={}
nodes=[]
phases=[]
levels={}
accesstypes={}
class access(object):
    __slots__ = ['accessType','time','order','nt','dbid','pid']
    def __init__(self,accessType,time,order,nt,dbid,pid):
        self.accessType=accessType
        self.nt=nt
        self.time=time
        self.order=order
        self.dbid=dbid
        self.pid=pid
        pass
    def p(self,f):
        t=self.nt
        if len(type2str)>0 and type2str.has_key(self.nt):
            type2str[self.nt]
        f.write("%s, %s, %s, %s, %s, %s, %s\n"%(self.time, self.order, self.accessType,t,nodemap[self.dbid][1],nodemap[self.dbid][2],self.dbid))
        pass
    pass
class phase(object):
    __slots__ = ['name','time','order']
    def __init__(self,name,time,order):
        self.name=name
        self.time=time
        self.order=order
        pass
    def p(self,f):
        f.write("%s, %s, %s\n"%(self.time,self.order,self.name))
        pass
    pass


def getDepth(a):
    d=0
    n = a
    while True:
        try:
            n1=nodemap[n][0]
            if n1==n: break
            n=n1
            d+=1
            pass
        except KeyError:
            break
        pass
    return d   


def makeGnuPlot(out,t, max_time, max_rank, lvls, doOrder):
    if doOrder: f=open("%s.%s.order.gnuplot"%(out,t),"w")
    else: f=open("%s.%s.time.gnuplot"%(out,t),"w")

    f.write("set terminal png size 1200, 800\n")
    if doOrder: f.write("set output \"%s_%s_order.png\"\n"%(out,t))
    else: f.write("set output \"%s_%s_time.png\"\n"%(out,t))
    f.write("set multiplot\n")
    f.write("unset key\n")
    for l in lvls:
        f.write("fx%s(x)=%s\n"%(l[0],l[1]))
        pass
    f.write("set xrange [0:%s]\n"%(max_time))
    f.write("set yrange [0:%s]\n"%(max_rank))
    f.write("set trange [0:%s]\n"%(max_rank))
    if doOrder: f.write("\nplot\t\"%s.%s\" u 2:6,\\\n"%(out,t))
    else: f.write("\nplot\t\"%s.%s\" u 1:6,\\\n"%(out,t))
    first=True
    for l in lvls:
        if not first:
            f.write(",\\\n")
            pass
        first=False
        f.write("\tfx%s(x) w l lt 2 lw 2"%(l[0]))
        pass
    f.write("\n\nset parametric\n")
    f.write("\nplot ")
    first=True
    for p in phases:
        if not first:
            f.write(",\\\n")
            pass
        first=False
        if doOrder: f.write("\t%s,t w l lt 3 lw 2"%(p.order))
        else:  f.write("\t%s,t w l lt 3 lw 2"%(p.time))
        pass
    f.write("\n\n")
    f.close()
    pass

def parseFile(f,out):
    order=0
    basetime=None
    print "Reading CSV"
    log = csv.reader(open(f, 'rb'), delimiter=',', quotechar='|')
    for row in log:
        if row[0] == "A" or row[0] == "L" or row[0] == "F":
            accesstypes[row[0]]=None
            if basetime is None: basetime=long(row[1])
            a=access(row[0],long(row[1])-basetime,order,long(row[2]),long(row[3]),long(row[4]))
            order+=1
            nodes.append(a)
            if a.pid == -1: a.pid=0
            if nodemap.has_key(a.dbid):
                t=nodemap[a.dbid]
                if t[0] == 0:
                    nodemap[a.dbid]=[a.pid,None]
                    pass
                elif a.pid == 0:
                    a.pid=t[0]
                    pass
                elif t[0] != a.pid:
                    print "parent id mis-match for %s, pid1=%s, pid2=%s, row=%s"%(a.dbid, a.pid, t, ",".join(row))
                    pass
                pass
            else:
                nodemap[a.dbid]=[a.pid,None]
                pass
            pass
        elif row[0] == "P":
            if basetime is None: basetime=long(row[1])
            phases.append(phase(row[2],long(row[1])-basetime,order))
            pass
        elif row[0] == "T":
            type2str[long(row[1])]=row[2]
            pass
        else:
            print "Skipping row: ",",".join(row)
            pass
        pass
    print "calculating depth"
    for k in nodemap.keys():
        d=getDepth(k)
        nodemap[k][1]=d
        if not levels.has_key(d): levels[d]=set()
        levels[d].add(k)
        pass
    max_rank=0

    print "calculating ranks"
    log=open("%s.rank"%(out),"w")
    log.write("#dbid, rank\n")
    lvls=[]
    for k1 in sorted(levels.keys()):
        lvls.append((k1,max_rank))
        for k2 in sorted(levels[k1]):
            nodemap[k2].append(max_rank)
            log.write("%s, %s\n"%(k2,max_rank))
            max_rank+=1
            pass
        pass
    log.close()

    print "writing levels,ranks,phases"

    log=open("%s.levels"%(out),"w")
    log.write("#start rank, level\n")
    for k in lvls:
        log.write("%s,%s\n"%(k[0],k[1]))
        pass
    log.close()
    
    log=open("%s.phase"%(out),"w")
    log.write("#time, phase starting\n")
    for p in phases:
        p.p(log)
    log.close()

    print "large data files"
    
    for a in accesstypes.keys():
        accesstypes[a]=open("%s.%s"%(out,a),"w")
        accesstypes[a].write("#time, accessType, node type, depth, rank, dbid\n")
        pass
    for n in nodes:
        n.p(accesstypes[n.accessType])
        pass
    pass
    for a in accesstypes.keys():
        accesstypes[a].close()
        makeGnuPlot(out,a, nodes[-1].order, max_rank, lvls,True)
        makeGnuPlot(out,a, nodes[-1].time, max_rank, lvls,False)
        pass

parseFile(sys.argv[1],sys.argv[2])

