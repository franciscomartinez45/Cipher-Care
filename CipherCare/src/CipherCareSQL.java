import java.sql.*;
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

    public static DefaultTableModel tableLookup(String keyword, String username, String password){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/CipherCare", username, password);
            //TODO: make table and column values alterable when forming the query
            String query = "SELECT * FROM patient";
            if (!(keyword.equals(""))){
                query.concat(" WHERE PatientID LIKE %"+keyword+"%");
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

}
