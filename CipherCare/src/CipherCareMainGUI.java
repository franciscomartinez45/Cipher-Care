import javax.swing.*;
import java.awt.event.*;
import java.util.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.awt.*;
import javax.swing.table.*;
import java.util.List;

public class CipherCareMainGUI{

    private JTextField searchField;
    private JComboBox<String> columnSelect;
    private JComboBox<String> tableSelect;
    private JTable dataTable;
    private JScrollPane scroll;
    private String username;
    private String password;
    private JFrame frame;
    private GridBagConstraints gbc;
   
    public CipherCareMainGUI(String username, String password) {
        frame = new JFrame("Healthcare Database");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1090, 600);
        frame.setLocationRelativeTo(null);
        
        this.username = username;
        this.password = password;
        frame.setLayout(new GridBagLayout());
        initializeUI();
        frame.setVisible(true);
    }
 
    private void initializeUI(){

        String[] tables = {"patient", "medicalrecord", "appointment", "telehealth"};
       
        gbc = new GridBagConstraints();
        tableSelect = new JComboBox<String>(tables);
        columnSelect = new JComboBox<String>(CipherCareSQL.getColumns("patient", username, password));
        searchField = new JTextField();
        JButton searchB = new JButton("Search");
        
        dataTable = new JTable(CipherCareSQL.tableLookup("patient", "","", username, password));
        dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        TableColumnModel columnModel = dataTable.getColumnModel();
        dataTable.setColumnSelectionAllowed(false);
        for(int i = 0; i < dataTable.getColumnCount(); i++){
            int estimate = ((String)dataTable.getModel().getValueAt(1, i)).length();
    
            if (estimate < 15){
                columnModel.getColumn(i).setPreferredWidth(150);
            }
            else{
                columnModel.getColumn(i).setPreferredWidth(300);
            }
        
        }
        System.out.println(columnSelect);
        scroll = new JScrollPane(dataTable);
        scroll.setVisible(true);
        // scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        JLabel resultsLabel = new JLabel("Results:");
        JLabel searchLabel = new JLabel("Enter Query:");
        JLabel columnLabel = new JLabel("Select Table:");
        JLabel tableLabel = new JLabel("Select Column:");
        JButton addB = new JButton("Add Data");
        JButton addServiceB = new JButton("Add Telehealth Service");
        JButton removeB = new JButton("Remove Data");
        JButton updateB = new JButton("Update Data");
        JButton refreshB = new JButton("Refresh Data");
        
        gbc.fill = GridBagConstraints.HORIZONTAL;  
        gbc.gridx = 0;  
        gbc.gridy = 0;
        frame.add(tableLabel, gbc);
        gbc.gridx = 1;  
        gbc.gridy = 0;
        frame.add(columnLabel, gbc);
        gbc.gridx = 2;  
        gbc.gridy = 0;
        frame.add(searchLabel, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;  
        gbc.gridx = 0;  
        gbc.gridy = 1;
        gbc.weightx = 0.1;
        frame.add(tableSelect, gbc);
        gbc.gridx = 1;  
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        frame.add(columnSelect, gbc);
        gbc.gridx = 2;  
        gbc.gridy = 1;
        gbc.weightx = 0.5;
        frame.add(searchField, gbc);
        gbc.weightx = 0;
        gbc.gridx = 3;  
        gbc.gridy = 1;
        frame.add(searchB, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL; 

        gbc.gridx = 0;  
        gbc.gridy = 2;
        frame.add(resultsLabel, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;  
        gbc.gridx = 0;  
        gbc.gridy = 3;
        gbc.gridwidth = 5;
        gbc.weightx = 1;
        frame.add(scroll, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;  
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.gridx = 0;  
        gbc.gridy = 16;
        frame.add(updateB, gbc);
        gbc.gridx = 1;  
        gbc.gridy = 16;
        frame.add(addB, gbc);
        gbc.gridx = 2;  
        gbc.gridy = 16;
        frame.add(removeB, gbc);
        gbc.gridx = 3;  
        gbc.gridy = 16;
        frame.add(addServiceB,gbc);
        gbc.gridx = 4;
        gbc.gridy = 16;
        frame.add(refreshB, gbc);
        gbc.gridx = 5;
        gbc.gridy = 16;
        

        addServiceB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(dataTable.getSelectedRow()!=-1){
                service(String.valueOf(dataTable.getSelectedRow()));
                }
            }
        });

        searchB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                search();
            }
        });

        tableSelect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                refresh();
            }
        });

        refreshB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                refresh();
            }
        });

        addB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                add();
            }
        });

        removeB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                remove();
            }
        });

      
        updateB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
            }
        });
    }

    public void insertTable(){
        dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        TableColumnModel columnModel = dataTable.getColumnModel();
        for(int i = 0; i < dataTable.getColumnCount(); i++){
            if(dataTable.getRowCount() <= 0){
                columnModel.getColumn(i).setPreferredWidth(150);
            }
            else{
                int estimate = ((String)dataTable.getModel().getValueAt(0, i)).length();
                if (estimate < 15){
                    columnModel.getColumn(i).setPreferredWidth(150);
                }
                else{
                    columnModel.getColumn(i).setPreferredWidth(300);
                }
            }
        }
        scroll = new JScrollPane(dataTable);
        scroll.setVisible(true);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        gbc.fill = GridBagConstraints.HORIZONTAL;  
        gbc.gridx = 0;  
        gbc.gridy = 3;
        gbc.gridwidth = 5;
        gbc.weightx = 1;
        frame.add(scroll, gbc);
    }
   
    public void refresh(){
            String table = tableSelect.getSelectedItem().toString();
           
            frame.remove(this.scroll);
            frame.remove(this.columnSelect);
            columnSelect = new JComboBox<String>(CipherCareSQL.getColumns(table, username, password));
            columnSelect.setBounds(180,100,120,40);
            dataTable = new JTable(CipherCareSQL.tableLookup(table, "", "", username, password));
            gbc.gridx = 1;  
            gbc.gridy = 1;
            gbc.gridwidth = 1;
            gbc.weightx = 0.3;
            frame.add(columnSelect, gbc);
            insertTable();
            frame.revalidate();
            frame.repaint();
    }

    public void search(){
        String keyword = searchField.getText();
        String column = columnSelect.getSelectedItem().toString();
        String table = tableSelect.getSelectedItem().toString();
    
        try{
            frame.remove(this.scroll);
            dataTable = new JTable(CipherCareSQL.tableLookup(table, column, keyword, username, password));
            insertTable();
            frame.revalidate();
            frame.repaint();
        }
        catch(Exception e){
            searchField.setText("Error searching for items: " + e.getMessage());
            refresh();
        }
    }

    public void add(){
        
        JFrame addFrame = new JFrame("Insert Into Database");
        addFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addFrame.setSize(400, 650);
        addFrame.setLocationRelativeTo(null);
        addFrame.setLayout(new GridBagLayout());
        addFrame.setVisible(true);
    }

    public void remove(){
        String table = tableSelect.getSelectedItem().toString();
        int[] rows = dataTable.getSelectedRows();
        String column = columnSelect.getItemAt(0);
        for(int row: rows){
            String value = (String) dataTable.getValueAt(row, 0);
            CipherCareSQL.remove(table, column, value, username, password);
        }
        refresh();
        
    }

    public void update(int selectedRow){
        //TODO
    }


    //Create JFrame
    public void service(String patientID) {
    JFrame serviceFrame = new JFrame("Select a Telehealth service");
    serviceFrame.setSize(400, 400);
    serviceFrame.setLocationRelativeTo(null);
    serviceFrame.setLayout(new GridBagLayout());

    GridBagConstraints gbcservice = new GridBagConstraints();
    gbcservice.fill = GridBagConstraints.HORIZONTAL;
    gbcservice.insets = new Insets(10, 10, 10, 10);
    
    //Display the patient information
    JLabel information = new JLabel("Patient information:");
    gbcservice.gridx = 0;
    gbcservice.gridy = 0;
    gbcservice.gridwidth = 2;
    serviceFrame.add(information, gbcservice);

    try {
        //Retrieve the patient information
        Map<String, String> data = CipherCareSQL.getPatientColumn(patientID, username,password); 
        if (data != null) {
            int row = 1;
            //Map to iterate through key, value pairs
           for (Map.Entry<String, String> entry : data.entrySet()) {
                gbcservice.gridwidth = 1; 
                gbcservice.gridx = 0;
                gbcservice.gridy = row;
                serviceFrame.add(new JLabel(entry.getKey() + ":"), gbcservice);
                gbcservice.gridx = 1;
                serviceFrame.add(new JLabel(entry.getValue()), gbcservice);
                row++;
            }

        } else {
            gbcservice.gridwidth = 2;
            gbcservice.gridx = 0;
            gbcservice.gridy = 1;
            serviceFrame.add(new JLabel("No patient found with ID: " + patientID), gbcservice);
        }
    } catch (ClassNotFoundException | SQLException e) {
        e.printStackTrace();
        gbcservice.gridwidth = 2;
        gbcservice.gridx = 0;
        gbcservice.gridy = 1;
        serviceFrame.add(new JLabel("Error fetching patient data"), gbcservice);
    }
    //Button to assign a Telehealth Service
    JButton addAppointmentButton = new JButton("Add Appointment");
    addAppointmentButton.addActionListener(e -> {
        createAppointmentForm(patientID);
    });
    
    gbcservice.gridwidth = 2;
    gbcservice.gridx = 0;
    gbcservice.gridy++;
    serviceFrame.add(addAppointmentButton, gbcservice);
    serviceFrame.setVisible(true);
}

//Create appointment form 
private void createAppointmentForm(String patientID) {
   
    JFrame appointmentFrame = new JFrame("Create Appointment");
    appointmentFrame.setSize(400, 400);
    appointmentFrame.setLocationRelativeTo(null);
    appointmentFrame.setLayout(new GridBagLayout());

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(10, 10, 10, 10);

    //Style the JFrame
    JLabel information = new JLabel("Create Appointment for Patient ID: " + patientID);
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 2;
    appointmentFrame.add(information, gbc);

    
    JLabel dateLabel = new JLabel("Date(YYYY-MM-DD):");
    gbc.gridwidth = 1;
    gbc.gridy++;
    appointmentFrame.add(dateLabel, gbc);
    
    JTextField dateField = new JTextField(10);
    gbc.gridx = 1;
    appointmentFrame.add(dateField, gbc);
   
    
    JLabel startTimeLabel = new JLabel("Start Time(HH:MM AM/PM):");
    gbc.gridx = 0;
    gbc.gridy++;
    appointmentFrame.add(startTimeLabel, gbc);
    
    JTextField startTimeField = new JTextField(10);
    
    gbc.gridx = 1;
    appointmentFrame.add(startTimeField, gbc);

   
    JLabel endTimeLabel = new JLabel("End Time(HH:MM AM/PM):");
    gbc.gridx = 0;
    gbc.gridy++;
    appointmentFrame.add(endTimeLabel, gbc);
    
    JTextField endTimeField = new JTextField(10);
    gbc.gridx = 1;
    appointmentFrame.add(endTimeField, gbc);

    //Drop Down menu to select telehealth service
    JLabel serviceLabel = new JLabel("Select Telehealth Service:");
    gbc.gridx = 0;
    gbc.gridy++;
    appointmentFrame.add(serviceLabel, gbc);
    
    JComboBox<String> serviceComboBox = new JComboBox<>();
    try {
        //Get Telehealth services as a list
        List<String> services =  CipherCareSQL.getTelehealthServices(username, password);
        for (String service : services) {
            serviceComboBox.addItem(service);
        }
    } catch (ClassNotFoundException | SQLException e) {
        e.printStackTrace();
    }
    gbc.gridx = 1;
    appointmentFrame.add(serviceComboBox, gbc);
    
    //Button to insert data
    JButton submitButton = new JButton("Create Appointment");
  submitButton.addActionListener(e -> {
    String date = dateField.getText();
    String startTime = startTimeField.getText();
    String endTime = endTimeField.getText();
    String selectedService = (String) serviceComboBox.getSelectedItem();

    
    boolean validFields = true;

    
    if (!isValidDate(date)) {
        dateField.setBackground(Color.RED);
        JOptionPane.showMessageDialog(appointmentFrame, "Invalid date.");
        validFields = false; 
    } else {
        dateField.setBackground(Color.GREEN);
    }

   
    if (!isValidTime(startTime)) {
        startTimeField.setBackground(Color.RED);
        JOptionPane.showMessageDialog(appointmentFrame, "Invalid start time format. Please use HH:MM AM/PM");
        validFields = false; 
    } else if (!isWithinBusinessHours(startTime)) {
        startTimeField.setBackground(Color.RED);
        JOptionPane.showMessageDialog(appointmentFrame, "Start time must be within business hours (8:00 AM - 5:00 PM).");
        validFields = false; 
    } else {
        startTimeField.setBackground(Color.GREEN);
    }

    
    if (!isValidTime(endTime)) {
        endTimeField.setBackground(Color.RED);
        JOptionPane.showMessageDialog(appointmentFrame, "Invalid end time format. Please use HH:MM AM/PM");
        validFields = false; 
    } else if (!isWithinBusinessHours(endTime)) {
        endTimeField.setBackground(Color.RED);
        JOptionPane.showMessageDialog(appointmentFrame, "End time must be within business hours (8:00 AM - 5:00 PM).");
        validFields = false; 
    } else {
        endTimeField.setBackground(Color.GREEN);
    }

   
    if (!isValidTimeRange(startTime, endTime)) {
        startTimeField.setBackground(Color.RED);
        endTimeField.setBackground(Color.RED);
        JOptionPane.showMessageDialog(appointmentFrame, "Start time must be at least 30 minutes before end time.");
        validFields = false; 
    }

  
    if (!validFields) {
        return; 
    }


    try {
        CipherCareSQL.saveAppointment(patientID, selectedService, date, startTime, endTime, username, password);
        JOptionPane.showMessageDialog(appointmentFrame, "Appointment Created Successfully!");
        appointmentFrame.dispose();
    } catch (Exception error) {
        JOptionPane.showMessageDialog(appointmentFrame, "Error creating appointment: " + error.getMessage());
        error.printStackTrace();
    }
});

    gbc.gridwidth = 2;
    gbc.gridy++;
    appointmentFrame.add(submitButton, gbc);
    appointmentFrame.setVisible(true);
}


 public static boolean isValidDate(String date) {
    SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
    dateformat.setLenient(false);
    try {  
        Date parsedDate = dateformat.parse(date);
        Date currentDate = dateformat.parse(dateformat.format(new Date()));
        return !parsedDate.before(currentDate);
    } catch (ParseException e) {
        return false; 
    }
}
public static boolean isValidTime(String time) {
        SimpleDateFormat dateformat = new SimpleDateFormat("hh:mm a");
        dateformat.setLenient(false);
        try {
            Date parsedTime = dateformat.parse(time);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
     public static boolean isValidTimeRange(String startTime, String endTime) {
        SimpleDateFormat dateformat = new SimpleDateFormat("hh:mm a");
        try {
            Date start = dateformat.parse(startTime);
            Date end = dateformat.parse(endTime);
            long differenceInMinutes = (end.getTime() - start.getTime()) / (1000 * 60);
            return differenceInMinutes >= 30;
        } catch (ParseException e) {
            return false;
        }
    }
    public static boolean isWithinBusinessHours(String time) {
        SimpleDateFormat dateformat = new SimpleDateFormat("hh:mm a");
        try {
            Date inputTime = dateformat.parse(time);
            Date businessStart = dateformat.parse("8:00 AM");
            Date businessEnd = dateformat.parse("5:00 PM");

            return inputTime.equals(businessStart) || inputTime.equals(businessEnd) ||
                   (inputTime.after(businessStart) && inputTime.before(businessEnd));
        } catch (ParseException e) {
            return false;
        }
    }
}