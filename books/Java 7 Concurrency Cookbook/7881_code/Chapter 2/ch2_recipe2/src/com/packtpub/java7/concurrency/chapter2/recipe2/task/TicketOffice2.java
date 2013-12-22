package com.packtpub.java7.concurrency.chapter2.recipe2.task;

/**
 * This class simulates a ticket office. It sell or return tickets
 * for the two cinemas
 *
 */
public class TicketOffice2 implements Runnable {

	/**
	 * The cinema 
	 */
	private Cinema cinema;
	
	/**
	 * Constructor of the class
	 * @param cinema the cinema
	 */
	public TicketOffice2(Cinema cinema){
		this.cinema=cinema;
	}
	
	/**
	 * Core method of this ticket office. Simulates selling and returning tickets
	 */
	@Override
	public void run() {
		cinema.sellTickets2(2);
		cinema.sellTickets2(4);
		cinema.sellTickets1(2);
		cinema.sellTickets1(1);
		cinema.returnTickets2(2);
		cinema.sellTickets1(3);
		cinema.sellTickets2(2);
		cinema.sellTickets1(2);
	}

}
