import java.util.*;
import java.io.*;

public class Die implements Serializable {

  private int face = 1;
  Random shooter = new Random();

  public Die(int face) {
    if (face < 1 || face > 6) throw new IllegalArgumentException();
    this.face = face;
  }

  public final int getFace() {
    return this.face;
  }

  public void setFace(int face) {
    if (face < 1 || face > 6) throw new IllegalArgumentException();
    this.face = face;
  }

  public int roll() {
    this.face = (Math.abs(shooter.nextInt()) % 6) + 1;
    return this.face;
  }
}
