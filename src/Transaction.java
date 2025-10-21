import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Transaction {
    private final int fromAccount;
    private final int toAccount;
    private final double amount;
    private final Currency fromCurrency;
    private final Currency toCurrency;
    private final LocalDateTime timestamp;
    private final double convertedAmount;
    private final TransactionStatus status;
    private final String message;

    public Transaction(int fromAccount, int toAccount, double amount,
                       Currency fromCurrency, Currency toCurrency,
                       double convertedAmount, TransactionStatus status, String message) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.convertedAmount = convertedAmount;
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    @Override
    public String toString() {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.format("[%s] TX from %d (%s %.2f) -> to %d (%s %.2f) : %s",
                timestamp.format(df),
                fromAccount, fromCurrency, amount,
                toAccount, toCurrency, convertedAmount,
                status + (message != null ? " (" + message + ")" : ""));
    }
}
