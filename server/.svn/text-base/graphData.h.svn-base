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


#ifndef GRAPHDATA_H
#define GRAPHDATA_H

#include <iostream>
#include <map>
#include <deque>
#include <vector>
#include <string>
#include <sstream>
#include <sys/time.h>
#include "packet.h"
#include "baseData.h"
#include "valueEquals.h"
#include "../common/threads.h"

typedef struct Graphstats Graphstats;

typedef struct Connections
{
  uint16_t sport;
  uint16_t dport;
  std::string protocol;
}Connections;

typedef struct GraphInfo
{
  uint32_t weights[6];//6seconds worth of weights weights[0] 0-1 second, weights[1] 1-2 second...
  time_t lastAccess;
  std::vector< Connections > connections;
}GraphInfo;

typedef struct BandwidthStats
{
  uint32_t total;
  uint32_t outgoing;
  uint32_t incomming;
}BandwidthStats;

typedef struct Edge
{
  uint32_t ipA;
  uint32_t ipB;
}Edge;
//NodeData Helpers
std::vector< std::string > getDNSInfo( const uint32_t & ip );

class GraphData : public BaseData
{
  public:
    //Constructor
    GraphData( Mutex &coutMutex, Mutex &logMutex,
	std::ofstream *log );
    //Add a new edge to the graph (from A to B)
    void addEdge( uint32_t ipA, uint32_t ipB, uint32_t weight );
    void addEdge( Packet p );
    //Deletes an edge from the graph (from A to B)
    void delEdge( uint32_t ipA, uint32_t ipB );
    //removes everything with the ip passed in
    void kill( uint32_t ip );
    //returns a string in the right format to send across the network to the
    //client. 
    std::string makeGraphPacket();
    void setEdgeLife( int time );
    int getEdgeLife();
    std::string makeDeadNodePacket(); //if string is zero no nodes removed
    std::string getNodeData( const uint32_t & ip );
    int getNumEdges();
    int getNumDeadNodes();


  private:
    // numToString -- takes 4 bytes and puts them in a format
    // to transfer across the network.
    uint32_t calculateWeight( uint32_t weights[6], time_t lastAccess );
    uint32_t getIncommingBandwidth( const uint32_t & ip, std::stringstream &connections );
    uint32_t getOutgoingBandwidth( const uint32_t & ip, std::stringstream &connections );

    //DATA
    int edgeLife_; //amount of seconds an edge can live without traffic
    Mutex edgeLifeMutex_;
    std::map< uint32_t, std::map< uint32_t, GraphInfo* > > graphMap_;
    Mutex graphMapMutex_;
    Mutex deadNodesMutex_;
    std::deque< Edge > deadNodes_;
};


#endif
