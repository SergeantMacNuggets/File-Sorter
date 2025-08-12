package back_end;

import com.mysql.cj.x.protobuf.MysqlxPrepare;

import java.sql.*;

public class DatabaseService {
    protected Connection connection;
    protected Statement statement;
    protected ResultSet resultSet;
    protected PreparedStatement preparedStatement;

    public DatabaseService() {
        String query = "USE file_sorter_schema";
        while (true) {
            try {
                this.connection = DatabaseConnection.getConnection();
                this.statement = connection.createStatement();
                statement.execute(query);
                statement.execute("USE file_sorter_schema");
                break;
            } catch (SQLException e) {
                query = "CREATE DATABASE file_sorter_schema";
            } catch(NullPointerException e) {
                DatabaseConnection.run();
            }
        }
    }

    public void serviceClose() throws SQLException {
        resultSet.close();
        statement.close();
        connection.close();
    }
    protected void createTable(String table, String[] columns){
        String query = String.format("CREATE TABLE %s (",table);
        StringBuilder sb = new StringBuilder(query);
        try {
            for (String column : columns) {
                sb.append((column.equals(columns[columns.length - 1])) ? column : column + "," );
            }
            query = sb + ")";
            System.out.println(query);
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
