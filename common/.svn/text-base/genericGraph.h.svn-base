/***********************************************************************
 *
 *   GenericGraph.h - Header file for the Graphs
 *
 *   The GenericGraph is the keeper of all graph information
 *
 *   (C) 2008 - Jeff Scaparra
 *
 *
 ********************************************************************/
#ifndef GENERICGRAPH_H
#define GENERICGRAPH_H
#include <map>
#include <string>
#include <deque>
#include <stdexcept>
#include "networkTypes.h"
#include "helper.h"
#include "node.h"
#include "edge.h"
#include "color.h"
#include "debugMacros.h"
#include "threads.h"

class DefaultMapper
{
  public:
    std::string operator()( ipv4 ip ) { return ipv4ToString( ip ); }
    std::string operator()( ipv6 ip ) { return ipv6ToString( ip ); }
    std::string operator()( std::string ip ){ return ip; }
};

template< typename T > class DefaultReverseMapper
{
  public:
    template<typename A> A operator()( std::string ip ){ return ip; }
};

template<> class DefaultReverseMapper< ipv4 >
{
  public:
    ipv4 operator()( std::string ip ){ return stringToIPv4( ip ); }
};

template<> class DefaultReverseMapper< ipv6 >
{
  public:
    ipv6 operator()( std::string ip ){ return stringToIPv6( ip ); }
};

template<typename T, class Mapper = DefaultMapper, class ReverseMapper = DefaultReverseMapper<T> > class GenericGraph
{
  public:
    //This is the type to use in the queue for massive node movement (i.e.
    //probably from a layout class
    typedef struct NodeMove
    {
      T name;
      float x;
      float y;
    }NodeMove;

    typedef struct Position
    {
      float x;
      float y;
    }Position;


  public:
     GenericGraph()
     {
       picked_ = NULL;
       upperRight_.x = 0;
       upperRight_.y = 0;
       lowerLeft_.x = 0;
       lowerLeft_.y = 0;
     }

     virtual ~GenericGraph(){}

     //Adds a node to the graph but not an edge from that node to any other
     //nodes
     void addNode( T address )
     {
       Node node( mapperToString_( address ) );
       
       START_MUTEX_TIMER(nodeMutex_);
       MutexLocker nodeLock( nodeMutex_ );
       OUTPUT_MUTEX_TIME(nodeMutex_);
      
       nodes_[ address ] = node;
       nodeLock.unlock();
       checkBounds( node );
     }
     
     //Adds a node to the graph but not an edge from that node to any other
     //nodes
     void addNode( Node a )
     {
       T ip = mapperToT_( a.getName() );
       START_MUTEX_TIMER(nodeMutex_);
       MutexLocker nodeLock( nodeMutex_ );
       OUTPUT_MUTEX_TIME(nodeMutex_);
      
       nodes_[ ip ] = a;
       nodeLock.unlock();
       checkBounds( a );
     }

     void addEdge( T a, T b )
     {
       Edge e;
       addEdge( a, b, e );
     }

     /* if T is a string this makes this function happen twice...
      * do we even need it?
     void addEdge( std::string a, std::string b )
     {
     }
     */

     void addEdge( T a, T b, Edge e )
     {
       //Makes no assumptions about the nodes already existing.
       //If the nodes don't exist we simply create them and add the edge.

       START_MUTEX_TIMER(nodeMutex_);
       MutexLocker nodeLock( nodeMutex_ );
       OUTPUT_MUTEX_TIME(nodeMutex_);

       typename std::map< T, Node >::iterator posA;
       typename std::map< T, Node >::iterator posB;

       posA = nodes_.find( a );
       posB = nodes_.find( b );
       nodeLock.unlock();

       if( posA == nodes_.end() )
       {
	 //we need to create this node!
	 addNode( a );
       }

       if( posB == nodes_.end() )
       {
	 //we need to create this node!
	 addNode( b );
       }

       START_MUTEX_TIMER(graphMapMutex_);
       MutexLocker graphLock( graphMapMutex_ );
       OUTPUT_MUTEX_TIME(graphMapMutex_);
       
       graphMap_[a][b] = e;
     }

     void setEdgeData( T a, T b, Edge e )
     {
       //This function is similar to the function addEdge( T, T, Edge) (above) except that we assume
       //that the Nodes already exist and if not we throw an exception.
       START_MUTEX_TIMER(graphMapMutex_);
       MutexLocker graphLock( graphMapMutex_ );
       OUTPUT_MUTEX_TIME(graphMapMutex_);

       START_MUTEX_TIMER(nodeMutex_);
       MutexLocker nodeLock( nodeMutex_ );
       OUTPUT_MUTEX_TIME(nodeMutex_);

       typename std::map< T, Node >::iterator posA;
       typename std::map< T, Node >::iterator posB;

       posA = nodes_.find( a );
       posB = nodes_.find( b );

       if( posA == nodes_.end() )
       {
	 //Node A doesn't exist AHHHHHHH!
	 throw std::runtime_error( FILEINFO + ": nodeA doesn't exist" );
       }

       if( posB == nodes_.end() )
       {
	 //Node B doesn't exist AHHHHHHH!
	 throw std::runtime_error( FILEINFO + ": nodeB doesn't exist" );
       }

       graphMap_[a][b] = e;
       
     }

     void delEdge( T a, T b )
     {
       //no need to throw any exceptions if it doesn't exist... 
       START_MUTEX_TIMER( graphMapMutex_ );
       MutexLocker graphLock( graphMapMutex_ );
       OUTPUT_MUTEX_TIME( graphMapMutex_ );

       graphMap_[a].erase( b );
     }

     /* if T is a string this makes this function happen twice...
      * again is it needed?
      void kill( std::string a )
      {
      }
      */

     void kill( T a )
     {
       START_MUTEX_TIMER( graphMapMutex_ );
       MutexLocker graphLock( graphMapMutex_ );
       OUTPUT_MUTEX_TIME( graphMapMutex_ );

       typename std::map< T, Edge >::iterator itr;
       for( itr = graphMap_[a].begin(); itr != graphMap_[a].end(); ++itr )
       {
	 graphMap_[a].erase( itr->first );
       }

       graphMap_.erase( a );
       typename std::map< T, std::map< T, Edge > >::iterator sitr; //super iterator
       for( sitr = graphMap_.begin(); sitr != graphMap_.end(); ++sitr )
       {
	 for( itr = sitr->second.begin(); itr != sitr->second.end(); ++itr )
	 {
	   if( a == itr->first )
	     graphMap_[sitr->first].erase(a);
	 }
       }

       graphLock.unlock();

       START_MUTEX_TIMER( nodeMutex_ );
       MutexLocker nodeLock( nodeMutex_ );
       OUTPUT_MUTEX_TIME( nodeMutex_ );

       nodes_.erase( a );
       //Could kill performance if we kill abunch of nodes...
       resetBounds();
     }

     void draw()
     {
       typename std::map< T, Node >::iterator nodeIterator;
       typename std::map< T, std::map< T, Edge > >::iterator edgeIteratorA;
       typename std::map< T, Edge >::iterator edgeIteratorB;

       START_MUTEX_TIMER( graphMapMutex_ );
       MutexLocker graphLock( graphMapMutex_ );
       OUTPUT_MUTEX_TIME( graphMapMutex_ );

       START_MUTEX_TIMER( nodeMutex_ );
       MutexLocker nodeLock( nodeMutex_ );
       OUTPUT_MUTEX_TIME( nodeMutex_ );

       for( edgeIteratorA = graphMap_.begin(); edgeIteratorA != graphMap_.end(); ++edgeIteratorA )
       {
	 for( edgeIteratorB = edgeIteratorA->second.begin(); edgeIteratorB != edgeIteratorA->second.end(); ++ edgeIteratorB )
	 {
	   edgeIteratorB->second.draw( nodes_[ edgeIteratorA->first ], nodes_[ edgeIteratorB->first ] );
	 }
       }
       graphLock.unlock();

       for( nodeIterator = nodes_.begin(); nodeIterator != nodes_.end(); ++nodeIterator )
       {
	 nodeIterator->second.draw();
       }
     }

     void drawPicker()
     {
       //Draws the scene in a buffer using a unquie color for every node. This
       //unquie color is stored in a lookup map so the node picked can easily be
       //identified later. Also deslects picked_;

       typename std::map< T, Node >::iterator nodeIterator;

       START_MUTEX_TIMER( pickerMutex_ );
       MutexLocker pickerLock( pickerMutex_ );
       OUTPUT_MUTEX_TIME( pickerMutex_ );

       picked_ = NULL;
       picker_.clear();

       START_MUTEX_TIMER( nodeMutex_ );
       MutexLocker nodeLocker( nodeMutex_ );
       OUTPUT_MUTEX_TIME( nodeMutex_ );

       Color unquieColor( 1 );
       for( nodeIterator = nodes_.begin(); nodeIterator != nodes_.end(); ++nodeIterator, unquieColor += 1 )
       {
	 picker_[ unquieColor ] = mapperToT_( nodeIterator->second.getName() );
	 nodeIterator->second.drawPicker( unquieColor );
       }
     }

     void selectPicked( Color color )
     {
       //Uses the above mapping of color to determine which node was picked;

       typename std::map< Color, T >::iterator pos;

       START_MUTEX_TIMER( pickerMutex_ );
       MutexLocker pickerLock( pickerMutex_ );
       OUTPUT_MUTEX_TIME( pickerMutex_ );

       pos = picker_.find( color );
       if( pos == picker_.end() )
       {
	 throw std::runtime_error( FILEINFO + "Unquie Color lookup mismatch" );
       }

       picked_ = &picker_[ color ];
     }

     void toggleLockedNode( T node )
     {
       typename std::map< T, Node >::iterator pos;
       START_MUTEX_TIMER( nodeMutex_ );
       MutexLocker nodeLocker( nodeMutex_ );
       OUTPUT_MUTEX_TIME( nodeMutex_ );

       pos = nodes_.find( node );
       if( pos == nodes_.end() )
       {
	 throw std::runtime_error( FILEINFO + "Not a node" );
       }

       pos->second.toggleLockedNode();
     }

     void unselectPicked( )
     {
       picked_ = NULL;
     }

     T getPicked()
     {
       //Returns the node picked. If no node has been picked this function
       //throws an exception.
       if( !picked_ )
	 throw std::runtime_error( FILEINFO + "Nothing Picked" );
       T a = picked_;
       return a;
     }

     void moveSelected( float x, float y )
     {
       START_MUTEX_TIMER( nodeMutex_ );
       MutexLocker nodeLocker( nodeMutex_ );
       OUTPUT_MUTEX_TIME( nodeMutex_ );

       nodes_[ picked_ ].setX( x );
       nodes_[ picked_ ].setY( y );
       checkBounds( nodes_[ picked_ ] );
     }

     void highlightNode( T node )
     {
       typename std::map< T, Node >::iterator pos;
       START_MUTEX_TIMER( graphMapMutex_ );
       MutexLocker graphLocker( graphMapMutex_ );
       OUTPUT_MUTEX_TIME( graphMapMutex_ );
       
       START_MUTEX_TIMER( nodeMutex_ );
       MutexLocker nodeLocker( nodeMutex_ );
       OUTPUT_MUTEX_TIME( nodeMutex_ );

       START_MUTEX_TIMER( highlightedMutex_ );
       MutexLocker highlightLocker( highlightedMutex_ );
       OUTPUT_MUTEX_TIME( highlightedMutex_ );
       
       pos = nodes_.find( node );
       if( pos == nodes_.end() )
	 throw std::runtime_error( FILEINFO + "Node does not exist" );

       highlighted_.push_back( node );
       nodes_[node].setHighlighted( );

       typename std::map< T, Edge>::iterator itr;
       for( itr = graphMap_[ highlighted_ ].begin(); itr != graphMap_[ highlighted_ ].end(); ++itr )
       {
	 nodes_[ itr->first ].setHighlighted();
	 highlighted_.push_back( itr->first );
       }
     }

     void turnoffHighlight()
     {
       typename std::vector< T >::iterator itr;

       START_MUTEX_TIMER( nodeMutex_ );
       MutexLocker nodeLocker( nodeMutex_ );
       OUTPUT_MUTEX_TIME( nodeMutex_ );

       START_MUTEX_TIMER( highlightedMutex_ );
       MutexLocker highlightLocker( highlightedMutex_ );
       OUTPUT_MUTEX_TIME( highlightedMutex_ );

       for( itr = highlighted_.begin(); itr != highlighted_.end(); ++itr )
       {
	 nodes_[ *itr ].setHighlighted( false );
       }

       highlighted_.clear();
     }

     std::map< T, uint32_t >countEdges()
     {
       //Returns a map with the key being the type T address and the data being
       //the number of edges to or from that node.
       std::map< T, uint32_t > edges;
       
       typename std::map< T, std::map< T, Edge > >::iterator itr;
       typename std::map< T, Edge >::iterator itr2;
       START_MUTEX_TIMER( graphMapMutex_ );
       MutexLocker graphLocker( graphMapMutex_ );
       OUTPUT_MUTEX_TIME( graphMapMutex_ );

       for( itr = graphMap_.begin(); itr != graphMap_.end(); ++itr )
       {
	 for( itr2 = itr->second.begin(); itr2 != itr->second.end(); ++itr2 )
	 {
	   edges[ itr->first ] += 1;
	   edges[ itr2->first ] += 1;
	 }
       }

       return edges;
     }

     void cleanUp()
     {
       //Remove all nodes with zero edges.
       typename std::map< T, std::map< T, Edge > >::iterator itr;
       typename std::map< T, Node >::iterator nitr;
       START_MUTEX_TIMER( graphMapMutex_ );
       MutexLocker graphLocker( graphMapMutex_ );
       OUTPUT_MUTEX_TIME( graphMapMutex_ );
       
       START_MUTEX_TIMER( nodeMutex_ );
       MutexLocker nodeLocker( nodeMutex_ );
       OUTPUT_MUTEX_TIME( nodeMutex_ );

       for( nitr = nodes_.begin(); nitr != nodes_.end(); ++nitr )
       {
	 if( graphMap_[ nitr->first ].size() == 0 )
	 {
	   kill( nitr->first );
	 }
       }
     }

     Position getUpperBound()
     {
       START_MUTEX_TIMER( upperBoundMutex_ );
       MutexLocker upperLock( upperBoundMutex_ );
       OUTPUT_MUTEX_TIME( upperBoundMutex_ );
       Position x = upperRight_;
       return x;
     }

     Position getLowerBound()
     {
       START_MUTEX_TIMER( lowerBoundMutex_ );
       MutexLocker lowerLock( lowerBoundMutex_ );
       OUTPUT_MUTEX_TIME( lowerBoundMutex_ );
       Position x = lowerLeft_;
       return x;
     }

     int getNumberOfNodes()
     {
       int number = 0;
       START_MUTEX_TIMER(nodeMutex_);
       nodeMutex_.lock();
       OUTPUT_MUTEX_TIME(nodeMutex_);
       number = nodes_.size();
       nodeMutex_.unlock();
       return number;
     }

     void moveNode( const Node &a, const float &x, const float &y )
     {
       T node = mapperToT_( a.getName() );
       typename std::map< T, Node >::iterator pos;
       START_MUTEX_TIMER(nodeMutex_);
       nodeMutex_.lock();
       OUTPUT_MUTEX_TIME(nodeMutex_);

       pos = nodes_.find( node );

       if( pos == nodes_.end() )
	 throw std::runtime_error( FILEINFO + "Can't move a node that doesn't exist" );

       nodes_[ node ].setX( x );
       nodes_[ node ].setY( y );

       resetBounds();
     }

     void moveNode( const std::string &nodeName, const float &x, const float &y)
     {
       T node = mapperToT_( nodeName );
       typename std::map< T, Node >::iterator pos;
       START_MUTEX_TIMER(nodeMutex_);
       MutexLocker nodeLock( nodeMutex_);
       OUTPUT_MUTEX_TIME(nodeMutex_);

       pos = nodes_.find( node );

       if( pos == nodes_.end() )
	 throw std::runtime_error( FILEINFO + "Can't move a node that doesn't exist" );

       nodes_[ node ].setX( x );
       nodes_[ node ].setY( y );

       // can be bad if we are moving a bunch of nodes need a way to serialize
       // the moves...
       resetBounds();
     }

     void moveNodes( const std::deque< NodeMove > &nodesToBeMoved )
     {
       typename std::deque< NodeMove >::const_iterator itr;
       START_MUTEX_TIMER(nodeMutex_);
       MutexLocker nodeLock( nodeMutex_);
       OUTPUT_MUTEX_TIME(nodeMutex_);

       for( itr = nodesToBeMoved.begin(); itr != nodesToBeMoved.end(); ++itr )
       {
	 nodes_[ itr->name ].setX( itr->x );
	 nodes_[ itr->name ].setY( itr->y );
       }
     }

     void clear()
     {
       START_MUTEX_TIMER( nodeMutex_ );
       MutexLocker nodeLock( nodeMutex_ );
       OUTPUT_MUTEX_TIME( nodeMutex_ );

       START_MUTEX_TIMER( graphMapMutex_ );
       MutexLocker graphLock( graphMapMutex_ );
       OUTPUT_MUTEX_TIME( graphMapMutex_ );

       START_MUTEX_TIMER( highlightedMutex_ );
       MutexLocker highlightLock( highlightedMutex_ );
       OUTPUT_MUTEX_TIME( highlightedMutex_ );

       START_MUTEX_TIMER( upperBoundMutex_ );
       MutexLocker upperLock( upperBoundMutex_ );
       OUTPUT_MUTEX_TIME( upperBoundMutex_ );

       START_MUTEX_TIMER( lowerBoundMutex_ );
       MutexLocker lowerLock( lowerBoundMutex_ );
       OUTPUT_MUTEX_TIME( lowerBoundMutex_ );

       if( picked_ )
	 delete picked_;
       picked_ = NULL;

       highlighted_.clear();
       nodes_.clear();
       graphMap_.clear();
       upperRight_.x = 0;
       upperRight_.y = 0;
       lowerLeft_.x = 0;
       lowerLeft_.y = 0;
     }

     std::map< T, std::map< T, Edge > > getGraph()
     {
       START_MUTEX_TIMER( graphMapMutex_ );
       MutexLocker graphLock( graphMapMutex_ );
       OUTPUT_MUTEX_TIME( graphMapMutex_ );
       return graphMap_;
     }

     std::map< T, Node > getNodes()
     {
       START_MUTEX_TIMER( nodeMutex_ );
       MutexLocker nodeLock( nodeMutex_ );
       OUTPUT_MUTEX_TIME( nodeMutex_ );
       return nodes_;
     }

  protected:
     //Looks at a node and updates the bounds of the drawing area if the node is
     //outside of the area
     void checkBounds( Node a )
     {
       //check upperbound...
       START_MUTEX_TIMER(upperBoundMutex_);
       upperBoundMutex_.lock();
       OUTPUT_MUTEX_TIME(upperBoundMutex_);
       if( a.getX() > upperRight_.x )
	 upperRight_.x = a.getX();
       if( a.getY() > upperRight_.y )
	 upperRight_.y = a.getY();
       upperBoundMutex_.unlock();
       //check lower
       START_MUTEX_TIMER(lowerBoundMutex_);
       lowerBoundMutex_.lock();
       OUTPUT_MUTEX_TIME(lowerBoundMutex_);
       if( a.getX() < lowerLeft_.x )
	 lowerLeft_.x = a.getX();
       if( a.getY() < lowerLeft_.y )
	 lowerLeft_.y = a.getY();
       lowerBoundMutex_.unlock();
     }

     void resetBounds()
     {
       START_MUTEX_TIMER( upperBoundMutex_ );
       MutexLocker upperLock( upperBoundMutex_ );
       OUTPUT_MUTEX_TIME( upperBoundMutex_ );
       upperRight_.x = 0;
       upperRight_.y = 0;
       upperLock.unlock();

       START_MUTEX_TIMER( lowerBoundMutex_ );
       MutexLocker lowerLock( lowerBoundMutex_ );
       OUTPUT_MUTEX_TIME( lowerBoundMutex_ );
       lowerLeft_.x = 0;
       lowerLeft_.y = 0;
       lowerLock.unlock();

       START_MUTEX_TIMER( nodeMutex_ );
       MutexLocker nodeLock( nodeMutex_ );
       OUTPUT_MUTEX_TIME( nodeMutex_ );

       typename std::map< T, Node >::iterator itr;
       for( itr = nodes_.begin(); itr != nodes_.end(); ++itr )
       {
	 checkBounds( itr->second );
       }
     }


     //Member Data
     std::map< T, Node > nodes_;
     Mutex nodeMutex_;
     std::map< T, std::map< T, Edge > > graphMap_;
     Mutex graphMapMutex_;
     std::map< Color, T > picker_;
     Mutex pickerMutex_;
     T* picked_;
     std::vector< T > highlighted_;
     Mutex highlightedMutex_;
     Mutex upperBoundMutex_;
     Position upperRight_;
     Mutex lowerBoundMutex_;
     Position lowerLeft_;
     Mapper mapperToString_;
     ReverseMapper mapperToT_;

};

#endif
