package com.packtpub.JavaScraping.RobustScraper.Tools;
/**
 * Is thrown if a two letter language code (e.g. "en," "fr," "de") does not actually exist
 */
public class InvalidLanguage extends Exception{

	private static final long serialVersionUID = 1L;

	public InvalidLanguage(String msg) 
	{
		System.out.println(msg);
    }
}
