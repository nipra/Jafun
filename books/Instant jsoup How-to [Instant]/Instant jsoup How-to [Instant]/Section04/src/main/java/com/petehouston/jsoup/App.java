package com.petehouston.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Tag;
import org.jsoup.nodes.Element;
import java.io.File;
import java.io.IOException;
import org.jsoup.nodes.Document.OutputSettings;

/**
 * Section04
 * Modify elements' contents
 *
 */
public class App 
{
	public static final String CLS_NAME = "App";

    public static void main( String[] args )
    {
		// load html from file
		Document doc = loadHtmlFromFile("index.html", "utf-8");	
		if(doc == null)	{
			LogUtils.d(CLS_NAME, "main", "Document is null");
			return;
		}

		/* BEFORE modification */
		System.out.println("===== BEFORE =====");
		System.out.println(doc.html());

		/* add meta charset utf-8 */
		Element tagMetaCharset = new Element(Tag.valueOf("meta"), "");
		tagMetaCharset.attr("charset", "utf-8");
		// add meta tag to head
		doc.head().appendChild(tagMetaCharset);

		/* create tag <p> for description */
		Element tagPDescription = new Element(Tag.valueOf("p"), "");
		tagPDescription.text("It is a very powerful HTML parser! I love it so much...");
		// add to body
		doc.body().appendChild(tagPDescription);

		/* create tag <p> for author */
		tagPDescription.before("<p>Author: Johnathan Hedley</p>");

		/* add attribute to tag <p> author */
		Element tagPAuthor = doc.body().select("p:contains(Author)").first();
		tagPAuthor.attr("align", "center");

		/* body add class */
		doc.body().addClass("content");
		doc.body().addClass("content");
		doc.body().addClass("content2");

		/* output the final HTML */
		OutputSettings settings = new OutputSettings();
		settings.indentAmount(8);
		doc.outputSettings(settings);
		System.out.println("===== AFTER =====");
		System.out.println(doc.html());
	}

	/**
	 * load html from a file
	 *
	 * @param filename	the path to the file
	 * @param charset	the character set of the input file
	 * @return	the document structure of HTML file
	 */
	public static Document loadHtmlFromFile(String filename, String charset) {
		try {
			File file = new File(filename);
			return Jsoup.parse(file, charset);
		} catch (IOException ioEx) {
			LogUtils.d(CLS_NAME, "loadHtmlFromString", ioEx.getMessage());
			return null;
		}
	}
}
