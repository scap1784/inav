/**
 * * INAV - Interactive Network Active-traffic Visualization
 * * Copyright © 2007  Nathan Robinson, Jeff Scaparra
 * * Copyright © 2008 Jeff Scaparra
 * *
 * * This file is a part of the INAV project.
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
#ifndef NETWORKTYPE_H
#define NETWORKTYPE_H
#include <vector>

typedef uint32_t ipv4;
typedef struct ipv6
{
  uint32_t ip[4];
}ipv6;

//Wondering if we should make these classes
class IPv6
{
  public:
    IPv6& operator=( const IPv6& a )
    {
      if( this != &a )
      {
	ip[0] = a.ip[0];
	ip[1] = a.ip[1];
	ip[2] = a.ip[2];
	ip[3] = a.ip[3];
      }
      return *this;
    }
    uint32_t ip[4];
};

class IPv4
{
  public:
    IPv4& operator=(const IPv4& rhs)
    {
      if( this != &rhs )
      {
	ip = rhs.ip;
      }
      return *this;
    }
    uint32_t ip;
};

#endif
