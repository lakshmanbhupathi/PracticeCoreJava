package com.lakshman.multithreading;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * ReadWriteLocks
 *
 */
public class CachingHashMap {
    private Map<Integer,Integer> map = new HashMap<>();
    ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    Lock writeLock = readWriteLock.writeLock();
    Lock readLock = readWriteLock.readLock();

    Integer put(Integer key, Integer value){
        Integer result=null;
        try {
            // Won't allow any thread to modify / read
            writeLock.lock();
            result = map.put(key, value);
        }
        finally {
            writeLock.unlock();
        }
        return result;
    }

    Integer get(Integer key){
        Integer result = null;
        try{
            // multiple reads will allowed like usual but won't allow tom read while writing
            readLock.lock();
            result = map.get(key);
        }finally {
            readLock.unlock();
        }
        return result;
    }

    public static void main(String[] args) {

    }
}
