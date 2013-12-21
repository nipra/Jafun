package yap;                                    // yet another package

public class Machine {                          // Class with 3 nested types

  public static class StateConstant {           // A static member class
    public static final String BUSY = "Busy";
    public static final String IDLE = "Idle";
    public static final String BLOCKED = "Blocked";
  }

  public enum MachineState {                    // A nested enum is static.
    BUSY, IDLE, BLOCKED
  }

  public enum AuxMachineState {                 // Another static enum
    UNDER_REPAIR, WRITE_OFF, HIRED, AVAILABLE;
  }
}