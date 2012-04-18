#include "dataQueue.hpp"

template< class T, class Y>
DataQueue<T,Y>::DataQueue( Mutex coutMutex, 
    			Mutex logMutex,
			std::ofstream *log ): BaseData(coutMutex, logMutex, log)
{
}

template< class T, class Y>
void DataQueue<T,Y>::pushData( Data< T, Y > data )
{
  queueMutex_.lock( );
  dataQueue_.push_back( data );
  semaphore_.post();
  queueMutex_.unlock( );
}

template< class T, class Y >
Data< T,Y > DataQueue<T,Y>::popData( )
{
  Data< T, Y >data;
  semaphore_.wait();
  queueMutex_.lock( );
  data = dataQueue_.at( 0 );
  queueMutex_.unlock( );
  return data;
}

template< class T, class Y>
int DataQueue<T,Y>::size()
{
  queueMutex_.lock();
  int size = dataQueue_.size();
  queueMutex_.unlock();
  return size;
}
