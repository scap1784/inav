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


#include "debugThread.h"
#include <unistd.h>

void *debugThread( void *data )
{
	DebugData* debugData = (DebugData*)data;
	int sleepTime = 2;

	while( true )
	{
		std::cout << "FilterData has " << debugData->filterData->size() << " in the queue." << std::endl;
		std::cout << "GraphData has " << debugData->graphData->getNumEdges() << " edges in the graph" << std::endl;
		std::cout << "GraphData has " << debugData->graphData->getNumDeadNodes() << " dead nodes" << std::endl;
		sleep( sleepTime );
	}
	return NULL;
}
	

