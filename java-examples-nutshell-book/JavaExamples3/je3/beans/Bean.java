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
package je3.beans;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.beans.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.List;  // explicit import to disambiguate from java.awt.List
import java.io.*;

/**
 * This class encapsulates a bean object and its BeanInfo.
 * It is a key part of the ShowBean "beanbox" program, and demonstrates
 * how to instantiate and instrospect beans, and how to use reflection to 
 * set properties and invoke methods.  It also illustrates how to work with
 * PropertyEditor classes.
 */
public class Bean {
    Object bean;     // The bean object we encapsulate
    BeanInfo info;   // Information about beans of that type
    Map properties;  // Map property names to PropertyDescriptor objects
    Map commands;    // Map command names to MethodDescriptor objects
    boolean expert;  // Whether to include "expert" properties and commands

    // Utility object used when invoking no-arg methods 
    static final Object[] NOARGS = new Object[0];

    // This constructor introspects the specified component.
    // Typically you'll use one of the static factory methods instead.
    public Bean(Object bean, boolean expert) throws IntrospectionException {
	this.bean = bean;     // The object to instrospect
	this.expert = expert; // Is the end-user an expert?

	// Introspect to get BeanInfo for the bean
	info = Introspector.getBeanInfo(bean.getClass());

	// Now Create a map of property names to PropertyDescriptor objects
	properties = new HashMap();
	PropertyDescriptor[] props = info.getPropertyDescriptors();
	for(int i = 0; i < props.length; i++) {
	    // Skip hidden properties, indexed properties, and expert 
	    // properties unless the end-user is an expert.
	    if (props[i].isHidden()) continue;
	    if (props[i] instanceof IndexedPropertyDescriptor) continue;
	    if (!expert && props[i].isExpert()) continue;
	    properties.put(props[i].getDisplayName(), props[i]);
	}

	// Create a map of command names to MethodDescriptor objects
	// Commands are methods with no arguments and no return value.
	// We skip commands defined in Object, Component, Container, and
	// JComponent because they contain methods that meet this definition
	// but are not intended for end-users.
	commands = new HashMap();
	MethodDescriptor[] methods = info.getMethodDescriptors();
	for(int i = 0; i < methods.length; i++) {
	    // Skip it if it is hidden or expert (unless user is expert)
	    if (methods[i].isHidden()) continue;
	    if (!expert && methods[i].isExpert()) continue;
	    Method m = methods[i].getMethod();
	    // Skip it if it has arguments or a return value
	    if (m.getParameterTypes().length > 0) continue;
	    if (m.getReturnType() != Void.TYPE)	continue;
	    // Check the declaring class and skip useless superclasses
	    Class c = m.getDeclaringClass();
	    if (c==JComponent.class || c==Component.class ||
		c==Container.class || c==Object.class)	continue;
	    // Get the unqualifed classname to prefix method name with
	    String classname = c.getName();
	    classname = classname.substring(classname.lastIndexOf('.')+1);
	    // Otherwise, this is a valid command, so add it to the list
	    commands.put(classname + "." + m.getName(),	 methods[i]);
	}
    }

    // Factory method to instantiate a bean from a named class
    public static Bean forClassName(String className, boolean expert)
	throws ClassNotFoundException, InstantiationException,
	       IllegalAccessException, IntrospectionException
    {
	// Load the named bean class
	Class c = Class.forName(className);
	// Instantiate it to create the component instance
	Object bean = c.newInstance();

	return new Bean(bean, expert);
    }

    // Factory method to read a serialized bean
    public static Bean fromSerializedStream(ObjectInputStream in,
					    boolean expert)
	throws IOException, ClassNotFoundException, IntrospectionException
    {
	return new Bean(in.readObject(), expert);
    }

    // Factory method to read a persistent XMLEncoded bean from a stream.
    public static Bean fromPersistentStream(InputStream in, boolean expert)
	throws IntrospectionException
    {
	return new Bean(new XMLDecoder(in).readObject(), expert);
    }

    // Return the bean object itself.
    public Object getBean() { return bean; }

    // Return the name of the bean
    public String getDisplayName() {
	return info.getBeanDescriptor().getDisplayName();
    }

    // Return an icon for the bean
    public Image getIcon() {
	Image icon = info.getIcon(BeanInfo.ICON_COLOR_32x32);
	if (icon != null) return icon;
	else return info.getIcon(BeanInfo.ICON_COLOR_16x16);
    }

    // Return a short description for the bean
    public String getShortDescription() {
	return info.getBeanDescriptor().getShortDescription();
    }

    // Return an alphabetized list of property names for the bean
    // Note the elegant use of the Collections Framework
    public List getPropertyNames() {
	// Make a List from a Set (from a Map), and sort it before returning.
	List names = new ArrayList(properties.keySet()); 
	Collections.sort(names);
	return names;
    }

    // Return an alphabetized list of command names for the bean.
    public List getCommandNames() {
	List names = new ArrayList(commands.keySet());
	Collections.sort(names);
	return names;
    }

    // Get a description of a property; useful for tooltips
    public String getPropertyDescription(String name) {
	PropertyDescriptor p = (PropertyDescriptor) properties.get(name);
	if (p == null) throw new IllegalArgumentException(name);
	return p.getShortDescription();
    }

    // Get a description of a command; useful for tooltips
    public String getCommandDescription(String name) {
	MethodDescriptor m = (MethodDescriptor) commands.get(name);
	if (m == null) throw new IllegalArgumentException(name);
	return m.getShortDescription();
    }

    // Return true if the named property is read-only
    public boolean isReadOnly(String name) {
	PropertyDescriptor p = (PropertyDescriptor) properties.get(name);
	if (p == null) throw new IllegalArgumentException(name);
	return p.getWriteMethod() == null;
    }

    // Invoke the named (no-arg) method of the bean
    public void invokeCommand(String name)
	throws IllegalAccessException, InvocationTargetException
    {
	MethodDescriptor method = (MethodDescriptor) commands.get(name);
	if (method == null) throw new IllegalArgumentException(name);
	Method m = method.getMethod();
	m.invoke(bean, NOARGS);
    }

    // Return the value of the named property as a string
    // This method relies on to toString() method of the returned value.
    // A more robust implementation might use a PropertyEditor.
    public String getPropertyValue(String name)
	throws IllegalAccessException, InvocationTargetException
    {
	PropertyDescriptor p = (PropertyDescriptor) properties.get(name);
	if (p == null) throw new IllegalArgumentException(name);
	Method m = p.getReadMethod();           // property accessor method
	Object value = m.invoke(bean, NOARGS);  // invoke it to get value
	if (value == null) return "null";
	return value.toString();                // use the toString method()
    }

    // Set the named property to the named value, if possible.
    // This method knows how to convert a handful of well-known types.  It
    // attempts to use a PropertyEditor for types it does not know about but
    // this only works for editors that have working setAsText() methods.
    public void setPropertyValue(String name, String value)
	throws IllegalAccessException, InvocationTargetException
    {
	// Get the descriptor for the named property
	PropertyDescriptor p = (PropertyDescriptor) properties.get(name);
	if (p == null || isReadOnly(name))  // Make sure we can set it
	    throw new IllegalArgumentException(name);

	Object v;  // Store the converted string value here.
	Class type = p.getPropertyType();
	
	// Convert common types in well-known ways
	if (type == String.class) v = value;   
	else if (type == boolean.class) v = Boolean.valueOf(value);
	else if (type == byte.class) v = Byte.valueOf(value);
	else if (type == char.class) v = new Character(value.charAt(0));
	else if (type == short.class) v = Short.valueOf(value);
	else if (type == int.class) v = Integer.valueOf(value);
	else if (type == long.class) v = Long.valueOf(value);
	else if (type == float.class) v = Float.valueOf(value);
	else if (type == double.class) v = Double.valueOf(value);
	else if (type == Color.class) v = Color.decode(value);
	else if (type == Font.class) v = Font.decode(value);
	else {
	    // Try to find a property editor for unknown types
	    PropertyEditor editor = PropertyEditorManager.findEditor(type);
	    if (editor != null) {
		editor.setAsText(value);
		v = editor.getValue();
	    }
	    // Otherwise, give up.
	    else throw new UnsupportedOperationException("Can't set " +
							 "properties of type "+
							 type.getName());
	}

	// Now get the Method object for the property setter method and
	// invoke it on the bean object, passing the converted value.
	Method setter = p.getWriteMethod();  
	setter.invoke(bean, new Object[] { v });
    }

    // Return a component that allows the user to edit the property value
    // the component is live and changes the property value in real time;
    // there is no need to call setPropertyValue().
    public Component getPropertyEditor(final String name)
	throws IllegalAccessException, InvocationTargetException,
	       InstantiationException
    {
	// Get the descriptor for the named property; final for inner classes.
	final PropertyDescriptor p = (PropertyDescriptor) properties.get(name);
	if (p == null || isReadOnly(name))  // Make sure we can edit it.
	    throw new IllegalArgumentException(name);

	// Find a PropertyEditor for the property
	final PropertyEditor editor;  // final for inner class use
	if (p.getPropertyEditorClass() != null) {  
	    // If there is a custom editor for this property instantiate one.
	    editor = (PropertyEditor)p.getPropertyEditorClass().newInstance();
	}
	else {
	    // Otherwise, look up an editor based on the property type
	    Class type = p.getPropertyType();
	    editor = PropertyEditorManager.findEditor(type);
	    // If there is no editor, give up
	    if (editor == null) 
		throw new UnsupportedOperationException("Can't set " +
							"properties of type " +
							type.getName());
	}

	// Get the property accessor methods for this property so we can
	// query the initial value and set the edited value
	final Method getter = p.getReadMethod();
	final Method setter = p.getWriteMethod();

	// Use Java reflection to find the current propery value. Then tell
	// the property editor about it.
	Object currentValue = getter.invoke(bean, NOARGS);
	editor.setValue(currentValue);

	// If the PropertyEditor has a custom editor, then we'll just return
	// that custom editor component from this method. User changes to the
	// component change the value in the PropertyEditor which generates
	// a PropertyChangeEvent. We register a listener so that these changes
	// set the property on the bean as well.
	if (editor.supportsCustomEditor()) {
	    final Component editComponent = editor.getCustomEditor();
	    // Note that we register the listener on the PropertyEditor, not
	    // on its custom editor Component.
	    editor.addPropertyChangeListener(new PropertyChangeListener() {
		    public void propertyChange(PropertyChangeEvent e) {
			try {
			    // Pass edited value to property setter
			    Object editedValue = editor.getValue();
			    setter.invoke(bean, new Object[] { editedValue});
			}
			catch(Exception ex) {
			    JOptionPane.showMessageDialog(editComponent,
						  ex, ex.getClass().getName(),
						  JOptionPane.ERROR_MESSAGE);
			}
		    }
		});
	    return editComponent;
	}

	// Otherwise, if the PropertyEditor is for an enumerated type based
	// on a fixed list of possible values, then return a JComboBox
	// component that allows the user to select one of the values.
	final String[] tags = editor.getTags();
	if (tags != null) {
	    // Create the component
	    final JComboBox combobox = new JComboBox(tags);
	    // Use the current value of the property as the currently selected
	    // item in the combo box.
	    combobox.setSelectedItem(editor.getAsText());
	    // Add a listener to hook the combo box up to the property. When
	    // the user selects an item, set the property value.
	    combobox.addItemListener(new ItemListener() {
		    public void itemStateChanged(ItemEvent e) {
			// Ignore deselect events
			if (e.getStateChange() == ItemEvent.DESELECTED) return;
			try {
			    // Get the user's selected string from combo box
			    String selectedTag =
				(String)combobox.getSelectedItem();
			    // Tell the editor about this string value
			    editor.setAsText(selectedTag);
			    // Ask the editor to convert to the property type
			    Object editedValue = editor.getValue();
			    // Pass this value to the property setter method
			    setter.invoke(bean, new Object[] { editedValue });
			}
			catch(Exception ex) {
			    JOptionPane.showMessageDialog(combobox,
						  ex, ex.getClass().getName(),
						  JOptionPane.ERROR_MESSAGE);
			}
		    }
		});
	    return combobox;
	}

	// Otherwise, property type is not enumerated, and we use a JTextField
	// to allow the user to enter arbitrary text for conversion by the
	// setAsText() method of the PropertyEditor
	final JTextField textfield = new JTextField();
	// Display the current value of the property in the field
	textfield.setText(editor.getAsText());
	// Hook the JTextField up to the PropertyEditor.
	textfield.addActionListener(new ActionListener() {
		// This is called when the user strikes the Enter key
		public void actionPerformed(ActionEvent e) {
		    try {
			// Get the user's input from the text field
			String newText = textfield.getText();
			// Tell the editor about it
			editor.setAsText(newText);
			// Ask the editor to convert to the property type
			Object editedValue = editor.getValue();
			// Pass this value to the property setter method
			setter.invoke(bean, new Object[] { editedValue });
		    }
		    catch(Exception ex) {
			JOptionPane.showMessageDialog(textfield,
					      ex, ex.getClass().getName(),
					      JOptionPane.ERROR_MESSAGE);
		    }
		}
	    });
	return textfield;
    }
}
