package com.packtpub.java7.concurrency.chapter9.recipe09.task;

import java.util.concurrent.TimeUnit;

public class Task implements Runnable {

	private int array[];
	
	public Task(int array[]) {
		this.array=array;
	}
	
	@Override
	public void run() {
		for (int i=0; i<array.length; i++ ){
			array[i]++;
			try {
				TimeUnit.MILLISECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
