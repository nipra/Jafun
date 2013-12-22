package com.petehouston.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.File;
import java.io.IOException;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

/*
 * Section06
 * Clean dirty HTML documents
 *
 */
public class App
{
	public static final String CLS_NAME = "App";

    public static void main( String[] args )
    {
		// load html from file
		Document doc = loadHtmlFromFile("index.html", "utf-8");

		// just leave if doc is null
		if(doc == null) {			
			LogUtils.d(CLS_NAME, "main", "document is null");
			return;
		}

		/* the dirty html */
		System.out.println("===BEFORE===");
		System.out.println(doc.html());

		/* create and config whitelist */
		Whitelist allowList = Whitelist.relaxed();
		allowList
			.addTags("meta", "title", "script", "iframe")
			.addAttributes("meta", "charset")
			.addAttributes("iframe", "src")
			.addProtocols("iframe", "src", "http", "https");

		/* clean the dirty doc */
		Cleaner cleaner = new Cleaner(allowList);
		Document newDoc = cleaner.clean(doc);

		/* the clean one */
		System.out.println("===AFTER===");
		System.out.println(newDoc.html());
    }

	/**
	 * load html source from file
	 *
	 * @param filename	the input file
	 * @param charset	the appropriate charset of file
	 * @return	the Document object structure of HTML file
	 */
	public static Document loadHtmlFromFile(String filename, String charset) { 
		try {
			File file = new File(filename);
			return Jsoup.parse(file, charset);
		} catch (IOException ioEx) {
			LogUtils.d(CLS_NAME, "loadHtmlFromFile", ioEx.getMessage());
			return null;
		}
	}
}
