import CoffeeMachine.*;		// The package created from the IDL module
import org.omg.CosNaming.*;
import org.omg.CORBA.*;

/** This is a simple client that just lists the CDs and tracks. */
public class SupplyClient {
	public static void main(String[] argv) {
		try {
			// Initialize the ORB
			ORB orb = ORB.init(argv, null);

			// Get the root naming context. Do NOT use "bind",
			// which is a VisiBroker non-standard extension.
			// (but a very convenient one!)
			org.omg.CORBA.Object objRef =
				orb.resolve_initial_references("NameService");
			NamingContext ncRef = NamingContextHelper.narrow(objRef);

			// Find an object that supports Supply
			NameComponent nc = new NameComponent("Supply", "");
			NameComponent path[] = {nc};
			Supply mySupply = SupplyHelper.narrow(ncRef.resolve(path));

			// Interact with Object
			int x = mySupply.dispenseCup();
		} catch (Exception e) {			// Any CORBA Exceptions?
			System.out.println("Error: " + e) ;
			e.printStackTrace(System.err);
		}
	}
}
