package com.petehouston.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

/**
 * Section08
 * List all images within a HTML document
 *
 */
public class App 
{
	public static final String CLS_NAME = "App";
	public static final String URL_SOURCE = "http://www.packtpub.com/";

    public static void main(String[] args) throws IOException
    {
    	// load document
    	Document doc = Jsoup.connect(URL_SOURCE).get();

    	// parse
    	Elements links = doc.select("img[src]");
    	System.out.println("Total results: " + links.size());
    	for(Element url: links) {
    		System.out.println("* " + url.attr("abs:src"));
    	}
    }
}
