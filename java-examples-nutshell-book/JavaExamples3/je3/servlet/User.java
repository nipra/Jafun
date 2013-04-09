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
package je3.servlet;

/**
 * This class represents a mailing list subscriber.
 * It has JavaBeans-style property accessor methods.
 */
public class User {
    String email;       // The user's e-mail address
    boolean html;       // Whether the user wants HTML-formatted messages
    boolean digest;     // Whether the user wants digests
    boolean deleted;    // Set by UserFactory.delete(); tested by insert()

    // The constructor is package-private.
    // See UserFactory for public methods to obtain a User object.
    User(String email, boolean html, boolean digest) {
	this.email = email;
	this.html = html;
	this.digest = digest;
	this.deleted = false;
    }

    // The following property accessors follow JavaBeans naming conventions
    public String getEmailAddress() { return email; }
    public boolean getPrefersHTML() { return html; }
    public boolean getPrefersDigests() { return digest; }
    public void setEmailAddress(String email) { this.email = email; }
    public void setPrefersHTML(boolean html) { this.html = html; }
    public void setPrefersDigests(boolean digest) { this.digest = digest; }
}
