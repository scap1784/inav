#include <iostream>
#include <string>
#include <vector>
#include <stdexcept>
#include <unistd.h>
/*Network Functions*/
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <fcntl.h>

//FD_SET
#include <sys/time.h>

//function prototypes
std::string parser( int argc, char *argv[], std::string option);
void printHelp();

int main( int argc, char *argv[] )
{

  std::string help = parser( argc, argv, "-h" );
  if( help.size() > 0 )
  {
    printHelp();
    exit( 0 );
  }

  std::string destinations = parser( argc, argv, "-d" );
  std::string sleepTime = parser( argc, argv, "-s" );
  std::string burstTime = parser( argc, argv, "-b" );
  int sleep = 0;
  int burst = 1;
  std::string ip;
  std::string slash;
  std::string packetToSend = "Scap's Traffic Generator! :) xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
  if( destinations.size() == 0 )
  {
    std::cerr << "USAGE: " << argv[0] << " -d x.x.x.x/yy" << std::endl;
  }
  int foundAt = destinations.find( "/" );
  if( foundAt == std::string::npos )
  {
    std::cerr << "USAGE: " << argv[0] << " -d x.x.x.x/yy" << std::endl;
  }
  slash = destinations.substr(foundAt + 1);
  ip = destinations.substr( 0 , foundAt );
  std::cout << "IP: " << ip << std::endl;
  uint32_t bits = atoi( slash.c_str() );
  uint32_t network = inet_addr( ip.c_str() );
  network = ntohl( network );

  int sd = socket( AF_INET, SOCK_DGRAM, IPPROTO_UDP );
  if( sd < 0 )
  {
    std::cerr << "ERROR creating socket" << std::endl;
    exit( -1 );
  }

  sockaddr_in sendAddress;
  sendAddress.sin_port = htons( 55000 );
  sendAddress.sin_family = AF_INET;

  uint32_t netmask = 0;
  for( int i = 0; i < bits; ++i )
  {
    netmask <<= 1;
    netmask += 1;
  }
  netmask <<= 32 - bits;
  network &=  netmask;
  uint32_t bitmask = 0xFFFFFFFF ^ netmask;
  uint32_t a = htonl( netmask );
  uint32_t b = htonl( bitmask );
  uint32_t c = htonl( network );
  std::cout << "NETMASK: " << inet_ntoa(  *((struct in_addr*) &a )) << std::endl;
  std::cout << "BITMASK: " << inet_ntoa(  *((struct in_addr*) &b )) << std::endl;
  std::cout << "NETWORK: " << inet_ntoa(  *((struct in_addr*) &c )) << std::endl;

  if( sleepTime.size() > 0 && sleepTime != "set" )
  {
    sleep = atoi( sleepTime.c_str() );
  }

  if( burstTime.size() > 0 && burstTime != "set" )
  {
    burst = atoi( burstTime.c_str() );
  }

  int count = 0;
  for ( ; bitmask > 0; --bitmask)
  {
    if( count == 0 )
    {
      usleep( sleep * 1000 );
    }
    sendAddress.sin_addr.s_addr = htonl(network | bitmask);
    sendto( sd, packetToSend.c_str(), packetToSend.size(), 0, (sockaddr*) &sendAddress, sizeof(sendAddress) );
    ++count;
    count %= burst+1;
  }
  return 0;
}

std::string parser(int argc, char *argv[], std::string option)
{
  const static std::string nullString;
  static std::vector<std::string> commandLineOptions;

  if( commandLineOptions.size() == 0 )
  {
    for( int i = 0; i < argc; ++i )
    {
      commandLineOptions.push_back( argv[i] );
    }
  }

  commandLineOptions.push_back("-");

  std::vector<std::string>::iterator itr;
  int foundAt;
  for( itr = commandLineOptions.begin(); itr != commandLineOptions.end(); ++itr )
  {
    foundAt = (*itr).find( option );
    if( foundAt != std::string::npos )
      break;
  }

  /* Debug Code
     std::cerr << "itr: " << *itr << std::endl;
     std::cerr << "foundAt: " << foundAt << std::endl;
   */

  if( itr != commandLineOptions.end() )
  {
    if( foundAt != std::string::npos )
    {
      try
      {
	if( (*itr).at(foundAt+option.size()) == '=' )
	  return (*itr).substr(foundAt + 1 + option.size());
      }
      catch( std::out_of_range error )
      {
	//This can happen and it is normal
      }
      try
      {
	if( (*(itr+1)).at(0) != '-' )
	  return *(itr+1);
      }
      catch( std::out_of_range error )
      {
	//This can happen and it is normal
      }
      return "set";
    }
  }

  return nullString;
}


void printHelp()
{
  std::cout << " -d a.b.c.d/xx" << std::endl;
  std::cout << " -s xx  (sleep time) " << std::endl;
  std::cout << " -b xx  (burst number) " << std::endl;
}

