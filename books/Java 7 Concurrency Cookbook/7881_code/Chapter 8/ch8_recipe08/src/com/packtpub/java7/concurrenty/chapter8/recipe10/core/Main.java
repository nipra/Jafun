package com.packtpub.java7.concurrenty.chapter8.recipe10.core;

import com.packtpub.java7.concurrenty.chapter8.recipe10.task.Task1;
import com.packtpub.java7.concurrenty.chapter8.recipe10.task.Task2;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main class of the example
 */
public class Main {

    /**
     * Main method of the example
     * @param args 
     */
    public static void main(String args[]) throws Exception{
        /*
         * Create two lock objects
         */
        Lock lock1, lock2;
        lock1=new ReentrantLock();
        lock2=new ReentrantLock();
        
        /*
         * Create two tasks
         */
        Task1 task1=new Task1(lock1, lock2);
        Task2 task2=new Task2(lock1, lock2);
        
        /*
         * Execute the two tasks 
         */
        Thread thread1=new Thread(task1);
        Thread thread2=new Thread(task2);
        
        thread1.start();
        thread2.start();
        
        thread1.join();
        thread2.join();
        /*
         * While the tasks haven't finished, write a message every 500 milliseconds
         */
        /*while ((thread1.isAlive()) &&(thread2.isAlive())) {
            System.out.println("Core: The example is running");
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException ex) {
              ex.printStackTrace();
            }
        }*/
    }
}
