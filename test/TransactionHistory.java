package test;

import java.io.*;
import javax.swing.JOptionPane;

public class TransactionHistory {

    private static final String FILE_PATH = "transaction_history.txt"; // File to save transaction history

    // Save a single stock movement to the transaction history file
    public static void saveToTransactionHistory(StockMovement movement) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(movement.toString());
            writer.newLine();  // Add a new line after each transaction
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Fetch all transaction history from the file
    public static String viewTransactionHistory() {
        StringBuilder history = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                history.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return history.toString();
    }

    // Display transaction history in a simple dialog
    public static void displayTransactionHistory() {
        String history = viewTransactionHistory();
        JOptionPane.showMessageDialog(null, history, "Transaction History", JOptionPane.INFORMATION_MESSAGE);
    }
}
