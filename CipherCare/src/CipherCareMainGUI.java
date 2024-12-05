import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

public class CipherCareMainGUI {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel model;

    public CipherCareMainGUI(String username, String password) {

        // Set up the frame
        frame = new JFrame("CipherCare - Main");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        initializeUI();
        frame.setVisible(true);
    }

    private void initializeUI() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Add Patient Button
        JButton addPatientButton = new JButton("Add Patient");
        addPatientButton.addActionListener(e -> new CipherCareAddGUI());

        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(addPatientButton, gbc);

        // View Patients Button
        JButton viewPatientsButton = new JButton("View Patients");
        viewPatientsButton.addActionListener(e -> viewPatients());

        gbc.gridy = 1;
        mainPanel.add(viewPatientsButton, gbc);

        // Edit Patient Button
        JButton editPatientButton = new JButton("Edit Patient");
        editPatientButton.addActionListener(e -> editPatient());

        gbc.gridy = 2;
        mainPanel.add(editPatientButton, gbc);

        // Delete Patient Button
        JButton deletePatientButton = new JButton("Delete Patient");
        deletePatientButton.addActionListener(e -> deletePatient());

        gbc.gridy = 3;
        mainPanel.add(deletePatientButton, gbc);

        // Search Patient Button
        JButton searchPatientButton = new JButton("Search Patient");
        searchPatientButton.addActionListener(e -> searchPatient());

        gbc.gridy = 4;
        mainPanel.add(searchPatientButton, gbc);

        // Manage Appointments Button
        JButton manageAppointmentsButton = new JButton("Manage Appointments");
        manageAppointmentsButton.addActionListener(e -> new CipherCareAppointmentGUI());

        gbc.gridy = 5;
        mainPanel.add(manageAppointmentsButton, gbc);

        frame.add(mainPanel, BorderLayout.WEST);

        // Table to display patients
        model = new DefaultTableModel();
        String[] columnNames = {"Patient ID", "Date of Birth", "Address", "Email", "Phone Number"};
        model.setColumnIdentifiers(columnNames);
        table = new JTable(model){
            public boolean isCellEditable(int row, int column) {                
                return false;               
            };
        };
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);


        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    openPatientProfile(table.getSelectedRow());
                }
            }
        });

    }
    private void openPatientProfile(int selectedRow) {
        try {
            // Retrieve the data for the selected row
            Object patientID = model.getValueAt(selectedRow, 0);
            Object dob = model.getValueAt(selectedRow, 1);
            Object address = model.getValueAt(selectedRow, 2);
            Object email = model.getValueAt(selectedRow, 3);
            Object phone = model.getValueAt(selectedRow, 4);

            // Fetch the report for this patient from the database
            String medicalReport = "";
            String prescription = "";
            String medicalCondition = "";
            try{
                Connection connection = CipherCareSQL.getConnection();
                String query = "SELECT medicalReport, prescription, medicalCondition FROM medicalrecord WHERE patientID = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, Integer.parseInt(patientID.toString()));
                ResultSet resultSet = preparedStatement.executeQuery();
                resultSet.next();
                medicalReport = resultSet.getString("medicalReport");
                prescription = resultSet.getString("prescription");
                medicalCondition = resultSet.getString("medicalCondition");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, "Error retrieving patient report: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }

            // Create the profile window
            JFrame profileFrame = new JFrame("Electronic Medical Record");
            profileFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            profileFrame.setSize(frame.getSize());
            profileFrame.setLocation(frame.getLocation());

            // Use BorderLayout for the frame
            profileFrame.setLayout(new BorderLayout());

            // Create a panel for the patient details
            JPanel detailsPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5); // Small padding
            gbc.anchor = GridBagConstraints.NORTHWEST; // Align to the top-left corner

            // Add labels and values
            gbc.gridx = 1;
            gbc.gridy = 0;
            detailsPanel.add(new JLabel("Patient ID:"), gbc);
            gbc.gridx = 2;
            detailsPanel.add(new JLabel(String.valueOf(patientID)), gbc);

            gbc.gridx = 1;
            gbc.gridy++;
            detailsPanel.add(new JLabel("Date of Birth:"), gbc);
            gbc.gridx = 2;
            detailsPanel.add(new JLabel(dob.toString()), gbc);

            gbc.gridx = 1;
            gbc.gridy++;
            detailsPanel.add(new JLabel("Address:"), gbc);
            gbc.gridx = 2;
            detailsPanel.add(new JLabel(address.toString()), gbc);

            gbc.gridx = 1;
            gbc.gridy++;
            detailsPanel.add(new JLabel("Email:"), gbc);
            gbc.gridx = 2;
            detailsPanel.add(new JLabel(email.toString()), gbc);

            gbc.gridx = 1;
            gbc.gridy++;
            detailsPanel.add(new JLabel("Phone Number:"), gbc);
            gbc.gridx = 2;
            detailsPanel.add(new JLabel(phone.toString()), gbc);

            gbc.gridx = 0;
            gbc.gridy += 2;
            detailsPanel.add(new JLabel("Current Prescriptions:"), gbc);
            gbc.gridx = 2;
            detailsPanel.add(new JLabel("Current Medical Conditions:"), gbc);

            JTextField presciptionField = new JTextField(prescription, 30);
            JTextField conditionField = new JTextField(medicalCondition, 30);

            gbc.gridx = 0;
            gbc.gridy++;
            detailsPanel.add(presciptionField, gbc);
            gbc.gridx = 2;
            detailsPanel.add(conditionField, gbc);

            // Add detailsPanel to the top of the frame
            profileFrame.add(detailsPanel, BorderLayout.NORTH);

            // Create a panel for the text area with a title
            JPanel reportPanel = new JPanel(new BorderLayout());
            JLabel reportTitle = new JLabel("Medical Reports");
            reportTitle.setHorizontalAlignment(SwingConstants.LEFT); // Align the title to the left
            reportTitle.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Add some padding

            // Add the text area for patient report
            JTextArea reportTextArea = new JTextArea(10, 40); // 10 rows, 40 columns
            reportTextArea.setLineWrap(true);
            reportTextArea.setWrapStyleWord(true);
            reportTextArea.setText(medicalReport); // Set the fetched report
            JScrollPane scrollPane = new JScrollPane(reportTextArea); // Add scrolling support

            // Add title and text area to the report panel
            reportPanel.add(reportTitle, BorderLayout.NORTH);
            reportPanel.add(scrollPane, BorderLayout.CENTER);

            // Add the report panel to the frame
            profileFrame.add(reportPanel, BorderLayout.CENTER);


            JButton saveButton = new JButton("Save");
            saveButton.setPreferredSize(new Dimension(80, 30)); // Small button dimensions
            saveButton.addActionListener(e -> {
                String report = reportTextArea.getText();
                try{
                    Connection connection = CipherCareSQL.getConnection();
                    String query = "UPDATE medicalrecord SET medicalReport = ?, prescription = ?, medicalCondition = ? WHERE patientID = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, report); 
                    preparedStatement.setString(2, presciptionField.getText()); 
                    preparedStatement.setString(3, conditionField.getText()); 
                    preparedStatement.setInt(4, Integer.parseInt(patientID.toString())); // Set the patient ID
                    int rowsUpdated = preparedStatement.executeUpdate();
                    if (rowsUpdated > 0) {
                        JOptionPane.showMessageDialog(profileFrame, "Report saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        profileFrame.dispose();
                    } else {
                        JOptionPane.showMessageDialog(profileFrame, "Failed to save report.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(profileFrame, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            });

            // Add Save button to a smaller JPanel at the bottom
            JPanel buttonPanel = new JPanel();
            buttonPanel.add(saveButton); // Center the button in the panel
            profileFrame.add(buttonPanel, BorderLayout.SOUTH);

            // Display the profile window
            profileFrame.setVisible(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame,
                    "An error occurred while opening the patient profile.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }





    private void viewPatients() {
        model.setRowCount(0); // Clear existing rows

        try (Connection connection = CipherCareSQL.getConnection()) {
            String query = "SELECT patientID, PatientDoB, patientAddress, patientEmail, patientPhone FROM Patient";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    int id = resultSet.getInt("patientID");
                    String dob = CipherCareEncryption.decrypt(resultSet.getString("PatientDoB"));
                    String address = CipherCareEncryption.decrypt(resultSet.getString("patientAddress"));
                    String email = CipherCareEncryption.decrypt(resultSet.getString("patientEmail"));
                    String phone = CipherCareEncryption.decrypt(resultSet.getString("patientPhone"));

                    // Log the retrieved row for debugging
                    //System.out.println("Row: " + id + ", " + dob + ", " + address + ", " + email + ", " + phone);

                    model.addRow(new Object[]{id, dob, address, email, phone});
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Failed to retrieve patient records: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    private void editPatient() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a patient to edit.", "Edit Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int patientID = (int) model.getValueAt(selectedRow, 0);
        String dob = (String) model.getValueAt(selectedRow, 1);
        String address = (String) model.getValueAt(selectedRow, 2);
        String email = (String) model.getValueAt(selectedRow, 3);
        String phone = (String) model.getValueAt(selectedRow, 4);

        JTextField dobField = new JTextField(dob);
        JTextField addressField = new JTextField(address);
        JTextField emailField = new JTextField(email);
        JTextField phoneField = new JTextField(phone);

        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(new JLabel("Date of Birth:"));
        panel.add(dobField);
        panel.add(new JLabel("Address:"));
        panel.add(addressField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Phone Number:"));
        panel.add(phoneField);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Edit Patient", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try (Connection connection = CipherCareSQL.getConnection()) {
                String newDob = CipherCareEncryption.encrypt(dobField.getText().trim());
                String newAddress = CipherCareEncryption.encrypt(addressField.getText().trim());
                String newEmail = CipherCareEncryption.encrypt(emailField.getText().trim());
                String newPhone = CipherCareEncryption.encrypt(phoneField.getText().trim());
                String query = "UPDATE Patient SET PatientDoB = ?, patientAddress = ?, patientEmail = ?, patientPhone = ? WHERE patientID = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, newDob);
                    preparedStatement.setString(2, newAddress);
                    preparedStatement.setString(3, newEmail);
                    preparedStatement.setString(4, newPhone);
                    preparedStatement.setInt(5, patientID);
                    preparedStatement.executeUpdate();
                    JOptionPane.showMessageDialog(frame, "Patient updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    viewPatients(); // Refresh the patient list
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, "Failed to update patient: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private void deletePatient() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a patient to delete.", "Delete Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int patientID = (int) model.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this patient?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection connection = CipherCareSQL.getConnection()) {
                String query = "DELETE FROM Patient WHERE patientID = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setInt(1, patientID);
                    preparedStatement.executeUpdate();
                    model.removeRow(selectedRow);
                    JOptionPane.showMessageDialog(frame, "Patient deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(frame, "Failed to delete patient: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private void searchPatient() {
        String options[] = {"All Columns", "patientID", "PatientDoB", "patientAddress", "patientEmail", "patientPhone"};
        JComboBox<String> searchby = new JComboBox<String>(options);
        Object[] message = {
            "Select Search Criteria:", searchby,
            "Enter patient information to search:"
        };
        String searchTerm = JOptionPane.showInputDialog(frame, message, "Search Patient", JOptionPane.QUESTION_MESSAGE);
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return;
        }

        model.setRowCount(0); // Clear existing rows

        try{
            Connection connection = CipherCareSQL.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Patient");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("patientID");
                String dob = CipherCareEncryption.decrypt(resultSet.getString("PatientDoB"));
                String address = CipherCareEncryption.decrypt(resultSet.getString("patientAddress"));
                String email = CipherCareEncryption.decrypt(resultSet.getString("patientEmail"));
                String phone = CipherCareEncryption.decrypt(resultSet.getString("patientPhone"));
                if((searchby.getSelectedIndex() == 0 || searchby.getSelectedIndex() == 1) && String.valueOf(id).contains(searchTerm)){
                    model.addRow(new Object[]{id, dob, address, email, phone});
                }
                else if((searchby.getSelectedIndex() == 0 || searchby.getSelectedIndex() == 2) && dob.contains(searchTerm)){
                    model.addRow(new Object[]{id, dob, address, email, phone});
                }
                else if((searchby.getSelectedIndex() == 0 || searchby.getSelectedIndex() == 3) && address.contains(searchTerm)){
                    model.addRow(new Object[]{id, dob, address, email, phone});
                }
                else if((searchby.getSelectedIndex() == 0 || searchby.getSelectedIndex() == 4) && email.contains(searchTerm)){
                    model.addRow(new Object[]{id, dob, address, email, phone});
                }
                else if((searchby.getSelectedIndex() == 0 || searchby.getSelectedIndex() == 5) && phone.contains(searchTerm)){
                    model.addRow(new Object[]{id, dob, address, email, phone});
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Failed to search patient records: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
