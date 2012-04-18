#ifndef EDGE_H 
#define EDGE_H
#include "node.h"
#include "color.h"

#include <GL/gl.h>
#include <GL/glu.h> 
#include <GL/glx.h> 

#include <stdint.h>

namespace EDGE
{
  const float DefaultLineWidth = 2;
  const uint16_t DefaultLineStipple = 0xFFFF;
  const uint8_t DefaultStippleMultiplier = 1;
  const Color DefaultLineColor( 0xFF0000FF );
}

class Edge
{
  public:
    Edge( );
    virtual ~Edge();
    void draw( const Node &nodeA, const Node &nodeB );
    void drawArrow( const Node &nodeA, const Node &nodeB );
    //sets
    void setLineWidth( float width );
    void setLineStipple( uint16_t stipple = 0xFFFF );
    void setStippleMultiplier( uint8_t factor );
    void setLineColor( Color color );
    void setArrowHeads( bool ison );
    
    //gets
    float getLineWidth( ){ return lineWidth_; }
    uint16_t getLineStipple( ) { return lineStipple_; }
    uint8_t getStippleMultiplier( ) { return stippleMultiplier_; }
    Color getLineColor( ) { return lineColor_; }
    bool getArrowHeads( ) { return arrowHeads_; }
    bool isArrowHeads( ) { return getArrowHeads(); }
   
  private:
    float lineWidth_;
    uint16_t lineStipple_;
    uint8_t stippleMultiplier_;
    bool arrowHeads_;
    Color lineColor_;
    float arrowScale_;

    //functions for finding the coordinates for the point of the arrow in the
    //line
    void case1( const Node &nodeA, const Node &nodeB, float &x, float &y );
    void case2( const Node &nodeA, const Node &nodeB, float &x, float &y );
    void case3( const Node &nodeA, const Node &nodeB, float &x, float &y );
    void case4( const Node &nodeA, const Node &nodeB, float &x, float &y );

    void drawArrow();
};
#endif
