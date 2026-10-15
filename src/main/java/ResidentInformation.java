import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class ResidentInformation {
    static JFrame frame;
    static JPanel panel;
    static int Height = 600;
    static final int Width = 1200;
    static Font bigFont = FontHandler.getGIB(60);
    static Font smallFont = FontHandler.getGIR(25);
    static JTextField[] fields = new JTextField[5];
    static int fieldcount = 0;

    static final int inset = 50;
    static final int labelinset = 20;

    public ResidentInformation() {
        panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Top, Left, Bottom, Right insets
        panel.setLayout(null);

        // Center horizontally and put near the top
        JLabel label = new JLabel("User Information");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.BOTTOM);
        label.setFont(bigFont);
        label.setSize(Width, Height/8);
        label.setLocation(0, 0);
        panel.add(label);

        //Divider
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setSize((int)(Width/1.5 + 30), 20);
        separator.setLocation(Width/6 - 15, Height/8);
        panel.add(separator);

        //Labels
        makeRow("NAME: ",Width/8,Width/5 * 3,Height/8 );
        makeRow("GENDER: ",Width/8,Width/5 * 3,Height/8*2 );
        makeRow("AGE: ",Width/8,Width/5 * 3,Height/8*3 );
        makeRow("ADDRESS: ",Width/8,Width/5 * 4,Height/8*4 );
        makeRow("PHONE #: ",Width/8,Width/5 * 4,Height/8*5 );

        JButton savebutton = new JButton("SAVE");
        savebutton.setFont(smallFont);
        savebutton.setBounds(0, (int)((Height / 8.0) * 6.5), Width, Height/10);
        savebutton.setBackground(Colors.blue);
        savebutton.setForeground(Color.WHITE);
        savebutton.addActionListener(new SaveListener());
        panel.add(savebutton);

        //IMAGE
        JButton button = new JButton("Click to select image");
        button.setBounds((int)(Width/8 * 6.4), Height/8 + labelinset, 200, 200);
        button.setBackground(Colors.blue);
        button.setForeground(Color.WHITE);

        // Add action listener to the button
        button.addActionListener(new HeaderListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open file chooser dialog to select an image
                JFileChooser chooser = new JFileChooser();
                chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Image files", "jpg", "png", "gif"));
                int result = chooser.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = chooser.getSelectedFile();  // This uses the File class from java.io
                    ImageIcon icon = new ImageIcon(selectedFile.getAbsolutePath());

                    // Optionally scale the image to fit in the button
                    Image scaledImage = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);  // Adjust the size of the button to match

                    // Set the image as the button icon
                    button.setIcon(new ImageIcon(scaledImage));

                    // Remove text from the button
                    button.setText("");  // Remove the "Click to select image" text
                }
            }
        });
        panel.add(button);
    }

    public static void main(String[] args) {
        // Create a JFrame to hold the panel
        frame = new JFrame("Panel with Inset");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(Width, Height);

        ResidentInformation resinfo = new ResidentInformation();

        frame.add(panel);
        frame.setVisible(true);
    }

    static void makeRow(String labelname, int labellength, int fieldlength, int heightfromtop) {
        JLabel newlabel = new JLabel(labelname);
        newlabel.setFont(smallFont);
        newlabel.setBounds( inset, heightfromtop + labelinset, labellength, Height/8 - labelinset );
        newlabel.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(newlabel);

        JTextField newfield = new JTextField();
        newfield.setBorder(null);
        newfield.setFont(smallFont);
        newfield.setBounds( inset+labellength, heightfromtop+labelinset, fieldlength, Height/8 - labelinset);
        newfield.setHorizontalAlignment(SwingConstants.CENTER);
        newfield.setText(getfieldtext(labelname));
        panel.add(newfield);

        fields[fieldcount] = newfield;
        fieldcount++;
    }

    static String getfieldtext(String labelname){
        if (labelname.equals("NAME: ")){
            return User.current.getString("name");
        }else if (labelname.equals("GENDER: ")){
            return User.current.getString("gender");
        }else if (labelname.equals("AGE: ")){
            return User.current.getString("age");
        }else if (labelname.equals("PHONE #: ")){
            return User.current.getString("contact");
        }else if (labelname.equals("ADDRESS: ")){
            return User.current.getString("address");
        }
        return null;
    }

    public class SaveListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = fields[0].getText();
            String gender = fields[1].getText();
            String age = fields[2].getText();
            String address = fields[3].getText();
            String contact = fields[4].getText();

            Firebase.updateUser(name,contact,gender,address,age);
        }
    }

}
