/**
 * * INAV - Interactive Network Active-traffic Visualization
 * * Copyright © 2008  Jeff Scaparra
 * *
 * * This file is a part of the INAV project.
 * *
 * * This program is free software: you can redistribute it and/or modify
 * * it under the terms of the GNU General Public License as published by
 * * the Free Software Foundation, either version 3 of the License, or
 * * (at your option) any later version.
 * *
 * * This program is distributed in the hope that it will be useful,
 * * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * * GNU General Public License for more details.
 * *
 * * You should have received a copy of the GNU General Public License
 * * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * */

#include "threads.h"
#include "constants.h"
ReaderWriterLock::ReaderWriterLock(): num_( threadlocks::MAX_READERS )
{
  pthread_mutex_init( &countMutex_, NULL );
  pthread_mutex_init( &writerMutex_, NULL );
  pthread_mutex_init( &writerWaiting_, NULL );
  pthread_cond_init( &noMoreReaders_, NULL );
  pthread_cond_init( &countCondition_, NULL );
  //there are no initial readers
  //pthread_cond_signal( &noMoreReaders_ );
}

ReaderWriterLock::~ReaderWriterLock( )
{
}

ReaderWriterLock::ReaderWriterLock( ReaderWriterLock& lock )
{
  pthread_mutex_lock( &(lock.countMutex_) );
  num_ = lock.num_;
  pthread_mutex_unlock( &(lock.countMutex_) );
  countMutex_ = lock.countMutex_;
  writerMutex_ = lock.writerMutex_;
  writerWaiting_ = lock.writerWaiting_;
  noMoreReaders_ = lock.noMoreReaders_;
  countCondition_ = lock.countCondition_;
}

ReaderWriterLock& ReaderWriterLock::operator=( ReaderWriterLock& lock )
{
  pthread_mutex_lock( &(lock.countMutex_) );
  num_ = lock.num_;
  pthread_mutex_unlock( &(lock.countMutex_) );
  countMutex_ = lock.countMutex_;
  writerMutex_ = lock.writerMutex_;
  writerWaiting_ = lock.writerWaiting_;
  noMoreReaders_ = lock.noMoreReaders_;
  countCondition_ = lock.countCondition_;
  return *this;
}

void ReaderWriterLock::readLock()
{

  pthread_mutex_lock( &writerWaiting_ ); //There are writers waiting we must halt reading for a min
  pthread_mutex_lock( &writerMutex_ ); //by locking this we ensure there isn't a writer.
  readerWait();
  pthread_mutex_unlock( &writerMutex_ );
  pthread_mutex_unlock( &writerWaiting_ );
}

void ReaderWriterLock::readUnlock()
{
  //locks writerMutex_ to ensure that the
  //pthread_cond_signal doesn't occur between the if statement and condition wait in writelock
  pthread_mutex_lock( &writerMutex_ ); 
  readerPost();
  if( getNum() == threadlocks::MAX_READERS )
    pthread_cond_signal( &noMoreReaders_ );
  pthread_mutex_unlock( &writerMutex_ );
}

void ReaderWriterLock::writeLock()
{
  //If we don't use the writerWaiting lock it is possible for read-only threads
  //to starve a thread that needs to write because the call to pthread_cond_wait
  //unlocks the writerMutex while it waits.
  pthread_mutex_lock( &writerWaiting_ );
  pthread_mutex_lock( &writerMutex_ );
  //if there is a reader thread we must wait for them to finish...
  //Because we are holding the writerWaiting lock no more will start.
  if( getNum() != threadlocks::MAX_READERS )
    pthread_cond_wait( &noMoreReaders_, &writerMutex_ );
  //HAS THE WRITE LOCK HERE!
  pthread_mutex_unlock( &writerWaiting_ );
}

void ReaderWriterLock::writeUnlock()
{
  pthread_mutex_unlock( &writerMutex_ );
}

inline void ReaderWriterLock::readerWait()
{
  pthread_mutex_lock( &countMutex_ );
  if( num_ == 0 )
  {
    pthread_cond_wait( &countCondition_, &countMutex_ );
  }
  --num_;
  pthread_mutex_unlock( &countMutex_ );
}

inline void ReaderWriterLock::readerPost()
{
  pthread_mutex_lock( &countMutex_ );
  ++num_;
  pthread_cond_signal( &countCondition_ );
  pthread_mutex_unlock( &countMutex_ );
}

inline int ReaderWriterLock::getNum()
{
  pthread_mutex_lock( &countMutex_ );
  int tmp = num_;
  pthread_mutex_unlock( &countMutex_ );
  return tmp;
}
