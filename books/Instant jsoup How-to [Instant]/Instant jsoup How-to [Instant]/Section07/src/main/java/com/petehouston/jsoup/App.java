package com.petehouston.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.parser.Parser;

/**
 * Section07
 * List all URLs within a HTML document
 *
 */
public class App 
{
	public static final String CLS_NAME = "App";
	public static final String URL_SOURCE = "http://jsoup.org";


    public static void main(String[] args) throws IOException
    {
		// load Document
		Document doc = Jsoup.connect(URL_SOURCE).get();
			
		// select only <a> tag with "href" attribute 
		Elements links = doc.select("a[href]");
		System.out.println("Total results: " + links.size());
		for(Element url: links) {
			System.out.println(String.format("* [%s] : %s ", url.text(), url.attr("abs:href")));
		}
    }
}
