import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

public class CipherCareAppointmentGUI {
    private JFrame frame;
    private JTable appointmentTable;
    private DefaultTableModel appointmentModel;

    public CipherCareAppointmentGUI() {

        // Set up the frame
        frame = new JFrame("CipherCare - Manage Appointments");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        initializeUI();
        frame.setVisible(true);
    }

    private void initializeUI() {
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Add Appointment Button
        JButton addAppointmentButton = new JButton("Add Appointment");
        addAppointmentButton.addActionListener(e -> addAppointment());

        gbc.gridx = 0;
        gbc.gridy = 0;
        buttonPanel.add(addAppointmentButton, gbc);

        // Edit Appointment Button
        JButton editAppointmentButton = new JButton("Edit Appointment");
        editAppointmentButton.addActionListener(e -> editAppointment());

        gbc.gridy = 1;
        buttonPanel.add(editAppointmentButton, gbc);

        // Delete Appointment Button
        JButton deleteAppointmentButton = new JButton("Delete Appointment");
        deleteAppointmentButton.addActionListener(e -> deleteAppointment());

        gbc.gridy = 2;
        buttonPanel.add(deleteAppointmentButton, gbc);

        frame.add(buttonPanel, BorderLayout.WEST);

        // Table to display appointments
        appointmentModel = new DefaultTableModel();
        String[] columnNames = {"Appointment ID", "Record ID", "Telehealth ID", "Date", "Start Time", "End Time"};
        appointmentModel.setColumnIdentifiers(columnNames);
        appointmentTable = new JTable(appointmentModel){
            public boolean isCellEditable(int row, int column) {                
                return false;               
            };
        };

        appointmentTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    editAppointment();
                }
            }
        });
     
        JScrollPane scrollPane = new JScrollPane(appointmentTable);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Load appointments initially
        loadAppointments();
    }

    private void loadAppointments() {
        appointmentModel.setRowCount(0); // Clear existing rows

        try (Connection connection = CipherCareSQL.getConnection()) {
            String query = "SELECT * FROM Appointment";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    int appointmentID = resultSet.getInt("appointmentID");
                    int recordID = resultSet.getInt("recordID");
                    int telehealthID = resultSet.getInt("telehealthID");
                    String date = resultSet.getString("date");
                    String startTime = resultSet.getString("startTime");
                    String endTime = resultSet.getString("endTime");

                    // Convert Telehealth ID to descriptive text
                    //String appointmentType = (telehealthID == 1) ? "Telehealth" : "In-person";

                    // Add row to the table
                    appointmentModel.addRow(new Object[]{appointmentID, recordID, telehealthID, date, startTime, endTime});
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Failed to load appointments: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }


    private void addAppointment() {
        // Create form fields
        JTextField recordIDField = new JTextField(10);
        //JTextField telehealthIDField = new JTextField(10);
        JTextField dateField = new JTextField(10); // Format: YYYY-MM-DD
        JTextField startTimeField = new JTextField(10); // Format: HH:MM
        JTextField endTimeField = new JTextField(10); // Format: HH:MM

        // Add a dropdown for appointment type
        JComboBox<String> serviceComboBox = new JComboBox<String>();
        try{
            Connection connection = CipherCareSQL.getConnection();
            String query = "SELECT name FROM Telehealth";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                serviceComboBox.addItem(resultSet.getString("name"));
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Failed to search patient records: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        JPanel panel = new JPanel(new GridLayout(6, 2));
        panel.add(new JLabel("Record ID:"));
        panel.add(recordIDField);
        panel.add(new JLabel("Date (YYYY-MM-DD):"));
        panel.add(dateField);
        panel.add(new JLabel("Start Time (HH:MM):"));
        panel.add(startTimeField);
        panel.add(new JLabel("End Time (HH:MM):"));
        panel.add(endTimeField);
        panel.add(new JLabel("Appointment Type:"));
        panel.add(serviceComboBox);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Add Appointment", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String recordID = recordIDField.getText().trim();
            String date = dateField.getText().trim();
            String startTime = startTimeField.getText().trim();
            String endTime = endTimeField.getText().trim();
            String appointmentType = (String) serviceComboBox.getSelectedItem();

            if (recordID.isEmpty() || date.isEmpty() || startTime.isEmpty() || endTime.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill all required fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Determine telehealthID based on appointment type
            String telehealthID = "1";
            try{
                Connection connection = CipherCareSQL.getConnection();
                String query = "SELECT telehealthID FROM Telehealth WHERE name = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, appointmentType);
                ResultSet resultSet = statement.executeQuery();
                resultSet.next();
                telehealthID = resultSet.getString("telehealthID");
                resultSet.close();
                statement.close();
                connection.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, "Failed to validate Telehealth Service: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }

            // Insert into database
            try (Connection connection = CipherCareSQL.getConnection()) {
                String query = "INSERT INTO Appointment (recordID, telehealthID, date, startTime, endTime) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setInt(1, Integer.parseInt(recordID));
                    preparedStatement.setInt(2, Integer.parseInt(telehealthID));
                    preparedStatement.setString(3, date);
                    preparedStatement.setString(4, startTime);
                    preparedStatement.setString(5, endTime);
                    preparedStatement.executeUpdate();

                    JOptionPane.showMessageDialog(frame, "Appointment added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadAppointments(); // Refresh the table
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(frame, "Failed to add appointment: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }



    private void editAppointment() {
        // Check if a row is selected
        int selectedRow = appointmentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select an appointment to edit.", "Edit Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Retrieve current values from the selected row
        int appointmentID = (int) appointmentModel.getValueAt(selectedRow, 0);
        String recordID = appointmentModel.getValueAt(selectedRow, 1).toString();
        int telehealthID = (int) appointmentModel.getValueAt(selectedRow, 2);
        String date = appointmentModel.getValueAt(selectedRow, 3).toString();
        String startTime = appointmentModel.getValueAt(selectedRow, 4).toString();
        String endTime = appointmentModel.getValueAt(selectedRow, 5).toString();

        // Create a form for editing
        JTextField recordIDField = new JTextField(recordID);
        JComboBox<String> telehealthIDField = new JComboBox<String>();
        try{
            Connection connection = CipherCareSQL.getConnection();
            String query = "SELECT name FROM Telehealth";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                telehealthIDField.addItem(resultSet.getString("name"));
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Failed to search patient records: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        telehealthIDField.setSelectedIndex(telehealthID-1);
        JTextField dateField = new JTextField(date);
        JTextField startTimeField = new JTextField(startTime);
        JTextField endTimeField = new JTextField(endTime);

        JPanel panel = new JPanel(new GridLayout(6, 2));
        panel.add(new JLabel("Record ID:"));
        panel.add(recordIDField);
        panel.add(new JLabel("Telehealth ID:"));
        panel.add(telehealthIDField);
        panel.add(new JLabel("Date (YYYY-MM-DD):"));
        panel.add(dateField);
        panel.add(new JLabel("Start Time (HH:MM):"));
        panel.add(startTimeField);
        panel.add(new JLabel("End Time (HH:MM):"));
        panel.add(endTimeField);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Edit Appointment", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try{
                Connection connection = CipherCareSQL.getConnection();
                String telehealthValue = "1";
                String query = "SELECT telehealthID FROM Telehealth WHERE name = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, telehealthIDField.getSelectedItem().toString());
                ResultSet resultSet = preparedStatement.executeQuery();
                resultSet.next();
                telehealthValue = resultSet.getString("telehealthID");
                query = "UPDATE Appointment SET recordID = ?, telehealthID = ?, date = ?, startTime = ?, endTime = ? WHERE appointmentID = ?";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, Integer.parseInt(recordIDField.getText().trim()));
                preparedStatement.setString(2, telehealthValue.trim());
                preparedStatement.setString(3, dateField.getText().trim());
                preparedStatement.setString(4, startTimeField.getText().trim());
                preparedStatement.setString(5, endTimeField.getText().trim());
                preparedStatement.setInt(6, appointmentID);
                preparedStatement.executeUpdate();
                resultSet.close();
                preparedStatement.close();
                connection.close();
                JOptionPane.showMessageDialog(frame, "Appointment updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadAppointments(); // Refresh the table

            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, "Failed to update appointment: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }



    private void deleteAppointment() {
        int selectedRow = appointmentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select an appointment to delete.", "Delete Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int appointmentID = (int) appointmentModel.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this appointment?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection connection = CipherCareSQL.getConnection()) {
                String query = "DELETE FROM Appointment WHERE appointmentID = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setInt(1, appointmentID);
                    preparedStatement.executeUpdate();
                    appointmentModel.removeRow(selectedRow);
                    JOptionPane.showMessageDialog(frame, "Appointment deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(frame, "Failed to delete appointment: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
}
