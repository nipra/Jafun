import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Formatter;
import java.util.Locale;

/* Using the format() method */
class UsingFormatMethod {

  public static void main(String[] args) throws FileNotFoundException {
    // String.format() returns a string with the formatted text.            (1)
    String output = String.format("1:Formatted output|%6d|%8.2f|%-10s|%n",
                                   2008, 12345.678, "Hello");
    System.out.print(output);

    // PrintWriter.format() writes to the specified file,                   (2)
    // using the specified locale.
    PrintWriter pw = new PrintWriter("output2.txt");                    
    pw.format(new Locale("no", "NO"),
              "2:Formatted output|%6d|%8.2f|%-10s|%n",
               2008, 12345.678, "Hello");
    pw.flush();
    pw.close();

    // PrintStream.format() writes to the standard output stream.           (3)
    System.out.format("3:Formatted output|%6d|%8.2f|%-10s|%n",
                       2008, 12345.678, "Hello");

    // Formatter.format() writes to the string builder,                     (4)
    // using specified locale.
    StringBuilder stb = new StringBuilder();
    Formatter fmt = new Formatter(stb, new Locale("no", "NO"));
    fmt.format("4:Formatted output|%6d|%8.2f|%-10s|%n",
                2008, 12345.678, "Hello");
    System.out.print(stb);
    fmt.flush();
    fmt.close();
    
    // Formatter.format() writes to the specified file,                     (5)
    // using the specified locale.
    Formatter fmt2 = new Formatter(new FileOutputStream("output5.txt"));
    fmt2.format(new Locale("no", "NO"),
                "5:Formatted output|%6d|%8.2f|%-10s|%n",
                 2008, 12345.678, "Hello");
    fmt2.flush();
    fmt2.close();
  }
}