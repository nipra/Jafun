import java.util.Properties;

public class SysProp {
  public static void main(String[] args) {
    Properties props = System.getProperties();      // (1)
    props.setProperty("appName", "BigKahuna");      // (2)
    for (String prop : args) {
      String value = props.getProperty(prop);       // (3)
      System.out.printf("%s=%s%n", prop, value);
    }
  }
}