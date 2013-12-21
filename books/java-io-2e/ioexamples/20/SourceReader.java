package com.elharo.io;

import java.io.*;

public class SourceReader extends FilterReader {

  public SourceReader(Reader in) {
    super(in);
  }

  private int buffer = -1;

  public int read() throws IOException {
  
    if (this.buffer != -1) {
      int c = this.buffer;
      this.buffer = -1; 
      return c;
    }
    
    int c = in.read();
    if (c != '\\') return c;
   
   int next = in.read();
   if (next != 'u' ) { // This is not a Unicode escape
     this.buffer = next;
     return c;
   }
   
   // Read next 4 hex digits
   // If the next four chars do not make a valid hex digit
   // this is not a valid .java file.
   StringBuffer sb = new StringBuffer();
   sb.append((char) in.read());
   sb.append((char) in.read());
   sb.append((char) in.read());
   sb.append((char) in.read());
   String hex = sb.toString();  
   try {
     return Integer.valueOf(hex, 16).intValue();
   }
   catch (NumberFormatException ex) {
     throw new IOException("Bad Unicode escape: \\u" + hex);  
   }
  }
 
  
  private boolean endOfStream = false;
  
  public int read(char[] text, int offset, int length) throws IOException {
  
    if (endOfStream) return -1;
    int numRead = 0;
    
    for (int i = offset; i < offset+length; i++) {
      int temp = this.read();
      if (temp == -1) {
        this.endOfStream = true;
        break;
      }
      text[i] = (char) temp;
      numRead++;
    }
    return numRead;
  
  }

  public long skip(long n) throws IOException {
    char[] c = new char[(int) n];
    int numSkipped = this.read(c);
    return numSkipped;
    
  }
}
