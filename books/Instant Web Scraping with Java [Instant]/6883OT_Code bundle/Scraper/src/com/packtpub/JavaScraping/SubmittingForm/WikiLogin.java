package com.packtpub.JavaScraping.SubmittingForm;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.Set;



import junit.framework.TestCase;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

public class WikiLogin extends TestCase{
	public static void main(String args[]) {
	      org.junit.runner.JUnitCore.main("SubmitForm.WikiLogin");
	}

	@Test
	public void test() throws FailingHttpStatusCodeException, MalformedURLException, IOException {

	    WebClient webClient = new WebClient(BrowserVersion.FIREFOX_10);

	    HtmlPage page1 = webClient.getPage("http://en.wikipedia.org/w/index.php?title=Special:UserLogin");
	    HtmlForm form = page1.getForms().get(0);
	    HtmlSubmitInput button = form.getInputByName("wpLoginAttempt");

	    HtmlTextInput userField = form.getInputByName("wpName");
	    HtmlPasswordInput passField = form.getInputByName("wpPassword");
	    
	    userField.setValueAttribute("<username>");
	    passField.setValueAttribute("<password>");
	    
	    HtmlPage page2 = button.click();

    	String headerText = page2.getHtmlElementById("firstHeading").getElementsByTagName("span").get(0).asText();
	    webClient.closeAllWindows();
	    assertEquals(headerText, "Login successful");
	}

}