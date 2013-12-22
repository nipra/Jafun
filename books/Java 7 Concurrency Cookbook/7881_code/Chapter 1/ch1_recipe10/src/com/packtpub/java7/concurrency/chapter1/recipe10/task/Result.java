package com.packtpub.java7.concurrency.chapter1.recipe10.task;

/**
 * Class that stores the result of the search
 *
 */
public class Result {
	
	/**
	 * Name of the Thread that finish
	 */
	private String name;
	
	/**
	 * Read the name of the Thread
	 * @return The name of the Thread
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Write the name of the Thread
	 * @param name The name of the Thread
	 */
	public void setName(String name) {
		this.name = name;
	}

}
