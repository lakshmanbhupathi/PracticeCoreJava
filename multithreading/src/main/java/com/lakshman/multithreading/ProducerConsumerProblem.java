package com.lakshman.multithreading;

/**
 * {@link ProducerConsumerProblem} is classic example for Inter thread communication
 * <p>
 * As both Producer and consumer need to be run on different threads concurrently.
 * needs inter thread communication to achieve this.
 * <p>
 * Without any sync plain code will lead to the Race condition ( Dirty read & write)
 * Normal sync on method will lead to Deadlock as thread will wait for another thread
 * to complete and wise versa.
 * <p>
 * Still sync block with common lock object will not work as one the thread will be
 * waiting for another and will leads to DeadLock
 * <p>
 * Solution is by using ITC API wait & notify pattern
 * when producer got buffer full park/wait thread let consumer consume from buffer and notify producer to produce data and so on.
 * here lock is common key for both producer and consumer for communication so wait / notify will be
 * present on Object class as make this ITC communication possible.
 */
public class ProducerConsumerProblem {

    private static final int BUFFER_SIZE = 50;
    private static final Object lock = new Object();

    // fixed size buffer
    private static int[] buffer = new int[BUFFER_SIZE];

    //count index
    private static int count = 0;

    public static class Producer {

        public void produce() {
            synchronized (lock) {

                // if full then
                if (isFull(buffer)) {
                    try {
                        // parking/waiting thread
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // populating buffer
                buffer[count++] = 1;
                // notifying consumer to consume produced data
                lock.notify();
            }
        }
    }

    public static class Consumer {

        public void consume() {
            synchronized (lock) {

                // if empty
                if (isEmpty(buffer)) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // consuming buffer
                buffer[--count] = 0;
                // notify parked/waited thread to start producing
                lock.notify();

            }
        }

    }

    private static boolean isFull(int[] buffer) {
        return count == BUFFER_SIZE;
    }

    private static boolean isEmpty(int[] buffer) {
        return count == 0;
    }

    public static void main(String[] args) throws InterruptedException {
        Producer producer = new Producer();
        Consumer consumer = new Consumer();

        Runnable producerTask = () -> {
            // produces 50 times
            for (int i = 0; i <= 50; i++) {
                producer.produce();
            }
        };

        Runnable consumerTask = () -> {
             // consumes 50 times
            for (int i = 0; i <= 50; i++) {
                consumer.consume();
            }
        };

        Thread producerThread = new Thread(producerTask, "Producer");
        Thread consumerThread = new Thread(consumerTask, "Consumer");

        producerThread.start();
        consumerThread.start();

        // joining so that threads will complete their respective executions
        producerThread.join();
        consumerThread.join();

        //Asserting
        System.out.println("Elements remaining in buffer :: " + count);
    }
}
