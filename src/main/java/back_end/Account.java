package back_end;

public abstract class Account {
    protected static Account account = null;

    protected PasswordService databaseService = new PasswordService("userlogin", "user") {{addUser("admin","admin");}};

    public static Account getInstance() {return account;}

    public static void clearInstance() {account = null;}

    public static boolean getState() {return account instanceof User;}

    public void changePassword(String username,String password) {databaseService.changePassword(username, password);}

    public boolean isEqual(String user, String pass) {return databaseService.authorize(user,pass);}

    public void addUser(String user, String pass) {
        databaseService.addUser(user,pass);
    }
}

class User extends Account {
    private User() {}

    public static Account getInstance() {return (account == null) ? account = new User() : account;}
}

class Guest extends Account {
    private Guest(){}

    public static Account getInstance() {return (account == null) ? account = new Guest() : account;}
}

