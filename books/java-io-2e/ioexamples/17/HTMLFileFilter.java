import java.io.*;

public class HTMLFileFilter implements FileFilter {

 public boolean accept(File pathname) {
 
   if (pathname.getName().endsWith(".html")) return true;
   if (pathname.getName().endsWith(".htm")) return true;
   return false;
 }
}
