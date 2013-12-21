package com.elharo.io;

import java.util.*;
import java.io.*;

public class RandomInputStream extends InputStream {

  private Random generator = new Random();
  private boolean closed = false;

  public int read() throws IOException {
    checkOpen();
    int result = generator.nextInt() % 256;
    if (result < 0) result = -result;
    return result;
  }

  public int read(byte[] data, int offset, int length) throws IOException {
    checkOpen();
    byte[] temp = new byte[length];
    generator.nextBytes(temp);
    System.arraycopy(temp, 0, data, offset, length);
    return length;

  }

  public int read(byte[] data) throws IOException {
    checkOpen();
    generator.nextBytes(data);
    return data.length;

  }

  public long skip(long bytesToSkip) throws IOException {
    checkOpen();
    // It's all random so skipping has no effect.
    return bytesToSkip;
  }
  
  public void close() {
      this.closed = true;
  }
  
  private void checkOpen() throws IOException {
      if (closed) throw new IOException("Input stream closed");
  }

  public int available() {
    // Limited only by available memory and the size of an array.
    return Integer.MAX_VALUE;
  }
}
