package com.macfaq.io;

import java.io.*;


public class LEDoubleFilter extends LEFilter {

  public LEDoubleFilter(LittleEndianInputStream lin) {
    super(lin);
  }

  protected void fill() throws IOException {
  
    double number = lin.readDouble();
    String s = Double.toString(number) 
     + System.getProperty("line.separator", "\r\n");
    byte[] b = s.getBytes("8859_1");
    buf = new int[b.length];
    for (int i = 0; i < b.length; i++) {
      buf[i] = b[i];
    }
  
  }
  
}
