package com.packtpub.JavaScraping.RobustScraper.Tools;

/**
 * IPResolver uses the free service provided by freegeoip.net to resolve IP addresses into a geographic location
 * @author rmitchell
 *
 */
public class IPResolver {

	/**
	 * @param ipAddress the IPv4 address to be resolved to location
	 * @param ipInfo plaintext location or other information that is returned
	 */
	String ipAddress;
	String ipInfo;
	public IPResolver(String declaredAddress){
		ipAddress = declaredAddress;
		Webpage ipInfoObj = new Webpage("http://freegeoip.net/xml/"+ipAddress);
		ipInfo = ipInfoObj.getString();
	}
	
	/**
	 * Simple method, returns anything between the \<CountryName\> tags (presumably the country of origin)
	 * @return String containing the name of the ipAddress country of origin
	 */
	public String getCountry(){
		int startCtry = ipInfo.indexOf("<CountryName>")+13;
		int endCtry = ipInfo.indexOf("</CountryName>");
		return ipInfo.substring(startCtry, endCtry);
	}

}