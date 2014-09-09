print "<!--"
# load the prime module; the scripting language is implemented as a
# python module
import pyprime

db = pyprime.Database()

exp=db.loadExperiment(name="basic example 1: 3-host ping")

#exp.run(100)
print "-->"

print "<!--",20*'*',"-->"
print "<!--",20*'*',"-->"

xmlprinter=pyprime.visitors.XMLVisitor(printSchema=False)
xmlprinter.visit(exp)

