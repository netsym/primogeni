import sys

def process_hops(fin,fout):
	fin=open(fin,"r")
	fout=open(fout,"w")
	for l in fin.readlines():
		l=l.strip().replace("]","").replace("{","").replace("}","").replace(" ","").split("[")
		l1=l[0].split(",")
		uid=l1[0]
		hops=l1[1]
		fout.write("[%s,%s](%s)\n"%(uid,hops,",".join(map(lambda l: l.split(",")[3], l[1:]))))
		pass
	fin.close()
	fout.close()
	pass
	
process_hops(sys.argv[1],sys.argv[2])