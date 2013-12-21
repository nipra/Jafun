package com.macfaq.io;

import java.io.*;


public class LELongFilter extends LEFilter {

  public LELongFilter(LittleEndianInputStream lin) {
    super(lin);
  }

  protected void fill() throws IOException {
  
    long number = lin.readLong();
    String s = Long.toString(number) 
     + System.getProperty("line.separator", "\r\n");
    byte[] b = s.getBytes("8859_1");
    buf = new int[b.length];
    for (int i = 0; i < b.length; i++) {
      buf[i] = b[i];
    }
  
  }
  
}
