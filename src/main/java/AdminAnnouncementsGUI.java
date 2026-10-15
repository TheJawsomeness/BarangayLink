import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AdminAnnouncementsGUI extends JFrame
{
    private final DefaultListModel<Announcement> model = new DefaultListModel<>();
    private final JList<Announcement> list = new JList<>(model);

    public AdminAnnouncementsGUI()
    {
        super("Announcements - Admin");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        JPanel contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);

        list.setCellRenderer(new AnnouncementRenderer(true));
        contentPane.add(new JScrollPane(list), BorderLayout.CENTER);

        JButton btnAdd = new JButton("Add New Announcement");
        btnAdd.addActionListener(e -> addAnnouncement());
        contentPane.add(btnAdd, BorderLayout.NORTH);

        // Sample data
        model.addElement(new Announcement("Free Health Checkup", "May 20, 2025. Bring your relatives!"));
        model.addElement(new Announcement("Basketball Championship 2025", "Join teams from all barangays."));
        model.addElement(new Announcement("Weekly Zumba", "Every Friday at 5 PM in the covered court."));
    }

    private void addAnnouncement()
    {
        String title = JOptionPane.showInputDialog(this, "Title:");
        if (title == null || title.trim().isEmpty()) return;
        String desc = JOptionPane.showInputDialog(this, "Description:");
        if (desc == null) desc = "";
        model.addElement(new Announcement(title, desc));
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() -> new AdminAnnouncementsGUI().setVisible(true));
    }
}