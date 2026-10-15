import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class MainMenu {
    boolean isadmin = true;
    static JPanel headerpanel;
    static JScrollPane residentinformationadmin;
    static JComponent[] panels = new JComponent[6];

    public static void main(String[] args) throws IOException, FontFormatException, ExecutionException, InterruptedException {
        JFrame frame = new JFrame();
        frame.setSize(1210, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        frame.add(panel);
        panel.setLayout(null);

        //Header
        headerpanel = new Header().panel;
        headerpanel.setBounds(0, 0, 1200, 200);
        panel.add(headerpanel);


        //Resident Admin
        JComponent residentinformationadmin = new ResidentInformationAdmin().gridPanel;
        residentinformationadmin.setBounds(0,200,1200,600);
        panel.add(residentinformationadmin);
        panels[3] = residentinformationadmin;

        //Resident
        JComponent residentinformation = new ResidentInformation().panel;
        residentinformation.setBounds(0,200,1200,600);
        panel.add(residentinformation);
        panels[0] = residentinformation;

        //DocumentRequest
        JComponent documentrequest = new DocumentRequest().panel;
        documentrequest.setBounds(0,200,1200,600);
        panel.add(documentrequest);
        panels[1] = documentrequest;

        //Annoucement
        JComponent announcement = new UserDashboard().scrollPane;
        announcement.setBounds(0,200,1200,600);
        panel.add(announcement);
        panels[2] = announcement;

        //AnnoucementAdmin
        JComponent announcementadmin = new AdminDashboard().scrollPane;
        announcementadmin.setBounds(0,200,1200,600);
        panel.add(announcementadmin);
        panels[5] = announcementadmin;

        //InboxAdmin
        JPanel inboxadmin = new DocumentAdmin().panel;
        inboxadmin.setBounds(0,200,1200,600);
        panel.add(inboxadmin);
        panels[4] = inboxadmin;

        MainMenu.setPage(0);
        frame.setVisible(true);
    }

    public static void setPage(int pagenum){
        if (User.current.getBoolean("admin"))
            pagenum = pagenum + 3;
        for(int i=0; i<panels.length; i++){
            System.out.println(i);
            panels[i].setVisible(i == pagenum);
        }
    }
}
