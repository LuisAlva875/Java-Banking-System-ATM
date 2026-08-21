package banking.domain;

public class InvalidCustomerException extends Exception {

    public InvalidCustomerException(String message) {
        super(message);
    }
}