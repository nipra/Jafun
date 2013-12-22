package com.packtpub.JavaScraping.Undercover;

import java.io.IOException;
import java.net.MalformedURLException;

import junit.framework.TestCase;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class FreeProxyLists extends TestCase{
	public static void main(String args[]) {
	      org.junit.runner.JUnitCore.main("tests.FreeProxyLists");
	}

	@Test
	public void test() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
	    WebClient webClient = new WebClient(BrowserVersion.FIREFOX_10);
		webClient.setThrowExceptionOnScriptError(false);
	    HtmlPage page = webClient.getPage("http://www.freeproxylists.net/");
	    Document jPage = Jsoup.parse(page.asXml());
	    String ipAddress = jPage.getElementsByClass("DataGrid").get(0).getElementsByTag("tbody").get(0).getElementsByClass("odd").get(0).getElementsByTag("td").get(0).getElementsByTag("a").get(0).text();
	    System.out.println(ipAddress);
	    webClient.closeAllWindows();
	    assertTrue(ipAddress.matches("(?:[0-9]{1,3}.){3}[0-9]{1,3}"));
	}
}
