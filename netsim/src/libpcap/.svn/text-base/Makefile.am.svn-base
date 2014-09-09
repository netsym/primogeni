#  Copyright (c) 1993, 1994, 1995, 1996
#       The Regents of the University of California.  All rights reserved.
#
#  Redistribution and use in source and binary forms, with or without
#  modification, are permitted provided that: (1) source code distributions
#  retain the above copyright notice and this paragraph in its entirety, (2)
#  distributions including binary code include the above copyright notice and
#  this paragraph in its entirety in the documentation or other materials
#  provided with the distribution, and (3) all advertising materials mentioning
#  features or use of this software display the following acknowledgement:
#  ``This product includes software developed by the University of California,
#  Lawrence Berkeley Laboratory and its contributors.'' Neither the name of
#  the University nor the names of its contributors may be used to endorse
#  or promote products derived from this software without specific prior
#  written permission.
#  THIS SOFTWARE IS PROVIDED ``AS IS'' AND WITHOUT ANY EXPRESS OR IMPLIED
#  WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF
#  MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
#
#  $Header: /n/CVS/sirt/libpcap/Makefile.am,v 0.10 2005/07/18 16:05:08 cpw Exp $

#
## Various configurable paths (remember to edit Makefile.am, not Makefile)
#

AUTOMAKE_OPTIONS	= foreign no-dependencies
ACLOCAL_AMFLAGS		= -I m4
# library versioning 
VCURRENT		= 0
VREVISION		= 9
VAGE			= 3
RMAJOR		= 0
RMINOR		= 9
RREVISION	= 8

LTRELEASE		= $(RMAJOR).$(RMINOR).$(RREVISION)
LTVERSION		= $(VCURRENT):$(VREVISION):$(VAGE)

PCAP			= @V_PCAP@
PSRC                    = pcap-@V_PCAP@.c
FSRC                    = fad-@V_FINDALLDEVS@.c
FADS			= @V_FADS@

LEX                     = @V_LEX@
YACC                    = @V_YACC@
OTHERS			= @V_OTHERS@
DEFS			= @DEFS@ @V_DEFS@
INCLUDES		= @V_INCLS@

EXTRA_DIST		= LICENSE INSTALL.txt FILES TODO CHANGES \
			  CREDITS README README.Win32 README.aix README.dag \
			  README.hpux README.linux README.macosx README.septel \
			  README.tru64 pcap.3 VERSION REVISION install-sh \
			  missing SUNOS4 bpf lbl packaging doc ChmodBPF \
			  Win32 msdos scanner.l grammar.y mkdep \
			  pcap-dag.c pcap-septel.c $(OTHERS) $(FADS) \
			  Makefile.am \
			  configure.ac acinclude.m4 FADDERS PCAPPERS \
			  README.ring pcap-ring.c bootstrap config

MAINTAINERCLEANFILES    = Makefile.in Makefile.am.bak configure.ac.bak aclocal.m4 configure config.h.in \
				.warrantee output.0 output.1 requests traces.0 traces.1 beenthere

MOSTLYCLEANFILES        = bpf_filter.c scanner.c grammar.c version.c lex.yy.c \
                          tokdefs.h version.h os-proto.h \
			  stamp-h stamp-h.in

HDR			= arcnet.h atmuni31.h config.h ethertype.h gencode.h \
			  llc.h nlpid.h pcap-bpf.h pcap-int.h pcap-namedb.h \
			  pcap-nit.h pcap-pf.h pcap.h ppp.h sll.h sunatmpos.h \
			  pf.h pcap-septel.h pcap-dag.h pcap-stdinc.h pcap1.h \
			  pcap-dos.h pcap-ring.h

GENSRC                  = bpf_filter.c scanner.c grammar.c version.c
CSRC                    = pcap.c inet.c gencode.c optimize.c  nametoaddr.c \
                          etherent.c savefile.c bpf_image.c bpf_dump.c

PCAPSRC			= $(PSRC) $(FSRC) $(CSRC)

lib_LTLIBRARIES		= libpcap.la

libpcap_la_LDFLAGS      = -release $(LTRELEASE)
#libpcap_la_LDFLAGS      = -version-info $(LTVERSION)

man3_MANS		= pcap.3

if USE_RING
libpcap_la_SOURCES	= $(GENSRC) $(PCAPSRC) $(HDR) pcap-ring.c
else
if USE_DAG
libpcap_la_SOURCES	= $(GENSRC) $(PCAPSRC) $(HDR) pcap-dag.c
else
if USE_SEPTEL
libpcap_la_SOURCES      = $(GENSRC) $(PCAPSRC) $(HDR) pcap-septel.c
else
libpcap_la_SOURCES      = $(GENSRC) $(PCAPSRC) $(HDR)
endif
endif
endif

clean-local:
	rm -f libpcap.a

#all-local:
#	rm -f libpcap.a; $(LN_S) .libs/libpcap.a libpcap.a
all-local: libpcap.a

libpcap.a: .libs/libpcap.a
	cp .libs/libpcap.a libpcap.a

install-data-local:

xxx:
	rm -f $(DESTDIR)$(includedir)/pcap-bpf.h
	rm -f $(DESTDIR)$(includedir)/pcap.h
	rm -f $(DESTDIR)$(includedir)/pcap-namedb.h
	cp pcap.h $(DESTDIR)$(includedir)
	cp pcap-bpf.h $(DESTDIR)$(includedir)
	cp pcap-namedb.h $(DESTDIR)$(includedir)
	rm -f $(DESTDIR)$(libdir)/libpcap $(DESTDIR)$(libdir)/libpcap.la
	$(STRIP) $(DESTDIR)$(libdir)/libpcap-0.$(RMINOR).$(RREVISION).so
	find /usr/lib -name "libpcap.so.*" -print | while read so; do rm $$so; ln -s $(DESTDIR)$(libdir)/libpcap-0.$(RMINOR).$(RREVISION).so $$so; done
	if test ! -s $(DESTDIR)$(libdir)/libpcap.so.0.$(RMINOR); then \
	   ln -s $(DESTDIR)$(libdir)/libpcap-0.$(RMINOR).$(RREVISION).so \
	   $(DESTDIR)$(libdir)/libpcap.so.0.$(RMINOR); fi
	ln -s $(DESTDIR)$(libdir)/libpcap-0.$(RMINOR).$(RREVISION).so $(DESTDIR)$(libdir)/libpcap.so.0.$(RMINOR).$(RREVISION)

yyy:
	sh beenthere

# generated source
pcap.3.gz: pcap.3
	gzip -c < pcap.3 > pcap.3.gz

bpf_filter.c: bpf/net/bpf_filter.c
	rm -f bpf_filter.c
	$(LN_S) $(srcdir)/bpf/net/bpf_filter.c bpf_filter.c

scanner.o: scanner.c tokdefs.h

scanner.c: scanner.l tokdefs.h
	@rm -f $@
	$(V_LEX) -t scanner.l > $$$$.$@; mv $$$$.$@ $@

tokdefs.h:
	rm -f grammar.c
	make grammar.o

grammar.o: $(srcdir)/grammar.c

grammar.c: $(srcdir)/grammar.y
	@rm -f grammar.c tokdefs.h
	$(YACC) -d $(srcdir)/grammar.y
	mv y.tab.c grammar.c
	mv y.tab.h tokdefs.h

version.o: version.c

version.c: $(srcdir)/REVISION version.h
	@rm -f $@
	echo `cat $(srcdir)/VERSION` | \
          sed -e 's/.*/char pcap_version[] = "&";/' > version.c

version.h: $(srcdir)/VERSION
	@rm -f $@
	sed -n -e 's/.*/static const char pcap_version_string[] = "libpcap version &";/p' $(srcdir)/VERSION > $@

