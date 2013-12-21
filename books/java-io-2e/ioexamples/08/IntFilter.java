package com.elharo.io;

import java.io.*;

public class IntFilter extends DataFilter {

  public IntFilter(DataInputStream din) {
    super(din);
  }

  protected void fill() throws IOException {
    int number = din.readInt();
    String s = Integer.toString(number) 
     + System.getProperty("line.separator", "\r\n");
    byte[] b = s.getBytes("8859_1");
    buf = new int[b.length];
    for (int i = 0; i < b.length; i++) {
      buf[i] = b[i];
    }
  }
}
