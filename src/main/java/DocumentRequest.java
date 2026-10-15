import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.ExecutionException;

public class DocumentRequest {
    static JFrame frame;
    static JPanel panel;
    static int Height = 600;
    static final int Width = 1200;
    static Font bigFont = FontHandler.getGIB(30);
    static Font smallFont = FontHandler.getGIR(25);
    static final int inset = 50;
    static final int labelinset = 20;
    static JPanel[] inboxpanel = new JPanel[3];
    static JTextField field;

    public DocumentRequest() throws ExecutionException, InterruptedException {
        panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Top, Left, Bottom, Right insets
        panel.setLayout(null);

        makeRow("Document Requested: ", Width/4, Width/5*3,20);

        //Save Button
        JButton savebutton = new JButton("SAVE");
        savebutton.setFont(smallFont);
        savebutton.setSize(Width/8, Height/12);
        savebutton.setBackground(Colors.blue);
        savebutton.setForeground(Color.WHITE);
        savebutton.setLocation((Width - savebutton.getWidth())/2, Height/8 + inset);
        savebutton.addActionListener(new SaveListener());
        panel.add(savebutton);

        //Inbox Label
        JLabel inboxlabel = new JLabel("INBOX");
        inboxlabel.setFont(bigFont);
        inboxlabel.setSize(Width/8, Height/12);
        inboxlabel.setLocation((Width/2) - 35, (Height/8 * 2) + inset);
        panel.add(inboxlabel);

        //Inbox Seperators
        JSeparator separator1 = new JSeparator(SwingConstants.HORIZONTAL);
        separator1.setSize((int)(500), 20);
        separator1.setLocation((Width/2) - 50 - (500), (Height/8 * 2) + inboxlabel.getHeight()/2 + inset );
        separator1.setBackground(Color.BLACK);
        separator1.setForeground(Color.BLACK);
        panel.add(separator1);

        JSeparator separator2 = new JSeparator(SwingConstants.HORIZONTAL);
        separator2.setSize((int)(500), 20);
        separator2.setLocation((Width/2) + 65, (Height/8 * 2) + inboxlabel.getHeight()/2 + inset );
        separator2.setBackground(Color.BLACK);
        separator2.setForeground(Color.BLACK);
        panel.add(separator2);

        //For Retrieval
        JLabel retrievallabel = new JLabel("FOR RETRIEVAL");
        retrievallabel.setFont(smallFont);
        retrievallabel.setSize(Width/3, Height/12);
        retrievallabel.setLocation(0,Height/4 + 90);
        retrievallabel.setHorizontalAlignment(JLabel.CENTER);
        panel.add(retrievallabel);


        //Pending
        JLabel pendinglabel = new JLabel("PENDING");
        pendinglabel.setFont(smallFont);
        pendinglabel.setSize(Width/3, Height/12);
        pendinglabel.setLocation(Width/3,Height/4 + 90);
        pendinglabel.setHorizontalAlignment(JLabel.CENTER);
        panel.add(pendinglabel);

        //Denied
        JLabel deniedlabel = new JLabel("DENIED");
        deniedlabel.setFont(smallFont);
        deniedlabel.setSize(Width/3, Height/12);
        deniedlabel.setLocation(Width/3*2,Height/4 + 90);
        deniedlabel.setHorizontalAlignment(JLabel.CENTER);
        panel.add(deniedlabel);

        //Seperator
        JSeparator separator3 = new JSeparator(SwingConstants.HORIZONTAL);
        separator3.setSize((int)(1120), 20);
        separator3.setLocation(50, Height/4 + 150 );
        separator3.setBackground(Color.BLACK);
        separator3.setForeground(Color.BLACK);
        panel.add(separator3);

        //retrievalscrollpane
        JPanel retrievalpanel = new JPanel();
        retrievalpanel.setLayout(new BoxLayout(retrievalpanel, BoxLayout.Y_AXIS));;
        JScrollPane scrollPane = new JScrollPane(retrievalpanel);
        scrollPane.setSize(Width/3, Height/12*5);
        scrollPane.setLocation(0,Height/4 + 160);
        scrollPane.setBorder(null);
        panel.add(scrollPane);
        inboxpanel[1] = retrievalpanel;

        //pending
        JPanel pendingpanel = new JPanel();
        pendingpanel.setLayout(new BoxLayout(pendingpanel, BoxLayout.Y_AXIS));;
        JScrollPane scrollpendingpanel = new JScrollPane(pendingpanel);
        scrollpendingpanel.setSize(Width/3, Height/12*5);
        scrollpendingpanel.setLocation(Width/3,Height/4 + 160);
        scrollpendingpanel.setBorder(null);
        panel.add(scrollpendingpanel);
        inboxpanel[0] = pendingpanel;

        //denied
        JPanel deniedpanel = new JPanel();
        deniedpanel.setLayout(new BoxLayout(deniedpanel, BoxLayout.Y_AXIS));;
        JScrollPane scrolldeniedpanel = new JScrollPane(deniedpanel);
        scrolldeniedpanel.setSize(Width/3, Height/12*5);
        scrolldeniedpanel.setLocation(Width/3 * 2,Height/4 + 160);
        scrolldeniedpanel.setBorder(null);
        panel.add(scrolldeniedpanel);
        inboxpanel[2] = deniedpanel;

        updateStatus(User.name);
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // Create a JFrame to hold the panel
        frame = new JFrame("Document Request");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(Width, Height);

        frame.add(new DocumentRequest().panel);
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
        panel.add(newfield);

        field = newfield;
    }

    static JLabel makeInbox(String name, int type) {
        JLabel newlabel = new JLabel(name);
        newlabel.setFont(smallFont);
        Dimension size = new Dimension(newlabel.getPreferredSize().width, Height / 8);
        newlabel.setPreferredSize(size);
        newlabel.setMinimumSize(size);
        newlabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Height / 8));
        newlabel.setHorizontalAlignment(SwingConstants.CENTER);
        return newlabel;
    }

    public class SaveListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String docname = field.getText();
            try {
                Firebase.addUserInbox(User.name,docname);
            } catch (ExecutionException ex) {
                throw new RuntimeException(ex);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            putStatus(docname,0);
        }
    }

    void putStatus(String docname, int status){
        inboxpanel[status].add(makeInbox(docname,status));
        inboxpanel[status].revalidate();
        inboxpanel[status].repaint();
    }

    void updateStatus(String name) throws ExecutionException, InterruptedException {
        QuerySnapshot query = Firebase.getUserInbox(name);
        for (DocumentSnapshot doc : query.getDocuments()) {
            String docsRequested = doc.getString("document");
            int status = doc.getLong("status").intValue();

            putStatus(docsRequested, status);
        }
    }
}
