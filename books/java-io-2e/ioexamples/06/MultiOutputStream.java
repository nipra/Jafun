package com.elharo.io;

import java.io.*;
import java.util.*;

public class MultiOutputStream extends FilterOutputStream {

  private List streams = new ArrayList();

  public MultiOutputStream(OutputStream out) {
    super(out);
    streams.add(out);
  }
  
  public void addOutputStream(OutputStream out) {
    streams.add(out);
  }

  public void write(int b) throws IOException {  
    Iterator iterator = streams.iterator();
    while (iterator.hasNext()) {
      OutputStream out = (OutputStream) iterator.next();
      out.write(b);
    }
  }

  public void write(byte[] data, int offset, int length) 
   throws IOException {
    Iterator iterator = streams.iterator();
    while (iterator.hasNext()) {
      OutputStream out = (OutputStream) iterator.next();
      out.write(data, offset, length);
    }
  }

  public void flush() throws IOException {
    Iterator iterator = streams.iterator();
    while (iterator.hasNext()) {
      OutputStream out = (OutputStream) iterator.next();
      out.flush();
    }
  }
  
  public void close() throws IOException {
    Iterator iterator = streams.iterator();
    while (iterator.hasNext()) {
      OutputStream out = (OutputStream) iterator.next();
      out.close();
    }
  }
}
