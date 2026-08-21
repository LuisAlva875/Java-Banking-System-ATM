package banking.domain;

public class SavingsAccount extends Account {

    private final double interestRate;

    public SavingsAccount(double balance, double interestRate) {
        super(balance);

        if (interestRate < 0) {
            throw new IllegalArgumentException(
                    "La tasa de interés no puede ser negativa.");
        }

        this.interestRate = interestRate;
    }

    public double getInterestRate() {
        return interestRate;
    }
}