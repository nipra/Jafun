import java.awt.*;
import java.awt.event.*;
/*
 *
 * MyFrame
 *
 */
public class MyFrame extends Frame 
		implements WindowListener, 
		           ActionListener
{
   private Button btnClose;
   private Button btnOk;

   public MyFrame ()
	{
	   setTitle ("This is my Frame");
	   setBackground (Color.lightGray);

	     //Create a toolbar Panel object and add the buttons
	   Panel panToolbar = new Panel ();
		 panToolbar.setLayout (new FlowLayout (FlowLayout.LEFT));
	       btnClose = new Button ("Close");
		   btnOk = new Button ("OK");
	     panToolbar.add ( btnClose );
		 panToolbar.add ( btnOk );
	   add ( panToolbar, "North");

	   add (new EtchedLabel (), "Center" );

	   addWindowListener (this);
	   btnClose.addActionListener (this);
	   btnOk.addActionListener (this);
	}

   public void actionPerformed(ActionEvent e)
    {
	   if ( e.getSource() == btnClose )
	     {
	     System.out.println ("Action ");
	     System.exit(0);
	     }

	   if ( e.getSource() == btnOk )
	     {
	     System.out.println ("Action on BtnOk");
	     }
    }

   public void windowActivated(WindowEvent e)
    {
	   System.out.println ("Window Activated");
    }

   public void windowDeactivated(WindowEvent e)
    {
	   System.out.println ("Window Deactivated");
    }

   public void windowIconified(WindowEvent e)
    {
	   System.out.println ("Window Iconified");
    }

   public void windowDeiconified(WindowEvent e)
    {
	   System.out.println ("Window Deiconified");
    }

   public void windowOpened(WindowEvent e)
    {
	   System.out.println ("Window Opened");
    }

   public void windowClosed(WindowEvent e)
    {
	   System.out.println ("Window Closed");
	   System.exit(0);
    }

   public void windowClosing(WindowEvent e)
    {
	   System.out.println ("Window Closing");
	   dispose();
    }

}

