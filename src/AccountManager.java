import java.util.*;
import java.util.stream.Collectors;

public class AccountManager {
    private final Map<Integer, Account> accounts = new HashMap<>();
    private static final AccountManager INSTANCE = new AccountManager();

    private AccountManager() {
    }

    public static AccountManager getInstance() {

        return INSTANCE;
    }

    public Account createAccount(String owner, Currency currency, double initialAmount, int pin) {
        Account acc = AccountFactory.create(owner, currency, initialAmount, pin);
        synchronized (accounts) {
            accounts.put(acc.getId(), acc);
        }
        return acc;
    }

    public Account getAccount(int id) {
        synchronized (accounts) {
            return accounts.get(id);
        }
    }

    public List<Account> listAccounts() {
        synchronized (accounts) {
            return new ArrayList<>(accounts.values());
        }
    }

    public void printAllAccountsToConsole() {
        List<Account> list = listAccounts();
        if (list.isEmpty()) {
            System.out.println("No accounts.");
            return;
        }
        list.forEach(a -> System.out.println(a));
    }


}