package banking.domain;

import java.util.ArrayList;
import java.util.ListIterator;

public class Bank {

    private static final Bank bank = new Bank();

    private final ArrayList<Customer> customers;

    private Bank() {
        customers = new ArrayList<>();
    }

    public static Bank getBank() {
        return bank;
    }

    public void addCustomer(
            int customerId,
            String firstName,
            String lastName,
            String pin)
            throws DuplicateCustomerException {

        if (findCustomer(customerId) != null) {
            throw new DuplicateCustomerException(
                    "El número de cliente " + customerId + " ya existe.");
        }

        customers.add(
                new Customer(customerId, firstName, lastName, pin)
        );
    }

    public Customer findCustomer(int customerId) {
        for (Customer customer : customers) {
            if (customer.getCustomerId() == customerId) {
                return customer;
            }
        }

        return null;
    }

    public Customer getCustomer(int index) {
        return customers.get(index);
    }

    public int getNumOfCustomers() {
        return customers.size();
    }

    public ListIterator<Customer> getCustomers() {
        return customers.listIterator();
    }

    public int getNextCustomerId() {
        int max = 0;

        for (Customer customer : customers) {
            if (customer.getCustomerId() > max) {
                max = customer.getCustomerId();
            }
        }

        return max + 1;
    }

    public void clear() {
        customers.clear();
    }
}