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
                case "save":
                    System.out.print("Enter output file path to save snapshot: ");
                    String out = scanner.nextLine().trim();
                    OutputWriter.dumpSnapshotToFile(out);
                    break;
                case "exit":
                case "quit":
                    running = false;
                    break;
                default:
                    System.out.println("Unknown command. Type 'menu' to see options.");
            }
        }

        shutdown();
        System.out.println("Simulator exited. Goodbye!");
    }

    private static void printHelp() {
        System.out.println("Commands:");
        System.out.println("  create   - Create a new account");
        System.out.println("  list     - List all accounts");
        System.out.println("  transfer - Transfer between accounts");
        System.out.println("  history  - Show transaction history");
        System.out.println("  suggest  - Show savings/investment suggestions");
        System.out.println("  batch    - Process batch commands from input file");
        System.out.println("  save     - Save accounts and transactions snapshot to output file");
        System.out.println("  exit     - Exit application");
    }

    private static void interactiveCreate() {
        System.out.print("Enter owner name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter initial currency (USD, EUR, AZN, GBP): ");
        String cur = scanner.nextLine().trim().toUpperCase();
        Currency currency;
        try {
            currency = Currency.valueOf(cur);
        } catch (Exception e) {
            System.out.println("Invalid currency. Defaulting to USD.");
            currency = Currency.USD;
        }
        System.out.print("Enter initial amount: ");
        double amount = Double.parseDouble(scanner.nextLine().trim());
        System.out.print("Set numeric PIN (4 digits): ");
        int pin = Integer.parseInt(scanner.nextLine().trim());

        Account acc = manager.createAccount(name, currency, amount, pin);
        System.out.println("Account created: " + acc);
    }

    private static void interactiveTransfer() {
        try {
            System.out.print("From Account ID: ");
            int from = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("To Account ID: ");
            int to = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Amount: ");
            double amt = Double.parseDouble(scanner.nextLine().trim());
            System.out.print("Currency of amount (USD, EUR, AZN, GBP): ");
            Currency c = Currency.valueOf(scanner.nextLine().trim().toUpperCase());

            System.out.print("Enter PIN for account " + from + ": ");
            int pin = Integer.parseInt(scanner.nextLine().trim());

            executor.submit(() -> {
                boolean ok = manager.verifyPinAndTransfer(from, pin, to, amt, c);
                if (ok) System.out.println("Transfer thread completed successfully.");
                else System.out.println("Transfer thread failed.");
            });

        } catch (Exception e) {
            System.out.println("Invalid input: " + e.getMessage());
        }
    }

    private static void shutdown() {
        executor.shutdownNow();
        OutputWriter.setDefaultOutputFile("bank_snapshot.txt");
        OutputWriter.dumpSnapshotToFile(OutputWriter.getDefaultOutputFile());
    }
}
