import java.io.*;
import java.util.*;

public class FileSpy {

  public static void main(String[] args) {
  
    for (int i = 0; i < args.length; i++) {
      File f = new File(args[i]);
      if (f.exists()) {
        System.out.println("Name: " + f.getName());
        System.out.println("Absolute path: " + f.getAbsolutePath());
        try {
          System.out.println("Canonical path: " + f.getCanonicalPath());
        }
        catch (IOException ex) {
          System.out.println("Could not determine the canonical path.");        
        }
        
        String parent = f.getParent();
        if (parent != null) {
          System.out.println("Parent: " + f.getParent());
        }
        
        if (f.canWrite()) System.out.println(f.getName() + " is writable.");
        if (f.canRead()) System.out.println(f.getName() + " is readable.");
        
        if (f.isFile()) {
          System.out.println(f.getName() + " is a file.");
        }
        else if (f.isDirectory()) {
          System.out.println(f.getName() + " is a directory.");
        }
        else {
          System.out.println("What is this?");
        }
        
        if (f.isAbsolute()) {
          System.out.println(f.getPath() + " is an absolute path.");
        }
        else {
          System.out.println(f.getPath() + " is not an absolute path.");
        }
        
        long lm = f.lastModified();
        if (lm != 0) System.out.println("Last Modified at " + new Date(lm));
        
        long length = f.length();
        if (length != 0) {
          System.out.println(f.getName() + " is " + length + " bytes long.");  
        }
      }  
      else {
        System.out.println("I'm sorry. I can't find the file " + args[i]);
      }      
    }
  }
}
