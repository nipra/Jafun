package com.petehouston.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.File;
import java.io.IOException;


/**
 * Section01
 * Get input for Jsoup parser 
 *
 */
public class App 
{
	public static final String CLS_NAME = "App";

    public static void main( String[] args )
    {
		// load html from a html string
		loadHtmlFromString("<html><head><title>Section 01: Load HTML from a string</title></head><body><p>This is the content body.</p></body></html>");

		// load html from file
		loadHtmlFromFile("index.html", "utf-8", "http://jsoup.org");	

		// load html from url
		loadHtmlFromURL("http://jsoup.org");
    }

	/**
	 * load html from an html string
	 *
	 * @param html	the input html string
	 * @return
	 */
	public static void loadHtmlFromString(String html) {
		Document doc = Jsoup.parse(html);
	}

	/**
	 * load html from a file
	 *
	 * @param filename	the path to the file
	 * @param charset	the character set of the input file
	 * @param baseUri	the base URI of the file
	 * @return
	 */
	public static void loadHtmlFromFile(String filename, String charset, String baseUri) {
		try {
			File file = new File(filename);
			Document doc = Jsoup.parse(file, charset, baseUri);
		} catch (IOException ioEx) {
			LogUtils.d(CLS_NAME, "loadHtmlFromString", ioEx.getMessage());
		}
	}
	
	/**
	 * load html from an url
	 *
	 * @param url	the url to load
	 * @return 
	 */
	public static void loadHtmlFromURL(String url) {
		try {
			Document doc = Jsoup.connect(url).get();
		} catch (IOException ioEx) {
			LogUtils.d(CLS_NAME, "loadHtmlFromURL", ioEx.getMessage());
		} catch (Exception ex) {
			LogUtils.d(CLS_NAME, "loadHtmlFromURL", ex.getMessage());
		}
	}

	/**
	 * load a html fragment string
	 * 
	 * @param fragment	a html fragment string
	 * @return
	 */
	public static void loadHtmlFromStringFragment(String fragment) {
		Document doc = Jsoup.parseBodyFragment(fragment);
	}

}
