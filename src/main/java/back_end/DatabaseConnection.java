package back_end;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileWriter;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class DatabaseConnection {
    private static final JSONObject jsonObject = new JSONObject();
    private static String username;
    private static String password;

    public static void run () {
        try {
            FileReader fileReader = new FileReader("mySQL_Info.json");
            Object parse = new JSONParser().parse(fileReader);
            JSONObject jb = (JSONObject) parse;
            checkConnection((String) jb.get("user"),(String) jb.get("password"));
            username = (String) jb.get("user");
            password = (String) jb.get("password");
        } catch (FileNotFoundException e) {
            write();
            run();
        } catch (IOException e) {
            write();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getUser() {return username;}
    public static String getPassword() {return password;}

    @SuppressWarnings("unchecked")
    public static void write() {
        try {
            String user = OptionPane(new JTextField(10));
            String password = OptionPane(new JPasswordField(10));
            jsonObject.put("user",user);
            jsonObject.put("password",password);
            checkConnection(user,password);
            FileWriter fileWriter = new FileWriter("mySQL_Info.json");
            fileWriter.write(jsonObject.toString());
            fileWriter.close();
        } catch (IOException e) {
            write();
        }
    }

    private static void checkConnection(String username, String password) throws IOException{
        try {
            DriverManager.getConnection("jdbc:mysql://localhost:3306", username,password);
        } catch (SQLException e) {
            throw new IOException(e);
        }
    }

    private static String OptionPane(JTextField textField) {
        String[] options = {"OK"};
        JPanel panel = new JPanel();
        String labelInstance = (textField instanceof JPasswordField) ? "Password" : "Username";
        JLabel lbl = new JLabel(String.format("Enter Your MySQL %s:", labelInstance));
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
