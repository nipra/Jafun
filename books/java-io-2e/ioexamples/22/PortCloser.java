import javax.comm.*;

public class PortCloser extends Thread {

  CommPort thePort; 
  Thread join1;
  Thread join2;

  public PortCloser(CommPort port, Thread t1, Thread t2) {
    this.thePort = port;
    join1 = t1;
    join2 = t2;
  }

  public void run() {
  
    try {
      join1.join();
      join2.join();
      // execution stops until both these thread finish
    }
    catch (InterruptedException e) {
    }
   
  
    try {
      thePort.close();  
    }
    catch (Exception e) {
      // the port's probably already been closed
    }
    
  }

}