#get the ssf vars (which includes all of the env vars and user options)
ifneq ($(INCLUDED_INC), true)
include /Users/liux/Workspace/primogeni/netsim/Makefile.inc 
endif

EXTRACT_VPNCONF_EXE = /Users/liux/Workspace/primogeni/netsim/extract_vpnconf
VPNSCRIPT_PL = /Users/liux/Workspace/primogeni/netsim/vpnscript.pl -d /Users/liux/Workspace/primogeni/netsim/src/emuproxy/openvpn

JPRIME_JAR = /Users/liux/Workspace/primogeni/netsim/../netscript/dist/jprime.jar

JPRIME_DRIVER_CMD = java -classpath $(JPRIME_JAR) test.Driver -outdir $(CURDIR) create $(MODEL_NAME)

$(JPRIME_JAR):
	cd /Users/liux/Workspace/primogeni/netsim/../netscript && ant jar
