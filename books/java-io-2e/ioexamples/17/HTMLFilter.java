import java.io.*;

public class HTMLFilter implements FilenameFilter {

 public boolean accept(File directory, String name) {
 
   if (name.endsWith(".html")) return true;
   if (name.endsWith(".htm")) return true;
   return false;
 }
}
