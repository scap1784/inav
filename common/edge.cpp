#include <iostream>
#include <cmath>
#include "edge.h"

const double PI = 3.14159265;

Edge::Edge( ): lineWidth_( EDGE::DefaultLineWidth), lineStipple_( EDGE::DefaultLineStipple ),
  stippleMultiplier_( EDGE::DefaultStippleMultiplier ), lineColor_(
      EDGE::DefaultLineColor )
{
 arrowHeads_ = true;
 arrowScale_ = 2.0;
}

Edge::~Edge( )
{
}

void Edge::draw( const Node &nodeA, const Node &nodeB ) 
{
  glLineWidth( lineWidth_ );
  glLineStipple( stippleMultiplier_, lineStipple_ ); 
  glBegin( GL_LINES );
  glColor4f( lineColor_.getGLRed(), lineColor_.getGLGreen(), lineColor_.getGLBlue(), lineColor_.getGLAlpha() );
  glVertex2f( nodeA.getX(), nodeA.getY() );
  glVertex2f( nodeB.getX(), nodeB.getY() );
  glEnd();
  if( arrowHeads_ )
    drawArrow( nodeA, nodeB );
}

void Edge::drawArrow( const Node &nodeA, const Node &nodeB )
{
  float yyy = nodeA.getY() - nodeB.getY();
  float xxx = nodeA.getX() - nodeB.getX();
  float y = 0;
  float x = 0;
  if( yyy > 0 && xxx > 0 )
  {
    if( yyy/xxx > 1 / Nodes::NodeAspectRatio )
      case1( nodeA, nodeB, x, y );
    else case2( nodeA, nodeB, x, y );
  }
  else if( yyy < 0 && xxx > 0 )
  {
    if( yyy/xxx > 1 / -Nodes::NodeAspectRatio )
      case2( nodeA, nodeB, x, y );
    else case3( nodeA, nodeB, x, y );
  }
  else if( yyy < 0 && xxx < 0 )
  {
    if( yyy/xxx < 1 / Nodes::NodeAspectRatio )
      case4( nodeA, nodeB, x, y );
    else case3( nodeA, nodeB, x, y );
  }
  else if ( yyy > 0 && xxx < 0 )
  {
    if( yyy/xxx > 1 / -Nodes::NodeAspectRatio )
      case4( nodeA, nodeB, x, y );
    else case1( nodeA, nodeB, x, y );
  }
  else if ( yyy == 0 && xxx > 0 )
  {
    y = nodeB.getY();
    x = nodeB.getX() + nodeB.getHorizontalSize();
    glMatrixMode( GL_MODELVIEW );
    glPushMatrix(); //save current matrix

    glTranslatef( x, y, 0 );
    glRotatef( 270, 0, 0, 1 );
    glScalef( arrowScale_, arrowScale_, arrowScale_ );
    drawArrow();
    glPopMatrix(); //restore matrix
  }
  else if ( yyy == 0 && xxx < 0 )
  {
    y = nodeB.getY(); 
    x = -nodeB.getHorizontalSize() + nodeB.getX();
    glMatrixMode( GL_MODELVIEW );
    glPushMatrix(); //save current matrix

    glTranslatef( x, y, 0 );
    glRotatef( 90, 0, 0, 1 );
    glScalef( arrowScale_, arrowScale_, arrowScale_ );
    drawArrow();
    glPopMatrix(); //restore matrix
  }
  else if ( yyy < 0 && xxx == 0 )
  {
    y = -nodeB.getVerticalSize() + nodeB.getY();
    x = nodeB.getX();
    glMatrixMode( GL_MODELVIEW );
    glPushMatrix(); //save current matrix

    glTranslatef( x, y, 0 );
    glRotatef( 180, 0, 0, 1 );
    glScalef( arrowScale_, arrowScale_, arrowScale_ );
    drawArrow();
    glPopMatrix(); //restore matrix
  }
  else if ( yyy > 0 && xxx == 0 )
  {
    y = nodeB.getVerticalSize() + nodeB.getY();
    x = nodeB.getX();
    glMatrixMode( GL_MODELVIEW );
    glPushMatrix(); //save current matrix

    glTranslatef( x, y, 0 );
    glScalef( arrowScale_, arrowScale_, arrowScale_ );
    drawArrow();
    glPopMatrix(); //restore matrix
  }
  if( x == 0 && y == 0 )
    return; //on top of each other
}

/****************************************************************************
 *
 *
 *                               Case 1
 *                  ---------------------------------
 *        Case 4    |                               |
 *                  |                               | Case 2
 *                  ---------------------------------
 *                               Case 3
 *
 *
 *
 ****************************************************************************/

void Edge::drawArrow()
{
  glBegin( GL_TRIANGLES );
  glVertex2f( 0, 0 );
  glVertex2f( 0.75, 1.25 );
  glVertex2f( -0.75, 1.25 );
  glEnd();
} 

void Edge::case1( const Node &nodeA, const Node &nodeB, float &x, float& y )
{
  y = nodeB.getVerticalSize() + nodeB.getY();
  float rise = nodeA.getY() - nodeB.getY();
  float run = nodeA.getX() - nodeB.getX();
  float w = ( run * nodeB.getVerticalSize() ) / rise;
  x = nodeB.getX() + w;

  double degrees = atan( run/rise ) * 180 / PI;
  //draw triangle and roate it degrees
  
  glMatrixMode( GL_MODELVIEW );
  glPushMatrix(); //save current matrix
  
  glTranslatef( x, y, 0 );
  glRotatef( -degrees, 0, 0, 1 );
  glScalef( arrowScale_, arrowScale_, arrowScale_ );
  drawArrow();
  glPopMatrix(); //restore matrix
  
}

void Edge::case3( const Node &nodeA, const Node &nodeB, float &x, float& y )
{
  y = -nodeB.getVerticalSize() + nodeB.getY();
  float rise = nodeA.getY() - nodeB.getY();
  float run = nodeA.getX() - nodeB.getX();
  float w = ( run * nodeB.getVerticalSize() ) / rise;
  x = nodeB.getX() - w;

  double degrees = atan( run/rise ) * 180 / PI;
  //draw triangle and roate it degrees
  
  glMatrixMode( GL_MODELVIEW );
  glPushMatrix(); //save current matrix
  
  glTranslatef( x, y, 0 );
  glRotatef( -degrees+180, 0, 0, 1 );
  glScalef( arrowScale_, arrowScale_, arrowScale_ );
  drawArrow();
  glPopMatrix(); //restore matrix
  

}

void Edge::case2( const Node &nodeA, const Node &nodeB, float &x, float&y )
{
  x = nodeB.getHorizontalSize() + nodeB.getX();
  float rise = nodeA.getY() - nodeB.getY();
  float run = nodeA.getX() - nodeB.getX();
  float w = ( rise * nodeB.getHorizontalSize() ) / run;
  y = nodeB.getY() + w;
  
  double degrees = atan( rise/run ) * 180 / PI;
  //draw triangle and roate it degrees
  
  glMatrixMode( GL_MODELVIEW );
  glPushMatrix(); //save current matrix
  
  glTranslatef( x, y, 0 );
  glRotatef( degrees+270, 0, 0, 1 );
  glScalef( arrowScale_, arrowScale_, arrowScale_ );
  drawArrow();
  glPopMatrix(); //restore matrix
}

void Edge::case4( const Node &nodeA, const Node &nodeB, float &x, float&y )
{
  x = -nodeB.getHorizontalSize() + nodeB.getX();
  float rise = nodeA.getY() - nodeB.getY();
  float run = nodeA.getX() - nodeB.getX();
  float w = ( rise * nodeB.getHorizontalSize() ) / run;
  y = nodeB.getY() - w;
  
  double degrees = atan( rise/run ) * 180 / PI;
  //draw triangle and roate it degrees
  
  glMatrixMode( GL_MODELVIEW );
  glPushMatrix(); //save current matrix
  
  glTranslatef( x, y, 0 );
  glRotatef( degrees+90, 0, 0, 1 );
  glScalef( arrowScale_, arrowScale_, arrowScale_ );
  drawArrow();
  glPopMatrix(); //restore matrix
}

void Edge::setLineWidth( float width )
{
  lineWidth_ = width;
}

void Edge::setArrowHeads( bool ison )
{
  arrowHeads_ = ison;
}

void Edge::setLineStipple( uint16_t stipple )
{
  lineStipple_ = stipple;
}

void Edge::setStippleMultiplier( uint8_t factor )
{
  stippleMultiplier_ = factor; 
}

void Edge::setLineColor( Color color )
{
  lineColor_ = color;
}
