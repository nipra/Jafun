package com.packtpub.java7.concurrency.chapter1.recipe10.core;

import java.util.concurrent.TimeUnit;

import com.packtpub.java7.concurrency.chapter1.recipe10.task.Result;
import com.packtpub.java7.concurrency.chapter1.recipe10.task.SearchTask;

public class Main {

	/**
	 * Main class of the example
	 * @param args
	 */
	public static void main(String[] args) {

		// Create a ThreadGroup
		ThreadGroup threadGroup = new ThreadGroup("Searcher");
		Result result=new Result();

		// Create a SeachTask and 10 Thread objects with this Runnable
		SearchTask searchTask=new SearchTask(result);
		for (int i=0; i<5; i++) {
			Thread thread=new Thread(threadGroup, searchTask);
			thread.start();
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		// Write information about the ThreadGroup to the console
		System.out.printf("Number of Threads: %d\n",threadGroup.activeCount());
		System.out.printf("Information about the Thread Group\n");
		threadGroup.list();

		// Write information about the status of the Thread objects to the console
		Thread[] threads=new Thread[threadGroup.activeCount()];
		threadGroup.enumerate(threads);
		for (int i=0; i<threadGroup.activeCount(); i++) {
			System.out.printf("Thread %s: %s\n",threads[i].getName(),threads[i].getState());
		}

		// Wait for the finalization of the Threadds
		waitFinish(threadGroup);
		
		// Interrupt all the Thread objects assigned to the ThreadGroup
		threadGroup.interrupt();
	}

	/**
	 * Method that waits for the finalization of one of the ten Thread objects
	 * assigned to the ThreadGroup
	 * @param threadGroup
	 */
	private static void waitFinish(ThreadGroup threadGroup) {
		while (threadGroup.activeCount()>9) {
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
