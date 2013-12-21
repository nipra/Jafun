// 8.4 Maintain Binary Compatibility

interface Name extends Comparable {
  public int compareTo(Object o);
}

class SimpleName implements Name {
  private String base;
  public SimpleName(String base) {
    this.base = base;
  }
  public int compareTo(Object o) {
    return base.compareTo(((SimpleName)o).base);
  }
}

class ExtendedName extends SimpleName {
  private String ext;
  public ExtendedName(String base, String ext) {
    super(base); this.ext = ext;
  }
  public int compareTo(Object o) {
    int c = super.compareTo(o);
    if (c == 0 && o instanceof ExtendedName)
      return ext.compareTo(((ExtendedName)o).ext);
    else
      return c;
  }
}    

class Client {
  public static void main(String[] args) {
    Name m = new ExtendedName("a","b");
    Name n = new ExtendedName("a","c");
    assert m.compareTo(n) < 0;
  }
}
