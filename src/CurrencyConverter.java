import java.util.EnumMap;
import java.util.Map;

public class CurrencyConverter {
    private static final CurrencyConverter INSTANCE = new CurrencyConverter();

    private final Map<Currency, Double> toUsdRates = new EnumMap<>(Currency.class);

    private CurrencyConverter() {
        toUsdRates.put(Currency.USD, 1.0);
        toUsdRates.put(Currency.EUR, 1.20);
        toUsdRates.put(Currency.AZN, 0.59);
        toUsdRates.put(Currency.GBP, 1.34);
    }

    public static CurrencyConverter getInstance() {

        return INSTANCE;
    }

    public double convert(double amount, Currency from, Currency to) {
        if (from == to) return amount;
        Double fromUsd = toUsdRates.get(from);
        Double toUsd = toUsdRates.get(to);
        if (fromUsd == null || toUsd == null) throw new IllegalArgumentException("Unsupported currency");
        double amountInUsd = amount * fromUsd;
        double result = amountInUsd / toUsd;
        return result;
    }

    public synchronized void updateRate(Currency currency, double toUsd) {

        toUsdRates.put(currency, toUsd);
    }
}
