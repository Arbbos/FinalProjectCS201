package test;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.ArrayList;

public class InventoryReportGenerator {

	public static void generateInventoryReport(InventorySystem inventorySystem) {
	    Collection<InventorySystem.ProductTracker> products = inventorySystem.getProducts();
	    List<InventorySystem.ProductTracker> productList = new ArrayList<>(products);

	    JPanel filterPanel = createFilterPanel();
	    filterPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding to the filter panel

	    List<InventorySystem.ProductTracker> filteredProducts = new ArrayList<>(productList);

	    String[] columnNames = {"Product ID", "Name", "Category", "Stock Level", "Size", "Color", "Location"};

	    DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

	    JButton filterButton = new JButton("Apply Filters");
	    filterButton.addActionListener(e -> {
	        filteredProducts.clear();
	        filteredProducts.addAll(applyFilters(productList, filterPanel));
	        updateTableModel(filteredProducts, tableModel);
	    });

	    JButton resetButton = new JButton("Reset Filters");
	    resetButton.addActionListener(e -> {
	        resetFilters(filterPanel);
	        filteredProducts.clear();
	        filteredProducts.addAll(productList);
	        updateTableModel(filteredProducts, tableModel);
	    });

	    updateTableModel(filteredProducts, tableModel);

	    JTable table = new JTable(tableModel);
	    table.setFillsViewportHeight(true);

	    JScrollPane scrollPane = new JScrollPane(table);
	    scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding around the table

	    JFrame frame = new JFrame("Inventory Report");
	    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

	    frame.getContentPane().setLayout(new BorderLayout(10, 10)); // Set gap between components

	    frame.getContentPane().add(filterPanel, BorderLayout.NORTH);
	    frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

	    JButton saveButton = new JButton("Save Report");
	    saveButton.addActionListener(e -> saveReport(table));

	    JPanel panel = new JPanel();
	    panel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 10)); // Add padding to panel and set right-aligned
	    panel.add(saveButton);
	    panel.add(filterButton);
	    panel.add(resetButton);  // Add the reset button to the panel

	    frame.getContentPane().add(panel, BorderLayout.SOUTH);

	    frame.setSize(800, 600);  // Set preferred window size
	    frame.setLocationRelativeTo(null);
	    frame.setVisible(true);
	}


    private static JPanel createFilterPanel() {
        JPanel filterPanel = new JPanel(new GridLayout(5, 2));

        JComboBox<String> nameComboBox = new JComboBox<>(new String[]{"All", "Blouse", "Polo", "T-Shirt", "Shorts"});
        JComboBox<String> categoryComboBox = new JComboBox<>(new String[]{"All", "Tops", "Pants"});
        JComboBox<String> colorComboBox = new JComboBox<>(new String[]{"All", "Red", "Blue", "Green", "White", "Black", "Pink", "Yellow", "Purple"});
        JComboBox<String> sizeComboBox = new JComboBox<>(new String[]{"All", "S", "M", "L"});
        JComboBox<String> locationComboBox = new JComboBox<>(new String[]{"All", "A", "B"});

        filterPanel.add(new JLabel("Name:"));
        filterPanel.add(nameComboBox);
        filterPanel.add(new JLabel("Category:"));
        filterPanel.add(categoryComboBox);
        filterPanel.add(new JLabel("Color:"));
        filterPanel.add(colorComboBox);
        filterPanel.add(new JLabel("Size:"));
        filterPanel.add(sizeComboBox);
        filterPanel.add(new JLabel("Location:"));
        filterPanel.add(locationComboBox);

        return filterPanel;
    }

    private static List<InventorySystem.ProductTracker> applyFilters(List<InventorySystem.ProductTracker> products, JPanel filterPanel) {
        JComboBox<String> nameComboBox = (JComboBox<String>) filterPanel.getComponent(1);
        JComboBox<String> categoryComboBox = (JComboBox<String>) filterPanel.getComponent(3);
        JComboBox<String> colorComboBox = (JComboBox<String>) filterPanel.getComponent(5);
        JComboBox<String> sizeComboBox = (JComboBox<String>) filterPanel.getComponent(7);
        JComboBox<String> locationComboBox = (JComboBox<String>) filterPanel.getComponent(9);

        String nameFilter = (String) nameComboBox.getSelectedItem();
        String categoryFilter = (String) categoryComboBox.getSelectedItem();
        String colorFilter = (String) colorComboBox.getSelectedItem();
        String sizeFilter = (String) sizeComboBox.getSelectedItem();
        String locationFilter = (String) locationComboBox.getSelectedItem();

        List<InventorySystem.ProductTracker> filteredList = new ArrayList<>();

        for (InventorySystem.ProductTracker product : products) {
            boolean matches = true;

            if (!nameFilter.equals("All") && !product.getName().equals(nameFilter)) {
                matches = false;
            }
            if (!categoryFilter.equals("All") && !product.getCategory().equals(categoryFilter)) {
                matches = false;
            }
            if (!colorFilter.equals("All") && !product.getColor().equals(colorFilter)) {
                matches = false;
            }
            if (!sizeFilter.equals("All") && !product.getSize().equals(sizeFilter)) {
                matches = false;
            }
            if (!locationFilter.equals("All") && !product.getLocation().equals(locationFilter)) {
                matches = false;
            }

            if (matches) {
                filteredList.add(product);
            }
        }

        return filteredList;
    }

    private static void resetFilters(JPanel filterPanel) {
        JComboBox<String> nameComboBox = (JComboBox<String>) filterPanel.getComponent(1);
        JComboBox<String> categoryComboBox = (JComboBox<String>) filterPanel.getComponent(3);
        JComboBox<String> colorComboBox = (JComboBox<String>) filterPanel.getComponent(5);
        JComboBox<String> sizeComboBox = (JComboBox<String>) filterPanel.getComponent(7);
        JComboBox<String> locationComboBox = (JComboBox<String>) filterPanel.getComponent(9);

        nameComboBox.setSelectedIndex(0);
        categoryComboBox.setSelectedIndex(0);
        colorComboBox.setSelectedIndex(0);
        sizeComboBox.setSelectedIndex(0);
        locationComboBox.setSelectedIndex(0);
    }

    private static void updateTableModel(List<InventorySystem.ProductTracker> filteredProducts, DefaultTableModel tableModel) {
        tableModel.setRowCount(0);

        for (InventorySystem.ProductTracker product : filteredProducts) {
            Object[] rowData = {
                product.getProductID(),
                product.getName(),
                product.getCategory(),
                product.getStockLevel(),
                product.getSize(),
                product.getColor(),
                product.getLocation()
            };
            tableModel.addRow(rowData);
        }
    }

    private static void saveReport(JTable table) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Report");
        fileChooser.setSelectedFile(new File("inventory-report.txt"));

        int userChoice = fileChooser.showSaveDialog(null);
        if (userChoice == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
                // Define column widths for alignment
                int[] columnWidths = new int[table.getColumnCount()];
                for (int i = 0; i < table.getColumnCount(); i++) {
                    columnWidths[i] = table.getColumnName(i).length();  // Set initial width based on column name length
                }

                // Find the maximum width for each column (column name and data)
                for (int i = 0; i < table.getRowCount(); i++) {
                    for (int j = 0; j < table.getColumnCount(); j++) {
                        int cellLength = table.getValueAt(i, j).toString().length();
                        columnWidths[j] = Math.max(columnWidths[j], cellLength);
                    }
                }

                // Write column headers with fixed width
                for (int i = 0; i < table.getColumnCount(); i++) {
                    String header = table.getColumnName(i);
                    writer.write(String.format("%-" + columnWidths[i] + "s", header));
                    if (i < table.getColumnCount() - 1) {
                        writer.write("\t");
                    }
                }
                writer.write("\n");

                // Write table data with fixed width
                for (int i = 0; i < table.getRowCount(); i++) {
                    for (int j = 0; j < table.getColumnCount(); j++) {
                        Object value = table.getValueAt(i, j);
                        writer.write(String.format("%-" + columnWidths[j] + "s", value.toString()));
                        if (j < table.getColumnCount() - 1) {
                            writer.write("\t");
                        }
                    }
                    writer.write("\n");
                }

                JOptionPane.showMessageDialog(null, "Report saved successfully!");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error saving the report.");
            }
        }
    }

}
