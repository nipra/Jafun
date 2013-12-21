package com.macfaq.io;

import java.io.*;


public class LEIntFilter extends LEFilter {

  public LEIntFilter(LittleEndianInputStream lin) {
    super(lin);
  }

  protected void fill() throws IOException {
  
    int number = lin.readInt();
    String s = Integer.toString(number) 
     + System.getProperty("line.separator", "\r\n");
    byte[] b = s.getBytes("8859_1");
    buf = new int[b.length];
    for (int i = 0; i < b.length; i++) {
      buf[i] = b[i];
    }
  
  }
  
}
