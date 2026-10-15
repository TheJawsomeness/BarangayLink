import javax.swing.*;
import java.awt.*;

public class AnnouncementRenderer extends JPanel implements ListCellRenderer<Announcement>
{
    private final JLabel lblTitle = new JLabel();
    private final JTextArea txtDesc = new JTextArea();
    private final JButton btnEdit = new JButton("�");
    private final boolean isAdmin;

    public AnnouncementRenderer(boolean isAdmin)
    {
        this.isAdmin = isAdmin;
        setLayout(new BorderLayout(5, 5));
        lblTitle.setFont(lblTitle.getFont().deriveFont(Font.BOLD, 14f));
        txtDesc.setLineWrap(true);
        txtDesc.setWrapStyleWord(true);
        txtDesc.setEditable(false);

        JPanel top = new JPanel(new BorderLayout());
        top.add(lblTitle, BorderLayout.WEST);
        if (isAdmin)
        {
            btnEdit.setMargin(new Insets(2, 5, 2, 5));
            top.add(btnEdit, BorderLayout.EAST);
        }

        add(top, BorderLayout.NORTH);
        add(txtDesc, BorderLayout.CENTER);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Announcement> list,
                                                  Announcement value,
                                                  int index,
                                                  boolean isSelected,
                                                  boolean cellHasFocus)
                                                  {
        lblTitle.setText(value.getTitle());
        txtDesc.setText(value.getDescription());
        setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
        return this;
    }
}