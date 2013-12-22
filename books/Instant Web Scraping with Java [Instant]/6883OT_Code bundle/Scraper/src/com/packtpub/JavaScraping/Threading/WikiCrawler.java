package com.packtpub.JavaScraping.Threading;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

class WikiCrawler extends Thread {
	
	private static Random generator;
	private static Connection dbConn;
	
    public WikiCrawler(String str) {
    	super(str);
		generator = new Random(27182845);

		String dbURL = "jdbc:mysql://localhost:3306/java";
		Properties connectionProps = new Properties();
		connectionProps.put("user", "root");
		connectionProps.put("password", "root");
		dbConn = null;
		try {
			dbConn = DriverManager.getConnection(dbURL, connectionProps);
		} catch (SQLException e) {
			System.out.println("There was a problem connecting to the database");
			e.printStackTrace();
		}

		PreparedStatement useStmt;
		try {
			useStmt = dbConn.prepareStatement("USE java");
			useStmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}   
    }
    
    public void run() {
    	String newUrl = getName();
        for (int i = 0; i < 10; i++) {
            System.out.println(i + " " + getName());
            newUrl = scrapeTopic(newUrl);
            if(newUrl == ""){
            	newUrl = getName();
            }
            try {
                sleep((int)(Math.random() * 1000));
            } catch (InterruptedException e) {}
        }
        System.out.println("DONE! " + getName());
    }
    

	public static String scrapeTopic(String url){
		String newUrl = "";
		String html = getUrl("http://www.wikipedia.org/"+url);
		Document doc = Jsoup.parse(html);
		Elements links = doc.select("#mw-content-text [href~=^/wiki/((?!:).)*$]");

		if(links.size() == 0){
			System.out.println("No links found at "+url+".");
			return "";
		}
		int r = generator.nextInt(links.size());
		newUrl = links.get(r).attr("href");
		System.out.println("Random link is: "+links.get(r).text()+" at url: "+newUrl);
		writeToDB(links.get(r).text(), newUrl);
		return newUrl;
	}    

	/**
	 * Takes in a Wikipedia title and relative URL, writes them both to the wikipedia table in the database
	 * using dbConn
	 * @param title
	 * @param url
	 */
	private static void writeToDB(String title, String url){
		PreparedStatement useStmt;
		try {
			useStmt = dbConn.prepareStatement("INSERT INTO wikipedia (title, url) VALUES (?,?)");
			useStmt.setString(1, title);
			useStmt.setString(2, url);
			useStmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
