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


#include "filterData.h"
//#include "bandwidthData.h"
#include "graphData.h"
#include "clientCommData.h"
//#include "tracerouteData.h"
#include <vector>

void sendConfigs( std::vector< int > &clientSockets, ClientCommData* clientCommData );
void sendDead( std::vector< int > &sockets, ClientCommData* clientCommData );
void send( std::vector<int> &sockets, std::string dataToSend );
void *communicationChannel(void *threadmsg);
void printGraph( std::vector< std::vector< float > > graph );
int startServerSocket( ClientCommData *clientCommData );
void setnonblocking( int sock );
void communicateOverSocket( ClientCommData *clientCommData, int serverSocket );
void sendConfiguration( int socket, ClientCommData* clientCommData );
void sendGraph( std::vector<int> &sockets, ClientCommData* clientCommData );
void processIncommingPacket( std::vector<int>::iterator socket, std::vector<int> &clientSockets, ClientCommData* clientCommData );
void processIncommingFilterTypes( std::vector<int>::iterator socket, std::vector< int > & clientSockets, ClientCommData* clientCommData);
void processIncommingNodeData( std::vector<int>::iterator socket, std::vector< int > & clientSockets, ClientCommData* clientCommData);
void processIncommingConfigData( std::vector<int>::iterator socket, std::vector< int > & clientSockets, ClientCommData* clientCommData);
void removeSocket( int removeMe, std::vector<int> &clientSockets );
void removeSocket( std::vector<int>::iterator socket, std::vector<int> &clientSockets );
