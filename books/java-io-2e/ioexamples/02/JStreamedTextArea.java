package com.elharo.io.ui;

import javax.swing.*;
import java.io.*;

public class JStreamedTextArea extends JTextArea {

  private OutputStream theOutput = new TextAreaOutputStream();

  public JStreamedTextArea() {
    this("", 0, 0);
  }

  public JStreamedTextArea(String text) {
    this(text, 0, 0);
  } 

  public JStreamedTextArea(int rows, int columns) {
    this("", rows, columns);
  }

  public JStreamedTextArea(String text, int rows, int columns) {
    super(text, rows, columns);
    setEditable(false);
  }

  public OutputStream getOutputStream() {
    return theOutput;
  }

  private class TextAreaOutputStream extends OutputStream {
      
    private boolean closed = false;

    public void write(int b) throws IOException {
      checkOpen(); 
      // recall that the int should really just be a byte
      b &= 0x000000FF;
      // must convert byte to a char in order to append it
      char c = (char) b;
      append(String.valueOf(c));
    }

    private void checkOpen() throws IOException {
        if (closed) throw new IOException("Write to closed stream");
    }

    public void write(byte[] data, int offset, int length) 
     throws IOException {
      checkOpen();
      append(new String(data, offset, length));
    }
    
    public void close() {
        this.closed = true;
    }
    
  }
}
