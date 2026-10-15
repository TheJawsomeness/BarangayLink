import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.ExecutionException;

public class ResidentInformationAdmin {

    static final int HEIGHT_DIVISOR = 4; // 1/3 of the frame height
    static final int COLUMN_COUNT = 3;
    static JPanel gridPanel;
    static JScrollPane scrollPane;
    static int row = 0;
    static int column = 0;
    static Font textfont = FontHandler.getGIR(15);;

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        JFrame frame = new JFrame("Fixed Height Grid");
        frame.setSize(1200, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(new ResidentInformationAdmin().gridPanel);

        frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                resizeComponents(frame.getContentPane().getHeight());
            }
        });

        frame.setVisible(true);
        resizeComponents(frame.getContentPane().getHeight());
    }

    public ResidentInformationAdmin() throws ExecutionException, InterruptedException {
        gridPanel = new JPanel(new GridBagLayout());
        scrollPane = new JScrollPane(gridPanel);
        updateAllUsers();
    }

    static void updateAllUsers() throws ExecutionException, InterruptedException {
            QuerySnapshot query = Firebase.getAllUsers();
            for (DocumentSnapshot doc : query.getDocuments()) {
                String name = doc.getString("name");
                String age = doc.getString("age");
                String address = doc.getString("address");
                addComponent(name,age,address);
            }
    }

    static void addComponent(String name, String age, String address) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.weightx = 1;
        gbc.weighty = 0;

        gbc.gridx = column % COLUMN_COUNT;
        gbc.gridy = column / COLUMN_COUNT;

        gridPanel.add(ResidentInfo(name,age,address), gbc);

        gridPanel.setBackground(Colors.offwhite);
        column++;
    }

    static void resizeComponents(int frameHeight) {
        int targetHeight = frameHeight / HEIGHT_DIVISOR - 20; // subtract padding

        for (Component comp : gridPanel.getComponents()) {
            comp.setPreferredSize(new Dimension(0, targetHeight));
        }

        gridPanel.revalidate();
    }

    static JButton ResidentInfo(String name, String age, String address){
        JButton button = new JButton();
        button.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        //Image Label at side
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 2;
        c.fill  = GridBagConstraints.BOTH;
        c.weightx = 0.2;
        c.weighty = 1;

        button.add(getImage(),c);

        //Name Label
        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill  = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;

        JLabel namelabel = new JLabel("Name: " + name);
        namelabel.setFont(textfont);
        button.add(namelabel,c);

        //address Label
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill  = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;

        JLabel addresslabel = new JLabel("Address: " + address);
        addresslabel.setFont(textfont);
        button.add(addresslabel,c);


        button.setBackground(Colors.white);
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
        button.setFont(textfont);

        return button;
    }

    static JLabel getImage(){
        ImageIcon originalIcon = new ImageIcon(ResidentInformationAdmin.class.getResource("/images/blankprofile.png"));

    // Resize the image to, for example, 100x100 pixels
        Image scaledImage = originalIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(scaledImage);

    // Create a label with the resized image
        JLabel imageLabel = new JLabel(resizedIcon);
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        return imageLabel;
    }
}
