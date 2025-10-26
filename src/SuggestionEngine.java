import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SuggestionEngine {

    public static void suggestForAllAccounts() {
        List<Account> accounts = AccountManager.getInstance().listAccounts();
        if (accounts.isEmpty()) {
            System.out.println("No accounts to suggest for.");
            return;
        }

        accounts.stream()
                .sorted(Comparator.comparing(Account::getId))
                .forEach(SuggestionEngine::suggestForAccount);
    }

    public static void suggestForAccount(Account acc) {
        CurrencyConverter conv = CurrencyConverter.getInstance();
        double totalUsd = acc.snapshotBalances().entrySet().stream()
                .mapToDouble(e -> conv.convert(e.getValue(), e.getKey(), Currency.USD))
                .sum();

        String suggestion = (totalUsd < 1000)
                ? "Consider a short-term savings plan. Aim to save 20% of monthly income."
                : (totalUsd < 5000)
                ? "Consider a 6-12 month deposit product or low-risk investment."
                : "You may explore diversified investments: index funds, bonds, or consult an advisor.";

        StringBuilder sb = new StringBuilder();
        sb.append("Suggestion for Account ")
                .append(acc.getId()).append(" (owner: ").append(acc.getOwnerName()).append("): ");
        sb.append(String.format("Total (USD): %.2f. ", totalUsd));
        sb.append(suggestion);

        System.out.println(sb.toString());
    }
}
