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
import java.awt.event.*;
import java.beans.*;
import java.lang.reflect.*;
import java.io.*;
import java.util.*;
import je3.classes.Tokenizer;
import je3.classes.CharSequenceTokenizer;

/**
 * This class represents a Method, the list of arguments to be passed
 * to that method, and the object on which the method is to be invoked.
 * The invoke() method invokes the method.  The actionPerformed() method
 * does the same thing, allowing this class to implement ActionListener
 * and be used to respond to ActionEvents generated in a GUI or elsewhere. 
 * The static parse() method parses a string representation of a method
 * and its arguments.
 **/
public class Command implements ActionListener, InvocationHandler {
    Method m;       // The method to be invoked
    Object target;  // The object to invoke it on
    Object[] args;  // The arguments to pass to the method

    // An empty array; used for methods with no arguments at all.
    static final Object[] nullargs = new Object[] {};
    
    /** This constructor creates a Command object for a no-arg method */
    public Command(Object target, Method m) { this(target, m, nullargs); }

    /** 
     * This constructor creates a Command object for a method that takes the
     * specified array of arguments.  Note that the parse() method provides
     * another way to create a Command object
     **/
    public Command(Object target, Method m, Object[] args) { 
	this.target = target;
	this.m = m;
	if (args == null) args = nullargs;
	this.args = args;
    }

    /** 
     * This construct specifies the method to call by name.  It looks for a 
     * method of the target object with the specified name and specified number
     * of arguments.  It does not attempt to verify the types of the arguments,
     * since wrapper object (Integer, Boolean, etc) in the args[] array could
     * represent reference or primitive arguments.
     */
    public Command(Object target, String methodName, Object[] args) {
	this.target = target;
	if (args == null) args = nullargs;
	this.args = args;
	
	Method[] methods = target.getClass().getMethods();
	for(int i = 0; i < methods.length; i++) {
	    Method m = methods[i];
	    if (m.getParameterTypes().length == args.length && 
		m.getName().equals(methodName)) {
		this.m = m;
		break;
	    }
	}
	
	// If we didn't find a method, throw an exceptoin
	if (this.m == null)
	    throw new IllegalArgumentException("Unknown method " + methodName);

    }

    /**
     * Invoke the Command by calling the method on its target, and passing
     * the arguments.  See also actionPerformed() which does not throw the
     * checked exceptions that this method does.
     **/
    public void invoke()
	throws IllegalAccessException, InvocationTargetException
    {
	m.invoke(target, args);  // Use reflection to invoke the method
    }

    /**
     * This method implements the ActionListener interface.  It is like
     * invoke() except that it catches the exceptions thrown by that method
     * and rethrows them as an unchecked RuntimeException
     **/
    public void actionPerformed(ActionEvent e) {
	try {
	    invoke();                          // Call the invoke method
	}
	catch (InvocationTargetException ex) { // But convert to unchecked
	    throw new RuntimeException(ex);    // exceptions.  Note that we
	}                                      // chain to the original 
	catch (IllegalAccessException ex) {    // exception.
	    throw new RuntimeException(ex);
	}
    }

    /**
     * This method implements the InvocationHandler interface, so that a
     * Command object can be used with Proxy objects.  Note that it simply
     * calls the no-argument invoke() method, ignoring its arguments and 
     * returning null. This means that it is only useful for proxying
     * interfaces that define a single no-arg void method.
     **/
    public Object invoke(Object p, Method m, Object[] a) throws Throwable {
	invoke();
	return null;
    }

    /**
     * This static method creates a Command using the specified target object,
     * and the specified string.  The string should contain method name
     * followed by an optional parenthesized comma-separated argument list and
     * a semicolon.  The arguments may be boolean, integer or double literals,
     * or double-quoted strings.  The parser is lenient about missing commas,
     * semicolons and quotes, but throws an IOException if it cannot parse the
     * string.
     **/
    public static Command parse(Object target, String text) throws IOException
    {
	String methodname;                 // The name of the method
	ArrayList args = new ArrayList();  // Hold arguments as we parse them.
	ArrayList types = new ArrayList(); // Hold argument types.

	Tokenizer t = new CharSequenceTokenizer(text);
	t.skipSpaces(true).tokenizeWords(true).tokenizeNumbers(true);
	t.quotes("\"'", "\"'");

	if (t.next() != Tokenizer.WORD)
	    throw new IOException("Missing method name for command");
	methodname = t.tokenText();
	t.next();
	if (t.tokenType() == '(') {
	    t.next(); 
	    for(;;) { // Loop through all arguments 
		int c = t.tokenType();
		if (c == ')') {
		    // Consume closing paren
		    t.next();
		    break;  // and break out of list
		}
		
		if (c == Tokenizer.WORD) {
		    String word = t.tokenText();
		    if (word.equals("true")) {
			args.add(Boolean.TRUE);
			types.add(boolean.class);
		    }
		    else if (word.equals("false")) {
			args.add(Boolean.FALSE);
			types.add(boolean.class);
		    }
		    else {  // Treat unquoted identifiers as strings...
			args.add(word);
			types.add(String.class);
		    }
		}
		else if (c == '"') {  // double-quoted string
		    args.add(t.tokenText());
		    types.add(String.class);
		}
		else if (c == '\'') { // single-quoted character
		    args.add(new Character(t.tokenText().charAt(0)));
		    types.add(char.class);
		}
		else if (c == Tokenizer.NUMBER) {  // An integer
		    args.add(new Integer(Integer.parseInt(t.tokenText())));
		    types.add(int.class);
		}
		else {  // Anything else is a syntax error
		    throw new IOException("Unexpected token " + t.tokenText() +
					  " in argument list of " +
					  methodname + "().");
		}
		
		// Consume the token we just parse, and then consume an
		// optional comma.  
		if (t.next() == ',') t.next();
	    }
	}

	// Consume optional semicolon after method name or argument list
	if (t.tokenType() == ';') t.next();


	// We've parsed the argument list.
	// Next, convert the lists of argument values and types to arrays
	Object[] argValues = args.toArray();
	Class[] argtypes = (Class[])types.toArray(new Class[argValues.length]);

	// At this point, we've got a method name, and arrays of argument
	// values and types.  Use reflection on the class of the target object
	// to find a method with the given name and argument types.  Throw
	// an exception if we can't find the named method.
	Method method;
	try { method = target.getClass().getMethod(methodname, argtypes); }
	catch (Exception e) {
	    throw new IOException("No such method found, or wrong argument " +
				  "types: " + methodname);
	}

	// Finally, create and return a Command object, using the target object
	// passed to this method, the Method object we obtained above, and
	// the array of argument values we parsed from the string.
	return new Command(target, method, argValues);
    }


    /**
     * This simple program demonstrates how a Command object can be parsed from
     * a string and used as an ActionListener object in a Swing application.
     **/
    static class Test {
	public static void main(String[] args) throws IOException {
	    javax.swing.JFrame f = new javax.swing.JFrame("Command Test");
	    javax.swing.JButton b1 = new javax.swing.JButton("Tick");
	    javax.swing.JButton b2 = new javax.swing.JButton("Tock");
	    javax.swing.JLabel label = new javax.swing.JLabel("Hello world");
	    java.awt.Container pane = f.getContentPane();

	    pane.add(b1, java.awt.BorderLayout.WEST);
	    pane.add(b2, java.awt.BorderLayout.EAST);
	    pane.add(label, java.awt.BorderLayout.NORTH);

	    b1.addActionListener(Command.parse(label, "setText(\"tick\");"));
	    b2.addActionListener(Command.parse(label, "setText(\"tock\");"));
	    
	    f.pack();
	    f.show();
	}
    }
}
