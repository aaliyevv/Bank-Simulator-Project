
public class AccountFactory {
    public static Account create(String owner, Currency currency, double initialAmount, int pin) {
        return new Account(owner, currency, initialAmount, pin);
    }
}