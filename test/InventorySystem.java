package test;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Queue;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Collection;

public class InventorySystem {
    // Inner class to represent each product's information
    public class ProductTracker {
        private String productID;
        private String name;
        private int stockLevel;
        private String size;
        private String color;
        private String category;
        private String location;

        public ProductTracker(String productID, String name, String category, int stockLevel, String size, String color) {
            this.productID = productID;
            this.name = name;
            this.stockLevel = stockLevel;
            this.size = size;
            this.color = color;
            this.category = category;
            this.location = determineLocation(category);
        }

        private String determineLocation(String category) {
            switch (category.toLowerCase()) {
                case "tops":
                    return "A";
                case "pants":
                    return "B";
                default:
                    return "Unknown Location";
            }
        }

        // Getters
        public String getProductID() {
            return productID;
        }

        public String getName() {
            return name;
        }

        public int getStockLevel() {
            return stockLevel;
        }

        public String getSize() {
            return size;
        }

        public String getColor() {
            return color;
        }

        public String getCategory() {
            return category;
        }

        public String getLocation() {
            return location;
        }

        // Setter for stock level
        public void setStockLevel(int stockLevel) {
            this.stockLevel = stockLevel;
        }

        @Override
        public String toString() {
            return "\nProduct ID: " + productID + 
                   "\nName: " + name + 
                   "\nCategory: " + category +
                   "\nStock Level: " + stockLevel + 
                   "\nSize: " + size + 
                   "\nColor: " + color +  
                   "\nLocation: " + location;
        }
    }

    // HashMap to store products using productID as the key
    private HashMap<String, ProductTracker> products = new HashMap<>();

    // Method to add a product to the inventory
    public void addProduct(String productID, String name, String category, int stockLevel, String size, String color) {
        if (products.containsKey(productID)) {
            System.out.println("Product with ID " + productID + " already exists.");
            return;
        }

        ProductTracker product = new ProductTracker(productID, name, category, stockLevel, size, color);
        products.put(productID, product);
    }

    // Retrieve a product by its ID (case insensitive and partial match)
public List<ProductTracker> getProductByID(String productID) {
    List<ProductTracker> matchingProducts = new ArrayList<>();
    for (ProductTracker product : products.values()) {
        if (product.getProductID().toLowerCase().contains(productID.toLowerCase())) {
            matchingProducts.add(product);
        }
    }
    return matchingProducts;
}

// Retrieve a product by its name (case insensitive and partial match)
public List<ProductTracker> getProductByName(String name) {
    List<ProductTracker> matchingProducts = new ArrayList<>();
    for (ProductTracker product : products.values()) {
        if (product.getName().toLowerCase().contains(name.toLowerCase())) {
            matchingProducts.add(product);
        }
    }
    return matchingProducts;
}

    // Get all products
    public Collection<ProductTracker> getProducts() {
        return products.values();
    }
       
}
