package com.packtpub.java7.concurrency.chapter2.recipe6.task;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This class implements a buffer to stores the simulate file lines between the
 * producer and the consumers
 * 
 */
public class Buffer {

	/**
	 * The buffer
	 */
	private LinkedList<String> buffer;

	/**
	 * Size of the buffer
	 */
	private int maxSize;

	/**
	 * Lock to control the access to the buffer
	 */
	private ReentrantLock lock;

	/**
	 * Conditions to control that the buffer has lines and has empty space
	 */
	private Condition lines;
	private Condition space;

	/**
	 * Attribute to control where are pending lines in the buffer
	 */
	private boolean pendingLines;

	/**
	 * Constructor of the class. Initialize all the objects
	 * 
	 * @param maxSize
	 *            The size of the buffer
	 */
	public Buffer(int maxSize) {
		this.maxSize = maxSize;
		buffer = new LinkedList<>();
		lock = new ReentrantLock();
		lines = lock.newCondition();
		space = lock.newCondition();
		pendingLines = true;
	}

	/**
	 * Insert a line in the buffer
	 * 
	 * @param line
	 *            line to insert in the buffer
	 */
	public void insert(String line) {
		lock.lock();
		try {
			while (buffer.size() == maxSize) {
				space.await();
			}
			buffer.offer(line);
			System.out.printf("%s: Inserted Line: %d\n", Thread.currentThread()
					.getName(), buffer.size());
			lines.signalAll();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Returns a line from the buffer
	 * 
	 * @return a line from the buffer
	 */
	public String get() {
		String line=null;
		lock.lock();		
		try {
			while ((buffer.size() == 0) &&(hasPendingLines())) {
				lines.await();
			}
			
			if (hasPendingLines()) {
				line = buffer.poll();
				System.out.printf("%s: Line Readed: %d\n",Thread.currentThread().getName(),buffer.size());
				space.signalAll();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
		return line;
	}

	/**
	 * Establish the value of the variable
	 * 
	 * @param pendingLines
	 */
	public void setPendingLines(boolean pendingLines) {
		this.pendingLines = pendingLines;
	}

	/**
	 * Returns the value of the variable
	 * 
	 * @return the value of the variable
	 */
	public boolean hasPendingLines() {
		return pendingLines || buffer.size() > 0;
	}

}
