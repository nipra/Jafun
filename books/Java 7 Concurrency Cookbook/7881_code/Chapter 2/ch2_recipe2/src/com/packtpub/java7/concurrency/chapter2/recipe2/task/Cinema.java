package com.packtpub.java7.concurrency.chapter2.recipe2.task;

public class Cinema {
	
	/**
	 * This two variables store the vacancies in two cinemas
	 */
	private long vacanciesCinema1;
	private long vacanciesCinema2;

	/**
	 * Two objects for the synchronization. ControlCinema1 synchronizes the
	 * access to the vacancesCinema1 attribute and controlCinema2 synchronizes
	 * the access to the vacanciesCinema2 attribute.
	 */
	private final Object controlCinema1, controlCinema2;
	
	/**
	 * Constructor of the class. Initializes the objects
	 */
	public Cinema(){
		controlCinema1=new Object();
		controlCinema2=new Object();
		vacanciesCinema1=20;
		vacanciesCinema2=20;
	}
	
	/**
	 * This method implements the operation of sell tickets for the cinema 1
	 * @param number number of tickets sold
	 * @return true if the tickets are sold, false if there is no vacancies
	 */
	public boolean sellTickets1 (int number) {
		synchronized (controlCinema1) {
			if (number<vacanciesCinema1) {
				vacanciesCinema1-=number;
				return true;
			} else {
				return false;
			}
		}
	}
	
	/**
	 * This method implements the operation of sell tickets for the cinema 2
	 * @param number number of tickets sold
	 * @return true if the tickets are sold, false if there is no vacancies
	 */
	public boolean sellTickets2 (int number){
		synchronized (controlCinema2) {
			if (number<vacanciesCinema2) {
				vacanciesCinema2-=number;
				return true;
			} else {
				return false;
			}
		}
	}
	
	/**
	 * This method implements the operation of return tickets for the cinema 1
	 * @param number number of the tickets returned
	 * @return true
	 */
	public boolean returnTickets1 (int number) {
		synchronized (controlCinema1) {
			vacanciesCinema1+=number;
			return true;
		}
	}

	/**
	 * This method implements the operation of return tickets for the cinema 1
	 * @param number number of the tickets returned
	 * @return true
	 */
	public boolean returnTickets2 (int number) {
		synchronized (controlCinema2) {
			vacanciesCinema2+=number;
			return true;
		}
	}

	/**
	 * Return the vacancies in the cinema 1
	 * @return the vacancies in the cinema 1
	 */
	public long getVacanciesCinema1() {
		return vacanciesCinema1;
	}

	/**
	 * Return the vacancies in the cinema 2
	 * @return the vacancies in the cinema 2
	 */
	public long getVacanciesCinema2() {
		return vacanciesCinema2;
	}

}
