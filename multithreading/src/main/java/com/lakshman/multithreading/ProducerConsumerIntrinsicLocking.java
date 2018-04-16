package com.lakshman.multithreading;

/**
 * Instead iof classic {@link ProducerConsumerProblem} which has been solved using Explicit Locking by having sync block
 * Here with out using sync solve {@link ProducerConsumerProblem} can't use wait/notify pattern as they work onlyt inside sync block/method
 *
 * Here instead of sync we use {@link java.util.concurrent.locks.Lock} interface its implementation {@link java.util.concurrent.locks.ReentrantLock}
 * ReentrantLock can replace sync block and provides more features like fairness, interrupt-ability
 *
 */
public class ProducerConsumerIntrinsicLocking {
}
