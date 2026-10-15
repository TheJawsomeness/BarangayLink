import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HeaderListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
                String command = e.getActionCommand();  // This returns a String
                int number = 0;
                try {
                        number = Integer.parseInt(command); // convert String to int
                } catch (NumberFormatException ex) {
                        System.out.println("Invalid number: " + command);
                }

                MainMenu.setPage(number);
        }
}
