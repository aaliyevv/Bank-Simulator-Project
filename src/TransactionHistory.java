import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class TransactionHistory {
    private final List<Transaction> history = new ArrayList<>();

    private static final TransactionHistory INSTANCE = new TransactionHistory();

    private TransactionHistory() {}

    public static TransactionHistory getInstance() {

        return INSTANCE;
    }

    public void add(Transaction tx) {
        synchronized (history) {
            history.add(tx);
        }
    }

    public List<Transaction> snapshot() {
        synchronized (history) {
            return new ArrayList<>(history);
        }
    }

    public void printToConsole() {
        List<Transaction> snap = snapshot();
        if (snap.isEmpty()) {
            System.out.println("No transactions yet.");
            return;
        }
        StringBuffer sb = new StringBuffer();
        for (Transaction t : snap) {
            sb.append(t.toString()).append(System.lineSeparator());
        }

        System.out.println(sb.toString());
    }
}
