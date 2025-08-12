package back_end;

import gui.FileMap;

import javax.swing.DefaultListModel;
import java.sql.SQLException;

public class ConfigService extends DatabaseService {
    private static ConfigService configService;
    private ConfigService() {
        String[] columns = {"id INT AUTO_INCREMENT PRIMARY KEY",
                            "username_ForeignKey VARCHAR(256) NOT NULL",
                            "category VARCHAR(50) NOT NULL",
                            "file_format VARCHAR(50) NOT NULL",
                            "FOREIGN KEY (username_ForeignKey) REFERENCES users_info(username)"};
        createTable("config_table", columns);
    }

    public void refresh() {
        DefaultListModel<String> temp;
        FileMap.getInstance().clear();
        String query = String.format("SELECT * FROM config_table WHERE username_ForeignKey = ?",
                Account.getInstance().getUsername());
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, Account.getInstance().getUsername());
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                String cat = resultSet.getString("category");
                FileMap.getInstance().put(cat,null);
            }
            for(String key: FileMap.getInstance().keySet()) {
                temp = new DefaultListModel<>();
                query = "SELECT * FROM config_table WHERE username_ForeignKey = ? AND category = ?";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, Account.getInstance().getUsername());
                preparedStatement.setString(2, key);
                resultSet = preparedStatement.executeQuery();
                while(resultSet.next()) {
                    temp.addElement(resultSet.getString("file_format"));
                }
                FileMap.getInstance().put(key,temp);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void resetFile() {
        String query = "DELETE FROM config_table WHERE username_ForeignKey = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,Account.getInstance().getUsername());
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addFile(String category, String file) {
        try {
            String query = "INSERT INTO config_table (username_ForeignKey,category,file_format) VALUES (?,?,?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,Account.getInstance().getUsername());
            preparedStatement.setString(2,category);
            preparedStatement.setString(3,file);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ConfigService getInstance() {
        if(configService == null) configService = new ConfigService();
        return configService;
    }

}
