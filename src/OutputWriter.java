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

                }
            }
        }
    }
}