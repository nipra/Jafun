package com.packtpub.JavaScraping.RMI;

import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.net.*;
import java.io.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class ScraperImp extends UnicastRemoteObject implements ScraperInterface {
	public ScraperImp() throws RemoteException {}
	public static void main() {
		System.out.println("Hello, World!");
	}
	
  public String getMessage() throws RemoteException {
	  return(scrapeTopic("wiki/Java"));
  }
  
  public static String scrapeTopic(String url){
		String html = getUrl("http://www.wikipedia.org/"+url);
		Document doc = Jsoup.parse(html);
		String contentText = doc.select("#mw-content-text > p").first().text();
		return contentText;
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