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

        boolean running = true;
        while (running) {
            System.out.print("\nEnter command (menu for options): ");
            String cmd = scanner.nextLine().trim().toLowerCase();
            switch (cmd) {
                case "menu":
                case "help":
                    printHelp();
                    break;
                case "create":
                    interactiveCreate();
                    break;
                case "list":
                    manager.printAllAccountsToConsole();
                    break;
                case "transfer":
                    interactiveTransfer();
                    break;
                case "history":
                    manager.printTransactionHistoryToConsole();
                    break;
                case "suggest":
                    SuggestionEngine.suggestForAllAccounts();
                    break;
                case "batch":
                    System.out.print("Enter input file path: ");
                    String inPath = scanner.nextLine().trim();
                    System.out.print("Enter output file path: ");
                    String outPath = scanner.nextLine().trim();
                    File inFile = new File(inPath);
                    if (!inFile.exists()) {
                        System.out.println("Input file does not exist.");
                    } else {
                        InputProcessor processor = new InputProcessor(inFile, outPath, executor);
                        executor.submit(processor::process);
                    }
                    break;
            }
        }
    }
}