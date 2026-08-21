# Banking ATM System

A Java-based ATM simulation that demonstrates object-oriented programming, account management, customer authentication, deposits, withdrawals, overdraft protection, customer registration, exception handling, data loading, and customer reporting.

The project is organized into independent packages that separate the domain model, data loading, graphical interface, and reporting functionality.

---

## Overview

This project simulates a basic banking system with an ATM graphical interface.

The application allows users to:

* Authenticate using a customer ID and PIN.
* Access their banking session.
* Check account balances.
* Make deposits.
* Make withdrawals.
* Handle checking-account overdraft protection.
* End an ATM session.
* Register new customers.
* Validate customer information.
* Prevent duplicate customer IDs.
* Handle invalid customer IDs.
* Handle invalid PINs.
* Recover access when the customer forgets their PIN.
* Load customers and accounts from a data file.
* Generate a customer account report.

The project also demonstrates several object-oriented programming concepts, including:

* Encapsulation
* Inheritance
* Polymorphism
* Exception handling
* Static object management
* Collections
* File processing
* GUI development with Swing

---

## Features

### Customer Authentication

Customers must provide:

1. Customer ID
2. PIN

The ATM validates the credentials before granting access to the banking operations.

Invalid authentication attempts are handled through dedicated exceptions.

### Account Operations

Once authenticated, a customer can:

* Check their balance.
* Deposit money.
* Withdraw money.
* End their current session.

The session-based design prevents the ATM from remaining permanently associated with the same customer.

### Savings Accounts

Savings accounts store:

* Current balance
* Interest rate

The account inherits the basic functionality provided by `Account`.

### Checking Accounts

Checking accounts support:

* Current balance
* Overdraft protection
* Withdrawals using available overdraft protection

If the requested withdrawal exceeds the available funds and overdraft protection, an `OverdraftException` is generated.

### Customer Registration

The system can create new customers while validating:

* Customer information
* Customer ID
* PIN
* Duplicate customer IDs

A customer cannot be registered using an ID that already exists.

### PIN Recovery

The system includes a recovery mechanism for customers who cannot access their accounts because they forgot their PIN.

The recovery process allows the customer to regain access without manually modifying the data file.

### Data Loading

Customers and accounts are loaded from:

```text
input/BankData.txt
```

The data source validates the structure of the file and reports malformed data through `DataFormatException`.

### Customer Reports

The reporting module generates a console report containing:

* Customer names
* Account types
* Account balances

---

## Project Structure

The repository should follow this structure:

```text
BankingATM/
│
├── README.md
│
├── src/
│   └── banking/
│       ├── data/
│       │   └── DataSource.java
│       │
│       ├── domain/
│       │   ├── Account.java
│       │   ├── Bank.java
│       │   ├── CheckingAccount.java
│       │   ├── Customer.java
│       │   ├── CustomerNotFoundException.java
│       │   ├── DataFormatException.java
│       │   ├── DuplicateCustomerException.java
│       │   ├── InvalidCustomerException.java
│       │   ├── InvalidPinException.java
│       │   ├── OverdraftException.java
│       │   └── SavingsAccount.java
│       │
│       ├── gui/
│       │   └── ATMClient.java
│       │
│       └── reports/
│           ├── CustomerReport.java
│           └── TestReport.java
│
├── input/
│   └── BankData.txt
│
└── assets/
    └── images/
        ├── atm_login.png
        ├── atm_main_menu.png
        ├── atm_deposit.png
        ├── atm_withdrawal.png
        ├── atm_balance.png
        ├── atm_session_end.png
        ├── atm_pin_recovery.png
        ├── atm_new_customer.png
        ├── atm_invalid_customer.png
        ├── atm_invalid_pin.png
        ├── atm_duplicate_customer.png
        ├── atm_overdraft.png
        └── customer_report.png
```

> If the project is being maintained as a NetBeans project, the IDE-generated folders such as `build/`, `dist/`, `nbproject/`, and `manifest.mf` may also be present.

---

## Package Organization

### `banking.domain`

This package contains the core banking model.

```text
Account.java
Bank.java
CheckingAccount.java
Customer.java
CustomerNotFoundException.java
DataFormatException.java
DuplicateCustomerException.java
InvalidCustomerException.java
InvalidPinException.java
OverdraftException.java
SavingsAccount.java
```

#### `Account`

Base class for banking accounts.

It provides common operations such as:

* Getting the balance
* Depositing money
* Withdrawing money

#### `Bank`

Represents the central bank object and manages the registered customers.

The project uses a single `Bank` instance to coordinate customer information.

#### `Customer`

Represents a bank customer.

A customer contains:

* Customer identification information
* First name
* Last name
* PIN
* Associated accounts

#### `CheckingAccount`

Extends `Account` and adds overdraft protection functionality.

#### `SavingsAccount`

Extends `Account` and stores the account's interest rate.

#### Exception Classes

The project defines specific exceptions for different banking errors:

```text
CustomerNotFoundException
DataFormatException
DuplicateCustomerException
InvalidCustomerException
InvalidPinException
OverdraftException
```

Using individual exception types makes the application easier to maintain and allows different error conditions to be handled independently.

---

## `banking.data`

This package contains:

```text
DataSource.java
```

`DataSource` is responsible for reading the customer and account information from the external data file.

The default data file is:

```text
input/BankData.txt
```

The data source creates the corresponding customers and accounts inside the `Bank` object.

---

## `banking.gui`

This package contains:

```text
ATMClient.java
```

`ATMClient` provides the graphical ATM interface using Java Swing.

The interface allows the user to interact with the banking system without directly manipulating the domain classes.

The ATM provides operations such as:

```text
Customer authentication
        ↓
Main banking menu
        ↓
 ┌───────────────┐
 │ Check balance │
 │ Deposit       │
 │ Withdraw      │
 │ End session   │
 └───────────────┘
```

Additional functionality is provided for:

* New customer registration
* PIN recovery
* Authentication errors
* Invalid operations

---

## `banking.reports`

This package contains:

```text
CustomerReport.java
TestReport.java
```

`CustomerReport` generates a report containing the registered customers and their accounts.

`TestReport` loads the bank data and executes the reporting process.

---

## Input Data

The application uses:

```text
input/BankData.txt
```

A sample data file can follow this structure:

```text
4
Jane Simms 2
S 500.00 0.05
C 200.00 400.00
Owen Bryant 1
C 200.00 0.00
Tim Soley 2
S 1500.00 0.05
C 200.00 0.00
Maria Soley 1
S 150.00 0.05
```

The first value represents the number of customers.

Each customer contains:

```text
FirstName LastName NumberOfAccounts
```

Savings accounts use:

```text
S Balance InterestRate
```

Checking accounts use:

```text
C Balance OverdraftProtection
```

If the final implementation stores customer IDs and PINs directly in the data file, the corresponding format must be used consistently with the current `DataSource.java`.

---

## Running the Application

### Running the ATM

The main application is:

```text
banking.gui.ATMClient
```

When running from the command line, the data file path must be provided:

```bash
java banking.gui.ATMClient input/BankData.txt
```

In NetBeans, the easiest approach is to configure:

```text
Run → Set Project Configuration → Customize
```

and provide:

```text
input/BankData.txt
```

as the program argument for `ATMClient`.

If the program is executed without the required argument, it displays:

```text
Usage: java banking.gui.ATMClient <dataFilePath>
```

---

## Running the Customer Report

The report application is:

```text
banking.reports.TestReport
```

It also requires the path to the bank data file.

Example:

```bash
java banking.reports.TestReport input/BankData.txt
```

The report displays the registered customers and their account information in the console.

---

## ATM Screenshots

The project includes screenshots documenting the main functionality of the application.

All screenshots should be stored inside:

```text
assets/images/
```

and referenced from the README using relative paths.

### ATM Login

Shows the initial customer authentication screen.

![ATM Login](assets/images/atm_login.png)

### Main ATM Menu

Shows the main menu after successful authentication.

![ATM Main Menu](assets/images/atm_main_menu.png)

### Balance Inquiry

Shows the balance inquiry operation.

![ATM Balance](assets/images/atm_balance.png)

### Deposit

Shows a successful deposit operation.

![ATM Deposit](assets/images/atm_deposit.png)

### Withdrawal

Shows a successful withdrawal operation.

![ATM Withdrawal](assets/images/atm_withdrawal.png)

### End Session

Shows the option used to terminate the current customer's ATM session.

![ATM Session End](assets/images/atm_session_end.png)

### PIN Recovery

Shows the PIN recovery functionality.

![ATM PIN Recovery](assets/images/atm_pin_recovery.png)

### New Customer

Shows the customer registration process.

![New Customer](assets/images/atm_new_customer.png)

### Invalid Customer

Shows the error generated when an unknown customer ID is entered.

![Invalid Customer](assets/images/atm_invalid_customer.png)

### Invalid PIN

Shows the authentication error when an incorrect PIN is entered.

![Invalid PIN](assets/images/atm_invalid_pin.png)

### Duplicate Customer

Shows the validation that prevents registering an existing customer ID.

![Duplicate Customer](assets/images/atm_duplicate_customer.png)

### Overdraft Protection

Shows the checking-account overdraft behavior.

![Overdraft Protection](assets/images/atm_overdraft.png)

### Customer Report

Shows the console output generated by the customer report.

![Customer Report](assets/images/customer_report.png)

---

## Recommended Screenshots

For the GitHub repository, the most important screenshots are:

1. `atm_login.png`
2. `atm_main_menu.png`
3. `atm_balance.png`
4. `atm_deposit.png`
5. `atm_withdrawal.png`
6. `atm_session_end.png`
7. `atm_pin_recovery.png`
8. `atm_new_customer.png`
9. `atm_invalid_customer.png`
10. `atm_invalid_pin.png`
11. `atm_duplicate_customer.png`
12. `atm_overdraft.png`
13. `customer_report.png`

The screenshots do not need to show every internal implementation detail. They should demonstrate the application's visible functionality and error handling.

---

## Exception Handling

The application uses dedicated exceptions to represent different error conditions.

### `CustomerNotFoundException`

Used when the requested customer cannot be found.

### `InvalidCustomerException`

Used when customer information does not satisfy the application's validation rules.

### `DuplicateCustomerException`

Used when attempting to register a customer using an existing customer ID.

### `InvalidPinException`

Used when the PIN provided during authentication is incorrect or invalid.

### `DataFormatException`

Used when the bank data file does not follow the expected format.

### `OverdraftException`

Used when an account does not have sufficient funds, including cases where overdraft protection is insufficient.

---

## Banking Workflow

The general ATM workflow is:

```text
Start ATM
   │
   ▼
Load BankData.txt
   │
   ▼
Display authentication
   │
   ├── Invalid customer ──► Error
   │
   ├── Invalid PIN ───────► Error / Recovery
   │
   ▼
Authenticated customer
   │
   ▼
Main menu
   │
   ├── Balance
   │
   ├── Deposit
   │
   ├── Withdrawal
   │
   └── End session
            │
            ▼
       Return to login
```

Ending a session does not terminate the entire ATM application. It returns the system to the authentication screen so another customer can use the ATM.

---

## Object-Oriented Design

The project demonstrates inheritance through the account hierarchy:

```text
              Account
              /     \
             /       \
            ▼         ▼
    SavingsAccount   CheckingAccount
```

`Account` contains functionality shared by both account types.

`SavingsAccount` adds savings-specific information such as the interest rate.

`CheckingAccount` adds overdraft protection.

The `Bank` manages `Customer` objects, while each `Customer` can own multiple `Account` objects.

---

## Technologies

* Java
* Java Swing
* Object-Oriented Programming
* Java Collections
* Java Exceptions
* File I/O
* NetBeans IDE

---

## Requirements

To run the project, you need:

* Java JDK
* NetBeans IDE or another Java IDE
* A properly configured `BankData.txt` file

The project does not require external libraries.

---

## NetBeans Configuration

When using NetBeans, make sure the project contains the following source packages:

```text
banking.data
banking.domain
banking.gui
banking.reports
```

The input directory should be located at the project root:

```text
BankingATM/
├── input/
│   └── BankData.txt
└── src/
    └── banking/
```

For `ATMClient`, configure the program argument:

```text
input/BankData.txt
```

For `TestReport`, use the same argument:

```text
input/BankData.txt
```

This prevents the application from starting without knowing where the bank data is located.

---

## Example ATM Operations

A typical session can look like:

```text
Customer ID: 1001
PIN: ****

Welcome customer.

1. Balance
2. Deposit
3. Withdraw
4. End Session
```

For a deposit:

```text
Deposit amount: 500

Deposit accepted.
```

For a withdrawal:

```text
Withdrawal amount: 200

Withdrawal accepted.
```

For an invalid PIN:

```text
Invalid PIN.
```

For an unknown customer:

```text
Customer not found.
```

---

## Project Goals

The main goals of this project are to demonstrate:

* Object-oriented software design.
* Class relationships.
* Inheritance.
* Exception handling.
* File-based data persistence.
* GUI development.
* Customer authentication.
* Account operations.
* Validation.
* Error recovery.
* Separation of concerns.

---

## Repository Organization

The final repository should keep application source code, input data, and documentation resources separated:

```text
BankingATM/
│
├── src/
│   └── banking/
│       ├── data/
│       ├── domain/
│       ├── gui/
│       └── reports/
│
├── input/
│   └── BankData.txt
│
├── assets/
│   └── images/
│       ├── atm_login.png
│       ├── atm_main_menu.png
│       ├── atm_balance.png
│       ├── atm_deposit.png
│       ├── atm_withdrawal.png
│       ├── atm_session_end.png
│       ├── atm_pin_recovery.png
│       ├── atm_new_customer.png
│       ├── atm_invalid_customer.png
│       ├── atm_invalid_pin.png
│       ├── atm_duplicate_customer.png
│       ├── atm_overdraft.png
│       └── customer_report.png
│
└── README.md
```

---

## Notes

The `BankData.txt` file is an essential part of the application because it provides the initial customer and account information.

The path used to execute the application must point to this file.

Screenshots are stored separately under `assets/images/` so that the README remains organized and GitHub can render them using relative paths.

The project is intended as an educational Java application demonstrating banking-domain modeling, GUI interaction, file processing, authentication, validation, exception handling, and reporting.
