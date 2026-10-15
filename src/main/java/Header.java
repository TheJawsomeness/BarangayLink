import java.awt.*;
import java.io.IOException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Header {
    Font h = FontHandler.getGIR(60);
    Font h2 = FontHandler.getGIR(25);
    Header hdr = null;

    JPanel panel;

    public Header() throws IOException, FontFormatException {
        panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        panel.setBackground(Colors.blue);

        //MainHeaderPanel
        JPanel MainHeaderPanel = new JPanel(new GridBagLayout());
        MainHeaderPanel.setBackground(Colors.blue);
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill  = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 4;
        panel.add(MainHeaderPanel,c);

        //SubHeaderPanel
        JPanel SubHeaderPanel = new JPanel(new GridBagLayout());
        SubHeaderPanel.setBackground(Colors.blue);
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill  = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        panel.add(SubHeaderPanel,c);

        //BarangayLink Header
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill  = GridBagConstraints.BOTH;
        c.weightx = 7;
        c.weighty = 1;

        JLabel BarangayLinkHeader = new JLabel("BARANGAYLINK");
        BarangayLinkHeader.setFont(h);
        BarangayLinkHeader.setBackground(Colors.blue);
        BarangayLinkHeader.setForeground(Colors.offwhite);
        BarangayLinkHeader.setBorder(new EmptyBorder(10, 20, 10, 20));
        MainHeaderPanel.add(BarangayLinkHeader,c);

        //PictureButton
        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill  = GridBagConstraints.BOTH;
        c.weightx = 2;
        c.weighty = 1;

        JButton PictureButton = new JButton(User.current.getString("name"));

        // Set image to the label
        PictureButton.setIcon(getImage());

        // Position the text to the left of the image
        PictureButton.setHorizontalTextPosition(SwingConstants.LEFT);  // Text on the left
        PictureButton.setVerticalTextPosition(SwingConstants.TOP);   // Vertically centered
        PictureButton.setFont(h2);

        // Optional: Add some padding between text and image
        PictureButton.setIconTextGap(10);  // 10 pixels between text and image

        PictureButton.setBackground(Colors.blue);
        PictureButton.setForeground(Colors.offwhite);
        PictureButton.setFocusPainted(false);
        PictureButton.setBorderPainted(false);
        MainHeaderPanel.add(PictureButton,c);

        //Button1
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill  = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;

        JButton Button1 = new JButton("Resident Information");
        Button1.setBackground(Colors.darkblue);
        Button1.setForeground(Colors.offwhite);
        Button1.setBorderPainted(false);
        Button1.setActionCommand("0");
        Button1.addActionListener(new HeaderListener());
        SubHeaderPanel.add(Button1,c);

        //Button2
        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill  = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;

        JButton Button2 = new JButton("Document Request");
        Button2.setBackground(Colors.darkblue);
        Button2.setForeground(Colors.offwhite);
        Button2.setBorderPainted(false);
        Button2.setActionCommand("1");
        Button2.addActionListener(new HeaderListener());
        SubHeaderPanel.add(Button2,c);

        //Button3
        c.gridx = 2;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill  = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;

        JButton Button3 = new JButton("Announcements");
        Button3.setBackground(Colors.darkblue);
        Button3.setForeground(Colors.offwhite);
        Button3.setBorderPainted(false);
        Button3.setActionCommand("2");
        Button3.addActionListener(new HeaderListener());
        SubHeaderPanel.add(Button3,c);

        //Add Panel
    }

    public static void main(String[] args) throws IOException, FontFormatException {
        JFrame frame = new JFrame("BarangayLink");
        Header head1 = new Header();

        frame.add(head1.panel);
        frame.setSize(800,200);
        frame.setVisible(true);
    }

    static ImageIcon getImage(){
        ImageIcon originalIcon = new ImageIcon(ResidentInformationAdmin.class.getResource("/images/blankprofile.png"));

        // Resize the image to, for example, 100x100 pixels
        Image scaledImage = originalIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(scaledImage);

        return resizedIcon;
    }
}

