import java.io.*; 

public class HTMLFiles {

  public static void main(String[] args) {
    
    File cwd = new File(System.getProperty("user.dir"));
    File[] htmlFiles = cwd.listFiles(new HTMLFileFilter());
    for (int i = 0; i < htmlFiles.length; i++) {
      System.out.println(htmlFiles[i]);
    }
  }
}
