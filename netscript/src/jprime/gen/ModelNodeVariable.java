/* ------------------------- */
/* ------------------------- */
/*         WARNING: */
/*  THIS FILE IS GENERATED! */
/*        DO NOT EDIT! */
/* ------------------------- */
/* ------------------------- */


package jprime.gen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.List;
import java.util.NoSuchElementException;

import jprime.variable.BooleanVariable;
import jprime.variable.FloatingPointNumberVariable;
import jprime.variable.ResourceIdentifierVariable;
import jprime.variable.IntegerVariable;
import jprime.variable.OpaqueVariable;
import jprime.variable.StringVariable;

public abstract class ModelNodeVariable {
	private final static Class<? extends ModelNodeVariable> BOOL = BooleanVariable.class;
	private final static Class<? extends ModelNodeVariable> FLOAT = FloatingPointNumberVariable.class;
	private final static Class<? extends ModelNodeVariable> INT = IntegerVariable.class;
	private final static Class<? extends ModelNodeVariable> OBJECT = OpaqueVariable.class;
	private final static Class<? extends ModelNodeVariable> RESOURCE_ID = ResourceIdentifierVariable.class;
	private final static Class<? extends ModelNodeVariable> STRING = StringVariable.class;
	public static class ClsDesc {
		public final String name;
		public final Class<?> cls;
		public final ClsDesc super_desc;
		public final Map<Integer,VarDesc> vars;
		public ClsDesc(String name, Class<?> cls, ClsDesc super_desc) {
			super();
			this.name = name;
			this.cls = cls;
			this.super_desc = super_desc;
			this.vars = new HashMap<Integer,VarDesc>();
		}
	}
	public static class VarDesc {
		public final Integer id;
		public final String name;
		public final String doc_string;
		public final String default_value;
		public final boolean is_visualized;
		public final boolean is_stat;
		public final Class<? extends ModelNodeVariable> type;
		public final ClsDesc cls;
		public VarDesc(Integer id, String name, String doc_string, String default_value, boolean is_visualized, boolean is_stat, Class<? extends ModelNodeVariable> type, ClsDesc cls) {
			super();
			this.id = id;
			this.name = name;
			this.doc_string = doc_string;
			this.default_value = default_value;
			this.is_visualized = is_visualized;
			this.is_stat = is_stat;
			this.type = type;
			this.cls = cls;
			this.cls.vars.put(this.id,this);
		}
	}
	public static class Tripple {
		public final Integer id;
		public final String fname;
		public final Class<?> cls;
		public Tripple(Integer id, String fname, Class<?> cls){
			this.id=id;
			this.fname=fname;
			this.cls=cls;
		}
	}
	protected final static HashMap<String, List<Tripple>> jythonMap = new HashMap<String, List<Tripple>>();
	protected final static HashMap<String, Integer> str2id = new HashMap<String, Integer>();
	protected final static HashMap<Integer, Class<? extends ModelNodeVariable>> varTypes = new HashMap<Integer, Class<? extends ModelNodeVariable>>();
	protected final static HashMap<Class<?>,ClsDesc> clsDescs = new HashMap<Class<?>,ClsDesc>();

	private final static String _active_sessions=new String("active_sessions");
	public static int active_sessions(){return 1;}

	private final static String _aggr_arrival=new String("aggr_arrival");
	public static int aggr_arrival(){return 2;}

	private final static String _alias_path=new String("alias_path");
	public static int alias_path(){return 3;}

	private final static String _attached_link=new String("attached_link");
	public static int attached_link(){return 4;}

	private final static String _automatic_protocol_session_creation=new String("automatic_protocol_session_creation");
	public static int automatic_protocol_session_creation(){return 5;}

	private final static String _avg_sessions=new String("avg_sessions");
	public static int avg_sessions(){return 6;}

	private final static String _avgque=new String("avgque");
	public static int avgque(){return 7;}

	private final static String _bandwidth=new String("bandwidth");
	public static int bandwidth(){return 8;}

	private final static String _bit_rate=new String("bit_rate");
	public static int bit_rate(){return 9;}

	private final static String _buffer_size=new String("buffer_size");
	public static int buffer_size(){return 10;}

	private final static String _bytesReceived=new String("bytesReceived");
	public static int bytesReceived(){return 11;}

	private final static String _bytesToSendEachInterval=new String("bytesToSendEachInterval");
	public static int bytesToSendEachInterval(){return 12;}

	private final static String _bytes_in_per_sec=new String("bytes_in_per_sec");
	public static int bytes_in_per_sec(){return 13;}

	private final static String _bytes_out_per_sec=new String("bytes_out_per_sec");
	public static int bytes_out_per_sec(){return 14;}

	private final static String _bytes_received=new String("bytes_received");
	public static int bytes_received(){return 15;}

	private final static String _bytes_sent=new String("bytes_sent");
	public static int bytes_sent(){return 16;}

	private final static String _bytes_to_send_per_interval=new String("bytes_to_send_per_interval");
	public static int bytes_to_send_per_interval(){return 17;}

	private final static String _cnf_content_ids=new String("cnf_content_ids");
	public static int cnf_content_ids(){return 18;}

	private final static String _cnf_content_sizes=new String("cnf_content_sizes");
	public static int cnf_content_sizes(){return 19;}

	private final static String _cnf_routers=new String("cnf_routers");
	public static int cnf_routers(){return 20;}

	private final static String _com_id2link_info=new String("com_id2link_info");
	public static int com_id2link_info(){return 21;}

	private final static String _community_id=new String("community_id");
	public static int community_id(){return 22;}

	private final static String _community_ids=new String("community_ids");
	public static int community_ids(){return 23;}

	private final static String _connections_per_session=new String("connections_per_session");
	public static int connections_per_session(){return 24;}

	private final static String _content_id=new String("content_id");
	public static int content_id(){return 25;}

	private final static String _controller_rid=new String("controller_rid");
	public static int controller_rid(){return 26;}

	private final static String _count=new String("count");
	public static int count(){return 27;}

	private final static String _crossing=new String("crossing");
	public static int crossing(){return 28;}

	private final static String _default_ip=new String("default_ip");
	public static int default_ip(){return 29;}

	private final static String _delay=new String("delay");
	public static int delay(){return 30;}

	private final static String _drop_probability=new String("drop_probability");
	public static int drop_probability(){return 31;}

	private final static String _dstPort=new String("dstPort");
	public static int dstPort(){return 32;}

	private final static String _dst_ips=new String("dst_ips");
	public static int dst_ips(){return 33;}

	private final static String _dst_port=new String("dst_port");
	public static int dst_port(){return 34;}

	private final static String _dsts=new String("dsts");
	public static int dsts(){return 35;}

	private final static String _edge_ifaces=new String("edge_ifaces");
	public static int edge_ifaces(){return 36;}

	private final static String _emu_protocol=new String("emu_protocol");
	public static int emu_protocol(){return 37;}

	private final static String _file_pareto=new String("file_pareto");
	public static int file_pareto(){return 38;}

	private final static String _file_size=new String("file_size");
	public static int file_size(){return 39;}

	private final static String _fluid_weight=new String("fluid_weight");
	public static int fluid_weight(){return 40;}

	private final static String _host_seed=new String("host_seed");
	public static int host_seed(){return 41;}

	private final static String _hurst=new String("hurst");
	public static int hurst(){return 42;}

	private final static String _hybrid_traffic_flows=new String("hybrid_traffic_flows");
	public static int hybrid_traffic_flows(){return 43;}

	private final static String _initialized=new String("initialized");
	public static int initialized(){return 44;}

	private final static String _interdrop=new String("interdrop");
	public static int interdrop(){return 45;}

	private final static String _interval=new String("interval");
	public static int interval(){return 46;}

	private final static String _interval_exponential=new String("interval_exponential");
	public static int interval_exponential(){return 47;}

	private final static String _ip2iface=new String("ip2iface");
	public static int ip2iface(){return 48;}

	private final static String _ip_address=new String("ip_address");
	public static int ip_address(){return 49;}

	private final static String _ip_forwarding=new String("ip_forwarding");
	public static int ip_forwarding(){return 50;}

	private final static String _ip_prefix=new String("ip_prefix");
	public static int ip_prefix(){return 51;}

	private final static String _ip_prefix_len=new String("ip_prefix_len");
	public static int ip_prefix_len(){return 52;}

	private final static String _ip_sess=new String("ip_sess");
	public static int ip_sess(){return 53;}

	private final static String _is_controller=new String("is_controller");
	public static int is_controller(){return 54;}

	private final static String _is_on=new String("is_on");
	public static int is_on(){return 55;}

	private final static String _is_red=new String("is_red");
	public static int is_red(){return 56;}

	private final static String _iss=new String("iss");
	public static int iss(){return 57;}

	private final static String _jitter_range=new String("jitter_range");
	public static int jitter_range(){return 58;}

	private final static String _last_send_time=new String("last_send_time");
	public static int last_send_time(){return 59;}

	private final static String _last_update_time=new String("last_update_time");
	public static int last_update_time(){return 60;}

	private final static String _last_xmit_time=new String("last_xmit_time");
	public static int last_xmit_time(){return 61;}

	private final static String _latency=new String("latency");
	public static int latency(){return 62;}

	private final static String _listeningPort=new String("listeningPort");
	public static int listeningPort(){return 63;}

	private final static String _listening_port=new String("listening_port");
	public static int listening_port(){return 64;}

	private final static String _local_dst_cache_size=new String("local_dst_cache_size");
	public static int local_dst_cache_size(){return 65;}

	private final static String _loss=new String("loss");
	public static int loss(){return 66;}

	private final static String _mac2iface=new String("mac2iface");
	public static int mac2iface(){return 67;}

	private final static String _mac_address=new String("mac_address");
	public static int mac_address(){return 68;}

	private final static String _mapping=new String("mapping");
	public static int mapping(){return 69;}

	private final static String _max=new String("max");
	public static int max(){return 70;}

	private final static String _max_datagram_size=new String("max_datagram_size");
	public static int max_datagram_size(){return 71;}

	private final static String _max_queue_delay=new String("max_queue_delay");
	public static int max_queue_delay(){return 72;}

	private final static String _md=new String("md");
	public static int md(){return 73;}

	private final static String _mean_pktsiz=new String("mean_pktsiz");
	public static int mean_pktsiz(){return 74;}

	private final static String _min=new String("min");
	public static int min(){return 75;}

	private final static String _monfn=new String("monfn");
	public static int monfn(){return 76;}

	private final static String _monres=new String("monres");
	public static int monres(){return 77;}

	private final static String _mss=new String("mss");
	public static int mss(){return 78;}

	private final static String _mtu=new String("mtu");
	public static int mtu(){return 79;}

	private final static String _my_edge_ifaces=new String("my_edge_ifaces");
	public static int my_edge_ifaces(){return 80;}

	private final static String _name=new String("name");
	public static int name(){return 81;}

	private final static String _networks=new String("networks");
	public static int networks(){return 82;}

	private final static String _nflows=new String("nflows");
	public static int nflows(){return 83;}

	private final static String _nix_vec_cache_size=new String("nix_vec_cache_size");
	public static int nix_vec_cache_size(){return 84;}

	private final static String _num_in_bytes=new String("num_in_bytes");
	public static int num_in_bytes(){return 85;}

	private final static String _num_in_packets=new String("num_in_packets");
	public static int num_in_packets(){return 86;}

	private final static String _num_in_ucast_bytes=new String("num_in_ucast_bytes");
	public static int num_in_ucast_bytes(){return 87;}

	private final static String _num_in_ucast_packets=new String("num_in_ucast_packets");
	public static int num_in_ucast_packets(){return 88;}

	private final static String _num_out_bytes=new String("num_out_bytes");
	public static int num_out_bytes(){return 89;}

	private final static String _num_out_packets=new String("num_out_packets");
	public static int num_out_packets(){return 90;}

	private final static String _num_out_ucast_bytes=new String("num_out_ucast_bytes");
	public static int num_out_ucast_bytes(){return 91;}

	private final static String _num_out_ucast_packets=new String("num_out_ucast_packets");
	public static int num_out_ucast_packets(){return 92;}

	private final static String _number_of_contents=new String("number_of_contents");
	public static int number_of_contents(){return 93;}

	private final static String _number_of_requests=new String("number_of_requests");
	public static int number_of_requests(){return 94;}

	private final static String _number_of_sessions=new String("number_of_sessions");
	public static int number_of_sessions(){return 95;}

	private final static String _off_time=new String("off_time");
	public static int off_time(){return 96;}

	private final static String _offset=new String("offset");
	public static int offset(){return 97;}

	private final static String _packets_in_per_sec=new String("packets_in_per_sec");
	public static int packets_in_per_sec(){return 98;}

	private final static String _packets_out_per_sec=new String("packets_out_per_sec");
	public static int packets_out_per_sec(){return 99;}

	private final static String _payload_size=new String("payload_size");
	public static int payload_size(){return 100;}

	private final static String _period=new String("period");
	public static int period(){return 101;}

	private final static String _pkt_arrival=new String("pkt_arrival");
	public static int pkt_arrival(){return 102;}

	private final static String _pktsiz=new String("pktsiz");
	public static int pktsiz(){return 103;}

	private final static String _pmax=new String("pmax");
	public static int pmax(){return 104;}

	private final static String _pname_map=new String("pname_map");
	public static int pname_map(){return 105;}

	private final static String _pno_map=new String("pno_map");
	public static int pno_map(){return 106;}

	private final static String _portal_rules=new String("portal_rules");
	public static int portal_rules(){return 107;}

	private final static String _portals=new String("portals");
	public static int portals(){return 108;}

	private final static String _prop_delay=new String("prop_delay");
	public static int prop_delay(){return 109;}

	private final static String _protocol_list=new String("protocol_list");
	public static int protocol_list(){return 110;}

	private final static String _protocol_type=new String("protocol_type");
	public static int protocol_type(){return 111;}

	private final static String _qcap=new String("qcap");
	public static int qcap(){return 112;}

	private final static String _qmax=new String("qmax");
	public static int qmax(){return 113;}

	private final static String _qmin=new String("qmin");
	public static int qmin(){return 114;}

	private final static String _queue=new String("queue");
	public static int queue(){return 115;}

	private final static String _queue_delay=new String("queue_delay");
	public static int queue_delay(){return 116;}

	private final static String _queue_size=new String("queue_size");
	public static int queue_size(){return 117;}

	private final static String _queue_type=new String("queue_type");
	public static int queue_type(){return 118;}

	private final static String _rcvWndSize=new String("rcvWndSize");
	public static int rcvWndSize(){return 119;}

	private final static String _real_uid=new String("real_uid");
	public static int real_uid(){return 120;}

	private final static String _requestsReceived=new String("requestsReceived");
	public static int requestsReceived(){return 121;}

	private final static String _requests_received=new String("requests_received");
	public static int requests_received(){return 122;}

	private final static String _rng=new String("rng");
	public static int rng(){return 123;}

	private final static String _rng_seed=new String("rng_seed");
	public static int rng_seed(){return 124;}

	private final static String _routemap=new String("routemap");
	public static int routemap(){return 125;}

	private final static String _rre_timeout=new String("rre_timeout");
	public static int rre_timeout(){return 126;}

	private final static String _rtt=new String("rtt");
	public static int rtt(){return 127;}

	private final static String _sample_count=new String("sample_count");
	public static int sample_count(){return 128;}

	private final static String _samplingInterval=new String("samplingInterval");
	public static int samplingInterval(){return 129;}

	private final static String _sendrate=new String("sendrate");
	public static int sendrate(){return 130;}

	private final static String _session_timeout=new String("session_timeout");
	public static int session_timeout(){return 131;}

	private final static String _shadow_avgque=new String("shadow_avgque");
	public static int shadow_avgque(){return 132;}

	private final static String _shadow_loss=new String("shadow_loss");
	public static int shadow_loss(){return 133;}

	private final static String _shadow_queue=new String("shadow_queue");
	public static int shadow_queue(){return 134;}

	private final static String _shortcut_to=new String("shortcut_to");
	public static int shortcut_to(){return 135;}

	private final static String _size=new String("size");
	public static int size(){return 136;}

	private final static String _sndBufSize=new String("sndBufSize");
	public static int sndBufSize(){return 137;}

	private final static String _sndWndSize=new String("sndWndSize");
	public static int sndWndSize(){return 138;}

	private final static String _srcs=new String("srcs");
	public static int srcs(){return 139;}

	private final static String _start=new String("start");
	public static int start(){return 140;}

	private final static String _start_time=new String("start_time");
	public static int start_time(){return 141;}

	private final static String _stop=new String("stop");
	public static int stop(){return 142;}

	private final static String _stretch=new String("stretch");
	public static int stretch(){return 143;}

	private final static String _sub_edge_iface_map=new String("sub_edge_iface_map");
	public static int sub_edge_iface_map(){return 144;}

	private final static String _sub_edge_ifaces=new String("sub_edge_ifaces");
	public static int sub_edge_ifaces(){return 145;}

	private final static String _sum=new String("sum");
	public static int sum(){return 146;}

	private final static String _tcpCA=new String("tcpCA");
	public static int tcpCA(){return 147;}

	private final static String _time_offset=new String("time_offset");
	public static int time_offset(){return 148;}

	private final static String _time_skew=new String("time_skew");
	public static int time_skew(){return 149;}

	private final static String _tmr_handle=new String("tmr_handle");
	public static int tmr_handle(){return 150;}

	private final static String _tmr_map=new String("tmr_map");
	public static int tmr_map(){return 151;}

	private final static String _tmr_queue=new String("tmr_queue");
	public static int tmr_queue(){return 152;}

	private final static String _toEmulated=new String("toEmulated");
	public static int toEmulated(){return 153;}

	private final static String _to_aggregate=new String("to_aggregate");
	public static int to_aggregate(){return 154;}

	private final static String _to_emulated=new String("to_emulated");
	public static int to_emulated(){return 155;}

	private final static String _to_monitor=new String("to_monitor");
	public static int to_monitor(){return 156;}

	private final static String _total_bytes_received=new String("total_bytes_received");
	public static int total_bytes_received(){return 157;}

	private final static String _total_bytes_sent=new String("total_bytes_sent");
	public static int total_bytes_sent(){return 158;}

	private final static String _trace_description=new String("trace_description");
	public static int trace_description(){return 159;}

	private final static String _traffic_flows=new String("traffic_flows");
	public static int traffic_flows(){return 160;}

	private final static String _traffic_intensity=new String("traffic_intensity");
	public static int traffic_intensity(){return 161;}

	private final static String _traffic_type_seed=new String("traffic_type_seed");
	public static int traffic_type_seed(){return 162;}

	private final static String _tx_queue=new String("tx_queue");
	public static int tx_queue(){return 163;}

	private final static String _tx_timer_queue=new String("tx_timer_queue");
	public static int tx_timer_queue(){return 164;}

	private final static String _uid=new String("uid");
	public static int uid(){return 165;}

	private final static String _vacate_time=new String("vacate_time");
	public static int vacate_time(){return 166;}

	private final static String _var_id=new String("var_id");
	public static int var_id(){return 167;}

	private final static String _version=new String("version");
	public static int version(){return 168;}

	private final static String _wait_opt=new String("wait_opt");
	public static int wait_opt(){return 169;}

	private final static String _weight=new String("weight");
	public static int weight(){return 170;}

	private final static String _window=new String("window");
	public static int window(){return 171;}

	private final static String _wndmax=new String("wndmax");
	public static int wndmax(){return 172;}
	static {


		//*************************
		//generate jython map valus for attribute accessors
		//*************************


		//*************************
		str2id.put(_active_sessions, active_sessions());
		varTypes.put(active_sessions(), INT);

		if(!jythonMap.containsKey(_active_sessions))jythonMap.put(_active_sessions, new ArrayList<Tripple>());
		jythonMap.get(_active_sessions).add(new Tripple(active_sessions(),"setActiveSessions",String.class));

		//*************************
		str2id.put(_aggr_arrival, aggr_arrival());
		varTypes.put(aggr_arrival(), FLOAT);

		if(!jythonMap.containsKey(_aggr_arrival))jythonMap.put(_aggr_arrival, new ArrayList<Tripple>());
		jythonMap.get(_aggr_arrival).add(new Tripple(aggr_arrival(),"setAggrArrival",String.class));

		//*************************
		str2id.put(_alias_path, alias_path());
		varTypes.put(alias_path(), OBJECT);

		if(!jythonMap.containsKey(_alias_path))jythonMap.put(_alias_path, new ArrayList<Tripple>());
		jythonMap.get(_alias_path).add(new Tripple(alias_path(),"setAliasPath",String.class));

		//*************************
		str2id.put(_attached_link, attached_link());
		varTypes.put(attached_link(), OBJECT);

		if(!jythonMap.containsKey(_attached_link))jythonMap.put(_attached_link, new ArrayList<Tripple>());
		jythonMap.get(_attached_link).add(new Tripple(attached_link(),"setAttachedLink",String.class));

		//*************************
		str2id.put(_automatic_protocol_session_creation, automatic_protocol_session_creation());
		varTypes.put(automatic_protocol_session_creation(), BOOL);

		if(!jythonMap.containsKey(_automatic_protocol_session_creation))jythonMap.put(_automatic_protocol_session_creation, new ArrayList<Tripple>());
		jythonMap.get(_automatic_protocol_session_creation).add(new Tripple(automatic_protocol_session_creation(),"setAutomaticProtocolSessionCreation",String.class));

		//*************************
		str2id.put(_avg_sessions, avg_sessions());
		varTypes.put(avg_sessions(), INT);

		if(!jythonMap.containsKey(_avg_sessions))jythonMap.put(_avg_sessions, new ArrayList<Tripple>());
		jythonMap.get(_avg_sessions).add(new Tripple(avg_sessions(),"setAvgSessions",String.class));

		//*************************
		str2id.put(_avgque, avgque());
		varTypes.put(avgque(), FLOAT);

		if(!jythonMap.containsKey(_avgque))jythonMap.put(_avgque, new ArrayList<Tripple>());
		jythonMap.get(_avgque).add(new Tripple(avgque(),"setAvgque",String.class));

		//*************************
		str2id.put(_bandwidth, bandwidth());
		varTypes.put(bandwidth(), FLOAT);

		if(!jythonMap.containsKey(_bandwidth))jythonMap.put(_bandwidth, new ArrayList<Tripple>());
		jythonMap.get(_bandwidth).add(new Tripple(bandwidth(),"setBandwidth",String.class));

		//*************************
		str2id.put(_bit_rate, bit_rate());
		varTypes.put(bit_rate(), FLOAT);

		if(!jythonMap.containsKey(_bit_rate))jythonMap.put(_bit_rate, new ArrayList<Tripple>());
		jythonMap.get(_bit_rate).add(new Tripple(bit_rate(),"setBitRate",String.class));

		//*************************
		str2id.put(_buffer_size, buffer_size());
		varTypes.put(buffer_size(), INT);

		if(!jythonMap.containsKey(_buffer_size))jythonMap.put(_buffer_size, new ArrayList<Tripple>());
		jythonMap.get(_buffer_size).add(new Tripple(buffer_size(),"setBufferSize",String.class));

		//*************************
		str2id.put(_bytesReceived, bytesReceived());
		varTypes.put(bytesReceived(), INT);

		if(!jythonMap.containsKey(_bytesReceived))jythonMap.put(_bytesReceived, new ArrayList<Tripple>());
		jythonMap.get(_bytesReceived).add(new Tripple(bytesReceived(),"setBytesReceived",String.class));

		//*************************
		str2id.put(_bytesToSendEachInterval, bytesToSendEachInterval());
		varTypes.put(bytesToSendEachInterval(), INT);

		if(!jythonMap.containsKey(_bytesToSendEachInterval))jythonMap.put(_bytesToSendEachInterval, new ArrayList<Tripple>());
		jythonMap.get(_bytesToSendEachInterval).add(new Tripple(bytesToSendEachInterval(),"setBytesToSendEachInterval",String.class));

		//*************************
		str2id.put(_bytes_in_per_sec, bytes_in_per_sec());
		varTypes.put(bytes_in_per_sec(), FLOAT);

		if(!jythonMap.containsKey(_bytes_in_per_sec))jythonMap.put(_bytes_in_per_sec, new ArrayList<Tripple>());
		jythonMap.get(_bytes_in_per_sec).add(new Tripple(bytes_in_per_sec(),"setBytesInPerSec",String.class));

		//*************************
		str2id.put(_bytes_out_per_sec, bytes_out_per_sec());
		varTypes.put(bytes_out_per_sec(), FLOAT);

		if(!jythonMap.containsKey(_bytes_out_per_sec))jythonMap.put(_bytes_out_per_sec, new ArrayList<Tripple>());
		jythonMap.get(_bytes_out_per_sec).add(new Tripple(bytes_out_per_sec(),"setBytesOutPerSec",String.class));

		//*************************
		str2id.put(_bytes_received, bytes_received());
		varTypes.put(bytes_received(), INT);

		if(!jythonMap.containsKey(_bytes_received))jythonMap.put(_bytes_received, new ArrayList<Tripple>());
		jythonMap.get(_bytes_received).add(new Tripple(bytes_received(),"setBytesReceived",String.class));

		//*************************
		str2id.put(_bytes_sent, bytes_sent());
		varTypes.put(bytes_sent(), INT);

		if(!jythonMap.containsKey(_bytes_sent))jythonMap.put(_bytes_sent, new ArrayList<Tripple>());
		jythonMap.get(_bytes_sent).add(new Tripple(bytes_sent(),"setBytesSent",String.class));

		//*************************
		str2id.put(_bytes_to_send_per_interval, bytes_to_send_per_interval());
		varTypes.put(bytes_to_send_per_interval(), INT);

		if(!jythonMap.containsKey(_bytes_to_send_per_interval))jythonMap.put(_bytes_to_send_per_interval, new ArrayList<Tripple>());
		jythonMap.get(_bytes_to_send_per_interval).add(new Tripple(bytes_to_send_per_interval(),"setBytesToSendPerInterval",String.class));

		//*************************
		str2id.put(_cnf_content_ids, cnf_content_ids());
		varTypes.put(cnf_content_ids(), OBJECT);

		if(!jythonMap.containsKey(_cnf_content_ids))jythonMap.put(_cnf_content_ids, new ArrayList<Tripple>());
		jythonMap.get(_cnf_content_ids).add(new Tripple(cnf_content_ids(),"setCnfContentIds",String.class));

		//*************************
		str2id.put(_cnf_content_sizes, cnf_content_sizes());
		varTypes.put(cnf_content_sizes(), OBJECT);

		if(!jythonMap.containsKey(_cnf_content_sizes))jythonMap.put(_cnf_content_sizes, new ArrayList<Tripple>());
		jythonMap.get(_cnf_content_sizes).add(new Tripple(cnf_content_sizes(),"setCnfContentSizes",String.class));

		//*************************
		str2id.put(_cnf_routers, cnf_routers());
		varTypes.put(cnf_routers(), OBJECT);

		if(!jythonMap.containsKey(_cnf_routers))jythonMap.put(_cnf_routers, new ArrayList<Tripple>());
		jythonMap.get(_cnf_routers).add(new Tripple(cnf_routers(),"setCnfRouters",String.class));

		//*************************
		str2id.put(_com_id2link_info, com_id2link_info());
		varTypes.put(com_id2link_info(), OBJECT);

		if(!jythonMap.containsKey(_com_id2link_info))jythonMap.put(_com_id2link_info, new ArrayList<Tripple>());
		jythonMap.get(_com_id2link_info).add(new Tripple(com_id2link_info(),"setComId2linkInfo",String.class));

		//*************************
		str2id.put(_community_id, community_id());
		varTypes.put(community_id(), INT);

		if(!jythonMap.containsKey(_community_id))jythonMap.put(_community_id, new ArrayList<Tripple>());
		jythonMap.get(_community_id).add(new Tripple(community_id(),"setCommunityId",String.class));

		//*************************
		str2id.put(_community_ids, community_ids());
		varTypes.put(community_ids(), OBJECT);

		if(!jythonMap.containsKey(_community_ids))jythonMap.put(_community_ids, new ArrayList<Tripple>());
		jythonMap.get(_community_ids).add(new Tripple(community_ids(),"setCommunityIds",String.class));

		//*************************
		str2id.put(_connections_per_session, connections_per_session());
		varTypes.put(connections_per_session(), INT);

		if(!jythonMap.containsKey(_connections_per_session))jythonMap.put(_connections_per_session, new ArrayList<Tripple>());
		jythonMap.get(_connections_per_session).add(new Tripple(connections_per_session(),"setConnectionsPerSession",String.class));

		//*************************
		str2id.put(_content_id, content_id());
		varTypes.put(content_id(), INT);

		if(!jythonMap.containsKey(_content_id))jythonMap.put(_content_id, new ArrayList<Tripple>());
		jythonMap.get(_content_id).add(new Tripple(content_id(),"setContentId",String.class));

		//*************************
		str2id.put(_controller_rid, controller_rid());
		varTypes.put(controller_rid(), INT);

		if(!jythonMap.containsKey(_controller_rid))jythonMap.put(_controller_rid, new ArrayList<Tripple>());
		jythonMap.get(_controller_rid).add(new Tripple(controller_rid(),"setControllerRid",String.class));

		//*************************
		str2id.put(_count, count());
		varTypes.put(count(), INT);

		if(!jythonMap.containsKey(_count))jythonMap.put(_count, new ArrayList<Tripple>());
		jythonMap.get(_count).add(new Tripple(count(),"setCount",String.class));

		//*************************
		str2id.put(_crossing, crossing());
		varTypes.put(crossing(), OBJECT);

		if(!jythonMap.containsKey(_crossing))jythonMap.put(_crossing, new ArrayList<Tripple>());
		jythonMap.get(_crossing).add(new Tripple(crossing(),"setCrossing",String.class));

		//*************************
		str2id.put(_default_ip, default_ip());
		varTypes.put(default_ip(), OBJECT);

		if(!jythonMap.containsKey(_default_ip))jythonMap.put(_default_ip, new ArrayList<Tripple>());
		jythonMap.get(_default_ip).add(new Tripple(default_ip(),"setDefaultIp",String.class));

		//*************************
		str2id.put(_delay, delay());
		varTypes.put(delay(), FLOAT);

		if(!jythonMap.containsKey(_delay))jythonMap.put(_delay, new ArrayList<Tripple>());
		jythonMap.get(_delay).add(new Tripple(delay(),"setDelay",String.class));

		//*************************
		str2id.put(_drop_probability, drop_probability());
		varTypes.put(drop_probability(), FLOAT);

		if(!jythonMap.containsKey(_drop_probability))jythonMap.put(_drop_probability, new ArrayList<Tripple>());
		jythonMap.get(_drop_probability).add(new Tripple(drop_probability(),"setDropProbability",String.class));

		//*************************
		str2id.put(_dstPort, dstPort());
		varTypes.put(dstPort(), INT);

		if(!jythonMap.containsKey(_dstPort))jythonMap.put(_dstPort, new ArrayList<Tripple>());
		jythonMap.get(_dstPort).add(new Tripple(dstPort(),"setDstPort",String.class));

		//*************************
		str2id.put(_dst_ips, dst_ips());
		varTypes.put(dst_ips(), OBJECT);

		if(!jythonMap.containsKey(_dst_ips))jythonMap.put(_dst_ips, new ArrayList<Tripple>());
		jythonMap.get(_dst_ips).add(new Tripple(dst_ips(),"setDstIps",String.class));

		//*************************
		str2id.put(_dst_port, dst_port());
		varTypes.put(dst_port(), INT);

		if(!jythonMap.containsKey(_dst_port))jythonMap.put(_dst_port, new ArrayList<Tripple>());
		jythonMap.get(_dst_port).add(new Tripple(dst_port(),"setDstPort",String.class));

		//*************************
		str2id.put(_dsts, dsts());
		varTypes.put(dsts(), RESOURCE_ID);

		if(!jythonMap.containsKey(_dsts))jythonMap.put(_dsts, new ArrayList<Tripple>());
		jythonMap.get(_dsts).add(new Tripple(dsts(),"setDsts",String.class));

		//*************************
		str2id.put(_edge_ifaces, edge_ifaces());
		varTypes.put(edge_ifaces(), OBJECT);

		if(!jythonMap.containsKey(_edge_ifaces))jythonMap.put(_edge_ifaces, new ArrayList<Tripple>());
		jythonMap.get(_edge_ifaces).add(new Tripple(edge_ifaces(),"setEdgeIfaces",String.class));

		//*************************
		str2id.put(_emu_protocol, emu_protocol());
		varTypes.put(emu_protocol(), OBJECT);

		if(!jythonMap.containsKey(_emu_protocol))jythonMap.put(_emu_protocol, new ArrayList<Tripple>());
		jythonMap.get(_emu_protocol).add(new Tripple(emu_protocol(),"setEmuProtocol",String.class));

		//*************************
		str2id.put(_file_pareto, file_pareto());
		varTypes.put(file_pareto(), BOOL);

		if(!jythonMap.containsKey(_file_pareto))jythonMap.put(_file_pareto, new ArrayList<Tripple>());
		jythonMap.get(_file_pareto).add(new Tripple(file_pareto(),"setFilePareto",String.class));

		//*************************
		str2id.put(_file_size, file_size());
		varTypes.put(file_size(), INT);

		if(!jythonMap.containsKey(_file_size))jythonMap.put(_file_size, new ArrayList<Tripple>());
		jythonMap.get(_file_size).add(new Tripple(file_size(),"setFileSize",String.class));

		//*************************
		str2id.put(_fluid_weight, fluid_weight());
		varTypes.put(fluid_weight(), FLOAT);

		if(!jythonMap.containsKey(_fluid_weight))jythonMap.put(_fluid_weight, new ArrayList<Tripple>());
		jythonMap.get(_fluid_weight).add(new Tripple(fluid_weight(),"setFluidWeight",String.class));

		//*************************
		str2id.put(_host_seed, host_seed());
		varTypes.put(host_seed(), INT);

		if(!jythonMap.containsKey(_host_seed))jythonMap.put(_host_seed, new ArrayList<Tripple>());
		jythonMap.get(_host_seed).add(new Tripple(host_seed(),"setHostSeed",String.class));

		//*************************
		str2id.put(_hurst, hurst());
		varTypes.put(hurst(), FLOAT);

		if(!jythonMap.containsKey(_hurst))jythonMap.put(_hurst, new ArrayList<Tripple>());
		jythonMap.get(_hurst).add(new Tripple(hurst(),"setHurst",String.class));

		//*************************
		str2id.put(_hybrid_traffic_flows, hybrid_traffic_flows());
		varTypes.put(hybrid_traffic_flows(), OBJECT);

		if(!jythonMap.containsKey(_hybrid_traffic_flows))jythonMap.put(_hybrid_traffic_flows, new ArrayList<Tripple>());
		jythonMap.get(_hybrid_traffic_flows).add(new Tripple(hybrid_traffic_flows(),"setHybridTrafficFlows",String.class));

		//*************************
		str2id.put(_initialized, initialized());
		varTypes.put(initialized(), OBJECT);

		if(!jythonMap.containsKey(_initialized))jythonMap.put(_initialized, new ArrayList<Tripple>());
		jythonMap.get(_initialized).add(new Tripple(initialized(),"setInitialized",String.class));

		//*************************
		str2id.put(_interdrop, interdrop());
		varTypes.put(interdrop(), FLOAT);

		if(!jythonMap.containsKey(_interdrop))jythonMap.put(_interdrop, new ArrayList<Tripple>());
		jythonMap.get(_interdrop).add(new Tripple(interdrop(),"setInterdrop",String.class));

		//*************************
		str2id.put(_interval, interval());
		varTypes.put(interval(), FLOAT);

		if(!jythonMap.containsKey(_interval))jythonMap.put(_interval, new ArrayList<Tripple>());
		jythonMap.get(_interval).add(new Tripple(interval(),"setInterval",String.class));

		//*************************
		str2id.put(_interval_exponential, interval_exponential());
		varTypes.put(interval_exponential(), BOOL);

		if(!jythonMap.containsKey(_interval_exponential))jythonMap.put(_interval_exponential, new ArrayList<Tripple>());
		jythonMap.get(_interval_exponential).add(new Tripple(interval_exponential(),"setIntervalExponential",String.class));

		//*************************
		str2id.put(_ip2iface, ip2iface());
		varTypes.put(ip2iface(), OBJECT);

		if(!jythonMap.containsKey(_ip2iface))jythonMap.put(_ip2iface, new ArrayList<Tripple>());
		jythonMap.get(_ip2iface).add(new Tripple(ip2iface(),"setIp2iface",String.class));

		//*************************
		str2id.put(_ip_address, ip_address());
		varTypes.put(ip_address(), OBJECT);

		if(!jythonMap.containsKey(_ip_address))jythonMap.put(_ip_address, new ArrayList<Tripple>());
		jythonMap.get(_ip_address).add(new Tripple(ip_address(),"setIpAddress",String.class));

		//*************************
		str2id.put(_ip_forwarding, ip_forwarding());
		varTypes.put(ip_forwarding(), BOOL);

		if(!jythonMap.containsKey(_ip_forwarding))jythonMap.put(_ip_forwarding, new ArrayList<Tripple>());
		jythonMap.get(_ip_forwarding).add(new Tripple(ip_forwarding(),"setIpForwarding",String.class));

		//*************************
		str2id.put(_ip_prefix, ip_prefix());
		varTypes.put(ip_prefix(), OBJECT);

		if(!jythonMap.containsKey(_ip_prefix))jythonMap.put(_ip_prefix, new ArrayList<Tripple>());
		jythonMap.get(_ip_prefix).add(new Tripple(ip_prefix(),"setIpPrefix",String.class));

		//*************************
		str2id.put(_ip_prefix_len, ip_prefix_len());
		varTypes.put(ip_prefix_len(), INT);

		if(!jythonMap.containsKey(_ip_prefix_len))jythonMap.put(_ip_prefix_len, new ArrayList<Tripple>());
		jythonMap.get(_ip_prefix_len).add(new Tripple(ip_prefix_len(),"setIpPrefixLen",String.class));

		//*************************
		str2id.put(_ip_sess, ip_sess());
		varTypes.put(ip_sess(), OBJECT);

		if(!jythonMap.containsKey(_ip_sess))jythonMap.put(_ip_sess, new ArrayList<Tripple>());
		jythonMap.get(_ip_sess).add(new Tripple(ip_sess(),"setIpSess",String.class));

		//*************************
		str2id.put(_is_controller, is_controller());
		varTypes.put(is_controller(), BOOL);

		if(!jythonMap.containsKey(_is_controller))jythonMap.put(_is_controller, new ArrayList<Tripple>());
		jythonMap.get(_is_controller).add(new Tripple(is_controller(),"setIsController",String.class));

		//*************************
		str2id.put(_is_on, is_on());
		varTypes.put(is_on(), BOOL);

		if(!jythonMap.containsKey(_is_on))jythonMap.put(_is_on, new ArrayList<Tripple>());
		jythonMap.get(_is_on).add(new Tripple(is_on(),"setIsOn",String.class));

		//*************************
		str2id.put(_is_red, is_red());
		varTypes.put(is_red(), BOOL);

		if(!jythonMap.containsKey(_is_red))jythonMap.put(_is_red, new ArrayList<Tripple>());
		jythonMap.get(_is_red).add(new Tripple(is_red(),"setIsRed",String.class));

		//*************************
		str2id.put(_iss, iss());
		varTypes.put(iss(), INT);

		if(!jythonMap.containsKey(_iss))jythonMap.put(_iss, new ArrayList<Tripple>());
		jythonMap.get(_iss).add(new Tripple(iss(),"setIss",String.class));

		//*************************
		str2id.put(_jitter_range, jitter_range());
		varTypes.put(jitter_range(), FLOAT);

		if(!jythonMap.containsKey(_jitter_range))jythonMap.put(_jitter_range, new ArrayList<Tripple>());
		jythonMap.get(_jitter_range).add(new Tripple(jitter_range(),"setJitterRange",String.class));

		//*************************
		str2id.put(_last_send_time, last_send_time());
		varTypes.put(last_send_time(), OBJECT);

		if(!jythonMap.containsKey(_last_send_time))jythonMap.put(_last_send_time, new ArrayList<Tripple>());
		jythonMap.get(_last_send_time).add(new Tripple(last_send_time(),"setLastSendTime",String.class));

		//*************************
		str2id.put(_last_update_time, last_update_time());
		varTypes.put(last_update_time(), OBJECT);

		if(!jythonMap.containsKey(_last_update_time))jythonMap.put(_last_update_time, new ArrayList<Tripple>());
		jythonMap.get(_last_update_time).add(new Tripple(last_update_time(),"setLastUpdateTime",String.class));

		//*************************
		str2id.put(_last_xmit_time, last_xmit_time());
		varTypes.put(last_xmit_time(), OBJECT);

		if(!jythonMap.containsKey(_last_xmit_time))jythonMap.put(_last_xmit_time, new ArrayList<Tripple>());
		jythonMap.get(_last_xmit_time).add(new Tripple(last_xmit_time(),"setLastXmitTime",String.class));

		//*************************
		str2id.put(_latency, latency());
		varTypes.put(latency(), FLOAT);

		if(!jythonMap.containsKey(_latency))jythonMap.put(_latency, new ArrayList<Tripple>());
		jythonMap.get(_latency).add(new Tripple(latency(),"setLatency",String.class));

		//*************************
		str2id.put(_listeningPort, listeningPort());
		varTypes.put(listeningPort(), INT);

		if(!jythonMap.containsKey(_listeningPort))jythonMap.put(_listeningPort, new ArrayList<Tripple>());
		jythonMap.get(_listeningPort).add(new Tripple(listeningPort(),"setListeningPort",String.class));

		//*************************
		str2id.put(_listening_port, listening_port());
		varTypes.put(listening_port(), INT);

		if(!jythonMap.containsKey(_listening_port))jythonMap.put(_listening_port, new ArrayList<Tripple>());
		jythonMap.get(_listening_port).add(new Tripple(listening_port(),"setListeningPort",String.class));

		//*************************
		str2id.put(_local_dst_cache_size, local_dst_cache_size());
		varTypes.put(local_dst_cache_size(), INT);

		if(!jythonMap.containsKey(_local_dst_cache_size))jythonMap.put(_local_dst_cache_size, new ArrayList<Tripple>());
		jythonMap.get(_local_dst_cache_size).add(new Tripple(local_dst_cache_size(),"setLocalDstCacheSize",String.class));

		//*************************
		str2id.put(_loss, loss());
		varTypes.put(loss(), FLOAT);

		if(!jythonMap.containsKey(_loss))jythonMap.put(_loss, new ArrayList<Tripple>());
		jythonMap.get(_loss).add(new Tripple(loss(),"setLoss",String.class));

		//*************************
		str2id.put(_mac2iface, mac2iface());
		varTypes.put(mac2iface(), OBJECT);

		if(!jythonMap.containsKey(_mac2iface))jythonMap.put(_mac2iface, new ArrayList<Tripple>());
		jythonMap.get(_mac2iface).add(new Tripple(mac2iface(),"setMac2iface",String.class));

		//*************************
		str2id.put(_mac_address, mac_address());
		varTypes.put(mac_address(), OBJECT);

		if(!jythonMap.containsKey(_mac_address))jythonMap.put(_mac_address, new ArrayList<Tripple>());
		jythonMap.get(_mac_address).add(new Tripple(mac_address(),"setMacAddress",String.class));

		//*************************
		str2id.put(_mapping, mapping());
		varTypes.put(mapping(), INT);

		if(!jythonMap.containsKey(_mapping))jythonMap.put(_mapping, new ArrayList<Tripple>());
		jythonMap.get(_mapping).add(new Tripple(mapping(),"setMapping",String.class));

		//*************************
		str2id.put(_max, max());
		varTypes.put(max(), FLOAT);

		if(!jythonMap.containsKey(_max))jythonMap.put(_max, new ArrayList<Tripple>());
		jythonMap.get(_max).add(new Tripple(max(),"setMax",String.class));

		//*************************
		str2id.put(_max_datagram_size, max_datagram_size());
		varTypes.put(max_datagram_size(), INT);

		if(!jythonMap.containsKey(_max_datagram_size))jythonMap.put(_max_datagram_size, new ArrayList<Tripple>());
		jythonMap.get(_max_datagram_size).add(new Tripple(max_datagram_size(),"setMaxDatagramSize",String.class));

		//*************************
		str2id.put(_max_queue_delay, max_queue_delay());
		varTypes.put(max_queue_delay(), OBJECT);

		if(!jythonMap.containsKey(_max_queue_delay))jythonMap.put(_max_queue_delay, new ArrayList<Tripple>());
		jythonMap.get(_max_queue_delay).add(new Tripple(max_queue_delay(),"setMaxQueueDelay",String.class));

		//*************************
		str2id.put(_md, md());
		varTypes.put(md(), FLOAT);

		if(!jythonMap.containsKey(_md))jythonMap.put(_md, new ArrayList<Tripple>());
		jythonMap.get(_md).add(new Tripple(md(),"setMd",String.class));

		//*************************
		str2id.put(_mean_pktsiz, mean_pktsiz());
		varTypes.put(mean_pktsiz(), FLOAT);

		if(!jythonMap.containsKey(_mean_pktsiz))jythonMap.put(_mean_pktsiz, new ArrayList<Tripple>());
		jythonMap.get(_mean_pktsiz).add(new Tripple(mean_pktsiz(),"setMeanPktsiz",String.class));

		//*************************
		str2id.put(_min, min());
		varTypes.put(min(), FLOAT);

		if(!jythonMap.containsKey(_min))jythonMap.put(_min, new ArrayList<Tripple>());
		jythonMap.get(_min).add(new Tripple(min(),"setMin",String.class));

		//*************************
		str2id.put(_monfn, monfn());
		varTypes.put(monfn(), STRING);

		if(!jythonMap.containsKey(_monfn))jythonMap.put(_monfn, new ArrayList<Tripple>());
		jythonMap.get(_monfn).add(new Tripple(monfn(),"setMonfn",String.class));

		//*************************
		str2id.put(_monres, monres());
		varTypes.put(monres(), FLOAT);

		if(!jythonMap.containsKey(_monres))jythonMap.put(_monres, new ArrayList<Tripple>());
		jythonMap.get(_monres).add(new Tripple(monres(),"setMonres",String.class));

		//*************************
		str2id.put(_mss, mss());
		varTypes.put(mss(), INT);

		if(!jythonMap.containsKey(_mss))jythonMap.put(_mss, new ArrayList<Tripple>());
		jythonMap.get(_mss).add(new Tripple(mss(),"setMss",String.class));

		//*************************
		str2id.put(_mtu, mtu());
		varTypes.put(mtu(), INT);

		if(!jythonMap.containsKey(_mtu))jythonMap.put(_mtu, new ArrayList<Tripple>());
		jythonMap.get(_mtu).add(new Tripple(mtu(),"setMtu",String.class));

		//*************************
		str2id.put(_my_edge_ifaces, my_edge_ifaces());
		varTypes.put(my_edge_ifaces(), OBJECT);

		if(!jythonMap.containsKey(_my_edge_ifaces))jythonMap.put(_my_edge_ifaces, new ArrayList<Tripple>());
		jythonMap.get(_my_edge_ifaces).add(new Tripple(my_edge_ifaces(),"setMyEdgeIfaces",String.class));

		//*************************
		str2id.put(_name, name());
		varTypes.put(name(), STRING);

		if(!jythonMap.containsKey(_name))jythonMap.put(_name, new ArrayList<Tripple>());
		jythonMap.get(_name).add(new Tripple(name(),"setName",String.class));

		//*************************
		str2id.put(_networks, networks());
		varTypes.put(networks(), OBJECT);

		if(!jythonMap.containsKey(_networks))jythonMap.put(_networks, new ArrayList<Tripple>());
		jythonMap.get(_networks).add(new Tripple(networks(),"setNetworks",String.class));

		//*************************
		str2id.put(_nflows, nflows());
		varTypes.put(nflows(), INT);

		if(!jythonMap.containsKey(_nflows))jythonMap.put(_nflows, new ArrayList<Tripple>());
		jythonMap.get(_nflows).add(new Tripple(nflows(),"setNflows",String.class));

		//*************************
		str2id.put(_nix_vec_cache_size, nix_vec_cache_size());
		varTypes.put(nix_vec_cache_size(), INT);

		if(!jythonMap.containsKey(_nix_vec_cache_size))jythonMap.put(_nix_vec_cache_size, new ArrayList<Tripple>());
		jythonMap.get(_nix_vec_cache_size).add(new Tripple(nix_vec_cache_size(),"setNixVecCacheSize",String.class));

		//*************************
		str2id.put(_num_in_bytes, num_in_bytes());
		varTypes.put(num_in_bytes(), INT);

		if(!jythonMap.containsKey(_num_in_bytes))jythonMap.put(_num_in_bytes, new ArrayList<Tripple>());
		jythonMap.get(_num_in_bytes).add(new Tripple(num_in_bytes(),"setNumInBytes",String.class));

		//*************************
		str2id.put(_num_in_packets, num_in_packets());
		varTypes.put(num_in_packets(), INT);

		if(!jythonMap.containsKey(_num_in_packets))jythonMap.put(_num_in_packets, new ArrayList<Tripple>());
		jythonMap.get(_num_in_packets).add(new Tripple(num_in_packets(),"setNumInPackets",String.class));

		//*************************
		str2id.put(_num_in_ucast_bytes, num_in_ucast_bytes());
		varTypes.put(num_in_ucast_bytes(), INT);

		if(!jythonMap.containsKey(_num_in_ucast_bytes))jythonMap.put(_num_in_ucast_bytes, new ArrayList<Tripple>());
		jythonMap.get(_num_in_ucast_bytes).add(new Tripple(num_in_ucast_bytes(),"setNumInUcastBytes",String.class));

		//*************************
		str2id.put(_num_in_ucast_packets, num_in_ucast_packets());
		varTypes.put(num_in_ucast_packets(), INT);

		if(!jythonMap.containsKey(_num_in_ucast_packets))jythonMap.put(_num_in_ucast_packets, new ArrayList<Tripple>());
		jythonMap.get(_num_in_ucast_packets).add(new Tripple(num_in_ucast_packets(),"setNumInUcastPackets",String.class));

		//*************************
		str2id.put(_num_out_bytes, num_out_bytes());
		varTypes.put(num_out_bytes(), INT);

		if(!jythonMap.containsKey(_num_out_bytes))jythonMap.put(_num_out_bytes, new ArrayList<Tripple>());
		jythonMap.get(_num_out_bytes).add(new Tripple(num_out_bytes(),"setNumOutBytes",String.class));

		//*************************
		str2id.put(_num_out_packets, num_out_packets());
		varTypes.put(num_out_packets(), INT);

		if(!jythonMap.containsKey(_num_out_packets))jythonMap.put(_num_out_packets, new ArrayList<Tripple>());
		jythonMap.get(_num_out_packets).add(new Tripple(num_out_packets(),"setNumOutPackets",String.class));

		//*************************
		str2id.put(_num_out_ucast_bytes, num_out_ucast_bytes());
		varTypes.put(num_out_ucast_bytes(), INT);

		if(!jythonMap.containsKey(_num_out_ucast_bytes))jythonMap.put(_num_out_ucast_bytes, new ArrayList<Tripple>());
		jythonMap.get(_num_out_ucast_bytes).add(new Tripple(num_out_ucast_bytes(),"setNumOutUcastBytes",String.class));

		//*************************
		str2id.put(_num_out_ucast_packets, num_out_ucast_packets());
		varTypes.put(num_out_ucast_packets(), INT);

		if(!jythonMap.containsKey(_num_out_ucast_packets))jythonMap.put(_num_out_ucast_packets, new ArrayList<Tripple>());
		jythonMap.get(_num_out_ucast_packets).add(new Tripple(num_out_ucast_packets(),"setNumOutUcastPackets",String.class));

		//*************************
		str2id.put(_number_of_contents, number_of_contents());
		varTypes.put(number_of_contents(), INT);

		if(!jythonMap.containsKey(_number_of_contents))jythonMap.put(_number_of_contents, new ArrayList<Tripple>());
		jythonMap.get(_number_of_contents).add(new Tripple(number_of_contents(),"setNumberOfContents",String.class));

		//*************************
		str2id.put(_number_of_requests, number_of_requests());
		varTypes.put(number_of_requests(), INT);

		if(!jythonMap.containsKey(_number_of_requests))jythonMap.put(_number_of_requests, new ArrayList<Tripple>());
		jythonMap.get(_number_of_requests).add(new Tripple(number_of_requests(),"setNumberOfRequests",String.class));

		//*************************
		str2id.put(_number_of_sessions, number_of_sessions());
		varTypes.put(number_of_sessions(), INT);

		if(!jythonMap.containsKey(_number_of_sessions))jythonMap.put(_number_of_sessions, new ArrayList<Tripple>());
		jythonMap.get(_number_of_sessions).add(new Tripple(number_of_sessions(),"setNumberOfSessions",String.class));

		//*************************
		str2id.put(_off_time, off_time());
		varTypes.put(off_time(), FLOAT);

		if(!jythonMap.containsKey(_off_time))jythonMap.put(_off_time, new ArrayList<Tripple>());
		jythonMap.get(_off_time).add(new Tripple(off_time(),"setOffTime",String.class));

		//*************************
		str2id.put(_offset, offset());
		varTypes.put(offset(), INT);

		if(!jythonMap.containsKey(_offset))jythonMap.put(_offset, new ArrayList<Tripple>());
		jythonMap.get(_offset).add(new Tripple(offset(),"setOffset",String.class));

		//*************************
		str2id.put(_packets_in_per_sec, packets_in_per_sec());
		varTypes.put(packets_in_per_sec(), FLOAT);

		if(!jythonMap.containsKey(_packets_in_per_sec))jythonMap.put(_packets_in_per_sec, new ArrayList<Tripple>());
		jythonMap.get(_packets_in_per_sec).add(new Tripple(packets_in_per_sec(),"setPacketsInPerSec",String.class));

		//*************************
		str2id.put(_packets_out_per_sec, packets_out_per_sec());
		varTypes.put(packets_out_per_sec(), FLOAT);

		if(!jythonMap.containsKey(_packets_out_per_sec))jythonMap.put(_packets_out_per_sec, new ArrayList<Tripple>());
		jythonMap.get(_packets_out_per_sec).add(new Tripple(packets_out_per_sec(),"setPacketsOutPerSec",String.class));

		//*************************
		str2id.put(_payload_size, payload_size());
		varTypes.put(payload_size(), INT);

		if(!jythonMap.containsKey(_payload_size))jythonMap.put(_payload_size, new ArrayList<Tripple>());
		jythonMap.get(_payload_size).add(new Tripple(payload_size(),"setPayloadSize",String.class));

		//*************************
		str2id.put(_period, period());
		varTypes.put(period(), INT);

		if(!jythonMap.containsKey(_period))jythonMap.put(_period, new ArrayList<Tripple>());
		jythonMap.get(_period).add(new Tripple(period(),"setPeriod",String.class));

		//*************************
		str2id.put(_pkt_arrival, pkt_arrival());
		varTypes.put(pkt_arrival(), FLOAT);

		if(!jythonMap.containsKey(_pkt_arrival))jythonMap.put(_pkt_arrival, new ArrayList<Tripple>());
		jythonMap.get(_pkt_arrival).add(new Tripple(pkt_arrival(),"setPktArrival",String.class));

		//*************************
		str2id.put(_pktsiz, pktsiz());
		varTypes.put(pktsiz(), FLOAT);

		if(!jythonMap.containsKey(_pktsiz))jythonMap.put(_pktsiz, new ArrayList<Tripple>());
		jythonMap.get(_pktsiz).add(new Tripple(pktsiz(),"setPktsiz",String.class));

		//*************************
		str2id.put(_pmax, pmax());
		varTypes.put(pmax(), FLOAT);

		if(!jythonMap.containsKey(_pmax))jythonMap.put(_pmax, new ArrayList<Tripple>());
		jythonMap.get(_pmax).add(new Tripple(pmax(),"setPmax",String.class));

		//*************************
		str2id.put(_pname_map, pname_map());
		varTypes.put(pname_map(), OBJECT);

		if(!jythonMap.containsKey(_pname_map))jythonMap.put(_pname_map, new ArrayList<Tripple>());
		jythonMap.get(_pname_map).add(new Tripple(pname_map(),"setPnameMap",String.class));

		//*************************
		str2id.put(_pno_map, pno_map());
		varTypes.put(pno_map(), OBJECT);

		if(!jythonMap.containsKey(_pno_map))jythonMap.put(_pno_map, new ArrayList<Tripple>());
		jythonMap.get(_pno_map).add(new Tripple(pno_map(),"setPnoMap",String.class));

		//*************************
		str2id.put(_portal_rules, portal_rules());
		varTypes.put(portal_rules(), OBJECT);

		if(!jythonMap.containsKey(_portal_rules))jythonMap.put(_portal_rules, new ArrayList<Tripple>());
		jythonMap.get(_portal_rules).add(new Tripple(portal_rules(),"setPortalRules",String.class));

		//*************************
		str2id.put(_portals, portals());
		varTypes.put(portals(), OBJECT);

		if(!jythonMap.containsKey(_portals))jythonMap.put(_portals, new ArrayList<Tripple>());
		jythonMap.get(_portals).add(new Tripple(portals(),"setPortals",String.class));

		//*************************
		str2id.put(_prop_delay, prop_delay());
		varTypes.put(prop_delay(), FLOAT);

		if(!jythonMap.containsKey(_prop_delay))jythonMap.put(_prop_delay, new ArrayList<Tripple>());
		jythonMap.get(_prop_delay).add(new Tripple(prop_delay(),"setPropDelay",String.class));

		//*************************
		str2id.put(_protocol_list, protocol_list());
		varTypes.put(protocol_list(), OBJECT);

		if(!jythonMap.containsKey(_protocol_list))jythonMap.put(_protocol_list, new ArrayList<Tripple>());
		jythonMap.get(_protocol_list).add(new Tripple(protocol_list(),"setProtocolList",String.class));

		//*************************
		str2id.put(_protocol_type, protocol_type());
		varTypes.put(protocol_type(), INT);

		if(!jythonMap.containsKey(_protocol_type))jythonMap.put(_protocol_type, new ArrayList<Tripple>());
		jythonMap.get(_protocol_type).add(new Tripple(protocol_type(),"setProtocolType",String.class));

		//*************************
		str2id.put(_qcap, qcap());
		varTypes.put(qcap(), FLOAT);

		if(!jythonMap.containsKey(_qcap))jythonMap.put(_qcap, new ArrayList<Tripple>());
		jythonMap.get(_qcap).add(new Tripple(qcap(),"setQcap",String.class));

		//*************************
		str2id.put(_qmax, qmax());
		varTypes.put(qmax(), FLOAT);

		if(!jythonMap.containsKey(_qmax))jythonMap.put(_qmax, new ArrayList<Tripple>());
		jythonMap.get(_qmax).add(new Tripple(qmax(),"setQmax",String.class));

		//*************************
		str2id.put(_qmin, qmin());
		varTypes.put(qmin(), FLOAT);

		if(!jythonMap.containsKey(_qmin))jythonMap.put(_qmin, new ArrayList<Tripple>());
		jythonMap.get(_qmin).add(new Tripple(qmin(),"setQmin",String.class));

		//*************************
		str2id.put(_queue, queue());
		varTypes.put(queue(), FLOAT);

		if(!jythonMap.containsKey(_queue))jythonMap.put(_queue, new ArrayList<Tripple>());
		jythonMap.get(_queue).add(new Tripple(queue(),"setQueue",String.class));

		//*************************
		str2id.put(_queue_delay, queue_delay());
		varTypes.put(queue_delay(), OBJECT);

		if(!jythonMap.containsKey(_queue_delay))jythonMap.put(_queue_delay, new ArrayList<Tripple>());
		jythonMap.get(_queue_delay).add(new Tripple(queue_delay(),"setQueueDelay",String.class));

		//*************************
		str2id.put(_queue_size, queue_size());
		varTypes.put(queue_size(), INT);

		if(!jythonMap.containsKey(_queue_size))jythonMap.put(_queue_size, new ArrayList<Tripple>());
		jythonMap.get(_queue_size).add(new Tripple(queue_size(),"setQueueSize",String.class));

		//*************************
		str2id.put(_queue_type, queue_type());
		varTypes.put(queue_type(), STRING);

		if(!jythonMap.containsKey(_queue_type))jythonMap.put(_queue_type, new ArrayList<Tripple>());
		jythonMap.get(_queue_type).add(new Tripple(queue_type(),"setQueueType",String.class));

		//*************************
		str2id.put(_rcvWndSize, rcvWndSize());
		varTypes.put(rcvWndSize(), INT);

		if(!jythonMap.containsKey(_rcvWndSize))jythonMap.put(_rcvWndSize, new ArrayList<Tripple>());
		jythonMap.get(_rcvWndSize).add(new Tripple(rcvWndSize(),"setRcvWndSize",String.class));

		//*************************
		str2id.put(_real_uid, real_uid());
		varTypes.put(real_uid(), INT);

		if(!jythonMap.containsKey(_real_uid))jythonMap.put(_real_uid, new ArrayList<Tripple>());
		jythonMap.get(_real_uid).add(new Tripple(real_uid(),"setRealUid",String.class));

		//*************************
		str2id.put(_requestsReceived, requestsReceived());
		varTypes.put(requestsReceived(), INT);

		if(!jythonMap.containsKey(_requestsReceived))jythonMap.put(_requestsReceived, new ArrayList<Tripple>());
		jythonMap.get(_requestsReceived).add(new Tripple(requestsReceived(),"setRequestsReceived",String.class));

		//*************************
		str2id.put(_requests_received, requests_received());
		varTypes.put(requests_received(), INT);

		if(!jythonMap.containsKey(_requests_received))jythonMap.put(_requests_received, new ArrayList<Tripple>());
		jythonMap.get(_requests_received).add(new Tripple(requests_received(),"setRequestsReceived",String.class));

		//*************************
		str2id.put(_rng, rng());
		varTypes.put(rng(), OBJECT);

		if(!jythonMap.containsKey(_rng))jythonMap.put(_rng, new ArrayList<Tripple>());
		jythonMap.get(_rng).add(new Tripple(rng(),"setRng",String.class));

		//*************************
		str2id.put(_rng_seed, rng_seed());
		varTypes.put(rng_seed(), INT);

		if(!jythonMap.containsKey(_rng_seed))jythonMap.put(_rng_seed, new ArrayList<Tripple>());
		jythonMap.get(_rng_seed).add(new Tripple(rng_seed(),"setRngSeed",String.class));

		//*************************
		str2id.put(_routemap, routemap());
		varTypes.put(routemap(), OBJECT);

		if(!jythonMap.containsKey(_routemap))jythonMap.put(_routemap, new ArrayList<Tripple>());
		jythonMap.get(_routemap).add(new Tripple(routemap(),"setRoutemap",String.class));

		//*************************
		str2id.put(_rre_timeout, rre_timeout());
		varTypes.put(rre_timeout(), FLOAT);

		if(!jythonMap.containsKey(_rre_timeout))jythonMap.put(_rre_timeout, new ArrayList<Tripple>());
		jythonMap.get(_rre_timeout).add(new Tripple(rre_timeout(),"setRreTimeout",String.class));

		//*************************
		str2id.put(_rtt, rtt());
		varTypes.put(rtt(), FLOAT);

		if(!jythonMap.containsKey(_rtt))jythonMap.put(_rtt, new ArrayList<Tripple>());
		jythonMap.get(_rtt).add(new Tripple(rtt(),"setRtt",String.class));

		//*************************
		str2id.put(_sample_count, sample_count());
		varTypes.put(sample_count(), FLOAT);

		if(!jythonMap.containsKey(_sample_count))jythonMap.put(_sample_count, new ArrayList<Tripple>());
		jythonMap.get(_sample_count).add(new Tripple(sample_count(),"setSampleCount",String.class));

		//*************************
		str2id.put(_samplingInterval, samplingInterval());
		varTypes.put(samplingInterval(), FLOAT);

		if(!jythonMap.containsKey(_samplingInterval))jythonMap.put(_samplingInterval, new ArrayList<Tripple>());
		jythonMap.get(_samplingInterval).add(new Tripple(samplingInterval(),"setSamplingInterval",String.class));

		//*************************
		str2id.put(_sendrate, sendrate());
		varTypes.put(sendrate(), FLOAT);

		if(!jythonMap.containsKey(_sendrate))jythonMap.put(_sendrate, new ArrayList<Tripple>());
		jythonMap.get(_sendrate).add(new Tripple(sendrate(),"setSendrate",String.class));

		//*************************
		str2id.put(_session_timeout, session_timeout());
		varTypes.put(session_timeout(), INT);

		if(!jythonMap.containsKey(_session_timeout))jythonMap.put(_session_timeout, new ArrayList<Tripple>());
		jythonMap.get(_session_timeout).add(new Tripple(session_timeout(),"setSessionTimeout",String.class));

		//*************************
		str2id.put(_shadow_avgque, shadow_avgque());
		varTypes.put(shadow_avgque(), FLOAT);

		if(!jythonMap.containsKey(_shadow_avgque))jythonMap.put(_shadow_avgque, new ArrayList<Tripple>());
		jythonMap.get(_shadow_avgque).add(new Tripple(shadow_avgque(),"setShadowAvgque",String.class));

		//*************************
		str2id.put(_shadow_loss, shadow_loss());
		varTypes.put(shadow_loss(), FLOAT);

		if(!jythonMap.containsKey(_shadow_loss))jythonMap.put(_shadow_loss, new ArrayList<Tripple>());
		jythonMap.get(_shadow_loss).add(new Tripple(shadow_loss(),"setShadowLoss",String.class));

		//*************************
		str2id.put(_shadow_queue, shadow_queue());
		varTypes.put(shadow_queue(), FLOAT);

		if(!jythonMap.containsKey(_shadow_queue))jythonMap.put(_shadow_queue, new ArrayList<Tripple>());
		jythonMap.get(_shadow_queue).add(new Tripple(shadow_queue(),"setShadowQueue",String.class));

		//*************************
		str2id.put(_shortcut_to, shortcut_to());
		varTypes.put(shortcut_to(), OBJECT);

		if(!jythonMap.containsKey(_shortcut_to))jythonMap.put(_shortcut_to, new ArrayList<Tripple>());
		jythonMap.get(_shortcut_to).add(new Tripple(shortcut_to(),"setShortcutTo",String.class));

		//*************************
		str2id.put(_size, size());
		varTypes.put(size(), INT);

		if(!jythonMap.containsKey(_size))jythonMap.put(_size, new ArrayList<Tripple>());
		jythonMap.get(_size).add(new Tripple(size(),"setSize",String.class));

		//*************************
		str2id.put(_sndBufSize, sndBufSize());
		varTypes.put(sndBufSize(), INT);

		if(!jythonMap.containsKey(_sndBufSize))jythonMap.put(_sndBufSize, new ArrayList<Tripple>());
		jythonMap.get(_sndBufSize).add(new Tripple(sndBufSize(),"setSndBufSize",String.class));

		//*************************
		str2id.put(_sndWndSize, sndWndSize());
		varTypes.put(sndWndSize(), INT);

		if(!jythonMap.containsKey(_sndWndSize))jythonMap.put(_sndWndSize, new ArrayList<Tripple>());
		jythonMap.get(_sndWndSize).add(new Tripple(sndWndSize(),"setSndWndSize",String.class));

		//*************************
		str2id.put(_srcs, srcs());
		varTypes.put(srcs(), RESOURCE_ID);

		if(!jythonMap.containsKey(_srcs))jythonMap.put(_srcs, new ArrayList<Tripple>());
		jythonMap.get(_srcs).add(new Tripple(srcs(),"setSrcs",String.class));

		//*************************
		str2id.put(_start, start());
		varTypes.put(start(), FLOAT);

		if(!jythonMap.containsKey(_start))jythonMap.put(_start, new ArrayList<Tripple>());
		jythonMap.get(_start).add(new Tripple(start(),"setStart",String.class));

		//*************************
		str2id.put(_start_time, start_time());
		varTypes.put(start_time(), FLOAT);

		if(!jythonMap.containsKey(_start_time))jythonMap.put(_start_time, new ArrayList<Tripple>());
		jythonMap.get(_start_time).add(new Tripple(start_time(),"setStartTime",String.class));

		//*************************
		str2id.put(_stop, stop());
		varTypes.put(stop(), FLOAT);

		if(!jythonMap.containsKey(_stop))jythonMap.put(_stop, new ArrayList<Tripple>());
		jythonMap.get(_stop).add(new Tripple(stop(),"setStop",String.class));

		//*************************
		str2id.put(_stretch, stretch());
		varTypes.put(stretch(), BOOL);

		if(!jythonMap.containsKey(_stretch))jythonMap.put(_stretch, new ArrayList<Tripple>());
		jythonMap.get(_stretch).add(new Tripple(stretch(),"setStretch",String.class));

		//*************************
		str2id.put(_sub_edge_iface_map, sub_edge_iface_map());
		varTypes.put(sub_edge_iface_map(), OBJECT);

		if(!jythonMap.containsKey(_sub_edge_iface_map))jythonMap.put(_sub_edge_iface_map, new ArrayList<Tripple>());
		jythonMap.get(_sub_edge_iface_map).add(new Tripple(sub_edge_iface_map(),"setSubEdgeIfaceMap",String.class));

		//*************************
		str2id.put(_sub_edge_ifaces, sub_edge_ifaces());
		varTypes.put(sub_edge_ifaces(), OBJECT);

		if(!jythonMap.containsKey(_sub_edge_ifaces))jythonMap.put(_sub_edge_ifaces, new ArrayList<Tripple>());
		jythonMap.get(_sub_edge_ifaces).add(new Tripple(sub_edge_ifaces(),"setSubEdgeIfaces",String.class));

		//*************************
		str2id.put(_sum, sum());
		varTypes.put(sum(), FLOAT);

		if(!jythonMap.containsKey(_sum))jythonMap.put(_sum, new ArrayList<Tripple>());
		jythonMap.get(_sum).add(new Tripple(sum(),"setSum",String.class));

		//*************************
		str2id.put(_tcpCA, tcpCA());
		varTypes.put(tcpCA(), STRING);

		if(!jythonMap.containsKey(_tcpCA))jythonMap.put(_tcpCA, new ArrayList<Tripple>());
		jythonMap.get(_tcpCA).add(new Tripple(tcpCA(),"setTcpCA",String.class));

		//*************************
		str2id.put(_time_offset, time_offset());
		varTypes.put(time_offset(), OBJECT);

		if(!jythonMap.containsKey(_time_offset))jythonMap.put(_time_offset, new ArrayList<Tripple>());
		jythonMap.get(_time_offset).add(new Tripple(time_offset(),"setTimeOffset",String.class));

		//*************************
		str2id.put(_time_skew, time_skew());
		varTypes.put(time_skew(), FLOAT);

		if(!jythonMap.containsKey(_time_skew))jythonMap.put(_time_skew, new ArrayList<Tripple>());
		jythonMap.get(_time_skew).add(new Tripple(time_skew(),"setTimeSkew",String.class));

		//*************************
		str2id.put(_tmr_handle, tmr_handle());
		varTypes.put(tmr_handle(), INT);

		if(!jythonMap.containsKey(_tmr_handle))jythonMap.put(_tmr_handle, new ArrayList<Tripple>());
		jythonMap.get(_tmr_handle).add(new Tripple(tmr_handle(),"setTmrHandle",String.class));

		//*************************
		str2id.put(_tmr_map, tmr_map());
		varTypes.put(tmr_map(), OBJECT);

		if(!jythonMap.containsKey(_tmr_map))jythonMap.put(_tmr_map, new ArrayList<Tripple>());
		jythonMap.get(_tmr_map).add(new Tripple(tmr_map(),"setTmrMap",String.class));

		//*************************
		str2id.put(_tmr_queue, tmr_queue());
		varTypes.put(tmr_queue(), OBJECT);

		if(!jythonMap.containsKey(_tmr_queue))jythonMap.put(_tmr_queue, new ArrayList<Tripple>());
		jythonMap.get(_tmr_queue).add(new Tripple(tmr_queue(),"setTmrQueue",String.class));

		//*************************
		str2id.put(_toEmulated, toEmulated());
		varTypes.put(toEmulated(), BOOL);

		if(!jythonMap.containsKey(_toEmulated))jythonMap.put(_toEmulated, new ArrayList<Tripple>());
		jythonMap.get(_toEmulated).add(new Tripple(toEmulated(),"setToEmulated",String.class));

		//*************************
		str2id.put(_to_aggregate, to_aggregate());
		varTypes.put(to_aggregate(), RESOURCE_ID);

		if(!jythonMap.containsKey(_to_aggregate))jythonMap.put(_to_aggregate, new ArrayList<Tripple>());
		jythonMap.get(_to_aggregate).add(new Tripple(to_aggregate(),"setToAggregate",String.class));

		//*************************
		str2id.put(_to_emulated, to_emulated());
		varTypes.put(to_emulated(), BOOL);

		if(!jythonMap.containsKey(_to_emulated))jythonMap.put(_to_emulated, new ArrayList<Tripple>());
		jythonMap.get(_to_emulated).add(new Tripple(to_emulated(),"setToEmulated",String.class));

		//*************************
		str2id.put(_to_monitor, to_monitor());
		varTypes.put(to_monitor(), RESOURCE_ID);

		if(!jythonMap.containsKey(_to_monitor))jythonMap.put(_to_monitor, new ArrayList<Tripple>());
		jythonMap.get(_to_monitor).add(new Tripple(to_monitor(),"setToMonitor",String.class));

		//*************************
		str2id.put(_total_bytes_received, total_bytes_received());
		varTypes.put(total_bytes_received(), INT);

		if(!jythonMap.containsKey(_total_bytes_received))jythonMap.put(_total_bytes_received, new ArrayList<Tripple>());
		jythonMap.get(_total_bytes_received).add(new Tripple(total_bytes_received(),"setTotalBytesReceived",String.class));

		//*************************
		str2id.put(_total_bytes_sent, total_bytes_sent());
		varTypes.put(total_bytes_sent(), INT);

		if(!jythonMap.containsKey(_total_bytes_sent))jythonMap.put(_total_bytes_sent, new ArrayList<Tripple>());
		jythonMap.get(_total_bytes_sent).add(new Tripple(total_bytes_sent(),"setTotalBytesSent",String.class));

		//*************************
		str2id.put(_trace_description, trace_description());
		varTypes.put(trace_description(), STRING);

		if(!jythonMap.containsKey(_trace_description))jythonMap.put(_trace_description, new ArrayList<Tripple>());
		jythonMap.get(_trace_description).add(new Tripple(trace_description(),"setTraceDescription",String.class));

		//*************************
		str2id.put(_traffic_flows, traffic_flows());
		varTypes.put(traffic_flows(), OBJECT);

		if(!jythonMap.containsKey(_traffic_flows))jythonMap.put(_traffic_flows, new ArrayList<Tripple>());
		jythonMap.get(_traffic_flows).add(new Tripple(traffic_flows(),"setTrafficFlows",String.class));

		//*************************
		str2id.put(_traffic_intensity, traffic_intensity());
		varTypes.put(traffic_intensity(), FLOAT);

		if(!jythonMap.containsKey(_traffic_intensity))jythonMap.put(_traffic_intensity, new ArrayList<Tripple>());
		jythonMap.get(_traffic_intensity).add(new Tripple(traffic_intensity(),"setTrafficIntensity",String.class));

		//*************************
		str2id.put(_traffic_type_seed, traffic_type_seed());
		varTypes.put(traffic_type_seed(), INT);

		if(!jythonMap.containsKey(_traffic_type_seed))jythonMap.put(_traffic_type_seed, new ArrayList<Tripple>());
		jythonMap.get(_traffic_type_seed).add(new Tripple(traffic_type_seed(),"setTrafficTypeSeed",String.class));

		//*************************
		str2id.put(_tx_queue, tx_queue());
		varTypes.put(tx_queue(), OBJECT);

		if(!jythonMap.containsKey(_tx_queue))jythonMap.put(_tx_queue, new ArrayList<Tripple>());
		jythonMap.get(_tx_queue).add(new Tripple(tx_queue(),"setTxQueue",String.class));

		//*************************
		str2id.put(_tx_timer_queue, tx_timer_queue());
		varTypes.put(tx_timer_queue(), OBJECT);

		if(!jythonMap.containsKey(_tx_timer_queue))jythonMap.put(_tx_timer_queue, new ArrayList<Tripple>());
		jythonMap.get(_tx_timer_queue).add(new Tripple(tx_timer_queue(),"setTxTimerQueue",String.class));

		//*************************
		str2id.put(_uid, uid());
		varTypes.put(uid(), INT);

		if(!jythonMap.containsKey(_uid))jythonMap.put(_uid, new ArrayList<Tripple>());
		jythonMap.get(_uid).add(new Tripple(uid(),"setUid",String.class));

		//*************************
		str2id.put(_vacate_time, vacate_time());
		varTypes.put(vacate_time(), OBJECT);

		if(!jythonMap.containsKey(_vacate_time))jythonMap.put(_vacate_time, new ArrayList<Tripple>());
		jythonMap.get(_vacate_time).add(new Tripple(vacate_time(),"setVacateTime",String.class));

		//*************************
		str2id.put(_var_id, var_id());
		varTypes.put(var_id(), INT);

		if(!jythonMap.containsKey(_var_id))jythonMap.put(_var_id, new ArrayList<Tripple>());
		jythonMap.get(_var_id).add(new Tripple(var_id(),"setVarId",String.class));

		//*************************
		str2id.put(_version, version());
		varTypes.put(version(), STRING);

		if(!jythonMap.containsKey(_version))jythonMap.put(_version, new ArrayList<Tripple>());
		jythonMap.get(_version).add(new Tripple(version(),"setVersion",String.class));

		//*************************
		str2id.put(_wait_opt, wait_opt());
		varTypes.put(wait_opt(), BOOL);

		if(!jythonMap.containsKey(_wait_opt))jythonMap.put(_wait_opt, new ArrayList<Tripple>());
		jythonMap.get(_wait_opt).add(new Tripple(wait_opt(),"setWaitOpt",String.class));

		//*************************
		str2id.put(_weight, weight());
		varTypes.put(weight(), FLOAT);

		if(!jythonMap.containsKey(_weight))jythonMap.put(_weight, new ArrayList<Tripple>());
		jythonMap.get(_weight).add(new Tripple(weight(),"setWeight",String.class));

		//*************************
		str2id.put(_window, window());
		varTypes.put(window(), FLOAT);

		if(!jythonMap.containsKey(_window))jythonMap.put(_window, new ArrayList<Tripple>());
		jythonMap.get(_window).add(new Tripple(window(),"setWindow",String.class));

		//*************************
		str2id.put(_wndmax, wndmax());
		varTypes.put(wndmax(), FLOAT);

		if(!jythonMap.containsKey(_wndmax))jythonMap.put(_wndmax, new ArrayList<Tripple>());
		jythonMap.get(_wndmax).add(new Tripple(wndmax(),"setWndmax",String.class));


		//*************************
		//generate jython map valus for child accessors
		//*************************


		//*************************
		if(!jythonMap.containsKey("attachments"))jythonMap.put("attachments",new ArrayList<Tripple>());
		jythonMap.get("attachments").add(new Tripple(-1,"addBaseInterface",jprime.BaseInterface.IBaseInterface.class));
		jythonMap.get("attachments").add(new Tripple(-1,"addBaseInterfaceAlias",jprime.BaseInterface.IBaseInterfaceAlias.class));

		//*************************
		if(!jythonMap.containsKey("nic_queue"))jythonMap.put("nic_queue",new ArrayList<Tripple>());
		jythonMap.get("nic_queue").add(new Tripple(-1,"addNicQueue",jprime.NicQueue.NicQueue.class));
		jythonMap.get("nic_queue").add(new Tripple(-1,"addNicQueueAlias",jprime.NicQueue.NicQueueAlias.class));
		jythonMap.get("nic_queue").add(new Tripple(-1,"addNicQueueReplica",jprime.NicQueue.NicQueueReplica.class));

		//*************************
		if(!jythonMap.containsKey("emu_proto"))jythonMap.put("emu_proto",new ArrayList<Tripple>());
		jythonMap.get("emu_proto").add(new Tripple(-1,"addEmulationProtocol",jprime.EmulationProtocol.EmulationProtocol.class));
		jythonMap.get("emu_proto").add(new Tripple(-1,"addEmulationProtocolAlias",jprime.EmulationProtocol.EmulationProtocolAlias.class));
		jythonMap.get("emu_proto").add(new Tripple(-1,"addEmulationProtocolReplica",jprime.EmulationProtocol.EmulationProtocolReplica.class));

		//*************************
		if(!jythonMap.containsKey("nics"))jythonMap.put("nics",new ArrayList<Tripple>());
		jythonMap.get("nics").add(new Tripple(-1,"addInterface",jprime.Interface.Interface.class));
		jythonMap.get("nics").add(new Tripple(-1,"addInterfaceAlias",jprime.Interface.InterfaceAlias.class));
		jythonMap.get("nics").add(new Tripple(-1,"addInterfaceReplica",jprime.Interface.InterfaceReplica.class));

		//*************************
		if(!jythonMap.containsKey("subnets"))jythonMap.put("subnets",new ArrayList<Tripple>());
		jythonMap.get("subnets").add(new Tripple(-1,"addNet",jprime.Net.Net.class));
		jythonMap.get("subnets").add(new Tripple(-1,"addNetAlias",jprime.Net.NetAlias.class));
		jythonMap.get("subnets").add(new Tripple(-1,"addNetReplica",jprime.Net.NetReplica.class));

		//*************************
		if(!jythonMap.containsKey("hosts"))jythonMap.put("hosts",new ArrayList<Tripple>());
		jythonMap.get("hosts").add(new Tripple(-1,"addHost",jprime.Host.Host.class));
		jythonMap.get("hosts").add(new Tripple(-1,"addHostAlias",jprime.Host.HostAlias.class));
		jythonMap.get("hosts").add(new Tripple(-1,"addHostReplica",jprime.Host.HostReplica.class));

		//*************************
		if(!jythonMap.containsKey("links"))jythonMap.put("links",new ArrayList<Tripple>());
		jythonMap.get("links").add(new Tripple(-1,"addLink",jprime.Link.Link.class));
		jythonMap.get("links").add(new Tripple(-1,"addLinkAlias",jprime.Link.LinkAlias.class));
		jythonMap.get("links").add(new Tripple(-1,"addLinkReplica",jprime.Link.LinkReplica.class));

		//*************************
		if(!jythonMap.containsKey("placeholders"))jythonMap.put("placeholders",new ArrayList<Tripple>());
		jythonMap.get("placeholders").add(new Tripple(-1,"addPlaceHolder",jprime.PlaceHolder.PlaceHolder.class));
		jythonMap.get("placeholders").add(new Tripple(-1,"addPlaceHolderAlias",jprime.PlaceHolder.PlaceHolderAlias.class));
		jythonMap.get("placeholders").add(new Tripple(-1,"addPlaceHolderReplica",jprime.PlaceHolder.PlaceHolderReplica.class));

		//*************************
		if(!jythonMap.containsKey("rsphere"))jythonMap.put("rsphere",new ArrayList<Tripple>());
		jythonMap.get("rsphere").add(new Tripple(-1,"addRoutingSphere",jprime.RoutingSphere.RoutingSphere.class));
		jythonMap.get("rsphere").add(new Tripple(-1,"addRoutingSphereAlias",jprime.RoutingSphere.RoutingSphereAlias.class));
		jythonMap.get("rsphere").add(new Tripple(-1,"addRoutingSphereReplica",jprime.RoutingSphere.RoutingSphereReplica.class));

		//*************************
		if(!jythonMap.containsKey("monitors"))jythonMap.put("monitors",new ArrayList<Tripple>());
		jythonMap.get("monitors").add(new Tripple(-1,"addMonitor",jprime.Monitor.Monitor.class));
		jythonMap.get("monitors").add(new Tripple(-1,"addMonitorAlias",jprime.Monitor.MonitorAlias.class));
		jythonMap.get("monitors").add(new Tripple(-1,"addMonitorReplica",jprime.Monitor.MonitorReplica.class));

		//*************************
		if(!jythonMap.containsKey("aggregates"))jythonMap.put("aggregates",new ArrayList<Tripple>());
		jythonMap.get("aggregates").add(new Tripple(-1,"addAggregate",jprime.Aggregate.Aggregate.class));
		jythonMap.get("aggregates").add(new Tripple(-1,"addAggregateAlias",jprime.Aggregate.AggregateAlias.class));
		jythonMap.get("aggregates").add(new Tripple(-1,"addAggregateReplica",jprime.Aggregate.AggregateReplica.class));

		//*************************
		if(!jythonMap.containsKey("traffics"))jythonMap.put("traffics",new ArrayList<Tripple>());
		jythonMap.get("traffics").add(new Tripple(-1,"addTraffic",jprime.Traffic.Traffic.class));
		jythonMap.get("traffics").add(new Tripple(-1,"addTrafficAlias",jprime.Traffic.TrafficAlias.class));
		jythonMap.get("traffics").add(new Tripple(-1,"addTrafficReplica",jprime.Traffic.TrafficReplica.class));

		//*************************
		if(!jythonMap.containsKey("traffic_types"))jythonMap.put("traffic_types",new ArrayList<Tripple>());
		jythonMap.get("traffic_types").add(new Tripple(-1,"addTrafficType",jprime.TrafficType.TrafficType.class));
		jythonMap.get("traffic_types").add(new Tripple(-1,"addTrafficTypeAlias",jprime.TrafficType.TrafficTypeAlias.class));
		jythonMap.get("traffic_types").add(new Tripple(-1,"addTrafficTypeReplica",jprime.TrafficType.TrafficTypeReplica.class));

		//*************************
		if(!jythonMap.containsKey("cfg_sess"))jythonMap.put("cfg_sess",new ArrayList<Tripple>());
		jythonMap.get("cfg_sess").add(new Tripple(-1,"addProtocolSession",jprime.ProtocolSession.ProtocolSession.class));
		jythonMap.get("cfg_sess").add(new Tripple(-1,"addProtocolSessionAlias",jprime.ProtocolSession.ProtocolSessionAlias.class));
		jythonMap.get("cfg_sess").add(new Tripple(-1,"addProtocolSessionReplica",jprime.ProtocolSession.ProtocolSessionReplica.class));

		//*************************
		if(!jythonMap.containsKey("default_route_table"))jythonMap.put("default_route_table",new ArrayList<Tripple>());
		jythonMap.get("default_route_table").add(new Tripple(-1,"addRouteTable",jprime.RouteTable.RouteTable.class));
		jythonMap.get("default_route_table").add(new Tripple(-1,"addRouteTableAlias",jprime.RouteTable.RouteTableAlias.class));
		jythonMap.get("default_route_table").add(new Tripple(-1,"addRouteTableReplica",jprime.RouteTable.RouteTableReplica.class));


		//*************************
		//generate default values
		//*************************


		//*************************
		//for type BaseEntity[NUL]
		//*************************
		{
			ClsDesc cls = new ClsDesc("ModelNode", jprime.ModelNode.class, null);
			new VarDesc(uid(), "uid", "The index of this node in the post-order traversal from top-most network", "0", true, false, varTypes.get(uid()), cls);
			new VarDesc(name(), "name", "The number of nodes which precede this node in the post-order traversal from this node's parent", "<no name>", true, false, varTypes.get(name()), cls);
			new VarDesc(size(), "size", "The number of nodes of which this node is an ancestor", "0", false, false, varTypes.get(size()), cls);
			new VarDesc(offset(), "offset", "The number of nodes which precede this node in the post-order traversal from this node's parent", "0", false, false, varTypes.get(offset()), cls);
			clsDescs.put(jprime.ModelNode.class, cls);

		}

		//*************************
		//for type EmulationProtocol[BaseEntity]
		//*************************
		{
			ClsDesc cls = new ClsDesc("EmulationProtocol", jprime.ModelNode.class, clsDescs.get(jprime.ModelNode.class));
			for(Entry<Integer, VarDesc> e : cls.super_desc.vars.entrySet()) {
				cls.vars.put(e.getKey(), e.getValue());
			}
			new VarDesc(ip_forwarding(), "ip_forwarding", "If ip_forwarding is set to true the emulated interface will intercept all pkts it sees. If it is set to false it will only capture pkts targeted for it.", "false", false, false, varTypes.get(ip_forwarding()), cls);
			clsDescs.put(jprime.EmulationProtocol.EmulationProtocol.class, cls);
			clsDescs.put(jprime.EmulationProtocol.EmulationProtocolReplica.class, cls);
		}

		//*************************
		//for type Link[BaseEntity]
		//*************************
		{
			ClsDesc cls = new ClsDesc("Link", jprime.ModelNode.class, clsDescs.get(jprime.ModelNode.class));
			for(Entry<Integer, VarDesc> e : cls.super_desc.vars.entrySet()) {
				cls.vars.put(e.getKey(), e.getValue());
			}
			new VarDesc(delay(), "delay", "link delay", "0.001", true, false, varTypes.get(delay()), cls);
			new VarDesc(bandwidth(), "bandwidth", "link bandwidth", "1e8", true, false, varTypes.get(bandwidth()), cls);
			new VarDesc(ip_prefix_len(), "ip_prefix_len", "The n in W.X.Y.Z/n", "-1", false, false, varTypes.get(ip_prefix_len()), cls);
			new VarDesc(ip_prefix(), "ip_prefix", "ip prefix of this Link", "0.0.0.0/0", false, false, varTypes.get(ip_prefix()), cls);
			clsDescs.put(jprime.Link.Link.class, cls);
			clsDescs.put(jprime.Link.LinkReplica.class, cls);
		}

		//*************************
		//for type BaseInterface[BaseEntity]
		//*************************
		{
			ClsDesc cls = new ClsDesc("BaseInterface", jprime.ModelNode.class, clsDescs.get(jprime.ModelNode.class));
			for(Entry<Integer, VarDesc> e : cls.super_desc.vars.entrySet()) {
				cls.vars.put(e.getKey(), e.getValue());
			}
			new VarDesc(ip_address(), "ip_address", "ip address", "0.0.0.0", true, false, varTypes.get(ip_address()), cls);
			new VarDesc(mac_address(), "mac_address", "mac address", "0:0:0:0:0:0", false, false, varTypes.get(mac_address()), cls);
			clsDescs.put(jprime.BaseInterface.BaseInterface.class, cls);
			clsDescs.put(jprime.BaseInterface.BaseInterfaceReplica.class, cls);
		}

		//*************************
		//for type Net[BaseEntity]
		//*************************
		{
			ClsDesc cls = new ClsDesc("Net", jprime.ModelNode.class, clsDescs.get(jprime.ModelNode.class));
			for(Entry<Integer, VarDesc> e : cls.super_desc.vars.entrySet()) {
				cls.vars.put(e.getKey(), e.getValue());
			}
			new VarDesc(ip_prefix_len(), "ip_prefix_len", "The n in W.X.Y.Z/n", "-1", false, false, varTypes.get(ip_prefix_len()), cls);
			new VarDesc(ip_prefix(), "ip_prefix", "ip prefix of this network", "0.0.0.0/0", false, false, varTypes.get(ip_prefix()), cls);
			new VarDesc(cnf_content_ids(), "cnf_content_ids", "a list of content id and uid. i.e. [1,100,2,200] --> [ cid:1, uid:100],[ cid:2,uid:200]", "[]", false, false, varTypes.get(cnf_content_ids()), cls);
			new VarDesc(portal_rules(), "portal_rules", "a list of rules in the form [ip_prefix->(router_uid,iface_uid,iface_ip)]. They will automatically be compiled into the topnet.", "[]", false, false, varTypes.get(portal_rules()), cls);
			new VarDesc(controller_rid(), "controller_rid", "The RID of the cnf controller.", "0", false, false, varTypes.get(controller_rid()), cls);
			new VarDesc(sub_edge_ifaces(), "sub_edge_ifaces", "A comma separated list of its child sphere's edge iface and owning_host rank pairs", "[]", false, false, varTypes.get(sub_edge_ifaces()), cls);
			clsDescs.put(jprime.Net.Net.class, cls);
			clsDescs.put(jprime.Net.NetReplica.class, cls);
		}

		//*************************
		//for type NicQueue[BaseEntity]
		//*************************
		{
			ClsDesc cls = new ClsDesc("NicQueue", jprime.ModelNode.class, clsDescs.get(jprime.ModelNode.class));
			for(Entry<Integer, VarDesc> e : cls.super_desc.vars.entrySet()) {
				cls.vars.put(e.getKey(), e.getValue());
			}
			clsDescs.put(jprime.NicQueue.NicQueue.class, cls);
			clsDescs.put(jprime.NicQueue.NicQueueReplica.class, cls);
		}

		//*************************
		//for type Aggregate[BaseEntity]
		//*************************
		{
			ClsDesc cls = new ClsDesc("Aggregate", jprime.ModelNode.class, clsDescs.get(jprime.ModelNode.class));
			for(Entry<Integer, VarDesc> e : cls.super_desc.vars.entrySet()) {
				cls.vars.put(e.getKey(), e.getValue());
			}
			new VarDesc(var_id(), "var_id", "the id of the variable which will be aggregated.", "0", false, false, varTypes.get(var_id()), cls);
			new VarDesc(to_aggregate(), "to_aggregate", "a list of entity relative ids which will have their state aggregated", "", false, false, varTypes.get(to_aggregate()), cls);
			new VarDesc(min(), "min", "min of all the values at the sample time.", "0", true, true, varTypes.get(min()), cls);
			new VarDesc(max(), "max", "max of all the values at the sample time.", "0", true, true, varTypes.get(max()), cls);
			new VarDesc(sample_count(), "sample_count", "the number of samples.", "0", true, true, varTypes.get(sample_count()), cls);
			new VarDesc(sum(), "sum", "total of all the values at the sample time.", "0", true, true, varTypes.get(sum()), cls);
			clsDescs.put(jprime.Aggregate.Aggregate.class, cls);
			clsDescs.put(jprime.Aggregate.AggregateReplica.class, cls);
		}

		//*************************
		//for type Monitor[BaseEntity]
		//*************************
		{
			ClsDesc cls = new ClsDesc("Monitor", jprime.ModelNode.class, clsDescs.get(jprime.ModelNode.class));
			for(Entry<Integer, VarDesc> e : cls.super_desc.vars.entrySet()) {
				cls.vars.put(e.getKey(), e.getValue());
			}
			new VarDesc(period(), "period", "the period of polling (i.e. how long between polls) in milliseconds.", "500", false, false, varTypes.get(period()), cls);
			new VarDesc(to_monitor(), "to_monitor", "a list of entity relative ids which will have their state monitored", "", false, false, varTypes.get(to_monitor()), cls);
			clsDescs.put(jprime.Monitor.Monitor.class, cls);
			clsDescs.put(jprime.Monitor.MonitorReplica.class, cls);
		}

		//*************************
		//for type Traffic[BaseEntity]
		//*************************
		{
			ClsDesc cls = new ClsDesc("Traffic", jprime.ModelNode.class, clsDescs.get(jprime.ModelNode.class));
			for(Entry<Integer, VarDesc> e : cls.super_desc.vars.entrySet()) {
				cls.vars.put(e.getKey(), e.getValue());
			}
			clsDescs.put(jprime.Traffic.Traffic.class, cls);
			clsDescs.put(jprime.Traffic.TrafficReplica.class, cls);
		}

		//*************************
		//for type TrafficType[BaseEntity]
		//*************************
		{
			ClsDesc cls = new ClsDesc("TrafficType", jprime.ModelNode.class, clsDescs.get(jprime.ModelNode.class));
			for(Entry<Integer, VarDesc> e : cls.super_desc.vars.entrySet()) {
				cls.vars.put(e.getKey(), e.getValue());
			}
			new VarDesc(traffic_type_seed(), "traffic_type_seed", "traffic type's seed", "0", false, false, varTypes.get(traffic_type_seed()), cls);
			new VarDesc(community_ids(), "community_ids", "A comma separated list of the ids of the communities in this partition", "[]", false, false, varTypes.get(community_ids()), cls);
			clsDescs.put(jprime.TrafficType.TrafficType.class, cls);
			clsDescs.put(jprime.TrafficType.TrafficTypeReplica.class, cls);
		}

		//*************************
		//for type ProtocolGraph[BaseEntity]
		//*************************
		{
			ClsDesc cls = new ClsDesc("ProtocolGraph", jprime.ModelNode.class, clsDescs.get(jprime.ModelNode.class));
			for(Entry<Integer, VarDesc> e : cls.super_desc.vars.entrySet()) {
				cls.vars.put(e.getKey(), e.getValue());
			}
			new VarDesc(automatic_protocol_session_creation(), "automatic_protocol_session_creation", "whether protocol sessions should be created on demand", "true", false, false, varTypes.get(automatic_protocol_session_creation()), cls);
			clsDescs.put(jprime.ProtocolGraph.ProtocolGraph.class, cls);
			clsDescs.put(jprime.ProtocolGraph.ProtocolGraphReplica.class, cls);
		}

		//*************************
		//for type RouteTable[BaseEntity]
		//*************************
		{
			ClsDesc cls = new ClsDesc("RouteTable", jprime.ModelNode.class, clsDescs.get(jprime.ModelNode.class));
			for(Entry<Integer, VarDesc> e : cls.super_desc.vars.entrySet()) {
				cls.vars.put(e.getKey(), e.getValue());
			}
			new VarDesc(edge_ifaces(), "edge_ifaces", "A comma separated list of edge ifaces", "[]", false, false, varTypes.get(edge_ifaces()), cls);
			clsDescs.put(jprime.RouteTable.RouteTable.class, cls);
			clsDescs.put(jprime.RouteTable.RouteTableReplica.class, cls);
		}

		//*************************
		//for type RoutingSphere[BaseEntity]
		//*************************
		{
			ClsDesc cls = new ClsDesc("RoutingSphere", jprime.ModelNode.class, clsDescs.get(jprime.ModelNode.class));
			for(Entry<Integer, VarDesc> e : cls.super_desc.vars.entrySet()) {
				cls.vars.put(e.getKey(), e.getValue());
			}
			new VarDesc(nix_vec_cache_size(), "nix_vec_cache_size", "The maximum number of nix vector cache entries for this sphere.", "1000", true, false, varTypes.get(nix_vec_cache_size()), cls);
			new VarDesc(local_dst_cache_size(), "local_dst_cache_size", "The maximum number of local dst cache entries for this sphere.", "1000", true, false, varTypes.get(local_dst_cache_size()), cls);
			clsDescs.put(jprime.RoutingSphere.RoutingSphere.class, cls);
			clsDescs.put(jprime.RoutingSphere.RoutingSphereReplica.class, cls);
		}

		//*************************
		//for type PlaceHolder[BaseEntity]
		//*************************
		{
			ClsDesc cls = new ClsDesc("PlaceHolder", jprime.ModelNode.class, clsDescs.get(jprime.ModelNode.class));
			for(Entry<Integer, VarDesc> e : cls.super_desc.vars.entrySet()) {
				cls.vars.put(e.getKey(), e.getValue());
			}
			clsDescs.put(jprime.PlaceHolder.PlaceHolder.class, cls);
			clsDescs.put(jprime.PlaceHolder.PlaceHolderReplica.class, cls);
		}

		//*************************
		//for type Alias[BaseEntity]
		//*************************
		{
			ClsDesc cls = new ClsDesc("Alias", jprime.ModelNode.class, clsDescs.get(jprime.ModelNode.class));
			for(Entry<Integer, VarDesc> e : cls.super_desc.vars.entrySet()) {
				cls.vars.put(e.getKey(), e.getValue());
			}
			new VarDesc(alias_path(), "alias_path", "the relative path to the node to which this alias points", "", true, false, varTypes.get(alias_path()), cls);
			clsDescs.put(jprime.ModelNodeAlias.class, cls);

		}

		//*************************
		//for type ProtocolSession[BaseEntity]
		//*************************
		{
			ClsDesc cls = new ClsDesc("ProtocolSession", jprime.ModelNode.class, clsDescs.get(jprime.ModelNode.class));
			for(Entry<Integer, VarDesc> e : cls.super_desc.vars.entrySet()) {
				cls.vars.put(e.getKey(), e.getValue());
			}
			clsDescs.put(jprime.ProtocolSession.ProtocolSession.class, cls);
			clsDescs.put(jprime.ProtocolSession.ProtocolSessionReplica.class, cls);
		}

		//*************************
		//for type TrafficPortal[EmulationProtocol]
		//*************************
		{
			ClsDesc cls = new ClsDesc("TrafficPortal", jprime.TrafficPortal.TrafficPortal.class, clsDescs.get(jprime.EmulationProtocol.EmulationProtocol.class));
			for(Entry<Integer, VarDesc> e : cls.super_desc.vars.entrySet()) {
				cls.vars.put(e.getKey(), e.getValue());
			}
			new VarDesc(networks(), "networks", "list of ip prefixes that are reachable via this portal", "[]", false, false, varTypes.get(networks()), cls);
			clsDescs.put(jprime.TrafficPortal.TrafficPortal.class, cls);
			clsDescs.put(jprime.TrafficPortal.TrafficPortalReplica.class, cls);
		}

		//*************************
		//for type TAPEmulation[EmulationProtocol]
		//*************************
		{
			ClsDesc cls = new ClsDesc("TAPEmulation", jprime.TAPEmulation.TAPEmulation.class, clsDescs.get(jprime.EmulationProtocol.EmulationProtocol.class));
			for(Entry<Integer, VarDesc> e : cls.super_desc.vars.entrySet()) {
				cls.vars.put(e.getKey(), e.getValue());
			}
			clsDescs.put(jprime.TAPEmulation.TAPEmulation.class, cls);
			clsDescs.put(jprime.TAPEmulation.TAPEmulationReplica.class, cls);
		}

		//*************************
		//for type OpenVPNEmulation[EmulationProtocol]
		//*************************
		{
			ClsDesc cls = new ClsDesc("OpenVPNEmulation", jprime.OpenVPNEmulation.OpenVPNEmulation.class, clsDescs.get(jprime.EmulationProtocol.EmulationProtocol.class));
			for(Entry<Integer, VarDesc> e : cls.super_desc.vars.entrySet()) {
				cls.vars.put(e.getKey(), e.getValue());
			}
			clsDescs.put(jprime.OpenVPNEmulation.OpenVPNEmulation.class, cls);
			clsDescs.put(jprime.OpenVPNEmulation.OpenVPNEmulationReplica.class, cls);
		}

		//*************************
		//for type Interface[BaseInterface]
		//*************************
		{
			ClsDesc cls = new ClsDesc("Interface", jprime.Interface.Interface.class, clsDescs.get(jprime.BaseInterface.BaseInterface.class));
			for(Entry<Integer, VarDesc> e : cls.super_desc.vars.entrySet()) {
				cls.vars.put(e.getKey(), e.getValue());
			}
			new VarDesc(bit_rate(), "bit_rate", "transmit speed", "1e10", true, false, varTypes.get(bit_rate()), cls);
			new VarDesc(latency(), "latency", "transmit latency", "0", true, false, varTypes.get(latency()), cls);
			new VarDesc(jitter_range(), "jitter_range", "jitter range", "0", true, false, varTypes.get(jitter_range()), cls);
			new VarDesc(drop_probability(), "drop_probability", "drop probability", "0", true, true, varTypes.get(drop_probability()), cls);
			new VarDesc(buffer_size(), "buffer_size", "send buffer size", "65536", true, false, varTypes.get(buffer_size()), cls);
			new VarDesc(mtu(), "mtu", "maximum transmission unit", "1500", true, false, varTypes.get(mtu()), cls);
			new VarDesc(queue_type(), "queue_type", "The class to use for the queue.", "DropTailQueue", true, false, varTypes.get(queue_type()), cls);
			new VarDesc(is_on(), "is_on", "is the interface on?", "true", true, true, varTypes.get(is_on()), cls);
			new VarDesc(num_in_packets(), "num_in_packets", "number of packets received", "0", true, true, varTypes.get(num_in_packets()), cls);
			new VarDesc(num_in_bytes(), "num_in_bytes", "number of bytes received", "0", true, true, varTypes.get(num_in_bytes()), cls);
			new VarDesc(packets_in_per_sec(), "packets_in_per_sec", "number of packets per second", "0", false, true, varTypes.get(packets_in_per_sec()), cls);
			new VarDesc(bytes_in_per_sec(), "bytes_in_per_sec", "number of bytes per second", "0", false, true, varTypes.get(bytes_in_per_sec()), cls);
			new VarDesc(num_in_ucast_packets(), "num_in_ucast_packets", "number of unicast packets received", "0", false, false, varTypes.get(num_in_ucast_packets()), cls);
			new VarDesc(num_in_ucast_bytes(), "num_in_ucast_bytes", "number of unicast bytes received", "0", false, false, varTypes.get(num_in_ucast_bytes()), cls);
			new VarDesc(num_out_packets(), "num_out_packets", "number of packets sent", "0", true, true, varTypes.get(num_out_packets()), cls);
			new VarDesc(num_out_bytes(), "num_out_bytes", "number of bytes sent", "0", true, true, varTypes.get(num_out_bytes()), cls);
			new VarDesc(num_out_ucast_packets(), "num_out_ucast_packets", "number of unicast packets sent", "0", false, false, varTypes.get(num_out_ucast_packets()), cls);
			new VarDesc(num_out_ucast_bytes(), "num_out_ucast_bytes", "number of unicast bytes sent", "0", false, false, varTypes.get(num_out_ucast_bytes()), cls);
			new VarDesc(packets_out_per_sec(), "packets_out_per_sec", "number of packets per second", "0", false, true, varTypes.get(packets_out_per_sec()), cls);
			new VarDesc(bytes_out_per_sec(), "bytes_out_per_sec", "number of bytes per second", "0", false, true, varTypes.get(bytes_out_per_sec()), cls);
			new VarDesc(queue_size(), "queue_size", "quesize", "0", true, true, varTypes.get(queue_size()), cls);
			clsDescs.put(jprime.Interface.Interface.class, cls);
			clsDescs.put(jprime.Interface.InterfaceReplica.class, cls);
		}

		//*************************
		//for type GhostInterface[BaseInterface]
		//*************************
		{
			ClsDesc cls = new ClsDesc("GhostInterface", jprime.GhostInterface.GhostInterface.class, clsDescs.get(jprime.BaseInterface.BaseInterface.class));
			for(Entry<Integer, VarDesc> e : cls.super_desc.vars.entrySet()) {
				cls.vars.put(e.getKey(), e.getValue());
			}
			new VarDesc(real_uid(), "real_uid", "the uid of the remote node", "0", true, false, varTypes.get(real_uid()), cls);
			new VarDesc(community_id(), "community_id", "the id of the remote community", "0", true, false, varTypes.get(community_id()), cls);
			clsDescs.put(jprime.GhostInterface.GhostInterface.class, cls);
			clsDescs.put(jprime.GhostInterface.GhostInterfaceReplica.class, cls);
		}

		//*************************
		//for type FluidQueue[NicQueue]
		//*************************
		{
			ClsDesc cls = new ClsDesc("FluidQueue", jprime.FluidQueue.FluidQueue.class, clsDescs.get(jprime.NicQueue.NicQueue.class));
			for(Entry<Integer, VarDesc> e : cls.super_desc.vars.entrySet()) {
				cls.vars.put(e.getKey(), e.getValue());
			}
			new VarDesc(queue_type(), "queue_type", "The type of the queue, must be red or droptail", "red", false, false, varTypes.get(queue_type()), cls);
			new VarDesc(is_red(), "is_red", "Whether this queue is a RED queue, rather than a drop-tail", "true", false, false, varTypes.get(is_red()), cls);
			new VarDesc(weight(), "weight", "The parameter used by RED queue to estimate the average queue length, must be between 0 and 1", "0.0001", false, false, varTypes.get(weight()), cls);
			new VarDesc(qmin(), "qmin", "the min threshold (in bytes) for calculating packet drop probability.", "0", false, false, varTypes.get(qmin()), cls);
			new VarDesc(qmax(), "qmax", "the max threshold (in bytes) for calculating packet drop probability.", "0", false, false, varTypes.get(qmax()), cls);
			new VarDesc(pmax(), "pmax", "Parameter to calculate packet drop probability, must be in the range (0,1].", "0.2", false, false, varTypes.get(pmax()), cls);
			new VarDesc(wait_opt(), "wait_opt", "This RED option is used to avoid marking/dropping two packets in a row", "true", false, false, varTypes.get(wait_opt()), cls);
			new VarDesc(mean_pktsiz(), "mean_pktsiz", "Mean packet size in bytes used by RED to compute the average queue length, must be positive.", "500", false, false, varTypes.get(mean_pktsiz()), cls);
			clsDescs.put(jprime.FluidQueue.FluidQueue.class, cls);
			clsDescs.put(jprime.FluidQueue.FluidQueueReplica.class, cls);
		}

		//*************************
		//for type DropTailQueue[NicQueue]
		//*************************
		{
			ClsDesc cls = new ClsDesc("DropTailQueue", jprime.DropTailQueue.DropTailQueue.class, clsDescs.get(jprime.NicQueue.NicQueue.class));
			for(Entry<Integer, VarDesc> e : cls.super_desc.vars.entrySet()) {
				cls.vars.put(e.getKey(), e.getValue());
			}
			clsDescs.put(jprime.DropTailQueue.DropTailQueue.class, cls);
			clsDescs.put(jprime.DropTailQueue.DropTailQueueReplica.class, cls);
		}

		//*************************
		//for type RedQueue[NicQueue]
		//*************************
		{
			ClsDesc cls = new ClsDesc("RedQueue", jprime.RedQueue.RedQueue.class, clsDescs.get(jprime.NicQueue.NicQueue.class));
			for(Entry<Integer, VarDesc> e : cls.super_desc.vars.entrySet()) {
				cls.vars.put(e.getKey(), e.getValue());
			}
			new VarDesc(weight(), "weight", "weight used by the AQM policy to calculate average queue length for each packet arrival", "0", true, false, varTypes.get(weight()), cls);
			new VarDesc(qmin(), "qmin", "the min threshold (in bytes) for calculating packet drop probability", "0", true, false, varTypes.get(qmin()), cls);
			new VarDesc(qmax(), "qmax", "the max threshold (in bytes) for calculating packet drop probability", "0", true, false, varTypes.get(qmax()), cls);
			new VarDesc(qcap(), "qcap", "qcap for calculating packet drop probability", "0", true, false, varTypes.get(qcap()), cls);
			new VarDesc(pmax(), "pmax", "pmax for calculating packet drop probability", "0.2", true, false, varTypes.get(pmax()), cls);
			new VarDesc(wait_opt(), "wait_opt", "an option to avoid marking/dropping two packets in a row", "true", true, false, varTypes.get(wait_opt()), cls);
			new VarDesc(mean_pktsiz(), "mean_pktsiz", "mean packet size in bytes", "500", true, false, varTypes.get(mean_pktsiz()), cls);
			clsDescs.put(jprime.RedQueue.RedQueue.class, cls);
			clsDescs.put(jprime.RedQueue.RedQueueReplica.class, cls);
		}

		//*************************
		//for type VizAggregate[Aggregate]
		//*************************
		{
			ClsDesc cls = new ClsDesc("VizAggregate", jprime.VizAggregate.VizAggregate.class, clsDescs.get(jprime.Aggregate.Aggregate.class));
			for(Entry<Integer, VarDesc> e : cls.super_desc.vars.entrySet()) {
				cls.vars.put(e.getKey(), e.getValue());
			}
			clsDescs.put(jprime.VizAggregate.VizAggregate.class, cls);
			clsDescs.put(jprime.VizAggregate.VizAggregateReplica.class, cls);
		}

		//*************************
		//for type FluidTraffic[TrafficType]
		//*************************
		{
			ClsDesc cls = new ClsDesc("FluidTraffic", jprime.FluidTraffic.FluidTraffic.class, clsDescs.get(jprime.TrafficType.TrafficType.class));
			for(Entry<Integer, VarDesc> e : cls.super_desc.vars.entrySet()) {
				cls.vars.put(e.getKey(), e.getValue());
			}
			new VarDesc(protocol_type(), "protocol_type", "The type of this fluid class.", "tcp_reno", false, false, varTypes.get(protocol_type()), cls);
			new VarDesc(srcs(), "srcs", "XXX", "", false, false, varTypes.get(srcs()), cls);
			new VarDesc(dsts(), "dsts", "XXX", "", false, false, varTypes.get(dsts()), cls);
			new VarDesc(off_time(), "off_time", "1/lamda, which is the expected value of the exponential distribution.", "0.01", false, false, varTypes.get(off_time()), cls);
			new VarDesc(mapping(), "mapping", "The method to map the source and destination based on the src and dst lists", "all2all", false, false, varTypes.get(mapping()), cls);
			new VarDesc(nflows(), "nflows", "The average number of homogeneous sessions in this fluid flow.", "10", false, false, varTypes.get(nflows()), cls);
			new VarDesc(pktsiz(), "pktsiz", "Mean packet size in Bytes.", "1000.0", false, false, varTypes.get(pktsiz()), cls);
			new VarDesc(hurst(), "hurst", "The hurst parameter indicates the presence of LRD (Long-Range Dependence).", "0.75", false, false, varTypes.get(hurst()), cls);
			new VarDesc(md(), "md", "TCP congestion window multiplicative decrease factor.", "1.5", false, false, varTypes.get(md()), cls);
			new VarDesc(wndmax(), "wndmax", "TCP send window maximum size in packets", "128", false, false, varTypes.get(wndmax()), cls);
			new VarDesc(sendrate(), "sendrate", "UDP send rate in Byte per second.", "0", false, false, varTypes.get(sendrate()), cls);
			new VarDesc(start(), "start", "The start time of the flow", "0", false, false, varTypes.get(start()), cls);
			new VarDesc(stop(), "stop", "The stop time of the flow", "1000", false, false, varTypes.get(stop()), cls);
			new VarDesc(monfn(), "monfn", "The name of monitor file.", " ", false, false, varTypes.get(monfn()), cls);
			new VarDesc(monres(), "monres", "Recording interval", "0.001", false, false, varTypes.get(monres()), cls);
			clsDescs.put(jprime.FluidTraffic.FluidTraffic.class, cls);
			clsDescs.put(jprime.FluidTraffic.FluidTrafficReplica.class, cls);
		}

		//*************************
		//for type DynamicTrafficType[TrafficType]
		//*************************
		{
			ClsDesc cls = new ClsDesc("DynamicTrafficType", jprime.DynamicTrafficType.DynamicTrafficType.class, clsDescs.get(jprime.TrafficType.TrafficType.class));
			for(Entry<Integer, VarDesc> e : cls.super_desc.vars.entrySet()) {
				cls.vars.put(e.getKey(), e.getValue());
			}
			clsDescs.put(jprime.DynamicTrafficType.DynamicTrafficType.class, cls);
			clsDescs.put(jprime.DynamicTrafficType.DynamicTrafficTypeReplica.class, cls);
		}

		//*************************
		//for type StaticTrafficType[TrafficType]
		//*************************
		{
			ClsDesc cls = new ClsDesc("StaticTrafficType", jprime.StaticTrafficType.StaticTrafficType.class, clsDescs.get(jprime.TrafficType.TrafficType.class));
			for(Entry<Integer, VarDesc> e : cls.super_desc.vars.entrySet()) {
				cls.vars.put(e.getKey(), e.getValue());
			}
			new VarDesc(srcs(), "srcs", "XXX", "", false, false, varTypes.get(srcs()), cls);
			new VarDesc(dsts(), "dsts", "XXX", "", false, false, varTypes.get(dsts()), cls);
			new VarDesc(dst_ips(), "dst_ips", "a list of emu/real nodes ips", "[]", false, false, varTypes.get(dst_ips()), cls);
			new VarDesc(start_time(), "start_time", "Time before the traffic starts (default: 0).", "0", false, false, varTypes.get(start_time()), cls);
			new VarDesc(interval(), "interval", "The time between sending each traffic (default: 0.1). ", "0.1", false, false, varTypes.get(interval()), cls);
			new VarDesc(interval_exponential(), "interval_exponential", "Whether to use exponential interval (default: false).", "false", false, false, varTypes.get(interval_exponential()), cls);
			new VarDesc(mapping(), "mapping", "The method to map the source and destination based on the src and dst lists", "all2all", false, false, varTypes.get(mapping()), cls);
			clsDescs.put(jprime.StaticTrafficType.StaticTrafficType.class, cls);
			clsDescs.put(jprime.StaticTrafficType.StaticTrafficTypeReplica.class, cls);
		}

		//*************************
		//for type Host[ProtocolGraph]
		//*************************
		{
			ClsDesc cls = new ClsDesc("Host", jprime.Host.Host.class, clsDescs.get(jprime.ProtocolGraph.ProtocolGraph.class));
			for(Entry<Integer, VarDesc> e : cls.super_desc.vars.entrySet()) {
				cls.vars.put(e.getKey(), e.getValue());
			}
			new VarDesc(host_seed(), "host_seed", "host's seed", "0", false, false, varTypes.get(host_seed()), cls);
			new VarDesc(time_skew(), "time_skew", "host's time skew (ratio of host time over absolute time)", "1.0", false, false, varTypes.get(time_skew()), cls);
			new VarDesc(time_offset(), "time_offset", "host's time offset (relative to absolute time zero)", "0.0", false, false, varTypes.get(time_offset()), cls);
			new VarDesc(traffic_intensity(), "traffic_intensity", "max  [ for each nic: (bits_out/bit_rate) ]", "0", true, true, varTypes.get(traffic_intensity()), cls);
			clsDescs.put(jprime.Host.Host.class, cls);
			clsDescs.put(jprime.Host.HostReplica.class, cls);
		}

		//*************************
		//for type GhostRoutingSphere[RoutingSphere]
		//*************************
		{
			ClsDesc cls = new ClsDesc("GhostRoutingSphere", jprime.GhostRoutingSphere.GhostRoutingSphere.class, clsDescs.get(jprime.RoutingSphere.RoutingSphere.class));
			for(Entry<Integer, VarDesc> e : cls.super_desc.vars.entrySet()) {
				cls.vars.put(e.getKey(), e.getValue());
			}
			new VarDesc(community_id(), "community_id", "the id of the community where the real sphere is", "0", true, false, varTypes.get(community_id()), cls);
			clsDescs.put(jprime.GhostRoutingSphere.GhostRoutingSphere.class, cls);
			clsDescs.put(jprime.GhostRoutingSphere.GhostRoutingSphereReplica.class, cls);
		}

		//*************************
		//for type CNFTransport[ProtocolSession]
		//*************************
		{
			ClsDesc cls = new ClsDesc("CNFTransport", jprime.CNFTransport.CNFTransport.class, clsDescs.get(jprime.ProtocolSession.ProtocolSession.class));
			for(Entry<Integer, VarDesc> e : cls.super_desc.vars.entrySet()) {
				cls.vars.put(e.getKey(), e.getValue());
			}
			new VarDesc(bytes_received(), "bytes_received", "Number of bytes received so far from all sessions", "0", false, false, varTypes.get(bytes_received()), cls);
			new VarDesc(requests_received(), "requests_received", "Number of requests received so far from all sessions", "0", false, false, varTypes.get(requests_received()), cls);
			new VarDesc(bytes_sent(), "bytes_sent", "Number of bytes sent so far from all sessions", "0", false, false, varTypes.get(bytes_sent()), cls);
			new VarDesc(cnf_routers(), "cnf_routers", "a list of RIDS of other routers with cnf transports installed", "[]", false, false, varTypes.get(cnf_routers()), cls);
			new VarDesc(is_controller(), "is_controller", "Whether this is a controller", "false", false, false, varTypes.get(is_controller()), cls);
			clsDescs.put(jprime.CNFTransport.CNFTransport.class, cls);
			clsDescs.put(jprime.CNFTransport.CNFTransportReplica.class, cls);
		}

		//*************************
		//for type STCPMaster[ProtocolSession]
		//*************************
		{
			ClsDesc cls = new ClsDesc("STCPMaster", jprime.STCPMaster.STCPMaster.class, clsDescs.get(jprime.ProtocolSession.ProtocolSession.class));
			for(Entry<Integer, VarDesc> e : cls.super_desc.vars.entrySet()) {
				cls.vars.put(e.getKey(), e.getValue());
			}
			new VarDesc(max_datagram_size(), "max_datagram_size", "maximum datagram size.", "1470", false, false, varTypes.get(max_datagram_size()), cls);
			clsDescs.put(jprime.STCPMaster.STCPMaster.class, cls);
			clsDescs.put(jprime.STCPMaster.STCPMasterReplica.class, cls);
		}

		//*************************
		//for type UDPMaster[ProtocolSession]
		//*************************
		{
			ClsDesc cls = new ClsDesc("UDPMaster", jprime.UDPMaster.UDPMaster.class, clsDescs.get(jprime.ProtocolSession.ProtocolSession.class));
			for(Entry<Integer, VarDesc> e : cls.super_desc.vars.entrySet()) {
				cls.vars.put(e.getKey(), e.getValue());
			}
			new VarDesc(max_datagram_size(), "max_datagram_size", "maximum datagram size.", "1470", false, false, varTypes.get(max_datagram_size()), cls);
			clsDescs.put(jprime.UDPMaster.UDPMaster.class, cls);
			clsDescs.put(jprime.UDPMaster.UDPMasterReplica.class, cls);
		}

		//*************************
		//for type SymbioSimAppProt[ProtocolSession]
		//*************************
		{
			ClsDesc cls = new ClsDesc("SymbioSimAppProt", jprime.SymbioSimAppProt.SymbioSimAppProt.class, clsDescs.get(jprime.ProtocolSession.ProtocolSession.class));
			for(Entry<Integer, VarDesc> e : cls.super_desc.vars.entrySet()) {
				cls.vars.put(e.getKey(), e.getValue());
			}
			clsDescs.put(jprime.SymbioSimAppProt.SymbioSimAppProt.class, cls);
			clsDescs.put(jprime.SymbioSimAppProt.SymbioSimAppProtReplica.class, cls);
		}

		//*************************
		//for type TCPMaster[ProtocolSession]
		//*************************
		{
			ClsDesc cls = new ClsDesc("TCPMaster", jprime.TCPMaster.TCPMaster.class, clsDescs.get(jprime.ProtocolSession.ProtocolSession.class));
			for(Entry<Integer, VarDesc> e : cls.super_desc.vars.entrySet()) {
				cls.vars.put(e.getKey(), e.getValue());
			}
			new VarDesc(tcpCA(), "tcpCA", "Congestion control algorithm.", "bic", false, false, varTypes.get(tcpCA()), cls);
			new VarDesc(mss(), "mss", "maximum segment size in bytes.", "1400", false, false, varTypes.get(mss()), cls);
			new VarDesc(sndWndSize(), "sndWndSize", "maximum sending window size of TCP protocol in bytes.", "1000000000", false, false, varTypes.get(sndWndSize()), cls);
			new VarDesc(sndBufSize(), "sndBufSize", "maximum size of the buffer for sending bytes.", "2000000000", false, false, varTypes.get(sndBufSize()), cls);
			new VarDesc(rcvWndSize(), "rcvWndSize", "maximum receiving window size of TCP protocol in bytes.", "1000000000", false, false, varTypes.get(rcvWndSize()), cls);
			new VarDesc(samplingInterval(), "samplingInterval", "Interval between consecutive sampling of TCP variables.", "10000", false, false, varTypes.get(samplingInterval()), cls);
			new VarDesc(iss(), "iss", "Initial sequence number of TCP protocol.", "1", false, false, varTypes.get(iss()), cls);
			clsDescs.put(jprime.TCPMaster.TCPMaster.class, cls);
			clsDescs.put(jprime.TCPMaster.TCPMasterReplica.class, cls);
		}

		//*************************
		//for type ProbeSession[ProtocolSession]
		//*************************
		{
			ClsDesc cls = new ClsDesc("ProbeSession", jprime.ProbeSession.ProbeSession.class, clsDescs.get(jprime.ProtocolSession.ProtocolSession.class));
			for(Entry<Integer, VarDesc> e : cls.super_desc.vars.entrySet()) {
				cls.vars.put(e.getKey(), e.getValue());
			}
			clsDescs.put(jprime.ProbeSession.ProbeSession.class, cls);
			clsDescs.put(jprime.ProbeSession.ProbeSessionReplica.class, cls);
		}

		//*************************
		//for type IPv4Session[ProtocolSession]
		//*************************
		{
			ClsDesc cls = new ClsDesc("IPv4Session", jprime.IPv4Session.IPv4Session.class, clsDescs.get(jprime.ProtocolSession.ProtocolSession.class));
			for(Entry<Integer, VarDesc> e : cls.super_desc.vars.entrySet()) {
				cls.vars.put(e.getKey(), e.getValue());
			}
			clsDescs.put(jprime.IPv4Session.IPv4Session.class, cls);
			clsDescs.put(jprime.IPv4Session.IPv4SessionReplica.class, cls);
		}

		//*************************
		//for type ApplicationSession[ProtocolSession]
		//*************************
		{
			ClsDesc cls = new ClsDesc("ApplicationSession", jprime.ApplicationSession.ApplicationSession.class, clsDescs.get(jprime.ProtocolSession.ProtocolSession.class));
			for(Entry<Integer, VarDesc> e : cls.super_desc.vars.entrySet()) {
				cls.vars.put(e.getKey(), e.getValue());
			}
			clsDescs.put(jprime.ApplicationSession.ApplicationSession.class, cls);
			clsDescs.put(jprime.ApplicationSession.ApplicationSessionReplica.class, cls);
		}

		//*************************
		//for type RoutingProtocol[ProtocolSession]
		//*************************
		{
			ClsDesc cls = new ClsDesc("RoutingProtocol", jprime.RoutingProtocol.RoutingProtocol.class, clsDescs.get(jprime.ProtocolSession.ProtocolSession.class));
			for(Entry<Integer, VarDesc> e : cls.super_desc.vars.entrySet()) {
				cls.vars.put(e.getKey(), e.getValue());
			}
			clsDescs.put(jprime.RoutingProtocol.RoutingProtocol.class, cls);
			clsDescs.put(jprime.RoutingProtocol.RoutingProtocolReplica.class, cls);
		}

		//*************************
		//for type DistributedTrafficType[DynamicTrafficType]
		//*************************
		{
			ClsDesc cls = new ClsDesc("DistributedTrafficType", jprime.DistributedTrafficType.DistributedTrafficType.class, clsDescs.get(jprime.DynamicTrafficType.DynamicTrafficType.class));
			for(Entry<Integer, VarDesc> e : cls.super_desc.vars.entrySet()) {
				cls.vars.put(e.getKey(), e.getValue());
			}
			clsDescs.put(jprime.DistributedTrafficType.DistributedTrafficType.class, cls);
			clsDescs.put(jprime.DistributedTrafficType.DistributedTrafficTypeReplica.class, cls);
		}

		//*************************
		//for type CentralizedTrafficType[DynamicTrafficType]
		//*************************
		{
			ClsDesc cls = new ClsDesc("CentralizedTrafficType", jprime.CentralizedTrafficType.CentralizedTrafficType.class, clsDescs.get(jprime.DynamicTrafficType.DynamicTrafficType.class));
			for(Entry<Integer, VarDesc> e : cls.super_desc.vars.entrySet()) {
				cls.vars.put(e.getKey(), e.getValue());
			}
			new VarDesc(srcs(), "srcs", "XXX", "", false, false, varTypes.get(srcs()), cls);
			new VarDesc(dsts(), "dsts", "XXX", "", false, false, varTypes.get(dsts()), cls);
			clsDescs.put(jprime.CentralizedTrafficType.CentralizedTrafficType.class, cls);
			clsDescs.put(jprime.CentralizedTrafficType.CentralizedTrafficTypeReplica.class, cls);
		}

		//*************************
		//for type CNFTraffic[StaticTrafficType]
		//*************************
		{
			ClsDesc cls = new ClsDesc("CNFTraffic", jprime.CNFTraffic.CNFTraffic.class, clsDescs.get(jprime.StaticTrafficType.StaticTrafficType.class));
			for(Entry<Integer, VarDesc> e : cls.super_desc.vars.entrySet()) {
				cls.vars.put(e.getKey(), e.getValue());
			}
			new VarDesc(dst_port(), "dst_port", "The destination port for an CNF connection.", "1000", false, false, varTypes.get(dst_port()), cls);
			new VarDesc(content_id(), "content_id", "The contenet id request to the controller.", "0", false, false, varTypes.get(content_id()), cls);
			new VarDesc(number_of_contents(), "number_of_contents", "The total number of contents.", "1", false, false, varTypes.get(number_of_contents()), cls);
			new VarDesc(number_of_requests(), "number_of_requests", "The total number of requests.", "1", false, false, varTypes.get(number_of_requests()), cls);
			clsDescs.put(jprime.CNFTraffic.CNFTraffic.class, cls);
			clsDescs.put(jprime.CNFTraffic.CNFTrafficReplica.class, cls);
		}

		//*************************
		//for type UDPTraffic[StaticTrafficType]
		//*************************
		{
			ClsDesc cls = new ClsDesc("UDPTraffic", jprime.UDPTraffic.UDPTraffic.class, clsDescs.get(jprime.StaticTrafficType.StaticTrafficType.class));
			for(Entry<Integer, VarDesc> e : cls.super_desc.vars.entrySet()) {
				cls.vars.put(e.getKey(), e.getValue());
			}
			new VarDesc(dstPort(), "dstPort", "The destination port for an CBR connection.", "5001", false, false, varTypes.get(dstPort()), cls);
			new VarDesc(bytesToSendEachInterval(), "bytesToSendEachInterval", "The number of bytes to send each time interval.", "1000000", false, false, varTypes.get(bytesToSendEachInterval()), cls);
			new VarDesc(count(), "count", "Number of times the server will send 'bytesToSendEachInterval' to client.", "10", false, false, varTypes.get(count()), cls);
			new VarDesc(toEmulated(), "toEmulated", "Whether this host will send packets to a simulated or emulated entity).", "false", false, false, varTypes.get(toEmulated()), cls);
			clsDescs.put(jprime.UDPTraffic.UDPTraffic.class, cls);
			clsDescs.put(jprime.UDPTraffic.UDPTrafficReplica.class, cls);
		}

		//*************************
		//for type PPBPTraffic[StaticTrafficType]
		//*************************
		{
			ClsDesc cls = new ClsDesc("PPBPTraffic", jprime.PPBPTraffic.PPBPTraffic.class, clsDescs.get(jprime.StaticTrafficType.StaticTrafficType.class));
			for(Entry<Integer, VarDesc> e : cls.super_desc.vars.entrySet()) {
				cls.vars.put(e.getKey(), e.getValue());
			}
			new VarDesc(dst_port(), "dst_port", "The destination port for an CBR connection.", "5001", false, false, varTypes.get(dst_port()), cls);
			new VarDesc(bytes_to_send_per_interval(), "bytes_to_send_per_interval", "The number of bytes to send each time interval.", "1000000", false, false, varTypes.get(bytes_to_send_per_interval()), cls);
			new VarDesc(avg_sessions(), "avg_sessions", "Average number of sessions (default: 0 means unset).", "1", false, false, varTypes.get(avg_sessions()), cls);
			new VarDesc(hurst(), "hurst", "The hurst parameter indicates the presence of LRD (Long-Range Dependence).", "0.8", false, false, varTypes.get(hurst()), cls);
			new VarDesc(stop(), "stop", "The traffic stops starting new traffic flows after stop time.", "1000000", false, false, varTypes.get(stop()), cls);
			clsDescs.put(jprime.PPBPTraffic.PPBPTraffic.class, cls);
			clsDescs.put(jprime.PPBPTraffic.PPBPTrafficReplica.class, cls);
		}

		//*************************
		//for type TCPTraffic[StaticTrafficType]
		//*************************
		{
			ClsDesc cls = new ClsDesc("TCPTraffic", jprime.TCPTraffic.TCPTraffic.class, clsDescs.get(jprime.StaticTrafficType.StaticTrafficType.class));
			for(Entry<Integer, VarDesc> e : cls.super_desc.vars.entrySet()) {
				cls.vars.put(e.getKey(), e.getValue());
			}
			new VarDesc(dst_port(), "dst_port", "The destination port for an HTTP connection.", "80", false, false, varTypes.get(dst_port()), cls);
			new VarDesc(file_size(), "file_size", "The size to request to the server.", "1024000", false, false, varTypes.get(file_size()), cls);
			new VarDesc(file_pareto(), "file_pareto", "Whether the file size are modeled by a concatenation of bounded Weibull and Pareto distributions", "false", false, false, varTypes.get(file_pareto()), cls);
			new VarDesc(to_emulated(), "to_emulated", "Whether this host will request packets from a simulated or emulated entity.", "false", false, false, varTypes.get(to_emulated()), cls);
			new VarDesc(number_of_sessions(), "number_of_sessions", "Number of session.", "1", false, false, varTypes.get(number_of_sessions()), cls);
			new VarDesc(connections_per_session(), "connections_per_session", "Number of connections within a session.", "1", false, false, varTypes.get(connections_per_session()), cls);
			new VarDesc(session_timeout(), "session_timeout", "Session timeout in seconds.", "30.0", false, false, varTypes.get(session_timeout()), cls);
			clsDescs.put(jprime.TCPTraffic.TCPTraffic.class, cls);
			clsDescs.put(jprime.TCPTraffic.TCPTrafficReplica.class, cls);
		}

		//*************************
		//for type ICMPTraffic[StaticTrafficType]
		//*************************
		{
			ClsDesc cls = new ClsDesc("ICMPTraffic", jprime.ICMPTraffic.ICMPTraffic.class, clsDescs.get(jprime.StaticTrafficType.StaticTrafficType.class));
			for(Entry<Integer, VarDesc> e : cls.super_desc.vars.entrySet()) {
				cls.vars.put(e.getKey(), e.getValue());
			}
			new VarDesc(version(), "version", "The version of ICMP protocol.", "4", false, false, varTypes.get(version()), cls);
			clsDescs.put(jprime.ICMPTraffic.ICMPTraffic.class, cls);
			clsDescs.put(jprime.ICMPTraffic.ICMPTrafficReplica.class, cls);
		}

		//*************************
		//for type Router[Host]
		//*************************
		{
			ClsDesc cls = new ClsDesc("Router", jprime.Router.Router.class, clsDescs.get(jprime.Host.Host.class));
			for(Entry<Integer, VarDesc> e : cls.super_desc.vars.entrySet()) {
				cls.vars.put(e.getKey(), e.getValue());
			}
			clsDescs.put(jprime.Router.Router.class, cls);
			clsDescs.put(jprime.Router.RouterReplica.class, cls);
		}

		//*************************
		//for type CNFApplication[ApplicationSession]
		//*************************
		{
			ClsDesc cls = new ClsDesc("CNFApplication", jprime.CNFApplication.CNFApplication.class, clsDescs.get(jprime.ApplicationSession.ApplicationSession.class));
			for(Entry<Integer, VarDesc> e : cls.super_desc.vars.entrySet()) {
				cls.vars.put(e.getKey(), e.getValue());
			}
			new VarDesc(listening_port(), "listening_port", "Listening port for incoming connections.", "80", false, false, varTypes.get(listening_port()), cls);
			new VarDesc(bytes_received(), "bytes_received", "Number of bytes received so far from all sessions", "0", false, false, varTypes.get(bytes_received()), cls);
			new VarDesc(bytes_sent(), "bytes_sent", "Number of bytes sent so far from all sessions", "0", false, false, varTypes.get(bytes_sent()), cls);
			new VarDesc(requests_received(), "requests_received", "Number of requests received so far from all sessions", "0", false, false, varTypes.get(requests_received()), cls);
			new VarDesc(cnf_content_sizes(), "cnf_content_sizes", "a list of content id and their sizes. i.e. [100,1,200,2] --> [cid:100, size:1],[cid:200, size:2]", "[]", false, false, varTypes.get(cnf_content_sizes()), cls);
			clsDescs.put(jprime.CNFApplication.CNFApplication.class, cls);
			clsDescs.put(jprime.CNFApplication.CNFApplicationReplica.class, cls);
		}

		//*************************
		//for type CBR[ApplicationSession]
		//*************************
		{
			ClsDesc cls = new ClsDesc("CBR", jprime.CBR.CBR.class, clsDescs.get(jprime.ApplicationSession.ApplicationSession.class));
			for(Entry<Integer, VarDesc> e : cls.super_desc.vars.entrySet()) {
				cls.vars.put(e.getKey(), e.getValue());
			}
			new VarDesc(listening_port(), "listening_port", "Listening port port UDP datagrams.", "5001", false, false, varTypes.get(listening_port()), cls);
			new VarDesc(total_bytes_received(), "total_bytes_received", "Number of bytes received from all sessions", "0", false, false, varTypes.get(total_bytes_received()), cls);
			new VarDesc(total_bytes_sent(), "total_bytes_sent", "Number of bytes sent from all sessions", "0", false, false, varTypes.get(total_bytes_sent()), cls);
			clsDescs.put(jprime.CBR.CBR.class, cls);
			clsDescs.put(jprime.CBR.CBRReplica.class, cls);
		}

		//*************************
		//for type SwingServer[ApplicationSession]
		//*************************
		{
			ClsDesc cls = new ClsDesc("SwingServer", jprime.SwingServer.SwingServer.class, clsDescs.get(jprime.ApplicationSession.ApplicationSession.class));
			for(Entry<Integer, VarDesc> e : cls.super_desc.vars.entrySet()) {
				cls.vars.put(e.getKey(), e.getValue());
			}
			new VarDesc(listeningPort(), "listeningPort", "Listening port for incoming connections.", "1024", false, false, varTypes.get(listeningPort()), cls);
			new VarDesc(bytesReceived(), "bytesReceived", "Number of bytes received so far from all sessions", "0", false, false, varTypes.get(bytesReceived()), cls);
			new VarDesc(requestsReceived(), "requestsReceived", "Number of requests received so far from all sessions", "0", false, false, varTypes.get(requestsReceived()), cls);
			clsDescs.put(jprime.SwingServer.SwingServer.class, cls);
			clsDescs.put(jprime.SwingServer.SwingServerReplica.class, cls);
		}

		//*************************
		//for type SwingClient[ApplicationSession]
		//*************************
		{
			ClsDesc cls = new ClsDesc("SwingClient", jprime.SwingClient.SwingClient.class, clsDescs.get(jprime.ApplicationSession.ApplicationSession.class));
			for(Entry<Integer, VarDesc> e : cls.super_desc.vars.entrySet()) {
				cls.vars.put(e.getKey(), e.getValue());
			}
			new VarDesc(active_sessions(), "active_sessions", "This is the number of active downloading sessions", "0", false, false, varTypes.get(active_sessions()), cls);
			new VarDesc(bytes_received(), "bytes_received", "Number of bytes received from all sessions", "0", false, false, varTypes.get(bytes_received()), cls);
			clsDescs.put(jprime.SwingClient.SwingClient.class, cls);
			clsDescs.put(jprime.SwingClient.SwingClientReplica.class, cls);
		}

		//*************************
		//for type HTTPClient[ApplicationSession]
		//*************************
		{
			ClsDesc cls = new ClsDesc("HTTPClient", jprime.HTTPClient.HTTPClient.class, clsDescs.get(jprime.ApplicationSession.ApplicationSession.class));
			for(Entry<Integer, VarDesc> e : cls.super_desc.vars.entrySet()) {
				cls.vars.put(e.getKey(), e.getValue());
			}
			new VarDesc(active_sessions(), "active_sessions", "This is the number of active downloading sessions", "0", false, false, varTypes.get(active_sessions()), cls);
			new VarDesc(bytes_received(), "bytes_received", "Number of bytes received from all sessions", "0", false, false, varTypes.get(bytes_received()), cls);
			clsDescs.put(jprime.HTTPClient.HTTPClient.class, cls);
			clsDescs.put(jprime.HTTPClient.HTTPClientReplica.class, cls);
		}

		//*************************
		//for type HTTPServer[ApplicationSession]
		//*************************
		{
			ClsDesc cls = new ClsDesc("HTTPServer", jprime.HTTPServer.HTTPServer.class, clsDescs.get(jprime.ApplicationSession.ApplicationSession.class));
			for(Entry<Integer, VarDesc> e : cls.super_desc.vars.entrySet()) {
				cls.vars.put(e.getKey(), e.getValue());
			}
			new VarDesc(listeningPort(), "listeningPort", "Listening port for incoming connections.", "80", false, false, varTypes.get(listeningPort()), cls);
			new VarDesc(bytesReceived(), "bytesReceived", "Number of bytes received so far from all sessions", "0", false, false, varTypes.get(bytesReceived()), cls);
			new VarDesc(requestsReceived(), "requestsReceived", "Number of requests received so far from all sessions", "0", false, false, varTypes.get(requestsReceived()), cls);
			clsDescs.put(jprime.HTTPServer.HTTPServer.class, cls);
			clsDescs.put(jprime.HTTPServer.HTTPServerReplica.class, cls);
		}

		//*************************
		//for type ICMPv4Session[ApplicationSession]
		//*************************
		{
			ClsDesc cls = new ClsDesc("ICMPv4Session", jprime.ICMPv4Session.ICMPv4Session.class, clsDescs.get(jprime.ApplicationSession.ApplicationSession.class));
			for(Entry<Integer, VarDesc> e : cls.super_desc.vars.entrySet()) {
				cls.vars.put(e.getKey(), e.getValue());
			}
			clsDescs.put(jprime.ICMPv4Session.ICMPv4Session.class, cls);
			clsDescs.put(jprime.ICMPv4Session.ICMPv4SessionReplica.class, cls);
		}

		//*************************
		//for type SwingTCPTraffic[CentralizedTrafficType]
		//*************************
		{
			ClsDesc cls = new ClsDesc("SwingTCPTraffic", jprime.SwingTCPTraffic.SwingTCPTraffic.class, clsDescs.get(jprime.CentralizedTrafficType.CentralizedTrafficType.class));
			for(Entry<Integer, VarDesc> e : cls.super_desc.vars.entrySet()) {
				cls.vars.put(e.getKey(), e.getValue());
			}
			new VarDesc(trace_description(), "trace_description", "The file that indicates the positions of all experical data of the parameters.", " ", false, false, varTypes.get(trace_description()), cls);
			new VarDesc(stretch(), "stretch", "whether to stretch some parameters", "false", false, false, varTypes.get(stretch()), cls);
			new VarDesc(session_timeout(), "session_timeout", "timeout for session (in second)", "300", false, false, varTypes.get(session_timeout()), cls);
			new VarDesc(rre_timeout(), "rre_timeout", "timeout for rre (in second)", "30", false, false, varTypes.get(rre_timeout()), cls);
			clsDescs.put(jprime.SwingTCPTraffic.SwingTCPTraffic.class, cls);
			clsDescs.put(jprime.SwingTCPTraffic.SwingTCPTrafficReplica.class, cls);
		}

		//*************************
		//for type PingTraffic[ICMPTraffic]
		//*************************
		{
			ClsDesc cls = new ClsDesc("PingTraffic", jprime.PingTraffic.PingTraffic.class, clsDescs.get(jprime.ICMPTraffic.ICMPTraffic.class));
			for(Entry<Integer, VarDesc> e : cls.super_desc.vars.entrySet()) {
				cls.vars.put(e.getKey(), e.getValue());
			}
			new VarDesc(payload_size(), "payload_size", "The number of bytes in the payload to be sent (default: 56).", "56", false, false, varTypes.get(payload_size()), cls);
			new VarDesc(count(), "count", "The number of requests to send (default: 1).", "1", false, false, varTypes.get(count()), cls);
			new VarDesc(rtt(), "rtt", "The rtt calculated by the ping traffic", "0", false, false, varTypes.get(rtt()), cls);
			clsDescs.put(jprime.PingTraffic.PingTraffic.class, cls);
			clsDescs.put(jprime.PingTraffic.PingTrafficReplica.class, cls);
		}


		//*************************
		//add entries for aliases
		//*************************
			clsDescs.put(jprime.EmulationProtocol.EmulationProtocolAlias.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.EmulationProtocol.EmulationProtocolAliasReplica.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.Link.LinkAlias.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.Link.LinkAliasReplica.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.BaseInterface.BaseInterfaceAlias.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.BaseInterface.BaseInterfaceAliasReplica.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.Net.NetAlias.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.Net.NetAliasReplica.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.NicQueue.NicQueueAlias.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.NicQueue.NicQueueAliasReplica.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.Aggregate.AggregateAlias.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.Aggregate.AggregateAliasReplica.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.Monitor.MonitorAlias.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.Monitor.MonitorAliasReplica.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.Traffic.TrafficAlias.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.Traffic.TrafficAliasReplica.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.TrafficType.TrafficTypeAlias.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.TrafficType.TrafficTypeAliasReplica.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.ProtocolGraph.ProtocolGraphAlias.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.ProtocolGraph.ProtocolGraphAliasReplica.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.RouteTable.RouteTableAlias.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.RouteTable.RouteTableAliasReplica.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.RoutingSphere.RoutingSphereAlias.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.RoutingSphere.RoutingSphereAliasReplica.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.PlaceHolder.PlaceHolderAlias.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.PlaceHolder.PlaceHolderAliasReplica.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.ProtocolSession.ProtocolSessionAlias.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.ProtocolSession.ProtocolSessionAliasReplica.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.TrafficPortal.TrafficPortalAlias.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.TrafficPortal.TrafficPortalAliasReplica.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.TAPEmulation.TAPEmulationAlias.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.TAPEmulation.TAPEmulationAliasReplica.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.OpenVPNEmulation.OpenVPNEmulationAlias.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.OpenVPNEmulation.OpenVPNEmulationAliasReplica.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.Interface.InterfaceAlias.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.Interface.InterfaceAliasReplica.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.GhostInterface.GhostInterfaceAlias.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.GhostInterface.GhostInterfaceAliasReplica.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.FluidQueue.FluidQueueAlias.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.FluidQueue.FluidQueueAliasReplica.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.DropTailQueue.DropTailQueueAlias.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.DropTailQueue.DropTailQueueAliasReplica.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.RedQueue.RedQueueAlias.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.RedQueue.RedQueueAliasReplica.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.VizAggregate.VizAggregateAlias.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.VizAggregate.VizAggregateAliasReplica.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.FluidTraffic.FluidTrafficAlias.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.FluidTraffic.FluidTrafficAliasReplica.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.DynamicTrafficType.DynamicTrafficTypeAlias.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.DynamicTrafficType.DynamicTrafficTypeAliasReplica.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.StaticTrafficType.StaticTrafficTypeAlias.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.StaticTrafficType.StaticTrafficTypeAliasReplica.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.Host.HostAlias.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.Host.HostAliasReplica.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.GhostRoutingSphere.GhostRoutingSphereAlias.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.GhostRoutingSphere.GhostRoutingSphereAliasReplica.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.CNFTransport.CNFTransportAlias.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.CNFTransport.CNFTransportAliasReplica.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.STCPMaster.STCPMasterAlias.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.STCPMaster.STCPMasterAliasReplica.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.UDPMaster.UDPMasterAlias.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.UDPMaster.UDPMasterAliasReplica.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.SymbioSimAppProt.SymbioSimAppProtAlias.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.SymbioSimAppProt.SymbioSimAppProtAliasReplica.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.TCPMaster.TCPMasterAlias.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.TCPMaster.TCPMasterAliasReplica.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.ProbeSession.ProbeSessionAlias.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.ProbeSession.ProbeSessionAliasReplica.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.IPv4Session.IPv4SessionAlias.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.IPv4Session.IPv4SessionAliasReplica.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.ApplicationSession.ApplicationSessionAlias.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.ApplicationSession.ApplicationSessionAliasReplica.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.RoutingProtocol.RoutingProtocolAlias.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.RoutingProtocol.RoutingProtocolAliasReplica.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.DistributedTrafficType.DistributedTrafficTypeAlias.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.DistributedTrafficType.DistributedTrafficTypeAliasReplica.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.CentralizedTrafficType.CentralizedTrafficTypeAlias.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.CentralizedTrafficType.CentralizedTrafficTypeAliasReplica.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.CNFTraffic.CNFTrafficAlias.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.CNFTraffic.CNFTrafficAliasReplica.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.UDPTraffic.UDPTrafficAlias.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.UDPTraffic.UDPTrafficAliasReplica.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.PPBPTraffic.PPBPTrafficAlias.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.PPBPTraffic.PPBPTrafficAliasReplica.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.TCPTraffic.TCPTrafficAlias.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.TCPTraffic.TCPTrafficAliasReplica.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.ICMPTraffic.ICMPTrafficAlias.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.ICMPTraffic.ICMPTrafficAliasReplica.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.Router.RouterAlias.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.Router.RouterAliasReplica.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.CNFApplication.CNFApplicationAlias.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.CNFApplication.CNFApplicationAliasReplica.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.CBR.CBRAlias.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.CBR.CBRAliasReplica.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.SwingServer.SwingServerAlias.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.SwingServer.SwingServerAliasReplica.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.SwingClient.SwingClientAlias.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.SwingClient.SwingClientAliasReplica.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.HTTPClient.HTTPClientAlias.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.HTTPClient.HTTPClientAliasReplica.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.HTTPServer.HTTPServerAlias.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.HTTPServer.HTTPServerAliasReplica.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.ICMPv4Session.ICMPv4SessionAlias.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.ICMPv4Session.ICMPv4SessionAliasReplica.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.SwingTCPTraffic.SwingTCPTrafficAlias.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.SwingTCPTraffic.SwingTCPTrafficAliasReplica.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.PingTraffic.PingTrafficAlias.class, clsDescs.get(jprime.ModelNodeAlias.class));
			clsDescs.put(jprime.PingTraffic.PingTrafficAliasReplica.class, clsDescs.get(jprime.ModelNodeAlias.class));
	}


	public static ClsDesc getClsDesc(jprime.ModelNode m) {
		return clsDescs.get(m.getClass());
	}
	public static List<Tripple> jythonName2Tripples(String name) {
		if(jythonMap.containsKey(name))
			return jythonMap.get(name);
		return null;
	}
	public static int name2int(String name){
		Integer id = null;
		id = str2id.get(name);
		if(id==null)
			throw new NoSuchElementException("The name \""+name+"\" was not found in the str2id map");
		return id;
	}
	public static Class<? extends ModelNodeVariable > int2type(Integer attr_id){
		Class<? extends ModelNodeVariable > type = null;
		type = varTypes.get(attr_id);
		if(type==null)
			throw new NoSuchElementException("The attr_id \""+attr_id+"\" was not found in the varTypes map");
		return type;
	}
	public static String int2name(int id){
		switch(id) {
			case 1: return _active_sessions;
			case 2: return _aggr_arrival;
			case 3: return _alias_path;
			case 4: return _attached_link;
			case 5: return _automatic_protocol_session_creation;
			case 6: return _avg_sessions;
			case 7: return _avgque;
			case 8: return _bandwidth;
			case 9: return _bit_rate;
			case 10: return _buffer_size;
			case 11: return _bytesReceived;
			case 12: return _bytesToSendEachInterval;
			case 13: return _bytes_in_per_sec;
			case 14: return _bytes_out_per_sec;
			case 15: return _bytes_received;
			case 16: return _bytes_sent;
			case 17: return _bytes_to_send_per_interval;
			case 18: return _cnf_content_ids;
			case 19: return _cnf_content_sizes;
			case 20: return _cnf_routers;
			case 21: return _com_id2link_info;
			case 22: return _community_id;
			case 23: return _community_ids;
			case 24: return _connections_per_session;
			case 25: return _content_id;
			case 26: return _controller_rid;
			case 27: return _count;
			case 28: return _crossing;
			case 29: return _default_ip;
			case 30: return _delay;
			case 31: return _drop_probability;
			case 32: return _dstPort;
			case 33: return _dst_ips;
			case 34: return _dst_port;
			case 35: return _dsts;
			case 36: return _edge_ifaces;
			case 37: return _emu_protocol;
			case 38: return _file_pareto;
			case 39: return _file_size;
			case 40: return _fluid_weight;
			case 41: return _host_seed;
			case 42: return _hurst;
			case 43: return _hybrid_traffic_flows;
			case 44: return _initialized;
			case 45: return _interdrop;
			case 46: return _interval;
			case 47: return _interval_exponential;
			case 48: return _ip2iface;
			case 49: return _ip_address;
			case 50: return _ip_forwarding;
			case 51: return _ip_prefix;
			case 52: return _ip_prefix_len;
			case 53: return _ip_sess;
			case 54: return _is_controller;
			case 55: return _is_on;
			case 56: return _is_red;
			case 57: return _iss;
			case 58: return _jitter_range;
			case 59: return _last_send_time;
			case 60: return _last_update_time;
			case 61: return _last_xmit_time;
			case 62: return _latency;
			case 63: return _listeningPort;
			case 64: return _listening_port;
			case 65: return _local_dst_cache_size;
			case 66: return _loss;
			case 67: return _mac2iface;
			case 68: return _mac_address;
			case 69: return _mapping;
			case 70: return _max;
			case 71: return _max_datagram_size;
			case 72: return _max_queue_delay;
			case 73: return _md;
			case 74: return _mean_pktsiz;
			case 75: return _min;
			case 76: return _monfn;
			case 77: return _monres;
			case 78: return _mss;
			case 79: return _mtu;
			case 80: return _my_edge_ifaces;
			case 81: return _name;
			case 82: return _networks;
			case 83: return _nflows;
			case 84: return _nix_vec_cache_size;
			case 85: return _num_in_bytes;
			case 86: return _num_in_packets;
			case 87: return _num_in_ucast_bytes;
			case 88: return _num_in_ucast_packets;
			case 89: return _num_out_bytes;
			case 90: return _num_out_packets;
			case 91: return _num_out_ucast_bytes;
			case 92: return _num_out_ucast_packets;
			case 93: return _number_of_contents;
			case 94: return _number_of_requests;
			case 95: return _number_of_sessions;
			case 96: return _off_time;
			case 97: return _offset;
			case 98: return _packets_in_per_sec;
			case 99: return _packets_out_per_sec;
			case 100: return _payload_size;
			case 101: return _period;
			case 102: return _pkt_arrival;
			case 103: return _pktsiz;
			case 104: return _pmax;
			case 105: return _pname_map;
			case 106: return _pno_map;
			case 107: return _portal_rules;
			case 108: return _portals;
			case 109: return _prop_delay;
			case 110: return _protocol_list;
			case 111: return _protocol_type;
			case 112: return _qcap;
			case 113: return _qmax;
			case 114: return _qmin;
			case 115: return _queue;
			case 116: return _queue_delay;
			case 117: return _queue_size;
			case 118: return _queue_type;
			case 119: return _rcvWndSize;
			case 120: return _real_uid;
			case 121: return _requestsReceived;
			case 122: return _requests_received;
			case 123: return _rng;
			case 124: return _rng_seed;
			case 125: return _routemap;
			case 126: return _rre_timeout;
			case 127: return _rtt;
			case 128: return _sample_count;
			case 129: return _samplingInterval;
			case 130: return _sendrate;
			case 131: return _session_timeout;
			case 132: return _shadow_avgque;
			case 133: return _shadow_loss;
			case 134: return _shadow_queue;
			case 135: return _shortcut_to;
			case 136: return _size;
			case 137: return _sndBufSize;
			case 138: return _sndWndSize;
			case 139: return _srcs;
			case 140: return _start;
			case 141: return _start_time;
			case 142: return _stop;
			case 143: return _stretch;
			case 144: return _sub_edge_iface_map;
			case 145: return _sub_edge_ifaces;
			case 146: return _sum;
			case 147: return _tcpCA;
			case 148: return _time_offset;
			case 149: return _time_skew;
			case 150: return _tmr_handle;
			case 151: return _tmr_map;
			case 152: return _tmr_queue;
			case 153: return _toEmulated;
			case 154: return _to_aggregate;
			case 155: return _to_emulated;
			case 156: return _to_monitor;
			case 157: return _total_bytes_received;
			case 158: return _total_bytes_sent;
			case 159: return _trace_description;
			case 160: return _traffic_flows;
			case 161: return _traffic_intensity;
			case 162: return _traffic_type_seed;
			case 163: return _tx_queue;
			case 164: return _tx_timer_queue;
			case 165: return _uid;
			case 166: return _vacate_time;
			case 167: return _var_id;
			case 168: return _version;
			case 169: return _wait_opt;
			case 170: return _weight;
			case 171: return _window;
			case 172: return _wndmax;
		}
		throw new NoSuchElementException("The id \""+id+"\" was not found in the id2str map");
	}
}
