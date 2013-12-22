package com.packtpub.java7.concurrency.chapter2.recipe4.task;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * This class simulates the store of two prices. We will
 * have a writer that stores the prices and readers that 
 * consult this prices
 *
 */
public class PricesInfo {
	
	/**
	 * The two prices
	 */
	private double price1;
	private double price2;
	
	/**
	 * Lock to control the access to the prices
	 */
	private ReadWriteLock lock;
	
	/**
	 * Constructor of the class. Initializes the prices and the Lock
	 */
	public PricesInfo(){
		price1=1.0;
		price2=2.0;
		lock=new ReentrantReadWriteLock();
	}

	/**
	 * Returns the first price
	 * @return the first price
	 */
	public double getPrice1() {
		lock.readLock().lock();
		double value=price1;
		lock.readLock().unlock();
		return value;
	}

	/**
	 * Returns the second price
	 * @return the second price
	 */
	public double getPrice2() {
		lock.readLock().lock();
		double value=price2;
		lock.readLock().unlock();
		return value;
	}

	/**
	 * Establish the prices
	 * @param price1 The price of the first product
	 * @param price2 The price of the second product
	 */
	public void setPrices(double price1, double price2) {
		lock.writeLock().lock();
		this.price1=price1;
		this.price2=price2;
		lock.writeLock().unlock();
	}
}
