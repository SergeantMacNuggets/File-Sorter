package back_end;

public abstract class Account {
    protected static Account account = null;

    protected PasswordService databaseService = new PasswordService("userlogin", "user");

    public static Account getInstance() {return account;}

    public static void clearInstance() {account = null;}

    public static boolean getState() {return account instanceof Admin;}

    public void changePassword(String password) {databaseService.changePassword("user", password);}

    public boolean isEqual(String user, String pass) {return databaseService.authorize(user,pass);}
}

class Admin extends Account {
    private Admin() {}

    public static Account getInstance() {return (account == null) ? account = new Admin() : account;}
}

class Guest extends Account {
    private Guest(){}

    public static Account getInstance() {return (account == null) ? account = new Guest() : account;}
}

