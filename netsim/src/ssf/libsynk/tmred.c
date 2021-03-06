/*----------------------------------------------------------------------------*/
/* The "all-new" TM implementation!                                           */
/* Author(s): Kalyan Perumalla <http://www.cc.gatech.edu/~kalyan> 01Jun2000   */
/* Revised to use the new hybrid/combination TM.                              */
/* Author(s): Kalyan Perumalla <http://www.cc.gatech.edu/~kalyan> 26Nov2003   */
/* $Revision: 1.1.1.1 $ $Name:  $ $Date: 2004/07/21 21:01:31 $ */
/*----------------------------------------------------------------------------*/
#include <stdlib.h>
#include <string.h>
#include <stdio.h>

#include "mycompat.h"
#include "fm.h"
#include "tm.h"
#include "rm.h"

/*----------------------------------------------------------------------------*/
struct RVALUE_TYPE_STRUCT
{
    TM_Time val;        /*Timestamp being reduced*/
    TM_TimeQual qual;   /*Timestamp qualification: see TM_TimeQual definition*/
    long nsent, nrecd;  /*Number of timestamped messages sent/received*/
	#if !PLATFORM_WIN
	    long padding; /*Compatibility with size generated by Windows compiler*/
	#endif
};
/*----------------------------------------------------------------------------*/
static void hton_RVALUE_TYPE( RVALUE_TYPE *v )
    { (v)->nsent = htonl((v)->nsent); (v)->nrecd = htonl((v)->nrecd); }
static void ntoh_RVALUE_TYPE( RVALUE_TYPE *v )
    { (v)->nsent = ntohl((v)->nsent); (v)->nrecd = ntohl((v)->nrecd); }
/*----------------------------------------------------------------------------*/
static RVALUE_TYPE *rv_new( void )
    { return malloc( sizeof( RVALUE_TYPE ) ); }
static void rv_delete( RVALUE_TYPE *v )
    { free( v ); }
static void rv_init(RVALUE_TYPE *a)
    { if(a){ (a)->val = TM_IDENT; (a)->qual = TM_TIME_QUAL_INCL;
	     (a)->nsent = (a)->nrecd = 0; } }
static void rv_assign( RVALUE_TYPE *a, RVALUE_TYPE *b )
    { if((a)&&(b)) *(a) = *(b); }
static void rv_reduce( RVALUE_TYPE *a, RVALUE_TYPE *b)
    { if((a)&&(b))
        { if(TM_LT((b)->val, (a)->val) ||
	     (TM_EQ((b)->val, (a)->val) && ((b)->qual < (a)->qual)) )
		 { (a)->val = (b)->val; (a)->qual = (b)->qual; }
          (a)->nsent += (b)->nsent; (a)->nrecd += (b)->nrecd; } }
static void rv_set( RVALUE_TYPE *a, TM_Time v, TM_TimeQual qual, int ns, int nr)
    { if(a) { (a)->val = v; (a)->qual = qual;
	      (a)->nsent = ns; (a)->nrecd = nr; } }
static void rv_print( FILE *fp, RVALUE_TYPE *a )
    { if(!a) return;
      fprintf((fp), "<");
      if((a)->val >=TM_IDENT) fprintf((fp), "Infinity");
      else fprintf((fp), "%lf (%s)", (double)(a)->val, TM_TIME_QUAL_STR((a)->qual));
      fprintf( (fp), ", %ld, %ld>", (a)->nsent, (a)->nrecd );
    }
/*----------------------------------------------------------------------------*/
static RVALUE_CLASS rv_class =
{
    rv_new, rv_delete, rv_init, rv_assign, rv_reduce, rv_print
};

/*----------------------------------------------------------------------------*/
enum { RM_START_MSG, RM_VALUE_MSG };

/*----------------------------------------------------------------------------*/
typedef struct TMMesgStruct
{
    long ssn;                  /*ID# of snapshot to which this mesg belongs*/
    long trial;                /*Trial number within this snapshot*/
    long type;                 /*Is it Start/Value message?*/
    long from_pe, to_pe;       /*From & To which processor?*/
	#if !PLATFORM_WIN
	    long padding; /*Compatibility with size generated by Windows compiler*/
	#endif
    RVALUE_TYPE val;           /*If Value message, contains the value*/
    TM_Time old_lbts;          /*LBTS computed in previous snapshot/epoch*/
    TM_TimeQual old_qual;      /*LBTS qualification in previous snapshot/epoch*/
    struct TMMesgStruct *next; /*Scratch for linking msg buffers*/
} TMMesg;

/*----------------------------------------------------------------------------*/
#define hton_TMMesg(/*TMMesg **/m) \
    do{ \
        (m)->ssn = htonl((m)->ssn); \
        (m)->trial = htonl((m)->trial); \
        (m)->type = htonl((m)->type); \
        (m)->from_pe = htonl((m)->from_pe); \
        (m)->to_pe = htonl((m)->to_pe); \
        hton_RVALUE_TYPE(&(m)->val); \
        hton_TM_Time(&(m)->old_lbts); \
        hton_TM_TimeQual(&(m)->old_qual); \
    }while(0)
#define ntoh_TMMesg(/*TMMesg **/m) \
    do{ \
        (m)->ssn = ntohl((m)->ssn); \
        (m)->trial = ntohl((m)->trial); \
        (m)->type = ntohl((m)->type); \
        (m)->from_pe = ntohl((m)->from_pe); \
        (m)->to_pe = ntohl((m)->to_pe); \
        ntoh_RVALUE_TYPE(&(m)->val); \
        ntoh_TM_Time(&(m)->old_lbts); \
        ntoh_TM_TimeQual(&(m)->old_qual); \
    }while(0)

/*----------------------------------------------------------------------------*/

/*----------------------------------------------------------------------------*/
typedef struct
{
    long ID;            /*Current epoch number (same as next snapshot ID)*/
    long nsent, nrecd;  /*Event counters for next (immediate) snapshot*/

    /*Special case data; see TMMRed_In()*/
    long next_id;       /*ID of next epoch*/
    long next_nrecd;    /*Number of incoming messages in the next epoch*/
} Epoch;

/*----------------------------------------------------------------------------*/
typedef struct
{
    int active;        /*Is this snapshot computation in progress now? */
    long ID;           /*Active (or most recently completed) snapshot number*/
    long trial;        /*Trial number within active snapshot computation*/
    RMUserHandle rh;   /*Handle for the reduction service*/
    RVALUE_TYPE value; /*Reduction value reported in currently active trial*/
    RVALUE_TYPE transients; /*Transient msgs of this snapshot accumulated here*/
    struct
    {
      int do_timeout;  /*Should timeouts be turned on or off?*/
      int counter;     /*#ticks so far, before starting to check timeout*/
      int max_count;   /*Start checking for timeout time after this many ticks*/
      TIMER_TYPE start;/*When did this snapshot computation start?*/
      double period;   /*Timeout this snapshot after this many seconds*/
    } timeout;
} Snapshot;

/*----------------------------------------------------------------------------*/
typedef struct
{
    TM_Time LBTS;           /*Most recently known value; updated via snapshots*/
    TM_TimeQual qual;       /*Qualification of recent LBTS; see TM_TimeQual*/
    TM_LBTSStartedProc sproc;/*Callback for getting this PE's snap shot value*/
    TM_LBTSDoneProc *dproc; /*List of callbacks waiting for new LBTS value*/
    int max_dproc;          /*Limit on #callbacks that can wait for new LBTS*/
    int n_dproc;            /*Actual #callbacks waiting for new LBTS*/
} LBTSInfo;

/*----------------------------------------------------------------------------*/
typedef struct
{
    unsigned int fmh;       /*FM handle ID*/
    int use_udp;            /*Should UDP used for TM messages?*/
    TMMesg *buffered_msgs;  /*Msgs for next epoch/trial, but arrived early*/
    TMMesg *free_msgs;      /*Free pool of message buffers*/
    double loss_prob;       /*Probability of losing any incoming TM message*/
    double msg_delay;       /*Average network latency for any TM message*/
} Communication;

/*----------------------------------------------------------------------------*/
typedef struct
{
    TIMER_TYPE start; /*When did TMMRed_Init() end?*/
    long nlbts;        /*Total number of LBTS computations so far*/
    struct
    {
	long max;      /*Max #trials per LBTS completion*/
	long tot;      /*Sum total of #trials across all LBTS computations*/
    } trial;
} Statistics;

/*----------------------------------------------------------------------------*/
typedef struct
{
    int debug;              /*Debugging level*/
    int myid;               /*My processor ID*/
    int N;                  /*Total number of processors*/
    Epoch epoch;            /*Current (active) epoch information*/
    Snapshot sshot;         /*Active (or most recently completed) snapshot*/
    LBTSInfo lbts;          /*How LBTS values must be acquired/stored/reported*/
    Communication comm;     /*Message send/receive/buffer information*/
    Statistics stats;       /*Self-explanatory*/
} TMState;

/*----------------------------------------------------------------------------*/
/* Global TM state, and cached pointers into the global TM state              */
/* In multi-threaded implementation, pass TM state as argument to API calls,  */
/* and move the cached pointers into the functions (as local variables).      */
/*----------------------------------------------------------------------------*/
static TMState tm_state;
static TMState *st;
static Epoch *epoch;
static Snapshot *sshot;
static LBTSInfo *lbts;
static Communication *comm;
static Statistics *stats;

/*----------------------------------------------------------------------------*/

/*----------------------------------------------------------------------------*/
static int recv_msg( FM_stream *strm, unsigned senderID );
static void send_start_msg( RMUserHandle usr, void *closure,
                            int from_pe, int to_pe );
static void send_value_msg( RMUserHandle usr, void *closure,
                            int from_pe, int to_pe, RVALUE_TYPE *v );
static void continue_active_reduction( void );
static int deliver_buffered_msgs( void );
static void move_to_next_sshot_trial( void );
static void printstats( void );
static void printstate( void );

/*----------------------------------------------------------------------------*/
static void TMMRed_Init(TMM_Closure closure, TMM_LBTSStartedProc sproc)
{
    char *dbgstr = getenv("TMRED_DEBUG");     /* Integer [1,inf] */
    char *lossstr= getenv("TMRED_LPROB");     /* Double  [0.0,1.0] */
    char *delaystr= getenv("TMRED_MSGDELAY"); /* Double  [0.0,inf] secs */
    char *useudpstr= getenv("TMRED_USEUDP");  /* String TRUE or FALSE */

    MYASSERT( closure == &tm_state, ("Closures should match") );

    st = &tm_state;
    epoch = &st->epoch;
    sshot = &st->sshot;
    lbts = &st->lbts;
    comm = &st->comm;
    stats = &st->stats;

    st->debug            = dbgstr?atoi(dbgstr):0;
    st->myid             = (int) FM_nodeid;
    st->N                = (int) FM_numnodes;

    epoch->ID            = 0;
    epoch->nsent         = 0;
    epoch->nrecd         = 0;
    epoch->next_id       = 1;
    epoch->next_nrecd    = 0;

    sshot->active        = FALSE;
    sshot->ID            = -1;
    sshot->trial         = -1;
    sshot->rh            = rm_register( st->N, st->myid, MAX_PE, &rv_class );

    sshot->timeout.do_timeout= TRUE;
    sshot->timeout.counter   = 0;
    sshot->timeout.max_count = 10;
    sshot->timeout.period    = 2.0; /*Updated later*/

    lbts->LBTS           = 0;
    lbts->qual           = TM_TIME_QUAL_INCL;
    lbts->sproc          = sproc;
    lbts->max_dproc      = 1000;
    lbts->n_dproc        = 0;
    lbts->dproc          = (TM_LBTSDoneProc *)malloc(
                           sizeof(TM_LBTSDoneProc)*lbts->max_dproc);

    comm->fmh            = -1;
    comm->use_udp        = (useudpstr&&!strcmp(useudpstr,"TRUE"));
    comm->buffered_msgs  = 0;
    comm->free_msgs      = 0;
    comm->loss_prob      = lossstr?atof(lossstr):0.0;
    comm->msg_delay      = delaystr?atof(delaystr):0.5;

if( comm->loss_prob <= 0 ) sshot->timeout.do_timeout = FALSE;

    stats->nlbts         = 0;
    stats->trial.max     = 1;
    stats->trial.tot     = 0;

    rv_init( &sshot->value );
    rv_init( &sshot->transients );

    FML_RegisterHandler( &comm->fmh, recv_msg );

    sshot->timeout.period = 2*(st->N-1)*comm->msg_delay;/*Worst case per trial*/

    FML_Barrier();

    TIMER_NOW(stats->start);

if(getenv("TMRED_UDPALWAYS")){FM_SetTransport(FM_TRANSPORT_UNRELIABLE);comm->use_udp=1;printf("--TMRED_UDPALWAYS---\n");fflush(stdout);}
if(st->debug)printf("-- PE %d: TMRED_LPROB=%g, TMRED_MSGDELAY=%g\n", st->myid, comm->loss_prob, comm->msg_delay);

if(st->debug>0){printf("%d: TMRed initialized.\n",st->myid);fflush(stdout);}
}

/*----------------------------------------------------------------------------*/
static void TMMRed_SetLBTSStartProc( TMM_Closure closure,
		TM_LBTSStartedProc started_proc )
{
    lbts->sproc = started_proc;
}

/*----------------------------------------------------------------------------*/
static long TMMRed_CurrentEpoch( TMM_Closure closure )
{
    return epoch->ID;
}

/*----------------------------------------------------------------------------*/
static void TMMRed_Recent_LBTS( TMM_Closure closure, TM_Time *pts )
{
    if( pts ) *pts = lbts->LBTS;
}

/*----------------------------------------------------------------------------*/
static long TMMRed_StartLBTSSnapShot( TM_Time min_ts, TM_TimeQual qual,
    TM_LBTSDoneProc done_proc, long *ptrans,
    long expected_sshot_id, long trial_num )
{
    MYASSERT( sshot->ID+1 == epoch->ID, ("!") );
    MYASSERT( TM_LE( lbts->LBTS, min_ts ), ("%lf %lf", (double)lbts->LBTS, (double)min_ts) );

    if( sshot->active )
    {
if(st->debug>2){printf("TMMRed_StartLBTSSnapShot(min_ts=%lf,sshot-epoch=%ld,sshot-trial=%ld,curr-epoch=%ld)\n",(double)min_ts,sshot->ID,sshot->trial,epoch->ID);}

        MYASSERT( sshot->ID+1 == epoch->ID,      ("!");printstate() );
	MYASSERT( sshot->ID+2 == epoch->next_id, ("!");printstate() );
    }
    else
    {
        /*Need to take a new snapshot and start reduction*/
	MYASSERT( expected_sshot_id == epoch->ID, ("!") );

        /*Transfer info from current epoch to snapshot, and prime reduction*/
        {
            sshot->active = TRUE;
            sshot->ID = epoch->ID;
            sshot->trial = trial_num-1;
	    rv_init( &sshot->value );
	    rv_set(&sshot->transients, min_ts,qual, epoch->nsent, epoch->nrecd);
	    move_to_next_sshot_trial();
        }

        /*Move to new epoch*/
        {
	    MYASSERT( epoch->next_id == epoch->ID+1, ("!");printstate() );

            ++epoch->ID;
            epoch->nsent = 0;
            epoch->nrecd = epoch->next_nrecd;

	    ++epoch->next_id;
	    epoch->next_nrecd = 0;
        }
if(st->debug>1){printf("TMMRed_StartLBTSSnapShot(min_ts=%lf,new-epoch=%ld)\n",(double)min_ts,epoch->ID);}

	MYASSERT( lbts->n_dproc <= 0, ("!");printstate(); printstats() );
        lbts->n_dproc = 0; /*Clear current callbacks*/
    }

    MYASSERT( sshot->active, ("!") );
if(0)/*XXX*/    MYASSERT( TM_LE( sshot->value.val, min_ts ),
	      ("%lf %lf",(double)sshot->value.val,(double)min_ts) );

    /*Add given procedure to the list of callbacks for the active computation*/
    if( done_proc )
    {
        MYASSERT( lbts->n_dproc < lbts->max_dproc, ("!") );
        lbts->dproc[lbts->n_dproc++] = done_proc;
    }

    if(ptrans) *ptrans = sshot->ID;

    return TM_SUCCESS;
}

/*----------------------------------------------------------------------------*/
static long TMMRed_StartLBTS( TMM_Closure closure,
		TM_Time min_ts, TM_TimeQual qual,
		TM_LBTSDoneProc done_proc, long *ptrans )
{
    return TMMRed_StartLBTSSnapShot(min_ts,qual,done_proc,ptrans,epoch->ID,0);
}

/*----------------------------------------------------------------------------*/
/*Used to start LBTS when it is inferred that a new epoch/LBTS must be started*/
/*----------------------------------------------------------------------------*/
static void remote_start_lbts( long sshot_id, long trial_num )
{
    TM_Time min_ts = 0;
    TM_TimeQual qual = TM_TIME_QUAL_INCL;
    long sproc_flag;
    TM_LBTSDoneProc dproc;

if(st->debug>1)printf("**** Starting remotely initiated LBTS\n");
if(st->debug>3)printstate();

    MYASSERT( !sshot->active, ("!") );
    MYASSERT( lbts->sproc, ("!") );
    sproc_flag = lbts->sproc( epoch->ID, &min_ts, &qual, &dproc );
    if( sproc_flag == TM_DEFER ) { min_ts = lbts->LBTS; qual = lbts->qual; }
    TMMRed_StartLBTSSnapShot( min_ts, qual, dproc, NULL, sshot_id, trial_num );

if(st->debug>3)printf("**** Done starting remotely initiated LBTS.\n");
if(st->debug>3)printstate();
}

/*----------------------------------------------------------------------------*/
static void TMMRed_PutTag( TMM_Closure closure, char *ptag, int *nbytes )
{
    *((long *)ptag) = epoch->ID;
    *nbytes = sizeof(epoch->ID);
if(st->debug>1){printf("TMMRed_PutTag(epoch=%ld)\n",epoch->ID);}
}

/*----------------------------------------------------------------------------*/
static void TMMRed_Out( TMM_Closure closure, TM_Time ts, long nevents )
{
    epoch->nsent += nevents;
if(st->debug>1){printf("TMMRed_Out(ts=%lf,nevents=%ld,epoch=%ld);epoch->nsent=%ld\n",(double)ts,nevents,epoch->ID,epoch->nsent);}
}

/*----------------------------------------------------------------------------*/
static void TMMRed_In( TMM_Closure closure, TM_Time ts, char *ptag, int *nbytes)
{
    long epoch_id = *((long*)ptag);
    *nbytes = sizeof(epoch_id);

if(st->debug>1){printf("TMMRed_In(ts=%lf,epoch=%ld,curr-epoch=%ld)\n",(double)ts,epoch_id,epoch->ID);}

    if( epoch_id == epoch->ID )
    {
if(st->debug>2){printf("IncomingRegularMsg(ts=%lf,epoch=%ld)\n",(double)ts,epoch_id);}
        /*Belongs to current epoch*/
        ++epoch->nrecd;
    }
    else if( !sshot->active && epoch_id < epoch->ID )
    {
if(st->debug>1){printf("IncomingOldMsg(ts=%lf,epoch=%ld)\n",(double)ts,epoch_id);}

        /* Ignore this message -- this is possible in the case in which  */
	/* TSO messages can be lost; TM will try to advance LBTS even if */
	/* some transient messages are still not received, after waiting */
	/* for a while for those messages. */

	/* The RTI should drop this message on the floor, because this */
	/* message was presumed lost in the earlier LBTS computation */
    }
    else if( sshot->active && epoch_id == sshot->ID )
    {
        RVALUE_TYPE transient_msg;
        rv_set( &transient_msg, ts, TM_TIME_QUAL_INCL, 0, 1 );
        rv_reduce( &sshot->transients, &transient_msg );
if(st->debug>2){printf("IncomingTransientMsg(ts=%lf,epoch=%ld)\n",(double)ts,epoch_id);}
    }
    else if( epoch_id == epoch->ID+1 )
    {
        if( sshot->active )
	{
if(st->debug>1){printf("Future epoch; must buffer info.\n");printstate();}

	    MYASSERT( epoch_id == epoch->next_id, ("!");printstate() );
	    MYASSERT( epoch_id == sshot->ID+2,    ("!");printstate() );

	    ++epoch->next_nrecd;
	}
	else
	{
if(st->debug>2){printf("Future epoch; must start new LBTS.\n");printstate();}

	    /*Start a new epoch+snapshot at this processor*/
            remote_start_lbts( epoch->ID, 0 );

	    MYASSERT( sshot->active, ("!");printstate() );
	    MYASSERT( epoch_id == epoch->ID, ("!");printstate() );

	    /*NOW, we have made sure this mesg belongs to current epoch!*/
	    ++epoch->nrecd;
	}
    }
    else
    {
        MYASSERT(FALSE,("TMMRed_In:Unexpected epoch %ld\n",epoch_id);printstate());
    }
}

/*----------------------------------------------------------------------------*/
static int tot_tm_msgs, nlost_tm_msgs;

/*----------------------------------------------------------------------------*/
static void printstats( void )
{
    TIMER_TYPE stop;
    double secs;

    TIMER_NOW(stop);
    secs = TIMER_DIFF(stop, stats->start);

    printf("TMMRED-Stastics\n");
    printf("-----------\n");
    printf("NEpochs=             %ld\n", epoch->ID-1);
    printf("NLBTS=               %ld\n", stats->nlbts);
    printf("Tot-trials=          %ld\n", stats->trial.tot-1);
    printf("Max-trials-per-LBTS= %ld\n", stats->trial.max);
    printf("Avg-trials-per-LBTS= %g\n",((double)stats->trial.tot-1)/stats->nlbts);
    printf("Time-per-trial=      %16.8f microsecs\n",secs/stats->trial.tot*1e6);
    printf("Time-per-LBTS=       %16.8f microsecs\n", secs/stats->nlbts*1e6);
    printf("NLBTS-per-sec=       %16.8f\n", stats->nlbts/secs);
    printf("-----------\n");

if(nlost_tm_msgs>0)printf( "Total TM messages = %d, lost = %d, P(loss) = %g\n", tot_tm_msgs, nlost_tm_msgs, nlost_tm_msgs/(double)tot_tm_msgs );
}

/*----------------------------------------------------------------------------*/
static void TMMRed_PrintStats( TMM_Closure closure ) { printstats(); }

/*----------------------------------------------------------------------------*/
static void print_tm_mesg(FILE *fp, TMMesg *m)
{
    fprintf(fp, "{ssn=%ld, trial=%ld, type=%ld, from_pe=%ld, to_pe=%ld, val=",
            m->ssn, m->trial, m->type, m->from_pe, m->to_pe );
    rv_print( fp, &m->val );
    fprintf( fp, ", old_lbts=%lf, next=%p}", (double)m->old_lbts, m->next );
}

/*----------------------------------------------------------------------------*/
static void printstate( void )
{
    int i = 0;

    printf("------------  TMMRED STATE START ------------\n");
    printf("myid=%d, N=%d\n", st->myid, st->N);

    printf("Epoch={ID=%ld, nsent=%ld, nrecd=%ld}\n",
           epoch->ID, epoch->nsent, epoch->nrecd);

    printf("Snapshot={active=%d, ID=%ld, trial=%ld, rh=%p, ",
           sshot->active, sshot->ID, sshot->trial, sshot->rh);
    printf("value="); rv_print(stdout, &sshot->value); printf(", ");
    printf("transients="); rv_print(stdout, &sshot->transients);
    printf("timeout{do_timeout=%s, counter=%d, max_count=%d, period=%f secs} ",
           (sshot->timeout.do_timeout ? "TRUE" : "FALSE"),
	   sshot->timeout.counter, sshot->timeout.max_count,
	   sshot->timeout.period);
    printf("}\n");

    printf("LBTS={LBTS=%lf (%s) sproc=%p, max_dproc=%d, n_dproc=%d, dproc=[",
           (double)lbts->LBTS, TM_TIME_QUAL_STR(lbts->qual),
	   lbts->sproc, lbts->max_dproc, lbts->n_dproc);
    for(i=0; i<lbts->n_dproc; i++) printf("%s%p", (i>0?",":"!"),lbts->dproc[i]);
    printf("]}\n");

    printf("Comm={fmh=%d, use_udp=%s, buffered_msgs=%p, free_msgs=%p"
           ", loss_prob=%f, msg_delay=%f secs}\n",
           comm->fmh, comm->use_udp?"TRUE":"FALSE",
	   comm->buffered_msgs, comm->free_msgs,
	   comm->loss_prob, comm->msg_delay);
    {
      TMMesg *m=comm->buffered_msgs;
      while(m){printf("\t");print_tm_mesg(stdout,m);printf("\n");m=m->next;}
    }

    printf("Stats={nlbts=%ld, trial{max=%ld,tot=%ld}}\n",
	   stats->nlbts, stats->trial.max, stats->trial.tot);

    printf("\n");
    printf("------------  TMMRED STATE END ------------\n");
}

/*----------------------------------------------------------------------------*/
static void TMMRed_PrintState( TMM_Closure closure ) { printstate(); }

/*----------------------------------------------------------------------------*/
static void TMMRed_Tick( TMM_Closure closure )
{
    int ndelivered = 0;
    do
    {
        continue_active_reduction();
        ndelivered = deliver_buffered_msgs();
    }while(ndelivered > 0);
}

/*----------------------------------------------------------------------------*/
static void TMMRed_NotifyNewLBTS(TMM_Closure closure, TM_Time ts,TM_TimeQual q)
{
    /*XXX TBC*/
}

/*----------------------------------------------------------------------------*/
static void move_to_next_sshot_trial( void )
{
    MYASSERT( sshot->active, ("!") );

    ++sshot->trial;
    rv_reduce( &sshot->value, &sshot->transients ); /*Update value*/
    rv_init( &sshot->transients ); /*Reset*/

    if( sshot->timeout.do_timeout )
    {
        sshot->timeout.counter = 0;
        TIMER_NOW( sshot->timeout.start );
    }

    ++stats->trial.tot;
    if(sshot->trial > stats->trial.max) {stats->trial.max = sshot->trial;}

    rm_init( sshot->rh, RM_SCHEDULE_GROUPED_BFLY );
    rm_receive_value( sshot->rh, st->myid, &sshot->value );
}

/*----------------------------------------------------------------------------*/
static void terminate_active_sshot( TM_Time new_lbts, TM_TimeQual new_qual )
{
    MYASSERT( sshot->active, ("!") );
    MYASSERT( TM_LE( lbts->LBTS, new_lbts ), ("LBTS must not decrease.") );

    sshot->active = FALSE;
    lbts->LBTS = new_lbts;
    lbts->qual = new_qual;

    /*Report to waiting callbacks*/
    {
        int c;
        for( c = 0; c < lbts->n_dproc; c++ )
        {
            lbts->dproc[c]( lbts->LBTS, lbts->qual, sshot->ID );
        }
        lbts->n_dproc = 0;
    }

    ++stats->nlbts;
}

/*----------------------------------------------------------------------------*/
static void continue_active_reduction( void )
{
    if( sshot->active )
    {
        RVALUE_TYPE dval;
	int timedout = FALSE;
        int done=rm_resume(sshot->rh, &dval, send_start_msg, send_value_msg, 0);

	/*Check if trial's taking too long (i.e.must be timedout?)*/
        if( !done && sshot->timeout.do_timeout )
	{
	    /*Performance opt: Poll the timer only after several initial ticks*/
	    if( sshot->timeout.counter < sshot->timeout.max_count )
	    {
	        ++sshot->timeout.counter;
	    }
	    else
	    {
	        TIMER_TYPE now;
		TIMER_NOW(now);
		if( TIMER_DIFF( now, sshot->timeout.start ) >
		    sshot->timeout.period )
		{
		    /*This trial is timedout*/
		    timedout = TRUE;
		}
	    }
	}

        if( done || timedout ) /*Current trial done or timedout*/
        {
if(st->debug>0){printf("TMMRed: %s snapshot %ld trial %ld: ",(done?"Done":"Timedout"),sshot->ID,sshot->trial);rv_print(stdout,&dval); printf("\n"); fflush(stdout);}
if(st->debug>2) printstate();

            if( done && (dval.nsent == dval.nrecd) ) /*Current snapshot done!*/
            {
	        terminate_active_sshot( dval.val, dval.qual );
            }
            else /*Snapshot incomplete or timedout; start next trial*/
            {
	        move_to_next_sshot_trial();
            }
        }
    }
}

/*----------------------------------------------------------------------------*/
static int deliver_buffered_msgs( void )
{
    TMMesg **b = &comm->buffered_msgs;
    int ndelivered = 0;

    while( *b )
    {
        TMMesg *msg = *b;
        int deliver = FALSE, consume = TRUE;

        if( sshot->active )
        {
            if( msg->ssn == sshot->ID )
            {
                if( msg->trial == sshot->trial )
                {
                    deliver = TRUE;
                }
                else
                {
                    if( msg->trial < sshot->trial )
                    {
                        /*Ignore outdated msg*/
                    }
                    else
                    {
                        /*Future message; retain in queue*/
                        consume = FALSE;
                    }
                }
            }
            else
            {
                if( msg->ssn < sshot->ID )
                {
                    /*Ignore outdated msg*/
                }
                else /*This must be a Start/Value msg from next epoch*/
                {
		    MYASSERT( msg->ssn==sshot->ID+1, ("Must be of next epoch") );

		    /*No point continuing currently active snapshot*/
		    /*since we have the LBTS as sent by the neighbor PE!*/

if(st->debug>1){printf("!!------------ Skipping currently active snapshot!\n");printf("Recd msg:");print_tm_mesg(stdout,msg);printf("\n");printstate();}

		    if( sshot->timeout.do_timeout )
		    terminate_active_sshot( msg->old_lbts, msg->old_qual );

                    /*Future message; retain in queue*/
                    consume = FALSE;
                }
            }
        }
        else
        {
            if( msg->ssn == epoch->ID )
            {
                /*Take new snapshot and start LBTS computation*/
		remote_start_lbts( msg->ssn, msg->trial );

		MYASSERT( sshot->active &&
			msg->ssn == sshot->ID &&
		        msg->trial == sshot->trial,
			("!");print_tm_mesg(stderr,msg);printstate() );

                deliver = TRUE; /*And, deliver this msg to new reduction*/
            }
            else
            {
                if( msg->ssn < epoch->ID )
                {
                    /*Ignore outdated msg*/
                }
                else
                {
                    /*Future message; retain in queue*/
                    consume = FALSE;
                }
            }
        }

        if( deliver )
        {
            MYASSERT( msg->ssn == sshot->ID && msg->trial == sshot->trial, ("!"));
            switch( msg->type )
            {
                case RM_START_MSG:
                {
                    rm_receive_start( sshot->rh, msg->from_pe );
                    break;
                }
                case RM_VALUE_MSG:
                {
                    rm_receive_value( sshot->rh, msg->from_pe, &msg->val );
                    break;
                }
                default:
                {
                    MYASSERT( FALSE, ("!") ); /*Unknown type*/
                    break;
                }
            }
        }

        if( consume )
        {
if(st->debug>3){printf("Consuming msg:");print_tm_mesg(stdout,msg);printf("\n");}
            { *b = msg->next; }
            { msg->next = comm->free_msgs; comm->free_msgs = msg; }
        }
        else /*retain msg in queue*/
        {
if(st->debug>3){printf("Retaining msg:");print_tm_mesg(stdout,msg);printf("\n");}
            { b = &((*b)->next); }
        }
    }
if(st->debug>4) if(ndelivered>0) {printf("+++ Delivered %d msgs\n",ndelivered);printstate();}

    return ndelivered;
}

/*----------------------------------------------------------------------------*/
static void send_msg( TMMesg *msg )
{
    FM_stream *strm = 0;
    FM_Transport prev_transport;

    MYASSERT( sshot->active, ("!") );

    msg->ssn = sshot->ID; msg->trial = sshot->trial;
    msg->old_lbts = lbts->LBTS;
    msg->old_qual = lbts->qual;

    prev_transport = FM_GetTransport();
    FM_SetTransport( comm->use_udp ?
                     FM_TRANSPORT_UNRELIABLE : FM_TRANSPORT_RELIABLE );

    strm = FM_begin_message( msg->to_pe, sizeof(TMMesg), comm->fmh );
    hton_TMMesg( msg );
    FM_send_piece( strm, msg, sizeof(*msg) );
    FM_end_message( strm );

    FM_SetTransport( prev_transport );
}

/*----------------------------------------------------------------------------*/
static void send_start_msg( RMUserHandle usr, void *closure,
    int from_pe, int to_pe )
{
    TMMesg msg;
    msg.type = RM_START_MSG; msg.from_pe = from_pe; msg.to_pe = to_pe;
    rv_init( &msg.val );
    send_msg( &msg );
}

/*----------------------------------------------------------------------------*/
static void send_value_msg( RMUserHandle usr, void *closure,
    int from_pe, int to_pe, RVALUE_TYPE *v )
{
    TMMesg msg;
    msg.type = RM_VALUE_MSG; msg.from_pe = from_pe; msg.to_pe = to_pe;
    rv_assign( &msg.val, v );
    send_msg( &msg );
}

/*----------------------------------------------------------------------------*/
static TMMesg *allocate_free_msgs( int nmsgs )
{
    int i = 0;
    TMMesg *first = 0, **b = &first;
    for( i = 0; i < nmsgs; i++ )
    {
        *b = (TMMesg *)malloc( sizeof(TMMesg) );
        if( *b ) { b = &((*b)->next); *b = 0; }
    }
    if(st->debug)printf("Allocated %d free TM buffers.\n",nmsgs);
    return first;
}

/*----------------------------------------------------------------------------*/
static int recv_msg( FM_stream *strm, unsigned senderID )
{
    TMMesg msg;

    FM_receive( &msg, strm, sizeof(msg) );
    ntoh_TMMesg( &msg );

if(st->debug>2){printf("recv_msg: "); print_tm_mesg(stdout,&msg); printf("\n");}

    if( sshot->active && msg.ssn != sshot->ID ) /*Debugging/Testing*/
    {
if(st->debug>0){printf("***Type %ld ID mismatch: %ld %s %ld\n", msg.type, msg.ssn, ((msg.ssn < sshot->ID)?"<":">"), sshot->ID);}
    }

++tot_tm_msgs;

    if( !(0<=msg.from_pe && msg.from_pe<st->N &&
          0<=msg.to_pe && msg.to_pe<st->N &&
          msg.to_pe == st->myid &&
          (msg.type==RM_START_MSG || msg.type==RM_VALUE_MSG)) )
    {
        printf( "***TM error on read: %ld, %ld\n", msg.from_pe, msg.to_pe );
    }
    else
if(FALSE&&(comm->loss_prob>0.0)&&((rand()%((int)(1/comm->loss_prob)))==0)){if(st->debug>0){printf("Dropped TM mesg:");print_tm_mesg(stdout,&msg);printf("\n");}nlost_tm_msgs++;}else
    {
        /*Buffer this message*/
        TMMesg *mbuf = 0;

        /*Get a free buffer and copy the contents to it*/
        {
            if( !comm->free_msgs )
                comm->free_msgs = allocate_free_msgs(100);
            MYASSERT( comm->free_msgs, ("!") );
            mbuf = comm->free_msgs;
            comm->free_msgs = mbuf->next;

            *mbuf = msg;
        }

        /*Append to queue*/
        {
            TMMesg **b = &comm->buffered_msgs;
            while(*b) b = &((*b)->next);
            *b = mbuf;
            mbuf->next = 0;
        }
    }

    return FM_CONTINUE;
}

/*----------------------------------------------------------------------------*/
void TMMRed_AddModule( void )
{
    TMModule mod = {
        &tm_state,
	TMMRed_Init,
	TMMRed_CurrentEpoch,
	TMMRed_Recent_LBTS,
	TMMRed_StartLBTS,
	TMMRed_PutTag,
	TMMRed_Out,
	TMMRed_In,
	TMMRed_PrintStats,
	TMMRed_PrintState,
	TMMRed_Tick,
	TMMRed_NotifyNewLBTS
    };

    TM_AddModule( &mod );
}

/*----------------------------------------------------------------------------*/
