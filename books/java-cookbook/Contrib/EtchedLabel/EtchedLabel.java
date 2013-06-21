import java.awt.*;
import java.io.Serializable;
/*
 *
 * EtchedLabel
 *
 */
public class EtchedLabel extends Canvas	implements Serializable 
{
   public EtchedLabel ()
    {
	   setSize (350,150);
    }

   public void paint (Graphics g)
    {
	   g.setFont (new Font ("Times", Font.ITALIC+Font.BOLD, 28));

	   g.setColor (Color.red);
	   g.drawString ("Etched Label", 100, 100);

	   g.setColor (Color.black);
	   g.drawString ("Etched Label", 101, 101);
    }
     
   public Dimension getPreferredSize()
    {
	return new Dimension (350, 150);
    }

   public Dimension getMinimumSize()
    {
	return new Dimension (50, 70);
    }
}

