import java.io.Serializable;

//public class Student extends Person {                        // (1a)
public class Student extends Person implements Serializable {  // (1b)

  private long studNum;

  Student(String name, long studNum) {
    super(name);
    this.studNum = studNum;
  }

  public String toString() {
    return "Student state(" + getName() + ", " + studNum + ")";
  }
}