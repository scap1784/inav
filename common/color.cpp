#include "color.h"

Color::Color( uint32_t color ): color_(color)
{
}

Color::Color()
{
  color_ = 0xFFFFFFFF; //default white
}

Color::Color( const Color &n )
{
  color_ = n.color_;
}

Color::~Color()
{
}

void Color::setColor( uint32_t color )
{
  color_ = color;
}

void Color::setRed( uint8_t red )
{
  uint32_t tmp = (uint32_t)red << 24;
  color_ = color_ & 0x00FFFFFF | tmp;
}

void Color::setGreen( uint8_t green )
{
  uint32_t tmp = (uint32_t)green << 16;
  color_ = color_ & 0xFF00FFFF | tmp;
}

void Color::setBlue( uint8_t blue )
{
  uint32_t tmp = (uint32_t) blue << 8;
  color_ = color_ & 0xFFFF00FF | tmp;
}

void Color::setAlpha( uint8_t alpha )
{
  color_ = color_ & 0xFFFFFF00 | (uint32_t) alpha;
}

Color& Color::operator=( const Color& n )
{
  color_ = n.color_;
  return *this;
}

Color operator+( const Color &r, const Color &l )
{
  Color c=r;
  c+=l;
  return c;
}

Color& Color::operator+=( const Color &n )
{
  color_ += n.color_;
  return *this;
}

Color operator-( const Color &r, const Color &l )
{
  Color c=r;
  c-=l;
  return c;
}

Color& Color::operator-=( const Color &n )
{
  int red = static_cast<int>( getRed() ) - static_cast<int>( n.getRed() );
  int green = static_cast<int>( getGreen() ) - static_cast<int>( n.getGreen() );
  int blue = static_cast<int>( getBlue() ) - static_cast<int>( n. getBlue() );
  red = ( red < 0 ? 0 : red );
  green = ( green < 0 ? 0 : green );
  blue = ( blue < 0 ? 0 : blue );
  setRed( static_cast<uint8_t>( red ) );
  setGreen( static_cast<uint8_t>( green ) );
  setBlue( static_cast<uint8_t>( blue ) );
  return *this;
}

bool Color::operator==( const Color &n )
{
  return n.color_ == color_;
}

bool Color::operator!=( const Color &n )
{
  return color_ != n.color_; 
}

