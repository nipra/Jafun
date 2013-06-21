/*
 * File: ./COFFEEMACHINE/_SUPPLYIMPLBASE.JAVA
 * From: COFFEEMACHINE1.IDL
 * Date: Fri Apr 23 00:24:52 1999
 *   By: C:\BIN\IDLTOJ~1.EXE Java IDL 1.2 Aug 18 1998 16:25:34
 */

package CoffeeMachine;
public abstract class _SupplyImplBase extends org.omg.CORBA.DynamicImplementation implements CoffeeMachine.Supply {
    // Constructor
    public _SupplyImplBase() {
         super();
    }
    // Type strings for this class and its superclases
    private static final String _type_ids[] = {
        "IDL:CoffeeMachine/Supply:1.0"
    };

    public String[] _ids() { return (String[]) _type_ids.clone(); }

    private static java.util.Dictionary _methods = new java.util.Hashtable();
    static {
      _methods.put("dispenseCup", new java.lang.Integer(0));
     }
    // DSI Dispatch call
    public void invoke(org.omg.CORBA.ServerRequest r) {
       switch (((java.lang.Integer) _methods.get(r.op_name())).intValue()) {
           case 0: // CoffeeMachine.Supply.dispenseCup
              {
              org.omg.CORBA.NVList _list = _orb().create_list(0);
              r.params(_list);
              int ___result;
                            ___result = this.dispenseCup();
              org.omg.CORBA.Any __result = _orb().create_any();
              __result.insert_long(___result);
              r.result(__result);
              }
              break;
            default:
              throw new org.omg.CORBA.BAD_OPERATION(0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
       }
 }
}
