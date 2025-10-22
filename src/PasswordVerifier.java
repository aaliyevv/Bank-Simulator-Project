
public class PasswordVerifier {

    public static boolean verify(Account account, int pin) {

        return account.verifyPin(pin);
    }

    public static boolean isValidPinFormat(int pin) {
        String p = String.valueOf(pin);
        return p.length() == 4;
    }
}
