package com.lakshman.multithreading;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class CyclicBarrierFriendCinema {
    public static void main(String[] args) {
        class Friend implements Callable<String> {
            CyclicBarrier cyclicBarrier;


            public Friend(CyclicBarrier cyclicBarrier) {
                this.cyclicBarrier = cyclicBarrier;
            }

            @Override
            public String call() throws Exception {
                try {
                    Random random = new Random();
                    Thread.sleep(random.nextInt(20) * 100);
                    System.out.println("Arrived to cafe");

                    // blocks current thread until barrier opens
                    cyclicBarrier.await();

                    // blocks thread and time blocking other wise throws TimeOut Exception
                    // other threads in block state will throw BrokenBarrierException
//                    cyclicBarrier.await(2,TimeUnit.SECONDS);

                    System.out.println("lets go to cinema");
                    return "ok";

                } catch (InterruptedException e) {
                    // will be executed when interrupted
                    System.out.println("friend thread Interrupted");
                }
                return "not ok";
            }
        }

//        ExecutorService executorService = Executors.newFixedThreadPool(4);

        // 2 threads aren't enough for breaking barrier , will lead to blocking only two threads
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        Runnable taskBeforeOpeningBarrier = () ->{
            System.out.println("Before opening barrier");
        };

        CyclicBarrier cyclicBarrier = new CyclicBarrier(4,taskBeforeOpeningBarrier);

        List<Future<String>> futures = new ArrayList<>();
        try {
            for (int i = 0; i < 4; i++) {
                Friend friend = new Friend(cyclicBarrier);
                futures.add(executorService.submit(friend));
            }

            futures.forEach(future -> {
                try {
                    // timed waiting for result
                    future.get(200,TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    System.out.println("InterruptedException");
                } catch (ExecutionException e) {
                    System.out.println("ExException");
                } catch (TimeoutException e) {

                    // time out exception will be raised after timed get() up
                    System.out.println("time out Exception");

                    // cancel to interrupt thread
                    future.cancel(true);
                }
            });
        } finally {
            executorService.shutdown();
        }
    }

}
