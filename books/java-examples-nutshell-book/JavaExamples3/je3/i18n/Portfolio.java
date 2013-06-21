/*
 * Copyright (c) 2004 David Flanagan.  All rights reserved.
 * This code is from the book Java Examples in a Nutshell, 3nd Edition.
 * It is provided AS-IS, WITHOUT ANY WARRANTY either expressed or implied.
 * You may study, use, and modify it for any non-commercial purpose,
 * including teaching and use in open-source projects.
 * You may distribute it non-commercially as long as you retain this notice.
 * For a commercial use license, or to purchase the book, 
 * please visit http://www.davidflanagan.com/javaexamples3.
 */
package je3.i18n;
import java.text.*;
import java.util.*;
import java.io.*;

/**
 * A partial implementation of a hypothetical stock portfolio class.
 * We use it only to demonstrate number and date internationalization.
 **/
public class Portfolio {
    EquityPosition[] positions;        // The positions in the portfolio
    Date lastQuoteTime = new Date();   // Time for current quotes

    // Create a Portfolio
    public Portfolio(EquityPosition[] positions, Date lastQuoteTime) {
	this.positions = positions;
	this.lastQuoteTime = lastQuoteTime;
    }
    
    // A helper class: represents a single stock purchase
    static class EquityPosition {
        String name;             // Name of the stock.
        int shares;              // Number of shares held.
        Date purchased;          // When purchased.
	Currency currency;       // What currency are the prices expressed in?
        double bought;           // Purchase price per share
	double current;          // Current price per share

	// Format objects like this one are useful for parsing strings as well
	// as formating them.  This is for converting date strings to Dates.
	static DateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd");

        EquityPosition(String n, int s, String date, Currency c,
		       double then, double now) throws ParseException
	{
	    // Convert the purchased date string to a Date object.
	    // The string must be in the format yyyy-mm-dd
	    purchased = dateParser.parse(date);
	    // And store the rest of the fields, too.
            name = n; shares = s; currency = c;
	    bought = then; current = now;
        }
    }

    // Return a localized HTML-formatted string describing the portfolio
    public String toString() {
	StringBuffer b = new StringBuffer();

        // Obtain NumberFormat and DateFormat objects to format our data.
        NumberFormat number = NumberFormat.getInstance();
        NumberFormat price = NumberFormat.getCurrencyInstance();
        NumberFormat percent = NumberFormat.getPercentInstance();
        DateFormat shortdate = DateFormat.getDateInstance(DateFormat.MEDIUM);
        DateFormat fulldate = DateFormat.getDateTimeInstance(DateFormat.LONG,
							     DateFormat.LONG);


        // Print some introductory data.
	b.append("<html><body>");
        b.append("<i>Portfolio value at ").
	    append(fulldate.format(lastQuoteTime)).append("</i>");
	b.append("<table border=1>");
        b.append("<tr><th>Symbol<th>Shares<th>Purchased<th>At<th>" +
		 "Quote<th>Change</tr>");
	
        // Display the table using the format() methods of the Format objects.
        for(int i = 0; i < positions.length; i++) {
	    b.append("<tr><td>");
            b.append(positions[i].name).append("<td>");
            b.append(number.format(positions[i].shares)).append("<td>");
            b.append(shortdate.format(positions[i].purchased)).append("<td>");
	    // Set the currency to use when printing the following prices
	    price.setCurrency(positions[i].currency);
            b.append(price.format(positions[i].bought)).append("<td>");
            b.append(price.format(positions[i].current)).append("<td>");
            double change =
                (positions[i].current-positions[i].bought)/positions[i].bought;
            b.append(percent.format(change)).append("</tr>");
        }
	b.append("</table></body></html>");
	return b.toString();
    }
    
    /**
     * This is a test program that demonstrates the class
     **/
    public static void main(String[] args) throws ParseException {
	Currency dollars = Currency.getInstance("USD");
	Currency pounds = Currency.getInstance("GBP");
	Currency euros = Currency.getInstance("EUR");
	Currency yen = Currency.getInstance("JPY");

	// This is the portfolio to display.
	EquityPosition[] positions = new EquityPosition[] {
	    new EquityPosition("WWW", 400, "2003-01-03", dollars, 11.90,13.00),
	    new EquityPosition("XXX", 1100, "2003-02-02", pounds, 71.09,27.25),
	    new EquityPosition("YYY", 6000, "2003-04-17", euros, 23.37,89.12),
	    new EquityPosition("ZZZ", 100, "2003-8-10", yen, 100000,121345)
	};

	// Create the portfolio from these positions
	Portfolio portfolio = new Portfolio(positions, new Date());

        // Set the default locale using the language code and country code
	// specified on the command line.
        if (args.length == 2) Locale.setDefault(new Locale(args[0], args[1]));

	// Now display the portfolio.
	// We use a Swing dialog box to display it because the console may
	// not be able to display non-ASCII characters like currency symbols
	// for Pounds, Euros, and Yen.
	javax.swing.JOptionPane.showMessageDialog(null, portfolio,
			       Locale.getDefault().getDisplayName(),
			       javax.swing.JOptionPane.INFORMATION_MESSAGE);

	// The modal dialog starts another thread running, so we have to exit
	// explictly when the user dismisses it.
	System.exit(0);
    }
}
