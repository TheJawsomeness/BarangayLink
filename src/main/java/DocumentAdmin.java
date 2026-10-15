import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class DocumentAdmin {
    JPanel panel;
    JPanel docspanel;
    JScrollPane scrolldocspanel;
    Font headerfont = FontHandler.getGIB(30);
    Font textfont = FontHandler.getGIR(25);
    int totalreq = 0;
    List<QueryDocumentSnapshot> requests;
    Map<String, List<QueryDocumentSnapshot>> userRequestsMap;
    JPanel requestspanel;



    public DocumentAdmin() throws ExecutionException, InterruptedException {
        panel = new JPanel();
        panel.setLayout(null);

        //Headers
        JPanel headerpanel = new JPanel();
        headerpanel.setLayout(new GridBagLayout());
        headerpanel.setBackground(Colors.offwhite);
        headerpanel.setBounds(0,25,1180,75);
        panel.add(headerpanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;

        //Docreq
        headerpanel.add(makelabel("Document Requested"),gbc);
        gbc.gridx++;

        headerpanel.add(makelabel("User"),gbc);
        gbc.gridx++;

        gbc.weightx = 0.2;
        headerpanel.add(makelabel("Approve"),gbc);
        gbc.gridx++;

        headerpanel.add(makelabel("Deny"),gbc);
        gbc.gridx++;

        //Docs panel
        docspanel = new JPanel();
        docspanel.setLayout(new GridBagLayout());

        scrolldocspanel = new JScrollPane(docspanel);
        scrolldocspanel.setBounds(0,100,1180,450);
        panel.add(scrolldocspanel);

        updateDocuments();
    }

    void redrawpanel() {
        panel.remove(scrolldocspanel);
        totalreq = 0;

        docspanel = new JPanel();
        docspanel.setLayout(new GridBagLayout());

        scrolldocspanel = new JScrollPane(docspanel);
        scrolldocspanel.setBounds(0, 100, 1180, 450);
        panel.add(scrolldocspanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        for (Map.Entry<String, List<QueryDocumentSnapshot>> entry : userRequestsMap.entrySet()) {
            String userId = entry.getKey();
            List<QueryDocumentSnapshot> requests = entry.getValue();

            System.out.println("UserInbox of "+userId + " Checked.");
            for (QueryDocumentSnapshot reqDoc : requests) {
                if (reqDoc.getLong("status").intValue() == 0) {
                    addNewReq(reqDoc, userId);
                    totalreq++;
                }
            }
        }

        panel.revalidate();
        panel.repaint();
    }



    JLabel makelabel(String label){
        JLabel label1 = new JLabel(label);
        label1.setFont(headerfont);
        label1.setHorizontalAlignment(SwingConstants.CENTER);
        return label1;
    }

    void addNewReq(QueryDocumentSnapshot docs, String name){
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = totalreq;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        //Docreq
        JLabel label1 = new JLabel(docs.getString("document"));
        label1.setFont(textfont);
        label1.setHorizontalAlignment(SwingConstants.CENTER);
        docspanel.add(label1,gbc);
        gbc.gridx++;

        JLabel label2 = new JLabel(name);
        label2.setFont(textfont);
        label2.setHorizontalAlignment(SwingConstants.CENTER);
        docspanel.add(label2,gbc);
        gbc.gridx++;

        gbc.weightx = 0.2;
        docspanel.add(makebutton("✔",name,docs),gbc);
        gbc.gridx++;

        docspanel.add(makebutton("✗",name,docs),gbc);
        gbc.gridx++;

        totalreq++;
    }

    JButton makebutton(String label, String userId, DocumentSnapshot docs){
        JButton button = new JButton(label);
        button.setFont(new Font("Monospaced", Font.PLAIN, 30));
        button.setBackground(Colors.blue);
        button.setForeground(Colors.white);
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setVerticalAlignment(SwingConstants.CENTER);

        int action = 2;
        if(label.equals("✔")){
            action = 1;
        }

        button.setActionCommand(userId+";"+docs.getId()+";"+action);  // store userId here
        button.addActionListener(new DocListener());

        return button;
    }

    public class DocListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String[] parts = e.getActionCommand().split(";");
            String uId = parts[0];
            String rId = parts[1];
            String action = parts[2];

            System.out.println("Remove clicked for user: " + uId + ", request: " + rId);
            removeUserRequest(uId, rId);
            try {
                Firebase.updateRequest(uId,rId,Integer.parseInt(action));
            } catch (ExecutionException ex) {
                throw new RuntimeException(ex);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }

        }
    }

    void removeUserRequest(String id, String docid){
        System.out.println("Remove user requests: " + id);

        List<QueryDocumentSnapshot> userRequests = userRequestsMap.get(id);
        if (userRequests != null) {
            userRequests.removeIf(req -> req.getId().equals(docid));
        }
        redrawpanel();
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(1200,600));

        //Panel
        frame.add(new DocumentAdmin().panel);
        frame.setVisible(true);
    }

    void updateDocuments() throws ExecutionException, InterruptedException {
        userRequestsMap = new HashMap<>();

        QuerySnapshot querySnapshot = Firebase.getAllInbox();
        System.out.println("Total users found in Inbox: " + querySnapshot.size());

        for (DocumentSnapshot userDoc : querySnapshot.getDocuments()) {
            String userId = userDoc.getId();

            CollectionReference requestSubCol = Firebase.db
                    .collection("Inbox")
                    .document(userId)
                    .collection("Request");

            ApiFuture<QuerySnapshot> requestsFuture = requestSubCol.get();
            List<QueryDocumentSnapshot> userRequests = new ArrayList<>(requestsFuture.get().getDocuments());

            userRequestsMap.put(userId, userRequests);
            System.out.println("Total users fetched: " + userRequestsMap.size());
        }

        // Now redrawpanel can accept this map and display accordingly
        redrawpanel();
    }
}