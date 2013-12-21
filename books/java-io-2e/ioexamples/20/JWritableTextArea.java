package com.elharo.io.ui;

import javax.swing.*;
import java.awt.Font;
import java.io.*;

public class JWritableTextArea extends JTextArea {

  private Writer writer = new BufferedWriter(new TextAreaWriter());

  public JWritableTextArea() {
    this("", 0, 0);
  }

  public JWritableTextArea(String text) {
    this(text, 0, 0);
  } 

  public JWritableTextArea(int rows, int columns) {
    this("", rows, columns);
  }

  public JWritableTextArea(String text, int rows, int columns) {
    super(text, rows, columns);
    setFont(new Font("Monospaced", Font.PLAIN, 12));
    setEditable(false);
  }

  public Writer getWriter() {
    return writer;
  }

  public void reset() {
    this.setText("");
    writer = new BufferedWriter(new TextAreaWriter());
  }
  
  private class TextAreaWriter extends Writer {
      
    private boolean closed = false;
    
    public void close() {
      closed = true;
    }

    public void write(char[] text, int offset, int length) throws IOException {
      if (closed) throw new IOException("Write to closed stream");
      JWritableTextArea.this.append(new String(text, offset, length));
    }

    public void flush() {} 
  }
}
