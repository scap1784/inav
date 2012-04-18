#ifndef DATAQUEUE_HPP
#define DATAQUEUE_HPP
#include <deque>
#include <fstream>
#include "../common/threads.h"
#include "baseData.h"
#include "data.hpp"

template <typename T, typename Y>
class DataQueue : public BaseData {
  protected:
    Mutex queueMutex_;
    std::deque< Data<T, Y> > dataQueue_;
    Semaphore semaphore_;

  public:
    DataQueue( Mutex coutMutex, Mutex logMutex, std::ofstream *log );
    void pushData( Data<T,Y> data );
    Data< T,Y > popData( );
    int size();

};

#endif
