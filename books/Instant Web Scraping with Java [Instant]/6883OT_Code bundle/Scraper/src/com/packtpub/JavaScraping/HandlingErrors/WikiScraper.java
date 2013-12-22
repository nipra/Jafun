package com.packtpub.JavaScraping.HandlingErrors;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.net.*;
import java.io.*;
import java.util.Random;

public class WikiScraper {
	private static Random generator;
	public static void main(String[] args) {
		generator = new Random(31415926);
		for(int i = 0; i < 10; i++){
			try{
				scrapeTopic("/wiki/Java");
			}
			catch(LinkNotFoundException e){
				System.out.println("Trying again!");
			}
		}
	}
	
	public static void scrapeTopic(String url) throws LinkNotFoundException{
		String html = getUrl("http://www.wikipedia.org/"+url);
		Document doc = Jsoup.parse(html);
		Elements links = doc.select("#mw-content-text [href~=^/wiki/((?!:).)*$]");
		if(links.size() == 0){
			throw new LinkNotFoundException("No links on page, or page malformed");
		}
		int r = generator.nextInt(links.size());
		System.out.println("Random link is: "+links.get(r).text()+" at url: "+links.get(r).attr("href"));
		scrapeTopic(links.get(r).attr("href"));
	}
	
	public static String getUrl(String url){
		URL urlObj = null;
		try{
			urlObj = new URL(url);
		}catch(MalformedURLException e){
			System.out.println("The url was malformed!");
			return "";
		}
		URLConnection urlCon = null;
		BufferedReader in = null;
		String outputText = "";
		try{
			urlCon = urlObj.openConnection();
			in = new BufferedReader(new InputStreamReader(urlCon.getInputStream()));
			String line = "";
			while((line = in.readLine()) != null){
				outputText += line;
			}
			in.close();
		}catch(IOException e){
			System.out.println("There was an error connecting to the URL");
			return "";
		}
		return outputText;
	}
}
