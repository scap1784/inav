#ifndef CLIENT_CONSTANTS_H
#define CLIENT_CONSTANTS_H

#include <stdint.h>
#include <string>
#include "color.h"

namespace layout
{
  const int ratioOfNodesToBoxes = 35;
}

namespace physics
{
  const float MovementConstant = 0.4; //how much the final Force affects movement
  const float DefaultEffectiveRadius = 100.0;
  const float DefaultStartingGravity = 4.0;
  const float DefaultStartingSpring = 0.10;
  const int MaxEdges = 100;// after this many edges we no longer affect the mass;
  const int MaxTime = 600;//after this time we no longer affect the spring;
  const int MaximumSpringWeakening = 10;
  const uint32_t WaitInterval = 30000; //Interval between physics runs...
}

namespace Render
{
  const int RenderRefresh = 30; //refresh the display every x ms
  const std::string InavFont = "inavFont.ttf";
  const int InavFontSize = 48; 
  //Use in calculations for text color
  const int TextGammaThreshold = 128;
  const int TextDeltaThreshold = 383;
}

namespace Nodes
{ //Colors in RRGGBBAA
  const Color DefaultColor( 0xAAAAAAFF );
  const Color LockedColorDifference( 0x55555500 );
  const Color HighlightedColor( 0xFFAA2200 );
  const float DefaultVerticalSize=1.0;
  const float DefaultHorizontalSize=6.0;
  const float NodeAspectRatio = (DefaultHorizontalSize / DefaultVerticalSize);
}

namespace Graph
{
  const uint32_t LowBandwidth = 10;
  const uint32_t MediumBandwidth = 50;
  const uint32_t HighBandwidth = 100;
  const Color LowBandwidthColor( 0xAAAAAAFF );
  const Color MediumLowBandwidthColor =( 0x777777FF );
  const Color MediumHighBandwidthColor =( 0x333333FF ); 
  const Color HighBandwidthColor = ( 0x000000FF ); 
}

namespace Net
{
  const int DefaultPort = 5000;
  const std::string DefaultServer = "127.0.0.1";
}

namespace Inav
{
  const int DefaultLowBandwidthValue = 1;
  const int DefaultMediumBandwidthValue = 100;
  const int DefaultHighBandwidthValue = 1000;
}

#endif

