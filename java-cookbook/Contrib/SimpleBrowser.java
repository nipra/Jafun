// SimpleBrowser.java
// From O'Reilly's web site about their Swing GUI book
//

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;
import javax.swing.text.html.*;
import javax.swing.event.*;

public class SimpleBrowser extends JFrame {

    static JTextField textField;
    static JEditorPane editor;

    public SimpleBrowser(String s) {
        super(s);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createRaisedBevelBorder());

        editor = new JEditorPane();
        textField  = new JTextField();
        JScrollPane scrollPane = new JScrollPane(editor);

        editor.setEditable(false);

        panel.add(new JLabel("Location:  "), BorderLayout.WEST);
        panel.add(textField, BorderLayout.CENTER);

        getContentPane().add(panel, BorderLayout.NORTH);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        textField.addActionListener(new TextFieldListener());
    }

    public static void main(String[] args) {
        SimpleBrowser frame = new SimpleBrowser("Simple Browser");
        frame.setSize(400,400);
        frame.setVisible(true);
    }

    class TextFieldListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            try {
                editor.setPage(textField.getText());
            } catch (IOException ex) {
                editor.setText("Page could not be loaded");
            }
        }
    }
}
