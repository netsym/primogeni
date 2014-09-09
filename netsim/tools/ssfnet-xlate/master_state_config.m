
state_configuration {
	__PRIME__T1__="ProtocolSession";
	__PRIME__T2__="BaseEntity";
	__PRIME__T1_TPL__="";
	__PRIME__T2_TPL__="";
	__PRIME__TYPEID__="";
	__PRIME__ALIAS__="";
	__PRIME__FILENAME__="/home/obaida/Desktop/pgc2vega/primex/netsim/src/ssfnet/os/protocol_session.h";

	__PRIME__EMPTY__;
	};
state_configuration {
	__PRIME__T1__="Alias";
	__PRIME__T2__="BaseEntity";
	__PRIME__T1_TPL__="";
	__PRIME__T2_TPL__="";
	__PRIME__TYPEID__=" 0";
	__PRIME__ALIAS__= "NodeAlias";
	__PRIME__FILENAME__="/home/obaida/Desktop/pgc2vega/primex/netsim/src/ssfnet/os/alias.h";

		configurable RelativePath alias_path {
			type=OBJECT;
			default_value="";
			doc_string="the relative path to the node to which this alias points";
			visualized=true;
		};
		/**
		 * This is the node which this alias is a shortcut to.
		 */
		BaseEntity* shortcut_to;
	};

state_configuration {
	__PRIME__T1__="PlaceHolder";
	__PRIME__T2__="BaseEntity";
	__PRIME__T1_TPL__="";
	__PRIME__T2_TPL__="";
	__PRIME__TYPEID__="";
	__PRIME__ALIAS__="";
	__PRIME__FILENAME__="/home/obaida/Desktop/pgc2vega/primex/netsim/src/ssfnet/os/placeholder.h";

	__PRIME__EMPTY__;
	};
state_configuration {
	__PRIME__T1__="RoutingSphere";
	__PRIME__T2__="BaseEntity";
	__PRIME__T1_TPL__="";
	__PRIME__T2_TPL__="";
	__PRIME__TYPEID__="";
	__PRIME__ALIAS__="";
	__PRIME__FILENAME__="/home/obaida/Desktop/pgc2vega/primex/netsim/src/ssfnet/os/routing.h";

		shared configurable uint32_t nix_vec_cache_size {
			type=INT;
			default_value="1000";
			doc_string="The maximum number of nix vector cache entries for this sphere.";
			visualized=true;
		};
		shared configurable uint32_t local_dst_cache_size {
			type=INT;
			default_value="1000";
			doc_string="The maximum number of local dst cache entries for this sphere.";
			visualized=true;
		};
		child_type<RouteTable> default_route_table {
			min_count = 0;
			max_count = 1;
			doc_string = "default routing table";
		};

		HostSrcRankListPairMap sub_edge_iface_map;
	};
state_configuration {
	__PRIME__T1__="GhostRoutingSphere";
	__PRIME__T2__="RoutingSphere";
	__PRIME__T1_TPL__="";
	__PRIME__T2_TPL__="";
	__PRIME__TYPEID__="";
	__PRIME__ALIAS__="";
	__PRIME__FILENAME__="/home/obaida/Desktop/pgc2vega/primex/netsim/src/ssfnet/os/routing.h";

		configurable UID_t community_id {
			type = INT;
			default_value =  "0" ;
			doc_string = "the id of the community where the real sphere is";
			visualized=true;
		};
	};
state_configuration {
	__PRIME__T1__="RouteTable";
	__PRIME__T2__="BaseEntity";
	__PRIME__T1_TPL__="";
	__PRIME__T2_TPL__="";
	__PRIME__TYPEID__="";
	__PRIME__ALIAS__="";
	__PRIME__FILENAME__="/home/obaida/Desktop/pgc2vega/primex/netsim/src/ssfnet/os/routing.h";

		shared configurable EdgeInterfaceList edge_ifaces {
			type=OBJECT;
			default_value="[]";
			doc_string="A comma separated list of edge ifaces";
		};

		shared RTREE routemap ;

		EdgeInterfaceList my_edge_ifaces ;

	};
state_configuration {
	__PRIME__T1__="ProtocolGraph";
	__PRIME__T2__="BaseEntity";
	__PRIME__T1_TPL__="";
	__PRIME__T2_TPL__="";
	__PRIME__TYPEID__="";
	__PRIME__ALIAS__="";
	__PRIME__FILENAME__="/home/obaida/Desktop/pgc2vega/primex/netsim/src/ssfnet/os/protocol_graph.h";

		shared configurable bool automatic_protocol_session_creation {
			type= BOOL;
			default_value= true;
			doc_string= "whether protocol sessions should be created on demand";
		};

		/** Whether the protocol graph has been initialized*/
		bool initialized;

		/** Mapping between protocol names and instances of protocol sessions. */
		PROTONAME_MAP pname_map;

		/** Mapping between protocol numbers and instances of protocol sessions. */
		PROTONUM_MAP pno_map;

		/** List of protocol sessions created. */
		PSESS_VECTOR protocol_list;

		child_type<ProtocolSession> cfg_sess {
			min_count = 0;
			max_count = 0;
			is_aliased = false; // by default it is false
			doc_string = "Protocol sessions defined in the protocol graph";
		};
	};
state_configuration {
	__PRIME__T1__="TrafficType";
	__PRIME__T2__="BaseEntity";
	__PRIME__T1_TPL__="";
	__PRIME__T2_TPL__="";
	__PRIME__TYPEID__="";
	__PRIME__ALIAS__="";
	__PRIME__FILENAME__="/home/obaida/Desktop/pgc2vega/primex/netsim/src/ssfnet/os/traffic.h";

                shared configurable int traffic_type_seed {
                        type=INT;
                        default_value = "0" ;
                        doc_string = "traffic type's seed";
                };

         	   configurable CommunityIDList community_ids {
         	            type = OBJECT;
         	            default_value =  "[]" ;
         	            doc_string = "A comma separated list of the ids of the communities in this partition";
         	    };

                /**
                 * The random number generator, per traffic type. Each traffic type
                 * is expected to maintain a unique random stream, seeded initially
                 * as a function of the traffic type's UID.
                 */
                prime::rng::Random* rng = 0;

                /**
                 * If it's not zero, this is the random seed used by traffic
                 * types to initialize their random streams; if it's zero, all
                 * traffic types will shared the same random stream (rng).
                 */
                int rng_seed = 0;

        };
state_configuration {
	__PRIME__T1__="StaticTrafficType";
	__PRIME__T2__="TrafficType";
	__PRIME__T1_TPL__="";
	__PRIME__T2_TPL__="";
	__PRIME__TYPEID__="";
	__PRIME__ALIAS__="";
	__PRIME__FILENAME__="/home/obaida/Desktop/pgc2vega/primex/netsim/src/ssfnet/os/traffic.h";

        	   shared configurable ResourceIdentifier srcs {
        	            type = RESOURCE_ID;
        	            default_value =  "" ;
        	            doc_string = "XXX";
        	    };
                shared configurable ResourceIdentifier dsts {
                        type = RESOURCE_ID;
                        default_value = "" ;
                        doc_string = "XXX";
                };
                shared configurable DstIPVec dst_ips {
	 					type=OBJECT;
	 					default_value="[]";
						doc_string= "a list of emu/real nodes ips";
	 			};
                shared configurable float start_time {
                        type=FLOAT;
                        default_value="0";
                        doc_string="Time before the traffic starts (default: 0).";
                };
                shared configurable float interval {
                        type=FLOAT;
                        default_value="0.1";
                        doc_string="The time between sending each traffic (default: 0.1). ";
                };
                shared configurable bool interval_exponential {
                        type=BOOL;
                        default_value="false";
                        doc_string="Whether to use exponential interval (default: false).";
                };
                shared configurable SSFNET_STRING mapping {
						type=STRING;
						default_value="all2all";
						doc_string="The method to map the source and destination based on the src and dst lists";
                };

                int mapping;
                TrafficFlowList* traffic_flows;
                HybridTrafficFlowList* hybrid_traffic_flows;
        };

state_configuration {
	__PRIME__T1__="DynamicTrafficType";
	__PRIME__T2__="TrafficType";
	__PRIME__T1_TPL__="";
	__PRIME__T2_TPL__="";
	__PRIME__TYPEID__="";
	__PRIME__ALIAS__="";
	__PRIME__FILENAME__="/home/obaida/Desktop/pgc2vega/primex/netsim/src/ssfnet/os/traffic.h";

	__PRIME__EMPTY__;
	};
state_configuration {
	__PRIME__T1__="CentralizedTrafficType";
	__PRIME__T2__="DynamicTrafficType";
	__PRIME__T1_TPL__="";
	__PRIME__T2_TPL__="";
	__PRIME__TYPEID__="";
	__PRIME__ALIAS__="";
	__PRIME__FILENAME__="/home/obaida/Desktop/pgc2vega/primex/netsim/src/ssfnet/os/traffic.h";

        	   shared configurable ResourceIdentifier srcs {
        	            type = RESOURCE_ID;
        	            default_value =  "" ;
        	            doc_string = "XXX";
        	    };
                shared configurable ResourceIdentifier dsts {
                        type = RESOURCE_ID;
                        default_value = "" ;
                        doc_string = "XXX";
                };
        };

state_configuration {
	__PRIME__T1__="DistributedTrafficType";
	__PRIME__T2__="DynamicTrafficType";
	__PRIME__T1_TPL__="";
	__PRIME__T2_TPL__="";
	__PRIME__TYPEID__="";
	__PRIME__ALIAS__="";
	__PRIME__FILENAME__="/home/obaida/Desktop/pgc2vega/primex/netsim/src/ssfnet/os/traffic.h";

	__PRIME__EMPTY__;
	};
state_configuration {
	__PRIME__T1__="Traffic";
	__PRIME__T2__="BaseEntity";
	__PRIME__T1_TPL__="";
	__PRIME__T2_TPL__="";
	__PRIME__TYPEID__="";
	__PRIME__ALIAS__="";
	__PRIME__FILENAME__="/home/obaida/Desktop/pgc2vega/primex/netsim/src/ssfnet/os/traffic.h";

                child_type<TrafficType> traffic_types {
                        min_count = 0;
                        max_count = 0;
                        is_aliased = false;
                        doc_string = "traffic types of the traffic";
                };
        };
state_configuration {
	__PRIME__T1__="Monitor";
	__PRIME__T2__="BaseEntity";
	__PRIME__T1_TPL__="";
	__PRIME__T2_TPL__="";
	__PRIME__TYPEID__="";
	__PRIME__ALIAS__="";
	__PRIME__FILENAME__="/home/obaida/Desktop/pgc2vega/primex/netsim/src/ssfnet/os/monitor.h";

	 		shared configurable int64_t period {
	 			type=INT;
	 			default_value="500";
	 			doc_string="the period of polling (i.e. how long between polls) in milliseconds.";
	 		};
	 		shared configurable ResourceIdentifier to_monitor {
	 			type=RESOURCE_ID;
	 			default_value="";
				doc_string= "a list of entity relative ids which will have their state monitored";
	 		};
 	};
state_configuration {
	__PRIME__T1__="Aggregate";
	__PRIME__T2__="BaseEntity";
	__PRIME__T1_TPL__="";
	__PRIME__T2_TPL__="";
	__PRIME__TYPEID__="";
	__PRIME__ALIAS__="";
	__PRIME__FILENAME__="/home/obaida/Desktop/pgc2vega/primex/netsim/src/ssfnet/os/monitor.h";

			shared configurable uint32_t var_id {
	 			type=INT;
	 			default_value="0";
	 			doc_string="the id of the variable which will be aggregated.";
	 		};
	 		shared configurable ResourceIdentifier to_aggregate {
	 			type=RESOURCE_ID;
	 			default_value="";
				doc_string= "a list of entity relative ids which will have their state aggregated";
	 		};

	 		//aggregates are special and even though these values are saved per-instance
	 		//we actually shaddow them for each community since nets can exist in multiple locations
	 		shared configurable double min {
	 			type=FLOAT;
	 			default_value="0";
	 			doc_string="min of all the values at the sample time.";
				visualized=true;
				statistic=true;
	 		};
	 		shared configurable double max {
	 			type=FLOAT;
	 			default_value="0";
	 			doc_string="max of all the values at the sample time.";
				visualized=true;
				statistic=true;
	 		};
	 		shared configurable int sample_count {
	 			type=FLOAT;
	 			default_value="0";
	 			doc_string="the number of samples.";
				visualized=true;
				statistic=true;
	 		};
	 		shared configurable double sum {
	 			type=FLOAT;
	 			default_value="0";
	 			doc_string="total of all the values at the sample time.";
				visualized=true;
				statistic=true;
	 		};
	};

state_configuration {
	__PRIME__T1__="VizAggregate";
	__PRIME__T2__="Aggregate";
	__PRIME__T1_TPL__="";
	__PRIME__T2_TPL__="";
	__PRIME__TYPEID__="";
	__PRIME__ALIAS__="";
	__PRIME__FILENAME__="/home/obaida/Desktop/pgc2vega/primex/netsim/src/ssfnet/os/monitor.h";

	__PRIME__EMPTY__;
	};

state_configuration {
	__PRIME__T1__="NicQueue";
	__PRIME__T2__="BaseEntity";
	__PRIME__T1_TPL__="";
	__PRIME__T2_TPL__="";
	__PRIME__TYPEID__="";
	__PRIME__ALIAS__="";
	__PRIME__FILENAME__="/home/obaida/Desktop/pgc2vega/primex/netsim/src/ssfnet/net/nic_queue.h";

	__PRIME__EMPTY__;
	};
state_configuration {
	__PRIME__T1__="RedQueue";
	__PRIME__T2__="NicQueue";
	__PRIME__T1_TPL__="";
	__PRIME__T2_TPL__="";
	__PRIME__TYPEID__="";
	__PRIME__ALIAS__="";
	__PRIME__FILENAME__="/home/obaida/Desktop/pgc2vega/primex/netsim/src/ssfnet/net/red_queue.h";


		shared configurable float weight {
			type=FLOAT;
			default_value = "0";
			doc_string = "weight used by the AQM policy to calculate average queue length for each packet arrival";
			visualized=true;
		};
		shared configurable float qmin {
			type=FLOAT;
			default_value = "0";
			doc_string = "the min threshold (in bytes) for calculating packet drop probability";
			visualized=true;
		};
		shared configurable float qmax {
			type=FLOAT;
			default_value = "0";
			doc_string = "the max threshold (in bytes) for calculating packet drop probability";
			visualized=true;
		};
		shared configurable float qcap {
			type=FLOAT;
			default_value = "0";
			doc_string = "qcap for calculating packet drop probability";
			visualized=true;
		};
		shared configurable float pmax {
			type=FLOAT;
			default_value = "0.2";
			doc_string = "pmax for calculating packet drop probability";
			visualized=true;
		};
		shared configurable bool wait_opt {
			type=BOOL;
			default_value = "true";
			doc_string = "an option to avoid marking/dropping two packets in a row";
			visualized=true;
		};
		shared configurable int mean_pktsiz {
			type=INT;
			default_value = "500";
			doc_string = "mean packet size in bytes";
			visualized=true;
		};
		  /** Weight used by the AQM policy to calculate average queue length
		      ON EACH PACKET ARRIVAL. */
		  float weight;

		  /* Parameters to calculate packet drop probability based on the
		     average queue length x(t):
		       P(x)=0, if 0 <= x < qmin;
		       P(x)=(x-qmin)/(qmax_qmin)*pmax, if qmin <= x < qmax;
		       P(x)=(x-qmax)/(bufsize-qmax)*(1-pmax)+pmax, if x > qmax.
		     Both qmin and qmax are in bits.
		   */
		  float qmin; ///< Parameter to calculate packet drop probability.
		  float qmax; ///< Parameter to calculate packet drop probability.
		  float qcap; ///< Parameter to calculate packet drop probability.
		  float pmax; ///< Parameter to calculate packet drop probability.

		  /** This option is to avoid dropping two packets in a row. */
		  //bool wait_opt;

		  /** Mean packet size in bits. */
		  float mean_pktsiz;

		  /** The instantaneous queue size (in bits). */
		  float queue;

		  /** The average queue size (in bits). */
		  float avgque;

		  /** The loss probability calcuated from avg queue length. */
		  float loss;

		  /** False if the average queue length is crossing the min threshold
		      for the first time. */
		  bool crossing;

		  /** The number of bits arrived between two consecutive packet drops
		      (when the qmin threshold is crossed). */
		  float interdrop;

		  /** The time of the last queue update. */
		  VirtualTime last_update_time;

		  /** The time when the queue will be emptied. */
		  VirtualTime vacate_time;
	};
state_configuration {
	__PRIME__T1__="Net";
	__PRIME__T2__="BaseEntity";
	__PRIME__T1_TPL__="";
	__PRIME__T2_TPL__="";
	__PRIME__TYPEID__="";
	__PRIME__ALIAS__="";
	__PRIME__FILENAME__="/home/obaida/Desktop/pgc2vega/primex/netsim/src/ssfnet/net/net.h";

		shared configurable int8_t ip_prefix_len {
			type= INT;
			default_value= "-1";
			doc_string= "The n in W.X.Y.Z/n";
		};
		configurable IPPrefix ip_prefix {
			type= OBJECT;
			default_value= "0.0.0.0/0";
			doc_string= "ip prefix of this network";
		};
		configurable CNFContentOwnerMap cnf_content_ids {
			type= OBJECT;
			default_value= "[]";
			doc_string= "a list of content id and uid. i.e. [1,100,2,200] --> [ cid:1, uid:100],[ cid:2,uid:200]";
		};

		shared configurable IPPrefixRoute::List portal_rules {
			type= OBJECT;
			default_value= "[]";
			doc_string= "a list of rules in the form [ip_prefix->(router_uid,iface_uid,iface_ip)]. They will automatically be compiled into the topnet.";
		};

		shared configurable UID_t controller_rid {
			type=INT;
			default_value="0";
			doc_string="The RID of the cnf controller.";
		};

		shared configurable SubEdgeList sub_edge_ifaces {
			type=OBJECT;
			default_value="[]";
			doc_string="A comma separated list of its child sphere's edge iface and owning_host rank pairs";
		};

		child_type<Net> subnets {
			min_count = 0;
			max_count = 0;
			doc_string = "sub-networks defined within this network";
		};
		child_type<Host> hosts {
			min_count = 0;
			max_count = 0;
			doc_string = "hosts defined within this network";
		};
		child_type<Link> links {
			min_count = 0;
			max_count = 0;
			doc_string = "links defined within this network";
		};
		child_type<PlaceHolder> placeholders{
			min_count = 0;
			max_count = 0;
			doc_string = "a node to hold the place of children which exist on remote partitions";
		};
		child_type<RoutingSphere> rsphere {
			min_count = 1;
			max_count = 1;
			doc_string = "routing sphere defined within this network";
		};
		child_type<Monitor> monitors {
			min_count = 0;
			max_count = 0;
			doc_string = "data monitors";
		};
		child_type<Aggregate> aggregates {
			min_count = 0;
			max_count = 0;
			doc_string = "aggregate statistics";
		};
		child_type<Traffic> traffics {
			min_count = 0;
			max_count = 1;
			doc_string = "traffic defined within this network";
		};
	};
state_configuration {
	__PRIME__T1__="DropTailQueue";
	__PRIME__T2__="NicQueue";
	__PRIME__T1_TPL__="";
	__PRIME__T2_TPL__="";
	__PRIME__TYPEID__="";
	__PRIME__ALIAS__="";
	__PRIME__FILENAME__="/home/obaida/Desktop/pgc2vega/primex/netsim/src/ssfnet/net/droptail_queue.h";

		/** The time of the last transmission. */
		VirtualTime last_xmit_time;

		/** Queuing delay since the last transmission time. */
		VirtualTime queue_delay;

		/** Maximum queuing delay is bounded by the queue length. */
		VirtualTime max_queue_delay;
	};
state_configuration {
	__PRIME__T1__="Host";
	__PRIME__T2__="ProtocolGraph";
	__PRIME__T1_TPL__="";
	__PRIME__T2_TPL__="";
	__PRIME__TYPEID__="";
	__PRIME__ALIAS__="";
	__PRIME__FILENAME__="/home/obaida/Desktop/pgc2vega/primex/netsim/src/ssfnet/net/host.h";

		shared configurable int host_seed {
			type=INT;
			default_value = "0" ;
			doc_string = "host's seed";
		};

		shared configurable float time_skew {
			type=FLOAT;
			default_value = 1.0;
			doc_string = "host's time skew (ratio of host time over absolute time)";
		};

		shared configurable VirtualTime time_offset {
			type= OBJECT; // could be set as default if not primitive type
			default_value= "0.0";
			doc_string= "host's time offset (relative to absolute time zero)";
		};

		configurable float traffic_intensity {
			type= FLOAT;
			default_value="0";
			doc_string="max  [ for each nic: (bits_out/bit_rate) ]";
			visualized=true;
			statistic=true;
		};

		/** A map from IP address to interface. */
		IP2IFACE_MAP ip2iface;

		/** Caching the default IP address of this host. */
		IPAddress default_ip = IPAddress::IPADDR_INVALID;

		/** Timer queue for managing all timers. */
		HostTimerQueue* tmr_queue = 0;

		/** Mapping from timer handle to timer data. */
		SSFNET_INT2PTR_MAP* tmr_map = 0;

		/** The timer handle that has been used last time. */
		int tmr_handle = 0;

		/**
		 * The random number generator, per host. Each host is expected to
		 * maintain a unique random stream, seeded initially as a function
		 * of the host's default ip address (or UID).
		 */
		prime::rng::Random* rng = 0;

		/**
		 * If it's not zero, this is the random seed used by protocol
		 * sessions to initialize their random streams; if it's zero, all
		 * protocol sessions will shared the same random stream (rng).
		 */
		int rng_seed = 0;

		PortalIfaceTable* portals = 0;
		
		child_type<Interface> nics {
			min_count = 0;
			max_count = 0; // infinity
			is_aliased = false; // by default it is false
			doc_string = "network interfaces on this host";
		};
	};

state_configuration {
	__PRIME__T1__="Router";
	__PRIME__T2__="Host";
	__PRIME__T1_TPL__="";
	__PRIME__T2_TPL__="";
	__PRIME__TYPEID__="";
	__PRIME__ALIAS__="";
	__PRIME__FILENAME__="/home/obaida/Desktop/pgc2vega/primex/netsim/src/ssfnet/net/host.h";

	__PRIME__EMPTY__;
	};
state_configuration {
	__PRIME__T1__="BaseInterface";
	__PRIME__T2__="BaseEntity";
	__PRIME__T1_TPL__="";
	__PRIME__T2_TPL__="";
	__PRIME__TYPEID__="";
	__PRIME__ALIAS__="";
	__PRIME__FILENAME__="/home/obaida/Desktop/pgc2vega/primex/netsim/src/ssfnet/net/interface.h";

		configurable IPAddress ip_address {
			type = OBJECT;
			default_value =  "0.0.0.0" ;
			unserialize_fct = str2ip;
			serialize_fct = ip2str;
			doc_string = "ip address";
			visualized=true;
		};
		configurable MACAddress mac_address {
			type = OBJECT;
			default_value =  "0:0:0:0:0:0" ;
			unserialize_fct = str2mac;
			serialize_fct = mac2str;
			doc_string = "mac address";
			visualized=false;
		};
		/** The link this interface is attached to. */
		Link* attached_link;
	};
state_configuration {
	__PRIME__T1__="GhostInterface";
	__PRIME__T2__="BaseInterface";
	__PRIME__T1_TPL__="";
	__PRIME__T2_TPL__="";
	__PRIME__TYPEID__="";
	__PRIME__ALIAS__="";
	__PRIME__FILENAME__="/home/obaida/Desktop/pgc2vega/primex/netsim/src/ssfnet/net/interface.h";

		configurable UID_t real_uid {
			type = INT;
			default_value =  "0" ;
			doc_string = "the uid of the remote node";
			visualized=true;
		};
		configurable UID_t community_id {
			type = INT;
			default_value =  "0" ;
			doc_string = "the id of the remote community";
			visualized=true;
		};
	};
state_configuration {
	__PRIME__T1__="Interface";
	__PRIME__T2__="BaseInterface";
	__PRIME__T1_TPL__="";
	__PRIME__T2_TPL__="";
	__PRIME__TYPEID__=" 0";
	__PRIME__ALIAS__= "Iface";
	__PRIME__FILENAME__="/home/obaida/Desktop/pgc2vega/primex/netsim/src/ssfnet/net/interface.h";

		shared configurable float bit_rate {
			type = FLOAT;
			default_value =  "1e10" ;
			doc_string = "transmit speed";
			visualized=true;
		};
		shared configurable float latency {
			type = FLOAT;
			default_value =  "0";
			doc_string = "transmit latency";
			visualized=true;
		};
		shared configurable float jitter_range {
			type = FLOAT;
			default_value =  "0" ;
			doc_string = "jitter range";
			visualized=true;
		};
		
		configurable float drop_probability {
			type = FLOAT;
			default_value =  "0" ;
			doc_string = "drop probability";
			visualized=true;
			statistic=true;
		};
		
		shared configurable int buffer_size {
			type = INT;
			default_value =  "65536" ;
			doc_string = "send buffer size";
			visualized=true;
		};
		shared configurable int mtu {
			type = INT;
			default_value =  "1500" ;
			doc_string = "maximum transmission unit";
			visualized=true;
		};
		shared configurable SSFNET_STRING queue_type {
			type= STRING;
			default_value="DropTailQueue";
			doc_string="The class to use for the queue.";
			visualized=true;
		};
		configurable bool is_on {
			type= BOOL;
			default_value="true";
			doc_string="is the interface on?";
			visualized=true;
			statistic=true;
		};
		configurable int num_in_packets {
			type= INT;
			default_value="0";
			doc_string="number of packets received";
			visualized=true;
			statistic=true;
		};
		configurable int num_in_bytes {
			type= INT;
			default_value="0";
			doc_string="number of bytes received";
			visualized=true;
			statistic=true;
		};
		configurable float packets_in_per_sec {
			type= FLOAT;
			default_value="0";
			doc_string="number of packets per second";
			visualized=false;
			statistic=true;
		};
		configurable float bytes_in_per_sec {
			type= FLOAT;
			default_value="0";
			doc_string="number of bytes per second";
			visualized=false;
			statistic=true;
		};
		configurable int num_in_ucast_packets {
			type= INT;
			default_value="0";
			doc_string="number of unicast packets received";
		};
		configurable int num_in_ucast_bytes {
			type= INT;
			default_value="0";
			doc_string="number of unicast bytes received";
		};
		configurable int num_out_packets {
			type= INT;
			default_value="0";
			doc_string="number of packets sent";
			visualized=true;
			statistic=true;
		};
		configurable int num_out_bytes {
			type= INT;
			default_value="0";
			doc_string="number of bytes sent";
			visualized=true;
			statistic=true;
		};
		configurable int num_out_ucast_packets {
			type= INT;
			default_value="0";
			doc_string="number of unicast packets sent";
		};
		configurable int num_out_ucast_bytes {
			type= INT;
			default_value="0";
			doc_string="number of unicast bytes sent";
		};
		configurable float packets_out_per_sec {
			type= FLOAT;
			default_value="0";
			doc_string="number of packets per second";
			visualized=false;
			statistic=true;
		};	
		configurable float bytes_out_per_sec {
			type= FLOAT;
			default_value="0";
			doc_string="number of bytes per second";
			visualized=false;
			statistic=true;
		};

		configurable int queue_size {
			type= INT;
			default_value="0";
			doc_string="quesize";
			visualized=true;
			statistic=true;
		};

		child_type<NicQueue> nic_queue {
			min_count = 0;
			max_count = 1;
			is_aliased = false;
			doc_string = "The queue for this nic";
		};

		child_type<EmulationProtocol> emu_proto {
			min_count = 0;
			max_count = 1;
			is_aliased = false;
			doc_string = "If this interface is emulated then this protcol must be set";
		};

		/** a cache of the emu proto*/
		EmulationProtocol* emu_protocol;

		/** The send queue maintained by this network interface. */
		NicQueue* tx_queue;

		/** The timer for packet transmission. */
		TxTimerQueue* tx_timer_queue;

		/** Caching a pointer to the IP session that manages this interface. */
		IPv4Session* ip_sess;

	};
state_configuration {
	__PRIME__T1__="Link";
	__PRIME__T2__="BaseEntity";
	__PRIME__T1_TPL__="";
	__PRIME__T2_TPL__="";
	__PRIME__TYPEID__="";
	__PRIME__ALIAS__="";
	__PRIME__FILENAME__="/home/obaida/Desktop/pgc2vega/primex/netsim/src/ssfnet/net/link.h";

		shared configurable float delay  {
			type= FLOAT;
			default_value="0.001";
			doc_string="link delay";
			visualized=true;
		};
		shared configurable float bandwidth {
			type= FLOAT;
			default_value="1e8";
			doc_string="link bandwidth";
			visualized=true;
		};
		shared configurable int8_t ip_prefix_len {
			type= INT;
			default_value="-1";
			doc_string="The n in W.X.Y.Z/n";
		};
		configurable IPPrefix ip_prefix {
			type= OBJECT;
			default_value="0.0.0.0/0";
			doc_string="ip prefix of this Link";
		};

		/**
		 * each community has a different view of links via a linkinfo.
		 * This is a map from community id to its linkinfo of this link
		 */
		LinkInfo::Map com_id2link_info;

		/** A map from IP address to interface. */
		IP2IFACE_MAP ip2iface;

		/** A map from UID to interface. */
		MAC2IFACE_MAP mac2iface;

		child_type<BaseInterface> attachments {
			min_count = 0;
			max_count = 0; // infinity
			is_aliased = true; // by default it is false
			doc_string = "network interfaces attached to this link";
		};
	};

state_configuration {
	__PRIME__T1__="RoutingProtocol";
	__PRIME__T2__="ProtocolSession";
	__PRIME__T1_TPL__="";
	__PRIME__T2_TPL__="";
	__PRIME__TYPEID__="";
	__PRIME__ALIAS__="";
	__PRIME__FILENAME__="/home/obaida/Desktop/pgc2vega/primex/netsim/src/ssfnet/proto/routing_protocol.h";

	__PRIME__EMPTY__;
	};

state_configuration {
	__PRIME__T1__="ApplicationSession";
	__PRIME__T2__="ProtocolSession";
	__PRIME__T1_TPL__="";
	__PRIME__T2_TPL__="";
	__PRIME__TYPEID__="";
	__PRIME__ALIAS__="";
	__PRIME__FILENAME__="/home/obaida/Desktop/pgc2vega/primex/netsim/src/ssfnet/proto/application_session.h";

	__PRIME__EMPTY__;
	};

state_configuration {
	__PRIME__T1__="ICMPv4Session";
	__PRIME__T2__="ApplicationSession";
	__PRIME__T1_TPL__="";
	__PRIME__T2_TPL__="";
	__PRIME__TYPEID__="convert_protonum_to_typeid(SSFNET_PROTOCOL_TYPE_ICMPV4)";
	__PRIME__ALIAS__="";
	__PRIME__FILENAME__="/home/obaida/Desktop/pgc2vega/primex/netsim/src/ssfnet/proto/ipv4/icmpv4_session.h";

	__PRIME__EMPTY__;
	};
state_configuration {
	__PRIME__T1__="ICMPTraffic";
	__PRIME__T2__="StaticTrafficType";
	__PRIME__T1_TPL__="";
	__PRIME__T2_TPL__="";
	__PRIME__TYPEID__=" GENERIC_ICMPv4_TRAFFIC";
	__PRIME__ALIAS__="";
	__PRIME__FILENAME__="/home/obaida/Desktop/pgc2vega/primex/netsim/src/ssfnet/proto/ipv4/icmp_traffic.h";

			shared configurable SSFNET_STRING version {
				type=STRING;
				default_value="4";
				doc_string="The version of ICMP protocol.";
			};
		};
state_configuration {
	__PRIME__T1__="PingTraffic";
	__PRIME__T2__="ICMPTraffic";
	__PRIME__T1_TPL__="";
	__PRIME__T2_TPL__="";
	__PRIME__TYPEID__=" ICMPv4_PING_TRAFFIC";
	__PRIME__ALIAS__="";
	__PRIME__FILENAME__="/home/obaida/Desktop/pgc2vega/primex/netsim/src/ssfnet/proto/ipv4/icmp_traffic.h";

		//XXX, SSFNET_CONFIG_PROPERTY_DECL(UNamePatternList, srcs)
		shared configurable uint32_t payload_size {
			type=INT;
			default_value="56";
			doc_string="The number of bytes in the payload to be sent (default: 56).";
		};

		shared configurable uint64_t count {
			type=INT;
			default_value="1";
			doc_string="The number of requests to send (default: 1).";
		};

		shared configurable float rtt {
			type=FLOAT;
			default_value="0";
			doc_string="The rtt calculated by the ping traffic";
		};
	};

state_configuration {
	__PRIME__T1__="IPv4Session";
	__PRIME__T2__="ProtocolSession";
	__PRIME__T1_TPL__="";
	__PRIME__T2_TPL__="";
	__PRIME__TYPEID__="convert_protonum_to_typeid(SSFNET_PROTOCOL_TYPE_IPV4)";
	__PRIME__ALIAS__="";
	__PRIME__FILENAME__="/home/obaida/Desktop/pgc2vega/primex/netsim/src/ssfnet/proto/ipv4/ipv4_session.h";

	__PRIME__EMPTY__;
	};
state_configuration {
	__PRIME__T1__="FluidQueue";
	__PRIME__T2__="NicQueue";
	__PRIME__T1_TPL__="";
	__PRIME__T2_TPL__="";
	__PRIME__TYPEID__="";
	__PRIME__ALIAS__="";
	__PRIME__FILENAME__="/home/obaida/Desktop/pgc2vega/primex/netsim/src/ssfnet/proto/fluid/fluid_queue.h";

		shared configurable SSFNET_STRING queue_type {
			type=STRING;
			default_value= "red";
			doc_string= "The type of the queue, must be red or droptail";
		};
		shared configurable bool is_red {
			type= BOOL;
			default_value= "true";
			doc_string= "Whether this queue is a RED queue, rather than a drop-tail";
		};
		/** This is the weight applied FOR EACH PACKET ARRIVAL. */
		shared configurable float weight {
			type= FLOAT;
			default_value= "0.0001";
			doc_string= "The parameter used by RED queue to estimate the average queue length, must be between 0 and 1";
		};
		shared configurable float qmin {
			type=FLOAT;
			default_value= "0";
			doc_string= "the min threshold (in bytes) for calculating packet drop probability.";
		};
		shared configurable float qmax {
			type=FLOAT;
			default_value= "0";
			doc_string= "the max threshold (in bytes) for calculating packet drop probability.";
		};
		/*shared configurable float qcap {
			type=FLOAT;
			default_value= "0";
			doc_string= "Parameter to calculate packet drop probability, must be positive.";
		};*/
		shared configurable float pmax {
			type=FLOAT;
			default_value= "0.2";
			doc_string= "Parameter to calculate packet drop probability, must be in the range (0,1].";
		};
		shared configurable bool wait_opt {
			type= BOOL;
			default_value= "true";
			doc_string= "This RED option is used to avoid marking/dropping two packets in a row";
		};
		shared configurable float mean_pktsiz {
			type= FLOAT;
			default_value= "500";
			doc_string= "Mean packet size in bytes used by RED to compute the average queue length, must be positive.";
		};
		/** Weight used by the AQM policy to calculate average queue length ON EACH PACKET ARRIVAL. */
		float weight;

		/* Parameters to calculate packet drop probability based on the average queue length x(t):
		  P(x)=0, if 0 <= x < qmin;
		  P(x)=(x-qmin)/(qmax_qmin)*pmax, if qmin <= x < qmax;
		  P(x)=(x-qmax)/(bufsize-qmax)*(1-pmax)+pmax, if x > qmax.
		  Both qmin and qmax are in bits.
		*/
		float qmin; ///< Parameter to calculate packet drop probability.
		float qmax; ///< Parameter to calculate packet drop probability.
		//float qcap; ///< Parameter to calculate packet drop probability.
		float pmax; ///< Parameter to calculate packet drop probability.

		/** This option is to avoid dropping two packets in a row. */
		//bool wait_opt;

		/** Mean packet size in bits. */
		float mean_pktsiz;

		/** Weight used by the AQM policy to calculate average queue
		 *  length. We assume it is an exponentially weighed moving average
		 *  based on samples taken every delta seconds.
		 */
		float fluid_weight=0;

		/** The propagation delay of this queue. */
		float prop_delay=0;

		/** The aggregate arrival of fluid flows into this queue. */
		float aggr_arrival=0;

		/** The total number of bits arrived as packet flows since the last Runge-Kutta step. */
		float pkt_arrival=0;

		/** The instantaneous queue size. */
		float queue=0;

		/** The average queue size. */
		float avgque=0;

		/** The packet drop probability. */
		float loss=0;

		/** The instantaneous queue size inherited from the corresponding variable maintained
		 * solely for packet arrivals between Runge-Kutta steps.
		 */
		float shadow_queue=0;

		/** The average queue size inherited from the corresponding variable maintained solely
		 *  for packet arrivals between Runge-Kutta steps.
		 */
		float shadow_avgque=0;

		/** The packet drop probability inherited from the corresponding variable maintained
		 * solely for packet arrivals between Runge-Kutta steps.
		 */
		float shadow_loss=0;

		/** For RED packet model only. False if the average queue length is crossing the min
		 * threshold for the first time.
		 */
		bool crossing=false;

		/** The number of bits arrived between two consecutive packet drops (when the qmin
		 * threshold is crossed). Used for RED packet model only.
		 */
		float interdrop=0;

		/** The time of the last queue length update. */
		VirtualTime last_update_time=0;

		/** The time of the last packet sent. */
		VirtualTime last_send_time=0;

	};
state_configuration{
	__PRIME__T1__="FluidTraffic";
	__PRIME__T2__="TrafficType";
	__PRIME__T1_TPL__="";
	__PRIME__T2_TPL__="";
	__PRIME__TYPEID__=" FLUID_TRAFFIC";
	__PRIME__ALIAS__="";
	__PRIME__FILENAME__="/home/obaida/Desktop/pgc2vega/primex/netsim/src/ssfnet/proto/fluid/fluid_traffic.h";

	  //The sending rate will behave differently depending on the type of the fluid traffic.
	  shared configurable SSFNET_STRING protocol_type {
			type=STRING;
			default_value="tcp_reno";
			doc_string="The type of this fluid class.";
	  };
      shared configurable ResourceIdentifier srcs {
			type = RESOURCE_ID;
			default_value =  "" ;
			doc_string = "XXX";
      };
      shared configurable ResourceIdentifier dsts {
			type = RESOURCE_ID;
			default_value = "" ;
			doc_string = "XXX";
      };
       shared configurable float off_time {
			type=FLOAT;
			default_value="0.01";
			doc_string="1/lamda, which is the expected value of the exponential distribution.";
       };
       shared configurable SSFNET_STRING mapping {
			type=STRING;
			default_value="all2all";
			doc_string="The method to map the source and destination based on the src and dst lists";
       };

	  shared configurable int nflows {
			type=INT;
			default_value="10";
			doc_string="The average number of homogeneous sessions in this fluid flow.";
	  };
	  shared configurable float pktsiz {
			type=FLOAT;
			default_value="1000.0";
			doc_string="Mean packet size in Bytes.";
	  };
//#ifdef SSFNET_FLUID_STOCHASTIC
	  shared configurable float hurst {
			type=FLOAT;
			default_value="0.75";
			doc_string="The hurst parameter indicates the presence of LRD (Long-Range Dependence).";
	  };
//#endif
	  shared configurable float md {
	  		type=FLOAT;
	  		default_value="1.5";
	  		doc_string="TCP congestion window multiplicative decrease factor.";
	  };
	  shared configurable float wndmax {
	  		type=FLOAT;
	  		default_value="128";
	  		doc_string="TCP send window maximum size in packets";
	  };
	  shared configurable float sendrate {
	  	    type=FLOAT;
	  		default_value="0";
	  		doc_string="UDP send rate in Byte per second.";
	  };
	  //XXX, change the type of start, stop to string for insert_activity()
	  shared configurable float start{
			type=FLOAT;
			default_value="0";
			doc_string="The start time of the flow";
	  };
	  shared configurable float stop{
	  		type=FLOAT;
	  		default_value="1000";
	  		doc_string="The stop time of the flow";
	  };
	  configurable SSFNET_STRING monfn {
	  		type=STRING;
	  		default_value=" ";
	  		doc_string="The name of monitor file.";
	  };
	  configurable float monres {
	  		type=FLOAT;
	  		default_value="0.001";
	  		doc_string="Recording interval";
	  };

	  int protocol_type;
	  float pktsiz; //in bits
	  float sendrate; //in bits per seconde
	  float wndmax;
	  float window;
      int mapping;
      TrafficFlowList* traffic_flows;

  };

state_configuration {
	__PRIME__T1__="ProbeSession";
	__PRIME__T2__="ProtocolSession";
	__PRIME__T1_TPL__="";
	__PRIME__T2_TPL__="";
	__PRIME__TYPEID__="convert_protonum_to_typeid(SSFNET_PROTOCOL_TYPE_PROBE)";
	__PRIME__ALIAS__="";
	__PRIME__FILENAME__="/home/obaida/Desktop/pgc2vega/primex/netsim/src/ssfnet/proto/fluid/probe_session.h";

	__PRIME__EMPTY__;
	};
state_configuration {
	__PRIME__T1__="EmulationProtocol";
	__PRIME__T2__="BaseEntity";
	__PRIME__T1_TPL__="";
	__PRIME__T2_TPL__="";
	__PRIME__TYPEID__="";
	__PRIME__ALIAS__="";
	__PRIME__FILENAME__="/home/obaida/Desktop/pgc2vega/primex/netsim/src/ssfnet/proto/emu/emulation_protocol.h";

		shared configurable bool ip_forwarding {
			type=BOOL;
			default_value="false";
			doc_string="If ip_forwarding is set to true the emulated interface will intercept all pkts it sees. If it is set to false it will only capture pkts targeted for it.";
		};
	};

state_configuration {
	__PRIME__T1__="OpenVPNEmulation";
	__PRIME__T2__="EmulationProtocol";
	__PRIME__T1_TPL__="";
	__PRIME__T2_TPL__="";
	__PRIME__TYPEID__="";
	__PRIME__ALIAS__="";
	__PRIME__FILENAME__="/home/obaida/Desktop/pgc2vega/primex/netsim/src/ssfnet/os/emu/vpn_emu_proto.h";

	__PRIME__EMPTY__;
	};

state_configuration {
	__PRIME__T1__="TAPEmulation";
	__PRIME__T2__="EmulationProtocol";
	__PRIME__T1_TPL__="";
	__PRIME__T2_TPL__="";
	__PRIME__TYPEID__="";
	__PRIME__ALIAS__="";
	__PRIME__FILENAME__="/home/obaida/Desktop/pgc2vega/primex/netsim/src/ssfnet/os/emu/tap_emu_proto.h";

	__PRIME__EMPTY__;
	};
state_configuration {
	__PRIME__T1__="TrafficPortal";
	__PRIME__T2__="EmulationProtocol";
	__PRIME__T1_TPL__="";
	__PRIME__T2_TPL__="";
	__PRIME__TYPEID__="";
	__PRIME__ALIAS__="";
	__PRIME__FILENAME__="/home/obaida/Desktop/pgc2vega/primex/netsim/src/ssfnet/os/emu/portal_emu_proto.h";

		configurable IPPrefix::List networks {
			type= OBJECT;
			default_value= "[]";
			doc_string= "list of ip prefixes that are reachable via this portal";
		};
	};
state_configuration {
	__PRIME__T1__="TCPMaster";
	__PRIME__T2__="ProtocolSession";
	__PRIME__T1_TPL__="";
	__PRIME__T2_TPL__="";
	__PRIME__TYPEID__="convert_protonum_to_typeid(SSFNET_PROTOCOL_TYPE_TCP)";
	__PRIME__ALIAS__="";
	__PRIME__FILENAME__="/home/obaida/Desktop/pgc2vega/primex/netsim/src/ssfnet/proto/tcp/tcp_master.h";

		shared configurable SSFNET_STRING tcpCA {
			type=STRING;
			default_value="bic";
			doc_string="Congestion control algorithm.";
		};
		shared configurable int mss {
			type=INT;
			default_value="1400";
			doc_string="maximum segment size in bytes.";
		};
		shared configurable int sndWndSize {
			type=INT;
			default_value="1000000000";
			doc_string="maximum sending window size of TCP protocol in bytes.";
		};
		shared configurable int sndBufSize {
			type=INT;
			default_value="2000000000";
			doc_string="maximum size of the buffer for sending bytes.";
		};
		shared configurable int rcvWndSize {
			type=INT;
			default_value="1000000000";
			doc_string="maximum receiving window size of TCP protocol in bytes.";
		};//Restore to life
		shared configurable float samplingInterval {
			type=FLOAT;
			default_value="10000";
			doc_string="Interval between consecutive sampling of TCP variables.";
		};
		shared configurable int iss {
			type=INT;
			default_value="1";
			doc_string="Initial sequence number of TCP protocol.";
		};
	};
state_configuration {
	__PRIME__T1__="HTTPServer";
	__PRIME__T2__="ApplicationSession";
	__PRIME__T1_TPL__="";
	__PRIME__T2_TPL__="";
	__PRIME__TYPEID__="convert_protonum_to_typeid(SSFNET_PROTOCOL_TYPE_HTTP_SERVER)";
	__PRIME__ALIAS__="";
	__PRIME__FILENAME__="/home/obaida/Desktop/pgc2vega/primex/netsim/src/ssfnet/proto/tcp/test/http_server.h";

 		shared configurable uint32_t listeningPort {
 			type=INT;
 			default_value="80";
 			doc_string="Listening port for incoming connections.";
 		};
 		configurable uint32_t bytesReceived {
 			type=INT;
 			default_value="0";
 			doc_string="Number of bytes received so far from all sessions";
 		};
 		configurable uint32_t requestsReceived {
 			type=INT;
 			default_value="0";
 			doc_string="Number of requests received so far from all sessions";
 		};
 	};
state_configuration {
	__PRIME__T1__="HTTPClient";
	__PRIME__T2__="ApplicationSession";
	__PRIME__T1_TPL__="";
	__PRIME__T2_TPL__="";
	__PRIME__TYPEID__="convert_protonum_to_typeid(SSFNET_PROTOCOL_TYPE_HTTP_CLIENT)";
	__PRIME__ALIAS__="";
	__PRIME__FILENAME__="/home/obaida/Desktop/pgc2vega/primex/netsim/src/ssfnet/proto/tcp/test/http_client.h";

		configurable uint32_t active_sessions {
			type=INT;
			default_value="0";
			doc_string="This is the number of active downloading sessions";
		};
		configurable uint32_t bytes_received {
			type=INT;
			default_value="0";
			doc_string="Number of bytes received from all sessions";
		};
	};
state_configuration {
	__PRIME__T1__="TCPTraffic";
	__PRIME__T2__="StaticTrafficType";
	__PRIME__T1_TPL__="";
	__PRIME__T2_TPL__="";
	__PRIME__TYPEID__=" TCP_TRAFFIC";
	__PRIME__ALIAS__="";
	__PRIME__FILENAME__="/home/obaida/Desktop/pgc2vega/primex/netsim/src/ssfnet/proto/tcp/test/http_traffic.h";

		shared configurable uint32_t dst_port {
			type=INT;
			default_value="80";
			doc_string="The destination port for an HTTP connection.";
		};
		shared configurable uint64_t file_size {
			type=INT;
			default_value="1024000";
			doc_string="The size to request to the server.";
		};
		shared configurable bool file_pareto{
			type=BOOL;
			default_value="false";
			doc_string="Whether the file size are modeled by a concatenation of bounded Weibull and Pareto distributions";
		};
		shared configurable bool to_emulated {
			type=BOOL;
			default_value="false";
			doc_string="Whether this host will request packets from a simulated or emulated entity.";
		};
		shared configurable uint32_t number_of_sessions{
			type=INT;
			default_value="1";
			doc_string="Number of session.";
		};
		shared configurable uint32_t connections_per_session{
			type=INT;
			default_value="1";
			doc_string="Number of connections within a session.";
		};
		shared configurable float session_timeout{
			type=INT;
			default_value="30.0";
			doc_string="Session timeout in seconds.";
		};
	};

state_configuration {
	__PRIME__T1__="SymbioSimAppProt";
	__PRIME__T2__="ProtocolSession";
	__PRIME__T1_TPL__="";
	__PRIME__T2_TPL__="";
	__PRIME__TYPEID__=" convert_protonum_to_typeid(SSFNET_PROTOCOL_SYMBIOSIM_APP)";
	__PRIME__ALIAS__="";
	__PRIME__FILENAME__="/home/obaida/Desktop/pgc2vega/primex/netsim/src/ssfnet/proto/tcp/test/SymbioSimAppProt.h";

	__PRIME__EMPTY__;
	};
state_configuration {
	__PRIME__T1__="SwingTCPTraffic";
	__PRIME__T2__="CentralizedTrafficType";
	__PRIME__T1_TPL__="";
	__PRIME__T2_TPL__="";
	__PRIME__TYPEID__=" SWING_TCP_TRAFFIC";
	__PRIME__ALIAS__="";
	__PRIME__FILENAME__="/home/obaida/Desktop/pgc2vega/primex/netsim/src/ssfnet/proto/tcp/swing_tcp_traffic/swing_tcp_traffic.h";

		shared configurable SSFNET_STRING trace_description {
			type=STRING;
			default_value=" ";
			doc_string="The file that indicates the positions of all experical data of the parameters.";
		};
		shared configurable bool stretch {
			type = BOOL;
			default_value = "false" ;
			doc_string = "whether to stretch some parameters";
		};

		shared configurable float session_timeout {
			type = FLOAT;
			default_value = "300" ;
			doc_string = "timeout for session (in second)";
		};

		shared configurable float rre_timeout {
		    type = FLOAT;
		    default_value="30";
		    doc_string="timeout for rre (in second)";
		};
		//set the parameters need to stretch and the stretch coefficients
	};
state_configuration {
	__PRIME__T1__="SwingClient";
	__PRIME__T2__="ApplicationSession";
	__PRIME__T1_TPL__="";
	__PRIME__T2_TPL__="";
	__PRIME__TYPEID__="convert_protonum_to_typeid(SSFNET_PROTOCOL_TYPE_SWING_CLIENT)";
	__PRIME__ALIAS__="";
	__PRIME__FILENAME__="/home/obaida/Desktop/pgc2vega/primex/netsim/src/ssfnet/proto/tcp/swing_tcp_traffic/swing_client.h";

		configurable uint32_t active_sessions {
			type=INT;
			default_value="0";
			doc_string="This is the number of active downloading sessions";
		};
		configurable uint32_t bytes_received {
			type=INT;
			default_value="0";
			doc_string="Number of bytes received from all sessions";
		};
	};
state_configuration {
	__PRIME__T1__="SwingServer";
	__PRIME__T2__="ApplicationSession";
	__PRIME__T1_TPL__="";
	__PRIME__T2_TPL__="";
	__PRIME__TYPEID__="convert_protonum_to_typeid(SSFNET_PROTOCOL_TYPE_SWING_SERVER)";
	__PRIME__ALIAS__="";
	__PRIME__FILENAME__="/home/obaida/Desktop/pgc2vega/primex/netsim/src/ssfnet/proto/tcp/swing_tcp_traffic/swing_server.h";

	 		shared configurable uint32_t listeningPort {
	 			type=INT;
	 			default_value="1024";
	 			doc_string="Listening port for incoming connections.";
	 		};
	 		configurable uint32_t bytesReceived {
	 			type=INT;
	 			default_value="0";
	 			doc_string="Number of bytes received so far from all sessions";
	 		};
	 		configurable uint32_t requestsReceived {
	 			type=INT;
	 			default_value="0";
	 			doc_string="Number of requests received so far from all sessions";
	 		};
	 	};
state_configuration {
	__PRIME__T1__="UDPMaster";
	__PRIME__T2__="ProtocolSession";
	__PRIME__T1_TPL__="";
	__PRIME__T2_TPL__="";
	__PRIME__TYPEID__="convert_protonum_to_typeid(SSFNET_PROTOCOL_TYPE_UDP)";
	__PRIME__ALIAS__="";
	__PRIME__FILENAME__="/home/obaida/Desktop/pgc2vega/primex/netsim/src/ssfnet/proto/udp/udp_master.h";

		shared configurable int max_datagram_size {
			type=INT;
			default_value="1470";
			doc_string="maximum datagram size.";
		};
	};
state_configuration {
	__PRIME__T1__="STCPMaster";
	__PRIME__T2__="ProtocolSession";
	__PRIME__T1_TPL__="";
	__PRIME__T2_TPL__="";
	__PRIME__TYPEID__="convert_protonum_to_typeid(SSFNET_PROTOCOL_TYPE_STCP)";
	__PRIME__ALIAS__="";
	__PRIME__FILENAME__="/home/obaida/Desktop/pgc2vega/primex/netsim/src/ssfnet/proto/stcp/stcp_master.h";

		shared configurable int max_datagram_size {
			type=INT;
			default_value="1470";
			doc_string="maximum datagram size.";
		};
	};
state_configuration {
	__PRIME__T1__="PPBPTraffic";
	__PRIME__T2__="StaticTrafficType";
	__PRIME__T1_TPL__="";
	__PRIME__T2_TPL__="";
	__PRIME__TYPEID__=" PPBP_TRAFFIC";
	__PRIME__ALIAS__="";
	__PRIME__FILENAME__="/home/obaida/Desktop/pgc2vega/primex/netsim/src/ssfnet/proto/udp/test/ppbp_traffic.h";

		shared configurable uint32_t dst_port {
			type=INT;
			default_value="5001";
			doc_string="The destination port for an CBR connection.";
		};
		configurable uint32_t bytes_to_send_per_interval {
			type=INT;
			default_value="1000000";
			doc_string="The number of bytes to send each time interval.";
		};
		configurable uint32_t avg_sessions {
			type=INT;
			default_value="1";
			doc_string="Average number of sessions (default: 0 means unset).";
		};
		configurable float hurst {
			type=FLOAT;
			default_value="0.8";
			doc_string="The hurst parameter indicates the presence of LRD (Long-Range Dependence).";
		};
		configurable float stop {
			type=FLOAT;
			default_value="1000000";
			doc_string="The traffic stops starting new traffic flows after stop time.";
		};

	};
state_configuration {
	__PRIME__T1__="CBR";
	__PRIME__T2__="ApplicationSession";
	__PRIME__T1_TPL__="";
	__PRIME__T2_TPL__="";
	__PRIME__TYPEID__="
	convert_protonum_to_typeid(SSFNET_PROTOCOL_TYPE_CBR)";
	__PRIME__ALIAS__="";
	__PRIME__FILENAME__="/home/obaida/Desktop/pgc2vega/primex/netsim/src/ssfnet/proto/udp/test/cbr.h";

		shared configurable int listening_port {
			type=INT;
			default_value="5001";
			doc_string="Listening port port UDP datagrams.";
		};
		configurable uint32_t total_bytes_received {
			type=INT;
			default_value="0";
			doc_string="Number of bytes received from all sessions";
		};
		configurable uint32_t total_bytes_sent {
			type=INT;
			default_value="0";
			doc_string="Number of bytes sent from all sessions";
		};
	};
state_configuration {
	__PRIME__T1__="UDPTraffic";
	__PRIME__T2__="StaticTrafficType";
	__PRIME__T1_TPL__="";
	__PRIME__T2_TPL__="";
	__PRIME__TYPEID__=" UDP_TRAFFIC";
	__PRIME__ALIAS__="";
	__PRIME__FILENAME__="/home/obaida/Desktop/pgc2vega/primex/netsim/src/ssfnet/proto/udp/test/cbr_traffic.h";

		shared configurable uint32_t dstPort {
			type=INT;
			default_value="5001";
			doc_string="The destination port for an CBR connection.";
		};
		configurable uint32_t bytesToSendEachInterval {
			type=INT;
			default_value="1000000";
			doc_string="The number of bytes to send each time interval.";
		};
		configurable uint32_t count {
			type=INT;
			default_value="10";
			doc_string="Number of times the server will send 'bytesToSendEachInterval' to client.";
		};
		configurable bool toEmulated {
			type=BOOL;
			default_value="false";
			doc_string="Whether this host will send packets to a simulated or emulated entity).";
		};
	};
state_configuration {
	__PRIME__T1__="CNFApplication";
	__PRIME__T2__="ApplicationSession";
	__PRIME__T1_TPL__="";
	__PRIME__T2_TPL__="";
	__PRIME__TYPEID__="convert_protonum_to_typeid(SSFNET_PROTOCOL_TYPE_CNF_APP)";
	__PRIME__ALIAS__="";
	__PRIME__FILENAME__="/home/obaida/Desktop/pgc2vega/primex/netsim/src/ssfnet/proto/cnf/cnf_session.h";

	 		shared configurable uint32_t listening_port {
	 			type=INT;
	 			default_value="80";
	 			doc_string="Listening port for incoming connections.";
	 		};
	 		configurable uint32_t bytes_received {
	 			type=INT;
	 			default_value="0";
	 			doc_string="Number of bytes received so far from all sessions";
	 		};
	 		configurable uint32_t bytes_sent {
	 			type=INT;
	 			default_value="0";
	 			doc_string="Number of bytes sent so far from all sessions";
	 		};
	 		configurable uint32_t requests_received {
	 			type=INT;
	 			default_value="0";
	 			doc_string="Number of requests received so far from all sessions";
	 		};
	 		configurable CNFContentMap cnf_content_sizes {
	 			type=OBJECT;
	 			default_value="[]";
				doc_string= "a list of content id and their sizes. i.e. [100,1,200,2] --> [cid:100, size:1],[cid:200, size:2]";
	 		};
 	};
state_configuration {
	__PRIME__T1__="CNFTransport";
	__PRIME__T2__="ProtocolSession";
	__PRIME__T1_TPL__="";
	__PRIME__T2_TPL__="";
	__PRIME__TYPEID__="convert_protonum_to_typeid(SSFNET_PROTOCOL_TYPE_CNF_TRANS)";
	__PRIME__ALIAS__="";
	__PRIME__FILENAME__="/home/obaida/Desktop/pgc2vega/primex/netsim/src/ssfnet/proto/cnf/cnf_session.h";

 		configurable uint32_t bytes_received {
 			type=INT;
 			default_value="0";
 			doc_string="Number of bytes received so far from all sessions";
 		};
 		configurable uint32_t requests_received {
 			type=INT;
 			default_value="0";
 			doc_string="Number of requests received so far from all sessions";
 		};
 		configurable uint32_t bytes_sent {
	 		type=INT;
	 		default_value="0";
	 		doc_string="Number of bytes sent so far from all sessions";
	 	};
 		shared configurable UIDVec cnf_routers {
 			type=OBJECT;
 			default_value="[]";
 			doc_string="a list of RIDS of other routers with cnf transports installed";
 		};
 		shared configurable bool is_controller {
 			type=BOOL;
 			default_value="false";
 			doc_string="Whether this is a controller";
 		};
 	};
state_configuration {
	__PRIME__T1__="CNFTraffic";
	__PRIME__T2__="StaticTrafficType";
	__PRIME__T1_TPL__="";
	__PRIME__T2_TPL__="";
	__PRIME__TYPEID__=" CNF_TRAFFIC";
	__PRIME__ALIAS__="";
	__PRIME__FILENAME__="/home/obaida/Desktop/pgc2vega/primex/netsim/src/ssfnet/proto/cnf/test/cnf_traffic.h";

		shared configurable UID_t dst_port {
			type=INT;
			default_value="1000";
			doc_string="The destination port for an CNF connection.";
		};
		shared configurable int content_id {
			type=INT;
			default_value="0";
			doc_string="The contenet id request to the controller.";
		};
		shared configurable int number_of_contents {
			type=INT;
			default_value="1";
			doc_string="The total number of contents.";
		};
		shared configurable int number_of_requests {
			type=INT;
			default_value="1";
			doc_string="The total number of requests.";
		};
	};
