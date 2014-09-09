#get the ssf vars (which includes all of the env vars and user options)
ifneq ($(INCLUDED_INC), true)
include /home/obaida/Desktop/pgc2vega/primex/netsim/Makefile.inc 
endif

EXTRACT_VPNCONF_EXE = /home/obaida/Desktop/pgc2vega/primex/netsim/extract_vpnconf
VPNSCRIPT_PL = /home/obaida/Desktop/pgc2vega/primex/netsim/vpnscript.pl -d /home/obaida/Desktop/pgc2vega/primex/netsim/src/emuproxy/openvpn

JPRIME_JAR = /home/obaida/Desktop/pgc2vega/primex/netsim/../netscript/dist/jprime.jar

JPRIME_DRIVER_CMD = java -classpath $(JPRIME_JAR) test.Driver -outdir $(CURDIR) create $(MODEL_NAME)

$(JPRIME_JAR):
	cd /home/obaida/Desktop/pgc2vega/primex/netsim/../netscript && ant jar
