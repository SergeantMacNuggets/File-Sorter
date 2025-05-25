package gui;

import org.mindrot.jbcrypt.BCrypt;

enum AccountType {
    ADMIN,
    GUEST
}

public abstract class Account {
    protected static Account account = null;
    public abstract boolean isEqual(String user, String pass);
    public abstract void changePassword(String newPass);
    protected String username = "user";
    protected String password = BCrypt.hashpw("user", BCrypt.gensalt());

    static Account getInstance() {
        return account;
    }
    static void clearInstance() {
        account = null;
    }
    static boolean getState() {
        return account instanceof Admin;
    }
}

abstract class AccountFactory {
    public static Account getAccount(AccountType acc) {
        return switch (acc) {
            case ADMIN -> Admin.getInstance();
            case GUEST -> Guest.getInstance();
        };
    }
}

class Admin extends Account {
    private Admin() {}

    public boolean isEqual(String user, String pass) {
        return (username.equals(user) && BCrypt.checkpw(pass, password));
    }

    public void changePassword(String newPass) {
        super.password = BCrypt.hashpw(newPass, BCrypt.gensalt());
    }

    static Account getInstance() {
        return (account == null) ? account = new Admin() : account;
    }
}

class Guest extends Account {
    private Guest(){}

    @Override
    public boolean isEqual(String user, String pass) {
        return false;
    }

    @Override
    public void changePassword(String newPass) {

    }

    static Account getInstance() {
        return (account == null) ? account = new Guest() : account;
    }
}
