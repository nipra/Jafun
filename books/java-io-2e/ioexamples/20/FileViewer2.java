import javax.swing.*;
import java.io.*;
import com.elharo.io.ui.*;
import java.awt.*;
import java.awt.event.*;

public class FileViewer2 extends JFrame implements ActionListener {

  JFileChooser chooser = new JFileChooser();
  JWritableTextArea theView = new JWritableTextArea();
  TextModePanel mp = new TextModePanel();

  public FileViewer2() {
    super("FileViewer");
  }

  public void init() {
    chooser.setApproveButtonText("View File");
    chooser.setApproveButtonMnemonic('V');
    chooser.addActionListener(this);
    
    this.getContentPane().add(BorderLayout.EAST, chooser);
    JScrollPane sp = new JScrollPane(theView);
    sp.setPreferredSize(new Dimension(640, 400));
    this.getContentPane().add(BorderLayout.SOUTH, sp);
    this.getContentPane().add(BorderLayout.WEST, mp);
    this.pack();
    
    // Center on display
    Dimension display = getToolkit().getScreenSize();
    Dimension bounds = this.getSize();
    int x = (display.width - bounds.width)/2;
    int y = (display.height - bounds.height)/2;
    if (x < 0) x = 10;
    if (y < 0) y = 15;
    this.setLocation(x, y);
  }
  
  public void actionPerformed(ActionEvent evt) {
  
    if (evt.getActionCommand().equals(JFileChooser.APPROVE_SELECTION)) {
      File f = chooser.getSelectedFile();
      if (f != null) {
        theView.reset();
        try {
          InputStream in = new FileInputStream(f);
          // This program was really slow until I buffered the stream.
          in = new BufferedInputStream(in);
          in = new ProgressMonitorInputStream(this, "Reading...", in);
          if (!mp.isText()) {
            FileDumper6.dump(in, theView.getWriter(), mp.getMode(), 
                             mp.isBigEndian(),
             mp.isDeflated(), mp.isGZipped(), mp.getPassword());
          }
          else {
            FileDumper6.dump(in, theView.getWriter(), mp.getEncoding(), null,
             mp.isDeflated(), mp.isGZipped(), mp.getPassword());        
          }
        }
        catch (IOException ex) {
          JOptionPane.showMessageDialog(this, ex.getMessage(), 
            "I/O Error", JOptionPane.ERROR_MESSAGE);
        }
      }
    }
    else if (evt.getActionCommand().equals(JFileChooser.CANCEL_SELECTION)) {
      this.setVisible(false);
      this.dispose();
      // This is a single window application
      System.exit(0);
    }
  }

  public static void main(String[] args) {
    FileViewer2 viewer = new FileViewer2();
    viewer.init();
    viewer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    viewer.setVisible(true);
  }
}
