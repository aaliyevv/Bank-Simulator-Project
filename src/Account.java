import java.text.DecimalFormat;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;


public class Account {
    private static final AtomicInteger idGenerator = new AtomicInteger(1000);
    private final int id;
    private final String ownerName;
    private final Map<Currency, Double> balances = new EnumMap<>(Currency.class);
    private Integer pin;

    public Account(String ownerName, Currency currency, double initialAmount, int pin) {
        this.id = idGenerator.getAndIncrement();
        this.ownerName = ownerName;
        for (Currency c : Currency.values()) balances.put(c, 0.0);
        balances.put(currency, initialAmount);
        this.pin = Integer.valueOf(pin); // explicit wrapper usage
    }

    public int getId() {

        return id;
    }

    public String getOwnerName() {

        return ownerName;
    }

    public synchronized double getBalance(Currency currency) {

        return balances.getOrDefault(currency, 0.0);
    }

    public synchronized void deposit(Currency currency, double amount) {
        double old = balances.getOrDefault(currency, 0.0);
        balances.put(currency, old + amount);
    }

