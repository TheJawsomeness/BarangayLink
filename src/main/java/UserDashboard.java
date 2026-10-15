import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ExecutionException;

public class UserDashboard
{
    JScrollPane scrollPane;
    Font textfont = FontHandler.getGIR(14);
    Font headerfont = FontHandler.getGIB(20);
    GridBagConstraints gbc = new GridBagConstraints();
    JPanel announcementsPanel;

    public UserDashboard() throws ExecutionException, InterruptedException {
        // Announcements panel
        announcementsPanel = new JPanel(new GridBagLayout());
        gbc.insets = new Insets(10, 10, 10, 10);


        // Layout frame

        updateAnnouncements();
        scrollPane = new JScrollPane(announcementsPanel);
    }

    int row, column;
    private JPanel createAnnouncement(String title, String imgPath, String date, String desc)
    {
        column = column%2;

        gbc.gridx = column;
        gbc.gridy = row;
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(Colors.offwhite);
        panel.setForeground(Colors.offwhite);
        panel.setBorder(BorderFactory.createLineBorder(Colors.darkblue));
        panel.setPreferredSize(new Dimension(500, 200));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(headerfont);
        titleLabel.setOpaque(true);
        panel.add(titleLabel, BorderLayout.NORTH);

        JTextArea textArea = new JTextArea();
        textArea.setText((date != null ? date + "\n" : "") + desc);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        textArea.setBackground(Colors.darkblue);
        textArea.setForeground(Colors.offwhite);
        textArea.setFont(textfont);
        panel.add(textArea, BorderLayout.CENTER);

        announcementsPanel.add(panel, gbc);
        column = column + 1;
        if (column == 2)
            row = row + 1;

        return panel;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        JFrame frame = new JFrame("Document Request");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 600);

        frame.add(new UserDashboard().scrollPane);
        frame.setVisible(true);
    }

   void updateAnnouncements() throws ExecutionException, InterruptedException {
        QuerySnapshot query = Firebase.getAnnouncement();
        for (DocumentSnapshot doc : query.getDocuments()) {
            String docsRequested = doc.getString("document");
            String title = doc.getString("title");
            String date = doc.getString("date");
            String text = doc.getString("text");

            createAnnouncement(title,null,date,text);
        }
    }
}
