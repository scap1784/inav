/**
 * * INAV - Interactive Network Active-traffic Visualization
 * * Copyright © 2008  Jeff Scaparra, Nathan Robinson
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
 * 
 * Edited: 
 * Jun 28, 2008 - Jeff Scaparra
 *
 * */
#ifndef XMLNODE_H
#define XMLNODE_H
#include <map>
#include <vector>
#include <string>

class XMLNode
{
  public:
    XMLNode( std::string xml );
    XMLNode( ) {}
    virtual ~XMLNode() { }
    XMLNode( const XMLNode& xmlNode );

    std::string getData() { return data_; }
    uint32_t getSizeOfChildren( std::string index ) { return children_[index].size(); }
    XMLNode& operator[]( std::string );
    XMLNode& getChild( std::string, int num = 0 );
    XMLNode& operator=( const XMLNode& node );


  private:
    std::map< std::string, std::vector< XMLNode > > children_;
    std::string data_;

};


#endif
