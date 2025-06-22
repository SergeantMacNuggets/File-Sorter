package back_end;

import java.sql.SQLException;
import java.util.ArrayList;

public class MainService extends DatabaseService{
    private static MainService mainService;
    private MainService() {
        String[] columns = {
                "id INT AUTO_INCREMENT PRIMARY KEY",
                "username_ForeignKey VARCHAR(256) NOT NULL",
                "include_state BOOLEAN",
                "category VARCHAR(50) NOT NULL",
                "file_format VARCHAR(50) NOT NULL",
                "date VARCHAR(15)",
                "source_folder VARCHAR(512) NOT NULL",
                "destination_folder VARCHAR(512) NOT NULL",
                "FOREIGN KEY (username_ForeignKey) REFERENCES users_info(username)"};
        createTable("main_table", columns);
    }

    public ArrayList<Object[]> load() {
        ArrayList<Object[]> obj = new ArrayList<>();
        String query = String.format("SELECT * FROM main_table WHERE username_ForeignKey = '%s'", Account.getInstance().getUsername());
        try {
            resultSet = statement.executeQuery(query);
            while(resultSet.next()) {
                boolean state = resultSet.getBoolean("include_state");
                String category = resultSet.getString("category");
                String file = resultSet.getString("file_format");
                String date = resultSet.getString("date");
                String source = resultSet.getString("source_folder");
                String destination = resultSet.getString("destination_folder");
                obj.add(new Object[] { state, category, file, date, source, destination} );

            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return obj;
    }

    public void add(boolean include_state, String category, String file, String date, String src, String dest) {
        String newSRC = src.replace('\\','/');
        String newDST = dest.replace('\\','/');
        String query =
                String.format("INSERT INTO main_table " +
                        "(username_ForeignKey, include_state, category, file_format, date, source_folder, destination_folder) " +
                        "VALUES ('%s', %b, '%s', '%s', '%s', '%s', '%s')",
                        Account.getInstance().getUsername(), include_state, category, file, date, newSRC, newDST);
        try {
            statement.execute(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void truncate() {
        String query = String.format("DELETE FROM main_table WHERE username_ForeignKey='%s'",Account.getInstance().getUsername());
        try {
            statement.execute(query);
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    public static MainService getInstance() {
        if(mainService == null) {
            mainService = new MainService();
        }
        return mainService;
    }

}
