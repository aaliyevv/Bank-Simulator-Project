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
    }
}
