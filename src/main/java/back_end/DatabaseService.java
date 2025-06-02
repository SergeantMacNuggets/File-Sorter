package back_end;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.SQLException;



public class DatabaseService {
    protected Connection connection;
    protected Statement statement;
    protected ResultSet resultSet;

    public DatabaseService(String schemaName, String tableName) {
        try {
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+schemaName,
                    DatabaseConnection.getUser(), DatabaseConnection.getPassword());
            this.statement = connection.createStatement();
            this.resultSet = statement.executeQuery("select * from " + tableName);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void serviceClose() throws SQLException {
        resultSet.close();
        statement.close();
        connection.close();
    }
}

class PasswordService extends DatabaseService {
    private String schema, table;
    public PasswordService(String schema, String table) {
        super(schema, table);
        this.schema = schema;
        this.table = table;
    }

    public void changePassword(String user, String password) {
        try {
            String query = String.format("SELECT * FROM %s WHERE username = '%s'",this.table, user);
            resultSet = statement.executeQuery(query);
            if(resultSet.next()) {
                String newPassword = BCrypt.hashpw(password,BCrypt.gensalt(12));
                query = String.format("UPDATE %s SET password='%s' WHERE username = '%s'",this.table,newPassword,user);
                statement.execute(query);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Ayaw kalimot Delete ani pag ting release!!!!
    public void resetPassword() {
        try {
            String defaultPassword = BCrypt.hashpw("user", BCrypt.gensalt(12));
            String query = String.format("UPDATE %s SET password='%s' WHERE username = user",this.table, defaultPassword);
            resultSet = statement.executeQuery(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public boolean authorize(String username, String password) {
        try {
            String query = String.format("SELECT * FROM %s WHERE username = '%s'", this.table ,username);
            resultSet = statement.executeQuery(query);

            if(resultSet.next()) {
                return (username.equals(resultSet.getString("username")) &&
                        BCrypt.checkpw(password, resultSet.getString("password")));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return false;
    }
}
