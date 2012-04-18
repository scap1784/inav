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

#ifndef NETWORK_H
#define NETWORK_H

#ifndef WIN32 // UNIX
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <fcntl.h>
#include <sys/time.h>
#include <string>
#include <sstream>
#endif

enum NetworkProtocol { TCP, UDP };
#ifndef WIN32 //UNIX
typedef int Socket;
#endif

class Network
{
  public:
    Network( NetworkProtocol networkProtocol = TCP );
    Network( const Network & network );
    ~Network( );
    int  send( std::string buffer );
    std::string recvAll();
    std::string recv( int size = 1500 );
    void listen( int port );
    void connect( std::string server, int port );
    void connect( std::string server, std::string port );
    void close();


  private:
    void checkPortString( std::string port );
    void checkServerString( std::string server );
    std::string printErrorMsg( int error );
    void setNonblocking( );
    void setBlocking( );

    NetworkProtocol networkProtocol_;
    Socket socket_;

};











#endif
