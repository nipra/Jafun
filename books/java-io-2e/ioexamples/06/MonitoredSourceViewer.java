import java.net.*;
import java.io.*;
import javax.swing.*;

public class MonitoredSourceViewer {

  public static void main (String[] args) {

    if  (args.length > 0) { 
        
      try {    
        // Open the URLConnection for reading
        URL u = new URL(args[0]);
        URLConnection uc = u.openConnection();
        InputStream in = uc.getInputStream();

        // Chain a ProgressMonitorInputStream to the 
        // URLConnection's InputStream
        ProgressMonitorInputStream pin 
         = new ProgressMonitorInputStream(null, u.toString(), in);
         
        // Set the maximum value of the ProgressMonitor
        ProgressMonitor pm = pin.getProgressMonitor(); 
        pm.setMaximum(uc.getContentLength());
        
        // Read the data
        for (int c = pin.read(); c != -1; c = pin.read()) {
          System.out.print((char) c);
        } 
        pin.close();
        
      }
      catch (MalformedURLException ex) {
        System.err.println(args[0] + " is not a parseable URL");
      }
      catch (InterruptedIOException ex) {
        // User cancelled. Do nothing.
      } 
      catch (IOException ex) {
        System.err.println(ex);
      }

    } //  end if
  
    // Since we brought up a GUI, we have to explicitly exit here
    // rather than simply returning from the main() method.
    System.exit(0);

  } // end main

}  // end MonitoredSourceViewer
