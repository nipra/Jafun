package com.packtpub.JavaScraping.Ajax;

import java.io.IOException;
import java.net.MalformedURLException;

import junit.framework.TestCase;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

public class Intelius extends TestCase{

	@Test
	public void test() throws FailingHttpStatusCodeException, MalformedURLException, IOException {

	    WebClient webClient = new WebClient(BrowserVersion.FIREFOX_10);
	    HtmlPage page = null;
	    webClient.setThrowExceptionOnFailingStatusCode(false);
	    webClient.setThrowExceptionOnScriptError(false);
		try {
			page = webClient.getPage("http://www.intelius.com/results.php?ReportType=1&formname=name&qf=Ryan&qmi=E&qn=Mitchell&qcs=Needham%2C+MA&focusfirst=1");
		} catch (FailingHttpStatusCodeException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		DomElement name = null;
		 for (int i = 0; i < 20; i++) {
				Boolean mblueExists = true;
				try{
					name = page.getElementById("name");
				}catch(ElementNotFoundException e){
					mblueExists = false;
				}
	            if (mblueExists) {
	                break;
	            }
	            synchronized (page) {
	                try {
				page.wait(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	            }
	        }
		   webClient.closeAllWindows();
		try{
			assertEquals("Ryan Elizabeth Mitchell", name.getElementsByTagName("em").get(0).asText());
		}catch(NullPointerException e){
			fail("name not found");
		}
	}
}
