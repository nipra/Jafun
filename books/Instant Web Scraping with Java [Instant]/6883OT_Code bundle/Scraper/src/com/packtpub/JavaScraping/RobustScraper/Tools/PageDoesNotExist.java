package com.packtpub.JavaScraping.RobustScraper.Tools;

/**
 * Error is thrown if the webpage does not exist
 */
public class PageDoesNotExist extends Exception{

	public PageDoesNotExist(String msg) {
			System.out.println(msg);
	}
}
