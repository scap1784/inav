/**
 * * INAV - Interactive Network Active-traffic Visualization
 * * Copyright © 2007  Nathan Robinson, Jeff Scaparra
 * *
 * * This file is a part of INAV.
 * *
 * * This program is free software: you can redistribute it and/or modify
 * * it under the terms of the GNU General Public License as published by
 * * the Free Software Foundation, either version 3 of the License, or
 * * (at your option) any later version.
 * *
 * * This program is distributed in the hope that it will be useful,
 * * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * * GNU General Public License for more details.
 * *
 * * You should have received a copy of the GNU General Public License
 * * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * */


#include "graphData.h"
#include "../common/constants.h"
#include "../common/helper.h"
#include <arpa/inet.h>
#include <sstream>
#include <stdexcept>
#include <algorithm>
#include <netdb.h>
#include <sys/socket.h>

GraphData::GraphData( Mutex &coutMutex, Mutex &logMutex, std::ofstream *log ): BaseData( coutMutex, logMutex, log)
{
  edgeLife_ = communication::EdgeLife;
}

void GraphData::setEdgeLife( int time )
{
  edgeLifeMutex_.lock();
  edgeLife_ = time;
  edgeLifeMutex_.unlock();
}

int GraphData::getEdgeLife()
{
  edgeLifeMutex_.lock();
  int tmp = edgeLife_;
  edgeLifeMutex_.unlock();
  return tmp;
}

inline Connections makeConnection( Packet p )
{
  Connections connection;
  if( p.isTCP() )
  {
    connection.sport = p.getTCPSourcePort();
    connection.dport = p.getTCPDestinationPort();
    connection.protocol = "TCP";
  }
  else if ( p.isUDP() )
  {
    connection.sport = p.getUDPSourcePort();
    connection.dport = p.getUDPDestinationPort();
    connection.protocol = "UDP";
  }
  else if ( p.isICMP() )
  {
    connection.sport = 0;
    connection.dport = 0;
    connection.protocol = "ICMP";
  }
  else if ( p.isIP() )
  {
    connection.sport = 0;
    connection.dport = 0;
    connection.protocol = "IP";
  }
  else
  {
    //unknown
    connection.sport = 0;
    connection.dport = 0;
    connection.protocol = "UNKNOWN";
  }
  return connection;
}

inline void addConnection( std::vector< Connections > &connections, Connections connection )
{
  std::vector< Connections >::iterator itr;
  for( itr = connections.begin(); itr != connections.end(); ++itr )
  {
    if( itr->sport == connection.sport &&
	itr->dport == connection.dport &&
	itr->protocol == connection.protocol )
      break;
  }
  if( itr != connections.end() )
    return;
  connections.push_back( connection );
}

void GraphData::addEdge( Packet p )
{
  uint32_t ipA = p.getSourceAddress();
  uint32_t ipB = p.getDestinationAddress();
  uint32_t weight = p.getActualSize();

  Connections connection = makeConnection( p );

  graphMapMutex_.lock();
  if( graphMap_[ipA][ipB] == NULL )
  {
    //new edge
    graphMap_[ipA][ipB] = new GraphInfo;
    graphMap_[ipA][ipB]->lastAccess = time( NULL );
    graphMap_[ipA][ipB]->weights[0] = weight;
    graphMap_[ipA][ipB]->weights[1] = 0;
    graphMap_[ipA][ipB]->weights[2] = 0;
    graphMap_[ipA][ipB]->weights[3] = 0;
    graphMap_[ipA][ipB]->weights[4] = 0;
    graphMap_[ipA][ipB]->weights[5] = 0;
    graphMap_[ipA][ipB]->connections.push_back( connection );
  }
  else
  {
    //already existed
    addConnection( graphMap_[ipA][ipB]->connections, connection );
    time_t currentTime = time(NULL);
    time_t oldAccessTime = graphMap_[ipA][ipB]->lastAccess;
    if( currentTime == oldAccessTime )
    {
      graphMap_[ipA][ipB]->weights[0] += weight;
    }
    else
    {
      uint32_t newWeights[6] = {0,0,0,0,0,0};
      int secondsPass = currentTime - oldAccessTime;
      for(int i = 0; i < 6; ++i )
      {
	int newIndex = secondsPass + i;
	if( newIndex > 5 )
	{
	  break;
	}
	newWeights[newIndex] = graphMap_[ipA][ipB]->weights[i];
      }
      for(int i = 0; i < 6; ++i )
      {
	graphMap_[ipA][ipB]->weights[i] = newWeights[i];
      }
    }
    graphMap_[ipA][ipB]->lastAccess = currentTime;
  }
  graphMapMutex_.unlock();
}


void GraphData::addEdge( uint32_t ipA, uint32_t ipB, uint32_t weight )
{
  graphMapMutex_.lock();
  if( graphMap_[ipA][ipB] == NULL )
  {
    //new edge
    graphMap_[ipA][ipB] = new GraphInfo;
    graphMap_[ipA][ipB]->lastAccess = time( NULL );
    graphMap_[ipA][ipB]->weights[0] = weight;
    graphMap_[ipA][ipB]->weights[1] = 0;
    graphMap_[ipA][ipB]->weights[2] = 0;
    graphMap_[ipA][ipB]->weights[3] = 0;
    graphMap_[ipA][ipB]->weights[4] = 0;
    graphMap_[ipA][ipB]->weights[5] = 0;
  }
  else
  {
    time_t currentTime = time(NULL);
    time_t oldAccessTime = graphMap_[ipA][ipB]->lastAccess;
    if( currentTime == oldAccessTime )
    {
      graphMap_[ipA][ipB]->weights[0] += weight;
    }
    else
    {
      uint32_t newWeights[6] = {0,0,0,0,0,0};
      int secondsPass = currentTime - oldAccessTime;
      for(int i = 0; i < 6; ++i )
      {
	int newIndex = secondsPass + i;
	if( newIndex > 5 )
	{
	  break;
	}
	newWeights[newIndex] = graphMap_[ipA][ipB]->weights[i];
      }
      for(int i = 0; i < 6; ++i )
      {
	graphMap_[ipA][ipB]->weights[i] = newWeights[i];
      }
    }
    graphMap_[ipA][ipB]->lastAccess = currentTime;
  }
  graphMapMutex_.unlock();
}

void GraphData::delEdge( uint32_t ipA, uint32_t ipB )
{
  graphMapMutex_.lock();
  GraphInfo* tmp = graphMap_[ipA][ipB];
  delete tmp;
  graphMap_[ipA].erase(ipB);
  graphMapMutex_.unlock();
}

void GraphData::kill( uint32_t ip )
{
  graphMapMutex_.lock( );
  std::map< uint32_t, GraphInfo* >::iterator itr;
  for( itr = graphMap_[ip].begin(); itr != graphMap_[ip].end(); ++itr )
  {
    delete itr->second;
  }
  graphMap_.erase( ip );
  graphMapMutex_.unlock( );
}

std::string GraphData::makeGraphPacket()
{
  std::string data;
  std::string datastream;
  graphMapMutex_.lock( );
  std::cerr << "mutex locked" << std::endl;

  std::map< uint32_t, std::map< uint32_t, GraphInfo*> >::iterator ritr;
  std::map< uint32_t, GraphInfo* >::iterator citr;
  std::map< uint32_t, GraphInfo* >::iterator tmp;
  uint32_t edges = 0;
  edgeLifeMutex_.lock( );
  for( ritr = graphMap_.begin(); ritr != graphMap_.end(); ++ritr )
  {
    for( citr = ritr->second.begin(); citr != ritr->second.end();  )
    {
      tmp = citr; //Needed to be able to delete the current element in all cases.
      ++citr;
      //edge exist
      //recalculate weight
      time_t currentTime = time( NULL );
      if( (currentTime - tmp->second->lastAccess)  >= edgeLife_ )
      {
	//edge past life expectancy - time to die
	Edge edge;
	edge.ipA = ritr->first;
	edge.ipB = tmp->first;
	deadNodesMutex_.lock( );
	deadNodes_.push_back( edge );
	deadNodesMutex_.unlock();
	delete tmp->second;
	(ritr->second).erase( tmp->first );
	continue;
      }
      ++edges;
      data += numToString( ritr->first );
      data += numToString( tmp->first );
      data += numToString( htonl(calculateWeight((tmp->second)->weights, tmp->second->lastAccess)) );
    }
  }
  edgeLifeMutex_.unlock( );
  graphMapMutex_.unlock( );
  std::cout << "Number of Edges: " << edges << std::endl;
  datastream += numToString( htonl(edges) );
  datastream += data;
  return datastream;
}

std::string GraphData::makeDeadNodePacket()
{
  std::string data;
  std::string datastream;
  int edges = 0;
  std::deque< Edge >::iterator itr;
  deadNodesMutex_.lock( );
  for( itr = deadNodes_.begin(); itr != deadNodes_.end(); ++itr )
  {
    edges++;
    data += numToString( itr->ipA );
    data += numToString( itr->ipB );
  }
  deadNodes_.clear();
  deadNodesMutex_.unlock( );

  datastream = numToString( htonl( edges ) );
  datastream += data;
  return datastream;
}


inline uint32_t GraphData::calculateWeight( uint32_t weights[6], time_t lastAccess )
{
  uint32_t newWeights[6] = {0,0,0,0,0,0};
  time_t currentTime = time (NULL);
  int secondsPass = currentTime - lastAccess;
  for(int i = 0; i < 6; ++i )
  {
    int newIndex = secondsPass + i;
    if( newIndex > 5 )
    {
      break;
    }
    newWeights[newIndex] = weights[i];
  }
  for( int i = 0; i < 6; ++i )
  {
    weights[i] = newWeights[i];
  }
  uint64_t total = 0;
  total += 3 * newWeights[0];
  total += 3 * newWeights[1];
  total += 2 * newWeights[2];
  total += 2 * newWeights[3];
  total += newWeights[4];
  total += newWeights[5];
  total /= 12;
  return (uint32_t)total;
}

std::string GraphData::getNodeData( const uint32_t & ip )
{
  std::vector< std::string > names = getDNSInfo( ip );
  BandwidthStats bandwidth;
  std::stringstream connections;
  bandwidth.outgoing = getOutgoingBandwidth( ip, connections );
  bandwidth.incomming = getIncommingBandwidth( ip, connections );
  bandwidth.total = bandwidth.incomming + bandwidth.outgoing;
  std::stringstream packet;
  packet << "<Information>" << std::endl;
  packet << "<IP>" << inet_ntoa( *((struct in_addr*)&ip) ) << "</IP>" << std::endl;
  std::vector< std::string >::iterator itr;
  for( itr = names.begin(); itr != names.end(); ++itr )
  {
    packet << "<DNS>" << *itr << "</DNS>" << std::endl;
  }

  packet << "<Bandwidth>" << std::endl;
  packet << "<Total>" << bandwidth.total << "</Total>" << std::endl;
  packet << "<Incomming>" << bandwidth.incomming << "</Incomming>" << std::endl;
  packet << "<Outgoing>" << bandwidth.outgoing << "</Outgoing>" << std::endl;
  packet << "</Bandwidth>" << std::endl;

  packet << "<Connections>" << std::endl;
  packet << connections.str();
  packet << "</Connections>" << std::endl;
  packet << "</Information>" << std::endl;

  return packet.str();
}

uint32_t GraphData::getOutgoingBandwidth( const uint32_t & ip, std::stringstream &connections )
{
  uint32_t outgoing = 0;
  std::map< uint32_t, GraphInfo* >::iterator itr;
  graphMapMutex_.lock( );
  connections << "<Outgoing>" << std::endl;
  for( itr = graphMap_[ip].begin(); itr != graphMap_[ip].end(); ++itr )
  {
    outgoing += calculateWeight( itr->second->weights, itr->second->lastAccess );
    std::vector< Connections >::iterator citr;
    for( citr = itr->second->connections.begin(); citr != itr->second->connections.end(); ++citr )
    {
      connections << "<Connection>" << std::endl;
      connections << "<Protocol>" << citr->protocol << "</Protocol>" << std::endl;
      connections << "<Source>" << std::endl;
      connections << "<IP>" << inet_ntoa( *((struct in_addr*)&ip)) << "</IP>" << std::endl;
      connections << "<Port>" << citr->sport << "</Port>" << std::endl;
      connections << "</Source>" << std::endl;
      connections << "<Destination>" << std::endl;
      connections << "<IP>" << inet_ntoa( *((struct in_addr*)&(itr->first))) << "</IP>" << std::endl;
      connections << "<Port>" << citr->dport << "</Port>" << std::endl;
      connections << "</Destination>" << std::endl;
      connections << "</Connection>" << std::endl;
    }
  }
  connections << "</Outgoing>" << std::endl;
  graphMapMutex_.unlock( );
  return outgoing;
}

uint32_t GraphData::getIncommingBandwidth( const uint32_t & ip, std::stringstream &connections )
{
  uint32_t incomming = 0;
  std::map< uint32_t, std::map< uint32_t, GraphInfo* > >::iterator ritr;
  std::map< uint32_t, GraphInfo* >::iterator citr;
  graphMapMutex_.lock( );
  connections << "<Incoming>"  << std::endl;
  for( ritr = graphMap_.begin( ); ritr != graphMap_.end(); ++ritr )
  {
    for( citr = ritr->second.begin(); citr != ritr->second.end(); ++citr )
    {
      if( citr->first == ip )
      {
	//incomming
	incomming += calculateWeight( citr->second->weights, citr->second->lastAccess );
	std::vector< Connections >::iterator itr;
	for( itr = citr->second->connections.begin(); itr != citr->second->connections.end(); ++itr )
	{
	  connections << "<Connection>" << std::endl;
	  connections << "<Protocol>" << itr->protocol << "</Protocol>" << std::endl;
	  connections << "<Source>" << std::endl;
	  connections << "<IP>" << inet_ntoa( *((struct in_addr*)&(citr->first)) ) << "</IP>" << std::endl;
	  connections << "<Port>" << itr->dport << "</Port>" << std::endl;
	  connections << "</Source>" << std::endl;
	  connections << "<Destination>" << std::endl;
	  connections << "<IP>" << inet_ntoa( *((struct in_addr*)&(ritr->first)) ) << "</IP>" << std::endl;
	  connections << "<Port>" << itr->sport << "</Port>" << std::endl;
	  connections << "</Destination>" << std::endl;
	  connections << "</Connection>" << std::endl;
	}
      }
      else
      {
	//next
	continue;
      }
    }
  }
  connections << "</Incoming>" << std::endl;
  graphMapMutex_.unlock( );
  return incomming;
}

inline std::vector<std::string> getDNSInfo( const uint32_t & ip )
{
  struct hostent* response = gethostbyaddr( &ip, 4 ,AF_INET);
  std::vector< std::string > names;
  if( response != NULL )
  {
    std::string name = response->h_name;
    names.push_back( name );
    int i = 0;
    while( response->h_aliases[ i ] != NULL )
    {
      name = response->h_aliases[i];
      names.push_back( name );
      ++i;
    }
  }
  else
  {
    //couldn't find the host
    std::string name = inet_ntoa( *((struct in_addr*)&ip) );
    names.push_back( name );
  }
  return names;
}

int GraphData::getNumEdges()
{
  std::map< uint32_t, std::map< uint32_t, GraphInfo* > >::iterator ritr;
  std::map< uint32_t, GraphInfo* >::iterator citr;
  int count = 0;
  graphMapMutex_.lock( );
  for( ritr = graphMap_.begin(); ritr != graphMap_.end(); ++ritr )
  {
    for( citr = ritr->second.begin(); citr != ritr->second.end(); ++citr )
    {
      ++count;
    }
  }
  graphMapMutex_.unlock( );
  return count;
}

int GraphData::getNumDeadNodes()
{
  deadNodesMutex_.lock( );
  int count = deadNodes_.size();
  deadNodesMutex_.unlock( );
  return count;
}

