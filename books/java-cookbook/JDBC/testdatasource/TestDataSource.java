import javax.naming.*;
import java.sql.*;
import javax.sql.*;

/** See if a DataSource exists and is usable.
 * @version $Id: TestDataSource.java,v 1.3 2004/03/20 03:09:44 ian Exp $
 */
public class TestDataSource {

	public static void main(String[] argv) 
		throws NamingException, SQLException {

		Context ctx = new InitialContext();

		String dsn = argv[0];
		System.out.println("Looking up " + dsn);
		Object o = ctx.lookup(dsn);
		DataSource d = (DataSource)o;

		System.out.println("Getting connection ");
		Connection con = d.getConnection();
		System.out.println("Got it!");
	}
}
