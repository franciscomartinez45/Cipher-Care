import javax.swing.*;
import java.awt.event.*;

public class CipherCareMainGUI{

    private JTextField searchField;
    private JTable dataTable;
    private String username;
    private String password;
    private JFrame frame;

    public CipherCareMainGUI(String username, String password) {
        frame = new JFrame("Healthcare Database");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 900);
        frame.setLocationRelativeTo(null);
        this.username = username;
        this.password = password;
        initializeUI(frame);
        frame.setLayout(null);
        frame.setVisible(true);
    }

    private void initializeUI(JFrame frame){
        searchField = new JTextField();
        searchField.setBounds(100,100,300,40);
        JButton searchB = new JButton("Search");
        searchB.setBounds(500,100,150,60);
        dataTable = new JTable(CipherCareSQL.tableLookup("", username, password));
        dataTable.setBounds(100,200,600,200);
        dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scrollPane = new JScrollPane(dataTable);
        frame.add(searchField);
        frame.add(searchB);
        frame.add(scrollPane);
        frame.add(dataTable);

        searchB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                search();
            }
        });
    }

    public void search(){
        String keyword = searchField.getText();
        try{
            frame.remove(this.dataTable);
            dataTable = new JTable(CipherCareSQL.tableLookup(keyword, username, password));
            dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            JScrollPane scroll = new JScrollPane(dataTable);
            scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            //TODO: FIX THIS so it updates
            frame.add(dataTable);
            frame.revalidate();
            frame.repaint();
        }
        catch(Exception e){
            searchField.setText("Error searching for items: " + e.getMessage());
        }
    }

}
