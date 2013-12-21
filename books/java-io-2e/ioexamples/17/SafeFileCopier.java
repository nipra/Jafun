import java.io.*;

public class SafeFileCopier {

  public static void main(String[] args) throws IOException {
    if (args.length != 2) {
      System.err.println("Usage: java FileCopier infile outfile");
    }
    else copy(new File(args[0]), new File(args[1]));
  }

  public static void copy(File inFile, File outFile) throws IOException {

    if (inFile.getCanonicalPath().equals(outFile.getCanonicalPath())) {
      // inFile and outFile are the same;
      // hence no copying is required.
      return;
    }

    InputStream in = null; 
    OutputStream out = null;
    
    try {
      in = new BufferedInputStream(new FileInputStream(inFile)); 
      out = new BufferedOutputStream(new FileOutputStream(outFile));
      for (int c = in.read(); c != -1; c = in.read()) {
        out.write(c);
      }
    }
    finally {
      if (in != null) in.close();
      if (out != null) out.close();
    }
  }
}
