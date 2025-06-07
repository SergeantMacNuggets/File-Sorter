package back_end;

import org.mindrot.jbcrypt.BCrypt;

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

    public void addUser(String user, String pass) throws SQLException{
        String encryptedPass = BCrypt.hashpw(pass, BCrypt.gensalt(12));
        String query = String.format("INSERT INTO %s (username, password) VALUES ('%s','%s')", this.table, user, encryptedPass);
        super.statement.execute(query);
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

    public boolean authorizeGuest(String username) {
        try {
            String query = String.format("SELECT * FROM %s WHERE username = '%s'", this.table ,username);
            resultSet = statement.executeQuery(query);

            if(resultSet.next()) {
                return username.equals(resultSet.getString("username"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return false;
    }
}
