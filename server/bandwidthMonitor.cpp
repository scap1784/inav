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
#include<string>
#include<stdlib.h>

#include<sys/time.h>
#include<arpa/inet.h>
/*THREADS*/
#include<pthread.h>

#include "bandwidthMonitor.h"
#include "packet.h"

void *bandwidthMonitor(void *bandwidthDataHolder )
{
	FilterData *filterData = ((BandwidthDataHolder*)bandwidthDataHolder)->filterData;
	GraphData *graphData =((BandwidthDataHolder*)bandwidthDataHolder)->graphData;
	
	if ( filterData == NULL )
	{
		std::cerr << "Bandwidth Monitor ERROR: filterData == NULL" << std::endl;
		exit( -1 );
	}

	if ( graphData == NULL )
	{
		std::cerr << "Bandwidth Monitor ERROR: graphData == NULL" << std::endl;
		exit( -1 );
	}
	filterData->log( "Bandwidth Monitor Started!" ); 
	
	while( true )
	{
		Packet packet = filterData->popPacket();
 		//graphData->addEdge( packet.getSourceAddress(), packet.getDestinationAddress(), packet.getActualSize() );
		graphData->addEdge( packet );
	}

	filterData->log( "Bandwidth Monitor Stopping!" );
	pthread_exit(NULL);
}
