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


#include <iostream>
#include <string>
#include <sstream>
#include <fstream>
//network functions
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
//inav files
#include "../../common/constants.h"
#include "../../common/commandLineParser.h"
#include "../../common/helper.h"

std::string printConfigData( int sd );
std::string printEdges( int sd );
std::string printNodeData( int sd );
std::string printFilterTypes( int sd );
void printHelp();
std::string printRemoved( int sd );
void sendNodeData( std::string parsedString, int sd );
void sendConfig( std::string parsedString, int sd );

bool zeroWeights = false;
bool debug = false;
bool blankScreen = false;
bool deadPackets = false;

int main( int argc, char *argv[] )
{
  int port = 5000;
  std::string server = "127.0.0.1";
  std::string parsedString = parser( argc, argv, "-h" );
  if( parsedString.size() > 0 )
  {
    printHelp();
    exit( 0 );
  }
  parsedString = parser( argc, argv, "--help" );
  if( parsedString.size() > 0 )
  {
    printHelp();
    exit( 0 );
  }
  parsedString = parser( argc, argv, "-s" );
  if( parsedString.size() > 0 )
    server = parsedString;
  parsedString = parser( argc, argv, "-p" );
  if( parsedString.size() > 0 )
    port = atoi( parsedString.c_str() );
  parsedString = parser( argc, argv, "-w" );
  if( parsedString.size() > 0 )
    zeroWeights = true;
  parsedString = parser( argc, argv, "-D" );
  if( parsedString.size() > 0 )
    debug = true;
  parsedString = parser( argc, argv, "-b" );
  if( parsedString.size() > 0 )
    blankScreen = true;
  parsedString = parser( argc, argv, "-d" );
  if( parsedString.size() > 0 )
  {
    std::cout << "displaying removed edges only..." << std::endl;
    deadPackets = true;
  }

  int sd = socket( AF_INET, SOCK_STREAM, 0);
  if ( 0 == sd )
  {
    std::cerr << "Couldn't start socket" << std::endl;
    exit( -1 );
  }

  struct sockaddr_in inavAddress;
  inavAddress.sin_family = AF_INET;
  inavAddress.sin_addr.s_addr = inet_addr( server.c_str() );
  inavAddress.sin_port = htons( port );

  int err = connect( sd, (struct sockaddr*) &inavAddress, sizeof( inavAddress ) );
  if( 0 > err )
  {
    std::cerr << "Connect failed!" << std::endl;
    exit( -1 );
  }

  parsedString = parser( argc, argv, "-c" );
  if( parsedString.size() > 0 )
  {
    sendConfig( parsedString, sd );
    exit( 0 );
  }
  parsedString = parser( argc, argv, "-n" );
  if( parsedString.size() > 0 && parsedString != "set" )
  {
    sendNodeData( parsedString, sd );
    exit( 0 );
  }

  while( true )
  {
    char type;
    int size = recv( sd, &type, 1, 0 );
    if ( size == 0 )
    {
      break;
    }


    std::string output;
    switch( type )
    {
      case communication::Config: output = printConfigData( sd );
				  break;
      case communication::GraphRefresh: output = printEdges( sd );
					break;
      case communication::NodeData: output = printNodeData( sd );
				    break;
      case communication::FilterTypes: output = printFilterTypes( sd );
				       break;
      case communication::RemovedEdges: output = printRemoved( sd );
					break;
      default: std::cout << "Unknown Channel Mode" << std::endl;
	       break;
    };

    if( deadPackets )
    {
      //only output type 5 junk
      if( type == communication::RemovedEdges )
	std::cout << output;
    }
    else if( zeroWeights )
    {
      if( type == communication::GraphRefresh )
	std::cout << output;
    }
    else
    {
      std::cout << output << std::endl;
    }

  }

  return 0;
}

void sendNodeData( std::string parsedString, int sd )
{
  std::cout << "Requesting node data for: " <<  parsedString << std::endl;
  uint32_t ip = (uint32_t)inet_addr( parsedString.c_str() );
  std::string dataToSend;
  dataToSend.push_back( communication::NodeData );
  dataToSend += numToString( ip );
  int size = send( sd, dataToSend.c_str(), dataToSend.size(), 0 );
  std::string received;

  uint8_t type;
  while(received.size() == 0 )
  {
    size = recv( sd, &type, 1, 0 );
    if ( size == 0 )
    {
      std::cerr << "sendConfig: Problem with recv()" << std::endl;
      exit( -1 );
    }
    switch( type )
    {
      case communication::Config: printConfigData( sd );
				  break;
      case communication::GraphRefresh:  printEdges( sd );
					 break;
      case communication::NodeData: received = printNodeData( sd );
				    break;
      case communication::FilterTypes:  printFilterTypes( sd );
					break;
      case communication::RemovedEdges: printRemoved( sd );
					break;
      default: 
					break;
    };
  }
  close( sd );
  std::cout << "NodeData: " << std::endl;
  std::cout << received << std::endl;
}

void sendConfig( std::string parsedString, int sd )
{
  std::stringstream input;
  std::stringstream output;

  if( parsedString == "set" )
  {
    //use stdin
    input << std::cin.rdbuf();
  }
  else
  {
    std::ifstream file(parsedString.c_str());
    input << file.rdbuf();
  }
  uint8_t type;
  int size = recv( sd, &type, 1, 0 );
  if ( size == 0 )
  {
    std::cerr << "sendConfig: Problem with recv()" << std::endl;
    exit( -1 );
  }

  output << "Input from user: " << std::endl;
  output << input.str() << std::endl << std::endl;

  std::string config;
  switch( type )
  {
    case communication::Config: config = printConfigData( sd );
				break;
    default: std::cout << "Send Config: Unknown Channel Mode" << std::endl;
	     exit( -1 );
	     break;
  };

  output << "Orginal Config from server: " << std::endl;
  output << config << std::endl << std::endl;

  //send the new config now.

  std::string dataToSend;
  dataToSend.push_back( communication::Config );
  dataToSend += numToString( htonl( input.str().size() ));
  dataToSend += input.str();
  size = send( sd, dataToSend.c_str(), dataToSend.size(), 0 );
  if( size != dataToSend.size() )
  {
    std::cerr << "sendConfig(): Problem sending data" << std::endl;
    exit( -1 );
  }

  output << "Awaiting a response from the server..." << std::endl;
  std::cout << output.str() << std::endl;
  output.clear();

  config.clear();

  while(config.size() == 0 )
  {
    size = recv( sd, &type, 1, 0 );
    if ( size == 0 )
    {
      std::cerr << "sendConfig: Problem with recv()" << std::endl;
      exit( -1 );
    }
    switch( type )
    {
      case communication::Config: config = printConfigData( sd );
				  break;
      case communication::GraphRefresh:  printEdges( sd );
					 break;
      case communication::NodeData:  printNodeData( sd );
				     break;
      case communication::FilterTypes:  printFilterTypes( sd );
					break;
      case communication::RemovedEdges: printRemoved( sd );
					break;
      default: 
					break;
    };
  }

  output << "A response: " << std::endl;
  output << config << std::endl;
  std::cout << output.str() << std::endl;
  close( sd );
}

std::string printConfigData( int sd )
{
  int size;
  recv( sd, &size, 4, 0 );
  size = ntohl( size );
  std::stringstream output;
  while( size > 0 )
  {
    char buff[size];
    int received = recv( sd, buff, size, 0 );
    output << buff;
    size = size - received;
  }
  output << std::endl;
  return output.str();
}

std::string printRemoved( int sd )
{
  std::stringstream output;
  if( blankScreen )
  {
    for( int i = 0; i < 150; ++i )
      output << std::endl;
  }
  char numberOfEdges[4];

  int size = recv( sd, &numberOfEdges, 4, 0 );
  if( size != 4 )
  {
    output << "ERROR: NumberofEdges not sent!"  << std::endl;
  }

  int sizeOfData = ntohl( *((uint32_t*)numberOfEdges) );
  output << "numberOfDeadEdges: " << sizeOfData << std::endl;
  sizeOfData *= 8;
  uint8_t buff[ sizeOfData ];

  size = 0;
  while ( size < sizeOfData )
  {
    int received = recv( sd, buff + size, sizeOfData - size, 0 );
    size += received;
  }

  output << "Size of Dead Data: " << sizeOfData << std::endl;

  for( int i = 0; i < sizeOfData; i+=8 )
  {
    uint32_t* ptr = (uint32_t*)(buff+i);
    output << inet_ntoa( *((struct in_addr*)(ptr)) ) << " -> ";
    output << inet_ntoa( *((struct in_addr*)(ptr+1)) ) << std::endl;
  }
  if(sizeOfData == 0 )
    return std::string();
  return output.str();
}

std::string printEdges( int sd )
{
  std::stringstream output;
  if( blankScreen )
  {
    for( int i = 0; i < 150; ++i )
      output << std::endl;
  }
  char numberOfEdges[4];

  int size = recv( sd, &numberOfEdges, 4, 0 );
  if( size != 4 )
  {
    output << "ERROR: NumberofEdges not sent!"  << std::endl;
  }

  int sizeOfData = ntohl( *((uint32_t*)numberOfEdges) );
  output << "numberOfEdges: " << sizeOfData << std::endl;
  sizeOfData *= 12;
  uint8_t buff[ sizeOfData ];

  size = 0;
  while ( size < sizeOfData )
  {
    int received = recv( sd, buff + size, sizeOfData - size, 0 );
    size += received;
  }

  output << "Size of Graph: " << sizeOfData << std::endl;

  for( int i = 0; i < sizeOfData; i+=12 )
  {
    uint32_t* ptr = (uint32_t*)(buff+i);
    if( (zeroWeights == true) &&( ptr[2] == 0 ) )
      continue;
    output << inet_ntoa( *((struct in_addr*)(ptr)) ) << " -> ";
    output << inet_ntoa( *((struct in_addr*)(ptr+1)) ) << " with weight: ";
    output << ntohl( ptr[2] ) << std::endl;
  }

  if( debug ) 
  {
    output << std::endl << std::endl << " HEX: " <<std::endl;
    output << std::hex;

    int newLine = 1;
    for( int i = 0; i < sizeOfData; i+=4 )
    {
      uint32_t tmp = *((uint32_t*)(buff+i));
      tmp = ntohl( tmp );
      output << " " << tmp << " ";
      if( newLine % 3 == 0 )
      {
	output << std::endl;
      }
      else
      {
	output << "\t";
      }
      ++newLine;
    }
    output << std::dec;
  }
  return output.str();
}

std::string printNodeData( int sd )
{
  return printConfigData( sd );
}

std::string printFilterTypes( int sd )
{
  std::stringstream output;
  output << "TODO: Filter Type" << std::endl;
  return output.str();
}

void printHelp( )
{
  std::cout << "Options" << std::endl;
  std::cout << "-h" << std::endl;
  std::cout << "--help          This menu" << std::endl;
  std::cout << "-s <x.x.x.x>    server option -s <x.x.x.x>" << std::endl;
  std::cout << "-p <portNum>    port option -p <portNum>" << std::endl;
  std::cout << "-w              Display only non-zero weight edges" << std::endl;
  std::cout << "-b              Blank the screen before each display" << std::endl;
  std::cout << "-d              Only print out deadnodes (type 5 packets)" << std::endl;
  std::cout << "-n <x.x.x.x>    Request node data for x.x.x.x" << std::endl;
  std::cout << "-c <filename>   Send a configuration change to the server" << std::endl;
  std::cout << "                If no filename is given stdin is used" << std::endl;
}
