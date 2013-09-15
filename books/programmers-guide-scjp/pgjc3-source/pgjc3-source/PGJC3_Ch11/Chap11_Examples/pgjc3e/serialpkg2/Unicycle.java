package pgjc3e.serialpkg2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Unicycle implements Serializable {                    // (2)
  transient private Wheel wheel;                                   // (3a)

  Unicycle(Wheel wheel) { this.wheel = wheel; }

  public String toString() { return "Unicycle with " + wheel; }

  private void writeObject(ObjectOutputStream oos) {               // (3b)
    try {
      oos.defaultWriteObject();
      oos.writeInt(wheel.getWheelSize());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void readObject(ObjectInputStream ois) {                 // (3c)
    try {
      ois.defaultReadObject();
      int wheelSize = ois.readInt();
      wheel = new Wheel(wheelSize);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }
}