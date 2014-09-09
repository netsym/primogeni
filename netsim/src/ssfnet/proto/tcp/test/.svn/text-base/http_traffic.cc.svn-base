/**
 * \file http_traffic.cc
 * \author Miguel Erazo
 * \author Ting Li
 * 
 * Copyright (c) 2011 Florida International University.
 *
 * Permission is hereby granted, free of charge, to any individual or
 * institution obtaining a copy of this software and associated
 * documentation files (the "software"), to use, copy, modify, and
 * distribute without restriction.
 *
 * The software is provided "as is", without warranty of any kind,
 * express or implied, including but not limited to the warranties of
 * merchantability, fitness for a particular purpose and
 * non-infringement.  In no event shall Florida International
 * University be liable for any claim, damages or other liability,
 * whether in an action of contract, tort or otherwise, arising from,
 * out of or in connection with the software or the use or other
 * dealings in the software.
 *
 * This software is developed and maintained by
 *
 *   Modeling and Networking Systems Research Group
 *   School of Computing and Information Sciences
 *   Florida International University
 *   Miami, Florida 33199, USA
 *
 * You can find our research at http://www.primessf.net/.
 */

#include "os/logger.h"
#include "os/ssfnet.h"
#include "os/config_type.h"
#include "os/config_entity.h"
#include "os/traffic.h"
#include "proto/tcp/test/http_traffic.h"
#include "os/protocol_session.h"

namespace prime {
namespace ssfnet {

LOGGING_COMPONENT(TCPTraffic)

HttpStartTrafficEvent::HttpStartTrafficEvent():
		StartTrafficEvent(SSFNetEvent::SSFNET_TRAFFIC_HTTP_START_EVT) {
}
HttpStartTrafficEvent::HttpStartTrafficEvent(UID_t traffic_type_uid, uint32_t id, UID_t h_uid, UID_t d_uid, uint32_t d_ip, VirtualTime st) :
        StartTrafficEvent(SSFNetEvent::SSFNET_TRAFFIC_HTTP_START_EVT, traffic_type_uid, id, h_uid, d_uid, d_ip, st), session_id(1), file_size(0){
}
HttpStartTrafficEvent::HttpStartTrafficEvent(TrafficType* traffic_type, uint32_t id, UID_t h_uid, UID_t d_uid, uint32_t d_ip, VirtualTime st) :
        StartTrafficEvent(SSFNetEvent::SSFNET_TRAFFIC_HTTP_START_EVT, traffic_type, id, h_uid, d_uid, d_ip, st), session_id(1), file_size(0) {
}
HttpStartTrafficEvent::HttpStartTrafficEvent(HttpStartTrafficEvent* evt) :
        StartTrafficEvent(evt), session_id(evt->session_id), file_size(evt->file_size){
}
HttpStartTrafficEvent::HttpStartTrafficEvent(const HttpStartTrafficEvent& evt) :
		StartTrafficEvent(evt), session_id(evt.session_id), file_size(evt.file_size){
}
HttpStartTrafficEvent::~HttpStartTrafficEvent(){

}
prime::ssf::ssf_compact* HttpStartTrafficEvent::pack(){
	prime::ssf::ssf_compact* dp = StartTrafficEvent::pack();
	dp->add_unsigned_int(session_id);
	dp->add_unsigned_long_long(file_size);
	return dp;
}

void HttpStartTrafficEvent::unpack(prime::ssf::ssf_compact* dp){
	StartTrafficEvent::unpack(dp);
	dp->get_unsigned_int(&session_id,1);
	unsigned long long t;
	dp->get_unsigned_long_long(&t,1);
	file_size=t;
}

void HttpStartTrafficEvent::setSessionID(uint32_t s_id){
        session_id=s_id;
}

uint32_t HttpStartTrafficEvent::getSessionID(){
        return session_id;
}

void HttpStartTrafficEvent::setFileSize(uint64_t size){
        file_size=size;
}

uint64_t HttpStartTrafficEvent::getFileSize(){
        return file_size;
}

prime::ssf::Event* HttpStartTrafficEvent::createHttpStartTrafficEvent(prime::ssf::ssf_compact* dp){
	HttpStartTrafficEvent* t_evt = new HttpStartTrafficEvent();
    t_evt->unpack(dp);
    return t_evt;
}

HttpFinishedTrafficEvent::HttpFinishedTrafficEvent():
		FinishedTrafficEvent(SSFNetEvent::SSFNET_TRAFFIC_HTTP_FINISH_EVT){
}

HttpFinishedTrafficEvent::HttpFinishedTrafficEvent(UID_t trafficTypeUID, int target_community_id, uint32_t id, uint32_t s_id, float tm) :
        FinishedTrafficEvent(SSFNetEvent::SSFNET_TRAFFIC_HTTP_FINISH_EVT, trafficTypeUID, target_community_id, id), session_id(s_id), timeout(tm){
}

HttpFinishedTrafficEvent::HttpFinishedTrafficEvent(TrafficType* trafficType, int target_community_id, uint32_t id, uint32_t s_id, float tm) :
        FinishedTrafficEvent(SSFNetEvent::SSFNET_TRAFFIC_HTTP_FINISH_EVT, trafficType, target_community_id, id), session_id(s_id), timeout(tm){
}

HttpFinishedTrafficEvent::HttpFinishedTrafficEvent(HttpFinishedTrafficEvent* evt) :
        FinishedTrafficEvent(evt), session_id(evt->session_id), timeout(evt->timeout){
}

HttpFinishedTrafficEvent::HttpFinishedTrafficEvent(const HttpFinishedTrafficEvent& evt) :
        FinishedTrafficEvent(evt), session_id(evt.session_id), timeout(evt.timeout){
}

HttpFinishedTrafficEvent::~HttpFinishedTrafficEvent(){

}

prime::ssf::ssf_compact* HttpFinishedTrafficEvent::pack(){
	prime::ssf::ssf_compact* dp = FinishedTrafficEvent::pack();
	dp->add_unsigned_int(session_id);
	dp->add_float(timeout);
	return dp;
}

void HttpFinishedTrafficEvent::unpack(prime::ssf::ssf_compact* dp){
	FinishedTrafficEvent::unpack(dp);
	dp->get_unsigned_int(&session_id,1);
	dp->get_float(&timeout);
}

void HttpFinishedTrafficEvent::setSessionID(uint32_t s_id){
	session_id=s_id;
}

uint32_t HttpFinishedTrafficEvent::getSessionID(){
    return session_id;
}

void HttpFinishedTrafficEvent::setTimeout(float tm){
	timeout = tm;
}

float HttpFinishedTrafficEvent::getTimeout(){
    return timeout;
}

prime::ssf::Event* HttpFinishedTrafficEvent::createHttpFinishedTrafficEvent(prime::ssf::ssf_compact* dp){
	HttpFinishedTrafficEvent* t_evt = new HttpFinishedTrafficEvent();
    t_evt->unpack(dp);
    return t_evt;
}


TCPTraffic::TCPTraffic(): traffic_id(0), session_id(1), queue(ActionsComparison(true)) {}

bool TCPTraffic::shouldBeIncludedInCommunity(Community* com){
	return StaticTrafficType::shouldBeIncludedInCommunity(com);
}

void TCPTraffic::init(){
	StaticTrafficType::init();
}

ConfigType* TCPTraffic::getProtocolType() { return HTTPClient::getClassConfigType(); }

void TCPTraffic::getNextEvent(StartTrafficEvent*& traffics_to_start,
			//the tm will send this to the correct host at the correct time
			UpdateTrafficTypeEvent*& update_vent,
			//if there needs to be an update event the TM will deliver it to all concerned traffic types on remote TMs
			bool& wrap_up,
			//if this is true the TM will wrap up the traffic type and remove it from its active list
			VirtualTime& recall_at
			//when should the traffic type be recalled
			//-- may need to update its current recall time. if its zero don't change it
			){
	VirtualTime now = prime::ssf::now();
	LOG_DEBUG("getNextEvent called at " << now.second() << " traffic id=" << traffic_id << endl);

	//This is the first time this method is called, populate the queue with all the sessions expected in this traffic
	if(traffic_id == 0){
		float time_to_schedule_session = shared.start_time.read();
		LOG_DEBUG("traffic id="<<getUID()<<", start time="<<shared.start_time.read()<<", num of sessions="<<(int)(shared.number_of_sessions.read())<<endl);
		for(int i = 0; i< (int)(shared.number_of_sessions.read()); i++){
			//Insert all sessions according to the session inter-arrival time;
			//which is dictated by an exponential distribution, this can be
			//configured through off_time and interval_exponential
			HTTPSession* sess = new HTTPSession(shared.connections_per_session.read());
			sess->setSessionID(session_id);
			id_session.insert(SSFNET_MAKE_PAIR(session_id, sess));
			queue.push(SSFNET_MAKE_PAIR(VirtualTime(time_to_schedule_session, VirtualTime::SECOND), sess));
			LOG_DEBUG("A session has been pushed! schedule at="<<time_to_schedule_session<<", session id="<<session_id<< endl);
			session_id++;
			if(shared.interval_exponential.read()){
				time_to_schedule_session += getRandom()->exponential(1.0/(shared.interval.read()));
			}
			else {
				time_to_schedule_session += shared.interval.read();
			}
		}
		//Dump UIDs to their respective containers
		if(shared.dst_ips.read().size()==0){
			while(!unshared.traffic_flows->empty()){
				int found = 0;
				UID_t src_uid_ = unshared.traffic_flows->front().first;
				UID_t dst_uid_ = unshared.traffic_flows->front().second;
				UIDVec::iterator iter;
				for(iter=srcUIDs.begin(); iter!=srcUIDs.end(); iter++){
					if(src_uid_ == *iter){
						found = 1;
						LOG_DEBUG("duplicate src UID=" << src_uid_ << endl);
						break;
					}
				}
				if(found == 0){
					srcUIDs.push_back(src_uid_);
					LOG_DEBUG("insert src UID=" << src_uid_ << endl);
				}
				found = 0;
				for(iter=dstUIDs.begin(); iter!=dstUIDs.end(); iter++){
					if(dst_uid_ == *iter){
						found = 1;
						LOG_DEBUG("duplicate dst UID=" << dst_uid_ << endl);
						break;
					}
				}
				if(found == 0){
					dstUIDs.push_back(dst_uid_);
					LOG_DEBUG("insert dst UID=" << dst_uid_ << endl);
				}
				unshared.traffic_flows->pop_front();
			}
		}else{
			while(!unshared.hybrid_traffic_flows->empty()){
				int found = 0;
				UID_t src_uid_ = unshared.hybrid_traffic_flows->front().first;
				IPAddress dst_ip_;
				dst_ip_.fromString(unshared.hybrid_traffic_flows->front().second);
				for(UIDVec::iterator iter=srcUIDs.begin(); iter!=srcUIDs.end(); iter++){
					if(src_uid_ == *iter){
						found = 1;
						LOG_DEBUG("duplicate src UID=" << src_uid_ << endl);
						break;
					}
				}
				if(found == 0){
					srcUIDs.push_back(src_uid_);
					LOG_DEBUG("insert src UID=" << src_uid_ << endl);
				}
				found = 0;
				for(IPVector::iterator iter=dstIPs.begin(); iter!=dstIPs.end(); iter++){
					if(dst_ip_ == *iter){
						found = 1;
						LOG_DEBUG("duplicate dst IP=" << dst_ip_ << endl);
						break;
					}
				}
				if(found == 0){
					dstIPs.push_back(dst_ip_);
					LOG_DEBUG("insert dst IP=" << dst_ip_ << endl);
				}
				unshared.hybrid_traffic_flows->pop_front();
			}
		}
		traffic_id++;
	} else {
		//pop the downloads from the queue if it's session has already been deleted
		int stop=0;
		while(!queue.empty() && stop==0){
			assert(queue.top().second);
			if(queue.top().second->getLevel() == HTTPLevel::DOWNLOAD &&
					SSFNET_DYNAMIC_CAST(HTTPDownload*,queue.top().second)->getSession()==0){
				LOG_DEBUG("DOWNLOAD, THE SESSION HAS BEEN DELETED!"<<endl)
				delete queue.top().second;
				queue.pop();
			}else{
				stop=1;
			}
		}
		if(!queue.empty()){
			assert(queue.top().second);
			if(queue.top().second->getLevel() == HTTPLevel::SESSION) {
				// This is a session
				HTTPSession* http_sess = SSFNET_DYNAMIC_CAST(HTTPSession*,queue.top().second);
				assert(http_sess);
				//std::cout<<"at time="<<now.second()<<", processing a new session, session id="<<http_sess->getSessionID()<<"\n";
				LOG_DEBUG("processing a new session, session id="<<http_sess->getSessionID()<< endl);
				// Take the session out of the queue, we have a pointer to it already
				queue.pop();

				//If there are remaining connections in this session, schedule them
				if(http_sess->getRemainingNumConnection()>0) {
					LOG_DEBUG("there are " << http_sess->getRemainingNumConnection() <<
							" to schedule" <<  " at " << now.second() << endl);
					// xxx, ask miguel
					// Before associating a session with a src UID, check in the busyList
					// whether a src can go back to srcs vector
					/*for(BusyList::iterator iter = busyList.begin(); iter!=busyList.end(); iter ++) {
						//XXX if(now.second() - iter->second.second() > shared.session_timeout.read()){
						if(now.second() - iter->second.second() > 20){
							// A session timeout has elapsed since the last time this
							// src was used, now it can go back to traffic_flows list
							UIDVec::iterator iter2 = srcUIDs.begin();
							srcUIDs.insert(iter2, iter->first);
							busyList.erase(iter);
							LOG_DEBUG("PUSH BACK src=" << iter->first <<" dst=" << " dst=" << iter->second << endl);
						}
						LOG_DEBUG("busylist: src UID=" << iter->first <<" time=" << iter->second << endl);
					}*/

					// Before scheduling a connection, this session must be associated to a srcUID
					int rnd_ = (int)(getRandom()->uniform(0,1) * srcUIDs.size());
					LOG_DEBUG("The random index obtained is " << rnd_ << " rnd=" << getRandom()->uniform(0,1) << endl);

					//pick a src for the session
					UID_t src_uid_ = srcUIDs[rnd_];
					http_sess->setSrcUID(src_uid_);
					LOG_DEBUG("The src UID gotten is " << src_uid_ << endl);

					//pick a dst for the first download of this session
					UID_t dst_uid_=0;
					uint32_t dst_ip_=0;
					int dst_rnd_=0;
					if(shared.dst_ips.read().size()==0){
						dst_rnd_ = (int)(getRandom()->uniform(0,1) * dstUIDs.size());
						dst_uid_ = dstUIDs[dst_rnd_];
					}else{
						dst_rnd_ = (int)(getRandom()->uniform(0,1) * dstIPs.size());
						dst_ip_ = dstIPs[dst_rnd_];
					}

                    /* related to busyList, ask miguel later
					// Insert the src into busyList so that other session cannot be associated with it
					busyList.insert(SSFNET_MAKE_PAIR(src_uid_, now));
					// Erase from the list of available sources
					if(!srcUIDs.empty())
						srcUIDs.erase(srcUIDs.begin() + rnd_);
					*/

					// Configure traffics_to_start with the current data
					traffics_to_start = new HttpStartTrafficEvent();
					traffics_to_start->setStartTime(now);
					traffics_to_start->setHostUID(src_uid_);
					traffics_to_start->setDstUID(dst_uid_);
					traffics_to_start->setDstIP(dst_ip_);
					traffics_to_start->setTrafficType(this);
					traffics_to_start->setTrafficTypeUID(getUID());
					traffics_to_start->setTrafficId(traffic_id);
					//set the additional parameters in HTTP start traffic event: session id, file size
					SSFNET_DYNAMIC_CAST(HttpStartTrafficEvent*,traffics_to_start)->setSessionID(http_sess->getSessionID());
					SSFNET_DYNAMIC_CAST(HttpStartTrafficEvent*,traffics_to_start)->setFileSize(getFileSize());
					traffic_id++;
					LOG_DEBUG("srcUID sess=" << src_uid_ << " dst_uid_=" << dst_uid_
							<<" dst_ip_=" <<dst_ip_<<" at" << now.second() << endl);
					// One less connection to schedule in this session
					http_sess->decreaseRemainingNumConnection();
					// Create a new connection if necessary and push it to the queue
					if(http_sess->getRemainingNumConnection()>0){
						LOG_DEBUG("remaining connections in session=" << http_sess->getRemainingNumConnection() << endl);
						HTTPDownload* download_ = new HTTPDownload(http_sess);
						if(download_->getSession()){
							download_->getSession()->download_list.push_back(download_);
						}
						VirtualTime next_ = now + VirtualTime(getFlowInterArrival(), VirtualTime::SECOND);
						queue.push(SSFNET_MAKE_PAIR(next_, download_));
						LOG_DEBUG("next time =" << next_ << endl)
						http_sess->decreaseRemainingNumConnection();
					}
				}
			} else if(queue.top().second->getLevel() == HTTPLevel::DOWNLOAD) {
				LOG_DEBUG("processing a new download" << endl);

				// Get a pointer to this download
				HTTPDownload* http_download_ = SSFNET_DYNAMIC_CAST(HTTPDownload*,queue.top().second);
				assert(http_download_);

				// Get the session this download belong to
				HTTPSession* http_sess_ = http_download_->getSession();
				assert(http_sess_);

				//std::cout<<"at time="<<now.second()<<", processing a new download, session id="<<http_sess_->getSessionID()<<"\n";

				UID_t src_uid_ = http_download_->getSession()->getSrcUID();
				UID_t dst_uid_=0;
				uint32_t dst_ip_=0;
				int dst_rnd_=0;
				if(shared.dst_ips.read().size()==0){
					dst_rnd_ = (int)(getRandom()->uniform(0,1) * dstUIDs.size());
					dst_uid_ = dstUIDs[dst_rnd_];
				}else{
					dst_rnd_ = (int)(getRandom()->uniform(0,1) * dstIPs.size());
					dst_ip_ = dstIPs[dst_rnd_];
				}

				LOG_DEBUG("schedule a new download in DOWNLOAD!=" << endl);
				traffics_to_start = new HttpStartTrafficEvent();
				traffics_to_start->setStartTime(now);
				traffics_to_start->setHostUID(src_uid_);
				traffics_to_start->setDstUID(dst_uid_);
				traffics_to_start->setDstIP(dst_ip_);
				traffics_to_start->setTrafficType(this);
				traffics_to_start->setTrafficTypeUID(getUID());
				traffics_to_start->setTrafficId(traffic_id);
				//set the additional parameters in HTTP start traffic event: session id, file size
				SSFNET_DYNAMIC_CAST(HttpStartTrafficEvent*,traffics_to_start)->setSessionID(http_sess_->getSessionID());
				SSFNET_DYNAMIC_CAST(HttpStartTrafficEvent*,traffics_to_start)->setFileSize(getFileSize());
				traffic_id++;

				LOG_DEBUG("in download, srcUID=" << http_sess_->getSrcUID() << " dst_uid_=" << dst_uid_ << endl);

				// This download was already processed and it is popped up
				queue.pop();

				// Insert the flow into busyList
				//busyList.insert(SSFNET_MAKE_PAIR(src_uid_, now));

				if(http_sess_->getRemainingNumConnection()>0){
					LOG_DEBUG("more connections to schedule! "<<http_sess_->getRemainingNumConnection()<<endl);
					HTTPDownload* download_ = new HTTPDownload(http_sess_);

					http_sess_->download_list.push_back(download_);

					VirtualTime next_ = now + VirtualTime(getFlowInterArrival(), VirtualTime::SECOND);
					LOG_DEBUG("next=" << next_.second() << " now=" << now.second() << endl);
					queue.push(SSFNET_MAKE_PAIR(next_, download_));
					LOG_DEBUG("next time =" << next_ << endl);
					http_sess_->decreaseRemainingNumConnection();
				} else {
					LOG_DEBUG("The session has finished." << endl);
				}
			}
		}
	}
	if(queue.empty()){
		//Finish traffic
		LOG_DEBUG("wrapping up traffic" << endl);
		wrap_up=true;
	} else {
		LOG_DEBUG("recalling at:" << (queue.top().first -now) << endl);
		recall_at = queue.top().first - now;
	}
}

void TCPTraffic::processFinishedEvent(FinishedTrafficEvent* finished_evt) {
	LOG_DEBUG("process finished traffic event."<<endl)
	VirtualTime now = prime::ssf::now();
	uint32_t session_id = SSFNET_DYNAMIC_CAST(HttpFinishedTrafficEvent*,finished_evt)->getSessionID();
	LOG_DEBUG("delete session! session id="<<session_id<<", current time="<<now.second()<<endl)
	if(id_session.find(session_id)!=id_session.end()){
		if((*id_session.find(session_id)).second){
			delete (*id_session.find(session_id)).second;
			(*id_session.find(session_id)).second=0;
		}
	}else{
		LOG_ERROR("invalid session id"<<endl)
	}
}

double TCPTraffic::getFlowInterArrival()
{
	//Get flow inter-arrival time according to the two-mode bounded Weibull distribution
	//refer to "A Comparative Analysis of Web and Peer-to-Peer Traffic", WWW2008
	double sample_ = getRandom()->uniform(0,1);
	if(sample_ <= 0.97){ /*inter-arrival time is less than or equal to 0.06 sec */
		LOG_DEBUG("INTERARRIVAL random number=" << sample_ <<
			" , got from Weibull:" << (double)exp(log(0.01)+log(-log(1-sample_))/0.76) << endl);
		return (double)exp(log(0.01)+log(-log(1-sample_))/0.76);
	} else { /* inter-arrival time is greater than 0.06 sec */
		LOG_DEBUG("INTERARRIVAL random number=" << sample_ <<
			" , got from Weibull:" << (double)exp(log(0.00003)+log(-log(1-sample_))/0.15) << endl);
		return (double)exp(log(0.00003)+log(-log(1-sample_))/0.15);
	}
}

int TCPTraffic::getDstPort(){
	return shared.dst_port.read();
}

uint64_t TCPTraffic::getFileSize(){
	if(shared.file_pareto.read()){
		//Get flow size according to a concatenation of bounded Weibull and Pareto distributions
		//refer to "A Comparative Analysis of Web and Peer-to-Peer Traffic", WWW2008
		double f_size = 0; //in KB
		double sample_ = getRandom()->uniform(0,1);
		if(sample_< 0.91){/* file size is smaller than 30KB*/
			f_size = (double)exp(log(2.7)+log(-log(1-sample_))/0.38);
			//LOG_DEBUG("FILE DEBUG, less than 30 KB, sample="<<sample_<<", f_size="<<(int)(f_size * 1000)<<endl)
		}else if (sample_>=0.91 && sample_<=0.9995){ /* file size is equal to or larger than 30KB and smaller than or equal to 5MB */
			f_size = (double)(3/exp(log(1-sample_)/1.05));
			//LOG_DEBUG("FILE DEBUG, greater than 30 KB, sample="<<sample_<<", f_size="<<(int)(f_size * 1000)<<endl)
		}else{/* file size is larger than 5MB */
			f_size = (double)(200/exp(log(1-sample_)/2.35));
			//LOG_DEBUG("FILE DEBUG, greater than 5MB, sample="<<sample_<<", f_size="<<(int)(f_size * 1000)<<endl)
		}
		if((int)(f_size*1000)==0){
			return 1; //B
		}else{
			return (uint64_t)(f_size * 1000); //in B
		}
	}else{//return constant file size
		//LOG_DEBUG("FILE DEBUG, constant, f_size="<<shared.file_size.read()<<endl)
		return shared.file_size.read();
	}
}

bool TCPTraffic::getToEmulated(){
	return shared.to_emulated.read();
}

float TCPTraffic::getSessionTimeout(){
	return shared.session_timeout.read();
}

HTTPSession::HTTPSession(int conns_per_session) : remaining_connections(conns_per_session){}

HTTPSession::~HTTPSession(){
	// Delete all downloads of this session
	for(SSFNET_LIST(HTTPDownload*)::iterator iter = download_list.begin(); iter!= download_list.end(); iter++){
		if(*iter){
			(*iter)->deleteSession();
		}
	}
}

HTTPLevel::Level HTTPSession::getLevel(){
	return HTTPLevel::SESSION;
}

int HTTPSession::getRemainingNumConnection() const{
	return remaining_connections;
}

void HTTPSession::decreaseRemainingNumConnection(){
	remaining_connections--;
}

void HTTPSession::setSrcUID(UID_t s_uid){
	src_uid = s_uid;
}

UID_t HTTPSession::getSrcUID(){
	return src_uid;
}

void HTTPSession::setSessionID(uint32_t s_id){
	session_id = s_id;
}

uint32_t HTTPSession::getSessionID(){
	return session_id;
}

HTTPDownload::HTTPDownload(HTTPSession* sess) : http_session(sess) {}

HTTPDownload::~HTTPDownload(){}

HTTPSession* HTTPDownload::getSession(){
	return http_session;
}

void HTTPDownload::deleteSession(){
	http_session = NULL;
}

HTTPLevel::Level HTTPDownload::getLevel(){
	return HTTPLevel::DOWNLOAD;
}

// ssf requires this macro to register an event class.
SSF_REGISTER_EVENT(HttpStartTrafficEvent, HttpStartTrafficEvent::createHttpStartTrafficEvent);
SSF_REGISTER_EVENT(HttpFinishedTrafficEvent, HttpFinishedTrafficEvent::createHttpFinishedTrafficEvent);

} // namespace ssfnet
} // namespace prime
