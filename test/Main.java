package test;

import javax.swing.*;

import java.util.ArrayList;
import java.util.List;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {
    private static InventorySystem inventorySystem = new InventorySystem();
    private static StockReplenishment stockReplenishment;
    private static UserAuthentication userAuth = new UserAuthentication();
    static StockMovement stockmovement= new StockMovement(null, null, 0, null, null);
    
    private static final String CONNECTION = "jdbc:mysql://localhost:3306/test";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static void main(String[] args) {
        // Initialize inventory with sample products
        initializeInventory();

        // Set threshold for replenishment
        stockReplenishment = new StockReplenishment(10, inventorySystem);
        // show splash screen
        SplashScreen.showSplashScreen();
        // Show login screen       
    }

    private static void initializeInventory() {
        inventorySystem.addProduct("PT01", "T-Shirt", "Tops", 50, "M", "Red", "A");
        inventorySystem.addProduct("PT02", "Polo", "Tops", 30, "L", "Red", "A");
        inventorySystem.addProduct("PT03", "Blouse", "Tops", 20, "S", "White", "B");
        inventorySystem.addProduct("PB01", "Shorts", "Pants", 25, "M", "Black", "B");
    }

    public static void showLoginScreen() {
        // Create the login frame
        JFrame loginFrame = new JFrame("User Authentication");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(400, 300);
        loginFrame.setLocationRelativeTo(null); // Center the login screen
    
        // Set a custom icon for the JFrame title
        ImageIcon logoIcon = new ImageIcon("lib/images/logo_dsa2.png"); // Replace with your logo's path
        loginFrame.setIconImage(logoIcon.getImage()); 
    
        // Create the panel with GridBagLayout
        JPanel loginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 5, 10, 5); // Add padding between components
    
        // Create components
        JLabel headerLabel = new JLabel("Log-In");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField(20);
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(20);
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register Here");
    
        // Resize buttons
        loginButton.setFont(new Font("Arial", Font.PLAIN, 16));
        loginButton.setPreferredSize(new Dimension(120, 40));
        loginButton.setBackground(new Color(0x233c4b)); // Set button color
        loginButton.setForeground(Color.WHITE); // Set text color
    
        registerButton.setFont(new Font("Arial", Font.PLAIN, 12));
        registerButton.setPreferredSize(new Dimension(120, 30));
        registerButton.setBackground(new Color(0x233c4b)); // Set button color
        registerButton.setForeground(Color.WHITE); // Set text color
    
        // Add components to the panel with constraints
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Make header span across both columns
        loginPanel.add(headerLabel, gbc);
    
        // Username label and input field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1; // Reset gridwidth
        loginPanel.add(usernameLabel, gbc);
    
        gbc.gridx = 1;
        loginPanel.add(usernameField, gbc);
    
        // Password label and input field
        gbc.gridx = 0;
        gbc.gridy = 2;
        loginPanel.add(passwordLabel, gbc);
    
        gbc.gridx = 1;
        loginPanel.add(passwordField, gbc);
    
        // Login button
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2; // Span across both columns for centering
        gbc.anchor = GridBagConstraints.CENTER;
        loginPanel.add(loginButton, gbc);
    
        // Register button
        gbc.gridy = 4;
        loginPanel.add(registerButton, gbc);
    
        // Add the panel to the frame
        loginFrame.add(loginPanel);
        loginFrame.setVisible(true);
    
        // Action listener for login button
        loginButton.addActionListener(e -> {
        	
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (LogIn(username, password)) {
                JOptionPane.showMessageDialog(null, "Login Successful!");
                loginFrame.dispose();
              startInventoryManagement();
               
               
            } else {
                JOptionPane.showMessageDialog(null, "Invalid credentials. Try again.");
            }
//            String username = usernameField.getText().trim();
//            String password = new String(passwordField.getPassword());
//            if (userAuth.loginUser(username, password)) {
//                JOptionPane.showMessageDialog(loginFrame, "Login successful! Welcome, " + username, "Login Success", JOptionPane.INFORMATION_MESSAGE);
//                loginFrame.dispose();
//                startInventoryManagement(); // Proceed to the inventory management system
//            } else {
//                JOptionPane.showMessageDialog(loginFrame, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
//            }
        });
    
        // Action listener for register button
        registerButton.addActionListener(e -> {
            loginFrame.dispose(); // Close login screen
            showRegistrationScreen(); // Open registration screen
        });
    }
    
    
    

    private static void showRegistrationScreen() {
        JFrame registrationFrame = new JFrame("User Registration");
        registrationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        registrationFrame.setSize(400, 300);

        registrationFrame.setLocationRelativeTo(null); // Center the login screen
    
        // Set a custom icon for the JFrame title
        ImageIcon logoIcon = new ImageIcon("lib/images/logo_dsa2.png"); // Replace with your logo's path
        registrationFrame.setIconImage(logoIcon.getImage()); 
    
        // Set the layout for the registration panel
        JPanel registrationPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Set padding between components
    
        // Create components
        JLabel headerLabel = new JLabel("Register");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        JTextField usernameField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);
        JPasswordField confirmPasswordField = new JPasswordField(20);
        JButton registerButton = new JButton("Register");

        registerButton.setFont(new Font("Arial", Font.PLAIN, 12));
        registerButton.setPreferredSize(new Dimension(120, 30));
        registerButton.setBackground(new Color(0x233c4b)); // Set button color
        registerButton.setForeground(Color.WHITE); // Set text color
    
        // Add the header label with centered alignment
        gbc.gridwidth = 2; // Span across 2 columns
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER; // Center the header
        registrationPanel.add(headerLabel, gbc);
    
        // Add username label and field
        gbc.gridwidth = 1; // Reset gridwidth to 1
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        registrationPanel.add(new JLabel("Username:"), gbc);
        
        gbc.gridx = 1;
        registrationPanel.add(usernameField, gbc);
    
        // Add password label and field
        gbc.gridx = 0;
        gbc.gridy = 2;
        registrationPanel.add(new JLabel("Password:"), gbc);
        
        gbc.gridx = 1;
        registrationPanel.add(passwordField, gbc);
    
        // Add confirm password label and field
        gbc.gridx = 0;
        gbc.gridy = 3;
        registrationPanel.add(new JLabel("Confirm Password:"), gbc);
        
        gbc.gridx = 1;
        registrationPanel.add(confirmPasswordField, gbc);
    
        // Add register button
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 2; // Span the button across 2 columns
        gbc.anchor = GridBagConstraints.CENTER;
        registrationPanel.add(registerButton, gbc);
    
        // Add the panel to the frame
        registrationFrame.add(registrationPanel);
        registrationFrame.setVisible(true);
    
        // Action listener for register button
        registerButton.addActionListener(e -> {

        	String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill in all fields.");
                return;
            }

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(null, "Passwords do not match.");
                return;
            }

    
            if (registerUser(username, password)) {
                JOptionPane.showMessageDialog(null, "Registration Successful!");
                registrationFrame.dispose(); 
                showLoginScreen();  
            } else {
                JOptionPane.showMessageDialog(null, "Error during registration. Please try again.");
            }
        	
        	//            String username = usernameField.getText().trim();
//            String password = new String(passwordField.getPassword());
//            String confirmPassword = new String(((JPasswordField) confirmPasswordField).getPassword());
//    
//            if (password.equals(confirmPassword)) {
//                if (userAuth.registerUser(username, password)) {
//                    JOptionPane.showMessageDialog(registrationFrame, "Registration successful! You can now log in.", "Registration Success", JOptionPane.INFORMATION_MESSAGE);
//                    registrationFrame.dispose();
//                    showLoginScreen(); // Return to login screen
//                } else {
//                    JOptionPane.showMessageDialog(registrationFrame, "Registration failed. Username already exists.", "Registration Failed", JOptionPane.ERROR_MESSAGE);
//                }
//            } else {
//                JOptionPane.showMessageDialog(registrationFrame, "Passwords do not match. Please try again.", "Password Mismatch", JOptionPane.ERROR_MESSAGE);
//            }
        });
    }
    

    private static void startInventoryManagement() {
        JFrame frame = new JFrame("Inventory Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(100, 100, 1200, 720);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        // Create a top container for the header and navigation bar
        JPanel topPanel = new JPanel(new BorderLayout());

        // Setup the header
        setupHeader(topPanel);

        // Create the navigation bar (footer buttons as a navbar)
        JPanel navBar = createNavBar(frame);

        // Add the header and navigation bar to the top panel
        topPanel.add(navBar, BorderLayout.SOUTH);

        // Add the top panel to the frame
        frame.add(topPanel, BorderLayout.NORTH);

        // Setup the main panel for content
        JPanel mainPanel = setupMainPanel(frame);
        frame.add(mainPanel, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private static void setupHeader(JPanel topPanel) {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Add padding
        headerPanel.setBackground(new Color(0x233c4b)); // Background color
        headerPanel.setForeground(Color.WHITE); // Text color

        JLabel titleLabel = new JLabel("Inventory Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Font size
        titleLabel.setForeground(Color.WHITE);

        JTextField searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(200, 30));
        searchField.setText("Search");
        searchField.setForeground(Color.GRAY);

        searchField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (searchField.getText().equals("Search")) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Search");
                    searchField.setForeground(Color.GRAY);
                }
            }
        });

        searchField.addActionListener(e -> {
            String input = searchField.getText().trim();
            JPanel mainPanel = (JPanel) topPanel.getParent().getComponent(1); // Get the main panel
            searchProducts(mainPanel, input);
        });

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(searchField, BorderLayout.EAST);

        topPanel.add(headerPanel, BorderLayout.NORTH);
    }

    private static JPanel createNavBar(JFrame frame) {
        JPanel navBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10)); // Horizontal layout
        navBar.setBackground(new Color(0x233c4b)); // Background color
        navBar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding

        // Create navigation buttons
        JButton orderButton = createNavButton("Process Order");
        JButton replenishButton = createNavButton("Replenish Stock");
        JButton addProductButton = createNavButton("Add New Product");
        JButton viewStockMovementsButton = createNavButton("View Stock Movements");
        JButton reportButton = createNavButton("Generate Inventory Report");
        JButton logoutButton = createNavButton("Logout");

        // Add action listeners
        orderButton.addActionListener(e -> processOrderDialog(frame));
        replenishButton.addActionListener(e -> {
            stockReplenishment.checkAndReplenishStock();
            refreshProductDisplay(setupMainPanel(frame)); // Refresh stock
        });
        addProductButton.addActionListener(e -> openAddProductDialog(frame));
        viewStockMovementsButton.addActionListener(e -> stockmovement.viewStockMovementsFromDatabase());
        reportButton.addActionListener(e -> InventoryReportGenerator.generateInventoryReport(inventorySystem));
        logoutButton.addActionListener(e -> {
            userAuth.logoutUser();
            frame.dispose();
            showLoginScreen();
        });

        // Add buttons to the navbar
        navBar.add(orderButton);
        navBar.add(replenishButton);
        navBar.add(addProductButton);
        navBar.add(viewStockMovementsButton);
        navBar.add(reportButton);
        navBar.add(logoutButton);

        return navBar;
    }

    private static JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(180, 40)); // Consistent button size
        button.setBackground(new Color(0x456a7f)); // Background color
        button.setForeground(Color.WHITE); // Text color
        button.setFont(new Font("Arial", Font.BOLD, 14)); // Font style
        button.setFocusPainted(false); // No focus border
        return button;
    }

    // Method to open the dialog for adding a new product
    private static void openAddProductDialog(JFrame frame) {
        JDialog dialog = new JDialog(frame, "Add New Product", true);
        dialog.setSize(400, 400);
        dialog.setLayout(new GridLayout(8, 2));
        dialog.setLocationRelativeTo(frame);

        // Create form fields for product details
        JLabel idLabel = new JLabel("Product ID:");
        idLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JTextField idField = new JTextField();

        JLabel nameLabel = new JLabel("Product Name:");
        nameLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JTextField nameField = new JTextField();

        JLabel categoryLabel = new JLabel("Category:");
        categoryLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JTextField categoryField = new JTextField();

        JLabel stockLabel = new JLabel("Stock Level:");
        stockLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JTextField stockField = new JTextField();

        JLabel sizeLabel = new JLabel("Size:");
        sizeLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JTextField sizeField = new JTextField();

        JLabel colorLabel = new JLabel("Color:");
        colorLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JTextField colorField = new JTextField();

        JLabel location = new JLabel("Location:");
        location.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JTextField locationField = new JTextField();

        // Button to save product
        JButton saveButton = new JButton("Save Product");
        saveButton.setBackground(new Color(0x233c4b));
        saveButton.setForeground(Color.WHITE);
        saveButton.addActionListener(e -> {
            String productID = idField.getText();
            String name = nameField.getText();
            String category = categoryField.getText();
            String stockText = stockField.getText();
            String size = sizeField.getText();
            String color = colorField.getText();
            String location1 = locationField.getText();

            // Validate inputs
            if (productID.isEmpty() || name.isEmpty() || category.isEmpty() || stockText.isEmpty() || size.isEmpty() || color.isEmpty() || location1.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    int stockLevel = Integer.parseInt(stockText);
                    inventorySystem.addProduct(productID, name, category, stockLevel, size, color, location1);

                    // Refresh the product display (assuming you have a method for this)
                    refreshProductDisplay(setupMainPanel(frame));

                    dialog.dispose();  // Close the dialog
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Invalid stock level.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Add components to the dialog
        dialog.add(idLabel);
        dialog.add(idField);
        dialog.add(nameLabel);
        dialog.add(nameField);
        dialog.add(categoryLabel);
        dialog.add(categoryField);
        dialog.add(stockLabel);
        dialog.add(stockField);
        dialog.add(sizeLabel);
        dialog.add(sizeField);
        dialog.add(colorLabel);
        dialog.add(colorField);
        dialog.add(location);
        dialog.add(locationField);

        // Create a panel for the button
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.add(saveButton, BorderLayout.CENTER);
        dialog.add(buttonPanel);

        dialog.setVisible(true);
    }

    


    private static JPanel setupMainPanel(JFrame frame) {
        JPanel mainPanel = new JPanel(new BorderLayout());  // Use BorderLayout to contain the JTable
        refreshProductDisplay(mainPanel);  // Refresh the table with products
        frame.add(mainPanel, BorderLayout.CENTER);  // Add the mainPanel to the JFrame
        return mainPanel;
    }


    private static void refreshProductDisplay(JPanel mainPanel) {
        // Create column names
        String[] columnNames = {"Product ID", "Name", "Category", "Stock Level", "Size", "Color", "Location"};

        // Create data for the table
        Object[][] data = new Object[inventorySystem.getProducts().size()][77];
        int row = 0;
        for (InventorySystem.ProductTracker product : inventorySystem.getProducts()) {
            data[row][0] = product.getProductID();
            data[row][1] = product.getName();
            data[row][2] = product.getCategory();
            data[row][3] = product.getStockLevel();
            data[row][4] = product.getSize();
            data[row][5] = product.getColor();
            data[row][6] = product.getLocation();
            row++;
        }

        // Create table with data and column names
        JTable productTable = new JTable(data, columnNames);
        
        // Make table columns automatically resize to fit content
        productTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // Set a default table cell editor for easier interaction
        productTable.setDefaultEditor(Object.class, null);

        // Add table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(productTable);
        mainPanel.removeAll();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private static void searchProducts(JPanel mainPanel, String input) {
        // Create column names
        String[] columnNames = {"Product ID", "Name", "Category", "Stock Level", "Size", "Color", "Location"};
        
        // Create a list of matched products
        List<Object[]> matchedProducts = new ArrayList<>();

        // Search by ID or Name
        InventorySystem.ProductTracker productInfoByID = inventorySystem.getProductByID(input);
        InventorySystem.ProductTracker productInfoByName = inventorySystem.getProductByName(input);

        if (productInfoByID != null) {
            matchedProducts.add(new Object[]{
                productInfoByID.getProductID(),
                productInfoByID.getName(),
                productInfoByID.getCategory(),
                productInfoByID.getStockLevel(),
                productInfoByID.getSize(),
                productInfoByID.getColor(),
                productInfoByID.getLocation()
            });
        } else if (productInfoByName != null) {
            matchedProducts.add(new Object[]{
                productInfoByName.getProductID(),
                productInfoByName.getName(),
                productInfoByName.getCategory(),
                productInfoByName.getStockLevel(),
                productInfoByName.getSize(),
                productInfoByName.getColor(),
                productInfoByName.getLocation()
            });
        } else {
            JOptionPane.showMessageDialog(mainPanel, "Product not found.", "Not Found", JOptionPane.ERROR_MESSAGE);
        }

        // Convert the list to a 2D array for the JTable
        Object[][] data = new Object[matchedProducts.size()][7];
        matchedProducts.toArray(data);

        // Create table with matched data
        JTable productTable = new JTable(data, columnNames);
        productTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        productTable.setDefaultEditor(Object.class, null); // Disable editing

        // Add table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(productTable);
        mainPanel.removeAll();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }


    private static void addProductToPanel(JPanel mainPanel, InventorySystem.ProductTracker product) {
        JPanel productPanel = new JPanel(new BorderLayout());
        
        // Map product IDs to image file names (or set up logic to load specific images)
        String imagePath = getProductImagePath(product.getProductID()); // Get image path based on product ID
        ImageIcon productImage = new ImageIcon(imagePath); // Load the image
        
        // Check if the image path is valid and the image is loaded
        if (productImage.getImageLoadStatus() != MediaTracker.COMPLETE) {
            System.err.println("Image failed to load: " + imagePath);
        }
        
        // Resize the image if needed (e.g., 100x100 px)
        ImageIcon resizedImage = new ImageIcon(productImage.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));
        JLabel imageLabel = new JLabel(resizedImage); // Set the resized image to the label
        
        // Product details
        JLabel nameLabel = new JLabel(product.getName() + " - Stock: " + product.getStockLevel());
        JLabel idLabel = new JLabel("ID: " + product.getProductID(), SwingConstants.CENTER);
        JButton detailsButton = new JButton("Details");
        
        // Action for the details button
        detailsButton.addActionListener(e -> {
            String details = "Product ID: " + product.getProductID() +
                             "\nName: " + product.getName() +
                             "\nCategory: " + product.getCategory() +
                             "\nStock: " + product.getStockLevel() +
                             "\nSize: " + product.getSize() +
                             "\nColor: " + product.getColor() +
            "\nColor: " + product.getLocation();
            JOptionPane.showMessageDialog(mainPanel, details, "Product Details", JOptionPane.INFORMATION_MESSAGE);
        });
        
        // Add the image, name, and other details to the product panel
        productPanel.add(imageLabel, BorderLayout.NORTH);  // Display the image at the top
        productPanel.add(nameLabel, BorderLayout.CENTER);
        productPanel.add(idLabel, BorderLayout.SOUTH);
        productPanel.add(detailsButton, BorderLayout.SOUTH);
        
        // Add the product panel to the main panel
        mainPanel.add(productPanel);
    }
    
    private static String getProductImagePath(String productID) {
        // This function returns a specific image path based on the product ID
        switch (productID) {
            case "PT01":
                return "lib/images/PT01.png";  // Path to image for PT01
            case "PT02":
                return "lib/images/PT02.png";  // Path to image for PT02
            case "PT03":
                return "lib/images/PT03.png";  // Path to image for PT03
            // Add more cases for other products...
            default:
                return "lib/images/default_image.png"; // Default image if no specific image is found
        }
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
    
    private static boolean LogIn(String username, String password) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(CONNECTION, USER, PASSWORD);
            String sql = "SELECT * FROM authentication WHERE username = ? AND password = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return true; 
            } else {
                return false; 
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    

	 private static boolean registerUser(String username, String password) {
	        Connection connection = null;
	        PreparedStatement preparedStatement = null;

	        try {
	            // Establish database connection
	            connection = DriverManager.getConnection(CONNECTION, USER, PASSWORD);
	            String sql = "INSERT INTO authentication (username, password) VALUES (?, ?)";
	            preparedStatement = connection.prepareStatement(sql);
	            preparedStatement.setString(1, username);
	            preparedStatement.setString(2, password);  // Store password as plain text

	            int rowsAffected = preparedStatement.executeUpdate();

	            return rowsAffected > 0;  // Return true if a row was inserted, false otherwise

	        } catch (SQLException e) {
	            e.printStackTrace();
	            return false;  // Return false if there was a SQL error
	        } finally {
	            try {
	                if (preparedStatement != null) preparedStatement.close();
	                if (connection != null) connection.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	    }

}
