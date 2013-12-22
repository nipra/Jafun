
package com.packtpub.java7.concurrenty.chapter8.recipe10.task;

import java.util.concurrent.locks.Lock;

/**
 * This task implements the second task of the example
 */
public class Task2 implements Runnable{

    /**
     * Two locks used by the example
     */
    private Lock lock1, lock2;
    
    /**
     * Constructor for the class. Initialize its attributes
     * @param lock1 A lock used by the class
     * @param lock2 A lock used by the class
     */
    public Task2(Lock lock1, Lock lock2) {
        this.lock1=lock1;
        this.lock2=lock2;
    }
    
    /**
     * Main method of the task
     */
    @Override
    public void run() {
        lock2.lock();
        System.out.printf("Task 2: Lock 2 locked\n");
        lock1.lock();
        System.out.printf("Task 2: Lock 1 locked\n");
        lock1.unlock();
        lock2.unlock();
    }
    
}
