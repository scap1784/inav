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


#ifndef TRACEROUTEDATA_H
#define TRACEROUTEDATA_H

#include <vector>
#include <iostream>
#include <map>
#include <deque>


#include "../baseData.h"
#include "../../common/constants.h"
#include "../../common/threads.h"
#include "../valueEquals.h"


typedef struct TraceList
{ 
	int TTL;
	uint32_t hopIP;
	struct TraceList* next;
}TraceList;

typedef struct ipQueueData
{
	uint32_t destinationIP;
	int TTL;
} ipQueueData;

typedef struct traceroutesOutstanding
{
	uint32_t destinationIP;
	int TTL;
	time_t timestamp;
}traceroutesOutstanding;

class TracerouteData : public BaseData
{
	public:
		TracerouteData( Mutex coutMutex, Mutex logMutex, std::ofstream *log, 
				int maxTTL = traceroute::MAX_TTL );
		int getMaxTTL();
		void setMaxTTL( int timeToLive );
		void finish();//finish the traceroutes in the queue and stop
		void start();//restart running traceroutes if finish was called previously
		void returnWhenFinished( uint32_t ip ); //function returns when the traceroute for an ip is finished.
		void noMoreOutstanding(); //Tells the data structure that the there are no more outstanding and
		//to unlock the unlockWhenDone mutex. if finish
		void checkOutstanding( );

		//removes all entries older than timeout
		void removeTracerouteInfo( int timeout );
		void setTracerouteInfo( uint32_t ip, TraceList* hop );
		std::vector< uint32_t > getTracerouteInfo( uint32_t ); //returns a vector of size 0 if no data exists
		ipQueueData getNextInQueue();
		void pushInToQueue( uint32_t destinationIP );

		void setWaitMilliSeconds( int wait );
		void setWaitSeconds( int wait );
		void setMilliSecondDelay( int delay );
		void setNumberOfThreads( int num );
		int getWaitSeconds( );
		int getWaitMilliSeconds( );
		int getMilliSecondDelay( );
		int getNumberOfThreads( );

	private:
		bool findOutstanding( uint32_t ip );
	  void clearOutstanding( uint32_t ip, int TTL );
		void checkWhosDone();

		//variable
		bool finish_;//finish the traceroutes in the queue and stop
		int maxTimeToLive_;
		std::deque<ipQueueData> ipQueue_;
		std::deque<traceroutesOutstanding*> outstanding_;//outstanding traceroute
		std::vector< TraceList* > tracerouteLookupTable_;
		std::map < uint32_t, int > indexLookup_; // key-ip, value-index
		std::map < int, time_t > timestamp_; //key-index, value-time
		std::map < uint32_t, Mutex* > tracerouteFinished_; //mutex starts 
		//locked when a traceroute starts but is unlocked when finished
		int numberOfThreads_;
		int waitMilliSeconds_;
		int millisecondDelay_;
		int waitSeconds_;
		Mutex waitSecondsMutex_;
		Mutex waitMilliSecondsMutex_;
		Mutex numberOfThreadsMutex_;
		Mutex millisecondDelayMutex_;

		std::deque< int > deadIndexes_;
		int currentIndex_;
		Mutex outstandingMutex_;
		Mutex tracerouteFinishedMutex_;
		Mutex unlockWhenFinshed_;
		Mutex currentIndexMutex_;
		Mutex deadIndexesMutex_;
		Mutex ipQueueMutex_;
		Semaphore semaphore_;
		Mutex tracerouteLookupTableMutex_;
		Mutex indexLookupMutex_;
		Mutex timestampMutex_;
};

#endif
