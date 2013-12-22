package com.packtpub.java7.concurrency.chapter8.recipe07.task;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.packtpub.java7.concurrency.chapter8.recipe07.logger.MyLogger;

/**
 * This class is the Task you're going to use to test the
 * Logger system you have implemented. It simply write a log
 * message indicating the start of the task, sleeps the thread for
 * two seconds and write another log message indicating the end of the
 * task.
 *
 */
public class Task implements Runnable {

	/**
	 * Main method of the task
	 */
	@Override
	public void run() {
		/*
		 * Get the Logger
		 */
		Logger logger=MyLogger.getLogger(this.getClass().getName());
		
		/*
		 * Write a message indicating the start of the task
		 */
		logger.entering(Thread.currentThread().getName(), "run()");
		
		/*
		 * Sleep the task for two seconds
		 */
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		/*
		 * Write a message indicating the end of the task
		 */
		logger.exiting(Thread.currentThread().getName(), "run()",Thread.currentThread());
	}
}
