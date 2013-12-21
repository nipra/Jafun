import yap.Machine;                           // (0)

import yap.Machine.StateConstant;             // (1)
import static yap.Machine.StateConstant;      // (2) Superfluous because of (1)
import static yap.Machine.StateConstant.*;    // (3)

import yap.Machine.MachineState;              // (4)
import static yap.Machine.MachineState;       // (5) Superfluous because of (4)
import static yap.Machine.MachineState.*;     // (6)

import yap.Machine.AuxMachineState;           // (7)
import static yap.Machine.AuxMachineState;    // (8) Superfluous because of (7)
import static yap.Machine.AuxMachineState.*;  // (9)
import static yap.Machine.AuxMachineState.WRITE_OFF;  // (10)

public class MachineClient {
  public static void main(String[] args) {    // (10)

    StateConstant msc = new StateConstant();  // Requires (1) or (2)
  //String s1 = IDLE;                         // Ambiguous because of (3) and (6)
    String s2 = StateConstant.IDLE;           // Explicit disambiguation necessary.

  //MachineState ms1 = BLOCKED;               // Ambiguous because of (3) and (6)
    MachineState ms2 = MachineState.BLOCKED;  // Requires (4) or (5)
    MachineState ms3 = MachineState.IDLE;     // Explicit disambiguation necessary.

    AuxMachineState[] states = {              // Requires (7) or (8)
        AVAILABLE, HIRED, UNDER_REPAIR,       // Requires (9)
        WRITE_OFF,                            // Requires (9) or (10)
        AuxMachineState.WRITE_OFF,            // Requires (7) or (8)
        Machine.AuxMachineState.WRITE_OFF,    // Requires (0)
        yap.Machine.AuxMachineState.WRITE_OFF // Does not require any import
    };

    for (AuxMachineState s : states)
      System.out.print(s + " ");
  }
}