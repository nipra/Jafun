package com.packtpub.java7.concurrency.chapter2.recipe5.task;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This class simulates a print queue. 
 *
 */
public class PrintQueue {

	/**
	 * Creates a lock to control the access to the queue.
	 * With the boolean attribute, we control the fairness of
	 * the Lock
	 */
	private final Lock queueLock=new ReentrantLock(false);
	
	/**
	 * Method that prints the Job. The printing is divided in two phase two
	 * show how the fairness attribute affects the election of the thread who
	 * has the control of the lock
	 * @param document The document to print
	 */
	public void printJob(Object document){
		queueLock.lock();
		
		try {
			Long duration=(long)(Math.random()*10000);
			System.out.printf("%s: PrintQueue: Printing a Job during %d seconds\n",Thread.currentThread().getName(),(duration/1000));
			Thread.sleep(duration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			queueLock.unlock();
		}
		
		
		queueLock.lock();
		try {
			Long duration=(long)(Math.random()*10000);
			System.out.printf("%s: PrintQueue: Printing a Job during %d seconds\n",Thread.currentThread().getName(),(duration/1000));
			Thread.sleep(duration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			queueLock.unlock();
		}
	}
}
