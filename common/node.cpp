#include "node.h"
#include "constants.h"
#include <iostream>

//GLuint FontListBase = 0; // Base number of the font bitmaps that Pango generates
//int FontHeight = 0; // Font height, also needed to render fonts

FTFont* font;

Node::Node( )
{
  name_ = NotANode;
  locked_ = false;
  highlighted_ = false;
}

Node::Node( std::string name )
{
  size_ = 3;
  color_ = Nodes::DefaultColor;
  x_ = 0;
  y_ = 0;
  name_ = name;
  locked_ = false;
  highlighted_ = false;
}

Node::~Node()
{
}

void Node::toggleLocked()
{
  locked_ = locked_ ? false : true;
  std::cerr << "Node" << ( locked_ ? "locked" : "unlocked" ) << std::endl;
}

void Node::drawPicker( Color color )
{
//  std::cerr << "DRAW RECT r:" << color_.getGLRed() << " g:" << color_.getGLGreen() << " b:" << color_.getGLBlue() << " a:" << color_.getGLAlpha() << std::endl;
  glColor4f( color.getGLRed(), color.getGLGreen(), color.getGLBlue(), color.getGLAlpha() );
  glRectf( x_-(Nodes::DefaultHorizontalSize*size_), y_-(Nodes::DefaultVerticalSize*size_), x_+(Nodes::DefaultHorizontalSize*size_), y_+(Nodes::DefaultVerticalSize*size_) );
}

bool Node::setTextBlack( const Color &color )
{
  int delta = color.getRed()+color.getBlue()+color.getGreen();
  int gamma = ((color.getRed()*299) + (color.getGreen()*587) + (color.getBlue()*114))/1000;
  //std::cout<<"Delta: "<<delta<<" Gamma: "<<gamma<<std::endl;
  if(delta<Render::TextDeltaThreshold || gamma<Render::TextGammaThreshold)
    return false;
  return true;
}

void Node::draw( )
{
  //std::cerr << "DRAW RECT x:" << x_ << " y:" << y_ << std::endl;
  Color color;
  if( ! highlighted_ )
    color = ( locked_ ? color_ - Nodes::LockedColorDifference : color_ );
  else
    color = ( locked_ ? Nodes::HighlightedColor - Nodes::LockedColorDifference : Nodes::HighlightedColor);

  float x1 = x_ - ( Nodes::DefaultHorizontalSize*size_ );
  float y1 = y_ - ( Nodes::DefaultVerticalSize*size_ );
  float x2 = x_ + ( Nodes::DefaultHorizontalSize*size_ );
  float y2 = y_ + ( Nodes::DefaultVerticalSize*size_ );
    
  
  glColor4f( color.getGLRed(), color.getGLGreen(), color.getGLBlue(), color.getGLAlpha() );
  glRectf( x1, y1, x2, y2 );
  //glRectf( x_-(Nodes::DefaultHorizontalSize*size_), y_-(Nodes::DefaultVerticalSize*size_), x_+(Nodes::DefaultHorizontalSize*size_), y_+(Nodes::DefaultVerticalSize*size_) );

  //---texture fonts---
  //decide black or white text
  setTextBlack( color ) ? glColor4f( 0.0, 0.0, 0.0, 1.0 ) : glColor4f( 1.0, 1.0, 1.0, 1.0 );
  glEnable( GL_TEXTURE_2D );
  glEnable( GL_BLEND );
  glBlendFunc( GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA );
  glPushMatrix();
  glTranslatef( x1 + 2, y1 + 1, 0 );
  glScalef( 0.1, 0.1, 0.1 );
  font->Render( name_.c_str());
  if( font->Error() )
    std::cerr << "PUKE" << std::endl;

  glPopMatrix();
  glDisable( GL_TEXTURE_2D );
  glDisable( GL_BLEND );

  //bitmap font stuff
  /*glColor4f(0.0, 0.0, 0.0, 0.0);
  glRasterPos2f(x_ - 10, y_ - 2);
  for (unsigned int i=0; i < name_.size(); i++) 
  {
    glCallList(FontListBase + name_[i]);
  }
  */
}

void Node::setSize( float size )
{
  size_ = size;
}

void Node::setColor( Color color )
{
  color_ = color;
}

void Node::setName( std::string name )
{
  name_=name;
}

void Node::setX( float x )
{
  x_ = x;
}

void Node::setY( float y )
{
  y_ = y;
}

void Node::setHighlighted( bool on )
{
  highlighted_ = on;
}
