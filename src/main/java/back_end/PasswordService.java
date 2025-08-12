package back_end;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.PreparedStatement;
import java.sql.SQLException;

class PasswordService extends DatabaseService {
    private String table;
    public PasswordService(String table) {
        String[] columns = {"username varchar(256) NOT NULL PRIMARY KEY", "password varchar(256) NOT NULL"};
        this.table = table;
        createTable(table, columns);
    }

    public void changePassword(String user, String password) {
        try {
            String query = String.format("SELECT * FROM %s WHERE username = ?",this.table);
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,user);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                String newPassword = BCrypt.hashpw(password,BCrypt.gensalt(12));
                query = String.format("UPDATE %s SET password=? WHERE username = ?",this.table);
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1,newPassword);
                preparedStatement.setString(2,user);
                preparedStatement.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addUser(String user, String pass) throws SQLException{
        String encryptedPass = BCrypt.hashpw(pass, BCrypt.gensalt(12));
        String query = String.format("INSERT INTO %s (username, password) VALUES (?,?)", this.table);
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, user);
        preparedStatement.setString(2, encryptedPass);
        preparedStatement.execute();
    }

    public boolean isTableEmpty() {
        try {
            String query = String.format("SELECT * FROM %s", this.table);
            resultSet = statement.executeQuery(query);
            if(!resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public boolean authorize(String username, String password) {
        try {
            String query = String.format("SELECT * FROM %s WHERE username = ?", this.table);
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                return (username.equals(resultSet.getString("username")) &&
                        BCrypt.checkpw(password, resultSet.getString("password")));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    public boolean authorizeGuest(String username) {
        try {
            String query = String.format("SELECT * FROM %s WHERE username = ?", this.table);
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                return username.equals(resultSet.getString("username"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return false;
    }
}
