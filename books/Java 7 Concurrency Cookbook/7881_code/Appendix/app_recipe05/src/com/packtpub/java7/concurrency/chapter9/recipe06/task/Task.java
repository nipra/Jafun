package com.packtpub.java7.concurrency.chapter9.recipe06.task;

public class Task implements Runnable {

	@Override
	public void run() {
		int r;
		for (int i=0; i<1000000; i++) {
			r=0;
			r++;
			r++;
			r*=r;
		}
	}

}
