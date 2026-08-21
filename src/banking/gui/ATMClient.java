package banking.gui;

import banking.data.DataSource;
import banking.domain.Account;
import banking.domain.Bank;
import banking.domain.CheckingAccount;
import banking.domain.Customer;
import banking.domain.DuplicateCustomerException;
import banking.domain.InvalidPinException;
import banking.domain.OverdraftException;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.FileNotFoundException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ATMClient {

    private static final String DEFAULT_DATA_FILE =
            "input/BankData.txt";

    private static final int MAX_PIN_ATTEMPTS = 3;

    private final DataSource dataSource;

    private JFrame frame;

    private JTextArea outputArea;

    private JTextField customerIdField;

    private JPasswordField pinField;

    private Customer currentCustomer;

    private JComboBox<String> accountComboBox;

    public ATMClient(String dataFilePath) {

        dataSource =
                new DataSource(dataFilePath);

        loadData();

        createLoginWindow();
    }

    private void loadData() {

        try {

            dataSource.loadData();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    null,
                    "No fue posible cargar los datos.\n\n"
                    + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            System.exit(1);
        }
    }

    private void createLoginWindow() {

        frame = new JFrame("Bank Ltd - ATM");

        frame.setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        JPanel panel =
                new JPanel(
                        new GridLayout(5, 2, 10, 10)
                );

        panel.setBorder(
                javax.swing.BorderFactory
                        .createEmptyBorder(
                                15,
                                15,
                                15,
                                15
                        )
        );

        customerIdField =
                new JTextField();

        pinField =
                new JPasswordField();

        JButton loginButton =
                new JButton("Ingresar");

        JButton recoverPinButton =
                new JButton("Recuperar NIP");

        JButton registerButton =
                new JButton("Alta de cliente");

        panel.add(
                new JLabel("Número de cliente:")
        );

        panel.add(customerIdField);

        panel.add(
                new JLabel("NIP:")
        );

        panel.add(pinField);

        panel.add(new JLabel());

        panel.add(loginButton);

        panel.add(new JLabel());

        panel.add(recoverPinButton);

        panel.add(new JLabel());

        panel.add(registerButton);

        loginButton.addActionListener(
                e -> login()
        );

        recoverPinButton.addActionListener(
                e -> recoverPin()
        );

        registerButton.addActionListener(
                e -> registerCustomer()
        );

        frame.add(panel);

        frame.pack();

        frame.setLocationRelativeTo(null);

        frame.setResizable(false);

        frame.setVisible(true);
    }

    private void login() {

        int customerId;

        try {

            customerId =
                    Integer.parseInt(
                            customerIdField.getText().trim()
                    );

        } catch (NumberFormatException e) {

            showError(
                    "El número de cliente debe ser numérico."
            );

            return;
        }

        Customer customer =
                Bank.getBank()
                        .findCustomer(customerId);

        if (customer == null) {

            showError(
                    "El cliente no existe."
            );

            return;
        }

        String enteredPin =
                new String(
                        pinField.getPassword()
                );

        int attempts = 0;

        while (attempts < MAX_PIN_ATTEMPTS) {

            if (customer.authenticate(enteredPin)) {

                currentCustomer = customer;

                pinField.setText("");

                openCustomerMenu();

                return;
            }

            attempts++;

            if (attempts >= MAX_PIN_ATTEMPTS) {

                showError(
                        "Se alcanzó el máximo de "
                        + MAX_PIN_ATTEMPTS
                        + " intentos.\n"
                        + "Debe volver a iniciar el proceso."
                );

                pinField.setText("");

                return;
            }

            String message =
                    "NIP incorrecto.\n"
                    + "Intentos restantes: "
                    + (MAX_PIN_ATTEMPTS - attempts);

            showError(message);

            enteredPin =
                    JOptionPane.showInputDialog(
                            frame,
                            "Ingrese nuevamente su NIP:"
                    );

            if (enteredPin == null) {
                return;
            }
        }
    }

    private void openCustomerMenu() {

        frame.getContentPane().removeAll();

        frame.setTitle(
                "Bank Ltd - "
                + currentCustomer.getFirstName()
                + " "
                + currentCustomer.getLastName()
        );

        JPanel leftPanel =
                new JPanel(
                        new GridLayout(7, 1, 5, 5)
                );

        JButton balanceButton =
                new JButton("Saldo");

        JButton depositButton =
                new JButton("Depósito");

        JButton withdrawButton =
                new JButton("Retiro");

        JButton changePinButton =
                new JButton("Cambiar NIP");

        JButton logoutButton =
                new JButton("Cerrar sesión");

        accountComboBox =
                new JComboBox<>();

        for (int i = 0;
                i < currentCustomer.getNumOfAccounts();
                i++) {

            accountComboBox.addItem(
                    "Cuenta " + (i + 1)
            );
        }

        leftPanel.add(
                new JLabel("Seleccione una cuenta:")
        );

        leftPanel.add(accountComboBox);

        leftPanel.add(balanceButton);

        leftPanel.add(depositButton);

        leftPanel.add(withdrawButton);

        leftPanel.add(changePinButton);

        leftPanel.add(logoutButton);

        outputArea =
                new JTextArea(15, 35);

        outputArea.setEditable(false);

        outputArea.setText(
                "Bienvenido, "
                + currentCustomer.getFirstName()
                + " "
                + currentCustomer.getLastName()
                + ".\n\n"
                + "Seleccione una operación."
        );

        balanceButton.addActionListener(
                e -> showBalance()
        );

        depositButton.addActionListener(
                e -> deposit()
        );

        withdrawButton.addActionListener(
                e -> withdraw()
        );

        changePinButton.addActionListener(
                e -> changePin()
        );

        logoutButton.addActionListener(
                e -> logout()
        );

        frame.add(
                leftPanel,
                BorderLayout.WEST
        );

        frame.add(
                new JScrollPane(outputArea),
                BorderLayout.CENTER
        );

        frame.pack();

        frame.setLocationRelativeTo(null);

        frame.setResizable(false);

        frame.revalidate();

        frame.repaint();
    }

    private Account getSelectedAccount() {

        int index =
                accountComboBox.getSelectedIndex();

        if (index < 0) {
            return null;
        }

        return currentCustomer.getAccount(index);
    }

    private void showBalance() {

        Account account =
                getSelectedAccount();

        if (account == null) {

            showError(
                    "No hay una cuenta seleccionada."
            );

            return;
        }

        outputArea.setText(
                "Cuenta seleccionada\n\n"
                + "Saldo disponible: $"
                + String.format(
                        "%.2f",
                        account.getBalance()
                )
        );
    }

    private void deposit() {

        Account account =
                getSelectedAccount();

        if (account == null) {
            showError(
                    "No hay una cuenta seleccionada."
            );
            return;
        }

        String value =
                JOptionPane.showInputDialog(
                        frame,
                        "Cantidad a depositar:"
                );

        if (value == null) {
            return;
        }

        try {

            double amount =
                    Double.parseDouble(
                            value
                    );

            if (account.deposit(amount)) {

                saveData();

                outputArea.setText(
                        "Depósito realizado correctamente.\n\n"
                        + "Cantidad: $"
                        + String.format("%.2f", amount)
                        + "\nSaldo actual: $"
                        + String.format(
                                "%.2f",
                                account.getBalance()
                        )
                );

            } else {

                showError(
                        "La cantidad debe ser mayor que cero."
                );
            }

        } catch (NumberFormatException e) {

            showError(
                    "Ingrese una cantidad válida."
            );
        }
    }

    private void withdraw() {

        Account account =
                getSelectedAccount();

        if (account == null) {

            showError(
                    "No hay una cuenta seleccionada."
            );

            return;
        }

        String value =
                JOptionPane.showInputDialog(
                        frame,
                        "Cantidad a retirar:"
                );

        if (value == null) {
            return;
        }

        try {

            double amount =
                    Double.parseDouble(value);

            account.withdraw(amount);

            saveData();

            outputArea.setText(
                    "Retiro realizado correctamente.\n\n"
                    + "Cantidad: $"
                    + String.format("%.2f", amount)
                    + "\nSaldo actual: $"
                    + String.format(
                            "%.2f",
                            account.getBalance()
                    )
            );

        } catch (NumberFormatException e) {

            showError(
                    "Ingrese una cantidad válida."
            );

        } catch (OverdraftException e) {

            showError(
                    e.getMessage()
                    + "\nDéficit: $"
                    + String.format(
                            "%.2f",
                            e.getDeficit()
                    )
            );
        }
    }

    private void changePin() {

        JPasswordField currentPinField =
                new JPasswordField();

        JPasswordField newPinField =
                new JPasswordField();

        JPasswordField confirmPinField =
                new JPasswordField();

        JPanel panel =
                new JPanel(
                        new GridLayout(3, 2, 5, 5)
                );

        panel.add(
                new JLabel("NIP actual:")
        );

        panel.add(currentPinField);

        panel.add(
                new JLabel("Nuevo NIP:")
        );

        panel.add(newPinField);

        panel.add(
                new JLabel("Confirmar NIP:")
        );

        panel.add(confirmPinField);

        int result =
                JOptionPane.showConfirmDialog(
                        frame,
                        panel,
                        "Cambiar NIP",
                        JOptionPane.OK_CANCEL_OPTION
                );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String currentPin =
                new String(
                        currentPinField.getPassword()
                );

        String newPin =
                new String(
                        newPinField.getPassword()
                );

        String confirmation =
                new String(
                        confirmPinField.getPassword()
                );

        if (!newPin.equals(confirmation)) {

            showError(
                    "El nuevo NIP y su confirmación no coinciden."
            );

            return;
        }

        try {

            currentCustomer.changePin(
                    currentPin,
                    newPin
            );

            saveData();

            JOptionPane.showMessageDialog(
                    frame,
                    "NIP actualizado correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (InvalidPinException e) {

            showError(
                    e.getMessage()
            );

        } catch (IllegalArgumentException e) {

            showError(
                    e.getMessage()
            );
        }
    }

    private void recoverPin() {

        JTextField idField =
                new JTextField();

        JTextField firstNameField =
                new JTextField();

        JTextField lastNameField =
                new JTextField();

        JPasswordField newPinField =
                new JPasswordField();

        JPasswordField confirmPinField =
                new JPasswordField();

        JPanel panel =
                new JPanel(
                        new GridLayout(5, 2, 5, 5)
                );

        panel.add(
                new JLabel("Número de cliente:")
        );

        panel.add(idField);

        panel.add(
                new JLabel("Nombre:")
        );

        panel.add(firstNameField);

        panel.add(
                new JLabel("Apellido:")
        );

        panel.add(lastNameField);

        panel.add(
                new JLabel("Nuevo NIP:")
        );

        panel.add(newPinField);

        panel.add(
                new JLabel("Confirmar NIP:")
        );

        panel.add(confirmPinField);

        int result =
                JOptionPane.showConfirmDialog(
                        frame,
                        panel,
                        "Recuperar NIP",
                        JOptionPane.OK_CANCEL_OPTION
                );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        int id;

        try {

            id =
                    Integer.parseInt(
                            idField.getText().trim()
                    );

        } catch (NumberFormatException e) {

            showError(
                    "Número de cliente inválido."
            );

            return;
        }

        Customer customer =
                Bank.getBank()
                        .findCustomer(id);

        if (customer == null) {

            showError(
                    "El cliente no existe."
            );

            return;
        }

        if (!customer.getFirstName()
                .equalsIgnoreCase(
                        firstNameField.getText().trim()
                )
                || !customer.getLastName()
                .equalsIgnoreCase(
                        lastNameField.getText().trim()
                )) {

            showError(
                    "Los datos proporcionados "
                    + "no coinciden con el cliente."
            );

            return;
        }

        String newPin =
                new String(
                        newPinField.getPassword()
                );

        String confirmation =
                new String(
                        confirmPinField.getPassword()
                );

        if (!newPin.equals(confirmation)) {

            showError(
                    "Los NIP no coinciden."
            );

            return;
        }

        try {

            customer.resetPin(newPin);

            saveData();

            JOptionPane.showMessageDialog(
                    frame,
                    "El NIP fue actualizado correctamente.",
                    "Recuperación exitosa",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (IllegalArgumentException e) {

            showError(
                    e.getMessage()
            );
        }
    }

    private void registerCustomer() {

        JTextField firstNameField =
                new JTextField();

        JTextField lastNameField =
                new JTextField();

        JPasswordField pinField =
                new JPasswordField();

        JPasswordField confirmPinField =
                new JPasswordField();

        JPanel panel =
                new JPanel(
                        new GridLayout(4, 2, 5, 5)
                );

        panel.add(
                new JLabel("Nombre:")
        );

        panel.add(firstNameField);

        panel.add(
                new JLabel("Apellido:")
        );

        panel.add(lastNameField);

        panel.add(
                new JLabel("NIP:")
        );

        panel.add(pinField);

        panel.add(
                new JLabel("Confirmar NIP:")
        );

        panel.add(confirmPinField);

        int result =
                JOptionPane.showConfirmDialog(
                        frame,
                        panel,
                        "Alta de cliente",
                        JOptionPane.OK_CANCEL_OPTION
                );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String firstName =
                firstNameField.getText().trim();

        String lastName =
                lastNameField.getText().trim();

        String pin =
                new String(
                        pinField.getPassword()
                );

        String confirmation =
                new String(
                        confirmPinField.getPassword()
                );

        if (firstName.isEmpty()
                || lastName.isEmpty()) {

            showError(
                    "Nombre y apellido son obligatorios."
            );

            return;
        }

        if (!pin.equals(confirmation)) {

            showError(
                    "Los NIP no coinciden."
            );

            return;
        }

        try {

            Bank bank =
                    Bank.getBank();

            int newId =
                    bank.getNextCustomerId();

            bank.addCustomer(
                    newId,
                    firstName,
                    lastName,
                    pin
            );

            Customer customer =
                    bank.findCustomer(newId);

            customer.addAccount(
                    new banking.domain.SavingsAccount(
                            0.0,
                            0.05
                    )
            );

            saveData();

            JOptionPane.showMessageDialog(
                    frame,
                    "Cliente creado correctamente.\n\n"
                    + "Número de cliente: "
                    + newId,
                    "Alta exitosa",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (DuplicateCustomerException e) {

            showError(
                    e.getMessage()
            );

        } catch (IllegalArgumentException e) {

            showError(
                    e.getMessage()
            );
        }
    }

    private void logout() {

        currentCustomer = null;

        frame.getContentPane().removeAll();

        customerIdField = null;
        pinField = null;
        accountComboBox = null;

        createLoginWindow();
    }

    private void saveData() {

        try {

            dataSource.saveData();

        } catch (FileNotFoundException e) {

            showError(
                    "No fue posible guardar los cambios.\n"
                    + e.getMessage()
            );
        }
    }

    private void showError(String message) {

        JOptionPane.showMessageDialog(
                frame,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    public static void main(String[] args) {

        String dataFilePath;

        if (args.length == 1) {
            dataFilePath = args[0];
        } else {
            dataFilePath = DEFAULT_DATA_FILE;
        }

        System.out.println(
                "Leyendo archivo de datos: "
                + dataFilePath
        );

        new ATMClient(dataFilePath);
    }
}