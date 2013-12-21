import java.io.*;
import java.util.zip.*;

public class DirectInflater {

  public static void main(String[] args) {
  
    Inflater inf = new Inflater();
    byte[] input = new byte[1024];
    byte[] output = new byte[1024];

    for (int i = 0; i < args.length; i++) {
    
      try {
        if (!args[i].endsWith(DirectDeflater.DEFLATE_SUFFIX)) {
          System.err.println(args[i] + " does not look like a deflated file");
          continue;
        }
        FileInputStream fin = new FileInputStream(args[i]);   
        FileOutputStream fout = new FileOutputStream(args[i].substring(0, 
         args[i].length() - DirectDeflater.DEFLATE_SUFFIX.length()));
        
        while (true) { // Read and inflate the data.      

          // Fill the input array.
          int numRead = fin.read(input);
          if (numRead != -1) { // End of stream, finish inflating.
            inf.setInput(input, 0, numRead);
          } // end if
          // Inflate the input.
            
          int numDecompressed = 0;
          while ((numDecompressed = inf.inflate(output, 0, output.length)) 
           != 0) {
            fout.write(output, 0, numDecompressed);
          }
          // At this point inflate() has returned 0.
          // Let's find out why.
          if (inf.finished()) { // all done
            break;
          }
          else if (inf.needsDictionary()) { // We don't handle dictionaries.
            System.err.println("Dictionary required! bailing...");
            break;
          }
          else if (inf.needsInput()) {
            continue;
          }
        } // end while
        
        // Close up and get ready for the next file.
        fin.close();
        fout.flush();
        fout.close();
        inf.reset();
      } // end try
      catch (IOException ex) {System.err.println(ex);}
      catch (DataFormatException ex) {
        System.err.println(args[i] + " appears to be corrupt");     
        System.err.println(ex);     
      } // end catch
    }
  }
}