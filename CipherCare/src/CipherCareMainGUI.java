import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.table.*;

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
        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);
        this.username = username;
        this.password = password;
        frame.setLayout(new GridBagLayout());
        initializeUI();
        frame.setVisible(true);
    }

    private void initializeUI(){

        String[] tables = {"patient", "medicalrecord", "appointment", "telehealth"};
        //JPanel datapanel = new JPanel();
        gbc = new GridBagConstraints();
        tableSelect = new JComboBox<String>(tables);
        columnSelect = new JComboBox<String>(CipherCareSQL.getColumns("patient", username, password));
        searchField = new JTextField();
        JButton searchB = new JButton("Search");

        dataTable = new JTable(CipherCareSQL.tableLookup("patient", "","", username, password));
        dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        TableColumnModel columnModel = dataTable.getColumnModel();
        for(int i = 0; i < dataTable.getColumnCount(); i++){
            int estimate = ((String)dataTable.getModel().getValueAt(1, i)).length();
            if (estimate < 15){
                columnModel.getColumn(i).setPreferredWidth(150);
            }
            else{
                columnModel.getColumn(i).setPreferredWidth(300);
            }
        }
        scroll = new JScrollPane(dataTable);
        scroll.setVisible(true);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        JLabel resultsLabel = new JLabel("Results:");
        JLabel searchLabel = new JLabel("Enter Query:");
        JLabel columnLabel = new JLabel("Select Table:");
        JLabel tableLabel = new JLabel("Select Column:");
        JButton addB = new JButton("Add Data");
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
        frame.add(refreshB, gbc);


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
                update();
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
        String table = tableSelect.getSelectedItem().toString();
        new CipherCareAddGUI(username, password, table);

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

    public void update(){
        //TODO
    }

}
