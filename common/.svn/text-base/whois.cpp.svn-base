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

#include"whois.h"
#include"network.h"
#include<iostream>

Whois::Whois()
{

  parseWhois("ip_del_list");

}

Whois::~Whois()
{
  //do nothing
}

std::string Whois::whois_Lookup(std::string ipAddr)
{
  /*
   * Test Code
   */
  Network net;
  net.connect("whois.verisign-grs.com",43);
  //std::cout << "connected" << std::endl;

  std::stringstream whois_query;
  whois_query << "=" <<  ipAddr << "\n";
  net.send(whois_query.str());
  //net.send("=google.com\n");
  //std::cout << "sent query, waiting for response" << std::endl;
  std::string output = net.recvAll();
  
  return output;

}


// TODO: Only works on IPv4, and is architecture dependent
std::string Whois::whois_Lookup(unsigned int ipAddr)
{
  stringstream strAddr;

  ipAddress ip;

  for(int i = 0; i < 4; i++)
  {
    unsigned int temp = ((0xFF000000 >> (i*8)) & ipAddr) >> ((3-i)*8);
    ipAddr &= (0x00FFFFFF >> (i*8));
    ip.numAddress[i] = temp;
    strAddr << temp;
    
    if(i != 3)
      strAddr << ".";
  }
  ip.strAddress = strAddr.str();
  
  std::string server;
  bool found = false;
  for(int i = 0; i < whoisServers.size(); i++)
  {
    if(ip >= whoisServers[i].subnetMin && ip <= whoisServers[i].subnetMax)
    {
       server = whoisServers[i].server;
       found = true;
       break;
    }
  }
  if(!found)
  {
    return "IP not found";
  }

  Network net;
  net.connect(server, 43);
  net.send(ip.strAddress + "\n");

  std::string output = net.recvAll();

  //std::cout << strAddr.str() << std::endl;

  //std::string subnetTest = "61.208.0.0/13";
  //std::pair<ipAddress, ipAddress> sub = subnetRange(subnetTest);
  //std::cout << subnetTest << " = " << sub.first << " - " << sub.second << std::endl;

  return output;
}

/*
 * Returns a pair of strings representing the minimum
 * and maximum IPs in the given subnet.
 * Example input: "192.168.1.0/24"
 * Example output: <"192.168.1.0","192.168.1.255">
 */
std::pair<ipAddress, ipAddress> Whois::subnetRange(std::string subnet)
{
  int powers[] = { 1, 2, 4, 8, 16, 32, 64, 128, 256 };

  unsigned int Amax,Bmax,Cmax,Dmax,mask;
  unsigned int Amin, Bmin, Cmin, Dmin;
  if(sscanf(subnet.c_str(), "%d.%d.%d.%d/%d", &Amax, &Bmax, &Cmax, &Dmax, &mask) == 5 && Amax < 256 && Bmax < 256 && Cmax < 256 && Dmax < 256 && mask < 32)
  {
    Amin = Amax;
    Bmin = Bmax;
    Cmin = Cmax;
    Dmin = Dmax;
    stringstream minIP;
    stringstream maxIP;
    if(mask >= 24)
    {
      Dmax |= (powers[32-mask] - 1);
      Dmin = Dmax ^ (powers[32-mask] - 1);
    }
    else if(mask >= 16)
    {
      Cmax |= (powers[24-mask] - 1);
      Dmax = 255;
      Cmin = Cmax ^ (powers[24-mask] - 1);
      Dmin = 0; 
    }
    else if(mask >= 8)
    {
      Bmax |= (powers[16-mask] - 1);
      Cmax = Dmax = 255;
      Bmin =  Bmax ^ (powers[16-mask] - 1);
      Cmin = Dmin = 0;
    }
    else
    {
      Amax |= (powers[8-mask] - 1);
      Bmax = Cmax = Dmax = 255;
      Amin = Amax ^ (powers[8-mask] - 1);
      Bmin = Cmin = Dmin = 0;
    }

    minIP << Amin << "." << Bmin << "." << Cmin << "." << Dmin;
    maxIP << Amax << "." << Bmax << "." << Cmax << "." << Dmax;

    ipAddress minAddr;
    minAddr.numAddress[0] = Amin;
    minAddr.numAddress[1] = Bmin;
    minAddr.numAddress[2] = Cmin;
    minAddr.numAddress[3] = Dmin;
    minAddr.strAddress = minIP.str();
    ipAddress maxAddr;
    maxAddr.numAddress[0] = Amax;
    maxAddr.numAddress[1] = Bmax;
    maxAddr.numAddress[2] = Cmax;
    maxAddr.numAddress[3] = Dmax;
    maxAddr.strAddress = maxIP.str();

    return make_pair(minAddr, maxAddr);

  }
  else
  {
    //TODO: Should throw an error instead of returning faulty data
    ipAddress error;
    return make_pair(error, error);
  }
}

void Whois::parseWhois(std::string serverListFile)
{
  std::ifstream fin(serverListFile.c_str());

  std::string line;
  getline(fin,line);
  while( !fin.eof() )
  {
    if(line.length() <= 0 || line[0] == '#')
    {
      //std::cout << line << std::endl;
      //invalid line or a comment, either way, ignore it
    }
    else
    {
      std::stringstream parser;
      std::string subnet, server;

      parser << line;
      parser >> subnet >> server;
      std::pair< ipAddress, ipAddress> sub = subnetRange(subnet);

      //find the real web address of the server
      if(server == "ripe")
      {
        server = "whois.ripe.net";
      }
      else if(server == "apnic")
      {
	server = "whois.apnic.net";
      }
      else if(server == "lacnic")
      {
	server = "whois.lacnic.net";
      }
      else if(server == "afrinic")
      {
	server = "whois.afrinic.net";
      }
      else if(server == "v6nic")
      {
	server = "whois.v6nic.net";
      }
      else if(server == "arin")
      {
	server = "whois.arin.net";
      }
      else if(server == "verio")
      {
	server = "whois.arin.net";
      }
      else if(server == "twnic")
      {
	server = "whois.twnic.net";
      }
      else if(server == "UNALLOCATED")
      {
	server = "whois.apnic.net";
      }

      whoisLookupNode node(sub.first,sub.second,server);
      whoisServers.push_back(node);
      //std::cout << sub.first.strAddress << " " << sub.second.strAddress << " " << server << std::endl;
    }

    getline(fin,line);
  }
}

