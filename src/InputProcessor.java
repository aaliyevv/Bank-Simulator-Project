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

                }
            }
        }
    }
}
