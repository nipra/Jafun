package com.macfaq.io;

import java.io.*;


public class FloatFilter extends DataFilter {

  public FloatFilter(DataInputStream din) {
    super(din);
  }

  protected void fill() throws IOException {
  
    float number = din.readFloat();
    String s = Float.toString(number) 
     + System.getProperty("line.separator", "\r\n");
    byte[] b = s.getBytes("8859_1");
    buf = new int[b.length];
    for (int i = 0; i < b.length; i++) {
      buf[i] = b[i];
    }
  
  }
  
}
