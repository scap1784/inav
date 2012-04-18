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
 * Jun 29, 2008 - Jeff Scaparra
 *
 * */

#include "xmlNode.h"
#include <stdexcept>
#include <iostream>

const std::string openTag = "<";
const std::string endTag = ">";
const std::string closeTag = "</";

XMLNode::XMLNode( std::string xml )
{
  std::transform( xml.begin(), xml.end(), //source
      xml.begin(), //destination
      tolower);  //operation

  std::string::size_type openidx = xml.find( openTag );
  std::string::size_type endidx = xml.find( endTag, openidx );
  std::string::size_type current = 0;

  //No tags in xml only data
  if( openidx == std::string::npos )
  {
    data_ = xml;
    return;
  }

  while( openidx != std::string::npos )
  {

    if( endidx == std::string::npos ) 
      throw std::runtime_error( "BAD XML" ); //openidx points to a < but there is no >...

    //some data and other tags
    if( current != openidx )
      data_ += xml.substr( current, openidx-current );
    
    current = openidx;
    openidx ++;
    std::string tag = xml.substr( openidx, endidx - openidx );
    //std::cerr << "found tag: " <<  tag << std::endl;
    openidx = xml.find( tag, endidx ); // find closing tag
    while( openidx != std::string::npos )
    {
      //std::cerr << "idx: " << openidx << std::endl;
      std::string::size_type previousOpen = xml.find_last_of( closeTag, openidx );
      std::string::size_type previousClose = xml.find_last_of( endTag, openidx );
      //std::cerr << "Open: " << previousOpen << "Close" << previousClose << std::endl;
      if( previousClose < previousOpen ) 
      {
	//we are in the right place
	//std::cerr << "CREATING CHILD" << xml.substr( endidx + 1, (previousOpen - 1 ) - ( endidx + 1 ) ) << std::endl;
	children_[ tag ].push_back( XMLNode( xml.substr( endidx + 1, ( previousOpen - 1) - (endidx + 1) ) ) );
	current = xml.find( endTag, openidx );
	break;
      }
      //keep looking
      openidx = xml.find( tag, openidx + 1 );
    }
    if( openidx == std::string::npos ) //Data at the EOF
    {
      data_+= xml.substr( endidx+1 );
      break;
    }
    current = xml.find( endTag, openidx ) + 1;
    openidx = xml.find( openTag, current );
    endidx = xml.find( endTag, openidx );
  }

}

XMLNode::XMLNode( const XMLNode& xmlNode )
{
  children_ = xmlNode.children_;
  data_ = xmlNode.data_;
}

XMLNode& XMLNode::getChild( std::string key, int child )
{
  return children_[key].at( child );
}

XMLNode& XMLNode::operator[]( std::string index )
{
  try
  {
    return children_[ index ].at( 0 );
  }
  catch( std::out_of_range e )
  {
    throw std::runtime_error( index + " doesn't exist" );
  }
}
 
XMLNode& XMLNode::operator=( const XMLNode& node )
{
  children_ = node.children_;
  data_ = node.data_;
  return *this;
}

