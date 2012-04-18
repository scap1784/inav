package network;

import trove.TLongIntHashMap;

public class Buffer
{
    static TLongIntHashMap buffer = new TLongIntHashMap(0);
    
    public Buffer() 
    { }
    
  /**
   * it doesn't matter if we are adding or removing from the queue.. Only the last action
   * (read in a linear fashion off the wire) will be stored.
   */ 
    synchronized public static void put(long key, int value)
    {
        buffer.put(key, value);
    }
    
    synchronized public static TLongIntHashMap get()
    {
        if (buffer.isEmpty())
            return null;
        TLongIntHashMap bufferTemp = buffer;
        buffer = new TLongIntHashMap(0);
        return bufferTemp;
    }
}
