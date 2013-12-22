package com.packtpub.java7.concurrency.chapter2.recipe2.task;

/**
 * This class simulates a sensor in the building
 */
public class Sensor1 implements Runnable {

	/**
	 * Object with the statistics of the building
	 */
	private BuildStats stats;
	
	/**
	 * Constructor of the class
	 * @param stats object with the statistics of the building
	 */
	public Sensor1(BuildStats stats){
		this.stats=stats;
	}
	
	/**
	 * Core method of the Runnable. Simulates inputs and outputs in the building
	 */
	@Override
	public void run() {
		stats.comeIn();
		stats.comeIn();
		stats.comeIn();
		stats.goOut();
		stats.comeIn();
	}

}
