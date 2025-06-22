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
        String query = String.format("SELECT * FROM config_table WHERE username_ForeignKey = '%s'",
                Account.getInstance().getUsername());
        try {
            resultSet = statement.executeQuery(query);
            while(resultSet.next()) {
                String cat = resultSet.getString("category");
                FileMap.getInstance().put(cat,null);
            }
            for(String key: FileMap.getInstance().keySet()) {
                temp = new DefaultListModel<>();
                query = String.format("SELECT * FROM config_table WHERE username_ForeignKey = '%s' AND category = '%s'",
                        Account.getInstance().getUsername(), key);
                resultSet = statement.executeQuery(query);
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
        String query = String.format("DELETE FROM config_table WHERE username_ForeignKey = '%s'",Account.getInstance().getUsername());
        try {
            statement.execute(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addFile(String category, String file) {
        String query = String.format
                        ("INSERT INTO config_table (username_ForeignKey,category,file_format) VALUES ('%s','%s','%s')",
                                Account.getInstance().getUsername(), category, file);
        try {
            statement.execute(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ConfigService getInstance() {
        if(configService == null) configService = new ConfigService();
        return configService;
    }

}
