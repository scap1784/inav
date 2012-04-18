#ifndef COLOR_H
#define COLOR_H

#include <stdint.h>
#include <sstream>
#include <string>
class Color
{
  public:
    Color( );
    Color( uint32_t color );
    Color( const Color& n );
    virtual ~Color();
    uint32_t getColor( ) { return color_; }
    uint8_t getRed( ) const { return color_ >> 24; }
    float getGLRed( ) { return  static_cast<float>(getRed()) / static_cast<float>(0xFF); }
    uint8_t getGreen( ) const { return ( color_ & 0x00FF0000 ) >> 16; }
    float getGLGreen( ) { return static_cast<float>(getGreen()) / static_cast<float>( 0xFF ); }
    uint8_t getBlue( ) const { return ( color_ & 0x0000FF00 ) >> 8; }
    float getGLBlue( ) { return static_cast<float>( getBlue() ) / static_cast<float>( 0xFF ); }
    uint8_t getAlpha( ) { return color_ & 0x000000FF; }
    float getGLAlpha( ) { return static_cast<float>( getAlpha() ) / static_cast<float>( 0xFF ); }
    std::string getAddress();
    void setColor( uint32_t color );
    void setRed( uint8_t red );
    void setGreen( uint8_t green );
    void setBlue( uint8_t blue );
    void setAlpha( uint8_t alpha );

    Color& operator=( const Color &n );
    Color& operator+=( const Color &n );
    friend Color operator+( const Color &r, const Color &l );
    Color& operator-=( const Color &n );
    friend Color operator-( const Color &r, const Color &l );
    bool operator==( const Color &n );
    bool operator!=( const Color &n );
   
   private:
    uint32_t color_; // 0xRRGGBBAA
};




#endif
