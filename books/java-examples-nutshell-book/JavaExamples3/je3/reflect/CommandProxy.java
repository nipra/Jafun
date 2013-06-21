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
package je3.reflect;
import java.lang.reflect.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;

/**
 * This class is an InvocationHandler based on a Map of method names to Command
 * objects.  When the invoke() method is called, the name of the method to be
 * invoked is looked up in the map, and the associated Command, if any is
 * invoked.  Arguments passed to invoke() are always ignored.  Note that there
 * is no public constructor for this class.  Instead, there is a static factory
 * method for creating Proxy objects that use an instance of this class.
 * Pass the interface to be implemented and a Map of name/Command pairs.
 **/
public class CommandProxy implements InvocationHandler {
    Map methodMap;  // Maps method names to Command objects that implement 

    // private constructor
    private CommandProxy(Map methodMap) { this.methodMap = methodMap; }

    // This method implements InvocationHandler, and invokes the Command,
    // if any associated with the name of Method m.  It ignores args[] and
    // always returns null.
    public Object invoke(Object p, Method m, Object[] args) throws Throwable {
	String methodName = m.getName();
	Command command = (Command) methodMap.get(methodName);
	if (command != null) command.invoke();
	return null;
    }

    // Return an object that implements the specified interface, using the
    // name-to-Command map as the implementation of the interface methods.
    public static Object create(Class iface, Map methodMap) {
	InvocationHandler handler = new CommandProxy(methodMap);
	ClassLoader loader = handler.getClass().getClassLoader();
	return Proxy.newProxyInstance(loader, new Class[] { iface }, handler);
    }

    // This is a test class to demonstrate the use of CommandProxy.
    static class Test {
	public static void main(String[] args)  throws java.io.IOException {
	    // Set up a simple GUI
	    javax.swing.JFrame f = new javax.swing.JFrame("Command Test");
	    javax.swing.JButton b = new javax.swing.JButton("Hello World");
	    f.getContentPane().add(b, java.awt.BorderLayout.CENTER);
	    f.pack();
	    f.show();

	    // Set up the Map of method names to Command objects
	    Map methodMap = new HashMap();
	    methodMap.put("focusGained",Command.parse(b,"setText(\"hello\")"));
	    methodMap.put("focusLost",Command.parse(b,"setText(\"goodbye\")"));

	    // Use CommandProxy.create() to create a proxy FocusListener
	    FocusListener l =
		(FocusListener)CommandProxy.create(FocusListener.class,
						   methodMap);
		       
	    // Use the synthetic FocusListener
	    b.addFocusListener(l);
	}
    }
}
