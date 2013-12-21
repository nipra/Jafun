import java.io.*;

class LineCat {

  public static void main (String[] args) {
    String thisLine;
    for (int i=0; i < args.length; i++) {
     try {
       LineNumberReader br = new LineNumberReader(new FileReader(args[i]));
       while ((thisLine = br.readLine()) != null) {
         System.out.println(br.getLineNumber() + ": " + thisLine);
       } // end while 
     } // end try
     catch (IOException ex) {System.err.println(ex);}
   } // end for
  } // end main
}
