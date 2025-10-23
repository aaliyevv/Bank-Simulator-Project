import java.util.*;
import java.util.stream.Collectors;

public class AccountManager {
    private final Map<Integer, Account> accounts = new HashMap<>();
    private static final AccountManager INSTANCE = new AccountManager();

    private AccountManager() {}

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


    public boolean verifyPinAndTransfer(int fromId, int pin, int toId, double amount, Currency currency) {
        Account from = getAccount(fromId);
        Account to = getAccount(toId);
        if (from == null || to == null) {
            System.out.println("Account not found.");
            return false;
        }
        if (!from.verifyPin(pin)) {
            System.out.println("PIN verification failed.");
            TransactionHistory.getInstance().add(new Transaction(fromId, toId, amount, currency, currency, 0.0, TransactionStatus.FAILED, "PIN failed"));
            return false;
        }
        return transfer(from, to, amount, currency);
    }


    public boolean transfer(Account from, Account to, double amount, Currency currency) {
        Account firstLock = from.getId() < to.getId() ? from : to; //prevent deadlock and determine first
        Account secondLock = from.getId() < to.getId() ? to : from;

        synchronized (firstLock) {
            synchronized (secondLock) {
                CurrencyConverter conv = CurrencyConverter.getInstance();
                double converted = conv.convert(amount, currency, currency);
                boolean withdrawn = from.withdraw(currency, amount);
                if (!withdrawn) {
                    String msg = "Insufficient funds";
                    TransactionHistory.getInstance().add(new Transaction(from.getId(), to.getId(), amount, currency, currency, 0.0, TransactionStatus.FAILED, msg));
                    System.out.println(msg);
                    return false;
                }

                Currency toPrimary = choosePrimaryCurrency(to);
                converted = CurrencyConverter.getInstance().convert(amount, currency, toPrimary);
                to.deposit(toPrimary, converted);

                TransactionHistory.getInstance().add(new Transaction(from.getId(), to.getId(), amount, currency, toPrimary, converted, TransactionStatus.SUCCESS, null));
                System.out.println("Transfer completed: from " + from.getId() + " to " + to.getId() + ". " + amount + " " + currency + " -> " + String.format("%.2f", converted) + " " + toPrimary);
                return true;
            }
        }
    }

    private Currency choosePrimaryCurrency(Account acc) {
        Map<Currency, Double> snap = acc.snapshotBalances();
        return snap.entrySet().stream()
                .max(Comparator.comparingDouble(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .orElse(Currency.USD);
    }

    public void printTransactionHistoryToConsole() {

        TransactionHistory.getInstance().printToConsole();
    }
}
