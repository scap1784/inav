#ifndef SHAPE_HPP
#define SHAPE_HPP

#include <string>

class Shape {
  protected:
    std::string shapeName_;
    int width_;
    int height_;

  public:
    Shape() {}
    virtual void setWidth( int width ) {}
    virtual int getWidth( ) {}
    virtual void setHeight( int height ) {}
    virtual int getHeight( ){}
    virtual std::string getShapeName() {}
};

#endif
