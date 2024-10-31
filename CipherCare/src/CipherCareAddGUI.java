import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CipherCareAddGUI {

    private String username;
    private String password;
    private JFrame frame;
    private JTextField dobField;
    private JTextField phoneField;
    private JTextField addressField;
    private JTextField emailField;
    private JTextField prescriptionField;
    private JTextField conditionField;
    private GridBagConstraints gbc;

    public CipherCareAddGUI(String username, String password, String table) {
        frame = new JFrame("Healthcare Database");
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null);
        this.username = username;
        this.password = password;
        frame.setLayout(new GridBagLayout());
        initializeUI(table);
        frame.setVisible(true);
    }

    private void initializeUI(String table){
        GridBagConstraints constraints = new GridBagConstraints();
        JButton commitB = new JButton("Add Data");

        if (table.equals("patient") || table.equals("medicalrecord")){
            JLabel emailLabel = new JLabel("Patient Email:");
            emailField = new JTextField();
            JLabel phoneLabel = new JLabel("Patient Phone Number:");
            phoneField = new JTextField();
            JLabel dobLabel = new JLabel("Patient Date of Birth:");
            dobField = new JTextField();
            JLabel addressLabel = new JLabel("Patient Address:");
            addressField = new JTextField();
            JLabel prescriptionLabel = new JLabel("Current Patient Prescriptions:");
            prescriptionField = new JTextField();
            JLabel conditionLabel = new JLabel("Current Patient Medical Conditions:");
            conditionField = new JTextField();
        
            constraints.fill = GridBagConstraints.HORIZONTAL;  
            constraints.gridx = 0;  
            constraints.gridy = 0;
            frame.add(dobLabel, constraints);
            constraints.gridx = 1;  
            constraints.gridy = 0;
            frame.add(addressLabel, constraints);
            constraints.fill = GridBagConstraints.HORIZONTAL;  
            constraints.gridx = 0;  
            constraints.gridy = 1;
            frame.add(dobField, constraints);
            constraints.gridx = 1;  
            constraints.gridy = 1;
            frame.add(addressField, constraints);
            constraints.fill = GridBagConstraints.HORIZONTAL;  
            constraints.gridx = 0;  
            constraints.gridy = 2;
            frame.add(emailLabel, constraints);
            constraints.gridx = 1;  
            constraints.gridy = 2;
            frame.add(phoneLabel, constraints);
            constraints.fill = GridBagConstraints.HORIZONTAL;  
            constraints.gridx = 0;  
            constraints.gridy = 3;
            frame.add(emailField, constraints);
            constraints.gridx = 1;  
            constraints.gridy = 3;
            frame.add(phoneField, constraints);
            constraints.fill = GridBagConstraints.HORIZONTAL;  
            constraints.gridx = 0;  
            constraints.gridy = 4;
            frame.add(conditionLabel, constraints);
            constraints.gridx = 1;  
            constraints.gridy = 4;
            frame.add(prescriptionLabel, constraints);
            constraints.fill = GridBagConstraints.HORIZONTAL;  
            constraints.gridx = 0;  
            constraints.gridy = 5;
            frame.add(conditionField, constraints);
            constraints.gridx = 1;  
            constraints.gridy = 5;
            frame.add(prescriptionField, constraints);
            constraints.fill = GridBagConstraints.HORIZONTAL;  
            constraints.gridx = 0;  
            constraints.gridy = 6;
            constraints.gridwidth = 2;
            frame.add(commitB, constraints);
        }
        else if(table.equals("appointment")){

        }

        commitB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                add();
            }
        });
        
    }

    private void add(){
        String dob = dobField.getText();
        String address = addressField.getText();
        String phone = phoneField.getText();
        String email = emailField.getText();
        String condition = conditionField.getText();
        String prescription = prescriptionField.getText();
        if(dob.equals("") || address.equals("") || (phone.equals("") && email.equals(""))){

        }
        else{

        }
    }

}
