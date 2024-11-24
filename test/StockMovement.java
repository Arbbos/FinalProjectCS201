package test;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

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
    public List<StockMovement> fetchStockMovementsFromDatabase() {
        List<StockMovement> movements = new ArrayList<>();
        String sql = "SELECT product_id, type, quantity, date, comments FROM stockmovements";

        try (Connection connection = DriverManager.getConnection(CONNECTION, USER, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                String productID = resultSet.getString("product_id");
                String type = resultSet.getString("type");
                int quantity = resultSet.getInt("quantity");
                String date = resultSet.getDate("date").toString(); // Convert to String
                String comments = resultSet.getString("comments");

                movements.add(new StockMovement(productID, type, quantity, date, comments));
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
        List<StockMovement> movements = fetchStockMovementsFromDatabase();

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
        table.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
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

    // Display stock movements in a JTable
    public void viewStockMovementsFromDatabase() {
        List<StockMovement> movements = fetchStockMovementsFromDatabase();

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

        // Footer panel with the "Add Stock Movement" button
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addStockMovementButton = new JButton("Add Stock Movement");

        // Pass the JTable to the dialog
        addStockMovementButton.addActionListener(e -> openAddStockMovementDialog(frame, table));

        footerPanel.add(addStockMovementButton);
        frame.add(footerPanel, BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
