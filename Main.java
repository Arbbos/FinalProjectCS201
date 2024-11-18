import javax.swing.*;
import java.awt.*;

public class Main {
    private static InventorySystem inventorySystem = new InventorySystem();
    private static StockReplenishment stockReplenishment;
    private static UserAuthentication userAuth = new UserAuthentication();

    public static void main(String[] args) {
        // Initialize inventory with sample products
        initializeInventory();

        // Set threshold for replenishment
        stockReplenishment = new StockReplenishment(10, inventorySystem);

        // Show login screen
        showLoginScreen();
    }

    private static void initializeInventory() {
        inventorySystem.addProduct("PT01", "T-Shirt", "Tops", 50, "M", "Red");
        inventorySystem.addProduct("PT02", "Polo", "Tops", 30, "L", "Red");
        inventorySystem.addProduct("PT03", "Blouse", "Tops", 20, "S", "White");
        inventorySystem.addProduct("PB01", "Shorts", "Pants", 25, "M", "Black");
    }

    private static void showLoginScreen() {
        JFrame loginFrame = new JFrame("User Authentication");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(400, 300);

        JPanel loginPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register Here");

        // Make login button bigger
        loginButton.setFont(new Font("Arial", Font.PLAIN, 18));
        loginButton.setPreferredSize(new Dimension(100, 50));

        // Make register button smaller and update text
        registerButton.setFont(new Font("Arial", Font.PLAIN, 12));
        registerButton.setPreferredSize(new Dimension(100, 30));

        // Add components to the panel
        loginPanel.add(new JLabel("Username:"));
        loginPanel.add(usernameField);
        loginPanel.add(new JLabel("Password:"));
        loginPanel.add(passwordField);
        loginPanel.add(loginButton);
        loginPanel.add(registerButton);

        loginFrame.add(loginPanel);
        loginFrame.setVisible(true);

        // Action listener for login button
        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            if (userAuth.loginUser(username, password)) {
                JOptionPane.showMessageDialog(loginFrame, "Login successful! Welcome, " + username, "Login Success", JOptionPane.INFORMATION_MESSAGE);
                loginFrame.dispose();
                startInventoryManagement(); // Proceed to the inventory management system
            } else {
                JOptionPane.showMessageDialog(loginFrame, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Action listener for register button
        registerButton.addActionListener(e -> {
            loginFrame.dispose();  // Close login screen
            showRegistrationScreen();  // Open registration screen
        });
    }

    private static void showRegistrationScreen() {
        JFrame registrationFrame = new JFrame("User Registration");
        registrationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        registrationFrame.setSize(400, 300);

        JPanel registrationPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JTextField confirmPasswordField = new JPasswordField();
        JButton registerButton = new JButton("Register");

        // Add components to the panel
        registrationPanel.add(new JLabel("Username:"));
        registrationPanel.add(usernameField);
        registrationPanel.add(new JLabel("Password:"));
        registrationPanel.add(passwordField);
        registrationPanel.add(new JLabel("Confirm Password:"));
        registrationPanel.add(confirmPasswordField);

        registrationPanel.add(registerButton);
        registrationFrame.add(registrationPanel);
        registrationFrame.setVisible(true);

        // Action listener for register button
        registerButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(((JPasswordField) confirmPasswordField).getPassword());

            if (password.equals(confirmPassword)) {
                if (userAuth.registerUser(username, password)) {
                    JOptionPane.showMessageDialog(registrationFrame, "Registration successful! You can now log in.", "Registration Success", JOptionPane.INFORMATION_MESSAGE);
                    registrationFrame.dispose();
                    showLoginScreen(); // Return to login screen
                } else {
                    JOptionPane.showMessageDialog(registrationFrame, "Registration failed. Username already exists.", "Registration Failed", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(registrationFrame, "Passwords do not match. Please try again.", "Password Mismatch", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private static void startInventoryManagement() {
        JFrame frame = new JFrame("Inventory Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        // Header and main panel setup
        setupHeader(frame);
        JPanel mainPanel = setupMainPanel(frame);

        // Add buttons for Order and Replenishment in the footer
        JPanel footerPanel = new JPanel();
        JButton orderButton = new JButton("Process Order");
        JButton replenishButton = new JButton("Replenish Stock");
        JButton logoutButton = new JButton("Logout");

        orderButton.addActionListener(e -> processOrderDialog(frame));
        replenishButton.addActionListener(e -> {
            stockReplenishment.checkAndReplenishStock();
            refreshProductDisplay(mainPanel); // Refresh to show updated stock levels
        });
        logoutButton.addActionListener(e -> {
            userAuth.logoutUser();
            frame.dispose();
            showLoginScreen(); // Return to login screen
        });

        footerPanel.add(orderButton);
        footerPanel.add(replenishButton);
        footerPanel.add(logoutButton);
        frame.add(footerPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private static void setupHeader(JFrame frame) {
        JPanel headerPanel = new JPanel(new BorderLayout());
        JTextField searchField = new JTextField();
        headerPanel.add(new JLabel("Inventory Management"), BorderLayout.WEST);
        headerPanel.add(searchField, BorderLayout.EAST);

        // Search functionality
        searchField.addActionListener(e -> {
            String input = searchField.getText().trim();
            JPanel mainPanel = (JPanel) frame.getContentPane().getComponent(1);
            searchProducts(mainPanel, input);
        });

        frame.add(headerPanel, BorderLayout.NORTH);
    }

    private static JPanel setupMainPanel(JFrame frame) {
        JPanel mainPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        frame.add(mainPanel, BorderLayout.CENTER);
        refreshProductDisplay(mainPanel);
        return mainPanel;
    }

    private static void refreshProductDisplay(JPanel mainPanel) {
        mainPanel.removeAll();
        for (InventorySystem.ProductTracker product : inventorySystem.getProducts()) {
            JPanel productPanel = new JPanel(new BorderLayout());
            JLabel nameLabel = new JLabel(product.getName() + " - Stock: " + product.getStockLevel());
            JLabel idLabel = new JLabel("ID: " + product.getProductID(), SwingConstants.CENTER);
            JButton detailsButton = new JButton("Details");

            detailsButton.addActionListener(e -> {
                String details = "Product ID: " + product.getProductID() +
                                 "\nName: " + product.getName() +
                                 "\nCategory: " + product.getCategory() +
                                 "\nStock: " + product.getStockLevel() +
                                 "\nSize: " + product.getSize() +
                                 "\nColor: " + product.getColor();
                JOptionPane.showMessageDialog(mainPanel, details, "Product Details", JOptionPane.INFORMATION_MESSAGE);
            });

            productPanel.add(nameLabel, BorderLayout.NORTH);
            productPanel.add(idLabel, BorderLayout.CENTER);
            productPanel.add(detailsButton, BorderLayout.SOUTH);
            mainPanel.add(productPanel);
        }
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private static void searchProducts(JPanel mainPanel, String input) {
        mainPanel.removeAll();
        InventorySystem.ProductTracker productInfoByID = inventorySystem.getProductByID(input);
        InventorySystem.ProductTracker productInfoByName = inventorySystem.getProductByName(input);

        if (productInfoByID != null) {
            addProductToPanel(mainPanel, productInfoByID);
        } else if (productInfoByName != null) {
            addProductToPanel(mainPanel, productInfoByName);
        } else {
            JOptionPane.showMessageDialog(mainPanel, "Product not found.", "Not Found", JOptionPane.ERROR_MESSAGE);
        }

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private static void addProductToPanel(JPanel mainPanel, InventorySystem.ProductTracker product) {
        JPanel productPanel = new JPanel(new BorderLayout());
        JLabel nameLabel = new JLabel(product.getName() + " - Stock: " + product.getStockLevel());
        JLabel idLabel = new JLabel("ID: " + product.getProductID(), SwingConstants.CENTER);
        JButton detailsButton = new JButton("Details");

        detailsButton.addActionListener(e -> {
            String details = "Product ID: " + product.getProductID() +
                             "\nName: " + product.getName() +
                             "\nCategory: " + product.getCategory() +
                             "\nStock: " + product.getStockLevel() +
                             "\nSize: " + product.getSize() +
                             "\nColor: " + product.getColor();
            JOptionPane.showMessageDialog(mainPanel, details, "Product Details", JOptionPane.INFORMATION_MESSAGE);
        });

        productPanel.add(nameLabel, BorderLayout.NORTH);
        productPanel.add(idLabel, BorderLayout.CENTER);
        productPanel.add(detailsButton, BorderLayout.SOUTH);
        mainPanel.add(productPanel);
    }

    private static void processOrderDialog(JFrame parentFrame) {
        JPanel orderPanel = new JPanel(new GridLayout(3, 2));
        JTextField orderIDField = new JTextField();
        JTextField productIDField = new JTextField();
        JTextField quantityField = new JTextField();

        orderPanel.add(new JLabel("Order ID:"));
        orderPanel.add(orderIDField);
        orderPanel.add(new JLabel("Product ID:"));
        orderPanel.add(productIDField);
        orderPanel.add(new JLabel("Quantity:"));
        orderPanel.add(quantityField);

        int result = JOptionPane.showConfirmDialog(parentFrame, orderPanel, "Process Order", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String orderID = orderIDField.getText().trim();
            String productID = productIDField.getText().trim();
            int quantity;

            try {
                quantity = Integer.parseInt(quantityField.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(parentFrame, "Enter a valid quantity.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return;
            }

            OrderFulfillment order = new OrderFulfillment(orderID, productID, quantity, inventorySystem);
            order.processOrder();
            refreshProductDisplay((JPanel) parentFrame.getContentPane().getComponent(1)); // Refresh main panel
        }
    }
}
