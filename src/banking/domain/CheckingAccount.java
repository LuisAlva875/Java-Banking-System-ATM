package banking.domain;

public class CheckingAccount extends Account {

    private double overdraftProtection;

    public CheckingAccount(double balance) {
        this(balance, 0.0);
    }

    public CheckingAccount(double balance, double protect) {
        super(balance);

        if (protect < 0) {
            throw new IllegalArgumentException(
                    "La protección de sobregiro no puede ser negativa.");
        }

        this.overdraftProtection = protect;
    }

    public double getOverdraftProtection() {
        return overdraftProtection;
    }

    @Override
    public void withdraw(double amount) throws OverdraftException {

        if (amount <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad debe ser mayor que cero.");
        }

        if (amount <= balance) {
            balance -= amount;
            return;
        }

        double deficit = amount - balance;

        if (overdraftProtection >= deficit) {

            overdraftProtection -= deficit;
            balance = 0.0;

            System.out.println(
                    "Se utilizó la protección de sobregiro."
            );

        } else {

            throw new OverdraftException(
                    "Fondos insuficientes aun con protección de sobregiro.",
                    deficit - overdraftProtection
            );
        }
    }
}