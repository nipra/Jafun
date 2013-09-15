import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.util.Locale;

public final class Stdin {
  // A BufferedReader chained to an InputStreamReader chained to an InputStream.
  private static BufferedReader reader = new BufferedReader(       // (1)
      new InputStreamReader(System.in)
  );
  
  // Read one line of text from the terminal and return it as a string.
  public static String readLine() {                                // (2)
    while (true) try {
      return reader.readLine();                                    // (3)
    } catch(IOException ioe) {
      reportError(ioe);
    }
  }
  
  // Read one integer value from the terminal.
  public static int readInteger() {                                // (4)
    while (true) try {
      return Integer.parseInt(reader.readLine());                  // (5)
    } catch (IOException ioe) {
      reportError(ioe);
    } catch(NumberFormatException nfe) {
      reportError(nfe);
    }
  }
  
  // Read one double value from the terminal.
  public static double readDouble() {                              // (6)
    while (true) try {
      return Double.parseDouble(reader.readLine());                // (7)
    } catch(IOException ioe) { 
      reportError(ioe);
    } catch(NumberFormatException nfe) {
      reportError(nfe);
    }
  }
  
  private static void reportError(Exception e) {
    System.err.println("Error in input: " + e);
    System.err.println("Please re-enter data.");
  }
  
  public static void main(String[] args) {
    System.out.println("Input a string:");
    String str = Stdin.readLine();
    System.out.println("Input an integer:");
    int i = Stdin.readInteger();
    System.out.println("Input a double:");
    double d = Stdin.readDouble();
    NumberFormat formatter = NumberFormat.getInstance(Locale.US);  // (8)
    System.out.println("Data read:");
    System.out.println(str);
    System.out.println(formatter.format(i));
    System.out.println(formatter.format(d));
  }
}