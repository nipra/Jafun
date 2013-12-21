package com.elharo.io;

import java.io.*;

public class PrintableInputStream extends FilterInputStream {

  public PrintableInputStream(InputStream in) {
    super(in);
  }

  public int read() throws IOException {
  
    int b = in.read();
    // printing, ASCII characters
    if (b >= 32 && b <= 126) return b;
    else if (b == '\n' || b == '\r' || b == '\t') return b;
    // nonprinting characters
    else return '?'; 

  }

  public int read(byte[] data, int offset, int length) throws IOException {
  
    int result = in.read(data, offset, length);
    for (int i = offset; i < offset+result; i++) {
      // Do nothing with the printing characters.
      if (data[i] == '\n'|| data[i] == '\r' || data[i] == '\t' || data[i] == -1) ;
      // nonprinting characters
      else if (data[i] < 32 || data[i] > 126) data[i] = (byte) '?';
    }
    return result;
  }
}
