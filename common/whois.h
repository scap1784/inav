/**
 * * INAV - Interactive Network Active-traffic Visualization
 * * Copyright © 2007  Ben Sanders 
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

#ifndef WHOIS_H
#define WHOIS_H

#include<string>
#include<sstream>
#include<utility>
#include<vector>
#include<fstream>

using namespace std;



struct ipAddress
{
  std::string strAddress;
  int numAddress[4];

  bool operator<=(const ipAddress &other) const
  {
    for(int i = 0; i < 4; i++)
    {
       if(numAddress[i] > other.numAddress[i])
       {
         return false;
       }
    }
    return true;
  }

  bool operator>=(const ipAddress &other) const
  {
    for(int i = 0; i < 4; i++)
    {
      if(numAddress[i] < other.numAddress[i])
      {
        return false;
      }
    }
    return true;
  }
};

struct whoisLookupNode
{
  ipAddress subnetMin;
  ipAddress subnetMax;
  std::string server;

  whoisLookupNode(ipAddress sMin, ipAddress sMax, std::string serv)
  {
    subnetMin = sMin;
    subnetMax = sMax;
    server = serv;
  }

};

class Whois
{
  public:
    Whois( );
    ~Whois( );
    std::string whois_Lookup(string ipAddr);
    std::string whois_Lookup(unsigned int ipAddr);

  private:
    std::vector< whoisLookupNode > whoisServers;

    std::pair<ipAddress, ipAddress> subnetRange(std::string subnet);
    void parseWhois(std::string serverListFile);

};



#endif
