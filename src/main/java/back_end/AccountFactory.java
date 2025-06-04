package back_end;

public abstract class AccountFactory {
    public static Account getAccount(AccountType acc) {
        return switch (acc) {
            case USER -> User.getInstance();
            case GUEST -> Guest.getInstance();
        };
    }
}
