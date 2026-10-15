import com.google.cloud.firestore.DocumentSnapshot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class LoginForm implements ActionListener{
    JButton loginbutton,registerbutton;
    JTextField userfield;
    JPasswordField passfield;
    JFrame Login;
    static int attemps = 0;
    Firebase db = new Firebase();

    public LoginForm(){
        Login = new JFrame("Login");
        Login.setSize(600,200);
        Login.setLayout(null);
        Login.setLocationRelativeTo(null);

        JPanel mainpanel = new JPanel();
        mainpanel.setSize(600,200);
        mainpanel.setLayout(null);
        Login.add(mainpanel);

        //Username Field
        JLabel userlabel = new JLabel("Username: ");
        userlabel.setBounds(30,20,100,25);
        mainpanel.add(userlabel);
        userfield = new JTextField();
        userfield.setBounds(130,20,400,25);
        mainpanel.add(userfield);

        //Password Field
        JLabel passlabel = new JLabel("Password: ");
        passlabel.setBounds(30,60,100,25);
        mainpanel.add(passlabel);
        passfield = new JPasswordField();
        passfield.setBounds(130,60,400,25);
        mainpanel.add(passfield);


        //Register Button
        registerbutton = new JButton("Register");
        registerbutton.setBounds(30,120,100,25);
        mainpanel.add(registerbutton);
        registerbutton.addActionListener(this);

        //Login Button
        loginbutton = new JButton("Login");
        loginbutton.setBounds(450,120,100,25);
        mainpanel.add(loginbutton);
        loginbutton.addActionListener(this);

        Login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Login.setVisible(true);
    }

    public void actionPerformed(ActionEvent e){
        if(attemps < 3){
            if(e.getSource()==loginbutton){
                String name = userfield.getText();
                String pass = new String(passfield.getPassword());

                DocumentSnapshot user = Firebase.readUser(name);
                if(!(user == null) && user.getString("password").equals(pass)){ // Verification here
                    try {
                        User.current = user;
                        User.name = name;
                        MainMenu.main(new String[]{});
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    } catch (FontFormatException ex) {
                        throw new RuntimeException(ex);
                    } catch (ExecutionException ex) {
                        throw new RuntimeException(ex);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                else{
                    JOptionPane.showMessageDialog(null,"Incorrect Password.","Wrong Password",JOptionPane.WARNING_MESSAGE);
                }
            }
            else if(e.getSource()==registerbutton){
                Login.dispose();
                new Register();
            }
        }
        else
            JOptionPane.showMessageDialog(null,"Incorrect Password Inputted Too Many Times. Log-In is Locked.","Login Locked",JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String args[]){
        new LoginForm();
        System.out.println("LoginForm started");
    }
}

class Register implements ActionListener{
    JButton loginbutton,registerbutton;
    JFrame Register;
    JTextField userfield;
    JPasswordField passfield, passfield2;

    public Register(){
        Register = new JFrame("Register");
        Register.setSize(400,220);
        Register.setLayout(null);
        Register.setLocationRelativeTo(null);

        JPanel mainpanel = new JPanel();
        mainpanel.setSize(400,220);
        mainpanel.setLayout(null);
        Register.add(mainpanel);

        //Username Field
        JLabel userlabel = new JLabel("Username: ");
        userlabel.setBounds(30,20,150,25);
        mainpanel.add(userlabel);
        userfield = new JTextField();
        userfield.setBounds(150,20,220,25);
        mainpanel.add(userfield);

        //Password Field
        JLabel passlabel = new JLabel("Password: ");
        passlabel.setBounds(30,60,150,25);
        mainpanel.add(passlabel);
        passfield = new JPasswordField();
        passfield.setBounds(150,60,220,25);
        mainpanel.add(passfield);

        //Repeat Password Field
        JLabel passlabel2 = new JLabel("Repeat Password: ");
        passlabel2.setBounds(30,100,150,25);
        mainpanel.add(passlabel2);
        passfield2 = new JPasswordField();
        passfield2.setBounds(150,100,220,25);
        mainpanel.add(passfield2);

        //Register newAccount Button
        registerbutton = new JButton("Register");
        registerbutton.setBounds(250,150,100,25);
        mainpanel.add(registerbutton);
        registerbutton.addActionListener(this);

        //Login Button
        loginbutton = new JButton("Login");
        loginbutton.setBounds(30,150,100,25);
        mainpanel.add(loginbutton);
        loginbutton.addActionListener(this);
        Register.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Register.setVisible(true);
    }

    public void actionPerformed(ActionEvent e){
        if(e.getSource()==loginbutton){
            Register.dispose();
            new LoginForm();
        }
        else if(e.getSource()==registerbutton){
            String name = userfield.getText();
            String pass = new String(passfield.getPassword());
            String pass2 = new String(passfield2.getPassword());
            if(!(Firebase.readUser(name) == null)){ //Check if user already created
                JOptionPane.showMessageDialog(null,"Account Already Created. Please Log-In.","Account Already Exists",JOptionPane.WARNING_MESSAGE);
                Register.dispose();
                new LoginForm();
                return;
            }
            else{
                if(!pass.equals(pass2)){  // Check if password 2 doesntequal.
                    JOptionPane.showMessageDialog(null,"Password Doesnt Match.","Account Creation Failed",JOptionPane.WARNING_MESSAGE);
                }
                else{ //User Created
                    Firebase.writeUser(name,pass);
                    Register.dispose();
                    JOptionPane.showMessageDialog(null,"Account Created.","Account Created",JOptionPane.INFORMATION_MESSAGE);
                    new LoginForm();
                }
            }
        }
    }
}

