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
import java.sql.*;

/** 
 * A class for creating new users in a database and retreiving existing users
 * from the database.  Note that it assumes that a database table with the 
 * specified name exists.  You can create such a table with SQL like this:
 *
 *   CREATE TABLE subscribers (
 *       email VARCHAR(64) PRIMARY KEY,
 *       password VARCHAR(20),
 *       html BIT,
 *       digest BIT);
 **/
public class UserFactory {
    Connection db;     // The connection to the database
    String tablename;  // The name of the database table we'll use

    // These prepared statements are created in the constructor, and used
    // in the insert(), select(), update() and delete() methods.
    // PreparedStatements are used for security so that the database cannot
    // be attacked with usernames and passwords that include SQL quotes.
    PreparedStatement insertUser, selectUser, updateUser, deleteUser;

    // Create a new UserFactory object backed by the specified DB table
    public UserFactory(Connection db, String tablename) throws SQLException {
	this.db = db;
	this.tablename = tablename;

	// Prepare the SQL statements we'll use later.  Parameters will be 
	// subsituted for the question marks in the methods below.
	insertUser = db.prepareStatement("insert into " + tablename + 
					 "(email,password,html,digest) " +
					 "values(?,?,0,0)");
	selectUser = db.prepareStatement("select * from " + tablename +
					 " where email=?");
	deleteUser = db.prepareStatement("delete from " + tablename +
					 " where email=?");
	updateUser = db.prepareStatement("update " + tablename +
					 " set html=?,digest=? where email=?");
    }

    // Create a new User with the specified e-mail address and password
    public User insert(String email, String password)
	throws UserAlreadyExists, SQLException
    {
	// Check whether the user already exists
	selectUser.setString(1, email);
	ResultSet results = selectUser.executeQuery();
	if (results.next()) throw new UserAlreadyExists(email);

	// If not, create a new entry in the database
	insertUser.setString(1, email);
	insertUser.setString(2, password);
	insertUser.executeUpdate();
	
	// And return a matching User object to the caller
	return new User(email, false, false);
    }

    // Look up the User object for the specified address and password
    public User select(String email, String password)
	throws NoSuchUser, BadPassword, SQLException
    {
	// Look up the user
	selectUser.setString(1, email);
	ResultSet results = selectUser.executeQuery();
	// Check that the user exists
	if (!results.next()) throw new NoSuchUser(email);
	// Check that the password is correct
	String pw = results.getString("password");
	if (!pw.equals(password)) throw new BadPassword(email);
	// Return a User object representing this user and their mail prefs.
	boolean html = results.getInt("html") == 1;
	boolean digest = results.getInt("digest") == 1;
	return new User(email, html, digest);
    }

    // Delete the specified User object from the database
    public void delete(User user) throws SQLException {
	if (user.deleted) return;  // make sure we're not already deleted
	// Delete the user from the database
	deleteUser.setString(1, user.getEmailAddress());
	deleteUser.executeUpdate();
	user.deleted = true;  // Don't allow update() after delete()
    }

    // Update the HTML and digest preferences of the specified User
    public void update(User user) throws SQLException {
	if (user.deleted) return;  // Don't allow updates to deleted users
	// Update the database record to reflect new preferences
	updateUser.setInt(1, user.getPrefersHTML()?1:0);
	updateUser.setInt(2, user.getPrefersDigests()?1:0);
	updateUser.setString(3, user.getEmailAddress());
	updateUser.executeUpdate();
    }

    // The following are custom exception types that we may throw
    public static class UserAlreadyExists extends Exception {
	public UserAlreadyExists(String msg) { super(msg); }
    }

    public static class NoSuchUser extends Exception {
	public NoSuchUser(String msg) { super(msg); }
    }

    public static class BadPassword extends Exception {
	public BadPassword(String msg) { super(msg); }
    }
}
