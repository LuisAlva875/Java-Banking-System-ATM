package banking.reports;

import banking.data.DataSource;
import banking.domain.DuplicateCustomerException;

import java.io.FileNotFoundException;

public class TestReport {

    private static final String DEFAULT_DATA_FILE =
            "input/BankData.txt";

    public static void main(String[] args) {

        String dataFilePath;

        if (args.length == 1) {
            dataFilePath = args[0];
        } else {
            dataFilePath = DEFAULT_DATA_FILE;
        }

        try {

            System.out.println(
                    "Leyendo archivo de datos: "
                    + dataFilePath
            );

            DataSource dataSource =
                    new DataSource(dataFilePath);

            dataSource.loadData();

            CustomerReport report =
                    new CustomerReport();

            report.generateReport();

        } catch (FileNotFoundException e) {

            System.out.println(
                    "ERROR: No se encontró el archivo de datos."
            );

            System.out.println(e.getMessage());

        } catch (DuplicateCustomerException e) {

            System.out.println(
                    "ERROR: Cliente duplicado."
            );

            System.out.println(e.getMessage());

        } catch (Exception e) {

            System.out.println(
                    "ERROR inesperado: "
                    + e.getMessage()
            );

            e.printStackTrace();
        }
    }
}