// A superclass
// public class Person implements Serializable {               // (1a)
public class Person  {                                         // (1b)
  private String name;

  Person() {}                                                  // (2)
  Person(String name) { this.name = name; }

  public String getName() { return name; }
}