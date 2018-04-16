package com.lakshman.multithreading;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Instead iof classic {@link ProducerConsumerProblem} which has been solved using Explicit Locking by having sync block
 * Here with out using sync solve {@link ProducerConsumerProblem} can't use wait/notify pattern as they work onlyt inside sync block/method
 *
 * Here instead of sync we use {@link java.util.concurrent.locks.Lock} interface its implementation {@link java.util.concurrent.locks.ReentrantLock}
 * ReentrantLock can replace sync block and provides more features like fairness, interrupt-ability
 *
 */
public class ProducerConsumerIntrinsicLocking {
    List<Integer> buffer = new ArrayList<>();

    Lock lock = new ReentrantLock();
    Condition isEmptyCondition = lock.newCondition();
    Condition isFullCondition = lock.newCondition();

    class Producer implements Callable<String>{

        @Override
        public String call() throws Exception {
            int count = 0;
            try {
                lock.lock();

                while(count++ < 50){
                    if(isFull(buffer)){
                        // wait == await
                        isFullCondition.await();
                    }
                }

                // produce
                buffer.add(1);
                // notify == signal
                isEmptyCondition.signal();

            }finally {
                // whatever wrong happens in try block, this will granteed to execute
                // prevents block (deadLock)
                lock.unlock();
            }

            return Thread.currentThread()+"Produced :"+ --count;
        }
    }

    private boolean isFull(List<Integer> buffer) {
        return buffer.size() == 10;
    }

    public class Consumer implements Callable<String{

        @Override
        public String call() throws Exception {
            int count = 0;

            try {
                lock.lock();
                while (count++ < 50) {
                    if(isEmpty(buffer)){
                        // wait
                        isEmptyCondition.await();
                    }

                    //consume
                    buffer.remove(buffer.size()-1);
                    //notify
                    isFullCondition.signal();
                }
            }finally {
                lock.unlock();
            }

            return "Consumed "+ count;
        }
    }

    private boolean isEmpty(List<Integer> buffer) {
        return buffer.size() == 0;
    }

}