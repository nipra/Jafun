package com.packtpub.java7.concurrency.chapter1.recipe1.core;

import com.packtpub.java7.concurrency.chapter1.recipe1.task.Calculator;

/**
 *  Main class of the example
 */
public class Main {

	/**
	 * Main method of the example
	 * @param args
	 */
	public static void main(String[] args) {

		//Launch 10 threads that make the operation with a different number
		for (int i=1; i<=10; i++){
			Calculator calculator=new Calculator(i);
			Thread thread=new Thread(calculator);
			thread.start();
		}
	}
}
