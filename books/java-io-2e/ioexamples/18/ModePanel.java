import java.awt.*;
import javax.swing.*;

public class ModePanel extends JPanel {

private JCheckBox bigEndian = new JCheckBox("Big Endian", true);
  private JCheckBox deflated  = new JCheckBox("Deflated", false);
  private JCheckBox gzipped   = new JCheckBox("GZipped", false);
  
  private ButtonGroup  dataTypes    = new ButtonGroup();
  private JRadioButton asciiRadio   = new JRadioButton("ASCII");
  private JRadioButton decimalRadio = new JRadioButton("Decimal");
  private JRadioButton hexRadio     = new JRadioButton("Hexadecimal");
  private JRadioButton shortRadio   = new JRadioButton("Short");
  private JRadioButton intRadio     = new JRadioButton("Int");
  private JRadioButton longRadio    = new JRadioButton("Long");
  private JRadioButton floatRadio   = new JRadioButton("Float");
  private JRadioButton doubleRadio  = new JRadioButton("Double");
  
  private JTextField password = new JPasswordField();
  
  public ModePanel() {
  
    this.setLayout(new GridLayout(13, 1));
    this.add(bigEndian);
    this.add(deflated);
    this.add(gzipped);
    
    this.add(asciiRadio);
    asciiRadio.setSelected(true);
    this.add(decimalRadio);
    this.add(hexRadio);
    this.add(shortRadio);
    this.add(intRadio);
    this.add(longRadio);
    this.add(floatRadio);
    this.add(doubleRadio);
    
    dataTypes.add(asciiRadio);
    dataTypes.add(decimalRadio);
    dataTypes.add(hexRadio);
    dataTypes.add(shortRadio);
    dataTypes.add(intRadio);
    dataTypes.add(longRadio);
    dataTypes.add(floatRadio);
    dataTypes.add(doubleRadio);
    
    this.add(password);
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
