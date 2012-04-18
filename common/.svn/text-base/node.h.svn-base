#ifndef Node_h
#define Node_h

#include <GL/gl.h>
#include <GL/glu.h> 
#include <GL/glx.h> 

#include <stdint.h>
#include <string>
#include "color.h"
#include "clientConstants.h"

#include <arpa/inet.h>
#include <netinet/in.h>
#include <sys/socket.h>
//#include <FTGL/ftgl.h>

#include <FTGL/FTGLTextureFont.h>

const std::string NotANode="notANode"; //the name of non-nodes ie. when nodes
//are used in a map nodes["asdf"] if that doesn't exist a node is returned with
//the name "notANode"

extern FTFont* font;

//Bitmap Font CRAP!
//extern GLuint FontListBase;
//extern int FontHeight;
//GLuint FontListBase = 0; // Base number of the font bitmaps that Pango generates
//int FontHeight = 0; // Font height, also needed to render fonts

class Node 
{
  public:
    Node( );
    Node( std::string name );
    virtual ~Node();
    void draw();
    void drawPicker( Color color );
    void toggleLocked();

    //sets
    void setSize( float size );
    void setColor( Color color );
    void setName( std::string name );
    void setX( float x );
    void setY( float y );
    void setHighlighted( bool on = true );

    //gets
    float getSize( ) const { return size_; }
    float getVerticalSize() const { return size_ * Nodes::DefaultVerticalSize; }
    float getHorizontalSize() const { return size_ * Nodes::DefaultHorizontalSize; }
    Color getColor( ) const { return color_; }
    std::string getName( ) const { return name_; }
    float getX( ) const { return x_; }
    float getY( ) const { return y_; }
    bool isLocked( ) const { return locked_; }

   private:
    //Sets the text color to white or black depending on the color of the node;
    bool setTextBlack( const Color &color ); 
    float size_; //defaults to 3 when NULL
    Color color_;
    float x_; //x,y coordinates in 2D plane
    float y_;
    std::string name_;
    bool locked_;
    bool highlighted_;
    

};
#endif
