import java.io.File;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final AccountManager manager = AccountManager.getInstance();
    private static final ExecutorService executor = Executors.newCachedThreadPool();

    public static void main(String[] args) {
        System.out.println("=== Bank Operations Simulator ===");
        printHelp();

        executor.submit(() -> GCMonitor.runGcDemo());
    }
}