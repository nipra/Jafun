package com.packtpub.java7.concurrency.chapter2.recipe2.core;

import com.packtpub.java7.concurrency.chapter2.recipe2.task.Cinema;
import com.packtpub.java7.concurrency.chapter2.recipe2.task.TicketOffice1;
import com.packtpub.java7.concurrency.chapter2.recipe2.task.TicketOffice2;

/**
 * Core class of the example. Creates a cinema and two threads for
 * the ticket office. Run the threads to analyze the results obtained
 *
 */
public class Main {

	/**
	 * Main method of the example
	 * @param args
	 */
	public static void main(String[] args) {
		// Creates a Cinema
		Cinema cinema=new Cinema();
		
		// Creates a TicketOffice1 and a Thread to run it
		TicketOffice1 ticketOffice1=new TicketOffice1(cinema);
		Thread thread1=new Thread(ticketOffice1,"TicketOffice1");

		// Creates a TicketOffice2 and a Thread to run it
		TicketOffice2 ticketOffice2=new TicketOffice2(cinema);
		Thread thread2=new Thread(ticketOffice2,"TicketOffice2");
		
		// Starts the threads
		thread1.start();
		thread2.start();
		
		try {
			// Waits for the finalization of the threads
			thread1.join();
			thread2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Print the vacancies in the cinemas
		System.out.printf("Room 1 Vacancies: %d\n",cinema.getVacanciesCinema1());
		System.out.printf("Room 2 Vacancies: %d\n",cinema.getVacanciesCinema2());
	}

}
