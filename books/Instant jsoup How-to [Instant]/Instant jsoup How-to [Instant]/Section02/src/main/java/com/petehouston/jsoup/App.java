package com.petehouston.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.lang.Exception;

/**
 * Section02
 * 	Extract content using DOM navigation
 *
 */
public class App 
{
	public static final String CLS_NAME = "App";
	public static final String SOURCE = "http://jsoup.org";

    public static void main(String[] args)
    {
		App app = new App(SOURCE);
		// get menu list
		Elements list = app.getMenu();
		// check condition, it should be not null and size is greater than 0
		if(list != null && list.size() > 0) {
			// get each <a> tag
			for(Element menu: list) {
				// output the menu content
				System.out.print(String.format("[%s]", menu.html()));
			}
			System.out.println("\nMenu and its relative path:");
			// query and parse each <a> tags' href attribute values
			for(Element menu: list) {
				// output the attribute values
				System.out.println(String.format("[%s] href = %s", menu.html(), menu.attr("href")));
			}
		}
    }

	/** the url to parse */
	private String mUrl = "";

	/**
	 * class constructor
	 *
	 * @param url	the input url to parse
	 */
	public App(String url) {
		mUrl = url;
	}

	/**
	 * Retrieves the menus from navigation bar
	 *
	 * @return	the list of <a> tags of all menus
	 * @see		Elements
	 */
	public Elements getMenu() {
		try {
			// load input from url
			Document doc = Jsoup.connect(mUrl).get();
			// navigate to <div class="nav-sections"> the parent of all menu items
			Elements navDivTag = doc.getElementsByClass("nav-sections");
			// get the first element since there is only one, and return a list of all sub <a> tag of menu items
			return navDivTag.get(0).getElementsByTag("a");

		} catch (Exception ex) {
			LogUtils.d(CLS_NAME, "getMenus", ex.getMessage());
			return null;
		}
	}
}
