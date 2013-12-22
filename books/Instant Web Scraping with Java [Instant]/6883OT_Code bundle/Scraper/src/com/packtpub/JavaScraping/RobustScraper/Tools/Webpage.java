package com.packtpub.JavaScraping.RobustScraper.Tools;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.net.*;
import java.io.*;

public class Webpage {

	/**
	 * @param url the url of the target page
	 * @param urlObj Java url object used by several methods, global variable here for convenience
	 */
	public String url;
	public URL urlObj;
	
	/**
	 * Sets urlObj, throws error if the URL is invalid
	 * @param declaredUrl
	 */
	public Webpage(String declaredUrl){
		url = declaredUrl;
		urlObj = null;
		try{
			urlObj = new URL(url);
		}catch(MalformedURLException e){
			System.out.println("The url was malformed!");
		}
	}
	
	/**
	 * Used to overload the getRedirect(Boolean secondRedirect) method if no Boolean is provided. 
	 * By default, this will perform only one redirect and return the result. 
	 * @return Returns final redirected URL, or an empty string if none was found.
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public String getRedirect() throws MalformedURLException, IOException{
		try{
			return getRedirect(false);
		}catch(MalformedURLException e){
			System.out.println("The url was malformed!");
		}catch(IOException e){
			System.out.println("There was an error connecting to the website");
		}
		return "";
	}
	
	/**
	 * Used to get the URL that a page redirects to, or, optionally, the second redirect that a page goes to.
	 * @param secondRedirect "True" if the redirect should be followed twice
	 * @return URL that the page redirects to
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public String getRedirect(Boolean secondRedirect) throws MalformedURLException, IOException{
		HttpURLConnection URLCon = (HttpURLConnection)urlObj.openConnection();
		URLCon.setInstanceFollowRedirects(false);
		URLCon.connect();
		String header = URLCon.getHeaderField( "Location" );
		System.out.println("First header is: "+URLCon.getHeaderField(7));
		System.out.println("Redirect is: "+header);
		
		//If a second redirect is required
		if(secondRedirect){
			try{
				URL newURL = new URL(header);
				HttpURLConnection newCon = (HttpURLConnection)newURL.openConnection();
				newCon.setInstanceFollowRedirects(false);
				newCon.connect();
				String finalHeader = newCon.getHeaderField( "Location" );
				return finalHeader;
			}catch(MalformedURLException e){
				System.out.println("The url was malformed!");
			}
			return "";
		}else{
			return header;
		}
	}
	
	/**
	 * Gets the text version of the webpage
	 * @return
	 */
	public String getString(){
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
			return null;
		}
		return outputText;
	}
	
	/**
	 * @return Jsoup of urlObj page
	 */
	public Document getJsoup(){
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
			return null;
		}
		return Jsoup.parse(outputText);
	}
		
}
