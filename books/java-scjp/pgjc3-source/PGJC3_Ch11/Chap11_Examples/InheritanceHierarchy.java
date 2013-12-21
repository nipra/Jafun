import java.io.Console;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Prints the superclasses of a given class.
 */
public final class InheritanceHierarchy {

  public static final void main(String[] args){

    // Get the console:
    Console console = System.console();
    if (console == null) {
      System.err.println("No console available.");
      System.exit(1);
    }

    // Read the user name:
    String username = console.readLine("Please enter your user Name: ");

    // Read the password:
    char[] password = console.readPassword("Please enter your password, %s: ",
                                            username);

    // Verification of password is omitted here.

    // Zero-out the password:
    Arrays.fill(password, '0');

    // Read the class name and print the inheritance hierarchy:
    String className = console.readLine(
           "Please enter a fully-qualified class name, %s: ", username);
    try {
      Class thisClass = Class.forName(className);
      console.printf("Inheritance hierarchy:%n%s%n", getSuperclasses(thisClass));
      console.printf("Thank you for using this service, %s.%n", username);
    } catch(ClassNotFoundException e){
      console.printf("Class not found: %s%nPlease try again.", className);
    }
  }

  /**
   * Create a list with the names of superclasses for the specified Class object.
   * @param classObj
   * @return List of superclass names
   */
  private static List<String> getSuperclasses(Class classObj){
    List<String> superclasses = new ArrayList<String>();
    Class currentClass = classObj;
    do {
      superclasses.add(currentClass.getName());
      currentClass = currentClass.getSuperclass();
    } while (currentClass != null);
    Collections.reverse(superclasses);
    return superclasses;
  }
}