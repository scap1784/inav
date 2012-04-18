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


#include "tracerouteThread.h"
#include "tracerouteData.h"
#include <pthread.h>
#include <iostream>
#include <string>

//Network Functions
#include<sys/types.h>
#include<sys/socket.h>
#include<netinet/in.h>
#include<fcntl.h>
/*FD_SET*/
#include <sys/time.h>
//inav includes
#include "../icmp.h"
#include "../packet.h"

void *tracerouteThread( void * data )
{
	TracerouteData tracerouteData =  *((TracerouteData*)data);
	std::vector<pthread_t*> workerThreads;
	
	workerThreads.push_back( new pthread_t );
	int returnCode = pthread_create( workerThreads.at( workerThreads.size() - 1 ), NULL, 
			listenerThread,(void*) data );
	if( returnCode )
	{
		std::cerr << "TraceRoute Thread: Problem creating listener" << std::endl;
		exit( -1 );
	}

	for( int i = 0; i < tracerouteData.getNumberOfThreads(); ++i )
	{
		workerThreads.push_back( new pthread_t );
		int returnCode = pthread_create( workerThreads.at( i + 1 ), NULL, sendPing, (void*) data );
		if( returnCode )
		{
			std::cerr << "TraceRoute Thread: Problem creating workers" << std::endl;
			exit( -1 );
		}
	}

	//Wait for threads to exit
	std::vector<pthread_t*>::iterator itr;
	for( itr = workerThreads.begin(); itr != workerThreads.end(); ++itr )
	{
		pthread_join(**itr,NULL);
		delete *itr;
	}
	pthread_exit( NULL );
}

void *listenerThread( void *data )
{
	TracerouteData *tracerouteData = (TracerouteData*)data;

	int sd = socket( AF_INET, SOCK_RAW, IPPROTO_ICMP );
	if( sd < 0 )
	{
		std::cerr << "Traceroute Listener: Error opening socket (Perhaps you should be root?)" << std::endl;
		exit( -1 );
	}

	fd_set fd;
	uint8_t buff[ traceroute::MAX_REPLY_SIZE ];

	timeval timeout; 
	timeout.tv_sec = tracerouteData->getWaitSeconds();
	timeout.tv_usec = tracerouteData->getWaitMilliSeconds();
	FD_ZERO( &fd );
	FD_SET( sd, &fd );

	while ( true )
	{
		int socketAvailable = select( sd + 1, &fd, NULL, NULL, &timeout );
		if( socketAvailable == -1 )
		{
			std::cerr << "Traceroute Listener: Problem with select() " << std::endl;
			exit( -1 );
		}
		else if ( socketAvailable > 0 )
		{
			//Got an ICMP message
			int size = recvfrom( sd, buff, traceroute::MAX_REPLY_SIZE, 0, NULL, NULL);
			//Packet packet( buff, size );
			Packet packet;
			packet.startAtIPHeader( buff, size );

			/* DEBUG
			if( packet.isIP() )
				std::cerr << "IP Packet" << std::endl;
			if( packet.isICMP() )
				std::cerr << "ICMP Packet :) " << std::endl;
			if( packet.isTCP() )
				std::cerr << "TCP PACKET :( " << std::endl;
			*/

			if( packet.getICMPType() == net::TimeOut && packet.getICMPCode() == 0 )
			{
				//is of the right type need to check the indent and sequence numbers
				Packet orginal = packet.getOrginalPacket();
				
				if( orginal.getICMPIdentifier() == getpid() )
				{
					//this is for us yea!!!
					TraceList *hop = new TraceList;
					hop->TTL = orginal.getICMPSequenceNum();
					hop->hopIP = packet.getSourceAddress();
					hop->next = NULL;
					tracerouteData->setTracerouteInfo( orginal.getDestinationAddress(), hop );
				}
			}
			else if ( packet.getICMPType() == net::EchoReply )
			{
				std::cout << "PONG" << std::endl;
				//could be a ping response
				if( packet.getICMPIdentifier() == getpid() )
				{
					std::cout << "Hello" << std::endl;
					//this is for us
					TraceList *hop = new TraceList;
					hop->TTL = packet.getICMPSequenceNum();
					hop->hopIP = packet.getSourceAddress();
					hop->next = NULL;
					tracerouteData->setTracerouteInfo( packet.getSourceAddress(), hop );
				}
					
			}

		}

		//Remove all traceroutes older than 1 hour
		tracerouteData->removeTracerouteInfo( traceroute::TRACEROUTE_TTL ); 
		//Clean up the outstanding traceroutes
		tracerouteData->checkOutstanding();

		//int replySize = recvfrom( sd, buff, traceroute::MAX_REPLY_SIZE, 0, NULL, NULL ); 
	}
	pthread_exit( NULL );
}

void *sendPing( void *data )
{
	TracerouteData *tracerouteData = (TracerouteData*)data;

	int sd = socket( AF_INET, SOCK_RAW, IPPROTO_ICMP );
	if( 0 == sd )
	{
		std::cerr << "Traceroute sendPing(): Socket Creation Failed" << std::endl;
		exit( -1 );
	}

	while( true )
	{
		ICMP icmpPacket;
		ipQueueData tmp = tracerouteData->getNextInQueue();
		if( -1 == setsockopt( sd, IPPROTO_IP, IP_TTL, (const uint8_t*) &tmp.TTL, sizeof( tmp.TTL ) ))
		{
			std::cerr << "Traceroute sendPing(): Socket Option Failed" << std::endl;
			exit( -1 );
		}
		icmpPacket.setType( net::EchoRequest );
		icmpPacket.setCode( 0 );
		icmpPacket.setIdentifier( getpid() );
		icmpPacket.setSequenceNum( tmp.TTL );
		icmpPacket.generateChecksum();
		std::string PacketToSend = icmpPacket.getPacket();

		sockaddr_in sendAddress;
		sendAddress.sin_addr.s_addr = tmp.destinationIP;
		sendAddress.sin_family = AF_INET;

		usleep( tracerouteData->getMilliSecondDelay() );

		sendto( sd, PacketToSend.c_str(), PacketToSend.size(), 0, (sockaddr*) & sendAddress, sizeof(sendAddress));
	}

	pthread_exit( NULL );
}
