/*
 * muxtree :- a tree of multiplexers.
 *
 * Muxtree is a model that builds a simple network of ssf
 * entities. The topology of this model is a tree: the leaves of the
 * tree are traffic sources and the interior nodes (including the
 * root) of the tree are multiplexers. Messages are generated from the
 * source node and travel through a series of multiplexers until it
 * reaches the root of the tree. Within each multiplexer, there is a
 * FIFO queue that has a buffer with a finite size and each message is
 * given a constant service time. Messages could be dropped if a
 * buffer overflow occurs. A detailed description of this example is
 * given in the SSF User's Manual.
 */

#include <stdio.h>
#include <stdlib.h>

#include "muxtree.h"

// commenting out the follwing definition, you will not see the
// verbose debugging information.
//#define MUXTREE_DEBUG

// This macro is required by ssf. Every user-defined event class
// (derived from the Event base class) must use this macro to register
// with the ssf runtime system.
SSF_REGISTER_EVENT(MyMessage, MyMessage::create_my_message);

SourceEntity::SourceEntity(int myid) : 
  id(myid), nsent(0),
  rng(Random::USE_RNG_LEHMER, myid+1) // use Lehmer RNG seeded with node id (plus 1 to avoid 0)
{
#ifdef MUXTREE_DEBUG
  printf("SourceEntity[%d] created\n", id);
#endif

  // create an output channel with an appropriate channel delay
  oc = new outChannel(this, TRANSMISSION_DELAY);

  // map the output channel to the input channel of the multiplexer at
  // the next tree level (level 0)
  char icname[128];
  sprintf(icname, "IN_0_%d", id/MUXTREE_FANIN);
  oc->mapto(icname);

  // create the emit process to generate messages
  new EmitProcess(this);
}

//! SSF PROCEDURE
void EmitProcess::action()
{
  ltime_t tm; //! SSF STATE
#ifdef MUXTREE_DEBUG
  printf("EmitProcess (for SourceEntity[%d]) started, ", owner()->id);
#endif
  for(;;) {
    tm = (ltime_t)owner()->rng.exponential(1.0/SRC_ENTITY_IAT);
#ifdef MUXTREE_DEBUG
    printf("waiting until %g\n", TICKS2SECS(now()+tm));
#endif
    waitFor(tm);

    // create and send out a newly generated message
    MyMessage* msg = new MyMessage(now(), owner()->id);
    owner()->oc->write(msg);
#ifdef MUXTREE_DEBUG
    printf("%g: EmitProcess (for SourceEntity[%d]) sent a message, ",
	   TICKS2SECS(now()), owner()->id);
#endif
    owner()->nsent++;
  }
}

MultiplexerEntity::MultiplexerEntity(int mylevel, int myid) :
  level(mylevel), id(myid), nrcvd(-1), // nrcvd marks the first time the arrive process is started
  nlost(0), nsent(0), tail(0), head(0), qlen(0)
{
#ifdef MUXTREE_DEBUG
  printf("MultiplexerEntity[level=%d,id=%d] created on mach=%d proc=%d\n",
	 level, id, ssf_machine_index(), ssf_processor_index());
#endif

  buf = new MyMessage*[MUX_ENTITY_BUFSIZ];

  // create the input channel and name it
  char icname[128];
  sprintf(icname, "IN_%d_%d", level, id);
  ic = new inChannel(icname, this);

  // create output channel and map it to input channel of the
  // multiplexer at the next tree level
  oc = new outChannel(this, TRANSMISSION_DELAY);
  if(level < MUXTREE_LEVELS-1) { // if it's not at the tree root
    sprintf(icname, "IN_%d_%d", level+1, id/MUXTREE_FANIN);
    oc->mapto(icname);
  }

  // create the internal input and output channels; map them together
  int_ic = new inChannel(this);
  int_oc = new outChannel(this);
  int_oc->mapto(int_ic);

  // create the SIMPLE process for handling message arrivals
  Process* arrive_proc = new Process
    (this, (void (Entity::*)(Process*))
     &MultiplexerEntity::arrive, true);
  arrive_proc->waitsOn(ic); // set default the input channel

  // create the process for servicing messages at the head of the queue
  Process* serve_proc = new Process
    (this, (void (Entity::*)(Process*))
     &MultiplexerEntity::serve, false);
  serve_proc->waitsOn(int_ic); // set the default input channel
}

MultiplexerEntity::~MultiplexerEntity() 
{
  while(qlen > 0) {
    // get the message at the front of the queue, release it
    MyMessage* msg = buf[(head++)%MUX_ENTITY_BUFSIZ];
    msg->release();
    qlen--;
  }
  delete[] buf;
}

void MultiplexerEntity::init()
{
  if(level == 0) {
    for(int i=0; i<MUXTREE_FANIN; i++) {
      SourceEntity* src = new SourceEntity(id*MUXTREE_FANIN+i);
      src->alignto(this); // align the source entity to the corresponding multiplexer entity
    }
  }
}

void MultiplexerEntity::wrapup()
{
  // the wrapup method is called before the MultiplexEntity is
  // reclaimed at the end of the simulation; we dump the statistics
  // for later processing.
  ssf_compact* dp = new ssf_compact;
  dp->add_long(nrcvd);
  dp->add_long(nlost);
  dp->add_long(nsent);
  dumpData(1, dp); // the dumped record is classified as type 1
  delete dp; // don't forget to delete the record after use
}

//! SSF PROCEDURE SIMPLE
void MultiplexerEntity::arrive(Process* p)
{
  if(nrcvd >= 0) { // if this is not the first time the procedure is called
    // there could be multiple events arrived at the input channel;
    // they are all in the null-terminated list.
    MyMessage** evts = (MyMessage**)ic->activeEvents();
    for(int i=0; evts[i]; i++) {
#ifdef MUXTREE_DEBUG
      printf("%g: MultiplexerEntity[level=%d,id=%d] arrive() received msg (time=%g,srcid=%d): ",
	     TICKS2SECS(now()), level, id, (double)evts[i]->time, evts[i]->srcid);
#endif
      if(qlen == MUX_ENTITY_BUFSIZ) { // if the queue is full
	nlost++;
#ifdef MUXTREE_DEBUG
	printf("dropped due to overflow\n");
#endif
      } else {
	// save a new reference of this event into the queue
	buf[(tail++)%MUX_ENTITY_BUFSIZ] = (MyMessage*)evts[i]->save();
#ifdef MUXTREE_DEBUG
	printf("enqueued\n");
#endif
	qlen++;

	// notify the server process if it's the first message in the queue
	if(qlen == 1) int_oc->write(new Event);
      }
      nrcvd++;
    }
  } else {
#ifdef MUXTREE_DEBUG
    printf("MultiplexerEntity[level=%d,id=%d] arrive() started\n", level, id);
#endif
    nrcvd++;
  }
  waitOn(); // wait on the default input channel
}

//! SSF PROCEDURE
void MultiplexerEntity::serve(Process* p)
{
  MyMessage* inservice; //! SSF STATE

#ifdef MUXTREE_DEBUG
  printf("%g: MultiplexerEntity[level=%d,id=%d] serve() blocked on empty queue\n",
	 TICKS2SECS(now()), level, id);
#endif
  waitOn();
  while(qlen > 0) {
    inservice = buf[(head++)%MUX_ENTITY_BUFSIZ];
    qlen--;
#ifdef MUXTREE_DEBUG
    printf("%g: MultiplexerEntity[level=%d,id=%d] serve() is servicing msg "
	   "(time=%g,srcid=%d), waiting until %g\n",
	   TICKS2SECS(now()), level, id, TICKS2SECS(inservice->time), 
	   inservice->srcid, TICKS2SECS(now()+MUX_ENTITY_MST));
#endif
    waitFor(MUX_ENTITY_MST);

    // send the serviced message
    oc->write(inservice);
#ifdef MUXTREE_DEBUG
    printf("%g: MultiplexerEntity[level=%d,id=%d] serve() sent inservice (time=%g,src=%d)\n",
	   TICKS2SECS(now()), level, id, TICKS2SECS(inservice->time), inservice->srcid);
#endif
    inservice->release(); // don't forget to release the reference to the event just sent
    nsent++;
  }
}

/* One can postprocess the data recorded during the simulation in the
   end when the simulation is finished. This can either be done within
   the main function after Entity::joinAll() returns, or within a
   separate callback function registered with the simulation runtime
   system (like in our case). */
static void my_global_wrapup() {
  int x = 0;
  long sum_nrcvd = 0;
  long sum_nlost = 0;
  long sum_nsent = 0;
  Enumeration* dd = Entity::retrieveDataDumped();
  while(dd->hasMoreElements()) { // enumerate each dumped data
    ssf_entity_data* dumprec = (ssf_entity_data*)dd->nextElement();
    if(dumprec->type() != 1) continue; // all our records are classified as type 1
    ssf_compact* packed = dumprec->data();
    long nrcvd, nlost, nsent;
    if(packed->get_long(&nrcvd) != 1 ||
       packed->get_long(&nlost) != 1 ||
       packed->get_long(&nsent) != 1) {
      fprintf(stderr, "ERROR: unknown statistic record format\n");
      return;
    }
    x++;
    sum_nrcvd += nrcvd;
    sum_nlost += nlost;
    sum_nsent += nsent;
    // both dumprec and packed are not to be reclaimed by user!
  }
  delete dd; // but we should reclaim the enumeration object!

  printf("(%d records) nrcvd=%ld, nlost=%ld, nsent=%ld\n", 
	 x, sum_nrcvd, sum_nlost, sum_nsent);
}

// use this macro to set the global wrapup callback function
SSF_SET_GLOBAL_WRAPUP(my_global_wrapup);

int main(int argc, char** argv)
{
  if(argc != 3) {
    fprintf(stderr, "Usage: %s start_time end_time\n\n", argv[0]);
    prime::ssf::print_command_line(stderr);
    return 1;
  }
  ltime_t start_time = (ltime_t)(atof(argv[1])*SECOND);
  ltime_t end_time = (ltime_t)(atof(argv[2])*SECOND);
  if(start_time > end_time) {
    fprintf(stderr, "ERROR: invalid simulation interval: start=%g, end=%g\n",
	    TICKS2SECS(start_time), TICKS2SECS(end_time));
    return 2;
  }

  // The main function is called on each distributed node that runs
  // the ssf instance. SSF uses the programming model called SPMD, or
  // simple program multiple data. Here, we make sure that each
  // distributed node has its portion of the tree instantiated.

  int nnodes = 1;
  for(int level=MUXTREE_LEVELS-1; level>=0; level--) {
    for(int index=0; index<nnodes; index++) {
      // Here we assign the multiplexers among the distributed
      // machines in a round-robin fashion; this is for simplicity,
      // not for performance!
      if(index%ssf_num_machines() == ssf_machine_index()) {
	MultiplexerEntity* mux = new MultiplexerEntity(level, index);
	mux->makeIndependent(); // each multiplexer entity is a separate timeline
      }
    }
    nnodes *= MUXTREE_FANIN; // number of nodes at the next tree level
  }

  // Start the simulation! The control will return when joinAll
  // returns, when the simulation finishes.
  Entity::startAll(start_time, end_time);
  Entity::joinAll();

  return 0;
}
