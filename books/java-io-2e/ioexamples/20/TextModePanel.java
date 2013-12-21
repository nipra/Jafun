import java.awt.*;
import javax.swing.*;
import java.nio.charset.*;
import java.util.*;

public class TextModePanel extends JPanel {

  JCheckBox bigEndian = new JCheckBox("Big Endian", true);
  JCheckBox deflated  = new JCheckBox("Deflated", false);
  JCheckBox gzipped   = new JCheckBox("GZipped", false);
  
  ButtonGroup dataTypes     = new ButtonGroup();
  JRadioButton asciiRadio   = new JRadioButton("Text");
  JRadioButton decimalRadio = new JRadioButton("Decimal");
  JRadioButton hexRadio     = new JRadioButton("Hexadecimal");
  JRadioButton shortRadio   = new JRadioButton("Short");
  JRadioButton intRadio     = new JRadioButton("Int");
  JRadioButton longRadio    = new JRadioButton("Long");
  JRadioButton floatRadio   = new JRadioButton("Float");
  JRadioButton doubleRadio  = new JRadioButton("Double");
  
  JTextField password = new JPasswordField();
  JList encodings = new JList();
  
  public TextModePanel() {

    Map charsets = Charset.availableCharsets();
    encodings.setListData(charsets.keySet().toArray());
    
    this.setLayout(new GridLayout(1, 2));
    
    JPanel left = new JPanel();
    JScrollPane right = new JScrollPane(encodings);
    left.setLayout(new GridLayout(13, 1));
    left.add(bigEndian);
    left.add(deflated);
    left.add(gzipped);
    
    left.add(asciiRadio);
    asciiRadio.setSelected(true);
    left.add(decimalRadio);
    left.add(hexRadio);
    left.add(shortRadio);
    left.add(intRadio);
    left.add(longRadio);
    left.add(floatRadio);
    left.add(doubleRadio);
    
    dataTypes.add(asciiRadio);
    dataTypes.add(decimalRadio);
    dataTypes.add(hexRadio);
    dataTypes.add(shortRadio);
    dataTypes.add(intRadio);
    dataTypes.add(longRadio);
    dataTypes.add(floatRadio);
    dataTypes.add(doubleRadio);
    
    left.add(password);
    this.add(left);
    this.add(right);
  }

  public boolean isBigEndian() {
    return bigEndian.isSelected();
  }
  
  public boolean isDeflated() {
    return deflated.isSelected();
  }
  
  public boolean isGZipped() {
    return gzipped.isSelected();
  }
  
  public boolean isText() {
    if (this.getMode() == FileDumper6.ASC) return true;
    return false;
  }
  
  public String getEncoding() {
    return (String) encodings.getSelectedValue();
  }
  
  public int getMode() {

    if (asciiRadio.isSelected()) return FileDumper6.ASC;
    else if (decimalRadio.isSelected()) return FileDumper6.DEC;
    else if (hexRadio.isSelected()) return FileDumper6.HEX;
    else if (shortRadio.isSelected()) return FileDumper6.SHORT;
    else if (intRadio.isSelected()) return FileDumper6.INT;
    else if (longRadio.isSelected()) return FileDumper6.LONG;
    else if (floatRadio.isSelected()) return FileDumper6.FLOAT;
    else if (doubleRadio.isSelected()) return FileDumper6.DOUBLE;
    else return FileDumper6.ASC;
  }
  
  public String getPassword() {
    return password.getText();
  }
}
