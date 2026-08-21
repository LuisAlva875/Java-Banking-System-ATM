package banking.reports;

import banking.domain.Account;
import banking.domain.Bank;
import banking.domain.CheckingAccount;
import banking.domain.Customer;
import banking.domain.SavingsAccount;

import java.text.NumberFormat;
import java.util.ListIterator;

public class CustomerReport {

    public void generateReport() {

        NumberFormat currencyFormat =
                NumberFormat.getCurrencyInstance();

        Bank bank = Bank.getBank();

        System.out.println();
        System.out.println("\t\t\tREPORTE DE CLIENTES");
        System.out.println("\t\t\t===================");
        System.out.println();

        ListIterator<Customer> bankIterator =
                bank.getCustomers();

        while (bankIterator.hasNext()) {

            Customer customer =
                    bankIterator.next();

            System.out.println(
                    "Cliente: "
                    + customer.getCustomerId()
                    + " - "
                    + customer.getLastName()
                    + ", "
                    + customer.getFirstName()
            );

            ListIterator<Account> accountIterator =
                    customer.getAccounts();

            int accountNumber = 1;

            while (accountIterator.hasNext()) {

                Account account =
                        accountIterator.next();

                String accountType;

                if (account instanceof SavingsAccount) {
                    accountType = "Savings Account";
                } else if (account instanceof CheckingAccount) {
                    accountType = "Checking Account";
                } else {
                    accountType = "Unknown Account Type";
                }

                System.out.println(
                        "    Cuenta "
                        + accountNumber
                        + " - "
                        + accountType
                        + ": "
                        + currencyFormat.format(
                                account.getBalance()
                        )
                );

                accountNumber++;
            }

            System.out.println();
        }
    }
}