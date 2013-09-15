import mypkg.State;                  // (1) Single type import

import static mypkg.State.*;         // (2) Static import on demand
import static java.lang.System.out;  // (3) Single static import

public class Factory {
  public static void main(String[] args) {
    State[] states = {
        IDLE, BUSY, IDLE, BLOCKED    // (4) Using static import implied by (2).
    };
    for (State s : states)           // (5) Using type import implied by (1).
      out.print(s + " ");            // (6) Using static import implied by (3).
  }
}