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
package je3.xml;
import java.io.*;

/**
 * Parse a web.xml file using the TAX pull parser and print out the servlet 
 * name-to-class and name-to-url mappings.
 **/
public class ListServlets2  {
    public static void main(String[] args)
	throws IOException, TAX.ParseException
    {
	// Create a TAX.Parser instance to parse the specified file
	TAX.Parser parser = new TAX.Parser(new FileReader(args[0]));
	// By default the parser returns TAG, TEXT and ENDTAG tokens and
	// skips others. Configure it to skip ENDTAG tokens, too.
	parser.ignoreTokens(TAX.ENDTAG);

	// Now loop through all tokens until the end of the file
	for(TAX.Token t = parser.next(); t != null; t = parser.next()) {
	    // If it is not a tag, we're not interested
	    if (t.type() != TAX.TAG) continue;
	    
	    if (t.text().equals("servlet")) {   // We found <servlet>
		parser.expect("servlet-name");  // Require <servlet-name> next
		t = parser.expect(TAX.TEXT);    // Require text token next
		String name = t.text();         // Get text from the token
		parser.expect("servlet-class"); // Require <servlet-class> next
		t = parser.expect(TAX.TEXT);    // Require text token 
		// Output name to class mapping
		System.out.println("Servlet " + name + 
				   " implemented by " + t.text());
	    }
	    else if (t.text().equals("servlet-mapping")) {
		// Now we do the same thing for <servlet-mapping> tags
		parser.expect("servlet-name");
		String name = parser.expect(TAX.TEXT).text();
		parser.expect("url-pattern");
		String mapping = parser.expect(TAX.TEXT).text();
		System.out.println("Servlet " + name + 
				   " mapped to " + mapping);
	    }
	}
    }
}
