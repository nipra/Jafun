package com.packtpub.java7.concurrency.chapter9.recipe09.task;

import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

public class TaskFJ extends RecursiveAction {


	private static final long serialVersionUID = 1L;
	private int array[];
	private int start, end;
	
	public TaskFJ(int array[], int start, int end) {
		this.array=array;
		this.start=start;
		this.end=end;
	}
	
	@Override
	protected void compute() {
		if (end-start>1000) {
			int mid=(start+end)/2;
			TaskFJ task1=new TaskFJ(array,start,mid);
			TaskFJ task2=new TaskFJ(array,mid,end);
			task1.fork();
			task2.fork();
			task1.join();
			task2.join();
		} else {
			for (int i=start; i<end; i++) {
				array[i]++;
				try {
					TimeUnit.MILLISECONDS.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
