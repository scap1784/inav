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


#ifndef SERVERSTATS_H 
#define SERVERSTATS_H

#include <iostream>
#include <deque>
#include <vector>
#include <string>
#include <sstream>
#include <pthread.h>
#include <sys/time.h>
#include "baseData.h"
#include "semaphore.h"

class ServerStats : public BaseData
{
	public:
		//Constructor
		ServerStats( pthread_mutex_t* coutMutex, pthread_mutex_t* logMutex,
				std::ofstream *log );

	private:
		uint32_t weights[6];//6seconds worth of weights weights[0] 0-1 second, weights[1] 1-2 second...
		time_t lastAccess;
};


#endif
