package com.packtpub.java7.concurrency.chapter9.recipe03.task;

import java.util.concurrent.atomic.AtomicInteger;

public class TaskAtomic implements Runnable {

	private AtomicInteger number;
	
	public TaskAtomic () {
		this.number=new AtomicInteger();
	}
	
	@Override
	public void run() {
		for (int i=0; i<1000000; i++) {
			number.set(i);
		}
	}

}
