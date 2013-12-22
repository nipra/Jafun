package com.packtpub.java7.concurrency.chapter1.recipe10.task;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Class that simulates a search operation
 *
 */
public class SearchTask implements Runnable {

	/**
	 * Store the name of the Thread if this Thread finish and is not interrupted
	 */
	private Result result;
	
	/**
	 * Constructor of the class
	 * @param result Parameter to initialize the object that stores the results
	 */
	public SearchTask(Result result) {
		this.result=result;
	}

	@Override
	public void run() {
		String name=Thread.currentThread().getName();
		System.out.printf("Thread %s: Start\n",name);
		try {
			doTask();
			result.setName(name);
		} catch (InterruptedException e) {
			System.out.printf("Thread %s: Interrupted\n",name);
			return;
		}
		System.out.printf("Thread %s: End\n",name);
	}
	
	/**
	 * Method that simulates the search operation
	 * @throws InterruptedException Throws this exception if the Thread is interrupted
	 */
	private void doTask() throws InterruptedException {
		Random random=new Random((new Date()).getTime());
		int value=(int)(random.nextDouble()*100);
		System.out.printf("Thread %s: %d\n",Thread.currentThread().getName(),value);
		TimeUnit.SECONDS.sleep(value);
	}

}
