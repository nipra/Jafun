package energy;
/** A PowerPlant with a reactor core.
    The solution presented here is provided by Jennie Yip. */
public class PowerPlant {
  /** Each power plant has a reactor core. This has package
      accessibility so that the Control class that is defined in
      the same package can access it. */
  Reactor core;

  /** Initializes the power plant, creates a reactor core. */
  PowerPlant() {
    core = new Reactor();
  }

  /** Sound the alarm to evacuate the power plant. */
  public void soundEvacuateAlarm() {
    // ... implementation unspecified ...
  }

  /** Get the level of reactor output that is most desirable at this time.
      (Units are unspecified.) */
  public int getOptimalThroughput() {
    // ... implementation unspecified ...
    return 0;
  }

  /** The main entry point of the program: sets up a PowerPlant
      object and a Control object and lets the Control object run the
      power plant. */
  public static void main(String[] args) {
    PowerPlant plant = new PowerPlant();
    Control ctrl = new Control(plant);
    ctrl.runSystem();
  }
}

/** A reactor core that has a throughput that can be either decreased or
    increased. */
class Reactor {
  /** Get the current throughput of the reactor. (Units are unspecified.) */
  public int getThroughput() {
    // ... implementation unspecified ...
    return 0;
  }

  /** @returns true if the reactor status is critical, false otherwise. */
  public boolean isCritical() {
    // ... implementation unspecified ...
    return false;
  }

  /** Ask the reactor to increase throughput. */
  void increaseThroughput() throws ReactorCritical {
    // ... implementation unspecified ...
  }

  /** Ask the reactor to decrease throughput. */
  void decreaseThroughput() {
    // ... implementation unspecified ...
  }
}

/** This exception class should be used to report that the reactor status is
    critical. */
class ReactorCritical extends Exception {}

/** A controller that will manage the power plant to make sure that the
    reactor runs with optimal throughput.  */
class Control {
  PowerPlant thePlant;

  final static int TOLERANCE = 10;

  public Control(PowerPlant p) {
    thePlant = p;
  }

  /** Run the power plant by continuously monitoring the
      optimalThroughput and the actual throughput of the reactor. If
      the throughputs differ by more than 10 units, i.e. tolerance,
      adjust the reactor throughput.
      If the reactor goes critical, the evacuate alarm is
      sounded and the reactor is shut down.
      <p>The runSystem() method does handle the reactor core directly
      but calls methods needAdjustment(), adjustThroughput(), and shutdown
      instead.  */
  public void runSystem() {
    try {
      while (true) { // infinite loop
        int optimalThroughput = thePlant.getOptimalThroughput();
        if (needAdjustment(optimalThroughput))
          adjustThroughput(optimalThroughput);
      }
    } catch (ReactorCritical rc) {
      thePlant.soundEvacuateAlarm();
    } finally {
      shutdown();
    }
  }

  /** Reports whether the throughput of the reactor needs
      adjusting. This method should also monitor and report if the
      reactor goes critical.
      @returns true if the optimal and actual throughput values
      differ by more than 10 units.  */
  public boolean needAdjustment(int target) throws ReactorCritical {
    /* We added the throws clause to the method declaration so that
       the method can throw a ReactorCritical exception if the reactor
       goes critical.  */
    if (thePlant.core.isCritical())
      throw new ReactorCritical();
    return Math.abs(thePlant.core.getThroughput() - target) > TOLERANCE;
  }

  /** Adjust the throughput of the reactor by calling increaseThroughput()
      and decreaseThroughput() until the actual throughput is within 10
      units of the target throughput.  */
  public void adjustThroughput(int target) throws ReactorCritical {
    /* We added the throws clause to the method declaration so that
       the method can pass on ReactorCritical exceptions thrown by
       increaseThroughput(). We do this because the adjustThroughput
       does not want to handle the exception.  */
    while (needAdjustment(target)) {
      if ((thePlant.core.getThroughput() - target) > TOLERANCE)
        thePlant.core.increaseThroughput();
      else
        thePlant.core.decreaseThroughput();
    }
  }

  /** Shut down the reactor by lowering the throughput to 0. */
  public void shutdown() {
    while (thePlant.core.getThroughput() > 0) {
      thePlant.core.decreaseThroughput();
    }
  }
}