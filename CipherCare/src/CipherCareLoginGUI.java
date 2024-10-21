import javax.swing.*;
import java.awt.event.*;

public class CipherCareLoginGUI{

    public CipherCareLoginGUI() {
        JFrame frame = new JFrame("Database Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        initializeUI(frame);
        frame.setLayout(null);
        frame.setVisible(true);
    }

    private void initializeUI(JFrame frame) {
        JLabel message = new JLabel("Please enter your username and password");
        message.setBounds(50,50,300,35);
        JLabel userLabel = new JLabel("Username");
        userLabel.setBounds(50,100,150,30);
        JTextField usernameField = new JTextField();
        usernameField.setBounds(150,100,150,30);
        JLabel passLabel = new JLabel("Password");
        passLabel.setBounds(50,150,150,30);
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(150,150,150,30);
        JButton loginButton = new JButton("Log In");
        loginButton.setBounds(150,200,95,30);  
        frame.add(message);
        frame.add(userLabel);
        frame.add(usernameField);
        frame.add(passLabel);
        frame.add(passwordField);
        frame.add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean isValid = CipherCareSQL.testConnection(usernameField.getText(), passwordField.getPassword());
                if(isValid){
                    String password = "";
                    for(char c: passwordField.getPassword()){
                        password += c;
                    }
                    new CipherCareMainGUI(usernameField.getText(), password);
                    frame.dispose();
                }
                else{
                    message.setText("ERROR: INVALID USERNAME AND PASSWORD");
                }
            }
        });
    }

}
