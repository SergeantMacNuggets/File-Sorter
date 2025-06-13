package back_end;

import java.sql.SQLException;

public class SortService extends DatabaseService {
    private static SortService sortService;

    private SortService() {
        String[] columns = {
                "id INT AUTO_INCREMENT PRIMARY KEY",
                "username_ForeignKey VARCHAR(256) NOT NULL",
                "category VARCHAR(50) NOT NULL",
                "file_format VARCHAR(50) NOT NULL",
                "date VARCHAR(50)",
                "source_folder VARCHAR(512)",
                "dest_folder VARCHAR(512)",
                "FOREIGN KEY (username_ForeignKey) REFERENCES users_info(username)"
        };

        createTable("sort_table", columns);
    }

    public void addSort(String category, String file, String date, String src, String dst) {
        String query = String.format
                ("INSERT INTO sort_table (username_ForeignKey, category, file_format, date, source_folder, dest_folder) VALUES ('%s','%s','%s','%s','%s','%s')",
                Account.getInstance().getUsername(), category, file, date, src, dst);
        try {
            statement.execute(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addSort(String category, String file, String src, String dst) {
        String query = String.format
                ("INSERT INTO sort_table (username_ForeignKey, category, file_format, date, source_folder, dest_folder) VALUES ('%s','%s','%s','%s','%s')",
                        Account.getInstance().getUsername(), category, file, src, dst);
        try {
            statement.execute(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void save() {

    }


}
