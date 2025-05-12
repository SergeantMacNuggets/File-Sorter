package gui;

enum AccountType {
    ADMIN,
    GUEST
}

public abstract class Account {
    protected static Account account = null;
    public abstract boolean isEqual(String user, String pass);

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
    String username = "user";
    String password = "user";
    private Admin() {}

    public boolean isEqual(String user, String pass) {
        return (username.equals(user) && password.equals(pass));
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

    static Account getInstance() {
        return (account == null) ? account = new Guest() : account;
    }
}
