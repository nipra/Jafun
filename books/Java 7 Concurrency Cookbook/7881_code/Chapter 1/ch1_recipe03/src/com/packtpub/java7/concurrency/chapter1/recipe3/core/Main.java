package com.packtpub.java7.concurrency.chapter1.recipe3.core;

import java.util.concurrent.TimeUnit;

import com.packtpub.java7.concurrency.chapter1.recipe3.task.PrimeGenerator;

/**
 *  Main class of the sample. Launch the PrimeGenerator, waits 
 *  five seconds and interrupts the Thread
 */
public class Main {

	/**
	 * Main method of the sample. Launch the PrimeGenerator, waits
	 * five seconds and interrupts the Thread
	 * @param args
	 */
	public static void main(String[] args) {

		// Launch the prime numbers generator
		Thread task=new PrimeGenerator();
		task.start();
		
		// Wait 5 seconds
		try {
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// Interrupt the prime number generator
		task.interrupt();
	}

}
