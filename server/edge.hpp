#ifndef EDGE_HPP
#define EDGE_HPP
#include <stdint.h>
enum LineType { straight, dotted, dashed, invisible };   

class Edge {
  private:
    int weight_;
    uint32_t color_;
    LineType lineType_;
    bool arrowheads_;
  
  public:
    Edge();
    void setWeight( int weight );
    int getWeight( );
    void setColor( uint32_t color );
    uint32_t getColor( );
    void setLineType( LineType lineType );
    LineType getLineType( );
    void setArrows( bool on );
    bool getArrows( );

};

#endif

