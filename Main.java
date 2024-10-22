import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    private static InventorySystem inventorySystem = new InventorySystem();

    public static void main(String[] args) {
        // Sample products to prepopulate inventory
        inventorySystem.addProduct("PT01", "T-Shirt", "Tops", 50, "M", "Red");
        inventorySystem.addProduct("PT02", "Polo", "Tops", 30, "L", "Red");
        inventorySystem.addProduct("PT03", "Blouse", "Tops", 20, "S", "White");
        inventorySystem.addProduct("PB01", "Shorts", "Pants", 25, "M", "Black");

        JFrame frame = new JFrame("Inventory Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        // Sticky Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel logoLabel = new JLabel("Your Logo Here", SwingConstants.LEFT);
        JTextField searchField = new JTextField();
        headerPanel.add(logoLabel, BorderLayout.WEST);
        headerPanel.add(searchField, BorderLayout.EAST);
        frame.add(headerPanel, BorderLayout.NORTH);

        // Main content area
        JPanel mainPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        frame.add(mainPanel, BorderLayout.CENTER);

        // Add button to the footer
        JButton addButton = new JButton("Add Inventory");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddProductDialog(frame);
            }
        });
        frame.add(addButton, BorderLayout.SOUTH);

        // Populate main panel with product information
        refreshProductDisplay(mainPanel);

        // Search functionality
        searchField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = searchField.getText().trim();
                searchProducts(mainPanel, input);
            }
        });

        frame.setVisible(true);
    }

    private static void refreshProductDisplay(JPanel mainPanel) {
        mainPanel.removeAll();
        // Iterate over the products directly
        for (InventorySystem.ProductTracker product : inventorySystem.getProducts()) {
            addProductToPanel(mainPanel, product);
        }
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private static void addProductToPanel(JPanel mainPanel, InventorySystem.ProductTracker product) {
        JPanel productPanel = new JPanel(new BorderLayout());
        JLabel productImage = new JLabel("Image", SwingConstants.CENTER); // Placeholder for the image
        JLabel productNameLabel = new JLabel(product.getName(), SwingConstants.CENTER);
        JLabel productIDLabel = new JLabel("ID: " + product.getProductID(), SwingConstants.CENTER);
        JLabel stockLabel = new JLabel("Stock: " + product.getStockLevel(), SwingConstants.CENTER);
    
        JButton detailsButton = new JButton("Show Details");
        JPanel detailsPanel = new JPanel(); // Panel to hold details
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS)); // Layout for details
    
        detailsButton.addActionListener(new ActionListener() {
            boolean detailsVisible = false;
    
            @Override
            public void actionPerformed(ActionEvent e) {
                if (detailsVisible) {
                    productPanel.remove(detailsPanel); // Remove details
                    detailsButton.setText("Show Details"); // Change button text back
                    detailsVisible = false;
                } else {
                    String details = "Size: " + product.getSize() + 
                                     "\nColor: " + product.getColor() + 
                                     "\nLocation: " + product.getLocation();
                    JLabel detailsLabel = new JLabel(details, SwingConstants.CENTER);
                    detailsPanel.removeAll(); // Clear previous details
                    detailsPanel.add(detailsLabel);
                    productPanel.add(detailsPanel, BorderLayout.SOUTH); // Add details panel
                    detailsButton.setText("Hide Details"); // Change button text
                    detailsVisible = true;
                }
                productPanel.revalidate();
                productPanel.repaint();
            }
        });
    
        productPanel.add(productImage, BorderLayout.NORTH);
        productPanel.add(productNameLabel, BorderLayout.CENTER);
        productPanel.add(productIDLabel, BorderLayout.SOUTH);
        productPanel.add(stockLabel, BorderLayout.SOUTH);
        productPanel.add(detailsButton, BorderLayout.SOUTH);
        mainPanel.add(productPanel);
    }
    

    private static void searchProducts(JPanel mainPanel, String input) {
        mainPanel.removeAll(); // Clear the current display

        // Check if input is a valid ID first
        InventorySystem.ProductTracker productInfoByID = inventorySystem.getProductByID(input);
        
        if (productInfoByID != null) {
            // Display the product info if found by ID
            addProductToPanel(mainPanel, productInfoByID);
        } else {
            // If not found by ID, check by name
            InventorySystem.ProductTracker productInfoByName = inventorySystem.getProductByName(input);
            
            if (productInfoByName != null) {
                addProductToPanel(mainPanel, productInfoByName);
            } else {
                JOptionPane.showMessageDialog(mainPanel, "Product does not exist.", "Not Found", JOptionPane.ERROR_MESSAGE);
            }
        }

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    // Show dialog for adding a new product
    private static void showAddProductDialog(JFrame parentFrame) {
        JPanel addProductPanel = new JPanel(new GridLayout(6, 2));
        JTextField productIDField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField stockField = new JTextField();
        JTextField categoryField = new JTextField();
        JTextField sizeField = new JTextField();
        JTextField colorField = new JTextField();

        addProductPanel.add(new JLabel("Product ID:"));
        addProductPanel.add(productIDField);
        addProductPanel.add(new JLabel("Name:"));
        addProductPanel.add(nameField);
        addProductPanel.add(new JLabel("Stock Level:"));
        addProductPanel.add(stockField);
        addProductPanel.add(new JLabel("Category (Tops/Pants):"));
        addProductPanel.add(categoryField);
        addProductPanel.add(new JLabel("Size (XS, S, M, L, XL, XXL):"));
        addProductPanel.add(sizeField);
        addProductPanel.add(new JLabel("Color:"));
        addProductPanel.add(colorField);

        int result = JOptionPane.showConfirmDialog(parentFrame, addProductPanel, "Add Product", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String productID = productIDField.getText().trim();
            String name = nameField.getText().trim();
            int stockLevel;

            // Try to parse stock level and catch any exceptions
            try {
                stockLevel = Integer.parseInt(stockField.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(parentFrame, "Please enter a valid number for Stock Level.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String category = categoryField.getText().trim();
            String size = sizeField.getText().trim().toUpperCase(); // Convert to uppercase for consistency
            String color = colorField.getText().trim();

            // Validate Product ID
            if (!isValidProductID(productID)) {
                JOptionPane.showMessageDialog(parentFrame, "Invalid Product ID. Please enter a valid ID (PT01, PT02, PT03, PB01, PB02, PB03).", "Invalid Product ID", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validate Size
            if (!isValidSize(size)) {
                JOptionPane.showMessageDialog(parentFrame, "Invalid Size. Please enter a valid size (XS, S, M, L, XL, XXL).", "Invalid Size", JOptionPane.ERROR_MESSAGE);
                return;
            }

            inventorySystem.addProduct(productID, name, category, stockLevel, size, color);
            JOptionPane.showMessageDialog(parentFrame, "Product added successfully!");
            refreshProductDisplay((JPanel) parentFrame.getContentPane().getComponent(1)); // Refresh the product display
        }
    }

    // Method to validate the size input
    private static boolean isValidSize(String size) {
        return size.equals("XS") || size.equals("S") || size.equals("M") ||
               size.equals("L") || size.equals("XL") || size.equals("XXL");
    }

    // Method to validate the product ID
    private static boolean isValidProductID(String productID) {
        return productID.equals("PT01") || productID.equals("PT02") || productID.equals("PT03") ||
               productID.equals("PB01") || productID.equals("PB02") || productID.equals("PB03");
    }
}
