
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.DefaultTableModel;

public class CipherCareSQL{

    public static boolean testConnection(String user, char[] pass){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/CipherCare";
            String password = "";
            for(char c: pass){
                password += c;
            }
            Connection connection = DriverManager.getConnection(url, user, password);

            System.out.println("Connection Secured");
            connection.close();
            return(true);

        }catch (Exception e) {
            return(false);
        }
    }

    public static String[] getColumns(String table, String username, String password){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/CipherCare", username, password);
            String query = "SELECT * FROM "+table;
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSetMetaData resultMetaData = resultSet.getMetaData();
            String[] columns = new String[resultMetaData.getColumnCount()];
            for(int i = 0; i < resultMetaData.getColumnCount(); i++){
                columns[i] = resultMetaData.getColumnName(i+1);
            }
            resultSet.close();
            preparedStatement.close();
            connection.close();
            return(columns);

        }catch (Exception e) {
            System.out.println("ERROR: "+e.getMessage());
            return(null);
        }
    }

    //Get patient data, returned as a key,value pair 
   public static Map<String, String> getPatientColumn(String patientID, String username, String password) throws ClassNotFoundException, SQLException {
        Map<String, String> patientData = new LinkedHashMap<>();
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/CipherCare", username, password);
        String query = "SELECT * FROM patient WHERE patientID = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, patientID);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                String columnValue = resultSet.getString(i);
                patientData.put(columnName, columnValue);
            }
        }   
        resultSet.close();
        statement.close();
        connection.close();

        return patientData.isEmpty() ? null : patientData;
}


//Returns all the available telehealth services
public static List<String> getTelehealthServices(String username, String password) throws ClassNotFoundException, SQLException {
    List<String> services = new ArrayList<>();
    
    Class.forName("com.mysql.cj.jdbc.Driver");
    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/CipherCare", username, password);
    String query = "SELECT name FROM Telehealth";
    PreparedStatement statement = connection.prepareStatement(query);
    ResultSet resultSet = statement.executeQuery();
    
    while (resultSet.next()) {
        services.add(resultSet.getString("name"));
    }
    
    resultSet.close();
    statement.close();
    connection.close();
    
    return services;

}

//Insert appointment into database
public static void saveAppointment(String patientID, String telehealthService, String date, String startTime, String endTime, String username, String password) throws ClassNotFoundException, SQLException {
    Class.forName("com.mysql.cj.jdbc.Driver");
    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/CipherCare", username, password);
    String serviceQuery = "SELECT telehealthID FROM Telehealth WHERE name = ?";
    PreparedStatement serviceStatement = connection.prepareStatement(serviceQuery);
    serviceStatement.setString(1, telehealthService);
    ResultSet serviceResultSet = serviceStatement.executeQuery();
    int telehealthID = 0;
    if (serviceResultSet.next()) {
        telehealthID = serviceResultSet.getInt("telehealthID");
    }
    String insertQuery = "INSERT INTO Appointment (recordID, telehealthID, date, startTime, endTime) VALUES (?, ?, ?, ?, ?)";
    PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
    insertStatement.setInt(1, Integer.parseInt(patientID)); 
    insertStatement.setInt(2, telehealthID);
    insertStatement.setDate(3, Date.valueOf(date));
    insertStatement.setString(4, startTime);
    insertStatement.setString(5, endTime);
    insertStatement.executeUpdate();
    serviceResultSet.close();
    serviceStatement.close();
    insertStatement.close();
    connection.close();
}

    public static DefaultTableModel tableLookup(String table, String column, String keyword, String username, String password){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/CipherCare", username, password);
            String query = "SELECT * FROM "+ table;
            if (!(keyword.equals("")||column.equals(""))){
                query = query.concat(" WHERE "+column+" LIKE '%"+keyword+"%'");
            }
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSetMetaData resultMetaData = resultSet.getMetaData();
            String[] columns = new String[resultMetaData.getColumnCount()];
            for(int i = 0; i < resultMetaData.getColumnCount(); i++){
                columns[i] = resultMetaData.getColumnName(i+1);
            }
            DefaultTableModel model = new DefaultTableModel();
            model.setColumnIdentifiers(columns);
            while(resultSet.next()){
                Object[] values = new Object[columns.length];
                for(int i = 0; i < columns.length; i++){
                    values[i] = resultSet.getString(columns[i]);
                }
                model.addRow(values);

            }
            resultSet.close();
            preparedStatement.close();
            connection.close();
            return(model);

        }catch (Exception e) {
            System.out.println("ERROR: "+e.getMessage());
            return(null);
        }
    }
    
    public static void remove(String table, String column, String keyword, String username, String password){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/CipherCare", username, password);
            String query = "DELETE FROM "+table;
            if (!(keyword.equals(""))){
                query = "DELETE FROM "+table+" WHERE "+column+"='"+keyword+"'";
            }
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.execute();
            preparedStatement.close();
            connection.close();

        }catch (Exception e) {
            System.out.println("ERROR: "+e.getMessage());
        }
    }

}


