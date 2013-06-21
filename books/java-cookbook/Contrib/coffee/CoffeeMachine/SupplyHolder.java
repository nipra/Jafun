/*
 * File: ./COFFEEMACHINE/SUPPLYHOLDER.JAVA
 * From: COFFEEMACHINE1.IDL
 * Date: Fri Apr 23 00:24:52 1999
 *   By: C:\BIN\IDLTOJ~1.EXE Java IDL 1.2 Aug 18 1998 16:25:34
 */

package CoffeeMachine;
public final class SupplyHolder
     implements org.omg.CORBA.portable.Streamable{
    //	instance variable 
    public CoffeeMachine.Supply value;
    //	constructors 
    public SupplyHolder() {
	this(null);
    }
    public SupplyHolder(CoffeeMachine.Supply __arg) {
	value = __arg;
    }

    public void _write(org.omg.CORBA.portable.OutputStream out) {
        CoffeeMachine.SupplyHelper.write(out, value);
    }

    public void _read(org.omg.CORBA.portable.InputStream in) {
        value = CoffeeMachine.SupplyHelper.read(in);
    }

    public org.omg.CORBA.TypeCode _type() {
        return CoffeeMachine.SupplyHelper.type();
    }
}
