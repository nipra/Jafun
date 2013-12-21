package com.elharo.io;
import java.io.*;

public class DecimalFilter extends DumpFilter {

  private int numRead = 0;
  private int breakAfter = 15;
  private int ratio = 4; // number of bytes of output per byte of input

  public DecimalFilter(InputStream in) {
    super(in);
  }

  protected void fill() throws IOException {
  
    buf = new int[ratio];
    int datum = in.read();
    this.numRead++;    
    if (datum == -1) {
      // Let read() handle end of stream.
      throw new EOFException();
    }

    String dec = Integer.toString(datum);
    if (datum < 10) { // Add two leading zeros.
      dec = "00" + dec;
    }
    else if (datum < 100) { // Add leading zero.
      dec = '0' + dec;
    }
    for (int i = 0; i < dec.length(); i++) {
      buf[i] = dec.charAt(i);
    }
    if (numRead < breakAfter) {
      buf[buf.length - 1] = ' ';
    }
    else {
      buf[buf.length - 1] = '\n';
      numRead = 0;
    }
  }
  
  public int available() throws IOException {
    return (buf.length - index) + ratio * in.available();
  }
}