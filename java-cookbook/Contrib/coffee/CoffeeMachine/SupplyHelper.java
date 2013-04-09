/*
 * File: ./COFFEEMACHINE/SUPPLYHELPER.JAVA
 * From: COFFEEMACHINE1.IDL
 * Date: Fri Apr 23 00:24:52 1999
 *   By: C:\BIN\IDLTOJ~1.EXE Java IDL 1.2 Aug 18 1998 16:25:34
 */

package CoffeeMachine;
public class SupplyHelper {
     // It is useless to have instances of this class
     private SupplyHelper() { }

    public static void write(org.omg.CORBA.portable.OutputStream out, CoffeeMachine.Supply that) {
        out.write_Object(that);
    }
    public static CoffeeMachine.Supply read(org.omg.CORBA.portable.InputStream in) {
        return CoffeeMachine.SupplyHelper.narrow(in.read_Object());
    }
   public static CoffeeMachine.Supply extract(org.omg.CORBA.Any a) {
     org.omg.CORBA.portable.InputStream in = a.create_input_stream();
     return read(in);
   }
   public static void insert(org.omg.CORBA.Any a, CoffeeMachine.Supply that) {
     org.omg.CORBA.portable.OutputStream out = a.create_output_stream();
     write(out, that);
     a.read_value(out.create_input_stream(), type());
   }
   private static org.omg.CORBA.TypeCode _tc;
   synchronized public static org.omg.CORBA.TypeCode type() {
          if (_tc == null)
             _tc = org.omg.CORBA.ORB.init().create_interface_tc(id(), "Supply");
      return _tc;
   }
   public static String id() {
       return "IDL:CoffeeMachine/Supply:1.0";
   }
   public static CoffeeMachine.Supply narrow(org.omg.CORBA.Object that)
	    throws org.omg.CORBA.BAD_PARAM {
        if (that == null)
            return null;
        if (that instanceof CoffeeMachine.Supply)
            return (CoffeeMachine.Supply) that;
	if (!that._is_a(id())) {
	    throw new org.omg.CORBA.BAD_PARAM();
	}
        org.omg.CORBA.portable.Delegate dup = ((org.omg.CORBA.portable.ObjectImpl)that)._get_delegate();
        CoffeeMachine.Supply result = new CoffeeMachine._SupplyStub(dup);
        return result;
   }
}
