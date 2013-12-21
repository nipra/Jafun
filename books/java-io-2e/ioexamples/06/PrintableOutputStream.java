package com.elharo.io;

import java.io.*;

public class PrintableOutputStream extends FilterOutputStream {

  public PrintableOutputStream(OutputStream out) {
    super(out);
  }

  public void write(int b) throws IOException {
  
    // carriage return, linefeed, and tab 
    if (b == '\n' || b == '\r' || b == '\t') out.write(b);
    // non-printing characters
    else if (b < 32 || b > 126) out.write('?');
    // printing, ASCII characters
    else out.write(b);  
  }

  public void write(byte[] data, int offset, int length) throws IOException {
    for (int i = offset; i < offset+length; i++) {
      this.write(data[i]);
    }
  }
}
