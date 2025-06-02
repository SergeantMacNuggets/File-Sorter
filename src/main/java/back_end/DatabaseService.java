package back_end;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseService {
    protected Connection connection;
    protected Statement statement;
    protected ResultSet resultSet;

    public DatabaseService(String schemaName, String tableName) {
        String query = "USE " + schemaName;
        while (true) {
            try {
                this.connection = DatabaseConnection.getConnection();
                this.statement = connection.createStatement();
                statement.execute(query);
                statement.execute("USE " + schemaName);
                break;
            } catch (SQLException e) {
                query = "CREATE DATABASE " + schemaName;
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
    protected void createTable(Statement s, String table, String[] columns){
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

class PasswordService extends DatabaseService {
    private String table;
    public PasswordService(String schema, String table) {
        super(schema, table);
        String[] columns = {"username varchar(256) NOT NULL PRIMARY KEY", "password varchar(256) NOT NULL"};
        this.table = table;
        createTable(super.statement, table, columns);
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

    public void addUser(String user, String pass) {
        String encryptedPass = BCrypt.hashpw(pass, BCrypt.gensalt(12));
        String query = String.format("INSERT INTO %s (username, password) VALUES ('%s','%s')", this.table, user, encryptedPass);
        while(true){
            try {
                super.statement.execute(query);
                break;
            } catch (SQLException e) {
                query = String.format("UPDATE %s SET password = '%s' WHERE username = '%s'", this.table, user, encryptedPass);
            }
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
