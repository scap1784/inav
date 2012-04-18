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


#include <pthread.h>
#include <arpa/inet.h>
#include "tracerouteThread.h"
#include "tracerouteData.h"
#include "../../common/commandLineParser.h"
#include "../../common/parseCommas.h"

namespace inav {
	const std::string logFile = "./inavd.log";

	/*Globals and not Constants */
	pthread_mutex_t coutMutex = PTHREAD_MUTEX_INITIALIZER;
	pthread_mutex_t logMutex = PTHREAD_MUTEX_INITIALIZER;
	std::ofstream log (logFile.c_str(), std::ios::out|std::ios::app);
}

void printRoute( std::string destination, std::vector< uint32_t > route );
std::vector< std::string > parseCommas( std::string destinations );

int main( int argc, char * argv[] )
{
  TracerouteData tracedata(  &inav::coutMutex, &inav::logMutex, &inav::log );
	tracedata.setNumberOfThreads( 10 );
	tracedata.setMilliSecondDelay( 50 );
	pthread_t* tracerouteThreadID = new pthread_t;
	int returnCode = pthread_create( tracerouteThreadID, NULL, tracerouteThread,(void*) &tracedata);

	std::string destinations = parser( argc, argv, "-d" );
	if( destinations == "set" || destinations.size() == 0 )
	{
		std::cerr << "No destinations given" << std::endl;
		std::cerr << "USEAGE: " << argv[0] << " -d x.x.x.x,y.y.y.y" << std::endl;
		exit( -1 );
	}
	std::vector< std::string > ips = parseCommas(destinations);
	std::vector< std::string >::iterator itr;
	for( itr = ips.begin(); itr != ips.end(); ++itr )
	{
		tracedata.pushInToQueue( inet_addr( (*itr).c_str() ) );
	}
	for( itr = ips.begin(); itr != ips.end(); ++itr )
	{
		tracedata.returnWhenFinished( inet_addr( itr->c_str() ) );
	}

	for( itr = ips.begin(); itr != ips.end(); ++itr )
	{
		std::vector< uint32_t > route = tracedata.getTracerouteInfo( inet_addr( (*itr).c_str() ) );
		printRoute( *itr, route );
	}
	//pthread_kill( tracerouteThreadID, 0 );
	return 0;
}

void printRoute( std::string destination, std::vector< uint32_t > route )
{
	std::cout << "Route to " << destination << ": " << std::endl;
	int hop = 1;
	std::vector< uint32_t >::iterator itr;
	if( route.size() == 0 )
	{
		std::cout << "No route found..." << std::endl;
	}
	for( itr = route.begin(); itr != route.end(); ++itr )
	{
		std::cout << "Hop " << hop << ": " << inet_ntoa(  *((struct in_addr*) &(*itr) )) << std::endl;
		++hop;
	}
}
