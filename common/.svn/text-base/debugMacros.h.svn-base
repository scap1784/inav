/****************************************************************
 *
 * debugMacros.h - Definition for debugging 
 *
 * Keeps track of the time between when it created and called
 *
 * (c) 2008 - Jeff Scaparra
 *
 * *********************************************************/

#ifndef DEBUGMACROS_H
#define DEBUGMACROS_H
#include <sstream>

#ifdef DEBUG_PERFORMANCE_MUTEX
  #include "timer.h"
  #define START_MUTEX_TIMER(...) Timer __VA_ARGS__;
  #define OUTPUT_MUTEX_TIME(...) std::cerr << __FILE__ <<" " << __LINE__ << ": " #__VA_ARGS__ " took: " << __VA_ARGS__ << std::endl;
#else
  #define START_MUTEX_TIMER(...)
  #define OUTPUT_MUTEX_TIME(...)
#endif
  
#define START_TIMER(...) Timer __VA_ARGS__;
#define OUTPUT_TIME(...) std::cerr << __FILE__ <<" " << __LINE__ << ": " #__VA_ARGS__ " took: " << __VA_ARGS__ << std::endl;

#define FILEINFO getFileInfo(__FILE__, __LINE__)

std::string getFileInfo( const std::string file, int line )
{
  std::stringstream s;
  s << file << " " << line;
  return s.str();
}

#endif
