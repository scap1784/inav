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


#include<iostream>
#include<sstream>
#include<string>
#include<stdlib.h>
#include<errno.h>
/*Network fuctions*/
#include<sys/types.h>
#include<sys/socket.h>
#include<netinet/in.h>
#include<fcntl.h>
/*FD_SET, FD_ISSET, FD_ZERO*/
#include<sys/time.h>
/*THREADS*/
#include<pthread.h>

#include "../common/constants.h"
#include "clientComm.h"
#include "../common/xmlParser.h"
#include "../common/helper.h"
//#include "clientCommData.h"

void *communicationChannel(void *clientCommData )
{
  if (clientCommData == NULL)
  {
    std::cerr << "Client Comm Channel: clientCommData == NULL" << std::endl;
    exit(-1);
  }
  FilterData* filterData = ((ClientCommData*)clientCommData)->filterData;
  filterData->log( "Communication Channel Started!" );

  int socket = startServerSocket( (ClientCommData*)clientCommData );
  communicateOverSocket( (ClientCommData*)clientCommData, socket );


  filterData->log( "Communication Channel Stopping!" );
  pthread_exit(NULL);
}

int startServerSocket( ClientCommData *clientCommData )
{
  int serverSocket;
  int opt = true;
  struct sockaddr_in address;
  FilterData* filterData = ((ClientCommData*)clientCommData)->filterData;

  if( 0 == (serverSocket = socket( AF_INET, SOCK_STREAM, 0) ) )
  {
    std::cerr << "Client Comm Channel: Server Socket Creation Failed" << std::endl;
    exit( -1 );
  }

  if( 0 > setsockopt( serverSocket, SOL_SOCKET, SO_REUSEADDR, (char *)&opt, sizeof(opt)) )
  {
    std::cerr << "Client Comm Channel: setsockopt() failed!" << std::endl;
    exit( -1  );
  }

  setnonblocking( serverSocket );
  std::stringstream message;
  message << "Binding to port: " << clientCommData->getServerPort() << std::endl;
  filterData->log( message.str() );

  address.sin_family = AF_INET;
  address.sin_addr.s_addr = INADDR_ANY;
  address.sin_port = htons( clientCommData->getServerPort() );

  if( 0 > bind(serverSocket, (struct sockaddr*)&address, sizeof(address)) )
  {
    std::cerr << "Client Comm Channel: Binding socket to port failed!" << std::endl;
    exit( -1 );
  }

  if( listen(serverSocket, 5) < 0 )
  {
    std::cerr << "Client Comm Channel: Listening failed!" << std::endl;
    exit( -1 );
  }

  return serverSocket;
}

void communicateOverSocket( ClientCommData *clientCommData, int serverSocket )
{
  struct timeval refreshTime;
  std::vector< int > clientSockets;

  fd_set readfds;

  bool refreashed = true;

  int fdMax = serverSocket;

  time_t lastPrune = time( NULL );
  while( true )
  {
    FD_ZERO( &readfds );

    FD_SET( serverSocket, &readfds );
    std::vector< int >::iterator itr;
    for( itr = clientSockets.begin(); itr != clientSockets.end(); ++itr )
    {
      FD_SET( (*itr), &readfds );
    }
    if( inav::DEBUG )
    {
      std::stringstream os;
      os << "Connections: " << clientSockets.size();
      clientCommData->print( os.str() );
      clientCommData->log( os.str() );
    }
    if( refreashed )
    {
      refreshTime.tv_sec = clientCommData->getRefreshTime();
      refreshTime.tv_usec = 0;
      refreashed = false;
      if( clientSockets.size() > 0 )
      {
	lastPrune = time( NULL );
      }
      else
      {
	//we may need to call makeGraphPacket to prune the graph even tho there
	//are no clients.
	//We will do this every edgeLife.
	time_t currentTime = time( NULL );
	int difference = currentTime - lastPrune;
	if( difference > clientCommData->graphData->getEdgeLife() )
	{
	  //time to prune
	  clientCommData->graphData->makeGraphPacket();
	  lastPrune=time( NULL );
	}
      }
    }

    int activity = select( fdMax + 1, &readfds, NULL, NULL, &refreshTime );

    if( (0 > activity ) && ( EINTR != errno ) )
    {
      std::cerr << "Client Comm Channel: Error with select()!" << std::endl;
      exit( -1 );
    }

    if( (0 > activity ) )
    {
      std::cerr << "Client Comm Channel: Error with select()!" << std::endl;
      exit( -1 );
    }

    if( FD_ISSET( serverSocket, &readfds ) )
    {
      /* Incomming connection */
      struct sockaddr_in address;
      int addressLength = sizeof( address );
      int new_socket;

      if( 0 >= ( new_socket = accept( serverSocket, (struct sockaddr *)&address, (socklen_t*)&addressLength) ) )
      {
	std::cerr << "Client Comm Channel: Error on accept()!" << std::endl;
	exit( -1 );
      }
      clientSockets.push_back( new_socket );
      if( new_socket > fdMax )
	fdMax = new_socket;
      sendConfiguration( new_socket, clientCommData );
    }
    else
    {
      bool timeout = true;
      //An established connection is sending us data
      for( itr = clientSockets.begin(); itr != clientSockets.end(); ++itr )
      {
	if( FD_ISSET( (*itr), &readfds ) )
	{
	  timeout = false;
	  processIncommingPacket( itr, clientSockets, clientCommData );
	  break;
	}
      }
      if( timeout )
      {
	//Or we timed out and need to send a refresh
	refreashed = true;
	sendGraph( clientSockets, clientCommData );
	sendDead( clientSockets, clientCommData );
      }

    }
  }
}

void processIncommingPacket( std::vector<int>::iterator socket, std::vector<int> &clientSockets, ClientCommData* clientCommData )
{
  uint8_t type;
  int size = recv( *socket, &type, 1, 0 );

  if( 0 > size )
  {
    switch( errno  )
    {
      case ECONNREFUSED:
	std::cerr << "Client Comm Channel: Problem with recv() - Connection Refused" << std::endl;
	break;
      case ENOTCONN:
	std::cerr << "Client Comm Channel: Problem with recv() - Not Connected" << std::endl;
	break;
      case EAGAIN:
	std::cerr << "Client Comm Channel: Problem with recv() - errno == EAGAIN" << std::endl;
	break;
      case EBADF:
	std::cerr << "Client Comm Channel: Problem with recv() - Invalid socket descriptor" << std::endl;
	break;
      case EFAULT:
	std::cerr << "Client Comm Channel: Problem with recv() - receive buffer outside of process address space"
	  << std::endl;
	break;
      case EINVAL:
	std::cerr << "Client Comm Channel: Problem with recv() - Invalid argument passed" << std::endl;
	break;
      case EINTR:
	std::cerr << "Client Comm Channel: Problem with recv() - Interrupted by signal" << std::endl;
	break;
      case ENOMEM:
	std::cerr << "Client Comm Channel: Problem with recv() - Could not allocate memory" << std::endl;
	break;
      case ENOTSOCK:
	std::cerr << "Client Comm Channel: Problem with recv() - socket argument not valid" << std::endl;
	break;
      default:
	std::cerr << "Client Comm Channel: Problem with recv() - unknown errno=" << errno << std::endl;
    };
    removeSocket( socket, clientSockets );
    return;
  }
  if( size == 0 )
  {
    //disconnected
    std::cerr << "Client Disconnected" << std::endl;
    removeSocket( socket, clientSockets );
    return;
  }

  switch( type )
  {
    case communication::Config: processIncommingConfigData( socket, clientSockets, clientCommData );
				break;
    case communication::NodeData: processIncommingNodeData( socket, clientSockets, clientCommData );
				  break;
    case communication::FilterTypes: processIncommingFilterTypes( socket, clientSockets, clientCommData );
				     break;
    default:  //If a client sends something else they should be KILLED! ;)
				     std::cerr <<"Client sent a BAD TYPE:" << type << " and will now die" << std::endl;
				     removeSocket( socket, clientSockets );
				     return;
  };
  return;
}

std::string readInConfig( std::vector<int>::iterator socket, std::vector<int> &clientSockets )
{
  uint32_t size = 0;
  recv( *socket, &size, 4, 0 );
  size = ntohl( size );
  std::stringstream output;
  while( size > 0 )
  {
    char buff[size];
    int received = recv( *socket, buff, size, 0 );
    if( received == 0 )
    {
      //problems?
      removeSocket( socket, clientSockets );
      return std::string();
    }
    output << buff;
    size = size - received;
  }
  return output.str();
}

void processIncommingConfigData( std::vector<int>::iterator socket,
    std::vector< int > & clientSockets, ClientCommData* clientCommData)
{
  std::string incommingConfig = readInConfig( socket, clientSockets );

  std::string parsedData = xmlParser( incommingConfig, "RefreshTime" );
  if( parsedData.size() > 0 )
    clientCommData->setRefreshTime( atoi( parsedData.c_str() ));
  parsedData = xmlParser(  incommingConfig, "EdgeLife" );
  if( parsedData.size() > 0 )
    clientCommData->graphData->setEdgeLife( atoi( parsedData.c_str() ) );

  sendConfigs( clientSockets, clientCommData );
}

void sendConfigs( std::vector< int > &clientSockets, ClientCommData* clientCommData )
{
  std::vector< int >::iterator itr;
  for( itr = clientSockets.begin(); itr != clientSockets.end(); ++itr )
  {
    sendConfiguration( *itr, clientCommData );
  }
}

void processIncommingNodeData( std::vector<int>::iterator socket,
    std::vector< int > & clientSockets, ClientCommData* clientCommData)
{
  uint32_t ip = 0;
  int size = 0;
  while( size != 4 )
  {
    uint32_t tmp;
    int newSize = recv( *socket, &tmp, 4-size, 0 );
    if( newSize == 0 )
    {
      std::cerr << "Problem processing incomming Node Data" << std::endl;
      removeSocket( socket, clientSockets );
      return;
    }
    size += newSize;
    ip |= tmp << 8 * ( 4 - newSize );
  }
  std::string packet = clientCommData->graphData->getNodeData( ip );
  std::string dataToSend;
  dataToSend.push_back( communication::NodeData );
  dataToSend += numToString( htonl(packet.size()) );
  dataToSend += packet;
  size = send( *socket, dataToSend.c_str(), dataToSend.size(), 0 );
}

void processIncommingFilterTypes( std::vector<int>::iterator socket,
    std::vector< int > & clientSockets, ClientCommData* clientCommData)
{
  //TODO
  std::cerr << "TODO: ProcessIncommingFilterTypes" << std::endl;
}

void removeSocket( int removeMe, std::vector<int> &clientSockets )
{
  int sd = removeMe;
  std::vector<int>::iterator socket;
  std::vector<int> newClientSocketList;
  for( socket = clientSockets.begin(); socket != clientSockets.end(); ++socket )
  {
    if( *socket != removeMe )
    {
      newClientSocketList.push_back( *socket );
    }
    else
    {
      std::cerr << "Removing socket" << std::endl;
    }
  }
  clientSockets = newClientSocketList;
  close( sd );
}

void removeSocket( std::vector<int>::iterator socket, std::vector<int> &clientSockets )
{
  int sd = *socket;
  std::cout << "SOCKET: " << *socket << std::endl;
  std::vector<int>::iterator removeMe = socket;
  std::vector<int> newClientSocketList;
  for( socket = clientSockets.begin(); socket != clientSockets.end(); ++socket )
  {
    if( *socket != *removeMe )
    {
      newClientSocketList.push_back( *socket );
    }
    else
    {
      std::cerr << "Found" << std::endl;
    }
  }
  clientSockets = newClientSocketList;
  close( sd );
}

void sendConfiguration( int socket, ClientCommData* clientCommData )
{
  std::stringstream dataStream; //Numbers entered this way will appear in string format
  dataStream << "<Config>" << std::endl;
  dataStream << "<RefreshTime>" << clientCommData->getRefreshTime() << "</RefreshTime>" << std::endl;
  dataStream << "<EdgeLife>" << clientCommData->graphData->getEdgeLife() << "</EdgeLife>" << std::endl;
  dataStream << "</Config>" << std::endl;
  std::string dataToSend;
  dataToSend.push_back( communication::Config );
  dataToSend += numToString( htonl(dataStream.str().size()) );
  dataToSend += dataStream.str();

  if( send( socket, dataToSend.c_str(), dataToSend.size(), 0 ) != dataToSend.size() )
  {
    std::cerr << "Client Comm Channel: sendConfiguration() failed!" << std::endl;
  }
}

void sendGraph( std::vector<int> &sockets, ClientCommData* clientCommData )
{
  if( sockets.size() == 0 )
  {
    //no one to send to
    return;
  }
  GraphData* graphData = clientCommData->graphData;
  std::string dataToSend;
  dataToSend.push_back( communication::GraphRefresh ); //set type
  dataToSend += graphData->makeGraphPacket();
  send( sockets, dataToSend );
}

void send( std::vector<int> &sockets, std::string dataToSend )
{
  //can't use iterators because sockets may be remade by removeSocket...
  for( int i = 0; i < sockets.size(); ++i )
  {
    int sent = send( sockets.at( i ), dataToSend.c_str(), dataToSend.size(), 0 );
    //if( send( *socket, dataToSend.c_str(), dataToSend.size(), MSG_DONTROUTE ) != dataToSend.size() )
    if( sent != dataToSend.size() )
    {
      std::cerr << "Sent: " << sent << " Bytes of " << dataToSend.size() << " Bytes." << std::endl;
      std::cerr << "Client Comm Channel: send() failed!" << std::endl;
      removeSocket( sockets.at( i ), sockets );
    }
  }
}

void sendDead( std::vector< int > &sockets, ClientCommData* clientCommData )
{
  //call makeDeadNodePacket() no matter what to clear the deadNode queue;
  std::string deadNodePacket = clientCommData->graphData->makeDeadNodePacket();
  if( sockets.size() == 0 || deadNodePacket.size() == 0 )
  {
    return;
  }
  std::string dataToSend;
  dataToSend.push_back( communication::RemovedEdges ); //set type
  dataToSend += deadNodePacket;
  send( sockets, dataToSend );
}



void setnonblocking( int sock )
{
  int opts;

  opts = fcntl( sock, F_GETFL );
  if ( opts < 0 )
  {
    std::cerr << "Client Comm Channel: fcntl F_GETFL ERROR" << std::endl;
    exit( -1 );
  }
  opts = (opts | O_NONBLOCK);
  if ( fcntl( sock,F_SETFL,opts ) < 0 )
  {
    std::cerr << "Client Comm Channel: fcntl F_SETFL ERROR" << std::endl;
    exit( -1 );
  }
}

void printGraph( std::vector< std::vector< float > > graph )
{
  for( int i = 0; i < graph.size(); ++i )
  {
    for( int k = 0; k < graph.size(); ++k )
    {
      std::cout << "\t" << graph[i][k];
    }
    std::cout << std::endl;
  }

}
