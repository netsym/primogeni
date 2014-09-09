#
# Makefile for Internetwork Topology Models
#

IDIR = ../include
LDIR = ../sgb
BDIR = ../bin

CC = gcc
CFLAGS = -I$(IDIR) -I$(LDIR) 
LIBS = -L$(LDIR) -lgb -lm

all:	sgb-build itm sgb2alt edriver

sgb-build:
	(cd $(LDIR); make lib)

GH = $(IDIR)/geog.h $(LDIR)/gb_graph.h $(LDIR)/gb_flip.h
CH = $(IDIR)/geog.h $(LDIR)/gb_graph.h $(LDIR)/gb_save.h
IH = $(LDIR)/gb_graph.h $(LDIR)/gb_dijk.h $(IDIR)/eval.h
EH = $(LDIR)/gb_graph.h $(LDIR)/gb_save.h $(LDIR)/gb_dijk.h $(IDIR)/eval.h

itm.o:	$(GH)
geog.o:	$(GH)
ts.o: $(GH)	
dfs.o: $(GH)
sgb2alt.o: $(CH)
eval.o: $(IH)
edriver.o: $(EH)

GO = itm.o geog.o ts.o dfs.o
CO = sgb2alt.o 
EO = edriver.o eval.o

itm:	$(GO)
	$(CC) $(CFLAGS) -o $(BDIR)/itm $(GO) $(LIBS)

sgb2alt: $(CO)
	$(CC) $(CFLAGS) -o $(BDIR)/sgb2alt $(CO) $(LIBS) 

edriver: $(EO)
	$(CC) $(CFLAGS) -o $(BDIR)/edriver $(EO) $(LIBS)

sgb-build-clean:
	(cd $(LDIR); make clean)

clean:  sgb-build-clean
	rm -f $(GO) $(CO) $(EO)
	rm -f $(BDIR)/itm $(BDIR)/sgb2alt $(BDIR)/edriver
