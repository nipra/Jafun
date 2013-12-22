package com.packtpub.java7.concurrency.chapter3.recipe6.task;

import java.util.Date;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * This class implements an student in the exam 
 *
 */
public class Student implements Runnable {

	/**
	 * Phaser to control the execution
	 */
	private Phaser phaser;
	
	/**
	 * Constructor of the class. Initialize its objects
	 * @param phaser Phaser to control the execution
	 */
	public Student(Phaser phaser) {
		this.phaser=phaser;
	}

	/**
	 * Main method of the student. It arrives to the exam and does three exercises. After each
	 * exercise, it calls the phaser to wait that all the students finishes the same exercise
	 */
	public void run() {
		System.out.printf("%s: Has arrived to do the exam. %s\n",Thread.currentThread().getName(),new Date());
		phaser.arriveAndAwaitAdvance();
		System.out.printf("%s: Is going to do the first exercise. %s\n",Thread.currentThread().getName(),new Date());
		doExercise1();
		System.out.printf("%s: Has done the first exercise. %s\n",Thread.currentThread().getName(),new Date());
		phaser.arriveAndAwaitAdvance();
		System.out.printf("%s: Is going to do the second exercise. %s\n",Thread.currentThread().getName(),new Date());
		doExercise2();
		System.out.printf("%s: Has done the second exercise. %s\n",Thread.currentThread().getName(),new Date());
		phaser.arriveAndAwaitAdvance();
		System.out.printf("%s: Is going to do the third exercise. %s\n",Thread.currentThread().getName(),new Date());
		doExercise3();
		System.out.printf("%s: Has finished the exam. %s\n",Thread.currentThread().getName(),new Date());
		phaser.arriveAndAwaitAdvance();
	}

	/**
	 * Does an exercise is to wait a random time 
	 */
	private void doExercise1() {
		try {
			Long duration=(long)(Math.random()*10);
			TimeUnit.SECONDS.sleep(duration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Does an exercise is wait a random time 
	 */
	private void doExercise2() {
		try {
			Long duration=(long)(Math.random()*10);
			TimeUnit.SECONDS.sleep(duration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Does an exercise is wait a random time 
	 */
	private void doExercise3() {
		try {
			Long duration=(long)(Math.random()*10);
			TimeUnit.SECONDS.sleep(duration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


}
