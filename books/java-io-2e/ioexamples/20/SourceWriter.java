package com.elharo.io;

import java.io.*;

public class SourceWriter extends FilterWriter {

  public SourceWriter(Writer out) {
    super(out); 
  }

  public void write(char[] text, int offset, int length) throws IOException {
    
    for (int i = offset; i < offset+length; i++) {
      this.write(text[i]);
    }
  }
  
  public void write(String s, int offset, int length) throws IOException {
    
    for (int i = offset; i < offset+length; i++) {
      this.write(s.charAt(i));
    }
  }
  
  public void write(int c) throws IOException {
    
    // We have to escape the backslashes below.
    if (c == '\\') out.write("\\u005C");
    else if (c < 128) out.write(c);
    else {
      String s = Integer.toHexString(c);
      // Pad with leading zeroes if necessary.
      if (c < 256) s = "00" + s;
      else if (c < 4096) s = "0" + s;
      out.write("\\u");
      out.write(s);
    }
  }
}
