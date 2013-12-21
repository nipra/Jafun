package com.elharo.io;

import java.io.*;

public abstract class DataFilter extends DumpFilter {

  // The use of DataInputStream here is a little forced.
  // It would be more natural (though more complicated)
  // to read the bytes and manually convert them to an int.
  private DataInputStream din;

  public DataFilter(DataInputStream din) {
    super(din);
    this.din = din;
  }
  
  public int available() throws IOException {
    return (buf.length - index) + in.available();
  }
}
