package banking.data;

import banking.domain.Account;
import banking.domain.Bank;
import banking.domain.CheckingAccount;
import banking.domain.Customer;
import banking.domain.DuplicateCustomerException;
import banking.domain.SavingsAccount;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ListIterator;
import java.util.Scanner;

public class DataSource {

    private final File dataFile;

    public DataSource(String dataFilePath) {
        this.dataFile = new File(dataFilePath);
    }

    public void loadData()
            throws FileNotFoundException,
            DuplicateCustomerException {

        if (!dataFile.exists()) {
            throw new FileNotFoundException(
                    "No existe el archivo: " + dataFile.getAbsolutePath()
            );
        }

        Bank bank = Bank.getBank();

        // Evita duplicar los clientes si se vuelve a cargar el archivo.
        bank.clear();

        Scanner sc = new Scanner(dataFile);

        try {

            if (!sc.hasNextInt()) {
                throw new IllegalArgumentException(
                        "El archivo no contiene el número de clientes."
                );
            }

            int numOfCustomers = sc.nextInt();

            for (int i = 0; i < numOfCustomers; i++) {

                if (!sc.hasNextInt()) {
                    throw new IllegalArgumentException(
                            "Falta el número del cliente."
                    );
                }

                int customerId = sc.nextInt();

                String firstName = sc.next();
                String lastName = sc.next();
                String pin = sc.next();

                int numOfAccounts = sc.nextInt();

                bank.addCustomer(
                        customerId,
                        firstName,
                        lastName,
                        pin
                );

                Customer customer =
                        bank.findCustomer(customerId);

                for (int j = 0; j < numOfAccounts; j++) {

                    String accountType = sc.next();

                    switch (accountType) {

                        case "S": {

                            double balance = sc.nextDouble();
                            double interestRate = sc.nextDouble();

                            customer.addAccount(
                                    new SavingsAccount(
                                            balance,
                                            interestRate
                                    )
                            );

                            break;
                        }

                        case "C": {

                            double balance = sc.nextDouble();
                            double overdraftProtection =
                                    sc.nextDouble();

                            customer.addAccount(
                                    new CheckingAccount(
                                            balance,
                                            overdraftProtection
                                    )
                            );

                            break;
                        }

                        default:

                            throw new IllegalArgumentException(
                                    "Tipo de cuenta desconocido: "
                                    + accountType
                            );
                    }
                }
            }

        } finally {
            sc.close();
        }
    }

    public void saveData()
            throws FileNotFoundException {

        File parent = dataFile.getParentFile();

        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        PrintWriter writer = new PrintWriter(dataFile);

        try {

            Bank bank = Bank.getBank();

            writer.println(bank.getNumOfCustomers());

            ListIterator<Customer> bankIterator =
                    bank.getCustomers();

            while (bankIterator.hasNext()) {

                Customer customer = bankIterator.next();

                writer.println(
                        customer.getCustomerId()
                        + " "
                        + customer.getFirstName()
                        + " "
                        + customer.getLastName()
                        + " "
                        + customer.getPin()
                        + " "
                        + customer.getNumOfAccounts()
                );

                ListIterator<Account> accountIterator =
                        customer.getAccounts();

                while (accountIterator.hasNext()) {

                    Account account = accountIterator.next();

                    if (account instanceof SavingsAccount) {

                        SavingsAccount savings =
                                (SavingsAccount) account;

                        writer.println(
                                "S "
                                + savings.getBalance()
                                + " "
                                + savings.getInterestRate()
                        );

                    } else if (account instanceof CheckingAccount) {

                        CheckingAccount checking =
                                (CheckingAccount) account;

                        writer.println(
                                "C "
                                + checking.getBalance()
                                + " "
                                + checking.getOverdraftProtection()
                        );
                    }
                }
            }

        } finally {
            writer.close();
        }
    }
}