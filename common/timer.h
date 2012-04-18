/****************************************************************
 *
 * timer.h - Definition for the timer 
 *
 * Keeps track of the time between when it created and called
 *
 * (c) 2008 - Jeff Scaparra
 *
 * *********************************************************/

#ifndef TIMER_H
#define TIMER_H
#include <iostream>
#include <sys/time.h>
#include <stdexcept>

class Timer 
{
  public:
    Timer()
    {
      gettimeofday( &start_, NULL );
      stopped_ = false;
    }

    virtual ~Timer()
    {
    }

    void stop()
    {
      gettimeofday( &stop_, NULL );
      stopped_ = true;
    }

    struct timeval getTimeDifference( )
    {
      if( ! stopped_ )
	throw std::runtime_error( "timer not stopped" );

      struct timeval difference;
      timersub( &stop_, &start_, &difference );
      return difference;
    }

  private:
    struct timeval start_;
    struct timeval stop_;
    bool stopped_;

    friend std::ostream& operator<< ( std::ostream& os, Timer timer );
};

std::ostream& operator<< (std::ostream& os, Timer timer) 
{
  struct timeval current;
  gettimeofday( &current, NULL);
  struct timeval difference;
  timersub( &current, &timer.start_, &difference );
  os << difference.tv_sec << " sec " << difference.tv_usec << " ns "; 
  return os;
} 

#endif
