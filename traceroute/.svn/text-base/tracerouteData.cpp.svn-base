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


#include "tracerouteData.h"
#include <iostream>
#include <stdexcept>
TracerouteData::TracerouteData( Mutex coutMutex, Mutex logMutex, 
		std::ofstream *log, int maxTTL ): BaseData( coutMutex, logMutex, log ), maxTimeToLive_(maxTTL)
{
	finish_ = false;
	currentIndex_ = 1;
	Semaphore semaphore_;
	numberOfThreads_ = maxTTL;
	millisecondDelay_ = 0;
	waitMilliSeconds_ = 0;
	waitSeconds_ = traceroute::ICMPTIMEOUT;
	unlockWhenFinshed_.lock( );
	tracerouteLookupTable_.push_back( NULL ); //this is so when the index map 
																						//doesn't hold a value it will pull up nothing. 
}

int TracerouteData::getMaxTTL()
{
	return maxTimeToLive_;
}

void TracerouteData::setMaxTTL( int timeToLive )
{
	maxTimeToLive_ = timeToLive;
}

void TracerouteData::finish()
{
	finish_ = true;
}

void TracerouteData::start()
{
	unlockWhenFinshed_.lock( );
	finish_ = false; 
}

void TracerouteData::noMoreOutstanding()
{
	if( finish_ )
		unlockWhenFinshed_.unlock( );
}

std::vector< uint32_t > TracerouteData::getTracerouteInfo( uint32_t ip )
{
	tracerouteLookupTableMutex_.lock( );
	indexLookupMutex_.lock();
	int index = indexLookup_[ ip ];
	indexLookupMutex_.unlock( );

	TraceList *path;
	std::vector< uint32_t >route;
	try
	{
	  path = tracerouteLookupTable_.at( index );
	}
	catch( std::out_of_range )
	{
		return route;
	}

	if( path == NULL )
	{
		std::cerr << "Inital path == NULL" << std::endl;
	}
	
	uint32_t lastHop = 0;
	while( path != NULL && lastHop != path->hopIP)
	{
		route.push_back( path->hopIP );
		lastHop = path->hopIP;
		path=path->next;
	}
	
	tracerouteLookupTableMutex_.unlock( );
	std::cerr << "route.size() = " << route.size() << std::endl;
	return route;
}

void TracerouteData::removeTracerouteInfo( int timeout )
{
	time_t currentTime = time( NULL );
	currentTime = currentTime - timeout;
	timestampMutex_.lock( );
	indexLookupMutex_.lock( );
	std::map<int, time_t>::iterator pos = std::find_if( timestamp_.begin(),
								//timestamp_.end(), value_equals<int,time_t>(currentTime));
			        timestamp_.end(), value_equal_or_less<int,time_t>(currentTime));
	while ( pos != timestamp_.end() )
	{
		int key = pos->first;
		timestamp_.erase(key);
		
		std::map<uint32_t, int>::iterator index = std::find_if( indexLookup_.begin(), 
				indexLookup_.end(), value_equals< uint32_t, int >( key ));
		uint32_t i = index->first;
		indexLookup_.erase(i);
		tracerouteLookupTableMutex_.lock( );

		TraceList *tmp = tracerouteLookupTable_[key];
		while( tmp != NULL )
		{
			TraceList *next;
			next = tmp->next;
			delete tmp;
			tmp = next;
		}
		tracerouteLookupTable_[key] = NULL;
		
		deadIndexesMutex_.lock( );
		deadIndexes_.push_back(key);
		deadIndexesMutex_.unlock( );

		tracerouteLookupTableMutex_.unlock( );

		pos = std::find_if( pos, timestamp_.end(), value_equal_or_less< int, time_t>( currentTime ));
	}
	indexLookupMutex_.unlock( );
	timestampMutex_.unlock( );
}

void TracerouteData::setTracerouteInfo( uint32_t ip, TraceList* hop )
{
	//int index = getIndex( ip );
	int index = indexLookup_[ ip ];
	std::cerr << "index = " << index << std::endl;
	if( index != 0 )
	{
		tracerouteLookupTableMutex_.lock();
		TraceList *tmp = tracerouteLookupTable_[index];
		if( tmp == NULL )
		{
			tracerouteLookupTable_[index] = hop;
		}
		else
		{
			if( tmp->TTL > hop->TTL )
			{
				hop->next = tmp;
				tracerouteLookupTable_[index] = hop;
			}
			else
			{
				TraceList *previous;
				while( tmp != NULL && tmp->TTL < hop->TTL )
				{
					previous=tmp;
					tmp=tmp->next;
				}
				previous->next = hop;
				hop->next = tmp;
			}
		}
		tracerouteLookupTableMutex_.unlock( );
	}
	clearOutstanding( ip, hop->TTL );
}

void TracerouteData::returnWhenFinished( uint32_t ip )
{
	std::cout << "IP: " << ip << std::endl;
	tracerouteFinishedMutex_.lock( );
	Mutex *tmp = tracerouteFinished_[ ip ];
	tracerouteFinishedMutex_.unlock( );
	if( tmp == NULL )
	{
		std::cout << "NULL CRAP" << std::endl;
		return;
	}
	tmp->lock();
	tmp->unlock();
	delete tmp;
	tracerouteFinishedMutex_.lock( );
	tracerouteFinished_.erase( ip );
	tracerouteFinishedMutex_.unlock( );
}

void TracerouteData::clearOutstanding( uint32_t ip, int TTL )
{
	traceroutesOutstanding* tmp;
	std::deque< traceroutesOutstanding* > placeholder;
	outstandingMutex_.lock( );
	while( outstanding_.size() > 0 )
	{
		if( outstanding_.at( 0 )->TTL == TTL && outstanding_.at( 0 )->destinationIP == ip )
		{
			//delete element
			delete outstanding_.at( 0 );
			outstanding_.pop_front();
			break;
		}
		else
		{
			placeholder.push_back( outstanding_.at( 0 ) );
			outstanding_.pop_front();
		}
	}
	//put all the non effected back
	std::deque< traceroutesOutstanding* >::reverse_iterator itr;
	for( itr = placeholder.rbegin(); itr != placeholder.rend(); ++itr )
	{
		outstanding_.push_front( *itr );
	}
	outstandingMutex_.unlock( );
}

void TracerouteData::checkOutstanding( )
{
	//Check timestamps on remaining to ensure none have timedout...
	uint32_t ip = 0;
	time_t currentTime = time( NULL );
	currentTime -= traceroute::ICMPTIMEOUT;
	outstandingMutex_.lock( );
	while( outstanding_.size() > 0 && outstanding_.at( 0 )->timestamp < currentTime )
	{
		outstanding_.pop_front();
	}
	outstandingMutex_.unlock( );

	checkWhosDone();

}

void TracerouteData::checkWhosDone()
{
	std::map<uint32_t, Mutex*>::iterator itr;
	tracerouteFinishedMutex_.lock( );
	for( itr = tracerouteFinished_.begin(); itr != tracerouteFinished_.end(); ++itr )
	{
		if(  (itr->second)->trylock() == 0 )
		{
			//already finished but not retervied
			(itr->second)->unlock();
			continue;
		}
		else
		{
			std::deque< traceroutesOutstanding* >::iterator it;
			outstandingMutex_.lock( );
			for( it = outstanding_.begin(); it != outstanding_.end(); ++it )
			{
				if( (*it)->destinationIP == itr->first )
					break;
			}
			if( it == outstanding_.end() )
			{
				( itr->second )->unlock();
			}
			outstandingMutex_.unlock( );
		}
	}
	tracerouteFinishedMutex_.unlock( );
}

bool TracerouteData::findOutstanding( uint32_t ip )
{
	std::deque< traceroutesOutstanding* >::iterator itr;
	outstandingMutex_.lock( );
	for( itr = outstanding_.begin(); itr != outstanding_.end(); ++itr )
	{
		if( (*itr)->destinationIP == ip )
		{
			outstandingMutex_.unlock();
			return true;
		}
	}
	outstandingMutex_.unlock();
	return false;
}
	
ipQueueData TracerouteData::getNextInQueue()
{
	traceroutesOutstanding *outstanding = new traceroutesOutstanding;
	semaphore_.wait();
	ipQueueMutex_.lock();
	ipQueueData tmp = ipQueue_.at( 0 );
	ipQueue_.pop_front();
	ipQueueMutex_.unlock();

	outstanding->destinationIP = tmp.destinationIP;
	outstanding->TTL = tmp.TTL;
	outstanding->timestamp = time( NULL );
	outstandingMutex_.lock();
	outstanding_.push_back( outstanding );
	outstandingMutex_.unlock();

	return tmp;
}

//returns true if found
bool findIP( std::deque<ipQueueData> queue, uint32_t ip )
{
	std::deque<ipQueueData>::iterator pos;
	for( pos = queue.begin(); pos != queue.end(); ++pos )
	{
		if( (*pos).destinationIP == ip )
		{
			return true;
		}
	}
	return false;
}

void TracerouteData::pushInToQueue( uint32_t destinationIP )
{
	if( finish_ )
		throw std::runtime_error( "Traceroute Data not accepting more lookups" );
	ipQueueData tmp;
	tmp.destinationIP = destinationIP;
	tmp.TTL = 0;
	//see if we already have data...
	indexLookupMutex_.lock();
	int index = indexLookup_[ destinationIP ];
	indexLookupMutex_.unlock();
	if( index != 0 )
	{
		//already preforming lookup or lookup already done..
		return;
	}
	//need to save an index
	indexLookupMutex_.lock();
	tracerouteLookupTableMutex_.lock();
	tracerouteLookupTable_.push_back( NULL );
	index = tracerouteLookupTable_.size() - 1;
	tracerouteLookupTableMutex_.unlock();
	indexLookup_[destinationIP] = index;
	indexLookupMutex_.unlock();
	ipQueueMutex_.lock();
  if( !findIP( ipQueue_, destinationIP ) )
	{
		for(tmp.TTL = 1; tmp.TTL < maxTimeToLive_ + 1 ; ++tmp.TTL )
		{
			ipQueue_.push_back( tmp );
			semaphore_.post();
		}
	}
	ipQueueMutex_.unlock();
	
	Mutex* mutexPtr = new Mutex;
	mutexPtr->lock( );
	tracerouteFinishedMutex_.lock();
	tracerouteFinished_[destinationIP] = mutexPtr;
	tracerouteFinishedMutex_.unlock();
}

int TracerouteData::getNumberOfThreads( )
{
	numberOfThreadsMutex_.lock();
	int tmp = numberOfThreads_;
	numberOfThreadsMutex_.unlock();
	return tmp;
}

void TracerouteData::setNumberOfThreads( int num )
{
	numberOfThreadsMutex_.lock();
	numberOfThreads_ = num;
	numberOfThreadsMutex_.unlock();
}

int TracerouteData::getMilliSecondDelay()
{
	millisecondDelayMutex_.lock();
	int tmp = millisecondDelay_;
	millisecondDelayMutex_.unlock();
	return tmp;
}

void TracerouteData::setMilliSecondDelay( int delay )
{
	millisecondDelayMutex_.lock();
	millisecondDelay_ = delay;
	millisecondDelayMutex_.unlock();
}

void TracerouteData::setWaitSeconds( int wait )
{
	waitSecondsMutex_.lock();
	waitSeconds_ = wait;
	waitSecondsMutex_.unlock();
}

int TracerouteData::getWaitSeconds( )
{
	waitSecondsMutex_.lock();
	int returnValue = waitSeconds_;
	waitSecondsMutex_.unlock();
	return returnValue;
}

void TracerouteData::setWaitMilliSeconds( int wait )
{
	waitMilliSecondsMutex_.lock();
	waitMilliSeconds_ = wait;
	waitMilliSecondsMutex_.unlock();
}

int TracerouteData::getWaitMilliSeconds( )
{
	waitMilliSecondsMutex_.lock();
	int returnValue = waitMilliSeconds_;
	waitMilliSecondsMutex_.unlock();
	return returnValue;
}
