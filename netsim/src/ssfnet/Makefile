#get all of the env vars and user options
ifneq ($(INCLUDED_INC), true)
include /Users/liux/Workspace/primogeni/netsim/Makefile.inc 
endif

# get the rules to build the metis code
ifneq ($(INCLUDED_METIS), true)
include /Users/liux/Workspace/primogeni/netsim/tools/metis/Makefile
endif

# get the rules to build the dml code
ifneq ($(INCLUDED_DML), true)
include /Users/liux/Workspace/primogeni/netsim/src/dml/Makefile 
endif

# get the rules to build the rng code
ifneq ($(INCLUDED_RNG), true)
include /Users/liux/Workspace/primogeni/netsim/src/rng/Makefile 
endif

# get the rules to build the ssf code
ifneq ($(INCLUDED_SSF), true)
include /Users/liux/Workspace/primogeni/netsim/src/ssf/Makefile 
endif 

SSFNET_PATH = /Users/liux/Workspace/primogeni/netsim/src/ssfnet
PCAP_PATH = /Users/liux/Workspace/primogeni/netsim/src/libpcap
JPRIME_SRC = /Users/liux/Workspace/primogeni/netsim/../netscript/src/jprime
JPRIME_GEN = /Users/liux/Workspace/primogeni/netsim/../netscript/src/jprime/gen/*.java
SSFNET_XLATE_PATH = /Users/liux/Workspace/primogeni/netsim/tools/ssfnet-xlate

ssfnet-doc: /Users/liux/Workspace/primogeni/netsim/../doc/doxy_predef.cfg /Users/liux/Workspace/primogeni/netsim/../doc/ssfnet.doxyfile
	$(DOXYGEN) /Users/liux/Workspace/primogeni/netsim/../doc/ssfnet.doxyfile

SSFNET_XLATE_SRCFILES = \
	$(SSFNET_XLATE_PATH)/code_generator.cc \
	$(SSFNET_XLATE_PATH)/generator.l \
	$(SSFNET_XLATE_PATH)/generator.y \
	$(SSFNET_XLATE_PATH)/pre-generator_lexer.c \
	$(SSFNET_XLATE_PATH)/pre-generator_parser.cc \
	$(SSFNET_XLATE_PATH)/extract.l \
	$(SSFNET_XLATE_PATH)/pre-extract.c

SSFNET_XLATE_BUILTFILES = \
	$(SSFNET_XLATE_PATH)/extract.c \
	$(SSFNET_XLATE_PATH)/generator_parser.h \
	$(SSFNET_XLATE_PATH)/generator_parser.cc \
	$(SSFNET_XLATE_PATH)/generator_lexer.c

SSFNET_CODEGEN_OBJFILES = \
	$(SSFNET_XLATE_PATH)/generator_parser.ssfnet.oo \
	$(SSFNET_XLATE_PATH)/generator_lexer.ssfnet.o \
	$(SSFNET_XLATE_PATH)/code_generator.ssfnet.oo

SSFNET_XLATE_OBJFILES = \
	$(SSFNET_CODEGEN_OBJFILES) \
	$(SSFNET_XLATE_PATH)/extract.ssfnet.o

SSFNET_EXTRACT = $(SSFNET_XLATE_PATH)/extract$(EXEEXT)

SSFNET_CODEGEN = $(SSFNET_XLATE_PATH)/code_gen$(EXEEXT)

SSFNET_XLATE_CLEANUP = \
	$(SSFNET_XLATE_BUILTFILES) \
	$(SSFNET_XLATE_OBJFILES) \
	$(SSFNET_EXTRACT) \
	$(SSFNET_CODEGEN) \
	lex.yy.c \
	generator_parser.h


ifeq ("x$(HAVE_BISON)", "xyes")
$(SSFNET_XLATE_PATH)/generator_parser.h $(SSFNET_XLATE_PATH)/generator_parser.cc:	$(SSFNET_XLATE_PATH)/generator.y
	$(YACC) $(YFLAGS) $<
	$(MV) y.tab.c $(SSFNET_XLATE_PATH)/generator_parser.cc
	$(MV) y.tab.h $(SSFNET_XLATE_PATH)/generator_parser.h
	$(CP) $(SSFNET_XLATE_PATH)/generator_parser.cc $(SSFNET_XLATE_PATH)/pre-generator_parser.cc
	$(CP) $(SSFNET_XLATE_PATH)/generator_parser.h $(SSFNET_XLATE_PATH)/pre-generator_parser.h
else
$(SSFNET_XLATE_PATH)/generator_parser.h $(SSFNET_XLATE_PATH)/generator_parser.cc:	$(SSFNET_XLATE_PATH)/pre-generator_parser.h $(SSFNET_XLATE_PATH)/pre-generator_parser.cc
	$(CP) $(SSFNET_XLATE_PATH)/pre-generator_parser.h $(SSFNET_XLATE_PATH)/generator_parser.h
	$(CP) $(SSFNET_XLATE_PATH)/pre-generator_parser.cc $(SSFNET_XLATE_PATH)/generator_parser.cc
endif

ifeq ("x$(HAVE_FLEX)", "xyes")
$(SSFNET_XLATE_PATH)/extract.c:	$(SSFNET_XLATE_PATH)/extract.l
	$(LEX) $(LFLAGS) $<
	$(MV) lex.yy.c $@
	$(CP) $@ $(SSFNET_XLATE_PATH)/pre-extract.c
$(SSFNET_XLATE_PATH)/generator_lexer.c:	$(SSFNET_XLATE_PATH)/generator.l
	$(LEX) $(LFLAGS) $<
	$(MV) lex.yy.c $@
	$(CP) $@ $(SSFNET_XLATE_PATH)/pre-generator_lexer.c
else
$(SSFNET_XLATE_PATH)/extract.c:	$(SSFNET_XLATE_PATH)/pre-extract.c
	$(CP) $(SSFNET_XLATE_PATH)/pre-extract.c $@
$(SSFNET_XLATE_PATH)/generator_lexer.c:	$(SSFNET_XLATE_PATH)/pre-generator_lexer.c
	$(CP) $(SSFNET_XLATE_PATH)/pre-generator_lexer.c $@
endif


$(SSFNET_XLATE_PATH)/%.ssfnet.o:	$(SSFNET_XLATE_PATH)/%.c $(SSFNET_XLATE_HDRFILES)
	$(CC_ORIG) -c -I$(SSFNET_XLATE_PATH) -I$(SSFNET_PATH)/../ $(CFLAGS) -o $@ $<

$(SSFNET_XLATE_PATH)/%.ssfnet.oo:	$(SSFNET_XLATE_PATH)/%.cc $(SSFNET_XLATE_HDRFILES)
	$(CXX) -c -I$(SSFNET_XLATE_PATH) -I$(SSFNET_PATH)/../ $(CXXFLAGS) -o $@ $<

$(SSFNET_EXTRACT):	$(SSFNET_XLATE_PATH)/extract.ssfnet.o
	$(CC_ORIG) $(SSFNET_LDFLAGS) -o $@ $<

$(SSFNET_CODEGEN):	$(SSFNET_CODEGEN_OBJFILES)
	$(CXX) $(SSFNET_LDFLAGS) -o $@ $(SSFNET_CODEGEN_OBJFILES)

GENERATED_HFILES = \
	$(SSFNET_PATH)/os/protocol_session.h \
	$(SSFNET_PATH)/os/alias.h \
	$(SSFNET_PATH)/os/placeholder.h \
	$(SSFNET_PATH)/os/routing.h \
	$(SSFNET_PATH)/os/protocol_graph.h \
	$(SSFNET_PATH)/os/traffic.h \
	$(SSFNET_PATH)/os/monitor.h \
	$(SSFNET_PATH)/net/nic_queue.h \
	$(SSFNET_PATH)/net/red_queue.h \
	$(SSFNET_PATH)/net/net.h \
	$(SSFNET_PATH)/net/droptail_queue.h \
	$(SSFNET_PATH)/net/host.h \
	$(SSFNET_PATH)/net/interface.h \
	$(SSFNET_PATH)/net/link.h \
	$(SSFNET_PATH)/proto/routing_protocol.h \
	$(SSFNET_PATH)/proto/application_session.h \
	$(SSFNET_PATH)/proto/ipv4/icmpv4_session.h \
	$(SSFNET_PATH)/proto/ipv4/icmp_traffic.h \
	$(SSFNET_PATH)/proto/ipv4/ipv4_session.h \
	$(SSFNET_PATH)/proto/fluid/fluid_queue.h \
	$(SSFNET_PATH)/proto/fluid/fluid_traffic.h \
	$(SSFNET_PATH)/proto/fluid/probe_session.h \
	$(SSFNET_PATH)/proto/emu/emulation_protocol.h \
	$(SSFNET_PATH)/os/emu/vpn_emu_proto.h \
	$(SSFNET_PATH)/os/emu/tap_emu_proto.h \
	$(SSFNET_PATH)/os/emu/portal_emu_proto.h \
	$(SSFNET_PATH)/proto/tcp/tcp_master.h \
	$(SSFNET_PATH)/proto/tcp/test/http_server.h \
	$(SSFNET_PATH)/proto/tcp/test/http_client.h \
	$(SSFNET_PATH)/proto/tcp/test/http_traffic.h \
	$(SSFNET_PATH)/proto/tcp/test/SymbioSimAppProt.h \
	$(SSFNET_PATH)/proto/tcp/swing_tcp_traffic/swing_tcp_traffic.h \
	$(SSFNET_PATH)/proto/tcp/swing_tcp_traffic/swing_client.h \
	$(SSFNET_PATH)/proto/tcp/swing_tcp_traffic/swing_server.h \
	$(SSFNET_PATH)/proto/udp/udp_master.h \
	$(SSFNET_PATH)/proto/stcp/stcp_master.h \
	$(SSFNET_PATH)/proto/udp/test/ppbp_traffic.h \
	$(SSFNET_PATH)/proto/udp/test/cbr.h \
	$(SSFNET_PATH)/proto/udp/test/cbr_traffic.h \
	$(SSFNET_PATH)/proto/cnf/cnf_session.h \
	$(SSFNET_PATH)/proto/cnf/test/cnf_traffic.h 
DOT_M_HFILES = $(GENERATED_HFILES:.h=.m)
DOT_M_EXTRACTED_HFILES = $(GENERATED_HFILES:.h=.m.extracted)
MASTER_M_FILE = $(SSFNET_XLATE_PATH)/master_state_config.m
COMMON_CONFIG_TYPES_H = $(SSFNET_PATH)/os/configurable_types.h
COMMON_CONFIG_TYPES_CC = $(SSFNET_PATH)/os/configurable_types.cc

SSFNET_OS_HFILES = \
	$(SSFNET_PATH)/os/configurable_types.h \
	$(SSFNET_PATH)/os/community.h \
	$(SSFNET_PATH)/os/partition.h \
	$(SSFNET_PATH)/os/model_builder.h \
	$(SSFNET_PATH)/os/ssfnet_types.h \
	$(SSFNET_PATH)/os/trie.h \
	$(SSFNET_PATH)/os/ssfnet.h \
	$(SSFNET_PATH)/os/logger_factory.h \
	$(SSFNET_PATH)/os/monitor.h \
	$(SSFNET_PATH)/os/logger.h \
	$(SSFNET_PATH)/os/state_logger.h \
	$(SSFNET_PATH)/os/resource_identifier.h \
	$(SSFNET_PATH)/os/protocol_message.h \
	$(SSFNET_PATH)/os/packet.h \
	$(SSFNET_PATH)/os/packet_event.h \
	$(SSFNET_PATH)/os/virtual_time.h \
	$(SSFNET_PATH)/os/config_factory.h \
	$(SSFNET_PATH)/os/config_vars.h \
	$(SSFNET_PATH)/os/config_type.h \
	$(SSFNET_PATH)/os/config_entity.h \
	$(SSFNET_PATH)/os/ssfnet_exception.h \
	$(SSFNET_PATH)/os/timer.h \
	$(SSFNET_PATH)/os/timer_queue.h \
	$(SSFNET_PATH)/os/protocol_session.h \
	$(SSFNET_PATH)/os/routing.h \
	$(SSFNET_PATH)/os/protocol_graph.h \
	$(SSFNET_PATH)/os/alias.h \
	$(SSFNET_PATH)/os/io_mgr.h \
	$(SSFNET_PATH)/os/traffic_mgr.h \
	$(SSFNET_PATH)/os/traffic.h \
	$(SSFNET_PATH)/os/nix_vector.h \
	$(SSFNET_PATH)/os/data_message.h \
	$(SSFNET_PATH)/os/emu/emulation_device.h \
	$(SSFNET_PATH)/os/emu/emulation_event.h \
	$(SSFNET_PATH)/os/emu/util.h \
	$(SSFNET_PATH)/os/emu/tap_device.h \
	$(SSFNET_PATH)/os/emu/tap_emu_proto.h \
	$(SSFNET_PATH)/os/emu/portal_device.h \
	$(SSFNET_PATH)/os/emu/portal_emu_proto.h \
	$(SSFNET_PATH)/os/emu/vpn_device.h \
	$(SSFNET_PATH)/os/emu/vpn_emu_proto.h	
SSFNET_OS_CFILES = \
	$(SSFNET_PATH)/os/configurable_types.cc \
	$(SSFNET_PATH)/os/community.cc \
	$(SSFNET_PATH)/os/partition.cc \
	$(SSFNET_PATH)/os/model_builder.cc \
	$(SSFNET_PATH)/os/ssfnet_types.cc \
	$(SSFNET_PATH)/os/trie.cc \
	$(SSFNET_PATH)/os/monitor.cc \
	$(SSFNET_PATH)/os/logger_factory.cc \
	$(SSFNET_PATH)/os/state_logger.cc \
	$(SSFNET_PATH)/os/resource_identifier.cc \
	$(SSFNET_PATH)/os/protocol_message.cc \
	$(SSFNET_PATH)/os/packet.cc \
	$(SSFNET_PATH)/os/packet_event.cc \
	$(SSFNET_PATH)/os/virtual_time.cc \
	$(SSFNET_PATH)/os/config_factory.cc \
	$(SSFNET_PATH)/os/config_vars.cc \
	$(SSFNET_PATH)/os/config_type.cc \
	$(SSFNET_PATH)/os/config_entity.cc \
	$(SSFNET_PATH)/os/ssfnet_exception.cc \
	$(SSFNET_PATH)/os/timer.cc \
	$(SSFNET_PATH)/os/timer_queue.cc \
	$(SSFNET_PATH)/os/protocol_session.cc \
	$(SSFNET_PATH)/os/routing.cc \
	$(SSFNET_PATH)/os/protocol_graph.cc \
	$(SSFNET_PATH)/os/rtree.h \
	$(SSFNET_PATH)/os/alias.cc \
	$(SSFNET_PATH)/os/io_mgr.cc \
	$(SSFNET_PATH)/os/traffic_mgr.cc \
	$(SSFNET_PATH)/os/traffic.cc \
	$(SSFNET_PATH)/os/nix_vector.cc \
	$(SSFNET_PATH)/os/data_message.cc \
	$(SSFNET_PATH)/os/emu/emulation_device.cc \
	$(SSFNET_PATH)/os/emu/emulation_event.cc \
	$(SSFNET_PATH)/os/emu/util.cc \
	$(SSFNET_PATH)/os/emu/portal_device.cc \
	$(SSFNET_PATH)/os/emu/tap_device.cc \
	$(SSFNET_PATH)/os/emu/vpn_device.cc 


SSFNET_OS_NONXFORM = $(filter %.cc,$(SSFNET_OS_CFILES))
SSFNET_OS_XFORM = $(filter %.cxx,$(SSFNET_OS_CFILES))
SSFNET_OS_OFILES = $(SSFNET_OS_XFORM:.cxx=.ssfnet.xo) $(SSFNET_OS_NONXFORM:.cc=.ssfnet.oo)

SSFNET_NET_HFILES = \
	$(SSFNET_PATH)/net/net.h \
	$(SSFNET_PATH)/net/host.h \
	$(SSFNET_PATH)/net/link.h \
	$(SSFNET_PATH)/net/interface.h \
	$(SSFNET_PATH)/net/nic_queue.h \
	$(SSFNET_PATH)/net/droptail_queue.h \
	$(SSFNET_PATH)/net/red_queue.h 
SSFNET_NET_CFILES = \
	$(SSFNET_PATH)/net/net.cc \
	$(SSFNET_PATH)/net/host.cc \
	$(SSFNET_PATH)/net/link.cc \
	$(SSFNET_PATH)/net/interface.cc \
	$(SSFNET_PATH)/net/nic_queue.cc \
	$(SSFNET_PATH)/net/droptail_queue.cc \
	$(SSFNET_PATH)/net/red_queue.cc
SSFNET_NET_NONXFORM = $(filter %.cc,$(SSFNET_NET_CFILES))
SSFNET_NET_XFORM = $(filter %.cxx,$(SSFNET_NET_CFILES))
SSFNET_NET_OFILES = $(SSFNET_NET_XFORM:.cxx=.ssfnet.xo) $(SSFNET_NET_NONXFORM:.cc=.ssfnet.oo)

SSFNET_PROTO_HFILES = \
	$(SSFNET_PATH)/proto/ipv4/ipv4_message.h \
	$(SSFNET_PATH)/proto/ipv4/ipv4_session.h \
	$(SSFNET_PATH)/proto/ipv4/icmpv4_message.h \
	$(SSFNET_PATH)/proto/ipv4/icmpv4_session.h \
	$(SSFNET_PATH)/proto/ipv4/icmp_traffic.h \
	$(SSFNET_PATH)/proto/fluid/fluid_event.h \
	$(SSFNET_PATH)/proto/fluid/fluid_hop.h \
	$(SSFNET_PATH)/proto/fluid/fluid_queue.h \
	$(SSFNET_PATH)/proto/fluid/fluid_traffic.h \
	$(SSFNET_PATH)/proto/fluid/probe_session.h \
	$(SSFNET_PATH)/proto/fluid/probe_message.h \
	$(SSFNET_PATH)/proto/routing_protocol.h \
	$(SSFNET_PATH)/proto/application_session.h \
	$(SSFNET_PATH)/proto/transport_session.h \
	$(SSFNET_PATH)/proto/simple_socket.h \
	$(SSFNET_PATH)/proto/emu/emulation_protocol.h \
	$(SSFNET_PATH)/proto/tcp/tcp_master.h \
	$(SSFNET_PATH)/proto/emu/emulation_protocol.h \
	$(SSFNET_PATH)/proto/tcp/agent/tcp_session.h \
	$(SSFNET_PATH)/proto/tcp/agent/tcp_seqwnd.h \
	$(SSFNET_PATH)/proto/tcp/agent/tcp_sndwnd.h \
	$(SSFNET_PATH)/proto/tcp/agent/tcp_rcvwnd.h \
	$(SSFNET_PATH)/proto/tcp/agent/tcp_message.h \
	$(SSFNET_PATH)/proto/tcp/agent/scoreboard_2_ns.h \
	$(SSFNET_PATH)/proto/tcp/agent/linux/ns-linux-param.h \
	$(SSFNET_PATH)/proto/tcp/agent/linux/ns-linux-util.h \
	$(SSFNET_PATH)/proto/tcp/agent/linux/src/tcp_vegas.h \
	$(SSFNET_PATH)/proto/tcp/test/http_server.h \
	$(SSFNET_PATH)/proto/tcp/test/http_client.h \
	$(SSFNET_PATH)/proto/tcp/test/http_traffic.h \
	$(SSFNET_PATH)/proto/tcp/test/SymbioSimAppProt.h \
	$(SSFNET_PATH)/proto/tcp/swing_tcp_traffic/swing_tcp_traffic.h \
	$(SSFNET_PATH)/proto/tcp/swing_tcp_traffic/swing_client.h \
	$(SSFNET_PATH)/proto/tcp/swing_tcp_traffic/swing_server.h \
	$(SSFNET_PATH)/proto/udp/udp_master.h \
	$(SSFNET_PATH)/proto/udp/udp_session.h \
	$(SSFNET_PATH)/proto/udp/udp_message.h \
	$(SSFNET_PATH)/proto/stcp/stcp_master.h \
	$(SSFNET_PATH)/proto/stcp/stcp_session.h \
	$(SSFNET_PATH)/proto/stcp/stcp_message.h \
	$(SSFNET_PATH)/proto/udp/test/ppbp_traffic.h \
	$(SSFNET_PATH)/proto/udp/test/cbr.h \
	$(SSFNET_PATH)/proto/udp/test/cbr_traffic.h \
	$(SSFNET_PATH)/proto/cnf/cnf_session.h \
	$(SSFNET_PATH)/proto/cnf/cnf_message.h \
	$(SSFNET_PATH)/proto/cnf/test/cnf_traffic.h 
SSFNET_PROTO_CFILES = \
	$(SSFNET_PATH)/proto/ipv4/ipv4_message.cc \
	$(SSFNET_PATH)/proto/ipv4/ipv4_session.cc \
	$(SSFNET_PATH)/proto/ipv4/icmpv4_message.cc \
	$(SSFNET_PATH)/proto/ipv4/icmpv4_session.cc \
	$(SSFNET_PATH)/proto/ipv4/icmp_traffic.cc \
	$(SSFNET_PATH)/proto/fluid/fluid_event.cc \
	$(SSFNET_PATH)/proto/fluid/fluid_hop.cc \
	$(SSFNET_PATH)/proto/fluid/fluid_queue.cc \
	$(SSFNET_PATH)/proto/fluid/fluid_traffic.cc \
	$(SSFNET_PATH)/proto/fluid/probe_session.cc \
	$(SSFNET_PATH)/proto/fluid/probe_message.cc \
	$(SSFNET_PATH)/proto/routing_protocol.cc \
	$(SSFNET_PATH)/proto/application_session.cc \
	$(SSFNET_PATH)/proto/transport_session.cc \
	$(SSFNET_PATH)/proto/simple_socket.cc \
	$(SSFNET_PATH)/proto/emu/emulation_protocol.cc \
	$(SSFNET_PATH)/proto/tcp/tcp_master.cc \
	$(SSFNET_PATH)/proto/tcp/agent/tcp_session.cc \
	$(SSFNET_PATH)/proto/tcp/agent/tcp_sndwnd.cc \
	$(SSFNET_PATH)/proto/tcp/agent/tcp_rcvwnd.cc \
	$(SSFNET_PATH)/proto/tcp/agent/tcp_message.cc \
	$(SSFNET_PATH)/proto/tcp/agent/scoreboard_2_ns.cc \
	$(SSFNET_PATH)/proto/tcp/agent/linux/ns-linux-c.c \
	$(SSFNET_PATH)/proto/tcp/agent/linux/ns-linux-param.c \
	$(SSFNET_PATH)/proto/tcp/agent/linux/ns-linux-util.cc \
	$(SSFNET_PATH)/proto/tcp/agent/linux/tcp_naivereno.c \
	$(SSFNET_PATH)/proto/tcp/agent/linux/src/tcp_bic.c \
	$(SSFNET_PATH)/proto/tcp/agent/linux/src/tcp_compound.c \
	$(SSFNET_PATH)/proto/tcp/agent/linux/src/tcp_cong.c \
	$(SSFNET_PATH)/proto/tcp/agent/linux/src/tcp_cubic.c \
	$(SSFNET_PATH)/proto/tcp/agent/linux/src/tcp_highspeed.c \
	$(SSFNET_PATH)/proto/tcp/agent/linux/src/tcp_htcp.c \
	$(SSFNET_PATH)/proto/tcp/agent/linux/src/tcp_hybla.c \
	$(SSFNET_PATH)/proto/tcp/agent/linux/src/tcp_illinois.c \
	$(SSFNET_PATH)/proto/tcp/agent/linux/src/tcp_lp.c \
	$(SSFNET_PATH)/proto/tcp/agent/linux/src/tcp_scalable.c \
	$(SSFNET_PATH)/proto/tcp/agent/linux/src/tcp_vegas.c \
	$(SSFNET_PATH)/proto/tcp/agent/linux/src/tcp_veno.c \
	$(SSFNET_PATH)/proto/tcp/agent/linux/src/tcp_westwood.c \
	$(SSFNET_PATH)/proto/tcp/agent/linux/src/tcp_yeah.c \
	$(SSFNET_PATH)/proto/tcp/test/http_server.cc \
	$(SSFNET_PATH)/proto/tcp/test/http_client.cc \
	$(SSFNET_PATH)/proto/tcp/test/http_traffic.cc \
	$(SSFNET_PATH)/proto/tcp/test/SymbioSimAppProt.cc \
	$(SSFNET_PATH)/proto/tcp/swing_tcp_traffic/swing_tcp_traffic.cc \
	$(SSFNET_PATH)/proto/tcp/swing_tcp_traffic/swing_client.cc \
	$(SSFNET_PATH)/proto/tcp/swing_tcp_traffic/swing_server.cc \
	$(SSFNET_PATH)/proto/udp/udp_master.cc \
	$(SSFNET_PATH)/proto/udp/udp_session.cc \
	$(SSFNET_PATH)/proto/udp/udp_message.cc \
	$(SSFNET_PATH)/proto/stcp/stcp_master.cc \
	$(SSFNET_PATH)/proto/stcp/stcp_session.cc \
	$(SSFNET_PATH)/proto/stcp/stcp_message.cc \
	$(SSFNET_PATH)/proto/udp/test/ppbp_traffic.cc \
	$(SSFNET_PATH)/proto/udp/test/cbr.cc \
	$(SSFNET_PATH)/proto/udp/test/cbr_traffic.cc \
	$(SSFNET_PATH)/proto/cnf/cnf_session.cc \
	$(SSFNET_PATH)/proto/cnf/cnf_message.cc \
	$(SSFNET_PATH)/proto/cnf/test/cnf_traffic.cc
SSFNET_PROTO_NONXFORM_C = $(filter %.c,$(SSFNET_PROTO_CFILES))
SSFNET_PROTO_NONXFORM_CC = $(filter %.cc,$(SSFNET_PROTO_CFILES))
SSFNET_PROTO_XFORM = $(filter %.cxx,$(SSFNET_PROTO_CFILES))
SSFNET_PROTO_OFILES = $(SSFNET_PROTO_XFORM:.cxx=.ssfnet.xo) $(SSFNET_PROTO_NONXFORM_CC:.cc=.ssfnet.oo) $(SSFNET_PROTO_NONXFORM_C:.c=.ssfnet.o)

SSFNET_HFILES = \
	$(SSFNET_OS_HFILES) \
	$(SSFNET_NET_HFILES) \
	$(SSFNET_PROTO_HFILES)
SSFNET_CFILES = \
	$(SSFNET_OS_CFILES) \
	$(SSFNET_NET_CFILES) \
	$(SSFNET_PROTO_CFILES)
SSFNET_OFILES = \
	$(SSFNET_OS_OFILES) \
	$(SSFNET_NET_OFILES) \
	$(SSFNET_PROTO_OFILES)
SSFNET_INCLUDES = $(SSF_INCLUDES) -I$(SSFNET_PATH)
SSFNET_OTHER_CFLAGS = \
	-DPRIME_SSFNET_IPV4_OFFLOAD_CKSUM=0
	

ifeq ($(ENABLE_SSFNET_EMULATION), yes)
PCAP_INC = -I$(PCAP_PATH)
#PCAP_LIBS = $(PCAP_PATH)/libpcap.a
PCAP_LIBS = -lpcap
else
PCAP_INC = 
PCAP_LIBS = 
endif


SSFNET_CFLAGS = $(SSF_CFLAGS) $(SSFNET_OTHER_CFLAGS) $(PCAP_INC) -Wall
SSFNET_CXXFLAGS = $(SSF_CXXFLAGS) $(SSFNET_OTHER_CFLAGS) $(PCAP_INC) -Wall
SSFNET_LDFLAGS = $(SSF_LDFLAGS)
SSFNET_LIBS = $(SSF_LIBS) $(PCAP_LIBS)
SSFNET_PROGRAM_OBJ = $(SSFNET_PATH)/os/ssfnet.ssfnet.oo
SSFNET_PROGRAM = primex$(EXEEXT)
SSFNET_CLEANUP = \
	$(SSFNET_OFILES) $(SSFNET_PROGRAM) $(SSFNET_XLATE_CLEANUP) $(SSFNET_PROGRAM_OBJ) \
	Makefile.depend Makefile.bak \
	_tmp?.cc .tmpdat* *.log \
	$(DOT_M_EXTRACTED_HFILES) $(MASTER_M_FILE)

# handle source files that require source-code transformation:
#   i) run preprocessor on the source code using CXXCPP and generate _tmp0.cc;
#  ii) strip the comments from _tmp0 using SSF_CMTSTRIP and generate _tmp1.cc;
# iii) run source-to-source translation using SSF_XLATER and generate _tmp2.cc;
#  iv) compile _tmp2.cc and generate the object file
#   v) delete all temperary _tmp?.cc files
$(SSFNET_PATH)/%.ssfnet.xo:	$(SSFNET_PATH)/%.cxx $(SSFNET_HFILES)
	$(CXXCPP) $(SSFNET_INCLUDES) $(SSFNET_CXXFLAGS) -o _tmp0.cc $<
	$(SSF_CMTSTRIP) < _tmp0.cc > _tmp1.cc
	$(SSF_XLATER) _tmp1.cc > _tmp2.cc
	$(CXX) $(SSFNET_CXXOPT) $(CXXFLAGS) -o $@ -c _tmp2.cc
	$(RMV) _tmp?.cc

# compile the source code that does not require instrumentation
$(SSFNET_PATH)/%.ssfnet.oo:	$(SSFNET_PATH)/%.cc $(SSFNET_HFILES)
	$(CXX) $(SSFNET_INCLUDES) $(SSFNET_CXXFLAGS) -o $@ -c $(CXXFLAGS) $<
$(SSFNET_PATH)/%.ssfnet.o:	$(SSFNET_PATH)/%.c $(SSFNET_HFILES)
	$(CC) $(SSFNET_INCLUDES) $(SSFNET_CFLAGS) -o $@ -c $(CFLAGS) $<

# extract the state_configuration blocks from m files
$(SSFNET_PATH)/%.m.extracted:	$(SSFNET_PATH)/%.m $(SSFNET_EXTRACT)
	$(SSFNET_EXTRACT) $< > $@
	$(RMV) $(MASTER_M_FILE)

.PHONY: ssfnet-depend ssfnet-dependclean

ssfnet-help:
	@ $(ECHO) "    ssfnet            :  Build the SSFNet simulator."
	@ $(ECHO) "    ssfnet-jprime     :  Generated the Java/Python bindings for SSFNet."
	@ $(ECHO) "    ssfnet-clean      :  Clean SSFNet."

ssfnet:	ssfnet-depend $(SSFNET_EXTRACT) $(SSFNET_CODEGEN) $(SSFNET_PROGRAM) $(SSFNET_EXTRACT)

$(SSFNET_PROGRAM):	$(SSFNET_OFILES) $(SSFNET_PROGRAM_OBJ)
	$(CXX) $(SSFNET_OFILES) $(SSFNET_PROGRAM_OBJ) -o $@ $(SSFNET_LDFLAGS) $(SSFNET_LIBS)

#generate the header files from the .m files
ssfnet-jprime: $(GENERATED_HFILES)

$(GENERATED_HFILES): $(MASTER_M_FILE)

$(MASTER_M_FILE): $(DOT_M_EXTRACTED_HFILES)
	$(CAT) $(DOT_M_EXTRACTED_HFILES) > $(MASTER_M_FILE)
	$(RMV) $(JPRIME_GEN)
	$(SSFNET_CODEGEN) $(MASTER_M_FILE) $(COMMON_CONFIG_TYPES_H) $(COMMON_CONFIG_TYPES_CC) $(JPRIME_SRC)

$(DOT_M_EXTRACTED_HFILES): $(SSFNET_CODEGEN) $(DOT_M_HFILES)

ssfnet-clean:	ssfnet-dependclean
	$(RMV) $(SSFNET_CLEANUP)
	$(RMV) $(SSFNET_XLATE_CLEANUP)

ssfnet-depend: ssf rng dml $(MASTER_M_FILE) $(SSFNET_EXTRACT) $(SSFNET_HFILES) $(SSFNET_CFILES)
	@ $(ECHO) '# Compilation Dependencies' > Makefile.depend
	$(CXX) -M $(SSFNET_INCLUDES) $(SSFNET_CXXFLAGS) $(SSFNET_CFILES) $(CXXFLAGS) >> Makefile.depend
	@ $(MV) Makefile Makefile.bak
	@ $(SED) -e '/^\# Compilation Dependencies/,$$d' < Makefile.bak > Makefile
	@ $(CAT) Makefile.depend >> Makefile
	@ $(RMV) Makefile.bak
	@ $(RMV) Makefile.depend

ssfnet-dependclean :
	@ $(MV) Makefile Makefile.bak
	@ $(SED) -e '/^\# Compilation Dependencies/,$$d' < Makefile.bak > Makefile
	@ $(ECHO) '# Compilation Dependencies' >> Makefile
	@ $(RMV) Makefile.bak

INCLUDED_SSFNET=true
# Compilation Dependencies
