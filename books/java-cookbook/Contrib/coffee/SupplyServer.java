import CoffeeMachine.*;
import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.*;
import org.omg.CORBA.*;

/** 
 * @author Ian Darwin, http://www.darwinsys.com/
 * @version $Id: SupplyServer.java,v 1.4 2004/02/09 03:33:40 ian Exp $
 */
public class SupplyServer {

	// Implementation. Extend the skeleton to get CORBA functionality.
	protected class SupplyImpl extends _SupplyImplBase {
		SupplyImpl(String name) {
			super();
		}

		// Implement all the functionality of the interface
		public int dispenseCup() {
			return 12;
		}
	}

	/** Execution of Java programs starts at main() */
	public static void main(String[] argv)
	{
		new SupplyServer().runServer(argv);
	}

	/** Do the work of contacting the ORB, registering with it,
	 * binding the Object, and waiting for clients. 
	 */
	protected void runServer(String[] argv) {
		try {
			// Create and initialize the ORB
			ORB orb = ORB.init(argv, null);

			// create servant and register it with the ORB
			SupplyImpl imp = new SupplyImpl("Fred");
			orb.connect(imp);

			// get the root naming context
			org.omg.CORBA.Object objRef = 
				orb.resolve_initial_references("NameService");
			NamingContext ncRef = NamingContextHelper.narrow(objRef);

			// bind the Object Reference in Naming
			NameComponent nc = new NameComponent("Supply", "");
			NameComponent path[] = {nc};
			ncRef.rebind(path, imp);

			// Wait for clients to invoke us. This use of Object
			// (fully-qualified because of ORG.OMG.CORBA.Object)
			// is equivalent to a UNIX pause() system call, that is,
			// an indefinite wait.
			System.out.println("Server: Waiting for clients...");
			java.lang.Object sync = new java.lang.Object();
			synchronized (sync) {
				sync.wait();
			}

		} catch (Exception e) {
			System.err.println("ERROR: " + e);
			e.printStackTrace(System.out);
		}
    }
}
