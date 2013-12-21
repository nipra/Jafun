package com.macfaq.swing;

import javax.swing.*;
import java.io.*;
import java.awt.*;


public class JStreamedTextArea extends JTextArea {

  OutputStream theOutput = new TextAreaOutputStream();

  public JStreamedTextArea() {
    this("", 12, 20);
  }

  public JStreamedTextArea(String text) {
    this(text, 12, 20);
  } 

  public JStreamedTextArea(int rows, int columns) {
    this("", rows, columns);
  }

  public JStreamedTextArea(String text, int rows, int columns) {
    super(text, rows, columns);
    this.setEditable(false); 
    this.setFont(new Font("Monospaced", Font.PLAIN, 12));
  }

  public OutputStream getOutputStream() {
    return theOutput;
  }
  
  public Dimension getMinimumSize() {
    return new Dimension(72, 200);
  }
  
  public Dimension getPreferredSize() {
    return new Dimension(60*12, getLineCount()*12);
  }
  
  class TextAreaOutputStream extends OutputStream {

    public void write(int b) {

      // recall that the int should really just be a byte
      b &= 0x000000FF;

      // must convert byte to a char in order to append it
      char c = (char) b;
      append(String.valueOf(c));

    }

    public void write(byte[] b, int offset, int length) {

      append(new String(b, offset, length));

    }

  }

}
