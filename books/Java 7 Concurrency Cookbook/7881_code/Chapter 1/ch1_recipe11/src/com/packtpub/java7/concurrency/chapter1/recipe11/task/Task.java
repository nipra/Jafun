package com.packtpub.java7.concurrency.chapter1.recipe11.task;

import java.util.Random;

/**
 * Class that implements the concurrent task
 *
 */
public class Task implements Runnable {

	@Override
	public void run() {
		int result;
		// Create a random number generator
		Random random=new Random(Thread.currentThread().getId());
		while (true) {
			// Generate a random number a calculate 1000 divide by that random number
			result=1000/((int)(random.nextDouble()*1000));
			System.out.printf("%s : %f\n",Thread.currentThread().getId(),result);
			// Check if the Thread has been interrupted
			if (Thread.currentThread().isInterrupted()) {
				System.out.printf("%d : Interrupted\n",Thread.currentThread().getId());
				return;
			}
		}
	}
}
