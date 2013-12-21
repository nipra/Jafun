import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import com.elharo.io.ui.JStreamedTextArea;

public class FileViewer extends JFrame implements ActionListener {

  private JFileChooser chooser = new JFileChooser();
  private JStreamedTextArea theView = new JStreamedTextArea();
  private ModePanel mp = new ModePanel();

  public FileViewer() {
    super("FileViewer");
  }

  public void init() {
    chooser.setApproveButtonText("View File");
    chooser.setApproveButtonMnemonic('V');
    chooser.addActionListener(this);
    
    this.getContentPane().add(BorderLayout.CENTER, chooser);
    JScrollPane sp = new JScrollPane(theView);
    sp.setPreferredSize(new Dimension(640, 400));
    this.getContentPane().add(BorderLayout.SOUTH, sp);
    this.getContentPane().add(BorderLayout.WEST, mp);
    this.pack();
    
    // Center on display.
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
          in = new ProgressMonitorInputStream(this, "Reading...", in);
          OutputStream out = theView.getOutputStream();
          FileDumper5.dump(in, out, mp.getMode(), mp.isBigEndian(),
           mp.isDeflated(), mp.isGZipped(), mp.getPassword());
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
    }
  }

  public static void main(String[] args) {  
    FileViewer viewer = new FileViewer();
    viewer.init();
    // This is a single window application
    viewer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    viewer.setVisible(true);
  }
}
