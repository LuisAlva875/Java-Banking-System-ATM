package banking.domain;

import java.util.ArrayList;
import java.util.ListIterator;

public class Customer {

    private final int customerId;
    private final String firstName;
    private final String lastName;
    private String pin;

    protected ArrayList<Account> accounts;

    public Customer(int customerId, String firstName, String lastName, String pin) {
        if (customerId <= 0) {
            throw new IllegalArgumentException("El número de cliente debe ser mayor que cero.");
        }

        validatePin(pin);

        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.pin = pin;
        this.accounts = new ArrayList<>();
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPin() {
        return pin;
    }

    public boolean authenticate(String enteredPin) {
        return pin.equals(enteredPin);
    }

    public void changePin(String currentPin, String newPin)
            throws InvalidPinException {

        if (!authenticate(currentPin)) {
            throw new InvalidPinException("El NIP actual es incorrecto.");
        }

        validatePin(newPin);

        if (pin.equals(newPin)) {
            throw new InvalidPinException(
                    "El nuevo NIP debe ser diferente al NIP actual.");
        }

        pin = newPin;
    }

    public void resetPin(String newPin) {
        validatePin(newPin);
        pin = newPin;
    }

    private void validatePin(String pin) {
        if (pin == null || !pin.matches("\\d{4}")) {
            throw new IllegalArgumentException(
                    "El NIP debe contener exactamente 4 dígitos.");
        }
    }

    public Account getAccount(int n) {
        return accounts.get(n);
    }

    public void addAccount(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("La cuenta no puede ser null.");
        }

        accounts.add(account);
    }

    public int getNumOfAccounts() {
        return accounts.size();
    }

    public ListIterator<Account> getAccounts() {
        return accounts.listIterator();
    }
}