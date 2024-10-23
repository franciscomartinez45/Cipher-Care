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

    public static DefaultTableModel tableLookup(String table, String column, String keyword, String username, String password){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/CipherCare", username, password);
            String query = "SELECT * FROM "+table;
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
