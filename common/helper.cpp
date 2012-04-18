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
#include "helper.h"
#include <stdexcept>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>

std::string numToString( const uint32_t & a )
{
	std::string foo;
	foo.push_back( (char) a );
	foo.push_back( (char) (a >> 8) );
	foo.push_back( (char) (a >> 16) );
	foo.push_back( (char) (a >> 24) );
	return foo;
}

int stringToInt( std::string a )
{
  int num = 0;
  char *index = (char* ) &num ;
  index[0] = a.at( 0 );
  index[1] = a.at( 1 );
  index[2] = a.at( 2 );
  index[3] = a.at( 3 );
  return num;
  /*std::string::iterator itr;
  for( itr = a.begin(); itr != a.end(); ++itr )
  {
    num <<= 8;
    num += static_cast<int>( *itr );
  }
  return num;
  */
}

uint32_t stringToUInt( std::string a )
{
  uint32_t num = 0;
  char *index = (char* ) &num ;
  index[0] = a.at( 0 );
  index[1] = a.at( 1 );
  index[2] = a.at( 2 );
  index[3] = a.at( 3 );
  return num;
}


ipv4 stringToIPv4( std::string a )
{
  ipv4 ip;
  if( !inet_aton( a.c_str(), (struct in_addr*) &ip  ) )
  {
    throw std::runtime_error( "Invalid ip" );
  }
  return ip;
}

std::string ipv4ToString( ipv4 ip ) 
{
  return inet_ntoa( *((struct in_addr*)&ip) );
}

std::string ipv6ToString( ipv6 ip )
{
  //TODO
  return "TODO";
}

ipv6 stringToIPv6( std::string a )
{
  ipv6 ip;
  //TODO
  return ip;
}

