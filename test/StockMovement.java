package test;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

import java.io.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import javax.swing.table.DefaultTableModel;

public class StockMovement {
    private String productID;
    private String type;
    private int quantity;
    private String date; // Use ISO format (YYYY-MM-DD)
    private String comments;

    public StockMovement(String productID, String type, int quantity, String date, String comments) {
        this.productID = productID;
        this.type = type;
        this.quantity = quantity;
        this.date = date;
        this.comments = comments;
    }

    public String getProductID() {
        return productID;
    }

    public String getType() {
        return type;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getDate() {
        return date;
    }

    public String getComments() {
        return comments;
    }

    @Override
    public String toString() {
        return "StockMovement{" +
               "productID='" + productID + '\'' +
               ", type='" + type + '\'' +
               ", quantity=" + quantity +
               ", date='" + date + '\'' +
               ", comments='" + comments + '\'' +
               '}';
    }

    private static final String CONNECTION = "jdbc:mysql://localhost:3306/test";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // Fetch stock movements from the database
    public List<StockMovement> fetchStockMovementsFromDatabase(String filter, String sortBy, boolean ascending) {
        List<StockMovement> movements = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT product_id, type, quantity, date, comments FROM stockmovements");

        if (filter != null && !filter.isEmpty()) {
            sql.append(" WHERE type = ?");
        }

        if (sortBy != null) {
            sql.append(" ORDER BY ").append(sortBy);
            if (!ascending) {
                sql.append(" DESC");
            }
        }
        
        try (Connection connection = DriverManager.getConnection(CONNECTION, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql.toString())) {

            // Set filter parameter
            if (filter != null && !filter.isEmpty()) {
                statement.setString(1, filter);
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String productID = resultSet.getString("product_id");
                    String type = resultSet.getString("type");
                    int quantity = resultSet.getInt("quantity");
                    String date = resultSet.getDate("date").toString(); // Convert to String
                    String comments = resultSet.getString("comments");

                    movements.add(new StockMovement(productID, type, quantity, date, comments));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return movements;
    }

    // Save a stock movement to the database
    public void saveStockMovementToDatabase(StockMovement movement) {
        String sql = "INSERT INTO stockmovements (product_id, type, quantity, date, comments) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(CONNECTION, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, movement.getProductID());
            statement.setString(2, movement.getType());
            statement.setInt(3, movement.getQuantity());
            statement.setString(4, movement.getDate());
            statement.setString(5, movement.getComments());
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Add this method to refresh the JTable
    private void refreshTable(JTable table) {
        List<StockMovement> movements = fetchStockMovementsFromDatabase(filter, sortBy, ascending);

        String[] columnNames = {"Product ID", "Type", "Quantity", "Date", "Comments"};
        Object[][] data = new Object[movements.size()][5];

        for (int i = 0; i < movements.size(); i++) {
            StockMovement movement = movements.get(i);
            data[i][0] = movement.getProductID();
            data[i][1] = movement.getType();
            data[i][2] = movement.getQuantity();
            data[i][3] = movement.getDate();
            data[i][4] = movement.getComments();
        }

        // Update the table model
        table.setModel(new DefaultTableModel(data, columnNames));
    }

    // Modify the openAddStockMovementDialog method to accept the JTable
    private void openAddStockMovementDialog(JFrame parentFrame, JTable table) {
        JDialog dialog = new JDialog(parentFrame, "Add Stock Movement", true);
        dialog.setSize(400, 300);
        dialog.setLayout(new GridLayout(6, 2, 10, 10));
        dialog.setLocationRelativeTo(parentFrame);

        // Input fields
        JLabel productIdLabel = new JLabel("Product ID:");
        JTextField productIdField = new JTextField();
        JLabel typeLabel = new JLabel("Type (SHIPMENT/RECEIPT):");
        JTextField typeField = new JTextField();
        JLabel quantityLabel = new JLabel("Quantity:");
        JTextField quantityField = new JTextField();
        JLabel dateLabel = new JLabel("Date (YYYY-MM-DD):");
        JTextField dateField = new JTextField();
        JLabel commentsLabel = new JLabel("Comments:");
        JTextField commentsField = new JTextField();

        // Buttons
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> {
            try {
                String productId = productIdField.getText();
                String type = typeField.getText();
                int quantity = Integer.parseInt(quantityField.getText());
                String date = dateField.getText();
                String comments = commentsField.getText();

                // Create a new StockMovement object and save it to the database
                StockMovement movement = new StockMovement(productId, type, quantity, date, comments);
                saveStockMovementToDatabase(movement);

                // Refresh the table with updated data
                refreshTable(table);

                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        // Add components to dialog
        dialog.add(productIdLabel);
        dialog.add(productIdField);
        dialog.add(typeLabel);
        dialog.add(typeField);
        dialog.add(quantityLabel);
        dialog.add(quantityField);
        dialog.add(dateLabel);
        dialog.add(dateField);
        dialog.add(commentsLabel);
        dialog.add(commentsField);
        dialog.add(saveButton);
        dialog.add(cancelButton);

        dialog.setVisible(true);
    }

    public void saveTableAsText(JTable table) throws java.io.IOException {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save As Text File");
        fileChooser.setSelectedFile(new File("StockMovements.txt")); // Default file name
        
        int userSelection = fileChooser.showSaveDialog(null);

        // If the user selected a file (not cancelled)
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            // Get the file chosen by the user
            File fileToSave = fileChooser.getSelectedFile();

            // Check if the file already exists, and if so, ask for confirmation
            if (fileToSave.exists()) {
                int overwriteConfirmation = JOptionPane.showConfirmDialog(
                    null,
                    "The file already exists. Do you want to overwrite it?",
                    "Confirm Overwrite",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
                );

                if (overwriteConfirmation == JOptionPane.NO_OPTION) {
                    return;
                }
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {

                // Write a title to the text file
                writer.write("Stock Movements Report");
                writer.newLine();  // Adds a new line
                writer.write("---------------------------------------");
                writer.newLine();  // Adds a new line

                // Calculate maximum column width
                int[] maxColWidths = new int[table.getColumnCount()];
                for (int col = 0; col < table.getColumnCount(); col++) {
                    maxColWidths[col] = table.getColumnName(col).length(); // Start with the header length
                }

                // Calculate the maximum width for each column (based on content)
                for (int row = 0; row < table.getRowCount(); row++) {
                    for (int col = 0; col < table.getColumnCount(); col++) {
                        Object value = table.getValueAt(row, col);
                        if (value != null) {
                            int length = value.toString().length();
                            if (length > maxColWidths[col]) {
                                maxColWidths[col] = length;
                            }
                        }
                    }
                }

                // Write the column headers to the file
                for (int col = 0; col < table.getColumnCount(); col++) {
                    String columnName = table.getColumnName(col);
                    writer.write(padString(columnName, maxColWidths[col]) + "\t"); // Pad the column name
                }
                writer.newLine();  // Adds a new line after column headers

                // Write the rows of the table to the file
                for (int row = 0; row < table.getRowCount(); row++) {
                    for (int col = 0; col < table.getColumnCount(); col++) {
                        Object value = table.getValueAt(row, col);
                        String cellValue = (value == null ? "" : value.toString());
                        writer.write(padString(cellValue, maxColWidths[col]) + "\t"); // Pad the cell value
                    }
                    writer.newLine();  // Adds a new line after each row
                }

                // Show success message
                JOptionPane.showMessageDialog(null, "Text file saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error saving text file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Helper method to pad the strings based on the maximum column width
    private String padString(String value, int width) {
        StringBuilder sb = new StringBuilder(value);
        while (sb.length() < width) {
            sb.append(" "); 
        }
        return sb.toString();
    }

    // Display stock movements in a JTable
    public void viewStockMovementsFromDatabase() {
        String filter = null;  // No filter by default
        String sortBy = "date";  // Default sort by date
        boolean ascending = true;  // Default ascending 
        
        List<StockMovement> movements = fetchStockMovementsFromDatabase(filter, sortBy, ascending);

        String[] columnNames = {"Product ID", "Type", "Quantity", "Date", "Comments"};
        Object[][] data = new Object[movements.size()][5];

        for (int i = 0; i < movements.size(); i++) {
            StockMovement movement = movements.get(i);
            data[i][0] = movement.getProductID();
            data[i][1] = movement.getType();
            data[i][2] = movement.getQuantity();
            data[i][3] = movement.getDate();
            data[i][4] = movement.getComments();
        }

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);

        // Create the frame
        JFrame frame = new JFrame("Stock Movements from Database");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 500);
        frame.setLayout(new BorderLayout());
        frame.add(scrollPane, BorderLayout.CENTER);

        // Create filter and sorting panel
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new FlowLayout());

        // Filter ComboBox (Transaction Types)
        String[] transactionTypes = {"All", "Receipt", "Shipment"};
        JComboBox<String> filterComboBox = new JComboBox<>(transactionTypes);
        filterPanel.add(new JLabel("Filter by Transaction Type:"));
        filterPanel.add(filterComboBox);

        // Sorting ComboBox (Sort By)
        String[] sortOptions = {"Date", "Product ID"};
        JComboBox<String> sortComboBox = new JComboBox<>(sortOptions);
        filterPanel.add(new JLabel("Sort By:"));
        filterPanel.add(sortComboBox);

        // Ascending/Descending ComboBox
        JComboBox<String> orderComboBox = new JComboBox<>(new String[]{"Ascending", "Descending"});
        filterPanel.add(new JLabel("Order:"));
        filterPanel.add(orderComboBox);
        
        // Apply filter and sorting
        JButton applyButton = new JButton("Apply");
        applyButton.addActionListener(e -> extracted(table));
        
        // Reset to default filter and sorting
        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> {
            filterComboBox.setSelectedIndex(0);
            sortComboBox.setSelectedIndex(0);
            orderComboBox.setSelectedIndex(0);

            String defaultFilter = null;
            String defaultSortBy = "date";
            boolean defaultAscending = true;

            // Call refreshTable with default values
            refreshTable(table, defaultFilter, defaultSortBy, defaultAscending);
        });

        filterPanel.add(applyButton);
        filterPanel.add(resetButton);
        frame.add(filterPanel, BorderLayout.NORTH);
        
        // Footer panel with the "Add Stock Movement" button
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton printButton = new JButton("Print");
        JButton addStockMovementButton = new JButton("Add Stock Movement");

        // Pass the JTable to the dialog
        addStockMovementButton.addActionListener(e -> openAddStockMovementDialog(frame, table));

        printButton.addActionListener(e -> {
            try {
                saveTableAsText(table);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        
        applyButton.addActionListener(e -> {
            // Get selected values from the ComboBoxes
            String selectedFilter = (String) filterComboBox.getSelectedItem();
            String selectedSortBy = (String) sortComboBox.getSelectedItem();
            String selectedOrder = (String) orderComboBox.getSelectedItem();

            // Determine the filter
            String filterToApply = null;  // Renaming the variable to avoid conflict
            if (!selectedFilter.equals("All")) {
                filterToApply = selectedFilter;
            }

            // Determine the sortBy column (either "date" or "product_id")
            String sortByColumn = (selectedSortBy.equals("Date")) ? "date" : "product_id"; // Changed variable name

            // Determine if the order is ascending or descending
            boolean ascendingOrder = selectedOrder.equals("Ascending");  // Renamed variable for clarity

            // Refresh table with selected filters and sorting
            refreshTable(table, filterToApply, sortByColumn, ascendingOrder);
        });
        
        footerPanel.add(addStockMovementButton);
        frame.add(footerPanel, BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void extracted(JTable table) {
		String filter;
		String sortBy;
		boolean ascending;
		filter = null;  // No filter by default
		sortBy = "date";  // Default sort by date
		ascending = true;  // Default ascending order

		// Update filter if "All" is selected
		if (filter.equals("All")) {
		    filter = null;
		}

		// Refresh table with selected filters and sorting
		refreshTable(table, filter, sortBy, ascending);
	}
}
