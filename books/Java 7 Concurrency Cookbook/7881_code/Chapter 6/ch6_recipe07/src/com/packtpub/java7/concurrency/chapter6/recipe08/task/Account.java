package com.packtpub.java7.concurrency.chapter6.recipe08.task;

import java.util.concurrent.atomic.AtomicLong;

/**
 * This class simulate a bank account 
 *
 */
public class Account {

	/**
	 * Balance of the bank account
	 */
	private AtomicLong balance;
	
	public Account(){
		balance=new AtomicLong();
	}

	/**
	 * Returns the balance of the account
	 * @return the balance of the account
	 */
	public long getBalance() {
		return balance.get();
	}

	/**
	 * Establish the balance of the account
	 * @param balance the new balance of the account
	 */
	public void setBalance(long balance) {
		this.balance.set(balance);
	}
	
	/**
	 * Add an import to the balance of the account
	 * @param amount import to add to the balance
	 */
	public void addAmount(long amount) {
		this.balance.getAndAdd(amount);
	}
	
	/**
	 * Subtract an import to the balance of the account
	 * @param amount import to subtract to the balance
	 */
	public void subtractAmount(long amount) {
		this.balance.getAndAdd(-amount);
	}
	
}
