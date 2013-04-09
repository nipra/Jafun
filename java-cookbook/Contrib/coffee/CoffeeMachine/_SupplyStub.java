/*
 * File: ./COFFEEMACHINE/_SUPPLYSTUB.JAVA
 * From: COFFEEMACHINE1.IDL
 * Date: Fri Apr 23 00:24:52 1999
 *   By: C:\BIN\IDLTOJ~1.EXE Java IDL 1.2 Aug 18 1998 16:25:34
 */

package CoffeeMachine;
public class _SupplyStub
	extends org.omg.CORBA.portable.ObjectImpl
    	implements CoffeeMachine.Supply {

    public _SupplyStub(org.omg.CORBA.portable.Delegate d) {
          super();
          _set_delegate(d);
    }

    private static final String _type_ids[] = {
        "IDL:CoffeeMachine/Supply:1.0"
    };

    public String[] _ids() { return (String[]) _type_ids.clone(); }

    //	IDL operations
    //	    Implementation of ::CoffeeMachine::Supply::dispenseCup
    public int dispenseCup()
 {
           org.omg.CORBA.Request r = _request("dispenseCup");
           r.set_return_type(org.omg.CORBA.ORB.init().get_primitive_tc(org.omg.CORBA.TCKind.tk_long));
           r.invoke();
           int __result;
           __result = r.return_value().extract_long();
           return __result;
   }

};
