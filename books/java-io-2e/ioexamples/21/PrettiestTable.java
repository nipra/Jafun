import java.text.*;
import java.applet.*;
import java.awt.*;

public class PrettiestTable extends Applet {

  NumberFormat myFormat = NumberFormat.getNumberInstance();
  FieldPosition fp = new FieldPosition(NumberFormat.INTEGER_FIELD);
  
  public void init() {

    this.setFont(new Font("Serif", Font.BOLD, 12));
    myFormat.setMaximumIntegerDigits(3);
    myFormat.setMaximumFractionDigits(2);
    myFormat.setMinimumFractionDigits(2);
  }
  
  public void paint(Graphics g) {
  
    FontMetrics fm = this.getFontMetrics(this.getFont()) ;
    int xmargin = 5;
    int lineHeight = fm.getMaxAscent() + fm.getMaxDescent();
    int y = lineHeight;
    int x = xmargin;
    int desiredPixelWidth = 3 * fm.getMaxAdvance();
    int fieldWidth = 6 * fm.getMaxAdvance();
    int headerWidth = fm.stringWidth("Degrees");
    g.drawString("Degrees", x + (fieldWidth - headerWidth)/2, y);
    headerWidth = fm.stringWidth("Radians");    
    g.drawString("Radians", x + fieldWidth + (fieldWidth - headerWidth)/2, y);
    headerWidth = fm.stringWidth("Grads");
    g.drawString("Grads", x + 2*fieldWidth + (fieldWidth - headerWidth)/2, y);
    
    for (double degrees = 0.0; degrees < 360.0; degrees++) {
      y += lineHeight;
      String degreeString = myFormat.format(degrees, new StringBuffer(), 
         fp).toString(); 
      String intPart = degreeString.substring(0, fp.getEndIndex());
      g.drawString(degreeString, xmargin + desiredPixelWidth 
         - fm.stringWidth(intPart), y);
      String radianString = myFormat.format(Math.PI*degrees/180.0, 
         new StringBuffer(), fp).toString();
      intPart = radianString.substring(0, fp.getEndIndex());
      g.drawString(radianString, 
          xmargin + fieldWidth + desiredPixelWidth - fm.stringWidth(intPart), y);
      String gradString = myFormat.format(400 * degrees / 360, 
         new StringBuffer(), fp).toString();
      intPart = gradString.substring(0, fp.getEndIndex());
      g.drawString(gradString, 
        xmargin + 2*fieldWidth + desiredPixelWidth - fm.stringWidth(intPart), y);
    }
  }
}
