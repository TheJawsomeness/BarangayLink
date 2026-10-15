import javax.swing.*;
 import javax.swing.border.EmptyBorder;
 import java.awt.*;

 public class UserAnnouncementsGUI extends JFrame
 {
     private final DefaultListModel<Announcement> model = new DefaultListModel<>();
     private final JList<Announcement> list = new JList<>(model);

     public UserAnnouncementsGUI()
     {
         super("Announcements");
         setDefaultCloseOperation(EXIT_ON_CLOSE);
         setSize(600, 400);
         setLocationRelativeTo(null);

         JPanel contentPane = new JPanel(new BorderLayout(10, 10));
         contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
         setContentPane(contentPane);

         list.setCellRenderer(new AnnouncementRenderer(false));
         contentPane.add(new JScrollPane(list), BorderLayout.CENTER);

         // Sample data
         model.addElement(new Announcement("Free Health Checkup", "May 20, 2025 � don�t miss it!"));
         model.addElement(new Announcement("Basketball Championship 2025", "Local teams battling it out."));
         model.addElement(new Announcement("Weekly Zumba", "Fridays at 5 PM in the covered court."));
     }

     public static void main(String[] args)
     {
         SwingUtilities.invokeLater(() -> new UserAnnouncementsGUI().setVisible(true));
     }
}