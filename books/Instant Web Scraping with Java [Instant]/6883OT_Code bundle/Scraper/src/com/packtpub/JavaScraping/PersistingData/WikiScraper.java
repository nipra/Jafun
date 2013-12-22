package com.packtpub.JavaScraping.PersistingData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.net.*;
import java.io.*;
import java.util.Properties;
import java.util.Random;
import java.sql.*;

/**
 * WikiScraper demonstrates Java's MySQL capabilities by crawling Wikipedia pages and writing
 * titles and relative URLs to a database. 
 *
 */


public class WikiScraper {
	private static Random generator;
	private static Connection dbConn;
	
	/**
	 * Creates a random number generator, creates a new MySQL connection and connects to MySQL database. 
	 * Selects the database "java" for writing. Calls the recursive "scrapeTopic" method to begin crawling
	 * @param args
	 */
	public static void main(String[] args) {

		generator = new Random(31415926);

		String dbURL = "jdbc:mysql://localhost:3306/java";
		Properties connectionProps = new Properties();
		connectionProps.put("user", "<username>");
		connectionProps.put("password", "<password>");
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
		
		scrapeTopic("/wiki/Java");
	}
	
	/**
	 * Retrieves the Wikipedia page at the given relative URL and finds a random link to another Wikipedia
	 * article in its text. Writes the next URL's title and URL to the database and calls itself recursively.
	 * @param url
	 */
	public static void scrapeTopic(String url){
		String html = getUrl("http://www.wikipedia.org/"+url);
		Document doc = Jsoup.parse(html);
		Elements links = doc.select("#mw-content-text [href~=^/wiki/((?!:).)*$]");

		if(links.size() == 0){
			System.out.println("No links found at "+url+". Going back to Java!");
			scrapeTopic("wiki/Java");
		}
		int r = generator.nextInt(links.size());
		System.out.println("Random link is: "+links.get(r).text()+" at url: "+links.get(r).attr("href"));
		writeToDB(links.get(r).text(), links.get(r).attr("href"));
		scrapeTopic(links.get(r).attr("href"));
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