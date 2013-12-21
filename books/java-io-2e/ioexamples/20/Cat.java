import java.io.*;

class Cat {

  public static void main (String[] args) {
    String thisLine;
    for (int i=0; i < args.length; i++) {
     try {
       BufferedReader br = new BufferedReader(new FileReader(args[i]));
       while ((thisLine = br.readLine()) != null) {
         System.out.println(thisLine);
       } // end while 
     } // end try
     catch (IOException ex) {System.err.println(ex);}
   } // end for
  } // end main
}
