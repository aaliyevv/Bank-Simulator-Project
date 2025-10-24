import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class OutputWriter {
    private static String defaultOutputFile = "bank_snapshot.txt";

    public static String getDefaultOutputFile() {

        return defaultOutputFile;
    }

    public static void setDefaultOutputFile(String path) {

        defaultOutputFile = path;
    }

    public static void dumpSnapshotToFile(String path) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            StringBuilder sb = new StringBuilder();
            sb.append("=== Accounts Snapshot ===").append(System.lineSeparator());
            List<Account> accounts = AccountManager.getInstance().listAccounts();
            if (accounts.isEmpty()) {
                sb.append("No accounts").append(System.lineSeparator());
            } else {
                for (Account a : accounts) {
                    sb.append(a.toString()).append(System.lineSeparator());
                    sb.append("Balances detail:").append(System.lineSeparator());
                    for (Map.Entry<Currency, Double> e : a.snapshotBalances().entrySet()) {
                        sb.append("  ")
                                .append(e.getKey())
                                .append(": ")
                                .append(String.format("%.2f", e.getValue()))
                                .append(System.lineSeparator());
                    }
                }
            }
            sb.append(System.lineSeparator()).append("=== Transactions ===").append(System.lineSeparator());
            for (Transaction t : TransactionHistory.getInstance().snapshot()) {
                sb.append(t.toString()).append(System.lineSeparator());
            }
            bw.write(sb.toString());
            System.out.println("Snapshot saved to " + path);
        } catch (IOException e) {
            System.err.println("Failed to write snapshot: " + e.getMessage());
        }
    }
}
