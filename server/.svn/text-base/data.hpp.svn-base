#ifndef DATA_HPP
#define DATA_HPP
#include "edge.hpp"
#include "node.hpp"

//T - the raw data from the input plugin
//Y - The type of the id
template <typename T, typename Y>
class Data {
  protected:
    T data_;
    Node< Y > source_;
    Node< Y > destination_;
    Edge edge_;
    std::string dataType_;

  public:
    Data () {}
    virtual ~Data() {}
    
    //source stuff
    virtual void setSourceID( Y id );
    virtual Y getSourceID( );
    virtual void setSourceColor( uint32_t color );
    virtual uint32_t getSourceColor( );
    virtual void setSourceShape( Shape shape ); //Shapes include size
    virtual Shape getSourceShape( );
    
    //destination stuff
    virtual void setDestinationID( Y id );
    virtual Y getDestinationID( );
    virtual void setDestinationColor( uint32_t color );
    virtual uint32_t getDestinationColor( );
    virtual void setDestinationShape( Shape shape ); //Shapes include size
    virtual Shape getDestinationShape( );

    //Edge stuff
    virtual void setEdgeWeight( int sizeOfLine );
    virtual int getEdgeWeight( );
    virtual void setEdgeColor( uint32_t color );
    virtual uint32_t getEdgeColor( );
    virtual void setArrows( bool isOn ) {}
    virtual bool getArrows( ) {}
    virtual void setLineType( LineType lineType ){} //enum Line Type
    virtual LineType getLineType( ) {} 

    //Raw Input Data Junk
    virtual T getRawData( ) {}
    virtual void setRawData( T data ) {}
};

typedef Data<class T,class Y>* create_t();
typedef void destroy_T( Data<class T, class Y>* );

#endif
