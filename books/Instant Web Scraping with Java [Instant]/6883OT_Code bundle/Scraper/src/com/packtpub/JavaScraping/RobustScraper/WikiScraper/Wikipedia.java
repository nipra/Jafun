package com.packtpub.JavaScraping.RobustScraper.WikiScraper;
import java.io.IOException;
import java.net.MalformedURLException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.packtpub.JavaScraping.RobustScraper.Tools.IPResolver;
import com.packtpub.JavaScraping.RobustScraper.Tools.Webpage;

public class Wikipedia {

	/**
	 * Crawls both English and German Wikipedias. Finds 10 random German Wikipedias by following redirects
	 * through the "special:random" page, and returns the country of origin for the first IP address
	 * (if any) that it finds on the page. After this, does the same for English Wikipedia edits.
	 * @param args
	 */
	//Crawls both English and German Wikipedias. Returns physical address, and datetime edits were made
	public static void main(String[] args) {
		for(int i = 0; i < 10; i++){
			String randomPage = getRandomHist("de");
			System.out.println("This page has been edited in "+getEditCountry(randomPage));
		}
		for(int j = 0; j < 10; j++){
			String randomPage = getRandomHist("en");
			System.out.println("This page has been edited in "+getEditCountry(randomPage));
		}
	}
	
	private static String getRandomHist(String language){
		Webpage randomPage = new Webpage("http://"+language+".wikipedia.org/wiki/Special:Random");
		
		String randomUrl = "";			
		try {
				if(language == "en"){
					randomUrl = randomPage.getRedirect();
				}else{
					randomUrl = randomPage.getRedirect(true);
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		if(randomUrl == ""){
			return "";
		}
		String randHist = randomUrl.replace("http://"+language+".wikipedia.org/wiki/", "");
		randHist = "http://en.wikipedia.org/w/index.php?title="+randHist+"&action=history";
		return randHist;
	}
	
	private static String getEditCountry(String histPageUrl){
		Webpage histPage = new Webpage(histPageUrl);
		Document histDoc = histPage.getJsoup();
		Elements rows = histDoc.select("li");
		for(Element row : rows){
			System.out.println(row.select(".mw-userlink").text());
			if(row.select(".mw-userlink").text().matches("^(?:[0-9]{1,3}.){3}[0-9]{1,3}$")){
				//"Username" is an IP Address
				IPResolver IPAddress = new IPResolver(row.select(".mw-userlink").text());
				return IPAddress.getCountry();
			}
		}
		return "No IP Addresses Found in Edit History";
	}
}
