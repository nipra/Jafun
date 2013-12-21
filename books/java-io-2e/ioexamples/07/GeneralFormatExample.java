import java.net.*;

public class GeneralFormatExample {

  public static void main(String[] args) throws MalformedURLException {
    URL u = new URL("http://www.example.com/Article.html");
    System.out.printf("boolean:   %b\n", u);
    System.out.printf("BOOLEAN:   %B\n", u);
    System.out.printf("hashcode:  %h\n", u);
    System.out.printf("HASHCODE:  %H\n", u);
    System.out.printf("string:    %s\n", u);
    System.out.printf("STRING:    %S\n", u);
    }
}
