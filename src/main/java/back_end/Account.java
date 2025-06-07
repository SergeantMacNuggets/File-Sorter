package back_end;

import java.sql.SQLException;

public abstract class Account {
    protected static Account account = null;

    protected String username = "";

    protected static PasswordService databaseService = new PasswordService("users_info");

    public static void clearInstance() {account = null;}

    public static Account getInstance() {return account;}

    public boolean getState() {return account instanceof User;}

    public static void changePassword(String username,String password) {databaseService.changePassword(username, password);}

    public static boolean isEqual(String user, String pass) {return databaseService.authorize(user,pass);}

    public void setUsername(String username) {this.username = username;}

    public String getUsername() {return username;}

    public static boolean isEqual(String user) {return databaseService.authorizeGuest(user);}

    public static boolean isTableEmpty() {return databaseService.isTableEmpty();}

    public static void addUser(String user, String pass) throws SQLException {databaseService.addUser(user,pass);}
}

class User extends Account {
    private User() {}

    public static Account getInstance() {return (account == null) ? account = new User() : account;}
}

class Guest extends Account {
    private Guest(){}

    public static Account getInstance() {return (account == null) ? account = new Guest() : account;}
}

