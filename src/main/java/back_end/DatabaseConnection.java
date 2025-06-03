package back_end;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.mindrot.jbcrypt.BCrypt;

import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import java.io.*;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;

public abstract class DatabaseConnection {
    private static final JSONObject jsonObject = new JSONObject();
    private static Connection connection;
    private static String hostname;
    public static void run () {
        try {
            FileReader fileReader = new FileReader("mySQL_Info.json");
            Object parse = new JSONParser().parse(fileReader);
            JSONObject jb = (JSONObject) parse;

            checkConnection((String) jb.get("hostname"),
                            (String) jb.get("user"),
                            (String) jb.get("password"));

            hostname = (String) jb.get(hostname);

        } catch (FileNotFoundException e) {
            write();
            run();
        } catch (IOException e) {
            write();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    @SuppressWarnings("unchecked")
    public static void write() {
        try {
            String hostname = OptionPane(new JTextField(10), "Hostname");
            String user = OptionPane(new JTextField(10), "Username");
            String password = OptionPane(new JPasswordField(10), "Password");
            jsonObject.put("hostname", hostname);
            jsonObject.put("user",user);
            jsonObject.put("password", password);
            checkConnection(hostname, user, password);

            FileWriter fileWriter = new FileWriter("mySQL_Info.json");
            fileWriter.write(jsonObject.toString());
            fileWriter.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Database host connection failed!");
            System.exit(0);
        }
    }


    private static void checkConnection(String hostname, String username, String password) throws IOException{
        try {
            connection = DriverManager.getConnection(String.format("jdbc:mysql://%s",hostname), username,password);
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    private static String OptionPane(JTextField textField, String opt) {
        String[] options = {"OK"};
        JPanel panel = new JPanel();
        JLabel lbl = new JLabel(String.format("Enter Your MySQL %s:\n", opt));
        JTextField txt = textField;
        panel.add(lbl);
        panel.add(txt);
        int selectedOption = JOptionPane.showOptionDialog(null, panel, "The Title", JOptionPane.NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options , options[0]);

        if(selectedOption == 0)
        {
            return txt.getText();
        }
        return "";
    }
}
