import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InventorySystem {
    // Inner class to represent each product's information
    public class ProductTracker {
        private String productID;
        private String name;
        private int stockLevel;
        private String size; 
        private String color; 
        private String category; // New field for product category
        private String location; // New field for product location

        public ProductTracker(String productID, String name, String category, int stockLevel, String size, String color) {
            this.productID = productID;
            this.name = name;
            this.stockLevel = stockLevel;
            this.size = size;
            this.color = color;
            this.category = category;
            this.location = determineLocation(category); // Set location based on category
        }

        // Determine the location based on category
        private String determineLocation(String category) {
            switch (category.toLowerCase()) {
                case "tops":
                    return "A"; // Specific logic for categorizing tops
                case "pants":
                    return "B"; // Specific logic for categorizing pants
                default:
                    return "Unknown Location"; // Fallback if category doesn't match
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
    private HashMap<String, List<ProductTracker>> products = new HashMap<>();

    // Method to add a product to the inventory
    public void addProduct(String productID, String name, String category, int stockLevel, String size, String color) {
        ProductTracker product = new ProductTracker(productID, name, category, stockLevel, size, color);
        products.computeIfAbsent(productID, k -> new ArrayList<>()).add(product);
    }

    // Retrieve a product by its ID
    public List<ProductTracker> getProductByID(String productID) {
        return products.get(productID);
    }

    // Retrieve a product by its name
    public ProductTracker getProductByName(String name) {
        for (List<ProductTracker> productList : products.values()) {
            for (ProductTracker product : productList) {
                if (product.getName().equalsIgnoreCase(name)) {
                    return product;
                }
            }
        }
        return null; // Not found
    }

    // List all products in the inventory
    public void listAllProducts() {
        if (products.isEmpty()) {
            System.out.println("No products in inventory.");
            return;
        }
        for (List<ProductTracker> productList : products.values()) {
            for (ProductTracker product : productList) {
                System.out.println(product);
            }
        }
    }
}
