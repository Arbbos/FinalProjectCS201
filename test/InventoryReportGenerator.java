package test;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.*;

public class InventoryReportGenerator {

    // Generate an inventory report with sorting using QuickSort
    public static void generateInventoryReport(InventorySystem inventorySystem) {
        // Fetch all products
        Collection<InventorySystem.ProductTracker> products = inventorySystem.getProducts();
        List<InventorySystem.ProductTracker> productList = new ArrayList<>(products);

        // Sort using QuickSort
        quickSort(productList, 0, productList.size() - 1);

        // Create a JTextArea to display the report
        JTextArea textArea = new JTextArea(20, 50);  // 20 rows and 50 columns for display
        textArea.setEditable(false);  // Make it non-editable
        textArea.setText("Inventory Report (Sorted by Stock Level):\n");

        // Append the sorted products to the JTextArea
        for (InventorySystem.ProductTracker product : productList) {
            textArea.append(product.toString() + "\n");
        }

        // Aggregate the total stock of all products
        int totalStock = products.stream()
                .mapToInt(InventorySystem.ProductTracker::getStockLevel)
                .sum();
        textArea.append("\nTotal Stock Level Across All Products: " + totalStock);

        // Create a JScrollPane to make the text area scrollable
        JScrollPane scrollPane = new JScrollPane(textArea);

        // Create the JFrame to hold the JScrollPane
        JFrame frame = new JFrame("Inventory Report");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(scrollPane);

        // Create a "Save" button
        JButton saveButton = new JButton("Save Report");
        saveButton.addActionListener(e -> saveReport(textArea.getText()));

        // Add the "Save" button to the frame
        JPanel panel = new JPanel();
        panel.add(saveButton);
        frame.getContentPane().add(panel, "South");

        frame.pack();  // Adjust size based on content
        frame.setLocationRelativeTo(null);  // Center the window
        frame.setVisible(true);
    }

    // QuickSort Algorithm
    public static void quickSort(List<InventorySystem.ProductTracker> list, int low, int high) {
        if (low < high) {
            // Partition the list
            int pi = partition(list, low, high);

            // Recursively sort elements before and after partition
            quickSort(list, low, pi - 1);
            quickSort(list, pi + 1, high);
        }
    }

    // Helper function to partition the list
    private static int partition(List<InventorySystem.ProductTracker> list, int low, int high) {
        InventorySystem.ProductTracker pivot = list.get(high);
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (list.get(j).getStockLevel() > pivot.getStockLevel()) {
                i++;
                Collections.swap(list, i, j);
            }
        }

        // Swap the pivot element with the element at i+1
        Collections.swap(list, i + 1, high);
        return i + 1;
    }

    // Save the report to a text file
    private static void saveReport(String reportContent) {
        // Open a file chooser to let the user select the location and file name
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Report");
        fileChooser.setSelectedFile(new File("inventory_report.txt"));

        int userChoice = fileChooser.showSaveDialog(null);
        if (userChoice == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
                writer.write(reportContent);
                JOptionPane.showMessageDialog(null, "Report saved successfully!");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error saving the report.");
            }
        }
    }
}
