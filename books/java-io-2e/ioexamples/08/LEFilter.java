package com.elharo.io;

import java.io.*;

public abstract class LEFilter extends DumpFilter {

  private LittleEndianInputStream lin;

  public LEFilter(LittleEndianInputStream lin) {
    super(lin);
    this.lin = lin;
  }
  
  public int available() throws IOException {
    return (buf.length - index) + lin.available();
  }
}
