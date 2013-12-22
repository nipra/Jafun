package com.packtpub.java7.concurrency.chapter2.recipe2.task;

import java.util.concurrent.TimeUnit;

/**
 * 
 * This class simulates a control class that stores the statistics of
 * access to a building, controlling the number of people inside the building
 *
 */
public class BuildStats {

	/**
	 * Number of people inside the building
	 */
	private long numPeople;
	
	/**
	 * Method that simulates when people come in into the building
	 */
	public /*synchronized*/ void comeIn(){
		System.out.printf("%s: A person enters.\n",Thread.currentThread().getName());
		synchronized(this) {
			numPeople++;
		}
		generateCard();
	}
	
	/**
	 * Method that simulates when people leave the building
	 */
	public /*synchronized*/ void goOut(){
		System.out.printf("%s: A person leaves.\n",Thread.currentThread().getName());
		synchronized(this) {
			numPeople--;
		}
		generateReport();
	}
	
	/**
	 * Method that simulates the generation of a card when people come in into the building
	 */
	private void generateCard(){
		try {
			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Method that simulates the generation of a report when people leaves the building
	 */
	private void generateReport(){
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method that print the number of people inside the building
	 */
	public void printStats(){
		System.out.printf("%d persons in the building.\n",numPeople);
	}
	
}
