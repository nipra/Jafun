package com.packtpub.JavaScraping.Test;

import junit.framework.TestCase;
import org.junit.Test;

public class ScraperTest extends TestCase{
	public static void main(String args[]) {
	      org.junit.runner.JUnitCore.main("tests.ScraperTest");
	}

	@Test
	public void test() {
		WikiScraper scraper = new WikiScraper();
		String scrapedText = WikiScraper.scrapeTopic();
		String openingText = "A python is a constricting snake belonging to the Python (genus), or, more generally, any snake in the family Pythonidae (containing the Python genus).";
		assertEquals("Nothing has changed!", scrapedText, openingText);
	}
}
