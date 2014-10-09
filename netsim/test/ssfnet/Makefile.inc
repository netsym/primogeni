#get the ssf vars (which includes all of the env vars and user options)
ifneq ($(INCLUDED_INC), true)
include /home/liux/primogeni/netsim/Makefile.inc 
endif

EXTRACT_VPNCONF_EXE = /home/liux/primogeni/netsim/extract_vpnconf
VPNSCRIPT_PL = /home/liux/primogeni/netsim/vpnscript.pl -d /home/liux/primogeni/netsim/src/emuproxy/openvpn

JPRIME_JAR = /home/liux/primogeni/netsim/../netscript/dist/jprime.jar

JPRIME_DRIVER_CMD = java -classpath $(JPRIME_JAR) test.Driver -outdir $(CURDIR) create $(MODEL_NAME)

$(JPRIME_JAR):
	cd /home/liux/primogeni/netsim/../netscript && ant jar
