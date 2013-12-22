package com.packtpub.java7.concurrency.chapter6.recipe06.core;

import java.util.Map;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import com.packtpub.java7.concurrency.chapter6.recipe06.task.Task;
import com.packtpub.java7.concurrency.chapter6.recipe06.util.Contact;

/**
 * Main class of the example. It executes twenty-five tasks that
 * store contacts in the navigable map and then shows part of the content
 * of that navigable map
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/*
		 * Create the navigable map
		 */
		ConcurrentSkipListMap<String, Contact> map;
		map=new ConcurrentSkipListMap<>();
		
		/*
		 * Create an array to store the 25 threads that execute
		 * the tasks
		 */
		Thread threads[]=new Thread[25];
		int counter=0;
		
		/*
		 * Execute the 25 tasks
		 */
		for (char i='A'; i<'Z'; i++) {
			Task task=new Task(map, String.valueOf(i));
			threads[counter]=new Thread(task);
			threads[counter].start();
			counter++;
		}
		
		/*
		 * Wait for the finalization of the threads
		 */
		for (int i=0; i<threads.length; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		/*
		 * Write the size of the map
		 */
		System.out.printf("Main: Size of the map: %d\n",map.size());
		
		/*
		 * Write the first element of the map
		 */
		Map.Entry<String, Contact> element;
		Contact contact;
		
		element=map.firstEntry();
		contact=element.getValue();
		System.out.printf("Main: First Entry: %s: %s\n",contact.getName(),contact.getPhone());
		
		/*
		 * Write the last element of the map
		 */
		element=map.lastEntry();
		contact=element.getValue();
		System.out.printf("Main: Last Entry: %s: %s\n",contact.getName(),contact.getPhone());

		/*
		 * Write a subset of the map 
		 */
		System.out.printf("Main: Submap from A1996 to B1002: \n");
		ConcurrentNavigableMap<String, Contact> submap=map.subMap("A1996", "B1002");
		do {
			element=submap.pollFirstEntry();
			if (element!=null) {
				contact=element.getValue();
				System.out.printf("%s: %s\n",contact.getName(),contact.getPhone());
			}
		} while (element!=null);
	}
}
