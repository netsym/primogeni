/* ------------------------- */
/* ------------------------- */
/*         WARNING: */
/*  THIS FILE IS GENERATED! */
/*        DO NOT EDIT! */
/* ------------------------- */
/* ------------------------- */

#include "/home/obaida/Desktop/pgc2vega/primex/netsim/src/ssfnet/os/configurable_types.h"
namespace prime {
namespace ssfnet {

 SSFNET_MAP(int,SSFNET_STRING*)* jprime_type_names=NULL;
void setup_jprime_type_types(){
	jprime_type_names=new SSFNET_MAP(int,SSFNET_STRING*)();
	jprime_type_names->insert(SSFNET_MAKE_PAIR(100,new SSFNET_STRING("BOOL")));
	jprime_type_names->insert(SSFNET_MAKE_PAIR(101,new SSFNET_STRING("FLOAT")));
	jprime_type_names->insert(SSFNET_MAKE_PAIR(102,new SSFNET_STRING("INTEGER")));
	jprime_type_names->insert(SSFNET_MAKE_PAIR(103,new SSFNET_STRING("LIST")));
	jprime_type_names->insert(SSFNET_MAKE_PAIR(104,new SSFNET_STRING("OPAQUE")));
	jprime_type_names->insert(SSFNET_MAKE_PAIR(105,new SSFNET_STRING("RESOURCE")));
	jprime_type_names->insert(SSFNET_MAKE_PAIR(106,new SSFNET_STRING("STRING")));
	jprime_type_names->insert(SSFNET_MAKE_PAIR(107,new SSFNET_STRING("SYMBOL")));
	jprime_type_names->insert(SSFNET_MAKE_PAIR(50,new SSFNET_STRING("PROPERTY")));
	jprime_type_names->insert(SSFNET_MAKE_PAIR(51,new SSFNET_STRING("SYMBOL_TABLE")));
	jprime_type_names->insert(SSFNET_MAKE_PAIR(52,new SSFNET_STRING("PARTITION")));
	jprime_type_names->insert(SSFNET_MAKE_PAIR(53,new SSFNET_STRING("COMMUNITY")));
	jprime_type_names->insert(SSFNET_MAKE_PAIR(54,new SSFNET_STRING("SYMBOL_TABLE_ENTRY")));
	jprime_type_names->insert(SSFNET_MAKE_PAIR(55,new SSFNET_STRING("ROUTE_TABLE")));
	jprime_type_names->insert(SSFNET_MAKE_PAIR(56,new SSFNET_STRING("ROUTE_ENTRY")));
	jprime_type_names->insert(SSFNET_MAKE_PAIR(57,new SSFNET_STRING("GHOST_NODE")));
	jprime_type_names->insert(SSFNET_MAKE_PAIR(58,new SSFNET_STRING("GENERIC_NODE")));
}

SSFNET_STRING* getJPRIMETypeString(int idx){
	if(!jprime_type_names)setup_jprime_type_types();
	SSFNET_MAP(int,SSFNET_STRING*)::iterator rv;
	rv=jprime_type_names->find(idx);
	if(rv==jprime_type_names->end()) {
		return NULL;
	}
	return rv->second;
}

SSFNET_MAP(SSFNET_STRING,uint32_t)* jprime_attr_ids = 0; 

SSFNET_MAP(uint32_t,SSFNET_STRING)* jprime_attr_strings = 0; 
void setupJPRIMEAttrIDs() {
	jprime_attr_ids = new SSFNET_MAP(SSFNET_STRING,uint32_t)();
	jprime_attr_strings = new SSFNET_MAP(uint32_t, SSFNET_STRING)();
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("active_sessions"),1));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(1,SSFNET_STRING("active_sessions")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("aggr_arrival"),2));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(2,SSFNET_STRING("aggr_arrival")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("alias_path"),3));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(3,SSFNET_STRING("alias_path")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("attached_link"),4));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(4,SSFNET_STRING("attached_link")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("automatic_protocol_session_creation"),5));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(5,SSFNET_STRING("automatic_protocol_session_creation")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("avg_sessions"),6));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(6,SSFNET_STRING("avg_sessions")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("avgque"),7));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(7,SSFNET_STRING("avgque")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("bandwidth"),8));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(8,SSFNET_STRING("bandwidth")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("bit_rate"),9));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(9,SSFNET_STRING("bit_rate")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("buffer_size"),10));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(10,SSFNET_STRING("buffer_size")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("bytesReceived"),11));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(11,SSFNET_STRING("bytesReceived")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("bytesToSendEachInterval"),12));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(12,SSFNET_STRING("bytesToSendEachInterval")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("bytes_in_per_sec"),13));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(13,SSFNET_STRING("bytes_in_per_sec")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("bytes_out_per_sec"),14));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(14,SSFNET_STRING("bytes_out_per_sec")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("bytes_received"),15));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(15,SSFNET_STRING("bytes_received")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("bytes_sent"),16));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(16,SSFNET_STRING("bytes_sent")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("bytes_to_send_per_interval"),17));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(17,SSFNET_STRING("bytes_to_send_per_interval")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("cnf_content_ids"),18));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(18,SSFNET_STRING("cnf_content_ids")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("cnf_content_sizes"),19));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(19,SSFNET_STRING("cnf_content_sizes")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("cnf_routers"),20));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(20,SSFNET_STRING("cnf_routers")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("com_id2link_info"),21));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(21,SSFNET_STRING("com_id2link_info")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("community_id"),22));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(22,SSFNET_STRING("community_id")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("community_ids"),23));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(23,SSFNET_STRING("community_ids")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("connections_per_session"),24));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(24,SSFNET_STRING("connections_per_session")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("content_id"),25));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(25,SSFNET_STRING("content_id")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("controller_rid"),26));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(26,SSFNET_STRING("controller_rid")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("count"),27));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(27,SSFNET_STRING("count")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("crossing"),28));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(28,SSFNET_STRING("crossing")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("default_ip"),29));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(29,SSFNET_STRING("default_ip")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("delay"),30));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(30,SSFNET_STRING("delay")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("drop_probability"),31));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(31,SSFNET_STRING("drop_probability")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("dstPort"),32));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(32,SSFNET_STRING("dstPort")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("dst_ips"),33));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(33,SSFNET_STRING("dst_ips")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("dst_port"),34));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(34,SSFNET_STRING("dst_port")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("dsts"),35));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(35,SSFNET_STRING("dsts")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("edge_ifaces"),36));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(36,SSFNET_STRING("edge_ifaces")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("emu_protocol"),37));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(37,SSFNET_STRING("emu_protocol")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("file_pareto"),38));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(38,SSFNET_STRING("file_pareto")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("file_size"),39));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(39,SSFNET_STRING("file_size")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("fluid_weight"),40));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(40,SSFNET_STRING("fluid_weight")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("host_seed"),41));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(41,SSFNET_STRING("host_seed")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("hurst"),42));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(42,SSFNET_STRING("hurst")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("hybrid_traffic_flows"),43));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(43,SSFNET_STRING("hybrid_traffic_flows")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("initialized"),44));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(44,SSFNET_STRING("initialized")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("interdrop"),45));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(45,SSFNET_STRING("interdrop")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("interval"),46));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(46,SSFNET_STRING("interval")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("interval_exponential"),47));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(47,SSFNET_STRING("interval_exponential")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("ip2iface"),48));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(48,SSFNET_STRING("ip2iface")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("ip_address"),49));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(49,SSFNET_STRING("ip_address")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("ip_forwarding"),50));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(50,SSFNET_STRING("ip_forwarding")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("ip_prefix"),51));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(51,SSFNET_STRING("ip_prefix")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("ip_prefix_len"),52));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(52,SSFNET_STRING("ip_prefix_len")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("ip_sess"),53));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(53,SSFNET_STRING("ip_sess")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("is_controller"),54));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(54,SSFNET_STRING("is_controller")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("is_on"),55));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(55,SSFNET_STRING("is_on")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("is_red"),56));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(56,SSFNET_STRING("is_red")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("iss"),57));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(57,SSFNET_STRING("iss")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("jitter_range"),58));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(58,SSFNET_STRING("jitter_range")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("last_send_time"),59));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(59,SSFNET_STRING("last_send_time")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("last_update_time"),60));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(60,SSFNET_STRING("last_update_time")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("last_xmit_time"),61));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(61,SSFNET_STRING("last_xmit_time")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("latency"),62));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(62,SSFNET_STRING("latency")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("listeningPort"),63));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(63,SSFNET_STRING("listeningPort")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("listening_port"),64));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(64,SSFNET_STRING("listening_port")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("local_dst_cache_size"),65));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(65,SSFNET_STRING("local_dst_cache_size")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("loss"),66));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(66,SSFNET_STRING("loss")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("mac2iface"),67));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(67,SSFNET_STRING("mac2iface")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("mac_address"),68));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(68,SSFNET_STRING("mac_address")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("mapping"),69));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(69,SSFNET_STRING("mapping")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("max"),70));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(70,SSFNET_STRING("max")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("max_datagram_size"),71));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(71,SSFNET_STRING("max_datagram_size")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("max_queue_delay"),72));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(72,SSFNET_STRING("max_queue_delay")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("md"),73));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(73,SSFNET_STRING("md")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("mean_pktsiz"),74));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(74,SSFNET_STRING("mean_pktsiz")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("min"),75));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(75,SSFNET_STRING("min")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("monfn"),76));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(76,SSFNET_STRING("monfn")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("monres"),77));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(77,SSFNET_STRING("monres")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("mss"),78));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(78,SSFNET_STRING("mss")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("mtu"),79));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(79,SSFNET_STRING("mtu")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("my_edge_ifaces"),80));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(80,SSFNET_STRING("my_edge_ifaces")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("name"),81));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(81,SSFNET_STRING("name")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("networks"),82));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(82,SSFNET_STRING("networks")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("nflows"),83));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(83,SSFNET_STRING("nflows")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("nix_vec_cache_size"),84));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(84,SSFNET_STRING("nix_vec_cache_size")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("num_in_bytes"),85));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(85,SSFNET_STRING("num_in_bytes")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("num_in_packets"),86));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(86,SSFNET_STRING("num_in_packets")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("num_in_ucast_bytes"),87));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(87,SSFNET_STRING("num_in_ucast_bytes")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("num_in_ucast_packets"),88));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(88,SSFNET_STRING("num_in_ucast_packets")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("num_out_bytes"),89));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(89,SSFNET_STRING("num_out_bytes")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("num_out_packets"),90));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(90,SSFNET_STRING("num_out_packets")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("num_out_ucast_bytes"),91));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(91,SSFNET_STRING("num_out_ucast_bytes")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("num_out_ucast_packets"),92));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(92,SSFNET_STRING("num_out_ucast_packets")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("number_of_contents"),93));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(93,SSFNET_STRING("number_of_contents")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("number_of_requests"),94));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(94,SSFNET_STRING("number_of_requests")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("number_of_sessions"),95));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(95,SSFNET_STRING("number_of_sessions")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("off_time"),96));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(96,SSFNET_STRING("off_time")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("offset"),97));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(97,SSFNET_STRING("offset")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("packets_in_per_sec"),98));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(98,SSFNET_STRING("packets_in_per_sec")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("packets_out_per_sec"),99));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(99,SSFNET_STRING("packets_out_per_sec")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("payload_size"),100));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(100,SSFNET_STRING("payload_size")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("period"),101));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(101,SSFNET_STRING("period")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("pkt_arrival"),102));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(102,SSFNET_STRING("pkt_arrival")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("pktsiz"),103));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(103,SSFNET_STRING("pktsiz")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("pmax"),104));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(104,SSFNET_STRING("pmax")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("pname_map"),105));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(105,SSFNET_STRING("pname_map")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("pno_map"),106));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(106,SSFNET_STRING("pno_map")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("portal_rules"),107));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(107,SSFNET_STRING("portal_rules")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("portals"),108));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(108,SSFNET_STRING("portals")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("prop_delay"),109));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(109,SSFNET_STRING("prop_delay")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("protocol_list"),110));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(110,SSFNET_STRING("protocol_list")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("protocol_type"),111));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(111,SSFNET_STRING("protocol_type")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("qcap"),112));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(112,SSFNET_STRING("qcap")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("qmax"),113));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(113,SSFNET_STRING("qmax")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("qmin"),114));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(114,SSFNET_STRING("qmin")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("queue"),115));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(115,SSFNET_STRING("queue")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("queue_delay"),116));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(116,SSFNET_STRING("queue_delay")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("queue_size"),117));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(117,SSFNET_STRING("queue_size")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("queue_type"),118));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(118,SSFNET_STRING("queue_type")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("rcvWndSize"),119));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(119,SSFNET_STRING("rcvWndSize")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("real_uid"),120));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(120,SSFNET_STRING("real_uid")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("requestsReceived"),121));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(121,SSFNET_STRING("requestsReceived")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("requests_received"),122));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(122,SSFNET_STRING("requests_received")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("rng"),123));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(123,SSFNET_STRING("rng")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("rng_seed"),124));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(124,SSFNET_STRING("rng_seed")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("routemap"),125));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(125,SSFNET_STRING("routemap")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("rre_timeout"),126));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(126,SSFNET_STRING("rre_timeout")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("rtt"),127));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(127,SSFNET_STRING("rtt")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("sample_count"),128));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(128,SSFNET_STRING("sample_count")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("samplingInterval"),129));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(129,SSFNET_STRING("samplingInterval")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("sendrate"),130));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(130,SSFNET_STRING("sendrate")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("session_timeout"),131));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(131,SSFNET_STRING("session_timeout")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("shadow_avgque"),132));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(132,SSFNET_STRING("shadow_avgque")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("shadow_loss"),133));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(133,SSFNET_STRING("shadow_loss")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("shadow_queue"),134));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(134,SSFNET_STRING("shadow_queue")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("shortcut_to"),135));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(135,SSFNET_STRING("shortcut_to")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("size"),136));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(136,SSFNET_STRING("size")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("sndBufSize"),137));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(137,SSFNET_STRING("sndBufSize")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("sndWndSize"),138));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(138,SSFNET_STRING("sndWndSize")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("srcs"),139));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(139,SSFNET_STRING("srcs")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("start"),140));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(140,SSFNET_STRING("start")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("start_time"),141));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(141,SSFNET_STRING("start_time")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("stop"),142));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(142,SSFNET_STRING("stop")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("stretch"),143));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(143,SSFNET_STRING("stretch")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("sub_edge_iface_map"),144));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(144,SSFNET_STRING("sub_edge_iface_map")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("sub_edge_ifaces"),145));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(145,SSFNET_STRING("sub_edge_ifaces")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("sum"),146));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(146,SSFNET_STRING("sum")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("tcpCA"),147));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(147,SSFNET_STRING("tcpCA")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("time_offset"),148));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(148,SSFNET_STRING("time_offset")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("time_skew"),149));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(149,SSFNET_STRING("time_skew")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("tmr_handle"),150));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(150,SSFNET_STRING("tmr_handle")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("tmr_map"),151));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(151,SSFNET_STRING("tmr_map")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("tmr_queue"),152));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(152,SSFNET_STRING("tmr_queue")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("toEmulated"),153));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(153,SSFNET_STRING("toEmulated")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("to_aggregate"),154));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(154,SSFNET_STRING("to_aggregate")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("to_emulated"),155));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(155,SSFNET_STRING("to_emulated")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("to_monitor"),156));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(156,SSFNET_STRING("to_monitor")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("total_bytes_received"),157));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(157,SSFNET_STRING("total_bytes_received")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("total_bytes_sent"),158));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(158,SSFNET_STRING("total_bytes_sent")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("trace_description"),159));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(159,SSFNET_STRING("trace_description")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("traffic_flows"),160));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(160,SSFNET_STRING("traffic_flows")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("traffic_intensity"),161));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(161,SSFNET_STRING("traffic_intensity")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("traffic_type_seed"),162));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(162,SSFNET_STRING("traffic_type_seed")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("tx_queue"),163));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(163,SSFNET_STRING("tx_queue")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("tx_timer_queue"),164));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(164,SSFNET_STRING("tx_timer_queue")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("uid"),165));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(165,SSFNET_STRING("uid")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("vacate_time"),166));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(166,SSFNET_STRING("vacate_time")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("var_id"),167));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(167,SSFNET_STRING("var_id")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("version"),168));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(168,SSFNET_STRING("version")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("wait_opt"),169));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(169,SSFNET_STRING("wait_opt")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("weight"),170));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(170,SSFNET_STRING("weight")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("window"),171));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(171,SSFNET_STRING("window")));
	jprime_attr_ids->insert(SSFNET_MAKE_PAIR(SSFNET_STRING("wndmax"),172));
	jprime_attr_strings->insert(SSFNET_MAKE_PAIR(172,SSFNET_STRING("wndmax")));
}
SSFNET_STRING* getJPRIMEAttrName(uint32_t attr_id) {	if(!jprime_attr_strings) setupJPRIMEAttrIDs();
	SSFNET_MAP(uint32_t,SSFNET_STRING)::iterator rv = jprime_attr_strings->find(attr_id);
	if(rv == jprime_attr_strings->end()) return NULL;
	return &(rv->second);
}
uint32_t getJPRIMEAttrID(SSFNET_STRING& str){
	if(!jprime_attr_ids) setupJPRIMEAttrIDs();
	SSFNET_MAP(SSFNET_STRING,uint32_t)::iterator rv = jprime_attr_ids->find(str);
	if(rv == jprime_attr_ids->end()) return 0;
	return rv->second;
}


//class CNFTraffic : public StaticTrafficType ...
SSFNET_REGISTER_ENTITY(CNFTraffic, , StaticTrafficType, , 1025, 1081, 1137, 1193);
SSFNET_CONFIG_PROPERTY_DEF(CNFTraffic, INT, dst_port, false, "1000", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(CNFTraffic, INT, content_id, false, "0", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(CNFTraffic, INT, number_of_contents, false, "1", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(CNFTraffic, INT, number_of_requests, false, "1", "xxx")
SSFNET_PROPMAP_DEF(CNFTraffic, &dst_port, &content_id, &number_of_contents, &number_of_requests)
SSFNET_STATEMAP_DEF(CNFTraffic)


//class CNFTransport : public ProtocolSession ...
SSFNET_REGISTER_ENTITY(CNFTransport, , ProtocolSession, , 1040, 1096, 1152, 1208);
SSFNET_CONFIG_STATE_DEF(CNFTransport, INT, bytes_received, false, "0", "xxx")
SSFNET_CONFIG_STATE_DEF(CNFTransport, INT, requests_received, false, "0", "xxx")
SSFNET_CONFIG_STATE_DEF(CNFTransport, INT, bytes_sent, false, "0", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(CNFTransport, OBJECT, cnf_routers, false, "[]", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(CNFTransport, BOOL, is_controller, false, "false", "xxx")
SSFNET_PROPMAP_DEF(CNFTransport, &cnf_routers, &is_controller)
SSFNET_STATEMAP_DEF(CNFTransport, &bytes_received, &requests_received, &bytes_sent)


//class CNFApplication : public ApplicationSession ...
SSFNET_REGISTER_ENTITY(CNFApplication, , ApplicationSession, , 1048, 1104, 1160, 1216);
SSFNET_CONFIG_PROPERTY_DEF(CNFApplication, INT, listening_port, false, "80", "xxx")
SSFNET_CONFIG_STATE_DEF(CNFApplication, INT, bytes_received, false, "0", "xxx")
SSFNET_CONFIG_STATE_DEF(CNFApplication, INT, bytes_sent, false, "0", "xxx")
SSFNET_CONFIG_STATE_DEF(CNFApplication, INT, requests_received, false, "0", "xxx")
SSFNET_CONFIG_STATE_DEF(CNFApplication, OBJECT, cnf_content_sizes, false, "[]", "xxx")
SSFNET_PROPMAP_DEF(CNFApplication, &listening_port)
SSFNET_STATEMAP_DEF(CNFApplication, &bytes_received, &bytes_sent, &requests_received, &cnf_content_sizes)


//class UDPTraffic : public StaticTrafficType ...
SSFNET_REGISTER_ENTITY(UDPTraffic, , StaticTrafficType, , 1026, 1082, 1138, 1194);
SSFNET_CONFIG_PROPERTY_DEF(UDPTraffic, INT, dstPort, false, "5001", "xxx")
SSFNET_CONFIG_STATE_DEF(UDPTraffic, INT, bytesToSendEachInterval, false, "1000000", "xxx")
SSFNET_CONFIG_STATE_DEF(UDPTraffic, INT, count, false, "10", "xxx")
SSFNET_CONFIG_STATE_DEF(UDPTraffic, BOOL, toEmulated, false, "false", "xxx")
SSFNET_PROPMAP_DEF(UDPTraffic, &dstPort)
SSFNET_STATEMAP_DEF(UDPTraffic, &bytesToSendEachInterval, &count, &toEmulated)


//class CBR : public ApplicationSession ...
SSFNET_REGISTER_ENTITY(CBR, , ApplicationSession, , 1049, 1105, 1161, 1217);
SSFNET_CONFIG_PROPERTY_DEF(CBR, INT, listening_port, false, "5001", "xxx")
SSFNET_CONFIG_STATE_DEF(CBR, INT, total_bytes_received, false, "0", "xxx")
SSFNET_CONFIG_STATE_DEF(CBR, INT, total_bytes_sent, false, "0", "xxx")
SSFNET_PROPMAP_DEF(CBR, &listening_port)
SSFNET_STATEMAP_DEF(CBR, &total_bytes_received, &total_bytes_sent)


//class PPBPTraffic : public StaticTrafficType ...
SSFNET_REGISTER_ENTITY(PPBPTraffic, , StaticTrafficType, , 1027, 1083, 1139, 1195);
SSFNET_CONFIG_PROPERTY_DEF(PPBPTraffic, INT, dst_port, false, "5001", "xxx")
SSFNET_CONFIG_STATE_DEF(PPBPTraffic, INT, bytes_to_send_per_interval, false, "1000000", "xxx")
SSFNET_CONFIG_STATE_DEF(PPBPTraffic, INT, avg_sessions, false, "1", "xxx")
SSFNET_CONFIG_STATE_DEF(PPBPTraffic, FLOAT, hurst, false, "0.8", "xxx")
SSFNET_CONFIG_STATE_DEF(PPBPTraffic, FLOAT, stop, false, "1000000", "xxx")
SSFNET_PROPMAP_DEF(PPBPTraffic, &dst_port)
SSFNET_STATEMAP_DEF(PPBPTraffic, &bytes_to_send_per_interval, &avg_sessions, &hurst, &stop)


//class STCPMaster : public ProtocolSession ...
SSFNET_REGISTER_ENTITY(STCPMaster, , ProtocolSession, , 1041, 1097, 1153, 1209);
SSFNET_CONFIG_PROPERTY_DEF(STCPMaster, INT, max_datagram_size, false, "1470", "xxx")
SSFNET_PROPMAP_DEF(STCPMaster, &max_datagram_size)
SSFNET_STATEMAP_DEF(STCPMaster)


//class UDPMaster : public ProtocolSession ...
SSFNET_REGISTER_ENTITY(UDPMaster, , ProtocolSession, , 1042, 1098, 1154, 1210);
SSFNET_CONFIG_PROPERTY_DEF(UDPMaster, INT, max_datagram_size, false, "1470", "xxx")
SSFNET_PROPMAP_DEF(UDPMaster, &max_datagram_size)
SSFNET_STATEMAP_DEF(UDPMaster)


//class SwingServer : public ApplicationSession ...
SSFNET_REGISTER_ENTITY(SwingServer, , ApplicationSession, , 1050, 1106, 1162, 1218);
SSFNET_CONFIG_PROPERTY_DEF(SwingServer, INT, listeningPort, false, "1024", "xxx")
SSFNET_CONFIG_STATE_DEF(SwingServer, INT, bytesReceived, false, "0", "xxx")
SSFNET_CONFIG_STATE_DEF(SwingServer, INT, requestsReceived, false, "0", "xxx")
SSFNET_PROPMAP_DEF(SwingServer, &listeningPort)
SSFNET_STATEMAP_DEF(SwingServer, &bytesReceived, &requestsReceived)


//class SwingClient : public ApplicationSession ...
SSFNET_REGISTER_ENTITY(SwingClient, , ApplicationSession, , 1051, 1107, 1163, 1219);
SSFNET_CONFIG_STATE_DEF(SwingClient, INT, active_sessions, false, "0", "xxx")
SSFNET_CONFIG_STATE_DEF(SwingClient, INT, bytes_received, false, "0", "xxx")
SSFNET_PROPMAP_DEF(SwingClient)
SSFNET_STATEMAP_DEF(SwingClient, &active_sessions, &bytes_received)


//class SwingTCPTraffic : public CentralizedTrafficType ...
SSFNET_REGISTER_ENTITY(SwingTCPTraffic, , CentralizedTrafficType, , 1023, 1079, 1135, 1191);
SSFNET_CONFIG_PROPERTY_DEF(SwingTCPTraffic, STRING, trace_description, false, " ", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(SwingTCPTraffic, BOOL, stretch, false, "false", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(SwingTCPTraffic, FLOAT, session_timeout, false, "300", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(SwingTCPTraffic, FLOAT, rre_timeout, false, "30", "xxx")
SSFNET_PROPMAP_DEF(SwingTCPTraffic, &trace_description, &stretch, &session_timeout, &rre_timeout)
SSFNET_STATEMAP_DEF(SwingTCPTraffic)


//class SymbioSimAppProt : public ProtocolSession ...
SSFNET_REGISTER_ENTITY(SymbioSimAppProt, , ProtocolSession, , 1043, 1099, 1155, 1211);
SSFNET_PROPMAP_DEF(SymbioSimAppProt)
SSFNET_STATEMAP_DEF(SymbioSimAppProt)


//class TCPTraffic : public StaticTrafficType ...
SSFNET_REGISTER_ENTITY(TCPTraffic, , StaticTrafficType, , 1028, 1084, 1140, 1196);
SSFNET_CONFIG_PROPERTY_DEF(TCPTraffic, INT, dst_port, false, "80", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(TCPTraffic, INT, file_size, false, "1024000", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(TCPTraffic, BOOL, file_pareto, false, "false", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(TCPTraffic, BOOL, to_emulated, false, "false", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(TCPTraffic, INT, number_of_sessions, false, "1", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(TCPTraffic, INT, connections_per_session, false, "1", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(TCPTraffic, INT, session_timeout, false, "30.0", "xxx")
SSFNET_PROPMAP_DEF(TCPTraffic, &dst_port, &file_size, &file_pareto, &to_emulated, &number_of_sessions, &connections_per_session, &session_timeout)
SSFNET_STATEMAP_DEF(TCPTraffic)


//class HTTPClient : public ApplicationSession ...
SSFNET_REGISTER_ENTITY(HTTPClient, , ApplicationSession, , 1052, 1108, 1164, 1220);
SSFNET_CONFIG_STATE_DEF(HTTPClient, INT, active_sessions, false, "0", "xxx")
SSFNET_CONFIG_STATE_DEF(HTTPClient, INT, bytes_received, false, "0", "xxx")
SSFNET_PROPMAP_DEF(HTTPClient)
SSFNET_STATEMAP_DEF(HTTPClient, &active_sessions, &bytes_received)


//class HTTPServer : public ApplicationSession ...
SSFNET_REGISTER_ENTITY(HTTPServer, , ApplicationSession, , 1053, 1109, 1165, 1221);
SSFNET_CONFIG_PROPERTY_DEF(HTTPServer, INT, listeningPort, false, "80", "xxx")
SSFNET_CONFIG_STATE_DEF(HTTPServer, INT, bytesReceived, false, "0", "xxx")
SSFNET_CONFIG_STATE_DEF(HTTPServer, INT, requestsReceived, false, "0", "xxx")
SSFNET_PROPMAP_DEF(HTTPServer, &listeningPort)
SSFNET_STATEMAP_DEF(HTTPServer, &bytesReceived, &requestsReceived)


//class TCPMaster : public ProtocolSession ...
SSFNET_REGISTER_ENTITY(TCPMaster, , ProtocolSession, , 1044, 1100, 1156, 1212);
SSFNET_CONFIG_PROPERTY_DEF(TCPMaster, STRING, tcpCA, false, "bic", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(TCPMaster, INT, mss, false, "1400", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(TCPMaster, INT, sndWndSize, false, "1000000000", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(TCPMaster, INT, sndBufSize, false, "2000000000", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(TCPMaster, INT, rcvWndSize, false, "1000000000", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(TCPMaster, FLOAT, samplingInterval, false, "10000", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(TCPMaster, INT, iss, false, "1", "xxx")
SSFNET_PROPMAP_DEF(TCPMaster, &tcpCA, &mss, &sndWndSize, &sndBufSize, &rcvWndSize, &samplingInterval, &iss)
SSFNET_STATEMAP_DEF(TCPMaster)


//class TrafficPortal : public EmulationProtocol ...
SSFNET_REGISTER_ENTITY(TrafficPortal, , EmulationProtocol, , 1002, 1058, 1114, 1170);
SSFNET_CONFIG_STATE_DEF(TrafficPortal, OBJECT, networks, false, "[]", "xxx")
SSFNET_PROPMAP_DEF(TrafficPortal)
SSFNET_STATEMAP_DEF(TrafficPortal, &networks)


//class TAPEmulation : public EmulationProtocol ...
SSFNET_REGISTER_ENTITY(TAPEmulation, , EmulationProtocol, , 1003, 1059, 1115, 1171);
SSFNET_PROPMAP_DEF(TAPEmulation)
SSFNET_STATEMAP_DEF(TAPEmulation)


//class OpenVPNEmulation : public EmulationProtocol ...
SSFNET_REGISTER_ENTITY(OpenVPNEmulation, , EmulationProtocol, , 1004, 1060, 1116, 1172);
SSFNET_PROPMAP_DEF(OpenVPNEmulation)
SSFNET_STATEMAP_DEF(OpenVPNEmulation)


//class EmulationProtocol : public BaseEntity ...
SSFNET_REGISTER_ENTITY(EmulationProtocol, , BaseEntity, , 1001, 1057, 1113, 1169);
SSFNET_CONFIG_PROPERTY_DEF(EmulationProtocol, BOOL, ip_forwarding, false, "false", "xxx")
SSFNET_PROPMAP_DEF(EmulationProtocol, &ip_forwarding)
SSFNET_STATEMAP_DEF(EmulationProtocol)


//class ProbeSession : public ProtocolSession ...
SSFNET_REGISTER_ENTITY(ProbeSession, , ProtocolSession, , 1045, 1101, 1157, 1213);
SSFNET_PROPMAP_DEF(ProbeSession)
SSFNET_STATEMAP_DEF(ProbeSession)


//class FluidTraffic : public TrafficType ...
SSFNET_REGISTER_ENTITY(FluidTraffic, , TrafficType, , 1019, 1075, 1131, 1187);
SSFNET_CONFIG_PROPERTY_DEF(FluidTraffic, STRING, protocol_type, false, "tcp_reno", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(FluidTraffic, RESOURCE_ID, srcs, false, "", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(FluidTraffic, RESOURCE_ID, dsts, false, "", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(FluidTraffic, FLOAT, off_time, false, "0.01", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(FluidTraffic, STRING, mapping, false, "all2all", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(FluidTraffic, INT, nflows, false, "10", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(FluidTraffic, FLOAT, pktsiz, false, "1000.0", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(FluidTraffic, FLOAT, hurst, false, "0.75", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(FluidTraffic, FLOAT, md, false, "1.5", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(FluidTraffic, FLOAT, wndmax, false, "128", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(FluidTraffic, FLOAT, sendrate, false, "0", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(FluidTraffic, FLOAT, start, false, "0", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(FluidTraffic, FLOAT, stop, false, "1000", "xxx")
SSFNET_CONFIG_STATE_DEF(FluidTraffic, STRING, monfn, false, " ", "xxx")
SSFNET_CONFIG_STATE_DEF(FluidTraffic, FLOAT, monres, false, "0.001", "xxx")
//skipping int protocol_type
//skipping float pktsiz
//skipping float sendrate
//skipping float wndmax
//skipping float window
//skipping int mapping
//skipping TrafficFlowList * traffic_flows
SSFNET_PROPMAP_DEF(FluidTraffic, &protocol_type, &srcs, &dsts, &off_time, &mapping, &nflows, &pktsiz, &hurst, &md, &wndmax, &sendrate, &start, &stop)
SSFNET_STATEMAP_DEF(FluidTraffic, &monfn, &monres)


//class FluidQueue : public NicQueue ...
SSFNET_REGISTER_ENTITY(FluidQueue, , NicQueue, , 1011, 1067, 1123, 1179);
SSFNET_CONFIG_PROPERTY_DEF(FluidQueue, STRING, queue_type, false, "red", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(FluidQueue, BOOL, is_red, false, "true", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(FluidQueue, FLOAT, weight, false, "0.0001", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(FluidQueue, FLOAT, qmin, false, "0", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(FluidQueue, FLOAT, qmax, false, "0", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(FluidQueue, FLOAT, pmax, false, "0.2", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(FluidQueue, BOOL, wait_opt, false, "true", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(FluidQueue, FLOAT, mean_pktsiz, false, "500", "xxx")
//skipping float weight
//skipping float qmin
//skipping float qmax
//skipping float pmax
//skipping float mean_pktsiz
//skipping float fluid_weight
//skipping float prop_delay
//skipping float aggr_arrival
//skipping float pkt_arrival
//skipping float queue
//skipping float avgque
//skipping float loss
//skipping float shadow_queue
//skipping float shadow_avgque
//skipping float shadow_loss
//skipping bool crossing
//skipping float interdrop
//skipping VirtualTime last_update_time
//skipping VirtualTime last_send_time
SSFNET_PROPMAP_DEF(FluidQueue, &queue_type, &is_red, &weight, &qmin, &qmax, &pmax, &wait_opt, &mean_pktsiz)
SSFNET_STATEMAP_DEF(FluidQueue)


//class IPv4Session : public ProtocolSession ...
SSFNET_REGISTER_ENTITY(IPv4Session, , ProtocolSession, , 1046, 1102, 1158, 1214);
SSFNET_PROPMAP_DEF(IPv4Session)
SSFNET_STATEMAP_DEF(IPv4Session)


//class PingTraffic : public ICMPTraffic ...
SSFNET_REGISTER_ENTITY(PingTraffic, , ICMPTraffic, , 1030, 1086, 1142, 1198);
SSFNET_CONFIG_PROPERTY_DEF(PingTraffic, INT, payload_size, false, "56", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(PingTraffic, INT, count, false, "1", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(PingTraffic, FLOAT, rtt, false, "0", "xxx")
SSFNET_PROPMAP_DEF(PingTraffic, &payload_size, &count, &rtt)
SSFNET_STATEMAP_DEF(PingTraffic)


//class ICMPTraffic : public StaticTrafficType ...
SSFNET_REGISTER_ENTITY(ICMPTraffic, , StaticTrafficType, , 1029, 1085, 1141, 1197);
SSFNET_CONFIG_PROPERTY_DEF(ICMPTraffic, STRING, version, false, "4", "xxx")
SSFNET_PROPMAP_DEF(ICMPTraffic, &version)
SSFNET_STATEMAP_DEF(ICMPTraffic)


//class ICMPv4Session : public ApplicationSession ...
SSFNET_REGISTER_ENTITY(ICMPv4Session, , ApplicationSession, , 1054, 1110, 1166, 1222);
SSFNET_PROPMAP_DEF(ICMPv4Session)
SSFNET_STATEMAP_DEF(ICMPv4Session)


//class ApplicationSession : public ProtocolSession ...
SSFNET_REGISTER_ENTITY(ApplicationSession, , ProtocolSession, , 1047, 1103, 1159, 1215);
SSFNET_PROPMAP_DEF(ApplicationSession)
SSFNET_STATEMAP_DEF(ApplicationSession)


//class RoutingProtocol : public ProtocolSession ...
SSFNET_REGISTER_ENTITY(RoutingProtocol, , ProtocolSession, , 1055, 1111, 1167, 1223);
SSFNET_PROPMAP_DEF(RoutingProtocol)
SSFNET_STATEMAP_DEF(RoutingProtocol)


//class Link : public BaseEntity ...
SSFNET_REGISTER_ENTITY(Link, , BaseEntity, , 1005, 1061, 1117, 1173);
SSFNET_CONFIG_CHILDREN_DEF(Link, BaseInterface, attachments, true, 0, 0, "xxx");
SSFNET_CONFIG_PROPERTY_DEF(Link, FLOAT, delay, false, "0.001", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(Link, FLOAT, bandwidth, false, "1e8", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(Link, INT, ip_prefix_len, false, "-1", "xxx")
SSFNET_CONFIG_STATE_DEF(Link, OBJECT, ip_prefix, false, "0.0.0.0/0", "xxx")
//skipping LinkInfo::Map com_id2link_info
//skipping IP2IFACE_MAP ip2iface
//skipping MAC2IFACE_MAP mac2iface
SSFNET_PROPMAP_DEF(Link, &delay, &bandwidth, &ip_prefix_len)
SSFNET_STATEMAP_DEF(Link, &ip_prefix)


//class Interface : public BaseInterface ...
SSFNET_REGISTER_ENTITY(Interface, , BaseInterface, , 1007, 1063, 1119, 1175, "Iface");
SSFNET_CONFIG_CHILDREN_DEF(Interface, NicQueue, nic_queue, false, 0, 1, "xxx");
SSFNET_CONFIG_CHILDREN_DEF(Interface, EmulationProtocol, emu_proto, false, 0, 1, "xxx");
SSFNET_CONFIG_PROPERTY_DEF(Interface, FLOAT, bit_rate, false, "1e10", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(Interface, FLOAT, latency, false, "0", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(Interface, FLOAT, jitter_range, false, "0", "xxx")
SSFNET_CONFIG_STATE_DEF(Interface, FLOAT, drop_probability, false, "0", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(Interface, INT, buffer_size, false, "65536", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(Interface, INT, mtu, false, "1500", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(Interface, STRING, queue_type, false, "DropTailQueue", "xxx")
SSFNET_CONFIG_STATE_DEF(Interface, BOOL, is_on, false, "true", "xxx")
SSFNET_CONFIG_STATE_DEF(Interface, INT, num_in_packets, false, "0", "xxx")
SSFNET_CONFIG_STATE_DEF(Interface, INT, num_in_bytes, false, "0", "xxx")
SSFNET_CONFIG_STATE_DEF(Interface, FLOAT, packets_in_per_sec, false, "0", "xxx")
SSFNET_CONFIG_STATE_DEF(Interface, FLOAT, bytes_in_per_sec, false, "0", "xxx")
SSFNET_CONFIG_STATE_DEF(Interface, INT, num_in_ucast_packets, false, "0", "xxx")
SSFNET_CONFIG_STATE_DEF(Interface, INT, num_in_ucast_bytes, false, "0", "xxx")
SSFNET_CONFIG_STATE_DEF(Interface, INT, num_out_packets, false, "0", "xxx")
SSFNET_CONFIG_STATE_DEF(Interface, INT, num_out_bytes, false, "0", "xxx")
SSFNET_CONFIG_STATE_DEF(Interface, INT, num_out_ucast_packets, false, "0", "xxx")
SSFNET_CONFIG_STATE_DEF(Interface, INT, num_out_ucast_bytes, false, "0", "xxx")
SSFNET_CONFIG_STATE_DEF(Interface, FLOAT, packets_out_per_sec, false, "0", "xxx")
SSFNET_CONFIG_STATE_DEF(Interface, FLOAT, bytes_out_per_sec, false, "0", "xxx")
SSFNET_CONFIG_STATE_DEF(Interface, INT, queue_size, false, "0", "xxx")
//skipping EmulationProtocol * emu_protocol
//skipping NicQueue * tx_queue
//skipping TxTimerQueue * tx_timer_queue
//skipping IPv4Session * ip_sess
SSFNET_PROPMAP_DEF(Interface, &bit_rate, &latency, &jitter_range, &buffer_size, &mtu, &queue_type)
SSFNET_STATEMAP_DEF(Interface, &drop_probability, &is_on, &num_in_packets, &num_in_bytes, &packets_in_per_sec, &bytes_in_per_sec, &num_in_ucast_packets, &num_in_ucast_bytes, &num_out_packets, &num_out_bytes, &num_out_ucast_packets, &num_out_ucast_bytes, &packets_out_per_sec, &bytes_out_per_sec, &queue_size)


//class GhostInterface : public BaseInterface ...
SSFNET_REGISTER_ENTITY(GhostInterface, , BaseInterface, , 1008, 1064, 1120, 1176);
SSFNET_CONFIG_STATE_DEF(GhostInterface, INT, real_uid, false, "0", "xxx")
SSFNET_CONFIG_STATE_DEF(GhostInterface, INT, community_id, false, "0", "xxx")
SSFNET_PROPMAP_DEF(GhostInterface)
SSFNET_STATEMAP_DEF(GhostInterface, &real_uid, &community_id)


//class BaseInterface : public BaseEntity ...
SSFNET_REGISTER_ENTITY(BaseInterface, , BaseEntity, , 1006, 1062, 1118, 1174);
SSFNET_CONFIG_STATE_DEF(BaseInterface, OBJECT, ip_address, false, "0.0.0.0", "xxx")
SSFNET_CONFIG_STATE_DEF(BaseInterface, OBJECT, mac_address, false, "0:0:0:0:0:0", "xxx")
//skipping Link * attached_link
SSFNET_PROPMAP_DEF(BaseInterface)
SSFNET_STATEMAP_DEF(BaseInterface, &ip_address, &mac_address)


//class Router : public Host ...
SSFNET_REGISTER_ENTITY(Router, , Host, , 1033, 1089, 1145, 1201);
SSFNET_PROPMAP_DEF(Router)
SSFNET_STATEMAP_DEF(Router)


//class Host : public ProtocolGraph ...
SSFNET_REGISTER_ENTITY(Host, , ProtocolGraph, , 1032, 1088, 1144, 1200);
SSFNET_CONFIG_CHILDREN_DEF(Host, Interface, nics, false, 0, 0, "xxx");
SSFNET_CONFIG_PROPERTY_DEF(Host, INT, host_seed, false, "0", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(Host, FLOAT, time_skew, false, "1.0", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(Host, OBJECT, time_offset, false, "0.0", "xxx")
SSFNET_CONFIG_STATE_DEF(Host, FLOAT, traffic_intensity, false, "0", "xxx")
//skipping IP2IFACE_MAP ip2iface
//skipping IPAddress default_ip
//skipping HostTimerQueue * tmr_queue
//skipping SSFNET_INT2PTR_MAP * tmr_map
//skipping int tmr_handle
//skipping prime::rng::Random * rng
//skipping int rng_seed
//skipping PortalIfaceTable * portals
SSFNET_PROPMAP_DEF(Host, &host_seed, &time_skew, &time_offset)
SSFNET_STATEMAP_DEF(Host, &traffic_intensity)


//class DropTailQueue : public NicQueue ...
SSFNET_REGISTER_ENTITY(DropTailQueue, , NicQueue, , 1012, 1068, 1124, 1180);
//skipping VirtualTime last_xmit_time
//skipping VirtualTime queue_delay
//skipping VirtualTime max_queue_delay
SSFNET_PROPMAP_DEF(DropTailQueue)
SSFNET_STATEMAP_DEF(DropTailQueue)


//class Net : public BaseEntity ...
SSFNET_REGISTER_ENTITY(Net, , BaseEntity, , 1009, 1065, 1121, 1177);
SSFNET_CONFIG_CHILDREN_DEF(Net, Net, subnets, false, 0, 0, "xxx");
SSFNET_CONFIG_CHILDREN_DEF(Net, Host, hosts, false, 0, 0, "xxx");
SSFNET_CONFIG_CHILDREN_DEF(Net, Link, links, false, 0, 0, "xxx");
SSFNET_CONFIG_CHILDREN_DEF(Net, PlaceHolder, placeholders, false, 0, 0, "xxx");
SSFNET_CONFIG_CHILDREN_DEF(Net, RoutingSphere, rsphere, false, 1, 1, "xxx");
SSFNET_CONFIG_CHILDREN_DEF(Net, Monitor, monitors, false, 0, 0, "xxx");
SSFNET_CONFIG_CHILDREN_DEF(Net, Aggregate, aggregates, false, 0, 0, "xxx");
SSFNET_CONFIG_CHILDREN_DEF(Net, Traffic, traffics, false, 0, 1, "xxx");
SSFNET_CONFIG_PROPERTY_DEF(Net, INT, ip_prefix_len, false, "-1", "xxx")
SSFNET_CONFIG_STATE_DEF(Net, OBJECT, ip_prefix, false, "0.0.0.0/0", "xxx")
SSFNET_CONFIG_STATE_DEF(Net, OBJECT, cnf_content_ids, false, "[]", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(Net, OBJECT, portal_rules, false, "[]", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(Net, INT, controller_rid, false, "0", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(Net, OBJECT, sub_edge_ifaces, false, "[]", "xxx")
SSFNET_PROPMAP_DEF(Net, &ip_prefix_len, &portal_rules, &controller_rid, &sub_edge_ifaces)
SSFNET_STATEMAP_DEF(Net, &ip_prefix, &cnf_content_ids)


//class RedQueue : public NicQueue ...
SSFNET_REGISTER_ENTITY(RedQueue, , NicQueue, , 1013, 1069, 1125, 1181);
SSFNET_CONFIG_PROPERTY_DEF(RedQueue, FLOAT, weight, false, "0", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(RedQueue, FLOAT, qmin, false, "0", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(RedQueue, FLOAT, qmax, false, "0", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(RedQueue, FLOAT, qcap, false, "0", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(RedQueue, FLOAT, pmax, false, "0.2", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(RedQueue, BOOL, wait_opt, false, "true", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(RedQueue, INT, mean_pktsiz, false, "500", "xxx")
//skipping float weight
//skipping float qmin
//skipping float qmax
//skipping float qcap
//skipping float pmax
//skipping float mean_pktsiz
//skipping float queue
//skipping float avgque
//skipping float loss
//skipping bool crossing
//skipping float interdrop
//skipping VirtualTime last_update_time
//skipping VirtualTime vacate_time
SSFNET_PROPMAP_DEF(RedQueue, &weight, &qmin, &qmax, &qcap, &pmax, &wait_opt, &mean_pktsiz)
SSFNET_STATEMAP_DEF(RedQueue)


//class NicQueue : public BaseEntity ...
SSFNET_REGISTER_ENTITY(NicQueue, , BaseEntity, , 1010, 1066, 1122, 1178);
SSFNET_PROPMAP_DEF(NicQueue)
SSFNET_STATEMAP_DEF(NicQueue)


//class VizAggregate : public Aggregate ...
SSFNET_REGISTER_ENTITY(VizAggregate, , Aggregate, , 1015, 1071, 1127, 1183);
SSFNET_PROPMAP_DEF(VizAggregate)
SSFNET_STATEMAP_DEF(VizAggregate)


//class Aggregate : public BaseEntity ...
SSFNET_REGISTER_ENTITY(Aggregate, , BaseEntity, , 1014, 1070, 1126, 1182);
SSFNET_CONFIG_PROPERTY_DEF(Aggregate, INT, var_id, false, "0", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(Aggregate, RESOURCE_ID, to_aggregate, false, "", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(Aggregate, FLOAT, min, false, "0", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(Aggregate, FLOAT, max, false, "0", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(Aggregate, FLOAT, sample_count, false, "0", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(Aggregate, FLOAT, sum, false, "0", "xxx")
SSFNET_PROPMAP_DEF(Aggregate, &var_id, &to_aggregate, &min, &max, &sample_count, &sum)
SSFNET_STATEMAP_DEF(Aggregate)


//class Monitor : public BaseEntity ...
SSFNET_REGISTER_ENTITY(Monitor, , BaseEntity, , 1016, 1072, 1128, 1184);
SSFNET_CONFIG_PROPERTY_DEF(Monitor, INT, period, false, "500", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(Monitor, RESOURCE_ID, to_monitor, false, "", "xxx")
SSFNET_PROPMAP_DEF(Monitor, &period, &to_monitor)
SSFNET_STATEMAP_DEF(Monitor)


//class Traffic : public BaseEntity ...
SSFNET_REGISTER_ENTITY(Traffic, , BaseEntity, , 1017, 1073, 1129, 1185);
SSFNET_CONFIG_CHILDREN_DEF(Traffic, TrafficType, traffic_types, false, 0, 0, "xxx");
SSFNET_PROPMAP_DEF(Traffic)
SSFNET_STATEMAP_DEF(Traffic)


//class DistributedTrafficType : public DynamicTrafficType ...
SSFNET_REGISTER_ENTITY(DistributedTrafficType, , DynamicTrafficType, , 1021, 1077, 1133, 1189);
SSFNET_PROPMAP_DEF(DistributedTrafficType)
SSFNET_STATEMAP_DEF(DistributedTrafficType)


//class CentralizedTrafficType : public DynamicTrafficType ...
SSFNET_REGISTER_ENTITY(CentralizedTrafficType, , DynamicTrafficType, , 1022, 1078, 1134, 1190);
SSFNET_CONFIG_PROPERTY_DEF(CentralizedTrafficType, RESOURCE_ID, srcs, false, "", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(CentralizedTrafficType, RESOURCE_ID, dsts, false, "", "xxx")
SSFNET_PROPMAP_DEF(CentralizedTrafficType, &srcs, &dsts)
SSFNET_STATEMAP_DEF(CentralizedTrafficType)


//class DynamicTrafficType : public TrafficType ...
SSFNET_REGISTER_ENTITY(DynamicTrafficType, , TrafficType, , 1020, 1076, 1132, 1188);
SSFNET_PROPMAP_DEF(DynamicTrafficType)
SSFNET_STATEMAP_DEF(DynamicTrafficType)


//class StaticTrafficType : public TrafficType ...
SSFNET_REGISTER_ENTITY(StaticTrafficType, , TrafficType, , 1024, 1080, 1136, 1192);
SSFNET_CONFIG_PROPERTY_DEF(StaticTrafficType, RESOURCE_ID, srcs, false, "", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(StaticTrafficType, RESOURCE_ID, dsts, false, "", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(StaticTrafficType, OBJECT, dst_ips, false, "[]", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(StaticTrafficType, FLOAT, start_time, false, "0", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(StaticTrafficType, FLOAT, interval, false, "0.1", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(StaticTrafficType, BOOL, interval_exponential, false, "false", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(StaticTrafficType, STRING, mapping, false, "all2all", "xxx")
//skipping int mapping
//skipping TrafficFlowList * traffic_flows
//skipping HybridTrafficFlowList * hybrid_traffic_flows
SSFNET_PROPMAP_DEF(StaticTrafficType, &srcs, &dsts, &dst_ips, &start_time, &interval, &interval_exponential, &mapping)
SSFNET_STATEMAP_DEF(StaticTrafficType)


//class TrafficType : public BaseEntity ...
SSFNET_REGISTER_ENTITY(TrafficType, , BaseEntity, , 1018, 1074, 1130, 1186);
SSFNET_CONFIG_PROPERTY_DEF(TrafficType, INT, traffic_type_seed, false, "0", "xxx")
SSFNET_CONFIG_STATE_DEF(TrafficType, OBJECT, community_ids, false, "[]", "xxx")
//skipping prime::rng::Random * rng
//skipping int rng_seed
SSFNET_PROPMAP_DEF(TrafficType, &traffic_type_seed)
SSFNET_STATEMAP_DEF(TrafficType, &community_ids)


//class ProtocolGraph : public BaseEntity ...
SSFNET_REGISTER_ENTITY(ProtocolGraph, , BaseEntity, , 1031, 1087, 1143, 1199);
SSFNET_CONFIG_CHILDREN_DEF(ProtocolGraph, ProtocolSession, cfg_sess, false, 0, 0, "xxx");
SSFNET_CONFIG_PROPERTY_DEF(ProtocolGraph, BOOL, automatic_protocol_session_creation, false, "true", "xxx")
//skipping bool initialized
//skipping PROTONAME_MAP pname_map
//skipping PROTONUM_MAP pno_map
//skipping PSESS_VECTOR protocol_list
SSFNET_PROPMAP_DEF(ProtocolGraph, &automatic_protocol_session_creation)
SSFNET_STATEMAP_DEF(ProtocolGraph)


//class RouteTable : public BaseEntity ...
SSFNET_REGISTER_ENTITY(RouteTable, , BaseEntity, , 1034, 1090, 1146, 1202);
SSFNET_CONFIG_PROPERTY_DEF(RouteTable, OBJECT, edge_ifaces, false, "[]", "xxx")
//skipping RTREE routemap
//skipping EdgeInterfaceList my_edge_ifaces
SSFNET_PROPMAP_DEF(RouteTable, &edge_ifaces)
SSFNET_STATEMAP_DEF(RouteTable)


//class GhostRoutingSphere : public RoutingSphere ...
SSFNET_REGISTER_ENTITY(GhostRoutingSphere, , RoutingSphere, , 1036, 1092, 1148, 1204);
SSFNET_CONFIG_STATE_DEF(GhostRoutingSphere, INT, community_id, false, "0", "xxx")
SSFNET_PROPMAP_DEF(GhostRoutingSphere)
SSFNET_STATEMAP_DEF(GhostRoutingSphere, &community_id)


//class RoutingSphere : public BaseEntity ...
SSFNET_REGISTER_ENTITY(RoutingSphere, , BaseEntity, , 1035, 1091, 1147, 1203);
SSFNET_CONFIG_CHILDREN_DEF(RoutingSphere, RouteTable, default_route_table, false, 0, 1, "xxx");
SSFNET_CONFIG_PROPERTY_DEF(RoutingSphere, INT, nix_vec_cache_size, false, "1000", "xxx")
SSFNET_CONFIG_PROPERTY_DEF(RoutingSphere, INT, local_dst_cache_size, false, "1000", "xxx")
//skipping HostSrcRankListPairMap sub_edge_iface_map
SSFNET_PROPMAP_DEF(RoutingSphere, &nix_vec_cache_size, &local_dst_cache_size)
SSFNET_STATEMAP_DEF(RoutingSphere)


//class PlaceHolder : public BaseEntity ...
SSFNET_REGISTER_ENTITY(PlaceHolder, , BaseEntity, , 1037, 1093, 1149, 1205);
SSFNET_PROPMAP_DEF(PlaceHolder)
SSFNET_STATEMAP_DEF(PlaceHolder)


//class Alias : public BaseEntity ...
SSFNET_REGISTER_ENTITY(Alias, , BaseEntity, , 1038, 1094, 1150, 1206, "NodeAlias");
SSFNET_CONFIG_STATE_DEF(Alias, OBJECT, alias_path, false, "", "xxx")
//skipping BaseEntity * shortcut_to
SSFNET_PROPMAP_DEF(Alias)
SSFNET_STATEMAP_DEF(Alias, &alias_path)


//class ProtocolSession : public BaseEntity ...
SSFNET_REGISTER_ENTITY(ProtocolSession, , BaseEntity, , 1039, 1095, 1151, 1207);
SSFNET_PROPMAP_DEF(ProtocolSession)
SSFNET_STATEMAP_DEF(ProtocolSession)
} //end prime
} //end ssfnet
