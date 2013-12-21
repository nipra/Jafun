package com.elharo.io;

import java.awt.*;
import java.util.*;
import java.io.*;

public class ExtensionFilenameFilter implements FilenameFilter  {

  ArrayList extensions = new ArrayList();

  public ExtensionFilenameFilter(String extension) {

    if (extension.indexOf('.') != -1) {
      extension = extension.substring(extension.lastIndexOf('.')+1);
    }
    extensions.add(extension);
  }

  public void addExtension(String extension) {
  
    if (extension.indexOf('.') != -1) {
      extension = extension.substring(extension.lastIndexOf('.')+1);
    }
    extensions.add(extension);
  }

  public boolean accept(File directory, String filename) {
  
    String extension = filename.substring(filename.lastIndexOf('.')+1);
    if (extensions.contains(extension)) {
      return true;
    }
    return false;
  }
}
