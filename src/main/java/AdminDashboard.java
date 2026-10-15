import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class AdminDashboard extends JFrame
{
    JScrollPane scrollPane;
    Font textfont = FontHandler.getGIR(14);
    Font headerfont = FontHandler.getGIB(20);
    Font addfont = FontHandler.getGIB(70);
    JPanel announcementsPanel;
    ArrayList<JPanel> announcement = new ArrayList<>();
    ArrayList<DocumentSnapshot> announcements = new ArrayList<>();
    int i;

    public AdminDashboard() throws ExecutionException, InterruptedException {
        setTitle("BARANGAYLINK - Admin");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 600);
        setLocationRelativeTo(null);


        // Announcement panel
        announcementsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Add new announcement button
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        JButton addButton = new JButton("+");
        addButton.setFont(addfont);
        addButton.setBackground(Colors.blue);
        addButton.setForeground(Colors.offwhite);
        addButton.setVerticalAlignment(SwingConstants.CENTER);
        addButton.setSize(80,80);
        addButton.setToolTipText("Add New Announcement");
        addButton.addActionListener(new NewListener());
        announcementsPanel.add(addButton, gbc);

        scrollPane = new JScrollPane(announcementsPanel);
        add(scrollPane);
        updateAnnouncements();
    }

    private JPanel createAnnouncement(String title, String imgPath, String date, String desc, int index)
    {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(Colors.offwhite);
        panel.setBorder(BorderFactory.createLineBorder(Colors.darkblue));
        panel.setPreferredSize(new Dimension(400, 200));

        // Title and menu button
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(true);
        top.setBackground(Colors.blue);
        JLabel titleLabel = new JLabel(title);
        titleLabel.setOpaque(true);
        titleLabel.setBackground(Colors.blue);
        titleLabel.setForeground(Colors.offwhite);
        titleLabel.setFont(headerfont);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JButton menuBtn = new JButton("...");
        menuBtn.setOpaque(true);
        menuBtn.setBackground(Colors.blue);
        menuBtn.setForeground(Colors.offwhite);
        menuBtn.setPreferredSize(new Dimension(40, 20));
        menuBtn.setActionCommand(""+index);
        menuBtn.addActionListener(new EditListener());
        top.add(titleLabel, BorderLayout.WEST);
        top.add(menuBtn, BorderLayout.EAST);
        panel.add(top, BorderLayout.NORTH);

        // Description
        JTextArea textArea = new JTextArea();
        textArea.setText((date != null ? date + "\n" : "") + desc);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(textfont);
        textArea.setBackground(Colors.darkblue);
        textArea.setBackground(Colors.offwhite);
        textArea.setEditable(false);
        panel.add(textArea, BorderLayout.CENTER);

        if (index < announcement.size()) {
            announcement.set(index, panel);
        } else {
            announcement.add(panel);
        }
        announcementsPanel.add(panel);
        announcementsPanel.revalidate();
        announcementsPanel.repaint();


        return panel;
    }
    void updateAnnouncements() throws ExecutionException, InterruptedException {
        QuerySnapshot query = Firebase.getAnnouncement();
        announcements.clear();
        announcement.clear();
        i = 0;
        for (DocumentSnapshot doc : query.getDocuments()) {
            String docsRequested = doc.getString("document");
            String title = doc.getString("title");
            String date = doc.getString("date");
            String text = doc.getString("body");
            createAnnouncement(title, null, date, text, i);
            announcements.add(doc);
            i++;
        }
    }

    void showPopup(JFrame parent){
        showPopup(parent,"Title","Date","Body", false, i);
    }

    void showPopup(JFrame parent, String title, String date, String body, Boolean overwrite, int index) {
        JDialog dialog = new JDialog(parent, "Enter Details", true);
        dialog.setSize(300, 400);
        dialog.setLocationRelativeTo(parent);
        dialog.setLayout(new BorderLayout());

        // Panel for text fields
        JPanel inputPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        JTextField field1 = new JTextField();
        JTextField field2 = new JTextField();
        JTextArea field3 = new JTextArea();

        field1.setText(title);
        field2.setText(date);
        field3.setText(body);
        field3.setPreferredSize(new Dimension(300,200));

        inputPanel.add(field1);
        inputPanel.add(field2);
        inputPanel.add(field3);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Save button
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            System.out.println(index);
            saveAnnouncement(field1.getText(),field2.getText(),field3.getText(),index,overwrite);
        });

        // Bottom panel for button
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);

        dialog.add(inputPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    void saveAnnouncement(String title, String date, String text, int index, boolean overwrite){
        if (overwrite) {
            announcement.get(index).setVisible(false);
            createAnnouncement(title, null, date, text, index);
            Firebase.updateAnnouncement(title, date, text, overwrite, announcements.get(index).getId());
        } else {
            createAnnouncement(title, null, date, text, index);
            DocumentReference newannouncement = Firebase.updateAnnouncement(title, date, text, overwrite, null);
            ApiFuture<DocumentSnapshot> future = newannouncement.get();

            try {
                DocumentSnapshot document = future.get();
                announcements.add(document);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            i++;
        }
    }

    public class NewListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            showPopup(null);
        }
    }

    public class EditListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int num = Integer.parseInt(e.getActionCommand());

            String title = announcements.get(num).getString("title");
            String date = announcements.get(num).getString("date");
            String text = announcements.get(num).getString("text");

            showPopup(null, title, date, text, true, num);
        }
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() -> {
            try {
                new AdminDashboard().setVisible(true);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
