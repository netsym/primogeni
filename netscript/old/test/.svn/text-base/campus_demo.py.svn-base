import pyprime
db = pyprime.Database()
campusNet = db.loadLibrary(name = "Campus Network")
exp=db.createExperiment(name="campus_demo", deleteIfExists=True)
topnet=exp.Net(campusNet,name="topnet")

exp.compile()

print "<!--",20*'*',"-->"
xmlprinter=pyprime.visitors.XMLVisitor(printSchema=True)
xmlprinter.visit(exp)
print "<!--",20*'*',"-->"
