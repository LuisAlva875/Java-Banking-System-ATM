package banking.domain;

public class Account {

    protected double balance;

    public Account(double balance) {
        if (balance < 0) {
            throw new IllegalArgumentException(
                    "El saldo no puede ser negativo.");
        }

        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }

    public boolean deposit(double amount) {

        if (amount <= 0) {
            return false;
        }

        balance += amount;
        return true;
    }

    public void withdraw(double amount) throws OverdraftException {

        if (amount <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad debe ser mayor que cero.");
        }

        if (amount > balance) {
            throw new OverdraftException(
                    "Fondos insuficientes.",
                    amount - balance
            );
        }

        balance -= amount;
    }
}