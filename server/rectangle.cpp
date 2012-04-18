#include "shape.hpp"

class Rectangle : public Shape {

  Rectangle()
  {
    shapeName_="Rectangle";
  }

  void setWidth( int width )
  {
    width_ = width;
  }

  int getWidth( )
  {
    return width_;
  }

  void setHeight( )
  {
    height_ = height;
  }

  int getHeight( )
  {
    return height_;
  }

  std::string getShapeName( )
  {
    return shapeName_;
  }
};

