import java.io.*;
import java.util.concurrent.ExecutorService;

public class InputProcessor {
    private final File inputFile;
    private final String outputFilePath;
    private final ExecutorService executor;

    public InputProcessor(File inputFile, String outputFilePath, ExecutorService executor) {
        this.inputFile = inputFile;
        this.outputFilePath = outputFilePath;
        this.executor = executor;
    }

    public void process() {
        System.out.println("Processing input file: " + inputFile.getAbsolutePath());
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
             BufferedWriter bw = new BufferedWriter(new FileWriter(outputFilePath))) {

            String line;
            while ((line = br.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.isEmpty() || trimmed.startsWith("#")) continue;
                String[] parts = trimmed.split("\\|");
                String cmd = parts[0].toUpperCase();
                switch (cmd) {
                    case "CREATE":
                        if (parts.length < 5) {
                            bw.write("BAD CREATE: " + line + System.lineSeparator());
                            break;
                        }
                        String owner = parts[1];
                        Currency c = Currency.valueOf(parts[2].toUpperCase());
                        double amt = Double.parseDouble(parts[3]);
                        int pin = Integer.parseInt(parts[4]);
                        Account acc = AccountManager.getInstance().createAccount(owner, c, amt, pin);
                        bw.write("CREATED|" + acc.getId() + "|" + owner + System.lineSeparator());
                        break;
                    case "TRANSFER":
                        if (parts.length < 6) {
                            bw.write("BAD TRANSFER: " + line + System.lineSeparator());
                            break;
                        }
                        int from = Integer.parseInt(parts[1]);
                        int to = Integer.parseInt(parts[2]);
                        double amount = Double.parseDouble(parts[3]);
                        Currency currency = Currency.valueOf(parts[4].toUpperCase());
                        int pin2 = Integer.parseInt(parts[5]);
                        executor.submit(() -> {
                            boolean ok = AccountManager.getInstance().verifyPinAndTransfer(from, pin2, to, amount, currency);
                            try {
                                synchronized (bw) {
                                    bw.write("TRANSFER|" + from + "|" + to + "|" + amount + "|" + currency + "|" + (ok ? "OK" : "FAILED") + System.lineSeparator());
                                    bw.flush();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        break;
                    default:
                        bw.write("UNKNOWN CMD: " + line + System.lineSeparator());
                }
            }
            bw.write("BATCH_PROCESS_COMPLETE" + System.lineSeparator());
            System.out.println("Input processing submitted tasks; results will be in: " + outputFilePath);
        } catch (Exception e) {
            System.err.println("Error processing input: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
