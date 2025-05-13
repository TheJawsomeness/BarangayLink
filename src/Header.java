import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Header {
    Font h = new Font("Monospaced", Font.BOLD, 60);
    Font h2 = new Font("Monospaced", Font.BOLD, 20);
    Color blue = new Color(0,74,173);
    Color darkblue = new Color(0,62,144);
    Color offwhite = new Color(251,251,251);

    public Header(){
        JFrame frame = new JFrame("BarangayLink");

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        panel.setBackground(blue);

        //BarangayLink Header
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 7;
        c.gridheight = 2;
        c.fill  = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;

        JLabel BarangayLinkHeader = new JLabel("BARANGAYLINK");
        BarangayLinkHeader.setFont(h);
        BarangayLinkHeader.setBackground(blue);
        BarangayLinkHeader.setForeground(offwhite);
        BarangayLinkHeader.setBorder(new EmptyBorder(10, 20, 10, 20));
        panel.add(BarangayLinkHeader,c);

        //PictureButton
        c.gridx = 7;
        c.gridy = 0;
        c.gridwidth = 2;
        c.gridheight = 2;
        c.fill  = GridBagConstraints.BOTH;
        c.weightx = 0.2;
        c.weighty = 1;

        JButton PictureButton = new JButton("Placeholder");
        PictureButton.setBackground(blue);
        PictureButton.setForeground(offwhite);
        PictureButton.setBorderPainted(false);
        panel.add(PictureButton,c);

        //Button1
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 3;
        c.gridheight = 1;
        c.fill  = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 0.2;

        JButton Button1 = new JButton("Button  1");
        Button1.setBackground(darkblue);
        Button1.setForeground(offwhite);
        Button1.setBorderPainted(false);
        panel.add(Button1,c);

        //Button2
        c.gridx = 3;
        c.gridy = 2;
        c.gridwidth = 3;
        c.gridheight = 1;
        c.fill  = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 0.2;

        JButton Button2 = new JButton("Button 2");
        Button2.setBackground(darkblue);
        Button2.setForeground(offwhite);
        Button2.setBorderPainted(false);
        panel.add(Button2,c);

        //Button3
        c.gridx = 6;
        c.gridy = 2;
        c.gridwidth = 3;
        c.gridheight = 1;
        c.fill  = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 0.2;

        JButton Button3 = new JButton("Button  3");
        Button3.setBackground(darkblue);
        Button3.setForeground(offwhite);
        Button3.setBorderPainted(false);
        panel.add(Button3,c);

        //Add Panel
        frame.add(panel);
        frame.setSize(800,200);
        frame.setVisible(true);
    }
}
