/**
 * * INAV - Interactive Network Active-traffic Visualization
 * * Copyright © 2007  Nathan Robinson, Jeff Scaparra, Ben Sanders
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

#include "network.h"
#include "constants.h"
#include "helper.h"
#include <stdexcept>
//network functions
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
//#include <arpa/inet.h>
#include <iostream>
#include <sstream>
#include <errno.h>


extern int errno;

Network::Network( NetworkProtocol networkProtocol ) : networkProtocol_( networkProtocol )
{
  socket_ = -1;
  /*
  if( networkProtocol_ == TCP )
  {
    socket_ = socket( AF_INET, SOCK_STREAM, 0 );
    if ( socket_ == 0 )
      throw std::runtime_error( "Cannot create TCP socket" );
  }
  else if ( networkProtocol_ == UDP )
  {
    socket_ = socket( AF_INET, SOCK_DGRAM, 0 );
    if ( socket_ == 0 )
      throw std::runtime_error( "Cannot create UDP socket" );
  }
  */

}

Network::Network( const Network & network )
{
  networkProtocol_ = network.networkProtocol_;
  socket_ = network.socket_;
}

Network::~Network()
{
  if( socket_ >= 0 )
    close( );
}

std::string Network::printErrorMsg( int error )
{
  std::string errorMsg;

  switch( errno  )
  {
    case ECONNREFUSED:
      std::cerr << "Socket: Connection Refused" << std::endl;
      errorMsg = "Socket: Connection Refused";
      break;
    case ENOTCONN:
      std::cerr << "Socket: Not Connected" << std::endl;
      errorMsg = "Socket: Not Connected";
      break;
    case EAGAIN:
      std::cerr << "Socket: errno == EAGAIN" << std::endl;
      errorMsg = "Socket: errno == EAGAIN";
      break;
    case EBADF:
      std::cerr << "Socket: Invalid socket descriptor" << std::endl;
      errorMsg = "Socket: Invalid socket descriptor";
      break;
    case EFAULT:
      std::cerr << "Socket: receive buffer outside of process address space" << std::endl;
      errorMsg = "Socket: receive buffer outside of process address space";
      break;
    case EINVAL:
      std::cerr << "Socket: Invalid argument passed" << std::endl;
      errorMsg = "Socket: Invalid argument passed";
      break;
    case EINTR:
      std::cerr << "Socket: Interrupted by signal" << std::endl;
      errorMsg = "Socket: Interrupted by signal";
      break;
    case ENOMEM:
      std::cerr << "Socket: Could not allocate memory" << std::endl;
      errorMsg = "Socket: Could not allocate memory";
      break;
    case ENOTSOCK:
      std::cerr << "Socket: socket argument not valid" << std::endl;
      errorMsg = "Socket: socket argument not valid";
      break;
    case 104:
      std::cerr << "Socket: Connection Reset by Peer" << std::endl;
      errorMsg = "Socket: Connection Reset by Peer";
      break;
    default:
      std::cerr << "Socket: unknown errno=" << errno << std::endl;
      errorMsg = "Socket: unknown error";
  };
  return errorMsg;
}

void Network::listen( int Port )
{
}


void Network::setBlocking()
{
  int flags;

  /* If they have O_NONBLOCK, use the Posix way to do it */
  #if defined(O_NONBLOCK)
  /* Fixme: O_NONBLOCK is defined but broken on SunOS 4.1.x and AIX
   * 3.2.5. */
  if ( -1 == (flags = fcntl( socket_ , F_GETFL, 0)))
    flags = 0;
  if ( -1 == fcntl( socket_ , F_SETFL, flags & (0xFFFFFFFF ^ O_NONBLOCK) ) )
    throw std::runtime_error( "Problems creating blocking socket" );
  #else
  /* Otherwise, use the old way of doing it */
  flags = 0;
  if( -1 == ioctl( socket_ , FIOBIO, &flags) )
    throw std::runtime_error( "Problems creating blocking socket" );
  #endif
}

void Network::setNonblocking( )
{
  int flags;

  /* If they have O_NONBLOCK, use the Posix way to do it */
  #if defined(O_NONBLOCK)
  /* Fixme: O_NONBLOCK is defined but broken on SunOS 4.1.x and AIX
   * 3.2.5. */
  if ( -1 == (flags = fcntl( socket_ , F_GETFL, 0)))
    flags = 0;
  if ( -1 == fcntl( socket_ , F_SETFL, flags | O_NONBLOCK) )
    throw std::runtime_error( "Problems creating non-blocking socket" );
  #else
  /* Otherwise, use the old way of doing it */
  flags = 1;
  if ( -1 == ioctl( socket_ , FIOBIO, &flags) )
    throw std::runtime_error( "Problems creating non-blocking socket" );
  #endif
}   

void Network::connect( std::string server, int port )
{
  std::stringstream s;
  s << port;
  connect( server, s.str() );
}

void Network::checkServerString( std::string server)
{
  std::string::iterator chr; 
  for( chr = server.begin(); chr != server.end(); ++chr )
  {
    if( *chr == ' ' )
    {
      std::cerr << "DIE" << std::endl;
      throw std::runtime_error( "Bad Servername"  );
    }
  }
}

void Network::checkPortString( std::string port )
{
  std::string::iterator chr; 
  for( chr = port.begin(); chr != port.end(); ++chr )
  {
    if( *chr < '0' || *chr > '9' )
      throw std::runtime_error( "Bad Port Number"  );
  }
  int portnum = ss_atoi<int>( port );
  
  if( portnum <= 0 && portnum > 0xFFFF ) //MAX PORT NUM
  { 
    throw std::runtime_error( "Bad Port Number" );
  }
}

void Network::close()
{
  if( socket_ >=0 )
  {
    int err = shutdown( socket_, SHUT_RDWR ); //Don't allow anymore transmissions
    if( err == -1 )
    { 
      //Handel error
      //TODO: do this better
      switch( errno )
      {
	case EBADF: //socket_ is not a valid descriptor.
	  //break;
	case ENOTCONN: //The specified socket is not connected.
	  //break;
	case ENOTSOCK: //socket_ is a file, not a socket.
	  //break;
	default: //OH CRAP NOW WHAT!
	  throw std::runtime_error( "SOCKET CANNOT SHUTDOWN" );
      };
    }
    err = ::close( socket_ );
    if( err == -1 )
    {
      //Handel error
      switch( errno )
      {
	case EINTR: //We were interupted by a signal.
	  close();
	  return;
	case EBADF: //socket_ isn't a valid open file descriptor.
	  //break;
	case EIO: //An I/O error occurred.
	  //break;
	default: //UNKNOWN?? SHIT!
	  throw std::runtime_error( "SOCKET: cannot close()" );
      };
    }
  }
}

void Network::connect( std::string server, std::string port )
{
  if( socket_ >= 0 )
    close();
  socket_ = -1;
  struct addrinfo hints, *res, *res0;
  //clear out hints
  memset( &hints, 0, sizeof( hints ) );
  hints.ai_family = AF_UNSPEC; //IPv4 or IPv6
  if( networkProtocol_ == TCP )
    hints.ai_socktype = SOCK_STREAM; //TCP
  else if( networkProtocol_ == UDP )
    hints.ai_socktype = SOCK_DGRAM; //UDP

  std::cerr << "Getting info" << std::endl;
  checkServerString( server );
  std::cerr << "Server Check" << std::endl;
  checkPortString( port );
  std::cerr << "CHECKED" << std::endl;
  int error = getaddrinfo( server.c_str(), port.c_str(), &hints, &res0 );
  if( error )
  {
    std::stringstream s;
    s << "getaddrinfo: " << server << ":" << port << " FAILED " << gai_strerror( error ) << std::endl;
    throw std::runtime_error( s.str() );
  }
  std::cerr << "poop" << std::endl;

  for( res = res0; res; res = res->ai_next )
  {
    //* For Print Out
    char hbuf[1500];
    char sbuf[1500];
    error = getnameinfo( res->ai_addr, res->ai_addrlen, hbuf,
	sizeof(hbuf), sbuf, sizeof(sbuf),
	NI_NUMERICHOST | NI_NUMERICSERV);
    if (error) 
    {
      std::cerr << "ERROR" << std::endl;
      continue;
    }
    std::cerr << "trying " << hbuf << " port " << sbuf << std::endl;
   // */
    std::cerr << "WHAAAAASDFDSADFASDF" << std::endl;


    socket_ = socket( res->ai_family, res->ai_socktype, res->ai_protocol );
    if( socket_ < 0 )
      continue;

    setNonblocking();

    if (::connect(socket_, res->ai_addr, res->ai_addrlen) < 0) 
    {
      fd_set writeSet;
      struct timeval tv;
      tv.tv_sec = communication::ConnectionTimeout;
      tv.tv_usec = 0;
      FD_ZERO( &writeSet );
      FD_SET( socket_, &writeSet );
      int result = select( socket_ + 1, NULL, &writeSet, NULL, &tv );
      if( result < 1 )
      {
	close();
	socket_ = -1;
	continue;
      }
      else
      {
	break;
      }
    }
    fd_set writeSet;
    struct timeval tv;
    tv.tv_sec = communication::ConnectionTimeout;
    tv.tv_usec = 0;
    FD_ZERO( &writeSet );
    FD_SET( socket_, &writeSet );
    int result = select( socket_ + 1, NULL, &writeSet, NULL, &tv );
    if( result < 1 )
    {
      close();
      socket_ = -1;
      continue;
    }
    else
    {
      break;
    }
  }
  setBlocking();
  freeaddrinfo( res0 );

  if( socket_ == -1 )
    throw std::runtime_error( "Couldn't Connect" );

}

/*
 * This receive is a blocking call that continues
 * to receive data until the socket connection is 
 * broken on the other side
 */
std::string Network::recvAll( ) 
{
  std::stringstream output;
  int received = 1; 
  int size = 512;
  char buffer[size];
  
  while(received > 0)
  {
    received = ::recv( socket_, buffer,size, 0);

    for(int i = 0; i < received; i++)
    {
      output << buffer[i];
    }
  }

  return output.str();
}

std::string Network::recv( int size ) 
{
  std::stringstream output;
  char buffer[size];
  while( size != 0  ) 
  {
    int received = ::recv( socket_, buffer, size, 0 );
    //std::cerr << "Received " << received << " bits and requested " << size << std::endl;
    if( received < 0 ) //error
    {
      throw std::runtime_error( printErrorMsg( errno ));
    }
    for( int i = 0; i < received; ++i )
      output << buffer[i];
    size -= received;
  }
  return output.str();
}

int Network::send( std::string dataToSend )
{

  uint32_t size = ::send( socket_, dataToSend.c_str(), dataToSend.size(), 0 );
  if( size != dataToSend.size() )
    throw std::runtime_error( "send problems" );
  return size;
}

