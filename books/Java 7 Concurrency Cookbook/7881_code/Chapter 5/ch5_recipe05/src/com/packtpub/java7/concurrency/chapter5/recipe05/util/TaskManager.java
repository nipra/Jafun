package com.packtpub.java7.concurrency.chapter5.recipe05.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinTask;

import com.packtpub.java7.concurrency.chapter5.recipe05.task.SearchNumberTask;

/**
 * Class that stores all the tasks that have been sent to
 * a ForkJoinPool. Provides a method for the cancellation of
 * all the tasks
 *
 */
public class TaskManager {

	/**
	 * List of tasks
	 */
	private List<ForkJoinTask<Integer>> tasks;
	
	/**
	 * Constructor of the class. Initializes the list of tasks
	 */
	public TaskManager(){
		tasks=new ArrayList<>();
	}
	
	/**
	 * Method to add a new Task in the list
	 * @param task The new task
	 */
	public void addTask(ForkJoinTask<Integer> task){
		tasks.add(task);
	}

	/**
	 * Method that cancel all the tasks in the list
	 * @param cancelTask 
	 */
	public void cancelTasks(ForkJoinTask<Integer> cancelTask){
		for (ForkJoinTask<Integer> task  :tasks) {
			if (task!=cancelTask) {
				task.cancel(true);
				((SearchNumberTask)task).writeCancelMessage();
			}
		}
	}
}
